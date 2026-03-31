package com.aitshiroku.thai_alphabet_block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod(ThaiAlphabetCommon.MOD_ID)
public class ThaiAlphabetBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ThaiAlphabetCommon.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ThaiAlphabetCommon.MOD_ID);
    private static final Map<String, RegistryObject<Item>> REGISTERED_ITEMS = new LinkedHashMap<>();

    static {
        registerCharacterBlocks(ThaiAlphabetDefinitions.all());
    }

    public ThaiAlphabetBlock() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private static void registerCharacterBlocks(Iterable<ThaiAlphabetDefinitions.CharacterDef> definitions) {
        for (ThaiAlphabetDefinitions.CharacterDef def : definitions) {
            RegistryObject<Block> registeredBlock = BLOCKS.register(def.id(), () -> {
                BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                        .mapColor(def.type() == ThaiAlphabetDefinitions.CharacterType.CONSONANT ? MapColor.COLOR_LIGHT_GRAY : MapColor.COLOR_PURPLE)
                        .strength(1.5f, 6.0f);
                if (def.type() == ThaiAlphabetDefinitions.CharacterType.CONSONANT) {
                    return new Block(props);
                }
                return new SlabBlock(props);
            });
            RegistryObject<Item> registeredItem = ITEMS.register(def.id(),
                    () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
            REGISTERED_ITEMS.put(def.id(), registeredItem);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            for (RegistryObject<Item> registeredItem : REGISTERED_ITEMS.values()) {
                event.accept(registeredItem.get());
            }
        }
    }
}
