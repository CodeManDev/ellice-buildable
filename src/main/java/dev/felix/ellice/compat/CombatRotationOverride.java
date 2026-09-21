package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventCancelHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventSubscribeService;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationNextService;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.SmoothRotationStepper;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.impl.ImplRequestsSprintClient;
import dev.felix.ellice.module.impl.ImplSuspendForPearlService;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

public final class CombatRotationOverride {
  private static CombatRotationOverride.Session session;

  private CombatRotationOverride() {}

  public static boolean active() {
    return session != null;
  }

  public static void start(Minecraft minecraft, double doubleValue, double currentDoubleValue) {
    start(minecraft, CombatOwnsService.Owner.KILL_AURA, doubleValue, currentDoubleValue);
  }

  public static void start(
      Minecraft minecraft,
      CombatOwnsService.Owner owner,
      double doubleValue,
      double currentDoubleValue) {
    if (CoreIsInitializedHandler.isReady()
        && CombatOwnsService.owns(owner)
        && minecraft != null
        && minecraft.player != null
        && minecraft.level != null
        && (owner == CombatOwnsService.Owner.SCAFFOLD || !checkCondition())
        && !RotationObserveService.current().isEmpty()) {
      cancel();
      CombatOwnsService.transfer(owner, CombatOwnsService.Owner.RETURN);
      CombatRotationOverride.Session currentSession =
          new CombatRotationOverride.Session(minecraft, owner, doubleValue, currentDoubleValue);
      session = currentSession;
      EventSubscribeService eventSubscribe = CoreIsInitializedHandler.get().bus();
      currentSession.items2.add(
          eventSubscribe.subscribe(
              EventAttackInputService.PRE_PLAYER_MOVEMENT,
              EventIsAfterHandler.Priority.LATE,
              item -> {
                if (session == currentSession) {
                  currentSession.updateState(minecraft);
                }
              }));
      currentSession.items2.add(
          eventSubscribe.subscribe(
              EventAttackInputService.MOVEMENT_PACKET_PREPARE,
              EventIsAfterHandler.Priority.LATE,
              item -> {
                if (session == currentSession) {
                  currentSession.updateState2();
                }
              }));
      currentSession.items2.add(
          eventSubscribe.subscribe(EventAttackInputService.WORLD, item -> cancel()));
      currentSession.items2.add(
          eventSubscribe.subscribe(
              EventAttackInputService.PLAYER_POSITION_CORRECTED, item -> cancel()));
    }
  }

  public static void cancel() {
    if (session != null) {
      CombatRotationOverride.Session currentSession = session;
      session = null;
      currentSession.items2.forEach(EventCancelHandler::cancel);
      currentSession.items2.clear();
      CombatOwnsService.release(CombatOwnsService.Owner.RETURN);
    }
  }

  public static void cancel(CombatOwnsService.Owner currentOwner) {
    if (session != null && session.owner == currentOwner) {
      cancel();
    }
  }

  private static boolean checkCondition() {
    return CoreIsInitializedHandler.get()
        .modules()
        .get(ImplSuspendForPearlService.class)
        .filter(Module::isEnabled)
        .isPresent();
  }

