package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {}

    /** ARGB tint for {@link net.minecraft.client.color.block.BlockColors} / item colors. */
    public static int argbFromDye(DyeColor dye) {
        float[] c = dye.getTextureDiffuseColors();
        int r = (int) (c[0] * 255.0F) & 255;
        int g = (int) (c[1] * 255.0F) & 255;
        int b = (int) (c[2] * 255.0F) & 255;
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
