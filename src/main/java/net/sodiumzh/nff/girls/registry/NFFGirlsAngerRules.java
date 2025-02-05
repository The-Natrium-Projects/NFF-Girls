package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nautils.entity.anger.MobAngerReason;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.registries.NaUtilsRegistries;
import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nff.girls.NFFGirls;

public class NFFGirlsAngerRules {

	public static final RegistryEntryCollection<MobAngerRules> ANGER_RULES
			= RegistryEntryCollection.create(NaUtilsRegistries.MOB_ANGER_RULES, NFFGirls.MOD_ID);

	/**
	 * Default for NFF-Girls. The mob will have major anger for 5 min to which damaged it, and have minor anger
	 * of 30s to which attacked it but not dealt damage.
	 */
	public static final NaUtilsRegistry.Accessor<MobAngerRules> DEFAULT
			= ANGER_RULES.register("default",
			() -> new MobAngerRules()
					.forReason(MobAngerReason.ATTACKED.get())
					.forReason(MobAngerReason.HIT.get(), 30 * 20)
					.end());

	/**
	 * The mob will have major anger for 5 min to which damaged it,
	 * and have minor anger of 30s to which attacked it but not dealt damage, or which it attacked and dealt damage.
	 */
	public static final NaUtilsRegistry.Accessor<MobAngerRules> ATTACKER_AND_MINOR_ATTACKING
			= ANGER_RULES.register("attacker_and_minor_attacking" ,
			() -> new MobAngerRules()
					.forReason(MobAngerReason.ATTACKING.get())
					.forReason(MobAngerReason.HIT.get(), 30 * 20)
					.forReason(MobAngerReason.ATTACKING.get(), 30 * 20)
					.end());
}
