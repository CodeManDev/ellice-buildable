package dev.felix.ellice.render.compositor;

public final class CompositorAmplitudeService {
  public static final int COUNT = 512;
  public static final CompositorAmplitudeService EMPTY =
      new CompositorAmplitudeService(new float[0]);
  private final float[] float2 = new float[512];

  public CompositorAmplitudeService(float[] floats) {
    if (floats != null && floats.length != 0) {
      for (int index = 0; index < 512; index++) {
        int currentLength = index * floats.length / 512;
        int nextLength = Math.max(currentLength + 1, (index + 1) * floats.length / 512);

        for (int currentIndex = currentLength;
            currentIndex < Math.min(nextLength, floats.length);
            currentIndex++) {
          float value = floats[currentIndex];
          if (Float.isFinite(value)) {
            this.float2[index] =
                Math.max(this.float2[index], Math.max(0.0F, Math.min(1.0F, value)));
          }
        }
      }
    }
  }

  public float amplitude(int index) {
    return this.float2[index];
  }
}
