package net.sodiumzh.nff.girls.entity.ai;

import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class AiMaths
{
	/** Get a random vector of given length, uniform in orientation (probability in direct proportion of sphere surface area) */
	public static Vec3 randomVector(double scale)
	{
		double x;
		double y;
		double z;
		// Inspired by UE4 random vector algorithm
		// Get a position in a radius 1 sphere
		Random rnd = new Random();
		do
		{
			x = rnd.nextDouble() * 2 - 1;
			y = rnd.nextDouble() * 2 - 1;
			z = rnd.nextDouble() * 2 - 1;
		}
		while (x * x + y * y + z * z > 1d);
		return new Vec3(x, y, z).normalize().scale(scale);		
	}
	
	/** Get a random normalized vector, uniform in orientation (probability in direct proportion of sphere surface area) */	
	public static Vec3 randomVector()
	{
		return randomVector(1d);
	}
	
	/** Random vector pointing to a oval surface */
	public static Vec3 randomOvalVector(Vec3 scale)
	{
		Vec3 v = randomVector();
		return new Vec3(v.x * scale.x, v.y * scale.y, v.z * scale.z);
	}
	
	/** Random vector pointing to a oval surface */
	public static Vec3 randomOvalVector(double xScale, double yScale, double zScale)
	{
		return randomOvalVector(new Vec3(xScale, yScale, zScale));
	}
	
	
}
