package com.run.game.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;

public class MapFactory {

    private static HashMap<WorldName, String> PATHS_TO_ASSETS;
    private static AssetManager manager;
    private static World world;

    public static void init(World world){
        MapFactory.world = world;
        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        PATHS_TO_ASSETS = new HashMap<>();

        initPathsToAsset();
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

    private static void initPathsToAsset(){ // FIXME: 24.06.2025 перенеси пути в json и из файла и бери эти пути
        PATHS_TO_ASSETS.put(WorldName.HOME, "maps/map.tmx");
    }

    public static MapContainer createMap(WorldName name){
        String rooms = PATHS_TO_ASSETS.get(name);
        if (rooms == null) throw new IllegalArgumentException("World on name " + name + " - not exist");

        MapContainer container = new MapContainer(manager.get(rooms, TiledMap.class));
        CollisionFactory.createCollisionForTiledObjects(RoomName.START_ROOM, container, world); // FIXME: 12.07.2025 Хардкод START_ROOM

        return container;
    }

    public static boolean isLoadTextureWorld(WorldName worldName){
        return manager.isLoaded(PATHS_TO_ASSETS.get(worldName));
    }

    private static boolean checkingTexturesLoading(WorldName name){
        return isLoadTextureWorld(name);
    }

    public static void loadTextureWorld(WorldName name){
        if (checkingTexturesLoading(name)) return;

        manager.load(PATHS_TO_ASSETS.get(name), TiledMap.class);
    }

    public static void dispose(){
        manager.dispose();
    }
}
