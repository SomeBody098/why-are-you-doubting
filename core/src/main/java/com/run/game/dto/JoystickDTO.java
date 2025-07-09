package com.run.game.dto;

import com.run.game.entity.DIRECTION;

public class JoystickDTO extends Dto {

    private float norPositionStickX, norPositionStickY;

    private boolean joystickActive;

    private DIRECTION joystickDirection;

    public JoystickDTO(String name) {
        super(name);
    }

    public float getNorPositionStickX() {
        return norPositionStickX;
    }

    public void setNorPositionStickX(float norPositionStickX) {
        this.norPositionStickX = norPositionStickX;
    }

    public float getNorPositionStickY() {
        return norPositionStickY;
    }

    public void setNorPositionStickY(float norPositionStickY) {
        this.norPositionStickY = norPositionStickY;
    }

    public boolean isJoystickActive() {
        return joystickActive;
    }

    public void setJoystickActive(boolean joystickActive) {
        this.joystickActive = joystickActive;
    }

    public DIRECTION getJoystickDirection() {
        return joystickDirection;
    }

    public void setJoystickDirection(DIRECTION joystickDirection) {
        this.joystickDirection = joystickDirection;
    }
}
