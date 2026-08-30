package net.sodiumzh.nff.girls.mixin;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEvents;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsHooks;
import net.sodiumzh.nfu.mixin.NFUMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JackFrostEntity.class)
public class NFFGirlsJackFrostEntityMixin implements NFUMixin<JackFrostEntity>
{
	@WrapOperation(method = "aiStep()V", at = @At(value = "INVOKE",
		target = "Lcom/github/mechalopa/hmag/world/entity/JackFrostEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private boolean allowMelting(JackFrostEntity entity, DamageSource dmg, float amount, Operation<Boolean> original) {
		if (!MinecraftForge.EVENT_BUS.post(new NFFGirlsEvents.JackFrostCheckMeltingBiomeEvent(entity))) {
			return false;
		}
		return original.call(entity, dmg, amount);
	}
}
