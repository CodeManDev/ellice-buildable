package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleSettingsService;

public final class ImplOpenPanelService extends ModuleSettingsService
    implements ModuleOperationHandler {
  public ImplOpenPanelService() {
    super(
        ModuleBuilderData.builder("Module Studio")
            .category(ModuleFeatureType.TOOLS)
            .description(
                "Build your own modules with shapes, live data and connected logic blocks.")
            .build());
  }

  @Override
  protected void onEnable() {
    this.openPanel();
  }

  @Override
  public void openPanel() {
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().screens() != null) {
      CoreIsInitializedHandler.get().screens().open("module-studio");
    }
  }
}
