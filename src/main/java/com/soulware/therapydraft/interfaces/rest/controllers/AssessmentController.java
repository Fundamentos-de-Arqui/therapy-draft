package com.soulware.therapydraft.interfaces.rest.controllers;

import com.soulware.therapydraft.application.commands.ChangeAssessmentStatusCommand;
import com.soulware.therapydraft.application.commands.CreateAssessmentCommand;
import com.soulware.therapydraft.application.services.AssessmentCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.AssessmentResourceFromEntityAssembler;
import com.soulware.therapydraft.interfaces.rest.resources.ChangeAssessmentStatusResource;
import com.soulware.therapydraft.interfaces.rest.resources.CreateAssessmentResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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

    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, ChangeAssessmentStatusResource request) {
        var command = new ChangeAssessmentStatusCommand(
                id,
                request.status()
        );

        var updated = commandService.updateStatus(command);

        var  assessment = AssessmentResourceFromEntityAssembler.toResourceFromEntity(updated);

        return Response
                .status(Response.Status.OK)
                .entity(assessment)
                .build();
    }
}
