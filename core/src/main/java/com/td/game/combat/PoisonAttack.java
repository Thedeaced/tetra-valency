package com.td.game.combat;

import com.td.game.elements.Element;
import com.td.game.entities.Enemy;

public class PoisonAttack implements AttackAction {
    private final float baseDamage;
    private final float range;
    private final float attackSpeed;
    private final float poisonDuration;
    private final float poisonDamage;

    public PoisonAttack(float baseDamage, float range, float attackSpeed, float poisonDuration, float poisonDamage) {
        this.baseDamage = baseDamage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.poisonDuration = poisonDuration;
        this.poisonDamage = poisonDamage;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null || context.getTarget() == null) {
            return;
        }
        Enemy target = context.getTarget();
        Element attackerElement = context.getSource() != null ? context.getSource().getCurrentElement() : null;
        target.takeDamage(baseDamage, attackerElement);
        if (poisonDuration > 0f) {
            target.applyPoison(poisonDuration, poisonDamage, 1);
        }
    }

    public static void applyOnHit(Enemy target, float impactDamage) {
        if (target == null || !target.isAlive()) {
            return;
        }
        target.takeDamage(impactDamage, Element.POISON);
        target.applyPoison(5.1f, impactDamage * 0.15f, 1);
        target.applyRegenBlock(5.1f);
    }
}
