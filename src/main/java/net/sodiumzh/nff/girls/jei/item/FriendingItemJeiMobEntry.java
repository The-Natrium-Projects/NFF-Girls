package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
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
    public void drawAdditional(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // Per-mob taming condition, keyed by the friending item table:
        // "jei.<namespace>.friending_condition.<table path>".
        // The friending page is 170px tall while the base sprite is 120px,
        // so the text is drawn in the bottom band, below the mob model and
        // the item grid, and never overlaps them.
        final int titleY = 118;
        final int textY = 128;
        if (itemTableKey.isValidated()
            && I18n.exists("jei.nffgirls.friending_condition.condition")) {
            ResourceLocation key = itemTableKey.get();
            String descKey = "jei." + key.getNamespace() + ".friending_condition." + key.getPath();
            guiGraphics.drawString(Minecraft.getInstance().font,
                NFUInfoStatics.createTranslatable("jei.nffgirls.friending_condition.condition"),
                2, titleY, 8, false);
            if (I18n.exists(descKey)) {
                int lineY = textY;
                for (FormattedCharSequence line : Minecraft.getInstance().font.split(
                        Component.translatable(descKey), 160)) {
                    if (lineY >= recipeHeight - 6) break;
                    guiGraphics.drawString(Minecraft.getInstance().font, line, 2, lineY, 8, false);
                    lineY += Minecraft.getInstance().font.lineHeight;
                }
            }
        }
        else {
            guiGraphics.drawWordWrap(Minecraft.getInstance().font,
                NFUInfoStatics.createTranslatable("jei.nffgirls.friending_not_including_all"),
                2, titleY, 160, 8);
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
