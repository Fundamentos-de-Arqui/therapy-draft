package com.soulware.therapydraft.domain.model.entities;

import com.soulware.therapydraft.domain.model.valueobjects.ids.SessionId;
import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.shared.domain.model.entities.BaseAbstractEntity;

/**
 * Represents a therapy session to be done.
 */
public class Session extends BaseAbstractEntity  {
    private TimeSlot slot;
    private TherapistId therapistId;

    /**
     * Constructs a Session Entity.
     */
    public Session(SessionId id, TimeSlot slot, TherapistId therapistId) {
        super(id);

        if (slot == null) {
            throw new IllegalArgumentException("Time slot cannot be null.");
        }
        if (therapistId == null) {
            throw new IllegalArgumentException("Therapist ID cannot be null.");
        }

        this.slot = slot;
        this.therapistId = therapistId;
    }
    
    // Getters
    public TimeSlot getSlot() { return this.slot; }
    public TherapistId getTherapist() { return this.therapistId; }

    @Override
    public SessionId getId() {
        return (SessionId) super.getId();
    }
}
