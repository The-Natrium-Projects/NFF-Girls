package net.sodiumzh.nff.girls.entity.hmag;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.ModConfigs;
import com.github.mechalopa.hmag.client.util.ModClientUtils;
import com.github.mechalopa.hmag.world.entity.IBeamAttackMob;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.block.EnderberryBushBlock;
import net.sodiumzh.nff.girls.entity.ICarriesBlock;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.HmagEnderExecutorInventory;
import net.sodiumzh.nff.girls.inventory.HmagEnderExecutorInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsBlocks;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestAttackableTargetGoal;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.preset.NFFTamedEnderManPreset;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

// Adjusted from EnderExcutor in HMaG
public class HmagEnderExecutorEntity extends NFFTamedEnderManPreset implements IBeamAttackMob, INFFGirlsTamed, ICarriesBlock
{

	// This value is by default -20, and increases every tick if it should do beam attack.
	// Positive means it's doing beam attack. When it reaches the attack duration (40), damage will be dealt.
	protected static final EntityDataAccessor<Integer> ATTACKING_TIME = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Integer> ATTACK_TARGET = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Optional<BlockPos>> GROW_ENDERBERRY_POS =
		SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	protected static final EntityDataAccessor<Integer> GROW_ENDERBERRY_REMAINED_BEAMING_TIME = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Integer> GROW_ENDERBERRY_REMAINED_COOLDOWN = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected static final int GROW_ENDERBERRY_BEAMING_TIME = 4 * 20;
	protected static final int GROW_ENDERBERRY_COOLDOWN = 5 * 60 * 20;
	protected static final double BEAM_ATTACK_MIN_DISTANCE = 1d;
	protected static final double BEAM_ATTACK_MAX_DISTANCE = 24d;
	protected static final double GROW_ENDERBERRY_MAX_DISTANCE = 8d;

