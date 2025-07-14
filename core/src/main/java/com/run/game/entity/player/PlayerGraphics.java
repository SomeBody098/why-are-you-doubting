package com.run.game.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.entity.DIRECTION;

public class PlayerGraphics implements Disposable {

    private DIRECTION direction = DIRECTION.NONE;

    private final TextureRegion currentFrame;

    public PlayerGraphics() {
        currentFrame = new TextureRegion(new Texture("textures/player.png"));
    }

    public void draw(Batch batch, Vector2 position, float width, float height, float ppm, float unitScale) {
        float divW = getWidth() / 2 * unitScale;
        float divH = getHeight() / 2 * unitScale;

        batch.begin();

        batch.draw(
            currentFrame,
            position.x - divW,
            position.y - divH,
            width / ppm,
            height / ppm
        );

        batch.end();
    }

    public void update(float delta){
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

    }
}
