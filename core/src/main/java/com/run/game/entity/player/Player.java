package com.run.game.entity.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.run.game.dto.JoystickDTO;
import com.run.game.entity.Entity;

public class Player extends Actor implements Entity {

    private final PlayerBody body;
    private final PlayerInputHandler inputHandler;
    private final PlayerGraphics graphics;

    private final JoystickDTO joystickDTO;

    public Player(PlayerBody body, PlayerInputHandler inputHandler, PlayerGraphics graphics, JoystickDTO joystickDTO) {
        this.body = body;
        this.inputHandler = inputHandler;
        this.graphics = graphics;
        this.joystickDTO = joystickDTO;
    }

    @Override
    public void act(float delta) {
        updateBody(joystickDTO);
        updateGraphics(delta);

        super.act(delta);
    }

    private void updateBody(JoystickDTO joystickDTO) {
        Vector2 newPosition = inputHandler.handleInput(
            joystickDTO,
            body.getPosition(),
            PlayerBody.SPEED
        );

        body.updatePosition(newPosition);
        body.updateDirection(inputHandler.getDirection());
    }

    private void updateGraphics(float delta){
        graphics.setDirection(body.getDirection());
        graphics.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        graphics.draw(batch, body.getPosition(), body.getWidth(), body.getHeight());
    }

    public float getWightTexture(){
        return graphics.getWidth();
    }

    public float getHeightTexture(){
        return graphics.getHeight();
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public void dispose() {
        graphics.dispose();
    }

    @Override
    public String getName(){
        return body.getName();
    }
}
