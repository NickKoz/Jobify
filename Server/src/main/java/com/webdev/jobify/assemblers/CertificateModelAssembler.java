package com.webdev.jobify.assemblers;

import com.webdev.jobify.controllers.CertificateController;
import com.webdev.jobify.model.Certificate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateModelAssembler implements RepresentationModelAssembler<Certificate, EntityModel<Certificate>> {

    @Override
    public EntityModel<Certificate> toModel(Certificate certificate) {
        return EntityModel.of(certificate,
                linkTo(methodOn(CertificateController.class).getCertificateById(certificate.getId())).withSelfRel(),
                linkTo(methodOn(CertificateController.class).getAllCertificates()).withRel("certificates"));
    }
}
