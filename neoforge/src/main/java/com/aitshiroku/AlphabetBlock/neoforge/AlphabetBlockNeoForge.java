package com.aitshiroku.AlphabetBlock.neoforge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(AlphabetCommon.MOD_ID)
public class AlphabetBlockNeoForge {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(
            AlphabetCommon.MOD_ID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
            AlphabetCommon.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            AlphabetCommon.MOD_ID);

    private static final Map<String, DeferredItem<Item>> REGISTERED_ITEMS = new LinkedHashMap<>();

    static {
        registerCharacterBlocks(AlphabetDefinitions.all());
    }

    public static final Supplier<CreativeModeTab> ALPHABET_TAB = CREATIVE_MODE_TABS
            .register("alphabet_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.alphabet_block"))
                    .icon(() -> {
                        DeferredItem<Item> firstItem = REGISTERED_ITEMS.get(
                                "letter_a");
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

    public AlphabetBlockNeoForge(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        if (net.neoforged.fml.loading.FMLEnvironment.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
            AlphabetNeoForgeClient.registerListeners(modEventBus);
        }

        // Register glyph dye handler on the game event bus (not the mod bus)
        NeoForge.EVENT_BUS.register(AlphabetGlyphDyeHandler.class);
    }

    private static void registerCharacterBlocks(
            Iterable<AlphabetDefinitions.CharacterDef> definitions) {
        for (AlphabetDefinitions.CharacterDef def : definitions) {
            DeferredBlock<Block> registeredBlock = BLOCKS.register(
                    def.id(),
                    id -> {
                        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                                .setId(ResourceKey.create(Registries.BLOCK, id))
                                .mapColor(MapColor.COLOR_LIGHT_GRAY)
                                .strength(1.5f, 6.0f);
                        return new LetterBlock(props);
                    });
            DeferredItem<Item> registeredItem = ITEMS.register(def.id(),
                    () -> {
                        ResourceKey<Item> itemKey = ResourceKey.create(
                            Registries.ITEM,
                            Identifier.fromNamespaceAndPath(AlphabetCommon.MOD_ID, def.id())
                        );
                        return new BlockItem(registeredBlock.get(), new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
                    });
            REGISTERED_ITEMS.put(def.id(), registeredItem);
        }
    }
}
