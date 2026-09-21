







package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.feature.terrain.TerrainCloseService;
import dev.felix.ellice.feature.terrain.TerrainSunDirectionService;
import dev.felix.ellice.feature.terrain.TerrainViewportService;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.geometry.GeometryCubeService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

final class PlayerShadowRenderer
implements AutoCloseable {
    static final float BLOB_SIZE = 0.95f;
    static final float BLOB_LIFT = 0.045f;
    static final float BLOB_ALPHA = 0.38f;
    static final double FADE_START = 300.0;
    static final double FADE_END = 600.0;
    private final Render3dSceneService fabyqk9ige56;
    private final Render3dAddService f7kewe172wvl;
    private final GpuMesh fiiepyx5h8j3;
    private final ShaderDefinition f7wdpyl9tb2l = new ShaderDefinition("ellice:map-player-shadow", "render3d/pbr.vert", "terrain/player_shadow.frag");
    private final Map<UUID, Entry> fg4lzxp44qyy = new HashMap<UUID, Entry>();

    PlayerShadowRenderer(Render3dSceneService render3dSceneService, Render3dAddService render3dAddService) {
        this.fabyqk9ige56 = render3dSceneService;
        this.f7kewe172wvl = render3dAddService;
        this.fiiepyx5h8j3 = render3dSceneService.createMesh(GeometryCubeService.plane());
    }

    private Entry m5upzw5ywtbq() {
        MaterialErFactory materialErFactory = MaterialErFactory.builder("ellice:map-player-shadow-blob", this.f7wdpyl9tb2l).queue(MaterialErFactory.Queue.TRANSPARENT).build();
        return new Entry(this.f7kewe172wvl.add(this.fiiepyx5h8j3, materialErFactory, GeometryXService.IDENTITY), materialErFactory);
    }

    void update(List<CompatLoadedHandler.MapPlayer> list, Vector3f vector3f, TerrainViewportService.Frame frame, TerrainCloseService terrainCloseService, Map<UUID, Double> map) {
        float f = PlayerShadowRenderer.shadowStrength(vector3f, frame.distance());
        int n = f > Float.intBitsToFloat(981668463) ? 1 : 0;
        HashSet<UUID> hashSet = new HashSet<UUID>();
        for (CompatLoadedHandler.MapPlayer object : list) {
            hashSet.add(object.id());
            Entry entry = this.fg4lzxp44qyy.get(object.id());
            if (entry == null) {
                entry = this.m5upzw5ywtbq();
                this.fg4lzxp44qyy.put(object.id(), entry);
            }
            Vector3d vector3d = terrainCloseService == null ? null : terrainCloseService.smoothed(object.id());
            double d = vector3d == null ? object.x() : vector3d.x;
            double d2 = vector3d == null ? object.y() : vector3d.y;
            double d3 = vector3d == null ? object.z() : vector3d.z;
            double d4 = map == null || map.get(object.id()) == null ? d2 : map.get(object.id());
            double d5 = Math.max(0.0, d2 - d4);
            float f2 = TerrainSunDirectionService.shadowHeightScale(d5);
            float f3 = f * TerrainSunDirectionService.shadowHeightFade(d5);
            entry.blobMaterial.color("uColor", PlayerShadowRenderer.fadedBlack(Float.intBitsToFloat(1052938076), f3));
            boolean bl = n != 0 && f3 > Float.intBitsToFloat(981668463) && frame.project(d, d4 + Double.longBitsToDouble(4606281698874543309L), d3) != null;
            float f4 = (object.crouching() ? Float.intBitsToFloat(1062836634) : 1.0f) * f2;
            entry.blob.transform(new GeometryXService(d, d4 + Double.longBitsToDouble(4586646004756905984L), d3, new Quaternionf(), new Vector3f(Float.intBitsToFloat(0x3F733333) * f4, 1.0f, Float.intBitsToFloat(0x3F733333) * f4)));
            entry.blob.visible(bl);
        }
        for (UUID uUID2 : this.fg4lzxp44qyy.keySet().stream().filter(uUID -> !hashSet.contains(uUID)).toList()) {
            this.mea73hjhuyne(uUID2);
        }
    }

    void clear() {
        for (UUID uUID : this.fg4lzxp44qyy.keySet().stream().toList()) {
            this.mea73hjhuyne(uUID);
        }
    }

    private void mea73hjhuyne(UUID uUID) {
        Entry entry = this.fg4lzxp44qyy.remove(uUID);
        if (entry == null) {
            return;
        }
        this.f7kewe172wvl.remove(entry.blob);
    }

    static float shadowStrength(Vector3f vector3f, double d) {
        float f;
        float f2 = Float.intBitsToFloat(1062333317);
        if (vector3f != null && vector3f.isFinite() && (f = vector3f.length()) > Float.intBitsToFloat(897988541)) {
            f2 = vector3f.y / f;
        }
        f = Math.clamp(f2, Float.intBitsToFloat(1034147594), 1.0f);
        return Math.clamp(f * Float.intBitsToFloat(1069547520), Float.intBitsToFloat(1050253722), 1.0f) * PlayerShadowRenderer.distanceFade(d);
    }

    static float distanceFade(double d) {
        if (!Double.isFinite(d)) {
            return 1.0f;
        }
        if (d <= Double.longBitsToDouble(4643985272004935680L)) {
            return 1.0f;
        }
        if (d >= Double.longBitsToDouble(4648488871632306176L)) {
            return 0.0f;
        }
        return (float)((Double.longBitsToDouble(4648488871632306176L) - d) / Double.longBitsToDouble(4643985272004935680L));
    }

    static int fadedBlack(float f, float f2) {
        int n = Math.round(Math.clamp(f, 0.0f, 1.0f) * Math.clamp(f2, 0.0f, 1.0f) * Float.intBitsToFloat(1132396544));
        return Math.clamp((long)n, 0, 255) << 24;
    }

    @Override
    public void close() {
        this.clear();
        this.fabyqk9ige56.destroyMesh(this.fiiepyx5h8j3);
    }

    private static final class Entry {
        final Render3dIdService blob;
        final MaterialErFactory blobMaterial;

        Entry(Render3dIdService render3dIdService, MaterialErFactory materialErFactory) {
            this.blob = render3dIdService;
            this.blobMaterial = materialErFactory;
        }
    }
}

