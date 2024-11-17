package net.sodiumzh.nff.girls.client;

import com.github.mechalopa.hmag.client.renderer.AlrauneRenderer;
import com.github.mechalopa.hmag.client.renderer.BansheeRenderer;
import com.github.mechalopa.hmag.client.renderer.CrimsonSlaughtererRenderer;
import com.github.mechalopa.hmag.client.renderer.CursedDollRenderer;
import com.github.mechalopa.hmag.client.renderer.DodomekiRenderer;
import com.github.mechalopa.hmag.client.renderer.DrownedGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.DullahanRenderer;
import com.github.mechalopa.hmag.client.renderer.GhastlySeekerRenderer;
import com.github.mechalopa.hmag.client.renderer.GlaryadRenderer;
import com.github.mechalopa.hmag.client.renderer.HarpyRenderer;
import com.github.mechalopa.hmag.client.renderer.HornetRenderer;
import com.github.mechalopa.hmag.client.renderer.HuskGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ImpRenderer;
import com.github.mechalopa.hmag.client.renderer.JackFrostRenderer;
import com.github.mechalopa.hmag.client.renderer.JiangshiRenderer;
import com.github.mechalopa.hmag.client.renderer.KoboldRenderer;
import com.github.mechalopa.hmag.client.renderer.MagicBulletRenderer;
import com.github.mechalopa.hmag.client.renderer.MeltyMonsterRenderer;
import com.github.mechalopa.hmag.client.renderer.ModThrownItemRenderer;
import com.github.mechalopa.hmag.client.renderer.NecroticReaperRenderer;
import com.github.mechalopa.hmag.client.renderer.NightwalkerRenderer;
import com.github.mechalopa.hmag.client.renderer.RedcapRenderer;
import com.github.mechalopa.hmag.client.renderer.SkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SlimeGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SnowCanineRenderer;
import com.github.mechalopa.hmag.client.renderer.StrayGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.WitherSkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ZombieGirlRenderer;

