package net.sodiumzh.nff.girls.item.bauble;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nfu.function.NFUOperator;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.item.bauble.BaubleRecord;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFUDataStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility methods for querying how many baubles (from {@link NFFGirlsBaubleAdditionalRegistry}) a mob
 * currently has equipped, filtered by category name, tier, or a custom condition.
 */
public class NFFGirlsBaubleStatics
{

	/**
	 * Count how many bauble of given name the mob has within the tier range.
	 */
	public static int countBaublesWithTierRange(Mob mob, ResourceLocation key, int minTier, int maxTierExcluding)
	{
		Collection<ItemStack> equipped = NFUBaubleAPI.getAllSlotItems(mob).values();
		int count = 0;
		for (ItemStack stack: equipped)
		{
			boolean is = NFUBaubleAPI.getAllRelatedEntries(stack).stream().anyMatch(entry ->
				entry instanceof INFFGirlsBauble bauble
					&& bauble.getCategoryKey().equals(key)
					&& bauble.getTier() >= minTier && bauble.getTier() < maxTierExcluding);
			if (is) count++;
		}
		return count;
	}
	
	/**
	 * Count how many bauble of given name the mob has with the minimum tier.
	 */
	public static int countBaublesWithMinTier(Mob mob, ResourceLocation key, int minTier)
	{
		return countBaublesWithTierRange(mob, key, minTier, Integer.MAX_VALUE);
	}
	
	/**
	 * Count how many bauble of given name the mob has with the exact tier.
	 */
	public static int countBaublesWithTier(Mob mob, ResourceLocation key, int tier)
	{
		return countBaublesWithTierRange(mob, key, tier, tier + 1);
	}

	/**
	 * Count how many bauble of given name the mob has with the exact tier.
	 */
	public static int countBaublesWithCondition(Mob mob, Predicate<ItemStack> condition)
	{
		return NFUBaubleAPI.getAllSlotItems(mob).values().stream().filter(condition).toList().size();
	}

