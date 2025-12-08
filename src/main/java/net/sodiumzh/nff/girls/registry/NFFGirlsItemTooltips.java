package net.sodiumzh.nff.girls.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsBaubleBehavior;
import net.sodiumzh.nfu.object.Validatable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirls.MOD_ID)
public class NFFGirlsItemTooltips {

    private static final Validatable<Multimap<Item, NFFGirlsBaubleBehavior>> BAUBLE_BEHAVIOR_ACCESS = new Validatable<>(HashMultimap.create());

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event)
    {
        if (!BAUBLE_BEHAVIOR_ACCESS.isValidated())
            BAUBLE_BEHAVIOR_ACCESS.modifyAndValidate(m -> {
                NFFGirlsBaubles.BAUBLE_REGISTRY.keySet().stream().map(NFFGirlsBaubles.BAUBLE_REGISTRY::getValue)
                    .filter(b -> b instanceof NFFGirlsBaubleBehavior).map(b -> (NFFGirlsBaubleBehavior)b)
                    .forEach(b -> m.put(b.getItem(), b));
            });
        if (BAUBLE_BEHAVIOR_ACCESS.get().containsKey(event.getItemStack().getItem())) {
            BAUBLE_BEHAVIOR_ACCESS.get().get(event.getItemStack().getItem()).forEach(b ->
                b.getTooltips().forEach(c -> event.getToolTip().add(c.get())));
        }
        /*if (event.getItemStack().is(ModItems.INSOMNIA_FRUIT.get()))
        {
            event.getToolTip().add(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.existing_item").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.at_night").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(NFFGirlsBaubles.baubleHPMax(60d).get());
            event.getToolTip().add(NFFGirlsBaubles.baubleAtk(8d).get());
        }*/

    }

}
