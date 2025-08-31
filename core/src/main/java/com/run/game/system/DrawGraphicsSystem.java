package com.run.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.component.graphics.GraphicsObjectComponent;

import map.creator.map.component.body.BodyComponent;

public class DrawGraphicsSystem extends IteratingSystem {

    private final Batch batch;
    private final Camera camera;
    private final Viewport viewport;

    public DrawGraphicsSystem(Batch batch, Camera camera, Viewport viewport) {
        super(Family.all(GraphicsObjectComponent.class, BodyComponent.class).get());
        this.batch = batch;
        this.camera = camera;
        this.viewport = viewport;
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        GraphicsObjectComponent component = entity.getComponent(GraphicsObjectComponent.class);
        BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        component.draw(batch, bodyComponent.getBody().getPosition());
    }
}
