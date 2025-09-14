package com.run.game.component.walking;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.run.game.DIRECTION;

import map.creator.map.component.body.BodyComponent;

public class WalkingBodyComponent extends BodyComponent {

    private final float speed;
    private boolean isWalk = false;
    private DIRECTION direction = DIRECTION.NONE;

    public WalkingBodyComponent(Body body, String owner, float speed) {
        super(body, owner);
        this.speed = speed;
    }

    public void updatePosition(Vector2 newPosition){
        getBody().setTransform(newPosition, 0);
    }

    public Vector2 getPosition(){
        return getBody().getPosition();
    }

    public boolean isWalk() {
        return isWalk;
    }

    public void setWalk(boolean walk) {
        isWalk = walk;
    }

    public void setDirection(DIRECTION direction){
        this.direction = direction;
    }
    public DIRECTION getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

}
