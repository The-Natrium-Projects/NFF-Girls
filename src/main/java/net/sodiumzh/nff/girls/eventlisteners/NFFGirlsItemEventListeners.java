package net.sodiumzh.nff.girls.eventlisteners;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.mixin.events.entity.EntityTickEvent;
import net.sodiumzh.nautils.mixin.events.entity.ItemEntityHurtEvent;
import net.sodiumzh.nautils.mixin.events.entity.MobInteractEvent;
import net.sodiumzh.nautils.statics.NaUtilsLevelStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.item.IWithDuration;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.eventlisteners.ServerEntityTickEvent;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.item.event.NFFMobRespawnerAfterConstructEvent;
import net.sodiumzh.nff.services.item.event.NFFMobRespawnerBeforeConstructEvent;
import net.sodiumzh.nff.services.item.event.NFFMobRespawnerStartRespawnEvent;
import org.apache.commons.lang3.mutable.MutableObject;


@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsItemEventListeners
{
	@SubscribeEvent
	public static void beforeRespawnerConstruct(NFFMobRespawnerBeforeConstructEvent event)
	{
	}
	
	@SubscribeEvent
	public static void afterRespawnerConstruct(NFFMobRespawnerAfterConstructEvent event)
	{
		CompoundTag mobNbt = event.getRespawner().getMobNbt();
		
		// Only modify nffgirls respawner types
		String itemKey = null;
		if (event.getRespawner().get().is(NFFGirlsItems.MOB_RESPAWNER.get()))
			itemKey = "item.nffgirls.mob_respawner";
		else if (event.getRespawner().get().is(NFFGirlsItems.MOB_STORAGE_POD.get()))
			itemKey = "item.nffgirls.mob_storage_pod";
		if (itemKey != null)
		{
			mobNbt.putShort("Fire", (short) 0);
		}
		
	}
	
	@SubscribeEvent
	public static void onItemEntityPickUp(EntityItemPickupEvent event)
	{
		if (event.getItem().getItem().getItem() instanceof NFFMobRespawnerItem)
		{
			NFFMobRespawnerInstance ins = NFFMobRespawnerInstance.createIfValid(event.getItem().getItem());
				// If the mob isn't a nffgirls befriended mob, it should not have this uuid
			if (ins != null && !ins.getOwnerUUID().equals(event.getEntity().getUUID()))
			{
				event.setCanceled(true);
			}
		}
		// Clear already picked mobs when player picking up
		if (event.getItem().getItem().getTag() != null && event.getItem().getItem().getTag().contains("already_picked_befriendable_mobs"))
		{
			event.getItem().getItem().removeTagKey("already_picked_befriendable_mobs");
		}
	}
	
	@SubscribeEvent
	public static void onMobRespawnerStartRespawn(NFFMobRespawnerStartRespawnEvent event)
	{
		if (event.getRespawner().getMobNbt().hasUUID("nffgirls:befriended_owner") 
				&& !event.getRespawner().getMobNbt().getUUID("nffgirls:befriended_owner").equals(event.getPlayer().getUUID()))
		{
			event.setCanceled(true);
		}
	}

	@Deprecated
	protected static final UUID SHARPNESS_MODIFIER_UUID = UUID.fromString("9c12b503-63c0-43e6-bd30-d7aae9818c99");
	
	/**
	 * Add sharpness atk modifier to befriended mobs
	 */
	/*@SubscribeEvent
	public static void onBefriendedMainHandItemChange(CItemStackMonitor.ChangeEvent event)
	{
		event.living.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(SHARPNESS_MODIFIER_UUID);
		@SuppressWarnings("deprecation")
		int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, event.to);
		if (lv > 0)
		{
			event.living.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
					SHARPNESS_MODIFIER_UUID, "sharpness_modifier", 0.5d + 0.5d * (double) lv, AttributeModifier.Operation.ADDITION));
		}
	}*/
	
	@SubscribeEvent
	public static void onAnvilChange(AnvilUpdateEvent event)
	{
		if (event.getLeft().getItem() instanceof IWithDuration wd)
		{
			// Necromancer's Wand
			if (event.getLeft().is(NFFGirlsItems.NECROMANCER_WAND.get()) 
					&& event.getRight().is(NFFGirlsItems.DEATH_CRYSTAL_POWDER.get())
					&& wd.canRepair(event.getLeft()))
			{
				ItemStack out = event.getLeft().copy();
				event.setCost(1);
				event.setMaterialCost(1);
				wd.repair(out, 32);
				event.setOutput(out);
			}
			// Evil Magnet fixing
			if (event.getLeft().is(NFFGirlsItems.EVIL_MAGNET.get())
					&& wd.canRepair(event.getLeft())
					&& event.getRight().is(ModItems.EVIL_CRYSTAL.get()))
			{
				ItemStack out = event.getLeft().copy();
				event.setCost(1);
				event.setMaterialCost(1);
				wd.repair(out, 8);
				event.setOutput(out);
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityHurt(ItemEntityHurtEvent event)
	{
		if (event.damageSource.getEntity() != null && event.damageSource.getEntity() instanceof INFFGirlsTamed)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void longDistanceInteract(PlayerInteractEvent.RightClickItem event) {
		// Lengthen the AI switching distance
		if (!event.getEntity().level().isClientSide()
				&& !event.getEntity().isShiftKeyDown()) {
			Player player = event.getEntity();
			InteractionHand usedHand = event.getHand();

			NaUtilsLevelStatics.eyeTrace(player, 32d).ifPresent(hr -> {
				if (hr.getType().equals(HitResult.Type.ENTITY)
						&& hr instanceof EntityHitResult ehr
						&& INFFGirlsTamed.isBMAnd(ehr.getEntity(), tamed ->
						Objects.equals(tamed.getOwnerUUID(), player.getUUID())
						&& tamed.isCommandingItem(player.getItemInHand(usedHand))))
				{
					INFFGirlsTamed.ifBM(ehr.getEntity(), INFFGirlsTamed::switchAIState);
					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.SUCCESS);
				}
			});
		}
	}

}
