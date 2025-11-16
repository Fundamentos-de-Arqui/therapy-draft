package com.soulware.therapydraft.interfaces.rest.controllers;

import com.soulware.therapydraft.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapydraft.application.commands.ScheduleEntryCommand;
import com.soulware.therapydraft.application.services.commands.TherapyPlanCommandService;
import com.soulware.therapydraft.interfaces.rest.assemblers.TherapyPlanResourceFromEntityAssembler;
import com.soulware.therapydraft.interfaces.rest.resources.CreateTherapyPlanResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/therapy-plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class TherapyPlanController {

    @Inject
    TherapyPlanCommandService  commandService;

    @POST
    public Response create(CreateTherapyPlanResource request) {
        var command = new CreateTherapyPlanCommand(
                request.assessmentId(),
                request.description(),
                request.goals(),
                request.assignedTherapistId(),
                request.patientId(),
                request.legalResponsibleId(),
                request.schedule()
        );

        var created = commandService.create(command);

        var therapyPlan = TherapyPlanResourceFromEntityAssembler.toResourceFromEntity(created);

        return Response.status(Response.Status.CREATED)
                .entity(therapyPlan)
                .build();
    }
}
