package net.sodiumzh.nff.girls.entity.ai;

import com.github.mechalopa.hmag.util.ModUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsHmagFlyingGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.annotation.DontOverride;

import java.util.Optional;

/**
 * Base interface for HMaG-style flying mobs. Used in {@link NFFGirlsHmagFlyingGoal}.
 */
public interface INFFGirlsFlyingMob {

    public static Optional<INFFGirlsFlyingMob> get(Object obj) {
        return obj instanceof INFFGirlsFlyingMob fm ? Optional.of(fm) : Optional.empty();
    }

    /**
     * Reflecting HMaG flying mob attack phase. Should be linked to a synched data, and should be added.
     * to save data.
     */
    @DontOverride
    public int getAttackPhase();

    /**
     * Reflecting HMaG flying mob attack phase. Should be linked to a synched data, and should be added.
     * to save data.
     */
    @DontOverride
    public void setAttackPhase(int value);

    @DontOverride
    public default boolean isCharging() {
        return this.getAttackPhase() == 2;
    }

    @DontOverride
    public default Mob getMob() {
        return (Mob) this;
    }

    @DontOverride
    public default MoveControl createMoveControl() {
        return new MoveControl(this.getMob());
    }

    /**
     * Create a NFF Girls HMaG-style flying move control. This move control doesn't necessarily require
     * INFFGirlsFlyingMob interface.
     */
    public static MoveControl createMoveControl(Mob mob) {
        return new MoveControl(mob);
    }

    public static class MoveControl extends net.minecraft.world.entity.ai.control.MoveControl {

        protected final Mob mob;
        public MoveControl(Mob mob)
        {
            super(mob);
            this.mob = mob;
        }

        @Override
        public void tick()
        {
            if (this.operation == net.minecraft.world.entity.ai.control.MoveControl.Operation.MOVE_TO)
            {
                this.mob.setNoGravity(true);
                Vec3 vec3 = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                double d0 = vec3.length();

                if (d0 < this.mob.getBoundingBox().getSize() || !ModUtils.canReach(this.mob, vec3.normalize(), Mth.ceil(d0)))
                {
                    this.operation = net.minecraft.world.entity.ai.control.MoveControl.Operation.WAIT;
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
                }
                else
                {
                    float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vec3.scale((float)this.speedModifier * f * 0.2D / d0)));

                    if (this.mob.getTarget() == null)
                    {
                        Vec3 vec31 = this.mob.getDeltaMovement();
                        this.mob.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float)Math.PI));
                        this.mob.yBodyRot = this.mob.getYRot();
                    }
                    else
                    {
                        double d2 = this.mob.getTarget().getX() - this.mob.getX();
                        double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                        this.mob.setYRot(-((float)Mth.atan2(d2, d1)) * (180.0F / (float)Math.PI));
                        this.mob.yBodyRot = this.mob.getYRot();
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void resetAttackOnHurt(LivingHurtEvent event) {
            if (!event.isCanceled()
                && !event.getEntity().level().isClientSide()
                && event.getEntity() instanceof INFFGirlsFlyingMob mob
                && !mob.getMob().isNoAi()
                && !event.getEntity().isInvulnerableTo(event.getSource())
                && event.getAmount() > 0
                && event.getSource().getEntity() instanceof LivingEntity
                && mob.isCharging()
                && event.getEntity().getRandom().nextInt(3) == 0) // IDK why randomize here, but HMaG did this
            {
                mob.setAttackPhase(1);
            }
        }
    }
}
