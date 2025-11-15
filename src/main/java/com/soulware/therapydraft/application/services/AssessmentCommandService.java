package com.soulware.therapydraft.application.services;

import com.soulware.therapydraft.application.commands.ChangeAssessmentStatusCommand;
import com.soulware.therapydraft.application.commands.CreateAssessmentCommand;
import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentStatus;
import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.domain.repositories.AssessmentRepository;
import com.soulware.therapydraft.infrastructure.events.cdi.CdiEventPublisher;
import com.soulware.therapydraft.infrastructure.messaging.senders.AssessmentMessageSender;
import com.soulware.therapydraft.infrastructure.persistence.jpa.repositories.AssessmentStatusRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

@ApplicationScoped
public class AssessmentCommandService {

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    AssessmentMessageSender  assessmentMessageSender;

    @Inject
    CdiEventPublisher  eventPublisher;

    public Assessment create(CreateAssessmentCommand command) {
        Assessment assessment = Assessment.createInitial(
                new AssessmentId(command.patientId()),
                eventPublisher,
                new PatientId(command.patientId()),
                new TherapistId(command.therapistId()),
                command.scheduledAt()
        );

        if(!assessmentRepository.findByPatientId(new PatientId(command.patientId())).isEmpty())
            assessment = Assessment.createReassessment(
                    new AssessmentId(command.patientId()),
                    eventPublisher,
                    new PatientId(command.patientId()),
                    new TherapistId(command.therapistId()),
                    command.scheduledAt()
            );

        assessmentRepository.save(assessment);

        return assessment;
    }

    public Assessment updateStatus(ChangeAssessmentStatusCommand command) {
        Assessment assessment = assessmentRepository.findById(new AssessmentId(command.assessmentId()))
                .orElseThrow(() -> new EntityNotFoundException("Assessment with " + command.assessmentId() + " not found"));

        assessment.changeStatus(AssessmentStatus.valueOf(command.status()));

        assessmentRepository.update(assessment);

        if(assessment.getStatus().name().equals(AssessmentStatus.DONE.name()))
            assessmentMessageSender.sendAssessmentDone(assessment);

        return assessment;
    }
}
