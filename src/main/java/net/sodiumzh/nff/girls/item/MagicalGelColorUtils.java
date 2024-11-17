package net.sodiumzh.nff.girls.item;

import java.util.Random;

import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nautils.math.LinearColor;

public class MagicalGelColorUtils
{
	protected static Random rnd = new Random();
	
	public static LinearColor getSlimeColor(SlimeGirlEntity.ColorVariant color)
	{
		float[] normRGB = color.getColors();
		return LinearColor.fromNormalized(normRGB[0], normRGB[1], normRGB[2]);
	}
	
	public static LinearColor getSlimeColor(MagicalSlimeEntity slime)
	{
		return getSlimeColor(SlimeGirlEntity.ColorVariant.byId(slime.getVariant().getId()));
	}
	
	public static LinearColor getSlimeColor(SlimeGirlEntity slime)
	{
		return getSlimeColor(SlimeGirlEntity.ColorVariant.byId(slime.getVariant().getId()));
	}
	
	public static SlimeGirlEntity.ColorVariant randomColorVariant()
	{
		return SlimeGirlEntity.ColorVariant.byId(rnd.nextInt(SlimeGirlEntity.ColorVariant.values().length));
	}
	
	public static SlimeGirlEntity.ColorVariant closestVariant(LinearColor color)
	{
		Vec3 colorVec = color.toNormalized();
		SlimeGirlEntity.ColorVariant res = null;
		double dist = 0;
		for (SlimeGirlEntity.ColorVariant cv: SlimeGirlEntity.ColorVariant.values())
		{
			float[] colorf = cv.getColors();
			Vec3 cvColor = new Vec3(colorf[0], colorf[1], colorf[2]);
			if (res == null || colorVec.distanceToSqr(cvColor) < dist)
			{
				res = cv;
				dist = colorVec.distanceToSqr(cvColor);
			}
		}
		return res;
	}
	
}
