package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ThaiAlphabetCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ThaiAlphabetForgeClient {

    private ThaiAlphabetForgeClient() {}

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (RegistryObject<Block> ro : ThaiAlphabetBlock.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            event.register(
                    (state, level, pos, tintIndex) -> {
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
        }
    }

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
                                if (tintIndex != 0) {
                                    return -1;
                                }
                                BlockState state = blockStateFromStack(stack, block);
                                if (state != null && state.hasProperty(ThaiLetterBlock.COLOR)) {
                                    return ThaiAlphabetColorUtil.argbFromDye(state.getValue(ThaiLetterBlock.COLOR));
                                }
                                return ThaiAlphabetColorUtil.argbFromDye(
                                        net.minecraft.world.item.DyeColor.WHITE);
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
