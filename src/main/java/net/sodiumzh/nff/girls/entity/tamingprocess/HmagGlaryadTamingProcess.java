package net.sodiumzh.nff.girls.entity.tamingprocess;

import com.github.mechalopa.hmag.util.ModTags;
import com.github.mechalopa.hmag.world.entity.GlaryadEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagGlaryadTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.hasEffect(MobEffects.REGENERATION) 
				&& mob.getEffect(MobEffects.REGENERATION).getAmplifier() >= 2
				&& mob.getEffect(MobEffects.REGENERATION).getDuration() > 10 * 20
				&& player.getOffhandItem().is(ModTags.ItemTags.GLARYAD_TEMPT_ITEMS)
				&& !player.getUUID().equals(((GlaryadEntity)mob).getPersistentAngerTarget());
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 5 * 20;
	}

	@Override
	public void tamableInit(NFFTamableComponent cnffTamable) {

	}
}
