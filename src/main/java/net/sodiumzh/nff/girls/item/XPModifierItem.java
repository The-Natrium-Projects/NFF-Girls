package net.sodiumzh.nff.girls.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;

public class XPModifierItem extends Item
{

	public XPModifierItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack res = new ItemStack(this);
		res.getOrCreateTag().putInt("value", 1);
		return res;
	}
	
	public int getValue(ItemStack stack)
	{
		if (!stack.getOrCreateTag().contains("value", NaUtilsNBTStatics.TAG_INT_ID))
			stack.getOrCreateTag().putInt("value", 1);
		return stack.getOrCreateTag().getInt("value");
	}
	
	public void setValue(ItemStack stack, int value)
	{
		stack.getOrCreateTag().putInt("value", value);
	}
	
    @Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
    	if (target.getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).isPresent())
    	{
    		target.getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap -> {
    			if (!player.level.isClientSide)
	    		{
	    			if (!player.isShiftKeyDown())
	    			{
	    				cap.setExp(cap.getExp() + this.getValue(stack));
	    				NaUtilsMiscStatics.printToScreen("Mob [" + target.getName().getString() + "] EXP +" + this.getValue(stack) + "." , player);
	    				NaUtilsMiscStatics.printToScreen("Current: Lv " + Integer.toString(cap.getExpectedLevel()) + ", EXP " + Long.toString(cap.getExpInThisLevel()) , player);
	    				NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(target);
	    			}
	    			else 
	    			{
	    				cap.setExp(Math.max(0, cap.getExp() - this.getValue(stack)));
	    				NaUtilsMiscStatics.printToScreen("Mob [" + target.getName().getString() + "] EXP -" + this.getValue(stack) + "." , player);
	    				NaUtilsMiscStatics.printToScreen("Current: Lv " + Integer.toString(cap.getExpectedLevel()) + ", EXP " + Long.toString(cap.getExpInThisLevel()) , player);
	    				NaUtilsEntityStatics.sendSmokeParticlesToLivingDefault(target);
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
    		if (!player.isShiftKeyDown())
    		{
    			if (this.getValue(player.getItemInHand(hand)) < (1 << 20)/* 2^20 */)
    					this.setValue(player.getItemInHand(hand), this.getValue(player.getItemInHand(hand)) * 4);
    			else this.setValue(player.getItemInHand(hand), 1);
    		}
    		else
    		{
    			if (this.getValue(player.getItemInHand(hand)) > 1)
					this.setValue(player.getItemInHand(hand), this.getValue(player.getItemInHand(hand)) / 4);
    			else this.setValue(player.getItemInHand(hand), 1 << 20 /* 2^20 */);
    		}
    		NaUtilsMiscStatics.printToScreen("EXP per operation: " + Integer.toString(this.getValue(player.getItemInHand(hand))), player);
    	}
    	return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
	
}
