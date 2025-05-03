package net.sodiumzh.nff.girls.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class NFFGirlsRespawnerItem extends NFFMobRespawnerItem
{

	public NFFGirlsRespawnerItem(Properties pProperties)
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
				list.add(NFUInfoStatics.createTranslatable("item.nffgirls.respawner.name").append(name));
				list.add(NFUInfoStatics.createTranslatable("item.nffgirls.respawner.type").append(type));
			}
		}
	}

	public Optional<ItemStack> getDefaultInstanceOverride()
	{
		return Optional.of(ItemStack.EMPTY);
	}

	@Override
	public boolean shouldConsumeInCreative() {
		return true;
	}
}
