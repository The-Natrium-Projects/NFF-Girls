package net.sodiumzh.nff.girls.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.entity.taming.NFFTamedDataAccessor;
import net.sodiumzh.nfu.entity.RepeatableAttributeModifier;

import java.util.UUID;

public class NFFGirlsDataAccessor extends NFFTamedDataAccessor {

    private final INFFGirlsTamed girlsTamed;
    public static final String KEY_MAX_FAVORABILITY = "nffgirlsMaxFavorability";
    public static final String KEY_FAVORABILITY = "nffgirlsFavorability";
    public static final String KEY_XP = "nffgirlsXP";
    public static final UUID FAVORABILITY_ATK_MODIFIER_UUID = UUID.fromString("0e570979-9f96-4559-b31e-93500e69da07");
    public static final UUID XP_HP_MODIFIER_UUID = UUID.fromString("7d8887d5-9c50-41d0-af8f-542bd6426fa0");
    public static final UUID XP_ATK_MODIFIER_UUID = UUID.fromString("f95763ee-6981-4951-bf2b-dd35688b0364");

    public NFFGirlsDataAccessor(INFFGirlsTamed tamed) {
        super(tamed);
        this.girlsTamed = tamed;
    }

    @Override
    public INFFGirlsTamed asTamed() {
        return girlsTamed;
    }

    public double getFavorability() {
        return this.getSynchedData(KEY_FAVORABILITY, Double.class).orElse(50d);
    }

    public void setFavorability(double value) {
        double actualValue = Mth.clamp(value, 0, getMaxFavorability());
        double current = this.getFavorability();
        if (Math.abs(value - current) < 1e-12)
            return;
        var event = new NFFGirlsFavorabilityChangeEvent(this.asTamed(), current, actualValue);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            this.setSynchedData(KEY_FAVORABILITY, Double.class, event.getNewValue());
        }
    }

    public void addFavorability(double deltaValue) {
        this.setFavorability(this.getFavorability() + deltaValue);
    }

    public double getMaxFavorability() {
        return this.getSynchedData(KEY_MAX_FAVORABILITY, Double.class).orElse(100d);
    }

    public void setMaxFavorability(double value) {
        double old = this.getMaxFavorability();
        double oldFav = this.getFavorability();
        if (Math.abs(value - old) < 1e-12d)
            return;
        this.setSynchedData(KEY_MAX_FAVORABILITY, Double.class, value);
        this.setSynchedData(KEY_FAVORABILITY, Double.class, oldFav * value / old);
    }


    public long getXP() {
        return this.getSynchedData(KEY_XP, Long.class).orElse(50L);
    }

    public void setXP(long val) {
        if (val < 0)
            throw new IllegalArgumentException("NFFGirls Level System: Illegal exp value (negative).");
        if (val == this.getXP())
            return;
        NFFGirlsXPChangeEvent event = new NFFGirlsXPChangeEvent(this.asTamed(), getXP(), val);
        boolean canceled = MinecraftForge.EVENT_BUS.post(event);
        if (!canceled) {
            int lvlOld = getExpectedXPLevel();
            this.setSynchedData(KEY_XP, Long.class, event.newXP);
            int lvl = getExpectedXPLevel();
            if (lvlOld != lvl)
                MinecraftForge.EVENT_BUS.post(new NFFGirlsXPLevelChangeEvent(this.asTamed(), lvlOld, lvl));
        }
    }

    public void addXP(long deltaVal) {
        if (deltaVal < 0)
            throw new IllegalArgumentException("NFFGirls Level System: Negative exp value to add. If reducing exp is needed, use setExp().");
        if (deltaVal == 0)
            return;
        NFFGirlsXPAddEvent event = new NFFGirlsXPAddEvent(this.asTamed(), deltaVal);
        boolean canceled = MinecraftForge.EVENT_BUS.post(event);
        if (!canceled && event.getAmount() > 0)
        {
            this.setXP(this.getXP() + event.getAmount());
        }
    }

    public int getExpectedXPLevel() {
        return getExpectedXPLevel(this.getXP());
    }

    public long getXPInThisLevel() {
        return getCurrentExp(this.getXP());
    }

    public long getRequiredExpInThisLevel() {
        return getExpRequiredForLevelUp(getExpectedXPLevel());
    }

    public boolean isLowFavorability() {
        return this.getFavorability() < 5d;
    }

    public static boolean isLowFavorability(Mob mob) {
        return INFFGirlsTamed.get(mob).map(t -> t.getDataAccessor().isLowFavorability()).orElse(false);
    }

    // =========================== //
    // Related constants / statics //
    // =========================== //

    /**
     * Get ACCUMULATED exp for reaching this level.
     * Identical to player exp table
     */
    public static long getAccumulatedExpRequirement(int level)
    {
        if (level < 0)
            throw new IllegalArgumentException("Illegal level value");
        else if (level < 16)
            return level * level + level * 6;
        else
        {
            double leveld = (double)level;
            if (level < 32)
                return Math.round(2.5d * leveld * leveld - 40.5d * leveld + 360d);
            else
                return Math.round(4.5d * leveld * leveld - 162.5 * leveld + 2220d);
        }
    }

    /**
     * Get expected level for a given accumulated exp.
     */
    public static int getExpectedXPLevel(long exp)
    {
        if (exp < 0)
            throw new IllegalArgumentException("Illegal exp value");
        // TODO Need this awkward algorithm be optimized?
        int i = 0;
        while (getAccumulatedExpRequirement(i) <= exp)
        {
            ++i;
        }
        return i - 1;
    }

    /**
     * Get exp under this level, i.e. (accumulated exp) - (exp required to reach this level)
     */
    public static long getCurrentExp(long accumulatedExp)
    {
        return accumulatedExp - getAccumulatedExpRequirement(getExpectedXPLevel(accumulatedExp));
    }

    public static long getExpRequiredForLevelUp(int levelNow)
    {
        return getAccumulatedExpRequirement(levelNow + 1) - getAccumulatedExpRequirement(levelNow);
    }

}
