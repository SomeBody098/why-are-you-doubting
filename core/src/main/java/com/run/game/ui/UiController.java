package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void clear(){
        stage.getActors().clear();
    }

    public void add(Actor actor){
        stage.addActor(actor);
    }

    public void addAll(Actor... actors){
        stage.getActors().addAll(actors);
    }

    public <T extends Collection<Actor>> void addAll(T actors){
        stage.getActors().addAll(actors.stream().toArray(Actor[]::new));
    }

    public Array<Actor> getActors(){
        return stage.getActors();
    }

    public Stage getStage() {
        return stage;
    }

    public Actor findActor(String name) {
        for (Actor actor: stage.getActors()){
            if (actor.getName().equals(name)){
                return actor;
            }

            if (actor.getName().contains("table")){
                Table table = (Table) actor;

                for (Cell cell : table.getCells()) {
                    Actor cellActor = cell.getActor();
                    if (cellActor != null && name.equals(cellActor.getName())) {
                        return cellActor;
                    }
                }
            }
        }

        return null;
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != stage) Gdx.input.setInputProcessor(stage);
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
