package net.sodiumzh.nff.girls.entity.ai.movecontrol;

import com.github.mechalopa.hmag.util.ModUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

// From Mechalopa's code
// Use INFFGirlsFlyingMob.MoveControl instead
@Deprecated
public class NFFGirlsHmagFlyingMoveControl extends MoveControl
{
	
	protected final INFFTamed mob;
	public NFFGirlsHmagFlyingMoveControl(INFFTamed mob)
	{
		super(mob.asMob());
		this.mob = mob;
	}

	@Override
	public void tick()
	{
		Mob flyingentity= this.mob.asMob();

		if (this.operation == MoveControl.Operation.MOVE_TO)
		{
            Vec3 vec3 = new Vec3(this.wantedX - flyingentity.getX(), this.wantedY - flyingentity.getY(), this.wantedZ - flyingentity.getZ());
            double d0 = vec3.length();

			if (d0 < flyingentity.getBoundingBox().getSize() || !ModUtils.canReach(flyingentity, vec3.normalize(), Mth.ceil(d0)))
			{
				this.operation = MoveControl.Operation.WAIT;
				flyingentity.setDeltaMovement(flyingentity.getDeltaMovement().scale(0.5D));
			}
			else
			{
				float f = (float)flyingentity.getAttributeValue(Attributes.MOVEMENT_SPEED);
				flyingentity.setDeltaMovement(flyingentity.getDeltaMovement().add(vec3.scale((float)this.speedModifier * f * 0.2D / d0)));

				if (flyingentity.getTarget() == null)
				{
					Vec3 vec31 = flyingentity.getDeltaMovement();
					flyingentity.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float)Math.PI));
					flyingentity.yBodyRot = flyingentity.getYRot();
				}
				else
				{
					double d2 = flyingentity.getTarget().getX() - flyingentity.getX();
					double d1 = flyingentity.getTarget().getZ() - flyingentity.getZ();
					flyingentity.setYRot(-((float)Mth.atan2(d2, d1)) * (180.0F / (float)Math.PI));
					flyingentity.yBodyRot = flyingentity.getYRot();
				}
			}
		}
	}
}
