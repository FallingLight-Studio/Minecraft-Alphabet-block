package com.aitshiroku.ColoredAlphabetBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.MissingMappingsEvent;

/**
 * Remaps registry entries saved under the 1.0.0 mod id ({@code alphabet_block})
 * onto their 2.0.0 equivalents ({@code colored_alphabet_blocks}).
 *
 * <p>Without this, every block placed in a world by 1.0.0 would be reported as
 * a missing registry entry and silently deleted on load, and every item in a
 * chest or inventory would vanish. Entry paths did not change in 2.0.0, so the
 * mapping is a straight namespace swap.
 *
 * <p>Forge is the only one of the three loaders that still exposes a name-remap
 * hook. NeoForge removed {@code MissingMappingsEvent} and Fabric never had an
 * equivalent, so worlds on those loaders cannot be migrated in-process — see
 * the migration notes in the changelog.
 */
public final class AlphabetLegacyRemapHandler {

    private AlphabetLegacyRemapHandler() {
    }

    /**
     * Called from the mod constructor. {@link MissingMappingsEvent#BUS} is a
     * global bus in Forge 1.21.11+, so this does not go through the mod bus.
     */
    public static void register() {
        MissingMappingsEvent.BUS.addListener(AlphabetLegacyRemapHandler::onMissingMappings);
    }

    private static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, Registries.BLOCK);
        remap(event, Registries.ITEM);
    }

    private static <T> void remap(MissingMappingsEvent event,
                                  ResourceKey<? extends Registry<T>> registryKey) {
        for (MissingMappingsEvent.Mapping<T> mapping :
                event.getMappings(registryKey, AlphabetCommon.LEGACY_MOD_ID)) {
            Identifier renamed = Identifier.fromNamespaceAndPath(
                    AlphabetCommon.MOD_ID, mapping.getKey().getPath());
            T replacement = mapping.getRegistry().getValue(renamed);
            if (replacement != null) {
                mapping.remap(replacement);
            } else {
                // Path no longer exists under the new id. Warn rather than fail
                // so one unknown entry cannot make the whole world unloadable.
                mapping.warn();
            }
        }
    }
}
