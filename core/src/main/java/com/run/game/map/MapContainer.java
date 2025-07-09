package com.run.game.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

public class MapContainer implements Disposable {

    private final TiledMap map;

    private final float mapPixelWidth;
    private final float mapPixelHeight;

    public MapContainer(TiledMap map) {
        this.map = map;

        int mapWidthInTiles = map.getProperties().get("width", Integer.class);  // width
        int mapHeightInTiles = map.getProperties().get("height", Integer.class); // height

        int tileWidth = map.getProperties().get("tilewidth", Integer.class);    // width tiles
        int tileHeight = map.getProperties().get("tileheight", Integer.class);  // height tiles

        // Final size:
        mapPixelWidth = mapWidthInTiles * tileWidth;
        mapPixelHeight = mapHeightInTiles * tileHeight;
    }

    public TiledMap getMap() {
        return map;
    }

    public float getMapPixelWidth() {
        return mapPixelWidth;
    }

    public float getMapPixelHeight() {
        return mapPixelHeight;
    }

    public void dispose(){
        map.dispose();
    }
}
