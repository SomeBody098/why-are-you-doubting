package com.run.game.creator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.run.game.component.graphics.GraphicsMovingObjectComponent;
import com.run.game.component.input.PlayerInputHandlerComponent;
import com.run.game.component.note.CountGetNotesComponent;
import com.run.game.component.walking.WalkingBodyComponent;
import com.run.game.dto.JoystickDTO;
import map.creator.map.entity.ObjectEntity;
import map.creator.map.factory.body.BodyFactory;
import map.creator.map.factory.body.BodyParam;
import map.creator.map.factory.body.FormBody;
import map.creator.map.factory.body.UserData;
import map.creator.map.factory.object.ObjectCreator;

import java.util.Map;

public class PlayerCreator implements ObjectCreator {

    private final JoystickDTO dto;

    private final TextureRegion frameRight;
    private final TextureRegion frameLeft;

    public PlayerCreator(JoystickDTO dto, TextureRegion frameLeft, TextureRegion frameRight) {
        this.dto = dto;
        this.frameLeft = frameLeft;
        this.frameRight = frameRight;
    }

    @Override
    public ObjectEntity createObject(String name, MapProperties mapProperties, Map<String, MapProperties> map, BodyFactory bodyFactory, FormBody formBody, Shape2D shape2D) {
        Rectangle rectangle = (Rectangle) shape2D;

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.bullet = true;
        bodyDef.position.set(rectangle.x, rectangle.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        BodyParam param = new BodyParam.BodyParamBuilder()
            .formBody(FormBody.RECTANGLE)
            .bodyDef(bodyDef)
            .fixtureDef(fixtureDef)
            .bounds(rectangle)
            .userData(new UserData(name, name, null))
            .build();

        Body body = bodyFactory.createCollision(param);

        ObjectEntity entity = new ObjectEntity(name, name);
        entity
            .add(new PlayerInputHandlerComponent(name, dto))
            .add(new WalkingBodyComponent(body, name, bodyFactory.getUnitScale()))
            .add(new GraphicsMovingObjectComponent(name, frameLeft, frameRight, bodyFactory.getUnitScale(), true))
            .add(new CountGetNotesComponent(name));

        return entity;
    }
}
