package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.run.game.DIRECTION;

public class AnimationPlayerMovingObjectComponent extends GraphicsObjectComponent{

    private final Animation<TextureRegion> idleLeft;
    private final Animation<TextureRegion> idleRight;
    private final Animation<TextureRegion> idleDown;
    private final Animation<TextureRegion> idleUp;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> walkDown;
    private final Animation<TextureRegion> walkUp;

    private Animation<TextureRegion> currentAnimation;

    private DIRECTION direction = DIRECTION.NONE;
    private float stateTime = 0;

    public AnimationPlayerMovingObjectComponent(String owner, TextureAtlas atlas, float unitScale, boolean isPlayer) {
        super(owner, atlas.findRegion("idleleft1"), unitScale, isPlayer);

        idleLeft = new Animation<>(0.5f, getAnimation(atlas,"idleleft"));
        idleRight = new Animation<>(0.5f, getAnimation(atlas,"idleright"));
        idleDown = new Animation<>(0.5f, getAnimation(atlas,"idledown"));
        idleUp = new Animation<>(0.5f, getAnimation(atlas,"idleup"));

        walkLeft = new Animation<>(0.2f, getAnimation(atlas,"walkleft"));
        walkRight = new Animation<>(0.2f, getAnimation(atlas,"walkright"));
        walkDown = new Animation<>(0.2f, getAnimation(atlas,"walkdown"));
        walkUp = new Animation<>(0.2f, getAnimation(atlas,"walkup"));
    }

    public void update(float delta, DIRECTION direction, boolean isWalk){
        setDirection(direction);
        handleDirectionFlipping(isWalk);

        stateTime += delta;
        setCurrentFrame(currentAnimation.getKeyFrame(stateTime, true));
    }

    private void handleDirectionFlipping(boolean isWalk) {
        switch (direction){
            case NONE:
            case LEFT:
                if (isWalk) currentAnimation = walkLeft;
                else currentAnimation = idleLeft;
                return;

            case UP:
                if (isWalk) currentAnimation = walkUp;
                else currentAnimation = idleUp;
                return;

            case DOWN:
                if (isWalk) currentAnimation = walkDown;
                else currentAnimation = idleDown;
                return;

            case RIGHT:
                if (isWalk) currentAnimation = walkRight;
                else currentAnimation = idleRight;
        }
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    private Array<TextureAtlas.AtlasRegion> getAnimation(TextureAtlas atlas, String name){
        Array<TextureAtlas.AtlasRegion> animation = new Array<>();
        TextureAtlas.AtlasRegion frame;
        int i = 1;
        while (true) {
            frame = atlas.findRegion(name + "_" + i);
            if (frame == null) break;

            animation.add(frame);
            i++;
        }


        return animation;
    }

}
