package com.soulware.therapydraft.shared.domain.model.valueobjects;

public record Name(String firstName, String lastName) {
    public Name() {
        this("John", "Doe");
    }

    public Name{
        if(firstName.isEmpty() || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty or blank");
        if(lastName.isEmpty() || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty or blank");
    }
}