package com.aitshiroku.ColoredAlphabetBlocks;

import com.aitshiroku.ColoredAlphabetBlocks.AlphabetCommon;

import net.minecraft.resources.Identifier;

/**
 * Shared helper for the registry-alias mixins.
 */
public final class LegacyIds {

    private LegacyIds() {
    }

    /**
     * Returns the 2.0.0 equivalent of a 1.0.0 name, or null when the name has
     * nothing to do with this mod.
     *
     * <p>Hot path — every registry lookup in the game reaches this, so the
     * namespace comparison short-circuits before anything is allocated.
     */
    public static Identifier rename(Identifier name) {
        if (name == null || !AlphabetCommon.LEGACY_MOD_ID.equals(name.getNamespace())) {
            return null;
        }
        return Identifier.fromNamespaceAndPath(AlphabetCommon.MOD_ID, name.getPath());
    }
}
