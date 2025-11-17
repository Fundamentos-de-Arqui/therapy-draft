package com.soulware.therapydraft.domain.model.aggregates;

import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.infrastructure.events.cdi.CdiEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.time.ZonedDateTime;

public class AssessmentTest {

    @Test
    void shouldCreateAssessmentRecordSuccessfully() {
        //Arrange
        CdiEventPublisher publisher = new CdiEventPublisher();
        AssessmentId assessmentId = new AssessmentId(1L);
        PatientId patientId = new PatientId(1L);
        TherapistId  therapistId = new TherapistId(1L);
        ZonedDateTime scheduledTo = ZonedDateTime.now();

        //Act
        Assessment assessment = Assessment.createInitial(
                assessmentId,
                publisher,
                patientId,
                therapistId,
                scheduledTo
        );

        //Assert
        assertThat(assessment.getPatientId().value()).isEqualTo(1L);
        assertThat(assessment.getTherapistId().value()).isEqualTo(1L);
        assertThat(assessment.getStatus().name()).isEqualTo("SCHEDULED");
        assertThat(assessment.getType().name()).isEqualTo("INITIAL");
    }
}
