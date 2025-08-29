package com.run.game.ui.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ProgressivelyLabel extends Label {

    private CharSequence text;
    private float index;
    private float speed;
    private boolean isTypedAllTheText;

    public ProgressivelyLabel(CharSequence text, Skin skin, String styleName, int startChars, float speed) {
        super(text, skin, styleName);
        newTyping(text, startChars, speed);

        isTypedAllTheText = false;
    }

    public void newTyping(CharSequence text, int startChars, float speed){
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

        if (index == -1) return;
        if (index < text.length()) {
            super.setText(text.subSequence(0, (int) Math.floor(index)));
            index += speed;
        } else {
            super.setText(text);
            index = -1;
            isTypedAllTheText = true;
        }
    }

    public boolean isTypedAllTheText() {
        return isTypedAllTheText;
    }
}
