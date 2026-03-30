package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.td.game.TowerDefenseGame;

public class CreditsScreen implements Screen {
    private final TowerDefenseGame game;

    private SpriteBatch batch;
    private ShapeRenderer shapes;
    private BitmapFont font;
    private BitmapFont titleFont;
    private GlyphLayout glyph;

    private Rectangle backBtn;
    private Array<LinkEntry> linkEntries;

    private static final float LINE_GAP = 22f;

    private static class LinkEntry {
        Rectangle rect;
        String url;

        LinkEntry(Rectangle rect, String url) {
            this.rect = rect;
            this.url = url;
        }
    }

    public CreditsScreen(TowerDefenseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = createFont("fonts/font_game_screen.ttf", scaledFontSize(23));
        titleFont = createFont("fonts/font_game_screen.ttf", scaledFontSize(36));
        glyph = new GlyphLayout();
        linkEntries = new Array<>();

        recalcLayout();
        Gdx.input.setInputProcessor(new InputHandler());
    }

    private void recalcLayout() {
        backBtn = new Rectangle(18f, 18f, 140f, 48f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawRect(backBtn, new Color(1f, 1f, 1f, 0.12f));
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        drawRect(backBtn, Color.WHITE);
        shapes.end();

        batch.begin();
        linkEntries.clear();
        renderStaticCredits();
        drawCentered(font, "Back", backBtn.x, backBtn.y + backBtn.height * 0.67f, backBtn.width, Color.WHITE);
        batch.end();
    }

    private void renderStaticCredits() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        drawCentered(titleFont, "Credits", 0f, h - 30f, w, Color.WHITE);

        float leftX = 40f;
        float rightX = w * 0.53f;
        float topY = h - 85f;

        float leftY = topY;
        leftY = drawHeading("Group Members", leftX, leftY);
        leftY = drawLine("- Umit Yusuf GONEN", leftX, leftY, null);
        leftY = drawLine("- Ahmet Efe CANPOLAT", leftX, leftY, null);
        leftY = drawLine("- Burhan TURK", leftX, leftY, null);
        leftY = drawLine("- Onur Yusuf YILMAZ", leftX, leftY, null);
        leftY = drawLine("- Oguzhan YILMAZ", leftX, leftY, null);

        leftY -= 8f;
        leftY = drawHeading("Icons", leftX, leftY);
        leftY = drawLine("- All icons found on Flaticon", leftX, leftY, "https://www.flaticon.com/");

        leftY -= 8f;
        leftY = drawHeading("Fonts", leftX, leftY);
        drawLine("- Paytone One", leftX, leftY, "https://fonts.google.com/specimen/Paytone+One");

        float rightY = topY;
        rightY = drawHeading("Music", rightX, rightY);
        rightY = drawLine("- Desert Oasis Music by STAROSTIN", rightX, rightY, "https://pixabay.com/music/");
        rightY = drawLine("- Elemental Plateau Music by DanielHren", rightX, rightY, "https://pixabay.com/music/");
        rightY = drawLine("- Main Menu Music by Roman_Sol", rightX, rightY, "https://pixabay.com/music/");

        rightY -= 8f;
        rightY = drawHeading("SFX", rightX, rightY);
        rightY = drawLine("- Source Website: Pixabay Sound Effects", rightX, rightY,
                "https://pixabay.com/sound-effects/");
        rightY = drawLine("- Authors: Universfield, Lesiakover, floraphonic", rightX, rightY, null);
        rightY = drawLine("  FoxBoy Tails, EAGLAXLE, Yodguard, AudioPapkin", rightX, rightY, null);
        rightY = drawLine("  Prmodrai, freesound_community", rightX, rightY, null);

        rightY -= 8f;
        rightY = drawHeading("3D Models", rightX, rightY);
        rightY = drawLine("- Quaternius", rightX, rightY, "https://poly.pizza/u/Quaternius");

        rightY -= 8f;
        rightY = drawHeading("Systems Used", rightX, rightY);
        rightY = drawLine("- Java", rightX, rightY, "https://www.java.com/");
        rightY = drawLine("- libGDX", rightX, rightY, "https://libgdx.com/");
        rightY = drawLine("- LWJGL", rightX, rightY, "https://www.lwjgl.org/");
        rightY = drawLine("- Gradle", rightX, rightY, "https://gradle.org/");
        rightY = drawLine("- Dreamlo", rightX, rightY, "http://dreamlo.com/");
        rightY = drawLine("- GitHub", rightX, rightY, "https://github.com/");
    }

