











package dev.felix.ellice.compat;

import dev.felix.ellice.compat.CompatComponent;
import dev.felix.ellice.compat.CompatEnableHandler;
import dev.felix.ellice.compat.CompatEnableService;
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
    private static final AntibotDecisionTracker fcl6btsyn6q1 = new AntibotDecisionTracker();
    private static final Map<Integer, Identity> fj7dtvfp0wat = new LinkedHashMap<Integer, Identity>();
    private static volatile ImplDetectionSettingsService fih3ou3iyvyq;
    private static volatile Snapshot f5vgcm4svsjn;
    private static ClientLevel f45fpuf2ccsk;
    private static long f23kak27csnv;
    private static long ficw5valc7g3;

    private CompatDecisionTracker() {
    }

    public static void enable(ImplDetectionSettingsService implDetectionSettingsService) {
        CompatDecisionTracker.reset();
        fih3ou3iyvyq = implDetectionSettingsService;
        CompatDecisionTracker.tick(implDetectionSettingsService);
    }

    public static void disable(ImplDetectionSettingsService implDetectionSettingsService) {
        if (fih3ou3iyvyq == implDetectionSettingsService) {
            fih3ou3iyvyq = null;
            CompatDecisionTracker.reset();
        }
    }

    public static void reset() {
        fcl6btsyn6q1.reset();
        fj7dtvfp0wat.clear();
        f45fpuf2ccsk = null;
        ficw5valc7g3 = 0L;
        f5vgcm4svsjn = Snapshot.m4twgknxncat();
    }

    public static void corrected() {
        fcl6btsyn6q1.resetMotion();
        ficw5valc7g3 = CompatDecisionTracker.me2rtuvkbjiu() + 750L;
    }

    public static void tick(ImplDetectionSettingsService implDetectionSettingsService) {
        Object object2;
        if (fih3ou3iyvyq != implDetectionSettingsService) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.getConnection() == null) {
            CompatDecisionTracker.reset();
            return;
        }
        if (f45fpuf2ccsk != minecraft.level) {
            CompatDecisionTracker.reset();
            f45fpuf2ccsk = minecraft.level;
        }
        if (CompatDecisionTracker.m6asvps41pbu((Entity)minecraft.player) == null) {
            CompatDecisionTracker.reset();
            return;
        }
        long l = CompatDecisionTracker.me2rtuvkbjiu();
        fj7dtvfp0wat.values().removeIf(identity -> minecraft.level.getEntity(identity.key().entityId()) != identity.entity());
        HashSet<UUID> hashSet = new HashSet<UUID>();
        for (var playerInfo : minecraft.getConnection().getListedOnlinePlayers()) {
            hashSet.add(CompatComponent.identity(playerInfo.getProfile()).uuid());
        }
        List<AntibotDecisionTracker.Profile> list = minecraft.getConnection().getOnlinePlayers().stream().map(playerInfo -> {
            FriendsData friendsData = CompatComponent.identity(playerInfo.getProfile());
            return new AntibotDecisionTracker.Profile(friendsData.uuid(), friendsData.name(), hashSet.contains(friendsData.uuid()));
        }).toList();
        object2 = CompatEnableHandler.status();
        boolean bl = l >= ficw5valc7g3 && CompatDecisionTracker.mg3jk2d0henx((Player)minecraft.player) && !CompatEnableService.replaying();
        ArrayList<AntibotDecisionTracker.Player> arrayList = new ArrayList<AntibotDecisionTracker.Player>();
        minecraft.level.players().stream().filter(abstractClientPlayer -> (abstractClientPlayer != minecraft.player && !abstractClientPlayer.isRemoved() ? 1 : 0) != 0).sorted(Comparator.comparingDouble(arg_0 -> ((LocalPlayer)minecraft.player).distanceToSqr(arg_0))).limit(512L).forEach(arg_0 -> CompatDecisionTracker.mh06mpxfr8hg((CompatEnableHandler.Status)object2, arrayList, arg_0));
        Map<Integer, AntibotDecisionTracker.Decision> map = fcl6btsyn6q1.update(new AntibotDecisionTracker.Frame(l, minecraft.player.getUUID(), CompatDecisionTracker.m6asvps41pbu((Entity)minecraft.player), minecraft.player.getYRot(), bl, arrayList, list), implDetectionSettingsService.detectionSettings());
        if (fj7dtvfp0wat.size() > 1024) {
            Iterator<Map.Entry<Integer, Identity>> iterator = fj7dtvfp0wat.entrySet().iterator();
            while (fj7dtvfp0wat.size() > 1024 && iterator.hasNext()) {
                if (map.containsKey(iterator.next().getKey())) continue;
                iterator.remove();
            }
        }
        f5vgcm4svsjn = new Snapshot(f45fpuf2ccsk, Map.copyOf(fj7dtvfp0wat), map);
    }

    public static boolean excluded(AntibotFeatureType antibotFeatureType, Entity entity) {
        ImplDetectionSettingsService implDetectionSettingsService = fih3ou3iyvyq;
        if (implDetectionSettingsService == null || !implDetectionSettingsService.isEnabled() || !implDetectionSettingsService.filters(antibotFeatureType)) {
            return false;
        }
        return CompatDecisionTracker.decision(entity).map(decision -> antibotFeatureType.visual() ? decision.filtered() : decision.exclude()).orElse(false);
    }

    public static Optional<AntibotDecisionTracker.Decision> decision(Entity entity) {
        Snapshot snapshot = f5vgcm4svsjn;
        Minecraft minecraft = Minecraft.getInstance();
        if (!(entity instanceof Player) || entity == minecraft.player || snapshot.world() != minecraft.level) {
            return Optional.empty();
        }
        Identity identity = snapshot.identities().get(entity.getId());
        if (identity == null || identity.entity() != entity || !identity.key().uuid().equals(entity.getUUID())) {
            return Optional.empty();
        }
        return Optional.ofNullable(snapshot.decisions().get(entity.getId())).filter(decision -> decision.key().equals(identity.key()));
    }

    public static List<AntibotDecisionTracker.Decision> decisions() {
        return List.copyOf(f5vgcm4svsjn.decisions().values());
    }

    private static boolean mg3jk2d0henx(Player player) {
        return (!player.isPassenger() && !player.isFallFlying() && !player.isSwimming() && !player.getAbilities().flying ? 1 : 0) != 0;
    }

    private static RotationVector m6asvps41pbu(Entity entity) {
        double d = entity.getX();
        double d2 = entity.getY();
        double d3 = entity.getZ();
        if (!Double.isFinite(d) || !Double.isFinite(d2) || !Double.isFinite(d3) || Math.abs(d) > Double.longBitsToDouble(4741671816366391296L) || Math.abs(d2) > Double.longBitsToDouble(4741671816366391296L) || Math.abs(d3) > Double.longBitsToDouble(4741671816366391296L)) {
            return null;
        }
        return new RotationVector(d, d2, d3);
    }

    private static long me2rtuvkbjiu() {
        return System.nanoTime() / 1000000L;
    }

    private static  void mh06mpxfr8hg(CompatEnableHandler.Status status, ArrayList arrayList, AbstractClientPlayer abstractClientPlayer) {
        Identity identity = fj7dtvfp0wat.get(abstractClientPlayer.getId());
        if (identity == null || identity.entity() != abstractClientPlayer || !identity.key().uuid().equals(abstractClientPlayer.getUUID())) {
            identity = new Identity((Player)abstractClientPlayer, new AntibotDecisionTracker.Key(abstractClientPlayer.getId(), abstractClientPlayer.getUUID(), ++f23kak27csnv));
            fj7dtvfp0wat.put(abstractClientPlayer.getId(), identity);
        }
        FriendsData friendsData = CompatComponent.identity(abstractClientPlayer.getGameProfile());
        AABB aABB = abstractClientPlayer.getBoundingBox();
        int n = Double.isFinite(aABB.minX) && Double.isFinite(aABB.minY) && Double.isFinite(aABB.minZ) && Double.isFinite(aABB.maxX) && Double.isFinite(aABB.maxY) && Double.isFinite(aABB.maxZ) && aABB.getXsize() > 0.0 && aABB.getYsize() > 0.0 && aABB.getZsize() > 0.0 ? 1 : 0;
        boolean bl = CompatDecisionTracker.mg3jk2d0henx((Player)abstractClientPlayer) && (status.packets() <= 0 || !abstractClientPlayer.getUUID().equals(status.target()));
        arrayList.add(new AntibotDecisionTracker.Player(identity.key(), friendsData.name(), abstractClientPlayer.getDisplayName().getString(), CompatDecisionTracker.m6asvps41pbu((Entity)abstractClientPlayer), abstractClientPlayer.getXRot(), abstractClientPlayer.getHealth(), abstractClientPlayer.isAlive(), n != 0, bl));
    }

    static {
        f5vgcm4svsjn = Snapshot.m4twgknxncat();
    }

    private record Snapshot(ClientLevel world, Map<Integer, Identity> identities, Map<Integer, AntibotDecisionTracker.Decision> decisions) {
        private static Snapshot m4twgknxncat() {
            return new Snapshot(null, Map.of(), Map.of());
        }
    }

    private record Identity(Player entity, AntibotDecisionTracker.Key key) {
    }
}
