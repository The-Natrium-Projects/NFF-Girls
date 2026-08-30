package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
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
    public void drawAdditional(int recipeWidth, int recipeHeight, @NotNull PoseStack poseStack, double mouseX, double mouseY) {
        // Per-mob taming condition. Prefer a per-mob key named after the
        // displayed entity type ("jei.<entity namespace>.friending_condition.<entity path>"),
        // falling back to the shared per-table key
        // ("jei.<table namespace>.friending_condition.<table path>"), so mobs
        // that share one friending item table can still show their own text.
        // The friending page is 170px tall while the base sprite is 120px,
        // so the text is drawn in the bottom band, below the mob model and
        // the item grid, and never overlaps them.
        final int titleY = 118;
        final int textY = 128;
        if (itemTableKey.isValidated()
            && I18n.exists("jei.nffgirls.friending_condition.condition")) {
            ResourceLocation tableKey = itemTableKey.get();
            String tableDescKey = "jei." + tableKey.getNamespace() + ".friending_condition." + tableKey.getPath();
            ResourceLocation hostKey = ForgeRegistries.ENTITIES.getKey(entityType);
            String mobDescKey = hostKey != null
                ? "jei." + hostKey.getNamespace() + ".friending_condition." + hostKey.getPath() : null;
            String descKey = mobDescKey != null && I18n.exists(mobDescKey) ? mobDescKey : tableDescKey;
            Minecraft.getInstance().font.draw(poseStack,
                NFUInfoStatics.createTranslatable("jei.nffgirls.friending_condition.condition"),
                2, titleY, 8);
            if (I18n.exists(descKey)) {
                int lineY = textY;
                for (FormattedCharSequence line : Minecraft.getInstance().font.split(
                        NFUInfoStatics.createTranslatable(descKey), 160)) {
                    if (lineY >= recipeHeight - 6) break;
                    Minecraft.getInstance().font.draw(poseStack, line, 2, lineY, 8);
                    lineY += Minecraft.getInstance().font.lineHeight;
                }
            }
        }
        else {
            Minecraft.getInstance().font.drawWordWrap(
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
