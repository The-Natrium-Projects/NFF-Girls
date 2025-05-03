package net.sodiumzh.nff.girls.sound;

import com.github.mechalopa.hmag.registry.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;

import java.util.Random;

public class NFFGirlsSoundPresets
{
	protected static Random rnd = new Random();
	
	public static SoundEvent generalAmbient(SoundEvent defaultSound)
	{
		if (rnd.nextDouble() > NFFGirlsConfigs.ValueCache.Sound.AMBIENT_SOUND_CHANCE)
			return null;
		return defaultSound;
	}
	
	public static SoundEvent zombieAmbient(SoundEvent defaultSound)
	{
		if (rnd.nextDouble() > NFFGirlsConfigs.ValueCache.Sound.AMBIENT_SOUND_CHANCE)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.ZOMBIES_NO_AMBIENT_SOUND)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.ZOMBIES_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_AMBIENT.get();
		return defaultSound;
	}
	
	public static SoundEvent zombieHurt(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.ZOMBIES_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_HURT.get();
		return defaultSound;
	}
	
	public static SoundEvent zombieDeath(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.ZOMBIES_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_DEATH.get();
		return defaultSound;
	}
	
	public static SoundEvent skeletonAmbient(SoundEvent defaultSound)
	{
		if (rnd.nextDouble() > NFFGirlsConfigs.ValueCache.Sound.AMBIENT_SOUND_CHANCE)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.SKELETONS_NO_AMBIENT_SOUND)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.SKELETONS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_AMBIENT.get();
		return defaultSound;
	}
	
	public static SoundEvent skeletonHurt(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.SKELETONS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_HURT.get();
		return defaultSound;
	}
	
	public static SoundEvent skeletonDeath(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.SKELETONS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_DEATH.get();
		return defaultSound;
	}
	
	public static SoundEvent ghastAmbient(SoundEvent defaultSound)
	{
		if (rnd.nextDouble() > NFFGirlsConfigs.ValueCache.Sound.AMBIENT_SOUND_CHANCE)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.GHASTS_NO_AMBIENT_SOUND)
			return null;
		if (NFFGirlsConfigs.ValueCache.Sound.GHASTS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_AMBIENT.get();
		return defaultSound;
	}
	
	public static SoundEvent ghastHurt(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.GHASTS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_HURT.get();
		return defaultSound;
	}
	
	public static SoundEvent ghastDeath(SoundEvent defaultSound)
	{
		if (NFFGirlsConfigs.ValueCache.Sound.GHASTS_USE_GIRL_SOUND)
			return ModSoundEvents.GIRL_MOB_DEATH.get();
		return defaultSound;
	}
}
