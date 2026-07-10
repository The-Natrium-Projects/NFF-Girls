package net.sodiumzh.nff.girls.registry;

import com.sun.jna.platform.win32.COM.IStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAttackingStrategy;
import net.sodiumzh.nfu.network.NFUDataSerializer;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsDataSerializers {

    public static final NFURegistryEntryCollection<NFUDataSerializer<?>> COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.DATA_SERIALIZERS, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<NFUDataSerializer<NFFGirlsAttackingStrategy>> ATTACKING_STRATEGY =
        COLLECTION.register("attacking_strategy", () -> NFUDataSerializer.create(NFFGirlsAttackingStrategy.class, CompoundTag.class,
            (FriendlyByteBuf buf, NFFGirlsAttackingStrategy strategy) -> strategy.writeBuf(buf),
            NFFGirlsAttackingStrategy::readBuf,
            NFFGirlsAttackingStrategy::toNBT,
            NFFGirlsAttackingStrategy::fromNBT));


}
