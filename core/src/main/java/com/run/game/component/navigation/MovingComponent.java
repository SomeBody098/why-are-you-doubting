package com.run.game.component.navigation;

import com.run.game.RoomName;

import map.creator.map.component.ObjectComponent;

public class MovingComponent extends ObjectComponent {

    public final RoomName from_where;
    public final RoomName where;

    public MovingComponent(String owner, RoomName from_where, RoomName where) {
        super("moving-component", "navigation", owner);
        this.from_where = from_where;
        this.where = where;
    }

}
