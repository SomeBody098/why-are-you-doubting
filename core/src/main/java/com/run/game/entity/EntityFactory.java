package com.run.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.run.game.dto.EmptyDto;
import com.run.game.dto.JoystickDTO;
import com.run.game.dto.PlayerDto;
import com.run.game.entity.player.Player;
import com.run.game.entity.player.PlayerBody;
import com.run.game.entity.player.PlayerGraphics;
import com.run.game.entity.player.PlayerInputHandler;
import com.run.game.utils.exception.NotInitializedObjectException;

public class EntityFactory {

    private static float PPM;
    private static float UNIT_SCALE;

    private static World world;

    public static void init(World world, float PPM, float UNIT_SCALE){
        EntityFactory.world = world;
        EntityFactory.PPM = PPM;
        EntityFactory.UNIT_SCALE = UNIT_SCALE;
    }

    public static Player createPlayer(JoystickDTO joystickDTO, Vector2 startPosition){
        isWorldInit();

        PlayerGraphics graphics = new PlayerGraphics();
        PlayerInputHandler inputHandler = new PlayerInputHandler();

        PlayerDto dto = new PlayerDto();

        PlayerBody body = new PlayerBody(
            startPosition.x * UNIT_SCALE,
            startPosition.y * UNIT_SCALE,
            graphics.getWidth(),
            graphics.getHeight(),
            UNIT_SCALE,
            world,
            dto
        );

        return new Player(body, dto, inputHandler, graphics, joystickDTO, PPM, UNIT_SCALE);
    }

    private static void isWorldInit(){
        if (world == null) throw new NotInitializedObjectException("World is not initialized in EntityFactory!");
    }
}
