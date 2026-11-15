package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/**
 * Client-side bauble listeners. As the NFU bauble registry is available on both sides,
 * tooltips are built directly from the client-side registry entries.
 */
@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class NFFGirlsBaubleEventListeners {

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event) {
        Map<ResourceLocation, INFFGirlsBauble> baubles = INFFGirlsBauble.asBauble(event.getItemStack());
        if (baubles.isEmpty())
            return;
        List<Component> tooltip = event.getToolTip();
        // Header (and rarity for dedicated baubles), added once per stack
        if (event.getItemStack().getItem() instanceof NFFGirlsDedicatedBaubleItem dedicated) {
            tooltip.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.dedicated_item").withStyle(ChatFormatting.WHITE));
            tooltip.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", dedicated.getRarityTier())
                .withStyle(INFFGirlsBauble.getRarityStyle(dedicated.getRarityTier())));
        } else {
            tooltip.add(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.existing_item").withStyle(ChatFormatting.WHITE));
        }
        baubles.values().forEach(bauble -> tooltip.addAll(bauble.getProperties().createTooltips()));
    }

}
