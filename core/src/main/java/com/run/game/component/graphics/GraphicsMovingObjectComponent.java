package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.run.game.DIRECTION;

public class GraphicsMovingObjectComponent extends GraphicsObjectComponent {

    private DIRECTION direction = DIRECTION.NONE;

    public GraphicsMovingObjectComponent(String owner, TextureRegion currentFrame, float unitScale) {
        super(owner, currentFrame, unitScale);
    }

    public void update(DIRECTION direction){
        setDirection(direction);
        handleDirectionFlipping(getCurrentFrame());
    }

    private void handleDirectionFlipping(TextureRegion frame) {
        if (direction == DIRECTION.RIGHT && !frame.isFlipX()) {
            frame.flip(true, false);
        }
        else if (direction == DIRECTION.LEFT && frame.isFlipX()) {
            frame.flip(true, false);
        }
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

}
