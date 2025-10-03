package com.soulware.therapydraft.shared.domain.model.exceptions;

/**
 * Base abstract class for all custom Domain Exceptions in the Bounded Context.
 * Ensures the exception is unchecked (RuntimeException) and enforces the
 * DomainException contract (getErrorCode()).
 */
public abstract class BaseAbstractDomainException extends RuntimeException implements DomainException {

    private final String errorCode;

    /**
     * Constructs a BaseAbstractDomainException with a detailed message and the unique error code.
     */
    protected BaseAbstractDomainException(String message, String errorCode) {
        super(message);
        if (errorCode == null || errorCode.isBlank()) {
            throw new IllegalArgumentException("Error code cannot be null or blank.");
        }

        this.errorCode = errorCode;
    }

    /**
     * Constructs a BaseAbstractDomainException with a detailed message, the unique error code,
     * and a cause (the underlying exception).
     * @param message The detailed message.
     * @param errorCode The business-specific error code.
     * @param cause The root cause of the exception.
     * @throws IllegalArgumentException if the errorCode is null or blank.
     */
    protected BaseAbstractDomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        if (errorCode == null || errorCode.isBlank()) {
            throw new IllegalArgumentException("Error code cannot be null or blank.");
        }

        this.errorCode = errorCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getErrorCode() {
        return this.errorCode;
    }
}
