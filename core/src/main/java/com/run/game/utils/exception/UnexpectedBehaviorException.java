package com.run.game.utils.exception;

// when the program behaves not as planned
public class UnexpectedBehaviorException extends RuntimeException {
    public UnexpectedBehaviorException(String message) {
        super(message);
    }
}
