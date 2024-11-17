package net.sodiumzh.nff.girls.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nautils.mixin.NaUtilsMixin;
import net.sodiumzh.nff.girls.event.NFFGirlsHooks;

@Mixin(JackFrostEntity.class)
public class NFFGirlsJackFrostEntityMixin implements NaUtilsMixin<JackFrostEntity>
{
	@Inject(method = "isMeltingBiome(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;)Z", at = @At("HEAD"), cancellable = true, remap = false, expect = -1)
	private static void isMeltingBiome(Entity e, Level level, CallbackInfoReturnable<Boolean> cir)
	{
		if (e instanceof JackFrostEntity jf)
		{
			if (MinecraftForge.EVENT_BUS.post(new NFFGirlsHooks.JackFrostCheckMeltingBiomeEvent(jf)))
			{
				cir.setReturnValue(false);
			}
		}
	}
}
