package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;

import static net.sodiumzh.nff.girls.registry.NFFGirlsItems.baubleAtk;
import static net.sodiumzh.nff.girls.registry.NFFGirlsItems.baubleHPMax;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirls.MOD_ID)
public class NFFGirlsItemTooltips {

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event)
    {
        if (event.getItemStack().is(ModItems.INSOMNIA_FRUIT.get()))
        {
            event.getToolTip().add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.existing_item").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.at_night").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(baubleHPMax(60d).get());
            event.getToolTip().add(baubleAtk(8d).get());
        }
    }

}
