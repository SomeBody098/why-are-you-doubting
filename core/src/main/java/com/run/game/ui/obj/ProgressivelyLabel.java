package com.run.game.ui.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * w = wait (5 sec)
 * i = increase speed (speed * 2)
 * d = decrease speed (speed / 2)
 */
public class ProgressivelyLabel extends Label {

    private String text;
    private float index;
    private float speed;
    private float wait;
    private boolean isTypedAllTheText;

    public ProgressivelyLabel(String text, Skin skin, String styleName, int startChars, float speed) {
        super(text, skin, styleName);
        newTyping(text, startChars, speed);

        isTypedAllTheText = false;
    }

    public void newTyping(String text, int startChars, float speed){
        this.text = text;
        this.speed = speed;

        if (startChars == -1) {
            super.setText("");
            index = 0;
        } else {
            super.setText(text.subSequence(0, startChars));
            index = startChars;
        }

        isTypedAllTheText = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (wait > 0){
            wait -= speed;
            return;
        }

        if (index == -1) return;
        if (index < text.length()) {
            if (text.charAt((int) Math.floor(index)) == '%') {
                char command = text.charAt((int) Math.floor(index) + 1);
                executeSpecialFunctions(command);

                StringBuilder builder = new StringBuilder(text);
                builder.replace((int) Math.floor(index), (int) Math.floor(index) + 2, "");
                text = builder.toString();
            }

            super.setText(text.subSequence(0, (int) Math.floor(index)));
            index += speed;
        } else {
            super.setText(text);
            index = -1;
            isTypedAllTheText = true;
        }
    }

    private void executeSpecialFunctions(char command){
        switch (command){
            case 'w':
                wait = 5;
                return;
            case 'i':
                speed *= 2;
                return;
            case 'd':
                speed /= 2;
                return;
            default:
                throw new IllegalArgumentException("Unknown command!");
        }
    }

    public boolean isTypedAllTheText() {
        return isTypedAllTheText;
    }
}
