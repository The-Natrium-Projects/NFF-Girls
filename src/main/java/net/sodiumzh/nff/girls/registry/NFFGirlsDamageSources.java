package net.sodiumzh.nff.girls.registry;

import javax.annotation.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.NFFGirls;

/**
 * This doesn't work but the reason is unknown
 */
public class NFFGirlsDamageSources
{
	public static final ResourceKey<DamageType> NECROMANCER_WITHER = register("necromancer_wither");
	
	protected static ResourceKey<DamageType> register(String name)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(NFFGirls.MOD_ID, name));
	}

	public static DamageSource source(Level level, ResourceKey<DamageType> key)
	{
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
	}

	public static DamageSource source(Level level, ResourceKey<DamageType> key, @Nullable Entity entity)
	{
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), entity);
	}

	public static DamageSource source(Level level, ResourceKey<DamageType> key, @Nullable Entity entity, @Nullable Entity sourceEntity)
	{
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), entity, sourceEntity);
	}
}
