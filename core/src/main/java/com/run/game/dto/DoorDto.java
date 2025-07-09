package com.run.game.dto;

public class DoorDto extends Dto{

    private boolean isPlayerEnters = false;
    private boolean isBeenEnters = false;

    public DoorDto(String name) {
        super(name);
    }

    public boolean isPlayerEnters() {
        return isPlayerEnters;
    }

    public void setPlayerEnters(boolean playerEnters) {
        isPlayerEnters = playerEnters;
    }

    public boolean isBeenEnters() {
        return isBeenEnters;
    }

    public void setBeenEnters(boolean beenEnters) {
        isBeenEnters = beenEnters;
    }
}
