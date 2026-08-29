package net.sodiumzh.nff.girls.entity.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityComponents;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.registry.NFFEntityComponents;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.anger.MobSetAngerResult;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.object.ServerOnly;
import net.sodiumzh.nfu.registry.NFUEntityComponents;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.stream.Stream;

/**
 * NFF Girls friended mobs neutralize their "wild" versions.
 * <p>Rules:
 * <p>a) When a wild mob sees a friended one, it gets "strong-neutral" to the owner;
 * <p>b) When a wild mob sees a "strong-neutral" wild mob, it gets "weak-neutral". Weak-neutral mobs don't spread neutrality;
 * <p>c) When a wild mob gets attacked by a player it's neutral to, it gets angry, and no longer neutral at any level;
 * <d>d) When a wild mob sees a angry wild mob, it's no longer neutral.
 */
public class NFFGirlsNeutralityHandlerComponent extends MobAngerHandlerComponent {

    private final Set<UUID> strongNeutralTo = new HashSet<>();
    private final Set<UUID> weakNeutralTo = new HashSet<>();

    public NFFGirlsNeutralityHandlerComponent(Mob mob, MobAngerRules rules) {
        super(mob, rules);
    }

    public void onAngryAt(LivingEntity target, int forgivingTicks, MobSetAngerResult setResult) {
        if (forgivingTicks != 0) {
            strongNeutralTo.remove(target.getUUID());
            weakNeutralTo.remove(target.getUUID());
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        ListTag strongNeutral = new ListTag();
        strongNeutral.addAll(this.strongNeutralTo.stream().map(NbtUtils::createUUID).toList());
        nbt.put("strongNeutral", strongNeutral);
        ListTag weakNeutral = new ListTag();
        weakNeutral.addAll(this.weakNeutralTo.stream().map(NbtUtils::createUUID).toList());
        nbt.put("weakNeutral", weakNeutral);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.strongNeutralTo.clear();
        this.strongNeutralTo.addAll(nbt.getList("strongNeutral", Tag.TAG_INT_ARRAY).stream().map(NbtUtils::loadUUID).toList());
        this.weakNeutralTo.clear();
        this.weakNeutralTo.addAll(nbt.getList("weakNeutral", Tag.TAG_INT_ARRAY).stream().map(NbtUtils::loadUUID).toList());
    }

    public List<Player> getNeutralPlayers() {
        return Stream.concat(this.strongNeutralTo.stream(), this.weakNeutralTo.stream())
            .map(this.getEntity().getLevel()::getPlayerByUUID).filter(Objects::nonNull).toList();
    }

    public NeutralityState getNeutralityState(UUID uuid) {
        return this.strongNeutralTo.contains(uuid) ? NeutralityState.STRONG : (
            this.weakNeutralTo.contains(uuid) ? NeutralityState.WEAK : NeutralityState.NOT
            );
    }

    public NeutralityState getNeutralityState(LivingEntity e) {
        if (this.isAngryAt(e)) return NeutralityState.NOT;
        return getNeutralityState(e.getUUID());
    }

    public boolean isNeutralTo(LivingEntity e) {
        return this.getNeutralityState(e).isNeutral();
    }

    public void addNeutral(UUID uuid, boolean isStrong) {
        if (isStrong) {
            this.strongNeutralTo.add(uuid);
            this.weakNeutralTo.remove(uuid);
        } else {
            if (!this.strongNeutralTo.contains(uuid))
                this.weakNeutralTo.add(uuid);
        }
        // Refresh target no matter if it has actually set neutral
        if (this.getEntity().getTarget() != null && this.getEntity().getTarget().getUUID().equals(uuid)) {
            this.getEntity().setTarget(null);
        }
    }

    public void addNeutral(Player player, boolean isStrong) {
        this.addNeutral(player.getUUID(), isStrong);
    }

    public void removeNeutral(UUID uuid) {
        strongNeutralTo.remove(uuid);
        weakNeutralTo.remove(uuid);
    }

    public void removeNeutral(Player player) {
        removeNeutral(player.getUUID());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isClientSide()) return;
        if (this.getEntity().tickCount % 20 != 0) return;   // Search each second to save resource
        if (this.strongNeutralTo.isEmpty() && this.angerList.isEmpty()) return;
        this.getEntity().getLevel().getEntitiesOfClass(Mob.class, this.getEntity().getBoundingBox().inflate(16d))
            .stream().filter(mob -> mob.getType().equals(this.getEntity().getType()))
            .filter(mob -> mob.hasLineOfSight(this.getEntity()))
            .forEach(mob -> {
                EntityComponentAPI.getComponentByPath(mob, NFFGirlsEntityComponents.ACCESSOR_NEUTRALITY_HANDLER)
                    .ifPresent(c -> {
                        for (UUID uuid : this.strongNeutralTo)
                            c.addNeutral(uuid, false);
                        for (UUID uuid : this.angerList.keySet())
                            c.removeNeutral(uuid);
                    });
            });
    }

    /**
     * Invoked on friended mob tick, spread strong neutrality to wild mobs
     * @see
     */
    public static void spreadStrongNeutral(LivingEntity living) {
        if (living.getLevel().isClientSide()) return;
        if (living.tickCount % 20 != 0) return;
        if (!NFFTamingMapping.containsAfter(living.getType())) return;
        EntityType<? extends Mob> typeBefore = NFFTamingMapping.getTypeBefore(living.getType());
        INFFGirlsTamed tamed = INFFGirlsTamed.get(living).orElse(null);
        if (tamed == null) return;
        Player owner = tamed.getOwnerInDimension();
        if (owner == null) return;
        living.getLevel().getEntitiesOfClass(Mob.class, living.getBoundingBox().inflate(16d)).stream()
            .filter(m -> m.getType().equals(typeBefore))
            .filter(m -> m.hasLineOfSight(living) && m.hasLineOfSight(owner))

            .forEach(m -> EntityComponentAPI.getComponentByPath(m, NFFGirlsEntityComponents.ACCESSOR_NEUTRALITY_HANDLER)
                .ifPresent(c -> {
                    if (!c.isAngryAt(owner))
                        c.addNeutral(owner, true);
                }));
    }

    public static enum NeutralityState {
        STRONG, WEAK, NOT;
        public boolean isNeutral() {
            return !this.equals(NOT);
        }
    }






}
