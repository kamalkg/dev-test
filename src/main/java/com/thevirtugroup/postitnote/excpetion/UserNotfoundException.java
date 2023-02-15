package com.thevirtugroup.postitnote.excpetion;

public class UserNotfoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserNotfoundException (String message) {
        super(message);
    }
}
