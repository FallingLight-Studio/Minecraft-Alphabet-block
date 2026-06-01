package com.aitshiroku.ThaiAlphabetBlock;

import java.util.LinkedHashMap;
import java.util.Map;

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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(ThaiAlphabetCommon.MOD_ID)
public class ThaiAlphabetBlock {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
                        ForgeRegistries.BLOCKS,
                        ThaiAlphabetCommon.MOD_ID);

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
                        ForgeRegistries.ITEMS,
                        ThaiAlphabetCommon.MOD_ID);

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
                        Registries.CREATIVE_MODE_TAB,
                        ThaiAlphabetCommon.MOD_ID);

        private static final Map<String, RegistryObject<Item>> REGISTERED_ITEMS = new LinkedHashMap<>();

        static {
                registerCharacterBlocks(ThaiAlphabetDefinitions.all());
        }

        public static final RegistryObject<CreativeModeTab> THAI_ALPHABET_TAB = CREATIVE_MODE_TABS
                        .register("thai_alphabet_tab", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.thai_alphabet_block"))
                                        .icon(() -> {
                                                RegistryObject<Item> firstItem = REGISTERED_ITEMS.get(
                                                                "consonant_ko_kai");
                                                return firstItem != null
                                                                ? firstItem.get().getDefaultInstance()
                                                                : ItemStack.EMPTY;
                                        })
                                        .displayItems((params, output) -> {
                                                for (RegistryObject<Item> registeredItem : REGISTERED_ITEMS.values()) {
                                                        output.accept(registeredItem.get());
                                                }
                                        })
                                        .build());

        public ThaiAlphabetBlock(FMLJavaModLoadingContext context) {
                var modEventBus = context.getModEventBus();
                BLOCKS.register(modEventBus);
                ITEMS.register(modEventBus);
                CREATIVE_MODE_TABS.register(modEventBus);
        }

        private static void registerCharacterBlocks(
                        Iterable<ThaiAlphabetDefinitions.CharacterDef> definitions) {
                for (ThaiAlphabetDefinitions.CharacterDef def : definitions) {
                        RegistryObject<Block> registeredBlock = BLOCKS.register(
                                        def.id(),
                                        () -> {
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
                        RegistryObject<Item> registeredItem = ITEMS.register(def.id(),
                                        () -> new BlockItem(registeredBlock.get(), new Item.Properties().useBlockDescriptionPrefix()));
                        REGISTERED_ITEMS.put(def.id(), registeredItem);
                }
        }
}
