package com.run.game.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.run.game.RoomName;
import com.run.game.component.navigation.RoomComponent;
import com.run.game.component.walking.WalkingBodyComponent;

import map.creator.map.component.data.ContactDataComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.system.ObjectEntityFilter;
import map.creator.map.system.contact.impl.ContactFullIteratingSystem;

public class ViewRoomSystem extends ContactFullIteratingSystem {

    private final Camera camera;

    private boolean isCalledAfterAllRendering = false;

    public ViewRoomSystem(OrthographicCamera camera) {
        super(new ObjectEntityFilter("player", "room"));
        this.camera = camera;
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        if (!isCalledAfterAllRendering) {
            isCalledAfterAllRendering = true;
            return false;
        }

        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;
        RoomComponent roomComponent;

        ObjectEntity player;
        if (AEntity.getType().equals("room")){
            player = BEntity;
            roomComponent = AEntity.getComponent(RoomComponent.class);
        } else {
            player = AEntity;
            roomComponent = BEntity.getComponent(RoomComponent.class);
        }

        Rectangle bounds = roomComponent.boundsRoom;
        RoomName roomName = roomComponent.currentName;

        if (roomName == RoomName.NONE){
            Vector2 positionPlayer = player.getComponent(WalkingBodyComponent.class).getPosition();

            if (bounds.contains(
                positionPlayer.x + bounds.width,
                positionPlayer.y + bounds.height) ||
            bounds.contains(
                positionPlayer.x - bounds.width,
                positionPlayer.y - bounds.height)
            ){
                camera.position.set(
                    (positionPlayer.x),
                    (positionPlayer.y),
                    0
                );
            }
        } else {
            camera.position.set(
                (bounds.x + (bounds.width / 2)),
                (bounds.y + (bounds.height / 2)),
                0
            );
        }

        isCalledAfterAllRendering = false;

        return true;
    }

    @Override
    public boolean stayContact(ContactDataComponent contactDataComponent, float v) {
        if (!isCalledAfterAllRendering) {
            isCalledAfterAllRendering = true;
            return false;
        }

        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;
        RoomComponent roomComponent;

        ObjectEntity player;
        if (AEntity.getType().equals("room")){
            player = BEntity;
            roomComponent = AEntity.getComponent(RoomComponent.class);
        } else {
            player = AEntity;
            roomComponent = BEntity.getComponent(RoomComponent.class);
        }

        Rectangle bounds = roomComponent.boundsRoom;
        RoomName roomName = roomComponent.currentName;

        if (roomName == RoomName.NONE){
            Vector2 positionPlayer = player.getComponent(WalkingBodyComponent.class).getPosition();

            if (bounds.contains(positionPlayer)){
                camera.position.set(
                    (positionPlayer.x),
                    (positionPlayer.y),
                    0
                );
            }
        }

        isCalledAfterAllRendering = false;

        return true;
    }

    @Override
    public boolean endContact(ContactDataComponent contactDataComponent, float v) {
        return false;
    }
}
