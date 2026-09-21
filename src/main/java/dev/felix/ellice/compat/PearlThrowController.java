package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.pearl.PearlPlanService;
import dev.felix.ellice.feature.pearl.PearlStatusService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.impl.ImplSuspendForPearlService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class PearlThrowController implements PearlStatusService.Environment {
   private static PearlThrowController compatOptionsTracker2;
   private final PearlPlanService pearlPlanService = new PearlPlanService();
   private final CombatCommitService combatCommitService = new CombatCommitService();
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private final LinkedHashMap<BlockPos, RotationVector> entries = new LinkedHashMap<>();
   private PearlThrowController.Options options2 = new PearlThrowController.Options(
      1.2,
      24.0,
      6.0,
      2,
      true
   );
   private LocalPlayer localPlayer;
   private Object object;
   private Object object2;
   private Object object3;
   private RotationVector rotationData7;
   private RotationVector rotationData72;
   private int count;
   private int count2;
   private List<int[]> items = List.of();
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private int count3 = -1;
   private int count4 = -1;
   private int count5 = -1;
   private int count6 = -1;
   private ItemStack itemStack = ItemStack.EMPTY;
   private ItemStack itemStack2 = ItemStack.EMPTY;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private boolean enabled6;
   private boolean enabled7;
   private RotationData rotationData;
   private PearlPlanService.Plan plan2;

   public static boolean reserved() {
      return compatOptionsTracker2 != null && compatOptionsTracker2.checkCondition();
   }

   public static boolean handBusy() {
      return CompatBeginTickService.blocking() || reserved() && !compatOptionsTracker2.enabled;
   }

   public void update(long offset, PearlThrowController.Options currentOptions) {
      this.timestamp = offset;
      this.options2 = currentOptions;
      Minecraft minecraft = Minecraft.getInstance();
      if (this.object3 != minecraft.level) {
         this.clearTerrain();
         this.object3 = minecraft.level;
      }

      if (minecraft.player != null && minecraft.level != null && minecraft.player.onGround()) {
         this.rotationData7 = createRotationData7(minecraft.player.position());
         this.rotationData72 = null;
         this.count = 0;
      }

      if (minecraft.player != null
         && minecraft.level != null
         && minecraft.getConnection() != null
         && minecraft.gameMode != null
         && !minecraft.options.keyUse.isDown()
         && CompatCanStartService.holding(minecraft)
         && this.danger()) {
         CompatCanStartService.release(minecraft);
      }
   }

   public void clearTerrain() {
      this.entries.clear();
      this.rotationData7 = this.rotationData72 = null;
      this.count = 0;
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
         && !localPlayer.getAbilities().flying
         && !localPlayer.isPassenger()
         && !localPlayer.isSleeping()
         && !localPlayer.isFallFlying()
         && !localPlayer.isAutoSpinAttack()
         && !localPlayer.isUsingItem()
         && !localPlayer.isHandsBusy()
         && !localPlayer.isInLava()
         && !minecraft.gameMode.isDestroying()
         && !localPlayer.hasEffect(MobEffects.LEVITATION)
         && !localPlayer.hasEffect(MobEffects.SLOW_FALLING)
         && localPlayer.containerMenu == localPlayer.inventoryMenu
         && localPlayer.inventoryMenu.getCarried().isEmpty()
         && !minecraft.gameMode.isServerControlledInventory()
         && this.timestamp >= this.timestamp2
         && !SoupInventoryBridge.handBusy()
         && !CompatActivateService.handBusy()
         && !CompatBeginTickService.blocking()
         && !minecraft.options.keyUse.isDown();
   }

   @Override
   public boolean grounded() {
      LocalPlayer localPlayer = Minecraft.getInstance().player;
      return localPlayer != null && (localPlayer.onGround() || localPlayer.isInWater() || localPlayer.onClimbable());
   }

   @Override
   public boolean danger() {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer localPlayer = minecraft.player;
      if (localPlayer != null
         && minecraft.level != null
         && !this.grounded()
         && !(localPlayer.getDeltaMovement().y > -0.15)
         && !(localPlayer.fallDistance < this.options2.fallDistance())
         && !(localPlayer.getHealth() + localPlayer.getAbsorptionAmount() < this.options2.minimumHealth())) {
         Vec3 vec3 = localPlayer.position();
         if (checkCondition3(vec3)
            && minecraft.level
                  .clip(new ClipContext(vec3, new Vec3(vec3.x, minecraft.level.getMinY() - 1, vec3.z), Block.COLLIDER, Fluid.ANY, localPlayer))
                  .getType()
               == Type.MISS) {
            Vec3 currentVec3 = vec3;
            Vec3 nextVec3 = localPlayer.getDeltaMovement();

            for (int index = 0; index < 8; index++) {
               Vec3 previousVec3 = currentVec3.add(nextVec3);
               if (!checkCondition3(previousVec3)) {
                  return false;
               }

               BlockHitResult blockHitResult = minecraft.level
                  .clip(
                     new ClipContext(
                        currentVec3.add(0.0, 0.02, 0.0), previousVec3, Block.COLLIDER, Fluid.ANY, localPlayer
                     )
                  );
               if (blockHitResult.getType() != Type.MISS && blockHitResult.getDirection() == Direction.UP) {
                  return false;
               }

               currentVec3 = previousVec3;
               nextVec3 = new Vec3(
                  nextVec3.x * 0.91,
                  (nextVec3.y - localPlayer.getGravity()) * 0.98,
                  nextVec3.z * 0.91
               );
            }

            return this.calculateValue() > 1;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int calculateValue() {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer localPlayer = minecraft.player;
      int value = 0;
      PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(localPlayer.getUUID());
      if (!minecraft.hasSingleplayerServer() && playerInfo != null) {
         value = (int)Math.ceil(Math.clamp(playerInfo.getLatency(), 0, 1000) / 50.0);
      }

      return PearlPlanService.deadline(
         localPlayer.getY(), localPlayer.getDeltaMovement().y, minecraft.level.getMinY() - 64, this.options2.reserveTicks() + value + 1
      );
   }

   @Override
   public boolean acquire() {
      if (this.context() && this.danger()) {
         Minecraft minecraft = Minecraft.getInstance();
         int value = this.calculateValue2();
         if (value < 0) {
            return false;
         }

         CompatStatusTracker.beforeInteraction();
         this.updateState();
         this.plan2 = this.findResult(true).orElse(null);
         if (this.plan2 == null) {
            return false;
         }

         CompatOptionsTracker.beforeInteraction();
         CompatReleaseTracker.suspendForScaffold();
         CompatConfigureService.beforeInteraction();
         if (CoreIsInitializedHandler.isReady()) {
            CoreIsInitializedHandler.get().modules().get(ImplSuspendForPearlService.class).filter(Module::isEnabled).ifPresent(ImplSuspendForPearlService::suspendForPearl);
         }

         CombatRotationOverride.cancel();
         if (this.context() && this.danger() && (value = this.calculateValue2()) >= 0) {
            this.localPlayer = minecraft.player;
            this.object = minecraft.level;
            this.object2 = minecraft.getConnection();
            this.count5 = InventoryInputActions.selected(minecraft);
            this.count6 = this.count5;
            this.count4 = -1;
            this.enabled5 = true;
            this.count3 = value;
            this.itemStack2 = this.localPlayer.inventoryMenu.getSlot(value).getItem().copy();
            compatOptionsTracker2 = this;
            CombatOwnsService.claim(CombatOwnsService.Owner.PEARL);

            try {
               this.enabled = true;
               if (value >= 9 && value < 36) {
                  int currentValue = this.count5;
                  int index = 0;

                  while (true) {
                     if (index < 9) {
                        if (!this.localPlayer.getInventory().getItem(index).isEmpty()) {
                           index++;
                           continue;
                        }

                        currentValue = index;
                     }

                     this.count3 = 36 + currentValue;
                     this.count4 = value;
                     this.itemStack = this.localPlayer.inventoryMenu.getSlot(this.count3).getItem().copy();
                     Slot slot = this.localPlayer.inventoryMenu.getSlot(this.count4);
                     Slot currentSlot = this.localPlayer.inventoryMenu.getSlot(this.count3);
                     if (!slot.mayPickup(this.localPlayer) || !currentSlot.mayPlace(this.itemStack2) || !slot.mayPlace(this.itemStack)) {
                        this.release();
                        return false;
                     }

                     this.enabled2 = true;
                     this.enabled3 = false;
                     InventoryInputActions.click(minecraft, this.count4, currentValue, SoupClickTracker.Click.SWAP);
                     this.enabled2 = false;
                     if (!this.enabled3) {
                        this.release();
                        return false;
                     }

                     this.enabled7 = true;
                     if (!ItemStack.matches(this.localPlayer.inventoryMenu.getSlot(this.count4).getItem(), this.itemStack)
                        || !ItemStack.matches(this.localPlayer.inventoryMenu.getSlot(this.count3).getItem(), this.itemStack2)) {
                        this.release();
                        return false;
                     }
                     break;
                  }
               }

               if (this.count3 != 45) {
                  this.count6 = this.count3 - 36;
                  this.enabled5 = this.count6 == this.count5;
                  InventoryInputActions.select(minecraft, this.count6);
                  this.enabled6 = true;
               }

               minecraft.gameMode.ensureHasSentCarriedItem();
               if (!this.valid()) {
                  this.release();
                  return false;
               } else {
                  return true;
               }
            } catch (RuntimeException | Error runtimeExceptionError) {
               this.release();
               throw runtimeExceptionError;
            } finally {
               this.enabled = this.enabled2 = false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int calculateValue2() {
      Minecraft minecraft = Minecraft.getInstance();
      int value = 36 + InventoryInputActions.selected(minecraft);
      if (this.checkCondition2(minecraft.player.inventoryMenu.getSlot(value).getItem())) {
         return value;
      }

      if (this.checkCondition2(minecraft.player.getOffhandItem())) {
         return 45;
      }

      for (int index = 36; index < 45; index++) {
         if (this.checkCondition2(minecraft.player.inventoryMenu.getSlot(index).getItem())) {
            return index;
         }
      }

      if (this.options2.inventory()) {
         for (int currentIndex = 9; currentIndex < 36; currentIndex++) {
            if (this.checkCondition2(minecraft.player.inventoryMenu.getSlot(currentIndex).getItem())) {
               return currentIndex;
            }
         }
      }

      return -1;
   }

   private boolean checkCondition2(ItemStack itemStack) {
      Minecraft minecraft = Minecraft.getInstance();
      return itemStack.is(Items.ENDER_PEARL)
         && itemStack.isItemEnabled(minecraft.level.enabledFeatures())
         && !minecraft.player.getCooldowns().isOnCooldown(itemStack);
   }

   @Override
   public boolean valid() {
      if (compatOptionsTracker2 == this
         && this.checkCondition()
         && this.enabled5
         && CombatOwnsService.owns(CombatOwnsService.Owner.PEARL)
         && InventoryInputActions.selected(Minecraft.getInstance()) == this.count6
         && this.count3 >= 36
         && this.count3 <= 45) {
         ItemStack currentItemStack = this.localPlayer.inventoryMenu.getSlot(this.count3).getItem();
         return this.checkCondition2(currentItemStack)
            && ItemStack.isSameItemSameComponents(currentItemStack, this.itemStack2)
            && (
               !this.enabled7
                  || ItemStack.matches(this.localPlayer.inventoryMenu.getSlot(this.count4).getItem(), this.itemStack)
            );
      } else {
         return false;
      }
   }

   @Override
   public boolean aim() {
      if (!this.valid()) {
         return false;
      }

      this.updateState();
      this.plan2 = this.findResult(true).orElse(null);
      if (this.plan2 == null) {
         return false;
      }

      Minecraft minecraft = Minecraft.getInstance();
      RotationData currentRotationData = RotationObserveService.current().orElse(new RotationData(this.localPlayer.getYRot(), this.localPlayer.getXRot()));
      this.rotationData = this.rotationVanillaGcdService
         .quantize(currentRotationData, this.plan2.rotation(), (Double)minecraft.options.sensitivity().get(), 0.0)
         .rotation();
      this.rotationData = new RotationData((float)this.rotationData.yaw(), (float)this.rotationData.pitch());
      this.timestamp3 = RotationObserveService.snapshot().map(RotationObserveService.Snapshot::ordinal).orElse(0L);
      RotationPublishService.publish(new RotationData(this.localPlayer.getYRot(), this.localPlayer.getXRot()), this.rotationData);
      if (this.combatCommitService.commit(this.timestamp, this.localPlayer.getYRot(), this.rotationData, this.createMap())) {
         this.localPlayer.setSprinting(false);
      }

      return true;
   }

   public void reconcile() {
      if (compatOptionsTracker2 == this && this.valid()) {
         if (LocalPhysicsFrameBus.wasAborted(this.timestamp)) {
            this.release();
         } else {
            PlacementOverrideBus.applied().ifPresent(item -> this.combatCommitService.reconcile(this.timestamp, item));
         }
      }
   }

   @Override
   public boolean throwPearl() {
      if (this.valid() && this.plan2 != null && this.rotationData != null) {
         Minecraft minecraft = Minecraft.getInstance();
         RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
         if (currentSnapshot != null
            && currentSnapshot.ordinal() > this.timestamp3
            && currentSnapshot.carriedPosition()
            && !(
               currentSnapshot.wireEyePosition().subtract(createRotationData7(this.localPlayer.getEyePosition())).length()
                  > 0.035
            )
            && !(RotationData.distance(currentSnapshot.rotation(), this.rotationData) > 1.0E-5)) {
            RotationVector rotationVector = RotationObserveService.inheritedVelocity().orElse(null);
            if (rotationVector == null) {
               return false;
            }

            this.plan2 = this.pearlPlanService
               .verify(currentSnapshot.wireEyePosition(), rotationVector, currentSnapshot.rotation(), this.calculateValue(), new PearlThrowController.Terrain())
               .orElse(null);
            if (this.plan2 != null && this.checkCondition2(this.localPlayer.inventoryMenu.getSlot(this.count3).getItem())) {
               this.enabled = true;
               this.enabled4 = false;

               try {
                  minecraft.gameMode.ensureHasSentCarriedItem();
                  InteractionHand interactionHand = this.count3 == 45 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                  InteractionResult interactionResult = ScaffoldForActorService.withRotation(
                     this.localPlayer, currentSnapshot.rotation(), () -> minecraft.gameMode.useItem(this.localPlayer, interactionHand)
                  );
                  if (this.enabled4) {
                     CompatBeginTickService.accepted();
                  }

                  if (this.enabled4 && interactionResult.consumesAction()) {
                     this.localPlayer.swing(interactionHand);
                  }

                  return this.enabled4;
               } finally {
                  this.enabled = false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void accepted(Object value) {
      if (this.enabled) {
         if (value instanceof ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket && serverboundSetCarriedItemPacket.getSlot() == this.count6) {
            this.enabled5 = true;
         }

         if (this.enabled2 && value instanceof ServerboundContainerClickPacket) {
            this.enabled3 = true;
         }

         if (!this.enabled2
            && value instanceof ServerboundUseItemPacket serverboundUseItemPacket
            && serverboundUseItemPacket.getHand() == (this.count3 == 45 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND)) {
            this.enabled4 = true;
         }
      }
   }

   @Override
   public void release() {
      if (compatOptionsTracker2 == this) {
         try {
            this.enabled = true;
            Minecraft minecraft = Minecraft.getInstance();
            if (this.checkCondition()
               && minecraft.gameMode != null
               && this.localPlayer.containerMenu == this.localPlayer.inventoryMenu
               && this.localPlayer.inventoryMenu.getCarried().isEmpty()) {
               ItemStack currentItemStack = this.count3 < 0
                  ? ItemStack.EMPTY
                  : this.localPlayer.inventoryMenu.getSlot(this.count3).getItem();
               int value = InventoryInputActions.selected(minecraft) == this.count6 ? 1 : 0;
               if (this.enabled7
                  && value != 0
                  && ItemStack.matches(this.localPlayer.inventoryMenu.getSlot(this.count4).getItem(), this.itemStack)
                  && (currentItemStack.isEmpty() || ItemStack.isSameItemSameComponents(currentItemStack, this.itemStack2))
                  && currentItemStack.getCount() >= this.itemStack2.getCount() - 1
                  && currentItemStack.getCount() <= this.itemStack2.getCount()) {
                  InventoryInputActions.click(minecraft, this.count4, this.count3 - 36, SoupClickTracker.Click.SWAP);
               }

               if (this.enabled6 && value != 0) {
                  InventoryInputActions.select(minecraft, this.count5);
                  minecraft.gameMode.ensureHasSentCarriedItem();
               }
            }
         } finally {
            if (CombatOwnsService.owns(CombatOwnsService.Owner.PEARL)) {
               CombatRotationOverride.start(
                  Minecraft.getInstance(),
                  CombatOwnsService.Owner.PEARL,
                  90.0,
                  0.2
               );
               CombatOwnsService.release(CombatOwnsService.Owner.PEARL);
            }

            compatOptionsTracker2 = null;
            this.enabled = this.enabled2 = this.enabled7 = this.enabled6 = false;
            this.count3 = this.count4 = this.count5 = this.count6 = -1;
            this.itemStack2 = this.itemStack = ItemStack.EMPTY;
            this.rotationData = null;
            this.plan2 = null;
         }
      }
   }

   public void watchdog() {
      if (compatOptionsTracker2 == this && (!this.context() || !this.valid())) {
         this.release();
      }
   }

   public static void manualInput() {
      if (compatOptionsTracker2 != null && !compatOptionsTracker2.enabled) {
         PearlThrowController pearlThrowController = compatOptionsTracker2;
         pearlThrowController.timestamp2 = pearlThrowController.timestamp + 6L;
         pearlThrowController.release();
      }
   }

   public static void consumed(KeyMapping keyMapping, boolean enabled) {
      if (enabled && compatOptionsTracker2 != null) {
         net.minecraft.client.Options currentOptions = Minecraft.getInstance().options;
         if (keyMapping != currentOptions.keyUse
            && keyMapping != currentOptions.keyAttack
            && keyMapping != currentOptions.keyDrop
            && keyMapping != currentOptions.keySwapOffhand
            && keyMapping != currentOptions.keyInventory
            && keyMapping != currentOptions.keyPickItem) {
            for (KeyMapping currentKeyMapping : currentOptions.keyHotbarSlots) {
               if (currentKeyMapping == keyMapping) {
                  manualInput();
                  return;
               }
            }
         } else {
            manualInput();
         }
      }
   }

   private Optional<PearlPlanService.Plan> findResult(boolean enabled) {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer localPlayer = minecraft.player;
      RotationVector rotationVector = createRotationData7(localPlayer.getEyePosition());
      RotationVector currentRotationVector = CompatContextReadyService.inheritedVelocity(minecraft);
      if (enabled) {
         ScaffoldRemapService.Input input = this.createMap();
         ScaffoldRemapService.WorldVector currentWorldVector = ScaffoldRemapService.worldVector(localPlayer.getYRot(), input.forwardAxis(), input.leftAxis());
         double doubleValue = (localPlayer.isSprinting() ? 0.026 : 0.02)
            * 0.98;
         Vec3 vec3 = localPlayer.getDeltaMovement();
         RotationVector currentX = new RotationVector(vec3.x + currentWorldVector.x() * doubleValue, vec3.y, vec3.z + currentWorldVector.z() * doubleValue);
         rotationVector = rotationVector.add(currentX);
         currentRotationVector = currentX;
      }

      return this.pearlPlanService
         .plan(rotationVector, currentRotationVector, new ArrayList<>(this.entries.values()), this.calculateValue(), new PearlThrowController.Terrain());
   }

   private ScaffoldRemapService.Input createMap() {
      net.minecraft.client.Options currentOptions = Minecraft.getInstance().options;
      return new ScaffoldRemapService.Input(
         currentOptions.keyUp.isDown(),
         currentOptions.keyDown.isDown(),
         currentOptions.keyLeft.isDown(),
         currentOptions.keyRight.isDown(),
         currentOptions.keyJump.isDown(),
         currentOptions.keyShift.isDown(),
         currentOptions.keySprint.isDown()
      );
   }

   private void updateState() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.rotationData72 == null) {
         this.rotationData72 = createRotationData7(minecraft.player.position());
         this.count = 0;
         this.entries.clear();
      }

      if (this.rotationData7 != null
         && this.rotationData7.subtract(this.rotationData72).length()
            <= this.options2.radius() + 4.0) {
         this.updateState2((int)Math.floor(this.rotationData7.x()), (int)Math.floor(this.rotationData7.z()));

         for (int index = -2; index <= 2; index++) {
            for (int currentIndex = -2; currentIndex <= 2; currentIndex++) {
               this.updateState2((int)Math.floor(this.rotationData7.x()) + index, (int)Math.floor(this.rotationData7.z()) + currentIndex);
            }
         }
      }

      for (byte byteValue = 8; byteValue <= this.options2.radius(); byteValue += 8) {
         for (int nextIndex = 0; nextIndex < 12; nextIndex++) {
            double doubleValue = nextIndex * 3.141592653589793 / 6.0;
            this.updateState2(
               (int)Math.floor(this.rotationData72.x() + Math.cos(doubleValue) * byteValue),
               (int)Math.floor(this.rotationData72.z() + Math.sin(doubleValue) * byteValue)
            );
         }
      }

      int value = (int)this.options2.radius();
      if (this.items.isEmpty() || value != this.count2) {
         ArrayList<int[]> arrayList = new ArrayList<>();

         for (int previousIndex = -value; previousIndex <= value; previousIndex++) {
            for (int sourceIndex = -value; sourceIndex <= value; sourceIndex++) {
               if (previousIndex * previousIndex + sourceIndex * sourceIndex <= value * value) {
                  arrayList.add(new int[]{previousIndex, sourceIndex});
               }
            }
         }

         arrayList.sort(Comparator.comparingInt(item -> item[0] * item[0] + item[1] * item[1]));
         this.items = arrayList;
         this.count2 = value;
         this.count = 0;
      }

      for (int targetIndex = 0; targetIndex < 128; targetIndex++) {
         int[] currentSize = this.items.get(this.count++ % this.items.size());
         this.updateState2((int)Math.floor(this.rotationData72.x()) + currentSize[0], (int)Math.floor(this.rotationData72.z()) + currentSize[1]);
      }

      this.entries
         .entrySet()
         .removeIf(
            entry -> entry.getValue().subtract(createRotationData7(minecraft.player.position())).length()
               > this.options2.radius() + 24.0
         );
      if (this.entries.size() > 128) {
         RotationVector rotationVector = createRotationData7(minecraft.player.position());
         Set currentLength = this.entries
            .entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(entry -> entry.getValue().subtract(rotationVector).length()))
            .limit(128L)
            .map(Entry::getKey)
            .collect(Collectors.toSet());
         this.entries.keySet().retainAll(currentLength);
      }
   }

   private void updateState2(int value, int currentValue) {
      Minecraft minecraft = Minecraft.getInstance();
      double currentY = Math.max(
         this.rotationData72.y() + 18.0,
         this.rotationData7 == null
            ? this.rotationData72.y()
            : Math.min(
               this.rotationData72.y() + 24.0,
               this.rotationData7.y() + 3.0
            )
      );
      double nextY = Math.max(minecraft.level.getMinY(), minecraft.player.getY() - 20.0);
      Vec3 vec3 = new Vec3(value + 0.5, currentY, currentValue + 0.5);
      Vec3 currentVec3 = new Vec3(value + 0.5, nextY, currentValue + 0.5);
      if (checkCondition3(vec3) && !(nextY >= currentY)) {
         for (int index = 0; index < 2; index++) {
            BlockHitResult blockHitResult = minecraft.level.clip(new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.ANY, minecraft.player));
            if (blockHitResult.getType() != Type.BLOCK) {
               break;
            }

            if (blockHitResult.getDirection() == Direction.UP && !checkCondition4(blockHitResult.getBlockPos())) {
               this.entries.put(blockHitResult.getBlockPos(), createRotationData7(blockHitResult.getLocation()));
            }

            vec3 = new Vec3(vec3.x, blockHitResult.getBlockPos().getY() - 0.01, vec3.z);
            if (vec3.y <= nextY) {
               break;
            }
         }
      }
   }

   private static boolean checkCondition3(Vec3 vec3) {
      return Minecraft.getInstance().level.hasChunkAt(BlockPos.containing(vec3));
   }

   private static RotationVector createRotationData7(Vec3 vec3) {
      return new RotationVector(vec3.x, vec3.y, vec3.z);
   }

   private static Vec3 createVec3(RotationVector rotationVector) {
      return new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z());
   }

   private static boolean checkCondition4(BlockPos blockPos) {
      BlockState blockState = Minecraft.getInstance().level.getBlockState(blockPos);
      return !blockState.getFluidState().isEmpty()
         || blockState.is(Blocks.FIRE)
         || blockState.is(Blocks.SOUL_FIRE)
         || blockState.is(Blocks.CACTUS)
         || blockState.is(Blocks.MAGMA_BLOCK)
         || blockState.is(Blocks.CAMPFIRE)
         || blockState.is(Blocks.SOUL_CAMPFIRE)
         || blockState.is(Blocks.SWEET_BERRY_BUSH)
         || blockState.is(Blocks.WITHER_ROSE)
         || blockState.is(Blocks.POWDER_SNOW)
         || blockState.is(Blocks.COBWEB)
         || blockState.is(Blocks.END_PORTAL)
         || blockState.is(Blocks.END_GATEWAY)
         || blockState.is(Blocks.NETHER_PORTAL);
   }

   public record Options(double fallDistance, double radius, double minimumHealth, int reserveTicks, boolean inventory) {
   }

   private final class Terrain implements PearlPlanService.World {
      @Override
      public PearlPlanService.Collision sweep(RotationVector rotationVector, RotationVector currentRotationVector) {
         Minecraft minecraft = Minecraft.getInstance();
         Vec3 vec3 = PearlThrowController.createVec3(rotationVector);
         Vec3 currentVec3 = PearlThrowController.createVec3(currentRotationVector);
         if (PearlThrowController.checkCondition3(vec3)
            && PearlThrowController.checkCondition3(currentVec3)
            && minecraft.level.getWorldBorder().isWithinBounds(BlockPos.containing(currentVec3))) {
            BlockHitResult blockHitResult = minecraft.level.clip(new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.ANY, minecraft.player));
            Vec3 nextVec3 = blockHitResult.getType() == Type.MISS ? currentVec3 : blockHitResult.getLocation();

            for (Entity entity : minecraft.level
               .getEntities(
                  minecraft.player,
                  new AABB(vec3, nextVec3).inflate(0.6),
                  item -> item.isAlive() && item.isPickable() && !item.isSpectator()
               )) {
               if (entity.getBoundingBox().inflate(0.3).clip(vec3, nextVec3).isPresent()) {
                  return PearlPlanService.Collision.BLOCKED;
               }
            }

            if (blockHitResult.getType() == Type.MISS) {
               return PearlPlanService.Collision.CLEAR;
            } else {
               return blockHitResult.getDirection() == Direction.UP && !PearlThrowController.checkCondition4(blockHitResult.getBlockPos())
                  ? PearlPlanService.Collision.TOP
                  : PearlPlanService.Collision.BLOCKED;
            }
         } else {
            return PearlPlanService.Collision.BLOCKED;
         }
      }

      @Override
      public boolean safeLanding(RotationVector rotationVector, double doubleValue) {
         Minecraft minecraft = Minecraft.getInstance();
         LocalPlayer localPlayer = minecraft.player;
         double currentDoubleValue = 0.08
            + Math.min(
               0.6,
               localPlayer.getKnownMovement().horizontalDistance() * 3.0
            );
         double nextDoubleValue = localPlayer.getBbWidth() * 0.5 + currentDoubleValue;
         Vec3 vec3 = PearlThrowController.createVec3(rotationVector);
         BlockHitResult blockHitResult = minecraft.level
            .clip(
               new ClipContext(
                  vec3.add(0.0, 0.01, 0.0),
                  vec3.add(0.0, -3.0, 0.0),
                  Block.COLLIDER,
                  Fluid.ANY,
                  localPlayer
               )
            );
         if (blockHitResult.getType() == Type.BLOCK && blockHitResult.getDirection() == Direction.UP && !PearlThrowController.checkCondition4(blockHitResult.getBlockPos())) {
            double previousDoubleValue = blockHitResult.getLocation().y;
            if (rotationVector.y() < previousDoubleValue - 1.0E-5) {
               return false;
            }

            AABB currentX = new AABB(
               rotationVector.x() - nextDoubleValue,
               previousDoubleValue + 0.001,
               rotationVector.z() - nextDoubleValue,
               rotationVector.x() + nextDoubleValue,
               Math.max(previousDoubleValue, rotationVector.y()) + localPlayer.getBbHeight(),
               rotationVector.z() + nextDoubleValue
            );
            if (minecraft.level.getWorldBorder().isWithinBounds(currentX) && minecraft.level.noCollision(localPlayer, currentX)) {
               for (BlockPos blockPos : BlockPos.betweenClosed(
                  BlockPos.containing(currentX.minX, previousDoubleValue - 0.1, currentX.minZ),
                  BlockPos.containing(currentX.maxX, currentX.maxY, currentX.maxZ)
               )) {
                  if (!minecraft.level.hasChunkAt(blockPos) || PearlThrowController.checkCondition4(blockPos)) {
                     return false;
                  }
               }

               for (double sourceDoubleValue : new double[]{-nextDoubleValue, 0.0, nextDoubleValue}) {
                  for (double targetDoubleValue : new double[]{-nextDoubleValue, 0.0, nextDoubleValue}) {
                     Vec3 nextX = new Vec3(rotationVector.x() + sourceDoubleValue, previousDoubleValue + 0.05, rotationVector.z() + targetDoubleValue);
                     BlockHitResult currentBlockHitResult = minecraft.level
                        .clip(
                           new ClipContext(
                              nextX, nextX.add(0.0, -0.15, 0.0), Block.COLLIDER, Fluid.ANY, localPlayer
                           )
                        );
                     if (currentBlockHitResult.getType() != Type.BLOCK
                        || currentBlockHitResult.getDirection() != Direction.UP
                        || PearlThrowController.checkCondition4(currentBlockHitResult.getBlockPos())) {
                        return false;
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}

