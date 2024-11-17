package net.sodiumzh.nff.girls.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nautils.mixin.NaUtilsMixin;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsHooks;

@Mixin(MeltyMonsterEntity.class)
public class NFFGirlsMeltyMonsterEntityMixin implements NaUtilsMixin<MeltyMonsterEntity>
{

	@WrapWithCondition(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), expect = -1)
	private boolean canSetFire(Level instance, BlockPos pos, BlockState blockstate)
	{
		return !MinecraftForge.EVENT_BUS.post(new NFFGirlsHooks.MeltyMonsterSetFireEvent(caller(), pos, blockstate));	
	}
}
