package com.aitshiroku.ColoredAlphabetBlocks.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aitshiroku.ColoredAlphabetBlocks.AlphabetCommon;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * Redirects registry lookups for the 1.0.0 mod id ({@code alphabet_block}) to
 * the 2.0.0 id ({@code colored_alphabet_blocks}), so worlds saved by 1.0.0 keep
 * their placed blocks and stored items.
 *
 * <p>Chunk sections store block names as strings in their palette, and item
 * stacks store them in NBT. Both are turned back into objects through
 * {@code Registry#getHolder(ResourceLocation)} (reached from
 * {@code byNameCodec()}), so rewriting the lookup here covers every
 * deserialisation path at once rather than patching each one.
 *
 * <p>NeoForge does not need this — it injects {@code IRegistryExtension} into
 * {@code Registry}, which provides a supported {@code addAlias}. Vanilla has no
 * such hook, so Fabric and Forge need the mixin.
 *
 * <p>Once a chunk has been loaded through this and saved again, its palette
 * holds the new name and no longer depends on the mixin. Chunks a player never
 * visits keep the old name on disk indefinitely, so this must stay in the mod
 * rather than being dropped after a release or two.
 */
@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements Registry<T> {

    @Inject(method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGetHolder(
            ResourceLocation name, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
        ResourceLocation renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            // The recursive call carries the new namespace, so rename() returns
            // null for it and this cannot loop.
            cir.setReturnValue(this.getHolder(renamed));
        }
    }

    @Inject(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGet(
            ResourceLocation name, CallbackInfoReturnable<T> cir) {
        ResourceLocation renamed = coloredalphabetblocks$rename(name);
        if (renamed != null) {
            cir.setReturnValue(this.get(renamed));
        }
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
