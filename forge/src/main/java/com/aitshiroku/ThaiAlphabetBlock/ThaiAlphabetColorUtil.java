package com.aitshiroku.ThaiAlphabetBlock;

import net.minecraft.world.item.DyeColor;

public final class ThaiAlphabetColorUtil {

    private ThaiAlphabetColorUtil() {}

    public static int backgroundArgbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye, false);
    }

    public static int backgroundArgbFromDye(DyeColor dye, boolean dyed) {
        if (!dyed && dye == DyeColor.WHITE) {
            return 0xD3B187;
        }
        float[] c = dye.getTextureDiffuseColors();
        int r = (int) (c[0] * 255.0F) & 255;
        int g = (int) (c[1] * 255.0F) & 255;
        int b = (int) (c[2] * 255.0F) & 255;
        return (r << 16) | (g << 8) | b;
    }

    public static int glyphArgbFromDye(DyeColor dye) {
        return glyphArgbFromDye(dye, false);
    }

    public static int glyphArgbFromDye(DyeColor dye, boolean dyed) {
        if (!dyed && dye == DyeColor.WHITE) {
            return 0x2D1A11;
        }
        float[] c = dye.getTextureDiffuseColors();
        int r = (int) (c[0] * 255.0F) & 255;
        int g = (int) (c[1] * 255.0F) & 255;
        int b = (int) (c[2] * 255.0F) & 255;
        return (r << 16) | (g << 8) | b;
    }

    @Deprecated
    public static int argbFromDye(DyeColor dye) {
        return backgroundArgbFromDye(dye, false);
    }
}
