package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nfu.event.NFULivingEvent;

public class NFFGirlsXPLevelChangeEvent extends NFULivingEvent<Mob> {

    public final INFFGirlsTamed tamed;
    public final int levelBefore;
    public final int levelAfter;
    private final NFFGirlsDataAccessor dataAccessor;

    public NFFGirlsXPLevelChangeEvent(INFFGirlsTamed tamed, int levelBefore, int levelAfter)
    {
        super(tamed.asMob());
        this.tamed = tamed;
        this.levelBefore = levelBefore;
        this.levelAfter = levelAfter;
        this.dataAccessor = tamed.getDataAccessor();
    }

    public NFFGirlsDataAccessor getDataAccessor() {
        return this.dataAccessor;
    }

}
