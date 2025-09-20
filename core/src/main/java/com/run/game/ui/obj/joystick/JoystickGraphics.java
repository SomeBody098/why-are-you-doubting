package com.run.game.ui.obj.joystick;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class JoystickGraphics {

    private final Texture circleTexture;
    private final Texture stickTexture;

    public JoystickGraphics() {
        circleTexture = new Texture("ui/virtualJoystickTexture/circle.png");
        stickTexture = new Texture("ui/virtualJoystickTexture/stick.png");
    }

    public void draw(Batch uiBatch, Color color, float centerX, float centerY, float stickX, float stickY, float radius, float parentAlpha) {
        uiBatch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        uiBatch.draw(circleTexture,
            centerX - radius, centerY - radius,
            radius * 2, radius * 2
        );

        float stickRadius = radius / 2;
        uiBatch.draw(stickTexture,
            stickX - stickRadius, stickY - stickRadius,
            radius, radius
        );

        uiBatch.setColor(Color.WHITE);
    }

    public float getWightCircle(){
        return circleTexture.getWidth();
    }

    public float getHeightCircle(){
        return circleTexture.getHeight();
    }

    public float getWightStick(){
        return stickTexture.getWidth();
    }

    public float getHeightStick(){
        return stickTexture.getHeight();
    }

    public void dispose(){
        circleTexture.dispose();
        stickTexture.dispose();
    }
}
