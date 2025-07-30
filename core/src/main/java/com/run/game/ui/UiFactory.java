package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.Main;
import com.run.game.ui.action.ScreenSwitchAction;
import com.run.game.ui.obj.ProgressivelyLabel;
import com.run.game.ui.obj.joystick.Joystick;
import com.run.game.utils.exception.NotInitializedObjectException;
import com.run.game.utils.param.ParamFactory;
import com.run.game.utils.param.BoundsTextParam;
import com.run.game.utils.param.BoundsParam;

public class UiFactory {

    private static float GLOBAL_WIGHT;
    private static float GLOBAL_HEIGHT;

    private static OrthographicCamera uiCamera;
    private static Viewport viewport;
    private static Batch batch;

    private static Skin skin;

    public static void init(OrthographicCamera uiCamera, Viewport viewport, Batch batch){
        UiFactory.uiCamera = uiCamera;
        UiFactory.viewport = viewport;
        UiFactory.batch = batch;

        GLOBAL_WIGHT = uiCamera.viewportWidth;
        GLOBAL_HEIGHT = uiCamera.viewportHeight;

        skin = new Skin();
        skin.addRegions(new TextureAtlas("ui/uiskin.atlas"));
        skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    public static Stage createMainMenuStage(Main game, Screen targetScreen){
        Stage mainMenu = new Stage(viewport, batch);

        mainMenu.addActor(createGameMenuLabel());
        mainMenu.addActor(createStartButton(game, targetScreen));

        return mainMenu;
    }

    public static Stage createLoadingStage(float min, float max, float stepSize){
        Stage mainMenu = new Stage(viewport, batch);

        mainMenu.addActor(createProgressBar(min, max, stepSize));

        return mainMenu;
    }

    public static Stage createGameUiStage(){
        Stage gameUi = new Stage(viewport, batch);

        Joystick joystick = createJoystick();
        gameUi.addActor(joystick);
        gameUi.addActor(createButtonInteract());

        gameUi.addListener(joystick.getInputHandler());

        return gameUi;
    }

    private static TextButton createStartButton(Main game, Screen targetScreen){
        isUiCameraInitialized();
        BoundsTextParam param = ParamFactory.getUiTextParam("start-button");

        TextButton button = new TextButton(param.text, skin, "default");
        button.addListener(new ScreenSwitchAction(game, targetScreen));

        setStandardBoundsForUiObject(button, param);

        return button;
    }

    private static ProgressivelyLabel createGameMenuLabel(){
        BoundsTextParam param = ParamFactory.getUiTextParam("name-game-label");
        ProgressivelyLabel label = new ProgressivelyLabel(param.text, skin, "window", -1);

        label.setAlignment(Align.center);
        setStandardBoundsForUiObject(label, param);

        return label;
    }

    private static TextButton createButtonInteract(){
        BoundsTextParam param = ParamFactory.getUiTextParam("button-interact");
        TextButton button = new TextButton(param.text, skin, "default");

        setStandardBoundsForUiObject(button, param);

        return button;
    }

    private static Joystick createJoystick(){
        BoundsParam param = ParamFactory.getUiParam("joystick");
        Joystick joystick = new Joystick();

        float radius = param.wight_percent * joystick.getWightCircle();

        joystick.createBounds(
            param.position_x_percent * GLOBAL_WIGHT,
            param.position_y_percent * GLOBAL_HEIGHT,
            radius
        );

        return joystick;
    }

    private static ProgressBar createProgressBar(float min, float max, float stepSize){
        BoundsParam param = ParamFactory.getUiParam("progress-bar");
        ProgressBar progressBar = new ProgressBar(min, max, stepSize, false, skin, "default-horizontal");

        setStandardBoundsForUiObject(progressBar, param);

        return progressBar;
    }

    private static void setStandardBoundsForUiObject(Actor uiObject, BoundsParam param){
        float wight = param.wight_percent * uiObject.getWidth();
        float height = param.height_percent * uiObject.getHeight();

        uiObject.setBounds(
            param.position_x_percent * GLOBAL_WIGHT - wight / 2,
            param.position_y_percent * GLOBAL_HEIGHT - height / 2,
            wight, height
        );
    }

    private static void isUiCameraInitialized(){
        if (uiCamera == null){
            throw new NotInitializedObjectException("uiCamera is not initialized!");
        }
    }
}
