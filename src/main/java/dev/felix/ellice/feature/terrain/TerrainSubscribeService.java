package dev.felix.ellice.feature.terrain;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TerrainSubscribeService {
  private static volatile long timestamp;
  private static final CopyOnWriteArrayList<Consumer<TerrainData>> items =
      new CopyOnWriteArrayList<>();

  private TerrainSubscribeService() {}

  public static AutoCloseable subscribe(Consumer<TerrainData> consumer) {
    items.add(Objects.requireNonNull(consumer));
    return () -> items.remove(consumer);
  }

  public static long resourceEpoch() {
    return timestamp;
  }

  public static void reload() {
    timestamp++;
  }

  public static void dirty(int value, int currentValue, int nextValue) {
    TerrainData terrainData = new TerrainData(value, currentValue, nextValue);

    for (Consumer consumer : items) {
      consumer.accept(terrainData);
    }
  }
}
