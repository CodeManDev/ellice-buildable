package dev.felix.ellice.module.impl;

import com.mojang.blaze3d.platform.Window;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.movement.MovementBurstScheduleService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.world.BoxOverlayRenderer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class TriggerComponent extends ModuleSettingsService {
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          new ModuleSetting.Mode("Trigger", new String[] {"Middle", "Left", "Right"}, "Middle")
              .description(
                  "Middle = pick-block key, no vanilla effect in survival. Left/Right also fire the"
                      + " vanilla action."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number("Reach", 120.0F, 10.0F, 500.0F, 10.0F)
              .description("Crosshair raycast range in blocks."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          new ModuleSetting.Number("Burst range", 40.0F, 10.0F, 48.0F, 1.0F)
              .description(
                  "Up to this distance the fast chokeless burst fires (Burst8/Dual8). Beyond it the"
                      + " trigger snap chain runs."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          new ModuleSetting.Number("Burst per tick", 2.0F, 1.0F, 4.0F, 1.0F)
              .description(
                  "Burst claims per tick. 2/tick = Dual8 level (16/tick, 0 flags STILL)."));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          new ModuleSetting.Number("Max snaps", 3.0F, 1.0F, 6.0F, 1.0F)
              .description(
                  "Max trigger snaps per shot (~48 blocks each, 60-tick pause between,"
                      + " DualSnapDelay)."));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          new ModuleSetting.Number("Cooldown (ms)", 3000.0F, 500.0F, 5000.0F, 100.0F)
              .description(
                  "Pause between shots. Covers the ~1.8s Vulcan reference frame with margin."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("Require standstill", true)
              .description(
                  "Fires only with no move input, near-zero velocity and on ground (lab STILL"
                      + " context; firing while moving flags, BurstLegit 16F). Preview turns amber"
                      + " while moving."));
  private final ModuleNameService moduleNameService = this.settingCategory("Visual");
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Show target", true)
              .description("Shows the landing box and, while travelling, the ghost box."));
  private final ModuleSetting.Color moduleColor =
      this.setting(this.moduleNameService, new ModuleSetting.Color("Target color", -866194732));
  private final ModuleSetting.Color moduleColor2 =
      this.setting(this.moduleNameService, new ModuleSetting.Color("Ghost color", -855916764));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("See through", true)
              .description("Keeps the boxes visible through terrain."));
  private final BoxOverlayRenderer renderer = new BoxOverlayRenderer();
  private List<MovementBurstScheduleService.SchedClaim> items = List.of();
  private int count;
  private int count2;
  private boolean enabled;
  private boolean enabled2;
  private Vec3 vec3;
  private Vec3 vec32;
  private Vec3 vec33;
  private Vec3 vec34;
  private long timestamp;
  private boolean enabled3;

  public TriggerComponent() {
    super(
        ModuleBuilderData.builder("ClickTeleport")
            .category(ModuleFeatureType.MOVEMENT)
            .description(
                "Click to teleport: fast burst nearby, trigger snap chain far away. Server carries"
                    + " every landing.")
            .build());
    this.moduleColor.visibleWhen(this.moduleBool2);
    this.moduleColor2.visibleWhen(this.moduleBool2);
    this.moduleBool3.visibleWhen(this.moduleBool2);
  }

  @Override
  protected void onEnable() {
    this.updateState();
    this.on(EventAttackInputService.PACKET)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .filter(item -> item.isOutgoing() && item.packet() instanceof ServerboundMovePlayerPacket)
        .run(
            item -> {
              if (this.enabled && !this.enabled2) {
                item.cancel();
              }
            });
    this.on(EventAttackInputService.TICK)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(item -> this.updateState2());
    this.on(EventAttackInputService.WORLD).run(item -> this.updateState());
    this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState4);
  }

  @Override
  protected void onDisable() {
    this.updateState();
    this.renderer.shutdown();
  }

  private void updateState() {
    this.enabled = false;
    this.enabled2 = false;
    this.items = List.of();
    this.count = 0;
    this.count2 = 0;
    this.vec3 = null;
    this.vec32 = null;
    this.vec33 = null;
    this.vec34 = null;
    this.enabled3 = false;
  }

  private void updateState2() {
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    LocalPlayer localPlayer = minecraft.player;
    ClientPacketListener clientPacketListener = minecraft.getConnection();
    if (localPlayer != null && clientPacketListener != null && minecraft.level != null) {
      if (this.count2 < this.items.size()) {
        if (this.vec34 != null) {
          localPlayer.setPos(this.vec34.x, this.vec34.y, this.vec34.z);
          localPlayer.setDeltaMovement(Vec3.ZERO);
        }

        while (this.count2 < this.items.size()
            && this.items.get(this.count2).tick() <= this.count) {
          MovementBurstScheduleService.SchedClaim schedClaim = this.items.get(this.count2++);
          this.vec3 = new Vec3(schedClaim.x(), schedClaim.y(), schedClaim.z());
          this.enabled2 = true;

          try {
            clientPacketListener.send(new Pos(this.vec3, true, false));
          } finally {
            this.enabled2 = false;
          }
        }

        this.count++;
        if (this.count2 >= this.items.size()) {
          this.enabled = false;
          this.vec3 = null;
          this.vec32 = null;
          this.timestamp = System.currentTimeMillis();
        }
      } else {
        this.vec33 = this.createVec3(localPlayer);

        boolean down =
            switch ((String) this.moduleMode.get()) {
              case "Left" -> minecraft.options.keyAttack.isDown();
              case "Right" -> minecraft.options.keyUse.isDown();
              default -> minecraft.options.keyPickItem.isDown();
            };
        int value = down && !this.enabled3 ? 1 : 0;
        this.enabled3 = down;
        if (value != 0 && minecraft.screen == null && localPlayer.isAlive() && this.vec33 != null) {
          if (!(Boolean) this.moduleBool.get() || this.checkCondition(minecraft, localPlayer)) {
            if (System.currentTimeMillis() - this.timestamp
                >= Math.round((Float) this.moduleNumber5.get())) {
              this.updateState3(localPlayer, this.vec33);
            }
          }
        }
      }
    } else {
      this.vec33 = null;
    }
  }

  private boolean checkCondition(Minecraft minecraft, LocalPlayer localPlayer) {
    if (!localPlayer.onGround()) {
      return false;
    }

    Options currentOptions = minecraft.options;
    return !currentOptions.keyUp.isDown()
            && !currentOptions.keyDown.isDown()
            && !currentOptions.keyLeft.isDown()
            && !currentOptions.keyRight.isDown()
            && !currentOptions.keyJump.isDown()
            && !currentOptions.keySprint.isDown()
        ? localPlayer.getDeltaMovement().length() < 0.05
        : false;
  }

  private Vec3 createVec3(LocalPlayer localPlayer) {
    HitResult hitResult =
        localPlayer.pick(((Float) this.moduleNumber.get()).floatValue(), 1.0F, false);
    if (hitResult instanceof BlockHitResult blockHitResult) {
      return this.createVec32(blockHitResult.getBlockPos().relative(blockHitResult.getDirection()));
    } else {
      return hitResult instanceof EntityHitResult entityHitResult
          ? this.createVec32(entityHitResult.getEntity().blockPosition())
          : hitResult.getLocation();
    }
  }

  private Vec3 createVec32(BlockPos blockPos) {
    ClientLevel clientLevel = CoreIsInitializedHandler.mc().level;
    if (clientLevel == null) {
      return null;
    }

    for (int index = 0; index < 4; index++) {
      if (clientLevel.getBlockState(blockPos).isAir()
          && clientLevel.getBlockState(blockPos.above()).isAir()) {
        return new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
      }

      blockPos = blockPos.above();
    }

    return null;
  }

  private void updateState3(LocalPlayer localPlayer, Vec3 currentVec3) {
    Vec3 nextVec3 = localPlayer.position();
    Vec3 previousVec3 = localPlayer.getLookAngle();
    double doubleValue =
        Math.sqrt(previousVec3.x * previousVec3.x + previousVec3.z * previousVec3.z);
    double[] doubles =
        doubleValue < 1.0E-4
            ? new double[] {1.0, 0.0}
            : new double[] {previousVec3.x / doubleValue, previousVec3.z / doubleValue};
    double currentDoubleValue =
        Math.sqrt(
            Math.pow(currentVec3.x - nextVec3.x, 2.0) + Math.pow(currentVec3.z - nextVec3.z, 2.0));
    if (currentDoubleValue <= ((Float) this.moduleNumber2.get()).floatValue()) {
      this.items =
          MovementBurstScheduleService.burstSchedule(
              nextVec3,
              currentVec3,
              doubles,
              Math.max(1, Math.round((Float) this.moduleNumber3.get())));
      this.enabled = false;
    } else {
      this.items =
          MovementBurstScheduleService.triggerSchedule(
              nextVec3, currentVec3, Math.max(1, Math.round((Float) this.moduleNumber4.get())));
      this.enabled = true;
    }

    if (!this.items.isEmpty()) {
      this.count = 0;
      this.count2 = 0;
      this.vec3 = nextVec3;
      MovementBurstScheduleService.SchedClaim currentSize = this.items.get(this.items.size() - 1);
      this.vec32 = new Vec3(currentSize.x(), currentSize.y(), currentSize.z());
      this.vec34 = nextVec3;
    }
  }

  private void updateState4(EventAttackInputService.WorldRender worldRender) {
    if ((Boolean) this.moduleBool2.get()) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      LocalPlayer localPlayer = minecraft.player;
      if (localPlayer != null) {
        AABB aABB = localPlayer.getBoundingBox();
        Vec3 currentVec3 = localPlayer.position();
        ArrayList arrayList = new ArrayList(1);
        ArrayList currentArrayList = new ArrayList(1);
        int value = this.moduleBool.get() && !this.checkCondition(minecraft, localPlayer) ? 0 : 1;
        int currentValue =
            value != 0 ? (Integer) this.moduleColor.get() : (Integer) this.moduleColor2.get();
        if (this.vec32 != null) {
          arrayList.add(aABB.move(this.vec32.subtract(currentVec3)));
        } else if (this.vec33 != null && this.count2 >= this.items.size()) {
          arrayList.add(aABB.move(this.vec33.subtract(currentVec3)));
        }

        if (this.vec3 != null) {
          currentArrayList.add(aABB.move(this.vec3.subtract(currentVec3)));
        }

        Window window = minecraft.getWindow();
        if (!arrayList.isEmpty()) {
          this.renderer.render(
              arrayList,
              worldRender,
              window.getWidth(),
              window.getHeight(),
              this.createStyle(currentValue));
        }

        if (!currentArrayList.isEmpty()) {
          this.renderer.render(
              currentArrayList,
              worldRender,
              window.getWidth(),
              window.getHeight(),
              this.createStyle((Integer) this.moduleColor2.get()));
        }
      }
    }
  }

  private BoxOverlayRenderer.Style createStyle(int value) {
    int currentValue = value & 16777215 | Math.min(38, value >>> 24) << 24;
    return new BoxOverlayRenderer.Style(
        currentValue, value, 0.012F, true, true, (Boolean) this.moduleBool3.get());
  }
}
