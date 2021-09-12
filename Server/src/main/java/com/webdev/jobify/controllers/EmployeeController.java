package com.webdev.jobify.controllers;

import com.webdev.jobify.assemblers.CertificateModelAssembler;
import com.webdev.jobify.assemblers.ConnectionModelAssembler;
import com.webdev.jobify.assemblers.JobModelAssembler;
import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.Certificate;
import com.webdev.jobify.model.Connection;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.assemblers.EmployeeModelAssembler;
import com.webdev.jobify._aux.Picture;
import com.webdev.jobify.model.Job;
import com.webdev.jobify.services.CertificateService;
import com.webdev.jobify.services.ConnectionService;
import com.webdev.jobify.services.EmployeeService;
import com.webdev.jobify.services.JobService;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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


    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler assembler,
                              JobModelAssembler jobAssembler, JobService jobService,
                              ConnectionService connectionService, ConnectionModelAssembler connectionAssembler,
                              CertificateService certificateService, CertificateModelAssembler certificateAssembler) {
        this.employeeService = employeeService;
        this.employeeAssembler = assembler;
        this.jobAssembler = jobAssembler;
        this.jobService = jobService;
        this.connectionService = connectionService;
        this.connectionAssembler = connectionAssembler;
        this.certificateService = certificateService;
        this.certificateAssembler = certificateAssembler;
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
        Path pictLocation = Paths.get(pictureName);

        if(pictureName.isEmpty()){
            return null;
        }

        byte[] data = Files.readAllBytes(pictLocation);

        String pictExtension = pictureName.substring(pictureName.lastIndexOf(".") + 1);

        return new Picture(pictExtension, data);
    }

    @GetMapping("/{id}/experience")
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

    @GetMapping("/{id}/certificates")
    public CollectionModel<EntityModel<Certificate>> getEmployeeCertificates(@PathVariable("id") Long id) {

        List<EntityModel<Certificate>> certificates = certificateService.findCertificatesByEmployeeId(id).stream().map(certificateAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(certificates, linkTo(methodOn(CertificateController.class).getAllCertificates()).withSelfRel());

    }


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

        EntityModel<Employee> entityModel = employeeAssembler.toModel(employeeService.addEmployee(employee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        Employee updatedEmployee = employeeService.employeeRepo.findEmployeeById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    return employeeService.addEmployee(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeService.addEmployee(newEmployee);
                });

        EntityModel<Employee> entityModel = employeeAssembler.toModel(updatedEmployee);

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