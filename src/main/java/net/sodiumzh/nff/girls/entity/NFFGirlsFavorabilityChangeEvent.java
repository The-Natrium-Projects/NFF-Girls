package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Cancelable;
import net.sodiumzh.nfu.event.NFULivingEvent;
import org.checkerframework.checker.units.qual.C;

@Cancelable
public class NFFGirlsFavorabilityChangeEvent extends NFULivingEvent<Mob> {

    private final INFFGirlsTamed tamed;
    private final double oldValue;
    private double newValue;

    public NFFGirlsFavorabilityChangeEvent(INFFGirlsTamed mob, double fromVal, double toVal)
    {
        super(mob.asMob());
        this.tamed = mob;
        this.oldValue = fromVal;
        this.newValue = toVal;
    }

    public INFFGirlsTamed getTamed() {
        return tamed;
    }

    public double getOldValue() {
        return oldValue;
    }

    public double getNewValue() {
        return newValue;
    }

    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }
}
