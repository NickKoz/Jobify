package com.webdev.jobify.controllers;


import com.webdev.jobify.assemblers.ConnectionModelAssembler;
import com.webdev.jobify.model.Connection;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.services.ConnectionService;
import com.webdev.jobify.services.EmployeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;
    private final ConnectionModelAssembler assembler;
    private final EmployeeService employeeService;

    public ConnectionController(ConnectionService connectionService, ConnectionModelAssembler assembler, EmployeeService employeeService) {
        this.connectionService = connectionService;
        this.assembler = assembler;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public EntityModel<Connection> getConnectionById(@PathVariable("id") Long id) {
        Connection connection = connectionService.findConnectionById(id);
        return assembler.toModel(connection);
    }

    @GetMapping("/employees/{ids}")
    public EntityModel<Connection> getConnectionByEmployees(@PathVariable Long[] ids) {
        Long id1 = ids[0];
        Long id2 = ids[1];

        Connection connection = connectionService.findConnectionOfEmployee1Employee2(id1, id2);

        return assembler.toModel(connection);
    }


    @GetMapping("/all")
    public CollectionModel<EntityModel<Connection>> getAllConnections() {
        List<EntityModel<Connection>> connections = connectionService.findAllConnections().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(connections, linkTo(methodOn(ConnectionController.class).getAllConnections()).withSelfRel());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPendingConnection(@RequestParam("requester") Long requester_id, @RequestParam("receiver") Long receiver_id) {

        EntityModel<Connection> entityModel;
        Connection connection = new Connection();

        try {

            if(Objects.equals(requester_id, receiver_id)) {
                throw new Exception("Connection with same IDs!");
            }


            if(connectionService.findConnectionOfEmployee1Employee2(receiver_id, requester_id) != null){
                throw new Exception("Connection exists!");
            }
            Employee requester = employeeService.findEmployeeById(requester_id);
            Employee receiver = employeeService.findEmployeeById(receiver_id);
            connection.setRequester(requester);
            connection.setReceiver(receiver);
            entityModel = assembler.toModel(connectionService.addConnection(connection));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add connection!");
        }

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptConnection(@RequestParam(value = "receiver") Long receiver_id,
                                              @RequestParam(value = "requester") Long requester_id){

        Connection updatedConnection = connectionService.findConnectionOfEmployee1Employee2(receiver_id, requester_id);
        updatedConnection.setPending(false);

        connectionService.addConnection(updatedConnection);

        EntityModel<Connection> entityModel = assembler.toModel(updatedConnection);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/delete")
    public ResponseEntity<?> removeConnection(@RequestParam(value = "receiver") Long receiver_id,
                                              @RequestParam(value = "requester") Long requester_id) {

        Connection updatedConnection = connectionService.findConnectionOfEmployee1Employee2(receiver_id, requester_id);

        connectionService.removeConnection(updatedConnection.getId());

        return ResponseEntity.noContent().build();
    }


}
