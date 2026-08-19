package me.cortex.voxy.client.mixin.minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import me.cortex.voxy.client.config.VoxyConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Pushes terrain fog to infinity so Voxy's LODs are not cut off by a fog wall at the vanilla
 * render distance. Same approach Distant Horizons uses.
 *
 * Fabric port: the NeoForge branch does this from VoxyClientEvents via ViewportEvent.RenderFog,
 * which has no Fabric counterpart, so the values are written straight after setupFog computed
 * them. MC 1.21.1 keeps fog in RenderSystem's shader fog state (1.21.6+ replaced this with a
 * FogParameters UBO, which is why upstream looks different here).
 */
@Mixin(FogRenderer.class)
public class MixinFogRenderer {
    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void voxy$pushTerrainFogToInfinity(Camera camera, FogRenderer.FogMode fogMode,
                                                      float farPlaneDistance, boolean shouldCreateFog,
                                                      float partialTick, CallbackInfo ci) {
        if (fogMode != FogRenderer.FogMode.FOG_TERRAIN) {
            return;
        }
        if (!VoxyConfig.CONFIG.enabled || !VoxyConfig.CONFIG.enableRendering) {
            return;
        }
        // Large, but not Float.MAX_VALUE - that breaks shader fog math
        RenderSystem.setShaderFogStart(999999.0f);
        RenderSystem.setShaderFogEnd(9999999.0f);
    }
}
