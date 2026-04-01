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
                        if (tintIndex != 0) {
                            return -1;
                        }
                        if (state.hasProperty(ThaiLetterBlock.COLOR)) {
                            return ThaiAlphabetColorUtil.argbFromDye(state.getValue(ThaiLetterBlock.COLOR));
                        }
                        return ThaiAlphabetColorUtil.argbFromDye(
                                net.minecraft.world.item.DyeColor.WHITE);
                    },
                    block);
            ColorProviderRegistry.ITEM.register(
                    (ItemStack stack, int tintIndex) -> {
                        if (tintIndex != 0) {
                            return -1;
                        }
                        if (!(stack.getItem() instanceof BlockItem bi) || bi.getBlock() != block) {
                            return ThaiAlphabetColorUtil.argbFromDye(
                                    net.minecraft.world.item.DyeColor.WHITE);
                        }
                        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        if (state.hasProperty(ThaiLetterBlock.COLOR)) {
                            return ThaiAlphabetColorUtil.argbFromDye(state.getValue(ThaiLetterBlock.COLOR));
                        }
                        return ThaiAlphabetColorUtil.argbFromDye(
                                net.minecraft.world.item.DyeColor.WHITE);
                    },
                    block.asItem());
        }
    }
}
