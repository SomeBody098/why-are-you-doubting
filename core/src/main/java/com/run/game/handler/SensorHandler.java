package com.run.game.handler;

import com.run.game.map.MapController;
import com.run.game.dto.MovingDto;
import com.run.game.dto.SensorDto;
import com.run.game.utils.exception.UnexpectedBehaviorException;

public class SensorHandler {

    private final MapController mapController;

    public SensorHandler(MapController mapController) {
        this.mapController = mapController;
    }

    public void handler(SensorDto dto){
        switch (dto.getName()){
            case "moving":
                handlerMoving((MovingDto) dto);
                break;

            default:
                throw new UnexpectedBehaviorException("Unknown name sensor dto.");
        }
    }

    private void handlerMoving(MovingDto dto){
        mapController.update(dto.getWhere());
    }

    public void endContact(SensorDto dto){

    }

}
