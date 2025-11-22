package net.sodiumzh.nff.girls.entity.ai;

import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.NFFGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

import java.util.function.Predicate;

public class NFFGirlsAIUtils {

    /**
     * Checking if the mob is following owner and the owner is further than the distance threshold.
     */
    public static boolean followingFurtherThan(INFFTamed mob, double distance) {
        return mob.isOwnerInDimension()
            && mob.getAIState().equals(NFFTamedMobAIState.FOLLOW)
            && mob.getOwnerInDimension().distanceToSqr(mob.asMob()) > distance * distance;
    }

    /**
     * Get a predicate checking if the mob is following owner and the owner is further than the distance threshold.
     */
    public static Predicate<NFFGoal> predicateFollowingFurtherThan(double distance) {
        return g -> followingFurtherThan(g.getMob(), distance);
    }

    /**
     * Check if the mob has a target, and the target is closer than the distance threshold.
     */
    public static boolean targetCloserThan(INFFTamed mob, double threshold) {
        return mob.asMob().getTarget() != null &&
            mob.asMob().distanceToSqr(mob.asMob().getTarget()) <= threshold * threshold;
    }

    /**
     * Get a predicate checking if the mob has a target, and the target is closer than the distance threshold.
     */
    public static Predicate<NFFGoal> predicateTargetCloserThan(double threshold) {
        return g -> targetCloserThan(g.getMob(), threshold);
    }

    /**
     * Check if the mob has a target, and the target is further than the distance threshold.
     */

    public static boolean targetFurtherThan(INFFTamed mob, double threshold) {
        return mob.asMob().getTarget() != null &&
            mob.asMob().distanceToSqr(mob.asMob().getTarget()) >= threshold * threshold;
    }
    /**
     * Get a predicate checking if the mob has a target, and the target is further than the distance threshold.
     */
    public static Predicate<NFFGoal> predicateTargetFurtherThan(double threshold) {
        return g -> targetFurtherThan(g.getMob(), threshold);
    }

}
