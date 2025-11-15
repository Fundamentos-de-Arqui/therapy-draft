package com.soulware.therapydraft.interfaces.rest.controllers;

import com.soulware.therapydraft.application.commands.CreateAssessmentCommand;
import com.soulware.therapydraft.application.services.AssessmentCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.AssessmentResourceFromEntityAssembler;
import com.soulware.therapydraft.interfaces.rest.resources.AssessmentResource;
import com.soulware.therapydraft.interfaces.rest.resources.CreateAssessmentResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/assessments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AssessmentController {

    @Inject
    AssessmentCommandService commandService;

    @POST
    public Response create(CreateAssessmentResource request) {
        var command = new CreateAssessmentCommand(
                request.patientId(),
                request.therapistId(),
                request.scheduledAt()
        );

        var created = commandService.create(command);

        var assessment = AssessmentResourceFromEntityAssembler.toResourceFromEntity(created);

        return Response
                .status(Response.Status.CREATED)
                .entity(assessment)
                .build();
    }
}
