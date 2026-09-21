package dev.felix.ellice.feature.cape;

import java.awt.image.BufferedImage;
import java.util.List;

public final class CapeSizeService {
  private final List<BufferedImage> items;
  private final long[] long2;
  private final long timestamp;

  public CapeSizeService(List<BufferedImage> currentItems, List<Integer> nextItems) {
    if (!currentItems.isEmpty() && currentItems.size() == nextItems.size()) {
      this.items = List.copyOf(currentItems);
      this.long2 = new long[currentItems.size()];
      long longValue = 0L;
      int width = ((BufferedImage) currentItems.getFirst()).getWidth();
      int height = ((BufferedImage) currentItems.getFirst()).getHeight();

      for (int index = 0; index < this.long2.length; index++) {
        if (((BufferedImage) currentItems.get(index)).getWidth() != width
            || ((BufferedImage) currentItems.get(index)).getHeight() != height
            || (Integer) nextItems.get(index) < 1) {
          throw new IllegalArgumentException("Inconsistent frame");
        }

        this.long2[index] = longValue += ((Integer) nextItems.get(index)).intValue();
      }

      this.timestamp = longValue;
    } else {
      throw new IllegalArgumentException("Invalid animation");
    }
  }

  public int size() {
    return this.items.size();
  }

  public long duration() {
    return this.timestamp;
  }

  public BufferedImage frame(int value) {
    return this.items.get(value);
  }

  public int frameAt(double doubleValue) {
    if (!Double.isFinite(doubleValue)) {
      return 0;
    }

    long longValue = Math.floorMod((long) doubleValue, this.timestamp);
    int value = 0;
    int currentLength = this.long2.length - 1;

    while (value < currentLength) {
      int index = value + currentLength >>> 1;
      if (longValue < this.long2[index]) {
        currentLength = index;
      } else {
        value = index + 1;
      }
    }

    return value;
  }
}
