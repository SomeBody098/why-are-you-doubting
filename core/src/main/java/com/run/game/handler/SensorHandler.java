package com.run.game.handler;

import com.badlogic.gdx.Gdx;
import com.run.game.dto.PlayerDto;
import com.run.game.event.EventBus;
import com.run.game.event.EventType;
import com.run.game.map.MapController;
import com.run.game.dto.MovingDto;
import com.run.game.dto.SensorDto;
import com.run.game.map.RoomManager;
import com.run.game.utils.exception.UnexpectedBehaviorException;

import java.util.function.Consumer;

public class SensorHandler {

    private final EventBus eventBus;

    private final RoomManager roomManager;

    public SensorHandler(EventBus eventBus, RoomManager roomManager) {
        this.eventBus = eventBus;
        this.roomManager = roomManager;
    }

    public void handler(SensorDto sensorDto, PlayerDto playerDto){
        switch (sensorDto.getName()){
            case "moving":
                handlerMoving((MovingDto) sensorDto, playerDto);
                break;

            default:
                throw new UnexpectedBehaviorException("Unknown name sensor dto.");
        }
    }

    private void handlerMoving(MovingDto dto, PlayerDto playerDto){
        eventBus.subscribe(EventType.TeleportPlayerEvent, new Consumer<EventType>() {
            @Override
            public void accept(EventType eventType) {
                roomManager.changeRoom(dto.getWhere(), playerDto.getCurrentRoom());
                Gdx.app.log(eventType.name(), "Teleport player in room - " + dto.getWhere());
            }
        });
    }

    public void endContact(SensorDto dto){

    }

}
