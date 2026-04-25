package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {}

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

    public static int glyphArgbFromDye(DyeColor dye) {
        return 0xFFFFFFFF;
    }

    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
