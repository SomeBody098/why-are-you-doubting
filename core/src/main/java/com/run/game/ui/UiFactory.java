package com.run.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.Main;
import com.run.game.ui.action.ScreenSwitchAction;
import com.run.game.ui.obj.ProgressivelyLabel;
import com.run.game.ui.obj.joystick.Joystick;
import com.run.game.utils.music.MusicManager;
import com.run.game.utils.param.ParamFactory;
import com.run.game.utils.param.BoundsTextParam;
import com.run.game.utils.param.BoundsParam;

public class UiFactory implements Disposable {

    private final float WIGHT = 640;
    private final float HEIGHT = 320;

    private final Camera uiCamera;

    private final Viewport viewport;
    private final Batch batch;

    private final Skin skin;

    public UiFactory(Batch batch) {
        uiCamera = new OrthographicCamera(WIGHT, HEIGHT);
        viewport = new ExtendViewport(WIGHT, HEIGHT, uiCamera);
        this.batch = batch;

        skin = new Skin();
        skin.addRegions(new TextureAtlas("ui/uiskin.atlas"));
        skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    public Stage createMainMenuStage(Main game, Screen targetScreen){
        Stage mainMenu = new Stage(viewport, batch);

        Table table = createTable();
        TextButton startButton = createStartButton(game, targetScreen);
        ProgressivelyLabel gameMenuLabel = createGameMenuLabel();

        table.add(gameMenuLabel)
            .expand()
            .fill();
        table.row();
        table.add(startButton)
            .width(200)
            .height(100)
            .center()
            .expand()
            .fill();

        mainMenu.addActor(table);

        return mainMenu;
    }

    public Stage createLoadingStage(){
        Stage mainMenu = new Stage(viewport, batch);

        Table table = createTable();
        table.add(createLoadingLabel()).center().fill();

        mainMenu.addActor(table);

        return mainMenu;
    }

    public Stage createGameUiStage(MusicManager musicManager){
        Stage gameUi = new Stage(viewport, batch);

        Table table = createTable();
        Joystick joystick = createJoystick(musicManager);
        ProgressivelyLabel noteLabel = createNoteLabel();
        noteLabel.setFontScale(0.75f);

        table.add(noteLabel)
            .top()
            .pad(20)
            .expandX()
            .fill();
        table.row();
        table.add(joystick)
            .width(100)
            .height(100)
            .bottom()
            .expand()
            .fill()
            .left()
            .pad(20);

        gameUi.addListener(joystick.getInputHandler());
        gameUi.addActor(table);

        return gameUi;
    }

    private TextButton createStartButton(Main game, Screen targetScreen){
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

    public ProgressivelyLabel createNoteLabel(){
        BoundsTextParam param = ParamFactory.getUiTextParam("note");
        ProgressivelyLabel label = new ProgressivelyLabel(param.text, skin, "window", -1, 0.5f);
        label.setName("note");
        label.setAlignment(Align.center);
        label.setVisible(false);
        setStandardBoundsForUiObject(label, param);

        return label;
    }

    public Joystick createJoystick(MusicManager musicManager){
        BoundsParam param = ParamFactory.getUiParam("joystick");
        Joystick joystick = new Joystick();

        joystick.createBounds(
            musicManager,
            75,
            75,
            param.wight_percent * WIGHT
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
            param.position_x_percent * WIGHT - wight / 2,
            param.position_y_percent * HEIGHT - height / 2,
            wight, height
        );
    }

    private Table createTable(){
        Table table = new Table();
        table.setName("table");
        table.setPosition(uiCamera.position.x - uiCamera.viewportWidth / 2, uiCamera.position.y - uiCamera.viewportHeight / 2);
        table.setSize(uiCamera.viewportWidth, uiCamera.viewportHeight);

        table.setFillParent(true);

        return table;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
