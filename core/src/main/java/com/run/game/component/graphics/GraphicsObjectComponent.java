package com.run.game.component.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.DIRECTION;

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

//        TextureRegion frame;
//        if (isPlayer){
//            frame = getHalfOfFrame();
//        } else {
//            frame = currentFrame;
//        }

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

    private TextureRegion getHalfOfFrame(){
        TextureRegion frame;
        TextureRegion[][] regions = currentFrame.split(currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2);

        if (!isFirstPartTextureDraw) {
            frame = regions[0][0];
            isFirstPartTextureDraw = true;
        } else {
            frame = regions[0][1];
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
