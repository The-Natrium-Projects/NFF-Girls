package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.component.NFFGirlsNeutralityHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.component.EntityComponentSetupEvent;
import net.sodiumzh.nfu.entity.component.EntityComponentType;
import net.sodiumzh.nfu.network.AvailableSide;
import net.sodiumzh.nfu.object.HierarchyPath;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsEntityComponents {

    public static final NFURegistryEntryCollection<EntityComponentType<?, ?>> COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.ENTITY_COMPONENT_TYPES, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<EntityComponentType<Mob, MobAngerHandlerComponent>> UNDEAD_AFFINITY_HANDLER =
        COLLECTION.register("undead_affinity_handler", () -> new EntityComponentType<>(Mob.class, MobAngerHandlerComponent.class, AvailableSide.SERVER,
            m -> new MobAngerHandlerComponent(m, NFFGirlsAngerRules.ZOMBIE_PIGLIN_LIKE.get())));
    public static final NFURegistry.Accessor<EntityComponentType<Mob, NFFGirlsNeutralityHandlerComponent>> NEUTRALITY_HANDLER =
        COLLECTION.register("neutrality_handler", () -> new EntityComponentType<>(Mob.class, NFFGirlsNeutralityHandlerComponent.class, AvailableSide.SERVER,
            m -> new NFFGirlsNeutralityHandlerComponent(m, MobAngerRules.ATTACKER.get())));

    public static final HierarchyPath PATH_NFF_GIRLS = HierarchyPath.byLiteral("/nff/girls");
    public static final HierarchyPath PATH_UNDEAD_AFFINITY_HANDLER = HierarchyPath.byLiteral("/nff/girls/undead_affinity_handler");
    public static final HierarchyPath PATH_NEUTRALITY_HANDLER = HierarchyPath.byLiteral("/nff/girls/neutrality_handler");

    @Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Attachment {

        @SubscribeEvent
        public static void attach(EntityComponentSetupEvent event) {
            if (event.getEntity() instanceof Mob mob) {
                event.addNode(PATH_NFF_GIRLS);
                MobType mobtype = MobType.UNDEFINED;
                // Some mods may not be able to load mob type at this phase since many parameters are not initialized
                // e.g. Target Dummy. At this time ignore it.
                try {mobtype = mob.getMobType();} catch (NullPointerException ignored) {}
                if (mobtype.equals(MobType.UNDEAD)) {
                    event.addComponent(PATH_UNDEAD_AFFINITY_HANDLER, UNDEAD_AFFINITY_HANDLER.get());
                }
                event.addComponent(PATH_NEUTRALITY_HANDLER, NEUTRALITY_HANDLER.get());
            }
        }

    }


}
