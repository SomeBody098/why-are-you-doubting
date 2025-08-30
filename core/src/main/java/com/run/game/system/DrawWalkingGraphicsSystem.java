package com.run.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.component.graphics.GraphicsMovingObjectComponent;
import com.run.game.component.walking.WalkingBodyComponent;

public class DrawWalkingGraphicsSystem extends IteratingSystem {

    private final Batch batch;
    private final Camera camera;
    private final Viewport viewport;

    public DrawWalkingGraphicsSystem(Batch batch, Camera camera, Viewport viewport) {
        super(Family.all(GraphicsMovingObjectComponent.class, WalkingBodyComponent.class).get());
        this.batch = batch;
        this.camera = camera;
        this.viewport = viewport;
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        GraphicsMovingObjectComponent component = entity.getComponent(GraphicsMovingObjectComponent.class);
        WalkingBodyComponent walking = entity.getComponent(WalkingBodyComponent.class);
        component.update(walking.direction);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        component.draw(batch, walking.getPosition());
    }
}
