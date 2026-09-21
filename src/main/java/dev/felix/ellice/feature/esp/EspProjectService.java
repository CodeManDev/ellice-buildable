package dev.felix.ellice.feature.esp;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;

public final class EspProjectService {
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int TOP = 4;
  public static final int BOTTOM = 8;
  private static final float value = 1.0E-4f;
  private static final int[] ff4yl0ey2mhu;
  private final Matrix4f matrix4f;
  private final Vector4f[] fimzyq916ngk;
  private final double value2;
  private final double value3;
  private final double value4;
  private final float value5;
  private final float value6;
  private final boolean enabled;
  private float value7;
  private float value8;
  private float value9;
  private float value10;

  public EspProjectService(
      final Matrix4fc matrix4fc,
      final Matrix4fc matrix4fc2,
      final double n,
      final double n2,
      final double n3,
      final float n4,
      final float n5) {
    this.fimzyq916ngk = new Vector4f[8];
    this.matrix4f = new Matrix4f(matrix4fc2).mul(matrix4fc);
    this.value2 = n;
    this.value3 = n2;
    this.value4 = n3;
    this.value5 = n4;
    this.value6 = n5;
    this.enabled =
        (this.matrix4f.isFinite()
            && Double.isFinite(n)
            && Double.isFinite(n2)
            && Double.isFinite(n3)
            && Float.isFinite(n4)
            && Float.isFinite(n5)
            && n4 > 0.0f
            && n5 > 0.0f);
    for (int i = 0; i < this.fimzyq916ngk.length; ++i) {
      this.fimzyq916ngk[i] = new Vector4f();
    }
  }

