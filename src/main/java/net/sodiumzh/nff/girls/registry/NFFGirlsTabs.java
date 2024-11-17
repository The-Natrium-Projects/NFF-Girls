package net.sodiumzh.nff.girls.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;


public class NFFGirlsTabs{
    
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NFFGirls.MOD_ID);
	
	public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", () -> CreativeModeTab.builder()
			.title(NaUtilsInfoStatics.createTranslatable("item_group.nffgirls_tab"))
			.icon(() -> NFFGirlsItems.TAB_ICON.get().getDefaultInstance())
			.displayItems((features, output) ->
			{
				for (var item: NFFGirlsItems.ITEMS.getEntries())
				{
					if (!NFFGirlsItems.NO_TAB.contains(item))
						output.accept(item.get());
				}
			}).build());
}
