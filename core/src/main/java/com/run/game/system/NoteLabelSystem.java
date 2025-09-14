package com.run.game.system;

import com.run.game.ui.obj.ProgressivelyLabel;

public class NoteLabelSystem {

    private final ProgressivelyLabel noteLabel;
    private float timer = 0;
    private boolean isActive;

    public NoteLabelSystem(ProgressivelyLabel noteLabel) {
        this.noteLabel = noteLabel;
    }

    public void update(float deltaTime){
        if (!isActive) return;

        if (!noteLabel.isTypedAllTheText()) return;
        timer += deltaTime;
        if (timer < (float) noteLabel.getText().length / 5) return;

        noteLabel.setVisible(false);
        isActive = false;
    }

    public void startWrite(String message){
        isActive = true;
        timer = 0;

        noteLabel.newTyping(message, -1, 0.25f);
        noteLabel.setVisible(true);
    }
}
