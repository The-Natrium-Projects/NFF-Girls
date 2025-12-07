package net.sodiumzh.nff.girls.entity.projectile;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFSafeTarget;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.exception.ReflectionFailedException;
import net.sodiumzh.nfu.mixin.event.entity.ThrownPotionAddEffectEvent;
import net.sodiumzh.nfu.mixin.event.entity.ThrownPotionEffectCloudEvent;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NFFSafeThrownPotionEntity extends ThrownPotion implements INFFSafeTarget<NFFSafeThrownPotionEntity> {

    @Nullable
    protected List<MobEffectInstance> effectOverride = null;
    @Nullable
    protected LivingEntity intendedTarget = null;
    protected INFFSafeTarget.TargetType targetType = INFFSafeTarget.TargetType.ALL;
    protected boolean revertsEffectForUndead = false;
    protected UnaryOperator<MobEffectInstance> effectModifier = i -> i;

    public NFFSafeThrownPotionEntity(EntityType<? extends NFFSafeThrownPotionEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public NFFSafeThrownPotionEntity(Level pLevel, double pX, double pY, double pZ) {
        this(NFFGirlsEntityTypes.SAFE_THROWN_POTION.get(), pLevel);
        this.setPos(pX, pY, pZ);
    }

    public NFFSafeThrownPotionEntity(Level pLevel, LivingEntity pShooter) {
        this(pLevel, pShooter.getX(), pShooter.getEyeY() - (double)0.1F, pShooter.getZ());
        this.setOwner(pShooter);
    }

    public NFFSafeThrownPotionEntity setTargetType(INFFSafeTarget.TargetType type) {
        this.targetType = type;
        return this;
    }

    public NFFSafeThrownPotionEntity setIntendedTarget(@Nullable LivingEntity intendedTarget) {
        this.intendedTarget = intendedTarget;
        return this;
    }

    @Nullable
    public LivingEntity getIntendedTarget() {
        return intendedTarget;
    }

    public INFFSafeTarget.TargetType getTargetType() {
        return targetType;
    }

    public boolean shouldRevertEffectForUndead() {
        return revertsEffectForUndead;
    }

    public NFFSafeThrownPotionEntity setRevertsEffectForUndead(boolean revertsEffectForUndead) {
        this.revertsEffectForUndead = revertsEffectForUndead;
        return this;
    }

    public UnaryOperator<MobEffectInstance> getEffectModifier() {
        return effectModifier;
    }

    public NFFSafeThrownPotionEntity setEffectModifier(UnaryOperator<MobEffectInstance> effectModifier) {
        this.effectModifier = effectModifier;
        return this;
    }

    public Optional<List<MobEffectInstance>> getEffectOverride() {
        return Optional.ofNullable(this.effectOverride);
    }

    public NFFSafeThrownPotionEntity setEffectOverride(@Nullable List<MobEffectInstance> list) {
        this.effectOverride = list;
        return this;
    }

    private static final Method THROWN_POTION_IS_LINGERING = NFUReflectionStatics.findMethodIfDeclared(
        ThrownPotion.class, "m_37553_").orElseThrow();
    public boolean isLingering() {
        try {
            return (Boolean) THROWN_POTION_IS_LINGERING.invoke(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new ReflectionFailedException(e);
        }
    }

    @Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        private static final Method METHOD_COPY_EFFECT_INSTANCE =
            NFUReflectionStatics.findMethodIfDeclared(MobEffectInstance.class, "m_19548_", MobEffectInstance.class)
                .orElseThrow();

        @SubscribeEvent
        public static void onAddEffect(ThrownPotionAddEffectEvent event) {
            if (event.getEntity() instanceof NFFSafeThrownPotionEntity stp) {
                // Check if should affect
                boolean shouldAffect;
                if (event.getTarget().equals(stp.getIntendedTarget()))
                    shouldAffect = true;
                else if (INFFTamed.get(event.getEntity().getOwner())
                    .filter(t -> !stp.canAffectNFF(event.getTarget(), t, stp.getTargetType()))
                    .isPresent())
                    shouldAffect = false;
                else shouldAffect = true;
                if (!shouldAffect) {
                    event.setCanceled(true);
                    return;
                }
                stp.getEffectOverride().ifPresent(event::setEffects);
                Stream<MobEffectInstance> instStream = event.getEffects().stream();
                // Revert for undead
                if (stp.shouldRevertEffectForUndead() && event.getTarget().getMobType().equals(MobType.UNDEAD)) {
                    instStream = instStream.map(inst -> {
                        MobEffect effect = inst.getEffect();
                        boolean reverted = true;
                        if (effect.equals(MobEffects.HEAL))
                            effect = MobEffects.HARM;
                        else if (effect.equals(MobEffects.HARM))
                            effect = MobEffects.HEAL;
                        else reverted = false;
                        if (reverted) {
                            MobEffectInstance newInst = new MobEffectInstance(effect);
                            NFUReflectionStatics.invokeMethod(METHOD_COPY_EFFECT_INSTANCE, newInst, inst);
                            return newInst;
                        }
                        else return inst;
                    });
                }
                if (stp.getEffectModifier() != null)
                    instStream = instStream.map(inst -> stp.getEffectModifier().apply(inst));
                event.setEffects(instStream.collect(Collectors.toList()));
            }
        }

        @SubscribeEvent
        public static void onSpawnEffectCloud(ThrownPotionEffectCloudEvent.Construct event) {
            if (event.getEntity() instanceof NFFSafeThrownPotionEntity stp) {
                NFFSafeEffectCloudEntity cloud = new NFFSafeEffectCloudEntity(event.getCloud().level(), event.getCloud().getX(), event.getCloud().getY(), event.getCloud().getZ());
                stp.getEffectOverride().ifPresent(cloud::setEffectOverride);
                cloud.setIntendedTarget(stp.getIntendedTarget());
                cloud.setTargetType(stp.getTargetType());
                cloud.setRevertsEffectForUndead(stp.shouldRevertEffectForUndead());
                cloud.setEffectModifier(stp.getEffectModifier());
                event.setCloud(cloud);
            }
        }
    }

}
