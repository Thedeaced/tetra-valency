package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.td.game.systems.EconomyManager;
import com.td.game.systems.WaveManager;

public class ConsoleMenu {
    public static class Layout {
        public final Rectangle panel = new Rectangle();
        public final Rectangle livesInputBox = new Rectangle();
        public final Rectangle goldInputBox = new Rectangle();
        public final Rectangle waveBox = new Rectangle();
        public final Rectangle augmentInputBox = new Rectangle();
        public final Rectangle[] buttons;
        public float titleY;
        public float livesLabelY;
        public float goldLabelY;
        public float waveLabelY;

        public Layout(int buttonCount) {
            buttons = new Rectangle[buttonCount];
            for (int i = 0; i < buttonCount; i++) {
                buttons[i] = new Rectangle();
            }
        }
    }

    public void render(boolean open,
                       int screenWidth,
                       int screenHeight,
                       float uiScale,
                       int activeConsoleInput,
                       int inputLives,
                       int inputWave,
                       int inputGold,
                       int inputAugment,
                       String[] buttonLabels,
                       String livesInput,
                       String waveInput,
                       String goldInput,
                       String augmentInput,
                       EconomyManager economyManager,
                       WaveManager waveManager,
                       SpriteBatch uiBatch,
                       ShapeRenderer uiShapeRenderer,
                       BitmapFont uiFont,
                       BitmapFont uiFontLarge,
                       GlyphLayout glyphLayout) {
        if (!open) {
            return;
        }

        Layout layout = getLayout(screenWidth, screenHeight, uiScale, buttonLabels.length);
        float defaultFontScale = uiScale * 0.54f;
        float defaultLargeFontScale = uiScale * 0.72f;

        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);

        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        uiShapeRenderer.setColor(0.05f, 0.05f, 0.05f, 0.92f);
        uiShapeRenderer.rect(layout.panel.x, layout.panel.y, layout.panel.width, layout.panel.height);
        drawInputBox(uiShapeRenderer, layout.livesInputBox, activeConsoleInput == inputLives);
        drawInputBox(uiShapeRenderer, layout.waveBox, activeConsoleInput == inputWave);
        drawInputBox(uiShapeRenderer, layout.goldInputBox, activeConsoleInput == inputGold);
        drawInputBox(uiShapeRenderer, layout.augmentInputBox, activeConsoleInput == inputAugment);
        uiShapeRenderer.setColor(0.16f, 0.16f, 0.16f, 0.98f);
        for (Rectangle button : layout.buttons) {
            uiShapeRenderer.rect(button.x, button.y, button.width, button.height);
        }
        uiShapeRenderer.end();

        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        uiShapeRenderer.setColor(0.95f, 0.95f, 0.95f, 1f);
        uiShapeRenderer.rect(layout.panel.x, layout.panel.y, layout.panel.width, layout.panel.height);
        uiShapeRenderer.rect(layout.livesInputBox.x, layout.livesInputBox.y, layout.livesInputBox.width, layout.livesInputBox.height);
        uiShapeRenderer.rect(layout.goldInputBox.x, layout.goldInputBox.y, layout.goldInputBox.width, layout.goldInputBox.height);
        uiShapeRenderer.rect(layout.waveBox.x, layout.waveBox.y, layout.waveBox.width, layout.waveBox.height);
        uiShapeRenderer.rect(layout.augmentInputBox.x, layout.augmentInputBox.y, layout.augmentInputBox.width, layout.augmentInputBox.height);
        for (Rectangle button : layout.buttons) {
            uiShapeRenderer.rect(button.x, button.y, button.width, button.height);
        }
        uiShapeRenderer.end();

        uiBatch.begin();
        uiFontLarge.getData().setScale(uiScale * 0.54f);
        uiFontLarge.setColor(Color.WHITE);
        glyphLayout.setText(uiFontLarge, "CONSOLE MENU");
        uiFontLarge.draw(uiBatch, glyphLayout,
                layout.panel.x + (layout.panel.width - glyphLayout.width) * 0.5f,
                layout.titleY);

        uiFont.getData().setScale(uiScale * 0.50f);
        uiFont.setColor(Color.WHITE);

