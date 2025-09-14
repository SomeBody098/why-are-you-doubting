package com.run.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.run.game.component.graphics.AnimationPlayerMovingObjectComponent;
import com.run.game.component.graphics.GraphicsMovingObjectComponent;
import com.run.game.component.walking.WalkingBodyComponent;

public class DrawWalkingAnimationGraphicsSystem extends IteratingSystem {

    private final Batch batch;
    private final Camera camera;
    private final Viewport viewport;

    public DrawWalkingAnimationGraphicsSystem(Batch batch, Camera camera, Viewport viewport) {
        super(Family.all(AnimationPlayerMovingObjectComponent.class, WalkingBodyComponent.class).get());
        this.batch = batch;
        this.camera = camera;
        this.viewport = viewport;
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        AnimationPlayerMovingObjectComponent component = entity.getComponent(AnimationPlayerMovingObjectComponent.class);
        WalkingBodyComponent walking = entity.getComponent(WalkingBodyComponent.class);
        component.update(delta, walking.getDirection(), walking.isWalk());

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        component.draw(batch, walking.getPosition());
    }
}
