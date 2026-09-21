package dev.felix.ellice.feature.terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public record TerrainLayerData(TerrainData key, List<TerrainLayerData.Layer> layers) {
  public static final int STRIDE = 28;

  public TerrainLayerData(TerrainData key, List<TerrainLayerData.Layer> layers) {
    layers = List.copyOf(layers);
    this.key = key;
    this.layers = layers;
  }

  public long bytes() {
    return this.layers.stream().mapToLong(item -> item.vertices.length).sum();
  }

  public static TerrainLayerData.Layer copy(
      TerrainLayerData.Pass pass,
      ByteBuffer byteBuffer,
      int value,
      int currentValue,
      float nextValue) {
    if (value >= 28 && currentValue >= 0 && (long) currentValue * value <= byteBuffer.remaining()) {
      ByteBuffer currentByteBuffer = byteBuffer.duplicate();
      byte[] bytes = new byte[Math.multiplyExact(currentValue, 28)];
      int previousValue = currentByteBuffer.position();

      for (int index = 0; index < currentValue; index++) {
        currentByteBuffer.get(previousValue + index * value, bytes, index * 28, 28);
      }

      return new TerrainLayerData.Layer(pass, bytes, currentValue, nextValue);
    } else {
      throw new IllegalArgumentException("Unsupported terrain vertex layout");
    }
  }

  public static void appendColumn(TerrainLayerData.Layer layer, int value, ByteBuffer byteBuffer) {
    ByteBuffer currentByteBuffer = ByteBuffer.wrap(layer.vertices).order(ByteOrder.nativeOrder());
    int currentValue = byteBuffer.position();
    byteBuffer.put(layer.vertices);

    for (int index = 0; index < layer.vertexCount; index++) {
      int nextValue = index * 28 + 4;
      byteBuffer.putFloat(
          currentValue + nextValue, currentByteBuffer.getFloat(nextValue) + value * 16.0F);
    }
  }

  public record Layer(
      TerrainLayerData.Pass pass, byte[] vertices, int vertexCount, float alphaCutoff) {
    public Layer(TerrainLayerData.Pass pass, byte[] vertices, int vertexCount, float alphaCutoff) {
      if (pass != null
          && vertices != null
          && vertexCount >= 0
          && vertexCount % 4 == 0
          && vertices.length == vertexCount * 28L) {
        this.pass = pass;
        this.vertices = vertices;
        this.vertexCount = vertexCount;
        this.alphaCutoff = alphaCutoff;
      } else {
        throw new IllegalArgumentException("Invalid quad buffer");
      }
    }

    public int quadCount() {
      return this.vertexCount / 4;
    }
  }

  public enum Pass {
    SOLID,
    CUTOUT_MIPPED,
    CUTOUT,
    TRANSLUCENT;

    private static TerrainLayerData.Pass[] $values() {
      return new TerrainLayerData.Pass[] {SOLID, CUTOUT_MIPPED, CUTOUT, TRANSLUCENT};
    }
  }
}
