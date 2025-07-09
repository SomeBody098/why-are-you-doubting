package com.run.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.controller.UiController;
import com.run.game.map.MapFactory;
import com.run.game.ui.UiFactory;

public class LoadingScreen implements Screen {

    private final Main main;
    private final Screen currentScreen;

    private final OrthographicCamera uiCamera;
    private final ScreenViewport uiViewport;

    private UiController controller;

    public LoadingScreen(Main main, Screen currentScreen, OrthographicCamera uiCamera, ScreenViewport uiViewport) {
        this.main = main;
        this.currentScreen = currentScreen;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
    }

    @Override
    public void show() {
        if (controller == null){
            float progress = MapFactory.getProgress() * 100;
            controller = new UiController(UiFactory.createLoadingStage(0, progress, 1));
        }
    }

    @Override
    public void render(float delta) {
        uiViewport.apply();
        uiCamera.update();

        controller.render(delta);

        if (MapFactory.isDone()) moveToCurrentScreen();
    }

    private void moveToCurrentScreen() {
        main.setScreen(currentScreen);

        dispose();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
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
