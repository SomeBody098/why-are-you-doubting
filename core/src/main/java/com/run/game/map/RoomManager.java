package com.run.game.map;

import com.run.game.entity.player.Player;

public class RoomManager {

    private final MapController mapController;
    private final Player player;

    public RoomManager(MapController mapController, Player player) {
        this.mapController = mapController;
        this.player = player;
    }

    public void render(){
        mapController.render();
    }

    public void changeRoom(RoomName where, RoomName fromWhere) {
        mapController.update(where);
        teleportPlayerToRoom(where, fromWhere);
    }

    private void teleportPlayerToRoom(RoomName where, RoomName fromWhere) {
        player.setPosition(mapController.getMap().getSpawnPlayerEnteredMoving(where, fromWhere));
    }

    public RoomName getCurrentRoom(){
        return mapController.getCurrentRoom();
    }
}
