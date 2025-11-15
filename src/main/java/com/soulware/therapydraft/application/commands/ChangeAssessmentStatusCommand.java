package com.soulware.therapydraft.application.commands;

public record ChangeAssessmentStatusCommand(Long assessmentId, String status) {
}
