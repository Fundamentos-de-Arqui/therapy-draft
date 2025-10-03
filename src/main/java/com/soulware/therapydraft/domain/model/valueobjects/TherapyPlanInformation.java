package com.soulware.therapydraft.domain.model.valueobjects;

public record TherapyPlanInformation(String Description, String Goals) {
    public TherapyPlanInformation{
        if (Description.isEmpty())
            throw new NullPointerException("Description cannot be empty");
        if (Goals.isEmpty())
            throw new NullPointerException("Goals cannot be empty");
    }
}
