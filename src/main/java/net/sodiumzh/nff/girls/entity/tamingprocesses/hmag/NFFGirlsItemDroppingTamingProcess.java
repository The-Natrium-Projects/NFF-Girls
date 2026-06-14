package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamablePickItemGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableWatchHandItemGoal;
import net.sodiumzh.nff.services.entity.taming.IItemTableUsingProcess;
import net.sodiumzh.nff.services.entity.taming.INFFDefaultProgressedTamingProcess;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.entity.component.preset.EntityTimerComponent;
import net.sodiumzh.nfu.entity.taming.ITamingProcess;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.util.NFUItemStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class NFFGirlsItemDroppingTamingProcess extends NFFTamingProcess implements INFFDefaultProgressedTamingProcess<Mob>, IItemTableUsingProcess<NFFTamingProcess>
{
	// Label on the item entity to record which mob(s) have picked an item from this entity,
	// to prevent picking immediately
	// This is a timer and handled in the event listener at the end of this class
	// In entity data cap, Compound (UUID string -> int for remaining time)
	protected static final String ITEM_ENTITY_TIMER_KEY_ALREADY_PICKED_TAMABLE_MOBS = "already_picked_befriendable_mobs";
	// Timer for how long this mob is holding an item.
	protected static final String TIMER_KEY_HOLD_ITEM_TIME = "holdItemTime";
	protected static final String TIMER_KEY_PICKING_COOLDOWN = "pickingCooldown";
	// Label an item if it's on the tamable mob's offhand and is player-thrown. The value is the thrower uuid.
	protected static final String ITEM_NBT_KEY_PICKED_FROM_PLAYER = "itemPickedFromPlayer";
	protected int[] watchAndPickItemGoalPriorities = {2, 3};


	@Nullable
	protected Supplier<MobApplicableItemTable> tamingItemTableOverride = null;
	@Override
	public void tamableInit(NFFTamableComponent c)
	{
		c.getEntity().goalSelector.addGoal(this.watchAndPickItemGoalPriorities()[0], new NFFGirlsTamableWatchHandItemGoal(c.getEntity()));
		c.getEntity().goalSelector.addGoal(this.watchAndPickItemGoalPriorities()[1], new NFFGirlsTamablePickItemGoal(c.getEntity()));
	}

	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand) {
		return TamingInteractionResult.unhandled(player.level);
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		if (this.getOngoingPlayer(mob).map(p -> p.equals(player)).orElse(false)) {
			interruptAll(mob, isQuiet);
		}
	}

	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet){
		if (this.getOngoingPlayerUUID(mob).isPresent()) {
			this.setOngoingPlayer(mob, null);
			this.dropHandItem(mob);
			this.removeCurrentCooldown(mob, false);
			if (!isQuiet)
				NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
			return true;
		}
		return false;
	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return this.getOngoingPlayer(mob).map(p -> Objects.equals(player, p)).orElse(false);
	}

	/**
	 * Map of accepted item registry ID to the progress function
	 */
	public Map<String, Supplier<Double>> getDeltaProgressMap() {
		throw new IllegalStateException("NFFGirlsItemDroppingTamingProcess: missing acceptable item info. " +
				"You must either use MobApplicableItemTable override by calling setItemGivingTableOverride(), " +
				"or override getDeltaProcMap() to define it in code as a map.");
	};

	public final Map<Item, Supplier<Double>> getItemDeltaProgress()
	{
		Map<String, Supplier<Double>> procMap = getDeltaProgressMap();
		Map<Item, Supplier<Double>> out = new HashMap<Item, Supplier<Double>>();
		for (String str: procMap.keySet())
		{
			if (NFUItemStatics.getItem(str) != null)
				out.put(NFUItemStatics.getItem(str), procMap.get(str));
		}
		return out;
	}
	
	@Nullable
	private static Player getThrowingPlayer(ItemEntity ie) {
		return Optional.ofNullable(ie.getThrower()).flatMap(uuid ->
			NFUEntityStatics.findPlayerInAllDimensions(uuid, ie.getLevel())).orElse(null);
	}

	/**
	 * Check if a mob can pick up the item
	 */
	public boolean canPickUpItem(Mob mob, ItemEntity itemEntity)
	{
		// If item type not matching, pass
		if (!isItemAcceptableInternal(itemEntity.getItem(), mob))
			return false;
		// If item not thrown by player, pass
		Player playerThrown = getThrowingPlayer(itemEntity);
		if (playerThrown == null)
			return false;
		// If other player ongoing, pass
		if (this.getOngoingPlayer(mob).map(p -> !p.equals(playerThrown)).orElse(false))
			return false;
		// If angry, pass
		if (this.getTamable(mob).getAngerHandler().isAngryAt(playerThrown))
			return false;
		// If the item is still in picking cooldown for the mob, pass
		if (this.getItemEntityPickingCooldown(itemEntity, mob) > 0)
			return false;
		// If in other player's process, pass
		if (this.getOngoingPlayer(mob).map(player -> !Objects.equals(player, playerThrown)).orElse(false))
			return false;
		// Overlap check is in serverTick()
		/*if (!mob.getBoundingBox().intersects(itemEntity.getBoundingBox()))
			return false;*/
		// Holding another item, pass
        if (this.getTamable(mob).getTimerComponent().hasGeneralTimer(TIMER_KEY_HOLD_ITEM_TIME))
            return false;
		// In picking up cooldown, pass
        if (this.getTamable(mob).getTimerComponent().hasGeneralTimer(TIMER_KEY_PICKING_COOLDOWN))
            return false;
		return true;
	}

	private void dropHandItem(Mob mob) {
		if (mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) return;
		// This tag should only exist on the mob off-hand item, so remove before dropping
		if (mob.getItemInHand(InteractionHand.OFF_HAND).hasTag()
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag().hasUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER)) {
			mob.getItemInHand(InteractionHand.OFF_HAND).removeTagKey(ITEM_NBT_KEY_PICKED_FROM_PLAYER);
		}
		mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
		mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		mob.getLookControl().setLookAt(new Vec3(0d, 0d, 0d));
	}

	/**
	 * Mob trying picking up the item stack on befriending
	 * @return Whether successfully picked up
	 */
	public boolean pickUpItem(Mob mob, ItemEntity itemEntity)
	{
		if (!canPickUpItem(mob, itemEntity)) return false;
		dropHandItem(mob);

		// Pick one
		ItemStack stack = itemEntity.getItem().copy();
		stack.setCount(1);
		stack.getOrCreateTag().putUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER, getThrowingPlayer(itemEntity).getUUID());
		mob.setItemInHand(InteractionHand.OFF_HAND, stack);
		if (itemEntity.getItem().getCount() <= 1)
		{
			itemEntity.discard();
		}
		// Otherwise take one and label taken
		else
		{
			itemEntity.getItem().shrink(1);
			// Label on the item entity that the mob has picked an item from this entity, to prevent picking immediately
			// This is a timer and handled in the event listener at the end of this class
            this.setItemEntityPickingCooldown(itemEntity, mob, getItemPickingCooldown());
		}
		// Add timer on mob during which it should hold the item on off-hand
        this.getTamable(mob).getTimerComponent().addTimer(TIMER_KEY_HOLD_ITEM_TIME, getHoldingItemTime(), true);
		return true;
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

	private void finalizeHoldingItem(Mob mob) {
		if (!mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty()
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag() != null
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag().hasUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER))
		{
			Player player = mob.level.getPlayerByUUID(mob.getItemInHand(InteractionHand.OFF_HAND)
					.getTag().getUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER));
			if (player != null && mob.hasLineOfSight(player))
			{
				NFFTamableComponent tamable = NFFTamableComponent.get(mob);
				double oldProgress = this.getProgressValue(mob).orElse(0d);
				double newProgress = oldProgress + getProgressGainInternal(mob.getItemInHand(InteractionHand.OFF_HAND), mob);
				NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
				onConsumeItem(mob, mob.getItemInHand(InteractionHand.OFF_HAND), newProgress - oldProgress);
				if (newProgress >= 1.0d)
				{
					mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
					doTaming(player, mob);
					return;
				}
				else
				{
					int heartCount = ((int) (newProgress / 0.2d) - (int) (oldProgress / 0.2d));
					NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.HEART, mob.getBbHeight() - 0.5, 0.2d, heartCount, 1d);
					this.setProgressValue(mob, player.getUUID(), newProgress);
                    this.getTamable(mob).getTimerComponent().addTimer(TIMER_KEY_PICKING_COOLDOWN, getMobPickingCooldown(), true);
				}
				this.debugPrint(player, "Progress: " + newProgress);
			}
			else
			{
				mob.getItemInHand(InteractionHand.OFF_HAND).removeTagKey(ITEM_NBT_KEY_PICKED_FROM_PLAYER);
				NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob);
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
			}
			mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
	}

	@Override
	public void onGeneralTimerExpire(Mob mob, String key) {
		super.onGeneralTimerExpire(mob, key);
		if (key.equals(TIMER_KEY_HOLD_ITEM_TIME))
			this.finalizeHoldingItem(mob);
	}

	@Override
	public void serverTick(Mob mob)
	{
        NFFTamableComponent tamable = this.getTamable(mob);
		// Check pick-up
		if (!tamable.getTimerComponent().hasGeneralTimer(TIMER_KEY_HOLD_ITEM_TIME))
		{
			// Overlapping or on neighboring block position
			Predicate<ItemEntity> pickCondition = (ItemEntity ie) -> ie.getBoundingBox().intersects(mob.getBoundingBox()) 
					|| (Math.abs(ie.getBlockX() - mob.getBlockX()) <= 1 && Math.abs(ie.getBlockZ() - mob.getBlockZ()) <= 1 && ie.getBlockY() == mob.getBlockY());
			List<ItemEntity> overlappingItems =
					mob.level.getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().minmax(mob.getBoundingBox().inflate(2.0d)))
						.stream().filter(pickCondition)
						.toList();
			if (!overlappingItems.isEmpty())
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
		tamable.setForcePersistent(this.isInAnyProcess(mob));
	}
	
	/** Actions executed on mob picking up item. */
	protected void onPickUpItem(Mob mob, ItemEntity item) {}
	
	/** Actions executed on mob consume an item. */
	protected void onConsumeItem(Mob mob, ItemStack item, double deltaProc) {}

	@Nullable
	public final Supplier<MobApplicableItemTable> getItemGivingTableOverride() { return tamingItemTableOverride; }

	public final NFFGirlsItemDroppingTamingProcess setItemGivingTableOverride(Supplier<MobApplicableItemTable> override)
	{
		this.tamingItemTableOverride = override;
		return this;
	}

	private boolean isItemAcceptableInternal(ItemStack item, Mob mob)
	{
		var table = this.getItemGivingTableOverride();
		if (table != null)
			return table.get().getOutcome(mob, item).isPresent();
		else return this.getItemDeltaProgress().containsKey(item.getItem());
	}

	private double getProgressGainInternal(ItemStack item, Mob mob) {

		var table = this.getItemGivingTableOverride();
		if (table != null)
		{
			var output = table.get().getOutcome(mob, item);
			return output.map(MobApplicableItemTable.Outcome::amount).orElse(0d);
		}
		else {
			var map = this.getItemDeltaProgress();
			return map.containsKey(item.getItem()) ? map.get(item.getItem()).get() : 0d;
		}
	}

	/**
	 * Get the mob's picking cooldown of an item entity i.e. how long will it take to allow the
	 * mob to pick the item again.
	 */
	private int getItemEntityPickingCooldown(ItemEntity itemEntity, Mob mob) {
		return EntityComponentAPI.getDefaultTimer(itemEntity)
                .getUUIDSpecificTimer(mob.getUUID(), ITEM_ENTITY_TIMER_KEY_ALREADY_PICKED_TAMABLE_MOBS)
                .map(EntityTimerComponent.Timer::getTicksRemaining).orElse(0);
	}

	/**
	 * Get the mob's picking cooldown of an item entity i.e. how long will it take to allow the
	 * mob to pick the item again.
	 */
	private void setItemEntityPickingCooldown(ItemEntity itemEntity, Mob mob, int ticks) {
        EntityComponentAPI.getDefaultTimer(itemEntity)
                .addUUIDSpecificTimer(mob.getUUID(), ITEM_ENTITY_TIMER_KEY_ALREADY_PICKED_TAMABLE_MOBS, ticks, 1, true);
	}

	/**
	 * Return an {@code int[2]} of {@code {watchItemGoalPriority, pickItemGoalPriority}}.
	 */
	@Nonnull
	public final int[] watchAndPickItemGoalPriorities() { return watchAndPickItemGoalPriorities; }

	@Override
	public ITamingProcess<Mob> asProcess() {return this;}

	public NFFGirlsItemDroppingTamingProcess setWatchAndPickItemGoalPriorities(int watch, int pick) {
		this.watchAndPickItemGoalPriorities = new int[] {watch, pick};
		return this;
	}

 }
