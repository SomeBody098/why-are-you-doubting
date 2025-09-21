package com.run.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.run.game.component.input.PlayerInputHandlerComponent;
import com.run.game.component.walking.WalkingBodyComponent;

public class WalkingSystem extends IteratingSystem {

    public WalkingSystem() {
        super(Family.all(PlayerInputHandlerComponent.class, WalkingBodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        PlayerInputHandlerComponent inputHandler = entity.getComponent(PlayerInputHandlerComponent.class);
        WalkingBodyComponent body = entity.getComponent(WalkingBodyComponent.class);
        Vector2 pastPosition = body.getPosition().cpy();

        Vector2 newPosition = inputHandler.handleInput(
            pastPosition,
            body.getSpeed()
        );

        boolean isWalk = !body.getPosition().toString().equals(newPosition.toString());
        body.setWalk(isWalk);

        body.updatePosition(newPosition);
        body.setDirection(inputHandler.getDirection());
    }

}