  public static boolean cancelForManualInteraction(KeyMapping keyMapping) {
    if (session != null && CombatOwnsService.owns(CombatOwnsService.Owner.RETURN)) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == session.localPlayer
          && minecraft.level == session.client2
          && (keyMapping == minecraft.options.keyAttack
              || keyMapping == minecraft.options.keyUse)) {
        boolean down =
            manualInteractionPressed(
                keyMapping.isDown(),
                ((ScaffoldKeyMappingAccess) keyMapping).ellice$pendingClicks());
        if (!down) {
          return false;
        }

        cancel();
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  static boolean manualInteractionPressed(boolean enabled, int value) {
    return enabled || value > 0;
  }

  private static final class Session {
    private final LocalPlayer localPlayer;
    private final ClientLevel client2;
    private final ClientPacketListener items;
    private final RotationNextService rotationNextService = new RotationNextService();
    private final SmoothRotationStepper rotationNextService3 = new SmoothRotationStepper();
    private final CombatCommitService combatCommitService = new CombatCommitService();
    private final List<EventCancelHandler> items2 = new ArrayList<>();
    private final CombatOwnsService.Owner owner;
    private final double value;
    private final double value2;
    private long timestamp;
    private RotationData rotationData;

    private Session(
        Minecraft minecraft,
        CombatOwnsService.Owner currentOwner,
        double doubleValue,
        double currentDoubleValue) {
      this.localPlayer = minecraft.player;
      this.client2 = minecraft.level;
      this.items = minecraft.getConnection();
      this.owner = currentOwner;
      this.value = doubleValue;
      this.value2 = currentDoubleValue;
    }

    private void updateState(Minecraft minecraft) {
      if (CombatOwnsService.owns(CombatOwnsService.Owner.RETURN)
          && minecraft.player == this.localPlayer
          && minecraft.level == this.client2
          && minecraft.getConnection() == this.items
          && this.localPlayer.isAlive()
          && !this.localPlayer.isSpectator()
          && !this.localPlayer.isSleeping()
          && !this.localPlayer.isPassenger()
          && !this.localPlayer.isFallFlying()
          && !this.localPlayer.isAutoSpinAttack()
          && !this.localPlayer.isSwimming()
          && !this.localPlayer.isInWater()
          && !this.localPlayer.isInLava()
          && !this.localPlayer.getAbilities().flying) {
        RotationData currentRotationData = RotationObserveService.current().orElse(null);
        if (currentRotationData == null) {
          CombatRotationOverride.cancel();
        } else if (this.owner == CombatOwnsService.Owner.KILL_AURA
            && this.rotationNextService3.completed(currentRotationData)) {
          CombatRotationOverride.cancel();
        } else {
          RotationData nextRotationData =
              new RotationData(this.localPlayer.getYRot(), this.localPlayer.getXRot());
          double doubleValue = (Double) minecraft.options.sensitivity().get();
          double currentDoubleValue =
              new RotationVanillaGcdService().vanillaGcd(doubleValue) * 1.1 + 1.0E-4;
          RotationPublishService.Snapshot currentSnapshot =
              RotationPublishService.snapshot().orElse(null);
          if (this.owner != CombatOwnsService.Owner.KILL_AURA
              && this.rotationData != null
              && RotationData.distance(currentRotationData, this.rotationData) < 1.0E-4
              && RotationData.distance(currentRotationData, nextRotationData) <= currentDoubleValue
              && currentSnapshot != null
              && Math.abs(
                      RotationData.yawDelta(currentSnapshot.bodyYaw(), this.localPlayer.yBodyRot))
                  < 0.8) {
            CombatRotationOverride.cancel();
          } else {
            RotationNextService.Step step =
                this.owner == CombatOwnsService.Owner.KILL_AURA
                    ? this.rotationNextService3.next(
                        currentRotationData, nextRotationData, doubleValue, this.value, this.value2)
                    : this.rotationNextService.next(
                        currentRotationData,
                        nextRotationData,
                        doubleValue,
                        this.value,
                        this.value2);
            this.rotationData = step.settled() ? step.rotation() : null;
            RotationPublishService.publishReturning(
                nextRotationData, step.rotation(), this.localPlayer.yBodyRot);
            Options currentOptions = minecraft.options;
            ScaffoldRemapService.Input input =
                new ScaffoldRemapService.Input(
                    currentOptions.keyUp.isDown(),
                    currentOptions.keyDown.isDown(),
                    currentOptions.keyLeft.isDown(),
                    currentOptions.keyRight.isDown(),
                    currentOptions.keyJump.isDown(),
                    currentOptions.keyShift.isDown(),
                    currentOptions.keySprint.isDown()
                        || ImplRequestsSprintClient.requestsSprint(minecraft));
            if (this.combatCommitService.commit(
                ++this.timestamp, nextRotationData.yaw(), step.rotation(), input)) {
              this.localPlayer.setSprinting(false);
            }
          }
        }
      } else {
        CombatRotationOverride.cancel();
      }
    }

    private void updateState2() {
      if (!CombatOwnsService.owns(CombatOwnsService.Owner.RETURN)) {
        CombatRotationOverride.cancel();
      } else if (LocalPhysicsFrameBus.wasAborted(this.timestamp)) {
        CombatRotationOverride.cancel();
      } else {
        PlacementOverrideBus.applied()
            .ifPresent(item -> this.combatCommitService.reconcile(this.timestamp, item));
      }
    }
  }
}
