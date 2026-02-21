package com.td.game.systems;

import com.badlogic.gdx.utils.Disposable;
import com.td.game.utils.Constants;


public class EconomyManager implements Disposable {

    private int gold;
    private int lives;

    public EconomyManager() {
        this.gold = Constants.STARTING_GOLD;
        this.lives = Constants.STARTING_LIVES;
    }

    public boolean canAfford(int cost) {
        return gold >= cost;
    }

    public boolean spend(int amount) {
        if (canAfford(amount)) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void earn(int amount) {
        gold += amount;
    }

    public void loseLife() {
        lives--;
        if (lives < 0)
            lives = 0;
    }

    public void loseLives(int amount) {
        lives -= amount;
        if (lives < 0)
            lives = 0;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void reset() {
        this.gold = Constants.STARTING_GOLD;
        this.lives = Constants.STARTING_LIVES;
    }

    @Override
    public void dispose() {
        
    }
}

