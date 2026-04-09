package edu.moravian.Exceptions;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("Must be a positive number greater than 0");
    }
}
