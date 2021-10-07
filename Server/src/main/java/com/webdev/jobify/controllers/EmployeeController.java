package com.webdev.jobify.controllers;

import com.webdev.jobify._aux.Comment;
import com.webdev.jobify._aux.MatrixFactElement;
import com.webdev.jobify._aux.MatrixFactorization;
import com.webdev.jobify.assemblers.*;
import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.*;
import com.webdev.jobify._aux.Picture;
import com.webdev.jobify.services.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    private final PostModelAssembler postAssembler;


    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler assembler,
                              JobModelAssembler jobAssembler, JobService jobService,
                              ConnectionService connectionService, ConnectionModelAssembler connectionAssembler,
                              CertificateService certificateService, CertificateModelAssembler certificateAssembler,
                              MessageService messageService, MessageModelAssembler messageAssembler,
                              JobAdService jobAdService, JobAdModelAssembler jobAdAssembler, PostService postService, PostModelAssembler postAssembler) {
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
        this.postAssembler = postAssembler;
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

    @PostMapping("/addjobadview")
    public ResponseEntity<?> addJobAdViewToEmploye(@RequestParam("id") Long id) {

        Employee employee = employeeService.findEmployeeById(id);
        employee.setJobAdViews(employee.getJobAdViews() + 1);
        employeeService.updateEmployee(employee);

        return ResponseEntity.status(HttpStatus.OK).body("View added!");
    }


    @GetMapping("/{id}/jobads")
    public CollectionModel<EntityModel<JobAd>> getEmployeeJobAds(@PathVariable("id") Long id) {

        Employee employee = employeeService.findEmployeeById(id);
        List<Employee> allEmployees = employeeService.findAllEmployees();

        List<JobAd> jobAds = jobAdService.findAllJobAds();
        List<String> employeeSkills = employee.getSkills();

        List<JobAd> result = new LinkedList<>();

        // Adding job ads that have employee's skill as position.
        if(employeeSkills.size() != 0){
            for(String skill: employeeSkills) {
                for(JobAd ad: jobAds) {
                    String position = ad.getPosition();
                    if(position.toLowerCase().contains(skill.toLowerCase())){
                        result.add(ad);
                    }
                }
            }
        }


        int[][] rating = new int[allEmployees.size()][jobAds.size()];
        int i=0, j=0;

        for(Employee emp: allEmployees) {
            j = 0;
            for(JobAd ad: jobAds) {
                rating[i][j] = emp.getJobAdViews();
                j++;
            }
            i++;
        }

        int K = 3;

        MatrixFactElement[][] P = new MatrixFactElement[allEmployees.size()][K];
        MatrixFactElement[][] Q = new MatrixFactElement[K][jobAds.size()];


        for(int a = 0 ; a < allEmployees.size() ; a++) {
            for(int b = 0 ; b < K ; b++){
                P[a][b] = new MatrixFactElement();
            }
        }

        for(int b = 0 ; b < K ; b++) {
            for(int a = 0 ; a < jobAds.size() ; a++){
                Q[b][a] = new MatrixFactElement();
            }
        }

        int a =0, b = 0;
        for(Employee emp : allEmployees) {
            b = 0;
            for(int k = 0 ; k < K ; k++){
                P[a][b].value = emp.getJobAdViews();
                P[a][b].object1 = emp;
                b++;
            }
            a++;
        }

        a =0;
        b = 0;
        for(int k = 0 ; k < K ; k++) {
            b = 0;
            for(JobAd ad : jobAds){
                Q[a][b].value = ad.getViews();
                Q[a][b].object1 = ad;
                b++;
            }
            a++;
        }

        MatrixFactElement[][] Rd = MatrixFactorization.matrixFactorization(rating, P, Q, K);

        int row = 0;
        for(int z=0 ; z < Rd.length ; z++) {
            if(Objects.equals(((Employee) Rd[z][0].object1).getId(), employee.getId())) {
                row = z;
            }
        }

        MatrixFactElement jobAdElement = MatrixFactorization.getMax1(Rd[row]);

        result.add(((JobAd)jobAdElement.object1));

        List<EntityModel<JobAd>> finalJobAds = result.stream().map(jobAdAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(finalJobAds, linkTo(methodOn(JobAdController.class).getAllJobAds()).withSelfRel());
    }





    @GetMapping("/{id}/postsfeed")
    public CollectionModel<EntityModel<Post>> getEmployeeFeedPosts(@PathVariable Long id) {

        List<EntityModel<Employee>> allConnections = new LinkedList<>(getEmployeeConnections(id).getContent());

        List<Employee> connections = new LinkedList<>();

        for(EntityModel<Employee> eemp: allConnections) {
            connections.add(eemp.getContent());
        }

        List<Post> resultPosts = new LinkedList<>();

        // First, adding the posts created by employee's connections.
        for(Employee connected: connections) {
            resultPosts.addAll(postService.findPostsByCreatorId(connected.getId()));
        }


        Employee employee = employeeService.findEmployeeById(id);
        List<Employee> allEmployees = employeeService.findAllEmployees();

        List<Post> allPosts = postService.findAllPosts();


        int[][] rating = new int[allEmployees.size()][allPosts.size()];
        int i=0, j=0;

        for(Employee emp: allEmployees) {
            j = 0;

            List<EntityModel<Post>> likedPosts = new LinkedList<>(getEmployeeLikes(emp.getId()).getContent());
            List<Comment> commentedPosts = new LinkedList<>(getEmployeeComments(emp.getId()));

            for(Post p: allPosts) {
                rating[i][j] = 2 * likedPosts.size() + commentedPosts.size() + 1;
                j++;
            }
            i++;
        }

        int K = 3;

        MatrixFactElement[][] P = new MatrixFactElement[allEmployees.size()][K];
        MatrixFactElement[][] Q = new MatrixFactElement[K][allPosts.size()];

        for(int a = 0 ; a < allEmployees.size() ; a++) {
            for(int b = 0 ; b < K ; b++){
                P[a][b] = new MatrixFactElement();
            }
        }

        for(int b = 0 ; b < K ; b++) {
            for(int a = 0 ; a < allPosts.size() ; a++){
                Q[b][a] = new MatrixFactElement();
            }
        }


        int a =0, b = 0;
        for(Employee emp : allEmployees) {
            b = 0;
            List<EntityModel<Post>> likedPosts = new LinkedList<>(getEmployeeLikes(emp.getId()).getContent());
            List<Comment> commentedPosts = new LinkedList<>(getEmployeeComments(emp.getId()));

            for(int k = 0 ; k < K ; k++){
                P[a][b].value =  2 * likedPosts.size() + commentedPosts.size() + 1;
                P[a][b].object1 = emp;
                b++;
            }
            a++;
        }

        a =0;
        b = 0;
        for(int k = 0 ; k < K ; k++) {
            b = 0;
            for(Post p : allPosts){
                Q[a][b].value = 2 * p.getLikes().size() + p.getComments().size()  +1;
                Q[a][b].object1 = p;
                b++;
            }
            a++;
        }


        MatrixFactElement[][] Rd = MatrixFactorization.matrixFactorization(rating, P, Q, K);


        int row = 0;
        for(int z=0 ; z < Rd.length ; z++) {
            if(Objects.equals(((Employee) Rd[z][0].object1).getId(), employee.getId())) {
                row = z;
            }
        }


        MatrixFactElement postElement = MatrixFactorization.getMax2(Rd[row]);

        resultPosts.add(((Post)postElement.object1));


        List<EntityModel<Post>> finalPosts = postService.findAllPosts().stream().map(postAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(finalPosts, linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
    }





    @GetMapping("/{id}/likes")
    public CollectionModel<EntityModel<Post>> getEmployeeLikes(@PathVariable Long id) {

        List<Post> allPosts = postService.findAllPosts();

        List<Post> likedPosts= new LinkedList<>();

        for(Post p : allPosts) {
            for(Employee e : p.getLikes()){
                if(Objects.equals(e.getId(), id)) {
                    likedPosts.add(p);
                }
            }
        }

        List<EntityModel<Post>> finalPosts = likedPosts.stream().map(postAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(finalPosts, linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getEmployeeComments(@PathVariable Long id) {

        List<Post> allPosts = postService.findAllPosts();

        List<Comment> comments = new LinkedList<>();

        for(Post p : allPosts) {
            for(Comment c : p.getComments()){
                if(Objects.equals(c.getCreator().getId(), id)) {
                    comments.add(c);
                }
            }
        }
        return comments;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill could not be added!");
        }

        return ResponseEntity.status(HttpStatus.OK).body("SKill added!");
    }


    @GetMapping("/{id}/messages")
    public CollectionModel<EntityModel<Message>> getMessagesOfEmployee1WithEmployee2(@PathVariable Long id) {

        List<EntityModel<Message>> messages = messageService.findMessagesOfEmployee(id).stream().map(messageAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).getAllMessages()).withSelfRel());
    }

    @GetMapping("/{id}/posts")
    public CollectionModel<EntityModel<Post>> getEmployeePosts(@PathVariable Long id) {
        List<EntityModel<Post>> posts = postService.findPostsByCreatorId(id).stream().map(postAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
    }


    @GetMapping("/search/{input}")
    public CollectionModel<EntityModel<Employee>> getEmployeesBySearch(@PathVariable String input) {

        List<Employee> employees = employeeService.findAllEmployees();

        List<Employee> result = new LinkedList<>();

        for(Employee e: employees) {
            if(e.getName().toLowerCase().contains(input.toLowerCase()) || e.getSurname().toLowerCase().contains(input.toLowerCase())){
                result.add(e);
            }
        }

        List<EntityModel<Employee>> finalEmployees = result.stream().map(employeeAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(finalEmployees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());

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