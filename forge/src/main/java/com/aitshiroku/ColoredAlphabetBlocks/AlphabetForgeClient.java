package com.aitshiroku.ColoredAlphabetBlocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = AlphabetCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AlphabetForgeClient {

    private AlphabetForgeClient() {
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (RegistryObject<Block> ro : AlphabetBlock.BLOCKS.getEntries()) {
                ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.cutout());
            }
        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (RegistryObject<Block> ro : AlphabetBlock.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof LetterBlock)) {
                continue;
            }
            event.register(
                    (state, level, pos, tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (tintIndex == 0) {
                            // Background color
                            AlphabetColorProperties.BlockColor color = state.hasProperty(LetterBlock.COLOR)
                                    ? state.getValue(LetterBlock.COLOR)
                                    : AlphabetColorProperties.BlockColor.NONE;
                            return AlphabetColorUtil.backgroundArgbFromColor(color);
                        } else {
                            // Glyph color
                            DyeColor glyphDye = state.hasProperty(LetterBlock.GLYPH_COLOR)
                                    ? state.getValue(LetterBlock.GLYPH_COLOR)
                                    : DyeColor.BLACK;
                            return AlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block);
        }
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (RegistryObject<Block> ro : AlphabetBlock.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof LetterBlock)) {
                continue;
            }
            event.register(
                    (ItemStack stack, int tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (!(stack.getItem() instanceof BlockItem bi) || bi.getBlock() != block) {
                            return tintIndex == 0
                                    ? AlphabetColorUtil.backgroundArgbFromColor(AlphabetColorProperties.BlockColor.NONE)
                                    : AlphabetColorUtil.glyphArgbFromDye(DyeColor.BLACK);
                        }
                        BlockState state = AlphabetBlockStateUtil.stateFromItemStack(stack, block);

                        if (tintIndex == 0) {
                            // Background color
                            AlphabetColorProperties.BlockColor color = state.hasProperty(LetterBlock.COLOR)
                                    ? state.getValue(LetterBlock.COLOR)
                                    : AlphabetColorProperties.BlockColor.NONE;
                            return AlphabetColorUtil.backgroundArgbFromColor(color);
                        } else {
                            // Glyph color
                            DyeColor glyphDye = state.hasProperty(LetterBlock.GLYPH_COLOR)
                                    ? state.getValue(LetterBlock.GLYPH_COLOR)
                                    : DyeColor.BLACK;
                            return AlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block.asItem());
        }
    }
}
