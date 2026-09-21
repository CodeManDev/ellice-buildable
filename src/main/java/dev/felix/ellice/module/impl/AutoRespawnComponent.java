package dev.felix.ellice.module.impl;

import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;

public final class AutoRespawnComponent extends ModuleSettingsService {
  public final ModuleSetting.Number delay =
      this.setting(
          new ModuleSetting.Number("Delay (ticks)", 20.0F, 0.0F, 100.0F, 1.0F)
              .description("Waits this many client ticks on the death screen before respawning."));
  private LocalPlayer localPlayer;
  private int count;
  private boolean enabled;

  public AutoRespawnComponent() {
    super(
        ModuleBuilderData.builder("AutoRespawn")
            .category(ModuleFeatureType.PLAYER)
            .description("Automatically respawns after death outside Hardcore worlds.")
            .build());
  }

  @Override
  protected void onEnable() {
    this.updateState();
    this.on(EventAttackInputService.TICK).run(item -> this.updateState2());
    this.on(EventAttackInputService.WORLD).run(item -> this.updateState());
  }

  @Override
  protected void onDisable() {
    this.updateState();
  }

  private void updateState() {
    this.localPlayer = null;
    this.count = 0;
    this.enabled = false;
  }

  private void updateState2() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player != null
        && minecraft.level != null
        && !minecraft.player.isAlive()
        && !minecraft.level.getLevelData().isHardcore()) {
      if (this.localPlayer != minecraft.player) {
        this.updateState();
        this.localPlayer = minecraft.player;
      }

      if (!this.enabled && minecraft.screen instanceof DeathScreen && !minecraft.isPaused()) {
        if (this.count++ >= ((Float) this.delay.get()).intValue()) {
          this.enabled = true;
          minecraft.player.respawn();
          minecraft.setScreen(null);
        }
      }
    } else {
      this.updateState();
    }
  }
}
