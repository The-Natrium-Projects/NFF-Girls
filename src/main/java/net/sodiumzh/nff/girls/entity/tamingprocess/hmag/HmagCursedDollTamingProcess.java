package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.mutable.MutableObject;

import net.minecraft.nbt.ByteTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsParticleStatics;
import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.entity.RepeatableAttributeModifier;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;

public class HmagCursedDollTamingProcess extends NFFTamingProcess
{

	protected static final RepeatableAttributeModifier ATK_MOD = new RepeatableAttributeModifier(2.5d, AttributeModifier.Operation.ADDITION);
	
	protected static final HashMap<String, Item> WOOL_MAP = NaUtilsContainerStatics.mapOf(
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
	public TamableInteractionResult handleInteract(TamableInteractArguments args) {
		Mob mob = args.getTarget();
		CNFFTamable cap = CNFFTamable.getCap(mob);
		Player player = args.getPlayer();
		TamableInteractionResult result = new TamableInteractionResult();
		
		if (!args.isClient())
		{
			// In hatred, if try using acceptable item, send angry particles
			if (cap.isInHatred(player))
			{
				if (player.getMainHandItem().is(Items.STRING) || WOOL_MAP_REVERSE.containsKey(player.getMainHandItem().getItem()))
				{
					NaUtilsParticleStatics.sendAngryParticlesToEntityDefault(mob);
					result.setHandled();
				}
			}
			// In cooldown. Cooldown is added only after giving string.
			else if (cap.hasPlayerTimer(player, "giving_cooldown"))
			{
				if (WOOL_MAP_REVERSE.containsKey(player.getMainHandItem().getItem()))
				{
					NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(mob);
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
						NaUtilsParticleStatics.sendGlintParticlesToEntityDefault(mob);
					}
					else
					{
						// Duplicate color, skip
						NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(mob);
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
						NaUtilsParticleStatics.sendHeartParticlesToEntityDefault(mob);
						ATK_MOD.clear(player, Attributes.ATTACK_DAMAGE);
						result.setHandled();
						result.nFFTamed = doTaming(player, mob);
						return result;
					}
					else
					{
						cap.setPlayerTimer(player, "giving_cooldown", 10 * 20);
						setPhase(mob, player, false);
						NaUtilsParticleStatics.sendGlintParticlesToEntityDefault(mob);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void serverTick(Mob mob)
	{
		super.serverTick(mob);
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
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		boolean inProcess = isInProcess(player, mob);
		CNFFTamable.getCap(mob).removePlayerData(player, "phase");
		for (String key: WOOL_MAP.keySet())
			CNFFTamable.getCap(mob).removePlayerData(player, key);
		if (inProcess && !isQuiet)
		{
			NaUtilsParticleStatics.sendAngryParticlesToEntityDefault(mob);
		}
	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		if (!hasValidPlayerData(mob, player))
			return false;
		return woolCount(mob, player) > 0;
	}

	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		return NaUtilsContainerStatics.setOf(TamableHatredReason.ATTACKED);
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		if (woolCount(mob, player) > 0 && damageGiven)
		{
			String droppedColor = NaUtilsContainerStatics.randomPick(getHoldingWools(mob, player));
			mob.spawnAtLocation(new ItemStack(WOOL_MAP.get(droppedColor)));
			setWool(mob, player, droppedColor, false);
			if (isInProcess(mob))
				setPhase(mob, player, false);
			else interrupt(player, mob, true);
		}
	}

	// Utilities

	protected void createPlayerData(Mob mob, Player player)
	{
		if (hasValidPlayerData(mob, player))
			return;
		CNFFTamable cap = CNFFTamable.getCap(mob);
		cap.putPlayerData(ByteTag.valueOf(false), player, "phase");//.putBoolean("phase", false);	// False - requires wools; true - requires string
		for (String key: WOOL_MAP.keySet())
			cap.putPlayerData(ByteTag.valueOf(false), player, key);
		
	}
	
	// Check if a player has data on this Cursed Doll with given format. It doesn't check the values.
	protected boolean hasValidPlayerData(Mob mob, Player player)
	{
		var cap = CNFFTamable.getCap(mob);
		if (!(cap.hasPlayerData(player, "phase") && cap.getPlayerData(player, "phase") instanceof ByteTag))
			return false;
		for (String key: WOOL_MAP.keySet())
		{
			if (!(cap.hasPlayerData(player, key) && cap.getPlayerData(player, key) instanceof ByteTag))
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
		return CNFFTamable.getCap(mob).hasPlayerData(player, colorKey) && (((ByteTag)CNFFTamable.getCap(mob).getPlayerData(player, colorKey)).getAsByte() > 0);
	}

	// Set if the mob has a wool of given color. Return if any actual operation is done.
	protected boolean setWool(Mob mob, Player player, String color, boolean value)
	{
		if (hasWool(mob, player, color) == value)
			return false;
		if (value && !hasValidPlayerData(mob, player))
			createPlayerData(mob, player);
		CNFFTamable.getCap(mob).putPlayerData(ByteTag.valueOf(value), player, color);
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
	
	// False - 
	protected boolean getPhase(Mob mob, Player player)
	{
		if (!hasValidPlayerData(mob, player))
			return false;
		return ((ByteTag)CNFFTamable.getCap(mob).getPlayerData(player, "phase")).getAsByte() > 0;
	}
	
	protected void setPhase(Mob mob, Player player, boolean phase)
	{
		if (!isInProcess(player, mob))
			throw new UnsupportedOperationException("setPhase is only invokable when in process.");
		CNFFTamable.getCap(mob).putPlayerData(ByteTag.valueOf(phase), player, "phase");
	}
	
	protected void swapPhase(Mob mob, Player player)
	{
		setPhase(mob, player, !getPhase(mob, player));
	}
}
