package com.td.game.combat;

import com.td.game.elements.Element;
import com.td.game.entities.Enemy;

public class LightAttack implements AttackAction {
    private final float baseDamage;
    private final float range;
    private final float attackSpeed;
    private final float currentHpDamage;

    public LightAttack(float baseDamage, float range, float attackSpeed, float currentHpDamage) {
        this.baseDamage = baseDamage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.currentHpDamage = currentHpDamage;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null || context.getTarget() == null) {
            return;
        }
        Enemy target = context.getTarget();
        Element attackerElement = context.getSource() != null ? context.getSource().getCurrentElement() : null;
        float scaledDamage = baseDamage + (currentHpDamage * target.getHealth());
        target.takeDamage(scaledDamage, attackerElement);
    }

    public static float scaleByCurrentHp(float baseDamage, Enemy target) {
        if (target == null) {
            return baseDamage;
        }
        float hpPercent = target.getHealth() / Math.max(1f, target.getMaxHealth());
        float lightMultiplier = 0.5f + (hpPercent * 2.0f);
        return baseDamage * lightMultiplier;
    }
}