package com.run.game.ui.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ProgressivelyLabel extends Label {

    private final CharSequence text;
    private float index;

    public ProgressivelyLabel(CharSequence text, Skin skin, String styleName, int startChars) {
        super(text, skin, styleName);

        this.text = text;

        if (startChars == -1) {
            super.setText("");
            index = 0;
        } else {
            super.setText(text.subSequence(0, startChars));
            index = startChars;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (index == -1) return;
        if (index < text.length()) {
            super.setText(text.subSequence(0, (int) Math.floor(index)));
            index += 0.5f;
        } else {
            super.setText(text);
            index = -1;
        }
    }
}
