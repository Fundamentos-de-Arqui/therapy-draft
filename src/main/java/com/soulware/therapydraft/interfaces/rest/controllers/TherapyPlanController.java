package com.soulware.therapydraft.interfaces.rest.controllers;

import com.soulware.therapydraft.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapydraft.application.commands.ScheduleEntryCommand;
import com.soulware.therapydraft.application.queries.GetTherapyPlansQuery;
import com.soulware.therapydraft.application.services.commands.TherapyPlanCommandService;
import com.soulware.therapydraft.application.services.queries.TherapyPlanQueryService;
import com.soulware.therapydraft.interfaces.rest.assemblers.TherapyPlanResourceFromEntityAssembler;
import com.soulware.therapydraft.interfaces.rest.resources.CreateTherapyPlanResource;
import com.soulware.therapydraft.interfaces.rest.resources.PagedResponseResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/therapy-plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class TherapyPlanController {

    @Inject
    TherapyPlanCommandService  commandService;

    @Inject
    TherapyPlanQueryService queryService;

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

    @GET
    public Response getTherapyPlans(
            @QueryParam("assessmentId") Long assessmentId,
            @QueryParam("therapistId") Long therapistId,
            @QueryParam("patientId") Long patientId,
            @QueryParam("legalResponsibleId") Long legalResponsibleId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        var query = new GetTherapyPlansQuery(assessmentId, therapistId, patientId, legalResponsibleId, page, size);

        var paged = queryService.getTherapyPlans(query);

        var resourceItems = paged.getItems().stream()
                .map(TherapyPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        var response = new PagedResponseResource<>(
                resourceItems,
                paged.getTotalItems(),
                paged.getTotalPages(),
                paged.getPage(),
                paged.getSize()
        );

        return Response.ok(response).build();
    }
}
