package com.td.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.td.game.TowerDefenseGame;

public class IntroScreen implements Screen {
    private final TowerDefenseGame game;
    private VideoPlayer videoPlayer;
    private SpriteBatch batch;
    private boolean videoFinished;
    private boolean transitioning;
    private float skipDelay;

    public IntroScreen(TowerDefenseGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        videoFinished = false;
        transitioning = false;
        skipDelay = 0.3f;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (skipDelay <= 0f) {
                    videoFinished = true;
                }
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (skipDelay <= 0f) {
                    videoFinished = true;
                }
                return true;
            }
        });

        try {
            videoPlayer = VideoPlayerCreator.createVideoPlayer();
            videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
                @Override
                public void onCompletionListener(FileHandle file) {
                    videoFinished = true;
                }
            });

            FileHandle videoFile = resolveAsset("videos/intro.webm");
            videoPlayer.load(videoFile);
            videoPlayer.play();
        } catch (Throwable e) {
            Gdx.app.error("IntroScreen", "Failed to create video player", e);
            videoFinished = true;
        }
    }

    @Override
    public void render(float delta) {
        if (skipDelay > 0f) {
            skipDelay -= delta;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (videoFinished && !transitioning) {
            transitioning = true;
            goToMainMenu();
            return;
        }

        if (videoPlayer != null) {
            try {
                videoPlayer.update();
                Texture frame = videoPlayer.getTexture();
                if (frame != null) {
                    float screenW = Gdx.graphics.getWidth();
                    float screenH = Gdx.graphics.getHeight();

                    float videoW = frame.getWidth();
                    float videoH = frame.getHeight();
                    float scale = Math.max(screenW / videoW, screenH / videoH);
                    float drawW = videoW * scale;
                    float drawH = videoH * scale;
                    float drawX = (screenW - drawW) * 0.5f;
                    float drawY = (screenH - drawH) * 0.5f;

                    batch.begin();
                    batch.draw(frame, drawX, drawY, drawW, drawH);
                    batch.end();
                }
            } catch (Throwable e) {
                Gdx.app.error("IntroScreen", "Video playback error", e);
                videoFinished = true;
            }
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
        if (videoPlayer != null) {
            try {
                videoPlayer.dispose();
            } catch (Throwable e) {
                Gdx.app.error("IntroScreen", "Error disposing video player", e);
            }
            videoPlayer = null;
        }
        if (batch != null) {
            batch.dispose();
        }
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

