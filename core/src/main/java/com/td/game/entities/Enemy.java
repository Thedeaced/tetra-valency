package com.td.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.elements.Element;
import com.td.game.utils.Constants;

public class Enemy implements Disposable {

    protected Vector3 position;
    protected float health;
    protected float maxHealth;
    protected float speed;
    protected float baseSpeed;
    protected int reward;
    protected boolean alive;
    protected boolean reachedEnd;
    protected boolean isFlying;

    protected Array<Vector3> waypoints;
    protected int currentWaypointIndex;
    protected float rotation;

    protected Model model;
    protected ModelInstance modelInstance;
    protected Element element;

    protected float modelScaleMultiplier = 1f;

    public Enemy(float maxHealth, float speed, int reward) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        float scaleMultiplier = Constants.TILE_SIZE / 2.0f;
        this.baseSpeed = speed * scaleMultiplier;
        this.speed = speed * scaleMultiplier;
        this.reward = reward;
        this.alive = true;
        this.reachedEnd = false;
        this.isFlying = false;
        this.currentWaypointIndex = 0;
        this.position = new Vector3();
    }

    public void setModel(Model model) {
        this.model = model;
        this.modelInstance = new ModelInstance(model);
        for (int i = 0; i < modelInstance.materials.size; i++) {
            modelInstance.materials.set(i, modelInstance.materials.get(i).copy());
        }

        com.badlogic.gdx.math.collision.BoundingBox bb = new com.badlogic.gdx.math.collision.BoundingBox();
        modelInstance.calculateBoundingBox(bb);
        Vector3 dim = new Vector3();
        bb.getDimensions(dim);
        float maxDim = Math.max(dim.x, Math.max(dim.y, dim.z));
        if (maxDim > 0.001f) {
            float baseToCurrentRatio = Constants.TILE_SIZE / 2.0f;
            float targetSize = 2.0f * 0.5f * baseToCurrentRatio;
            this.modelScaleMultiplier = targetSize / maxDim;
        } else {
            this.modelScaleMultiplier = Constants.TILE_SIZE / 2.0f;
        }
    }

    public void setElement(Element element) {
        this.element = element;
        if (this.element != null && this.modelInstance != null) {
            Color eleColor = new Color(element.getR(), element.getG(), element.getB(), 1f);

            for (com.badlogic.gdx.graphics.g3d.Material mat : this.modelInstance.materials) {
                com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute ca = (com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute) mat
                        .get(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.Diffuse);

                if (ca != null) {
                    if (mat.id.toLowerCase(java.util.Locale.ROOT).contains("green")
                            || (ca.color.g > ca.color.r * 1.5f && ca.color.g > ca.color.b * 1.5f)) {
                        ca.color.set(eleColor);
                    } else if (!isFlying) {
                        float brightness = (ca.color.r + ca.color.g + ca.color.b) / 3f;
                        if (brightness > 0.1f) {
                            ca.color.set(eleColor);
                        }
                    }
                }
            }
        }
    }

    public Element getElement() {
        return element;
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if (alive && modelInstance != null) {
            modelBatch.render(modelInstance, environment);
        }
    }

    public void setWaypoints(Array<Vector3> waypoints) {
        this.waypoints = waypoints;
        if (waypoints.size > 0) {
            this.position.set(waypoints.first());
            this.currentWaypointIndex = 0;
        }
    }

    public void update(float deltaTime) {
        if (!alive || waypoints == null || waypoints.size == 0)
            return;

        if (currentWaypointIndex < waypoints.size) {
            Vector3 target = waypoints.get(currentWaypointIndex);
            Vector3 direction = target.cpy().sub(position).nor();

            if (direction.len2() > 0.01f) {
                float targetRotation = (float) Math.toDegrees(Math.atan2(-direction.x, -direction.z));
                
                float diff = targetRotation - this.rotation;
                while (diff > 180)
                    diff -= 360;
                while (diff < -180)
                    diff += 360;
                this.rotation += diff * 12f * deltaTime;
            }

            position.add(direction.scl(speed * deltaTime));

            if (position.dst(target) < 0.2f) {
                currentWaypointIndex++;

                if (currentWaypointIndex >= waypoints.size) {
                    reachedEnd = true;
                    alive = false;
                }
            }
        }

        updateModelPosition();
    }

    protected void updateModelPosition() {
        if (modelInstance != null) {
            float scale = Constants.TILE_SIZE / 2.0f;
            float yOffset = isFlying ? 2f * scale : 0.25f * scale;
            
            modelInstance.transform.setToTranslation(position.x, position.y + yOffset, position.z);
            modelInstance.transform.scl(modelScaleMultiplier);
            modelInstance.transform.rotate(Vector3.Y, rotation + 180f);

            modelInstance.calculateTransforms();
        }
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    public void setFlying(boolean flying) {
        this.isFlying = flying;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getReward() {
        return reward;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealthPercent() {
        return health / maxHealth;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public String getName() {
        return "Enemy";
    }

    @Override
    public void dispose() {
    }
}
