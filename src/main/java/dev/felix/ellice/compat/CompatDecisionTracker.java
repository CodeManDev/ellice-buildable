package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotDecisionTracker;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.friends.FriendsData;
import dev.felix.ellice.module.impl.ImplDetectionSettingsService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public final class CompatDecisionTracker {
  private static final AntibotDecisionTracker antibotDecisionTracker = new AntibotDecisionTracker();
  private static final Map<Integer, Identity> entries = new LinkedHashMap<Integer, Identity>();
  private static volatile ImplDetectionSettingsService values2;
  private static volatile Snapshot createSnapshot;
  private static ClientLevel client2;
  private static long timestamp;
  private static long timestamp2;

  private CompatDecisionTracker() {}

  public static void enable(ImplDetectionSettingsService implDetectionSettingsService) {
    CompatDecisionTracker.reset();
    values2 = implDetectionSettingsService;
    CompatDecisionTracker.tick(implDetectionSettingsService);
  }

  public static void disable(ImplDetectionSettingsService implDetectionSettingsService) {
    if (values2 == implDetectionSettingsService) {
      values2 = null;
      CompatDecisionTracker.reset();
    }
  }

  public static void reset() {
    antibotDecisionTracker.reset();
    entries.clear();
    client2 = null;
    timestamp2 = 0L;
    createSnapshot = Snapshot.m4twgknxncat();
  }

  public static void corrected() {
    antibotDecisionTracker.resetMotion();
    timestamp2 = CompatDecisionTracker.me2rtuvkbjiu() + 750L;
  }

  public static void tick(ImplDetectionSettingsService implDetectionSettingsService) {
    Object object2;
    if (values2 != implDetectionSettingsService) {
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null || minecraft.level == null || minecraft.getConnection() == null) {
      CompatDecisionTracker.reset();
      return;
    }
    if (client2 != minecraft.level) {
      CompatDecisionTracker.reset();
      client2 = minecraft.level;
    }
    if (CompatDecisionTracker.createRotationData7((Entity) minecraft.player) == null) {
      CompatDecisionTracker.reset();
      return;
    }
    long l = CompatDecisionTracker.me2rtuvkbjiu();
    entries
        .values()
        .removeIf(
            identity -> minecraft.level.getEntity(identity.key().entityId()) != identity.entity());
    HashSet<UUID> hashSet = new HashSet<UUID>();
    for (var playerInfo : minecraft.getConnection().getListedOnlinePlayers()) {
      hashSet.add(CompatComponent.identity(playerInfo.getProfile()).uuid());
    }
    List<AntibotDecisionTracker.Profile> list =
        minecraft.getConnection().getOnlinePlayers().stream()
            .map(
                playerInfo -> {
                  FriendsData friendsData = CompatComponent.identity(playerInfo.getProfile());
                  return new AntibotDecisionTracker.Profile(
                      friendsData.uuid(), friendsData.name(), hashSet.contains(friendsData.uuid()));
                })
            .toList();
    object2 = CompatEnableHandler.status();
    boolean bl =
        l >= timestamp2
            && CompatDecisionTracker.checkCondition((Player) minecraft.player)
            && !CompatEnableService.replaying();
    ArrayList<AntibotDecisionTracker.Player> arrayList =
        new ArrayList<AntibotDecisionTracker.Player>();
    minecraft.level.players().stream()
        .filter(
            abstractClientPlayer ->
                (abstractClientPlayer != minecraft.player && !abstractClientPlayer.isRemoved()
                        ? 1
                        : 0)
                    != 0)
        .sorted(
            Comparator.comparingDouble(
                arg_0 -> ((LocalPlayer) minecraft.player).distanceToSqr(arg_0)))
        .limit(512L)
        .forEach(
            arg_0 ->
                CompatDecisionTracker.mh06mpxfr8hg(
                    (CompatEnableHandler.Status) object2, arrayList, arg_0));
    Map<Integer, AntibotDecisionTracker.Decision> map =
        antibotDecisionTracker.update(
            new AntibotDecisionTracker.Frame(
                l,
                minecraft.player.getUUID(),
                CompatDecisionTracker.createRotationData7((Entity) minecraft.player),
                minecraft.player.getYRot(),
                bl,
                arrayList,
                list),
            implDetectionSettingsService.detectionSettings());
    if (entries.size() > 1024) {
      Iterator<Map.Entry<Integer, Identity>> iterator = entries.entrySet().iterator();
      while (entries.size() > 1024 && iterator.hasNext()) {
        if (map.containsKey(iterator.next().getKey())) continue;
        iterator.remove();
      }
    }
    createSnapshot = new Snapshot(client2, Map.copyOf(entries), map);
  }

  public static boolean excluded(AntibotFeatureType antibotFeatureType, Entity entity) {
    ImplDetectionSettingsService implDetectionSettingsService = values2;
    if (implDetectionSettingsService == null
        || !implDetectionSettingsService.isEnabled()
        || !implDetectionSettingsService.filters(antibotFeatureType)) {
      return false;
    }
    return CompatDecisionTracker.decision(entity)
        .map(decision -> antibotFeatureType.visual() ? decision.filtered() : decision.exclude())
        .orElse(false);
  }

  public static Optional<AntibotDecisionTracker.Decision> decision(Entity entity) {
    Snapshot snapshot = createSnapshot;
    Minecraft minecraft = Minecraft.getInstance();
    if (!(entity instanceof Player)
        || entity == minecraft.player
        || snapshot.world() != minecraft.level) {
      return Optional.empty();
    }
    Identity identity = snapshot.identities().get(entity.getId());
    if (identity == null
        || identity.entity() != entity
        || !identity.key().uuid().equals(entity.getUUID())) {
      return Optional.empty();
    }
    return Optional.ofNullable(snapshot.decisions().get(entity.getId()))
        .filter(decision -> decision.key().equals(identity.key()));
  }

  public static List<AntibotDecisionTracker.Decision> decisions() {
    return List.copyOf(createSnapshot.decisions().values());
  }

  private static boolean checkCondition(Player player) {
    return (!player.isPassenger()
                && !player.isFallFlying()
                && !player.isSwimming()
                && !player.getAbilities().flying
            ? 1
            : 0)
        != 0;
  }

  private static RotationVector createRotationData7(Entity entity) {
    double d = entity.getX();
    double d2 = entity.getY();
    double d3 = entity.getZ();
    if (!Double.isFinite(d)
        || !Double.isFinite(d2)
        || !Double.isFinite(d3)
        || Math.abs(d) > Double.longBitsToDouble(4741671816366391296L)
        || Math.abs(d2) > Double.longBitsToDouble(4741671816366391296L)
        || Math.abs(d3) > Double.longBitsToDouble(4741671816366391296L)) {
      return null;
    }
    return new RotationVector(d, d2, d3);
  }

  private static long me2rtuvkbjiu() {
    return System.nanoTime() / 1000000L;
  }

  private static void mh06mpxfr8hg(
      CompatEnableHandler.Status status,
      ArrayList arrayList,
      AbstractClientPlayer abstractClientPlayer) {
    Identity identity = entries.get(abstractClientPlayer.getId());
    if (identity == null
        || identity.entity() != abstractClientPlayer
        || !identity.key().uuid().equals(abstractClientPlayer.getUUID())) {
      identity =
          new Identity(
              (Player) abstractClientPlayer,
              new AntibotDecisionTracker.Key(
                  abstractClientPlayer.getId(), abstractClientPlayer.getUUID(), ++timestamp));
      entries.put(abstractClientPlayer.getId(), identity);
    }
    FriendsData friendsData = CompatComponent.identity(abstractClientPlayer.getGameProfile());
    AABB aABB = abstractClientPlayer.getBoundingBox();
    int n =
        Double.isFinite(aABB.minX)
                && Double.isFinite(aABB.minY)
                && Double.isFinite(aABB.minZ)
                && Double.isFinite(aABB.maxX)
                && Double.isFinite(aABB.maxY)
                && Double.isFinite(aABB.maxZ)
                && aABB.getXsize() > 0.0
                && aABB.getYsize() > 0.0
                && aABB.getZsize() > 0.0
            ? 1
            : 0;
    boolean bl =
        CompatDecisionTracker.checkCondition((Player) abstractClientPlayer)
            && (status.packets() <= 0 || !abstractClientPlayer.getUUID().equals(status.target()));
    arrayList.add(
        new AntibotDecisionTracker.Player(
            identity.key(),
            friendsData.name(),
            abstractClientPlayer.getDisplayName().getString(),
            CompatDecisionTracker.createRotationData7((Entity) abstractClientPlayer),
            abstractClientPlayer.getXRot(),
            abstractClientPlayer.getHealth(),
            abstractClientPlayer.isAlive(),
            n != 0,
            bl));
  }

  static {
    createSnapshot = Snapshot.m4twgknxncat();
  }

  private record Snapshot(
      ClientLevel world,
      Map<Integer, Identity> identities,
      Map<Integer, AntibotDecisionTracker.Decision> decisions) {
    private static Snapshot m4twgknxncat() {
      return new Snapshot(null, Map.of(), Map.of());
    }
  }

  private record Identity(Player entity, AntibotDecisionTracker.Key key) {}
}
