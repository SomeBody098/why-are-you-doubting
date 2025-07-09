package com.run.game.ui.joystick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.run.game.dto.JoystickDTO;
import com.run.game.utils.exception.NotInitializedObjectException;

public class Joystick extends Actor {

    private final JoystickGraphics graphics;

    private JoystickBody body;

    private JoystickInputHandler inputHandler;

    private final JoystickDTO dto;

    public Joystick() {
        graphics = new JoystickGraphics();
        dto = new JoystickDTO("joystick");
    }

    public void createBounds(float x, float y, float radius){
        body = new JoystickBody(x, y, radius);
        inputHandler = new JoystickInputHandler(
            body.getPositionStick().cpy(),
            body.getPositionCircle().cpy(),
            body.getVectorPool(),
            body.getRadius()
        );

        addListener(inputHandler);
        setBounds(x, y, radius, radius);
    }

    @Override
    public void draw(Batch uiBatch, float parentAlpha) {
        isBodyCreated();
        super.draw(uiBatch, parentAlpha);

        graphics.draw(
            uiBatch, getColor(),
            body.getPositionCircle(),
            body.getPositionStick(),
            body.getRadius(),
            parentAlpha
        );
    }

    @Override
    public void act(float delta) {
        isBodyCreated();
        super.act(delta);

        body.setPositionStick(inputHandler.getPosition());
        body.act(inputHandler.isActive());

        updateDto();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        isBodyCreated();
        return inputHandler.isActive() ? this : null;
    }

    private void updateDto(){
        dto.setJoystickActive(inputHandler.isActive());
        dto.setJoystickDirection(body.getDirection());
        dto.setNorPositionStickX(body.getNorPositionStickX());
        dto.setNorPositionStickY(body.getNorPositionStickY());
    }

    public float getWightCircle(){
        return graphics.getWightCircle();
    }

    public float getHeightCircle(){
        return graphics.getHeightCircle();
    }

    public float getWightStick(){
        return graphics.getWightStick();
    }

    public float getHeightStick(){
        return graphics.getHeightStick();
    }

    public JoystickDTO getDto(){
        return dto;
    }

    public JoystickInputHandler getInputHandler() {
        return inputHandler;
    }

    private void isBodyCreated(){
        if (body == null) throw new NotInitializedObjectException("Joystick body is not created!");
    }

    public void dispose(){
        graphics.dispose();
        body.dispose();
    }
}
