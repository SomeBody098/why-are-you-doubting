package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UiController implements Disposable {

    private final Stage stage;
    private final Map<String, Integer> actorsIndex;

    public UiController(Stage stage) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);

        actorsIndex = new HashMap<>();
        updateIndex();
    }

    public void render(float delta){
        reboot();
        stage.act(delta);
        stage.draw();
    }

    public void clear(){
        stage.getActors().clear();
        actorsIndex.clear();
    }

    public void add(Actor actor){
        stage.addActor(actor);
        actorsIndex.put(actor.getName(), stage.getActors().size - 1);
    }

    public void addAll(Actor... actors){
        stage.getActors().addAll(actors);
        updateIndex();
    }

    public <T extends Collection<Actor>> void addAll(T actors){
        stage.getActors().addAll(actors.stream().toArray(Actor[]::new));
        updateIndex();
    }

    public void remove(String name){
        int index = actorsIndex.get(name);

        stage.getActors().removeIndex(index);
        actorsIndex.remove(name);
    }

    public Actor get(String name){
        int index = actorsIndex.get(name);

        return stage.getActors().get(index);
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != stage) Gdx.input.setInputProcessor(stage);
    }

    private void updateIndex(){
        if (stage.getActors().size == actorsIndex.size()) return;

        actorsIndex.clear();
        for (int i = 0; i < stage.getActors().size; i++) {
            Actor actor = stage.getActors().get(i);
            actorsIndex.put(actor.getName(), i);
        }
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
