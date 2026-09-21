package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.joml.Vector3d;

public final class TerrainAddService {
  public static final double DURATION = 20.0;
  public static final int FPS = 24;
  public static final long BUDGET = 536870912L;
  private final List<TerrainAddService.Sample> items = new ArrayList<>();
  private final Set<Object> values2 = Collections.newSetFromMap(new IdentityHashMap<>());
  private long timestamp;
  private double value;
  private boolean enabled;
  private final double value2;

  public TerrainAddService() {
    this(20.0);
  }

  public TerrainAddService(double doubleValue) {
    if (Double.isFinite(doubleValue) && !(doubleValue < 1.0) && !(doubleValue > 60.0)) {
      this.value2 = doubleValue;
    } else {
      throw new IllegalArgumentException("Recording duration must be between 1 and 60 seconds");
    }
  }

  public double maximumDuration() {
    return this.value2;
  }

  public void add(TerrainAddService.Sample sample) {
    if (this.enabled) {
      throw new IllegalStateException("Take is finished");
    }

    if (Double.isFinite(sample.seconds())
        && !(sample.seconds() < 0.0)
        && !(sample.seconds() > this.value2)
        && sample.player().isFinite()
        && (this.items.isEmpty() || !(sample.seconds() <= this.items.getLast().seconds()))) {
      long currentSize = 256L + sample.terrain().size() * 48L + sample.players().size() * 256L;
      Set currentValues = Collections.newSetFromMap(new IdentityHashMap());

      for (TerrainLayerData terrainLayerData : sample.terrain().values()) {
        if (!this.values2.contains(terrainLayerData) && currentValues.add(terrainLayerData)) {
          currentSize += terrainLayerData.bytes();
        }
      }

      for (CompatLoadedHandler.MapPlayer mapPlayer : sample.players()) {
        for (CompatLoadedHandler.PlayerPart playerPart : mapPlayer.model()) {
          GeometryVertexCountService geometryVertexCount = playerPart.mesh();
          if (!this.values2.contains(geometryVertexCount)
              && currentValues.add(geometryVertexCount)) {
            currentSize +=
                (long) geometryVertexCount.vertexCount() * geometryVertexCount.vertexStrideBytes()
                    + geometryVertexCount.indexCount() * 4L;
          }
        }
      }

      if (this.timestamp + currentSize > 536870912L) {
        throw new IllegalStateException("3D recording memory limit reached (512 MiB)");
      }

      this.timestamp += currentSize;
      this.values2.addAll(currentValues);
      this.items.add(sample);
    } else {
      throw new IllegalArgumentException("Invalid recording time or position");
    }
  }

  public void finish(double doubleValue) {
    if (this.items.isEmpty()) {
      throw new IllegalStateException("No recorded frames");
    }

    HashMap hashMap = new HashMap();
    HashMap currentHashMap = new HashMap();

    for (TerrainAddService.Sample sample : this.items) {
      sample
          .terrain()
          .forEach(
              (item, currentItem) -> {
                hashMap.putIfAbsent(item, currentItem);
                currentHashMap.putIfAbsent(item, sample.seconds());
              });
    }

    for (int index = 0; index < this.items.size(); index++) {
      TerrainAddService.Sample currentSample = this.items.get(index);
      HashMap nextHashMap = new HashMap<>(currentSample.terrain());
      hashMap.forEach(
          (item, currentItem) -> {
            if (currentSample.seconds() < (Double) currentHashMap.get(item)) {
              nextHashMap.put(item, currentItem);
            }
          });
      this.items.set(
          index,
          new TerrainAddService.Sample(
              currentSample.seconds(),
              currentSample.player(),
              currentSample.players(),
              nextHashMap,
              currentSample.sky(),
              currentSample.ground()));
    }

    this.value =
        Math.clamp(
            doubleValue,
            Math.max(0.041666666666666664, this.items.getLast().seconds()),
            this.value2);
    this.enabled = true;
  }

  public boolean finished() {
    return this.enabled;
  }

  public double duration() {
    return this.value;
  }

  public int frameCount() {
    return (int) Math.ceil(this.value * 24.0);
  }

  public long bytes() {
    return this.timestamp;
  }

