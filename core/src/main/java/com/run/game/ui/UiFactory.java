package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.Main;
import com.run.game.ui.action.ScreenSwitchAction;
import com.run.game.utils.exception.NotInitializedObjectException;
import com.run.game.utils.param.ParamFactory;
import com.run.game.utils.param.UiTextParam;
import com.run.game.utils.param.UiParam;

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

        Gdx.app.log("global", "GLOBAL_WIGHT = " + GLOBAL_WIGHT + ", GLOBAL_HEIGHT = " + GLOBAL_HEIGHT);

        skin = new Skin();
        skin.addRegions(new TextureAtlas("ui/uiskin.atlas"));
        skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    public static Stage createMainMenuStage(Main game, Screen targetScreen){
        Stage mainMenu = new Stage(viewport, batch);

        mainMenu.addActor(createStartButton(game, targetScreen));
        mainMenu.addActor(createLabel());

        return mainMenu;
    }

    public static Stage createGameUiStage(){
        Stage gameUi = new Stage(viewport, batch);

        return gameUi;
    }

    private static TextButton createStartButton(Main game, Screen targetScreen){
        isUiCameraInitialized();
        UiTextParam param = ParamFactory.getUiTextParam("start-button");

        TextButton button = new TextButton(param.text, skin, "default");
        button.addListener(new ScreenSwitchAction(game, targetScreen));

        setStandardBoundsForUiObject(button, param);

        return button;
    }

    private static Label createLabel(){
        UiTextParam param = ParamFactory.getUiTextParam("name-game-label");
        Label label = new Label(param.text, skin, "bitter");

        label.setAlignment(Align.center);
//        setStandardBoundsForUiObject(label, param);
        label.setBounds(100, 100, 200, 50);
        label.setWrap(true);

        Gdx.app.log("LabelDebug", "Text: " + param.text + ", Visible: " + label.isVisible() +
            ", Scale: " + label.getScaleX() + "x" + label.getScaleY());

        return label;
    }

    private static void setStandardBoundsForUiObject(Actor uiObject, UiParam param){
        float wight = param.wight_percent * uiObject.getWidth();
        float height = param.height_percent * uiObject.getHeight();

        uiObject.setBounds(
            param.position_x_percent * GLOBAL_WIGHT - wight / 2,
            param.position_y_percent * GLOBAL_HEIGHT - height / 2,
            wight, height
        );

        float x = param.position_x_percent * GLOBAL_WIGHT - wight / 2;
        float y = param.position_y_percent * GLOBAL_HEIGHT - height / 2;

        Gdx.app.log("bounds", "x - " + x + ", y - " + y + ", wight - " + wight + ", height - " + height);
    }

    private static void isUiCameraInitialized(){
        if (uiCamera == null){
            throw new NotInitializedObjectException("uiCamera is not initialized!");
        }
    }
}
