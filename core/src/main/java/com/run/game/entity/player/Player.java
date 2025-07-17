package com.run.game.entity.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.dto.JoystickDTO;
import com.run.game.dto.PlayerDto;
import com.run.game.entity.Entity;
import com.run.game.map.RoomName;

public class Player implements Entity, Disposable {

    private final float ppm;
    private final float unitScale;

    private final PlayerBody body;
    private final PlayerDto dto;
    private final PlayerInputHandler inputHandler;
    private final PlayerGraphics graphics;

    private final JoystickDTO joystickDTO;

    public Player(PlayerBody body, PlayerDto dto, PlayerInputHandler inputHandler, PlayerGraphics graphics, JoystickDTO joystickDTO, float ppm, float unitScale) {
        this.body = body;
        this.dto = dto;
        this.inputHandler = inputHandler;
        this.graphics = graphics;
        this.joystickDTO = joystickDTO;
        this.ppm = ppm;
        this.unitScale = unitScale;
    }

    public void update(float delta, RoomName currentRoom) {
        updateBody(joystickDTO);
        updateGraphics(delta);
        dto.setCurrentRoom(currentRoom);
    }

    private void updateBody(JoystickDTO joystickDTO) {
        Vector2 newPosition = inputHandler.handleInput(
            joystickDTO,
            body.getPosition(),
            body.getSpeed()
        );

        body.updatePosition(newPosition);
        body.updateDirection(inputHandler.getDirection());
    }

    private void updateGraphics(float delta){
        graphics.setDirection(body.getDirection());
        graphics.update(delta);
    }

    public void draw(Batch batch) {
        graphics.draw(batch, body.getPosition(), body.getWidth(), body.getHeight(), ppm, unitScale);
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

    public void setPosition(Vector2 position){
        body.updatePosition(position);
    }

    @Override
    public void dispose() {
        graphics.dispose();
    }

    @Override
    public String getName(){
        return body.getName();
    }
}
