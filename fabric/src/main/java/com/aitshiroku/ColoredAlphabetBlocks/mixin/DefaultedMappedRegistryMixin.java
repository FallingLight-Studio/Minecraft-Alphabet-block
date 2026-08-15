package com.aitshiroku.ColoredAlphabetBlocks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import com.aitshiroku.ColoredAlphabetBlocks.LegacyIds;

/**
 * Companion to {@link MappedRegistryMixin} for {@code getValue(Identifier)},
 * which {@code DefaultedMappedRegistry} overrides — so a mixin on the
 * superclass would never run for the block and item registries.
 *
 * <p>This registry returns its default value (air) rather than null for an
 * unknown name, which is exactly why the rename has to happen before the
 * lookup: otherwise a 1.0.0 block silently becomes air.
 */
@Mixin(DefaultedMappedRegistry.class)
public abstract class DefaultedMappedRegistryMixin<T> implements Registry<T> {

    @Inject(method = "getValue(Lnet/minecraft/resources/Identifier;)Ljava/lang/Object;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGetValue(
            Identifier name, CallbackInfoReturnable<T> cir) {
        Identifier renamed = LegacyIds.rename(name);
        if (renamed != null) {
            cir.setReturnValue(this.getValue(renamed));
        }
    }
}
