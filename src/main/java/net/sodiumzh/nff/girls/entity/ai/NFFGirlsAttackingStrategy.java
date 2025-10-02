package net.sodiumzh.nff.girls.entity.ai;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NFFGirlsAttackingStrategy {

    private final Set<EntityType<? extends Mob>> activeAttackingList = new HashSet<>();
    private final Set<EntityType<? extends Mob>> notAttackingList = new HashSet<>();

    public Set<EntityType<? extends Mob>> getActiveAttackingList() {
        return activeAttackingList;
    }

    public Set<EntityType<? extends Mob>> getNotAttackingList() {
        return notAttackingList;
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag attacking = new ListTag();
        ListTag notAttacking = new ListTag();
        this.activeAttackingList.stream().map(ForgeRegistries.ENTITY_TYPES::getKey)
            .filter(Objects::nonNull)
            .map(ResourceLocation::toString)
            .forEach(str -> attacking.add(StringTag.valueOf(str)));
        this.notAttackingList.stream().map(ForgeRegistries.ENTITY_TYPES::getKey)
            .filter(Objects::nonNull)
            .map(ResourceLocation::toString)
            .forEach(str -> notAttacking.add(StringTag.valueOf(str)));
        nbt.put("attacking", attacking);
        nbt.put("notAttacking", notAttacking);
        return nbt;
    }

    @SuppressWarnings("unchecked")
    public static NFFGirlsAttackingStrategy fromNBT(CompoundTag nbt) {
        try {
            NFFGirlsAttackingStrategy strategy = new NFFGirlsAttackingStrategy();
            nbt.getList("attacking", Tag.TAG_STRING).stream()
                .map(t -> new ResourceLocation(t.getAsString()))
                .map(ForgeRegistries.ENTITY_TYPES::getValue)
                .filter(Objects::nonNull)
                .map(t -> (EntityType<? extends Mob>) t)
                .forEach(strategy.activeAttackingList::add);
            nbt.getList("notAttacking", Tag.TAG_STRING).stream()
                .map(t -> new ResourceLocation(t.getAsString()))
                .map(ForgeRegistries.ENTITY_TYPES::getValue)
                .filter(Objects::nonNull)
                .map(t -> (EntityType<? extends Mob>) t)
                .forEach(strategy.notAttackingList::add);
            return strategy;
        } catch (RuntimeException e) {
            LogUtils.getLogger().error("NFF: Girls Attacking Strategy loading failed:", e);
            return new NFFGirlsAttackingStrategy();
        }
    }

    public boolean shouldActivelyAttack(INFFGirlsTamed attacker, Mob target) {
        if (NFFTamedStatics.isLivingAlliedToBM(attacker, target)) return false;
        if (Arrays.stream(attacker.notAttacksIgnoringStrategy()).anyMatch(et -> target.getType().equals(et))) return false;
        if (Arrays.stream(attacker.activelyAttacksIgnoringStrategy()).anyMatch(et -> target.getType().equals(et))) return true;
        if (notAttackingList.contains(target.getType())) return false;
        if (activeAttackingList.contains(target.getType())) return true;
        return false;
    }

    public boolean shouldNeverAttack(INFFGirlsTamed attacker, Mob target) {
        if (Arrays.stream(attacker.notAttacksIgnoringStrategy()).anyMatch(et -> target.getType().equals(et))) return true;
        if (Arrays.stream(attacker.activelyAttacksIgnoringStrategy()).anyMatch(et -> target.getType().equals(et))) return false;
        if (notAttackingList.contains(target.getType())) return true;
        return false;
    }

    public static NFFGirlsAttackingStrategy empty() {
        return new NFFGirlsAttackingStrategy();
    }

    public boolean isEmpty() {
        return this.activeAttackingList.isEmpty() && this.notAttackingList.isEmpty();
    }

}
