package net.sodiumzh.nff.girls.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nautils.statics.NaUtilsLevelStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class CombatCommandingWandItem extends NaUtilsItem {

	private static final String KEY_SETTING_MOB_IDENTIFIER = "settingIdentifier";

	public CombatCommandingWandItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (!level.isClientSide()) {
			HitResult res = NaUtilsLevelStatics.eyeTrace(player, 32d).orElse(null);
			// If targeting an entity
			if (res != null && res.getType().equals(HitResult.Type.ENTITY)
				&& res instanceof EntityHitResult ehr
				&& ehr.getEntity() instanceof LivingEntity target
				&& target.distanceTo(player) > player.getEntityReach()) {
				return doServerInteractLivingEntity(player, target, usedHand).consumesAction() ?
					InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide()) :
					InteractionResultHolder.pass(player.getItemInHand(usedHand));
			}

			// Not targeting entity, reset
			else if (player.isShiftKeyDown()) {
				if (player.getItemInHand(usedHand).hasTag() && player.getItemInHand(usedHand).getTag().hasUUID(KEY_SETTING_MOB_IDENTIFIER)) {
					player.getItemInHand(usedHand).removeTagKey(KEY_SETTING_MOB_IDENTIFIER);
					NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.combat_set_target_reset");
					return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
				}
			}
		}

		return InteractionResultHolder.pass(player.getItemInHand(usedHand));
	}

	@Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
		if (!player.level.isClientSide())
			return doServerInteractLivingEntity(player, target, hand);
		return InteractionResult.PASS;
	}

	private InteractionResult doServerInteractLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
		if (player.isShiftKeyDown()) {
			return INFFGirlsTamed.get(target).filter(t -> t.isOwnedBy(player)).map(tamed -> {
				tamed.asMob().setTarget(null);
				NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.combat_stop_attacking", target.getName().getString());
				return InteractionResult.sidedSuccess(player.level.isClientSide());
			}).orElseGet(() -> {
				AtomicBoolean set = new AtomicBoolean(false);
				player.level.getEntities(player, player.getBoundingBox().inflate(16d), INFFGirlsTamed::isBM)
					.forEach(e -> {
						if (trySetTarget(player, e, target)) set.set(true);
					});
				if (set.get())
					NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.combat_set_target_all", target.getName().getString());
				return InteractionResult.sidedSuccess(player.level.isClientSide());
			});
		}
		else if (player.getItemInHand(hand).hasTag() && player.getItemInHand(hand).getTag().hasUUID(KEY_SETTING_MOB_IDENTIFIER)) {
			if (player.level instanceof ServerLevel sl) {
				Entity attacker = INFFTamed.byIdentifier(player.getItemInHand(hand).getTag().getUUID(KEY_SETTING_MOB_IDENTIFIER), sl).orElse(null);
				if (trySetTarget(player, attacker, target)) {
					NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.combat_set_target", attacker.getName().getString(), target.getName().getString());
				} else {
					NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.combat_set_target_failed");
				}
				player.getItemInHand(hand).removeTagKey(KEY_SETTING_MOB_IDENTIFIER);
				return InteractionResult.sidedSuccess(player.level.isClientSide());
			}
		}
		else if (INFFGirlsTamed.isBMAnd(target, tamed -> player.getUUID().equals(tamed.getOwnerUUID()))) {
			player.getItemInHand(hand).getOrCreateTag().putUUID(KEY_SETTING_MOB_IDENTIFIER, INFFGirlsTamed.getBM(target).getIdentifier());
			NaUtilsInfoStatics.printMessageTranslatable(player, "info.nffgirls.item." +
				"combat_setting_target_selected", target.getName().getString());
			return InteractionResult.sidedSuccess(player.level.isClientSide());
		}
		return InteractionResult.PASS;
	}

	private static boolean canTamedAttackTarget(Entity tamed, LivingEntity target) {
		if (tamed == null || target == null) return false;
		return INFFGirlsTamed.isBMAnd(tamed, tm -> tm.wantsToAttack(target));
	}

	private static boolean trySetTarget(@Nonnull Player owner, @Nullable Entity tamedEntity, @Nullable LivingEntity target) {
		if (tamedEntity == null || target == null) return false;
		if (!INFFGirlsTamed.isBMAnd(tamedEntity, t -> owner.getUUID().equals(t.getOwnerUUID()))) return false;
		if (canTamedAttackTarget(tamedEntity, target)) {
			INFFGirlsTamed.ifBM(tamedEntity, tm -> tm.asMob().setTarget(target));
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldSkipEntityInteract(Player user, Entity target, InteractionHand hand) {
		return true;
	}
}
