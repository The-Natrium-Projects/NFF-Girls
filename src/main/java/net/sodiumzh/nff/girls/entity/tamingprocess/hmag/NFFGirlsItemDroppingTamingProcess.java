package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumzh.nautils.capability.EntityTimerAccessor;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nautils.entity.taming.ITamingProcess;
import net.sodiumzh.nautils.entity.taming.TamingInteractionResult;
import net.sodiumzh.nautils.mixin.events.entity.EntityTickEvent;
import net.sodiumzh.nautils.registries.NaUtilsCaps;
import net.sodiumzh.nautils.statics.NaUtilsItemStatics;
import net.sodiumzh.nautils.statics.NaUtilsParticleStatics;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamablePickItemGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableWatchHandItemGoal;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.INFFDefaultProgressedTamingProcess;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class NFFGirlsItemDroppingTamingProcess extends NFFTamingProcess implements INFFDefaultProgressedTamingProcess<Mob>
{
	// Label on the item entity to record which mob(s) have picked an item from this entity,
	// to prevent picking immediately
	// This is a timer and handled in the event listener at the end of this class
	// In entity data cap, Compound (UUID string -> int for remaining time)
	protected static final String ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS = "already_picked_befriendable_mobs";
	// Timer for how long this mob is holding an item.
	protected static final EntityTimerAccessor TIMER_KEY_HOLD_ITEM_TIME
			= CNFFTamable.getTimerAccessor("hold_item_time");
	protected static final EntityTimerAccessor TIMER_KEY_PICKING_COOLDOWN
			= CNFFTamable.getTimerAccessor("picking_cooldown");
	// Label an item if it's on the tamable mob's offhand and is player-thrown. The value is the thrower uuid.
	protected static final String ITEM_NBT_KEY_PICKED_FROM_PLAYER = "befriendable_picked_from_player";
	protected int[] watchAndPickItemGoalPriorities = {2, 3};


	@Nullable
	protected Supplier<MobApplicableItemTable> tamingItemTableOverride = null;
	@Override
	public void tamableInit(CNFFTamable cap)
	{
		cap.getEntity().goalSelector.addGoal(this.watchAndPickItemGoalPriorities()[0], new NFFGirlsTamableWatchHandItemGoal(cap.getEntity()));
		cap.getEntity().goalSelector.addGoal(this.watchAndPickItemGoalPriorities()[1], new NFFGirlsTamablePickItemGoal(cap.getEntity()));
	}

	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand) {
		return TamingInteractionResult.unhandled(player.level());
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
				NaUtilsParticleStatics.sendAngryParticlesToEntityDefault(mob);
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
		if (!isItemAcceptableInternal(itemEntity.getItem(), mob))
			return false;
		// If item not thrown by player, pass
		Entity entityThrown = itemEntity.getOwner();
		if (entityThrown == null)
			return false;
		if (!(entityThrown instanceof Player playerThrown))
			return false;
		// If other player ongoing, pass
		if (this.getOngoingPlayer(mob).map(p -> !p.equals(playerThrown)).orElse(false))
			return false;
		// If angry, pass
		if (this.getTamable(mob).isAngryAt(playerThrown))
			return false;
		// If the item is still in picking cooldown for the mob, pass
		if (itemEntity.getCapability(NaUtilsCaps.CAP_ENTITY_DATA)
				.map(c -> c.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS).getInt(mob.getStringUUID()))
				.orElse(0) > 0)
			return false;
		// If in other player's process, pass
		if (this.getOngoingPlayer(mob).map(player -> !Objects.equals(player, playerThrown)).orElse(false))
			return false;
		// Overlap check is in serverTick()
		/*if (!mob.getBoundingBox().intersects(itemEntity.getBoundingBox()))
			return false;*/
		// Holding another item, pass
		if (TIMER_KEY_HOLD_ITEM_TIME.hasTimer(mob))
			return false;
		// In picking up cooldown, pass
		if (TIMER_KEY_PICKING_COOLDOWN.hasTimer(mob))
			return false;
		return true;
	}

	private void dropHandItem(Mob mob) {
		if (mob.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) return;
		// This tag should only exist on the mob off-hand item, so remove before dropping
		if (mob.getItemInHand(InteractionHand.OFF_HAND).hasTag()
				&& mob.getItemInHand(InteractionHand.OFF_HAND).getTag().hasUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER)) {
			mob.getItemInHand(InteractionHand.OFF_HAND).getTag().remove(ITEM_NBT_KEY_PICKED_FROM_PLAYER);
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
		stack.getOrCreateTag().putUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER, itemEntity.getOwner().getUUID());
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
			itemEntity.getCapability(NaUtilsCaps.CAP_ENTITY_DATA).ifPresent(c -> {
				this.setItemEntityPickingCooldown(itemEntity, mob, getItemPickingCooldown());
			});
		}
		// Add timer on mob during which it should hold the item on off-hand
		TIMER_KEY_HOLD_ITEM_TIME.setTimer(mob, getHoldingItemTime());
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
			Player player = mob.level().getPlayerByUUID(mob.getItemInHand(InteractionHand.OFF_HAND)
					.getTag().getUUID(ITEM_NBT_KEY_PICKED_FROM_PLAYER));
			if (player != null && mob.hasLineOfSight(player))
			{
				CNFFTamable tamable = CNFFTamable.get(mob);
				double oldProgress = this.getProgressValue(mob).orElse(0d);
				double newProgress = oldProgress + getProgressGainInternal(mob.getItemInHand(InteractionHand.OFF_HAND), mob);
				NaUtilsParticleStatics.sendGlintParticlesToEntityDefault(mob);
				onConsumeItem(mob, mob.getItemInHand(InteractionHand.OFF_HAND), newProgress - oldProgress);
				if (newProgress >= 1.0d)
				{
					mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					NaUtilsParticleStatics.sendHeartParticlesToEntityDefault(mob);
					doTaming(player, mob);
					return;
				}
				else
				{
					int heartCount = ((int) (newProgress / 0.2d) - (int) (oldProgress / 0.2d));
					NaUtilsParticleStatics.sendParticlesToEntity(mob, ParticleTypes.HEART, mob.getBbHeight() - 0.5, 0.2d, heartCount, 1d);
					this.setProgressValue(mob, player.getUUID(), newProgress);
					TIMER_KEY_PICKING_COOLDOWN.setTimer(mob, getMobPickingCooldown());
				}
				this.debugPrint(player, "Progress: " + newProgress);
			}
			else
			{
				mob.getItemInHand(InteractionHand.OFF_HAND).removeTagKey(ITEM_NBT_KEY_PICKED_FROM_PLAYER);
				NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(mob);
				mob.spawnAtLocation(mob.getItemInHand(InteractionHand.OFF_HAND));
			}
			mob.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
	}

	@Override
	public void onGeneralTimerExpire(Mob mob, String key) {
		super.onGeneralTimerExpire(mob, key);
		if (key.equals(TIMER_KEY_HOLD_ITEM_TIME.getKey()))
			this.finalizeHoldingItem(mob);
	}

	@Override
	public void serverTick(Mob mob)
	{
		LazyOptional<CNFFTamable> tamableOptional = getOptional(mob);
		if (!tamableOptional.isPresent())
			return;
		CNFFTamable tamable = tamableOptional.orElseThrow(RuntimeException::new);
		// Check pick-up
		if (!TIMER_KEY_HOLD_ITEM_TIME.hasTimer(mob))
		{
			// Overlapping or on neighboring block position
			Predicate<ItemEntity> pickCondition = (ItemEntity ie) -> ie.getBoundingBox().intersects(mob.getBoundingBox()) 
					|| (Math.abs(ie.getBlockX() - mob.getBlockX()) <= 1 && Math.abs(ie.getBlockZ() - mob.getBlockZ()) <= 1 && ie.getBlockY() == mob.getBlockY());
			List<ItemEntity> overlappingItems =
					mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().minmax(mob.getBoundingBox().inflate(2.0d)))
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
			return table.get().getOutput(mob, item) != null;
		else return this.getItemDeltaProgress().containsKey(item.getItem());
	}

	private double getProgressGainInternal(ItemStack item, Mob mob) {

		var table = this.getItemGivingTableOverride();
		if (table != null)
		{
			var output = table.get().getOutput(mob, item);
			return output != null ? output.amount() : 0d;
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
		return itemEntity.getCapability(NaUtilsCaps.CAP_ENTITY_DATA)
				.map(c -> c.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS).getInt(mob.getStringUUID()))
				.orElse(0);
	}

	/**
	 * Get the mob's picking cooldown of an item entity i.e. how long will it take to allow the
	 * mob to pick the item again.
	 */
	private void setItemEntityPickingCooldown(ItemEntity itemEntity, Mob mob, int ticks) {
		var optC = itemEntity.getCapability(NaUtilsCaps.CAP_ENTITY_DATA);
		if (!optC.isPresent()) return;
		var c = optC.orElseThrow(RuntimeException::new);
		if (!c.getNBT().contains(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS, Tag.TAG_COMPOUND))
			c.getNBT().put(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS, new CompoundTag());
		if (ticks <= 0) c.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS).remove(mob.getStringUUID());
		else {
			c.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS).putInt(mob.getStringUUID(), ticks);
			if (c.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS).isEmpty())
				c.getNBT().remove(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS);
		}
	}

	private void tickItemEntityPickingCooldown(ItemEntity itemEntity) {

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

	@SubscribeEvent
	public static void tickItemEntityPickingCooldown(EntityTickEvent event)
	{
		if (event.getEntity() instanceof ItemEntity ie && !ie.level().isClientSide)
		{
			ie.getCapability(NaUtilsCaps.CAP_ENTITY_DATA).ifPresent(dataCap -> {
				CompoundTag allTimers = dataCap.getNBT().getCompound(ENTITY_DATA_KEY_ALREADY_PICKED_TAMABLE_MOBS);
				Set<String> removal = new HashSet<>();
				for (String key: allTimers.getAllKeys()) {
					int oldVal = allTimers.getInt(key);
					allTimers.putInt(key, oldVal - 1);
					if (oldVal - 1 <= 0) removal.add(key);
				}
				for (String key: removal) {
					allTimers.remove(key);
				}
			});
		}
	}
 }
