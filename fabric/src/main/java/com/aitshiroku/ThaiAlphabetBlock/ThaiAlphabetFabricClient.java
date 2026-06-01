package com.aitshiroku.ThaiAlphabetBlock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.color.item.ItemTintSource;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public final class ThaiAlphabetFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register custom item tint sources
        ItemTintSources.ID_MAPPER.put(
            ResourceLocation.fromNamespaceAndPath("thai_alphabet_block", "background_tint"),
            MapCodec.unit(new ItemTintSource() {
                @Override
                public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity entity) {
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        Block block = blockItem.getBlock();
                        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        if (state.hasProperty(ThaiLetterBlock.COLOR)) {
                            ThaiAlphabetColorProperties.ThaiBlockColor color = state.getValue(ThaiLetterBlock.COLOR);
                            return ThaiAlphabetColorUtil.backgroundArgbFromColor(color);
                        }
                    }
                    return ThaiAlphabetColorUtil.backgroundArgbFromColor(ThaiAlphabetColorProperties.ThaiBlockColor.NONE);
                }

                @Override
                public MapCodec<? extends ItemTintSource> type() {
                    return MapCodec.unit(this);
                }
            })
        );

        ItemTintSources.ID_MAPPER.put(
            ResourceLocation.fromNamespaceAndPath("thai_alphabet_block", "glyph_tint"),
            MapCodec.unit(new ItemTintSource() {
                @Override
                public int calculate(ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity entity) {
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        Block block = blockItem.getBlock();
                        BlockState state = ThaiAlphabetBlockStateUtil.stateFromItemStack(stack, block);
                        if (state.hasProperty(ThaiLetterBlock.GLYPH_COLOR)) {
                            DyeColor glyphDye = state.getValue(ThaiLetterBlock.GLYPH_COLOR);
                            return ThaiAlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    }
                    return ThaiAlphabetColorUtil.glyphArgbFromDye(DyeColor.BLACK);
                }

                @Override
                public MapCodec<? extends ItemTintSource> type() {
                    return MapCodec.unit(this);
                }
            })
        );

        for (Block block : ThaiAlphabetBlockFabric.letterBlocksView()) {
            if (!(block instanceof ThaiLetterBlock)) {
                continue;
            }
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
            ColorProviderRegistry.BLOCK.register(
                    (state, world, pos, tintIndex) -> {
                        if (tintIndex != 0 && tintIndex != 1) {
                            return -1;
                        }
                        if (tintIndex == 0) {
                            // Background color
                            ThaiAlphabetColorProperties.ThaiBlockColor color = state.hasProperty(ThaiLetterBlock.COLOR)
                                    ? state.getValue(ThaiLetterBlock.COLOR)
                                    : ThaiAlphabetColorProperties.ThaiBlockColor.NONE;
                            return ThaiAlphabetColorUtil.backgroundArgbFromColor(color);
                        } else {
                            // Glyph color
                            DyeColor glyphDye = state.hasProperty(ThaiLetterBlock.GLYPH_COLOR)
                                    ? state.getValue(ThaiLetterBlock.GLYPH_COLOR)
                                    : DyeColor.BLACK;
                            return ThaiAlphabetColorUtil.glyphArgbFromDye(glyphDye);
                        }
                    },
                    block);
        }
    }
}
