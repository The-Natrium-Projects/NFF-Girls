package net.sodiumzh.nff.girls.client.renderer.layer;

import com.github.mechalopa.hmag.client.ModModelLayers;
import com.github.mechalopa.hmag.client.model.AbstractGirlModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.client.model.NFFGirlsHmagCreeperGirlArmorModel;
import net.sodiumzh.nff.girls.entity.hmag.HmagCreeperGirlEntity;

// Ported from HMaG CreeperGirlPowerLayer
@OnlyIn(Dist.CLIENT)
public class NFFGirlsHmagCreeperGirlPowerLayer extends EnergySwirlLayer<HmagCreeperGirlEntity, AbstractGirlModel<HmagCreeperGirlEntity>>
{
	private static final ResourceLocation TEX = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final NFFGirlsHmagCreeperGirlArmorModel<HmagCreeperGirlEntity> model;

	public NFFGirlsHmagCreeperGirlPowerLayer(RenderLayerParent<HmagCreeperGirlEntity, AbstractGirlModel<HmagCreeperGirlEntity>> renderLayerParent, EntityModelSet modelSet)
	{
		super(renderLayerParent);
		this.model = new NFFGirlsHmagCreeperGirlArmorModel<>(modelSet.bakeLayer(ModModelLayers.CREEPER_GIRL_POWER_ARMOR));
	}

	@Override
	protected float xOffset(float f)
	{
		return f * 0.01F;
	}

	@Override
	protected ResourceLocation getTextureLocation()
	{
		return TEX;
	}

	@Override
	protected EntityModel<HmagCreeperGirlEntity> model()
	{
		return this.model;
	}
}