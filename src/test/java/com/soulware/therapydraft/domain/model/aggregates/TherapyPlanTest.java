package com.soulware.therapydraft.domain.model.aggregates;

import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanInformation;
import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.infrastructure.events.cdi.CdiEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class TherapyPlanTest {

    @Test
    void shouldCreateTherapyPlanSuccessfully(){
        //Arrange
        TherapyPlanId therapyPlanId = new TherapyPlanId(1L);
        CdiEventPublisher publisher = new CdiEventPublisher();
        AssessmentId assessmentId = new AssessmentId(1L);
        TherapyPlanInformation therapyPlanInformation = new TherapyPlanInformation(
                "Plan de rehabilitación post-operatoria enfocado en la movilidad y fuerza.",
                "Recuperar el 90% del rango de movimiento en 12 semanas. Reducir el dolor a nivel 2/10."
        );
        TherapistId therapistId = new TherapistId(1L);
        PatientId patientId = new PatientId(1L);
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(1L);

        ZoneId zone = ZoneId.of("UTC");

        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime slotStart = now.plusDays(1).withHour(10).withMinute(30).withSecond(0).withNano(0);
        ZonedDateTime slotEnd = slotStart.plusHours(1); // 11:30

        TimeSlot timeSlot = new TimeSlot(slotStart, slotEnd);

        Map<DayOfWeek, TimeSlot> scheduleMap = Map.of(
                DayOfWeek.MONDAY, timeSlot,
                DayOfWeek.THURSDAY, timeSlot
        );

        WeeklySchedule weeklySchedule = new WeeklySchedule(scheduleMap);

        //Act
        TherapyPlan therapyPlan = new TherapyPlan(
                therapyPlanId,
                publisher,
                assessmentId,
                therapyPlanInformation,
                therapistId,
                patientId,
                legalResponsibleId,
                weeklySchedule
        );

        //Assert
        assertThat(therapyPlan.getAssessmentId().value()).isEqualTo(1L);
        assertThat(therapyPlan.getPatientId().value()).isEqualTo(1L);
        assertThat(therapyPlan.getTherapyPlanInformation().Description()).isEqualTo("Plan de rehabilitación post-operatoria enfocado en la movilidad y fuerza.");
    }
}
