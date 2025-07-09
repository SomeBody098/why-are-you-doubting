package com.run.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UiController extends StageController{

    public UiController(Stage stage) {
        super(stage);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        reboot();
        super.render(delta);
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != getStage()) Gdx.input.setInputProcessor(getStage());
    }
}
