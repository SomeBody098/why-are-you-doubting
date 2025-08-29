package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.DIRECTION;

import map.creator.map.component.ObjectComponent;

public class GraphicsMovingObjectComponent extends ObjectComponent implements Disposable {

    private DIRECTION direction = DIRECTION.NONE;
    private final float unitScale;
    private final TextureRegion currentFrame;

    public GraphicsMovingObjectComponent(String owner, TextureRegion currentFrame, float unitScale) {
        super("graphics-moving-object", "graphics", owner);
        this.currentFrame = currentFrame;
        this.unitScale = unitScale;
    }

    public void draw(Batch batch, Vector2 position) {
        float divW = getWidth() / 2 * unitScale;
        float divH = getHeight() / 2 * unitScale;

        batch.begin();

        batch.draw(
            currentFrame,
            position.x - divW,
            position.y - divH,
            getWidth() * unitScale,
            getHeight() * unitScale
        );

        batch.end();
    }

    public void update(DIRECTION direction){
        setDirection(direction);
        handleDirectionFlipping(currentFrame);
    }

    private void handleDirectionFlipping(TextureRegion frame) {
        if (direction == DIRECTION.RIGHT && !frame.isFlipX()) {
            frame.flip(true, false);
        }
        else if (direction == DIRECTION.LEFT && frame.isFlipX()) {
            frame.flip(true, false);
        }
    }

    public float getWidth(){
        return currentFrame.getRegionWidth();
    }

    public float getHeight(){
        return currentFrame.getRegionHeight();
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    @Override
    public void dispose() {
        currentFrame.getTexture().dispose();
    }
}
