package edu.moravian.Exceptions;

public class NoGameInProgressException extends RuntimeException {
    public NoGameInProgressException() {
        super("No game in progress");
    }
}
