package dev.felix.ellice.feature.terrain;

public record TerrainData(int x, int y, int z) {
  public long column() {
    return (long) this.x << 32 | this.z & 4294967295L;
  }

  public int blockX() {
    return this.x * 16;
  }

  public int blockY() {
    return this.y * 16;
  }

  public int blockZ() {
    return this.z * 16;
  }

  public static int columnX(long longValue) {
    return (int) (longValue >> 32);
  }

  public static int columnZ(long longValue) {
    return (int) longValue;
  }
}
