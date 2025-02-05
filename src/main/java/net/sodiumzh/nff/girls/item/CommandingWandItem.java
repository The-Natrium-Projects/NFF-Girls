package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsAIStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.presets.INFFFollowOwner;
import net.sodiumzh.nff.services.entity.taming.CNFFTamedCommonData;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class CommandingWandItem extends NaUtilsItem
{

	public CommandingWandItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	@SuppressWarnings("resource")
	public @Nonnull InteractionResult useOn(UseOnContext context)
	{
		if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.EVIL_CRYSTAL_BLOCK.get()))
		{
			if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl)
			{
				BlockPos blockpos = context.getClickedPos();
				AABB bound = new AABB(blockpos.subtract(new Vec3i(16, 16, 16)), blockpos.offset(new Vec3i(16, 16, 16)));
				List<Entity> bmList = sl.getEntities(null, bound);
				for (Entity e: bmList)
				{
					if (e instanceof INFFGirlsTamed bm && bm.getOwnerUUID().equals(context.getPlayer().getUUID()))
						((INFFGirlsTamed)e).setAIState(NFFTamedMobAIState.FOLLOW, true);
					else if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(NFFGirlsItems.MOB_RESPAWNER.get()))
						{
							NFFMobRespawnerInstance mr = NFFMobRespawnerInstance.createIfValid(ie.getItem());
							if (mr != null && CNFFTamedCommonData.getOwnerUUIDFromMobTag(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								ie.moveTo(blockpos.getX() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.5, blockpos.getZ() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2));
							}
						}
					}
				}
			}
			context.getLevel().playSound(context.getPlayer(), context.getClickedPos(),
					SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

	// When shift key down, summon all mobs
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (!level.isClientSide() && player.isShiftKeyDown()) {
			List<Entity> mobs = level.getEntities(player, player.getBoundingBox().inflate(16d),
					(Entity e) -> INFFGirlsTamed.isBMAnd(e, tamed -> Objects.equals(player.getUUID(), tamed.getOwnerUUID())));
			// Set each mob follow, clear target, try teleport
			for (Entity mobEntity : mobs) {
				if (mobEntity instanceof Mob mob) {
					mob.setTarget(null);
					INFFGirlsTamed.ifBM(mob, (bm) -> bm.setAIState(NFFTamedMobAIState.FOLLOW, false));
					List<INFFFollowOwner> followGoals =
							NaUtilsAIStatics.getGoalsAndPriorities(mob).keySet().stream()
							.filter(goal -> goal instanceof INFFFollowOwner follow)
							.map(goal -> (INFFFollowOwner)goal).toList();
					if (followGoals.isEmpty()) continue;
				}
			}
			level.playSound(player, player.blockPosition(),
					SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 2.0F, 1.0F);
			return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
		}
		return InteractionResultHolder.pass(player.getItemInHand(usedHand));
	}
}
