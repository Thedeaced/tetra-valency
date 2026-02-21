package com.td.game.placement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.elements.Element;
import com.td.game.pillars.PillarType;
import com.td.game.utils.Constants;


public class PlacementPreview implements Disposable {

    public enum PlacementMode {
        NONE,
        PILLAR,
        ORB_ON_PILLAR
    }

    private PlacementMode mode;
    private PillarType pillarType;
    private Element orbElement;

    private Vector3 previewPosition;
    private int gridX, gridZ;
    private boolean validPlacement;
    private float range;

    private ShapeRenderer shapeRenderer;

    public PlacementPreview() {
        this.mode = PlacementMode.NONE;
        this.previewPosition = new Vector3();
        this.gridX = 0;
        this.gridZ = 0;
        this.validPlacement = false;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void startPillarPlacement(PillarType type) {
        this.mode = PlacementMode.PILLAR;
        this.pillarType = type;
        this.orbElement = null;
        float baseRange = 5f;
        this.range = baseRange * type.getRangeMult();
    }
    
    public void startOrbPlacement(Element element) {
        this.mode = PlacementMode.ORB_ON_PILLAR;
        this.orbElement = element;
        this.pillarType = null;
    }

    public void cancel() {
        this.mode = PlacementMode.NONE;
        this.pillarType = null;
        this.orbElement = null;
    }

    public void updatePosition(Camera camera, int screenX, int screenY) {
        if (mode == PlacementMode.NONE)
            return;

        Ray ray = camera.getPickRay(screenX, screenY);

        if (Math.abs(ray.direction.y) > 0.0001f) {
            float t = -ray.origin.y / ray.direction.y;
            if (t > 0) {
                float worldX = ray.origin.x + ray.direction.x * t;
                float worldZ = ray.origin.z + ray.direction.z * t;

                float tileSize = Constants.TILE_SIZE;
                gridX = Math.round(worldX / tileSize);
                gridZ = Math.round(worldZ / tileSize);

                
                
                previewPosition.set(gridX * tileSize, 0.1f, gridZ * tileSize);
            }
        }
    }

    public void setValidPlacement(boolean valid) {
        this.validPlacement = valid;
    }

    public void render(Camera camera) {
        if (mode == PlacementMode.NONE)
            return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float tileSize = Constants.TILE_SIZE;
        float half = tileSize / 2;

        
        if (validPlacement) {
            shapeRenderer.setColor(1f, 1f, 0f, 0.5f); 
        } else {
            shapeRenderer.setColor(1f, 0f, 0f, 0.5f); 
        }

        
        
        
        
        

        shapeRenderer.box(
                previewPosition.x - half,
                0.1f, 
                previewPosition.z + half,
                tileSize * 0.95f,
                0f, 
                -tileSize * 0.95f);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(validPlacement ? Color.YELLOW : Color.RED);
        float y = 0.15f;
        shapeRenderer.line(previewPosition.x - half, y, previewPosition.z - half,
                previewPosition.x + half, y, previewPosition.z - half);
        shapeRenderer.line(previewPosition.x + half, y, previewPosition.z - half,
                previewPosition.x + half, y, previewPosition.z + half);
        shapeRenderer.line(previewPosition.x + half, y, previewPosition.z + half,
                previewPosition.x - half, y, previewPosition.z + half);
        shapeRenderer.line(previewPosition.x - half, y, previewPosition.z + half,
                previewPosition.x - half, y, previewPosition.z - half);

        
        if (mode == PlacementMode.PILLAR && range > 0) {
            int segments = 48;
            for (int i = 0; i < segments; i++) {
                float angle1 = (float) (i * 2 * Math.PI / segments);
                float angle2 = (float) ((i + 1) * 2 * Math.PI / segments);
                float x1 = previewPosition.x + (float) Math.cos(angle1) * range;
                float z1 = previewPosition.z + (float) Math.sin(angle1) * range;
                float x2 = previewPosition.x + (float) Math.cos(angle2) * range;
                float z2 = previewPosition.z + (float) Math.sin(angle2) * range;
                shapeRenderer.line(x1, y, z1, x2, y, z2);
            }
        }
        shapeRenderer.end();
    }

    public PlacementMode getMode() {
        return mode;
    }

    public PillarType getPillarType() {
        return pillarType;
    }

    public Vector3 getPreviewPosition() {
        return previewPosition;
    }

    public boolean isValidPlacement() {
        return validPlacement;
    }

    public boolean isActive() {
        return mode != PlacementMode.NONE;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}

