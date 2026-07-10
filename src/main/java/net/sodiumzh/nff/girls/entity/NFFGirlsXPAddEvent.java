package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Cancelable;
import net.sodiumzh.nfu.event.NFULivingEvent;

@Cancelable
public class NFFGirlsXPAddEvent extends NFULivingEvent<Mob> {

    public final INFFGirlsTamed tamed;
    private long amount;
    private final NFFGirlsDataAccessor dataAccessor;

    public NFFGirlsXPAddEvent(INFFGirlsTamed tamed, long amount)
    {
        super(tamed.asMob());
        this.tamed = tamed;
        this.amount = amount;
        this.dataAccessor = tamed.getDataAccessor();
    }

    public NFFGirlsDataAccessor getDataAccessor() {
        return this.dataAccessor;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        if (amount < 0)
            throw new IllegalArgumentException("NFFGirls Level System: Negative exp value to add. If reducing exp is needed, use setExp().");
        this.amount = amount;
    }

}
