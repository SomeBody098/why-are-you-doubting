package com.run.game.dto;

import com.run.game.map.RoomName;

public class MovingDto extends SensorDto{

    private final RoomName where;

    public MovingDto(String name, RoomName where) {
        super(name);
        this.where = where;
    }

    public RoomName getWhere() {
        return where;
    }
}
