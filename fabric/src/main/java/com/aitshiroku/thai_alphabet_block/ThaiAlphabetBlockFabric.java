package com.aitshiroku.thai_alphabet_block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ThaiAlphabetBlockFabric implements ModInitializer {

    public static final List<Block> REGISTERED_BLOCKS = new ArrayList<>();

    private static final Map<String, Item> REGISTERED_ITEMS =
        new LinkedHashMap<>();

    @Override
    public void onInitialize() {
        // Register all blocks and items
        for (ThaiAlphabetDefinitions.CharacterDef def : ThaiAlphabetDefinitions.all()) {
            // In 1.21.1, ResourceLocation constructor is private; use factory method
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                ThaiAlphabetCommon.MOD_ID,
                def.id()
            );

            Block block =
                def.shape() == ThaiAlphabetDefinitions.LetterBlockShape.FULL
                    ? new ThaiLetterBlock(
                          BlockBehaviour.Properties.of()
                              .mapColor(MapColor.COLOR_LIGHT_GRAY)
                              .strength(1.5f, 6.0f)
                      )
                    : new ThaiLetterSlabBlock(
                          BlockBehaviour.Properties.of()
                              .mapColor(MapColor.COLOR_PURPLE)
                              .strength(1.5f, 6.0f)
                      );
            Registry.register(BuiltInRegistries.BLOCK, id, block);
            REGISTERED_BLOCKS.add(block);

            Item item = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, item);
            REGISTERED_ITEMS.put(def.id(), item);
        }

        // Create tab ResourceKey
        ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(ThaiAlphabetCommon.MOD_ID, "thai_alphabet_tab")
        );

        // Register custom creative tab
        CreativeModeTab thaiAlphabetTab = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.thai_alphabet_block"))
            .icon(() -> {
                Item firstItem = REGISTERED_ITEMS.get("consonant_ko_kai");
                return firstItem != null
                    ? new ItemStack(firstItem)
                    : ItemStack.EMPTY;
            })
            .displayItems((params, output) -> {
                for (Item item : REGISTERED_ITEMS.values()) {
                    output.accept(item);
                }
            })
            .build();

        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            tabKey,
            thaiAlphabetTab
        );
    }

    public static List<Block> letterBlocksView() {
        return Collections.unmodifiableList(REGISTERED_BLOCKS);
    }
}
