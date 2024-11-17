package net.sodiumzh.nff.girls.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.blocks.SoulCarpetBlock;
import net.sodiumzh.nff.girls.entity.hmag.HmagNecroticReaperEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

public class NecromancerArmorItem extends ArmorItem
{

	public NecromancerArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties)
	{
		super(pMaterial, pSlot, pProperties);
	}
	
	// Called only in LivingUpdateEvent listener
	public static void necromancerArmorUpdate(LivingEntity living)
	{				
		if (living instanceof ArmorStand)
			return;

		if (living.getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.NECROMANCER_HAT.get()))
		{
			NaUtilsEntityStatics.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 1, true, false));
			NaUtilsEntityStatics.addEffectIfNotHaving(living, new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(), 40, 0, true, false));
			NaUtilsEntityStatics.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.DIG_SPEED, 40, 1, true, false));
			if (!SoulCarpetBlock.isEntityInside(living))
				NaUtilsEntityStatics.addEffectIfNotHaving(living, new MobEffectInstance(NFFGirlsEffects.NECROMANCER_WITHER.get(), 40, 0, true, false));	
			if (living instanceof Player p)
			{
				// Nearby Necrotic Reapers add regeneration
				int regLvl = HmagNecroticReaperEntity.countNearby(p) - 1;
				// Max level 3
				if (regLvl > 2)
					regLvl = 2;
				if (regLvl >= 0)
				{
					NaUtilsEntityStatics.addEffectIfNotHaving(living, new MobEffectInstance(MobEffects.REGENERATION, 80, regLvl, true, false)); 
				}
			}
		}	
	}

}
