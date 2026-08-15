package com.aitshiroku.ColoredAlphabetBlocks;

public final class AlphabetCommon {
    /**
     * The single source of truth for the mod id. This doubles as the registry
     * namespace, the assets/ and data/ folder name, and the lang key prefix, so
     * never hardcode it anywhere else.
     */
    public static final String MOD_ID = "colored_alphabet_blocks";

    /**
     * The mod id shipped in 1.0.0, kept only so the Forge remap handler can
     * recognise blocks and items saved by that version. Nothing else should
     * reference this.
     */
    public static final String LEGACY_MOD_ID = "alphabet_block";

    private AlphabetCommon() {
    }
}
