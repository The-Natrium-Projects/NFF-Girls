package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModBlocks;
import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.INFFFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nfu.util.NFUAIStatics;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CommandingWandItem extends NFUItem
{
	private static final UUID EMPTY_UUID = new UUID(0L, 0L);


	public CommandingWandItem(Item.Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	@SuppressWarnings("resource")
	public @Nonnull InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		if (context.getHand().equals(InteractionHand.MAIN_HAND)
			&& context.getPlayer() != null)
		{
			/*if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl)
			{
				BlockPos blockpos = context.getClickedPos();
				AABB bound = new AABB(blockpos.subtract(new Vec3i(16, 16, 16)), blockpos.offset(new Vec3i(16, 16, 16)));
				List<Entity> bmList = sl.getEntities(null, bound);
				for (Entity e: bmList)
				{
					if (INFFGirlsTamed.isBMAnd(e,  bm -> bm.getOwnerUUID().equals(context.getPlayer().getUUID())))
						INFFGirlsTamed.ifBM(e, tamed -> tamed.setAIState(NFFTamedMobAIState.FOLLOW, true));
					else if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(NFFGirlsItems.MOB_RESPAWNER.get()))
						{
							NFFMobRespawnerInstance mr = NFFMobRespawnerInstance.createIfValid(ie.getItem());
							if (mr != null && CNFFTamedCommonData.getOwnerUUIDFromMobTag(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								ie.moveTo(blockpos.getX() + 0.5 + NFUMathStatics.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.5, blockpos.getZ() + 0.5 + NFUMathStatics.rndRangedDouble(-0.2,  0.2));
							}
						}
					}
				}
			}
			context.getLevel().playSound(context.getPlayer(), context.getClickedPos(),
					SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
			*/
			if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl) {

				if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.SOUL_POWDER_BLOCK.get())) {
					if (!context.getPlayer().isShiftKeyDown()) {
						FindMobResult res = findNext(context.getItemInHand(), sl, context.getPlayer());
						if (!res.isEnd()) {
							NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
								"info.nffgirls.item.commanding_wand.mob_search_found", res.location().mobName(),
								res.location().dimension().location().toString(),
								res.location().pos().getX(), res.location().pos().getY(), res.location().pos().getZ());
							if (res.isFirst())
								NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
									"info.nffgirls.item.commanding_wand.mob_search_summoning_tip");
						} else {
							if (context.getItemInHand().getTag().getList("alreadyFound", Tag.TAG_COMPOUND).isEmpty())
								NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
									"info.nffgirls.item.commanding_wand.mob_search_not_found");
							else NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
								"info.nffgirls.item.commanding_wand.mob_search_end");
						}
					} else if (context.getPlayer().getOffhandItem().is(ModItems.EVIL_CRYSTAL_FRAGMENT.get())) {
						Optional<UUID> idOpt = this.getLastFoundIdentifier(context.getItemInHand(), sl);
						Optional<Mob> mobOpt = this.getLastFoundMob(context.getItemInHand(), sl);
						if (idOpt.isEmpty()) {
							NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
								"info.nffgirls.item.commanding_wand.mob_search_summon_no_target");
						} else if (mobOpt.isEmpty()) {
							NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
								"info.nffgirls.item.commanding_wand.mob_search_summon_not_found");
						} else {
							Mob mob = mobOpt.get();
							if (this.summonMob(sl, context.getPlayer(), mob)) {
								context.getPlayer().getOffhandItem().shrink(1);
								NFUInfoStatics.printMessageTranslatable(context.getPlayer(),
									"info.nffgirls.item.commanding_wand.mob_search_summoned_mob", mob.getName().getString());
							}
						}
					}
				}

				else if (context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.BELL)
					&& !context.getPlayer().isShiftKeyDown()) {
					summonMobsAround(sl, context.getPlayer());
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

	// When shift key down, summon all mobs
	/*@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide) summonMobsAround(level, player);
			return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
		}
		return InteractionResultHolder.pass(player.getItemInHand(usedHand));
	}*/

	// Server only, no action on client
	private boolean summonMobsAround(Level level, Player player) {
		if (level.isClientSide) return false;
		List<Entity> mobs = level.getEntities(player, player.getBoundingBox().inflate(16d),
			(Entity e) -> INFFGirlsTamed.get(e).filter(tamed -> Objects.equals(player.getUUID(), tamed.getOwnerUUID())).isPresent());
		boolean res = false;
		// Set each mob follow, clear target, try teleport
		for (Entity mobEntity : mobs) {
			if (mobEntity instanceof Mob mob && this.summonMob(level, player, mob)) {
				res = true;
				NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.commanding_wand.summoned_mob", mob.getName().getString());
			}
		}
		if (res) {
			level.playSound(player, player.blockPosition(),
				SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 2.0F, 1.0F);
		} else {
			NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.commanding_wand.summon_failed");
		}
		return res;
	}

	// Summon a mob to the owner. No action if the mob isn't an nff mob of the player.
	// Only on server, no action on client.
	// Return if summoned.
	private boolean summonMob(Level level, Player player, Mob mob) {
		if (level.isClientSide) return false;
		mob.setTarget(null);
		return INFFGirlsTamed.get(mob).filter(bm -> {
			bm.setAIState(NFFTamedMobAIState.FOLLOW, false);
			List<INFFFollowOwnerGoal> followGoals =
				NFUAIStatics.getGoalsAndPriorities(mob).keySet().stream()
					.filter(goal -> goal instanceof INFFFollowOwnerGoal follow)
					.map(goal -> (INFFFollowOwnerGoal)goal).toList();
			if (!followGoals.isEmpty()) followGoals.get(0).teleportToOwner();
			return true;
		}).isPresent();
	}

	// *** Mob Finding Related

	private void tickFindMob(ItemStack stack, Level level) {
		if (!level.isClientSide) {
			if (stack.hasTag() && stack.getTag().contains("findMobData", Tag.TAG_COMPOUND)) {
				CompoundTag findMobData = stack.getTag().getCompound("findMobData");
				int expire = findMobData.contains("expire", Tag.TAG_INT) ? findMobData.getInt("expire") : 0;
				if (expire > 0) {
					findMobData.putInt("expire", expire - 1);
				} else {
					stack.getTag().remove("findMobData");
				}
			}
		}
	}

	@Nonnull
	private FindMobResult findNext(ItemStack stack, ServerLevel level, Player player) {
		// Init nbt
		if (!stack.getOrCreateTag().contains("findMobData", Tag.TAG_COMPOUND) ||
			stack.getTag().getCompound("findMobData").getBoolean("ended"))
		{
			stack.getTag().put("findMobData", new CompoundTag());
			stack.getTag().getCompound("findMobData").put("alreadyFound", new ListTag());
			stack.getTag().getCompound("findMobData").putUUID("lastFound", EMPTY_UUID);
			stack.getTag().getCompound("findMobData").putInt("expire", 10 * 20);
			stack.getTag().getCompound("findMobData").putBoolean("ended", false);
		}
		List<UUID> alreadyFound = /*NFUNBTStatics.listFromListTag(stack.getTag().getCompound("findMobData")
			.getList("alreadyFound", Tag.TAG_INT_ARRAY), NbtUtils::loadUUID);*/
			stack.getTag().getCompound("findMobData").getList("alreadyFound", Tag.TAG_INT_ARRAY)
				.stream().map(NbtUtils::loadUUID).toList();
		AtomicReference<FindMobResult> found = new AtomicReference<>(FindMobResult.end());

		List<INFFGirlsTamed> mobsLoaded = NFUEntityStatics.getEntitiesOnServer(level, EntityTypeTest.forClass(Mob.class),
			e -> INFFGirlsTamed.get(e).filter(tm -> Objects.equals(tm.getOwnerUUID(), player.getUUID())).isPresent())
			.stream().flatMap(en -> INFFGirlsTamed.get(en).stream()).toList();
		// Search in levels
		mobsLoaded.stream().filter(e -> {
			UUID uuid = e.getIdentifier();
			return !uuid.equals(EMPTY_UUID) && !alreadyFound.contains(e.getIdentifier());
		}).findFirst().ifPresent(e -> found.set(new FindMobResult(INFFTamed.MobLocationInfo.fromMob(e), alreadyFound.isEmpty())));
		// Search in saved locations
		if (found.get().isEnd()) {
			INFFTamed.removeSuspiciousMobLocations(player);
			INFFTamed.getAllMobLocations(player).values().stream().filter(loc -> !alreadyFound.contains(loc.identifier())).findFirst()
				.ifPresent(loc -> found.set(new FindMobResult(loc, alreadyFound.isEmpty())));
		}
		// Update nbt
		if (!found.get().isEnd()) {
			stack.getTag().getCompound("findMobData").getList("alreadyFound", Tag.TAG_INT_ARRAY)
				.add(NbtUtils.createUUID(found.get().location().identifier()));
			stack.getTag().getCompound("findMobData").putUUID("lastFound", found.get().location().identifier());
			stack.getTag().getCompound("findMobData").putInt("expire", 10 * 20);
		} else {
			stack.getTag().getCompound("findMobData").putBoolean("ended", true);
		}
		return found.get();
	}

	private Optional<UUID> getLastFoundIdentifier(ItemStack stack, ServerLevel level) {
		if (!stack.hasTag()
			|| !stack.getTag().contains("findMobData")
			|| !stack.getTag().getCompound("findMobData").hasUUID("lastFound"))
			return Optional.empty();
		UUID uuid = stack.getTag().getCompound("findMobData").getUUID("lastFound");
		if (Objects.equals(uuid, EMPTY_UUID)) return Optional.empty();
		return Optional.of(uuid);
	}

	private Optional<Mob> getLastFoundMob(ItemStack stack, ServerLevel level) {
		return getLastFoundIdentifier(stack, level).flatMap(id -> INFFTamed.byIdentifier(id, level));
	}

	private static record FindMobResult(INFFTamed.MobLocationInfo location, boolean isFirst){
		private static final FindMobResult END = new FindMobResult(null, false);
		public boolean isEnd() {return this == END;}
		public static FindMobResult end() { return END;}
	}


	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		tickFindMob(stack, level);
	}
}
