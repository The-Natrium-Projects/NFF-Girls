package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestAttackableTargetGoal;

import java.util.function.Function;
import java.util.function.Predicate;

public class NFFGirlsAttackingStrategyTargetGoal extends NFFNearestAttackableTargetGoal<Mob> {

    protected static Function<INFFGirlsTamed, Predicate<LivingEntity>> TARGET_PREDICATE_PROVIDER =
        (tamed) -> (target -> {
        if (!(target instanceof Mob)) return false;
        return tamed.getAttackingStrategy().shouldActivelyAttack(tamed, (Mob)target);
    });

    public NFFGirlsAttackingStrategyTargetGoal(INFFGirlsTamed mob) {
        this(mob, 10, true, false);
    }

    public NFFGirlsAttackingStrategyTargetGoal(INFFGirlsTamed mob, boolean mustSee, boolean mustReach) {
        this(mob, 10, mustSee, mustReach);
    }

    public NFFGirlsAttackingStrategyTargetGoal(INFFGirlsTamed mob, int randomInterval, boolean mustSee, boolean mustReach) {
        super(mob, Mob.class, randomInterval, mustSee, mustReach, TARGET_PREDICATE_PROVIDER.apply(mob));
    }

}
