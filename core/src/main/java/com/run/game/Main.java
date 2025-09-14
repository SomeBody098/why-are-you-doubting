package com.run.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.screen.MainManuScreen;
import com.run.game.ui.UiFactory;
import com.run.game.utils.music.MusicManager;

import map.creator.map.factory.MapFactory;

public class Main extends Game {

    private SpriteBatch batch;

    private Engine engine;
    private World world;

    private MapFactory mapFactory;
    private UiFactory uiFactory;
    private MusicManager musicManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        engine = new Engine();

        OrthographicCamera uiCamera = createUiCamera();
        ScreenViewport uiViewport = new ScreenViewport(uiCamera);

        world = new World(new Vector2(), false);

        mapFactory = new MapFactory(world, engine, true);
        uiFactory = new UiFactory(uiCamera, uiViewport, batch);
        musicManager = new MusicManager();

        setScreen(new MainManuScreen(this, batch, uiCamera, uiViewport, world, uiFactory, mapFactory, musicManager, engine));
    }

    private OrthographicCamera createUiCamera(){
        OrthographicCamera uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(
            (float) Gdx.graphics.getWidth() / 2,
            (float) Gdx.graphics.getHeight() / 2,
            0
        );
        uiCamera.setToOrtho(false);
        uiCamera.update();

        return uiCamera;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapFactory.dispose();
        world.dispose();
        musicManager.dispose();
        uiFactory.dispose();
    }
}
