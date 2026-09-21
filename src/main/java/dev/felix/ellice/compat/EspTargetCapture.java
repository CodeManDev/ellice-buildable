package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.esp.EspLabelData;
import dev.felix.ellice.feature.esp.EspProjectService;
import dev.felix.ellice.friends.FriendsMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class EspTargetCapture {
  private EspTargetCapture() {}

  public static List<EspTargetCapture.Target> capture(
      EspTargetCapture.Options currentOptions, float value) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null && minecraft.player != null) {
      Entity currentValue =
          minecraft.getCameraEntity() != null ? minecraft.getCameraEntity() : minecraft.player;
      Vec3 vec3 = currentValue.getPosition(value);
      ArrayList<EspTargetCapture.Target> arrayList = new ArrayList<>();
      double doubleValue = currentOptions.range * currentOptions.range;

      for (Entity entity : minecraft.level.entitiesForRendering()) {
        if (!FriendSyncController.excluded(FriendsMode.ESP, entity)
            && !CompatDecisionTracker.excluded(AntibotFeatureType.ESP, entity)) {
          boolean enabled = entity instanceof Player;
          boolean currentEnabled = entity instanceof ItemEntity;
          if ((currentEnabled
                  ? currentOptions.items
                  : entity instanceof LivingEntity && !(entity instanceof ArmorStand))
              && entity.isAlive()
              && !entity.isSpectator()
              && !skipInvisibleEntity(
                  currentOptions.invisible,
                  entity.isInvisible(),
                  entity.isInvisibleTo(minecraft.player))
              && (entity != minecraft.player || currentOptions.self)
              && (entity != currentValue || !minecraft.options.getCameraType().isFirstPerson())
              && (currentEnabled
                  || (enabled
                      ? currentOptions.players
                      : currentOptions.mobs.equals("All")
                          || currentOptions.mobs.equals("Hostile") && entity instanceof Enemy))) {
            Vec3 currentVec3 = entity.getPosition(value);
            double currentDoubleValue = currentVec3.distanceToSqr(vec3);
            if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue > doubleValue)) {
              AABB currentX =
                  entity
                      .getBoundingBox()
                      .move(
                          currentVec3.x - entity.getX(),
                          currentVec3.y - entity.getY(),
                          currentVec3.z - entity.getZ());
              if (currentEnabled) {
                currentX =
                    new AABB(
                        currentVec3.x - 0.3,
                        currentVec3.y,
                        currentVec3.z - 0.3,
                        currentVec3.x + 0.3,
                        currentVec3.y + 0.65,
                        currentVec3.z + 0.3);
              }

              arrayList.add(
                  new EspTargetCapture.Target(
                      entity.getUUID(),
                      entity,
                      List.of(),
                      createBox(currentX),
                      Math.sqrt(currentDoubleValue),
                      enabled));
            }
          }
        }
      }

      if (currentOptions.storage()) {
        HashSet hashSet = new HashSet();
        int nextValue = (int) Math.ceil(currentOptions.range / 16.0) + 1;
        int nextX = BlockPos.containing(vec3).getX() >> 4;
        int previousValue = BlockPos.containing(vec3).getZ() >> 4;

        for (int index = nextX - nextValue; index <= nextX + nextValue; index++) {
          for (int currentIndex = previousValue - nextValue;
              currentIndex <= previousValue + nextValue;
              currentIndex++) {
            LevelChunk levelChunk =
                minecraft
                    .level
                    .getChunkSource()
                    .getChunk(index, currentIndex, ChunkStatus.FULL, false);
            if (levelChunk != null) {
              for (BlockEntity blockEntity : levelChunk.getBlockEntities().values()) {
                BlockPos blockPos = blockEntity.getBlockPos();
                if (!blockEntity.isRemoved()
                    && checkCondition(blockEntity, currentOptions)
                    && hashSet.add(blockPos)
                    && !(blockPos.getCenter().distanceToSqr(vec3) > doubleValue)) {
                  ArrayList<BlockEntity> currentArrayList = new ArrayList<>();
                  currentArrayList.add(blockEntity);
                  AABB aABB = createAABB(blockEntity);
                  BlockState blockState = blockEntity.getBlockState();
                  if (blockEntity instanceof ChestBlockEntity
                      && blockState.hasProperty(ChestBlock.TYPE)
                      && blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
                    BlockPos currentBlockPos =
                        blockPos.relative(ChestBlock.getConnectedDirection(blockState));
                    if (minecraft.level.hasChunkAt(currentBlockPos)
                        && minecraft.level.getBlockEntity(currentBlockPos)
                            instanceof ChestBlockEntity chestBlockEntity
                        && chestBlockEntity.getBlockState().is(blockState.getBlock())
                        && !chestBlockEntity.isRemoved()) {
                      currentArrayList.add(chestBlockEntity);
                      hashSet.add(currentBlockPos);
                      aABB = aABB.minmax(createAABB(chestBlockEntity));
                    }
                  }

                  currentArrayList.sort(
                      Comparator.comparingLong(item -> item.getBlockPos().asLong()));
                  UUID uUID =
                      new UUID(
                          4995424730511004229L,
                          ((BlockEntity) currentArrayList.getFirst()).getBlockPos().asLong());
                  arrayList.add(
                      new EspTargetCapture.Target(
                          uUID,
                          null,
                          List.copyOf(currentArrayList),
                          createBox(aABB),
                          Math.sqrt(aABB.getCenter().distanceToSqr(vec3)),
                          false));
                }
              }
            }
          }
        }
      }

      arrayList.sort(
          Comparator.comparingDouble(EspTargetCapture.Target::distance)
              .thenComparing(EspTargetCapture.Target::id));
      return arrayList;
    } else {
      return List.of();
    }
  }

  private static boolean checkCondition(BlockEntity blockEntity, EspTargetCapture.Options options) {
    if (blockEntity instanceof ChestBlockEntity) {
      return options.chests;
    } else if (blockEntity instanceof EnderChestBlockEntity) {
      return options.enderChests;
    } else if (blockEntity instanceof BarrelBlockEntity) {
      return options.barrels;
    } else {
      return blockEntity instanceof ShulkerBoxBlockEntity
          ? options.shulkers
          : options.otherStorage && blockEntity instanceof Container;
    }
  }

  private static AABB createAABB(BlockEntity blockEntity) {
    VoxelShape voxelShape =
        blockEntity.getBlockState().getShape(blockEntity.getLevel(), blockEntity.getBlockPos());
    return (voxelShape.isEmpty() ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : voxelShape.bounds())
        .move(blockEntity.getBlockPos());
  }

  static boolean skipInvisibleEntity(boolean visible, boolean currentVisible, boolean nextVisible) {
    return !visible && (currentVisible || nextVisible);
  }

  private static EspProjectService.Box createBox(AABB aABB) {
    return new EspProjectService.Box(
        aABB.minX, aABB.minY, aABB.minZ, aABB.maxX, aABB.maxY, aABB.maxZ);
  }

  public static boolean visible(EspTargetCapture.Target target, Vec3 vec3) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null && minecraft.player != null) {
      EspProjectService.Box currentBox = target.box;
      double doubleValue = (currentBox.minX() + currentBox.maxX()) * 0.5;
      double currentDoubleValue = (currentBox.minZ() + currentBox.maxZ()) * 0.5;

      for (double nextDoubleValue : new double[] {0.85, 0.5}) {
        Vec3 currentVec3 =
            new Vec3(
                doubleValue,
                currentBox.minY() + (currentBox.maxY() - currentBox.minY()) * nextDoubleValue,
                currentDoubleValue);
        BlockHitResult blockHitResult =
            minecraft.level.clip(
                new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.NONE, minecraft.player));
        if (blockHitResult.getType() == Type.MISS
            || target.blocks.stream()
                .anyMatch(item -> item.getBlockPos().equals(blockHitResult.getBlockPos()))) {
          return true;
        }
      }

      return false;
    } else {
      return false;
    }
  }

  public static EspLabelData describe(EspTargetCapture.Target target) {
    Entity currentEntity = target.entity;
    if (currentEntity instanceof LivingEntity livingEntity) {
      float value = livingEntity.getMaxHealth();
      return new EspLabelData(
          currentEntity.getDisplayName().getString(),
          (int) Math.round(target.distance),
          value > 0.0F ? livingEntity.getHealth() / value : 0.0F,
          value > 0.0F ? livingEntity.getAbsorptionAmount() / value : 0.0F,
          livingEntity.getArmorValue() / 20.0F,
          currentEntity.getTeam() != null ? 0xFF000000 | currentEntity.getTeamColor() : 0);
    } else {
      String text;
      if (currentEntity instanceof ItemEntity itemEntity) {
        text =
            itemEntity.getItem().getHoverName().getString()
                + (itemEntity.getItem().getCount() > 1
                    ? " ×" + itemEntity.getItem().getCount()
                    : "");
      } else {
        text = target.blocks.getFirst().getBlockState().getBlock().getName().getString();
      }

      return new EspLabelData(text, (int) Math.round(target.distance), 0.0F, 0.0F, 0.0F, 0);
    }
  }

  public record Options(
      boolean players,
      String mobs,
      boolean self,
      boolean invisible,
      double range,
      boolean items,
      boolean chests,
      boolean enderChests,
      boolean barrels,
      boolean shulkers,
      boolean otherStorage) {
    public Options(
        boolean enabled,
        String text,
        boolean currentEnabled,
        boolean nextEnabled,
        double doubleValue) {
      this(
          enabled,
          text,
          currentEnabled,
          nextEnabled,
          doubleValue,
          false,
          false,
          false,
          false,
          false,
          false);
    }

    boolean storage() {
      return this.chests || this.enderChests || this.barrels || this.shulkers || this.otherStorage;
    }
  }

  public record Target(
      UUID id,
      Entity entity,
      List<BlockEntity> blocks,
      EspProjectService.Box box,
      double distance,
      boolean player) {
    public boolean alive() {
      if (this.entity != null) {
        return this.entity.isAlive() && !this.entity.isRemoved();
      }

      ClientLevel clientLevel = Minecraft.getInstance().level;
      return clientLevel != null
          && !this.blocks.isEmpty()
          && this.blocks.stream()
              .allMatch(
                  item ->
                      !item.isRemoved()
                          && clientLevel.hasChunkAt(item.getBlockPos())
                          && clientLevel.getBlockEntity(item.getBlockPos()) == item);
    }
  }
}
