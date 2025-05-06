package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.container.MapPair;
import net.sodiumzh.nfu.entity.RepeatableAttributeModifier;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class HmagCursedDollTamingProcess extends NFFTamingProcess
{

	protected static final String PLAYER_TIMER_KEY_GIVING_COOLDOWN = "giving_cooldown";
	protected static final String PLAYER_KEY_PHASE = "phase";

	protected static final RepeatableAttributeModifier ATK_MOD = new RepeatableAttributeModifier(2.5d, AttributeModifier.Operation.ADDITION);
	
	protected static final HashMap<String, Item> WOOL_MAP = NFUContainerStatics.mapOf(
			MapPair.of("white", Items.WHITE_WOOL),
			MapPair.of("light_gray", Items.LIGHT_GRAY_WOOL),
			MapPair.of("gray", Items.GRAY_WOOL),
			MapPair.of("black", Items.BLACK_WOOL),
			MapPair.of("red", Items.RED_WOOL),
			MapPair.of("green", Items.GREEN_WOOL),
			MapPair.of("blue", Items.BLUE_WOOL),
			MapPair.of("yellow", Items.YELLOW_WOOL),
			MapPair.of("magenta", Items.MAGENTA_WOOL),
			MapPair.of("cyan", Items.CYAN_WOOL),
			MapPair.of("orange", Items.ORANGE_WOOL),
			MapPair.of("brown", Items.BROWN_WOOL),
			MapPair.of("light_blue", Items.LIGHT_BLUE_WOOL),
			MapPair.of("lime", Items.LIME_WOOL),
			MapPair.of("pink", Items.PINK_WOOL),
			MapPair.of("purple", Items.PURPLE_WOOL));
	
	protected static final HashMap<Item, String> WOOL_MAP_REVERSE = new HashMap<>();
	
	static {
		WOOL_MAP.forEach((key, item) -> WOOL_MAP_REVERSE.put(item, key));
	}
	
	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand) {
		CNFFTamable cap = CNFFTamable.get(mob);
		TamingInteractionResult result = TamingInteractionResult.unhandled(player.level);
		
		if (!player.level.isClientSide)
		{
			// In hatred, if try using acceptable item, send angry particles
			if (cap.isAngryAt(player))
			{
				if (player.getMainHandItem().is(Items.STRING) || WOOL_MAP_REVERSE.containsKey(player.getMainHandItem().getItem()))
				{
					NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
					result.setHandled();
				}
			}
			// In cooldown. Cooldown is added only after giving string.
			else if (cap.hasPlayerTimer(player, PLAYER_TIMER_KEY_GIVING_COOLDOWN))
			{
				if (WOOL_MAP_REVERSE.containsKey(player.getMainHandItem().getItem()))
				{
					NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob);
					result.setHandled();
				}
			}
			// Phase for giving a wool
			else if (!getPhase(mob, player))
			{
				if (WOOL_MAP_REVERSE.containsKey(player.getMainHandItem().getItem()))
				{
					if (!hasWool(mob, player, WOOL_MAP_REVERSE.get(player.getMainHandItem().getItem())))
					{
						setWool(mob, player, WOOL_MAP_REVERSE.get(player.getMainHandItem().getItem()), true);
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
						cap.putPlayerTimer(player, PLAYER_TIMER_KEY_GIVING_COOLDOWN, NFFGirlsTamingRules.COOLDOWN_MIDDLE);
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
	public void tamableInit(CNFFTamable cnffTamable) {

	}


	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		boolean inProcess = isInProcess(player, mob);
		CNFFTamable.get(mob).getPlayerSpecificNBT(player).ifPresent(nbt -> nbt.remove( "phase"));
		for (String key: WOOL_MAP.keySet())
			CNFFTamable.get(mob).getPlayerSpecificNBT(player).ifPresent(nbt -> nbt.remove( key));
		if (inProcess && !isQuiet)
		{
			NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
		}
	}

	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet) {
		Set<UUID> nbtPlayers = CNFFTamable.get(mob).getAllPlayersWithNBT();
		nbtPlayers.forEach(uuid -> {
			CNFFTamable.get(mob).getPlayerSpecificNBT(uuid).ifPresent(nbt -> {
				nbt.remove( "phase");
				for (String key: WOOL_MAP.keySet()) nbt.remove(key);
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
	public MobAngerRules getAngerRules() {
		return MobAngerRules.ATTACKER_DAMAGED.get();
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		if (woolCount(mob, player) > 0 && damageGiven > CNFFTamable.get(mob).getDamageThreshold())
		{
			String droppedColor = NFUContainerStatics.randomPick(getHoldingWools(mob, player));
			mob.spawnAtLocation(new ItemStack(WOOL_MAP.get(droppedColor)));
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
		CNFFTamable cap = CNFFTamable.get(mob);
		cap.getOrCreatePlayerSpecificNBT(player).putBoolean(PLAYER_KEY_PHASE, false);//.putBoolean("phase", false);	// False - requires wools; true - requires string
		for (String key: WOOL_MAP.keySet())
			cap.getOrCreatePlayerSpecificNBT(player).putBoolean(key, false);
		
	}
	
	// Check if a player has data on this Cursed Doll with given format. It doesn't check the values.
	protected boolean hasValidPlayerData(Mob mob, Player player)
	{
		CNFFTamable cap = CNFFTamable.get(mob);
		if (!cap.getPlayerSpecificNBT(player).map(nbt -> nbt.contains(PLAYER_KEY_PHASE, Tag.TAG_BYTE)).orElse(false))
			return false;
		for (String key: WOOL_MAP.keySet())
		{
			if (!cap.getPlayerSpecificNBT(player).map(nbt -> nbt.contains(key, Tag.TAG_BYTE)).orElse(false))
				return false;
		}
		return true;
	}

	// Check if the Cursed Doll has wool of given color.
	protected boolean hasWool(Mob mob, Player player, String colorKey)
	{
		if (!WOOL_MAP.containsKey(colorKey))
			throw new IllegalArgumentException("Invalid color key");
		if (!hasValidPlayerData(mob, player))
			return false;
		return CNFFTamable.get(mob).getPlayerSpecificNBT(player).map(nbt -> nbt.getBoolean(colorKey)).orElse(false);
	}

	// Set if the mob has a wool of given color. Return if any actual operation is done.
	protected boolean setWool(Mob mob, Player player, String color, boolean value)
	{
		if (hasWool(mob, player, color) == value)
			return false;
		if (value && !hasValidPlayerData(mob, player))
			createPlayerData(mob, player);
		CNFFTamable.get(mob).getOrCreatePlayerSpecificNBT(player).putBoolean(color, value);
		return true;
	}
	
	// Count how many wools it has for the player.
	protected int woolCount(Mob mob, Player player)
	{
		int count = 0;
		for (String key: WOOL_MAP.keySet())
		{
			if (hasWool(mob, player, key))
				count++;
		}
		return count;
	}
	
	protected ArrayList<String> getHoldingWools(Mob mob, Player player)
	{
		ArrayList<String> list = new ArrayList<>();
		for (String key: WOOL_MAP.keySet())
		{
			if (hasWool(mob, player, key))
				list.add(key);
		}
		return list;
	}
	
	// false - should give wool; true = should give string.
	protected boolean getPhase(Mob mob, Player player)
	{
		if (!hasValidPlayerData(mob, player))
			return false;
		return CNFFTamable.get(mob).getPlayerSpecificNBT(player).map(nbt -> nbt.getBoolean(PLAYER_KEY_PHASE)).orElse(false);
	}
	
	protected void setPhase(Mob mob, Player player, boolean phase)
	{
		if (!isInProcess(player, mob))
			throw new UnsupportedOperationException("setPhase is only invokable when in process.");
		CNFFTamable.get(mob).getOrCreatePlayerSpecificNBT(player).putBoolean(PLAYER_KEY_PHASE, phase);
	}
	
	protected void swapPhase(Mob mob, Player player)
	{
		setPhase(mob, player, !getPhase(mob, player));
	}
}
