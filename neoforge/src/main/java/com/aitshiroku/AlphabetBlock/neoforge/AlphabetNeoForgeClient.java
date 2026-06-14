package com.aitshiroku.AlphabetBlock.neoforge;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.minecraft.resources.Identifier;
import com.mojang.serialization.MapCodec;

public final class AlphabetNeoForgeClient {

    private AlphabetNeoForgeClient() {
    }

    public static void registerListeners(IEventBus modEventBus) {
        modEventBus.addListener(AlphabetNeoForgeClient::onClientSetup);
        modEventBus.addListener(AlphabetNeoForgeClient::registerItemTintSources);
        modEventBus.addListener(AlphabetNeoForgeClient::registerBlockColors);
    }

    @SuppressWarnings("deprecation")
    public static void onClientSetup(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : AlphabetBlockNeoForge.BLOCKS
                    .getEntries()) {
                net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ro.get(),
                        net.minecraft.client.renderer.chunk.ChunkSectionLayer.CUTOUT);
            }
        });
    }

    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(
                Identifier.fromNamespaceAndPath("alphabet_block", "background_tint"),
                MapCodec.unit(new net.minecraft.client.color.item.ItemTintSource() {
                    @Override
                    public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level,
                            net.minecraft.world.entity.LivingEntity entity) {
                        if (stack.getItem() instanceof BlockItem blockItem) {
                            Block block = blockItem.getBlock();
                            BlockState state = AlphabetBlockStateUtil.stateFromItemStack(stack, block);
                            if (state.hasProperty(LetterBlock.COLOR)) {
                                AlphabetColorProperties.BlockColor color = state
                                        .getValue(LetterBlock.COLOR);
                                return AlphabetColorUtil.backgroundArgbFromColor(color);
                            }
                        }
                        return AlphabetColorUtil
                                .backgroundArgbFromColor(AlphabetColorProperties.BlockColor.NONE);
                    }

                    @Override
                    public MapCodec<? extends net.minecraft.client.color.item.ItemTintSource> type() {
                        return MapCodec.unit(this);
                    }
                }));

        event.register(
                Identifier.fromNamespaceAndPath("alphabet_block", "glyph_tint"),
                MapCodec.unit(new net.minecraft.client.color.item.ItemTintSource() {
                    @Override
                    public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level,
                            net.minecraft.world.entity.LivingEntity entity) {
                        if (stack.getItem() instanceof BlockItem blockItem) {
                            Block block = blockItem.getBlock();
                            BlockState state = AlphabetBlockStateUtil.stateFromItemStack(stack, block);
                            if (state.hasProperty(LetterBlock.GLYPH_COLOR)) {
                                DyeColor glyphDye = state.getValue(LetterBlock.GLYPH_COLOR);
                                return AlphabetColorUtil.glyphArgbFromDye(glyphDye);
                            }
                        }
                        return AlphabetColorUtil.glyphArgbFromDye(DyeColor.BLACK);
                    }

                    @Override
                    public MapCodec<? extends net.minecraft.client.color.item.ItemTintSource> type() {
                        return MapCodec.unit(this);
                    }
                }));
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : AlphabetBlockNeoForge.BLOCKS
                .getEntries()) {
            Block block = ro.get();
            if (!(block instanceof LetterBlock)) {
                continue;
            }
            event.register(
                    (state, level, pos, tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (tintIndex == 0) {
                            // Background color
                            AlphabetColorProperties.BlockColor color = state.hasProperty(LetterBlock.COLOR)
                                    ? state.getValue(LetterBlock.COLOR)
                                    : AlphabetColorProperties.BlockColor.NONE;
                            return AlphabetColorUtil.backgroundArgbFromColor(color);
                        } else {
                            // Glyph color
                            DyeColor glyphDye = state.hasProperty(LetterBlock.GLYPH_COLOR)
                                    ? state.getValue(LetterBlock.GLYPH_COLOR)
                                    : DyeColor.BLACK;
                            return AlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block);
        }
    }
}
