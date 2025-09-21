package com.run.game.component.note;

import map.creator.map.component.ObjectComponent;

public class CountGetNotesComponent extends ObjectComponent {

    private int countNotes = 0;

    public CountGetNotesComponent(String owner) {
        super("count-notes", "count", owner);
    }

    public void increaseCount(){
        countNotes++;
    }

    public int getCountNotes() {
        return countNotes;
    }

    public void setCountNotes(int countNotes) {
        this.countNotes = countNotes;
    }

}
