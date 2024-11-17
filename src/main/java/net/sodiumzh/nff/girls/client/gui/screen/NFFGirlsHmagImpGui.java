package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagImpGui extends NFFGirlsHandItemsTwoBaublesGUI
{
	
	public NFFGirlsHmagImpGui(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory, INFFTamed mob)
	{
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	public GuiPos getMainHandIconPos()
	{
		return GuiPos.valueOf(3, 2);
	}
	
	@Override
	public GuiPos getOffHandIconPos()
	{
		return GuiPos.valueOf(3, 4);
	}
}
