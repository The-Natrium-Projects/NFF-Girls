package net.sodiumzh.nff.girls.entity.projectile;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFSafeTarget;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.exception.ReflectionFailedException;
import net.sodiumzh.nfu.mixin.event.entity.EffectCloudTakeEffectEvent;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class NFFSafeEffectCloudEntity extends AreaEffectCloud implements INFFSafeTarget<NFFSafeEffectCloudEntity> {

    @Nullable
    protected List<MobEffectInstance> effectOverride = null;
    @Nullable
    protected LivingEntity intendedTarget = null;
    protected INFFSafeTarget.TargetType targetType = INFFSafeTarget.TargetType.ALL;
    protected boolean revertsEffectForUndead = false;
    protected UnaryOperator<MobEffectInstance> effectModifier = i -> i;

    public NFFSafeEffectCloudEntity(EntityType<? extends NFFSafeEffectCloudEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public NFFSafeEffectCloudEntity(Level level, double x, double y, double z) {
        this(NFFGirlsEntityTypes.SAFE_EFFECT_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    public NFFSafeEffectCloudEntity setTargetType(INFFSafeTarget.TargetType type) {
        this.targetType = type;
        return this;
    }

    public NFFSafeEffectCloudEntity setIntendedTarget(@Nullable LivingEntity intendedTarget) {
        this.intendedTarget = intendedTarget;
        return this;
    }

    @Override
    public @Nullable LivingEntity getIntendedTarget() {
        return intendedTarget;
    }

    @Override
    public INFFSafeTarget.TargetType getTargetType() {
        return targetType;
    }

    public boolean shouldRevertEffectForUndead() {
        return revertsEffectForUndead;
    }

    public NFFSafeEffectCloudEntity setRevertsEffectForUndead(boolean revertsEffectForUndead) {
        this.revertsEffectForUndead = revertsEffectForUndead;
        return this;
    }

    public UnaryOperator<MobEffectInstance> getEffectModifier() {
        return effectModifier;
    }

    public NFFSafeEffectCloudEntity setEffectModifier(UnaryOperator<MobEffectInstance> effectModifier) {
        this.effectModifier = effectModifier;
        return this;
    }

    public Optional<List<MobEffectInstance>> getEffectOverride() {
        return Optional.ofNullable(this.effectOverride);
    }

    public NFFSafeEffectCloudEntity setEffectOverride(@Nullable List<MobEffectInstance> list) {
        this.effectOverride = list;
        return this;
    }

    @Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        private static final Method METHOD_COPY_EFFECT_INSTANCE =
            NFUReflectionStatics.findMethodIfDeclared(MobEffectInstance.class, "m_19548_", MobEffectInstance.class)
                .orElseThrow();

        @SubscribeEvent
        public static void onAddEffect(EffectCloudTakeEffectEvent event) {
            if (event.getEntity() instanceof NFFSafeEffectCloudEntity stp) {
                // Check if should affect
                boolean shouldAffect;
                if (event.getAffectedEntity().equals(stp.getIntendedTarget()))
                    shouldAffect = true;
                else if (INFFTamed.get(event.getEntity().getOwner())
                    .filter(t -> !stp.canAffectNFF(event.getAffectedEntity(), t, stp.getTargetType()))
                    .isPresent())
                    shouldAffect = false;
                else shouldAffect = true;
                if (!shouldAffect) {
                    event.setCanceled(true);
                    return;
                }
                stp.getEffectOverride().ifPresent(list -> {
                    event.getApplyingEffects().clear();
                    event.getApplyingEffects().addAll(list);
                });
                // Revert for undead
                if (stp.shouldRevertEffectForUndead() && event.getAffectedEntity().getMobType().equals(MobType.UNDEAD)) {
                    List<MobEffectInstance> revertedList = event.getApplyingEffects().stream()
                        .map(inst -> {
                            boolean reverted = true;
                            MobEffect effect = inst.getEffect();
                            if (effect.equals(MobEffects.HEAL))
                                effect = MobEffects.HARM;
                            else if (effect.equals(MobEffects.HARM))
                                effect = MobEffects.HEAL;
                            else reverted = false;
                            if (reverted) {
                                MobEffectInstance newEffect = new MobEffectInstance(effect);
                                NFUReflectionStatics.invokeMethod(METHOD_COPY_EFFECT_INSTANCE, newEffect, inst);
                                return newEffect;
                            }
                            else return inst;
                        })
                        .map(stp.getEffectModifier()).toList();
                    event.getApplyingEffects().clear();
                    event.getApplyingEffects().addAll(revertedList);
                }
            }
        }
    }

}
