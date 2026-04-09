package edu.moravian.Exceptions;

public class StorageException extends RuntimeException {
    public StorageException() {
        super("Error accessing storage");
    }
}
