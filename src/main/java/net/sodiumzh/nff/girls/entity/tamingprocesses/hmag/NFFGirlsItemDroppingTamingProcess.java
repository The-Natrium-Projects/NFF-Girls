package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsDebugStatics;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsItemStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;

public abstract class NFFGirlsItemDroppingTamingProcess extends NFFTamingProcess
{
	
	@Override
	public void initCap(CNFFTamable cap)
	{
		cap.getNbt().put("ongoing_players", new CompoundTag());
	}
	
	@Override
	public TamableInteractionResult handleInteract(TamableInteractArguments args) {
		return new TamableInteractionResult();
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").remove(player.getStringUUID());
		mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		NaUtilsDebugStatics.debugPrintToScreen("Interrupted", player);
		if (!isQuiet)
			NaUtilsEntityStatics.sendAngryParticlesToLivingDefault(mob);
	}

	/**
	 * Player progress is stored at: nbt/ongoing_players/(uuid)
	 */
	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").contains(player.getStringUUID(), NaUtilsNBTStatics.TAG_DOUBLE_ID)
				&& CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").getDouble(player.getStringUUID()) > 0;
	}

	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> reasons = new HashSet<TamableHatredReason>();
		reasons.add(TamableHatredReason.ATTACKED);
		return reasons;
	}
	
	/**
	 * Map of accepted item registry ID to the progress function
	 */
	@SuppressWarnings("unchecked")
	public abstract Map<String, Supplier<Double>> getDeltaProc();

	public final Map<Item, Supplier<Double>> getItemDeltaProc()
	{
		Map<String, Supplier<Double>> procMap = getDeltaProc();
		Map<Item, Supplier<Double>> out = new HashMap<Item, Supplier<Double>>();
		for (String str: procMap.keySet())
		{
			if (NaUtilsItemStatics.getItem(str) != null)
				out.put(NaUtilsItemStatics.getItem(str), procMap.get(str));
		}
		return out;
	}
	
	
	/**
	 * Check if a mob can pick up the item
	 */
	public boolean canPickUpItem(Mob mob, ItemEntity itemEntity)
	{
		// If item type not matching, pass
		if (!getDeltaProc().keySet().contains(NaUtilsItemStatics.getRegistryKeyStr(itemEntity.getItem())))
			return false;
		// If item not thrown by player, pass
		if (itemEntity.getThrower() == null || mob.level.getPlayerByUUID(itemEntity.getThrower()) == null)
			return false;
		// If in hatred, pass
		if (CNFFTamable.getCap(mob).getHatred().contains(itemEntity.getThrower()))
			return false;
		// If the item is still in picking cooldown for the mob, pass
		if (itemEntity.getItem().getOrCreateTagElement("already_picked_befriendable_mobs").contains(mob.getStringUUID(), NaUtilsNBTStatics.TAG_INT_ID)
			&& itemEntity.getItem().getOrCreateTagElement("already_picked_befriendable_mobs").getInt(mob.getStringUUID()) > 0)
			return false;
		// Overlap check is in serverTick()
		/*if (!mob.getBoundingBox().intersects(itemEntity.getBoundingBox()))
			return false;*/
		// Holding another item, pass
		if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty() || CNFFTamable.getCap(mob).hasTimer("hold_item_time"))
			return false;
		// In picking up cooldown, pass
		if (CNFFTamable.getCap(mob).hasTimer("picking_cooldown"))
			return false;
		return true;
	}
		
	/**
	 * Mob trying picking up the item stack on befriending
	 * @return Whether successfully picked up
	 */
	public boolean pickUpItem(Mob mob, ItemEntity itemEntity)
	{
		if (canPickUpItem(mob, itemEntity))
		{
			// Drop the holding item first
			if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty())
			{
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
				mob.setItemInHand(InteractionHand.OFF_HAND, null);
			}
		
			// Pick one and label picked
			ItemStack stack = itemEntity.getItem().copy();
			stack.setCount(1);
			stack.getOrCreateTag().putUUID("befriendable_picked_from_player", itemEntity.getThrower());
			mob.setItemInHand(InteractionHand.OFF_HAND, stack);
			// If only one, remove item entity
			if (itemEntity.getItem().getCount() <= 1)
			{
				itemEntity.discard();
			}
			// Otherwise take one and label taken
			else
			{
				itemEntity.getItem().shrink(1);
				/** Label on the item entity that the mob has picked
				* This is a timer and handled in {@link NFFGirlsItemEventListeners#onServerItemEntityTick}   
				*/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
				itemEntity.getItem().getOrCreateTag().getCompound("already_picked_befriendable_mobs").putInt(mob.getStringUUID(), getItemPickingCooldown());
			}
			// Add timer on mob during which it should hold the item on off-hand
			CNFFTamable.getCap(mob).setTimer("hold_item_time", getHoldingItemTime());
			return true;
		}
		return false;
	}
	
	/** Get how long the mob should hold the item on its off-hand and be unable to pick another one */
	public abstract int getHoldingItemTime();
	
	/** Get how long before an item stack can be picked up again by the same mob */
	public int getItemPickingCooldown()
	{
		return 300 * 20;	// 5 min by default
	}
	
	/** Get how long before the mob can pick up another item after consuming an item */
	public int getMobPickingCooldown()
	{
		return 15 * 20; 
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		if (CNFFTamable.getCap(mob) == null)
			return;
		// Remove off-hand item when timer up, and update proc
		if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty()
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag() != null
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag().contains("befriendable_picked_from_player", NaUtilsNBTStatics.TAG_INT_ARRAY_ID)
				&& !CNFFTamable.getCap(mob).hasTimer("hold_item_time"))
		{
			Player player = mob.level.getPlayerByUUID(mob.getItemInHand(InteractionHand.OFF_HAND).getTag().getUUID("befriendable_picked_from_player"));
			if (player != null && mob.hasLineOfSight(player))
			{
				String strUUID = player.getStringUUID();
				double oldProc = CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").contains(strUUID, NaUtilsNBTStatics.TAG_DOUBLE_ID) ?
						CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").getDouble(strUUID) : 0d;
				double newProc = oldProc + getItemDeltaProc().get(mob.getItemInHand(InteractionHand.OFF_HAND).getItem()).get();
				NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(mob);
				onConsumeItem(mob, mob.getItemInHand(InteractionHand.OFF_HAND), newProc - oldProc);
				if (newProc >= 1.0d)
				{
					mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					NaUtilsEntityStatics.sendHeartParticlesToLivingDefault(mob);
					doTaming(player, mob);
					return;
				}
				else
				{
					int heartCount = ((int) (newProc / 0.2d) - (int) (oldProc / 0.2d));
					NaUtilsEntityStatics.sendParticlesToEntity(mob, ParticleTypes.HEART, mob.getBbHeight() - 0.5, 0.2d, heartCount, 1d);
					CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").putDouble(strUUID, newProc);
					CNFFTamable.getCap(mob).setTimer("picking_cooldown", getMobPickingCooldown());
				}
				NaUtilsDebugStatics.debugPrintToScreen("Proc: " + Double.toString(newProc), player);
			}
			else
			{
				mob.getItemInHand(InteractionHand.OFF_HAND).removeTagKey("befriendable_picked_from_player");
				NaUtilsEntityStatics.sendSmokeParticlesToLivingDefault(mob);
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
			}
			mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
		// Check pick-up
		else if (!CNFFTamable.getCap(mob).hasTimer("hold_item_time"))
		{
			// Overlapping or on neighboring block position
			Predicate<ItemEntity> pickCondition = (ItemEntity ie) -> ie.getBoundingBox().intersects(mob.getBoundingBox()) 
					|| (Math.abs(ie.getBlockX() - mob.getBlockX()) <= 1 && Math.abs(ie.getBlockZ() - mob.getBlockZ()) <= 1 && ie.getBlockY() == mob.getBlockY());
			List<ItemEntity> overlappingItems = 
					mob.level.getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().minmax(NaUtilsEntityStatics.getNeighboringArea(mob, 2.0d)))
						.stream().filter(pickCondition)
						.toList();
			if (overlappingItems.size() > 0)
			{
				ItemEntity toPick = null;
				for (ItemEntity ie: overlappingItems)
				{
					if (canPickUpItem(mob, ie))
					{
						toPick = ie;
						break;
					}
				}
				if (toPick != null)
				{
					onPickUpItem(mob, toPick);
					pickUpItem(mob, toPick);
				}
			}
		}
		CNFFTamable.getCap(mob).setForcePersistent(!CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").isEmpty());
	}
	
	/** Actions executed on mob picking up item. */
	protected void onPickUpItem(Mob mob, ItemEntity item) {}
	
	/** Actions executed on mob consume an item. */
	protected void onConsumeItem(Mob mob, ItemStack item, double deltaProc) {}
	
	public double getProgress(Mob mob, Player player)
	{
		if (CNFFTamable.getCap(mob) != null && CNFFTamable.getCapNbt(mob).contains("ongoing_players", NaUtilsNBTStatics.TAG_COMPOUND_ID)
				&& CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").contains(player.getStringUUID(), NaUtilsNBTStatics.TAG_DOUBLE_ID))
		{
			return CNFFTamable.getCapNbt(mob).getCompound("ongoing_players").getDouble(player.getStringUUID());
		}
		else return 0d;
	}
 }
