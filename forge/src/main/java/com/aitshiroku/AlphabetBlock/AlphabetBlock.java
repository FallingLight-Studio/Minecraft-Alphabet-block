package com.aitshiroku.AlphabetBlock;

import java.util.LinkedHashMap;
import java.util.Map;

import com.aitshiroku.alphabet_block.AlphabetCommon;
import com.aitshiroku.alphabet_block.AlphabetDefinitions;

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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(AlphabetCommon.MOD_ID)
public class AlphabetBlock {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
                        ForgeRegistries.BLOCKS,
                        AlphabetCommon.MOD_ID);

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
                        ForgeRegistries.ITEMS,
                        AlphabetCommon.MOD_ID);

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
                        Registries.CREATIVE_MODE_TAB,
                        AlphabetCommon.MOD_ID);

        private static final Map<String, RegistryObject<Item>> REGISTERED_ITEMS = new LinkedHashMap<>();

        static {
                registerCharacterBlocks(AlphabetDefinitions.all());
        }

        public static final RegistryObject<CreativeModeTab> ALPHABET_TAB = CREATIVE_MODE_TABS
                        .register("alphabet_tab", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.alphabet_block"))
                                        .icon(() -> {
                                                RegistryObject<Item> firstItem = REGISTERED_ITEMS.get(
                                                                "letter_a");
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

        public AlphabetBlock() {
                IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
                BLOCKS.register(modEventBus);
                ITEMS.register(modEventBus);
                CREATIVE_MODE_TABS.register(modEventBus);
        }

        private static void registerCharacterBlocks(
                        Iterable<AlphabetDefinitions.CharacterDef> definitions) {
                for (AlphabetDefinitions.CharacterDef def : definitions) {
                        RegistryObject<Block> registeredBlock = BLOCKS.register(
                                         def.id(),
                                         () -> new LetterBlock(BlockBehaviour.Properties.of()
                                                         .mapColor(MapColor.COLOR_LIGHT_GRAY)
                                                         .strength(1.5f, 6.0f)));
                        RegistryObject<Item> registeredItem = ITEMS.register(def.id(),
                                         () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
                        REGISTERED_ITEMS.put(def.id(), registeredItem);
                }
        }
}
