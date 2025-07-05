package com.laboratorio.exception;

/**
 *
 * author Rafael
 * version 1.1
 * created 03/05/2025
 * updated 05/07/2025
 */
public class OperationException extends RuntimeException {
    private Throwable causaOriginal = null;

    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable causaOriginal) {
        super(message, causaOriginal);
        this.causaOriginal = causaOriginal;
    }

    @Override
    public String getMessage() {
        if (this.causaOriginal != null) {
            return super.getMessage() + " | Causa original: " + this.causaOriginal.getMessage();
        }

        return super.getMessage();
    }
}