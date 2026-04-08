package com.td.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.td.game.screens.IntroScreen;
import com.td.game.systems.OptionsManager;

public class TowerDefenseGame extends Game {

    public ModelBatch modelBatch;
    public BitmapFont font;
    public AudioManager audio;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        font = new BitmapFont();
        audio = new AudioManager();
        audio.init();
        OptionsManager.load();
        audio.setMusicVolume(OptionsManager.get().musicVolume);
        audio.setSoundVolume(OptionsManager.get().soundVolume);
        boolean wantsFullscreen = OptionsManager.get().fullscreen;
        if (wantsFullscreen) {
            Gdx.graphics.setWindowedMode(1920, 1080);
            Gdx.app.postRunnable(() -> {
                com.badlogic.gdx.Graphics.DisplayMode dm = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(dm);
            });
        } else if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(1920, 1080);
        }

        setScreen(new IntroScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (modelBatch != null)
            modelBatch.dispose();
        if (font != null)
            font.dispose();
        if (audio != null)
            audio.dispose();
    }
}

