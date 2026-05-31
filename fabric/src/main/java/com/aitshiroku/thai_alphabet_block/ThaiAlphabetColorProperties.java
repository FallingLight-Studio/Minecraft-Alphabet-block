package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class ThaiAlphabetColorProperties {

    public static final EnumProperty<DyeColor> COLOR =
            EnumProperty.create("color", DyeColor.class);

    public static final EnumProperty<DyeColor> GLYPH_COLOR =
            EnumProperty.create("glyph_color", DyeColor.class);

    public static final BooleanProperty COLOR_DYED =
            BooleanProperty.create("color_dyed");

    public static final BooleanProperty GLYPH_DYED =
            BooleanProperty.create("glyph_dyed");

    private ThaiAlphabetColorProperties() {}
}
