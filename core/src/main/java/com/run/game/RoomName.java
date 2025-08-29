package com.run.game;

public enum RoomName {
    BATHROOM,
    BEDROOM1,
    BEDROOM2,
    CORRIDOR_DOWN,
    CORRIDOR_UP,
    DINNING_ROOM,
    REST_ROOM,
    START_ROOM,
    WINDOW_ROOM,
    STORAGE_ROOM,
    NONE;

    public static RoomName getRoomNameByString(String string){
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
            case "WINDOW_ROOM":
                return WINDOW_ROOM;
            case "STORAGE_ROOM":
                return STORAGE_ROOM;
            case "NONE":
                return NONE;
            default:
                throw new IllegalArgumentException("Unknown name room!");
        }
    }

    public static boolean stringIsContainsRoomName(String string){
        switch (string.toUpperCase()){
            case "BATHROOM":
            case "BEDROOM1":
            case "BEDROOM2":
            case "CORRIDOR_DOWN":
            case "CORRIDOR_UP":
            case "DINNING_ROOM":
            case "REST_ROOM":
            case "START_ROOM":
            case "WINDOW_ROOM":
            case "STORAGE_ROOM":
            case "NONE":
                return true;
            default:
                return false;
        }
    }
}
