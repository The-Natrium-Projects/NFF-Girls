package net.sodiumzh.nff.girls.entity.vanillatrade;

import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsTrades;
import net.sodiumzh.nfu.capability.SerializableCapabilityProvider;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.entity.vanillatrade.CVanillaMerchant;
import net.sodiumzh.nfu.entity.vanillatrade.IVanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.ScaledVanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaMerchant;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFUDebugStatics;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface CNFFGirlsTradeHandler extends CVanillaMerchant
{
	public static final int[] LEVEL_REQUIREMENTS = {0, 5, 15, 30, 45};
	public static final int[] OFFER_COUNT_FOR_LEVEL = {2, 2, 2, 2, 1};
	public static final ItemListing INTRODUCTION = (e, rnd) -> 
	{
		ItemStack res = new ItemStack(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get());
		NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get().write(res, INFFGirlsTamed.getBM(e));
		return new MerchantOffer(new ItemStack(Items.WRITABLE_BOOK), res, 1, 0, 0);
	};

	public void serverTick();

	public INFFGirlsTamed getBM();
	
	public List<NFFGirlsTradeOfferMetaData> getMeta();
	
	public boolean isValidOffers();

	/**
	 * Check if this trader has any valid entries in the trade registry.
	 */
	public boolean isValidTrader();

	/**
	 * How many points it needs to get an introduction letter
	 */
	public int getPointsPerIntroduction();
	
	/**
	 * Current trade points
	 */
	public int getPoints();
	
	/**
	 * Set current trade points
	 */
	public void setPoints(int val);
	
	/**
	 * Remove all offers and regenerate. This happens in game when specific item is used.
	 * @param setOutOfStock If true, the new offers will be out-of-stock, otherwise all available.
	 */
	public void regenerateTrades(boolean setOutOfStock);
	
	/**
	 * Ticks after sending a restock event.
	 * Note: a restock event doesn't necessarily restock all offers. The probability depends on the required level.
	 */
	public int getRestockTicks();
	
	public static class Impl extends VanillaMerchant implements CNFFGirlsTradeHandler
	{
		protected static final RandomSource RND = RandomSource.create();
		private List<NFFGirlsTradeOfferMetaData> meta = new ArrayList<>();
		private int cachedLevel = 1;
		private int restockTimer = 600 * 20;
		private boolean triedRegenerate = false;	// When tick encounters an error, it will try regenerating the offers. If it still errors, the offers will be set back and throw the error/exception.
		private MerchantOffers backupOffers = null;
		private List<NFFGirlsTradeOfferMetaData> backupMeta = null;
		private int tradePoints = 0;
		
		public Impl(INFFGirlsTamed bm)
		{
			super(bm.asMob());
		}
		
		@Override
		public INFFGirlsTamed getBM()
		{
			return INFFGirlsTamed.getBM(this.getMob());
		}
		
		@Override
		public List<NFFGirlsTradeOfferMetaData> getMeta()
		{
			return meta;
		}
		
		@Override
		public int getPointsPerIntroduction()
		{
			return INFFGirlsTamed.isBM(this.getMob()) ? INFFGirlsTamed.getBM(this.getMob()).pointsPerIntroductionLetter() : 128;
		}
		
		@Override
		public int getPoints()
		{
			return this.tradePoints;
		}
		
		@Override
		public void setPoints(int val)
		{
			this.tradePoints = val;
		}
		
		@Override
		public void generateTrades(){
			if (!this.isValidTrader()) return;
			this.getOffersRaw().clear();
			this.getMeta().clear();
			Multimap<Integer, ScaledVanillaTradeListing> trades =
					NFFGirlsTrades.TRADE_REGISTRY.get().collect()
						.get(ForgeRegistries.ENTITY_TYPES.getKey(this.getMob().getType()), VillagerProfession.NONE)
						.pickListingForSpecifiedLevels(this.getBM().getTradeEntryCountEachLevel());

			if (trades.isEmpty()) return;
			trades.entries().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEach(entry -> {
				MerchantOffer offer = entry.getValue().getOffer(getMob(), RND);
				this.getOffersRaw().add(offer);
				NFFGirlsTradeOfferMetaData meta = new NFFGirlsTradeOfferMetaData(entry.getKey(), 0, !this.getOffersRaw().get(this.getOffersRaw().size() - 1).getCostB().isEmpty());
				this.meta.add(meta);
			});
			

			// Finally add introduction letter entry
			this.getOffersRaw().add(INTRODUCTION.getOffer(getMob(), RND));
			this.getMeta().add(new NFFGirlsTradeOfferMetaData(1, 0, false));
		}

		/**
		 * Regenerate a specified trade entry at the given position.
		 * Note: this method is not performance-friendly and should not be called every tick.
		 */
		private void regenerateTradeAt(int index)
		{
			// Don't allow to regenerate at the introduction letter entry
			if (index < 0 || index >= this.getOffersRaw().size() - 1) throw new IllegalArgumentException();
			Set<ScaledVanillaTradeListing> available = NFFGirlsTrades.TRADE_REGISTRY.get().collect().
				get(ForgeRegistries.ENTITY_TYPES.getKey(INFFGirlsTamed.getBM(this.getMob()).getData().getInitialEntityType()), VillagerProfession.NONE)
				.forLevel(this.getMeta(index).requiredMerchantLevel);
			if (available.isEmpty()) return;
			int merchantLevel = this.getMeta(index).requiredMerchantLevel;

			// Prevent generating a duplicate of another entry
			Set<MerchantOffer> existingOffers = new HashSet<>();
			for (int i = 0 ; i < this.getOffersRaw().size() - 1; ++i)
			{
				if (this.getMeta(i).requiredMerchantLevel == merchantLevel && i != index)
					existingOffers.add(this.getOffersRaw().get(i));
			}
			Predicate<MerchantOffer> exclude = offer -> {
				for (MerchantOffer existing: existingOffers)
				{
					if (existing.getBaseCostA().getItem().equals(offer.getBaseCostA().getItem())
						&& existing.getResult().getItem().equals(offer.getResult().getItem())
						&& (existing.getCostB().isEmpty() || existing.getCostB().getItem().equals(offer.getCostB().getItem())))
						return true;
				}
				return false;
			};
			MerchantOffer out = null;

			// Try 16 times, give up if failed and keep it unchanged
			for (int i = 0; i < 16; ++i)
			{
				Set<IVanillaTradeListing> listingPicked = NFUContainerStatics.getWeightedRandomSubset(
					available.stream().collect(Collectors.toMap(l -> l, IVanillaTradeListing::getSelectionWeight)), 1);
				if (listingPicked.isEmpty()) return;	// This shouldn't happen
				IVanillaTradeListing listing = listingPicked.stream().findFirst().orElseThrow();
				out = listing.getOffer(this.getMob(), this.getMob().getRandom());
				if (!exclude.test(out))
				{
					this.getOffersRaw().set(index, out);
					this.getMeta().set(index, new NFFGirlsTradeOfferMetaData(merchantLevel, 0, !out.getCostB().isEmpty()));
					return;
				}
			}
		}

		@Override
		public int getMerchantLevel() {
			if (this.getMob() instanceof INFFGirlsTamed bm)
			{
				int[] levelRequirements = bm.getXpLevelRequirementsEachMerchantLevel();
				for (int i = levelRequirements.length; i > 0; --i)
				{
					if (bm.getXpLevel() >= levelRequirements[i - 1])
						return i;
				}
				return 1;
			}
			return 1;
		}

		@Override
		public void onTrade(MerchantOffer offer) {
			var meta = this.getMeta(offer);
			if (meta != null) {
                this.getBM().getFavorabilityHandler().addFavorability(meta.requiredMerchantLevel * 0.1f);
                if (offer.getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get()))
                    tradePoints -= this.getPointsPerIntroduction();
                else
                {
                    tradePoints += meta.requiredMerchantLevel;
                    this.getBM().getLevelHandler().addExp((1 + meta.requiredMerchantLevel) * meta.requiredMerchantLevel / 2 );
                }
            }
		}

		
		@Override
		public boolean canRestock() {
			return true;
		}

		@Override
		public void serverTick() 
		{
			if (!this.isValidTrader()) return;
			//boolean tryingRegenerate = false;
			try {
				this.serverTickInternal();
			} catch (Throwable t) {
				if (!triedRegenerate)
				{
					LogUtils.getLogger().error("CNFFGirlsTradeHandler ticking encountered an error. Try regenerate offers.");
					t.printStackTrace();
					this.triedRegenerate = true;
					this.backupOffers = this.getOffersRaw();
					this.backupMeta = this.getMeta();
					this.setOffers(new MerchantOffers());
					this.meta = new ArrayList<>();
					this.generateTrades();
					//tryingRegenerate = true;
				}
				else 
				{
					LogUtils.getLogger().error("CNFFGirlsTradeHandler ticking encountered an error which cannot be fixed by regenerating offers.");
					if (this.backupOffers != null)
						this.setOffers(backupOffers);
					if (this.backupMeta != null)
						this.meta = this.backupMeta;
					throw t;
				}
			}
		}
		
		protected void serverTickInternal()
		{

			// Handle offer uses cache
			if (this.getOffers().size() != meta.size() || !this.isValidOffers() && this.isValidTrader())
			{
				/*throw new IllegalStateException(String.format("CNFFGirlsTradeHandler: offer meta data size error. Expected: %d; Actual: %d",
						this.getOffers().size(), this.meta.size()));*/
				this.generateTrades();
			}
			if (!this.isValidOffers()) return;

			int level = this.getMerchantLevel();
			if (level != cachedLevel)
			{
				this.onMerchantLevelChange(cachedLevel, level);
			}
			MerchantOffers offers = this.getOffers();
			// Cache available offer count, then set unavailable offer count to max
			for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, no tick
			{
				if (meta.get(i).requiredMerchantLevel <= level)
					meta.get(i).cachedUse = offers.get(i).getUses();
				else offers.get(i).setToOutOfStock();
			}
			this.cachedLevel = level;
			
			// Handle restock
			this.restockTimer --;
			if (this.restockTimer <= 0)
			{
				for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, no tick
				{
					if (this.getMeta(i).requiredMerchantLevel <= this.getMerchantLevel())
					{
						if (RND.nextFloat() <= 1d / Math.sqrt(this.getMeta(i).requiredMerchantLevel))
						{
							if (this.getMeta().get(i).requiredMerchantLevel >= 5) {
								if (this.getOffers().get(i).isOutOfStock()) {
									this.regenerateTradeAt(i);
									this.getOffers().get(i).resetUses();
								}
							}
							else {
								this.getOffers().get(i).resetUses();
							}
						}
					}
				}

				
				this.restockTimer = this.getBM().getRestockTicks();
				/*if (getBM().isOwnerInDimension())
					NaUtilsMiscStatics.printToScreen("Restocked", getBM().getOwner());*/
			}
			/*if (restockTimer % 200 == 0)
				if (getBM().isOwnerInDimension())
			
			NaUtilsMiscStatics.printToScreen(String.format("Restock time left: %d s", restockTimer / 20) , getBM().getOwner());*/
			
			// Update introduction letter entry
            if (this.getOffers().get(this.getOffers().size() - 1).getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get())) {
                // Regenerate this entry every second, as the mob's name may update
                if (this.getMob().tickCount % 20 == 10)
                   this.getOffers().set(this.getOffers().size() - 1, INTRODUCTION.getOffer(getMob(), RND));
                // Update letter entry availability
                if (this.tradePoints < this.getPointsPerIntroduction())
                    this.getOffers().get(this.getOffers().size() - 1).setToOutOfStock();
                else this.getOffers().get(this.getOffers().size() - 1).resetUses();
            }
            // This branch should not happen, going into this branch means the introduction
            // letter entry is not correctly added. Try fixing
            else {
                NFUDebugStatics.errorOnce("NFFGirls: Mob " + this.getMob().getName().getString()
                + " trade offers missing introduction letter. Trying to regenerate.");
                // Collect and remove letter entries that are not at the end
                List<Integer> wrongLetterEntries = new ArrayList<>();
                for (int i = 0; i < this.getOffers().size(); ++i) {
                    if (getOffers().get(i).getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get()))
                        wrongLetterEntries.add(i);
                }
                for (int i = wrongLetterEntries.size() - 1; i >= 0; --i) {
                    this.getOffers().remove(wrongLetterEntries.get(i).intValue());
                    this.getMeta().remove(wrongLetterEntries.get(i).intValue());
                }
                // Try regenerating
                this.getOffersRaw().add(INTRODUCTION.getOffer(getMob(), RND));
                this.getMeta().add(new NFFGirlsTradeOfferMetaData(1, 0, false));
            }

			// Update discount
			for (var offer: this.getOffers())
			{
				float factor = Mth.lerp(this.getBM().getNormalizedFavorability(), 0.5f, -0.5f);
				offer.setSpecialPriceDiff(Math.round(factor * offer.getBaseCostA().getCount()));
			}
			
			// Handle sync
			ClientboundNFFGirlsTradeSyncPacket packet = new ClientboundNFFGirlsTradeSyncPacket(this);
			if (this.getBM().isOwnerInDimension() && this.getBM().getOwner() instanceof ServerPlayer toPlayer)
				NFFGirlsChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
			
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			var tag = super.serializeNBT();
			ListTag tagMeta = new ListTag();
			tagMeta.addAll(this.meta.stream().map(NFFGirlsTradeOfferMetaData::toTag).toList());
			tag.put("meta", tagMeta);
			tag.putInt("cached_level", this.cachedLevel);
			tag.putInt("restock_timer", this.restockTimer);
			tag.putInt("points", this.tradePoints);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag tag)
		{
			super.deserializeNBT(tag);
			ListTag tagCachedUse = tag.getList("meta", Tag.TAG_COMPOUND);
			this.meta.clear();
			for (int i = 0; i < tagCachedUse.size(); ++i)
			{
				meta.add(NFFGirlsTradeOfferMetaData.fromTag(tagCachedUse.getCompound(i)));
			}
			this.cachedLevel = tag.getInt("cached_level");
			this.restockTimer = tag.getInt("restock_timer");
			this.tradePoints = tag.getInt("points");
			this.checkAndRemoveInvalidOffers();
		}

		protected void onMerchantLevelChange(int from, int to)
		{
			if (!isValidOffers()) return;
			for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, skip
			{
				if (this.meta.get(i).requiredMerchantLevel <= to)
				{
					this.setOfferUses(i, this.meta.get(i).cachedUse);
				}
			}
		}

		// === Utils === //
		
		protected NFFGirlsTradeOfferMetaData getMeta(int index)
		{
			return this.meta.get(index);
		}
		
		@Nullable
		protected NFFGirlsTradeOfferMetaData getMeta(MerchantOffer offer)
		{
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (this.getOffers().get(i) == offer)
					return this.meta.get(i);
			}
			return null;
		}
		
		@Override
		public boolean isValidOffers()
		{
			return this.getOffers().size() == this.meta.size() && !this.getOffers().isEmpty();
		}

		@Override
		public boolean isValidTrader()
		{
			if (NFFGirlsTrades.TRADE_REGISTRY.get() == null) return false;
			return !NFFGirlsTrades.TRADE_REGISTRY.get().collect()
				.get(ForgeRegistries.ENTITY_TYPES.getKey(this.getMob().getType()), VillagerProfession.NONE)
				.isEmpty();
		}

		public List<Tuple2<MerchantOffer, NFFGirlsTradeOfferMetaData>> getOffersAndMeta() {
			List<Tuple2<MerchantOffer, NFFGirlsTradeOfferMetaData>> res = new ArrayList<>();
			for (int i = 0; i < this.getOffersRaw().size(); ++i) {
				res.add(Tuple2.of(this.getOffersRaw().get(i), this.getMeta(i)));
			}
			return res;
		}

		@Override
		public int getRestockTicks() {
			return this.getBM().getRestockTicks();
		}

		protected void setAllOffersOutOfStock() {
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				this.getOffers().get(i).setToOutOfStock();
				this.getMeta().get(i).cachedUse = this.getOffers().get(i).getMaxUses();
			}
		}

		@Override
		public void regenerateTrades(boolean setOutOfStock) {
			this.generateTrades();
			if (setOutOfStock)
				this.setAllOffersOutOfStock();
		}
		
		protected void checkAndRemoveInvalidOffers()
		{
			if (!isValidOffers())
				return;
			List<MerchantOffer> toRemove0 = new ArrayList<>();
			List<NFFGirlsTradeOfferMetaData> toRemove1 = new ArrayList<>();
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (this.getOffers().get(i).getBaseCostA().isEmpty() || this.getOffers().get(i).getResult().isEmpty()
						|| (this.getOffers().get(i).getCostB().isEmpty() && this.getMeta().get(i).hasB))
				{
					toRemove0.add(this.getOffers().get(i));
					toRemove1.add(this.getMeta().get(i));
				}
			}
			for (var elem: toRemove0)
			{
				this.getOffers().remove(elem);
			}
			for (var elem: toRemove1)
			{
				this.getMeta().remove(elem);
			}
		}
	}
	
	public static class Prvd extends SerializableCapabilityProvider<CompoundTag, CNFFGirlsTradeHandler>
	{

		public Prvd(INFFGirlsTamed bm, Capability<CNFFGirlsTradeHandler> holder)
		{
			super(() -> new Impl(bm), holder);
		}
		
	}

	/**
	 * Search for merchant using {@code CNFFGirlsTradeHandler} that's trading with the player around the mob.
	 * If not found (not existing or not using {@code CNFFGirlsTradeHandler} e.g. vanilla villager), returns null.
	 */
	@OnlyIn(Dist.CLIENT)
	@Nullable
	public static CNFFGirlsTradeHandler searchOngoingTrader(Player player, double range)
	{
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(range)).stream().filter(e ->
			e.getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).resolve()
				.map(Merchant::getTradingPlayer).filter(p -> p.equals(player)).isPresent()
		).toList();
		return list.isEmpty() ? null : list.get(0).getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).resolve().orElse(null);
	}
}
