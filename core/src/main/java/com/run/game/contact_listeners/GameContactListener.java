package com.run.game.contact_listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.run.game.dto.DoorDto;
import com.run.game.dto.Dto;
import com.run.game.dto.EmptyDto;

public class GameContactListener implements ContactListener {
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

        DoorDto dto;
        if (isDoorDto(aDto)) dto = (DoorDto) aDto;
        else if (isDoorDto(bDto)) dto = (DoorDto) bDto;
        else return;

        doorReset(dto);
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
        EmptyDto playerDTO = null;
        Dto lastDto = null;

        if (isPlayerDto(aDto)) {
            playerDTO = (EmptyDto) aDto;
            lastDto = bDto;

        } else if (isPlayerDto(bDto)) {
            playerDTO = (EmptyDto) bDto;
            lastDto = aDto;
        }

        if (playerDTO == null) return;

        if (!isDoorDto(lastDto)) doorHandler((DoorDto) lastDto);
    }

    private void doorHandler(DoorDto doorDto){
        doorDto.setPlayerEnters(true);
        doorDto.setBeenEnters(true);
    }

    private void doorReset(DoorDto doorDto){
        doorDto.setPlayerEnters(false);
        doorDto.setBeenEnters(false);
    }

    private boolean isPlayerDto(Dto dto){
        return dto.getName().equals("player");
    }
    private boolean isDoorDto(Dto dto){
        return dto.getName().equals("door");
    }
}
