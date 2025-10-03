package com.soulware.therapydraft.domain.model.valueobjects;

import java.util.Set;

public enum AssessmentStatus {
    /**
     * The session has been scheduled and is upcoming.
     */
    SCHEDULED{
        @Override
        public Set<AssessmentStatus> nextStates(){
            return Set.of(REQUIRES_RESCHEDULING, DONE, CANCELED, MISSED);
        }
    },

    /**
     * The session programming falls on an valid date (like a holiday) and must be rebooked for a future time slot.
     */
    REQUIRES_RESCHEDULING{
        @Override
        public Set<AssessmentStatus> nextStates(){
            return Set.of(SCHEDULED, CANCELED);
        }
    },

    /**
     * The session was canceled by the therapist or the client before it started.
     */
    CANCELED,

    /**
     * The session was neither completed nor canceled.
     * Either the client or the assigned therapist did not attend or reach the session at the scheduled time.
     */
    MISSED,


    /**
     * The session was successfully completed.
     */
    DONE;

    public Set<AssessmentStatus> nextStates(){
        return Set.of();
    }

    public Boolean canTransitionTo(AssessmentStatus newState) {
        return nextStates().contains(newState);
    }
}