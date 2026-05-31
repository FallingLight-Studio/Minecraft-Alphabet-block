package com.aitshiroku.thai_alphabet_block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ThaiLetterBlock extends Block {

    public static final EnumProperty<DyeColor> COLOR = ThaiAlphabetColorProperties.COLOR;
    public static final EnumProperty<DyeColor> GLYPH_COLOR = ThaiAlphabetColorProperties.GLYPH_COLOR;
    public static final BooleanProperty COLOR_DYED = ThaiAlphabetColorProperties.COLOR_DYED;
    public static final BooleanProperty GLYPH_DYED = ThaiAlphabetColorProperties.GLYPH_DYED;

    public ThaiLetterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(COLOR, DyeColor.WHITE)
                .setValue(GLYPH_COLOR, DyeColor.WHITE)
                .setValue(COLOR_DYED, Boolean.FALSE)
                .setValue(GLYPH_DYED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, GLYPH_COLOR, COLOR_DYED, GLYPH_DYED);
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor newColor = dyeItem.getDyeColor();
            if (state.getValue(COLOR_DYED) && state.getValue(COLOR) == newColor) {
                return InteractionResult.CONSUME;
            }
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(COLOR, newColor).setValue(COLOR_DYED, Boolean.TRUE), 3);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
