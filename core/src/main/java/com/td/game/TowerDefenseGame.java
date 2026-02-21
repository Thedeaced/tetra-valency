package com.td.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.td.game.screens.IntroScreen;

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

