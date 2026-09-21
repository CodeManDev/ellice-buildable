package dev.felix.ellice.module.impl;

import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.speed.SpeedActivateService;
import dev.felix.ellice.feature.speed.SpeedDefinitionService;
import dev.felix.ellice.feature.speed.SpeedOperationHandler;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleEntriesService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleIdService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ImplBoolService extends ModuleSettingsService {
  public final ModuleEntriesService mode =
      this.setting(
          new ModuleEntriesService(
              "Mode",
              SpeedDefinitionService.ALL.stream()
                  .map(SpeedDefinitionService.Definition::entry)
                  .toList(),
              "nextgen/LegitHop"));
  public final ModuleSetting.Bool autoSprint =
      this.setting(
          new ModuleSetting.Bool("Auto sprint", true)
              .description("Sprint while moving. Legacy Legit controls forward sprint itself."));
  public final ModuleSetting.Bool pauseScaffold =
      this.setting(
          new ModuleSetting.Bool("Pause with scaffold", true)
              .description("Pause Speed while ScaffoldWalk controls movement."));
  private final Map<String, Map<String, ModuleSetting<?>>> text = new LinkedHashMap<>();

  public ImplBoolService() {
    super(
        ModuleBuilderData.builder("Speed")
            .category(ModuleFeatureType.MOVEMENT)
            .description(
                SpeedDefinitionService.ALL.size()
                    + " movement modes from LiquidBounce and six historical clients, organized by"
                    + " client and family.")
            .build());
    this.mode.description(
        "Browse modes by client, edition, group and name. Each mode keeps its own settings.");
    ModuleNameService moduleName = this.settingCategory("Mode settings");

    for (SpeedDefinitionService.Definition definition : SpeedDefinitionService.ALL) {
      LinkedHashMap linkedHashMap = new LinkedHashMap();

      for (SpeedDefinitionService.Option option : definition.options()) {
        String currentText = definition.id() + " / " + option.key();
        ModuleSetting moduleSetting;
        if (option.value() instanceof Boolean enabled) {
          moduleSetting = new ModuleSetting.Bool(currentText, enabled);
        } else if (option.value() instanceof Double doubleValue) {
          moduleSetting =
              new ModuleSetting.Number(
                  currentText,
                  doubleValue.floatValue(),
                  (float) option.min(),
                  (float) option.max(),
                  (float) option.step());
        } else {
          moduleSetting =
              new ModuleSetting.Mode(
                  currentText, option.choices().toArray(String[]::new), (String) option.value());
        }

        moduleSetting.label(option.key());
        moduleSetting.visibleWhen(this.mode, definition.id()::equals);
        moduleSetting.description(
            option.key() + " for " + definition.edition() + " / " + definition.name() + ".");
        this.setting(moduleName, moduleSetting);
        linkedHashMap.put(option.key(), moduleSetting);
      }

      this.text.put(definition.id(), linkedHashMap);
    }

    this.updateState(
        "nextgen/Custom",
        "Horizontal modification",
        "Horizontal acceleration",
        "Jump off multiplier",
        "Boost delay");
    this.updateState(
        "nextgen/Custom", "Vertical modification", "Jump height", "Pull down", "Fall pull down");
    this.updateState(
        "nextgen/Custom",
        "Strafe",
        "Strafe strength",
        "Custom speed",
        "Speed",
        "Velocity timeout",
        "Strafe knockback");
    this.updateState("nextgen/Custom", "Custom speed", "Speed");
    this.updateState("nextgen/NCP", "Pull down", "Motion multiplier", "On tick", "On hurt");
    this.updateState("nextgen/NCP", "Boost", "Initial boost multiplier");
    this.updateState("nextgen/Intave14", "Strafe", "Strafe strength");
    this.updateState("legacy/IntaveHop14", "Boost", "Initial boost multiplier");
    this.updateState("legacy/UNCPHopNew", "Pull down", "On tick", "On hurt");
    this.mode.onChange(
        item -> {
          if (this.isEnabled()) {
            SpeedActivateService.modeChanged();
          }
        });
  }

  private void updateState(String currentText, String nextText, String... strings) {
    Map entries = this.text.get(currentText);

    for (String previousText : strings) {
      ((ModuleSetting) entries.get(previousText))
          .visibleWhen((ModuleSetting.Bool) entries.get(nextText));
    }
  }

  public SpeedOperationHandler values(final String currentText) {
    final Map entries = this.text.get(currentText);
    if (entries == null) {
      throw new IllegalArgumentException(currentText);
    } else {
      return new SpeedOperationHandler() {
        private Object createObject(String text) {
          ModuleSetting moduleSetting = (ModuleSetting) entries.get(text);
          if (moduleSetting == null) {
            throw new IllegalArgumentException(currentText + " / " + text);
          } else {
            return moduleSetting.get();
          }
        }

        @Override
        public boolean bool(String text) {
          return (Boolean) this.createObject(text);
        }

        @Override
        public double number(String text) {
          return ((Number) this.createObject(text)).doubleValue();
        }

        @Override
        public String choice(String text) {
          return (String) this.createObject(text);
        }
      };
    }
  }

  @Override
  public List<ModuleIdService> presets() {
    return List.of();
  }

  @Override
  protected void onEnable() {
    SpeedActivateService.activate(this);
    this.on(EventAttackInputService.TICK).run(item -> SpeedActivateService.clientTick());
    this.on(EventAttackInputService.WORLD).run(item -> SpeedActivateService.worldChanged());
    this.on(EventAttackInputService.PRE_MOVEMENT_PACKET)
        .run(item -> SpeedActivateService.motion(false));
    this.on(EventAttackInputService.POST_MOVEMENT_PACKET)
        .run(item -> SpeedActivateService.motion(true));
    this.on(EventAttackInputService.PACKET)
        .priority(EventIsAfterHandler.Priority.LAST)
        .run(SpeedActivateService::packet);
  }

  @Override
  protected void onDisable() {
    SpeedActivateService.deactivate();
  }
}
