package com.aitshiroku.thai_alphabet_block;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Block block : ThaiAlphabetBlockFabric.letterBlocksView()) {
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
            ColorProviderRegistry.BLOCK.register(
                    (state, world, pos, tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (tintIndex == 0) {
                            DyeColor dye = state.hasProperty(ThaiLetterBlock.COLOR)
                                    ? state.getValue(ThaiLetterBlock.COLOR)
                                    : DyeColor.WHITE;
                            return ThaiAlphabetColorUtil.backgroundArgbFromDye(dye);
                        } else {
                            DyeColor glyphDye = state.hasProperty(ThaiLetterBlock.GLYPH_COLOR)
                                    ? state.getValue(ThaiLetterBlock.GLYPH_COLOR)
                                    : DyeColor.WHITE;
                            return ThaiAlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block);
            ColorProviderRegistry.ITEM.register(
                    (ItemStack stack, int tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (!(stack.getItem() instanceof BlockItem bi) || bi.getBlock() != block) {
                            DyeColor white = DyeColor.WHITE;
                            return tintIndex == 0
                                    ? ThaiAlphabetColorUtil.backgroundArgbFromDye(white)
                                    : ThaiAlphabetColorUtil.glyphArgbFromDye(white);
                        }
                        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        if (tintIndex == 0) {
                            DyeColor dye = state.hasProperty(ThaiLetterBlock.COLOR)
                                    ? state.getValue(ThaiLetterBlock.COLOR)
                                    : DyeColor.WHITE;
                            return ThaiAlphabetColorUtil.backgroundArgbFromDye(dye);
                        } else {
                            DyeColor glyphDye = state.hasProperty(ThaiLetterBlock.GLYPH_COLOR)
                                    ? state.getValue(ThaiLetterBlock.GLYPH_COLOR)
                                    : DyeColor.WHITE;
                            return ThaiAlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block.asItem());
        }
    }
}
