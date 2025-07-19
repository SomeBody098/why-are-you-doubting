package com.run.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.map.MapFactory;
import com.run.game.screen.MainManuScreen;
import com.run.game.ui.UiFactory;
import com.run.game.utils.music.MusicManager;

public class Main extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        OrthographicCamera uiCamera = createUiCamera();
        ScreenViewport uiViewport = new ScreenViewport(uiCamera);

        World world = new World(new Vector2(), false);

        UiFactory.init(uiCamera, uiViewport, batch);
        MapFactory.init(world);
        MusicManager.init();

        setScreen(new MainManuScreen(this, batch, uiCamera, uiViewport, world));
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
        MusicManager.dispose();
    }
}
