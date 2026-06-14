package com.aitshiroku.AlphabetBlock;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class AlphabetColorProperties {

    public enum BlockColor implements StringRepresentable {
        NONE("none", null),
        WHITE("white", DyeColor.WHITE),
        ORANGE("orange", DyeColor.ORANGE),
        MAGENTA("magenta", DyeColor.MAGENTA),
        LIGHT_BLUE("light_blue", DyeColor.LIGHT_BLUE),
        YELLOW("yellow", DyeColor.YELLOW),
        LIME("lime", DyeColor.LIME),
        PINK("pink", DyeColor.PINK),
        GRAY("gray", DyeColor.GRAY),
        LIGHT_GRAY("light_gray", DyeColor.LIGHT_GRAY),
        CYAN("cyan", DyeColor.CYAN),
        PURPLE("purple", DyeColor.PURPLE),
        BLUE("blue", DyeColor.BLUE),
        BROWN("brown", DyeColor.BROWN),
        GREEN("green", DyeColor.GREEN),
        RED("red", DyeColor.RED),
        BLACK("black", DyeColor.BLACK);

        private final String name;
        private final DyeColor dyeColor;

        BlockColor(String name, DyeColor dyeColor) {
            this.name = name;
            this.dyeColor = dyeColor;
        }

        public DyeColor toDyeColor() {
            return this.dyeColor;
        }

        public static BlockColor fromDyeColor(DyeColor dyeColor) {
            for (BlockColor color : values()) {
                if (color.dyeColor == dyeColor) {
                    return color;
                }
            }
            return NONE;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static final EnumProperty<BlockColor> COLOR =
            EnumProperty.create("color", BlockColor.class);

    public static final EnumProperty<DyeColor> GLYPH_COLOR =
            EnumProperty.create("glyph_color", DyeColor.class);

    private AlphabetColorProperties() {}
}
