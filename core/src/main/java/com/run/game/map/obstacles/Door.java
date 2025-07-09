package com.run.game.map.obstacles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.run.game.dto.EmptyDto;
import com.run.game.entity.BodyFactory;
import com.run.game.map.Interactable;

public class Door implements Interactable {

    private final Vector2 position;
    private final Body body;

    public Door(float x, float y, float wight, float height, World world) {
        position = new Vector2(x, y);
        body = BodyFactory.createPolygonBody(
            BodyFactory.BODY_TYPE.STATIC,
            true,
            true,
            x, y,
            wight, height,
            world,
            new EmptyDto("door")
        );
    }

    @Override
    public void interacted() {

    }

    @Override
    public boolean isTouched() {
        return false;
    }

    @Override
    public boolean isActivate() {
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

}
