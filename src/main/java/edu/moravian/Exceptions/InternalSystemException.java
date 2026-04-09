package edu.moravian.Exceptions;

public class InternalSystemException extends RuntimeException {
    public InternalSystemException() {
        super("An internal system error has occurred.");
    }
}
