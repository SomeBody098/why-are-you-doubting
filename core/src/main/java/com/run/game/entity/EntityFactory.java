package com.run.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.dto.EmptyDto;
import com.run.game.dto.JoystickDTO;
import com.run.game.entity.player.Player;
import com.run.game.entity.player.PlayerBody;
import com.run.game.entity.player.PlayerGraphics;
import com.run.game.entity.player.PlayerInputHandler;
import com.run.game.utils.exception.NotInitializedObjectException;
import com.run.game.utils.param.BoundsParam;
import com.run.game.utils.param.ParamFactory;

public class EntityFactory {

    private static float WIGHT;
    private static float HEIGHT;

    private static OrthographicCamera gameCamera;
    private static Viewport viewport;
    private static Batch batch;

    private static World world;

    public static void init(OrthographicCamera gameCamera, Viewport viewport, Batch batch, World world){
        EntityFactory.gameCamera = gameCamera;
        EntityFactory.viewport = viewport;
        EntityFactory.batch = batch;
        EntityFactory.world = world;

        WIGHT = gameCamera.viewportWidth;
        HEIGHT = gameCamera.viewportHeight;
    }

    public static Stage createStageGame(JoystickDTO joystickDTO){
        Stage stageGame = new Stage(viewport, batch);

        stageGame.addActor(createPlayer(joystickDTO));

        return stageGame;
    }

    private static Player createPlayer(JoystickDTO joystickDTO){
        isWorldInit();

        BoundsParam param = ParamFactory.getEntityParam("player");

        PlayerGraphics graphics = new PlayerGraphics();
        PlayerInputHandler inputHandler = new PlayerInputHandler();

        PlayerBody body = new PlayerBody(
            param.position_x_percent * WIGHT,
            param.position_y_percent * HEIGHT,
            param.wight_percent * graphics.getWidth() * 2,
            param.height_percent * graphics.getHeight() * 2,
            world,
            new EmptyDto("player")
        );

        return new Player(body, inputHandler, graphics, joystickDTO);
    }

    private static void isWorldInit(){
        if (world == null) throw new NotInitializedObjectException("World is not initialized in EntityFactory!");
    }
}
