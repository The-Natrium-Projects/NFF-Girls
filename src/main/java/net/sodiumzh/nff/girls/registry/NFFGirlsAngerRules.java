package net.sodiumzh.nff.girls.registry;

import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.anger.MobAngerReason;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import org.checkerframework.checker.units.qual.A;

public class NFFGirlsAngerRules {

	public static final NFURegistryEntryCollection<MobAngerRules> ANGER_RULES
			= NFURegistryEntryCollection.create(NFURegistries.MOB_ANGER_RULES, NFFGirls.MOD_ID);

	/**
	 * Default for NFF-Girls. The mob will have major anger for 5 min to which damaged it, and have minor anger
	 * of 30s to which attacked it but not dealt damage.
	 */
	public static final NFURegistry.Accessor<MobAngerRules> DEFAULT
			= ANGER_RULES.register("default",
			() -> new MobAngerRules()
					.forReason(MobAngerReason.ATTACKED.get())
					.forReason(MobAngerReason.HIT.get(), 30 * 20)
					.end());

	/**
	 * The mob will have major anger for 5 min to which damaged it,
	 * and have minor anger of 30s to which attacked it but not dealt damage, or which it attacked and dealt damage.
	 */
	public static final NFURegistry.Accessor<MobAngerRules> ATTACKER_AND_MINOR_ATTACKING
			= ANGER_RULES.register("attacker_and_minor_attacking" ,
			() -> new MobAngerRules()
					.forReason(MobAngerReason.ATTACKED.get())
					.forReason(MobAngerReason.HIT.get(), 30 * 20)
					.forReason(MobAngerReason.ATTACKING.get(), 30 * 20)
					.end());

	/**
	 * The mob will have major anger for 5 min to which damaged it,
	 * and have minor anger of 30s to which attacked it but not dealt damage.
	 */
	public static final NFURegistry.Accessor<MobAngerRules> ATTACKER_AND_MINOR_HIT
		= ANGER_RULES.register("attacker_and_minor_hit" ,
		() -> new MobAngerRules()
			.forReason(MobAngerReason.ATTACKED.get())
			.forReason(MobAngerReason.HIT.get(), 30 * 20)
			.end());

	public static final NFURegistry.Accessor<MobAngerRules> UNDEAD_AFFINITY
		= ANGER_RULES.register("undead_affinity",
		() -> new MobAngerRules()
			.forReason(Mob))
}
