package com.run.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class MapController implements Disposable {

    private final MapContainer map;
    private final OrthogonalTiledMapRenderer renderer;

    private final OrthographicCamera gameCamera;

    private RoomName currentRoom;

    public MapController(MapContainer map, OrthographicCamera gameCamera, Batch batch) {
        this.map = map;
        this.gameCamera = gameCamera;

        renderer = new OrthogonalTiledMapRenderer(map.getMap(), map.UNIT_SCALE, batch);
        renderer.setView(gameCamera);

        currentRoom = RoomName.START_ROOM;
    }

    public void update(RoomName roomName){
        currentRoom = roomName;
        Rectangle border = map.getBorderRoom(roomName);

        gameCamera.setToOrtho(
            false,
            border.width * map.UNIT_SCALE,
            border.height * map.UNIT_SCALE
        );
        gameCamera.position.set(
            (border.x + (border.width / 2)) * map.UNIT_SCALE,
            (border.y + (border.height / 2)) * map.UNIT_SCALE,
            0
        );
        gameCamera.update();

        renderer.setView(gameCamera);
    }

    public void render(){
        renderer.render();
    }

    public MapContainer getMap() {
        return map;
    }

    public RoomName getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        map.dispose();
    }
}
