package com.soulware.therapydraft.application.services.commands;

import com.soulware.therapydraft.application.commands.CreateTherapyPlanCommand;
import com.soulware.therapydraft.application.commands.ScheduleEntryCommand;
import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanInformation;
import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.domain.repositories.AssessmentRepository;
import com.soulware.therapydraft.domain.repositories.TherapyPlanRepository;
import com.soulware.therapydraft.infrastructure.events.cdi.CdiEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TherapyPlanCommandService {

    @Inject
    TherapyPlanRepository  therapyPlanRepository;

    @Inject
    AssessmentRepository  assessmentRepository;

    @Inject
    CdiEventPublisher eventPublisher;

    public TherapyPlan create(CreateTherapyPlanCommand command){
        Assessment assessment = assessmentRepository.findById(new AssessmentId(command.assessmentId()))
                .orElseThrow(() -> new RuntimeException("Assessment " + command.assessmentId() + " not found"));

        WeeklySchedule weeklySchedule = buildWeeklySchedule(command.schedule());

        TherapyPlan therapyPlan = new TherapyPlan(
                new TherapyPlanId(command.assessmentId()),
                eventPublisher,
                new AssessmentId(assessment.getId().value()),
                new TherapyPlanInformation(
                        command.description(),
                        command.goals()
                ),
                new TherapistId(command.assignedTherapistId()),
                new PatientId(command.patientId()),
                new LegalResponsibleId(command.legalResponsibleId()),
                new WeeklySchedule(weeklySchedule.schedule())
        );

        therapyPlanRepository.save(therapyPlan);

        return therapyPlan;
    }

    private WeeklySchedule buildWeeklySchedule(List<ScheduleEntryCommand> scheduleEntries) {
        if (scheduleEntries == null || scheduleEntries.isEmpty()) {
            return new WeeklySchedule(Collections.emptyMap());
        }

        Map<DayOfWeek, TimeSlot> scheduleMap = scheduleEntries.stream()
                .collect(Collectors.toMap(
                        entry -> {
                            try {
                                return DayOfWeek.valueOf(entry.dayOfWeek().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException("Invalid day of week value in command: " + entry.dayOfWeek(), e);
                            }
                        },
                        entry -> new TimeSlot(entry.startTime(), entry.endTime()),
                        (existing, replacement) -> {
                            throw new IllegalArgumentException("Duplicate schedule entry detected for the same day.");
                        }
                ));

        return new WeeklySchedule(scheduleMap);
    }
}
