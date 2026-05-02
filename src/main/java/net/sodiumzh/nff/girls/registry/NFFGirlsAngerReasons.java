package net.sodiumzh.nff.girls.registry;

import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.anger.MobAngerReason;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsAngerReasons {

    public static final NFURegistryEntryCollection<MobAngerReason> COLLECTION = NFURegistryEntryCollection.create(
        NFURegistries.MOB_ANGER_REASONS, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<MobAngerReason> OTHER_ANGRY = COLLECTION.register("other_angry", MobAngerReason::new);

}
