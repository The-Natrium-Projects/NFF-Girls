package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.girls.entity.hmag.HmagCreeperGirlEntity;
import net.sodiumzh.nff.services.entity.taming.preset.NFFTamedCreeperPreset;

public class NFFGirlsHmagCreeperGirlSwellGoal extends NFFTamedCreeperPreset.SwellGoal
{
	
	protected HmagCreeperGirlEntity cg;
	
	public NFFGirlsHmagCreeperGirlSwellGoal(HmagCreeperGirlEntity creeper)
	{
		super(creeper);
		this.cg = (HmagCreeperGirlEntity)creeper;
		targetedSwelling = false;
	}
	
	@Override
	public boolean checkCanUse()
	{
		if (isDisabled())
			return false;
		return cg.getSwell() > 0;
	}
	
	private boolean stoppedFlag = false;
	@Override
	public void onTick()
	{
		if (cg.getOwner() != null && cg.distanceToSqr(cg.getOwner()) <= cg.explodeSafeDistance * cg.explodeSafeDistance)
		{
			cg.setSwellDir(-1);
			stoppedFlag = true;
		}
		else if (stoppedFlag)
		{
			cg.setSwellDir(1);
			stoppedFlag = false;
		}
	}
	
	
}
