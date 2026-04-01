package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {}

    public static int backgroundArgbFromDye(DyeColor dye) {
        float[] c = dye.getTextureDiffuseColors();
        int r = (int) (c[0] * 255.0F) & 255;
        int g = (int) (c[1] * 255.0F) & 255;
        int b = (int) (c[2] * 255.0F) & 255;
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static int glyphArgbFromDye(DyeColor dye) {
        float[] c = dye.getTextureDiffuseColors();
        float lum = 0.2126f * c[0] + 0.7152f * c[1] + 0.0722f * c[2];
        if (lum < 0.45f) {
            return 0xFFFFFFFF;
        }
        return 0xFF000000;
    }

    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye);
    }
}
