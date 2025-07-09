package com.run.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.controller.UiController;
import com.run.game.ui.UiFactory;

public class MainManuScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private final FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;

    private UiController uiController;

    public MainManuScreen(Main main, SpriteBatch batch, OrthographicCamera gameCamera, OrthographicCamera uiCamera, FitViewport gameViewport, ScreenViewport uiViewport, World world) {
        this.main = main;
        this.batch = batch;
        this.gameCamera = gameCamera;
        this.uiCamera = uiCamera;
        this.gameViewport = gameViewport;
        this.uiViewport = uiViewport;
        this.world = world;
    }

    @Override
    public void show() {
        if (uiController == null) {
            GameScreen screen = new GameScreen(main, batch, gameCamera, uiCamera, gameViewport, uiViewport, world);

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
