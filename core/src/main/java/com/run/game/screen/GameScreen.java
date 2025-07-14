package com.run.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.entity.player.Player;
import com.run.game.handler.GameContactListener;
import com.run.game.map.MapController;
import com.run.game.entity.EntityFactory;
import com.run.game.ui.UiController;
import com.run.game.handler.SensorHandler;
import com.run.game.map.MapContainer;
import com.run.game.map.MapFactory;
import com.run.game.map.RoomName;
import com.run.game.map.WorldName;
import com.run.game.ui.UiFactory;
import com.run.game.ui.joystick.Joystick;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;

    private UiController uiController;
    private MapController mapController;

    private Player player;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Main main, SpriteBatch batch, OrthographicCamera uiCamera, ScreenViewport uiViewport, World world) {
        this.main = main;
        this.batch = batch;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.world = world;
    }

    @Override
    public void show() {
        if (loadTextureWorld()) {
            createMapAndGameEntity();
        }
    }

    private boolean loadTextureWorld(){
        if (!MapFactory.isLoadTextureWorld(WorldName.HOME)){
            MapFactory.loadTextureWorld(WorldName.HOME);
            main.setScreen(new LoadingScreen(main, this, uiCamera, uiViewport));
            return false;
        }

        return true;
    }

    private void createMapAndGameEntity(){
        if (mapController == null || uiController == null){
            MapContainer container = MapFactory.createMap(WorldName.HOME);
            createGameCameraAndViewport(container);
            mapController = new MapController(container, gameCamera, batch);

            world.setContactListener(new GameContactListener(new SensorHandler(mapController))); // FIXME: 12.07.2025 выглядит немного глупо и замудренно (P.S: это так и есть ;) )

            Stage stage = UiFactory.createGameUiStage();
            uiController = new UiController(stage);
            Joystick joystick = (Joystick) stage.getActors().get(0); // FIXME: 14.07.2025 опасный хардкод (что если на 0 индексе не джойстик? = облом)

            EntityFactory.init(world, container.PPM, container.UNIT_SCALE);
            player = EntityFactory.createPlayer(joystick.getDto(), container.getObject("spawn-player", Vector2.class));

            debugRenderer = new Box2DDebugRenderer(); // FIXME: 14.07.2025 УДАЛИ ПРИ РЕЛИЗЕ
        }
    }

    private void createGameCameraAndViewport(MapContainer container){
        Rectangle room = container.getBorderRoom(RoomName.START_ROOM);

        gameCamera = new OrthographicCamera(
            room.width * container.UNIT_SCALE,
            room.height * container.UNIT_SCALE
        );
        gameCamera.position.set(
            (room.x + (room.width / 2)) * container.UNIT_SCALE,
            (room.y + (room.height / 2)) * container.UNIT_SCALE,
            0
        );
        gameCamera.update();

        Gdx.app.log("gameCamera", gameCamera.viewportHeight + "");

        gameViewport = new FitViewport(gameCamera.viewportWidth, gameCamera.viewportHeight, gameCamera);
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

        player.update(delta);
        player.draw(batch);

        debugRenderer.render(world, gameCamera.combined); // FIXME: 09.07.2025 УДАЛИТЬ К РЕЛИЗУ!

        world.step(delta, 6, 6);
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
        uiController.dispose();
        world.dispose();
    }
}
