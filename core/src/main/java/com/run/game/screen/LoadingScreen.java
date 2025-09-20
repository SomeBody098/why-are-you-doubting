package com.run.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.ui.UiController;
import com.run.game.ui.UiFactory;

import map.creator.map.factory.AsynchronousFactory;

public class LoadingScreen implements Screen {

    private final Main main;
    private final Screen currentScreen;

    private final OrthographicCamera uiCamera;
    private final ScreenViewport uiViewport;
    private final AsynchronousFactory[] asynchronousFactors;

    private final UiFactory uiFactory;
    private UiController controller;

    public LoadingScreen(Main main, Screen currentScreen, OrthographicCamera uiCamera, ScreenViewport uiViewport, UiFactory uiFactory, AsynchronousFactory... asynchronousFactors) {
        this.main = main;
        this.currentScreen = currentScreen;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.uiFactory = uiFactory;
        this.asynchronousFactors = asynchronousFactors;
    }

    @Override
    public void show() {
        if (controller == null){
            controller = new UiController(uiFactory.createLoadingStage());
        }
    }

    @Override
    public void render(float delta) {
        uiViewport.apply();
        uiCamera.update();
        controller.render(delta);

        for (AsynchronousFactory factory : asynchronousFactors) {
            if (!factory.isDone()) {
                return;
            }
        }

        moveToCurrentScreen();
    }

    private void moveToCurrentScreen() {
        main.setScreen(currentScreen);

        dispose();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
        controller.resize(width, height);
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
        controller.dispose();
    }
}
