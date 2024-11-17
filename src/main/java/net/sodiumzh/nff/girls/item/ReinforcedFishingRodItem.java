package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.entity.projectile.ReinforcedFishingHookEntity;

public class ReinforcedFishingRodItem extends FishingRodItem
{

	public ReinforcedFishingRodItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (player.fishing != null)
		{
			if (!level.isClientSide)
			{
				if (player.fishing instanceof ReinforcedFishingHookEntity hook)
				{
					boolean playSound = true;
					int dmg = 0;
					if (hook.getHookedIn() != null && !player.isShiftKeyDown() && itemstack.getDamageValue() < itemstack.getMaxDamage())
					{
						if (hook.isInPullingCooldown())
						{
							dmg = 0;
							playSound = false;
						}
						else
						{
							hook.pullEntity(hook.getHookedIn());
							dmg = 1;
						}
					}
					else
					{
						dmg = hook.retrieve(itemstack);
					}
					if (dmg > 0)
					{
						itemstack.hurtAndBreak(dmg, player, (p) -> {
							p.broadcastBreakEvent(hand);
						});
					}
					if (playSound)
					{
						level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
								SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 2.0F,
								0.3F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
					}
				}
				else 
				{
					throw new UnsupportedOperationException("Entity type mismatch: ReinforcedFishingRodItem can only use ReinforcedFishingHookEntity as hook.");
				}
			}
			player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
		} 
		// Throwing same as vanilla
		else
		{
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F,
					0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!level.isClientSide)
			{
				int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
				int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
				level.addFreshEntity(new ReinforcedFishingHookEntity(player, level, j, k));
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			player.gameEvent(GameEvent.ITEM_INTERACT_START);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair)
	{
		return pRepair.is(ModItems.REINFORCING_CHAIN.get());
	}
	
	// For display item icon only
	@OnlyIn(Dist.CLIENT)
	public static float isCastClient(ItemStack stack, ClientLevel level, LivingEntity entity, int seed)
	{
        if (entity == null) {
            return 0.0F;
         } else {
            boolean flag = entity.getMainHandItem() == stack;
            boolean flag1 = entity.getOffhandItem() == stack;
            if (entity.getMainHandItem().getItem() instanceof FishingRodItem) {
               flag1 = false;
            }
            return (flag || flag1) && entity instanceof Player && ((Player)entity).fishing != null ? 1.0F : 0.0F;
         }
	}
	
}
