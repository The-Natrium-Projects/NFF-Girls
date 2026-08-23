package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

@Deprecated
public interface INFFGirlsTamedSunSensitiveMob extends INFFGirlsTamed {

    @Override
    public default boolean enableSunSensitivity() {
        return true;
    }

}
