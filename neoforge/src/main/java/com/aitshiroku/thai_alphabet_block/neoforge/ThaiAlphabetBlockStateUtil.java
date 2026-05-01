package com.aitshiroku.thai_alphabet_block.neoforge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class ThaiAlphabetBlockStateUtil {

    private ThaiAlphabetBlockStateUtil() {
    }

    /**
     * Extracts a BlockState from an ItemStack using the 1.21.1 Data Component system.
     * In 1.21+, BlockStateTag NBT was replaced by DataComponents.BLOCK_STATE component.
     */
    public static BlockState stateFromItemStack(ItemStack stack, Block block) {
        BlockState state = block.defaultBlockState();
        // In 1.21.1, use the BLOCK_STATE data component instead of NBT BlockStateTag
        BlockItemStateProperties stateProps = stack.get(
                net.minecraft.core.component.DataComponents.BLOCK_STATE);
        if (stateProps == null) {
            return state;
        }
        // Apply all properties from the component
        return stateProps.apply(state);
    }
}
