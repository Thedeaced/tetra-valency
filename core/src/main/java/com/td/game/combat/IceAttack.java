package com.td.game.combat;

import com.td.game.elements.Element;
import com.td.game.entities.Enemy;

public class IceAttack implements AttackAction {
    private final float baseDamage;
    private final float range;
    private final float attackSpeed;
    private final float freezeDuration;

    public IceAttack(float baseDamage, float range, float attackSpeed, float freezeDuration) {
        this.baseDamage = baseDamage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.freezeDuration = freezeDuration;
    }

    @Override
    public void attack(AttackContext context) {
        if (context == null || context.getTarget() == null) {
            return;
        }
        Enemy target = context.getTarget();
        Element attackerElement = context.getSource() != null ? context.getSource().getCurrentElement() : null;
        target.takeDamage(baseDamage, attackerElement);
        if (freezeDuration > 0f) {
            target.applyFreeze(freezeDuration);
        }
    }
}