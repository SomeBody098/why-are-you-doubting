package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.DIRECTION;

import map.creator.map.component.ObjectComponent;

public class GraphicsObjectComponent extends ObjectComponent implements Disposable {

    private final float unitScale;
    private final TextureRegion currentFrame;

    public GraphicsObjectComponent(String owner, TextureRegion currentFrame, float unitScale) {
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

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public float getWidth(){
        return currentFrame.getRegionWidth();
    }

    public float getHeight(){
        return currentFrame.getRegionHeight();
    }

    @Override
    public void dispose() {
        currentFrame.getTexture().dispose();
    }
}
