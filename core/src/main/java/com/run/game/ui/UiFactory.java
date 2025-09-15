package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.Main;
import com.run.game.ui.action.ScreenSwitchAction;
import com.run.game.ui.obj.ProgressivelyLabel;
import com.run.game.ui.obj.joystick.Joystick;
import com.run.game.utils.music.MusicManager;
import com.run.game.utils.param.ParamFactory;
import com.run.game.utils.param.BoundsTextParam;
import com.run.game.utils.param.BoundsParam;

import map.creator.map.utils.exception.NotInitializedObjectException;

public class UiFactory implements Disposable {

    private final float GLOBAL_WIGHT;
    private final float GLOBAL_HEIGHT;

    private final OrthographicCamera uiCamera;
    private final Viewport viewport;
    private final Batch batch;

    private final Skin skin;

    public UiFactory(OrthographicCamera uiCamera, Viewport viewport, Batch batch) {
        this.uiCamera = uiCamera;
        this.viewport = viewport;
        this.batch = batch;

        GLOBAL_WIGHT = uiCamera.viewportWidth;
        GLOBAL_HEIGHT = uiCamera.viewportHeight;

        skin = new Skin();
        skin.addRegions(new TextureAtlas("ui/uiskin.atlas"));
        skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    public Stage createMainMenuStage(Main game, Screen targetScreen){
        Stage mainMenu = new Stage(viewport, batch);

        mainMenu.addActor(createGameMenuLabel());
        mainMenu.addActor(createStartButton(game, targetScreen));

        return mainMenu;
    }

    public Stage createLoadingStage(){
        Stage mainMenu = new Stage(viewport, batch);

        mainMenu.addActor(createLoadingLabel());

        return mainMenu;
    }

    public Stage createGameUiStage(MusicManager musicManager){
        Stage gameUi = new Stage(viewport, batch);

        Joystick joystick = createJoystick(musicManager);
        gameUi.addActor(joystick);
        gameUi.addActor(createNoteLabel());

        gameUi.addListener(joystick.getInputHandler());

        return gameUi;
    }

    private TextButton createStartButton(Main game, Screen targetScreen){
        isUiCameraInitialized();
        BoundsTextParam param = ParamFactory.getUiTextParam("start-button");

        TextButton button = new TextButton(param.text, skin, "default");
        button.addListener(new ScreenSwitchAction(game, targetScreen));

        setStandardBoundsForUiObject(button, param);

        return button;
    }

    private ProgressivelyLabel createGameMenuLabel(){
        BoundsTextParam param = ParamFactory.getUiTextParam("name-game-label");
        ProgressivelyLabel label = new ProgressivelyLabel(param.text, skin, "window", -1, 0.5f);
        label.setName("name-game-label");
        label.setAlignment(Align.center);
        setStandardBoundsForUiObject(label, param);

        return label;
    }

    private ProgressivelyLabel createNoteLabel(){
        BoundsTextParam param = ParamFactory.getUiTextParam("note");
        ProgressivelyLabel label = new ProgressivelyLabel(param.text, skin, "window", -1, 0.5f);
        label.setName("note");
        label.setAlignment(Align.center);
        label.setVisible(false);
        setStandardBoundsForUiObject(label, param);

        return label;
    }

    private Joystick createJoystick(MusicManager musicManager){
        BoundsParam param = ParamFactory.getUiParam("joystick");
        Joystick joystick = new Joystick();

        float radius = param.wight_percent * joystick.getWightCircle();

        joystick.createBounds(
            musicManager,
            param.position_x_percent * GLOBAL_WIGHT,
            param.position_y_percent * GLOBAL_HEIGHT,
            radius
        );

        joystick.setName("joystick");

        return joystick;
    }

    private Label createLoadingLabel(){
        BoundsParam param = ParamFactory.getUiParam("loading-label");
        Label label = new Label("loading", skin, "window");
        label.setName("loading-label");
        setStandardBoundsForUiObject(label, param);

        return label;
    }

    private void setStandardBoundsForUiObject(Actor uiObject, BoundsParam param){
        float wight = param.wight_percent * uiObject.getWidth();
        float height = param.height_percent * uiObject.getHeight();

        uiObject.setBounds(
            param.position_x_percent * GLOBAL_WIGHT - wight / 2,
            param.position_y_percent * GLOBAL_HEIGHT - height / 2,
            wight, height
        );
    }

    private void isUiCameraInitialized(){
        if (uiCamera == null){
            throw new NotInitializedObjectException("uiCamera is not initialized!");
        }
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
