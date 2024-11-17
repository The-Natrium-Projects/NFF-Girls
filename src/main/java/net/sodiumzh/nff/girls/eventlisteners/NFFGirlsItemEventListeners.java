package net.sodiumzh.nff.girls.eventlisteners;

import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.mixin.events.entity.ItemEntityHurtEvent;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.item.IWithDuration;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.eventlisteners.ServerEntityTickEvent;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.item.event.NFFMobRespawnerConstructEvent;
import net.sodiumzh.nff.services.item.event.NFFMobRespawnerStartRespawnEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsItemEventListeners
{
	@SubscribeEvent
	public static void beforeRespawnerConstruct(NFFMobRespawnerConstructEvent.Before event)
	{
	}
	
	@SubscribeEvent
	public static void afterRespawnerConstruct(NFFMobRespawnerConstructEvent.After event)
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
			NFFMobRespawnerInstance ins = NFFMobRespawnerInstance.create(event.getItem().getItem());
				// If the mob isn't a nffgirls befriended mob, it should not have this uuid
			if (!ins.getOwnerUUID().equals(event.getEntity().getUUID()))
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
		if (!event.getRespawner().getOwnerUUID().equals(event.getPlayer().getUUID()))
		{
			event.setCanceled(true);
		}
	}

	/** @deprecated Use {@link INFFGirlsTamed#getSharpnessModifierUUID} instead */
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
	public static void onServerItemEntityTick(ServerEntityTickEvent.PreWorldTick event)
	{
		if (event.getEntity() instanceof ItemEntity ie)
		{
			if (ie.getItem().getTag() != null 
					&& ie.getItem().getTag().contains("already_picked_befriendable_mobs", NaUtilsNBTStatics.TAG_COMPOUND_ID))
			{
				for (String key: ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getAllKeys())
				{
					if (ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) > 0)
					{
						ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").putInt(key, 
								ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) - 1);
					}
					if (ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").getInt(key) == 0)
					{
						ie.getItem().getTag().getCompound("already_picked_befriendable_mobs").remove(key);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemEntityHurt(ItemEntityHurtEvent event)
	{
		if (event.damageSource.getEntity() != null && event.damageSource.getEntity() instanceof INFFGirlsTamed)
			event.setCanceled(true);
	}
}
