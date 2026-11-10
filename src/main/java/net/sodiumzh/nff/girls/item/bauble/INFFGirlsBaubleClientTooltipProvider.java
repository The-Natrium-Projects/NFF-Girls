package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nfu.object.ClientOnly;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@OnlyIn(Dist.CLIENT)
public class INFFGirlsBaubleClientTooltipProvider implements Supplier<List<Component>> {

    public static final ClientOnly<Map<Item, List<Component>>> TOOLTIPS
        = new ClientOnly<>(new HashMap<>());

    private static final Component DESC_DEDICATED_BAUBLE
        = NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.dedicated_item").withStyle(ChatFormatting.WHITE);
    private static final Component DESC_ALSO_FOR_BAUBLE =
        NFUInfoStatics.createTranslatable("info.nffgirls.bauble.existing_item").withStyle(ChatFormatting.WHITE);

    private Type type;
    private int rarity;
    List<Component> desc;

    @Override
    public List<Component> get() {
        List<Component> res = new ArrayList<>();
        switch (type) {
            case DEDICATED_ITEM -> {
                res.add(DESC_DEDICATED_BAUBLE);
                res.add(getRarityDescription());
                break;
            }
            case BEHAVIORS -> {
                res.add(DESC_ALSO_FOR_BAUBLE);
                break;
            }
            default -> throw new RuntimeException();
        }
        res.addAll(desc);
        return res;
    }

    private Component getRarityDescription() {
        return NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", this.rarity)
            .withStyle(INFFGirlsBauble.getRarityStyle(rarity));
    }

    public enum Type {
        DEDICATED_ITEM, BEHAVIORS
    }

}
