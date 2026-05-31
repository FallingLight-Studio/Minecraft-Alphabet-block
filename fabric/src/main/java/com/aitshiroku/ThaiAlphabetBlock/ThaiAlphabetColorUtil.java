package com.aitshiroku.ThaiAlphabetBlock;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {}

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
        // In 1.21.1, getTextureDiffuseColor() returns a packed int (ARGB)
        int packed = dye.getTextureDiffuseColor();
        // Strip alpha channel, keep only RGB
        return 0xFF000000 | (packed & 0x00FFFFFF);
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

    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
