package net.sodiumzh.nff.girls.client.renderer.layer;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.client.renderer.layers.EyesLayer2;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.client.model.NFFGirlsHmagEnderExecutorModel;
import net.sodiumzh.nff.girls.entity.hmag.HmagEnderExecutorEntity;

@OnlyIn(Dist.CLIENT)
public class NFFGirlsHmagEnderExecutorEyesLayer<T extends HmagEnderExecutorEntity, M extends NFFGirlsHmagEnderExecutorModel<T>> extends EyesLayer2<T, M>
{
	private static final RenderType RENDER_TYPE0 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor_eyes.png"));
	private static final RenderType RENDER_TYPE1 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor_eyes_is_beam.png"));

	public NFFGirlsHmagEnderExecutorEyesLayer(RenderLayerParent<T, M> renderLayerParent)
	{
		super(renderLayerParent);
	}

	@Override
	public RenderType renderType(T entity)
	{
		return this.getParentModel().beamAttacking && entity.tickCount % 2 == 0 ? RENDER_TYPE1 : RENDER_TYPE0;
	}
}