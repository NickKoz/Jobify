package com.webdev.jobify.assemblers;

import com.webdev.jobify.controllers.AdministratorController;
import com.webdev.jobify.model.Administrator;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdministratorModelAssembler implements RepresentationModelAssembler<Administrator, EntityModel<Administrator>> {

    @Override
    public EntityModel<Administrator> toModel(Administrator admin) {

        return EntityModel.of(admin, //
                linkTo(methodOn(AdministratorController.class).getAdminByEmail(admin.getEmail())).withSelfRel());
    }
}
