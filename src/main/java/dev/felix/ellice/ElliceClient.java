package dev.felix.ellice;

import dev.felix.ellice.compat.CompatInitializeService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.feature.license.LicenseIsLicensedService;
import dev.felix.ellice.security.ElliceKeep;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.loader.api.FabricLoader;

@ElliceKeep
public final class ElliceClient implements ClientModInitializer {
   public void onInitializeClient() {
      try {
         LicenseIsLicensedService.BootResult gate = LicenseIsLicensedService.evaluateAtBoot(FabricLoader.getInstance().getGameDir(), CoreIsInitializedHandler.VERSION);
         if (!gate.licensed()) {
            CoreIsInitializedHandler.LOGGER.error("ellice license denied: {}", gate.reason());
            FatalIsTrippedService.trip(new IllegalStateException(gate.reason()), FatalCaptureService.Kind.LICENSE);
            return;
         }

         CoreIsInitializedHandler.initialize();
         LicenseIsLicensedService.beginSession(CoreIsInitializedHandler.get().bus(), FabricLoader.getInstance().getGameDir(), CoreIsInitializedHandler.VERSION);
      } catch (Throwable t) {
         FatalIsTrippedService.trip(t, FatalCaptureService.Kind.STARTUP);
      }

      try {
         CompatInitializeService.initialize();
      } catch (Throwable t) {
         CoreIsInitializedHandler.LOGGER.warn("Terrain map QA failed to start", t);
      }

      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> {
         if (CoreIsInitializedHandler.isInitialized()) {
            try {
               CoreIsInitializedHandler.get().shutdown();
            } catch (Throwable exception) {
            }
         }
      });
   }
}
