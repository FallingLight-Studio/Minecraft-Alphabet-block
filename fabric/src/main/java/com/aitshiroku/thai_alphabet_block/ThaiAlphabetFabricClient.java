package com.aitshiroku.thai_alphabet_block;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class ThaiAlphabetFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Block block : ThaiAlphabetBlockFabric.letterBlocksView()) {
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            ColorProviderRegistry.BLOCK.register(
                    (state, world, pos, tintIndex) -> {
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
            ColorProviderRegistry.ITEM.register(
                    (ItemStack stack, int tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (!(stack.getItem() instanceof BlockItem bi) || bi.getBlock() != block) {
                            net.minecraft.world.item.DyeColor white = net.minecraft.world.item.DyeColor.WHITE;
                            return tintIndex == 0
                                    ? ThaiAlphabetColorUtil.backgroundArgbFromDye(white)
                                    : ThaiAlphabetColorUtil.glyphArgbFromDye(white);
                        }
                        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        net.minecraft.world.item.DyeColor dye = state.hasProperty(ThaiLetterBlock.COLOR)
                                ? state.getValue(ThaiLetterBlock.COLOR)
                                : net.minecraft.world.item.DyeColor.WHITE;
                        return tintIndex == 0
                                ? ThaiAlphabetColorUtil.backgroundArgbFromDye(dye)
                                : ThaiAlphabetColorUtil.glyphArgbFromDye(dye);
                    },
                    block.asItem());
        }
    }
}
