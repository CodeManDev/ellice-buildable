package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.bow.BowMotionTracker;
import dev.felix.ellice.feature.bow.BowNextService;
import dev.felix.ellice.feature.bow.BowObserveService;
import dev.felix.ellice.feature.bow.BowTrajectorySolver;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rod.RodStateService;
import dev.felix.ellice.feature.rod.RodTrajectorySolver;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationSynchronizeService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.ImplRequestsSprintClient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CompatOptionsTracker implements RodStateService.Environment {
  private static CompatOptionsTracker compatOptionsTracker;
  private final BowMotionTracker bowObserveService2 = new BowMotionTracker();
  private final BowNextService bowNextService = new BowNextService();
  private final RotationSynchronizeService rotationSynchronizeService =
      new RotationSynchronizeService();
  private final CombatCommitService combatCommitService = new CombatCommitService();
  private CompatOptionsTracker.Options options2 =
      new CompatOptionsTracker.Options(true, true, false, false, 7.0, 10.0, 65.0, 0.6, true);
  private long timestamp;
  private long timestamp2;
  private long timestamp3;
  private LocalPlayer localPlayer;
  private Object object;
  private Object object2;
  private LivingEntity livingEntity;
  private UUID uUID;
  private BowMotionTracker.Motion motion;
  private RodTrajectorySolver.Kind kind;
  private int count = -1;
  private int count2 = -1;
  private int count3 = -1;
  private int count4 = 8;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private FishingHook fishingHook;
  private RotationData rotationData;
  private CompatOptionsTracker.Shot shot;

  public void update(long offset, CompatOptionsTracker.Options options) {
    this.timestamp = offset;
    this.options2 = options;
    Minecraft minecraft = Minecraft.getInstance();
    if (this.context()) {
      if (compatOptionsTracker != this) {
        this.livingEntity = this.createLivingEntity(minecraft);
      }

      if (this.livingEntity != null && this.checkCondition2(this.livingEntity)) {
        this.uUID = this.livingEntity.getUUID();
        this.motion =
            this.bowObserveService2.observe(
                offset,
                this.uUID,
                CompatContextReadyService.observedPosition(this.livingEntity),
                this.livingEntity.onGround());
      }
    }
  }

  public void clearTracking() {
    this.bowObserveService2.clear();
    this.uUID = null;
    this.livingEntity = null;
    this.motion = null;
    this.bowNextService.reset();
  }

  public static boolean handBusy() {
    return CompatBeginTickService.blocking()
        || compatOptionsTracker != null
            && compatOptionsTracker.checkCondition()
            && !compatOptionsTracker.enabled2
            && !compatOptionsTracker.enabled4;
  }

  public static void reconcileOwnership() {
    if (CombatOwnsService.owns(CombatOwnsService.Owner.ROD)) {
      CompatOptionsTracker currentCompatOptionsTracker = compatOptionsTracker;
      if (currentCompatOptionsTracker == null) {
        CombatOwnsService.release(CombatOwnsService.Owner.ROD);
      } else if (currentCompatOptionsTracker.enabled4) {
        currentCompatOptionsTracker.updateState();
      } else {
        if (!currentCompatOptionsTracker.valid() || !currentCompatOptionsTracker.context()) {
          currentCompatOptionsTracker.release();
        }
      }
    }
  }

  public static void beforeInteraction() {
    if (compatOptionsTracker != null && !compatOptionsTracker.enabled2) {
      CompatOptionsTracker currentCompatOptionsTracker = compatOptionsTracker;
      currentCompatOptionsTracker.timestamp2 = currentCompatOptionsTracker.timestamp + 6L;
      currentCompatOptionsTracker.release();
    }
  }

  public static void consumed(KeyMapping keyMapping, boolean enabled) {
    if (enabled && compatOptionsTracker != null) {
      net.minecraft.client.Options currentOptions = Minecraft.getInstance().options;
      if (keyMapping != currentOptions.keyAttack
          && keyMapping != currentOptions.keyUse
          && keyMapping != currentOptions.keyDrop
          && keyMapping != currentOptions.keySwapOffhand
          && keyMapping != currentOptions.keyPickItem
          && keyMapping != currentOptions.keyInventory) {
        for (KeyMapping currentKeyMapping : currentOptions.keyHotbarSlots) {
          if (keyMapping == currentKeyMapping) {
            beforeInteraction();
            return;
          }
        }
      } else {
        beforeInteraction();
      }
    }
  }

  public void watchdog() {
    if (compatOptionsTracker == this && (!this.context() || !this.valid())) {
      this.release();
    }
  }

  private boolean checkCondition() {
    Minecraft minecraft = Minecraft.getInstance();
    return this.localPlayer != null
        && minecraft.player == this.localPlayer
        && minecraft.level == this.object
        && minecraft.getConnection() == this.object2;
  }

  @Override
  public boolean context() {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer localPlayer = minecraft.player;
    return localPlayer != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.getConnection() != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && !minecraft.isPaused()
        && minecraft.isWindowActive()
        && localPlayer.isAlive()
        && !localPlayer.isSpectator()
        && !localPlayer.getAbilities().instabuild
        && !localPlayer.isSleeping()
        && !localPlayer.isUsingItem()
        && !localPlayer.isHandsBusy()
        && !localPlayer.isPassenger()
        && !localPlayer.isFallFlying()
        && !localPlayer.isAutoSpinAttack()
        && !localPlayer.isInWater()
        && !localPlayer.isInLava()
        && !localPlayer.isSwimming()
        && !localPlayer.getAbilities().flying
        && !minecraft.gameMode.isDestroying()
        && localPlayer.containerMenu == localPlayer.inventoryMenu
        && localPlayer.inventoryMenu.getCarried().isEmpty()
        && !minecraft.options.keyUse.isDown()
        && !minecraft.options.keyAttack.isDown()
        && ((ScaffoldKeyMappingAccess) minecraft.options.keyUse).ellice$pendingClicks() == 0
        && ((ScaffoldKeyMappingAccess) minecraft.options.keyAttack).ellice$pendingClicks() == 0
        && !SoupInventoryBridge.handBusy()
        && !CompatActivateService.handBusy()
        && !CompatReleaseTracker.reserved()
        && !PearlThrowController.reserved()
        && (!CompatBeginTickService.blocking() || compatOptionsTracker == this)
        && !CombatOwnsService.owns(CombatOwnsService.Owner.BOW)
        && !CombatOwnsService.owns(CombatOwnsService.Owner.BUCKET)
        && this.timestamp >= this.timestamp2
        && (!CoreIsInitializedHandler.isReady()
            || !CoreIsInitializedHandler.get()
                .modules()
                .get("ScaffoldWalk")
                .map(item -> item.isEnabled())
                .orElse(false))
        && (!localPlayer.isOnFire()
            || !CoreIsInitializedHandler.isReady()
            || !CoreIsInitializedHandler.get()
                .modules()
                .get("AutoExtinguish")
                .map(item -> item.isEnabled())
                .orElse(false));
  }

  private ImplDecisionTracker createImplDecisionTracker() {
    return CoreIsInitializedHandler.isReady()
        ? CoreIsInitializedHandler.get()
            .modules()
            .get(ImplDecisionTracker.class)
            .filter(Module::isEnabled)
            .orElse(null)
        : null;
  }

  @Override
  public boolean meleeReady() {
    Minecraft minecraft = Minecraft.getInstance();
    ImplDecisionTracker implDecisionTracker = this.createImplDecisionTracker();
    if (minecraft.player != null && this.livingEntity != null) {
      double doubleValue =
          implDecisionTracker == null
              ? minecraft.player.entityInteractionRange()
              : Math.min(
                  minecraft.player.entityInteractionRange(),
                  implDecisionTracker.supportAttackRange());
      return this.livingEntity.getBoundingBox().distanceToSqr(minecraft.player.getEyePosition())
          <= doubleValue * doubleValue;
    } else {
      return false;
    }
  }

  private boolean checkCondition2(LivingEntity livingEntity) {
    LocalPlayer localPlayer = Minecraft.getInstance().player;
    return localPlayer != null
        && livingEntity instanceof Player currentPlayer
        && livingEntity != localPlayer
        && livingEntity.isAlive()
        && !livingEntity.isSpectator()
        && livingEntity.isPickable()
        && livingEntity.isAttackable()
        && !livingEntity.isInvisibleTo(localPlayer)
        && !livingEntity.isAlliedTo(localPlayer)
        && !localPlayer.isAlliedTo(livingEntity)
        && !currentPlayer.getAbilities().instabuild
        && localPlayer.canHarmPlayer(currentPlayer)
        && !FriendSyncController.excluded(FriendsMode.KILL_AURA, livingEntity)
        && !CompatDecisionTracker.excluded(AntibotFeatureType.AUTO_ROD, livingEntity);
  }

  private LivingEntity createLivingEntity(Minecraft minecraft) {
    ImplDecisionTracker implDecisionTracker = this.createImplDecisionTracker();
    UUID currentUUID =
        implDecisionTracker == null ? null : implDecisionTracker.targetUuid().orElse(null);
    if (currentUUID != null) {
      for (AbstractClientPlayer abstractClientPlayer : minecraft.level.players()) {
        if (abstractClientPlayer.getUUID().equals(currentUUID)) {
          return this.checkCondition2(abstractClientPlayer) ? abstractClientPlayer : null;
        }
      }
    }

    if (this.options2.auraOnly()) {
      return null;
    }

    RotationData rotationData =
        new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot());
    return minecraft.level.players().stream()
        .filter(this::checkCondition2)
        .filter(
            item ->
                item.distanceToSqr(minecraft.player)
                    <= Math.pow(Math.max(this.options2.range(), this.options2.rodRange()), 2.0))
        .filter(
            item ->
                Math.abs(
                        RotationData.yawDelta(
                            rotationData.yaw(),
                            RotationData.lookAt(
                                    createRotationData7(minecraft.player.getEyePosition()),
                                    createRotationData7(item.getBoundingBox().getCenter()))
                                .yaw()))
                    <= 90.0)
        .filter(
            item ->
                checkCondition4(
                    minecraft,
                    createRotationData7(minecraft.player.getEyePosition()),
                    createRotationData7(item.getBoundingBox().getCenter()),
                    item))
        .min(
            Comparator.comparingDouble(
                item ->
                    item.distanceToSqr(minecraft.player)
                        + (item.getUUID().equals(this.uUID) ? -3 : 0)))
        .orElse(null);
  }

  @Override
  public RodTrajectorySolver.Kind acquire(boolean currentEnabled, boolean nextEnabled) {
    Minecraft minecraft = Minecraft.getInstance();
    if (compatOptionsTracker == null
        && this.context()
        && this.livingEntity != null
        && this.motion != null
        && this.checkCondition2(this.livingEntity)
        && this.livingEntity.hurtTime <= 6
        && minecraft.player.fishing == null
        && !(this.livingEntity.distanceTo(minecraft.player) < 2.6)
        && !this.meleeReady()) {
      for (RodTrajectorySolver.Kind currentKind : RodTrajectorySolver.Kind.values()) {
        if ((currentKind == RodTrajectorySolver.Kind.ROD
                ? this.options2.rod()
                    && currentEnabled
                    && !(this.livingEntity.distanceTo(minecraft.player) > this.options2.rodRange())
                : nextEnabled
                    && !(this.livingEntity.distanceTo(minecraft.player) > this.options2.range())
                    && (currentKind == RodTrajectorySolver.Kind.SNOWBALL
                        ? this.options2.snow()
                        : this.options2.eggs()))
            && (currentKind != RodTrajectorySolver.Kind.ROD
                || !this.options2.saveRod()
                || this.calculateValue(minecraft) >= 0)) {
          int value = this.calculateValue2(minecraft, currentKind);
          if (value >= 0) {
            CompatStatusTracker.beforeInteraction();
            this.kind = currentKind;
            CompatOptionsTracker.Shot currentShot = this.createShot(minecraft, currentKind);
            if (currentShot != null) {
              this.localPlayer = minecraft.player;
              this.object = minecraft.level;
              this.object2 = minecraft.getConnection();
              this.count = value;
              this.count2 = this.count3 = InventoryInputActions.selected(minecraft);
              this.enabled = false;
              this.enabled4 = false;
              this.fishingHook = null;
              this.shot = currentShot;
              this.count4 = (int) Math.ceil(currentShot.solution().ticks());
              compatOptionsTracker = this;
              CombatRotationOverride.cancel();
              this.rotationData =
                  CombatOwnsService.claim(CombatOwnsService.Owner.ROD)
                      .orElseGet(
                          () ->
                              RotationObserveService.current()
                                  .orElse(
                                      new RotationData(
                                          this.localPlayer.getYRot(), this.localPlayer.getXRot())));
              this.bowNextService.reset();
              return this.kind;
            }
          }
        }
      }

      this.kind = null;
      return null;
    } else {
      return null;
    }
  }

  private static boolean checkCondition3(ItemStack itemStack, RodTrajectorySolver.Kind kind) {
    return itemStack.is(
        switch (kind) {
          case ROD -> Items.FISHING_ROD;
          case SNOWBALL -> Items.SNOWBALL;
          case EGG -> Items.EGG;
        });
  }

  private int calculateValue(Minecraft minecraft) {
    if (minecraft.player.getOffhandItem().is(Items.FISHING_ROD)) {
      return -1;
    }

    int value =
        compatOptionsTracker == this ? this.count2 : InventoryInputActions.selected(minecraft);
    if (value >= 0 && !minecraft.player.getInventory().getItem(value).is(Items.FISHING_ROD)) {
      return value;
    }

    for (int index = 0; index < 9; index++) {
      if (!minecraft.player.getInventory().getItem(index).is(Items.FISHING_ROD)) {
        return index;
      }
    }

    return -1;
  }

  private int calculateValue2(Minecraft minecraft, RodTrajectorySolver.Kind kind) {
    for (int value : new int[] {40, 0, 1, 2, 3, 4, 5, 6, 7, 8}) {
      ItemStack itemStack =
          value == 40
              ? minecraft.player.getOffhandItem()
              : minecraft.player.getInventory().getItem(value);
      if (checkCondition3(itemStack, kind)
          && itemStack.isItemEnabled(minecraft.level.enabledFeatures())
          && !minecraft.player.getCooldowns().isOnCooldown(itemStack)
          && (!itemStack.isDamageableItem()
              || itemStack.getMaxDamage() - itemStack.getDamageValue() > 5)) {
        return value;
      }
    }

    return -1;
  }

  private ItemStack createItemStack() {
    return this.count == 40
        ? this.localPlayer.getOffhandItem()
        : this.localPlayer.getInventory().getItem(this.count);
  }

  private InteractionHand createInteractionHand() {
    return this.count == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
  }

  @Override
  public boolean valid() {
    return compatOptionsTracker == this
        && this.checkCondition()
        && (this.enabled4 || CombatOwnsService.owns(CombatOwnsService.Owner.ROD))
        && InventoryInputActions.selected(Minecraft.getInstance()) == this.count3
        && checkCondition3(this.createItemStack(), this.kind);
  }

  @Override
  public boolean targetLost() {
    return this.livingEntity == null
        || !this.checkCondition2(this.livingEntity)
        || !this.checkCondition()
        || Minecraft.getInstance().level.getEntity(this.livingEntity.getId()) != this.livingEntity
        || this.livingEntity.distanceTo(this.localPlayer)
            > Math.max(this.options2.range(), this.options2.rodRange()) + 1.0;
  }

  @Override
  public int expectedFlight() {
    return this.count4;
  }

  @Override
  public RodStateService.Hook hook() {
    if (this.checkCondition() && this.localPlayer.fishing != null) {
      if (this.fishingHook == null) {
        this.fishingHook = this.localPlayer.fishing;
      }

      if (this.localPlayer.fishing != this.fishingHook) {
        return RodStateService.Hook.OTHER;
      }

      Entity entity = this.fishingHook.getHookedIn();
      if (entity != null) {
        return this.livingEntity != null && entity.getUUID().equals(this.livingEntity.getUUID())
            ? RodStateService.Hook.TARGET
            : RodStateService.Hook.OTHER;
      }

      if (this.livingEntity != null && this.fishingHook.tickCount >= 2) {
        Vec3 vec3 = this.livingEntity.position().subtract(this.fishingHook.position());
        Vec3 currentVec3 = this.fishingHook.getDeltaMovement();
        if (vec3.x * currentVec3.x + vec3.z * currentVec3.z < -0.05) {
          return RodStateService.Hook.BLOCKED;
        }
      }

      return !this.fishingHook.onGround()
              && !this.fishingHook.horizontalCollision
              && !this.fishingHook.isInWater()
          ? RodStateService.Hook.FLYING
          : RodStateService.Hook.BLOCKED;
    } else {
      return RodStateService.Hook.NONE;
    }
  }

  @Override
  public void aim(boolean currentEnabled) {
    if (this.valid() && !this.enabled4) {
      Minecraft currentMinecraft = Minecraft.getInstance();
      if (!this.enabled && !currentEnabled) {
        this.shot = this.targetLost() ? null : this.createShot(currentMinecraft, this.kind);
      }

      RotationData currentRotationData =
          this.shot != null ? this.shot.solution().rotation() : this.rotationData;
      if (this.enabled && this.livingEntity != null && !this.targetLost()) {
        currentRotationData =
            RotationData.lookAt(
                createRotationData7(this.localPlayer.getEyePosition()),
                createRotationData7(this.livingEntity.getBoundingBox().getCenter()));
      }

      RotationData nextRotationData = RotationObserveService.current().orElse(this.rotationData);
      this.rotationData =
          this.bowNextService
              .next(
                  nextRotationData,
                  currentRotationData,
                  (Double) currentMinecraft.options.sensitivity().get(),
                  this.options2.turn(),
                  0.15)
              .rotation();
      if (this.createImplDecisionTracker() != null) {
        this.rotationSynchronizeService.synchronize(
            RotationObserveService.previousTickRotation().orElse(nextRotationData),
            nextRotationData);
        this.rotationData =
            this.rotationSynchronizeService
                .next(
                    nextRotationData,
                    this.rotationData,
                    (Double) currentMinecraft.options.sensitivity().get(),
                    0.0,
                    this.options2.turn())
                .rotation();
      }

      RotationPublishService.publish(
          new RotationData(this.localPlayer.getYRot(), this.localPlayer.getXRot()),
          this.rotationData);
      net.minecraft.client.Options currentOptions = currentMinecraft.options;
      ScaffoldRemapService.Input input =
          new ScaffoldRemapService.Input(
              currentOptions.keyUp.isDown(),
              currentOptions.keyDown.isDown(),
              currentOptions.keyLeft.isDown(),
              currentOptions.keyRight.isDown(),
              currentOptions.keyJump.isDown(),
              currentOptions.keyShift.isDown(),
              currentOptions.keySprint.isDown()
                  || ImplRequestsSprintClient.requestsSprint(currentMinecraft));
      if (this.combatCommitService.commit(
          this.timestamp, this.localPlayer.getYRot(), this.rotationData, input)) {
        this.localPlayer.setSprinting(false);
      }
    }
  }

  public void reconcile(long longValue) {
    if (this.valid() && !this.enabled4) {
      if (LocalPhysicsFrameBus.wasAborted(longValue)) {
        this.release();
      } else {
        PlacementOverrideBus.applied()
            .ifPresent(item -> this.combatCommitService.reconcile(longValue, item));
      }
    }
  }

  @Override
  public boolean aligned(boolean enabled) {
    if (this.valid() && !this.enabled4) {
      if (this.timestamp3 != 0L && System.nanoTime() - this.timestamp3 < 200000000L) {
        return false;
      } else {
        Minecraft minecraft = Minecraft.getInstance();
        RotationObserveService.Snapshot currentSnapshot =
            RotationObserveService.snapshot().orElse(null);
        if (currentSnapshot != null
            && !(RotationData.distance(
                    currentSnapshot.rotation(),
                    new RotationData(
                        (float) this.rotationData.yaw(), (float) this.rotationData.pitch()))
                > 1.0E-5)
            && !(currentSnapshot
                    .wireEyePosition()
                    .subtract(createRotationData7(this.localPlayer.getEyePosition()))
                    .length()
                > 0.1)) {
          RotationVector rotationVector = RotationObserveService.inheritedVelocity().orElse(null);
          return enabled
              || rotationVector != null
                  && this.shot != null
                  && !this.targetLost()
                  && this.calculateValue3(
                          minecraft,
                          this.shot,
                          this.kind,
                          currentSnapshot.rotation(),
                          currentSnapshot.wireEyePosition(),
                          rotationVector)
                      >= this.options2.quality();
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
  }

  @Override
  public boolean use(boolean currentEnabled) {
    if (CompatBeginTickService.canAct()
        && this.aligned(currentEnabled)
        && !this.localPlayer.getCooldowns().isOnCooldown(this.createItemStack())) {
      if (currentEnabled
          ? this.fishingHook != null && this.localPlayer.fishing == this.fishingHook
          : this.kind != RodTrajectorySolver.Kind.ROD || this.localPlayer.fishing == null) {
        Minecraft minecraft = Minecraft.getInstance();
        this.enabled3 = false;
        this.enabled2 = true;

        try {
          CompatConfigureService.beforeInteraction();
          if (this.count != 40) {
            InventoryInputActions.select(minecraft, this.count);
            this.count3 = this.count;
          }

          this.enabled = true;
          minecraft.gameMode.ensureHasSentCarriedItem();
          InteractionResult interactionResult =
              ScaffoldForActorService.withRotation(
                  this.localPlayer,
                  this.rotationData,
                  () -> minecraft.gameMode.useItem(this.localPlayer, this.createInteractionHand()));
          if (this.enabled3) {
            CompatBeginTickService.accepted();
            this.timestamp3 = System.nanoTime();
            if (interactionResult.consumesAction()) {
              this.localPlayer.swing(this.createInteractionHand());
            }
          }

          return this.enabled3;
        } finally {
          this.enabled2 = false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public boolean resetRod() {
    if (this.valid()
        && this.kind == RodTrajectorySolver.Kind.ROD
        && this.fishingHook != null
        && this.localPlayer.fishing == this.fishingHook) {
      Minecraft minecraft = Minecraft.getInstance();
      int value = this.calculateValue(minecraft);
      if (value < 0) {
        return false;
      }

      this.enabled2 = true;

      try {
        InventoryInputActions.select(minecraft, value);
        this.count3 = value;
        this.enabled = true;
        this.enabled4 = true;
        minecraft.gameMode.ensureHasSentCarriedItem();
        this.updateState();
        return true;
      } finally {
        this.enabled2 = false;
      }
    } else {
      return false;
    }
  }

  public void accepted(Object value) {
    if (this.enabled2
        && value instanceof ServerboundUseItemPacket serverboundUseItemPacket
        && serverboundUseItemPacket.getHand() == this.createInteractionHand()) {
      this.enabled3 = true;
    }
  }

  @Override
  public void release() {
    if (compatOptionsTracker == this) {
      compatOptionsTracker = null;

      try {
        if (this.checkCondition()
            && this.enabled
            && this.count != 40
            && InventoryInputActions.selected(Minecraft.getInstance()) == this.count3
            && (!this.enabled4
                || this.localPlayer.fishing == null
                || !this.localPlayer.getInventory().getItem(this.count2).is(Items.FISHING_ROD))) {
          InventoryInputActions.select(Minecraft.getInstance(), this.count2);
          Minecraft.getInstance().gameMode.ensureHasSentCarriedItem();
        }
      } finally {
        this.updateState();
        this.shot = null;
        this.kind = null;
        this.fishingHook = null;
        this.enabled = false;
        this.enabled4 = false;
        this.count = -1;
        this.bowNextService.reset();
      }
    }
  }

  private void updateState() {
    if (CombatOwnsService.owns(CombatOwnsService.Owner.ROD)) {
      if (this.createImplDecisionTracker() == null) {
        CombatRotationOverride.start(
            Minecraft.getInstance(), CombatOwnsService.Owner.ROD, this.options2.turn(), 0.15);
        CombatOwnsService.release(CombatOwnsService.Owner.ROD);
      } else {
        CombatOwnsService.transfer(CombatOwnsService.Owner.ROD, CombatOwnsService.Owner.KILL_AURA);
      }
    }
  }

  private CompatOptionsTracker.Shot createShot(Minecraft minecraft, RodTrajectorySolver.Kind kind) {
    if (this.livingEntity != null && this.motion != null) {
      RotationVector rotationVector = CompatContextReadyService.observedPosition(this.livingEntity);
      RotationVector currentRotationVector =
          rotationVector.subtract(createRotationData7(this.livingEntity.position()));
      AABB currentX =
          this.livingEntity
              .getBoundingBox()
              .move(
                  currentRotationVector.x(), currentRotationVector.y(), currentRotationVector.z());
      ArrayList<CompatOptionsTracker.Body> arrayList = new ArrayList<>();

      for (BowObserveService.Weighted weighted :
          this.motion.ensemble().at(kind == RodTrajectorySolver.Kind.ROD ? 8.0 : 5.0)) {
        List<RotationVector> items = weighted.hypothesis().steps();
        List<RotationVector> currentSize =
            CompatForecastService.forecast(
                currentX,
                items.subList(0, Math.min(items.size(), 26)),
                this.motion.velocity().y(),
                this.motion.grounded(),
                this.livingEntity.maxUpStep(),
                this.livingEntity.hurtTime > 0,
                item -> {
                  ArrayList<VoxelShape> collisions = new ArrayList<>();
                  minecraft
                      .level
                      .getBlockCollisions(this.livingEntity, item)
                      .forEach(collisions::add);
                  return collisions;
                },
                item ->
                    minecraft.level.hasChunkAt(
                        BlockPos.containing(item.minX, item.minY, item.minZ)));
        arrayList.add(
            new CompatOptionsTracker.Body(
                createRotationData2(currentX), currentSize, weighted.weight()));
      }

      double doubleValue = this.motion.ageTicks() + 1;
      PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID());
      if (!minecraft.hasSingleplayerServer() && playerInfo != null) {
        doubleValue += Math.clamp(playerInfo.getLatency() / 50.0, 0.0, 3.0);
      }

      double currentDoubleValue = doubleValue;
      RodTrajectorySolver.Launch launch = this.createLaunch(minecraft);
      if (launch == null) {
        return null;
      }

      RotationVector nextRotationVector = launch.origin();
      RotationVector previousRotationVector = launch.velocity();
      CompatOptionsTracker.Shot shot = null;
      double nextDoubleValue = 0.0;

      for (CompatOptionsTracker.Body body :
          arrayList.stream()
              .sorted(Comparator.comparingDouble(CompatOptionsTracker.Body::weight).reversed())
              .limit(3L)
              .toList()) {
        RodTrajectorySolver.Solution solution =
            RodTrajectorySolver.solve(
                    kind,
                    nextRotationVector,
                    previousRotationVector,
                    item ->
                        body.box()
                            .sample(0.5, 0.55, 0.5)
                            .add(body.offset(item + currentDoubleValue)))
                .orElse(null);
        if (solution != null) {
          CompatOptionsTracker.Shot currentShot =
              new CompatOptionsTracker.Shot(solution, arrayList, doubleValue);
          double previousDoubleValue =
              this.calculateValue3(
                  minecraft,
                  currentShot,
                  kind,
                  solution.rotation(),
                  nextRotationVector,
                  previousRotationVector);
          if (previousDoubleValue > nextDoubleValue) {
            nextDoubleValue = previousDoubleValue;
            shot = currentShot;
          }
        }
      }

      return nextDoubleValue >= this.options2.quality() ? shot : null;
    } else {
      return null;
    }
  }

  private RodTrajectorySolver.Launch createLaunch(Minecraft currentMinecraft) {
    LocalPlayer localPlayer = currentMinecraft.player;
    ScaffoldCompatibility scaffoldCompatibility =
        CompatAdapterService.scaffoldEnvironment().orElse(null);
    if (scaffoldCompatibility == null) {
      return null;
    }

    net.minecraft.client.Options currentOptions = currentMinecraft.options;
    ScaffoldRemapService.Input input =
        new ScaffoldRemapService.Input(
            currentOptions.keyUp.isDown(),
            currentOptions.keyDown.isDown(),
            currentOptions.keyLeft.isDown(),
            currentOptions.keyRight.isDown(),
            currentOptions.keyJump.isDown(),
            currentOptions.keyShift.isDown(),
            currentOptions.keySprint.isDown()
                || ImplRequestsSprintClient.requestsSprint(currentMinecraft)
                || localPlayer.isSprinting());
    double doubleValue =
        RotationObserveService.current()
            .map(RotationData::yaw)
            .orElse((double) localPlayer.getYRot());
    ScaffoldRemapService.Result result =
        new ScaffoldRemapService().remap(localPlayer.getYRot(), doubleValue, input);
    ScaffoldRemapService.Input currentInput = result.serverInput();
    ScaffoldRemapService.WorldVector currentWorldVector = result.serverWorldVector();
    double currentDoubleValue =
        localPlayer.onGround()
            ? scaffoldCompatibility.groundInputAccelerationEstimate(
                currentMinecraft,
                currentInput.forwardAxis(),
                currentInput.leftAxis(),
                currentInput.sprint())
            : scaffoldCompatibility.airInputAccelerationEstimate(
                    currentMinecraft, currentInput.forwardAxis(), currentInput.leftAxis())
                * (currentInput.sprint() ? 1.3 : 1.0);
    if (!Double.isFinite(currentDoubleValue)) {
      return null;
    }

    if (localPlayer.onGround() && localPlayer.isSprinting() && !currentInput.sprint()) {
      currentDoubleValue /= 1.300000011920929;
    }

    Vec3 vec3 = localPlayer.getDeltaMovement();
    double nextDoubleValue = vec3.y;
    if (localPlayer.onGround() && currentInput.jump()) {
      nextDoubleValue = scaffoldCompatibility.jumpVelocityEstimate(currentMinecraft);
    }

    Vec3 currentX =
        new Vec3(
            vec3.x + currentWorldVector.x() * currentDoubleValue,
            nextDoubleValue,
            vec3.z + currentWorldVector.z() * currentDoubleValue);
    if (localPlayer.onGround() && currentInput.jump() && currentInput.sprint()) {
      ScaffoldRemapService.WorldVector nextWorldVector =
          ScaffoldRemapService.worldVector(doubleValue, 1, 0);
      currentX = currentX.add(nextWorldVector.x() * 0.2, 0.0, nextWorldVector.z() * 0.2);
    }

    Vec3 currentVec3 =
        Entity.collideBoundingBox(
            localPlayer, currentX, localPlayer.getBoundingBox(), currentMinecraft.level, List.of());
    int value = currentX.y < 0.0 && Math.abs(currentVec3.y - currentX.y) > 1.0E-7 ? 1 : 0;
    return new RodTrajectorySolver.Launch(
        createRotationData7(localPlayer.getEyePosition().add(currentVec3)),
        new RotationVector(currentVec3.x, value != 0 ? 0.0 : currentVec3.y, currentVec3.z));
  }

  private double calculateValue3(
      Minecraft minecraft,
      CompatOptionsTracker.Shot shot,
      RodTrajectorySolver.Kind kind,
      RotationData rotationData,
      RotationVector rotationVector,
      RotationVector currentRotationVector) {
    double doubleValue = 0.0;
    RodTrajectorySolver.Launch currentLaunch =
        RodTrajectorySolver.launch(kind, rotationVector, currentRotationVector, rotationData);

    for (CompatOptionsTracker.Body body : shot.bodies()) {
      if (!(body.weight() < 0.01)) {
        RotationVector nextRotationVector = currentLaunch.origin();
        RotationBoundingBox rotationBoundingBox = body.box();
        double currentDoubleValue = 0.055;
        RotationBoundingBox currentRotationBoundingBox =
            new RotationBoundingBox(
                rotationBoundingBox.minX() + currentDoubleValue,
                rotationBoundingBox.minY() + currentDoubleValue,
                rotationBoundingBox.minZ() + currentDoubleValue,
                rotationBoundingBox.maxX() - currentDoubleValue,
                rotationBoundingBox.maxY() - currentDoubleValue,
                rotationBoundingBox.maxZ() - currentDoubleValue);

        for (int index = 1;
            index <= Math.min(24, (int) Math.ceil(shot.solution().ticks()) + 3);
            index++) {
          RotationVector previousRotationVector =
              RodTrajectorySolver.position(kind, currentLaunch, index);
          OptionalDouble optionalDouble =
              BowTrajectorySolver.hitFraction(
                  nextRotationVector,
                  previousRotationVector,
                  currentRotationBoundingBox,
                  body.offset(index - 1 + shot.lead()),
                  body.offset(index + shot.lead()));
          RotationVector sourceRotationVector =
              optionalDouble.isPresent()
                  ? nextRotationVector.add(
                      previousRotationVector
                          .subtract(nextRotationVector)
                          .multiply(optionalDouble.getAsDouble()))
                  : previousRotationVector;
          if (!checkCondition4(
              minecraft, nextRotationVector, sourceRotationVector, this.livingEntity)) {
            break;
          }

          if (optionalDouble.isPresent()) {
            doubleValue += body.weight();
            break;
          }

          nextRotationVector = previousRotationVector;
        }
      }
    }

    return doubleValue;
  }

  private static boolean checkCondition4(
      Minecraft minecraft,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      Entity entity) {
    if (minecraft.level.hasChunkAt(
            BlockPos.containing(rotationVector.x(), rotationVector.y(), rotationVector.z()))
        && minecraft.level.hasChunkAt(
            BlockPos.containing(
                currentRotationVector.x(), currentRotationVector.y(), currentRotationVector.z()))) {
      Vec3 vec3 = createVec3(rotationVector);
      Vec3 currentVec3 = createVec3(currentRotationVector);
      if (minecraft
              .level
              .clip(new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.ANY, minecraft.player))
              .getType()
          != Type.MISS) {
        return false;
      }

      for (Entity currentEntity :
          minecraft.level.getEntities(
              minecraft.player,
              new AABB(vec3, currentVec3).inflate(0.5),
              item ->
                  item != entity && item.isAlive() && item.isPickable() && !item.isSpectator())) {
        if (currentEntity.getBoundingBox().inflate(0.3).clip(vec3, currentVec3).isPresent()) {
          return false;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private static RotationVector createRotationData7(Vec3 vec3) {
    return new RotationVector(vec3.x, vec3.y, vec3.z);
  }

  private static Vec3 createVec3(RotationVector rotationVector) {
    return new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z());
  }

  private static RotationBoundingBox createRotationData2(AABB aABB) {
    return new RotationBoundingBox(
        aABB.minX, aABB.minY, aABB.minZ, aABB.maxX, aABB.maxY, aABB.maxZ);
  }

  private record Body(RotationBoundingBox box, List<RotationVector> offsets, double weight) {
    RotationVector offset(double doubleValue) {
      double currentSize = Math.clamp(doubleValue, 0.0, this.offsets.size() - 1);
      int value = (int) currentSize;
      return this.offsets
          .get(value)
          .add(
              this.offsets
                  .get(Math.min(value + 1, this.offsets.size() - 1))
                  .subtract(this.offsets.get(value))
                  .multiply(currentSize - value));
    }
  }

  public record Options(
      boolean rod,
      boolean snow,
      boolean eggs,
      boolean auraOnly,
      double rodRange,
      double range,
      double turn,
      double quality,
      boolean saveRod) {}

  private record Shot(
      RodTrajectorySolver.Solution solution, List<CompatOptionsTracker.Body> bodies, double lead) {}
}
