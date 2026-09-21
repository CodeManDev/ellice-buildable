package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.hud.ActiveModulesHud;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class ScaleComponent extends ModuleSettingsService {
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          (ModuleSetting.Number)
              new ModuleSetting.Number(
                      "Scale",
                      1.0f,
                      Float.intBitsToFloat(1059481190),
                      Float.intBitsToFloat(1070386381),
                      Float.intBitsToFloat(1028443341))
                  .description(
                      "Scales the list's text and spacing. Long lists shrink further to fit the"
                          + " screen."));
  private final ModuleSetting.Bool ff6tsl615fh =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Background", true)
                  .description(
                      "Matches the minimap's Material surface, with a soft shadow around the"
                          + " connected outline."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Animations", true)
                  .description(
                      "Fluid surface transitions, staggered entrances and soft text fades, with a"
                          + " steady anchor."));
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          (ModuleSetting.Mode)
              new ModuleSetting.Mode("Show", new String[] {"All", "Necessary"}, "All")
                  .description(
                      "All shows every enabled module. Necessary shows only combat, movement and"
                          + " player modules."));
  private ActiveModulesHud f9yukr0b0c8;
  private ComponentMountService componentMountService;

  public ScaleComponent() {
    super(
        ModuleBuilderData.builder("Module List")
            .category(ModuleFeatureType.HUD)
            .description(
                "Active modules on the left, sorted by rendered name width, longest first.")
            .build());
  }

  @Override
  protected void onEnable() {
    this.f9yukr0b0c8 = new ActiveModulesHud();
    this.componentMountService =
        new ComponentMountService().mount(this.f9yukr0b0c8, CoreIsInitializedHandler.get().theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            this.componentMountService.id("module-list.overlay"))
                                        .absolute())
                                .position(0.0f, 0.0f))
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .pointerEvents(false))
        .visible(false);
    CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
    this.on(EventAttackInputService.RENDER)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(render -> this.updateState());
    this.on(EventAttackInputService.WORLD).run(world -> this.componentMountService.visible(false));
  }

  private void updateState() {
    Minecraft minecraft = Minecraft.getInstance();
    int n =
        minecraft.level != null
                && minecraft.player != null
                && minecraft.screen == null
                && !minecraft.options.hideGui
                && !CoreIsInitializedHandler.get().screens().isActive()
            ? 1
            : 0;
    this.componentMountService.visible(n != 0);
    if (n == 0) {
      return;
    }
    CoreIsInitializedHandler coreIsInitializedHandler = CoreIsInitializedHandler.get();
    boolean bl = ((String) this.moduleMode.get()).equals("Necessary");
    List<String> list =
        coreIsInitializedHandler
            .modules()
            .enabled()
            .filter(module -> (!bl || module.category().isNecessary() ? 1 : 0) != 0)
            .map(Module::name)
            .toList();
    float f =
        Math.min(
            ((Float) this.moduleNumber.get()).floatValue(),
            Math.max(
                    1.0f,
                    (float) coreIsInitializedHandler.viewport().height()
                        - Float.intBitsToFloat(1098907648))
                / (Float.intBitsToFloat(1099956224) * (float) Math.max(1, list.size())));
    this.f9yukr0b0c8.fontRevision(coreIsInitializedHandler.compositor().fontMetricsRevision());
    this.f9yukr0b0c8.update(
        list,
        f,
        Math.max(
            1.0f,
            (float) coreIsInitializedHandler.viewport().width() - Float.intBitsToFloat(1098907648)),
        (Boolean) this.ff6tsl615fh.get(),
        (Boolean) this.moduleBool2.get(),
        string ->
            coreIsInitializedHandler
                .compositor()
                .textWidth((String) string, Float.intBitsToFloat(1092616192) * f));
  }

  @Override
  protected void onDisable() {
    if (this.componentMountService != null) {
      CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
    }
    this.componentMountService = null;
    this.f9yukr0b0c8 = null;
  }
}
