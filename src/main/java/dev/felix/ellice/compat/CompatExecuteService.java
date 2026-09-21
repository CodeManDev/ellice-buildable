package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.feature.task.FoodGatheringTask;
import dev.felix.ellice.feature.task.TaskAction;
import dev.felix.ellice.feature.task.TaskBlockPosition;
import dev.felix.ellice.feature.task.TaskData;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelSteerService;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.impl.ImplSuspendForPearlService;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public final class CompatExecuteService {
  private static final float value = 0.35F;
  private static final float value2 = 100.0F;
  private static final float value3 = 0.25F;
  private static final float value4 = 180.0F;
  private static CompatExecuteService compatExecuteService;
  private final TravelSteerService travelSteerService = new TravelSteerService();
  private long timestamp;
  private TaskBlockPosition taskData2;
  private int count = -1;
  private Class<?> class2;
  private String text = "";
  private boolean enabled;

  public String execute(
      TaskAction taskAction, TaskData taskData, TravelLoadedHandler travelLoaded) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null || minecraft.level == null || minecraft.gameMode == null) {
      return "Waiting for gameplay";
    }

    if (minecraft.screen != null) {
      this.release();
      return "Waiting for screen";
    }

    if (!CompatReleaseTracker.reserved()
        && !PearlThrowController.reserved()
        && !SoupInventoryBridge.handBusy()
        && !CompatActivateService.handBusy()
        && !CompatOptionsTracker.handBusy()) {
      CombatOwnsService.Owner nextOwner = CombatOwnsService.currentOwner().orElse(null);
      if (nextOwner == CombatOwnsService.Owner.KILL_AURA
          || nextOwner == CombatOwnsService.Owner.BOW
          || nextOwner == CombatOwnsService.Owner.RETURN
          || nextOwner == CombatOwnsService.Owner.BUCKET
          || nextOwner == CombatOwnsService.Owner.ROD
          || nextOwner == CombatOwnsService.Owner.PEARL) {
        this.release();
        return "Paused for " + nextOwner.name().toLowerCase();
      } else if (nextOwner == CombatOwnsService.Owner.SCAFFOLD && checkCondition()) {
        this.release();
        return "Paused for scaffold";
      } else if (taskAction instanceof TaskAction.Done || taskAction instanceof TaskAction.Abort) {
        this.release();
        return "";
      } else if (taskAction instanceof TaskAction.Wait wait) {
        this.release();
        return wait.reason();
      } else {
        if (this.class2 != null && this.class2 != taskAction.getClass()) {
          this.updateState4(minecraft);
        }

        this.class2 = taskAction.getClass();
        CombatOwnsService.claim(CombatOwnsService.Owner.TRAVEL);
        CombatOwnsService.clearFrame(CombatOwnsService.Owner.TRAVEL);
        if (!(taskAction instanceof TaskAction.Mine)) {
          this.updateState6(minecraft);
        }

        if (taskAction instanceof TaskAction.Goto gotoAction) {
          return this.createText(minecraft, taskData, travelLoaded, gotoAction);
        } else if (taskAction instanceof TaskAction.Mine mine) {
          return this.createText2(minecraft, taskData, mine);
        } else if (taskAction instanceof TaskAction.Place place) {
          return this.createText3(minecraft, taskData, place);
        } else if (taskAction instanceof TaskAction.Strike strike) {
          return this.createText4(minecraft, strike);
        } else if (taskAction instanceof TaskAction.Cast cast) {
          return this.createText5(minecraft, cast);
        } else {
          if (taskAction instanceof TaskAction.Reel) {
            return this.createText6(minecraft);
          }

          this.release();
          return "";
        }
      }
    } else {
      this.release();
      return "Paused for hand owner";
    }
  }

  public static boolean ownsMiningInput() {
    Minecraft minecraft = Minecraft.getInstance();
    return compatExecuteService != null
        && compatExecuteService.taskData2 != null
        && CombatOwnsService.owns(CombatOwnsService.Owner.TRAVEL)
        && minecraft.screen == null
        && minecraft.player != null
        && minecraft.player.isAlive()
        && !CompatCurrentService.handsBusy();
  }

  public void release() {
    Minecraft minecraft = Minecraft.getInstance();

    try {
      if (this.taskData2 != null && minecraft.gameMode != null) {
        minecraft.gameMode.stopDestroyBlock();
      }

      if (this.count >= 0
          && minecraft.player != null
          && minecraft.gameMode != null
          && CombatOwnsService.owns(CombatOwnsService.Owner.TRAVEL)) {
        InventoryInputActions.select(minecraft, this.count);
        minecraft.gameMode.ensureHasSentCarriedItem();
      }
    } finally {
      this.taskData2 = null;
      if (compatExecuteService == this) {
        compatExecuteService = null;
      }

      this.count = -1;
      this.class2 = null;
      this.travelSteerService.reset();
      this.text = "";
      this.enabled = false;
      if (CombatOwnsService.owns(CombatOwnsService.Owner.TRAVEL)) {
        CompatActiveService.stop();
      }

      CombatOwnsService.release(CombatOwnsService.Owner.TRAVEL);
    }
  }

  private void updateState(String currentText) {
    if (!currentText.equals(this.text)) {
      this.text = currentText;
      CoreIsInitializedHandler.LOGGER.info("[FlowPath] {}", currentText);
    }
  }

  private void updateState2(
      RotationData rotationData, ScaffoldRemapService.Input currentInput, boolean enabled) {
    if ((!currentInput.forward() || !currentInput.backward())
        && (!currentInput.left() || !currentInput.right())) {
      if (enabled && currentInput.sprint()) {
        currentInput =
            new ScaffoldRemapService.Input(
                currentInput.forward(),
                currentInput.backward(),
                currentInput.left(),
                currentInput.right(),
                currentInput.jump(),
                currentInput.sneak(),
                false);
      }

      long offset = this.timestamp++;
      PlacementOverrideBus.publish(
          new PlacementOverrideBus.Override(true, currentInput, false, enabled, true, true));
      ScaffoldPublishService.publish(currentInput, enabled);
      LocalPhysicsFrameBus.publish(
          new LocalPhysicsFrameBus.Frame(
              offset, (float) rotationData.yaw(), currentInput, enabled));
    } else {
      throw new IllegalArgumentException("Travel input must use canonical Vanilla directions");
    }
  }

  private String createText(
      Minecraft minecraft,
      TaskData taskData,
      TravelLoadedHandler travelLoaded,
      TaskAction.Goto gotoAction) {
    this.updateState6(minecraft);
    if (gotoAction.route().isEmpty()) {
      this.enabled = false;
      return gotoAction.arrived() ? "" : "Planning route";
    }

    if (!this.enabled) {
      this.enabled = true;
      this.updateState("route ready waypoints=" + gotoAction.route().size());
    }

    Vector3d vector3d = new Vector3d(taskData.player());
    TravelSteerService.Steer currentSteer =
        this.travelSteerService.steer(
            gotoAction.route(),
            vector3d,
            taskData.onGround(),
            taskData.inWater(),
            gotoAction.canSprint(),
            travelLoaded::jumpHeadroom);
    if (currentSteer.done()) {
      this.updateState2(
          new RotationData(currentSteer.yawDeg(), currentSteer.pitchDeg()), createMap(), true);
      return "";
    }

    boolean shouldJump = currentSteer.jump();
    if (!shouldJump
        && currentSteer.sprint()
        && TravelSteerService.gapAhead(travelLoaded::floor, vector3d, currentSteer.yawDeg())) {
      shouldJump = true;
    }

    RotationData rotationData =
        new RotationData((float) currentSteer.yawDeg(), (float) currentSteer.pitchDeg());
    CompatActiveService.follow(minecraft, rotationData, 0.35F, 100.0F);
    RotationPublishService.publish(
        new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()), rotationData);
    this.updateState2(
        rotationData,
        new ScaffoldRemapService.Input(
            currentSteer.forward(), false, false, false, shouldJump, false, currentSteer.sprint()),
        !currentSteer.sprint());
    return "";
  }

  private String createText2(Minecraft minecraft, TaskData taskData, TaskAction.Mine mine) {
    BlockPos currentX = new BlockPos(mine.pos().x(), mine.pos().y(), mine.pos().z());
    Vec3 vec3 = minecraft.player.getEyePosition();
    byte byteValue = 0;
    if (aim(minecraft, currentX).isEmpty()) {
      BlockHitResult blockHitResult =
          minecraft.level.clip(
              new ClipContext(
                  vec3, Vec3.atCenterOf(currentX), Block.OUTLINE, Fluid.NONE, minecraft.player));
      if (blockHitResult.getType() == Type.BLOCK
          && minecraft.level.getBlockState(blockHitResult.getBlockPos()).is(BlockTags.LEAVES)
          && vec3.distanceToSqr(blockHitResult.getLocation())
              <= Math.pow(minecraft.player.blockInteractionRange(), 2.0)) {
        currentX = blockHitResult.getBlockPos();
        byteValue = 1;
      }
    }

    TaskBlockPosition nextX =
        new TaskBlockPosition(currentX.getX(), currentX.getY(), currentX.getZ());
    Vec3 currentVec3 = Vec3.atCenterOf(currentX);
    RotationData rotationData =
        RotationData.lookAt(
            new RotationVector(vec3.x, vec3.y, vec3.z),
            new RotationVector(currentVec3.x, currentVec3.y, currentVec3.z));
    CompatActiveService.follow(minecraft, rotationData, 0.25F, 180.0F);
    RotationPublishService.publish(
        new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()), rotationData);
    this.updateState2(rotationData, createMap(), true);
    if (minecraft.player.isUsingItem()) {
      return "Waiting for current item use";
    }

    Optional result = aim(minecraft, currentX);
    double doubleValue = Math.sqrt(vec3.distanceToSqr(currentVec3));
    if (result.isEmpty()) {
      int value = !minecraft.level.getBlockState(BlockPos.containing(vec3)).isAir() ? 1 : 0;
      this.updateState(
          "aim-missing "
              + mine.pos()
              + " dist="
              + String.format("%.2f", doubleValue)
              + " reach="
              + String.format("%.2f", minecraft.player.blockInteractionRange())
              + " eyeInBlock="
              + value);
      return "Aiming at " + mine.pos();
    }

    if (this.taskData2 != null && this.taskData2.equals(nextX)) {
      minecraft.gameMode.continueDestroyBlock(
          currentX, ((BlockHitResult) result.get()).getDirection());
    } else {
      if (this.taskData2 != null) {
        minecraft.gameMode.stopDestroyBlock();
      }

      this.updateState3(minecraft, currentX);
      boolean enabled =
          minecraft.gameMode.startDestroyBlock(
              currentX, ((BlockHitResult) result.get()).getDirection());
      this.updateState(
          "start "
              + mine.pos()
              + " ok="
              + enabled
              + " dist="
              + String.format("%.2f", doubleValue)
              + " face="
              + ((BlockHitResult) result.get()).getDirection());
      if (!enabled) {
        this.taskData2 = null;
        return "Cannot start mining";
      }

      this.taskData2 = nextX;
      compatExecuteService = this;
      minecraft.player.swing(InteractionHand.MAIN_HAND);
    }

    return byteValue != 0 ? "Clearing leaves in the way" : "";
  }

  private void updateState3(Minecraft minecraft, BlockPos blockPos) {
    BlockState blockState = minecraft.level.getBlockState(blockPos);
    int value = -1;
    double doubleValue = -1.0;

    for (int index = 0; index < 2 && value < 0; index++) {
      for (int currentIndex = 0; currentIndex < 9; currentIndex++) {
        ItemStack itemStack = minecraft.player.getInventory().getItem(currentIndex);
        if (!itemStack.isEmpty()
            && (index != 0
                || !itemStack.isDamageableItem()
                || itemStack.getMaxDamage() - itemStack.getDamageValue() > 5)) {
          float currentValue = itemStack.getDestroySpeed(blockState);
          int nextValue =
              blockState.requiresCorrectToolForDrops()
                      && !itemStack.isCorrectToolForDrops(blockState)
                  ? 0
                  : 1;
          double currentDoubleValue =
              (nextValue != 0 ? currentValue * 30.0F : currentValue)
                  + (currentIndex == InventoryInputActions.selected(minecraft) ? 0.5 : 0.0);
          if (currentDoubleValue > doubleValue) {
            doubleValue = currentDoubleValue;
            value = currentIndex;
          }
        }
      }
    }

    if (value >= 0) {
      if (this.count < 0) {
        this.count = InventoryInputActions.selected(minecraft);
      }

      if (InventoryInputActions.selected(minecraft) != value) {
        InventoryInputActions.select(minecraft, value);
      }

      minecraft.gameMode.ensureHasSentCarriedItem();
    }
  }

  private static boolean checkCondition() {
    try {
      CoreIsInitializedHandler coreIsInitialized = CoreIsInitializedHandler.get();
      return coreIsInitialized != null && coreIsInitialized.modules() != null
          ? coreIsInitialized
              .modules()
              .get(ImplSuspendForPearlService.class)
              .filter(Module::isEnabled)
              .map(item -> item.activePlacement().isPresent())
              .orElse(false)
          : false;
    } catch (Exception exception) {
      return false;
    }
  }

  static Optional<BlockHitResult> aim(Minecraft minecraft, BlockPos blockPos) {
    if (!minecraft.level.hasChunkAt(blockPos)) {
      return Optional.empty();
    }

    Vec3 vec3 = minecraft.player.getEyePosition();
    double doubleValue = minecraft.player.blockInteractionRange();
    Vec3 currentVec3 = Vec3.atCenterOf(blockPos);

    for (Direction direction : Direction.values()) {
      Vec3 nextVec3 =
          currentVec3.add(
              direction.getStepX() * 0.499,
              direction.getStepY() * 0.499,
              direction.getStepZ() * 0.499);
      if (!(vec3.distanceToSqr(nextVec3) > doubleValue * doubleValue)) {
        BlockHitResult blockHitResult =
            minecraft.level.clip(
                new ClipContext(vec3, nextVec3, Block.OUTLINE, Fluid.NONE, minecraft.player));
        if (blockHitResult.getType() == Type.BLOCK
            && blockHitResult.getBlockPos().equals(blockPos)) {
          return Optional.of(blockHitResult);
        }
      }
    }

    return Optional.empty();
  }

  private String createText3(Minecraft minecraft, TaskData taskData, TaskAction.Place place) {
    this.updateState6(minecraft);
    int value = this.calculateValue(minecraft, place.id());
    if (value < 0) {
      return "Missing " + place.id();
    }

    Direction direction =
        switch (place.face()) {
          case UP -> Direction.UP;
          case DOWN -> Direction.DOWN;
          case NORTH -> Direction.NORTH;
          case SOUTH -> Direction.SOUTH;
          case EAST -> Direction.EAST;
          case WEST -> Direction.WEST;
        };
    BlockPos currentX =
        new BlockPos(
            place.pos().x() - direction.getStepX(),
            place.pos().y() - direction.getStepY(),
            place.pos().z() - direction.getStepZ());
    Vec3 nextX =
        new Vec3(
            place.pos().x() + 0.5 - direction.getStepX() * 0.5,
            place.pos().y() + 0.5 - direction.getStepY() * 0.5,
            place.pos().z() + 0.5 - direction.getStepZ() * 0.5);
    Vec3 vec3 = minecraft.player.getEyePosition();
    if (vec3.distanceToSqr(nextX) > Math.pow(minecraft.player.blockInteractionRange() + 1.0, 2.0)) {
      return "Out of reach";
    }

    RotationData rotationData =
        RotationData.lookAt(
            new RotationVector(vec3.x, vec3.y, vec3.z),
            new RotationVector(nextX.x, nextX.y, nextX.z));
    CompatActiveService.follow(minecraft, rotationData, 0.25F, 180.0F);
    RotationPublishService.publish(
        new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()), rotationData);
    this.updateState2(rotationData, createMap(), true);
    InteractionResult interactionResult =
        minecraft.gameMode.useItemOn(
            minecraft.player,
            InteractionHand.MAIN_HAND,
            new BlockHitResult(nextX, direction, currentX, false));
    if (interactionResult.consumesAction()) {
      minecraft.player.swing(InteractionHand.MAIN_HAND);
    }

    this.updateState4(minecraft);
    return "";
  }

  private void updateState4(Minecraft minecraft) {
    if (this.count >= 0) {
      InventoryInputActions.select(minecraft, this.count);
      minecraft.gameMode.ensureHasSentCarriedItem();
      this.count = -1;
    }
  }

  private int calculateValue(Minecraft minecraft, String text) {
    int value = calculateValue2(minecraft, text);
    if (value < 0) {
      int currentValue = calculateValue3(minecraft, text);
      if (currentValue < 0) {
        return -1;
      }

      int nextValue = -1;
      int index = 0;

      while (true) {
        if (index < 9) {
          if (!minecraft.player.getInventory().getItem(index).isEmpty()) {
            index++;
            continue;
          }

          nextValue = index;
        }

        index = nextValue >= 0 ? nextValue : InventoryInputActions.selected(minecraft);
        Slot slot = minecraft.player.inventoryMenu.getSlot(9 + currentValue);
        Slot currentSlot = minecraft.player.inventoryMenu.getSlot(36 + index);
        ItemStack itemStack = slot.getItem().copy();
        ItemStack currentItemStack = currentSlot.getItem().copy();
        if (itemStack.isEmpty()
            || !minecraft.player.inventoryMenu.getCarried().isEmpty()
            || !slot.mayPickup(minecraft.player)
            || !currentSlot.mayPlace(itemStack)
            || !slot.mayPlace(currentItemStack)) {
          return -1;
        }

        InventoryInputActions.click(
            minecraft, 9 + currentValue, index, SoupClickTracker.Click.SWAP);
        if (!ItemStack.isSameItemSameComponents(
            minecraft.player.inventoryMenu.getSlot(36 + index).getItem(), itemStack)) {
          return -1;
        }

        value = index;
        break;
      }
    }

    if (this.count < 0) {
      this.count = InventoryInputActions.selected(minecraft);
    }

    if (InventoryInputActions.selected(minecraft) != value) {
      InventoryInputActions.select(minecraft, value);
    }

    minecraft.gameMode.ensureHasSentCarriedItem();
    return value;
  }

  private String createText4(Minecraft minecraft, TaskAction.Strike strike) {
    this.updateState6(minecraft);
    Entity entity = minecraft.level.getEntity(strike.entityRef());
    if (entity != null && entity.isAlive()) {
      String text;
      try {
        text = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
      } catch (Exception exception) {
        return "Target gone";
      }

      if (!FoodGatheringTask.PREY.contains(text)) {
        return "Not prey — standing down";
      }

      if (minecraft.player.distanceToSqr(entity.getX(), entity.getY(), entity.getZ())
          > 10.240000000000002) {
        return "Out of reach";
      }

      Vec3 vec3 = minecraft.player.getEyePosition();
      RotationData currentX =
          RotationData.lookAt(
              new RotationVector(vec3.x, vec3.y, vec3.z),
              new RotationVector(
                  entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ()));
      CompatActiveService.follow(minecraft, currentX, 0.25F, 180.0F);
      RotationPublishService.publish(
          new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()), currentX);
      this.updateState2(currentX, createMap(), true);
      if (minecraft.player.isUsingItem()) {
        return "Waiting for current item use";
      }

      this.updateState5(minecraft);
      if (minecraft.player.getAttackStrengthScale(0.5F) < 0.9F) {
        return "Winding up";
      }

      minecraft.gameMode.attack(minecraft.player, entity);
      minecraft.player.swing(InteractionHand.MAIN_HAND);
      return "";
    } else {
      return "Target gone";
    }
  }

  private void updateState5(Minecraft currentMinecraft) {
    String[] strings =
        new String[] {
          "minecraft:netherite_sword",
          "minecraft:diamond_sword",
          "minecraft:iron_sword",
          "minecraft:stone_sword",
          "minecraft:wooden_sword",
          "minecraft:netherite_axe",
          "minecraft:diamond_axe",
          "minecraft:iron_axe",
          "minecraft:stone_axe",
          "minecraft:wooden_axe"
        };

    for (String text : strings) {
      int value = calculateValue2(currentMinecraft, text);
      if (value >= 0) {
        ItemStack itemStack = currentMinecraft.player.getInventory().getItem(value);
        if (!itemStack.isDamageableItem()
            || itemStack.getMaxDamage() - itemStack.getDamageValue() > 5) {
          if (this.count < 0) {
            this.count = InventoryInputActions.selected(currentMinecraft);
          }

          if (InventoryInputActions.selected(currentMinecraft) != value) {
            InventoryInputActions.select(currentMinecraft, value);
          }

          currentMinecraft.gameMode.ensureHasSentCarriedItem();
          return;
        }
      }
    }
  }

  private String createText5(Minecraft currentMinecraft, TaskAction.Cast cast) {
    this.updateState6(currentMinecraft);
    int value = this.calculateValue(currentMinecraft, "minecraft:fishing_rod");
    if (value < 0) {
      return "No rod in reach";
    } else if (currentMinecraft.player.isUsingItem()) {
      currentMinecraft.gameMode.releaseUsingItem(currentMinecraft.player);
      return "Clearing line";
    } else {
      BlockPos currentX = new BlockPos(cast.water().x(), cast.water().y(), cast.water().z());
      Vec3 vec3 = currentMinecraft.player.getEyePosition();
      Vec3 currentVec3 = Vec3.atCenterOf(currentX);
      RotationData rotationData =
          RotationData.lookAt(
              new RotationVector(vec3.x, vec3.y, vec3.z),
              new RotationVector(currentVec3.x, currentVec3.y, currentVec3.z));
      CompatActiveService.follow(currentMinecraft, rotationData, 0.25F, 180.0F);
      RotationPublishService.publish(
          new RotationData(currentMinecraft.player.getYRot(), currentMinecraft.player.getXRot()),
          rotationData);
      this.updateState2(rotationData, createMap(), true);
      currentMinecraft.gameMode.useItem(currentMinecraft.player, InteractionHand.MAIN_HAND);
      return "";
    }
  }

  private String createText6(Minecraft currentMinecraft) {
    this.updateState6(currentMinecraft);
    int value = this.calculateValue(currentMinecraft, "minecraft:fishing_rod");
    if (value < 0) {
      return "No rod in reach";
    }

    if (!currentMinecraft.player.isUsingItem()) {
      return "";
    }

    this.updateState2(
        new RotationData(currentMinecraft.player.getYRot(), currentMinecraft.player.getXRot()),
        createMap(),
        true);
    currentMinecraft.gameMode.useItem(currentMinecraft.player, InteractionHand.MAIN_HAND);
    return "";
  }

  private static int calculateValue2(Minecraft minecraft, String text) {
    for (int index = 0; index < 9; index++) {
      ItemStack itemStack = minecraft.player.getInventory().getItem(index);
      if (!itemStack.isEmpty()
          && BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().equals(text)) {
        return index;
      }
    }

    return -1;
  }

  private static int calculateValue3(Minecraft minecraft, String text) {
    for (int index = 9; index < 36; index++) {
      ItemStack itemStack = minecraft.player.getInventory().getItem(index);
      if (!itemStack.isEmpty()
          && BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().equals(text)) {
        return index - 9;
      }
    }

    return -1;
  }

  private void updateState6(Minecraft minecraft) {
    if (compatExecuteService == this) {
      compatExecuteService = null;
    }

    if (this.taskData2 != null) {
      minecraft.gameMode.stopDestroyBlock();
      this.taskData2 = null;
      this.updateState4(minecraft);
    }
  }

  private static ScaffoldRemapService.Input createMap() {
    return new ScaffoldRemapService.Input(false, false, false, false, false, false, false);
  }
}
