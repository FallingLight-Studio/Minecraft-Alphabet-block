package com.aitshiroku.AlphabetBlock;

import net.minecraft.world.item.DyeColor;

public final class AlphabetColorUtil {

    private AlphabetColorUtil() {
    }

    /**
     * Background layer (tintindex 0): the grayscale base texture is multiplied
     * by this color, producing a wood-panel pattern in the dye's shade.
     *
     * NONE returns the original warm-brown wood color rather than neutral
     * gray, so an un-dyed block looks like natural wood.
     * WHITE dye returns neutral white so the block background looks white.
     */
    public static int backgroundArgbFromColor(AlphabetColorProperties.BlockColor color) {
        if (color == AlphabetColorProperties.BlockColor.NONE) {
            // Original wood tone: matches the texture_generator.html palette
            // so an un-dyed block looks warm and natural.
            return 0xFFD3B187;  // (211, 177, 135)
        }
        if (color == AlphabetColorProperties.BlockColor.WHITE) {
            // White dye matches true white (allows player to make background white)
            return 0xFFFFFFFF;
        }
        DyeColor dye = color.toDyeColor();
        if (dye == null) {
            return 0xFFFFFFFF;
        }
        // In 1.21.1, getTextureDiffuseColor() returns a packed int (ARGB)
        int packed = dye.getTextureDiffuseColor();
        // Strip alpha channel, keep only RGB
        return 0xFF000000 | (packed & 0x00FFFFFF);
    }

    @Deprecated
    public static int backgroundArgbFromDye(DyeColor dye) {
        return backgroundArgbFromColor(AlphabetColorProperties.BlockColor.fromDyeColor(dye));
    }

    /**
     * Glyph layer (tintindex 1): applies selected dye color to the glyph.
     * Glyph textures are white-on-alpha, so the tint color IS the visible color.
     * WHITE dye returns the original dark brown to preserve the classic look.
     */
    public static int glyphArgbFromDye(DyeColor dye) {
        if (dye == DyeColor.WHITE) {
            // White Dye matches true white (allows player to make text white)
            return 0xFFFFFFFF;
        }
        if (dye == DyeColor.BROWN) {
            // Brighter warm brown for brown dye (Red: 122, Green: 75, Blue: 40)
            return 0xFF7A4B28;
        }
        // Apply dye color to glyph layer
        int packed = dye.getTextureDiffuseColor();
        return 0xFF000000 | (packed & 0x00FFFFFF);
    }

    /** @deprecated use {@link #backgroundArgbFromDye(DyeColor)} */
    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
