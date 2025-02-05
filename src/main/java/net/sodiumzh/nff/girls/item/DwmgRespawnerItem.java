package net.sodiumzh.nff.girls.item;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;

public class DwmgRespawnerItem extends NFFMobRespawnerItem
{

	public DwmgRespawnerItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		if (stack.getTag() != null)
		{
			NFFMobRespawnerInstance inst = NFFMobRespawnerInstance.createIfValid(stack);
			if (inst == null) return;
			MutableComponent name = (MutableComponent) (inst.getName());
			MutableComponent type = (MutableComponent) inst.getType().getDescription();
			
			if (name != null && type != null)
			{
				list.add(NaUtilsInfoStatics.createTranslatable("item.nffgirls.respawner.name").append(name));
				list.add(NaUtilsInfoStatics.createTranslatable("item.nffgirls.respawner.type").append(type));
			}
		}
	}

	public Optional<ItemStack> getDefaultInstanceOverride()
	{
		return Optional.of(ItemStack.EMPTY);
	}
}
