package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiStatics;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HealingItemJeiMobEntry extends MobApplicableItemTableJeiMobEntry {

    public HealingItemJeiMobEntry(EntityType<? extends Mob> type) {
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
                if (NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().isValidated()) {
                    entries.setAndValidate(NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().get().get(entityType).getB());
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

    }

    @Override
    public boolean showCooldown() {
        return true;
    }

    @Override
    protected boolean showAmountAsPercentage() {
        return false;
    }
}
