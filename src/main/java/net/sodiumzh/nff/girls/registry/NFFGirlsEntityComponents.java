package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.component.EntityComponentType;
import net.sodiumzh.nfu.entity.component.IEntityComponent;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsEntityComponents {

    public static final NFURegistryEntryCollection<EntityComponentType<?, ?>> COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.ENTITY_COMPONENT_TYPES, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<EntityComponentType<Mob, MobAngerHandlerComponent>> UNDEAD_AFFINITY_HANDLER =
        COLLECTION.register("undead_affinity_handler", () -> new EntityComponentType<>(Mob.class, MobAngerHandlerComponent.class,
            MobAngerHandlerComponent::new))

}
