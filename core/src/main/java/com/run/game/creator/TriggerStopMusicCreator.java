package com.run.game.creator;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.run.game.utils.music.MusicManager;

import java.util.Map;

import map.creator.map.component.body.BodyComponent;
import map.creator.map.component.trigger.Trigger;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.factory.body.BodyFactory;
import map.creator.map.factory.body.BodyParam;
import map.creator.map.factory.body.FormBody;
import map.creator.map.factory.body.UserData;
import map.creator.map.factory.object.ObjectCreator;

public class TriggerStopMusicCreator implements ObjectCreator {

    private final MusicManager musicManager;
    private final String nameMusic;

    public TriggerStopMusicCreator(MusicManager musicManager, String nameMusic) {
        this.musicManager = musicManager;
        this.nameMusic = nameMusic;
    }

    @Override
    public ObjectEntity createObject(String name, MapProperties mapProperties, Map<String, MapProperties> map, BodyFactory bodyFactory, FormBody formBody, Shape2D shape2D) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.density = 1;

        ObjectEntity trigger = new ObjectEntity(name, "trigger");
        trigger
            .add(new BodyComponent(
                bodyFactory.createCollision(
                    new BodyParam.BodyParamBuilder()
                        .bounds(shape2D)
                        .formBody(formBody)
                        .bodyDef(bodyDef)
                        .fixtureDef(fixtureDef)
                        .userData(new UserData(name, "trigger", name))
                        .build()
                ),
                name
            ))
            .add(new Trigger(name, name) {
                @Override
                public boolean beginContact(ObjectEntity objectEntity, ObjectEntity objectEntity1, float v) {
                    if (objectEntity.getName().equals("player") || objectEntity.getName().equals("trigger")
                    && objectEntity1.getName().equals("player") || objectEntity1.getName().equals("trigger")) {

                        musicManager.stopMusic(nameMusic);
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean stayContact(ObjectEntity objectEntity, ObjectEntity objectEntity1, float v) {
                    return objectEntity.getName().equals("player") || objectEntity.getName().equals("trigger")
                        && objectEntity1.getName().equals("player") || objectEntity1.getName().equals("trigger");
                }

                @Override
                public boolean endContact(ObjectEntity objectEntity, ObjectEntity objectEntity1, float v) {
                    return objectEntity.getName().equals("player") || objectEntity.getName().equals("trigger")
                        && objectEntity1.getName().equals("player") || objectEntity1.getName().equals("trigger");
                }
            });

        return trigger;
    }
}
