package me.cortex.voxy.client.mixin.iris;

import me.cortex.voxy.client.core.IGetVoxyRenderSystem;
import me.cortex.voxy.client.core.util.IrisUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11C.glViewport;

/**
 * Captures the viewport parameters Iris needs, before Iris takes over level rendering.
 *
 * MC 1.21.1 port: upstream targets the 1.21.6+ signature
 *   renderLevel(GraphicsResourceAllocator, DeltaTracker, boolean, Camera, Matrix4f, Matrix4f,
 *               Matrix4f, GpuBufferSlice, Vector4f, boolean)
 * whose parameter types do not exist here. MC 1.21.1 uses
 *   renderLevel(DeltaTracker, boolean, Camera, GameRenderer, LightTexture,
 *               Matrix4f frustumMatrix, Matrix4f projectionMatrix)
 * where frustumMatrix is the model-view matrix.
 *
 * Upstream's captured FogParameters is dropped - see IrisUtil.CapturedViewportParameters.
 */
@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Inject(method = "renderLevel", at = @At("HEAD"), order = 100)
    private void voxy$injectIrisCompat(
            DeltaTracker deltaTracker,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f frustumMatrix,
            Matrix4f projectionMatrix,
            CallbackInfo ci) {
        if (IrisUtil.irisShaderPackEnabled()) {
            var renderer = ((IGetVoxyRenderSystem) this).getVoxyRenderSystem();
            if (renderer != null) {
                //Fix the viewport dims, Iris leaves them pointing at its own targets
                var mainTarget = Minecraft.getInstance().getMainRenderTarget();
                glViewport(0, 0, mainTarget.width, mainTarget.height);

                var pos = camera.getPosition();
                IrisUtil.CAPTURED_VIEWPORT_PARAMETERS = new IrisUtil.CapturedViewportParameters(
                        new ChunkRenderMatrices(projectionMatrix, frustumMatrix), pos.x, pos.y, pos.z);
            }
        }
    }
}
