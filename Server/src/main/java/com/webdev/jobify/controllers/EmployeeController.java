package com.webdev.jobify.controllers;

import com.webdev.jobify.assemblers.*;
import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.*;
import com.webdev.jobify._aux.Picture;
import com.webdev.jobify.services.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeAssembler;
    private final JobModelAssembler jobAssembler;
    private final JobService jobService;
    private final ConnectionService connectionService;
    private final ConnectionModelAssembler connectionAssembler;
    public final CertificateService certificateService;
    public final CertificateModelAssembler certificateAssembler;
    private final MessageService messageService;
    private final MessageModelAssembler messageAssembler;
    private final JobAdService jobAdService;
    private final JobAdModelAssembler jobAdAssembler;
    private final PostService postService;


    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler assembler,
                              JobModelAssembler jobAssembler, JobService jobService,
                              ConnectionService connectionService, ConnectionModelAssembler connectionAssembler,
                              CertificateService certificateService, CertificateModelAssembler certificateAssembler,
                              MessageService messageService, MessageModelAssembler messageAssembler,
                              JobAdService jobAdService, JobAdModelAssembler jobAdAssembler, PostService postService) {
        this.employeeService = employeeService;
        this.employeeAssembler = assembler;
        this.jobAssembler = jobAssembler;
        this.jobService = jobService;
        this.connectionService = connectionService;
        this.connectionAssembler = connectionAssembler;
        this.certificateService = certificateService;
        this.certificateAssembler = certificateAssembler;
        this.messageService = messageService;
        this.messageAssembler = messageAssembler;
        this.jobAdService = jobAdService;
        this.jobAdAssembler = jobAdAssembler;
        this.postService = postService;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Employee>> getAllEmployees(){
        List<EntityModel<Employee>> employees = employeeService.findAllEmployees().stream().map(employeeAssembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }


    @GetMapping("/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.findEmployeeById(id);
        return employeeAssembler.toModel(employee);
    }


    @GetMapping("/{id}/picture")
    public Picture getEmployeePicture(@PathVariable("id") Long id) throws IOException {

        Employee employee = employeeService.findEmployeeById(id);
        String pictureName = employee.getPhoto();
        if(pictureName == null || pictureName.isEmpty()){
            return null;
        }
        Path pictLocation = Paths.get(pictureName);


        byte[] data = Files.readAllBytes(pictLocation);

        String pictExtension = pictureName.substring(pictureName.lastIndexOf(".") + 1);

        return new Picture(pictExtension, data);
    }

    @GetMapping("/{id}/jobs")
    public CollectionModel<EntityModel<Job>> getEmployeeJobs(@PathVariable("id") Long id) {
        List<EntityModel<Job>> jobs = jobService.findJobsByEmployeeId(id).stream().map(jobAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).getAllJobs()).withSelfRel());
    }

    @GetMapping("/{id}/connections")
    public CollectionModel<EntityModel<Employee>> getEmployeeConnections(@PathVariable("id") Long id) {

        List<Connection> connections = connectionService.findConnectionsOfEmployee(id);

        List<Employee> connectedEmployees = new LinkedList<>();

        for (Connection c : connections) {
            if(Objects.equals(c.getReceiver().getId(), id)) {
                connectedEmployees.add(c.getRequester());
            }
            else {
                connectedEmployees.add(c.getReceiver());
            }
        }

        List<EntityModel<Employee>> result = connectedEmployees.stream().map(employeeAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(result, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @GetMapping("/{id}/connections/pending")
    public CollectionModel<EntityModel<Employee>> getEmployeePendingConnections(@PathVariable("id") Long id) {

        List<Connection> pendingConnections = connectionService.findPendingConnectionsOfEmployee(id);

        List<Employee> pendingEmployees = new LinkedList<>();

        for(Connection c : pendingConnections) {
            pendingEmployees.add(c.getRequester());
        }

        List<EntityModel<Employee>> result = pendingEmployees.stream().map(employeeAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(result, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @GetMapping("/{id}/certificates")
    public CollectionModel<EntityModel<Certificate>> getEmployeeCertificates(@PathVariable("id") Long id) {

        List<EntityModel<Certificate>> certificates = certificateService.findCertificatesByEmployeeId(id).stream().map(certificateAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(certificates, linkTo(methodOn(CertificateController.class).getAllCertificates()).withSelfRel());
    }

    @GetMapping("/{id}/skills")
    public List<String> getEmployeeSkills(@PathVariable("id") Long id) {
        Employee employee = employeeService.findEmployeeById(id);
        return employee.getSkills();
    }

    @PostMapping("/addskill")
    public ResponseEntity<?>addSkillToEmployee(@RequestParam("id") Long id, @RequestParam("skill")String skill) {

        try{
            Employee employee = employeeService.findEmployeeById(id);
            LinkedList<String> employeeSkills = new LinkedList<>(employee.getSkills());

            employeeSkills.push(skill);

            employee.setSkills(employeeSkills);

            employeeService.updateEmployee(employee);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill could not be added!");
        }

        return ResponseEntity.status(HttpStatus.OK).body("SKill added!");
    }


    @GetMapping("/{ids}/messages")
    public CollectionModel<EntityModel<Message>> getEmployee1MessagesWithEmployee2(@PathVariable Long[] ids) {

        Long id1 = ids[0];
        Long id2 = ids[1];

        List<EntityModel<Message>> messages = messageService.findMessagesOfEmployee1WithEmployee2(id1, id2).stream().map(messageAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).getAllMessages()).withSelfRel());
    }

//    @GetMapping("/{ids}/posts")




    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@RequestParam("email") String email, @RequestParam("password") String password) {

        EntityModel<Employee> entityModel;

        try {
            entityModel = employeeAssembler.toModel(employeeService.findEmployeeByEmail(email));
        }
        catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email was not found!");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isCorrectPassword = passwordEncoder.matches(password, entityModel.getContent().getPassword());

        if(isCorrectPassword){
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password!");
    }


    @GetMapping("/find/{email}")
    public EntityModel<Employee> getEmployeeByEmail(@PathVariable("email") String email){
        Employee employee = employeeService.findEmployeeByEmail(email);
        return employeeAssembler.toModel(employee);
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addEmployee(@RequestPart("employee") Employee employee, @RequestPart(value = "picture", required = false) MultipartFile picture) throws IOException {

        employee.updateProfilePicture(picture);

        EntityModel<Employee> entityModel;

        try {
            entityModel = employeeAssembler.toModel(employeeService.addEmployee(employee));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with email: " + employee.getEmail() + " already exists!");
        }

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }


    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee(@RequestParam("id") Long id, @RequestParam(value = "email", required = false) String newEmail,
                                            @RequestParam(value = "password", required = false) String newPassword){

        Optional<Employee> updatedEmployee;
        try {
            updatedEmployee = employeeService.employeeRepo.findEmployeeById(id);
        }
        catch ( Exception e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " was not found!");
        }

        if(newEmail != null && !newEmail.equals("")) {
            if(employeeService.exists(newEmail)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email: " + newEmail + " already exists!");
            }
            updatedEmployee.get().setEmail(newEmail);
            employeeService.updateEmployee(updatedEmployee.get());
        }

        if(newPassword != null) {
            updatedEmployee.get().setPassword(newPassword);
            employeeService.addEmployee(updatedEmployee.get());
        }

        EntityModel<Employee> entityModel = employeeAssembler.toModel(updatedEmployee.get());

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}