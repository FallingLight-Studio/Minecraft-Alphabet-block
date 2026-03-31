package com.aitshiroku.thai_alphabet_block;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ThaiAlphabetBlockFabric implements ModInitializer {
    private static final Map<String, Item> REGISTERED_ITEMS = new LinkedHashMap<>();

    @Override
    public void onInitialize() {
        for (ThaiAlphabetDefinitions.CharacterDef def : ThaiAlphabetDefinitions.all()) {
            ResourceLocation id = new ResourceLocation(ThaiAlphabetCommon.MOD_ID, def.id());
            Block block = def.type() == ThaiAlphabetDefinitions.CharacterType.CONSONANT
                    ? new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1.5f, 6.0f))
                    : new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.5f, 6.0f));
            Registry.register(BuiltInRegistries.BLOCK, id, block);

            Item item = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, item);
            REGISTERED_ITEMS.put(def.id(), item);
        }

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            for (Item item : REGISTERED_ITEMS.values()) {
                entries.accept(item);
            }
        });
    }
}