    private float drawHeading(String text, float x, float y) {
        drawLeft(font, text, x, y, Color.WHITE);
        return y - LINE_GAP;
    }

    private float drawLine(String text, float x, float y, String url) {
        drawLeft(font, text, x, y, Color.WHITE);
        float nextY = y - (LINE_GAP * 0.95f);

        if (url != null && !url.isEmpty()) {
            drawLeft(font, url, x + 14f, nextY, Color.WHITE);
            glyph.setText(font, url);
            linkEntries.add(new LinkEntry(new Rectangle(x + 14f, nextY - glyph.height, glyph.width, glyph.height + 4f), url));
            nextY -= (LINE_GAP * 0.8f);
        }

        return nextY;
    }

    private void drawRect(Rectangle r, Color c) {
        shapes.setColor(c);
        shapes.rect(r.x, r.y, r.width, r.height);
    }

    private void drawCentered(BitmapFont drawFont, String text, float x, float baselineY, float width, Color color) {
        glyph.setText(drawFont, text);
        drawFont.setColor(color);
        drawFont.draw(batch, text, x + (width - glyph.width) * 0.5f, baselineY);
    }

    private void drawLeft(BitmapFont drawFont, String text, float x, float baselineY, Color color) {
        drawFont.setColor(color);
        drawFont.draw(batch, text, x, baselineY);
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapes.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        if (font != null) {
            font.dispose();
        }
        if (titleFont != null) {
            titleFont.dispose();
        }
        font = createFont("fonts/font_game_screen.ttf", scaledFontSize(23));
        titleFont = createFont("fonts/font_game_screen.ttf", scaledFontSize(36));
        recalcLayout();
    }

    private int scaledFontSize(int baseSize) {
        return Math.max(12, Math.round(baseSize * Gdx.graphics.getHeight() / 1080f));
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
        if (titleFont != null) {
            titleFont.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (shapes != null) {
            shapes.dispose();
        }
    }

    private BitmapFont createFont(String path, int size) {
        FileHandle f = resolveAsset(path);
        if (!f.exists()) {
            return new BitmapFont();
        }

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(f);
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                + "\u00e7\u011f\u0131\u015f\u00f6\u00fc\u00c7\u011e\u0130\u015e\u00d6\u00dc";
        p.size = size;
        p.color = Color.WHITE;

        BitmapFont out = gen.generateFont(p);
        gen.dispose();
        return out;
    }

    private static FileHandle resolveAsset(String name) {
        FileHandle f = Gdx.files.internal(name);
        if (f.exists()) {
            return f;
        }
        f = Gdx.files.internal("assets/" + name);
        if (f.exists()) {
            return f;
        }
        return Gdx.files.internal(name);
    }

    private class InputHandler extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT) {
                return false;
            }

            float y = Gdx.graphics.getHeight() - screenY;

            if (linkEntries != null) {
                for (LinkEntry entry : linkEntries) {
                    if (entry != null && entry.url != null && entry.rect.contains(screenX, y)) {
                        game.audio.playClick();
                        Gdx.net.openURI(entry.url);
                        return true;
                    }
                }
            }

            if (backBtn.contains(screenX, y)) {
                game.audio.playClick();
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return true;
            }
            return false;
        }
    }
}
