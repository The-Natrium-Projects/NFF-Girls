package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

public class TaoistTalismanItem extends Item
{

	public TaoistTalismanItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand usedHand)
	{
		if (!player.level().isClientSide && living.getType() == ModEntityTypes.JIANGSHI.get() && living instanceof JiangshiEntity js)
		{
			MutableObject<Boolean> interacted = new MutableObject<>(false);
			if (NFFTamingMapping.getHandler(js) != null && NFFTamingMapping.getHandler(js) instanceof HmagJiangshiTamingProcess handler)
			{
				js.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent(cap ->
				{
					if (handler.applyTalisman(js));
					{
						stack.shrink(1);
						interacted.setValue(true);
						living.level().playSound(null, living, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 2.0f, 1.0f);
					}
				});
				
			}
			if (interacted.getValue())
				return InteractionResult.sidedSuccess(player.level().isClientSide);
		}
		return InteractionResult.PASS;
	}
	
}
