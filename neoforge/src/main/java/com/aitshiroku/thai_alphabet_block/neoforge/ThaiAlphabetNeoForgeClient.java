package com.aitshiroku.thai_alphabet_block.neoforge;

import com.aitshiroku.thai_alphabet_block.ThaiAlphabetCommon;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = ThaiAlphabetCommon.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ThaiAlphabetNeoForgeClient {

    private ThaiAlphabetNeoForgeClient() {
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : ThaiAlphabetBlockNeoForge.BLOCKS.getEntries()) {
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

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : ThaiAlphabetBlockNeoForge.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            event.register(
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
