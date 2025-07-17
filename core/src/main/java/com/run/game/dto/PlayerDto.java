package com.run.game.dto;

import com.run.game.map.RoomName;

public class PlayerDto extends Dto{

    private RoomName currentRoom = RoomName.START_ROOM;

    public PlayerDto() {
        super("player");
    }

    public RoomName getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(RoomName currentRoom) {
        this.currentRoom = currentRoom;
    }
}
