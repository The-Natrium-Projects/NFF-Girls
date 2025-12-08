package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiStatics;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class FriendingItemJeiMobEntry extends MobApplicableItemTableJeiMobEntry {

    protected Validatable<ResourceLocation> itemTableKey = new Validatable<>();

    public FriendingItemJeiMobEntry(EntityType<? extends Mob> type) {
        super(type);
    }

    @Override
    public void tryInitialize() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (!mobInstance.isValidated()) {
                Optional.ofNullable(mc.level).map(entityType::create).ifPresent(mobInstance::setAndValidate);
            }
            if (!entries.isValidated()) {
                if (NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().isValidated()) {
                    itemTableKey.setAndValidate(NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().get().get(entityType).getA());
                    entries.setAndValidate(NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().get().get(entityType).getB());
                    mergeEntries();
                }
                else Optional.ofNullable(mc.player).ifPresent(NFFGirlsJeiStatics::requestJeiDataSync);
            }
        } catch (RuntimeException e) {
            NFUDebugStatics.errorOnce(MobApplicableItemTableJeiMobEntry.class, "Initialization error for type "
                + entityType.getDescriptionId() + "\n" + e.getMessage());
        }
    }

    @Override
    public void drawAdditional(int recipeWidth, int recipeHeight, @NotNull PoseStack guiGraphics, double mouseX, double mouseY) {
       /* if (itemTableKey.isValidated()) {
            String descKey = "jei." + itemTableKey.get().getNamespace() +
                ".friending_condition." + itemTableKey.get().getPath();
            guiGraphics.drawString(Minecraft.getInstance().font, ComponentBuilder.create()
                    .appendTranslatable("jei.nffgirls.friending_condition.condition")
                    .appendText(descKey).build(),
                2, 14, 8, false);
        }*/
        List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(
            NFUInfoStatics.createTranslatable("jei.nffgirls.friending_not_including_all"), 158
        );
        for (int i = 0; i < lines.size(); i++) {
            Minecraft.getInstance().font.draw(guiGraphics, lines.get(i), 2, 102 + i * 9, 8);
        }
    }

    @Override
    public boolean showCooldown() {
        return false;
    }

    @Override
    protected boolean showAmountAsPercentage() {
        return true;
    }

}
