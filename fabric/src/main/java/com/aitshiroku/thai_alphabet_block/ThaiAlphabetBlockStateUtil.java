package com.aitshiroku.thai_alphabet_block;

import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class ThaiAlphabetBlockStateUtil {

    private ThaiAlphabetBlockStateUtil() {}

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BlockState stateFromItemStack(ItemStack stack, Block block) {
        BlockState state = block.defaultBlockState();
        CompoundTag root = stack.getTag();
        if (root == null || !root.contains("BlockStateTag", 10)) {
            return state;
        }
        CompoundTag stateTag = root.getCompound("BlockStateTag");
        for (Property property : state.getProperties()) {
            if (!stateTag.contains(property.getName())) {
                continue;
            }
            String value = stateTag.getString(property.getName());
            Optional optional = property.getValue(value);
            if (optional.isPresent()) {
                state = state.setValue(property, (Comparable) optional.get());
            }
        }
        return state;
    }
}
