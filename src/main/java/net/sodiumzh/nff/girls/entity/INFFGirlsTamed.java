package net.sodiumzh.nff.girls.entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexthe666.citadel.repack.jaad.Play;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nautils.capability.CEntityDataCapability;
import net.sodiumzh.nautils.containers.Tuple3;
import net.sodiumzh.nautils.mixin.events.entity.MobInteractEvent;
import net.sodiumzh.nautils.registries.NaUtilsCaps;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsEntityEventListeners;
import org.apache.commons.lang3.mutable.MutableObject;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumzh.nautils.annotation.DontCallManually;
import net.sodiumzh.nautils.annotation.DontOverride;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.network.ClientboundNFFGirlsMobGeneralSyncPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsBaubleStatics;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nff.services.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.item.capability.wrapper.IItemStackMonitor;

public interface INFFGirlsTamed extends INFFTamed, IAttributeMonitor, IItemStackMonitor
{

	/**
	 * Check if a mob has a NFFGirls BM interface.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check.
	 */
	public static boolean isBM(Object o)
	{
		if (o == null) return false;
		if (o instanceof INFFGirlsTamed bm)
			return true;
		else return false;
	}
	
	/**
	 * Cast a mob to the NFFGirls BM interface. Null if failed.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this to cast a mob to BM.
	 */
	@Nullable
	public static INFFGirlsTamed getBM(Object o)
	{
		if (o == null) return null;
		if (o instanceof INFFGirlsTamed bm)
			return bm;
		else return null;
	}
	
	/**
	 * Do an action if a mob has a NFFGirls BM interface.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * you can use this to safely cast and do things to BM.
	 * @return Whether the action is invoked.
	 */
	public static boolean ifBM(Object o, Consumer<INFFGirlsTamed> action)
	{
		if (o == null) return false;
		if (INFFGirlsTamed.isBM(o))
		{
			action.accept(INFFGirlsTamed.getBM(o));
			return true;
		}
		else return false;
	}

	/**
	 * Check if a mob has a NFFGirls BM interface and satisfied the given condition.
	 * <p>
	 * As INFFTamed could also be implemented in capabilities instead of the mob class in the future,
	 * always use this instead of {@code instanceof} check and followed checks of the cast BM.
	 */
	public static boolean isBMAnd(Object o, Predicate<INFFGirlsTamed> cond)
	{
		if (!isBM(o)) return false;
		return cond.test(getBM(o));
	}

	public static void ifBMAnd(Object o, Predicate<INFFGirlsTamed> cond, Consumer<INFFGirlsTamed> operation) {
		if (isBMAnd(o, cond)) {
			ifBM(o, operation);
		}
	}

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

}
