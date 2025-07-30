package com.run.game.screen;

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
import com.run.game.event.EventBus;
import com.run.game.event.EventType;
import com.run.game.handler.GameContactListener;
import com.run.game.map.RoomManager;
import com.run.game.map.MapController;
import com.run.game.entity.EntityFactory;
import com.run.game.ui.UiController;
import com.run.game.handler.SensorHandler;
import com.run.game.map.MapContainer;
import com.run.game.map.MapFactory;
import com.run.game.map.RoomName;
import com.run.game.map.WorldName;
import com.run.game.ui.UiFactory;
import com.run.game.ui.obj.joystick.Joystick;
import com.run.game.utils.music.MusicManager;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;
    private final WorldName currentWorld;

    private UiController uiController;
    private RoomManager roomManager;

    private Player player;

    private EventBus eventBus;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Main main, SpriteBatch batch, OrthographicCamera uiCamera, ScreenViewport uiViewport, World world, WorldName currentWorld) {
        this.main = main;
        this.batch = batch;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.world = world;
        this.currentWorld = currentWorld;
    }

    @Override
    public void show() {
        if (loadTextureWorldAndMusic()) {
            createMapAndGameEntity();
        }
    }

    private boolean loadTextureWorldAndMusic(){
        if (!MapFactory.isLoadTextureWorld(currentWorld)){
            MapFactory.loadTextureWorld(currentWorld);
            MusicManager.loadMusic(currentWorld);

            main.setScreen(new LoadingScreen(main, this, uiCamera, uiViewport));
            return false;
        } else {
            MusicManager.initMusic(currentWorld, "house_theme");
        }

        return true;
    }

    private void createMapAndGameEntity(){
        if (roomManager == null || uiController == null){
            MapContainer container = MapFactory.createMap(currentWorld);
            createGameCameraAndViewport(container);
            MapController mapController = new MapController(container, gameCamera, batch);

            Stage stage = UiFactory.createGameUiStage();
            uiController = new UiController(stage);
            Joystick joystick = (Joystick) stage.getActors().get(0); // FIXME: 14.07.2025 опасный хардкод (что если на 0 индексе не джойстик? = облом)

            eventBus = new EventBus();
            EntityFactory.init(world, container.PPM, container.UNIT_SCALE);
            player = EntityFactory.createPlayer(joystick.getDto(), container.getObject("spawn-player", Vector2.class));

            roomManager = new RoomManager(mapController, player);

            world.setContactListener(new GameContactListener(new SensorHandler(eventBus, roomManager))); // FIXME: 12.07.2025 выглядит немного глупо и замудренно (P.S: это так и есть ;) )

            debugRenderer = new Box2DDebugRenderer(); // FIXME: 14.07.2025 УДАЛИ ПРИ РЕЛИЗЕ
        }
    }

    private void createGameCameraAndViewport(MapContainer container){
        Rectangle room = container.getBorderRoom(RoomName.START_ROOM); // FIXME: 19.07.2025 хардкод

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

        roomManager.render();
        eventBus.publish(EventType.TeleportPlayerEvent);

        player.update(delta, roomManager.getCurrentRoom());
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
