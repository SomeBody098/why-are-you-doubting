package com.run.game.map.obstacles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.run.game.dto.EmptyDto;
import com.run.game.entity.BodyFactory;

public class Obstacle implements Obstacles {

    private final Vector2 position;
    private final Body body;

    public Obstacle(float x, float y, float wight, float height, World world) {
        position = new Vector2(x, y);
        body = BodyFactory.createPolygonBody(
            BodyFactory.BODY_TYPE.STATIC,
            true,
            false,
            x, y,
            wight, height,
            world,
            new EmptyDto(getNameObstacles())
        );
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String getNameObstacles() {
        return "obstacle";
    }
}
