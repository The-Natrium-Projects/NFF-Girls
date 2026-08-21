package net.sodiumzh.nff.girls.mixin;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEvents;
import net.sodiumzh.nfu.mixin.NFUMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JackFrostEntity.class)
public class NFFGirlsJackFrostEntityMixin implements NFUMixin<JackFrostEntity>
{
	@WrapWithCondition(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "com/github/mechalopa/hmag/world/entity/JackFrostEntity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
			expect = -1)
	private boolean canTakeMeltingBiomeDamage(JackFrostEntity caller, DamageSource dmgSource, float value)
	{
		return !MinecraftForge.EVENT_BUS.post(new NFFGirlsEvents.JackFrostCheckMeltingBiomeEvent(caller));
	}
}
