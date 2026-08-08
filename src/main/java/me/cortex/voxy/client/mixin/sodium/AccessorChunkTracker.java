package me.cortex.voxy.client.mixin.sodium;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Embeddium port: targets org.embeddedt.embeddium.impl.render.chunk.map.ChunkTracker,
 * which keeps the same {@code chunkStatus} field as Sodium's ChunkTracker.
 */
@Mixin(value = ChunkTracker.class, remap = false)
public interface AccessorChunkTracker {
    @Accessor
    Long2IntOpenHashMap getChunkStatus();
}
