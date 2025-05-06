package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.util.NFUInfoStatics;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirls.MOD_ID)
public class NFFGirlsItemTooltips {

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event)
    {
        if (event.getItemStack().is(ModItems.INSOMNIA_FRUIT.get()))
        {
            event.getToolTip().add(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.existing_item").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.at_night").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(NFFGirlsItems.baubleHPMax(60d).get());
            event.getToolTip().add(NFFGirlsItems.baubleAtk(8d).get());
        }
    }

}
