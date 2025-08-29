package com.run.game.component.input;

import com.badlogic.gdx.math.Vector2;
import com.run.game.dto.JoystickDTO;
import com.run.game.DIRECTION;

import map.creator.map.component.ObjectComponent;

public class PlayerInputHandlerComponent extends ObjectComponent {

    private final JoystickDTO dto;

    private DIRECTION direction = DIRECTION.NONE;

    public PlayerInputHandlerComponent(String owner, JoystickDTO dto) {
        super("player-input-handler", "input", owner);
        this.dto = dto;
    }

    public Vector2 handleInput(Vector2 position, float speed) {
        if (dto.isJoystickActive()) {
            direction = dto.getJoystickDirection();

            position.add(
                dto.getNorPositionStickX() * speed,
                dto.getNorPositionStickY() * speed
            );
        }

        return position;
    }

    public DIRECTION getDirection() {
        return direction;
    }

}
