package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.component.WildMobNeutralityHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerHandlerComponent;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.component.EntityComponentInitEvent;
import net.sodiumzh.nfu.entity.component.EntityComponentType;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsEntityComponents {

    public static final NFURegistryEntryCollection<EntityComponentType<?, ?>> COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.ENTITY_COMPONENT_TYPES, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<EntityComponentType<Mob, MobAngerHandlerComponent>> UNDEAD_AFFINITY_HANDLER =
        COLLECTION.register("undead_affinity_handler", () -> new EntityComponentType<>(Mob.class, MobAngerHandlerComponent.class,
            m -> new MobAngerHandlerComponent(m, NFFGirlsAngerRules.ZOMBIE_PIGLIN_LIKE.get())));
    public static final NFURegistry.Accessor<EntityComponentType<Mob, WildMobNeutralityHandlerComponent>> WILD_MOB_NEUTRALITY_HANDLER =
        COLLECTION.register("wild_mob_neutrality_handler", () -> new EntityComponentType<>(Mob.class, WildMobNeutralityHandlerComponent.class,
            m -> new WildMobNeutralityHandlerComponent(m, MobAngerRules.ATTACKER.get())));

    @Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Attachment {

        @SubscribeEvent
        public static void attach(EntityComponentInitEvent event) {
            if (event.getEntity() instanceof Mob mob) {
                if (mob.getMobType().equals(MobType.UNDEAD)) {
                    event.getComponentManager().setRequired("/undead_affinity_handler", UNDEAD_AFFINITY_HANDLER.get());
                }
                event.getComponentManager().setRequired("/wild_mob_neutrality_handler", WILD_MOB_NEUTRALITY_HANDLER.get());
            }
        }

    }


}
