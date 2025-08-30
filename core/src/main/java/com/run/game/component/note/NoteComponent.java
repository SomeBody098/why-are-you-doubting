package com.run.game.component.note;

import map.creator.map.component.ObjectComponent;

public class NoteComponent extends ObjectComponent {

    private String message;

    public NoteComponent(String message, String owner) {
        super("note", "note", owner);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
