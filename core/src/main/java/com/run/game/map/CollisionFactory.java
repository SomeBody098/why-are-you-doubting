package com.run.game.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.run.game.Main;
import com.run.game.map.obstacles.Door;
import com.run.game.map.obstacles.Obstacle;

public class CollisionFactory {

    public static void createBodyForTiledObjects(TiledMap map, World world){ // FIXME: 09.07.2025 коллизии инициализируються неправильно!
        ObjectMap<String, TileObject> items = new ObjectMap<>();
        TiledMapTileLayer itemsLayer = (TiledMapTileLayer) map.getLayers().get("items");

        float toCenterX = (float) itemsLayer.getTileWidth() / 2 * Main.UNIT_SCALE;
        float toCenterY = (float) itemsLayer.getTileHeight() / 2 * Main.UNIT_SCALE;

        for (int y = 0; y < itemsLayer.getHeight(); y++) {
            for (int x = 0; x < itemsLayer.getWidth(); x++) {

                TiledMapTileLayer.Cell cell = itemsLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    String name = cell.getTile().getProperties().get("type", String.class);

                    if (name != null) {
                        switch (name) {
                            case "obstacle":
                                items.put("obstacle",
                                    createObstacle(
                                        x + toCenterX,
                                        y + toCenterY,
                                        world
                                    )
                                );
                                break;
                            case "door":
                                items.put("door",
                                    createDoor(
                                        x + toCenterX,
                                        y + toCenterY,
                                        world
                                    )
                                );
                                break;
                        }
                    }
                }
            }
        }

    }

    private static Obstacle createObstacle(float x, float y, World world){
        return new Obstacle(
            x, y,
            Main.PPM,
            Main.PPM,
            world
        );
    }

    private static Door createDoor(float x, float y, World world){
        return new Door(
            x, y,
            Main.PPM,
            Main.PPM,
            world
        );
    }
}
