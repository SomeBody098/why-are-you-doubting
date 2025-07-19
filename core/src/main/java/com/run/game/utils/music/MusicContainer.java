package com.run.game.utils.music;

import com.run.game.utils.param.Param;

public class MusicContainer implements Param {

    private final String name;
    private final String path;
    private final float volume;
    private final boolean looping;

    public MusicContainer(String name, String path, float volume, boolean looping) {
        this.name = name;
        this.path = path;
        this.volume = volume;
        this.looping = looping;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isLooping() {
        return looping;
    }

}
