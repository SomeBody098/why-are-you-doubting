package com.run.game.utils.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.run.game.map.WorldName;

import java.util.HashMap;
import java.util.function.Consumer;

public class MusicManager {

    private static JsonValue value;

    private static HashMap<WorldName, MusicStorage> MUSIC_STORAGES;
    private static HashMap<String, Music> playingMusic;
    private static HashMap<String, Sound> playingSound;

    private static AssetManager manager;

    public static void init(){
        manager = new AssetManager();
        manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));

        MUSIC_STORAGES = new HashMap<>();
        playingMusic = new HashMap<>();
        playingSound = new HashMap<>();

        value = new JsonReader().parse(Gdx.files.internal("music/music_property.json"));
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

    public static void initMusic(WorldName worldName, String containerName){
        MusicStorage storage = MUSIC_STORAGES.get(worldName);
        MusicContainer container = storage.getMusicContainer(containerName);

        if (playingMusic.containsKey(container.getName())) {
            playMusic(container.getName());
            return;
        }

        Music music = manager.get(container.getPath(), Music.class);
        music.setVolume(container.getVolume());
        music.setLooping(container.isLooping());

        music.play();

        playingMusic.put(container.getName(), music);
    }

    public static void initSound(WorldName worldName, String containerName){
        MusicStorage storage = MUSIC_STORAGES.get(worldName);
        MusicContainer container = storage.getSoundContainer(containerName);

        if (playingSound.containsKey(container.getName())) {
            playSound(container.getName());
            return;
        }

        Sound sound = manager.get(container.getPath(), Sound.class);
        sound.setVolume(1, container.getVolume());
        sound.setLooping(1, container.isLooping());

        sound.play();

        playingSound.put(container.getName(), sound);
    }

    public static void playMusic(String name){
        handleMusic(name, Music::play);
    }

    public static void playSound(String name){
        handleSound(name, Sound::play);
    }

    public static void pauseMusic(String name){
        handleMusic(name, Music::pause);
    }

    public static void pauseSound(String name){
        handleSound(name, Sound::pause);
    }

    public static void stopMusic(String name){
        handleMusic(name, Music::stop);
    }

    public static void stopSound(String name){
        handleSound(name, Sound::stop);
    }

    public static void disposeMusic(String name){
        handleMusic(name, Music::dispose);
    }

    public static void disposeSound(String name){
        handleSound(name, Sound::dispose);
    }

    private static void handleMusic(String name, Consumer<Music> action) {
        if (!isMusicPlaying(name)) {
            Gdx.app.error("MusicManager", "Music not loaded: " + name);
            return;
        }
        action.accept(playingMusic.get(name));
    }

    private static void handleSound(String name, Consumer<Sound> action) {
        if (!isSoundPlaying(name)) {
            Gdx.app.error("MusicManager", "Sound not loaded: " + name);
            return;
        }
        action.accept(playingSound.get(name));
    }

    public static boolean isMusicPlaying(String name){
        return playingMusic.containsKey(name);
    }

    public static boolean isSoundPlaying(String name){
        return playingSound.containsKey(name);
    }

    public static void loadMusic(WorldName worldName){
        MUSIC_STORAGES.put(worldName, getMusicStorage(worldName));
    }

    private static MusicStorage getMusicStorage(WorldName worldName){
        JsonValue storage = value.get(worldName.name());
        HashMap<String, MusicContainer> musicContainers = getMusicContainers(storage.get("music"));
        HashMap<String, MusicContainer> soundContainers = getMusicContainers(storage.get("sound"));

        return new MusicStorage(musicContainers, soundContainers);
    }

    private static HashMap<String, MusicContainer> getMusicContainers(JsonValue music){
        HashMap<String, MusicContainer> musicContainers = new HashMap<>();
        if (music.isEmpty() || music.isNull()) return musicContainers;

        for (JsonValue child : music) {
            String name = child.name;
            String path = child.getString("path");

            giveManagerLoadMusic(music.name, path);

            MusicContainer container = new MusicContainer(
                name, path,
                child.getFloat("volume"),
                child.getBoolean("looping")
            );

            musicContainers.put(name, container);
        }

        return musicContainers;
    }

    private static void giveManagerLoadMusic(String name, String path){
        if (name.equals("music")) manager.load(path, Music.class);
        else manager.load(path, Sound.class);
    }

    public static void dispose(){
        manager.dispose();

        playingMusic.forEach((key, value1) -> value1.dispose());
        playingSound.forEach((key, value1) -> value1.dispose());
    }
}
