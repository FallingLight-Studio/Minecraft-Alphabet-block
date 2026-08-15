package com.aitshiroku.ColoredAlphabetBlocks.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import com.aitshiroku.ColoredAlphabetBlocks.LegacyIds;

/**
 * Redirects registry lookups for the 1.0.0 mod id ({@code alphabet_block}) to
 * the 2.0.0 id ({@code colored_alphabet_blocks}), so worlds saved by 1.0.0 keep
 * their placed blocks and stored items.
 *
 * <p>Chunk sections store block names as strings in their palette, and item
 * stacks store them in NBT. Both are turned back into objects through
 * {@code get(Identifier)} (reached from {@code byNameCodec()}), so rewriting the
 * lookup here covers every deserialisation path at once.
 *
 * <p>{@code DefaultedMappedRegistry} — which is what the block and item
 * registries actually are — overrides {@code getValue} but not {@code get}, so
 * {@code getValue} is handled by {@link DefaultedMappedRegistryMixin} instead.
 *
 * <p>Only Fabric needs this. NeoForge injects {@code IRegistryExtension} into
 * {@code Registry} and provides a supported {@code addAlias}, and Forge has
 * {@code MissingMappingsEvent}.
 *
 * <p>Once a chunk has been loaded through this and saved again, its palette
 * holds the new name. Chunks a player never visits keep the old name on disk
 * indefinitely, so this must stay in the mod rather than being dropped later.
 */
@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements Registry<T> {

    @Inject(method = "get(Lnet/minecraft/resources/Identifier;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private void coloredalphabetblocks$aliasGet(
            Identifier name, CallbackInfoReturnable<Optional<Holder.Reference<T>>> cir) {
        Identifier renamed = LegacyIds.rename(name);
        if (renamed != null) {
            // The recursive call carries the new namespace, so rename() returns
            // null for it and this cannot loop.
            cir.setReturnValue(this.get(renamed));
        }
    }
}
