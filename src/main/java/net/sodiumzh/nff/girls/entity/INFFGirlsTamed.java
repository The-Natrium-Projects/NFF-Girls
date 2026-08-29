package net.sodiumzh.nff.girls.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAttackingStrategy;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEntityEventListeners;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.network.ClientboundNFFGirlsMobGeneralSyncPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.*;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.annotation.DontOverride;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.object.FilteredMapper;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public interface INFFGirlsTamed extends INFFTamed
{

	public static Optional<INFFGirlsTamed> get(Entity o) {
		return INFFTamed.get(o).filter(t -> t instanceof INFFGirlsTamed).map(t -> (INFFGirlsTamed)t);
	}

	@Override
	public default NFFGirlsDataAccessor getDataAccessor() {
		return new NFFGirlsDataAccessor(this);
	}

	public default double getFavorability()
	{
		return this.getDataAccessor().getFavorability();
	}
	
	/** Get the proportion of fav/maxfav, ranged 0-1 */
	@DontOverride
	public default double getNormalizedFavorability()
	{
		NFFGirlsDataAccessor ac = this.getDataAccessor();
		return Mth.clamp(ac.getFavorability() / ac.getMaxFavorability(), 0d, 1d);
	}

	@DontOverride
	public default float getXpLevel()
	{
		return this.getDataAccessor().getExpectedXPLevel();
	}
	
	@DontOverride
	public default float getOverallXp()
	{
		return this.getDataAccessor().getXP();
	}
	
	@DontOverride
	public default float getXpInThisLevel()
	{
		return this.getDataAccessor().getXPInThisLevel();
	}
	
	@DontOverride
	@DontCallManually
	public default void touchEntity(Entity other)
	{
		MinecraftForge.EVENT_BUS.post(new OverlapEntityEvent(this, other));
	}
	
	@Override
	public default double getAnchoredStrollRadius()  
	{
		return 16.0d;
	}
	
	/**
	 * Fired EVERY TICK when a befriended mob overlaps an entity.
	 */
	public static class OverlapEntityEvent extends Event
	{
		public final INFFTamed thisMob;
		public final Entity touchedEntity;
		public OverlapEntityEvent(INFFTamed thisMob, Entity touchedEntity)
		{
			this.thisMob = thisMob;
			this.touchedEntity = touchedEntity;
		}
	}

	// === Interactions

	/**
	 * If true, it will bypass common interactions defined in {@link NFFGirlsEntityEventListeners#onMobInteract}.
	 */
	public default boolean shouldBypassCommonInteractions() { return false; }

	public default InteractionResult serversideMainHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}

	/**
	 * Common interactions that can be applied to any NFF Girls mobs. Invoked AFTER if the mob's unique interactions
	 * don't consume, and before {@link Mob#mobInteract}.
	 */
	@DontOverride
	public default InteractionResult commonInteractions(Player player, InteractionHand hand, LogicalSide side) {
		if (!Objects.equals(this.getOwnerUUID(), player.getUUID())) return InteractionResult.PASS;

		if (hand == InteractionHand.MAIN_HAND) {
			// Handle AI state and GUI opening
			if (this.isCommandingItem(player.getItemInHand(InteractionHand.MAIN_HAND)) ||
				this.isCommandingItem(player.getItemInHand(InteractionHand.OFF_HAND))) {

				if (player.isShiftKeyDown()) {
					if (!side.isClient())
						NFFTamedStatics.openBefriendedInventory(player, this);
				} else {
					if (side.isServer()) {
						this.switchAIState();
						if (this.getAIState().equals(NFFTamedMobAIState.WAIT))
							this.asMob().setTarget(null);
					}
				}
				return InteractionResult.SUCCESS;
			}
			// Handle healing
			if (this.tryApplyHealingItems(player.getItemInHand(hand), player) != InteractionResult.PASS) {
				return InteractionResult.SUCCESS;
			}
			// Handle trade
			if ((player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || player.getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.EVIL_GEM.get()))
				&& this.getDataAccessor().getAttackTarget() == null
				&& !player.isShiftKeyDown()) {
				AtomicReference<InteractionResult> res = new AtomicReference<>(InteractionResult.PASS);
				this.asMob().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
					if (cap.isValidTrader()) {
						cap.openTradingScreen(player, NFUInfoStatics.createTranslatable("info.nffgirls.open_trade"), 1);
						res.set(InteractionResult.sidedSuccess(side.isClient()));
					}
				});
				if (res.get().consumesAction()) return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}
	public default InteractionResult clientsideMainHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}

	public default boolean isCommandingItem(ItemStack test)
	{
		return test.is(NFFGirlsItems.COMMANDING_WAND.get());
	}

	// === INFFTamed interface
	
	@Override
	public default NFFMobRespawnerItem getRespawnerType()
	{
		return NFFGirlsItems.MOB_RESPAWNER.get();
	}
	
	@Override
	public default DeathRespawnerGenerationType getDeathRespawnerGenerationType()
	{
		return DeathRespawnerGenerationType.GIVE;
	}
	
	@Deprecated
	@Override
	public default boolean dropInventoryOnDeath()
	{
		//return !this.asMob().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
		return true;
	}
	
	// === AttributeMonitor related
	
	/*@Override
	public default void onAttributeChange(EntityAttributeMonitorComponent component, Attribute attribute, double oldVal, double newVal)
	{
		if (attribute == Attributes.MAX_HEALTH)
		{
			this.asMob().setHealth((float) (this.asMob().getHealth() * newVal / oldVal));
		}
	}

	@Override
	void setupAttributeMonitor(EntityAttributeMonitorComponent entityAttributeMonitorComponent);

	// === IItemStackMonitor interface

	private static UUID getSharpnessModifierUUID()
	{
		return UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	}

	@Override
	default void setupItemStackMonitor(EntityItemStackMonitorComponent entityItemStackMonitorComponent) {};

	@Override
	default void onItemStackChange(EntityItemStackMonitorComponent entityItemStackMonitorComponent, String key, ItemStack oldStack, ItemStack newStack) {
		if (key.equals("main_hand"))
		{
			this.asMob().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(getSharpnessModifierUUID());
			@SuppressWarnings("deprecation")
			int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, newStack);
			if (lv > 0)
			{
				this.asMob().getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
					getSharpnessModifierUUID(), "sharpness_modifier", 0.5d + 0.5d * (double) lv, AttributeModifier.Operation.ADDITION));
			}
		}
	}*/

	// Healing

	@Override
	public default MobApplicableItemTable getHealingItems() {
		return NFFGirlsHealingItemMappings.get(this.asMob().getType()).orElse(MobApplicableItemTable.EMPTY);
	}




	// == Trade interface ==
	
	/**
	 * Ticks after sending a trade restock event.
	 * Note: a restock event doesn't necessarily restock all offers. The probability depends on the required level.
	 */
	public default int getRestockTicks()
	{
		return 600 * 20;
	}
	
	/**
	 * Get amount of trade entries for each level. Result[i] = level i+1.
	 */
	public default int[] getTradeEntryCountEachLevel()
	{
		return CNFFGirlsTradeHandler.OFFER_COUNT_FOR_LEVEL;
	}
	
	public default int pointsPerIntroductionLetter()
	{
		return 128;
	}

	public default int[] getXpLevelRequirementsEachMerchantLevel() {
		return CNFFGirlsTradeHandler.LEVEL_REQUIREMENTS;
	}
	
	// ===================== NFFGirls gamerules related ===================
	
	/**
	 * True if this mob should actively attack mobs hostile to itself.
	 */
	public default boolean shouldAttackMobsHostileToSelf()
	{
		return NFUBaubleAPI.getAllSlotItems(this.asMob()).values().stream()
			.map(i -> INFFGirlsBauble.asBauble(i.getItem())).reduce(new ArrayList<>(), (a, b) -> { a.addAll(b); return a;})
			.stream().anyMatch(b -> b.hasBaubleTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK));
	}
	
	/**
	 * True if this mob should actively attack mobs hostile to its owner.
	 */
	public default boolean shouldAttackMobsHostileToOwner()
	{
		return NFUBaubleAPI.getAllSlotItems(this.asMob()).values().stream()
			.map(i -> INFFGirlsBauble.asBauble(i.getItem())).reduce(new ArrayList<>(), (a, b) -> { a.addAll(b); return a;})
			.stream().anyMatch(b -> b.hasBaubleTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK));
	}

	/**
	 * Get mob types that becomes neutral when it sees player with a friended mob of this type.
	 * <p>The wild version of this mob is always added, and no need to add here.
	 */
	public default Set<EntityType<?>> getNeutralizingTypes() {
		return Set.of();
	}

	// ===== Network =========== //
	
	public default void doSync()
	{
		ClientboundNFFGirlsMobGeneralSyncPacket packet = new ClientboundNFFGirlsMobGeneralSyncPacket(this);
		if (this.isOwnerInDimension() && this.getOwner() instanceof ServerPlayer toPlayer)
			NFFGirlsChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
	}

	// === Util === //
	
	/**
	 * @deprecated Only for old bauble system.
	 */
	@Deprecated
	public default HashMap<String, ItemStack> continuousBaubleSlots(int startIndex, int endIndexExclude)
	{
		HashMap<String, ItemStack> map = new HashMap<>();
		int j = 0;
		for (int i = startIndex; i < endIndexExclude; ++i)
		{
			map.put(Integer.toString(j), this.getAdditionalInventory().orElseThrow().getItem(i));
			j++;
		}
		return map;
	}

	// ATTACKING STRATEGY RELATED //

	public default NFFGirlsAttackingStrategy getAttackingStrategy() {
		return this.getDataAccessor().getDataComponent().getVariable("attackingStrategy", NFFGirlsAttackingStrategy.class)
			.orElseGet(NFFGirlsAttackingStrategy::new);
	}

	public default void setAttackingStrategy(NFFGirlsAttackingStrategy strategy) {
		this.getDataAccessor().getDataComponent().putPermanentVariable("attackingStrategy", strategy, NFFGirlsDataSerializers.ATTACKING_STRATEGY.get());
	}

	/**
	 * The mob will actively attack the listed mobs ignoring the attacking strategy.
	 */
	public default EntityType<?>[] activelyAttacksIgnoringStrategy() {
		return new EntityType[]{};
	}

	/**
	 * The mob will not attack the listed mobs ignoring the attacking strategy.
	 */
	public default EntityType<?>[] notAttacksIgnoringStrategy() {
		return new EntityType[]{this.asMob().getType()};
	}

	// CLIENT ONLY //

	public default boolean shouldSitOnWaiting() {
		return true;
	}

	public default float sitPositionOffset() {
		return this.asMob().isBaby() ? -0.3f : -0.6f;
	}


	// Sun sensitive related

	@Override
	public default void setupSunImmunityRules()
	{
		this.getSunImmunity().putOptional("sunhat", mob -> mob.asMob().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("bauble", mob -> INFFGirlsBauble.isEnvironmentImmunized(mob.asMob()));
	}

	@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class EventListeners {


	}

}
