package com.webdev.jobify.assemblers;


import com.webdev.jobify.controllers.JobAdController;
import com.webdev.jobify.model.JobAd;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class JobAdModelAssembler implements RepresentationModelAssembler<JobAd, EntityModel<JobAd>> {

    @Override
    public EntityModel<JobAd> toModel(JobAd jobAd) {
        return EntityModel.of(jobAd,
                linkTo(methodOn(JobAdController.class).getJobAdById(jobAd.getId())).withSelfRel(),
                linkTo(methodOn(JobAdController.class).getAllJobAds()).withRel("jobads"));
    }

}
