package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.cape.CapeEnabledService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.world.cosmetic.CosmeticData;
import dev.felix.ellice.render.world.cosmetic.CosmeticRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class CapeComponent extends ModuleSettingsService {
  private final ModuleNameService moduleNameService =
      this.settingCategory("Cape").description("Controls ellice's procedural cape renderer.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory(this.moduleNameService, "Visibility")
          .description("Chooses which players and camera views render capes.");
  private final ModuleNameService moduleNameService3 =
      this.settingCategory(this.moduleNameService, "Appearance")
          .description("Controls cape material, colors, transparency, and depth behavior.");
  private final ModuleNameService moduleNameService4 =
      this.settingCategory(this.moduleNameService, "Geometry")
          .description("Controls cape dimensions, motion, animation energy, and render distance.");
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Capes", true)
              .description("Master switch for ellice's procedural cape renderer."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Self Cape", true)
              .description("Renders the procedural cape on your own player model."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Other Players", false)
              .description(
                  "Also renders procedural capes on visible, non-spectating players within Render"
                      + " Range."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("First Person", false)
              .description(
                  "Keeps your own cape visible in first person; it may enter the camera view while"
                      + " moving."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("See Through", false)
              .description(
                  "Disables depth testing so rendered capes remain visible through players and"
                      + " terrain."));
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Mode(
                  "Material", new String[] {"ellice", "Aurora", "Hologram"}, "ellice")
              .description(
                  "Selects the cape shader pattern: crystalline ellice, flowing Aurora, or"
                      + " scanlined Hologram."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Cape Width", 0.68F, 0.35F, 1.2F, 0.01F)
              .description("Sets the cape mesh width in blocks across the player's shoulders."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Cape Length", 1.1F, 0.5F, 1.9F, 0.01F)
              .description("Sets how far the cape mesh hangs down, measured in blocks."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Motion Response", 0.7F, 0.0F, 2.0F, 0.05F)
              .description(
                  "Controls how strongly the cloth trails player movement; 0 stays rigid and high"
                      + " values swing farther."));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Shader Energy", 0.45F, 0.0F, 2.0F, 0.05F)
              .description(
                  "Controls procedural cloth ripples and material animation; higher values move"
                      + " faster and farther."));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Opacity", 0.82F, 0.1F, 1.0F, 0.01F)
              .description(
                  "Sets overall cape transparency before the individual color alpha values are"
                      + " applied."));
  private final ModuleSetting.Number moduleNumber6 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Render Range", 64.0F, 8.0F, 160.0F, 1.0F)
              .description(
                  "Limits cape rendering to players within this many blocks; shorter ranges reduce"
                      + " geometry work."));
  private final ModuleSetting.Color moduleColor =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Color("Top Color", -429332225)
              .description("Sets the tint and alpha at the cape's shoulder end."));
  private final ModuleSetting.Color moduleColor2 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Color("Bottom Color", -661955338)
              .description(
                  "Sets the tint and alpha at the cape's lower end, forming a vertical gradient."));
  private final ModuleSetting.Color moduleColor3 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Color("Edge Color", -117440513)
              .description(
                  "Sets the accent tint and alpha along the cape sides and material highlights."));
  private final CosmeticRenderer renderer2 = new CosmeticRenderer();

  public CapeComponent() {
    super(
        ModuleBuilderData.builder("Cosmetics")
            .description("Renders configurable procedural capes for you and nearby players.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.moduleBool2.activeWhen(this.moduleBool);
    this.moduleBool3.activeWhen(this.moduleBool);
    this.moduleBool4.visibleWhen(this.moduleBool2).activeWhen(this.moduleBool);
    this.moduleBool5.activeWhen(this.moduleBool);
    this.moduleMode.activeWhen(this.moduleBool);
    this.moduleNumber.activeWhen(this.moduleBool);
    this.moduleNumber2.activeWhen(this.moduleBool);
    this.moduleNumber3.activeWhen(this.moduleBool);
    this.moduleNumber4.activeWhen(this.moduleBool);
    this.moduleNumber5.activeWhen(this.moduleBool);
    this.moduleNumber6.activeWhen(this.moduleBool);
    this.moduleColor.activeWhen(this.moduleBool);
    this.moduleColor2.activeWhen(this.moduleBool);
    this.moduleColor3.activeWhen(this.moduleBool);
  }

  @Override
  protected void onEnable() {
    this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState);
    this.on(EventAttackInputService.WORLD)
        .run(
            item -> {
              if (item.isLeaving()) {
                this.renderer2.clearStates();
              }
            });
  }

  @Override
  protected void onDisable() {
    this.renderer2.shutdown();
  }

  private void updateState(EventAttackInputService.WorldRender worldRender) {
    if ((Boolean) this.moduleBool.get()) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (minecraft.level != null && minecraft.player != null) {
        List items = this.collectValues(minecraft);
        if (items.isEmpty()) {
          this.renderer2.clearStates();
        } else {
          this.renderer2.render(
              items,
              new CosmeticData(
                  worldRender,
                  minecraft.getWindow().getWidth(),
                  minecraft.getWindow().getHeight(),
                  System.nanoTime() / 1.0E9),
              new CosmeticRenderer.Style(
                  (Float) this.moduleNumber.get(),
                  (Float) this.moduleNumber2.get(),
                  (Float) this.moduleNumber3.get(),
                  (Float) this.moduleNumber4.get(),
                  (Float) this.moduleNumber5.get(),
                  (Integer) this.moduleColor.get(),
                  (Integer) this.moduleColor2.get(),
                  (Integer) this.moduleColor3.get(),
                  this.calculateValue((String) this.moduleMode.get()),
                  (Boolean) this.moduleBool5.get()));
        }
      } else {
        this.renderer2.clearStates();
      }
    }
  }

  private List<Player> collectValues(Minecraft minecraft) {
    ArrayList arrayList = new ArrayList();
    double doubleValue = (Float) this.moduleNumber6.get() * (Float) this.moduleNumber6.get();
    boolean firstPerson = minecraft.options.getCameraType().isFirstPerson();

    for (Player currentPlayer : minecraft.level.players()) {
      int value = currentPlayer == minecraft.player ? 1 : 0;
      if ((value != 0
              ? !CapeEnabledService.active()
                  && (Boolean) this.moduleBool2.get()
                  && (!firstPerson || (Boolean) this.moduleBool4.get())
              : (Boolean) this.moduleBool3.get())
          && !currentPlayer.isSpectator()
          && !currentPlayer.isInvisible()
          && !(currentPlayer.distanceToSqr(minecraft.player) > doubleValue)) {
        arrayList.add(currentPlayer);
      }
    }

    return arrayList;
  }

  private int calculateValue(String text) {
    return switch (text == null ? "" : text.toLowerCase(Locale.ROOT)) {
      case "aurora" -> 1;
      case "hologram" -> 2;
      default -> 0;
    };
  }
}
