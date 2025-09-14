package com.run.game.creator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.run.game.component.graphics.GraphicsObjectComponent;
import com.run.game.component.note.NoteComponent;

import java.util.Map;

import map.creator.map.component.body.BodyComponent;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.factory.body.BodyFactory;
import map.creator.map.factory.body.BodyParam;
import map.creator.map.factory.body.FormBody;
import map.creator.map.factory.body.UserData;
import map.creator.map.factory.object.ObjectCreator;

public class NoteCreator implements ObjectCreator {

    private final TextureRegion textureRegion;

    public NoteCreator(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public ObjectEntity createObject(String name, MapProperties mapProperties, Map<String, MapProperties> map, BodyFactory bodyFactory, FormBody formBody, Shape2D shape2D) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        Body body = bodyFactory.createCollision(
            new BodyParam.BodyParamBuilder()
            .bodyDef(bodyDef)
            .fixtureDef(fixtureDef)
            .bounds(shape2D)
            .formBody(formBody)
            .userData(new UserData(name, "note", name))
            .build()
        );

        MapProperties noteProperties = map.get("noteProperty");

        ObjectEntity note = new ObjectEntity(name, "note");
        note.add(new NoteComponent(noteProperties.get("message", String.class), name))
            .add(new GraphicsObjectComponent(name, textureRegion, bodyFactory.getUnitScale(), false))
            .add(new BodyComponent(body, name));

        return note;
    }

}
