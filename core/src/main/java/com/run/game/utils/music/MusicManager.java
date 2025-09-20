package com.run.game.utils.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import map.creator.map.factory.AsynchronousFactory;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.function.Consumer;

public class MusicManager implements AsynchronousFactory, Disposable {

    private final JsonValue value;
    private final AssetManager manager;

    private final HashMap<String, MusicStorage> MUSIC_STORAGES;
    private final HashMap<String, Music> playingMusic;
    private final HashMap<String, Sound> playingSound;

    public MusicManager() {
        manager = new AssetManager();
        manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));

        MUSIC_STORAGES = new HashMap<>();
        playingMusic = new HashMap<>();
        playingSound = new HashMap<>();

        value = new JsonReader().parse(Gdx.files.internal("music/music_property.json"));
    }

    @Override
    public boolean isDone(){
        if (manager.update()){
            manager.finishLoading();
            return true;
        }

        return false;
    }

    @Override
    public float getProgress(){
        return manager.getProgress();
    }

    public void initMusic(String nameStorage, String containerName){
        MusicStorage storage = MUSIC_STORAGES.get(nameStorage);
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

    public void initSound(String nameStorage, String containerName){
        MusicStorage storage = MUSIC_STORAGES.get(nameStorage);
        MusicContainer container = storage.getSoundContainer(containerName);

        if (playingSound.containsKey(container.getName())) {
            playSound(container.getName());
            return;
        }

        Sound sound = manager.get(container.getPath(), Sound.class);
        long soundId = sound.play();

        sound.setVolume(soundId, container.getVolume());
        sound.setLooping(soundId, container.isLooping());

        playingSound.put(container.getName(), sound);
    }

    public void playMusic(String name){
        handleMusic(name, Music::play);
    }

    public void playSound(String name){
        handleSound(name, Sound::play);
    }

    public void pauseMusic(String name){
        handleMusic(name, Music::pause);
    }

    public void pauseSound(String name){
        handleSound(name, Sound::pause);
    }

    public void stopMusic(String name){
        handleMusic(name, Music::stop);
        playingMusic.remove(name);
    }

    public void stopSound(String name){
        handleSound(name, Sound::stop);
        playingSound.remove(name);
    }

    public void disposeMusic(String name){
        handleMusic(name, Music::dispose);
    }

    public void disposeSound(String name){
        handleSound(name, Sound::dispose);
    }

    private void handleMusic(String name, Consumer<Music> action) {
        if (!isMusicPlaying(name)) {
            Gdx.app.error("MusicManager", "Music not loaded: " + name);
            return;
        }
        action.accept(playingMusic.get(name));
    }

    private void handleSound(String name, Consumer<Sound> action) {
        if (!isSoundPlaying(name)) {
            Gdx.app.error("MusicManager", "Sound not loaded: " + name);
            return;
        }
        action.accept(playingSound.get(name));
    }

    public boolean isMusicPlaying(String name){
        return playingMusic.containsKey(name);
    }

    public boolean isSoundPlaying(String name){
        return playingSound.containsKey(name);
    }

    public boolean isMusicOrSoundInit(String name){
        return manager.isLoaded(name);
    }

    public void loadMusic(String nameStorage){
        MUSIC_STORAGES.put(nameStorage, getMusicStorage(nameStorage));
    }

    private MusicStorage getMusicStorage(String nameStorage){
        JsonValue storage = value.get(nameStorage);
        HashMap<String, MusicContainer> musicContainers = getMusicContainers(storage.get("music"));
        HashMap<String, MusicContainer> soundContainers = getMusicContainers(storage.get("sound"));

        return new MusicStorage(musicContainers, soundContainers);
    }

    private HashMap<String, MusicContainer> getMusicContainers(JsonValue music){
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

    private void giveManagerLoadMusic(String name, String path){
        if (name.equals("music")) manager.load(path, Music.class);
        else manager.load(path, Sound.class);
    }

    @Override
    public void dispose(){
        manager.dispose();

        playingMusic.forEach((key, value1) -> value1.dispose());
        playingSound.forEach((key, value1) -> value1.dispose());
        MUSIC_STORAGES.clear();
    }
}
