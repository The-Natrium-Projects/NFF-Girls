package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.entity.RepeatableAttributeModifier;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.item.ColoredItems;
import net.sodiumzh.nfu.math.WithDyeColors;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;

public class HmagCursedDollTamingProcess extends NFFTamingProcess
{

	protected static final String PLAYER_TIMER_KEY_GIVING_COOLDOWN = "giving_cooldown";
	protected static final String PLAYER_KEY_PHASE = "phase";

	protected static final RepeatableAttributeModifier ATK_MOD = new RepeatableAttributeModifier(2.5d, new ResourceLocation(NFFGirls.MOD_ID, "cursed_doll_friending_atk"),
		AttributeModifier.Operation.ADDITION);

	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand) {
		NFFTamableComponent tamable = NFFTamableComponent.getOrDefault(mob);
		TamingInteractionResult result = TamingInteractionResult.unhandled(player.level());
		
		if (!player.level.isClientSide)
		{
			// In hatred, if try using acceptable item, send angry particles
			if (tamable.getAngerHandler().isAngryAt(player))
			{
				if (player.getMainHandItem().is(Items.STRING) || ColoredItems.WOOL_ITEMS.contains(player.getMainHandItem().getItem()))
				{
					NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
					result.setHandled();
				}
			}
			// In cooldown. Cooldown is added only after giving string.
			else if (tamable.hasPlayerTimer(player, PLAYER_TIMER_KEY_GIVING_COOLDOWN))
			{
				if (ColoredItems.WOOL_ITEMS.contains(player.getMainHandItem().getItem()))
				{
					NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob);
					result.setHandled();
				}
			}
			// Phase for giving a wool
			else if (!getPhase(mob, player))
			{
				if (ColoredItems.WOOL_ITEMS.contains(player.getMainHandItem().getItem()))
				{
					if (!hasWool(mob, player, ColoredItems.WOOL_ITEMS.getColor(player.getMainHandItem().getItem()).getName()))
					{
						setWool(mob, player, ColoredItems.WOOL_ITEMS.getColor(player.getMainHandItem().getItem()).getName(), true);
						setPhase(mob, player, true);
						player.getMainHandItem().shrink(1);
						NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
					}
					else
					{
						// Duplicate color, skip
						NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob);
					}
					result.setHandled();
				}
			}
			// Phase for giving a string
			else
			{
				if (player.getMainHandItem().is(Items.STRING))
				{
					player.getMainHandItem().shrink(1);
					// Sewed 8 times, succeed
					if (woolCount(mob, player) >= 8)
					{
						NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
						ATK_MOD.clear(player, Attributes.ATTACK_DAMAGE);
						result.setHandled();
						result.setTamedMob(this.doTaming(player, mob));
						return result;
					}
					else
					{
						tamable.putPlayerTimer(player, PLAYER_TIMER_KEY_GIVING_COOLDOWN, NFFGirlsTamingRules.COOLDOWN_MIDDLE);
						setPhase(mob, player, false);
						NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void serverTick(Mob mob)
	{
		MutableObject<Integer> strengthLevel = new MutableObject<>(0);
		this.forAllPlayersInProcess(mob, player -> 
		{
			int i = woolCount(mob, player);
			if (i > strengthLevel.getValue())
				strengthLevel.setValue(i);
		});
		ATK_MOD.apply(mob, Attributes.ATTACK_DAMAGE, strengthLevel.getValue());
	}

	@Override
	public void tamableInit(NFFTamableComponent cnffTamable) {

	}


	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		boolean inProcess = isInProcess(player, mob);
		NFFTamableComponent.getOrDefault(mob).getPlayerSpecificNBT(player).ifPresent(nbt -> nbt.remove( "phase"));
		for (DyeColor key: DyeColor.values())
			NFFTamableComponent.getOrDefault(mob).getPlayerSpecificNBT(player).ifPresent(nbt -> nbt.remove(key.getName()));
		if (inProcess && !isQuiet)
		{
			NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
		}
	}

	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet) {
		Set<UUID> nbtPlayers = NFFTamableComponent.getOrDefault(mob).getAllPlayersWithNBT();
		nbtPlayers.forEach(uuid -> {
			NFFTamableComponent.getOrDefault(mob).getPlayerSpecificNBT(uuid).ifPresent(nbt -> {
				nbt.remove( "phase");
				for (DyeColor key: DyeColor.values()) nbt.remove(key.getName());
			});
		});
		if (!nbtPlayers.isEmpty() && !isQuiet)
		{
			NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
		}
		return !nbtPlayers.isEmpty();
	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		if (!hasValidPlayerData(mob, player))
			return false;
		return woolCount(mob, player) > 0;
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		if (woolCount(mob, player) > 0 && damageGiven > NFFTamableComponent.getOrDefault(mob).getAngerHandler().getDamageThreshold())
		{
			List<String> holdingWools = getHoldingWools(mob, player);
			String droppedColor = holdingWools.get(mob.getRandom().nextInt(holdingWools.size()));
			mob.spawnAtLocation(new ItemStack(ColoredItems.WOOL_ITEMS.ofColor(DyeColor.byName(droppedColor, null))));
			setWool(mob, player, droppedColor, false);
			if (isInAnyProcess(mob))
				setPhase(mob, player, false);
			else interrupt(player, mob, true);
		}
	}

	// Utilities

	protected void createPlayerData(Mob mob, Player player)
	{
		if (hasValidPlayerData(mob, player))
			return;
		NFFTamableComponent cap = NFFTamableComponent.getOrDefault(mob);
		cap.getOrCreatePlayerSpecificNBT(player).putBoolean(PLAYER_KEY_PHASE, false);//.putBoolean("phase", false);	// False - requires wools; true - requires string
		for (DyeColor key: DyeColor.values())
			cap.getOrCreatePlayerSpecificNBT(player).putBoolean(key.getName(), false);
		
	}
	
	// Check if a player has data on this Cursed Doll with given format. It doesn't check the values.
	protected boolean hasValidPlayerData(Mob mob, Player player)
	{
		NFFTamableComponent cap = NFFTamableComponent.getOrDefault(mob);
		if (!cap.getPlayerSpecificNBT(player).map(nbt -> nbt.contains(PLAYER_KEY_PHASE, Tag.TAG_BYTE)).orElse(false))
			return false;
		for (DyeColor key: DyeColor.values())
		{
			if (!cap.getPlayerSpecificNBT(player).map(nbt -> nbt.contains(key.getName(), Tag.TAG_BYTE)).orElse(false))
				return false;
		}
		return true;
	}

	// Check if the Cursed Doll has wool of given color.
	protected boolean hasWool(Mob mob, Player player, String colorKey)
	{
		if (DyeColor.byName(colorKey, null) == null)
			throw new IllegalArgumentException("Invalid color key");
		if (!hasValidPlayerData(mob, player))
			return false;
		return NFFTamableComponent.getOrDefault(mob).getPlayerSpecificNBT(player).map(nbt -> nbt.getBoolean(colorKey)).orElse(false);
	}

	// Set if the mob has a wool of given color. Return if any actual operation is done.
	protected boolean setWool(Mob mob, Player player, String color, boolean value)
	{
		if (hasWool(mob, player, color) == value)
			return false;
		if (value && !hasValidPlayerData(mob, player))
			createPlayerData(mob, player);
		NFFTamableComponent.getOrDefault(mob).getOrCreatePlayerSpecificNBT(player).putBoolean(color, value);
		return true;
	}
	
	// Count how many wools it has for the player.
	protected int woolCount(Mob mob, Player player)
	{
		return (int) Arrays.stream(DyeColor.values()).filter(c -> this.hasWool(mob, player, c.getName())).count();
	}
	
	protected List<String> getHoldingWools(Mob mob, Player player)
	{
		return Arrays.stream(DyeColor.values()).map(DyeColor::getName).filter(key -> this.hasWool(mob, player, key)).toList();
	}
	
	// false - should give wool; true = should give string.
	protected boolean getPhase(Mob mob, Player player)
	{
		if (!hasValidPlayerData(mob, player))
			return false;
		return NFFTamableComponent.getOrDefault(mob).getPlayerSpecificNBT(player).map(nbt -> nbt.getBoolean(PLAYER_KEY_PHASE)).orElse(false);
	}
	
	protected void setPhase(Mob mob, Player player, boolean phase)
	{
		if (!isInProcess(player, mob))
			throw new UnsupportedOperationException("setPhase is only invokable when in process.");
		NFFTamableComponent.getOrDefault(mob).getOrCreatePlayerSpecificNBT(player).putBoolean(PLAYER_KEY_PHASE, phase);
	}
	
	protected void swapPhase(Mob mob, Player player)
	{
		setPhase(mob, player, !getPhase(mob, player));
	}
}
