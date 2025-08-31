package com.run.game.system;

import com.badlogic.gdx.maps.MapProperties;
import com.run.game.component.note.CountGetNotesComponent;
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
    private final NoteLabelSystem noteLabelSystem;
    private final Map<String, MapProperties> noteProperties;
    private final float unitScale;

    public NoteSystem(NoteLabelSystem noteLabelSystem, Map<String, MapProperties> noteProperties, float unitScale) {
        super(new ObjectEntityFilter("player", "note"));
        this.noteProperties = noteProperties;
        this.unitScale = unitScale;
        this.noteLabelSystem = noteLabelSystem;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        noteLabelSystem.update(deltaTime);
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        ObjectEntity note = getNoteEntity(contactDataComponent);
        NoteComponent noteComponent = note.getComponent(NoteComponent.class);
        BodyComponent body = note.getComponent(BodyComponent.class);
        MapProperties currentProperties = noteProperties.get("noteProperty" + count);

        noteLabelSystem.startWrite(noteComponent.getMessage());

        body.getBody().setTransform(
            currentProperties.get("x", Float.class) * unitScale,
            currentProperties.get("y", Float.class) * unitScale,
            0
        );
        noteComponent.setMessage(currentProperties.get("message", String.class));

        count++;

        return true;
    }

    private ObjectEntity getNoteEntity(ContactDataComponent contactDataComponent){
        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;

        ObjectEntity note;
        ObjectEntity player;
        if (AEntity.getType().equals("note")){
            note = AEntity;
            player = BEntity;
        } else {
            note = BEntity;
            player = AEntity;
        }

        player.getComponent(CountGetNotesComponent.class).increaseCount();
        return note;
    }
}
