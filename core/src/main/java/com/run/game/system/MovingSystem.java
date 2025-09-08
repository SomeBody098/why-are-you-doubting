package com.run.game.system;

import com.badlogic.gdx.math.Vector2;
import com.run.game.component.navigation.TeleportComponent;
import com.run.game.component.note.CountGetNotesComponent;
import com.run.game.component.walking.WalkingBodyComponent;
import com.run.game.utils.music.MusicManager;

import map.creator.map.component.data.ContactDataComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.system.ObjectEntityFilter;
import map.creator.map.system.contact.impl.ContactBeginIteratingSystem;

public class MovingSystem extends ContactBeginIteratingSystem {

    private final MusicManager musicManager;
    private final NoteLabelSystem noteLabelSystem;

    public MovingSystem(MusicManager musicManager, NoteLabelSystem noteLabelSystem) {
        super(new ObjectEntityFilter("player", "moving"));
        this.musicManager = musicManager;
        this.noteLabelSystem = noteLabelSystem;
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;

        if (AEntity.getName().equals("player")){
            process(BEntity, AEntity);

        } else if (BEntity.getName().equals("player")){
            process(AEntity, BEntity);

        } else {
            return false;
        }

        return true;
    }

    private void process(ObjectEntity moving, ObjectEntity player){
        int countNotes = moving.getComponent(TeleportComponent.class).getCountNotes();
        int countGetNotes = player.getComponent(CountGetNotesComponent.class).getCountNotes();

        if (countNotes == -1 || countGetNotes == countNotes) {
            Vector2 teleportPosition = moving.getComponent(TeleportComponent.class).getTeleportPosition();
            playMusic(moving);
            player.getComponent(WalkingBodyComponent.class).updatePosition(teleportPosition);

        } else if (countGetNotes < countNotes){
            noteLabelSystem.startWrite("Collect the " + (countNotes - countGetNotes) + "  notes first");
        }
    }

    private void playMusic(ObjectEntity entity){
        String soundLadder = "run_over_ladder";
        String soundDoor = "door_opening";

        if (entity.getName().contains("ladder")) {
            musicManager.initSound("home", soundLadder);
        } else {
            musicManager.initSound("home", soundDoor);
        }
    }
}
