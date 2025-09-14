package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.run.game.DIRECTION;

public class GraphicsMovingObjectComponent extends GraphicsObjectComponent {

    private DIRECTION direction = DIRECTION.NONE;

    private final TextureRegion frameRight;
    private final TextureRegion frameLeft;

    public GraphicsMovingObjectComponent(String owner, TextureRegion currentFrameLeft, TextureRegion currentFrameRight, float unitScale, boolean isPlayer) {
        super(owner, currentFrameLeft, unitScale, isPlayer);

        frameLeft = currentFrameLeft;
        frameRight = currentFrameRight;
    }

    public void update(DIRECTION direction){
        setDirection(direction);
        handleDirectionFlipping();
    }

    private void handleDirectionFlipping() {
        if (direction == DIRECTION.RIGHT) {
            setCurrentFrame(frameRight);
        }
        else if (direction == DIRECTION.LEFT) {
            setCurrentFrame(frameLeft);
        }
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

}
