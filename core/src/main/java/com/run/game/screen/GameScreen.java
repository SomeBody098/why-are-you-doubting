package com.run.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.creator.MovingCreator;
import com.run.game.creator.NoteCreator;
import com.run.game.creator.PlayerCreator;
import com.run.game.creator.RoomCreator;
import com.run.game.system.DrawGraphicsSystem;
import com.run.game.system.DrawWalkingGraphicsSystem;
import com.run.game.system.MovingSystem;
import com.run.game.system.NoteSystem;
import com.run.game.system.ViewRoomSystem;
import com.run.game.ui.UiController;
import com.run.game.RoomName;
import com.run.game.ui.UiFactory;
import com.run.game.ui.obj.joystick.Joystick;
import com.run.game.utils.music.MusicManager;

import java.util.Map;
import java.util.stream.Collectors;

import map.creator.map.controller.MapContainer;
import map.creator.map.controller.MapController;
import map.creator.map.factory.MapFactory;
import map.creator.map.system.MapContactListener;
import map.creator.map.system.TriggerSystem;

public class GameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;

    private final String pathToMap;
    private final String nameMusicStorage;

    private final MapFactory mapFactory;
    private final UiFactory uiFactory;
    private final MusicManager musicManager;

    private final Engine engine;

    private UiController uiController;

    private MapController mapController;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Main main, SpriteBatch batch, OrthographicCamera uiCamera, ScreenViewport uiViewport, World world, MapFactory mapFactory, UiFactory uiFactory, MusicManager musicManager, Engine engine) {
        this.main = main;
        this.batch = batch;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.world = world;
        this.mapFactory = mapFactory;
        this.uiFactory = uiFactory;
        this.musicManager = musicManager;
        this.engine = engine;

        pathToMap = "maps/map.tmx";
        nameMusicStorage = "home";
    }

    @Override
    public void show() {
        if (isLoadMap()) {
            createMapControllerAndGameEntity();
            registerSystems();
        }
    }

    private boolean isLoadMap(){
        if (!mapFactory.isLoadMap(pathToMap)){
            Stage stage = uiFactory.createGameUiStage(musicManager);
            uiController = new UiController(stage);
            Joystick joystick = (Joystick) uiController.get("joystick");

            mapFactory.registerCreator("room", new RoomCreator());
            mapFactory.registerCreator("player", new PlayerCreator(joystick.getDto(), new TextureRegion(new Texture("textures/player.png"))));
            mapFactory.registerCreator("moving", new MovingCreator(musicManager));
            mapFactory.registerCreator("note", new NoteCreator(new TextureRegion(new Texture("textures/note.png"))));

            mapFactory.createMap(pathToMap, "objects", "rooms");
            musicManager.loadMusic(nameMusicStorage);

            main.setScreen(new LoadingScreen(main, this, uiCamera, uiViewport, uiFactory, mapFactory, musicManager));
            return false;
        }

        musicManager.initMusic(nameMusicStorage, "house_theme");

        return true;
    }

    private void createMapControllerAndGameEntity(){
        if (mapController == null){
            MapContainer container = mapFactory.getMap(pathToMap);
            createGameCameraAndViewport(container);
            mapController = new MapController(container, gameCamera, batch);

            world.setContactListener(new MapContactListener(engine, mapFactory.getObjectsFactory().getCache()));

            debugRenderer = new Box2DDebugRenderer(); // FIXME: 14.07.2025 УДАЛИ ПРИ РЕЛИЗЕ
        }
    }

    private void createGameCameraAndViewport(MapContainer container){
        Rectangle room = ((RectangleMapObject) container.getObjectOnNameInLayer("rooms", RoomName.START_ROOM.name())).getRectangle();

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

    private void registerSystems(){
        engine.addSystem(new MovingSystem());
        engine.addSystem(new DrawWalkingGraphicsSystem(batch, gameCamera, gameViewport));
        engine.addSystem(new DrawGraphicsSystem(batch, gameCamera, gameViewport));
        engine.addSystem(new ViewRoomSystem(gameCamera));
        engine.addSystem(new TriggerSystem());

        Map<String, MapProperties> dataObjects = mapFactory.getObjectsFactory().getCache().getDataObjects();
        Map<String, MapProperties> noteProperties =
            dataObjects.entrySet().stream().filter(entry -> entry.getKey()
                .contains("noteProperty")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));

        engine.addSystem(new NoteSystem(uiController, noteProperties, mapFactory.getObjectsFactory().getBodyFactory().getUnitScale()));
    }

    @Override
    public void render(float delta) {
        renderGameObjects(delta);
        renderUi(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){ // TODO: 28.08.2025 УБЕРИ!!!!!!!
            gameCamera.position.x--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            gameCamera.position.x++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            gameCamera.position.y--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            gameCamera.position.y++;
        }
    }

    private void renderGameObjects(float delta){
        gameViewport.apply();
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);

        mapController.render(gameCamera);
        engine.update(Gdx.graphics.getDeltaTime());

        world.step(delta, 6, 6);

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
        uiController.dispose();
        world.dispose();
    }
}
