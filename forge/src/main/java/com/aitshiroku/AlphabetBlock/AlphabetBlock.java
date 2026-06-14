package com.aitshiroku.AlphabetBlock;

import java.util.LinkedHashMap;
import java.util.Map;

import com.aitshiroku.alphabet_block.AlphabetCommon;
import com.aitshiroku.alphabet_block.AlphabetDefinitions;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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

        public AlphabetBlock(FMLJavaModLoadingContext context) {
                var modBusGroup = context.getModBusGroup();
                BLOCKS.register(modBusGroup);
                ITEMS.register(modBusGroup);
                CREATIVE_MODE_TABS.register(modBusGroup);

                if (net.minecraftforge.fml.loading.FMLEnvironment.dist == net.minecraftforge.api.distmarker.Dist.CLIENT) {
                        AlphabetForgeClient.registerListeners();
                }
        }

        private static void registerCharacterBlocks(
                        Iterable<AlphabetDefinitions.CharacterDef> definitions) {
                for (AlphabetDefinitions.CharacterDef def : definitions) {
                        Identifier id = Identifier.fromNamespaceAndPath(AlphabetCommon.MOD_ID, def.id());
                        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
                        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

                        RegistryObject<Block> registeredBlock = BLOCKS.register(
                                         def.id(),
                                         () -> {
                                                 BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                                                                 .setId(blockKey)
                                                                 .mapColor(MapColor.COLOR_LIGHT_GRAY)
                                                                 .strength(1.5f, 6.0f);
                                                 return new LetterBlock(props);
                                         });
                        RegistryObject<Item> registeredItem = ITEMS.register(def.id(),
                                         () -> new BlockItem(registeredBlock.get(), new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
                        REGISTERED_ITEMS.put(def.id(), registeredItem);
                }
        }
}