        String livesLabel = "HEALTH";
        String livesPlaceholder = "";
        if (economyManager != null) {
            livesLabel = "HEALTH (" + economyManager.getLives() + ")";
            livesPlaceholder = String.valueOf(economyManager.getLives());
        }
        drawFieldLabel(uiBatch, uiFont, glyphLayout, livesLabel, layout.livesLabelY, layout.livesInputBox);
        drawInputField(uiBatch, uiFontLarge, glyphLayout, uiScale, layout.livesInputBox, livesInput, livesPlaceholder);

        String waveLabel = "WAVE";
        String wavePlaceholder = "";
        if (waveManager != null) {
            waveLabel = "WAVE (" + waveManager.getCurrentWave() + ")";
            wavePlaceholder = String.valueOf(waveManager.getCurrentWave());
        }
        drawFieldLabel(uiBatch, uiFont, glyphLayout, waveLabel, layout.waveLabelY, layout.waveBox);
        drawInputField(uiBatch, uiFontLarge, glyphLayout, uiScale, layout.waveBox, waveInput, wavePlaceholder);

        String goldLabel = "GOLD";
        String goldPlaceholder = "";
        if (economyManager != null) {
            goldLabel = "GOLD (" + economyManager.getGold() + ")";
            goldPlaceholder = String.valueOf(economyManager.getGold());
        }
        drawFieldLabel(uiBatch, uiFont, glyphLayout, goldLabel, layout.goldLabelY, layout.goldInputBox);
        drawInputField(uiBatch, uiFontLarge, glyphLayout, uiScale, layout.goldInputBox, goldInput, goldPlaceholder);

        drawInputField(uiBatch, uiFontLarge, glyphLayout, uiScale, layout.augmentInputBox, augmentInput, "");

        uiFontLarge.getData().setScale(uiScale * 0.44f);
        for (int i = 0; i < buttonLabels.length; i++) {
            Rectangle button = layout.buttons[i];
            glyphLayout.setText(uiFontLarge, buttonLabels[i], Color.WHITE, button.width,
                    com.badlogic.gdx.utils.Align.center, false);
            float textY = button.y + button.height * 0.5f + glyphLayout.height * 0.5f;
            uiFontLarge.draw(uiBatch, glyphLayout, button.x, textY);
        }
        uiBatch.end();

