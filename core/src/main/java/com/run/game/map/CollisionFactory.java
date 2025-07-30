package com.run.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.run.game.dto.MovingDto;
import com.run.game.dto.EmptyDto;
import com.run.game.entity.BodyFactory;

import java.util.Optional;

public class CollisionFactory {

    public static void createCollisionForTiledObjects(RoomName currentRoom, MapContainer map, World world){
        Optional<MapLayer> objectLayer = Optional.of(map.getMap().getLayers().get("objects"));
        MapObjects objects = objectLayer.get().getObjects();

        Rectangle border = map.getBorderRoom(currentRoom);

        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                float divW = (bounds.width / 2) * map.UNIT_SCALE;
                float divH = (bounds.height / 2) * map.UNIT_SCALE;

                boolean isActive = border.overlaps(bounds);

                String className = object.getProperties().get("type", String.class);

                Gdx.app.log("class", className);

                if (className == null) {
                    Gdx.app.log("ob", bounds.toString());
                    createObstacle(
                        bounds.x * map.UNIT_SCALE + divW,
                        bounds.y * map.UNIT_SCALE + divH,
                        bounds.width,
                        bounds.height,
                        map.UNIT_SCALE,
                        world,
                        isActive
                    );
                    continue;
                }

                MapProperties properties = object.getProperties();

                switch (className){
                    case "moving":
                        createMoving(
                            bounds.x * map.UNIT_SCALE + divW,
                            bounds.y * map.UNIT_SCALE + divH,
                            bounds.width,
                            bounds.height,
                            map.UNIT_SCALE,
                            properties.get("where", String.class),
                            world,
                            isActive
                        );
                        continue;
                }
            }
        }
    }

    private static void createObstacle(float x, float y, float width, float height, float unitScale, World world, boolean isActive){
        Body body = BodyFactory.createPolygonBody(
            BodyFactory.BODY_TYPE.STATIC,
            true,
            false,
            x, y,
            width, height,
            unitScale,
            world,
            new EmptyDto("obstacle")
        );

        body.setActive(true);
    }

    private static void createMoving(float x, float y, float width, float height, float unitScale, String where, World world, boolean isActive){
        Body body = BodyFactory.createPolygonBody(
            BodyFactory.BODY_TYPE.STATIC,
            true,
            true,
            x, y,
            width, height,
            unitScale,
            world,
            new MovingDto("moving", RoomName.getRoomNameByString(where))
        );

        body.setActive(true);
    }
}
