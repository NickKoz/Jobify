package com.webdev.jobify.controllers;


import com.webdev.jobify.assemblers.MessageModelAssembler;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.model.Message;
import com.webdev.jobify.services.EmployeeService;
import com.webdev.jobify.services.MessageService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final MessageModelAssembler assembler;
    private final EmployeeService employeeService;

    public MessageController(MessageService messageService, MessageModelAssembler assembler, EmployeeService employeeService) {
        this.messageService = messageService;
        this.assembler = assembler;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public EntityModel<Message> getMessageById(@PathVariable("id") Long id) {
        Message message = messageService.findMessageById(id);
        return assembler.toModel(message);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Message>> getAllMessages() {
        List<EntityModel<Message>> messages = messageService.findAllMessages().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(messages, linkTo(methodOn(MessageController.class).getAllMessages()).withSelfRel());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addMessage(@RequestParam("sender_id") Long sender_id, @RequestParam("receiver_id") Long receiver_id,
                                        @RequestParam("text") String text) {

        EntityModel<Message> entityModel;

        try {

            if(Objects.equals(sender_id, receiver_id)) {
                throw new Exception();
            }

            Employee sender = employeeService.findEmployeeById(sender_id);
            Employee receiver = employeeService.findEmployeeById(receiver_id);

            Date now = new Date();
            Message message = new Message(sender, receiver, text, new Timestamp(now.getTime()));
            entityModel = assembler.toModel(messageService.addMessage(message));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add message!");
        }

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }



}
