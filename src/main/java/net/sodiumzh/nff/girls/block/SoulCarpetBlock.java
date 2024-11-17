package net.sodiumzh.nff.girls.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsBlocks;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;

/**
 * Soul Carpet is a carpet which provides mobs Death Affinity effect on it
 */

public class SoulCarpetBlock extends CarpetBlock 
{
	public SoulCarpetBlock(Properties prop) 
	{
		super(prop);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) 
	{
		if (!level.isClientSide && entityIn != null && entityIn instanceof LivingEntity)
		{
			LivingEntity livingentity = (LivingEntity) entityIn;
			NaUtilsEntityStatics.addEffectIfNotHaving(livingentity, new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(), 20, 0, true, true));
			// If player has <2s Wither I (usually brought by Necromancer's Hat), remove it
			if (livingentity.hasEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()) 
					&& livingentity.getEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()).getAmplifier() == 0
					&& livingentity.getEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()).getDuration() <= 40)
			{
				livingentity.removeEffect(NFFGirlsEffects.NECROMANCER_WITHER.get());
			}
		}
	}
	
	public static boolean isEntityInside(LivingEntity living)
	{

		return living.level().getBlockState(living.blockPosition()).is(NFFGirlsBlocks.SOUL_CARPET.get());
	}
	
}
