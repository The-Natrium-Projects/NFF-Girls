package net.sodiumzh.nff.girls.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAttackingStrategy;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEntityEventListeners;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.network.ClientboundNFFGirlsMobGeneralSyncPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItemMappings;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.item.capability.wrapper.IItemStackMonitor;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.annotation.DontOverride;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.object.FilteredMapper;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.*;
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
	public default float getXpLevel()
	{
		return this.getLevelHandler().getExpectedLevel();
	}
	
	@DontOverride
	public default float getOverallXp()
	{
		return this.getLevelHandler().getExp();
	}
	
	@DontOverride
	public default float getXpInThisLevel()
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

	public default InteractionResult serversideMainHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}
/*
	public default InteractionResult serversideOffHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}
*/
	public default InteractionResult clientsideMainHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}
	/*
	public default InteractionResult clientsideOffHandInteraction(Player player, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}*/

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
	public default MobApplicableItemTable getHealingItems() {
		return NFFGirlsHealingItemMappings.get(this.asMob().getType()).orElse(MobApplicableItemTable.EMPTY);
	}

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
			map.put(Integer.toString(j), this.getAdditionalInventory().getItem(i));
			j++;
		}
		return map;
	}

	// ATTACKING STRATEGY RELATED //

	public default NFFGirlsAttackingStrategy getAttackingStrategy() {
		return NFFGirlsAttackingStrategy.fromNBT(this.getData().getAdditionalNBT().getCompound("attackingStrategy"));
	}

	public default void setAttackingStrategy(NFFGirlsAttackingStrategy strategy) {
		this.getData().getAdditionalNBT().put("attackingStrategy", strategy.toNBT());
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

}
