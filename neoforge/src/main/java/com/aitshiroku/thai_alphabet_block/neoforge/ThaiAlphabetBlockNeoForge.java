package com.aitshiroku.thai_alphabet_block.neoforge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.aitshiroku.thai_alphabet_block.ThaiAlphabetCommon;
import com.aitshiroku.thai_alphabet_block.ThaiAlphabetDefinitions;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ThaiAlphabetCommon.MOD_ID)
public class ThaiAlphabetBlockNeoForge {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(
            ThaiAlphabetCommon.MOD_ID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
            ThaiAlphabetCommon.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ThaiAlphabetCommon.MOD_ID);

    private static final Map<String, DeferredItem<Item>> REGISTERED_ITEMS = new LinkedHashMap<>();

    static {
        registerCharacterBlocks(ThaiAlphabetDefinitions.all());
    }

    public static final Supplier<CreativeModeTab> THAI_ALPHABET_TAB = CREATIVE_MODE_TABS
            .register("thai_alphabet_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thai_alphabet_block"))
                    .icon(() -> {
                        DeferredItem<Item> firstItem = REGISTERED_ITEMS.get(
                                "consonant_ko_kai");
                        return firstItem != null
                                ? firstItem.get().getDefaultInstance()
                                : ItemStack.EMPTY;
                    })
                    .displayItems((params, output) -> {
                        for (DeferredItem<Item> registeredItem : REGISTERED_ITEMS.values()) {
                            output.accept(registeredItem.get());
                        }
                    })
                    .build());

    public ThaiAlphabetBlockNeoForge(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    private static void registerCharacterBlocks(
            Iterable<ThaiAlphabetDefinitions.CharacterDef> definitions) {
        for (ThaiAlphabetDefinitions.CharacterDef def : definitions) {
            DeferredBlock<Block> registeredBlock = BLOCKS.register(
                    def.id(),
                    id -> {
                        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                                .mapColor(
                                        def.shape() == ThaiAlphabetDefinitions.LetterBlockShape.FULL
                                                ? MapColor.COLOR_LIGHT_GRAY
                                                : MapColor.COLOR_PURPLE)
                                .strength(1.5f, 6.0f);
                        if (def.shape() == ThaiAlphabetDefinitions.LetterBlockShape.FULL) {
                            return new ThaiLetterBlock(props);
                        }
                        return new ThaiLetterSlabBlock(props);
                    });
            DeferredItem<Item> registeredItem = ITEMS.register(def.id(),
                    () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
            REGISTERED_ITEMS.put(def.id(), registeredItem);
        }
    }
}