	protected LivingEntity targetedEntity;
	protected int clientAttackTime;
	public boolean reduceDamage = true;
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ATTACKING_TIME, -20);
		entityData.define(ATTACK_TARGET, 0);
		entityData.define(GROW_ENDERBERRY_POS, Optional.empty());
		entityData.define(GROW_ENDERBERRY_REMAINED_BEAMING_TIME, 0);
		entityData.define(GROW_ENDERBERRY_REMAINED_COOLDOWN, 0);
		teleportNotOnHurtByWater = false;
	}
	
	public HmagEnderExecutorEntity(EntityType<? extends HmagEnderExecutorEntity> type, Level worldIn)
	{
		super(type, worldIn);
		this.xpReward = 0;
		this.modId = NFFGirls.MOD_ID;
	}

	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(1, new FloatGoal(this));
	      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
	      this.goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
	      this.goalSelector.addGoal(7, new NFFWaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
	      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
	      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	      this.goalSelector.addGoal(10, new NFFTamedEnderManPreset.LeaveBlockGoal(this));
	      this.goalSelector.addGoal(11, new NFFTamedEnderManPreset.TakeBlockGoal(this));
	      this.targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
	      this.targetSelector.addGoal(2, new NFFNearestAttackableTargetGoal<Endermite>(this, Endermite.class, true, false).allowAllStates().asGoal());
	      this.targetSelector.addGoal(3, new NFFHurtByTargetGoal(this));
	      this.targetSelector.addGoal(4, new NFFGirlsOwnerHurtTargetGoal(this));
	      targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
	      targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));

	}

	// Initialization end

	// Interaction
	
	@Override
	public MobApplicableItemTable getHealingItems()
	{
		return NFFGirlsHealingItems.ENDERMAN.get();
	}
	
	/*@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
				{
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
					{}
					else if (NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}	
					else return InteractionResult.PASS;
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			return InteractionResult.PASS;
		}
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {		
				if (hand == InteractionHand.MAIN_HAND && NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
				{
					NFFTamedStatics.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
			return InteractionResult.PASS;
		}
	}*/


	// Interaction end

	// No armor, hand items(0, 1), holding block(2) and 2 baubles(3, 4)

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new HmagEnderExecutorInventory(5, this);
	}

	@Override
	public void setCarryingBlock(BlockState newBlock) {
		this.setCarriedBlock(newBlock);
		
	}

	@Override
	public BlockState getCarryingBlock() {
		return this.getCarriedBlock();
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container)
	{
		return new HmagEnderExecutorInventoryMenu(containerId, playerInventory, container, this);
	}
	
	protected void updateHoldingBlock()
	{
		if (getAdditionalInventory().getItem(2).isEmpty())
		{
			this.setCarriedBlock(null);
		}			
		else if (getAdditionalInventory().getItem(2).getItem() instanceof BlockItem bi)
		{
			this.setCarriedBlock(bi.getBlock().defaultBlockState());
		}
		else this.setCarriedBlock(null);
	}
	
	
	// Inventory end

	// AI
	
	public boolean doBeamAttack = true;

	protected void tickHandleBeamAttack() {
		LivingEntity target = this.getTarget();
		if (target != null && target.isAlive() && ModConfigs.cachedServer.ENDER_EXECUTOR_BEAM_ATTACK
			&& doBeamAttack && !this.getAIState().equals(NFFTamedMobAIState.WAIT))
		{
			double d0 = this.distanceToSqr(target);
			// Case target is still in reach
			if (this.hasLineOfSight(target) && d0 > BEAM_ATTACK_MIN_DISTANCE * BEAM_ATTACK_MIN_DISTANCE
				&& d0 <= BEAM_ATTACK_MAX_DISTANCE * BEAM_ATTACK_MAX_DISTANCE) {
				int i = this.getAttackingTime();
				++i;
				// Case to start beam attack
				if (i == 0) {
					this.setAttackingTime(i);
					this.setActiveAttackTarget(target.getId());
				}
				// Case to deal damage
				else if (i >= this.getAttackDuration()) {
					if (this.getActiveAttackTarget() != null && doBeamAttack)
						this.attackEntityWithBeamAttack(this.getActiveAttackTarget(), 8f + 0.1f * (float) (this.getLevelHandler().getExpectedLevel()));
					this.setAttackingTime(-(10 + this.random.nextInt(6)));
					this.setActiveAttackTarget(0);
				}
				// Otherwise
				else {
					this.setAttackingTime(i);
				}
			}
			// If missing target, reset attack timer and target
			else {
				this.setAttackingTime(-20);
				this.setActiveAttackTarget(0);
			}
		}
		else
		{
			this.setAttackingTime(-20);
		}
	}

	// Adjusted
	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		{
			if (this.isInvulnerableTo(source)) {
				return false;
			}
			else
			{
				float f = amount;
				if (reduceDamage) f = this.reduceDamage(source, f);
				return super.hurt(source, f);
			}
		}
	}

	protected float reduceDamage(DamageSource dmgSource, float dmg) {
		float f = dmg;
		if (!(dmgSource.getEntity() != null
			&& dmgSource.isCreativePlayer())
			&& dmgSource != DamageSource.OUT_OF_WORLD
			&& f > 10.0F) {
			f = 10.0F + (f - 10.0F) * 0.1F;
		}
		return f;
	}

	@Override
	public void aiStep()
	{
		if (!this.level.isClientSide)
		{
			if (this.isAlive() && !this.isNoAi()) {
				this.tickHandleBeamAttack();
				this.tickHandleGrowingEnderberry();
			}
		}
		else {
			this.tickUpdateClientBeamAttackTime();
		}
		super.aiStep();
	}
	
	@Override
	public void customServerAiStep()
	{
		updateHoldingBlock();
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity livingEntity)
	{
		if (livingEntity == null)
		{
			this.setActiveAttackTarget(0);
		}

		super.setTarget(livingEntity);
	}
	public int getAttackingTime()
	{
		return this.entityData.get(ATTACKING_TIME);
	}

	private void setAttackingTime(int value)
	{
		this.entityData.set(ATTACKING_TIME, value);
	}

	@Override
	public boolean randomTeleport(double x, double y, double z, boolean flag)
	{
		if (super.randomTeleport(x, y, z, flag))
		{
			if (this.hasActiveAttackTarget())
			{
				this.setActiveAttackTarget(0);
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key)
	{
		super.onSyncedDataUpdated(key);

		if (ATTACK_TARGET.equals(key))
		{
			this.clientAttackTime = 0;
			this.targetedEntity = null;
		}
	}

	@Override
	public int getAttackDuration()
	{
		return 40;
	}

	@Override
	public boolean attackEntityWithBeamAttack(LivingEntity target, float damage)
	{
		if (!this.isSilent())
		{
			this.level.playSound((Player)null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
		}
		return target.hurt(DamageSource.indirectMagic(this, this), damage);
	}

	@Override
	public void setActiveAttackTarget(int entityId)
	{
		this.entityData.set(ATTACK_TARGET, entityId);
	}

	@Override
	public boolean hasActiveAttackTarget()
	{
		return entityData.get(ATTACK_TARGET).intValue() != 0;
	}

	@Nullable
	@Override
	public LivingEntity getActiveAttackTarget()
	{
		if (!this.hasActiveAttackTarget())
		{
			return null;
		}
		else if (this.level.isClientSide)
		{
			if (this.targetedEntity != null)
			{
				return this.targetedEntity;
			}
			else
			{
				Entity entity = this.level.getEntity(this.entityData.get(ATTACK_TARGET));

				if (entity instanceof LivingEntity)
				{
					this.targetedEntity = (LivingEntity)entity;
					return this.targetedEntity;
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			return this.getTarget();
		}
	}

	@Override
	public float getAttackAnimationScale(float f)
	{
		return (this.clientAttackTime + f) / this.getAttackDuration();
	}
	
	public boolean teleportToOwnerInRain(int tryTimes)
	{
		if (this.isInWaterOrRain() && !this.isInWater())
		{
			for (int i = 0; i < tryTimes; ++i)
			{
				if (this.teleportTowards(this.getOwner())
					&& !this.level.canSeeSky(this.blockPosition()))
					return true;
			}
		}
		return false;
	}



	@Override
	public boolean tryTeleportOnWaterHurt(int tryTimes)
	{
		if (this.teleportToOwnerInRain(tryTimes))
			return true;
		else {
			for (int i = 0; i < tryTimes; ++i)
			{
				if (this.teleport() && this.hasLineOfSight(this.getOwner()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// save&load

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setGrowEnderberryRemainedCooldown(nbt.getInt("growEnderberryRemainedCooldown"));
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		this.setInit();
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("growEnderberryRemainedCooldown", this.getGrowEnderberryRemainedCooldown());
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}

	/* Grow Enderberry related */
	public int getGrowEnderberryRemainedCooldown() {
		return this.getEntityData().get(GROW_ENDERBERRY_REMAINED_COOLDOWN);
	}

	protected void setGrowEnderberryRemainedCooldown(int val) {
		this.getEntityData().set(GROW_ENDERBERRY_REMAINED_COOLDOWN, val);
	}

	protected void updateGrowEnderberryTimers() {
		this.setGrowEnderberryRemainedCooldown(Math.max(this.getGrowEnderberryRemainedCooldown() - 1, 0));
		this.getEntityData().set(GROW_ENDERBERRY_REMAINED_BEAMING_TIME,
			Math.max(this.getEntityData().get(GROW_ENDERBERRY_REMAINED_BEAMING_TIME) - 1, 0));
	}

	private void startGrowEnderberryCooldown() {
		this.getEntityData().set(GROW_ENDERBERRY_POS, Optional.empty());
		this.setGrowEnderberryRemainedCooldown(GROW_ENDERBERRY_COOLDOWN);
	}

	private boolean canSeeEnderberryPos(BlockPos pos) {
		Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
		Vec3 vec31 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
		if (vec31.distanceTo(vec3) > GROW_ENDERBERRY_MAX_DISTANCE * GROW_ENDERBERRY_MAX_DISTANCE) {
			return false;
		} else {
			return this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
		}
	}

	private Optional<BlockPos> findEnderberryBush() {
		Level level = this.getLevel();
		Predicate<BlockPos> isEnderberry = pos -> level.getBlockState(pos).is(NFFGirlsBlocks.ENDERBERRY_BUSH.get());
		Predicate<BlockPos> isGrowable = pos -> level.getBlockState(pos).getValue(EnderberryBushBlock.AGE) < EnderberryBushBlock.MAX_AGE
			&& level.getBlockState(pos).getValue(EnderberryBushBlock.CAN_GROW_ENDERBERRY);
		List<BlockPos> bushesList = BlockPos.betweenClosedStream(this.getBoundingBox().inflate(GROW_ENDERBERRY_MAX_DISTANCE))
			.filter(isEnderberry).filter(isGrowable)
			.map(pos -> new BlockPos(pos.getX(), pos.getY(), pos.getZ()))
			.toList();
		return bushesList.isEmpty() ? Optional.empty() : Optional.of(bushesList.get(this.getRandom().nextInt(bushesList.size())));
	}

	// End enderberry growing action
	private void resetGrowingEnderberry() {
		this.getEntityData().set(GROW_ENDERBERRY_POS, Optional.empty());
		this.getEntityData().set(GROW_ENDERBERRY_REMAINED_BEAMING_TIME, 0);
	}

	public boolean isBeamingEnderberryBush() {
		return this.getEntityData().get(GROW_ENDERBERRY_POS).isPresent() && this.getGrowEnderberryRemainedCooldown() <= 0;
	}

	private boolean startBeamingEnderberry(BlockPos pos) {
		if (!this.level.getBlockState(pos).is(NFFGirlsBlocks.ENDERBERRY_BUSH.get())) return false;
		this.getEntityData().set(GROW_ENDERBERRY_POS, Optional.of(pos));
		this.getEntityData().set(GROW_ENDERBERRY_REMAINED_BEAMING_TIME, GROW_ENDERBERRY_BEAMING_TIME);
		return true;
	}

	private boolean doGrowEnderberry() {
		BlockPos pos = this.getEntityData().get(GROW_ENDERBERRY_POS).orElse(null);
		if (pos == null) return false;
		BlockState bs = this.getLevel().getBlockState(pos);
		if (bs.is(NFFGirlsBlocks.ENDERBERRY_BUSH.get()) && bs.getValue(EnderberryBushBlock.CAN_GROW_ENDERBERRY)
			&& bs.getValue(EnderberryBushBlock.AGE) < EnderberryBushBlock.MAX_AGE)
		{
			bs = bs.setValue(EnderberryBushBlock.CAN_GROW_ENDERBERRY, false);
			bs = bs.setValue(EnderberryBushBlock.AGE, bs.getValue(EnderberryBushBlock.AGE) + 1);
			this.getLevel().setBlock(pos, bs, 2);
			this.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, pos);
			this.startGrowEnderberryCooldown();
			this.getEntityData().set(GROW_ENDERBERRY_POS, Optional.empty());
			return true;
		}
		else return false;
	}

	public Optional<BlockPos> getBeamingPos() {
		return this.getEntityData().get(GROW_ENDERBERRY_POS);
	}

	protected void tickHandleGrowingEnderberry() {
		this.updateGrowEnderberryTimers();
		if ((this.getTarget() == null || !this.getTarget().isAlive())) {
			if (this.isBeamingEnderberryBush()) {
				// Case beaming succeeded
				if (this.getEntityData().get(GROW_ENDERBERRY_REMAINED_BEAMING_TIME) <= 0) {
					if (!doGrowEnderberry()) this.resetGrowingEnderberry();
				}
				// Case beaming in progress
				else {
					if (!this.canSeeEnderberryPos(this.getEntityData().get(GROW_ENDERBERRY_POS).get()))
						this.resetGrowingEnderberry();
					else {
						BlockPos beamingPos = this.getBeamingPos().orElseThrow();
						this.getLookControl().setLookAt(new Vec3(beamingPos.getX(), beamingPos.getY(), beamingPos.getZ()));
					}
				}
			}
			// Not beaming and not in cooldown, find and start
			else if (this.getRandom().nextInt(10) == 0
				&& this.getGrowEnderberryRemainedCooldown() <= 0)
			{
				this.findEnderberryBush().ifPresent(this::startBeamingEnderberry);
			}
		}
	}

	/**
	 * For renderer accessing the beam end point
	 */
	@OnlyIn(Dist.CLIENT)
	public Optional<Vec3> getBeamEndPoint(float partialTicks) {
		return this.getTarget() != null ?
			Optional.of(ModClientUtils.getPosition(this.getTarget(), (double)this.getTarget().getBbHeight() * 0.5D, partialTicks))
			: this.getBeamingPos().map(pos -> new Vec3(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d));
	}

	protected void tickUpdateClientBeamAttackTime() {
		if (!this.level.isClientSide) throw new RuntimeException();
		if (this.isAlive() && this.hasActiveAttackTarget())
		{
			if (this.clientAttackTime < this.getAttackDuration())
			{
				++this.clientAttackTime;
			}
			LivingEntity target = this.getActiveAttackTarget();
			if (target != null)
			{
				this.getLookControl().setLookAt(target, 90.0F, 90.0F);
				this.getLookControl().tick();
			}
		}
		else
		{
			this.clientAttackTime = 0;
		}
	}
}
