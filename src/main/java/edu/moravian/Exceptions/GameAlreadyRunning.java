package edu.moravian.Exceptions;

public class GameAlreadyRunning extends RuntimeException {
    public GameAlreadyRunning() {
        super("A game is already running");
    }
}
