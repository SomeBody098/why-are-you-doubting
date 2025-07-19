package com.run.game.utils.music;

import java.util.HashMap;

public class MusicStorage {

    private final HashMap<String, MusicContainer> musicContainers;
    private final HashMap<String, MusicContainer> soundContainers;

    public MusicStorage(HashMap<String, MusicContainer> musicContainers, HashMap<String, MusicContainer> soundContainers) {
        this.musicContainers = musicContainers;
        this.soundContainers = soundContainers;
    }

    public MusicContainer getMusicContainer(String name){
        MusicContainer container = musicContainers.get(name);
        if (container == null) throw new NullPointerException("Container on name " + name + " - not exist!");

        return container;
    }

    public MusicContainer getSoundContainer(String name){
        MusicContainer container = soundContainers.get(name);
        if (container == null) throw new NullPointerException("Container on name " + name + " - not exist!");

        return container;
    }

}
