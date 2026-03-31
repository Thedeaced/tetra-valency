package com.td.game.combat;

import com.td.game.elements.Element;
import com.td.game.entities.Enemy;

public class WaterAttack implements AttackAction {
    private final float baseDamage;
    private final float range;
    private final float attackSpeed;
    private final float slowMultiplier;
    private final float slowDuration;

    
    public WaterAttack(float baseDamage, float range, float attackSpeed, float slowMultiplier, float slowDuration) {
        this.baseDamage = baseDamage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.slowMultiplier = slowMultiplier;
        this.slowDuration = slowDuration;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null || context.getTarget() == null) {
            return;
        }
        Enemy target = context.getTarget();
        Element attackerElement = context.getSource() != null ? context.getSource().getCurrentElement() : null;
        target.takeDamage(baseDamage, attackerElement);
        target.applySlow(slowDuration, slowMultiplier);
    }
}
