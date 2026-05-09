package net.sodiumzh.nff.girls.entity.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityComponents;
import net.sodiumzh.nff.girls.registry.NFFGirlsNeutralizationMapping;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.anger.MobSetAngerResult;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.object.ServerOnly;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class NFFGirlsNeutralityHandlerComponent extends MobAngerHandlerComponent {

    protected final Set<UUID> neutralTo = new HashSet<>();

    public NFFGirlsNeutralityHandlerComponent(Mob mob, MobAngerRules rules) {
        super(mob, rules);
    }

    public void onAngryAt(LivingEntity target, int forgivingTicks, MobSetAngerResult setResult) {
        if (forgivingTicks != 0) {
            neutralTo.remove(target.getUUID());
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        ListTag neutralList = new ListTag();
        neutralList.addAll(this.neutralTo.stream().map(NbtUtils::createUUID).toList());
        nbt.put("neutralList", neutralList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.neutralTo.clear();
        this.neutralTo.addAll(nbt.getList("neutralList", Tag.TAG_INT_ARRAY).stream().map(NbtUtils::loadUUID).toList());
    }

    public List<Player> getNeutralPlayers() {
        return this.neutralTo.stream().map(this.getEntity().level()::getPlayerByUUID).filter(Objects::nonNull).toList();
    }

    public boolean isNeutralTo(LivingEntity e) {
        return this.neutralTo.contains(e.getUUID());
    }

    public void addNeutral(Player player) {
        this.neutralTo.add(player.getUUID());
    }

    public void addNeutralAndSpread(Player player) {
        this.addNeutral(player);
        this.getEntity().level().getEntitiesOfClass(this.getEntity().getClass(), this.getEntity().getBoundingBox().inflate(8d, 8d, 8d))
            .stream().filter(m -> m.getType().equals(this.getEntity().getType()))
            .filter(m -> this.getEntity().hasLineOfSight(m))
            .forEach(m ->
                EntityComponentAPI.getComponentManager(m).getSubComponentByPath(this.getPathFromRoot())
                .filter(c -> c instanceof NFFGirlsNeutralityHandlerComponent).map(c -> (NFFGirlsNeutralityHandlerComponent)c)
                    .ifPresent(c -> c.addNeutral(player))
            );
    }

    // Event listener methods

    /* Rules:
    * (a) When a wild mob sees a friended mob and the owner player, it gets neutral;
    * (b) When an unprovoked wild mobs see a neutralized mob and the player, it gets neutral;
    * (c) When a neutral mob sees a provoked (only by player attacking) one, it's no longer neutral (but not provoked).
    */
    @ApiStatus.Internal
    public static void updateOnServerTick(Mob mobUpdated) {
        // Friended mob neutralizes wild ones
        // Now mobUpdated is the friended mob
        if (INFFGirlsTamed.get(mobUpdated).filter(INFFGirlsTamed::isOwnerInDimension).isPresent()) {
            mobUpdated.level().getEntitiesOfClass(Mob.class, mobUpdated.getBoundingBox().inflate(16d), m ->
               m.getType().equals(NFFTamingMapping.getTypeBefore(mobUpdated))).forEach(m ->
                EntityComponentAPI.getComponentManager(m).getSubComponentByPath("/neutrality_handler", NFFGirlsEntityComponents.NEUTRALITY_HANDLER.get()).ifPresent(c -> {
                    Player owner = INFFGirlsTamed.get(mobUpdated).map(INFFGirlsTamed::getOwnerInDimension).orElseThrow();
                    if (!c.isAngryAt(owner) && !c.isAngryAt(mobUpdated) && m.hasLineOfSight(mobUpdated) && m.hasLineOfSight(owner))
                        c.addNeutral(INFFGirlsTamed.get(mobUpdated).map(INFFGirlsTamed::getOwnerInDimension).orElseThrow());
                }));
        }
        // Neutralized mob spreads neutrality
        // Now mobUpdated is the neutralized mob
        // Update each second for performance
        if (mobUpdated.tickCount / 20 == 10) {
            EntityComponentAPI.getComponentManager(mobUpdated).getSubComponentByPath("/neutrality_handler", NFFGirlsEntityComponents.NEUTRALITY_HANDLER.get()).ifPresent(c -> {
                List<Player> neutralToPlayers = c.getNeutralPlayers();
                if (!neutralToPlayers.isEmpty()) {
                    List<Mob> surroundingMobsSameType = mobUpdated.level().getEntitiesOfClass(Mob.class, mobUpdated.getBoundingBox().inflate(12d),
                        m -> mobUpdated.getType().equals(m.getType()) && m.hasLineOfSight(mobUpdated));
                    for (Player player: neutralToPlayers) {
                        surroundingMobsSameType.stream().filter(m -> m.hasLineOfSight(player)).forEach(m ->
                            EntityComponentAPI.getComponentManager(m).getSubComponentByPath("/neutrality_handler", NFFGirlsEntityComponents.NEUTRALITY_HANDLER.get()).ifPresent(c1 -> {
                                if (!c1.isAngryAt(player))
                                    c1.addNeutral(player);
                            }));
                    }
                }
            });
        }
        // Provoked mob spreads anger. This is handled in the implementation of anger reason "OTHER_ANGRY".
    }
}
