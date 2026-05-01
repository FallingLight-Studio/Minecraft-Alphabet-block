package com.aitshiroku.thai_alphabet_block.neoforge;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class ThaiAlphabetColorProperties {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private ThaiAlphabetColorProperties() {
    }
}
