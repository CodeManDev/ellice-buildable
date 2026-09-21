package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatApplyService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.cape.CapeRevisionService;
import dev.felix.ellice.feature.cape.CapeRepository;
import dev.felix.ellice.feature.cape.CapeEnabledService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.ui.screen.builtin.CapeStudioScreen;

public final class CapeModule extends ModuleSettingsService implements ModuleOperationHandler {
   private CapeRevisionService capeRevisionService;

   public CapeModule() {
      super(
         ModuleBuilderData.builder("Cape")
            .category(ModuleFeatureType.VISUALS)
            .description("Wear an animated GIF or custom Minecraft cape. Open Cape Studio for search and a 3D preview.")
            .build()
      );
   }

   public CapeRevisionService controller() {
      if (this.capeRevisionService == null) {
         this.capeRevisionService = new CapeRevisionService(
            new CapeRepository(CoreIsInitializedHandler.mc().gameDirectory.toPath().resolve("config/ellice/capes")),
            item -> CoreIsInitializedHandler.mc().execute(item)
         );
         this.capeRevisionService.restore();
      }

      return this.capeRevisionService;
   }

   @Override
   protected void onEnable() {
      this.controller();
      CapeEnabledService.enabled(true);
   }

   @Override
   protected void onDisable() {
      CapeEnabledService.enabled(false);
      CompatApplyService.close();
   }

   @Override
   public void openPanel() {
      if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().screens() != null) {
         CapeRevisionService capeRevision = this.controller();
         capeRevision.skin(CoreIsInitializedHandler.mc().getUser().getProfileId());
         CoreIsInitializedHandler.get().screens().open(new CapeStudioScreen(capeRevision, () -> {
            if (!this.isEnabled()) {
               this.enable();
            }
         }));
      }
   }
}
