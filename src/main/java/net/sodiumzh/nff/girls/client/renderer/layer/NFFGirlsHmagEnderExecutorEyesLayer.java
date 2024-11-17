package net.sodiumzh.nff.girls.client.renderer.layer;

import com.github.mechalopa.hmag.HMaG;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.client.model.NFFGirlsHmagEnderExecutorModel;
import net.sodiumzh.nff.girls.entity.hmag.HmagEnderExecutorEntity;

@OnlyIn(Dist.CLIENT)
public class NFFGirlsHmagEnderExecutorEyesLayer<T extends HmagEnderExecutorEntity, M extends NFFGirlsHmagEnderExecutorModel<T>> extends RenderLayer<T, M>
{
	private static final RenderType RENDER_TYPE0 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor/ender_executor_eyes.png"));
	private static final RenderType RENDER_TYPE1 = RenderType.eyes(new ResourceLocation(HMaG.MODID, "textures/entity/ender_executor/ender_executor_eyes_is_beam.png"));

	public NFFGirlsHmagEnderExecutorEyesLayer(RenderLayerParent<T, M> renderLayerParent)
	{
		super(renderLayerParent);
	}

	public RenderType renderType(boolean flag)
	{
		return flag ? RENDER_TYPE1 : RENDER_TYPE0;
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType(((M)this.getParentModel()).beamAttacking && livingEntity.tickCount % 2 == 0));
		((M)this.getParentModel()).renderToBuffer(pose, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
}