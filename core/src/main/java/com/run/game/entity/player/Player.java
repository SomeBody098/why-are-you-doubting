package com.run.game.entity.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.run.game.dto.JoystickDTO;
import com.run.game.dto.EmptyDto;
import com.run.game.entity.Entity;

public class Player implements Entity {

    private final PlayerBody body;
    private final PlayerInputHandler inputHandler;
    private final EmptyDto dto;
    private final PlayerGraphics graphics;

    public Player(float x, float y, float wight, float height, World world) {
        graphics = new PlayerGraphics();
        inputHandler = new PlayerInputHandler();

        dto = new EmptyDto("player");

        body = new PlayerBody(x, y, wight, height, world, dto);
    }

    public void updateBody(JoystickDTO joystickDTO) {
        Vector2 newPosition = inputHandler.handleInput(
            joystickDTO,
            body.getPosition(),
            PlayerBody.SPEED
        );

        body.updatePosition(newPosition);
        body.updateDirection(inputHandler.getDirection());
    }

    public void updateGraphics(float delta){
        graphics.setDirection(body.getDirection());
        graphics.update(delta);
    }

    public void draw(Batch batch) {
        graphics.draw(batch, body.getPosition(), body.getWidth(), body.getHeight());
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
