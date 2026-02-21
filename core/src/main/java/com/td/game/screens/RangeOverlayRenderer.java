package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.td.game.elements.Element;
import com.td.game.pillars.Pillar;
import com.td.game.player.Player;

final class RangeOverlayRenderer {
    private RangeOverlayRenderer() {
    }

    static void drawPillarRange(ShapeRenderer shapeRenderer, PerspectiveCamera camera, Pillar pillar, int mapAreaWidth,
            int mapAreaHeight) {
        float radius = pillar.getAttackRange();
        Vector3 center = pillar.getPosition();
        int segments = 100;
        float y = 0.2f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(2f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.08f, 0.95f, 1f, 0.9f);
        for (int i = 0; i < segments; i++) {
            float a0 = MathUtils.PI2 * i / segments;
            float a1 = MathUtils.PI2 * (i + 1) / segments;

            Vector3 p0 = new Vector3(center.x + MathUtils.cos(a0) * radius, y, center.z + MathUtils.sin(a0) * radius);
            Vector3 p1 = new Vector3(center.x + MathUtils.cos(a1) * radius, y, center.z + MathUtils.sin(a1) * radius);
            camera.project(p0, 0, 0, mapAreaWidth, mapAreaHeight);
            camera.project(p1, 0, 0, mapAreaWidth, mapAreaHeight);
            shapeRenderer.line(p0.x, p0.y, p1.x, p1.y);
        }
        shapeRenderer.end();

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    static void drawAuraRange(ShapeRenderer shapeRenderer, PerspectiveCamera camera, Player player, Array<Pillar> pillars,
            float radius, float uiScale, int mapAreaWidth, int mapAreaHeight, Element equippedElement) {
        Vector3 center = player.getPosition();
        int segments = 120;
        float y = 0.16f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(3f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (equippedElement != null) {
            shapeRenderer.setColor(equippedElement.getR(), equippedElement.getG(), equippedElement.getB(), 0.8f);
        } else {
            shapeRenderer.setColor(0.35f, 0.75f, 1f, 0.8f);
        }

        for (int i = 0; i < segments; i++) {
            float a0 = MathUtils.PI2 * i / segments;
            float a1 = MathUtils.PI2 * (i + 1) / segments;

            Vector3 p0 = new Vector3(center.x + MathUtils.cos(a0) * radius, y, center.z + MathUtils.sin(a0) * radius);
            Vector3 p1 = new Vector3(center.x + MathUtils.cos(a1) * radius, y, center.z + MathUtils.sin(a1) * radius);
            camera.project(p0, 0, 0, mapAreaWidth, mapAreaHeight);
            camera.project(p1, 0, 0, mapAreaWidth, mapAreaHeight);
            shapeRenderer.line(p0.x, p0.y, p1.x, p1.y);
        }

        shapeRenderer.setColor(1f, 1f, 1f, 0.7f);
        for (Pillar pillar : pillars) {
            if (pillar.getPosition().dst(center) <= radius) {
                Vector3 sp = new Vector3(pillar.getPosition());
                camera.project(sp, 0, 0, mapAreaWidth, mapAreaHeight);
                shapeRenderer.circle(sp.x, sp.y + 10f * uiScale, 24f * uiScale, 30);
            }
        }

        shapeRenderer.end();

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}

