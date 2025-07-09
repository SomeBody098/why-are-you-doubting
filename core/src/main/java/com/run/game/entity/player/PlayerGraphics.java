package com.run.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.run.game.Main;
import com.run.game.entity.DIRECTION;

public class PlayerGraphics implements Disposable {

    private DIRECTION direction = DIRECTION.NONE;

    private final TextureRegion currentFrame;

    public PlayerGraphics() {
        currentFrame = new TextureRegion(new Texture("textures/player.png"));
    }

    public void draw(Batch batch, Vector2 position, float width, float height) {
        float divW = (getWidth() / 2) * Main.UNIT_SCALE;
        float divH = (getHeight() / 2) * Main.UNIT_SCALE;

        batch.draw(
            currentFrame,
            position.x - divW,
            position.y - divH,
            width / Main.PPM / 2,
            height / Main.PPM / 2
        );
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
