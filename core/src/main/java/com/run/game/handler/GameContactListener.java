package com.run.game.handler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.run.game.dto.MovingDto;
import com.run.game.dto.Dto;
import com.run.game.dto.PlayerDto;
import com.run.game.dto.SensorDto;

public class GameContactListener implements ContactListener {

    private final SensorHandler sensorHandler;

    public GameContactListener(SensorHandler sensorHandler) {
        this.sensorHandler = sensorHandler;
    }

    @Override
    public void beginContact(Contact contact) {
        Dto aDto = getDtoFromFixture(contact.getFixtureA());
        Dto bDto = getDtoFromFixture(contact.getFixtureB());

        if (aDto == null || bDto == null) return;

        playerHandler(aDto, bDto);
    }

    @Override
    public void endContact(Contact contact) {
        Dto aDto = getDtoFromFixture(contact.getFixtureA());
        Dto bDto = getDtoFromFixture(contact.getFixtureB());

        if (aDto == null || bDto == null) return;

        SensorDto dto;
        if (isSensorDto(aDto)) dto = (MovingDto) aDto;
        else if (isSensorDto(bDto)) dto = (MovingDto) bDto;
        else return;

        sensorHandler.endContact(dto);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private Dto getDtoFromFixture(Fixture fixture){
        if (fixture.getUserData() == null) return null;

        return (Dto) fixture.getUserData();
    }

    private void playerHandler(Dto aDto, Dto bDto){
        PlayerDto playerDTO = null;
        Dto lastDto = null;

        if (isPlayerDto(aDto)) {
            playerDTO = (PlayerDto) aDto;
            lastDto = bDto;

        } else if (isPlayerDto(bDto)) {
            playerDTO = (PlayerDto) bDto;
            lastDto = aDto;
        }

        if (playerDTO == null) return;

        if (isSensorDto(lastDto)) sensorHandler.handler((SensorDto) lastDto, playerDTO);
    }

    private boolean isPlayerDto(Dto dto){
        return dto.getName().equals("player");
    }

    private boolean isSensorDto(Dto dto){
        return dto instanceof SensorDto;
    }
}
