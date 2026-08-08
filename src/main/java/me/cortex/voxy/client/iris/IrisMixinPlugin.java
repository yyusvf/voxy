package me.cortex.voxy.client.iris;

import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Disables every mixin in client.voxy.iris.mixins.json when Iris is not present.
 *
 * Presence MUST be probed through LoadingModList, never with Class.forName: this runs during
 * mixin config preparation, and merely loading net.irisshaders.iris.Iris here makes Mixin abort
 * every later config that targets it with
 *   MixinTargetAlreadyLoadedException: ... target net.irisshaders.iris.Iris was loaded too early
 * which is exactly what Monocle's mixins.monocle.compat.iris.json does. Do not reference any
 * Iris type from this class.
 */
public class IrisMixinPlugin implements IMixinConfigPlugin {
    private boolean irisPresent;

    @Override
    public void onLoad(String mixinPackage) {
        this.irisPresent = LoadingModList.get() != null
                && LoadingModList.get().getModFileById("iris") != null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.irisPresent;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
