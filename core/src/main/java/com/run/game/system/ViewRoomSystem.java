package com.run.game.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.run.game.component.navigation.RoomComponent;
import map.creator.map.component.data.ContactDataComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.system.ObjectEntityFilter;
import map.creator.map.system.contact.impl.ContactBeginIteratingSystem;

public class ViewRoomSystem extends ContactBeginIteratingSystem {

    private final Camera camera;

    public ViewRoomSystem(OrthographicCamera camera) {
        super(new ObjectEntityFilter("player", "room"));
        this.camera = camera;
    }

    @Override
    public boolean beginContact(ContactDataComponent contactDataComponent, float deltaTime) {
        ObjectEntity AEntity = contactDataComponent.AEntity;
        ObjectEntity BEntity = contactDataComponent.BEntity;
        RoomComponent roomComponent;

        if (AEntity.getType().equals("room")){
            roomComponent = AEntity.getComponent(RoomComponent.class);
        } else {
            roomComponent = BEntity.getComponent(RoomComponent.class);
        }

        Rectangle bounds = roomComponent.boundsRoom;
        camera.position.set(
            (bounds.x + (bounds.width / 2)),
            (bounds.y + (bounds.height / 2)),
            0
        );

        return true;
    }
}
