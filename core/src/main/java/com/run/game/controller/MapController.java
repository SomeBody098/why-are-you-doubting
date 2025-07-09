package com.run.game.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.Main;
import com.run.game.map.MapContainer;
import com.run.game.map.RoomName;

import java.util.HashMap;

public class MapController implements Disposable {

    private final Batch batch;
    private final HashMap<RoomName, MapContainer> map;
    private final OrthogonalTiledMapRenderer renderer;

    private RoomName currentRoom;

    public MapController(HashMap<RoomName, MapContainer> map, OrthographicCamera gameCamera, Batch batch) {
        this.map = map;
        this.batch = batch;
        renderer = new OrthogonalTiledMapRenderer(map.get(RoomName.START_ROOM).getMap(), Main.UNIT_SCALE, batch);
        currentRoom = RoomName.START_ROOM;

        renderer.setView(gameCamera);
    }

    public void update(RoomName roomName){
        currentRoom = roomName;
        renderer.setMap(map.get(roomName).getMap());
    }

    public void render(){
        renderer.render();
    }

    public RoomName getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        map.forEach((key, value) -> value.dispose());
    }
}
