package com.run.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.creator.MovingCreator;
import com.run.game.creator.NoteCreator;
import com.run.game.creator.PlayerCreator;
import com.run.game.creator.RoomCreator;
import com.run.game.creator.TriggerStopMusicCreator;
import com.run.game.system.DrawGraphicsSystem;
import com.run.game.system.DrawWalkingGraphicsSystem;
import com.run.game.system.MovingSystem;
import com.run.game.system.WalkingSystem;
import com.run.game.system.NoteLabelSystem;
import com.run.game.system.NoteSystem;
import com.run.game.system.ViewRoomSystem;
import com.run.game.ui.UiController;
import com.run.game.RoomName;
import com.run.game.ui.UiFactory;
import com.run.game.ui.obj.ProgressivelyLabel;
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

    private MovingSystem movingSystem;

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

        pathToMap = "maps/legacy/map.tmx";
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
            Joystick joystick = (Joystick) uiController.findActor("joystick");

            mapFactory.registerCreator("room", new RoomCreator());
            mapFactory.registerCreator("moving", new MovingCreator());
            mapFactory.registerCreator("player", new PlayerCreator(joystick.getDto(), new TextureRegion(new Texture(Gdx.files.internal("textures/playerLeft.png"))), new TextureRegion(new Texture(Gdx.files.internal("textures/playerRight.png")))));
            mapFactory.registerCreator("note", new NoteCreator(new TextureRegion(new Texture(Gdx.files.internal("textures/note.png")))));
            mapFactory.registerCreator("trigger-stop-music", new TriggerStopMusicCreator(musicManager, "house_theme"));

            mapFactory.createMap(pathToMap, "objects", "notes", "rooms");
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

            world.setContactListener(new MapContactListener(engine, mapFactory.getObjectsFactory().getCache(), true));
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
        engine.addSystem(new WalkingSystem());
        engine.addSystem(new TriggerSystem());
        engine.addSystem(new DrawWalkingGraphicsSystem(batch, gameCamera, gameViewport, 2));
        engine.addSystem(new DrawGraphicsSystem(batch, gameCamera, gameViewport, 1));
        engine.addSystem(new ViewRoomSystem(gameCamera));

        Map<String, MapProperties> dataObjects = mapFactory.getObjectsFactory().getCache().getDataObjects();
        Map<String, MapProperties> noteProperties =
            dataObjects.entrySet().stream().filter(entry -> entry.getKey()
                .contains("noteProperty")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));

        NoteLabelSystem noteLabelSystem = new NoteLabelSystem((ProgressivelyLabel) uiController.findActor("note"));

        engine.addSystem(
            new NoteSystem(
                noteLabelSystem, noteProperties,
                mapFactory.getObjectsFactory().getBodyFactory().getUnitScale())
        );

        movingSystem = new MovingSystem(
            musicManager, noteLabelSystem
        );

        engine.addSystem(movingSystem);
    }

    @Override
    public void render(float delta) {
        renderGameObjects(delta);
        renderUi(delta);

        if (movingSystem.isTransition()){
            main.setScreen(new NewGameScreen(main, batch, uiCamera, uiViewport, world, mapFactory, uiController, musicManager, engine));
            dispose();
        }
    }

    private void renderGameObjects(float delta){
        gameViewport.apply();
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);

        mapController.render(gameCamera, "background", "background+");
        engine.update(delta);
        mapController.render(gameCamera, "items", "topground");
        engine.getSystem(DrawWalkingGraphicsSystem.class).update(delta);
        engine.getSystem(ViewRoomSystem.class).update(delta);

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
        uiController.resize(width, height);
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
        mapController.dispose();
    }
}
