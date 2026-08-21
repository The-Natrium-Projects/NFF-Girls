package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nfu.event.NFULivingEvent;

public class NFFGirlsXPChangeEvent extends NFULivingEvent<Mob> {

    public final INFFGirlsTamed tamed;
    public final long oldXP;
    private final NFFGirlsDataAccessor dataAccessor;
    public long newXP;

    public NFFGirlsXPChangeEvent(INFFGirlsTamed tamed, long oldXP, long newXP)
    {
        super(tamed.asMob());
        this.tamed = tamed;
        this.oldXP = oldXP;
        this.newXP = newXP;
        this.dataAccessor = tamed.getDataAccessor();
    }

    public NFFGirlsDataAccessor getDataAccessor() {
        return this.dataAccessor;
    }

}