	/**
	 * Count how many bauble of given name the mob has, despite the tier.
	 */
	public static int countBaubles(Mob mob, ResourceLocation key)
	{
		return countBaublesWithMinTier(mob, key, 0); 
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleTooltipSync(ClientboundNFFGirlsBaubleTooltipSyncPacket packet, ClientGamePacketListener listener) {
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		INFFGirlsBaubleClientTooltipProvider.TOOLTIPS.get().clear();
		packet.tooltipMap.forEach((key, value) -> {
			INFFGirlsBaubleClientTooltipProvider.TOOLTIPS.get().put(ForgeRegistries.ITEMS.getValue(key), value);
		});
	}

	@Nullable
	static NFFGirlsBaubleProperties.IProperty readProperty(JsonElement element) {
		if (!element.isJsonObject()) return null;
		JsonObject data = element.getAsJsonObject();
		try {
			String type = data.get("type").getAsString();
			if ("modifier".equals(type) || "attribute_modifier".equals(type)) {
				boolean repeatable = NFUDataStatics.getOptional(data, "repeatable", JsonElement::getAsBoolean).orElse(false);
				Attribute attribute = Objects.requireNonNull(
					ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(data.get("attribute").getAsString())));
				double amount = data.get("amount").getAsDouble();
				AttributeModifier.Operation operation = Objects.requireNonNull(modifierOperationByJson(data.get("operation")));
				@Nullable ResourceLocation unrepeatableIdentifier = NFUDataStatics.getOptional(data, "unrepeatable_identifier", JsonElement::getAsString)
					.map(ResourceLocation::new).orElse(null);
				@Nullable String groupIdentifier = NFUDataStatics.getOptional(data, "group_identifier", JsonElement::getAsString).orElse(null);
				@Nullable Function<BaubleProcessingArgs, Boolean> applyingCondition =
					NFUDataStatics.getOptional(data, "applying_condition", je -> je)
						.map(je -> NFUOperator.getOperatorAsFunctionByData(je, BaubleProcessingArgs.class, Boolean.class))
						.orElse(null);
				return new NFFGirlsBaubleProperties.ModifierProperty(repeatable, attribute, amount, operation, unrepeatableIdentifier, groupIdentifier,
					applyingCondition != null ? applyingCondition::apply : null);
			}
			else if ("tag".equals(type)) {
				String  name = Objects.requireNonNull(data.get("name").getAsString());
				Component desc = null;
				if (data.get("description").isJsonPrimitive() && data.get("description").getAsJsonPrimitive().isString()) {
					desc = NFUInfoStatics.createTranslatable(data.get("description").getAsString()).withStyle(ChatFormatting.WHITE);
				}
				else if (data.get("description") != null) {
					try {
						desc = Component.Serializer.fromJson(data.get("description"));
					} catch (JsonParseException exception) {
						LogUtils.getLogger().error("Description parse failed.", exception);
					}
				}
				if (desc == null)
					desc = NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.tag." + name).withStyle(ChatFormatting.WHITE);
				@Nullable Function<BaubleProcessingArgs, Boolean> applyingCondition =
					NFUDataStatics.getOptional(data, "applying_condition", je -> je)
						.map(je -> NFUOperator.getOperatorAsFunctionByData(je, BaubleProcessingArgs.class, Boolean.class))
						.orElse(null);
				return new NFFGirlsBaubleProperties.TagProperty(name, desc, applyingCondition != null ? applyingCondition::apply : null);
			}
			else if ("environment_resistance".equals(type)) {
				@Nullable Function<BaubleProcessingArgs, Boolean> applyingCondition =
					NFUDataStatics.getOptional(data, "applying_condition", je -> je)
						.map(je -> NFUOperator.getOperatorAsFunctionByData(je, BaubleProcessingArgs.class, Boolean.class))
						.orElse(null);
				return new NFFGirlsBaubleProperties.EnvironmentResistanceProperty(applyingCondition != null ? applyingCondition::apply : null);
			}
			else if ("tick".equals(type) || "tick_action".equals(type)) {
				Function<BaubleProcessingArgs, Object> action =
					NFUDataStatics.getOptional(data, "action", je -> je)
						.map(je -> NFUOperator.getOperatorAsFunctionByData(je, BaubleProcessingArgs.class, Object.class))
						.orElseThrow();
				@Nullable String name = NFUDataStatics.getOptional(data, "name", JsonElement::getAsString).orElse(null);
				Component desc = null;
				if (data.get("description").isJsonPrimitive() && data.get("description").getAsJsonPrimitive().isString()) {
					desc = NFUInfoStatics.createTranslatable(data.get("description").getAsString()).withStyle(ChatFormatting.WHITE);
				}
				else if (data.get("description") != null) {
					try {
						desc = Component.Serializer.fromJson(data.get("description"));
					} catch (JsonParseException exception) {
						LogUtils.getLogger().error("Description parse failed.", exception);
					}
				}
				if (desc == null) {
					if (name != null)
						desc = NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.tick_action." + name).withStyle(ChatFormatting.WHITE);
					else throw new JsonParseException("Missing tick-action name and description. Must not be both absent.");
				}
				@Nullable Function<BaubleProcessingArgs, Boolean> applyingCondition =
					NFUDataStatics.getOptional(data, "applying_condition", je -> je)
						.map(je -> NFUOperator.getOperatorAsFunctionByData(je, BaubleProcessingArgs.class, Boolean.class))
						.orElse(null);
				return new NFFGirlsBaubleProperties.TickActionProperty(action::apply,
					desc, name, applyingCondition != null ? applyingCondition::apply : null);
			}
			return null;
		} catch (RuntimeException ex) {
			LogUtils.getLogger().error("Read NFF-Girls Bauble property failed. Skipped.", ex);
			return null;
		}
	}

	@Nullable
	private static AttributeModifier.Operation modifierOperationByJson(JsonElement json) {
		if (!json.isJsonPrimitive()) return null;
		if (json.getAsJsonPrimitive().isString()) {
			switch (json.getAsJsonPrimitive().getAsString()) {
				case "add", "addition" -> {return AttributeModifier.Operation.ADDITION;}
				case "multiply", "multiply_base" -> {return AttributeModifier.Operation.MULTIPLY_BASE;}
				case "multiply_total" -> {return AttributeModifier.Operation.MULTIPLY_TOTAL;}
				default -> {return null;}
			}
		}
		else if (json.getAsJsonPrimitive().isNumber()) {
			try {
			return AttributeModifier.Operation.fromValue(json.getAsJsonPrimitive().getAsInt());
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}
}
