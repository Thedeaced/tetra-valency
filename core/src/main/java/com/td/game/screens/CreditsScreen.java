package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

    private Texture bgTexture;
    private Texture logoTexture;
    private Texture githubBadgeTexture;
    private Texture libgdxBadgeTexture;
    private Texture gradleBadgeTexture;
    private Texture dreamloBadgeTexture;

    private Rectangle rootPanel;
    private Rectangle crawlArea;
    private Rectangle backBtn;

    private Array<String> teamMembers;
    private Array<CreditLine> creditLines;
    private Array<LinkEntry> linkEntries;

    private float crawlOffset;
    private float crawlContentHeight;

    private static final float CRAWL_SPEED = 42f;
    private static final float SCROLL_STEP = 38f;
    private static final float LOGO_SIZE = 140f;
    private static final float SECTION_GAP = 28f;
    private static final float LINE_GAP = 22f;

    private enum IconKind {
        NONE, GITHUB, LIBGDX, GRADLE, DREAMLO
    }

    private static class CreditLine {
        final String text;
        final String url;
        final IconKind icon;
        final boolean heading;

        CreditLine(String text, String url, IconKind icon, boolean heading) {
            this.text = text;
            this.url = url;
            this.icon = icon;
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

        bgTexture = loadTextureSafe("ui/main_menu_bg.png");
        if (bgTexture == null) {
            bgTexture = loadTextureSafe("ui/augment_screen_bg.png");
        }

        logoTexture = loadTextureSafe("ui/cosmovision.png");
        githubBadgeTexture = loadTextureSafe("credits/github.png");
        libgdxBadgeTexture = loadTextureSafe("credits/libgdx.png");
        gradleBadgeTexture = loadTextureSafe("credits/gradle.png");
        dreamloBadgeTexture = loadTextureSafe("credits/dreamlo.png");

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

        lines.add(new CreditLine("Icons", "", IconKind.NONE, true));
        lines.add(new CreditLine("GitHub-sourced UI/Game Icons", "https://github.com/", IconKind.NONE, false));

        lines.add(new CreditLine("Fonts", "", IconKind.NONE, true));
        lines.add(new CreditLine("Paytone One", "https://fonts.google.com/specimen/Paytone+One", IconKind.NONE, false));

        lines.add(new CreditLine("Music", "", IconKind.NONE, true));
        lines.add(new CreditLine("Background Music (source link pending)", "", IconKind.NONE, false));

        lines.add(new CreditLine("SFX", "", IconKind.NONE, true));
        lines.add(new CreditLine("Click sound effect by Universfield", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as UI click effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Error sound effect by Lesiakover", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as UI click error effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Casual Click Pop UI 3 sound effect by floraphonic", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as pause toggle effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("High Speed sound effect by Universfield", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as speed toggle effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Game Level Complete sound effect by Universfield", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as wave complete effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Game Start sound effect by FoxBoy Tails", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as wave start effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Gaming victory sound effect by EAGLAXLE", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as victory effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Marimba Lose sound effect by Universfield", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as lose effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("fire magic (6) sound effect by Yodguard", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as attack effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Crushing shells of eggs sound effect by AudioPapkin", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as core hit effect", "", IconKind.NONE, false));
        lines.add(new CreditLine("Damage blowhole sound effect by Prmodrai", "https://pixabay.com/sound-effects/", IconKind.NONE, false));
        lines.add(new CreditLine("Used as enemy hit effect", "", IconKind.NONE, false));

        lines.add(new CreditLine("3D Models", "", IconKind.NONE, true));
        lines.add(new CreditLine("3D Models (source links pending)", "", IconKind.NONE, false));

        lines.add(new CreditLine("Tools/Libraries", "", IconKind.NONE, true));
        lines.add(new CreditLine("GitHub", "https://github.com/", IconKind.GITHUB, false));
        lines.add(new CreditLine("libGDX", "https://libgdx.com/", IconKind.LIBGDX, false));
        lines.add(new CreditLine("Gradle", "https://gradle.org/", IconKind.GRADLE, false));
        lines.add(new CreditLine("Dreamlo", "http://dreamlo.com/", IconKind.DREAMLO, false));

        return lines;
    }

    private void recalcLayout() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        rootPanel = new Rectangle(w * 0.18f, h * 0.06f, w * 0.64f, h * 0.88f);
        backBtn = new Rectangle(rootPanel.x + 18f, rootPanel.y + 18f, 140f, 48f);

        float crawlY = backBtn.y + backBtn.height + 12f;
        float crawlH = rootPanel.height - (crawlY - rootPanel.y) - 16f;
        crawlArea = new Rectangle(rootPanel.x + 24f, crawlY, rootPanel.width - 48f, crawlH);

        crawlContentHeight = calculateCrawlContentHeight();
        if (crawlOffset <= 0f) {
            crawlOffset = 0f;
        }
    }

    private float calculateCrawlContentHeight() {
        float total = 0f;

        total += LOGO_SIZE + SECTION_GAP;
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
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCrawl(delta);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        if (bgTexture != null) {
            batch.begin();
            batch.draw(bgTexture, 0, 0, w, h);
            batch.end();
        }

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawRect(rootPanel, new Color(0.89f, 0.67f, 0.26f, 0.94f));
        drawRect(crawlArea, new Color(0.95f, 0.79f, 0.42f, 0.24f));
        drawRect(backBtn, new Color(0.56f, 0.43f, 0.33f, 1f));
        shapes.end();

        batch.begin();
        linkEntries.clear();
        renderCrawlContent();
        drawCentered(font, "Back", backBtn.x, backBtn.y + backBtn.height * 0.67f, backBtn.width,
                new Color(0.14f, 0.1f, 0.06f, 1f));
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
        float y = crawlArea.y - crawlContentHeight + crawlOffset;
        float centerX = rootPanel.x + rootPanel.width * 0.5f;

        if (logoTexture != null) {
            float logoX = centerX - LOGO_SIZE * 0.5f;
            if (isRectVisible(y, LOGO_SIZE)) {
                batch.draw(logoTexture, logoX, y, LOGO_SIZE, LOGO_SIZE);
            }
        }
        y += LOGO_SIZE + SECTION_GAP;

        y = drawCenteredLine(titleFont, "Credits", y, new Color(0.23f, 0.15f, 0.08f, 1f), 1.2f);
        y += SECTION_GAP * 0.7f;

        y = drawCenteredLine(font, "Team Members", y, new Color(0.40f, 0.28f, 0.14f, 1f), 1f);
        for (String member : teamMembers) {
            y = drawCenteredLine(font, member, y, new Color(0.14f, 0.1f, 0.06f, 1f), 1f);
        }

        y += SECTION_GAP;
        y = drawCenteredLine(font, "Used Assets", y, new Color(0.40f, 0.28f, 0.14f, 1f), 1f);
        y += SECTION_GAP * 0.6f;

        float textX = crawlArea.x + 10f;

        for (CreditLine line : creditLines) {
            if (line.heading) {
                if (isLineVisible(y)) {
                    drawLeft(font, line.text, textX, y, new Color(0.35f, 0.24f, 0.12f, 1f));
                }
                y += LINE_GAP;
                continue;
            }

            float iconSize = 18f;
            float rowTextX = textX;
            if (line.icon != IconKind.NONE) {
                if (isRectVisible(y - iconSize + 3f, iconSize)) {
                    drawProviderIcon(line.icon, textX, y - iconSize + 3f, iconSize);
                }
                rowTextX += 24f;
            }

            if (isLineVisible(y)) {
                drawLeft(font, line.text, rowTextX, y, new Color(0.14f, 0.1f, 0.06f, 1f));
            }
            y += LINE_GAP * 0.95f;

            if (line.hasLink()) {
                if (isLineVisible(y)) {
                    glyph.setText(font, line.url);
                    drawLeft(font, line.url, rowTextX, y, new Color(0.10f, 0.24f, 0.55f, 1f));
                    linkEntries.add(new LinkEntry(
                            new Rectangle(rowTextX, y - glyph.height, glyph.width, glyph.height + 4f),
                            line.url));
                }
                y += LINE_GAP * 0.8f;
            }
        }
    }

    private float drawCenteredLine(BitmapFont drawFont, String text, float y, Color color, float gapMul) {
        if (isLineVisible(y)) {
            drawCentered(drawFont, text, crawlArea.x, y, crawlArea.width, color);
        }
        return y + (LINE_GAP * gapMul);
    }

    private boolean isLineVisible(float baselineY) {
        return baselineY >= crawlArea.y && baselineY <= crawlArea.y + crawlArea.height;
    }

    private boolean isRectVisible(float y, float h) {
        return y + h >= crawlArea.y && y <= crawlArea.y + crawlArea.height;
    }

    private void drawProviderIcon(IconKind kind, float x, float y, float size) {
        Texture tex = null;
        switch (kind) {
            case GITHUB:
                tex = githubBadgeTexture;
                break;
            case LIBGDX:
                tex = libgdxBadgeTexture;
                break;
            case GRADLE:
                tex = gradleBadgeTexture;
                break;
            case DREAMLO:
                tex = dreamloBadgeTexture;
                break;
            default:
                break;
        }

        if (tex != null) {
            batch.setColor(Color.WHITE);
            batch.draw(tex, x, y, size, size);
            return;
        }

        if (kind == IconKind.GITHUB) {
            drawLeft(font, "GH", x, y + size - 2f, new Color(0.16f, 0.16f, 0.16f, 1f));
        }
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
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        if (logoTexture != null) {
            logoTexture.dispose();
        }
        if (githubBadgeTexture != null) {
            githubBadgeTexture.dispose();
        }
        if (libgdxBadgeTexture != null) {
            libgdxBadgeTexture.dispose();
        }
        if (gradleBadgeTexture != null) {
            gradleBadgeTexture.dispose();
        }
        if (dreamloBadgeTexture != null) {
            dreamloBadgeTexture.dispose();
        }
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

    private Texture loadTextureSafe(String path) {
        FileHandle f = resolveAsset(path);
        if (!f.exists()) {
            return null;
        }
        try {
            return new Texture(f);
        } catch (Exception e) {
            Gdx.app.log("CreditsScreen", "Texture load failed: " + path + " (" + e.getMessage() + ")");
            return null;
        }
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
