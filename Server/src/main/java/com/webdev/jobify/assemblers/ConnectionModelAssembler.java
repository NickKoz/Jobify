package com.webdev.jobify.assemblers;

import com.webdev.jobify.controllers.ConnectionController;
import com.webdev.jobify.model.Connection;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConnectionModelAssembler implements RepresentationModelAssembler<Connection, EntityModel<Connection>> {

    @Override
    public EntityModel<Connection> toModel(Connection connection) {
        return EntityModel.of(connection,
                linkTo(methodOn(ConnectionController.class).getConnectionById(connection.getId())).withSelfRel(),
                linkTo(methodOn(ConnectionController.class).getAllConnections()).withRel("jobs"));
    }

}
