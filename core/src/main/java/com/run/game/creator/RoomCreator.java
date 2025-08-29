package com.run.game.creator;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.run.game.RoomName;
import com.run.game.component.navigation.RoomComponent;

import map.creator.map.component.body.BodyComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.factory.body.BodyFactory;
import map.creator.map.factory.body.BodyParam;
import map.creator.map.factory.body.FormBody;
import map.creator.map.factory.body.UserData;
import map.creator.map.factory.object.ObjectCreator;

import java.util.Map;

public class RoomCreator implements ObjectCreator{

    @Override
    public ObjectEntity createObject(String name, MapProperties mapProperties, Map<String, MapProperties> dataObjects, BodyFactory bodyFactory, FormBody formBody, Shape2D bounds) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.density = 1f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = bodyFactory.createCollision(
            new BodyParam.BodyParamBuilder()
                .bodyDef(bodyDef)
                .fixtureDef(fixtureDef)
                .formBody(formBody)
                .bounds(bounds)
                .userData(new UserData(name, "room", name))
            .build()
        );

        ObjectEntity entity = new ObjectEntity(name, "room");
        entity
            .add(new BodyComponent(body, name))
            .add(new RoomComponent(
                name, RoomName.getRoomNameByString(name), (Rectangle) bounds, bodyFactory.getUnitScale()
            ));

        return entity;
    }

}
