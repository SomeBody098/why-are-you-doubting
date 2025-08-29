package com.run.game.component.navigation;

import com.badlogic.gdx.math.Rectangle;
import com.run.game.RoomName;

import map.creator.map.component.ObjectComponent;

public class RoomComponent extends ObjectComponent {

    public final RoomName currentName;

    public final Rectangle boundsRoom;

    public RoomComponent(String owner, RoomName currentName, Rectangle boundsRoom, float unitScale) {
        super("room-component", "navigation", owner);
        this.currentName = currentName;

        this.boundsRoom = new Rectangle(boundsRoom);
        this.boundsRoom.x *= unitScale;
        this.boundsRoom.y *= unitScale;
        this.boundsRoom.width *= unitScale;
        this.boundsRoom.height *= unitScale;
    }
}
