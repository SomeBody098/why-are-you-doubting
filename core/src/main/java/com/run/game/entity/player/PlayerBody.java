package com.run.game.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.run.game.Main;
import com.run.game.dto.EmptyDto;
import com.run.game.entity.BodyFactory;
import com.run.game.entity.DIRECTION;
import com.run.game.entity.Entity;

public class PlayerBody implements Entity {

    private final float speed;

    private final Body body;

    private final float width, height;

    private DIRECTION direction = DIRECTION.NONE;

    public PlayerBody(float x, float y, float wight, float height, float unitScale, World world, EmptyDto playerDTO) {
        body = BodyFactory.createPolygonBody(
            BodyFactory.BODY_TYPE.DYNAMIC,
            true,
            false,
            x, y,
            wight / 2,
            height / 2,
            unitScale,
            world,
            playerDTO
        );

        this.width = wight;
        this.height = height;

        speed = unitScale;
    }

    public void updatePosition(Vector2 newPosition){
        body.setTransform(newPosition, 0);
    }

    public void updateDirection(DIRECTION direction){
        this.direction = direction;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public String getName() {
        return "player";
    }
}
