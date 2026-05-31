package com.aitshiroku.ThaiAlphabetBlock;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class ThaiAlphabetColorProperties {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public static final EnumProperty<DyeColor> GLYPH_COLOR = EnumProperty.create("glyph_color", DyeColor.class);

    private ThaiAlphabetColorProperties() {
    }
}