        uiFont.getData().setScale(defaultFontScale);
        uiFontLarge.getData().setScale(defaultLargeFontScale);
    }

    public Layout getLayout(int screenWidth, int screenHeight, float uiScale, int buttonCount) {
        Layout layout = new Layout(buttonCount);
        float sideMargin = 24f * uiScale;
        float topMargin = 64f * uiScale;
        float bottomMargin = 0f;
        float panelW = MathUtils.clamp(screenWidth * 0.185f, 210f * uiScale, 300f * uiScale);
        float titleInset = 16f * uiScale;
        float titleBlockH = 20f * uiScale;
        float titleToFirstLabel = 28f * uiScale;
        float inputBoxH = 22f * uiScale;
        float labelToInputGap = 9f * uiScale;
        float sectionGap = 10f * uiScale;
        float buttonGap = 8f * uiScale;
        float buttonH = 30f * uiScale;
        float extraButtonTopGap = 10f * uiScale;
        float panelH = titleInset + titleBlockH + titleToFirstLabel
                + (labelToInputGap + inputBoxH)
                + (sectionGap + labelToInputGap + inputBoxH)
                + (sectionGap + labelToInputGap + inputBoxH)
                + (buttonCount * buttonH)
                + ((buttonCount - 1) * buttonGap)
                + (labelToInputGap + titleBlockH);
        panelH = Math.min(panelH, screenHeight - topMargin - bottomMargin);
        float panelX = sideMargin;
        float panelY = screenHeight - panelH - topMargin;
        layout.panel.set(panelX, panelY, panelW, panelH);

        float innerPadding = 10f * uiScale;
        float contentX = panelX + innerPadding;
        float contentW = panelW - innerPadding * 2f;

        layout.titleY = panelY + panelH - titleInset;
        layout.livesLabelY = layout.titleY - titleToFirstLabel;
        float livesInputY = layout.livesLabelY - labelToInputGap - inputBoxH;
        layout.livesInputBox.set(contentX, livesInputY, contentW, inputBoxH);

        layout.waveLabelY = layout.livesInputBox.y - sectionGap;
        float waveBoxY = layout.waveLabelY - labelToInputGap - inputBoxH;
        layout.waveBox.set(contentX, waveBoxY, contentW, inputBoxH);

        layout.goldLabelY = layout.waveBox.y - sectionGap;
        float goldInputY = layout.goldLabelY - labelToInputGap - inputBoxH;
        layout.goldInputBox.set(contentX, goldInputY, contentW, inputBoxH);

        float buttonY = layout.goldInputBox.y - extraButtonTopGap - buttonH;
        for (Rectangle button : layout.buttons) {
            button.set(contentX, buttonY, contentW, buttonH);
            buttonY -= buttonH + buttonGap;
        }

        if (layout.buttons.length > 0) {
            Rectangle addAugmentButton = layout.buttons[layout.buttons.length - 1];
            float gap = 6f * uiScale;
            float augmentInputW = Math.max(70f * uiScale, contentW * 0.36f);
            float augmentButtonW = Math.max(90f * uiScale, contentW - augmentInputW - gap);
            addAugmentButton.width = augmentButtonW;
            layout.augmentInputBox.set(addAugmentButton.x + addAugmentButton.width + gap, addAugmentButton.y,
                    augmentInputW, addAugmentButton.height);
        }

        return layout;
    }

    public int getButtonIndexAt(int screenX, int flippedY, int screenWidth, int screenHeight, float uiScale, int buttonCount) {
        Layout layout = getLayout(screenWidth, screenHeight, uiScale, buttonCount);
        for (int i = 0; i < layout.buttons.length; i++) {
            Rectangle button = layout.buttons[i];
            if (isInRect(screenX, flippedY, button.x, button.y, button.width, button.height)) {
                return i;
            }
        }
        return -1;
    }

    public int getInputTargetAt(int screenX,
                                int flippedY,
                                int screenWidth,
                                int screenHeight,
                                float uiScale,
                                int inputNone,
                                int inputLives,
                                int inputWave,
                                int inputGold,
                                int inputAugment,
                                int buttonCount) {
        Layout layout = getLayout(screenWidth, screenHeight, uiScale, buttonCount);
        if (isInRect(screenX, flippedY, layout.livesInputBox.x, layout.livesInputBox.y,
                layout.livesInputBox.width, layout.livesInputBox.height)) {
            return inputLives;
        }
        if (isInRect(screenX, flippedY, layout.waveBox.x, layout.waveBox.y, layout.waveBox.width, layout.waveBox.height)) {
            return inputWave;
        }
        if (isInRect(screenX, flippedY, layout.goldInputBox.x, layout.goldInputBox.y,
                layout.goldInputBox.width, layout.goldInputBox.height)) {
            return inputGold;
        }
        if (isInRect(screenX, flippedY, layout.augmentInputBox.x, layout.augmentInputBox.y,
                layout.augmentInputBox.width, layout.augmentInputBox.height)) {
            return inputAugment;
        }
        return inputNone;
    }

    private void drawInputBox(ShapeRenderer uiShapeRenderer, Rectangle box, boolean active) {
        if (active) {
            uiShapeRenderer.setColor(0.18f, 0.18f, 0.18f, 0.98f);
        } else {
            uiShapeRenderer.setColor(0.12f, 0.12f, 0.12f, 0.96f);
        }
        uiShapeRenderer.rect(box.x, box.y, box.width, box.height);
    }

    private void drawFieldLabel(SpriteBatch batch, BitmapFont uiFont, GlyphLayout glyphLayout, String label, float labelY, Rectangle inputBox) {
        glyphLayout.setText(uiFont, label, Color.WHITE, inputBox.width, com.badlogic.gdx.utils.Align.center, false);
        uiFont.draw(batch, glyphLayout, inputBox.x, labelY);
    }

    private void drawInputField(SpriteBatch batch, BitmapFont uiFontLarge, GlyphLayout glyphLayout, float uiScale,
                                Rectangle box, String value, String placeholder) {
        String displayValue = value == null ? "" : value;
        if (displayValue.isEmpty()) {
            displayValue = placeholder == null ? "" : placeholder;
        }
        uiFontLarge.getData().setScale(uiScale * 0.40f);
        glyphLayout.setText(uiFontLarge, displayValue, Color.WHITE, box.width,
                com.badlogic.gdx.utils.Align.center, false);
        float valueY = box.y + box.height * 0.5f + glyphLayout.height * 0.5f;
        uiFontLarge.draw(batch, glyphLayout, box.x, valueY);
    }

    private boolean isInRect(float x, float y, float rx, float ry, float rw, float rh) {
        return x >= rx && x <= rx + rw && y >= ry && y <= ry + rh;
    }
}
