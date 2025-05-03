package net.sodiumzh.nff.girls.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.client.gui.screen.CitadelBasedMobDictionaryGUI;
import net.sodiumzh.nfu.item.NFUItem;

// Copied and adjusted from Alex's Mobs book
public class CitadelBasedMobDictionaryItem extends NFUItem {

    private final ResourceLocation root;
    private final String titleTranslationKey;
    private final String textFileDirectory;

    public CitadelBasedMobDictionaryItem(Properties pProperties,
          ResourceLocation rootLocation, String titleTranslationKey, String textFileDirectory) {
        super(pProperties);
        this.root = rootLocation;
        this.titleTranslationKey = titleTranslationKey;
        this.textFileDirectory = textFileDirectory;
    }

    //private boolean usedOnEntity = false;


    /*public InteractionResult interactLivingEntity(Player playerIn, LivingEntity target, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        if (playerIn instanceof ServerPlayer serverplayerentity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, itemStackIn);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
        }

        if (playerIn.level.isClientSide && target.getEncodeId() != null && target.getEncodeId().contains("alexsmobs:")) {
            this.usedOnEntity = true;
            String id = target.getEncodeId().replace("alexsmobs:", "");
            if (target instanceof EntityBoneSerpent || target instanceof EntityBoneSerpentPart) {
                id = "bone_serpent";
            }

            if (target instanceof EntityCentipedeHead || target instanceof EntityCentipedeBody || target instanceof EntityCentipedeTail) {
                id = "cave_centipede";
            }

            if (target instanceof EntityVoidWorm || target instanceof EntityVoidWormPart) {
                id = "void_worm";
            }

            if (target instanceof EntityAnaconda || target instanceof EntityAnacondaPart) {
                id = "anaconda";
            }

            if (target instanceof EntityMurmur || target instanceof EntityMurmurHead) {
                id = "murmur";
            }

            AlexsMobs.PROXY.openBookGUI(itemStackIn, id);
        }

        return InteractionResult.CONSUME;
    }*/

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (/*!this.usedOnEntity*/true) {
            /*if (playerIn instanceof ServerPlayer) {
                ServerPlayer serverplayerentity = (ServerPlayer)playerIn;
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, itemStackIn);
                serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
            }*/

            if (worldIn.isClientSide) {
                CitadelBasedMobDictionaryGUI.openGUI(itemStackIn, this.root, this.titleTranslationKey, this.textFileDirectory);
            }
        }

        //this.usedOnEntity = false;
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

}
