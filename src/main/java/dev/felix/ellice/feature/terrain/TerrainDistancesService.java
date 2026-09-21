package dev.felix.ellice.feature.terrain;

import java.util.Set;

public final class TerrainDistancesService {
  public static final int SIZE = 65;
  public static final float DISTANCE_BLOCKS = 64.0F;

  private TerrainDistancesService() {}

  public static byte[] distances(Set<Long> values, int value, int currentValue) {
    float[] floats = new float[4225];

    for (int index = 0; index < 65; index++) {
      for (int currentIndex = 0; currentIndex < 65; currentIndex++) {
        long longValue = new TerrainData(value + currentIndex, 0, currentValue + index).column();
        floats[index * 65 + currentIndex] =
            currentIndex != 0
                    && index != 0
                    && currentIndex != 64
                    && index != 64
                    && values.contains(longValue)
                ? 65.0F
                : 0.0F;
      }
    }

    for (int nextIndex = 0; nextIndex < 65; nextIndex++) {
      for (int previousIndex = 0; previousIndex < 65; previousIndex++) {
        int sourceIndex = nextIndex * 65 + previousIndex;
        if (previousIndex > 0) {
          floats[sourceIndex] = Math.min(floats[sourceIndex], floats[sourceIndex - 1] + 1.0F);
        }

        if (nextIndex > 0) {
          floats[sourceIndex] = Math.min(floats[sourceIndex], floats[sourceIndex - 65] + 1.0F);
        }

        if (previousIndex > 0 && nextIndex > 0) {
          floats[sourceIndex] =
              Math.min(floats[sourceIndex], floats[sourceIndex - 65 - 1] + 1.414214F);
        }

        if (previousIndex < 64 && nextIndex > 0) {
          floats[sourceIndex] =
              Math.min(floats[sourceIndex], floats[sourceIndex - 65 + 1] + 1.414214F);
        }
      }
    }

    for (byte byteValue = 64; byteValue >= 0; byteValue += -1) {
      for (byte currentByteValue = 64; currentByteValue >= 0; currentByteValue += -1) {
        int targetIndex = byteValue * 65 + currentByteValue;
        if (currentByteValue < 64) {
          floats[targetIndex] = Math.min(floats[targetIndex], floats[targetIndex + 1] + 1.0F);
        }

        if (byteValue < 64) {
          floats[targetIndex] = Math.min(floats[targetIndex], floats[targetIndex + 65] + 1.0F);
        }

        if (currentByteValue < 64 && byteValue < 64) {
          floats[targetIndex] =
              Math.min(floats[targetIndex], floats[targetIndex + 65 + 1] + 1.414214F);
        }

        if (currentByteValue > 0 && byteValue < 64) {
          floats[targetIndex] =
              Math.min(floats[targetIndex], floats[targetIndex + 65 - 1] + 1.414214F);
        }
      }
    }

    byte[] bytes = new byte[4225];

    for (int inputIndex = 0; inputIndex < bytes.length; inputIndex++) {
      bytes[inputIndex] =
          (byte)
              Math.round(
                  Math.clamp((floats[inputIndex] - 0.5F) * 16.0F / 64.0F, 0.0F, 1.0F) * 255.0F);
    }

    return bytes;
  }

  public static byte[] rgbaDistances(Set<Long> values, int value, int currentValue) {
    byte[] bytes = distances(values, value, currentValue);
    byte[] currentBytes = new byte[16900];

    for (int index = 0; index < 65; index++) {
      for (int currentIndex = 0; currentIndex < 65; currentIndex++) {
        int nextIndex = index * 65 + currentIndex;
        byte byteValue = bytes[nextIndex];
        currentBytes[nextIndex * 4] = byteValue;
        currentBytes[nextIndex * 4 + 1] = byteValue;
        currentBytes[nextIndex * 4 + 2] = byteValue;
        int nextValue = currentIndex > 0 && index > 0 && currentIndex < 64 && index < 64 ? 1 : 0;
        int previousValue =
            nextValue != 0
                    && values.contains(
                        new TerrainData(value + currentIndex, 0, currentValue + index).column())
                ? 1
                : 0;
        currentBytes[nextIndex * 4 + 3] = (byte) (previousValue != 0 ? 255 : 0);
      }
    }

    return currentBytes;
  }
}
