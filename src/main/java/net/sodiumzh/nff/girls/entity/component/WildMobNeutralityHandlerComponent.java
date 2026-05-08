package net.sodiumzh.nff.girls.entity.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.anger.MobSetAngerResult;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;

import java.util.*;

public class WildMobNeutralityHandlerComponent extends MobAngerHandlerComponent {

    protected final Set<UUID> neutralTo = new HashSet<>();

    public WildMobNeutralityHandlerComponent(Mob mob, MobAngerRules rules) {
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
                .filter(c -> c instanceof WildMobNeutralityHandlerComponent).map(c -> (WildMobNeutralityHandlerComponent)c)
                    .ifPresent(c -> c.addNeutral(player))
            );
    }
}
