package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.bucket.BucketStageService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.soup.SoupConfirmedData;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.module.impl.ImplRequestsSprintClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class CompatReleaseTracker implements BucketStageService.Environment {
   private static CompatReleaseTracker values2;
   private final CompatReleaseTracker.Purpose values3;
   private CompatReleaseTracker.Options values4 = new CompatReleaseTracker.Options(
      false, true, true, false, 4.0, " "
   );
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private final CombatCommitService combatCommitService = new CombatCommitService();
   private long timestamp;
   private CompatReleaseTracker.Plan values5;
   private LocalPlayer localPlayer;
   private Object object;
   private Object object2;
   private RotationData rotationData;
   private RotationData rotationData2;
   private int count = -1;
   private int count2 = -1;
   private int count3 = -1;
   private long timestamp2;
   private long timestamp3;
   private ItemStack itemStack = ItemStack.EMPTY;
   private boolean enabled;
   private boolean enabled2;

   public CompatReleaseTracker(CompatReleaseTracker.Purpose purpose) {
      this.values3 = purpose;
   }

   public void options(CompatReleaseTracker.Options currentOptions) {
      this.values4 = currentOptions;
   }

   public static boolean handBusy() {
      return values2 != null && values2.checkCondition() && !values2.enabled;
   }

   public static boolean reserved() {
      return values2 != null && values2.checkCondition();
   }

   public static void suspendForScaffold() {
      if (values2 != null) {
         values2.release();
      }
   }

   private static RotationData createRotationData(LocalPlayer localPlayer) {
      return new RotationData(localPlayer.getYRot(), localPlayer.getXRot());
   }

   private static RotationVector createRotationData7(Vec3 vec3) {
      return new RotationVector(vec3.x, vec3.y, vec3.z);
   }

   private boolean checkCondition() {
      Minecraft minecraft = Minecraft.getInstance();
      return this.localPlayer != null
         && minecraft.player == this.localPlayer
         && minecraft.level == this.object
         && minecraft.getConnection() == this.object2;
   }

   private boolean checkCondition2() {
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
         && !localPlayer.isPassenger()
         && !localPlayer.isSleeping()
         && !localPlayer.isUsingItem()
         && localPlayer.containerMenu == localPlayer.inventoryMenu
         && localPlayer.inventoryMenu.getCarried().isEmpty()
         && !minecraft.gameMode.isDestroying()
         && !minecraft.options.keyUse.isDown()
         && (this.values3 == CompatReleaseTracker.Purpose.LAVA || !minecraft.options.keyAttack.isDown())
         && !SoupInventoryBridge.handBusy()
         && !CompatActivateService.handBusy()
         && !PearlThrowController.reserved()
         && this.checkCondition3()
         && (
            this.values3 != CompatReleaseTracker.Purpose.LAVA
               || !localPlayer.isFallFlying() && !localPlayer.isAutoSpinAttack() && !localPlayer.getAbilities().flying && !localPlayer.isInWater() && !localPlayer.isInLava()
         )
         && (!CoreIsInitializedHandler.isReady() || !CoreIsInitializedHandler.get().modules().get("ScaffoldWalk").map(item -> item.isEnabled()).orElse(false));
   }

   private boolean checkCondition3() {
      return CombatOwnsService.owns(CombatOwnsService.Owner.BUCKET)
         ? values2 == this || this.values3 == CompatReleaseTracker.Purpose.EXTINGUISH
         : CombatOwnsService.unclaimed()
            || CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)
            || CombatOwnsService.owns(CombatOwnsService.Owner.RETURN)
            || this.values3 == CompatReleaseTracker.Purpose.EXTINGUISH && CombatOwnsService.owns(CombatOwnsService.Owner.ROD);
   }

   @Override
   public boolean plan() {
      Minecraft minecraft = Minecraft.getInstance();
      if (values2 == null
         || values2 == this
         || this.values3 == CompatReleaseTracker.Purpose.EXTINGUISH
            && minecraft.player != null
            && (minecraft.player.isInLava() || minecraft.player.isOnFire())
            && values2.values3 == CompatReleaseTracker.Purpose.LAVA) {
         if (!this.checkCondition2()) {
            return false;
         }

         this.localPlayer = minecraft.player;
         this.object = minecraft.level;
         this.object2 = minecraft.getConnection();
         if (this.values3 == CompatReleaseTracker.Purpose.EXTINGUISH) {
            if (!this.localPlayer.isInLava() && (this.values4.lavaOnly() || !this.localPlayer.isOnFire())
               || this.localPlayer.isInWater()) {
               return false;
            }

            for (CompatReleaseTracker.Kind kind : this.values4.powderSnow()
               ? List.of(CompatReleaseTracker.Kind.WATER, CompatReleaseTracker.Kind.SNOW)
               : List.of(CompatReleaseTracker.Kind.WATER)) {
               int value = this.calculateValue(kind);
               if (value >= 0) {
                  Vec3 vec3 = this.localPlayer.position();

                  for (double[] doubles : new double[][]{
                     {0.0, 0.0},
                     {0.22, 0.0},
                     {-0.22, 0.0},
                     {0.0, 0.22},
                     {0.0, -0.22}
                  }) {
                     Vec3 currentVec3 = new Vec3(vec3.x + doubles[0], vec3.y - 1.0, vec3.z + doubles[1]);
                     CompatReleaseTracker.Plan currentPlan = this.collectValues2(kind, value, this.createRotationData2(currentVec3), null);
                     if (currentPlan != null && new AABB(currentPlan.destination()).intersects(this.localPlayer.getBoundingBox())) {
                        this.values5 = currentPlan;
                        return true;
                     }
                  }
               }
            }
         } else {
            if (this.localPlayer.isInLava() || this.localPlayer.isInWater() || this.localPlayer.isOnFire()) {
               return false;
            }

            int currentValue = this.calculateValue(CompatReleaseTracker.Kind.LAVA);
            if (currentValue < 0) {
               return false;
            }

            ArrayList<LivingEntity> arrayList = new ArrayList<>();

            for (Entity entity : minecraft.level.entitiesForRendering()) {
               if (entity instanceof LivingEntity livingEntity && this.checkCondition5(livingEntity)) {
                  arrayList.add(livingEntity);
               }
            }

            arrayList.sort(Comparator.comparingDouble(this.localPlayer::distanceToSqr));

            for (LivingEntity currentLivingEntity : (Iterable<LivingEntity>) (Iterable<?>) (arrayList.stream().limit(8L).toList())) {
               CompatReleaseTracker.Plan nextPlan = this.collectValues(currentValue, currentLivingEntity);
               if (nextPlan != null) {
                  this.values5 = nextPlan;
                  return true;
               }
            }
         }

         this.values5 = null;
         return false;
      } else {
         return false;
      }
   }

   private CompatReleaseTracker.Plan collectValues(int value, LivingEntity livingEntity) {
      ClientLevel clientLevel = Minecraft.getInstance().level;
      Vec3 vec3 = livingEntity.position();
      double currentX = Math.clamp(
         livingEntity.getX() - livingEntity.xo, -0.35, 0.35
      );
      double doubleValue = Math.clamp(
         livingEntity.getZ() - livingEntity.zo, -0.35, 0.35
      );

      for (double[] doubles : new double[][]{
         {0.0, 0.0},
         {currentX, doubleValue},
         {0.22, 0.0},
         {-0.22, 0.0},
         {0.0, 0.22},
         {0.0, -0.22}
      }) {
         Vec3 currentVec3 = new Vec3(vec3.x + doubles[0], vec3.y + 0.05, vec3.z + doubles[1]);
         BlockHitResult blockHitResult = clientLevel.clip(
            new ClipContext(
               currentVec3, currentVec3.add(0.0, -2.6, 0.0), Block.OUTLINE, Fluid.NONE, this.localPlayer
            )
         );
         if (blockHitResult.getType() == Type.BLOCK && blockHitResult.getDirection() == Direction.UP) {
            CompatReleaseTracker.Plan plan = this.collectValues2(
               CompatReleaseTracker.Kind.LAVA, value, this.createRotationData2(blockHitResult.getLocation()), livingEntity.getUUID()
            );
            if (plan != null && this.checkCondition6(plan, livingEntity)) {
               return plan;
            }
         }
      }

      return null;
   }

   private int calculateValue(CompatReleaseTracker.Kind kind) {
      int value = InventoryInputActions.selected(Minecraft.getInstance());
      if (checkCondition4(this.localPlayer.getInventory().getItem(value), kind)) {
         return value;
      }

      if (checkCondition4(this.localPlayer.getOffhandItem(), kind)) {
         return 40;
      }

      for (int index = 0; index < 9; index++) {
         if (checkCondition4(this.localPlayer.getInventory().getItem(index), kind)) {
            return index;
         }
      }

      return -1;
   }

   private static boolean checkCondition4(ItemStack itemStack, CompatReleaseTracker.Kind kind) {
      return itemStack.is(switch (kind) {
         case WATER -> Items.WATER_BUCKET;
         case SNOW -> Items.POWDER_SNOW_BUCKET;
         case LAVA -> Items.LAVA_BUCKET;
      });
   }

   private ItemStack createItemStack() {
      return this.values5.slot() == 40
         ? this.localPlayer.getOffhandItem()
         : this.localPlayer.getInventory().getItem(this.values5.slot());
   }

   private InteractionHand createInteractionHand() {
      return this.values5.slot() == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
   }

   private RotationData createRotationData2(Vec3 vec3) {
      return RotationData.lookAt(createRotationData7(this.localPlayer.getEyePosition()), createRotationData7(vec3));
   }

   private RotationData createRotationData3(RotationData rotationData) {
      RotationData currentRotationData = this.values3 == CompatReleaseTracker.Purpose.LAVA
         ? RotationObserveService.current().orElseGet(() -> createRotationData(this.localPlayer))
         : createRotationData(this.localPlayer);
      return this.rotationVanillaGcdService.quantize(currentRotationData, rotationData, (Double)Minecraft.getInstance().options.sensitivity().get(), 0.0).rotation();
   }

   private BlockHitResult createBlockHitResult(RotationData rotationData, boolean enabled) {
      Vec3 vec3 = this.localPlayer.getEyePosition();
      Vec3 currentVec3 = Vec3.directionFromRotation((float)rotationData.pitch(), (float)rotationData.yaw());
      double doubleValue = Math.min(this.values4.range(), this.localPlayer.blockInteractionRange());
      return Minecraft.getInstance()
         .level
         .clip(new ClipContext(vec3, vec3.add(currentVec3.scale(doubleValue)), Block.OUTLINE, enabled ? Fluid.SOURCE_ONLY : Fluid.NONE, this.localPlayer));
   }

   private CompatReleaseTracker.Plan collectValues2(CompatReleaseTracker.Kind kind, int value, RotationData rotationData, UUID uUID) {
      Minecraft minecraft = Minecraft.getInstance();
      BlockHitResult blockHitResult = this.createBlockHitResult(this.createRotationData3(rotationData), false);
      if (blockHitResult.getType() == Type.BLOCK && !blockHitResult.isInside() && blockHitResult.getDirection() == Direction.UP) {
         BlockPos blockPos = blockHitResult.getBlockPos();
         BlockState blockState = minecraft.level.getBlockState(blockPos);
         if (!(blockState.getBlock() instanceof LiquidBlockContainer) && blockState.getMenuProvider(minecraft.level, blockPos) == null) {
            BlockPos currentBlockPos = blockPos.above();
            if (kind == CompatReleaseTracker.Kind.SNOW) {
               BlockPlaceContext blockPlaceContext = new BlockPlaceContext(
                  this.localPlayer,
                  value == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND,
                  value == 40 ? this.localPlayer.getOffhandItem() : this.localPlayer.getInventory().getItem(value),
                  blockHitResult
               );
               if (!blockPlaceContext.canPlace() || !blockPlaceContext.getClickedPos().equals(currentBlockPos)) {
                  return null;
               }
            } else if (!minecraft.level.getBlockState(currentBlockPos).canBeReplaced(kind == CompatReleaseTracker.Kind.WATER ? Fluids.WATER : Fluids.LAVA)) {
               return null;
            }

            if (!minecraft.level.isInWorldBounds(currentBlockPos)
               || !minecraft.level.getWorldBorder().isWithinBounds(currentBlockPos)
               || !minecraft.level.mayInteract(this.localPlayer, blockPos)
               || !this.localPlayer
                  .mayUseItemAt(
                     currentBlockPos, Direction.UP, value == 40 ? this.localPlayer.getOffhandItem() : this.localPlayer.getInventory().getItem(value)
                  )) {
               return null;
            } else if (kind == CompatReleaseTracker.Kind.WATER && DimensionEnvironmentAccess.waterEvaporates(minecraft, currentBlockPos)) {
               return null;
            } else {
               return kind == CompatReleaseTracker.Kind.LAVA && !minecraft.level.getFluidState(currentBlockPos).isEmpty()
                  ? null
                  : new CompatReleaseTracker.Plan(kind, value, currentBlockPos.immutable(), blockPos.immutable(), Direction.UP, blockHitResult.getLocation(), uUID);
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private boolean checkCondition5(LivingEntity livingEntity) {
      if (FriendSyncController.excluded(FriendsMode.AUTO_LAVA, livingEntity)) {
         return false;
      } else if (CompatDecisionTracker.excluded(AntibotFeatureType.AUTO_LAVA, livingEntity)) {
         return false;
      } else if (livingEntity != this.localPlayer
         && livingEntity.isAlive()
         && !livingEntity.isSpectator()
         && livingEntity.isPickable()
         && livingEntity.isAttackable()
         && !livingEntity.isInvisibleTo(this.localPlayer)
         && !livingEntity.isAlliedTo(this.localPlayer)
         && !this.localPlayer.isAlliedTo(livingEntity)
         && !(livingEntity instanceof Player player && (!this.localPlayer.canHarmPlayer(player) || player.getAbilities().instabuild))
         && (this.values4.players() && livingEntity instanceof Player || this.values4.mobs() && livingEntity instanceof Mob)
         && !livingEntity.fireImmune()
         && !livingEntity.isInWater()
         && !livingEntity.isInLava()
         && this.localPlayer.hasLineOfSight(livingEntity)
         && !(livingEntity.distanceToSqr(this.localPlayer) > this.values4.range() * this.values4.range())) {
         String text = livingEntity.getName().getString().toLowerCase(Locale.ROOT);
         String currentText = livingEntity.getUUID().toString();
         return Arrays.stream(this.values4.excluded().toLowerCase(Locale.ROOT).split("[,;\\s]+"))
            .noneMatch(item -> !item.isEmpty() && (item.equals(text) || item.equals(currentText)));
      } else {
         return false;
      }
   }

   private LivingEntity createLivingEntity() {
      for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
         if (entity instanceof LivingEntity livingEntity && livingEntity.getUUID().equals(this.values5.target())) {
            return livingEntity;
         }
      }

      return null;
   }

   private boolean checkCondition6(CompatReleaseTracker.Plan plan, LivingEntity livingEntity) {
      AABB aABB = new AABB(plan.destination());
      AABB currentAABB = livingEntity.getBoundingBox();
      if (aABB.inflate(0.0, 1.25, 0.0).intersects(currentAABB)
         && !aABB.inflate(
               0.35,
               2.0,
               0.35
            )
            .intersects(
               this.localPlayer
                  .getBoundingBox()
                  .expandTowards(this.localPlayer.getDeltaMovement().scale(2.0))
            )) {
         ClientLevel clientLevel = Minecraft.getInstance().level;

         for (AbstractClientPlayer abstractClientPlayer : clientLevel.players()) {
            if (FriendSyncController.excluded(FriendsMode.AUTO_LAVA, abstractClientPlayer)
               && aABB.inflate(1.0, 2.0, 1.0)
                  .intersects(
                     abstractClientPlayer.getBoundingBox().expandTowards(abstractClientPlayer.getDeltaMovement().scale(2.0))
                  )) {
               return false;
            }
         }

         for (Direction direction : List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            BlockPos blockPos = plan.destination().relative(direction);
            if (clientLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean begin(boolean enabled) {
      if (this.values5 != null && this.checkCondition() && this.checkCondition2()) {
         if (values2 != null) {
            if (this.values3 != CompatReleaseTracker.Purpose.EXTINGUISH || values2.values3 != CompatReleaseTracker.Purpose.LAVA || enabled) {
               return false;
            }

            values2.release();
         }

         if (enabled ? this.createItemStack().is(Items.BUCKET) : checkCondition4(this.createItemStack(), this.values5.kind())) {
            CompatStatusTracker.beforeInteraction();
            CompatOptionsTracker.beforeInteraction();
            CompatConfigureService.beforeInteraction();
            CombatRotationOverride.cancel();
            values2 = this;
            this.enabled2 = false;
            this.count = this.count2 = InventoryInputActions.selected(Minecraft.getInstance());
            this.rotationData = this.rotationData2 = createRotationData(this.localPlayer);
            this.count3 = -1;
            Optional<RotationData> result = CombatOwnsService.claim(CombatOwnsService.Owner.BUCKET);
            if (this.values3 == CompatReleaseTracker.Purpose.LAVA) {
               this.rotationData2 = result.orElseGet(() -> RotationObserveService.current().orElse(this.rotationData));
            } else {
               CombatOwnsService.clearFrame(CombatOwnsService.Owner.BUCKET);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean valid(boolean currentValid) {
      if (this.values5 == null || !this.checkCondition() || !this.checkCondition2()) {
         return false;
      } else {
         return !currentValid
            ? true
            : values2 == this
               && CombatOwnsService.owns(CombatOwnsService.Owner.BUCKET)
               && InventoryInputActions.selected(Minecraft.getInstance()) == this.count2
               && (
                  this.values3 == CompatReleaseTracker.Purpose.LAVA
                     || RotationData.distance(createRotationData(this.localPlayer), this.rotationData2)
                        < 0.01
               );
      }
   }

   @Override
   public void aim(boolean enabled) {
      if (this.localPlayer.tickCount != this.count3) {
         this.count3 = this.localPlayer.tickCount;
         if (this.values3 == CompatReleaseTracker.Purpose.LAVA && !enabled) {
            LivingEntity livingEntity = this.createLivingEntity();
            CompatReleaseTracker.Plan plan = livingEntity != null && this.checkCondition5(livingEntity) ? this.collectValues(this.values5.slot(), livingEntity) : null;
            if (plan == null) {
               return;
            }

            this.values5 = plan;
         }

         RotationData rotationData = this.createRotationData2(enabled ? Vec3.atCenterOf(this.values5.destination()) : this.values5.hit());
         RotationData currentRotationData = this.values3 == CompatReleaseTracker.Purpose.LAVA
            ? RotationObserveService.current().orElse(this.rotationData2)
            : createRotationData(this.localPlayer);
         double doubleValue = RotationData.yawDelta(currentRotationData.yaw(), rotationData.yaw());
         rotationData = new RotationData(
            currentRotationData.yaw() + Math.clamp(doubleValue, -90.0, 90.0),
            currentRotationData.pitch()
               + Math.clamp(
                  rotationData.pitch() - currentRotationData.pitch(),
                  -90.0,
                  90.0
               )
         );
         this.rotationData2 = this.rotationVanillaGcdService
            .quantize(currentRotationData, rotationData, (Double)Minecraft.getInstance().options.sensitivity().get(), 0.0)
            .rotation();
         if (this.values3 != CompatReleaseTracker.Purpose.LAVA) {
            this.localPlayer.setYRot((float)this.rotationData2.yaw());
            this.localPlayer.setXRot((float)this.rotationData2.pitch());
         }
      }
   }

   public void beforeMovement() {
      if (values2 == this && this.values3 == CompatReleaseTracker.Purpose.LAVA && this.valid(true)) {
         Minecraft currentMinecraft = Minecraft.getInstance();
         net.minecraft.client.Options currentOptions = currentMinecraft.options;
         RotationPublishService.publish(createRotationData(this.localPlayer), this.rotationData2);
         ScaffoldRemapService.Input input = new ScaffoldRemapService.Input(
            currentOptions.keyUp.isDown(),
            currentOptions.keyDown.isDown(),
            currentOptions.keyLeft.isDown(),
            currentOptions.keyRight.isDown(),
            currentOptions.keyJump.isDown(),
            currentOptions.keyShift.isDown(),
            currentOptions.keySprint.isDown() || ImplRequestsSprintClient.requestsSprint(currentMinecraft)
         );
         if (this.combatCommitService.commit(++this.timestamp, this.localPlayer.getYRot(), this.rotationData2, input)) {
            this.localPlayer.setSprinting(false);
         }
      }
   }

   public void reconcileMovement() {
      if (values2 == this && this.values3 == CompatReleaseTracker.Purpose.LAVA && CombatOwnsService.owns(CombatOwnsService.Owner.BUCKET)) {
         if (LocalPhysicsFrameBus.wasAborted(this.timestamp)) {
            this.release();
         } else {
            PlacementOverrideBus.applied().ifPresent(item -> this.combatCommitService.reconcile(this.timestamp, item));
         }
      }
   }

   @Override
   public boolean ready(boolean enabled) {
      if (!this.valid(true)) {
         return false;
      }

      RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
      if (currentSnapshot != null
         && !(RotationData.distance(currentSnapshot.rotation(), this.rotationData2) > 0.01)
         && !(
            currentSnapshot.wireEyePosition().subtract(createRotationData7(this.localPlayer.getEyePosition())).length()
               > 0.1
         )) {
         BlockHitResult blockHitResult = this.createBlockHitResult(this.rotationData2, enabled);
         if (enabled) {
            return blockHitResult.getType() == Type.BLOCK && blockHitResult.getBlockPos().equals(this.values5.destination()) && this.canRecover();
         }

         if (!checkCondition4(this.createItemStack(), this.values5.kind())) {
            return false;
         }

         CompatReleaseTracker.Plan plan = this.collectValues2(
            this.values5.kind(), this.values5.slot(), this.rotationData2, this.values5.target()
         );
         if (plan != null
            && plan.destination().equals(this.values5.destination())
            && plan.support().equals(this.values5.support())) {
            if (this.values3 == CompatReleaseTracker.Purpose.LAVA) {
               LivingEntity livingEntity = this.createLivingEntity();
               if (livingEntity == null || !this.checkCondition5(livingEntity) || !this.checkCondition6(plan, livingEntity)) {
                  return false;
               }
            } else if (!this.localPlayer.isInLava() && !this.localPlayer.isOnFire()
               || !new AABB(this.values5.destination()).intersects(this.localPlayer.getBoundingBox())) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean use(boolean currentEnabled) {
      if (this.ready(currentEnabled) && !this.localPlayer.getCooldowns().isOnCooldown(this.createItemStack())) {
         Minecraft minecraft = Minecraft.getInstance();
         this.timestamp3 = this.timestamp2;
         this.itemStack = ItemStack.EMPTY;
         this.enabled = true;

         try {
            if (this.values5.slot() != 40) {
               InventoryInputActions.select(minecraft, this.values5.slot());
               this.count2 = this.values5.slot();
            }

            this.enabled2 = true;
            minecraft.gameMode.ensureHasSentCarriedItem();
            InteractionResult interactionResult = ScaffoldForActorService.withRotation(
               this.localPlayer,
               this.rotationData2,
               () -> !currentEnabled && this.values5.kind() == CompatReleaseTracker.Kind.SNOW
                  ? minecraft.gameMode.useItemOn(this.localPlayer, this.createInteractionHand(), this.createBlockHitResult(this.rotationData2, false))
                  : minecraft.gameMode.useItem(this.localPlayer, this.createInteractionHand())
            );
            if (interactionResult.consumesAction()) {
               this.localPlayer.swing(this.createInteractionHand());
            }

            return interactionResult.consumesAction();
         } finally {
            this.enabled = false;
         }
      } else {
         return false;
      }
   }

   @Override
   public BucketStageService.Reply reply(boolean enabled) {
      if (this.timestamp2 <= this.timestamp3) {
         return BucketStageService.Reply.WAITING;
      } else {
         return (enabled ? !checkCondition4(this.itemStack, this.values5.kind()) : !this.itemStack.is(Items.BUCKET))
            ? BucketStageService.Reply.REJECTED
            : BucketStageService.Reply.ACCEPTED;
      }
   }

   @Override
   public boolean canRecover() {
      if (this.checkCondition() && this.values5 != null && this.createItemStack().is(Items.BUCKET)) {
         Minecraft minecraft = Minecraft.getInstance();
         FluidState fluidState = minecraft.level.getFluidState(this.values5.destination());
         if (this.values5.kind() == CompatReleaseTracker.Kind.SNOW) {
            return minecraft.level.getBlockState(this.values5.destination()).is(Blocks.POWDER_SNOW)
               && !this.localPlayer.isOnFire()
               && !this.localPlayer.isInLava();
         }

         if (fluidState.isSource() && fluidState.is(this.values5.kind() == CompatReleaseTracker.Kind.WATER ? FluidTags.WATER : FluidTags.LAVA)) {
            if (this.values5.kind() == CompatReleaseTracker.Kind.WATER) {
               if (this.localPlayer.isOnFire() || this.localPlayer.isInLava()) {
                  return false;
               }

               for (BlockPos blockPos : BlockPos.betweenClosed(
                  this.localPlayer.blockPosition().offset(-1, -1, -1), this.localPlayer.blockPosition().offset(1, 1, 1)
               )) {
                  if (minecraft.level.getFluidState(blockPos).is(FluidTags.LAVA)) {
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

   @Override
   public void release() {
      if (values2 == this) {
         Minecraft minecraft = Minecraft.getInstance();

         try {
            if (this.checkCondition()) {
               if (this.enabled2 && InventoryInputActions.selected(minecraft) == this.count2 && this.count >= 0) {
                  InventoryInputActions.select(minecraft, this.count);
                  if (minecraft.gameMode != null) {
                     minecraft.gameMode.ensureHasSentCarriedItem();
                  }
               }

               if (this.values3 != CompatReleaseTracker.Purpose.LAVA
                  && CombatOwnsService.owns(CombatOwnsService.Owner.BUCKET)
                  && this.rotationData2 != null
                  && RotationData.distance(createRotationData(this.localPlayer), this.rotationData2)
                     < 0.01) {
                  this.localPlayer.setYRot((float)this.rotationData.yaw());
                  this.localPlayer.setXRot((float)this.rotationData.pitch());
               }
            }
         } finally {
            CombatOwnsService.release(CombatOwnsService.Owner.BUCKET);
            values2 = null;
            this.count = this.count2 = -1;
            this.rotationData = this.rotationData2 = null;
            this.enabled2 = false;
         }
      }
   }

   @Override
   public void forget() {
      this.values5 = null;
      this.localPlayer = null;
      this.object = this.object2 = null;
      this.timestamp2 = this.timestamp3 = 0L;
      this.itemStack = ItemStack.EMPTY;
   }

   public static void serverSlot(int value, int currentValue, ItemStack currentItemStack) {
      CompatReleaseTracker compatReleaseTracker = values2;
      if (compatReleaseTracker != null && compatReleaseTracker.values5 != null && compatReleaseTracker.checkCondition()) {
         int nextValue = compatReleaseTracker.values5.slot() == 40 ? 45 : 36 + compatReleaseTracker.values5.slot();
         if (value == -2) {
            currentValue = SoupConfirmedData.playerSlotToMenu(currentValue);
         } else if (value != 0) {
            return;
         }

         if (currentValue == nextValue) {
            compatReleaseTracker.itemStack = currentItemStack.copy();
            compatReleaseTracker.timestamp2++;
         }
      }
   }

   public static void serverContent(int value, List<ItemStack> items) {
      if (value == 0) {
         for (int index = 0; index < items.size(); index++) {
            serverSlot(0, index, (ItemStack)items.get(index));
         }
      }
   }

   public static void serverInventorySlot(int value, ItemStack itemStack) {
      serverSlot(0, SoupConfirmedData.playerSlotToMenu(value), itemStack);
   }

   private enum Kind {
      WATER,
      SNOW,
      LAVA;


      private static CompatReleaseTracker.Kind[] $values() {
         return new CompatReleaseTracker.Kind[]{WATER, SNOW, LAVA};
      }
   }

   public record Options(boolean lavaOnly, boolean powderSnow, boolean players, boolean mobs, double range, String excluded) {
   }

   private record Plan(CompatReleaseTracker.Kind kind, int slot, BlockPos destination, BlockPos support, Direction face, Vec3 hit, UUID target) {
   }

   public enum Purpose {
      EXTINGUISH,
      LAVA;


      private static CompatReleaseTracker.Purpose[] $values() {
         return new CompatReleaseTracker.Purpose[]{EXTINGUISH, LAVA};
      }
   }
}

