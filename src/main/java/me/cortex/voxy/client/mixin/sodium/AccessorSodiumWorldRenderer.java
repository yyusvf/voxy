package me.cortex.voxy.client.mixin.sodium;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Embeddium port: Sodium's SodiumWorldRenderer is SodiumWorldRenderer here.
 * The {@code renderSectionManager} field is unchanged.
 */
@Mixin(value = SodiumWorldRenderer.class, remap = false)
public interface AccessorSodiumWorldRenderer {
    @Accessor
    RenderSectionManager getRenderSectionManager();
}