  public Bounds project(final Box box) {
    if (!this.enabled
        || box == null
        || !box.valid()
        || box.contains(this.value2, this.value3, this.value4)) {
      return null;
    }
    for (int i = 0; i < 8; ++i) {
      final Vector4f set =
          this.fimzyq916ngk[i].set(
              (float) ((((i & 0x1) == 0x0) ? box.minX : box.maxX) - this.value2),
              (float) ((((i & 0x2) == 0x0) ? box.minY : box.maxY) - this.value3),
              (float) ((((i & 0x4) == 0x0) ? box.minZ : box.maxZ) - this.value4),
              1.0f);
      this.matrix4f.transform(set);
      if (!set.isFinite()) {
        return null;
      }
    }
    for (int j = 0; j < 6; ++j) {
      boolean b = true;
      final Vector4f[] fimzyq916ngk = this.fimzyq916ngk;
      for (int length = fimzyq916ngk.length, k = 0; k < length; ++k) {
        if (calculateValue(fimzyq916ngk[k], j) >= 0.0f) {
          b = false;
          break;
        }
      }
      if (b) {
        return null;
      }
    }
    final float intBitsToFloat = Float.intBitsToFloat(2139095040);
    this.value8 = intBitsToFloat;
    this.value7 = intBitsToFloat;
    final float intBitsToFloat2 = Float.intBitsToFloat(-8388608);
    this.value10 = intBitsToFloat2;
    this.value9 = intBitsToFloat2;
    for (int l = 0; l < EspProjectService.ff4yl0ey2mhu.length; l += 2) {
      this.updateState(
          this.fimzyq916ngk[EspProjectService.ff4yl0ey2mhu[l]],
          this.fimzyq916ngk[EspProjectService.ff4yl0ey2mhu[l + 1]]);
    }
    if (!Float.isFinite(this.value7) || this.value9 <= this.value7 || this.value10 <= this.value8) {
      return null;
    }
    final float n =
        (this.value7 * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608))
            * this.value5;
    final float n2 =
        (this.value9 * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608))
            * this.value5;
    final float n3 =
        (Float.intBitsToFloat(1056964608) - this.value10 * Float.intBitsToFloat(1056964608))
            * this.value6;
    final float n4 =
        (Float.intBitsToFloat(1056964608) - this.value8 * Float.intBitsToFloat(1056964608))
            * this.value6;
    if (!Float.isFinite(n)
        || !Float.isFinite(n2)
        || !Float.isFinite(n3)
        || !Float.isFinite(n4)
        || n2 <= 0.0f
        || n4 <= 0.0f
        || n >= this.value5
        || n3 >= this.value6) {
      return null;
    }
    final int n5 =
        ((n < 0.0f) ? 1 : 0)
            | ((n2 > this.value5) ? 2 : 0)
            | ((n3 < 0.0f) ? 4 : 0)
            | ((n4 > this.value6) ? 8 : 0);
    if (n5 == 15) {
      return null;
    }
    final float max = Math.max(0.0f, n);
    final float min = Math.min(this.value5, n2);
    final float max2 = Math.max(0.0f, n3);
    return new Bounds(max, max2, min - max, Math.min(this.value6, n4) - max2, n5);
  }

  private void updateState(final Vector4f vector4f, final Vector4f vector4f2) {
    float max = 0.0f;
    float min = 1.0f;
    for (int i = 4; i < 7; ++i) {
      final float calculateValue = calculateValue(vector4f, i);
      final float magl42hu35vg2 = calculateValue(vector4f2, i);
      if (calculateValue < 0.0f && magl42hu35vg2 < 0.0f) {
        return;
      }
      if (calculateValue < 0.0f) {
        max = Math.max(max, calculateValue / (calculateValue - magl42hu35vg2));
      } else if (magl42hu35vg2 < 0.0f) {
        min = Math.min(min, calculateValue / (calculateValue - magl42hu35vg2));
      }
      if (max > min) {
        return;
      }
    }
    this.updateState2(vector4f, vector4f2, max);
    this.updateState2(vector4f, vector4f2, min);
  }

  private void updateState2(final Vector4f vector4f, final Vector4f vector4f2, final float n) {
    final float n2 = vector4f.w + (vector4f2.w - vector4f.w) * n;
    if (n2 < Float.intBitsToFloat(944879383)) {
      return;
    }
    final float n3 = (vector4f.x + (vector4f2.x - vector4f.x) * n) / n2;
    final float n4 = (vector4f.y + (vector4f2.y - vector4f.y) * n) / n2;
    this.value7 = Math.min(this.value7, n3);
    this.value9 = Math.max(this.value9, n3);
    this.value8 = Math.min(this.value8, n4);
    this.value10 = Math.max(this.value10, n4);
  }

  private static float calculateValue(final Vector4f vector4f, final int n) {
    return switch (n) {
      case 0 -> vector4f.w + vector4f.x;
      case 1 -> vector4f.w - vector4f.x;
      case 2 -> vector4f.w + vector4f.y;
      case 3 -> vector4f.w - vector4f.y;
      case 4 -> vector4f.w + vector4f.z;
      case 5 -> vector4f.w - vector4f.z;
      default -> vector4f.w - Float.intBitsToFloat(953267991);
    };
  }

  static {
    ff4yl0ey2mhu =
        new int[] {0, 1, 2, 3, 4, 5, 6, 7, 0, 2, 1, 3, 4, 6, 5, 7, 0, 4, 1, 5, 2, 6, 3, 7};
  }

  public record Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    public Box move(final double n, final double n2, final double n3) {
      return new Box(
          this.minX + n,
          this.minY + n2,
          this.minZ + n3,
          this.maxX + n,
          this.maxY + n2,
          this.maxZ + n3);
    }

    boolean valid() {
      return Double.isFinite(this.minX)
          && Double.isFinite(this.minY)
          && Double.isFinite(this.minZ)
          && Double.isFinite(this.maxX)
          && Double.isFinite(this.maxY)
          && Double.isFinite(this.maxZ)
          && this.minX < this.maxX
          && this.minY < this.maxY
          && this.minZ < this.maxZ;
    }

    boolean contains(final double n, final double n2, final double n3) {
      return n >= this.minX
          && n <= this.maxX
          && n2 >= this.minY
          && n2 <= this.maxY
          && n3 >= this.minZ
          && n3 <= this.maxZ;
    }
  }

  public record Bounds(float x, float y, float width, float height, int clipped) {
    public float right() {
      return this.x + this.width;
    }

    public float bottom() {
      return this.y + this.height;
    }

    public boolean hasEdge(final int n) {
      return (this.clipped & n) == 0x0;
    }
  }
}
