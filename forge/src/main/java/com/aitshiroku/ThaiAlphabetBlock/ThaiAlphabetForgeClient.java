package com.aitshiroku.ThaiAlphabetBlock;

import com.aitshiroku.thai_alphabet_block.ThaiAlphabetCommon;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ThaiAlphabetCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ThaiAlphabetForgeClient {

    private ThaiAlphabetForgeClient() {
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (RegistryObject<Block> ro : ThaiAlphabetBlock.BLOCKS.getEntries()) {
                ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.cutout());
            }
        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (RegistryObject<Block> ro : ThaiAlphabetBlock.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            event.register(
                    (state, level, pos, tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (!state.hasProperty(ThaiLetterBlock.COLOR)) {
                            return tintIndex == 0
                                    ? ThaiAlphabetColorUtil.backgroundArgbFromDye(
                                            net.minecraft.world.item.DyeColor.WHITE)
                                    : ThaiAlphabetColorUtil.glyphArgbFromDye(
                                            net.minecraft.world.item.DyeColor.WHITE);
                        }
                        net.minecraft.world.item.DyeColor dye = state.getValue(ThaiLetterBlock.COLOR);
                        return tintIndex == 0
                                ? ThaiAlphabetColorUtil.backgroundArgbFromDye(dye)
                                : ThaiAlphabetColorUtil.glyphArgbFromDye(dye);
                    },
                    block);
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (RegistryObject<Block> ro : ThaiAlphabetBlock.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            event.getItemColors()
                    .register(
                            (ItemStack stack, int tintIndex) -> {
                                if (tintIndex != 0 && tintIndex != 1) {
                                    return -1;
                                }
                                BlockState state = blockStateFromStack(stack, block);
                                net.minecraft.world.item.DyeColor dye = state != null
                                        && state.hasProperty(ThaiLetterBlock.COLOR)
                                                ? state.getValue(ThaiLetterBlock.COLOR)
                                                : net.minecraft.world.item.DyeColor.WHITE;
                                return tintIndex == 0
                                        ? ThaiAlphabetColorUtil.backgroundArgbFromDye(dye)
                                        : ThaiAlphabetColorUtil.glyphArgbFromDye(dye);
                            },
                            block.asItem());
        }
    }

    private static BlockState blockStateFromStack(ItemStack stack, Block block) {
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem bi)) {
            return null;
        }
        if (bi.getBlock() != block) {
            return null;
        }
        return ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
    }
}
