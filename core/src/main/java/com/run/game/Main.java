package com.run.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.screen.MainManuScreen;
import com.run.game.ui.UiFactory;

import java.util.HashMap;
import java.util.Map;

public class Main extends Game {

    public static final float PPM = 16;
    public static final float UNIT_SCALE = 1f / PPM;
    public static final int MAP_WIDTH_TILES = 8;
    public static final int MAP_HEIGHT_TILES = 8;
    public static final float MAP_WIDTH_PIXELS = MAP_WIDTH_TILES * 16;
    public static final float MAP_HEIGHT_PIXELS = MAP_HEIGHT_TILES * 16;
    public static final float MAP_WIDTH_METERS = MAP_WIDTH_PIXELS / PPM;
    public static final float MAP_HEIGHT_METERS = MAP_HEIGHT_PIXELS / PPM;

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        OrthographicCamera gameCamera = new OrthographicCamera(10, 10);
        gameCamera.position.set(
            MAP_WIDTH_METERS / 2,
            MAP_HEIGHT_METERS / 2,
            0
        );
        gameCamera.update();

        OrthographicCamera uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(
            (float) Gdx.graphics.getWidth() / 2,
            (float) Gdx.graphics.getHeight() / 2,
            0
        );
        uiCamera.setToOrtho(false);
        uiCamera.update();

        FitViewport gameViewport = new FitViewport(Main.MAP_WIDTH_METERS, Main.MAP_HEIGHT_METERS, gameCamera);
        ScreenViewport uiViewport = new ScreenViewport(uiCamera);

        UiFactory.init(uiCamera, uiViewport, batch);

        World world = new World(new Vector2(), false);

        setScreen(new MainManuScreen(this, batch, gameCamera, uiCamera, gameViewport, uiViewport, world));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
