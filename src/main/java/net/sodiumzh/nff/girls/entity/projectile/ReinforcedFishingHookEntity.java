package net.sodiumzh.nff.girls.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nfu.entity.projectile.AbstractFishingHookEntity;

public class ReinforcedFishingHookEntity extends AbstractFishingHookEntity
{

	protected double strengthItem = 0.4d;
	protected double strengthNonItem = 0.3d;
	protected int pullingCooldown = 6;
	protected int pullingCooldownTimer = 0;
	
	public ReinforcedFishingHookEntity(Player pPlayer, Level pLevel, int pLuck, int pLureSpeed)
	{
		super(pPlayer, pLevel, pLuck, pLureSpeed);
	}

	public ReinforcedFishingHookEntity(EntityType<? extends FishingHook> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public ReinforcedFishingHookEntity setStrength(double forItem, double forNonItem)
	{
		strengthItem = forItem;
		strengthNonItem = forNonItem;
		return this;
	}
		
	@Override
	public void pullEntity(Entity pEntity)
	{
		Entity entity = this.getOwner();
		if (entity != null && this.pullingCooldownTimer == 0)
		{
			Vec3 vec3 = (new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY(),
					entity.getZ() - this.getZ())).scale(pEntity instanceof ItemEntity ? strengthItem : strengthNonItem);
			pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(vec3));
			this.pullingCooldownTimer = this.pullingCooldown;
		}
	}
	
	@Override
	public int retrieve(ItemStack pStack) 
	{
		if (this.getHookedIn() != null)
		{
			this.discard();
			return 0;
		}
		else return super.retrieve(pStack);
	}
	
	@Override
	public void tick()
	{
		if (this.pullingCooldownTimer > 0)
			this.pullingCooldownTimer --;
		if (this.pullingCooldownTimer < 0)
			this.pullingCooldownTimer = 0;
		super.tick();
	}

	public boolean isInPullingCooldown()
	{
		return pullingCooldownTimer > 0;
	}

	@Override
	protected FishingRodItem getFishingRodType() {
		return NFFGirlsItems.REINFORCED_FISHING_ROD.get();
	}

}
