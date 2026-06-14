package com.aitshiroku.AlphabetBlock;

import com.aitshiroku.alphabet_block.AlphabetCommon;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.resources.Identifier;
import com.mojang.serialization.MapCodec;

@Mod.EventBusSubscriber(modid = AlphabetCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AlphabetForgeClient {

    private AlphabetForgeClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (net.minecraftforge.registries.RegistryObject<Block> ro : AlphabetBlock.BLOCKS.getEntries()) {
                net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ro.get(),
                        net.minecraft.client.renderer.chunk.ChunkSectionLayer.CUTOUT);
            }
        });
    }

    private static void registerItemTintSources() {
        // Register custom item tint sources synchronously before model baking
        net.minecraft.client.color.item.ItemTintSources.ID_MAPPER.put(
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

        net.minecraft.client.color.item.ItemTintSources.ID_MAPPER.put(
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

    public static void registerListeners() {
        registerItemTintSources();
        RegisterColorHandlersEvent.Block.BUS.addListener(AlphabetForgeClient::registerBlockColors);
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (RegistryObject<Block> ro : AlphabetBlock.BLOCKS.getEntries()) {
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
