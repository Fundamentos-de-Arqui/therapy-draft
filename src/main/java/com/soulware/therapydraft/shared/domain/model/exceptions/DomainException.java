package com.soulware.therapydraft.shared.domain.model.exceptions;

/**
 * Contract for all business exceptions specific to this Domain Bounded Context.
 * Ensures that all domain exceptions are identifiable and carry a standardized code
 * for easier external handling and logging.
 */
public interface DomainException {

    /**
     * Gets a business-specific, immutable code that identifies the type of error
     * regardless of the exception message (e.g., "OVERLAP_CONFLICT" or "INVALID_SLOT_TIME").
     * @return The unique identifier code for the exception type.
     */
    String getErrorCode();
}
