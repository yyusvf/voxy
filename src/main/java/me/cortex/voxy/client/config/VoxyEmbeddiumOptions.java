package me.cortex.voxy.client.config;

import com.google.common.collect.ImmutableList;
import me.cortex.voxy.client.RenderStatistics;
import net.minecraft.network.chat.Component;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.options.OptionIdentifier;
import org.embeddedt.embeddium.api.options.control.ControlValueFormatter;
import org.embeddedt.embeddium.api.options.control.SliderControl;
import org.embeddedt.embeddium.api.options.control.TickBoxControl;
import org.embeddedt.embeddium.api.options.structure.OptionFlag;
import org.embeddedt.embeddium.api.options.structure.OptionGroup;
import org.embeddedt.embeddium.api.options.structure.OptionImpl;
import org.embeddedt.embeddium.api.options.structure.OptionPage;
import org.embeddedt.embeddium.api.options.structure.OptionStorage;

/**
 * Embeddium Video Settings integration for Voxy.
 *
 * Replaces the old SodiumOptionsAPI integration: Embeddium ships its own public event
 * ({@link OptionGUIConstructionEvent}) for adding pages to the video settings GUI, so no
 * third-party bridge mod is required.
 *
 * Unlike Sodium's old options API, Embeddium requires an explicit {@link OptionIdentifier}
 * on every option and group.
 */
public class VoxyEmbeddiumOptions {
    private static final String MOD_ID = "voxy";

    private static final OptionIdentifier<Void> PAGE_ID = OptionIdentifier.create(MOD_ID, "page");
    private static final OptionIdentifier<Void> GROUP_GENERAL = OptionIdentifier.create(MOD_ID, "general");
    private static final OptionIdentifier<Void> GROUP_PERFORMANCE = OptionIdentifier.create(MOD_ID, "performance");
    private static final OptionIdentifier<Void> GROUP_ADVANCED = OptionIdentifier.create(MOD_ID, "advanced");

    /**
     * Register the Voxy options page with Embeddium.
     * Call this during mod initialization.
     */
    public static void register() {
        OptionGUIConstructionEvent.BUS.addListener(VoxyEmbeddiumOptions::addVoxyPage);
    }

    private static void addVoxyPage(OptionGUIConstructionEvent event) {
        VoxyConfigStorage storage = new VoxyConfigStorage();

        event.addPage(new OptionPage(
                PAGE_ID,
                Component.translatable("voxy.sodium.page.title"),
                ImmutableList.of(
                        createGeneralGroup(storage),
                        createPerformanceGroup(storage),
                        createAdvancedGroup(storage)
                )
        ));
    }

    private static OptionIdentifier<Boolean> boolId(String path) {
        return OptionIdentifier.create(MOD_ID, path, Boolean.class);
    }

    private static OptionIdentifier<Integer> intId(String path) {
        return OptionIdentifier.create(MOD_ID, path, Integer.class);
    }

