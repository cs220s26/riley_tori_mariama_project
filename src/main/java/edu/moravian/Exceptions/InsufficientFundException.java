package edu.moravian.Exceptions;

public class InsufficientFundException extends RuntimeException {
    public InsufficientFundException() {
        super("Not enough funds to complete the transaction");
    }
}
