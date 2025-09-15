package com.run.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.run.game.Main;
import com.run.game.RoomName;
import com.run.game.creator.NewPlayerCreator;
import com.run.game.creator.NoteCreator;
import com.run.game.system.DrawGraphicsSystem;
import com.run.game.system.DrawWalkingAnimationGraphicsSystem;
import com.run.game.system.DrawWalkingGraphicsSystem;
import com.run.game.system.MovingSystem;
import com.run.game.system.NoteLabelSystem;
import com.run.game.system.NoteSystem;
import com.run.game.system.ViewRoomSystem;
import com.run.game.system.WalkingSystem;
import com.run.game.ui.UiController;
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

public class NewGameScreen implements Screen {

    private final Main main;
    private final SpriteBatch batch;

    private OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private FitViewport gameViewport;
    private final ScreenViewport uiViewport;

    private final World world;

    private final String pathToMap;

    private final MapFactory mapFactory;
    private final MusicManager musicManager;

    private final Engine engine;

    private final UiController uiController;

    private MapController mapController;

    private Texture texture;
    private final Color color;

    public NewGameScreen(Main main, SpriteBatch batch, OrthographicCamera uiCamera, ScreenViewport uiViewport, World world, MapFactory mapFactory, UiController uiController, MusicManager musicManager, Engine engine) {
        this.main = main;
        this.batch = batch;
        this.uiCamera = uiCamera;
        this.uiViewport = uiViewport;
        this.world = world;
        this.mapFactory = mapFactory;
        this.uiController = uiController;
        this.musicManager = musicManager;
        this.engine = engine;

        pathToMap = "maps/new/newMap.tmx";

        texture = new Texture("textures/black.png");
        color = new Color();
        color.a = 1;
    }


    @Override
    public void show() {
        engine.removeAllSystems();

        if (!mapFactory.isLoadMap(pathToMap)){
            mapFactory.unregisterCreator("player");
            mapFactory.unregisterCreator("trigger-stop-music");
            mapFactory.unregisterCreator("note");

            mapFactory.registerCreator("player", new NewPlayerCreator(((Joystick) uiController.get("joystick")).getDto(), new TextureAtlas("textures/newPlayer.atlas")));
            mapFactory.registerCreator("note", new NoteCreator(new TextureRegion(new Texture("textures/new_note.png"))));

            mapFactory.createMap(pathToMap, "objects", "notes", "rooms");

            while (!mapFactory.isDone()){} // I don't care!
            createMapControllerAndGameEntity();
        }
    }

    private void createMapControllerAndGameEntity(){
        if (mapController == null){
            MapContainer container = mapFactory.getMap(pathToMap);
            createGameCameraAndViewport(container);
            mapController = new MapController(container, gameCamera, batch);

            world.setContactListener(new MapContactListener(engine, mapFactory.getObjectsFactory().getCache(), true));

            registerSystems();
        }
    }

    private void createGameCameraAndViewport(MapContainer container){
        Rectangle room = ((RectangleMapObject) container.getObjectOnNameInLayer("rooms", RoomName.DINNING_ROOM.name())).getRectangle();

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
        engine.addSystem(new DrawWalkingAnimationGraphicsSystem(batch, gameCamera, gameViewport));
        engine.addSystem(new DrawGraphicsSystem(batch, gameCamera, gameViewport));
        engine.addSystem(new ViewRoomSystem(gameCamera));

        Map<String, MapProperties> dataObjects = mapFactory.getObjectsFactory().getCache().getDataObjects();
        Map<String, MapProperties> noteProperties =
            dataObjects.entrySet().stream().filter(entry -> entry.getKey()
                .contains("noteProperty")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));

        NoteLabelSystem noteLabelSystem = new NoteLabelSystem((ProgressivelyLabel) uiController.get("note"));

        engine.addSystem(
            new NoteSystem(
                noteLabelSystem, noteProperties,
                mapFactory.getObjectsFactory().getBodyFactory().getUnitScale())
        );

        engine.addSystem(new MovingSystem(
            musicManager, noteLabelSystem
        ));
    }

    @Override
    public void render(float delta) {
        renderGameObjects(delta);
        renderUi(delta);
    }

    private float timer = 0;
    private void renderGameObjects(float delta) {
        gameViewport.apply();
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);

        mapController.render(gameCamera, "background", "background+");
        engine.update(delta);
        mapController.render(gameCamera, "items");
        engine.getSystem(DrawWalkingAnimationGraphicsSystem.class).update(delta);
        engine.getSystem(DrawGraphicsSystem.class).update(delta);
        mapController.render(gameCamera, "topground", "shadows");
        engine.getSystem(ViewRoomSystem.class).update(delta);

        world.step(delta, 6, 6);

        if (color.a > 0) {
            if (timer >= 3) {
                color.a -= 0.001F;
            } else {
                timer += delta;
            }

            Color pastColor = batch.getColor();
            batch.setColor(color);

            batch.begin();
            batch.draw(texture, gameCamera.position.x - gameCamera.viewportWidth / 2, gameCamera.position.y - gameCamera.viewportHeight / 2);
            batch.end();

            batch.setColor(pastColor);
        } else if (texture != null){
            texture.dispose();
            texture = null;
        }
    }

    private void renderUi(float delta){
        uiViewport.apply();
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);

        uiController.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        uiViewport.update(width, height, true);
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
        uiController.dispose();
        mapController.dispose();
        main.dispose();
    }
}