    private static OptionGroup createGeneralGroup(VoxyConfigStorage storage) {
        return OptionGroup.createBuilder()
                .setId(GROUP_GENERAL)
                // Both flags need a renderer reload: VoxyConfig.enabled/enableRendering are only
                // read in MixinLevelRenderer.createRenderer(), so without allChanged() a running
                // render system keeps going and the toggle appears to do nothing.
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("enabled"))
                        .setName(Component.translatable("voxy.sodium.option.enabled"))
                        .setTooltip(Component.translatable("voxy.sodium.option.enabled.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setBinding(
                                (config, value) -> config.enabled = value,
                                config -> config.enabled
                        )
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("enable_rendering"))
                        .setName(Component.translatable("voxy.sodium.option.enable_rendering"))
                        .setTooltip(Component.translatable("voxy.sodium.option.enable_rendering.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setBinding(
                                (config, value) -> config.enableRendering = value,
                                config -> config.enableRendering
                        )
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("ingest_enabled"))
                        .setName(Component.translatable("voxy.sodium.option.ingest_enabled"))
                        .setTooltip(Component.translatable("voxy.sodium.option.ingest_enabled.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding(
                                (config, value) -> config.ingestEnabled = value,
                                config -> config.ingestEnabled
                        )
                        .build())
                .build();
    }

    private static OptionGroup createPerformanceGroup(VoxyConfigStorage storage) {
        return OptionGroup.createBuilder()
                .setId(GROUP_PERFORMANCE)
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setId(intId("render_distance"))
                        .setName(Component.translatable("voxy.sodium.option.render_distance"))
                        .setTooltip(Component.translatable("voxy.sodium.option.render_distance.tooltip"))
                        .setControl(opt -> new SliderControl(opt, 2, 64, 1,
                                v -> Component.literal(v + " (" + (v * 32) + " chunks)")))
                        .setBinding(
                                (config, value) -> config.sectionRenderDistance = value,
                                config -> config.sectionRenderDistance
                        )
                        .build())
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setId(intId("service_threads"))
                        .setName(Component.translatable("voxy.sodium.option.service_threads"))
                        .setTooltip(Component.translatable("voxy.sodium.option.service_threads.tooltip"))
                        .setControl(opt -> new SliderControl(opt, 1, Runtime.getRuntime().availableProcessors(), 1,
                                ControlValueFormatter.number()))
                        .setBinding(
                                (config, value) -> config.serviceThreads = value,
                                config -> config.serviceThreads
                        )
                        .build())
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setId(intId("subdivision_size"))
                        .setName(Component.translatable("voxy.sodium.option.subdivision_size"))
                        .setTooltip(Component.translatable("voxy.sodium.option.subdivision_size.tooltip"))
                        .setControl(opt -> new SliderControl(opt, 28, 256, 4,
                                ControlValueFormatter.number()))
                        .setBinding(
                                (config, value) -> config.subDivisionSize = value,
                                config -> (int) config.subDivisionSize
                        )
                        .build())
                .build();
    }

    private static OptionGroup createAdvancedGroup(VoxyConfigStorage storage) {
        return OptionGroup.createBuilder()
                .setId(GROUP_ADVANCED)
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("environmental_fog"))
                        .setName(Component.translatable("voxy.sodium.option.environmental_fog"))
                        .setTooltip(Component.translatable("voxy.sodium.option.environmental_fog.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding(
                                (config, value) -> config.useEnvironmentalFog = value,
                                config -> config.useEnvironmentalFog
                        )
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("dont_use_sodium_threads"))
                        .setName(Component.translatable("voxy.sodium.option.dont_use_sodium_threads"))
                        .setTooltip(Component.translatable("voxy.sodium.option.dont_use_sodium_threads.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding(
                                (config, value) -> config.dontUseSodiumBuilderThreads = value,
                                config -> config.dontUseSodiumBuilderThreads
                        )
                        .build())
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setId(intId("lod_boundary_buffer"))
                        .setName(Component.translatable("voxy.sodium.option.lod_boundary_buffer"))
                        .setTooltip(Component.translatable("voxy.sodium.option.lod_boundary_buffer.tooltip"))
                        .setControl(opt -> new SliderControl(opt, 0, 4, 1,
                                v -> v == 0 ? Component.literal("Exact") : Component.literal(v + " blocks")))
                        .setBinding(
                                (config, value) -> config.lodBoundaryBuffer = value,
                                config -> config.lodBoundaryBuffer
                        )
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, storage)
                        .setId(boolId("render_statistics"))
                        .setName(Component.translatable("voxy.sodium.option.render_statistics"))
                        .setTooltip(Component.translatable("voxy.sodium.option.render_statistics.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding(
                                (config, value) -> RenderStatistics.enabled = value,
                                config -> RenderStatistics.enabled
                        )
                        .build())
                .add(OptionImpl.createBuilder(int.class, storage)
                        .setId(intId("earth_curve_ratio"))
                        .setName(Component.translatable("voxy.sodium.option.earth_curve_ratio"))
                        .setTooltip(Component.translatable("voxy.sodium.option.earth_curve_ratio.tooltip"))
                        .setControl(opt -> new SliderControl(opt, 0, 500, 10,
                                v -> v == 0 ? Component.literal("Disabled") :
                                     v < 50 ? Component.literal("→ 50 (min)") :
                                     Component.literal(v + "x")))
                        .setBinding(
                                (config, value) -> config.earthCurveRatio = value < 50 && value > 0 ? 50 : value,
                                config -> config.earthCurveRatio
                        )
                        .build())
                .build();
    }

    /**
     * Storage implementation that wraps VoxyConfig.CONFIG
     */
    private static class VoxyConfigStorage implements OptionStorage<VoxyConfig> {
        @Override
        public VoxyConfig getData() {
            return VoxyConfig.CONFIG;
        }

        @Override
        public void save() {
            VoxyConfig.CONFIG.save();
            // The NeoForge config is the source of truth and would otherwise overwrite these
            // values again on the next config (re)load.
            VoxyNeoForgeConfig.syncFromVoxyConfig();
        }
    }
}
