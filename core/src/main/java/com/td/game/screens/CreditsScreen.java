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
import com.badlogic.gdx.math.MathUtils;
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

    private Rectangle crawlArea;
    private Rectangle backBtn;

    private Array<String> teamMembers;
    private Array<CreditLine> creditLines;
    private Array<LinkEntry> linkEntries;

    private float crawlOffset;
    private float crawlContentHeight;

    private static final float CRAWL_SPEED = 42f;
    private static final float SCROLL_STEP = 38f;
    private static final float SECTION_GAP = 28f;
    private static final float LINE_GAP = 22f;

    private static class CreditLine {
        final String text;
        final String url;
        final boolean heading;

        CreditLine(String text, String url, boolean heading) {
            this.text = text;
            this.url = url;
            this.heading = heading;
        }

        boolean hasLink() {
            return url != null && !url.isEmpty();
        }
    }

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
        font = createFont("fonts/font_game_screen.ttf", scaledFontSize(24));
        titleFont = createFont("fonts/font_game_screen.ttf", scaledFontSize(40));
        glyph = new GlyphLayout();

        teamMembers = new Array<>();
        teamMembers.add("Umit Yusuf GONEN");
        teamMembers.add("Ahmet Efe CANPOLAT");
        teamMembers.add("Burhan TURK");
        teamMembers.add("Onur Yusuf YILMAZ");
        teamMembers.add("Oguzhan YILMAZ");

        creditLines = createCreditLines();
        linkEntries = new Array<>();

        recalcLayout();
        Gdx.input.setInputProcessor(new InputHandler());
    }

    private Array<CreditLine> createCreditLines() {
        Array<CreditLine> lines = new Array<>();

        lines.add(new CreditLine("Fonts", "", true));
        lines.add(new CreditLine("- Paytone One", "https://fonts.google.com/specimen/Paytone+One", false));

        lines.add(new CreditLine("Music", "", true));
        lines.add(new CreditLine("- Background Music", "https://pixabay.com/music/", false));

        lines.add(new CreditLine("SFX", "", true));
        lines.add(new CreditLine("- Source Website", "https://pixabay.com/sound-effects/", false));
        lines.add(new CreditLine("SFX Authors", "", true));
        lines.add(new CreditLine("- Universfield", "", false));
        lines.add(new CreditLine("- Lesiakover", "", false));
        lines.add(new CreditLine("- floraphonic", "", false));
        lines.add(new CreditLine("- FoxBoy Tails", "", false));
        lines.add(new CreditLine("- EAGLAXLE", "", false));
        lines.add(new CreditLine("- Yodguard", "", false));
        lines.add(new CreditLine("- AudioPapkin", "", false));
        lines.add(new CreditLine("- Prmodrai", "", false));
        lines.add(new CreditLine("- freesound_community", "", false));

        lines.add(new CreditLine("SFX Files Used", "", true));
        lines.add(new CreditLine("- ui_click.ogg", "", false));
        lines.add(new CreditLine("- ui_error.ogg", "", false));
        lines.add(new CreditLine("- pause_toggle.ogg", "", false));
        lines.add(new CreditLine("- speed_toggle.ogg", "", false));
        lines.add(new CreditLine("- wave_start.ogg", "", false));
        lines.add(new CreditLine("- wave_complete.ogg", "", false));
        lines.add(new CreditLine("- victory.ogg", "", false));
        lines.add(new CreditLine("- lose.ogg", "", false));
        lines.add(new CreditLine("- tower_attack_basic.ogg", "", false));
        lines.add(new CreditLine("- core_hit.ogg", "", false));
        lines.add(new CreditLine("- enemy_hit.ogg", "", false));
        lines.add(new CreditLine("- enemy_death.ogg", "", false));
        lines.add(new CreditLine("- gold_gain.ogg", "", false));
        lines.add(new CreditLine("- buy_success.ogg", "", false));
        lines.add(new CreditLine("- sell.ogg", "", false));
        lines.add(new CreditLine("- augment_pick.ogg", "", false));

        lines.add(new CreditLine("3D Models", "", true));
        lines.add(new CreditLine("- Quaternius", "https://poly.pizza/u/Quaternius", false));

        lines.add(new CreditLine("Systems and Libraries", "", true));
        lines.add(new CreditLine("- Java", "https://www.java.com/", false));
        lines.add(new CreditLine("- GitHub", "https://github.com/", false));
        lines.add(new CreditLine("- libGDX", "https://libgdx.com/", false));
        lines.add(new CreditLine("- LWJGL", "https://www.lwjgl.org/", false));
        lines.add(new CreditLine("- Gradle", "https://gradle.org/", false));
        lines.add(new CreditLine("- Dreamlo", "http://dreamlo.com/", false));

        return lines;
    }

    private void recalcLayout() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        backBtn = new Rectangle(18f, 18f, 140f, 48f);

        float crawlY = backBtn.y + backBtn.height + 14f;
        float crawlH = h - crawlY - 14f;
        crawlArea = new Rectangle(24f, crawlY, w - 48f, crawlH);

        crawlContentHeight = calculateCrawlContentHeight();
        if (crawlOffset <= 0f) {
            crawlOffset = 0f;
        }
    }

    private float calculateCrawlContentHeight() {
        float total = 0f;

        total += LINE_GAP * 1.2f; // Credits title
        total += SECTION_GAP * 0.7f;

        total += LINE_GAP; // Team Members heading
        total += teamMembers.size * LINE_GAP;
        total += SECTION_GAP;

        total += LINE_GAP; // Used Assets heading
        total += SECTION_GAP * 0.6f;

        for (CreditLine line : creditLines) {
            total += line.heading ? LINE_GAP : LINE_GAP * 0.95f;
            if (line.hasLink()) {
                total += LINE_GAP * 0.8f;
            }
        }

        return total + 80f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCrawl(delta);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawRect(backBtn, new Color(1f, 1f, 1f, 0.12f));
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        drawRect(backBtn, Color.WHITE);
        shapes.end();

        batch.begin();
        linkEntries.clear();
        renderCrawlContent();
        drawCentered(font, "Back", backBtn.x, backBtn.y + backBtn.height * 0.67f, backBtn.width, Color.WHITE);
        batch.end();
    }

    private void updateCrawl(float delta) {
        float resetThreshold = crawlArea.height + crawlContentHeight;
        crawlOffset += delta * CRAWL_SPEED;
        if (crawlOffset > resetThreshold) {
            crawlOffset = 0f;
        }
    }

    private void renderCrawlContent() {
        float y = crawlArea.y + crawlArea.height + crawlContentHeight - crawlOffset;

        y = drawCenteredLine(titleFont, "Credits", y, Color.WHITE, 1.2f);
        y -= SECTION_GAP * 0.7f;

        y = drawCenteredLine(font, "Group Members", y, Color.WHITE, 1f);
        for (String member : teamMembers) {
            y = drawCenteredLine(font, member, y, Color.WHITE, 1f);
        }

        y -= SECTION_GAP;
        y = drawCenteredLine(font, "Used Assets", y, Color.WHITE, 1f);
        y -= SECTION_GAP * 0.6f;

        float textX = crawlArea.x + 10f;

        for (CreditLine line : creditLines) {
            if (line.heading) {
                if (isLineVisible(y)) {
                    drawLeft(font, line.text, textX, y, Color.WHITE);
                }
                y -= LINE_GAP;
                continue;
            }

            if (isLineVisible(y)) {
                drawLeft(font, line.text, textX, y, Color.WHITE);
            }
            y -= LINE_GAP * 0.95f;

            if (line.hasLink()) {
                if (isLineVisible(y)) {
                    glyph.setText(font, line.url);
                    drawLeft(font, line.url, textX, y, Color.WHITE);
                    linkEntries.add(new LinkEntry(
                            new Rectangle(textX, y - glyph.height, glyph.width, glyph.height + 4f),
                            line.url));
                }
                y -= LINE_GAP * 0.8f;
            }
        }
    }

    private float drawCenteredLine(BitmapFont drawFont, String text, float y, Color color, float gapMul) {
        if (isLineVisible(y)) {
            drawCentered(drawFont, text, crawlArea.x, y, crawlArea.width, color);
        }
        return y - (LINE_GAP * gapMul);
    }

    private boolean isLineVisible(float baselineY) {
        return baselineY >= crawlArea.y && baselineY <= crawlArea.y + crawlArea.height;
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
        font = createFont("fonts/font_game_screen.ttf", scaledFontSize(22));
        titleFont = createFont("fonts/font_game_screen.ttf", scaledFontSize(46));
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

        @Override
        public boolean scrolled(float amountX, float amountY) {
            float manualStep = amountY * SCROLL_STEP;
            crawlOffset = MathUtils.clamp(crawlOffset + manualStep, 0f, crawlArea.height + crawlContentHeight);
            return true;
        }
    }
}
