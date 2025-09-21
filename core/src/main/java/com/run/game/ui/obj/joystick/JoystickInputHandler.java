package com.run.game.ui.obj.joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Pool;
import com.run.game.utils.music.MusicManager;

public class JoystickInputHandler extends InputListener {

    private final MusicManager musicManager;

    private final Vector2 position;

    private final Vector2 finalPosition;

    private final Pool<Vector2> vectorPool;

    private int pointer = -1;

    private float radius;
    private boolean isActive = false;

    private final boolean isDesktop;

    public JoystickInputHandler(Vector2 position, Vector2 finalPosition, Pool<Vector2> vectorPool, MusicManager musicManager, float radius,  boolean isDesktop) {
        this.position = position;
        this.finalPosition = finalPosition;
        this.vectorPool = vectorPool;
        this.musicManager = musicManager;
        this.radius = radius;
        this.isDesktop = isDesktop;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (isTouchInJoystickArea(x, y) && JoystickInputHandler.this.pointer == -1) {
            initSound();

            isActive = true;
            JoystickInputHandler.this.pointer = pointer;
            position.set(x, y);
            return true;
        }

        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (JoystickInputHandler.this.pointer == pointer) {
            stopSound();
            resetJoystick();
            return;
        }

        super.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (pointer == JoystickInputHandler.this.pointer && isActive) {
            Vector2 tempPosition = vectorPool.obtain();

            tempPosition.set(x - finalPosition.x, y - finalPosition.y);

            if (tempPosition.len() > radius) {
                tempPosition.nor().scl(radius);
            }

            position.set(finalPosition).add(tempPosition);

            vectorPool.free(tempPosition);

            return;
        }

        super.touchDragged(event, x, y, pointer);
    }

    private boolean isTouchInJoystickArea(float screenX, float screenY) {
        Vector2 tempPosition = vectorPool.obtain();
        tempPosition.set(screenX, screenY);

        boolean isTouchInJoystickArea = tempPosition.dst(finalPosition) <= radius;

        vectorPool.free(tempPosition);

        return isTouchInJoystickArea;
    }

    private void resetJoystick() {
        position.set(finalPosition);
        isActive = false;
        pointer = -1;
    }

    @Override
    public boolean handle(Event e) {
        if (isDesktop) {
            float x = 0;
            float y = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                y = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                y = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                x = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                x = -1;

            if (x != 0 || y != 0) {
                Vector2 direction = new Vector2(x, y).nor().scl(radius);
                position.set(finalPosition).add(direction);
                isActive = true;
                initSound();

                return true;
            } else {
                isActive = false;
                position.set(finalPosition);
                stopSound();
            }
        }

        return super.handle(e);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    private void initSound(){
        musicManager.initSound("home", "walk");
    }

    private void stopSound(){
        if (musicManager.isSoundPlaying("walk")) {
            musicManager.stopSound("walk");
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
