package com.run.game.component.navigation;

import com.badlogic.gdx.math.Vector2;
import com.run.game.component.walking.WalkingBodyComponent;
import com.run.game.utils.music.MusicManager;

import map.creator.map.component.trigger.Trigger;
import map.creator.map.entity.ObjectEntity;

public class MovingTrigger extends Trigger {

    private final Vector2 teleportPosition;
    public final MusicManager musicManager;

    public MovingTrigger(String owner, Vector2 teleportPosition, MusicManager musicManager) {
        super("moving", owner);
        this.teleportPosition = teleportPosition;
        this.musicManager = musicManager;
    }

    @Override
    public boolean beginContact(ObjectEntity AEntity, ObjectEntity BEntity, float deltaTime) {
        if (AEntity.getName().equals("player")){
            playMusic();
            AEntity.getComponent(WalkingBodyComponent.class).updatePosition(teleportPosition);

        } else if (BEntity.getName().equals("player")){
            playMusic();
            BEntity.getComponent(WalkingBodyComponent.class).updatePosition(teleportPosition);

        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean stayContact(ObjectEntity objectEntity, ObjectEntity objectEntity1, float deltaTime) {
        return false;
    }

    @Override
    public boolean endContact(ObjectEntity objectEntity, ObjectEntity objectEntity1, float deltaTime) {
        return false;
    }

    private void playMusic(){
        String soundLadder = "run_over_ladder";
        String soundDoor = "door_opening";

        if (owner.equals("ladder")) {
            musicManager.initSound("home", soundLadder);
        } else {
            musicManager.initSound("home", soundDoor);
        }
    }
}
