package com.aitshiroku.ThaiAlphabetBlock.neoforge;

import com.aitshiroku.thai_alphabet_block.ThaiAlphabetCommon;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ThaiAlphabetCommon.MOD_ID, value = Dist.CLIENT)
public final class ThaiAlphabetNeoForgeClient {

    private ThaiAlphabetNeoForgeClient() {
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : ThaiAlphabetBlockNeoForge.BLOCKS.getEntries()) {
                ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.cutout());
            }
        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (net.neoforged.neoforge.registries.DeferredHolder<Block, ? extends Block> ro : ThaiAlphabetBlockNeoForge.BLOCKS.getEntries()) {
            Block block = ro.get();
            if (!(block instanceof ThaiLetterBlock) && !(block instanceof ThaiLetterSlabBlock)) {
                continue;
            }
            event.register(
                    (state, level, pos, tintIndex) -> {
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
