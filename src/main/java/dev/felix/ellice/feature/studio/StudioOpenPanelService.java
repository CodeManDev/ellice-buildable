package dev.felix.ellice.feature.studio;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleRegisterService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.impl.ImplOpenPanelService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class StudioOpenPanelService extends ModuleSettingsService
    implements ModuleOperationHandler {
  final StudioShapeService project;
  private final ModuleRegisterService moduleRegisterService;
  private final Runnable runnable;
  final ModuleSetting.Number x =
      this.setting(new ModuleSetting.Number("Offset X", 16.0F, 0.0F, 2000.0F, 1.0F).exactInput());
  final ModuleSetting.Number y =
      this.setting(new ModuleSetting.Number("Offset Y", 16.0F, 0.0F, 2000.0F, 1.0F).exactInput());
  final ModuleSetting.Number scale =
      this.setting(new ModuleSetting.Number("Scale", 0.6F, 0.25F, 2.0F, 0.05F));
  final ModuleSetting.Number opacity =
      this.setting(new ModuleSetting.Number("Opacity", 1.0F, 0.0F, 1.0F, 0.05F));
  final ModuleSetting.Mode anchor =
      this.setting(
          new ModuleSetting.Mode(
              "Anchor",
              new String[] {"Top left", "Top right", "Bottom left", "Bottom right", "Center"},
              "Top left"));
  private final Map<Module, StudioOpenPanelService.Gate> entries = new LinkedHashMap<>();
  private String text = "";

  StudioOpenPanelService(
      StudioShapeService studioShape,
      ModuleRegisterService moduleRegister,
      Runnable currentRunnable) {
    super(
        ModuleBuilderData.builder(studioShape.name)
            .description(
                "Made in Module Studio · "
                    + studioShape.shapes.size()
                    + " shapes · "
                    + studioShape.blocks.size()
                    + " blocks")
            .category(ModuleFeatureType.HUD)
            .build());
    this.project = studioShape.copy();
    this.moduleRegisterService = moduleRegister;
    this.runnable = currentRunnable;
  }

  @Override
  public void openPanel() {
    this.runnable.run();
  }

  public String projectId() {
    return this.project.id;
  }

  public String runtimeMessage() {
    return this.text;
  }

  void gates(Map<String, Boolean> currentEntries) {
    for (Entry entry : currentEntries.entrySet()) {
      Module module = this.moduleRegisterService.get((String) entry.getKey()).orElse(null);
      if (module != null
          && !(module instanceof StudioOpenPanelService)
          && !(module instanceof ImplOpenPanelService)) {
        boolean enabled = (Boolean) entry.getValue();
        StudioOpenPanelService.Gate gate = this.entries.get(module);
        if (gate == null || gate.applied != enabled) {
          boolean currentEnabled = gate == null ? module.isEnabled() : gate.original;

          try {
            LocalConfigRepository.runWithoutSaving(
                () -> {
                  if (module.isEnabled() != enabled) {
                    module.toggle();
                  }
                });
            this.entries.put(module, new StudioOpenPanelService.Gate(currentEnabled, enabled));
            this.text = "";
          } catch (RuntimeException exception) {
            this.text = "Could not switch " + module.name();
          }
        }
      }
    }
  }

  @Override
  protected void onDisable() {
    this.releaseActions();
  }

  void releaseActions() {
    for (Entry entry : this.entries.entrySet()) {
      try {
        Module module = (Module) entry.getKey();
        StudioOpenPanelService.Gate gate = (StudioOpenPanelService.Gate) entry.getValue();
        if (module.isEnabled() == gate.applied && module.isEnabled() != gate.original) {
          LocalConfigRepository.runWithoutSaving(module::toggle);
        }
      } catch (RuntimeException exception) {
      }
    }

    this.entries.clear();
  }

  void copySettings(StudioOpenPanelService studioOpenPanel) {
    this.x.set((Float) studioOpenPanel.x.get());
    this.y.set((Float) studioOpenPanel.y.get());
    this.scale.set((Float) studioOpenPanel.scale.get());
    this.opacity.set((Float) studioOpenPanel.opacity.get());
    this.anchor.set((String) studioOpenPanel.anchor.get());
  }

  private record Gate(boolean original, boolean applied) {}
}
