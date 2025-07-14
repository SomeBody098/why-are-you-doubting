package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

public class UiController implements Disposable {

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

    public Stage getStage() {
        return stage;
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != stage) Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
