package edu.moravian.Exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super("Not enough stocks in portfolio");
    }
}