import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsBowSecWeaponOneBaubleGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsEquipmentTwoBaublesGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsFourBaublesGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHandItemsSixBaublesGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHandItemsTwoBaublesGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagBansheeGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagCreeperGirlGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagDodomekiGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagDullahan;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagEnderExecutorGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagGhastlySeekerGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagImpGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagJiangshiGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagKoboldGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagMeltyMonsterGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagNecroticReaperGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagNightwalkerGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagRedcapGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagSlimeGirlGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsSixBaublesGui;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsThreeBaublesGui;
import net.sodiumzh.nff.girls.client.particle.MagicalGelBallParticle;
import net.sodiumzh.nff.girls.client.renderer.NFFGirlsHmagCreeperGirlRenderer;
import net.sodiumzh.nff.girls.client.renderer.NFFGirlsHmagEnderExecutorRenderer;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsSixBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagImpInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagJiangshiInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagKoboldInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagMeltyMonsterInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsNecroticReaperInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsNightwalkerInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagRedcapInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSixBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagSlimeGirlInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsCreeperInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsEquipmentTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsGhastlySeekerInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagBansheeInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagDodomekiInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagEnderExecutorInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagZombieGirlInventoryMenu;
import net.sodiumzh.nff.girls.item.ReinforcedFishingRodItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsParticleTypes;
import net.sodiumzh.nff.services.event.client.NFFGuiScreenRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsClientSetupEventListeners 
{

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(), ZombieGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(), SkeletonGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(), HuskGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(), DrownedGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(), NFFGirlsHmagCreeperGirlRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(), NFFGirlsHmagEnderExecutorRenderer::new);  
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(), StrayGirlRenderer::new);  
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), WitherSkeletonGirlRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HORNET.get(), HornetRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(), NecroticReaperRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(), GhastlySeekerRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_BANSHEE.get(), BansheeRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_KOBOLD.get(), KoboldRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_IMP.get(), ImpRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HARPY.get(), HarpyRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(), SnowCanineRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(), SlimeGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DULLAHAN.get(), DullahanRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DODOMEKI.get(), DodomekiRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ALRAUNE.get(), AlrauneRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_GLARYAD.get(), GlaryadRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(), CrimsonSlaughtererRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(), CursedDollRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_REDCAP.get(), RedcapRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_JACK_FROST.get(), JackFrostRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(), MeltyMonsterRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(), NightwalkerRenderer::new);
        
        event.registerEntityRenderer(NFFGirlsEntityTypes.NECROMANCER_MAGIC_BULLET.get(), MagicBulletRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.BEFRIENDED_GHAST_FIREBALL.get(), c -> new ThrownItemRenderer<>(c, 3.0F, true));
        event.registerEntityRenderer(NFFGirlsEntityTypes.MAGICAL_GEL_BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.ALRAUNE_POISON_SEED.get(), ModThrownItemRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.ALRAUNE_HEALING_SEED.get(), ModThrownItemRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.REINFORCED_FISHING_HOOK.get(), FishingHookRenderer::new);
    }

    public static void onRegisterParticleProvider(RegisterParticleProvidersEvent event)
    {
    	event.register(NFFGirlsParticleTypes.MAGICAL_GEL_BALL.get(), new MagicalGelBallParticle.Provider());
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
    	event.enqueueWork(() -> 
    	{
    		// Item properties
    		ItemProperties.register(NFFGirlsItems.REINFORCED_FISHING_ROD.get(), new ResourceLocation("cast"), ReinforcedFishingRodItem::isCastClient);
    	});
    }
    
    @SubscribeEvent
    public static void registerGuiScreen(NFFGuiScreenRegisterEvent event)
    {
		event.registerDefault(NFFGirlsCreeperInventoryMenu.class, NFFGirlsHmagCreeperGirlGui::new);
		event.registerDefault(NFFGirlsHmagEnderExecutorInventoryMenu.class, NFFGirlsHmagEnderExecutorGui::new);
		event.registerDefault(NFFGirlsEquipmentTwoBaublesInventoryMenu.class, NFFGirlsEquipmentTwoBaublesGui::new);
		event.registerDefault(NFFGirlsGhastlySeekerInventoryMenu.class, NFFGirlsHmagGhastlySeekerGui::new);
		event.registerDefault(NFFGirlsHandItemsTwoBaublesInventoryMenu.class, NFFGirlsHandItemsTwoBaublesGui::new);
		event.registerDefault(NFFGirlsNecroticReaperInventoryMenu.class, NFFGirlsHmagNecroticReaperGui::new);
		event.registerDefault(NFFGirlsSkeletonInventoryMenu.class, NFFGirlsBowSecWeaponOneBaubleGui::new);
		event.registerDefault(NFFGirlsHmagBansheeInventoryMenu.class, NFFGirlsHmagBansheeGui::new);
		event.registerDefault(NFFGirlsHmagKoboldInventoryMenu.class, NFFGirlsHmagKoboldGui::new);
		event.registerDefault(NFFGirlsHmagImpInventoryMenu.class, NFFGirlsHmagImpGui::new);
		event.registerDefault(NFFGirlsFourBaublesInventoryMenu.class, NFFGirlsFourBaublesGui::new);
		event.registerDefault(NFFGirlsHmagSlimeGirlInventoryMenu.class, NFFGirlsHmagSlimeGirlGui::new);
		event.registerDefault(NFFGirlsHandItemsFourBaublesDefaultInventoryMenu.class, NFFGirlsHmagDullahan::new);
		event.registerDefault(NFFGirlsHmagDodomekiInventoryMenu.class, NFFGirlsHmagDodomekiGui::new);
		event.registerDefault(NFFGirlsHmagJiangshiInventoryMenu.class, NFFGirlsHmagJiangshiGui::new);
		event.registerDefault(NFFGirlsHmagThreeBaublesInventoryMenu.class, NFFGirlsThreeBaublesGui::new);
		event.registerDefault(NFFGirlsSixBaublesInventoryMenu.class, NFFGirlsSixBaublesGui::new);
		event.registerDefault(NFFGirlsHandItemsSixBaublesInventoryMenu.class, NFFGirlsHandItemsSixBaublesGui::new);
		event.registerDefault(NFFGirlsHmagRedcapInventoryMenu.class, NFFGirlsHmagRedcapGui::new);
		event.registerDefault(NFFGirlsHmagMeltyMonsterInventoryMenu.class, NFFGirlsHmagMeltyMonsterGui::new);
		event.registerDefault(NFFGirlsNightwalkerInventoryMenu.class, NFFGirlsHmagNightwalkerGui::new);
		
		event.registerDefault(NFFGirlsHmagZombieGirlInventoryMenu.class, NFFGirlsEquipmentTwoBaublesGui::new);
    }

}
