package net.sodiumzh.nff.girls.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEntityEventListeners;
import net.sodiumzh.nff.girls.network.ClientboundNFFGirlsMobGeneralSyncPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.subsystem.bauble.NFFGirlsBaubleStatics;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.item.capability.wrapper.IItemStackMonitor;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.annotation.DontOverride;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.object.FilteredMapper;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface INFFGirlsTamed extends INFFTamed, IAttributeMonitor, IItemStackMonitor
{

	public static FilteredMapper<Object, INFFGirlsTamed> GETTER = FilteredMapper.unconditionalNoVararg(Object.class, INFFGirlsTamed.class, o -> {
			if (o == null) return null;
			if (o instanceof INFFGirlsTamed bm)
				return bm;
			else return null;
		});

	public static Optional<INFFGirlsTamed> get(Object o) {
		return GETTER.apply(o);
	}

	/**
	 * Check if a mob has a NFFGirls BM interface.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check.
	 */
	@Deprecated
	public static boolean isBM(Object o)
	{
		return get(o).isPresent();
	}
	
	/**
	 * Cast a mob to the NFFGirls BM interface. Null if failed.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this to cast a mob to BM.
	 */
	@Deprecated
	@Nullable
	public static INFFGirlsTamed getBM(Object o)
	{
		return get(o).orElse(null);
	}
	
	/**
	 * Do an action if a mob has a NFFGirls BM interface.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * you can use this to safely cast and do things to BM.
	 * @return Whether the action is invoked.
	 */
	@Deprecated
	public static boolean ifBM(Object o, Consumer<INFFGirlsTamed> action)
	{
		return get(o).map(t -> {
			action.accept(t);
			return true;
		}).orElse(false);
	}

	/**
	 * Check if a mob has a NFFGirls BM interface and satisfied the given condition.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check and followed checks of the cast BM.
	 */
	@Deprecated
	public static boolean isBMAnd(Object o, Predicate<INFFGirlsTamed> cond)
	{
		return get(o).filter(cond).isPresent();
	}

	@Deprecated
	public static void ifBMAnd(Object o, Predicate<INFFGirlsTamed> cond, Consumer<INFFGirlsTamed> operation) {
		get(o).filter(cond).ifPresent(operation);
	}

	@Deprecated
	public static boolean isBMAndOwnedBy(Object o, UUID ownerUUID) {
		return isBMAnd(o, bm -> Objects.equals(ownerUUID, bm.getOwnerUUID()));
	}

	@DontOverride
	public default CNFFGirlsFavorabilityHandler getFavorabilityHandler()
	{
		MutableObject<CNFFGirlsFavorabilityHandler> cap = new MutableObject<CNFFGirlsFavorabilityHandler>(null);
		asMob().getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((c) ->
		{
			cap.setValue(c);
		});
		if (cap.getValue() == null)
		{
			LogUtils.getLogger().error("Missing CNFFGirlsFavorabilityHandler capability");
			return new CNFFGirlsFavorabilityHandler.Impl(this.asMob());
		}
		return cap.getValue();
	}
	
	@DontOverride
	public default CNFFGirlsLevelHandler getLevelHandler()
	{
		MutableObject<CNFFGirlsLevelHandler> cap = new MutableObject<CNFFGirlsLevelHandler>(null);
		asMob().getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).ifPresent((c) -> 
		{
			cap.setValue(c);
		});
		if (cap.getValue() == null)
		{
			LogUtils.getLogger().error("Missing CNFFGirlsLevelHandler capability");
			return new CNFFGirlsLevelHandler.Impl(this.asMob());
		}
		return cap.getValue();
	}

	@DontOverride
	public default float getFavorability()
	{
		return this.getFavorabilityHandler().getFavorability();
	}
	
	/** Get the proportion of fav/maxfav, ranged 0-1 */
	@DontOverride
	public default float getNormalizedFavorability()
	{
		var cap = this.getFavorabilityHandler();
		return cap.getFavorability() / cap.getMaxFavorability();
	}
	
	@DontOverride
	public default int getXpLevel()
	{
		return this.getLevelHandler().getExpectedLevel();
	}
	
	@DontOverride
	public default long getOverallXp()
	{
		return this.getLevelHandler().getExp();
	}
	
	@DontOverride
	public default long getXpInThisLevel()
	{
		return this.getLevelHandler().getExpInThisLevel();
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

	public default InteractionResult ownerInteraction(Player player, InteractionHand hand, LogicalSide side)
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
					if (side.isClient())
						NFFTamedStatics.openBefriendedInventory(player, this);
				} else {
					if (side.isServer())
						this.switchAIState();
				}
				return InteractionResult.sidedSuccess(side.isClient());
			}
			// Handle healing
			if (this.tryApplyHealingItems(player.getItemInHand(hand), player) != InteractionResult.PASS) {
				return InteractionResult.sidedSuccess(side.isClient());
			}
			// Handle trade
			if ((player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || player.getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.EVIL_GEM.get()))
				&& this.getData().getAttackTarget() == null
				&& !player.isShiftKeyDown()) {
				AtomicReference<InteractionResult> res = new AtomicReference<>(InteractionResult.PASS);
				this.asMob().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
					if (cap.isValidTrader()) {
						cap.openTradingScreen(player, NFUInfoStatics.createTranslatable("info.nffgirls.open_trade"), 1);
						res.set(InteractionResult.sidedSuccess(side.isClient()));
					}
				});
				if (res.get().consumesAction()) return InteractionResult.sidedSuccess(side.isClient());
			}
		}

		return InteractionResult.PASS;

		/*if (player.level().isClientSide()) {
			if (hand == InteractionHand.MAIN_HAND) {
				if (NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get())) {
					NFFTamedStatics.openBefriendedInventory(player, target);
					return InteractionResult.sidedSuccess(player.level().isClientSide);
				}
			}
		} else {
			if (target.tryApplyHealingItems(player.getItemInHand(hand), player) != InteractionResult.PASS) {
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			} else if (target.isCommandingItem(player.getItemInHand(hand))) {
				target.switchAIState();
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}
		}
		return InteractionResult.PASS;*/
	}

	/**
	 * Reserved interactions that will skip each mob's unique interactions (in {@code ownerInteraction})
	 */
	@DontOverride
	public default boolean isReservedInteraction( Player player, InteractionHand hand, LogicalSide side) {
		// For trade
		if ((player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
			|| player.getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.EVIL_GEM.get()))
			&& !player.isShiftKeyDown())
			return true;
		// For ai switching and GUI
		if ((this.isCommandingItem(player.getItemInHand(InteractionHand.MAIN_HAND))
			|| this.isCommandingItem(player.getItemInHand(InteractionHand.OFF_HAND))))
			return true;
		return false;
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
		return !this.asMob().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
	}
	
	// === IAttributeMonitor interface
	
	@Override
	public default void onAttributeChange(Attribute attr, double oldVal, double newVal) 
	{
		if (attr == Attributes.MAX_HEALTH)
		{
			this.asMob().setHealth((float) (this.asMob().getHealth() * newVal / oldVal));
		}
	}

	@Override
	public MobApplicableItemTable getHealingItems();

	// === IItemStackMonitor interface
	
	private static UUID getSharpnessModifierUUID()
	{
		return UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	}
	
	@Override
	public default void onItemStackChange(String key, ItemStack oldStack, ItemStack newStack) {
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
	
	
	// ===================== NFFGirls gamerules related ===================
	
	/**
	 * True if this mob should proactively attack mobs hostile to itself.
	 */
	public default boolean shouldAttackMobsHostileToSelf()
	{
		return NFFGirlsBaubleStatics.countBaubles(this.asMob(), new ResourceLocation(NFFGirls.MOD_ID, "courage_amulet")) > 0;
	}
	
	/**
	 * True if this mob should proactively attack mobs hostile to its owner.
	 */
	public default boolean shouldAttackMobsHostileToOwner()
	{
		return NFFGirlsBaubleStatics.countBaublesWithMinTier(this.asMob(), new ResourceLocation(NFFGirls.MOD_ID, "courage_amulet"), 2) > 0;
	}
	
	// ===== Network =========== //
	
	public default void doSync()
	{
		ClientboundNFFGirlsMobGeneralSyncPacket packet = new ClientboundNFFGirlsMobGeneralSyncPacket(this);
		if (this.isOwnerInDimension() && this.getOwner() instanceof ServerPlayer toPlayer)
			NFFGirlsChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
	}

	// ===== Util ===
	
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
			map.put(Integer.toString(j), this.getAdditionalInventory().getItem(i));
			j++;
		}
		return map;
	}

	// CLIENT ONLY
	public default boolean shouldSitOnWaiting() {
		return true;
	}

	public default float sitPositionOffset() {
		return this.asMob().isBaby() ? -0.3f : -0.6f;
	}

}
