package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatEnableService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.tickbase.TickbaseCreditService;
import dev.felix.ellice.hud.TickBaseStatusHud;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Locale;
import net.minecraft.client.Minecraft;

public final class TickBaseModule extends ModuleSettingsService {
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          (ModuleSetting.Mode)
              new ModuleSetting.Mode("Timing", new String[] {"Past", "Future"}, "Past")
                  .description(
                      "Past banks idle player ticks before catching up. Future advances time first"
                          + " and can trigger timer checks even with repayment."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Sync KillAura", true)
                  .description(
                      "Uses KillAura's exact target, attack range, weapon cooldown and click"
                          + " readiness."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          (ModuleSetting.Number)
              new ModuleSetting.Number(
                      "Max shift ticks", 2.0f, 1.0f, Float.intBitsToFloat(0x41000000), 1.0f)
                  .description(
                      "One tick is 50 ms at the normal tick rate. Larger shifts create a larger"
                          + " timing discontinuity."));
  private final ModuleSetting.Number fug5hqp3lyv =
      this.setting(
          new ModuleSetting.Number(
              "Activation range",
              Float.intBitsToFloat(0x40A00000),
              Float.intBitsToFloat(0x40400000),
              Float.intBitsToFloat(0x40C00000),
              Float.intBitsToFloat(0x3DCCCCCD)));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          new ModuleSetting.Number(
              "Cooldown (ms)",
              Float.intBitsToFloat(1153138688),
              Float.intBitsToFloat(1140457472),
              Float.intBitsToFloat(1167867904),
              Float.intBitsToFloat(1120403456)));
  private final ModuleSetting.Bool fx52bzd1856 =
      this.setting(new ModuleSetting.Bool("Target players", true));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(new ModuleSetting.Bool("Target mobs", false));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Combat only", true)
                  .description(
                      "Requires held attack input or an active KillAura target. Otherwise triggers"
                          + " when approaching."));
  private final ModuleSetting.Text moduleText =
      this.setting(
          (ModuleSetting.Text)
              new ModuleSetting.Text("Excluded players", "", 256)
                  .description(
                      "Names or UUIDs separated by commas. Scoreboard teammates are always"
                          + " excluded."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Status display", true)
                  .description(
                      "Shows the current wait reason, shift repayment and last burst below the"
                          + " crosshair."));
  private TickBaseStatusHud renderer;
  private ComponentMountService componentMountService;

  public TickBaseModule() {
    super(
        ModuleBuilderData.builder("TickBase")
            .category(ModuleFeatureType.COMBAT)
            .description(
                "Predicts a useful attack window and shifts only the player ticks needed to reach"
                    + " it.")
            .build());
    this.fx52bzd1856.visibleWhen(this.moduleBool, bl -> bl == false);
    this.moduleBool3.visibleWhen(this.moduleBool, bl -> bl == false);
    this.moduleBool4.visibleWhen(this.moduleBool, bl -> bl == false);
  }

  public Profile profile() {
    return new Profile(
        ((Float) this.moduleNumber.get()).intValue(),
        ((Float) this.fug5hqp3lyv.get()).floatValue(),
        ((Float) this.moduleNumber3.get()).longValue(),
        (Boolean) this.fx52bzd1856.get(),
        (Boolean) this.moduleBool3.get(),
        (Boolean) this.moduleBool4.get(),
        (String) this.moduleText.get(),
        TickbaseCreditService.Mode.valueOf(
            ((String) this.moduleMode.get()).toUpperCase(Locale.ROOT)),
        (Boolean) this.moduleBool.get());
  }

  @Override
  protected void onEnable() {
    CompatEnableService.enable(this);
    this.renderer = new TickBaseStatusHud();
    this.componentMountService =
        new ComponentMountService().mount(this.renderer, CoreIsInitializedHandler.get().theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) this.componentMountService.id("tickbase.status"))
                                .absolute())
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.px(Float.intBitsToFloat(1098907648))))
                .pointerEvents(false))
        .visible(false);
    CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
    this.on(EventAttackInputService.WORLD).run(world -> CompatEnableService.reset());
    this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED)
        .run(playerPositionCorrected -> CompatEnableService.corrected());
    this.on(EventAttackInputService.TICK).run(tick -> CompatEnableService.watchdog());
    this.on(EventAttackInputService.ATTACK_INPUT)
        .run(attackInput -> CompatEnableService.attackInput());
    this.on(EventAttackInputService.RENDER).run(render -> this.updateState());
  }

  @Override
  protected void onDisable() {
    CompatEnableService.disable(this);
    if (this.componentMountService != null) {
      CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
    }
    this.componentMountService = null;
    this.renderer = null;
  }

  public String status() {
    return CompatEnableService.status(this);
  }

  private void updateState() {
    ComponentMountService componentMountService = this.componentMountService;
    TickBaseStatusHud tickBaseStatusHud = this.renderer;
    if (componentMountService == null || tickBaseStatusHud == null || !this.isEnabled()) {
      return;
    }
    CoreIsInitializedHandler coreIsInitializedHandler = CoreIsInitializedHandler.get();
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    int n =
        (Boolean) this.moduleBool5.get() != false
                && minecraft.player != null
                && minecraft.level != null
                && minecraft.screen == null
                && !minecraft.options.hideGui
                && !coreIsInitializedHandler.screens().isActive()
            ? 1
            : 0;
    componentMountService.visible(n != 0);
    if (n == 0) {
      return;
    }
    componentMountService.position(0.0f, coreIsInitializedHandler.viewport().height() / 2 + 30);
    tickBaseStatusHud.update("TickBase \u00b7 " + CompatEnableService.compactStatus(this));
  }

  public record Profile(
      int ticks,
      double range,
      long cooldown,
      boolean players,
      boolean mobs,
      boolean combatOnly,
      String excluded,
      TickbaseCreditService.Mode timing,
      boolean syncAura) {}
}
