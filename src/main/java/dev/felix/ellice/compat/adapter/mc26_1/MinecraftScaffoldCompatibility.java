package dev.felix.ellice.compat.adapter.mc26_1;

import dev.felix.ellice.compat.CompatCanReplaceService;
import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.ScaffoldBlockHit;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.feature.scaffold.ScaffoldMode;
import dev.felix.ellice.feature.scaffold.ScaffoldPlayerSnapshot;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.InteractionResult.SwingSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

final class MinecraftScaffoldCompatibility implements ScaffoldCompatibility {
  static final MinecraftScaffoldCompatibility INSTANCE = new MinecraftScaffoldCompatibility();

  private MinecraftScaffoldCompatibility() {}

  @Override
  public Optional<ScaffoldPlayerSnapshot> capture(Minecraft minecraft) {
    if (minecraft.player != null && minecraft.level != null && !minecraft.player.isPassenger()) {
      LocalPlayer localPlayer = minecraft.player;
      Vec3 vec3 = localPlayer.getEyePosition();
      Vec3 currentVec3 = localPlayer.getDeltaMovement();
      return Optional.of(
          new ScaffoldPlayerSnapshot(
              createRotationData7(vec3),
              new RotationVector(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ()),
              createRotationData7(currentVec3),
              new RotationData(localPlayer.getYRot(), localPlayer.getXRot()),
              (Double) minecraft.options.sensitivity().get(),
              localPlayer.onGround()));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public boolean isReplaceable(Minecraft minecraft, BlockCoordinates blockCoordinates) {
    return CompatCanReplaceService.canReplace(
        minecraft, blockCoordinates, this.findPlaceableHotbarSlot(minecraft));
  }

  @Override
  public boolean isPlayerSupporting(Minecraft minecraft, BlockCoordinates blockCoordinates) {
    if (minecraft.level == null) {
      return false;
    }

    BlockPos blockPos = createBlockPos(blockCoordinates);
    BlockState blockState = minecraft.level.getBlockState(blockPos);
    return !blockState.canBeReplaced()
        && blockState.isCollisionShapeFullBlock(minecraft.level, blockPos);
  }

  @Override
  public double groundFriction(Minecraft minecraft) {
    return minecraft.player != null && minecraft.level != null
        ? minecraft
            .level
            .getBlockState(minecraft.player.getBlockPosBelowThatAffectsMyMovement())
            .getBlock()
            .getFriction()
        : 1.0;
  }

  @Override
  public double groundInputAcceleration(Minecraft minecraft) {
    return this.groundInputAcceleration(minecraft, false);
  }

  @Override
  public double groundInputAcceleration(Minecraft minecraft, boolean enabled) {
    if (minecraft.player != null && minecraft.level != null) {
      float value = (float) this.groundFriction(minecraft);
      double doubleValue = minecraft.player.getAttributeValue(Attributes.MOVEMENT_SPEED);
      if (enabled && !minecraft.player.isSprinting()) {
        doubleValue *= 1.300000011920929;
      }

      return (float) doubleValue * (0.21600002F / (value * value * value));
    } else {
      return Double.NaN;
    }
  }

  @Override
  public double groundInputAccelerationEstimate(
      Minecraft minecraft, int value, int currentValue, boolean enabled) {
    if (value < -1 || value > 1 || currentValue < -1 || currentValue > 1) {
      throw new IllegalArgumentException("Input axes must be in [-1, 1]");
    } else if (minecraft.player == null || minecraft.level == null) {
      return Double.NaN;
    } else {
      return value == 0 && currentValue == 0
          ? 0.0
          : this.groundInputAcceleration(minecraft, enabled)
              * calculateValue(minecraft, value, currentValue);
    }
  }

  @Override
  public double airInputAccelerationEstimate(Minecraft minecraft, int value, int currentValue) {
    return minecraft.player == null
        ? Double.NaN
        : 0.019999999552965164 * calculateValue(minecraft, value, currentValue);
  }

  @Override
  public double jumpVelocityEstimate(Minecraft minecraft) {
    if (minecraft.player == null) {
      return Double.NaN;
    }

    float value = minecraft.player.getJumpPower();
    return value <= 1.0E-5F ? 0.0 : Math.max(value, minecraft.player.getDeltaMovement().y);
  }

  private static double calculateValue(Minecraft minecraft, int value, int currentValue) {
    Vec2 vec2 = new Vec2(currentValue, value).normalized();
    return minecraft.player.modifyInput(vec2).length();
  }

  @Override
  public boolean isFaceSturdy(
      Minecraft minecraft, BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
    if (minecraft.level == null) {
      return false;
    }

    BlockPos blockPos = createBlockPos(blockCoordinates);
    return minecraft
        .level
        .getBlockState(blockPos)
        .isFaceSturdy(minecraft.level, blockPos, createDirection(scaffoldMode));
  }

  @Override
  public int findPlaceableHotbarSlot(Minecraft minecraft) {
    return CompatProfileTracker.find(minecraft);
  }

  @Override
  public int selectedHotbarSlot(Minecraft minecraft) {
    return minecraft.player == null ? -1 : minecraft.player.getInventory().getSelectedSlot();
  }

  @Override
  public void selectHotbarSlot(Minecraft minecraft, int value) {
    if (minecraft.player != null && value >= 0 && value < 9) {
      minecraft.player.getInventory().setSelectedSlot(value);
      CompatProfileTracker.selectedByScaffold(value);
      if (minecraft.gameMode != null) {
        minecraft.gameMode.ensureHasSentCarriedItem();
      }
    }
  }

  @Override
  public boolean canStartUseItem(Minecraft minecraft) {
    return minecraft.player != null
        && minecraft.gameMode != null
        && !minecraft.gameMode.isDestroying()
        && !minecraft.player.isHandsBusy();
  }

  @Override
  public Optional<PlacementCandidate> raycastPlacement(
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      RotationData rotationData,
      double doubleValue) {
    return minecraft.player != null && minecraft.level != null
        ? this.raycastPlacementFromEye(
            minecraft,
            placementCandidate,
            rotationData,
            doubleValue,
            createRotationData7(minecraft.player.getEyePosition()))
        : Optional.empty();
  }

  @Override
  public Optional<PlacementCandidate> raycastPlacementFromEye(
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      RotationData rotationData,
      double doubleValue,
      RotationVector rotationVector) {
    return this.raycastBlockFromEye(minecraft, rotationData, doubleValue, rotationVector)
        .filter(item -> item.blockPosition().equals(placementCandidate.supportPosition()))
        .filter(item -> item.face() == placementCandidate.clickedFace())
        .map(
            item ->
                new PlacementCandidate(
                    placementCandidate.targetPosition(),
                    placementCandidate.supportPosition(),
                    placementCandidate.clickedFace(),
                    item.hitPoint(),
                    item.inside()));
  }

  @Override
  public Optional<ScaffoldBlockHit> raycastBlockFromEye(
      Minecraft minecraft,
      RotationData rotationData,
      double doubleValue,
      RotationVector rotationVector) {
    if (minecraft.player != null && minecraft.level != null) {
      Vec3 vec3 = createVec3(rotationVector);
      Vec3 currentVec3 =
          minecraft.player.calculateViewVector(
              (float) rotationData.pitch(), (float) rotationData.yaw());
      double currentDoubleValue = Math.min(doubleValue, minecraft.player.blockInteractionRange());
      Vec3 nextVec3 = vec3.add(currentVec3.scale(currentDoubleValue));
      BlockHitResult blockHitResult =
          minecraft.level.clip(
              new ClipContext(vec3, nextVec3, Block.OUTLINE, Fluid.NONE, minecraft.player));
      if (blockHitResult instanceof BlockHitResult currentBlockHitResult
          && blockHitResult.getType() == Type.BLOCK) {
        double nextDoubleValue = vec3.distanceToSqr(currentBlockHitResult.getLocation());
        Vec3 previousVec3 = vec3.subtract(minecraft.player.getEyePosition());
        AABB aABB =
            minecraft
                .player
                .getBoundingBox()
                .move(previousVec3)
                .expandTowards(currentVec3.scale(currentDoubleValue))
                .inflate(1.0);
        EntityHitResult entityHitResult =
            ProjectileUtil.getEntityHitResult(
                minecraft.player,
                vec3,
                nextVec3,
                aABB,
                EntitySelector.CAN_BE_PICKED,
                nextDoubleValue);
        return entityHitResult != null
                && vec3.distanceToSqr(entityHitResult.getLocation()) < nextDoubleValue
            ? Optional.empty()
            : Optional.of(
                new ScaffoldBlockHit(
                    createScaffoldData7(currentBlockHitResult.getBlockPos()),
                    createScaffoldMode(currentBlockHitResult.getDirection()),
                    createRotationData7(currentBlockHitResult.getLocation()),
                    currentBlockHitResult.isInside()));
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  @Override
  public boolean place(Minecraft minecraft, PlacementCandidate placementCandidate) {
    if (minecraft.player != null && minecraft.gameMode != null) {
      BlockHitResult blockHitResult =
          new BlockHitResult(
              createVec3(placementCandidate.hitPoint()),
              createDirection(placementCandidate.clickedFace()),
              createBlockPos(placementCandidate.supportPosition()),
              placementCandidate.inside());
      ItemStack itemStack = minecraft.player.getMainHandItem();
      int value = itemStack.getCount();
      RotationData rotationData =
          RotationPublishService.current()
              .orElseGet(
                  () -> new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()));
      InteractionResult interactionResult =
          ScaffoldForActorService.withRotation(
              minecraft.player,
              rotationData,
              () ->
                  minecraft.gameMode.useItemOn(
                      minecraft.player, InteractionHand.MAIN_HAND, blockHitResult));
      if (interactionResult instanceof Success success
          && success.swingSource() == SwingSource.CLIENT) {
        minecraft.player.swing(InteractionHand.MAIN_HAND);
        if (!itemStack.isEmpty()
            && (itemStack.getCount() != value || minecraft.player.hasInfiniteMaterials())) {
          minecraft.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.MAIN_HAND);
        }
      }

      return interactionResult.consumesAction();
    } else {
      return false;
    }
  }

  @Override
  public ScaffoldCompatibility.PlacementFeedback placementFeedback(
      Object value, BlockCoordinates blockCoordinates) {
    if (value instanceof ClientboundBlockUpdatePacket clientboundBlockUpdatePacket
        && clientboundBlockUpdatePacket.getPos().equals(createBlockPos(blockCoordinates))) {
      return checkCondition2(
              clientboundBlockUpdatePacket.getBlockState(), createBlockPos(blockCoordinates))
          ? ScaffoldCompatibility.PlacementFeedback.ACCEPTED
          : ScaffoldCompatibility.PlacementFeedback.REJECTED;
    } else if (value
        instanceof ClientboundSectionBlocksUpdatePacket clientboundSectionBlocksUpdatePacket) {
      AtomicReference atomicReference =
          new AtomicReference<>(ScaffoldCompatibility.PlacementFeedback.NONE);
      clientboundSectionBlocksUpdatePacket.runUpdates(
          (item, currentItem) -> {
            if (item.equals(createBlockPos(blockCoordinates))) {
              atomicReference.set(
                  checkCondition2(currentItem, item)
                      ? ScaffoldCompatibility.PlacementFeedback.ACCEPTED
                      : ScaffoldCompatibility.PlacementFeedback.REJECTED);
            }
          });
      return (ScaffoldCompatibility.PlacementFeedback) atomicReference.get();
    } else {
      return ScaffoldCompatibility.PlacementFeedback.NONE;
    }
  }

  @Override
  public int placementSequence(Object value, PlacementCandidate placementCandidate) {
    if (!(value instanceof ServerboundUseItemOnPacket serverboundUseItemOnPacket)) {
      return -1;
    } else {
      BlockHitResult blockHitResult = serverboundUseItemOnPacket.getHitResult();
      return blockHitResult
                  .getBlockPos()
                  .equals(createBlockPos(placementCandidate.supportPosition()))
              && blockHitResult.getDirection() == createDirection(placementCandidate.clickedFace())
          ? serverboundUseItemOnPacket.getSequence()
          : -1;
    }
  }

  @Override
  public int acknowledgedPlacementSequence(Object value) {
    return value instanceof ClientboundBlockChangedAckPacket clientboundBlockChangedAckPacket
        ? clientboundBlockChangedAckPacket.sequence()
        : -1;
  }

  @Override
  public void reconcilePlacementPrediction(Minecraft minecraft, int value) {
    if (minecraft.level != null && value >= 0) {
      minecraft.level.handleBlockChangedAck(value);
    } else {
      throw new IllegalArgumentException("Exact placement sequence and level are required");
    }
  }

  private static BlockPos createBlockPos(BlockCoordinates blockCoordinates) {
    return new BlockPos(blockCoordinates.x(), blockCoordinates.y(), blockCoordinates.z());
  }

  private static BlockCoordinates createScaffoldData7(BlockPos blockPos) {
    return new BlockCoordinates(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  private static boolean checkCondition(ItemStack itemStack) {
    if (!(!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem blockItem)) {
      return false;
    } else {
      return blockItem.getBlock() instanceof FallingBlock
          ? false
          : checkCondition2(blockItem.getBlock().defaultBlockState(), BlockPos.ZERO);
    }
  }

  private static boolean checkCondition2(BlockState blockState, BlockPos blockPos) {
    return !blockState.canBeReplaced()
        && blockState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, blockPos);
  }

  private static Direction createDirection(ScaffoldMode scaffoldMode) {
    return switch (scaffoldMode) {
      case DOWN -> Direction.DOWN;
      case UP -> Direction.UP;
      case NORTH -> Direction.NORTH;
      case SOUTH -> Direction.SOUTH;
      case WEST -> Direction.WEST;
      case EAST -> Direction.EAST;
    };
  }

  private static ScaffoldMode createScaffoldMode(Direction direction) {
    return switch (direction) {
      case DOWN -> ScaffoldMode.DOWN;
      case UP -> ScaffoldMode.UP;
      case NORTH -> ScaffoldMode.NORTH;
      case SOUTH -> ScaffoldMode.SOUTH;
      case WEST -> ScaffoldMode.WEST;
      case EAST -> ScaffoldMode.EAST;
      default -> throw new MatchException(null, null);
    };
  }

  private static Vec3 createVec3(RotationVector rotationVector) {
    return new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z());
  }

  private static RotationVector createRotationData7(Vec3 vec3) {
    return new RotationVector(vec3.x, vec3.y, vec3.z);
  }
}
