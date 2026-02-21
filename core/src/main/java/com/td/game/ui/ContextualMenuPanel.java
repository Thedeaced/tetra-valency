package com.td.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.td.game.pillars.PillarType;

public class ContextualMenuPanel {
    private boolean active = false;
    private float x, y;
    private BitmapFont font;

    private final PillarType[] options = PillarType.values();
    private final Rectangle[] hitboxes;
    private int hoveredIndex = -1;
    private final Texture bgTexture;
    private final GlyphLayout layout;

    private float scale = 1f;

    public ContextualMenuPanel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        bgTexture = new Texture(pixmap);
        pixmap.dispose();

        hitboxes = new Rectangle[options.length];
        for (int i = 0; i < options.length; i++) {
            hitboxes[i] = new Rectangle();
        }
        layout = new GlyphLayout();
    }

    public void setFont(BitmapFont font, SpriteBatch batch) {
        this.font = font;
    }

    public void show(float x, float y, boolean isNewPillar) {
        this.x = x;
        this.y = y;
        this.active = true;
        this.hoveredIndex = -1;

        scale = Math.min(Gdx.graphics.getWidth() / 1920f, Gdx.graphics.getHeight() / 1080f);

        float boxW = 280 * scale;
        float boxH = 50 * scale;
        float gap = 5 * scale;

        float startY = y + 20 * scale;
        float startX = x - boxW / 2;

        for (int i = 0; i < options.length; i++) {
            hitboxes[i].set(startX, startY + i * (boxH + gap), boxW, boxH);
        }
    }

    public void hide() {
        active = false;
        hoveredIndex = -1;
    }

    public boolean isActive() {
        return active;
    }

    public int getSelection() {
        if (!active)
            return -1;
        int mx = Gdx.input.getX();
        int my = Gdx.graphics.getHeight() - Gdx.input.getY();
        for (int i = 0; i < options.length; i++) {
            if (hitboxes[i].contains(mx, my)) {
                return i;
            }
        }
        return -1;
    }

    public PillarType getSelectedType() {
        int sel = getSelection();
        if (sel >= 0 && sel < options.length) {
            return options[sel];
        }
        return null;
    }

    public void render(SpriteBatch batch) {
        if (!active || font == null)
            return;

        batch.begin();
        for (int i = 0; i < options.length; i++) {
            Rectangle rect = hitboxes[i];

            Color bgColor = (i == hoveredIndex) ? new Color(0.2f, 0.4f, 0.6f, 0.9f)
                    : new Color(0.1f, 0.1f, 0.15f, 0.9f);
            batch.setColor(bgColor);
            batch.draw(bgTexture, rect.x, rect.y, rect.width, rect.height);

            batch.setColor(Color.LIGHT_GRAY);
            batch.draw(bgTexture, rect.x, rect.y, rect.width, 2);
            batch.draw(bgTexture, rect.x, rect.y, 2, rect.height);
            batch.draw(bgTexture, rect.x, rect.y + rect.height - 2, rect.width, 2);
            batch.draw(bgTexture, rect.x + rect.width - 2, rect.y, 2, rect.height);

            PillarType type = options[i];
            String text = type.getDisplayName() + " (" + type.getPrice() + "G)";

            font.setColor(Color.WHITE);
            float oldScaleX = font.getScaleX();
            float oldScaleY = font.getScaleY();
            font.getData().setScale(scale * 0.54f);

            layout.setText(font, text);
            font.draw(batch, text, rect.x + (rect.width - layout.width) / 2,
                    rect.y + (rect.height + layout.height) / 2);

            font.getData().setScale(oldScaleX, oldScaleY);
        }
        batch.end();
        batch.setColor(Color.WHITE);
    }

    public void updateHover(int mx, int my) {
        if (!active)
            return;
        hoveredIndex = -1;
        for (int i = 0; i < options.length; i++) {
            if (hitboxes[i].contains(mx, my)) {
                hoveredIndex = i;
                break;
            }
        }
    }

    public void dispose() {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
    }
}

