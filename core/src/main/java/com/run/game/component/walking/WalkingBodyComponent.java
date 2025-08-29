package com.run.game.component.walking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.run.game.DIRECTION;

import map.creator.map.component.body.BodyComponent;

public class WalkingBodyComponent extends BodyComponent {

    public final float speed;

    public DIRECTION direction = DIRECTION.NONE;

    public WalkingBodyComponent(Body body, String owner, float speed) {
        super(body, owner);
        this.speed = speed;
    }

    public void updatePosition(Vector2 newPosition){
        getBody().setTransform(newPosition, 0);
    }

    public void updateDirection(DIRECTION direction){
        this.direction = direction;
    }

    public Vector2 getPosition(){
        return getBody().getPosition();
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

}
