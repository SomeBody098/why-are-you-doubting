package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import map.creator.map.component.ObjectComponent;

public class GraphicsObjectComponent extends ObjectComponent implements Disposable {

    private final float unitScale;
    private TextureRegion currentFrame;

    private final boolean isPlayer;

    private boolean isFirstPartTextureDraw;

    public GraphicsObjectComponent(String owner, TextureRegion currentFrame, float unitScale, boolean isPlayer) {
        super("graphics-moving-object", "graphics", owner);
        this.currentFrame = currentFrame;
        this.unitScale = unitScale;
        this.isPlayer = isPlayer;
    }

    public void draw(Batch batch, Vector2 position) {
        float divW = getWidth() / 2 * unitScale;
        float divH = getHeight() / 2 * unitScale;

        TextureRegion frame;
        if (isPlayer){
            frame = getHalfOfFrame();
        } else {
            frame = currentFrame;
        }

        batch.begin();
        batch.draw(
            frame,
            position.x - divW,
            position.y - divH - (isPlayer ? (!isFirstPartTextureDraw ? -divH : 0) : 0),
            getWidth() * unitScale,
            getHeight() / (isPlayer ? 2 : 1) * unitScale
        );
        batch.end();
    }

    private TextureRegion getHalfOfFrame(){
        TextureRegion frame;
        TextureRegion[][] regions = currentFrame.split(currentFrame.getRegionWidth(), currentFrame.getRegionHeight() / 2);

        if (!isFirstPartTextureDraw) {
            frame = regions[1][0];
            isFirstPartTextureDraw = true;
        } else {
            frame = regions[0][0];
            isFirstPartTextureDraw = false;
        }

        return frame;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
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
