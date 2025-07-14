package com.run.game.map;

import com.badlogic.gdx.Gdx;

public enum RoomName {
    BATHROOM("BATHROOM"),
    BEDROOM1("BEDROOM1"),
    BEDROOM2("BEDROOM2"),
    CORRIDOR_DOWN("CORRIDOR_DOWN"),
    CORRIDOR_UP("CORRIDOR_UP"),
    DINNING_ROOM("DINNING_ROOM"),
    REST_ROOM("REST_ROOM"),
    START_ROOM("START_ROOM"),
    WINDOW_ROOM("WINDOW_ROOM"),
    NONE("NONE");

    private final String value;

    RoomName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RoomName getRoomNameByString(String string){
        Gdx.app.log("str", string);
        switch (string.toUpperCase()){
            case "BATHROOM":
                return BATHROOM;
            case "BEDROOM1":
                return BEDROOM1;
            case "BEDROOM2":
                return BEDROOM2;
            case "CORRIDOR_DOWN":
                return CORRIDOR_DOWN;
            case "CORRIDOR_UP":
                return CORRIDOR_UP;
            case "DINNING_ROOM":
                return DINNING_ROOM;
            case "REST_ROOM":
                return REST_ROOM;
            case "START_ROOM":
                return START_ROOM;
            case "NONE":
                return NONE;
            default:
                throw new IllegalArgumentException("Unknown name room!");
        }
    }
}
