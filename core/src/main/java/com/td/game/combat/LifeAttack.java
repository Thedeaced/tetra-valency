package com.td.game.combat;

import com.badlogic.gdx.utils.Array;
import com.td.game.elements.Element;
import com.td.game.entities.Enemy;
import com.td.game.pillars.Pillar;

public class LifeAttack implements AttackAction {
    private final float range;
    private final float attackSpeed;

    public LifeAttack(float range, float attackSpeed) {
        this.range = range;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null) {
            return;
        }
        // No direct damage here. Ally revival requires a dedicated ally system, which isn't present yet.
    }

    public static boolean canRevive(Array<Pillar> pillars, Enemy enemy) {
        if (pillars == null || enemy == null) {
            return false;
        }
        for (Pillar p : pillars) {
            if (p.isActive() && p.getCurrentElement() == Element.LIFE) {
                if (p.getPosition().dst(enemy.getPosition()) < p.getAttackRange()) {
                    return true;
                }
            }
        }
        return false;
    }
}
