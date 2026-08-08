package me.cortex.voxy;

import me.cortex.voxy.client.config.VoxyNeoForgeConfig;
import me.cortex.voxy.common.Logger;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/**
 * Main mod class for Voxy on NeoForge.
 *
 * Handles config registration and config screen setup.
 * Actual initialization happens via mixins (MixinRenderSystem).
 */
@Mod("voxy")
public class Voxy {

    public Voxy(IEventBus modEventBus, ModContainer container) {
        // Only register client config on client side
        if (FMLLoader.getDist() == Dist.CLIENT) {
            // Register NeoForge config
            VoxyNeoForgeConfig.register(container);

            // Register the built-in NeoForge config screen
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

            // Register Embeddium options integration if available
            // This adds Voxy settings to Embeddium's Video Settings menu
            // Uses reflection to avoid hard dependency - graceful fallback if not present
            tryRegisterEmbeddiumOptionsIntegration();
        }
    }

    /**
     * Attempts to register the Embeddium video settings integration.
     * Uses reflection to avoid class loading errors when Embeddium is not present.
     * Falls back gracefully to NeoForge config screen if unavailable.
     */
    private static void tryRegisterEmbeddiumOptionsIntegration() {
        if (!ModList.get().isLoaded("embeddium")) {
            Logger.info("Embeddium not found - Voxy settings available via Mods menu");
            return;
        }

        try {
            // Load and invoke the integration class only when we know Embeddium is present
            // This prevents NoClassDefFoundError when Embeddium is not installed
            Class<?> embeddiumOptionsClass = Class.forName("me.cortex.voxy.client.config.VoxyEmbeddiumOptions");
            embeddiumOptionsClass.getMethod("register").invoke(null);
            Logger.info("Registered Voxy settings in Embeddium Video Settings menu");
        } catch (Throwable e) {
            Logger.warn("Failed to register Embeddium options integration: " + e.getMessage());
            Logger.info("Voxy settings available via Mods menu instead");
        }
    }
}
