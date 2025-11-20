package net.sodiumzh.nff.girls.entity.ai;

import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.NFFGoal;

import java.util.function.Predicate;

public class NFFGirlsAIRules {

    public static final double FOLLOWING_TELEPORT_DISTANCE = 12d;

    public static final Predicate<NFFGoal> FOLLOWING_OWNER_FAR_AWAY = g ->
        g.getMob().isOwnerInDimension()
        && g.getMob().getAIState().equals(NFFTamedMobAIState.FOLLOW)
        && g.getMob().getOwnerInDimension().distanceToSqr(g.getMob().asMob()) > FOLLOWING_TELEPORT_DISTANCE * FOLLOWING_TELEPORT_DISTANCE * 4d;

}
