package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatEnableHandler;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.world.BoxOverlayRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.AABB;

public final class BacktrackModule extends ModuleSettingsService {
  private final ModuleNameService moduleNameService =
      this.settingCategory("Tracking")
          .description("Chooses the opponent and the distance at which a delay can help.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Timing")
          .description(
              "Bounds the delay and releases updates when the target closes the distance.");
  private final ModuleNameService moduleNameService3 =
      this.settingCategory("Visual")
          .description("Shows the latest received position alongside the delayed entity.");
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Target Mode", new String[] {"Attack", "KillAura", "Nearest"}, "Attack")
              .description(
                  "Attack follows your last actual attack; KillAura follows its selected target;"
                      + " Nearest works independently."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(this.moduleNameService, new ModuleSetting.Bool("Players", true));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(this.moduleNameService, new ModuleSetting.Bool("Mobs", false));
  private final ModuleSetting.Range moduleRange =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Range("Range", 1.0F, 3.0F, 0.0F, 6.0F, 0.1F)
              .description(
                  "Distance to the displayed hitbox where a backtrack window may remain active."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Attack Window (ms)", 1000.0F, 100.0F, 3000.0F, 50.0F)
              .description(
                  "How long an actually attacked target remains selected in Attack mode."));
  private final ModuleSetting.Range moduleRange2 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Range("Delay (ms)", 100.0F, 150.0F, 0.0F, 500.0F, 10.0F)
              .description(
                  "Samples one delay for a packet window. Oldest packets always release first."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Window Interval (ms)", 100.0F, 0.0F, 1000.0F, 25.0F)
              .description("Minimum pause after a window is flushed before another can begin."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Only Retreating", true)
              .description(
                  "Starts only when received movement takes the opponent farther away; approaching"
                      + " movement releases the window."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Pause When Hurt", true)
              .description(
                  "Releases queued updates immediately when you take damage and pauses while your"
                      + " hurt animation is active."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Received Position", true)
              .description(
                  "Outlines the latest received position. A bright filled box means movement is"
                      + " currently delayed."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Show Tracked Target", true)
              .description(
                  "Keeps a faint unfilled outline on the tracked opponent between active delay"
                      + " windows."));
  private final ModuleSetting.Color moduleColor =
      this.setting(this.moduleNameService3, new ModuleSetting.Color("Position Color", -863187981));
  private final ModuleSetting.Bool moduleBool7 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Fill", true));
  private final ModuleSetting.Bool moduleBool8 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("See Through", false)
              .description(
                  "Keeps the received-position box visible through entities and terrain if the"
                      + " delayed opponent covers it."));
  private final BoxOverlayRenderer renderer = new BoxOverlayRenderer();

  public BacktrackModule() {
    super(
        ModuleBuilderData.builder("Backtrack")
            .category(ModuleFeatureType.COMBAT)
            .description(
                "Briefly delays the selected opponent's movement while tracking its received"
                    + " position.")
            .build());
    this.moduleNumber.visibleWhen(this.moduleMode, "Attack"::equals);
    this.moduleColor.visibleWhen(this.moduleBool5);
    this.moduleBool6.visibleWhen(this.moduleBool5);
    this.moduleBool7.visibleWhen(this.moduleBool5);
    this.moduleBool8.visibleWhen(this.moduleBool5);
  }

  public CompatEnableHandler.Profile profile() {
    return new CompatEnableHandler.Profile(
        (String) this.moduleMode.get(),
        (Boolean) this.moduleBool.get(),
        (Boolean) this.moduleBool2.get(),
        this.moduleRange.low(),
        this.moduleRange.high(),
        Math.round((Float) this.moduleNumber.get()),
        Math.round(this.moduleRange2.low()),
        Math.round(this.moduleRange2.high()),
        Math.round((Float) this.moduleNumber2.get()),
        (Boolean) this.moduleBool3.get(),
        (Boolean) this.moduleBool4.get());
  }

  @Override
  protected void onEnable() {
    CompatEnableHandler.enable(this);
    this.on(EventAttackInputService.TICK)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(item -> CompatEnableHandler.tick());
    this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED)
        .run(item -> CompatEnableHandler.attackPacket(item.packet()));
    this.on(EventAttackInputService.WORLD).run(item -> CompatEnableHandler.worldChanged());
    this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED)
        .run(item -> CompatEnableHandler.reset());
    this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState);
  }

  @Override
  protected void onDisable() {
    CompatEnableHandler.disable(this);
    this.renderer.shutdown();
  }

  private void updateState(EventAttackInputService.WorldRender worldRender) {
    if ((Boolean) this.moduleBool5.get()) {
      AABB aABB = CompatEnableHandler.receivedBox().orElse(null);
      if (aABB != null) {
        int value = CompatEnableHandler.status().packets() > 0 ? 1 : 0;
        if (value != 0 || (Boolean) this.moduleBool6.get()) {
          Minecraft minecraft = CoreIsInitializedHandler.mc();
          int currentValue = (Integer) this.moduleColor.get();
          if (value == 0) {
            currentValue = currentValue & 16777215 | Math.min(80, currentValue >>> 24) << 24;
          }

          int nextValue = currentValue & 16777215 | Math.min(38, currentValue >>> 24) << 24;
          this.renderer.render(
              List.of(aABB.inflate(0.015)),
              worldRender,
              minecraft.getWindow().getWidth(),
              minecraft.getWindow().getHeight(),
              new BoxOverlayRenderer.Style(
                  nextValue,
                  currentValue,
                  value != 0 ? 0.012F : 0.006F,
                  value != 0 && (Boolean) this.moduleBool7.get(),
                  true,
                  (Boolean) this.moduleBool8.get()));
        }
      }
    }
  }
}
