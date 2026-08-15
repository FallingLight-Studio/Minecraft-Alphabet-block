package com.aitshiroku.ColoredAlphabetBlocks.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aitshiroku.ColoredAlphabetBlocks.AlphabetCommon;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/**
 * Redirects registry lookups for the 1.0.0 mod id ({@code alphabet_block}) to
 * the 2.0.0 id ({@code colored_alphabet_blocks}) on Forge 1.21.11.
 *
 * <p>In 1.21.11 Minecraft renamed {@code ResourceLocation} to {@code Identifier}
 * and the registry query methods on {@code NamespacedWrapper} were also renamed:
 * <ul>
 *   <li>{@code get(Identifier)} and {@code getHolder(Identifier)} both return
 *       {@code Optional<Holder.Reference<T>>} and both read directly from
 *       {@code holdersByName} with no alias step.
 *   <li>{@code getValue(Identifier)} returns {@code T} and delegates to
 *       {@code ForgeRegistry.getRaw}; intercepted here for completeness.
 * </ul>
 *
 * <p>{@code NamespacedWrapper} is package-private, so it is targeted by string
 * and accessed through the public {@link Registry} interface. No refmap is
 * needed: Forge 61.1.0 runs on official mappings, so the targeted names match
 * at runtime.
 *
 * <p>See {@code NamespacedWrapperMixin} in {@code dev-1.21.1} for the full
 * analysis of why {@code MissingMappingsEvent} alone is not enough.
 */
@Mixin(targets = "net.minecraftforge.registries.NamespacedWrapper")
public abstract class NamespacedWrapperMixin<T> {

    // ── Optional<Holder.Reference<T>> path ────────────────────────────────

    @Inject(method = "get(Lnet/minecraft/resources/Identifier;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGet(
            Identifier name, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
        Identifier renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            cir.setReturnValue(coloredalphabetblocks$self().get(renamed));
        }
    }

    @Inject(method = "getHolder(Lnet/minecraft/resources/Identifier;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGetHolder(
            Identifier name, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
        Identifier renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            // getHolder(Identifier) and get(Identifier) are identical in
            // NamespacedWrapper (both read holdersByName). Delegating to get()
            // avoids a cast outside the Registry<T> interface. No loop: renamed
            // has the new namespace, so rename() returns null on re-entry.
            cir.setReturnValue(coloredalphabetblocks$self().get(renamed));
        }
    }

    // ── T path ────────────────────────────────────────────────────────────

    @Inject(method = "getValue(Lnet/minecraft/resources/Identifier;)Ljava/lang/Object;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGetValue(
            Identifier name, CallbackInfoReturnable<T> cir) {
        Identifier renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            cir.setReturnValue(coloredalphabetblocks$self().getValue(renamed));
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Registry<T> coloredalphabetblocks$self() {
        return (Registry<T>) (Object) this;
    }

    /**
     * Returns the 2.0.0 equivalent of a 1.0.0 name, or {@code null} when the
     * name has nothing to do with this mod.
     *
     * <p>Hot path — every registry lookup in the game reaches this, so the
     * namespace comparison short-circuits before anything is allocated.
     */
    private static Identifier coloredalphabetblocks$rename(Identifier name) {
        if (name == null || !AlphabetCommon.LEGACY_MOD_ID.equals(name.getNamespace())) {
            return null;
        }
        return Identifier.fromNamespaceAndPath(AlphabetCommon.MOD_ID, name.getPath());
    }
}
