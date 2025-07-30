package com.run.game.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.utils.exception.UnexpectedBehaviorException;

import java.util.HashMap;

public class MapContainer implements Disposable {

    public final float PPM;
    public final float UNIT_SCALE;
    public final float MAP_WIDTH_PIXELS;
    public final float MAP_HEIGHT_PIXELS;
    public final float MAP_WIDTH_METERS;
    public final float MAP_HEIGHT_METERS;

    private final TiledMap map;

    private final HashMap<RoomName, Rectangle> rooms;

    public MapContainer(TiledMap map) {
        this.map = map;
        rooms = new HashMap<>();
        designRooms();

        MAP_WIDTH_METERS = map.getProperties().get("width", Integer.class);  // width
        MAP_HEIGHT_METERS = map.getProperties().get("height", Integer.class); // height

        int tileWidth = map.getProperties().get("tilewidth", Integer.class);    // width tiles
        int tileHeight = map.getProperties().get("tileheight", Integer.class);  // height tiles

        if (!(tileWidth == tileHeight)) throw new IllegalArgumentException("Width and height tile is different.");
        PPM = tileWidth;
        UNIT_SCALE = 1f / PPM;

        // Final size:
        MAP_WIDTH_PIXELS = MAP_WIDTH_METERS * PPM;
        MAP_HEIGHT_PIXELS = MAP_HEIGHT_METERS * PPM;
    }

    private void designRooms(){
        for (MapObject object: getMapObjects("rooms")) {
            RectangleMapObject border = ((RectangleMapObject) object);
            rooms.put(
                RoomName.getRoomNameByString(border.getName()),
                border.getRectangle()
            );
        }
    }

    public <T> T getObject(String name, Class<T> clazz){
        MapProperties properties = getMapObjects("objects").get(name).getProperties();
        switch (clazz.getSimpleName()){
            case "Vector2":
                return (T) new Vector2(                     // это дерьмо, но - рабочее! )))
                    properties.get("x", Float.class),
                    properties.get("y", Float.class)
                );
        }

        throw new IllegalArgumentException(String.format("\"%s\" object not contains \"%s\"", name, clazz.getSimpleName()));
    }

    public Vector2 getSpawnPlayerEnteredMoving(RoomName where, RoomName fromWhere){
        for (MapObject object: getMapObjects("objects")){
            String name = object.getName();
            if (name == null || !name.equals("spawn-player-entered-moving")) continue;

            MapProperties properties = object.getProperties();
            if (!properties.get("from_where", String.class).equals(fromWhere.name()) ||
                !properties.get("where", String.class).equals(where.name())) continue;

            return new Vector2(
                properties.get("x", Float.class) * UNIT_SCALE,
                properties.get("y", Float.class) * UNIT_SCALE
            );
        }

        throw new UnexpectedBehaviorException("Not found \"spawn-player-entered-door\" with property \"from_where\" " + fromWhere.name());
    }

    private MapObjects getMapObjects(String name){
        MapLayer layer = map.getLayers().get(name);
        if (layer == null) throw new IllegalArgumentException("Layer \"rooms\" - not exist!");

        return layer.getObjects();
    }

    public TiledMap getMap() {
        return map;
    }

    public Rectangle getBorderRoom(RoomName name){
        return rooms.get(name);
    }

    public void dispose(){
        map.dispose();
    }
}
