package com.td.game.screens;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.td.game.pillars.Pillar;
import com.td.game.player.Player;
import com.td.game.ui.ContextualMenuPanel;
import com.td.game.utils.Constants;

final class HoverDetector {
    private HoverDetector() {
    }

    static HoverState detect(PerspectiveCamera camera, int screenX, int screenY, float shopWidth, Player player,
            Array<Pillar> pillars, ContextualMenuPanel buildMenu) {
        if (buildMenu.isActive()) {
            return new HoverState(null, false);
        }

        int mapWidth = (int) (com.badlogic.gdx.Gdx.graphics.getWidth() - shopWidth);
        Ray ray = camera.getPickRay(screenX, screenY, 0, 0, mapWidth, com.badlogic.gdx.Gdx.graphics.getHeight());

        BoundingBox pBox = new BoundingBox(
                new Vector3(player.getPosition()).sub(0.4f, 0f, 0.4f),
                new Vector3(player.getPosition()).add(0.4f, 1.8f, 0.4f));
        Vector3 alchemistIntersection = new Vector3();
        boolean hoveringAlchemist = com.badlogic.gdx.math.Intersector.intersectRayBounds(ray, pBox,
                alchemistIntersection);

        Pillar hoveredPillar = null;
        float closestDist = Float.MAX_VALUE;
        for (Pillar pillar : pillars) {
            BoundingBox bb = pillar.getBoundingBox();
            Vector3 intersection = new Vector3();
            if (com.badlogic.gdx.math.Intersector.intersectRayBounds(ray, bb, intersection)) {
                float dist = ray.origin.dst2(intersection);
                if (dist < closestDist) {
                    closestDist = dist;
                    hoveredPillar = pillar;
                }
            }
        }

        if (hoveredPillar == null) {
            float t = (0.1f - ray.origin.y) / ray.direction.y;
            if (t > 0) {
                float worldX = ray.origin.x + ray.direction.x * t;
                float worldZ = ray.origin.z + ray.direction.z * t;
                int gx = Math.round(worldX / Constants.TILE_SIZE);
                int gz = Math.round(worldZ / Constants.TILE_SIZE);
                float snapX = gx * Constants.TILE_SIZE;
                float snapZ = gz * Constants.TILE_SIZE;
                for (Pillar pillar : pillars) {
                    Vector3 pos = pillar.getPosition();
                    if (Math.abs(pos.x - snapX) < 0.5f && Math.abs(pos.z - snapZ) < 0.5f) {
                        hoveredPillar = pillar;
                        break;
                    }
                }
            }
        }

        return new HoverState(hoveredPillar, hoveringAlchemist);
    }

    static final class HoverState {
        final Pillar hoveredPillar;
        final boolean hoveringAlchemist;

        HoverState(Pillar hoveredPillar, boolean hoveringAlchemist) {
            this.hoveredPillar = hoveredPillar;
            this.hoveringAlchemist = hoveringAlchemist;
        }
    }
}

