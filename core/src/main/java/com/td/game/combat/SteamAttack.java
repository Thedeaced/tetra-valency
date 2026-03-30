package com.td.game.combat;

import com.td.game.elements.Element;
import com.td.game.entities.Enemy;

public class SteamAttack implements AttackAction {
    private final float baseDamage;
    private final float range;
    private final float attackSpeed;
    private final float baseKnockback;
    private final float scalingKnockback;

    public SteamAttack(float baseDamage, float range, float attackSpeed, float baseKnockback, float scalingKnockback) {
        this.baseDamage = baseDamage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.baseKnockback = baseKnockback;
        this.scalingKnockback = scalingKnockback;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null || context.getTarget() == null) {
            return;
        }
        Enemy target = context.getTarget();
        Element attackerElement = context.getSource() != null ? context.getSource().getCurrentElement() : null;
        target.takeDamage(baseDamage, attackerElement);

        float hpPercent = target.getHealthPercent();
        float scaledKnockback = baseKnockback + (1f - hpPercent) * scalingKnockback;
        if (scaledKnockback > 0f) {
            target.applyKnockback(scaledKnockback);
        }
    }

    public static void applyOnHit(Enemy target, float impactDamage) {
        if (target == null || !target.isAlive()) {
            return;
        }
        target.takeDamage(impactDamage, Element.STEAM);
        float hpPercent = target.getHealth() / Math.max(1f, target.getMaxHealth());
        float kbDist = 1.0f + (1.0f - hpPercent) * 3.0f;
        target.applyKnockback(kbDist);
    }
}
