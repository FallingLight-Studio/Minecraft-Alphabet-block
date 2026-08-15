package com.aitshiroku.ColoredAlphabetBlocks.neoforge;

import com.aitshiroku.ColoredAlphabetBlocks.AlphabetCommon;
import com.aitshiroku.ColoredAlphabetBlocks.AlphabetDefinitions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/**
 * Points the 1.0.0 registry names ({@code alphabet_block:*}) at their 2.0.0
 * equivalents ({@code colored_alphabet_blocks:*}) so worlds saved by 1.0.0 keep
 * their placed blocks and stored items.
 *
 * <p>NeoForge injects {@code IRegistryExtension} into every {@code Registry},
 * which patches {@code MappedRegistry} to run every lookup through
 * {@code resolve()}. An alias registered here therefore covers all lookup paths
 * at once — chunk palettes, item stacks, recipes, and tags — with no mixin.
 *
 * <p>Per the {@code addAlias} contract the alias only applies when the source
 * name is absent from the registry, so this cannot shadow a real entry.
 *
 * <p>Aliases resolve lazily at lookup time, so this may run before the deferred
 * registers have actually fired.
 */
public final class AlphabetLegacyAliasHandler {

    private AlphabetLegacyAliasHandler() {
    }

    public static void register() {
        for (AlphabetDefinitions.CharacterDef def : AlphabetDefinitions.all()) {
            ResourceLocation legacy = ResourceLocation.fromNamespaceAndPath(
                    AlphabetCommon.LEGACY_MOD_ID, def.id());
            ResourceLocation current = ResourceLocation.fromNamespaceAndPath(
                    AlphabetCommon.MOD_ID, def.id());

            BuiltInRegistries.BLOCK.addAlias(legacy, current);
            BuiltInRegistries.ITEM.addAlias(legacy, current);
        }
    }
}
