package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.TowerDefenseGame;

import java.util.Arrays;
import java.util.Comparator;

public class CinematicScreen implements Screen {
    private static final float BYTES_PER_SECOND_ESTIMATE = 16000f;
    private static final int SPECIAL_FRAME_INDEX = 5;

    private final TowerDefenseGame game;
    private SpriteBatch batch;
    private Array<Texture> frames;
    private float[] frameDurations;
    private float totalDuration;
    private float elapsed;
    private Music voiceover;
    private boolean finished;

    public CinematicScreen(TowerDefenseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        frames = new Array<>();
        elapsed = 0f;
        finished = false;

        FileHandle dir = resolveAsset("cinematic");
        if (dir.exists() && dir.isDirectory()) {
            FileHandle[] files = dir.list();
            Arrays.sort(files, new Comparator<FileHandle>() {
                @Override
                public int compare(FileHandle a, FileHandle b) {
                    int ai = extractLeadingInt(a.name());
                    int bi = extractLeadingInt(b.name());
                    if (ai != -1 && bi != -1) {
                        return Integer.compare(ai, bi);
                    }
                    return a.name().compareToIgnoreCase(b.name());
                }
            });
            for (FileHandle file : files) {
                if (!isImageFile(file.name())) {
                    continue;
                }
                try {
                    frames.add(new Texture(file));
                } catch (Exception e) {
                    Gdx.app.error("CinematicScreen", "Failed to load frame: " + file.path(), e);
                }
            }
        }

        FileHandle voiceoverFile = resolveAsset("audio/voiceover/voiceover.ogg");
        if (voiceoverFile.exists()) {
            voiceover = Gdx.audio.newMusic(voiceoverFile);
            voiceover.setVolume(game.audio.getSoundVolume());
            voiceover.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    finished = true;
                }
            });
            voiceover.play();
        }

        totalDuration = estimateDurationSeconds(voiceoverFile);
        if (frames.size == 0) {
            finished = true;
        }
        buildDurations();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                finished = true;
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                finished = true;
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        elapsed += delta;
        if (finished || (totalDuration > 0f && elapsed >= totalDuration)) {
            goToMainMenu();
            return;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (frames.size == 0) {
            return;
        }

        int index = getFrameIndex(elapsed);
        index = Math.max(0, Math.min(index, frames.size - 1));
        Texture frame = frames.get(index);
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float imgW = frame.getWidth();
        float imgH = frame.getHeight();
        float scale = Math.max(screenW / imgW, screenH / imgH);
        float drawW = imgW * scale;
        float drawH = imgH * scale;
        float drawX = (screenW - drawW) * 0.5f;
        float drawY = (screenH - drawH) * 0.5f;

        batch.begin();
        batch.draw(frame, drawX, drawY, drawW, drawH);
        batch.end();
    }

    private void buildDurations() {
        int count = frames.size;
        frameDurations = new float[count];
        if (count == 0) {
            return;
        }

        int extra = count > SPECIAL_FRAME_INDEX ? 1 : 0;
        float weight = count + extra;
        float base;
        if (totalDuration <= 0f) {
            base = 2.0f;
            totalDuration = base * weight;
        } else {
            base = totalDuration / weight;
        }
        for (int i = 0; i < count; i++) {
            frameDurations[i] = (i == SPECIAL_FRAME_INDEX ? base * 2f : base);
        }
    }

    private int getFrameIndex(float time) {
        if (frameDurations == null || frameDurations.length == 0) {
            return 0;
        }
        float acc = 0f;
        for (int i = 0; i < frameDurations.length; i++) {
            acc += frameDurations[i];
            if (time <= acc) {
                return i;
            }
        }
        return frameDurations.length - 1;
    }

    private float estimateDurationSeconds(FileHandle file) {
        if (file == null || !file.exists()) {
            return 0f;
        }
        float seconds = file.length() / BYTES_PER_SECOND_ESTIMATE;
        if (seconds < 10f) {
            seconds = 10f;
        } else if (seconds > 180f) {
            seconds = 180f;
        }
        return seconds;
    }

    private boolean isImageFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
    }

    private int extractLeadingInt(String name) {
        int idx = 0;
        while (idx < name.length() && Character.isDigit(name.charAt(idx))) {
            idx++;
        }
        if (idx == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(name.substring(0, idx));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void goToMainMenu() {
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        if (batch != null) {
            batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        }
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
        if (voiceover != null) {
            voiceover.stop();
            voiceover.dispose();
            voiceover = null;
        }
        disposeAll(frames);
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
    }

    private void disposeAll(Array<? extends Disposable> list) {
        if (list == null) {
            return;
        }
        for (Disposable d : list) {
            if (d != null) {
                d.dispose();
            }
        }
        list.clear();
    }

    private static FileHandle resolveAsset(String name) {
        FileHandle f = Gdx.files.internal(name);
        if (f.exists())
            return f;
        f = Gdx.files.internal("assets/" + name);
        if (f.exists())
            return f;
        return Gdx.files.internal(name);
    }
}
