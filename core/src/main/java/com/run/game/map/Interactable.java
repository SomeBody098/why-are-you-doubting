package com.run.game.map;

public interface Interactable extends TileObject{
    void interacted();
    boolean isTouched();
    boolean isActivate();
}
