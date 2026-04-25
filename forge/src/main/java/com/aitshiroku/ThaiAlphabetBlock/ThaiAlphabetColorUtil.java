package com.aitshiroku.ThaiAlphabetBlock;

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
            return 0xFFD3B187;  // (211, 177, 135)
        }
        float[] c = dye.getTextureDiffuseColors();
        int r = (int) (c[0] * 255.0F) & 255;
        int g = (int) (c[1] * 255.0F) & 255;
        int b = (int) (c[2] * 255.0F) & 255;
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * Glyph layer (tintindex 1): always pass-through (white multiplier).
     * The glyph textures are pre-colored dark brown and should stay unchanged
     * regardless of the selected dye.
     */
    public static int glyphArgbFromDye(DyeColor dye) {
        return 0xFFFFFFFF;
    }

    /** @deprecated use {@link #backgroundArgbFromDye(DyeColor)} */
    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
