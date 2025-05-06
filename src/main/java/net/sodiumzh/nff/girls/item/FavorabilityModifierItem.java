package net.sodiumzh.nff.girls.item;

import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nfu.util.NFUMiscStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import java.util.Optional;

public class FavorabilityModifierItem extends NFUItem
{

	public FavorabilityModifierItem(Item.Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	/**
	 * Get a initialized instance.
	 */
	public Optional<ItemStack> getDefaultInstanceOverride()
	{
		ItemStack res = new ItemStack(this);
		res.getOrCreateTag().putFloat("value", 1f);
		return Optional.of(res);
	}
	
	public float getValue(ItemStack stack)
	{
		if (!stack.getOrCreateTag().contains("value", Tag.TAG_FLOAT))
			stack.getOrCreateTag().putFloat("value", 1);
		return stack.getOrCreateTag().getFloat("value");
	}
	
	public void setValue(ItemStack stack, float value)
	{
		stack.getOrCreateTag().putFloat("value", value);
	}
	
    @Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
    	if (target.getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).isPresent())
    	{
    		target.getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap -> {
    			if (!player.level.isClientSide)
	    		{
	    			if (!player.isShiftKeyDown())
	    			{
	    				cap.addFavorability(this.getValue(player.getItemInHand(hand)));
	    				NFUMiscStatics.printToScreen(String.format("Mob [%s] favorability + %.0f.", target.getName().getString(), this.getValue(player.getItemInHand(hand))) , player);
	    				NFUMiscStatics.printToScreen(String.format("Current: %.2f", cap.getFavorability()) , player);
	    				NFUParticleStatics.sendGlintParticlesToEntityDefault(target);
	    			}
	    			else 
	    			{
	    				cap.addFavorability(-this.getValue(player.getItemInHand(hand)));
	    				NFUMiscStatics.printToScreen(String.format("Mob [%s] favorability - %.0f.", target.getName().getString(), this.getValue(player.getItemInHand(hand))) , player);
	    				NFUMiscStatics.printToScreen(String.format("Current: %.2f", cap.getFavorability()) , player);
	    				NFUParticleStatics.sendSmokeParticlesToEntityDefault(target);
	    			}
    			}
    		});
    		return InteractionResult.sidedSuccess(player.level.isClientSide);
    	}
    	return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
    	if (!level.isClientSide)
    	{
    		this.setValue(player.getItemInHand(hand), this.getValue(player.getItemInHand(hand)) < 2f ? 5f : 1f);
    		NFUMiscStatics.printToScreen(String.format("Favorability per operation: %.0f", this.getValue(player.getItemInHand(hand))), player);
    	}
    	return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
