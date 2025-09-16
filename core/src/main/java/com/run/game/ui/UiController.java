package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UiController implements Disposable {

    private final float viewportWidth;
    private final float viewportHeight;
    private final List<Rectangle> defaultActorsPositions;
    private final Stage stage;
    private final Map<String, Integer> actorsIndex;

    public UiController(Stage stage, Camera camera) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);

        viewportWidth = camera.viewportWidth;
        viewportHeight = camera.viewportHeight;

        defaultActorsPositions = new ArrayList<>();

        for (Actor actor : stage.getActors()) {
            defaultActorsPositions.add(new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight()));
        }

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
        defaultActorsPositions.clear();
    }

    public void add(Actor actor){
        stage.addActor(actor);
        actorsIndex.put(actor.getName(), stage.getActors().size - 1);
        defaultActorsPositions.add(new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight()));
    }

    public void addAll(Actor... actors){
        stage.getActors().addAll(actors);
        updateIndex();

        for (Actor actor : stage.getActors()) {
            defaultActorsPositions.add(new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight()));
        }
    }

    public <T extends Collection<Actor>> void addAll(T actors){
        stage.getActors().addAll(actors.stream().toArray(Actor[]::new));
        updateIndex();
    }

    public void remove(String name){
        int index = actorsIndex.get(name);

        stage.getActors().removeIndex(index);
        actorsIndex.remove(name);
        defaultActorsPositions.remove(index);
    }

    public Actor get(String name){
        int index = actorsIndex.get(name);

        return stage.getActors().get(index);
    }

    public void reboot(){
        if (Gdx.input.getInputProcessor() != stage) Gdx.input.setInputProcessor(stage);
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);

        float percentX = width / viewportWidth;
        float percentY = height / viewportHeight;

        for (int i = 0; i < stage.getActors().size; i++) {
            Actor actor = stage.getActors().get(i);
            Rectangle defaultSize = defaultActorsPositions.get(i);

            actor.setSize(
                defaultSize.width * percentX,
                defaultSize.height * percentY
            );

            actor.setPosition(
                defaultSize.x * percentX,
                defaultSize.y * percentY
            );

            Gdx.app.log("hhh", "actor: " + actor.getName() + "size: " + actor.getWidth());
        }
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
