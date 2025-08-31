package com.run.game.component.navigation;

import com.badlogic.gdx.math.Vector2;

import map.creator.map.component.ObjectComponent;

public class TeleportComponent extends ObjectComponent {

    private final Vector2 teleportPosition;
    private final int countNotes;

    public TeleportComponent(String owner, Vector2 teleportPosition, int countNotes) {
        super("teleport", "navigation", owner);
        this.teleportPosition = teleportPosition;
        this.countNotes = countNotes;
    }

    public Vector2 getTeleportPosition() {
        return teleportPosition;
    }

    public int getCountNotes() {
        return countNotes;
    }
}
