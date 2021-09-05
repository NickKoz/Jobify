package com.webdev.jobify.controllers;

import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.assemblers.EmployeeModelAssembler;
import com.webdev.jobify.model.Picture;
import com.webdev.jobify.services.EmployeeService;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler assembler;


    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler assembler) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Employee>> getAllEmployees(){
        List<EntityModel<Employee>> employees = employeeService.findAllEmployees().stream().map(assembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }


    @GetMapping("/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.findEmployeeById(id);
        return assembler.toModel(employee);
    }


    @GetMapping("/{id}/picture")
    public Picture getEmployeePicture(@PathVariable("id") Long id) throws IOException {

        Employee employee = employeeService.findEmployeeById(id);
        String pictureName = employee.getImageUrl();
        Path pictLocation = Paths.get(pictureName);

        if(pictureName.isEmpty()){
            return null;
        }

        byte[] data = Files.readAllBytes(pictLocation);

        String pictExtension = pictureName.substring(pictureName.lastIndexOf(".") + 1);

        return new Picture(pictExtension, data);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@RequestParam("email") String email, @RequestParam("password") String password) {

        EntityModel<Employee> entityModel;

        try {
            entityModel = assembler.toModel(employeeService.findEmployeeByEmail(email));
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
        return assembler.toModel(employee);
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addEmployee(@RequestPart("employee") Employee employee, @RequestPart(value = "picture", required = false) MultipartFile picture) throws IOException {

        employee.updateProfilePicture(picture);

        EntityModel<Employee> entityModel = assembler.toModel(employeeService.addEmployee(employee));

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

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

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