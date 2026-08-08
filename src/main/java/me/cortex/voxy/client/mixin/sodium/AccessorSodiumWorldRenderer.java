package me.cortex.voxy.client.mixin.sodium;

import org.embeddedt.embeddium.impl.render.EmbeddiumWorldRenderer;
import org.embeddedt.embeddium.impl.render.chunk.RenderSectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Embeddium port: Sodium's SodiumWorldRenderer is EmbeddiumWorldRenderer here.
 * The {@code renderSectionManager} field is unchanged.
 */
@Mixin(value = EmbeddiumWorldRenderer.class, remap = false)
public interface AccessorSodiumWorldRenderer {
    @Accessor
    RenderSectionManager getRenderSectionManager();
}
