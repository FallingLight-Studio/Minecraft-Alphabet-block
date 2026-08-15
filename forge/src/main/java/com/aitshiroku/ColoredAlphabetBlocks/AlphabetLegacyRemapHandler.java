package com.aitshiroku.ColoredAlphabetBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

/**
 * Remaps registry entries saved under the 1.0.0 mod id ({@code alphabet_block})
 * onto their 2.0.0 equivalents ({@code colored_alphabet_blocks}).
 *
 * <p>Without this, every block placed in a world by 1.0.0 is reported as a
 * missing registry entry and replaced with air on load, and every item in a
 * chest or inventory disappears. Entry paths did not change in 2.0.0, so the
 * mapping is a straight namespace swap.
 *
 * <p>On this Forge version the event is posted on the game bus
 * ({@code MinecraftForge.EVENT_BUS}), not the mod bus, hence {@code Bus.FORGE}.
 *
 * <p>The replacement must be looked up in the <em>active</em> registry
 * ({@link ForgeRegistries}) rather than through {@code mapping.getRegistry()},
 * which is the staging registry built from the save and therefore does not
 * contain the 2.0.0 names yet.
 */
@Mod.EventBusSubscriber(modid = AlphabetCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AlphabetLegacyRemapHandler {

    private AlphabetLegacyRemapHandler() {
    }

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        remap(event, Registries.BLOCK, ForgeRegistries.BLOCKS);
        remap(event, Registries.ITEM, ForgeRegistries.ITEMS);
    }

    private static <T> void remap(MissingMappingsEvent event,
                                  ResourceKey<? extends Registry<T>> registryKey,
                                  IForgeRegistry<T> active) {
        for (MissingMappingsEvent.Mapping<T> mapping :
                event.getMappings(registryKey, AlphabetCommon.LEGACY_MOD_ID)) {
            ResourceLocation renamed = ResourceLocation.fromNamespaceAndPath(
                    AlphabetCommon.MOD_ID, mapping.getKey().getPath());

            if (active.containsKey(renamed)) {
                mapping.remap(active.getValue(renamed));
            } else {
                // Path no longer exists under the new id. Warn rather than fail
                // so one unknown entry cannot make the whole world unloadable.
                mapping.warn();
            }
        }
    }
}
