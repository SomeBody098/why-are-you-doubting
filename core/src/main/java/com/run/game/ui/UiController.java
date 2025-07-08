package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UiController {

    private final Stage stage;

    public UiController(Stage stage) {
        this.stage = stage;

        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta){
        reboot();
        stage.act(delta);
        stage.draw();
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != stage) Gdx.input.setInputProcessor(stage);
    }

    public void dispose(){
        stage.dispose();
    }
}
