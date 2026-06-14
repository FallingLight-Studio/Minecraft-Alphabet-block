package com.aitshiroku.AlphabetBlock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.color.item.ItemTintSource;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

public final class AlphabetFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register custom item tint sources
        ItemTintSources.ID_MAPPER.put(
            Identifier.fromNamespaceAndPath("alphabet_block", "background_tint"),
            MapCodec.unit(new ItemTintSource() {
                @Override
                public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity entity) {
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        Block block = blockItem.getBlock();
                        BlockState state = AlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        if (state.hasProperty(LetterBlock.COLOR)) {
                            AlphabetColorProperties.BlockColor color = state.getValue(LetterBlock.COLOR);
                            return AlphabetColorUtil.backgroundArgbFromColor(color);
                        }
                    }
                    return AlphabetColorUtil.backgroundArgbFromColor(AlphabetColorProperties.BlockColor.NONE);
                }

                @Override
                public MapCodec<? extends ItemTintSource> type() {
                    return MapCodec.unit(this);
                }
            })
        );

        ItemTintSources.ID_MAPPER.put(
            Identifier.fromNamespaceAndPath("alphabet_block", "glyph_tint"),
            MapCodec.unit(new ItemTintSource() {
                @Override
                public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity entity) {
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
                public MapCodec<? extends ItemTintSource> type() {
                    return MapCodec.unit(this);
                }
            })
        );

        for (Block block : AlphabetBlockFabric.letterBlocksView()) {
            if (!(block instanceof LetterBlock)) {
                continue;
            }
            BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
            ColorProviderRegistry.BLOCK.register(
                    (state, world, pos, tintIndex) -> {
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
