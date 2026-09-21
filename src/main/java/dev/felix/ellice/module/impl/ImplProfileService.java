package dev.felix.ellice.module.impl;

import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.timer.TimerModeTracker;
import dev.felix.ellice.feature.timer.TimerStateController;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import java.util.Locale;

public final class ImplProfileService extends ModuleSettingsService {
  private final ModuleNameService moduleNameService =
      this.settingCategory("Timing")
          .description("Controls how the client game clock changes over time.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Packet pulse")
          .description(
              "Uses movement packets that really reached the connection as its phase clock.");
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
              "Mode", new String[] {"Constant", "Pulse", "Random", "Packet Pulse"}, "Constant"));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Speed", 1.2F, 0.1F, 5.0F, 0.05F)
              .description("Fixed game speed in Constant mode. 1.00 is Vanilla."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService, new ModuleSetting.Number("Boost Speed", 1.5F, 0.1F, 5.0F, 0.05F));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Brake Speed", 0.75F, 0.1F, 5.0F, 0.05F));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          this.moduleNameService, new ModuleSetting.Number("Boost Ticks", 8.0F, 1.0F, 40.0F, 1.0F));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          this.moduleNameService, new ModuleSetting.Number("Brake Ticks", 4.0F, 1.0F, 40.0F, 1.0F));
  private final ModuleSetting.Range moduleRange =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Range("Random Speed", 0.85F, 1.35F, 0.1F, 5.0F, 0.05F)
              .description("Chooses a new multiplier inside this range."));
  private final ModuleSetting.Number moduleNumber6 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Random Interval", 5.0F, 1.0F, 40.0F, 1.0F)
              .description("Keeps each random multiplier for this many client ticks."));
  private final ModuleSetting.Number moduleNumber7 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Packets per Phase", 4.0F, 1.0F, 20.0F, 1.0F)
              .description(
                  "Switches between boost and brake after this many accepted movement packets."));

  public ImplProfileService() {
    super(
        ModuleBuilderData.builder("Timer")
            .category(ModuleFeatureType.MOVEMENT)
            .description(
                "Changes the client game clock with steady, pulsing, random and packet-driven"
                    + " timing.")
            .build());
    this.moduleNumber.visibleWhen(this.moduleMode, "Constant"::equals);
    this.moduleNumber2.visibleWhen(
        this.moduleMode, item -> item.equals("Pulse") || item.equals("Packet Pulse"));
    this.moduleNumber3.visibleWhen(
        this.moduleMode, item -> item.equals("Pulse") || item.equals("Packet Pulse"));
    this.moduleNumber4.visibleWhen(this.moduleMode, "Pulse"::equals);
    this.moduleNumber5.visibleWhen(this.moduleMode, "Pulse"::equals);
    this.moduleRange.visibleWhen(this.moduleMode, "Random"::equals);
    this.moduleNumber6.visibleWhen(this.moduleMode, "Random"::equals);
    this.moduleNumber7.visibleWhen(this.moduleMode, "Packet Pulse"::equals);
  }

  public TimerModeTracker.Profile profile() {
    return new TimerModeTracker.Profile(
        TimerModeTracker.Mode.valueOf(
            ((String) this.moduleMode.get()).toUpperCase(Locale.ROOT).replace(' ', '_')),
        (Float) this.moduleNumber.get(),
        (Float) this.moduleNumber2.get(),
        (Float) this.moduleNumber3.get(),
        Math.round((Float) this.moduleNumber4.get()),
        Math.round((Float) this.moduleNumber5.get()),
        this.moduleRange.low(),
        this.moduleRange.high(),
        Math.round((Float) this.moduleNumber6.get()),
        Math.round((Float) this.moduleNumber7.get()));
  }

  public String modeName() {
    return (String) this.moduleMode.get();
  }

  public String status() {
    return TimerStateController.status();
  }

  @Override
  protected void onEnable() {
    TimerStateController.activate(this);
    this.on(EventAttackInputService.TICK)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(item -> TimerStateController.tick());
    this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED)
        .run(item -> TimerStateController.accepted(item.packet()));
    this.on(EventAttackInputService.WORLD).run(item -> TimerStateController.worldChanged());
  }

  @Override
  protected void onDisable() {
    TimerStateController.deactivate(this);
  }
}
