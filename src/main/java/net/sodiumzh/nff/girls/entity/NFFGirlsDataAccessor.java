package net.sodiumzh.nff.girls.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nff.services.entity.taming.NFFTamedDataAccessor;

import javax.annotation.Nonnull;
import java.util.UUID;

public class NFFGirlsDataAccessor extends NFFTamedDataAccessor {

    private static NFFGirlsDataAccessor INSTANCE = null;

    public static final String KEY_MAX_FAVORABILITY = "nffgirlsMaxFavorability";
    public static final String KEY_FAVORABILITY = "nffgirlsFavorability";
    public static final String KEY_XP = "nffgirlsXP";
    public static final UUID FAVORABILITY_ATK_MODIFIER_UUID = UUID.fromString("0e570979-9f96-4559-b31e-93500e69da07");
    public static final UUID XP_HP_MODIFIER_UUID = UUID.fromString("7d8887d5-9c50-41d0-af8f-542bd6426fa0");
    public static final UUID XP_ATK_MODIFIER_UUID = UUID.fromString("f95763ee-6981-4951-bf2b-dd35688b0364");

    protected INFFGirlsTamed girlsTamed;

    protected NFFGirlsDataAccessor(INFFGirlsTamed tamed) {
        super(tamed);
        this.girlsTamed = tamed;
    }

    public static NFFGirlsDataAccessor get(@Nonnull INFFGirlsTamed tamed) {
        if (INSTANCE == null) INSTANCE = new NFFGirlsDataAccessor(tamed);
        else {
            INSTANCE.girlsTamed = tamed;
        }
        return INSTANCE;
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

    public long getRequiredXPInThisLevel() {
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
            if (level < 32)
                return Math.round(2.5d * level * level- 40.5d * level + 360d);
            else
                return Math.round(4.5d * level * level - 162.5d * level + 2220d);
        }
    }

    /**
     * Get expected level for a given accumulated exp.
     * <p>
     * Inverse of {@link #getAccumulatedExpRequirement(int)}, computed in O(1) by solving the
     * quadratic of the matching segment:
     * <ul>
     *   <li>exp &lt; 352   (level &lt; 16):  L² + 6L = exp             → L = sqrt(exp + 9) - 3</li>
     *   <li>exp &lt; 1628  (level &lt; 32):  2.5L² - 40.5L + 360 = exp → L = (40.5 + sqrt(10·exp - 1959.75)) / 5</li>
     *   <li>otherwise      (level ≥ 32):     4.5L² - 162.5L + 2220 = exp → L = (162.5 + sqrt(18·exp - 13553.75)) / 9</li>
     * </ul>
     * The segment thresholds 352 and 1628 are {@code getAccumulatedExpRequirement(16)} and
     * {@code getAccumulatedExpRequirement(32)} respectively.
     */
    public static int getExpectedXPLevel(long exp)
    {
        if (exp < 0)
            throw new IllegalArgumentException("Illegal exp value");

        double expd = (double) exp;
        int level;
        if (exp < 352L)
            level = (int) Math.floor(Math.sqrt(expd + 9d) - 3d);
        else if (exp < 1628L)
            level = (int) Math.floor((40.5d + Math.sqrt(10d * expd - 1959.75d)) / 5d);
        else
            level = (int) Math.floor((162.5d + Math.sqrt(18d * expd - 13553.75d)) / 9d);

        // Guard against floating-point rounding at segment boundaries / exact level thresholds.
        // At most one step is ever needed in practice, so this stays O(1).
        while (level > 0 && getAccumulatedExpRequirement(level) > exp)
            --level;
        while (getAccumulatedExpRequirement(level + 1) <= exp)
            ++level;
        return level;
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