  public int size() {
    return this.items.size();
  }

  public List<TerrainAddService.Sample> samples() {
    return Collections.unmodifiableList(this.items);
  }

  public TerrainAddService.Sample at(double doubleValue) {
    if (this.items.isEmpty()) {
      throw new IllegalStateException("Empty take");
    }

    int value = 0;
    int currentSize = this.items.size() - 1;

    while (value < currentSize) {
      int currentValue = value + currentSize + 1 >>> 1;
      if (this.items.get(currentValue).seconds() <= doubleValue) {
        value = currentValue;
      } else {
        currentSize = currentValue - 1;
      }
    }

    TerrainAddService.Sample sample = this.items.get(value);
    TerrainAddService.Sample nextSize = this.items.get(Math.min(value + 1, this.items.size() - 1));
    if (sample != nextSize && !(doubleValue <= sample.seconds())) {
      double currentDoubleValue =
          Math.clamp(
              (doubleValue - sample.seconds()) / (nextSize.seconds() - sample.seconds()), 0.0, 1.0);
      HashMap hashMap = new HashMap();
      nextSize.players().forEach(item -> hashMap.put(item.id(), item));
      ArrayList arrayList = new ArrayList();

      for (CompatLoadedHandler.MapPlayer mapPlayer : sample.players()) {
        CompatLoadedHandler.MapPlayer currentMapPlayer =
            (CompatLoadedHandler.MapPlayer) hashMap.get(mapPlayer.id());
        if (currentMapPlayer != null
            && !(new Vector3d(mapPlayer.x(), mapPlayer.y(), mapPlayer.z())
                    .distanceSquared(
                        currentMapPlayer.x(), currentMapPlayer.y(), currentMapPlayer.z())
                > 64.0)) {
          arrayList.add(
              new CompatLoadedHandler.MapPlayer(
                  mapPlayer.id(),
                  mapPlayer.name(),
                  calculateValue(mapPlayer.x(), currentMapPlayer.x(), currentDoubleValue),
                  calculateValue(mapPlayer.y(), currentMapPlayer.y(), currentDoubleValue),
                  calculateValue(mapPlayer.z(), currentMapPlayer.z(), currentDoubleValue),
                  (float)
                      (mapPlayer.yaw()
                          + Math.IEEEremainder(currentMapPlayer.yaw() - mapPlayer.yaw(), 360.0)
                              * currentDoubleValue),
                  (float)
                      calculateValue(
                          mapPlayer.pitch(), currentMapPlayer.pitch(), currentDoubleValue),
                  mapPlayer.slim(),
                  mapPlayer.skinGlId(),
                  mapPlayer.legacySkin(),
                  mapPlayer.health(),
                  mapPlayer.maxHealth(),
                  mapPlayer.crouching(),
                  mapPlayer.model(),
                  mapPlayer.blockLight(),
                  mapPlayer.skyLight()));
        } else {
          arrayList.add(mapPlayer);
        }
      }

      return new TerrainAddService.Sample(
          doubleValue,
          new Vector3d(sample.player()).lerp(nextSize.player(), currentDoubleValue),
          arrayList,
          sample.terrain(),
          sample.sky(),
          sample.ground());
    } else {
      return sample;
    }
  }

  private static double calculateValue(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return doubleValue + (currentDoubleValue - doubleValue) * nextDoubleValue;
  }

  public record Sample(
      double seconds,
      Vector3d player,
      List<CompatLoadedHandler.MapPlayer> players,
      Map<TerrainData, TerrainLayerData> terrain,
      CompatLoadedHandler.SkyState sky,
      Map<UUID, Double> ground) {
    public Sample(
        double seconds,
        Vector3d player,
        List<CompatLoadedHandler.MapPlayer> players,
        Map<TerrainData, TerrainLayerData> terrain,
        CompatLoadedHandler.SkyState sky,
        Map<UUID, Double> ground) {
      player = new Vector3d(player);
      players = List.copyOf(players);
      terrain = Map.copyOf(terrain);
      ground = Map.copyOf(ground);
      this.seconds = seconds;
      this.player = player;
      this.players = players;
      this.terrain = terrain;
      this.sky = sky;
      this.ground = ground;
    }
  }
}
