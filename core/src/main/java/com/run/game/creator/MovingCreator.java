package com.run.game.creator;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.run.game.RoomName;
import com.run.game.component.navigation.MovingComponent;
import com.run.game.component.navigation.TeleportComponent;
import com.run.game.system.NoteLabelSystem;
import com.run.game.utils.music.MusicManager;
import map.creator.map.component.body.BodyComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.factory.body.BodyFactory;
import map.creator.map.factory.body.BodyParam;
import map.creator.map.factory.body.FormBody;
import map.creator.map.factory.body.UserData;
import map.creator.map.factory.object.ObjectCreator;

import java.util.Map;

public class MovingCreator implements ObjectCreator {

    @Override
    public ObjectEntity createObject(String name, MapProperties properties, Map<String, MapProperties> dataObjects, BodyFactory bodyFactory, FormBody formBody, Shape2D boundsObject) {
        ObjectEntity entity = new ObjectEntity(name, "moving");

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        BodyParam param = new BodyParam.BodyParamBuilder()
            .formBody(FormBody.RECTANGLE)
            .bounds(boundsObject)
            .bodyDef(bodyDef)
            .fixtureDef(fixtureDef)
            .userData(new UserData(name, "moving", name))
            .build();

        RoomName from_where = RoomName.getRoomNameByString(properties.get("from_where", String.class));
        RoomName where = RoomName.getRoomNameByString(properties.get("where", String.class));

        Vector2 teleportPosition = new Vector2();
        for (Map.Entry<String, MapProperties> displacement : dataObjects.entrySet()) {
            if (!displacement.getKey().contains("displacement")) continue;
            MapProperties data = displacement.getValue();

            if (data.containsKey("from_where") &&
                data.containsKey("where") &&
                data.get("from_where", String.class).equals(from_where.name()) &&
                data.get("where", String.class).equals(where.name())) {

                teleportPosition.set(
                    data.get("x", Float.class),
                    data.get("y", Float.class)
                );

            }
        }

        teleportPosition.scl(bodyFactory.getUnitScale());

        entity
            .add(new MovingComponent(
                name,
                from_where,
                where
            ))
            .add(
                new BodyComponent(
                    bodyFactory.createCollision(param),
                    name
                ))
            .add(new TeleportComponent(
                name,
                teleportPosition,
                properties.containsKey("block_if_no_notes") ? properties.get("block_if_no_notes", Integer.class) : -1
                )
            );

        return entity;
    }

}
