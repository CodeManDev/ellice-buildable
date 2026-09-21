package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.task.TaskBlockPosition;
import dev.felix.ellice.feature.task.TaskData;
import dev.felix.ellice.feature.task.TaskOperationHandler;
import dev.felix.ellice.feature.task.TaskWorldView;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public final class CompatCurrentService implements TaskOperationHandler {
  private final TravelLoadedHandler travelLoadedHandler;
  private final View f9orpj79fiz = new View();

  public CompatCurrentService(TravelLoadedHandler travelLoadedHandler) {
    this.travelLoadedHandler = Objects.requireNonNull(travelLoadedHandler, "traversal");
  }

  public static Optional<CompatCurrentService> current() {
    return CompatAdapterService.terrainEnvironment()
        .map(CompatLoadedHandler::traversal)
        .map(CompatCurrentService::new);
  }

  @Override
  public TravelLoadedHandler traversal() {
    return this.travelLoadedHandler;
  }

  @Override
  public TaskWorldView blocks() {
    return this.f9orpj79fiz;
  }

  @Override
  public long timeOfDay() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      return 6000L;
    }
    for (String string : new String[] {"getOverworldClockTime", "getDayTime", "getGameTime"}) {
      try {
        Object object =
            minecraft
                .level
                .getClass()
                .getMethod(string, new Class[0])
                .invoke((Object) minecraft.level, new Object[0]);
        if (!(object instanceof Number)) continue;
        Number number = (Number) object;
        return number.longValue();
      } catch (ReflectiveOperationException reflectiveOperationException) {

      }
    }
    return 6000L;
  }

  @Override
  public boolean skyAbove() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null || minecraft.player == null) {
      return true;
    }
    try {
      return minecraft.level.canSeeSky(minecraft.player.blockPosition().above());
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public List<TaskOperationHandler.SeenEntity> entities() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null || minecraft.player == null) {
      return List.of();
    }
    ArrayList<TaskOperationHandler.SeenEntity> arrayList =
        new ArrayList<TaskOperationHandler.SeenEntity>();
    try {
      for (Entity entity : minecraft.level.entitiesForRendering()) {
        String string;
        if (!entity.isAlive()
            || entity.isSpectator()
            || entity.getUUID().equals(minecraft.player.getUUID())) continue;
        try {
          string = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        } catch (Exception exception) {
          continue;
        }
        arrayList.add(
            new TaskOperationHandler.SeenEntity(
                string,
                entity.getId(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                entity.getDeltaMovement().y));
      }
    } catch (Exception exception) {
      return List.of();
    }
    arrayList.sort(
        Comparator.comparingDouble(
            seenEntity ->
                minecraft.player.distanceToSqr(seenEntity.x(), seenEntity.y(), seenEntity.z())));
    return arrayList.size() > 48 ? arrayList.subList(0, 48) : List.copyOf(arrayList);
  }

  @Override
  public List<TaskOperationHandler.DroppedItem> droppedItems() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null || minecraft.player == null) {
      return List.of();
    }
    ArrayList<TaskOperationHandler.DroppedItem> arrayList =
        new ArrayList<TaskOperationHandler.DroppedItem>();
    for (Entity entity : minecraft.level.entitiesForRendering()) {
      ItemStack itemStack;
      ItemEntity itemEntity;
      if (!(entity instanceof ItemEntity)
          || !(itemEntity = (ItemEntity) entity).isAlive()
          || minecraft.player.distanceToSqr((Entity) itemEntity)
              > Double.longBitsToDouble(0x4090000000000000L)
          || (itemStack = itemEntity.getItem()).isEmpty()) continue;
      arrayList.add(
          new TaskOperationHandler.DroppedItem(
              BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
              itemStack.getCount(),
              itemEntity.getId(),
              itemEntity.getX(),
              itemEntity.getY(),
              itemEntity.getZ()));
    }
    return List.copyOf(arrayList);
  }

  @Override
  public TaskBlockPosition foliageInWay(double d, double d2, double d3) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null || minecraft.level == null) {
      return null;
    }
    for (double d4 :
        new double[] {
          Double.longBitsToDouble(4600877379321698714L),
          Double.longBitsToDouble(4609434218613702656L)
        }) {
      Vec3 vec3 =
          new Vec3(minecraft.player.getX(), minecraft.player.getY() + d4, minecraft.player.getZ());
      Vec3 vec32 = new Vec3(d, d2 + d4, d3);
      BlockHitResult blockHitResult =
          minecraft.level.clip(
              new ClipContext(
                  vec3,
                  vec32,
                  ClipContext.Block.COLLIDER,
                  ClipContext.Fluid.NONE,
                  (Entity) minecraft.player));
      BlockPos blockPos = blockHitResult.getBlockPos();
      if (blockHitResult.getType() != HitResult.Type.BLOCK
          || !minecraft.level.getBlockState(blockPos).is(BlockTags.LEAVES)
          || !CompatExecuteService.aim(minecraft, blockPos).isPresent()) continue;
      return new TaskBlockPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    return null;
  }

  @Override
  public TaskData sense() {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer localPlayer = minecraft.player;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    for (int i = 0; i < 36; ++i) {
      ItemStack itemStack = localPlayer.getInventory().getItem(i);
      if (itemStack.isEmpty()) continue;
      hashMap.merge(
          BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
          itemStack.getCount(),
          Integer::sum);
    }
    ItemStack itemStack = localPlayer.getOffhandItem();
    if (!itemStack.isEmpty()) {
      hashMap.merge(
          BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
          itemStack.getCount(),
          Integer::sum);
    }
    boolean bl = true;
    for (int i = 0; i < 36; ++i) {
      if (!localPlayer.getInventory().getItem(i).isEmpty()) continue;
      bl = false;
      break;
    }
    return new TaskData(
        new Vector3d(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ()),
        localPlayer.onGround(),
        localPlayer.isInWater(),
        localPlayer.getFoodData().getFoodLevel(),
        hashMap,
        bl,
        !localPlayer.isAlive(),
        CompatCurrentService.mhsr4rotzzqi(minecraft, (Player) localPlayer),
        localPlayer.getHealth(),
        localPlayer.getMaxHealth(),
        localPlayer.getAbsorptionAmount(),
        localPlayer.getFoodData().getSaturationLevel(),
        Math.max(0, localPlayer.getAirSupply()),
        localPlayer.getMaxAirSupply(),
        localPlayer.getRemainingFireTicks(),
        localPlayer.fallDistance,
        localPlayer.isEyeInFluid(FluidTags.WATER));
  }

  private static boolean mhsr4rotzzqi(Minecraft minecraft, Player player) {
    if (minecraft.level == null) {
      return false;
    }
    for (AbstractClientPlayer abstractClientPlayer : minecraft.level.players()) {
      if (abstractClientPlayer.getUUID().equals(player.getUUID())
          || !abstractClientPlayer.isAlive()
          || abstractClientPlayer.isSpectator()
          || !(abstractClientPlayer.distanceToSqr((Entity) player)
              < Double.longBitsToDouble(4639270566145032192L))) continue;
      return true;
    }
    return false;
  }

  public static boolean available() {
    return (CompatCurrentService.senseAvailable() && Minecraft.getInstance().screen == null ? 1 : 0)
        != 0;
  }

  public static boolean senseAvailable() {
    Minecraft minecraft = Minecraft.getInstance();
    return (minecraft.player != null
                && minecraft.level != null
                && minecraft.gameMode != null
                && minecraft.getConnection() != null
                && minecraft.player.isAlive()
                && !minecraft.player.isSpectator()
                && !minecraft.player.getAbilities().instabuild
                && !minecraft.player.isPassenger()
            ? 1
            : 0)
        != 0;
  }

  public static boolean handsBusy() {
    CombatOwnsService.Owner owner = CombatOwnsService.currentOwner().orElse(null);
    return (CompatReleaseTracker.reserved()
                || PearlThrowController.reserved()
                || SoupInventoryBridge.handBusy()
                || CompatActivateService.handBusy()
                || CompatOptionsTracker.handBusy()
                || owner != null && owner != CombatOwnsService.Owner.TRAVEL
            ? 1
            : 0)
        != 0;
  }

  static final class View implements TaskWorldView {
    View() {}

    @Override
    public boolean loaded(int n, int n2) {
      Minecraft minecraft = Minecraft.getInstance();
      return (minecraft.level != null
                  && minecraft.level.getChunk(
                          Math.floorDiv(n, 16), Math.floorDiv(n2, 16), ChunkStatus.FULL, false)
                      != null
              ? 1
              : 0)
          != 0;
    }

    @Override
    public String blockId(int n, int n2, int n3) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null || !this.loaded(n, n3)) {
        return "minecraft:air";
      }
      try {
        BlockState blockState = minecraft.level.getBlockState(new BlockPos(n, n2, n3));
        if (blockState.isAir()) {
          return "minecraft:air";
        }
        return BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString();
      } catch (Exception exception) {
        return "minecraft:air";
      }
    }

    @Override
    public Map<String, String> blockProps(int n, int n2, int n3) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null || !this.loaded(n, n3)) {
        return Map.of();
      }
      try {
        BlockState blockState = minecraft.level.getBlockState(new BlockPos(n, n2, n3));
        if (!(blockState.getBlock() instanceof CropBlock)) {
          return Map.of();
        }
        for (Property property : blockState.getProperties()) {
          if (!(property instanceof IntegerProperty)) continue;
          IntegerProperty integerProperty = (IntegerProperty) property;
          if (!property.getName().equals("age")) continue;
          return Map.of(
              "age", Integer.toString((Integer) blockState.getValue((Property) integerProperty)));
        }
        return Map.of();
      } catch (Exception exception) {
        return Map.of();
      }
    }

    @Override
    public int minY() {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.level == null ? -64 : minecraft.level.getMinY();
    }

    @Override
    public int maxY() {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.level == null ? 320 : minecraft.level.getMaxY();
    }
  }
}
