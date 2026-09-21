package dev.felix.ellice.feature.rotation;

import java.util.Objects;

public final class RotationNextService {
  private final RotationVanillaGcdService rotationVanillaGcdService =
      new RotationVanillaGcdService();
  private double value;
  private double value2;

  public Step next(
      RotationData rotationData, RotationData rotationData2, double d, double d2, double d3) {
    boolean bl;
    double d4;
    Objects.requireNonNull(rotationData, "current");
    Objects.requireNonNull(rotationData2, "camera");
    if (!Double.isFinite(d2) || d2 <= 0.0 || !Double.isFinite(d3) || d3 < 0.0 || d3 > 1.0) {
      throw new IllegalArgumentException(
          "Positive finite turn speed and smoothness in [0, 1] required");
    }
    double d5 = this.rotationVanillaGcdService.vanillaGcd(d);
    RotationData rotationData3 =
        new RotationData((float) rotationData.yaw(), (float) rotationData.pitch());
    RotationData rotationData4 =
        new RotationData(
            (float) rotationData2.yaw(), (float) RotationData.clampPitch(rotationData2.pitch()));
    double d6 = RotationData.yawDelta(rotationData3.yaw(), rotationData4.yaw());
    if (Math.hypot(d6, d4 = rotationData4.pitch() - rotationData3.pitch()) <= d5) {
      this.reset();
      return new Step(rotationData3, true);
    }
    double d7 =
        Double.longBitsToDouble(0x3FF3333333333333L)
            - Double.longBitsToDouble(0x3FE3333333333333L) * d3;
    double d8 = Math.exp(-d7);
    double d9 = 1.0 - (1.0 + d7) * d8;
    double d10 = d6 * d9 + this.value * d8;
    double d11 = d4 * d9 + this.value2 * d8;
    this.value = (this.value * (1.0 - d7) + d7 * d7 * d6) * d8;
    this.value2 = (this.value2 * (1.0 - d7) + d7 * d7 * d4) * d8;
    double d12 = RotationNextService.calculateValue(d10, d6);
    double d13 = RotationNextService.calculateValue(d11, d4);
    if (d12 != d10) {
      this.value = 0.0;
    }
    if (d13 != d11) {
      this.value2 = 0.0;
    }
    double d14 = Math.min(1.0, d2 / Math.hypot(d12, d13));
    double d15 = d12 * d14;
    double d16 = d13 * d14;
    RotationData rotationData5 =
        new RotationData(rotationData3.yaw() + d15, rotationData3.pitch() + d16);
    double d17 = Math.min(1.0, d2 / Math.hypot(this.value, this.value2));
    this.value *= d17;
    this.value2 *= d17;
    RotationData rotationData6 =
        this.rotationVanillaGcdService.quantize(rotationData3, rotationData5, d, 0.0).rotation();
    if (RotationData.distance(rotationData3, rotationData6) == 0.0) {
      bl = Math.abs(d6) >= Math.abs(d4);
      d15 = bl ? Math.copySign(d5, d6) : 0.0;
      d16 = bl ? 0.0 : Math.copySign(d5, d4);
      rotationData6 =
          this.rotationVanillaGcdService
              .quantize(
                  rotationData3,
                  new RotationData(rotationData3.yaw() + d15, rotationData3.pitch() + d16),
                  d,
                  0.0)
              .rotation();
    }
    if (RotationData.distance(rotationData3, rotationData6) > d2) {
      double d18 = 0.0;
      double d19 = 1.0;
      rotationData6 = rotationData3;
      for (int i = 0; i < 32; ++i) {
        double d20 = (d18 + d19) * Double.longBitsToDouble(4602678819172646912L);
        RotationData rotationData7 =
            this.rotationVanillaGcdService
                .quantize(
                    rotationData3,
                    new RotationData(
                        rotationData3.yaw() + d15 * d20, rotationData3.pitch() + d16 * d20),
                    d,
                    0.0)
                .rotation();
        if (RotationData.distance(rotationData3, rotationData7) <= d2) {
          rotationData6 = rotationData7;
          d18 = d20;
          continue;
        }
        d19 = d20;
      }
    }
    boolean bl2 = bl = RotationData.distance(rotationData6, rotationData4) <= d5;
    if (bl) {
      this.reset();
    }
    return new Step(rotationData6, bl);
  }

  public void reset() {
    this.value2 = 0.0;
    this.value = 0.0;
  }

  private static double calculateValue(double d, double d2) {
    return d2 >= 0.0 ? Math.max(0.0, Math.min(d2, d)) : Math.min(0.0, Math.max(d2, d));
  }

  public record Step(RotationData rotation, boolean settled) {}
}
