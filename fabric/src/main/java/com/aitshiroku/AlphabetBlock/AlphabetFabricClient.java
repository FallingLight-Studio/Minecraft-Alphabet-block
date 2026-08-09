package com.aitshiroku.AlphabetBlock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class AlphabetFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Block block : AlphabetBlockFabric.letterBlocksView()) {
            if (!(block instanceof LetterBlock)) {
                continue;
            }
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
            ColorProviderRegistry.BLOCK.register(
                    (state, world, pos, tintIndex) -> {
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
            ColorProviderRegistry.ITEM.register(
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
