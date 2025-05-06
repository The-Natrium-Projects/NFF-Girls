package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.util.NFUEntityStatics;

public class HmagAnimalTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	protected static final String NBT_KEY_STRENGTH = "strength";

	@Override
	public void tamableInit(CNFFTamable cap)
	{
		cap.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, 0d);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public void serverTick(Mob mob)
	{
		CNFFTamable.getOptional(mob).ifPresent(tamable -> {
			super.serverTick(mob);
			if (tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) >= 1e-5d)
				NFUEntityStatics.addEffectSafe(mob, new MobEffectInstance(
						MobEffects.DAMAGE_BOOST, 10, (int)(tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) / 0.2)));
			tamable.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, Math.max(tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) - 5e-5d, 0d));	// decrease by 0.001 per second
		});

	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

	@Override
	public void onConsumeItem(Mob mob, ItemStack item, double deltaProc)
	{
		CNFFTamable.getOptional(mob).ifPresent(tamable -> {
			tamable.getGeneralNBT().putDouble(NBT_KEY_STRENGTH, tamable.getGeneralNBT().getDouble(NBT_KEY_STRENGTH) + deltaProc);
		});
	}
	
}
