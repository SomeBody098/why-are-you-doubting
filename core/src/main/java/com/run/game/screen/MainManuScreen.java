package com.run.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.map.WorldName;
import com.run.game.ui.UiController;
import com.run.game.ui.UiFactory;
import com.run.game.utils.music.MusicManager;

public class MainManuScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private final OrthographicCamera uiCamera;
    private final ScreenViewport uiViewport;

    private final World world;

    private UiController uiController;

    public MainManuScreen(Main main, SpriteBatch batch, OrthographicCamera uiCamera, ScreenViewport uiViewport, World world) {
        this.main = main;
        this.batch = batch;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.world = world;
    }

    @Override
    public void show() {
        if (uiController == null) {
            GameScreen screen = new GameScreen(main, batch, uiCamera, uiViewport, world, WorldName.HOME);

            uiController = new UiController(UiFactory.createMainMenuStage(
                main, screen
            ));
        }
    }

    @Override
    public void render(float delta) {
        uiViewport.apply();
        batch.setProjectionMatrix(uiCamera.combined);

        uiController.render(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        uiController.dispose();
    }
}
