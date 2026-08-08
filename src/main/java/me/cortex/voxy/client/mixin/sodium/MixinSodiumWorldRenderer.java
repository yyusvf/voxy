package me.cortex.voxy.client.mixin.sodium;

import me.cortex.voxy.commonImpl.VoxyCommon;
import org.embeddedt.embeddium.impl.gl.device.CommandList;
import org.embeddedt.embeddium.impl.render.EmbeddiumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Embeddium port: EmbeddiumWorldRenderer.initRenderer(CommandList) is private but otherwise
 * identical to Sodium's SodiumWorldRenderer.initRenderer.
 */
@Mixin(value = EmbeddiumWorldRenderer.class, remap = false)
public class MixinSodiumWorldRenderer {
    @Inject(method = "initRenderer", at = @At("TAIL"), remap = false)
    private void voxy$injectThreadUpdate(CommandList cl, CallbackInfo ci) {
        var vi = VoxyCommon.getInstance();
        if (vi != null) vi.updateDedicatedThreads();
    }
}
