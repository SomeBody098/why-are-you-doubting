package com.run.game.system;

import com.badlogic.ashley.core.Entity;
import com.run.game.component.note.NoteComponent;
import com.run.game.ui.UiController;
import com.run.game.ui.obj.ProgressivelyLabel;
import map.creator.map.component.data.ContactDataComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.system.ObjectEntityFilter;
import map.creator.map.system.contact.impl.ContactBeginIteratingSystem;

public class NoteSystem extends ContactBeginIteratingSystem {

    private int count = 0;
    private final UiController controller;
    private String currentNoteName;

    public NoteSystem(UiController controller) {
        super(new ObjectEntityFilter("player", "note"));
        this.controller = controller;
        currentNoteName = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        super.processEntity(entity, deltaTime);

        if (currentNoteName == null) return;

        ProgressivelyLabel currentLabel = (ProgressivelyLabel) controller.get(currentNoteName);
        if (!currentLabel.isTypedAllTheText()) return;

        currentLabel.setVisible(false);
        currentNoteName = null;
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        NoteComponent noteComponent = getNoteComponent(contactDataComponent);

        ProgressivelyLabel label = (ProgressivelyLabel) controller.get(noteComponent.getOwner());
        String newMessage = noteComponent.getMessage();

        label.newTyping(newMessage, -1, 0.5f);
        label.setVisible(true);

        currentNoteName = label.getName();
        count++;

        return true;
    }

    private NoteComponent getNoteComponent(ContactDataComponent contactDataComponent){
        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;

        ObjectEntity note;
        if (AEntity.getType().equals("note")){
            note = AEntity;
        } else {
            note = BEntity;
        }

        return note.getComponent(NoteComponent.class);
    }
}
