package com.td.game.screens;

import com.badlogic.gdx.utils.Array;

public final class LeaderboardStore {
    public static final class WinEntry {
        public final String name;
        public final float timeSeconds;

        public WinEntry(String name, float timeSeconds) {
            this.name = name;
            this.timeSeconds = timeSeconds;
        }
    }

    public static final class EndlessEntry {
        public final String name;
        public final int wave;

        public EndlessEntry(String name, int wave) {
            this.name = name;
            this.wave = wave;
        }
    }

    private static final Array<WinEntry> winEntries = new Array<>();
    private static final Array<EndlessEntry> endlessEntries = new Array<>();

    private LeaderboardStore() {
    }

    public static void addWinEntry(String name, float timeSeconds) {
        winEntries.add(new WinEntry(name, timeSeconds));
        winEntries.sort((a, b) -> Float.compare(a.timeSeconds, b.timeSeconds));
        trimWinEntries();
    }

    public static void addEndlessEntry(String name, int wave) {
        endlessEntries.add(new EndlessEntry(name, wave));
        endlessEntries.sort((a, b) -> Integer.compare(b.wave, a.wave));
        trimEndlessEntries();
    }

    public static Array<WinEntry> getWinEntries() {
        return winEntries;
    }

    public static Array<EndlessEntry> getEndlessEntries() {
        return endlessEntries;
    }

    private static void trimWinEntries() {
        while (winEntries.size > 10) {
            winEntries.pop();
        }
    }

    private static void trimEndlessEntries() {
        while (endlessEntries.size > 10) {
            endlessEntries.pop();
        }
    }
}

