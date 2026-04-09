package edu.moravian.Exceptions;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException(String name) {
        super("Player " + name + " is not in the game");
    }
}
