package com.run.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

public class StageController implements Disposable {

    private final Stage stage;

    public StageController(Stage stage) {
        this.stage = stage;
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

}
