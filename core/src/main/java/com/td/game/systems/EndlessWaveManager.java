package com.td.game.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.td.game.entities.DemonEnemy;
import com.td.game.entities.Enemy;
import com.td.game.utils.ModelFactory;

public class EndlessWaveManager extends WaveManager {

    private static final int ENDLESS_START_WAVE = 50;
    private static final int BASE_ENDLESS_WAVE = 49;
    private static final int ENDLESS_WAVE_CAP = 500;

    public EndlessWaveManager(Array<Vector3> pathWaypoints, ModelFactory modelFactory) {
        super(pathWaypoints, modelFactory);
        setCurrentWave(ENDLESS_START_WAVE);
    }

    @Override
    public int getMaxWaves() {
        return ENDLESS_WAVE_CAP;
    }

    @Override
    public int getEnemiesForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getEnemiesForWave(wave);
        }
        int endlessStep = wave - ENDLESS_START_WAVE;
        int baseCount = Math.max(1, super.getEnemiesForWave(BASE_ENDLESS_WAVE));
        float count = baseCount * (float) Math.pow(1.2f, endlessStep);
        int normalCount = MathUtils.clamp((int) Math.ceil(count), baseCount, 500);
        int bossCount = getBossCountForWave(wave);
        int total = normalCount + bossCount;
        return MathUtils.clamp(total, bossCount, 500);
    }

    @Override
    protected float getSpawnIntervalForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getSpawnIntervalForWave(wave);
        }
        float endlessStep = wave - ENDLESS_START_WAVE;
        float base = super.getSpawnIntervalForWave(BASE_ENDLESS_WAVE);
        float interval = base * (float) Math.pow(0.985f, endlessStep);
        return Math.max(0.22f, Math.min(base, interval));
    }

    @Override
    protected float getHealthMultiplierForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getHealthMultiplierForWave(wave);
        }
        float endlessStep = wave - ENDLESS_START_WAVE;
        float base = super.getHealthMultiplierForWave(BASE_ENDLESS_WAVE);
        return base * (float) Math.pow(1.06f, endlessStep);
    }

    @Override
    protected float getSpeedMultiplierForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getSpeedMultiplierForWave(wave);
        }
        float endlessStep = wave - ENDLESS_START_WAVE;
        float base = super.getSpeedMultiplierForWave(BASE_ENDLESS_WAVE);
        return Math.min(2.3f, base * (float) Math.pow(1.01f, endlessStep));
    }

    @Override
    protected float getRewardMultiplierForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getRewardMultiplierForWave(wave);
        }
        float endlessStep = wave - ENDLESS_START_WAVE;
        float base = super.getRewardMultiplierForWave(BASE_ENDLESS_WAVE);
        return base * (float) Math.pow(1.05f, endlessStep);
    }

    @Override
    protected JsonValue getWaveConfig(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.getWaveConfig(wave);
        }
        return super.getWaveConfig(BASE_ENDLESS_WAVE);
    }

    @Override
    protected Enemy createEnemyForWave(int wave, int index) {
        if (wave <= ENDLESS_START_WAVE) {
            return super.createEnemyForWave(wave, index);
        }

        int bossCount = getBossCountForWave(wave);
        if (index < bossCount) {
            return createEndlessBoss(wave);
        }

        return super.createEnemyForWave(wave, index - bossCount);
    }

    private int getBossCountForWave(int wave) {
        if (wave <= ENDLESS_START_WAVE) {
            return 0;
        }
        int endlessStep = wave - ENDLESS_START_WAVE;
        if (endlessStep <= 0) {
            return 0;
        }
        int tiers = Math.max(0, (endlessStep - 1) / 5);
        int count = 1;
        for (int i = 0; i < tiers && count < 500; i++) {
            if (count > 250) {
                count = 500;
                break;
            }
            count *= 2;
        }
        return MathUtils.clamp(count, 1, 500);
    }

    private Enemy createEndlessBoss(int wave) {
        float healthMult = getHealthMultiplierForWave(wave);
        float speedMult = getSpeedMultiplierForWave(wave);
        float rewardMult = getRewardMultiplierForWave(wave);

        DemonEnemy boss = new DemonEnemy(5000f * healthMult, 0.4f * Math.min(1.2f, speedMult),
                Math.round(1000 * rewardMult));
        boss.setModel(demonModel);
        boss.setVisualScaleMultiplier(4.0f);
        boss.setAllElementsAffinity(true);
        boss.setElementalDamageTakenMultiplier(0.8f);
        return boss;
    }
}
