package com.webdev.jobify.controllers;

import com.webdev.jobify.model.Employee;
import com.webdev.jobify.model.EmployeeModelAssembler;
import com.webdev.jobify.services.EmployeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/find/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.findEmployeeById(id);
        return assembler.toModel(employee);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee){
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