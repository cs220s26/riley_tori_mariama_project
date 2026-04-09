package edu.moravian.Exceptions;

public class InvalidStockException extends RuntimeException {
    public InvalidStockException() {
        super("Invalid stock symbol");
    }
}
