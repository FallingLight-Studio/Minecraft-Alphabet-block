package com.aitshiroku.thai_alphabet_block.neoforge;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {
    }

    /**
     * Background layer (tintindex 0): the grayscale base texture is multiplied
     * by this color, producing a wood-panel pattern in the dye's shade.
     *
     * WHITE dye returns the original warm-brown wood color rather than neutral
     * gray, so an un-dyed block looks like natural wood.
     */
    public static int backgroundArgbFromDye(DyeColor dye) {
        if (dye == DyeColor.WHITE) {
            // Original wood tone: matches the texture_generator.html palette
            // so an un-dyed block looks warm and natural.
            return 0xD3B187;  // (211, 177, 135)
        }
        // In 1.21.1, getTextureDiffuseColor() returns a packed int (ARGB)
        int packed = dye.getTextureDiffuseColor();
        // Strip alpha channel, keep only RGB
        return packed & 0x00FFFFFF;
    }

    /**
     * Glyph layer (tintindex 1): always pass-through (white multiplier).
     * The glyph textures are pre-colored dark brown and should stay unchanged
     * regardless of the selected dye.
     */
    public static int glyphArgbFromDye(DyeColor dye) {
        return 0xFFFFFF;
    }

    /** @deprecated use {@link #backgroundArgbFromDye(DyeColor)} */
    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
