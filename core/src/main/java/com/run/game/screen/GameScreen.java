package com.run.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.contact_listeners.GameContactListener;
import com.run.game.controller.MapController;
import com.run.game.controller.StageController;
import com.run.game.entity.EntityFactory;
import com.run.game.controller.UiController;
import com.run.game.map.MapFactory;
import com.run.game.map.WorldName;
import com.run.game.ui.UiFactory;
import com.run.game.ui.joystick.Joystick;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private final FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;

    private UiController uiController;
    private StageController gameController;
    private MapController mapController;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Main main, SpriteBatch batch, OrthographicCamera gameCamera, OrthographicCamera uiCamera, FitViewport gameViewport, ScreenViewport uiViewport, World world) {
        this.main = main;
        this.batch = batch;
        this.gameCamera = gameCamera;
        this.uiCamera = uiCamera;
        this.gameViewport = gameViewport;
        this.uiViewport = uiViewport;
        this.world = world;

        world.setContactListener(new GameContactListener());
    }

    @Override
    public void show() {
        if (!MapFactory.isLoadTextureHomeWorld()){
            MapFactory.loadTextureWorld(WorldName.HOME);
            main.setScreen(new LoadingScreen(main, this, uiCamera, uiViewport));

        } else if (mapController == null){
            mapController = new MapController(MapFactory.createMap(WorldName.HOME), gameCamera, batch);
        }

        if (uiController == null || gameController == null){
            Stage stage = UiFactory.createGameUiStage();
            Joystick joystick = (Joystick) stage.getActors().get(0);

            uiController = new UiController(stage);
            gameController = new StageController(EntityFactory.createStageGame(joystick.getDto()));
            debugRenderer = new Box2DDebugRenderer();
        }
    }

    @Override
    public void render(float delta) {
        renderGameObjects(delta);
        renderUi(delta);
    }

    private void renderGameObjects(float delta){
        gameViewport.apply();
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);

        mapController.render();
        gameController.render(delta);

        debugRenderer.render(world, gameCamera.combined); // FIXME: 09.07.2025 УДАЛИТЬ К РЕЛИЗУ!
    }

    private void renderUi(float delta){
        uiViewport.apply();
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);

        uiController.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
        gameViewport.update(width, height);
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
        gameController.dispose();
        uiController.dispose();
        world.dispose();
    }
}
