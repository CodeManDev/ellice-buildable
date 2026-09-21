package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class MovementPacketRotationMixin {
  @Unique private boolean ellice$passengerMovementPacketPending;

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
              shift = Shift.BEFORE),
      require = 1)
  private void ellice$beginScaffoldPhysics(CallbackInfo ci) {
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get()
          .bus()
          .post(
              EventAttackInputService.PRE_PLAYER_MOVEMENT,
              EventAttackInputService.PrePlayerMovement.INSTANCE);
    }

    LocalPhysicsFrameBus.beginLocalPhysics();
  }

  @Inject(
      method = "aiStep",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V",
              shift = Shift.BEFORE),
      require = 1)
  private void ellice$abortUnsupportedScaffoldPhysics(CallbackInfo ci) {
    LocalPlayer player = (LocalPlayer) (Object) this;
    if (LocalPhysicsFrameBus.isLocalPhysicsActive()
        && (player.isPassenger()
            || player.isFallFlying()
            || player.isAutoSpinAttack()
            || player.isSwimming()
            || player.isInWater()
            || player.isInLava()
            || player.getAbilities().flying)) {
      if (LocalPhysicsFrameBus.abortLocalPhysics()) {
        RotationPublishService.clear();
        ScaffoldPublishService.clear();
        PlacementOverrideBus.clear();
      }
    }
  }

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
              shift = Shift.AFTER),
      require = 1)
  private void ellice$prepareMovementPacket(CallbackInfo ci) {
    LocalPhysicsFrameBus.endLocalPhysics();
    if (CoreIsInitializedHandler.isReady()) {
      Input input = ((LocalPlayer) (Object) this).input.keyPresses;
      PlacementOverrideBus.captureApplied(
          new ScaffoldRemapService.Input(
              input.forward(),
              input.backward(),
              input.left(),
              input.right(),
              input.jump(),
              input.shift(),
              input.sprint()));
      CoreIsInitializedHandler.get()
          .bus()
          .post(
              EventAttackInputService.MOVEMENT_PACKET_PREPARE,
              EventAttackInputService.MovementPacketPrepare.INSTANCE);
    }
  }

  @Inject(method = "tick", at = @At("RETURN"))
  private void ellice$finishMovementPacket(CallbackInfo ci) {
    LocalPhysicsFrameBus.endLocalPhysics();
    if (this.ellice$passengerMovementPacketPending) {
      this.ellice$passengerMovementPacketPending = false;
      ellice$postPostMovementPacket();
    }
  }

  @Inject(method = "sendPosition", at = @At("HEAD"))
  private void ellice$beforeMovementPacket(CallbackInfo ci) {
    CompatProfileTracker.flushPendingSync();
    ellice$postPreMovementPacket();
  }

  @Inject(method = "sendPosition", at = @At("RETURN"))
  private void ellice$afterMovementPacket(CallbackInfo ci) {
    ellice$postPostMovementPacket();
  }

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F",
              shift = Shift.BEFORE))
  private void ellice$beforePassengerMovementPacket(CallbackInfo ci) {
    this.ellice$passengerMovementPacketPending = true;
    CompatProfileTracker.flushPendingSync();
    ellice$postPreMovementPacket();
  }

  @Redirect(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"),
      require = 4)
  private float ellice$serverYaw(LocalPlayer player) {
    return RotationPublishService.current()
        .map(rotation -> (float) rotation.yaw())
        .orElseGet(player::getYRot);
  }

  @Redirect(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"),
      require = 1)
  private float ellice$passengerServerYaw(LocalPlayer player) {
    return this.ellice$serverYaw(player);
  }

  @Redirect(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"),
      require = 4)
  private float ellice$serverPitch(LocalPlayer player) {
    return RotationPublishService.current()
        .map(rotation -> (float) rotation.pitch())
        .orElseGet(player::getXRot);
  }

  @Redirect(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"),
      require = 1)
  private float ellice$passengerServerPitch(LocalPlayer player) {
    return this.ellice$serverPitch(player);
  }

  @Redirect(
      method = "tick",
      at =
          @At(
              value = "FIELD",
              target =
                  "Lnet/minecraft/client/player/ClientInput;keyPresses:Lnet/minecraft/world/entity/player/Input;"),
      require = 3)
  private Input ellice$serverMovementInput(ClientInput input) {
    ScaffoldRemapService.Input mapped = ScaffoldPublishService.current().orElse(null);
    return mapped == null
        ? input.keyPresses
        : new Input(
            mapped.forward(),
            mapped.backward(),
            mapped.left(),
            mapped.right(),
            mapped.jump(),
            mapped.sneak(),
            mapped.sprint());
  }

  @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
  private void ellice$suppressInvalidSprint(CallbackInfoReturnable<Boolean> cir) {
    if (ScaffoldPublishService.suppressLocalSprint()
        || CoreAutoSprintRequestedClient.suppressCombatSprint()) {
      cir.setReturnValue(false);
    }
  }

  private static void ellice$postPostMovementPacket() {
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get()
          .bus()
          .post(
              EventAttackInputService.POST_MOVEMENT_PACKET,
              EventAttackInputService.PostMovementPacket.INSTANCE);
    }
  }

  private static void ellice$postPreMovementPacket() {
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get()
          .bus()
          .post(
              EventAttackInputService.PRE_MOVEMENT_PACKET,
              EventAttackInputService.PreMovementPacket.INSTANCE);
    }
  }
}
