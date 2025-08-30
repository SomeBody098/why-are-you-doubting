package com.run.game.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.run.game.component.note.NoteComponent;
import com.run.game.ui.UiController;
import com.run.game.ui.obj.ProgressivelyLabel;
import map.creator.map.component.body.BodyComponent;
import map.creator.map.component.data.ContactDataComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.system.ObjectEntityFilter;
import map.creator.map.system.contact.impl.ContactBeginIteratingSystem;
import java.util.Map;

public class NoteSystem extends ContactBeginIteratingSystem {

    private int count = 0;
    private float timer = 0;
    private final UiController controller;
    private final Map<String, MapProperties> noteProperties;
    private final float unitScale;
    private String currentNoteName;

    public NoteSystem(UiController controller, Map<String, MapProperties> noteProperties, float unitScale) {
        super(new ObjectEntityFilter("player", "note"));
        this.controller = controller;
        this.noteProperties = noteProperties;
        this.unitScale = unitScale;
        currentNoteName = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (currentNoteName == null) return;

        ProgressivelyLabel currentLabel = (ProgressivelyLabel) controller.get(currentNoteName);
        if (!currentLabel.isTypedAllTheText()) return;
        timer += deltaTime;
        if (timer < (float) currentLabel.getText().length / 10) return;

        currentLabel.setVisible(false);
        currentNoteName = null;
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        ObjectEntity note = getNoteEntity(contactDataComponent);
        NoteComponent noteComponent = note.getComponent(NoteComponent.class);
        BodyComponent body = note.getComponent(BodyComponent.class);
        MapProperties currentProperties = noteProperties.get("noteProperty" + count);

        ProgressivelyLabel label = (ProgressivelyLabel) controller.get(note.getName());
        label.newTyping(noteComponent.getMessage(), -1, 0.25f);
        label.setVisible(true);

        body.getBody().setTransform(
            currentProperties.get("x", Float.class) * unitScale,
            currentProperties.get("y", Float.class) * unitScale,
            0
        );
        noteComponent.setMessage(currentProperties.get("message", String.class));

        Gdx.app.log("bodyNotePos", body.getBody().getPosition().toString());

        currentNoteName = label.getName();
        count++;

        return true;
    }

    private ObjectEntity getNoteEntity(ContactDataComponent contactDataComponent){
        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;

        ObjectEntity note;
        if (AEntity.getType().equals("note")){
            note = AEntity;
        } else {
            note = BEntity;
        }

        return note;
    }
}
