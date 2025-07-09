package com.run.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;
import java.util.Map;

public class MapFactory {

    private static HashMap<WorldName, HashMap<RoomName, String>> PATHS_TO_ASSETS;
    private static AssetManager manager;
    private static World world;

    public static void init(World world){
        MapFactory.world = world;
        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        PATHS_TO_ASSETS = new HashMap<>();

        initPathsToAssetNecrophobia();
    }

    public static boolean isDone(){
        if (manager.update()){
            manager.finishLoading();
            return true;
        }

        return false;
    }

    public static float getProgress(){
        return manager.getProgress();
    }

    private static void initPathsToAssetNecrophobia(){ // FIXME: 24.06.2025 перенеси пути в json и из файла и бери эти пути
        HashMap<RoomName, String> home = new HashMap<>();

        home.put(RoomName.BATHROOM,"maps/bathroom.tmx");
        home.put(RoomName.BEDROOM1,"maps/bedroom1.tmx");
        home.put(RoomName.BEDROOM2,"maps/bedroom2.tmx");
        home.put(RoomName.CORRIDOR_DOWN,"maps/corridor_down.tmx");
        home.put(RoomName.CORRIDOR_UP,"maps/corridor_up.tmx");
        home.put(RoomName.DINNING_ROOM,"maps/dinning_room.tmx");
        home.put(RoomName.REST_ROOM,"maps/rest_room.tmx");
        home.put(RoomName.START_ROOM,"maps/start_room.tmx");
        home.put(RoomName.WINDOW_ROOM,"maps/window_room.tmx");

        PATHS_TO_ASSETS.put(WorldName.HOME, home);
    }

    public static HashMap<RoomName, MapContainer> createMap(WorldName name){
        HashMap<RoomName, MapContainer> map = new HashMap<>();
        HashMap<RoomName, String> rooms = PATHS_TO_ASSETS.get(name);

        if (rooms == null) throw new IllegalArgumentException("World on name " + name + " - not exist");

        for (Map.Entry<RoomName, String> path : rooms.entrySet()) {
            RoomName key = path.getKey();
            TiledMap tiledMap = manager.get(path.getValue(), TiledMap.class);

            map.put(key, new MapContainer(tiledMap));

            CollisionFactory.createBodyForTiledObjects(tiledMap, world);
        }

        return map;
    }

    public static boolean isLoadTextureWorld(WorldName worldName, RoomName roomName){
        return manager.isLoaded(PATHS_TO_ASSETS.get(worldName).get(roomName));
    }

    public static boolean isLoadTextureHomeWorld(){
        return isLoadTextureWorld(WorldName.HOME, RoomName.START_ROOM);
    }

    private static boolean checkingTexturesLoading(WorldName name){
        switch (name){
            case HOME:
                return isLoadTextureHomeWorld(); // if true - success, Textures been loaded
        }

        return false; // fail - Textures was not loading
    }

    public static void loadTextureWorld(WorldName name){
        if (checkingTexturesLoading(name)) return;

        HashMap<RoomName, String> rooms = PATHS_TO_ASSETS.get(name);

        for (Map.Entry<RoomName, String> path : rooms.entrySet()) {
            manager.load(path.getValue(), TiledMap.class);
        }

    }

    public static void dispose(){
        manager.dispose();
    }
}
