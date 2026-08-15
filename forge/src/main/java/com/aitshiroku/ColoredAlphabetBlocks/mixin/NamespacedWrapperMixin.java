package com.aitshiroku.ColoredAlphabetBlocks.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aitshiroku.ColoredAlphabetBlocks.AlphabetCommon;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * Redirects registry lookups for the 1.0.0 mod id ({@code alphabet_block}) to
 * the 2.0.0 id ({@code colored_alphabet_blocks}) on Forge.
 *
 * <p>{@code MissingMappingsEvent} alone is not enough. {@code mapping.remap(...)}
 * makes Forge call {@code ForgeRegistry#addAlias}, but the block and item
 * registries the game actually reads are {@code NamespacedWrapper} instances,
 * and that class resolves a name straight out of its own {@code holdersByName}
 * map with no alias step. So a 1.0.0 name still resolves to nothing, and the
 * chunk palette turns it into air.
 *
 * <p>{@code NamespacedWrapper} extends {@code MappedRegistry} but overrides
 * {@code getHolder}, which is why a mixin on the vanilla superclass never runs
 * here. The target has to be the wrapper itself, and it is package-private, so
 * it is named as a string and reached through the public {@link Registry}
 * interface.
 *
 * <p>Rewriting the name here covers every deserialisation path at once: chunk
 * palettes, item stacks in NBT, recipes and tags all reach the registry by name.
 *
 * <p>No refmap is needed: this Forge version runs on official mappings, so the
 * targeted names match at runtime.
 *
 * <p>Once a chunk has been loaded through this and saved again, its palette
 * holds the new name. Chunks a player never visits keep the old name on disk
 * indefinitely, so this must stay in the mod rather than being dropped later.
 */
@Mixin(targets = "net.minecraftforge.registries.NamespacedWrapper")
public abstract class NamespacedWrapperMixin<T> {

    @Inject(method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGetHolder(
            ResourceLocation name, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
        ResourceLocation renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            // Dispatches back to this same override, but the new namespace makes
            // rename() return null, so this cannot loop.
            cir.setReturnValue(coloredalphabetblocks$self().getHolder(renamed));
        }
    }

    @Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGet(
            ResourceLocation name, CallbackInfoReturnable<T> cir) {
        ResourceLocation renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            cir.setReturnValue(coloredalphabetblocks$self().get(renamed));
        }
    }

    @SuppressWarnings("unchecked")
    private Registry<T> coloredalphabetblocks$self() {
        return (Registry<T>) (Object) this;
    }

    /**
     * Returns the 2.0.0 equivalent of a 1.0.0 name, or null when the name has
     * nothing to do with this mod.
     *
     * <p>Hot path — every registry lookup in the game reaches this, so the
     * namespace comparison short-circuits before anything is allocated.
     */
    private static ResourceLocation coloredalphabetblocks$rename(ResourceLocation name) {
        if (name == null || !AlphabetCommon.LEGACY_MOD_ID.equals(name.getNamespace())) {
            return null;
        }
        return ResourceLocation.fromNamespaceAndPath(AlphabetCommon.MOD_ID, name.getPath());
    }
}
