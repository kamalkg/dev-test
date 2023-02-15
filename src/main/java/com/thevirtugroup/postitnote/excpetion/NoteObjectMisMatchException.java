package com.thevirtugroup.postitnote.excpetion;

public class NoteObjectMisMatchException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoteObjectMisMatchException(String message) {
        super(message);
    }
}
