package dev.felix.ellice.ui.svg;

import java.util.ArrayList;
import java.util.List;

public final class SvgCodec {
  private final List<SubPath> path;
  private final float fao5tydtqofx;
  private final float feh0ufgmilmg;
  private static final float value3 = 0.15f;

  public SvgCodec(List<SubPath> list, float f, float f2) {
    this.path = List.copyOf(list);
    this.fao5tydtqofx = f;
    this.feh0ufgmilmg = f2;
  }

  public List<SubPath> subPaths() {
    return this.path;
  }

  public float viewBoxW() {
    return this.fao5tydtqofx;
  }

  public float viewBoxH() {
    return this.feh0ufgmilmg;
  }

  public SvgCodec transform(float[] fArray) {
    if (fArray[0] == 1.0f
        && fArray[1] == 0.0f
        && fArray[2] == 0.0f
        && fArray[3] == 1.0f
        && fArray[4] == 0.0f
        && fArray[5] == 0.0f) {
      return this;
    }
    ArrayList<SubPath> arrayList = new ArrayList<SubPath>(this.path.size());
    for (SubPath subPath : this.path) {
      float[] fArray2 = new float[subPath.points().length];
      for (int i = 0; i < subPath.vertexCount(); ++i) {
        float f = subPath.x(i);
        float f2 = subPath.y(i);
        fArray2[i * 2] = fArray[0] * f + fArray[2] * f2 + fArray[4];
        fArray2[i * 2 + 1] = fArray[1] * f + fArray[3] * f2 + fArray[5];
      }
      arrayList.add(new SubPath(fArray2, subPath.closed()));
    }
    return new SvgCodec(arrayList, this.fao5tydtqofx, this.feh0ufgmilmg);
  }

  public static SvgCodec parse(String string, float f, float f2) {
    ArrayList<SubPath> arrayList = new ArrayList<SubPath>();
    ArrayList<Float> arrayList2 = new ArrayList<Float>();
    float f3 = 0.0f;
    float f4 = 0.0f;
    float f5 = 0.0f;
    float f6 = 0.0f;
    float f7 = 0.0f;
    float f8 = 0.0f;
    float f9 = 0.0f;
    float f10 = 0.0f;
    int n = 0;
    int[] nArray = new int[] {0};
    while (nArray[0] < string.length()) {
      nArray[0] = SvgCodec.mixrfpyhjm8y(string, nArray[0]);
      if (nArray[0] >= string.length()) break;
      int n2 = string.charAt(nArray[0]);
      if (Character.isLetter((char) n2) && n2 != 101 && n2 != 69) {
        n = n2;
        nArray[0] = nArray[0] + 1;
      } else {
        n2 = n;
      }
      int n3 = 0;
      int n4 = 0;
      switch (n2) {
        case 77:
          {
            if (!arrayList2.isEmpty()) {
              arrayList.add(SvgCodec.mfosr0czatrc(arrayList2, false));
              arrayList2.clear();
            }
            f3 = f5 = SvgCodec.readFloat(string, nArray);
            f4 = f6 = SvgCodec.readFloat(string, nArray);
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4));
            n = 76;
            break;
          }
        case 109:
          {
            if (!arrayList2.isEmpty()) {
              arrayList.add(SvgCodec.mfosr0czatrc(arrayList2, false));
              arrayList2.clear();
            }
            f3 = f5 = f3 + SvgCodec.readFloat(string, nArray);
            f4 = f6 = f4 + SvgCodec.readFloat(string, nArray);
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4));
            n = 108;
            break;
          }
        case 76:
          {
            f3 = SvgCodec.readFloat(string, nArray);
            f4 = SvgCodec.readFloat(string, nArray);
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4));
            break;
          }
        case 108:
          {
            arrayList2.add(Float.valueOf(f3 += SvgCodec.readFloat(string, nArray)));
            arrayList2.add(Float.valueOf(f4 += SvgCodec.readFloat(string, nArray)));
            break;
          }
        case 72:
          {
            f3 = SvgCodec.readFloat(string, nArray);
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4));
            break;
          }
        case 104:
          {
            arrayList2.add(Float.valueOf(f3 += SvgCodec.readFloat(string, nArray)));
            arrayList2.add(Float.valueOf(f4));
            break;
          }
        case 86:
          {
            f4 = SvgCodec.readFloat(string, nArray);
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4));
            break;
          }
        case 118:
          {
            arrayList2.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(f4 += SvgCodec.readFloat(string, nArray)));
            break;
          }
        case 67:
          {
            float f11 = SvgCodec.readFloat(string, nArray);
            float f12 = SvgCodec.readFloat(string, nArray);
            float f13 = SvgCodec.readFloat(string, nArray);
            float f14 = SvgCodec.readFloat(string, nArray);
            float f15 = SvgCodec.readFloat(string, nArray);
            float f16 = SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState2(arrayList2, f3, f4, f11, f12, f13, f14, f15, f16);
            f7 = f13;
            f8 = f14;
            f3 = f15;
            f4 = f16;
            n3 = 1;
            break;
          }
        case 99:
          {
            float f17 = f3 + SvgCodec.readFloat(string, nArray);
            float f12 = f4 + SvgCodec.readFloat(string, nArray);
            float f13 = f3 + SvgCodec.readFloat(string, nArray);
            float f14 = f4 + SvgCodec.readFloat(string, nArray);
            float f18 = f3 + SvgCodec.readFloat(string, nArray);
            float f19 = f4 + SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState2(arrayList2, f3, f4, f17, f12, f13, f14, f18, f19);
            f7 = f13;
            f8 = f14;
            f3 = f18;
            f4 = f19;
            n3 = 1;
            break;
          }
        case 83:
          {
            float f20 = 2.0f * f3 - f7;
            float f12 = 2.0f * f4 - f8;
            float f13 = SvgCodec.readFloat(string, nArray);
            float f14 = SvgCodec.readFloat(string, nArray);
            float f21 = SvgCodec.readFloat(string, nArray);
            float f22 = SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState2(arrayList2, f3, f4, f20, f12, f13, f14, f21, f22);
            f7 = f13;
            f8 = f14;
            f3 = f21;
            f4 = f22;
            n3 = 1;
            break;
          }
        case 115:
          {
            float f23 = 2.0f * f3 - f7;
            float f12 = 2.0f * f4 - f8;
            float f13 = f3 + SvgCodec.readFloat(string, nArray);
            float f14 = f4 + SvgCodec.readFloat(string, nArray);
            float f24 = f3 + SvgCodec.readFloat(string, nArray);
            float f25 = f4 + SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState2(arrayList2, f3, f4, f23, f12, f13, f14, f24, f25);
            f7 = f13;
            f8 = f14;
            f3 = f24;
            f4 = f25;
            n3 = 1;
            break;
          }
        case 81:
          {
            float f26 = SvgCodec.readFloat(string, nArray);
            float f12 = SvgCodec.readFloat(string, nArray);
            float f13 = SvgCodec.readFloat(string, nArray);
            float f14 = SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState3(arrayList2, f3, f4, f26, f12, f13, f14);
            f9 = f26;
            f10 = f12;
            f3 = f13;
            f4 = f14;
            n4 = 1;
            break;
          }
        case 113:
          {
            float f27 = f3 + SvgCodec.readFloat(string, nArray);
            float f12 = f4 + SvgCodec.readFloat(string, nArray);
            float f13 = f3 + SvgCodec.readFloat(string, nArray);
            float f14 = f4 + SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState3(arrayList2, f3, f4, f27, f12, f13, f14);
            f9 = f27;
            f10 = f12;
            f3 = f13;
            f4 = f14;
            n4 = 1;
            break;
          }
        case 84:
          {
            float f28 = 2.0f * f3 - f9;
            float f12 = 2.0f * f4 - f10;
            float f13 = SvgCodec.readFloat(string, nArray);
            float f14 = SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState3(arrayList2, f3, f4, f28, f12, f13, f14);
            f9 = f28;
            f10 = f12;
            f3 = f13;
            f4 = f14;
            n4 = 1;
            break;
          }
        case 116:
          {
            float f29 = 2.0f * f3 - f9;
            float f12 = 2.0f * f4 - f10;
            float f13 = f3 + SvgCodec.readFloat(string, nArray);
            float f14 = f4 + SvgCodec.readFloat(string, nArray);
            SvgCodec.updateState3(arrayList2, f3, f4, f29, f12, f13, f14);
            f9 = f29;
            f10 = f12;
            f3 = f13;
            f4 = f14;
            n4 = 1;
            break;
          }
        case 65:
        case 97:
          {
            int n5 = n2 == 97 ? 1 : 0;
            float f12 = Math.abs(SvgCodec.readFloat(string, nArray));
            float f13 = Math.abs(SvgCodec.readFloat(string, nArray));
            float f14 = (float) Math.toRadians(SvgCodec.readFloat(string, nArray));
            boolean bl = SvgCodec.checkCondition(string, nArray);
            boolean bl2 = SvgCodec.checkCondition(string, nArray);
            float f30 = SvgCodec.readFloat(string, nArray);
            float f31 = SvgCodec.readFloat(string, nArray);
            if (n5 != 0) {
              f30 += f3;
              f31 += f4;
            }
            SvgCodec.updateState(arrayList2, f3, f4, f30, f31, f12, f13, f14, bl, bl2);
            f3 = f30;
            f4 = f31;
            break;
          }
        case 90:
        case 122:
          {
            if (!arrayList2.isEmpty()) {
              arrayList.add(SvgCodec.mfosr0czatrc(arrayList2, true));
              arrayList2.clear();
            }
            f3 = f5;
            f4 = f6;
            break;
          }
        default:
          {
            nArray[0] = nArray[0] + 1;
          }
      }
      if (n3 == 0) {
        f7 = f3;
        f8 = f4;
      }
      if (n4 != 0) continue;
      f9 = f3;
      f10 = f4;
    }
    if (!arrayList2.isEmpty()) {
      arrayList.add(SvgCodec.mfosr0czatrc(arrayList2, false));
    }
    return new SvgCodec(arrayList, f, f2);
  }

  private static void updateState(
      List<Float> list,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      boolean bl,
      boolean bl2) {
    float f8;
    float f9;
    float f10;
    float f11;
    float f12;
    float f13;
    float f14;
    float f15 = f3 - f;
    float f16 = f4 - f2;
    if (f5 < Float.intBitsToFloat(981668463)
        || f6 < Float.intBitsToFloat(981668463)
        || f15 * f15 + f16 * f16 < Float.intBitsToFloat(953267991)) {
      list.add(Float.valueOf(f3));
      list.add(Float.valueOf(f4));
      return;
    }
    float f17 = (float) Math.cos(f7);
    float f18 = (float) Math.sin(f7);
    float f19 = (f - f3) * Float.intBitsToFloat(0x3F000000);
    float f20 = f17 * f19 + f18 * (f14 = (f2 - f4) * Float.intBitsToFloat(0x3F000000));
    float f21 = f20 * f20;
    float f22 =
        f21 / (f13 = f5 * f5) + (f12 = (f11 = -f18 * f19 + f17 * f14) * f11) / (f10 = f6 * f6);
    if (f22 > 1.0f) {
      f9 = (float) Math.sqrt(f22);
      f13 = (f5 *= f9) * f5;
      f10 = (f6 *= f9) * f6;
    }
    f9 = Math.max(0.0f, f13 * f10 - f13 * f12 - f10 * f21);
    float f23 = f13 * f12 + f10 * f21;
    float f24 = f8 = f23 > 0.0f ? (float) Math.sqrt(f9 / f23) : 0.0f;
    if (bl == bl2) {
      f8 = -f8;
    }
    float f25 = f8 * f5 * f11 / f6;
    float f26 = -f8 * f6 * f20 / f5;
    float f27 = (f + f3) * Float.intBitsToFloat(0x3F000000);
    float f28 = (f2 + f4) * Float.intBitsToFloat(0x3F000000);
    float f29 = f17 * f25 - f18 * f26 + f27;
    float f30 = f18 * f25 + f17 * f26 + f28;
    float f31 = (f20 - f25) / f5;
    float f32 = (f11 - f26) / f6;
    float f33 = (-f20 - f25) / f5;
    float f34 = (-f11 - f26) / f6;
    float f35 = SvgCodec.calculateValue(1.0f, 0.0f, f31, f32);
    float f36 = SvgCodec.calculateValue(f31, f32, f33, f34);
    if (!bl2 && f36 > 0.0f) {
      f36 -= Float.intBitsToFloat(1086918619);
    }
    if (bl2 && f36 < 0.0f) {
      f36 += Float.intBitsToFloat(1086918619);
    }
    int n =
        Math.max(
            1,
            (int)
                Math.ceil((double) Math.abs(f36) / Double.longBitsToDouble(4609753056924675352L)));
    float f37 = f36 / (float) n;
    float f38 =
        (float)
            (Double.longBitsToDouble(0x3FF5555555555555L)
                * Math.tan((double) f37 / Double.longBitsToDouble(0x4010000000000000L)));
    float f39 = (float) Math.cos(f35);
    float f40 = (float) Math.sin(f35);
    float f41 = f;
    float f42 = f2;
    for (int i = 0; i < n; ++i) {
      float f43 = (float) Math.cos(f35 + (float) (i + 1) * f37);
      float f44 = (float) Math.sin(f35 + (float) (i + 1) * f37);
      float f45 = f39 - f38 * f40;
      float f46 = f40 + f38 * f39;
      float f47 = f43 + f38 * f44;
      float f48 = f44 - f38 * f43;
      float f49 = f17 * f5 * f45 - f18 * f6 * f46 + f29;
      float f50 = f18 * f5 * f45 + f17 * f6 * f46 + f30;
      float f51 = f17 * f5 * f47 - f18 * f6 * f48 + f29;
      float f52 = f18 * f5 * f47 + f17 * f6 * f48 + f30;
      float f53 = f17 * f5 * f43 - f18 * f6 * f44 + f29;
      float f54 = f18 * f5 * f43 + f17 * f6 * f44 + f30;
      SvgCodec.updateState2(list, f41, f42, f49, f50, f51, f52, f53, f54);
      f41 = f53;
      f42 = f54;
      f39 = f43;
      f40 = f44;
    }
  }

  private static float calculateValue(float f, float f2, float f3, float f4) {
    float f5 = f * f3 + f2 * f4;
    float f6 = (float) Math.sqrt((f * f + f2 * f2) * (f3 * f3 + f4 * f4));
    float f7 =
        Math.max(
            Float.intBitsToFloat(-1082130432),
            Math.min(1.0f, f5 / Math.max(f6, Float.intBitsToFloat(786163455))));
    float f8 = (float) Math.acos(f7);
    if (f * f4 - f2 * f3 < 0.0f) {
      f8 = -f8;
    }
    return f8;
  }

  static float readFloat(String string, int[] nArray) {
    int n;
    int n2 = string.length();
    for (n = nArray[0]; n < n2 && (string.charAt(n) == ' ' || string.charAt(n) == ','); ++n) {}
    int n3 = n;
    if (n < n2 && (string.charAt(n) == '-' || string.charAt(n) == '+')) {
      ++n;
    }
    int n4 = 0;
    while (n < n2) {
      char c = string.charAt(n);
      if (c == '.' && n4 == 0) {
        n4 = 1;
        ++n;
        continue;
      }
      if (c >= '0' && c <= '9') {
        ++n;
        continue;
      }
      if (c != 'e' && c != 'E' || n <= n3) break;
      if (++n >= n2 || string.charAt(n) != '+' && string.charAt(n) != '-') continue;
      ++n;
    }
    nArray[0] = n;
    if (n3 == n) {
      return 0.0f;
    }
    try {
      return Float.parseFloat(string.substring(n3, n));
    } catch (NumberFormatException numberFormatException) {
      return 0.0f;
    }
  }

  private static boolean checkCondition(String string, int[] nArray) {
    boolean bl;
    int n;
    int n2 = string.length();
    for (n = nArray[0]; n < n2 && (string.charAt(n) == ' ' || string.charAt(n) == ','); ++n) {}
    boolean bl2 = bl = n < n2 && string.charAt(n) == '1';
    if (n >= n2 || string.charAt(n) == '0' || string.charAt(n) == '1') {}

    nArray[0] = ++n;
    return bl;
  }

  private static int mixrfpyhjm8y(String string, int n) {
    char c;
    while (n < string.length()
        && ((c = string.charAt(n)) == ' ' || c == ',' || c == '\n' || c == '\r' || c == '\t')) {
      ++n;
    }
    return n;
  }

  private static void updateState2(
      List<Float> list,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      float f8) {
    float f9;
    float f10 = f8 - f2;
    float f11 = f7 - f;
    float f12 = Math.abs((f3 - f7) * f10 - (f4 - f8) * f11);
    if ((f12 + (f9 = Math.abs((f5 - f7) * f10 - (f6 - f8) * f11))) * (f12 + f9)
        < Float.intBitsToFloat(1041865114) * (f11 * f11 + f10 * f10)) {
      list.add(Float.valueOf(f7));
      list.add(Float.valueOf(f8));
      return;
    }
    float f13 = (f + f3) / 2.0f;
    float f14 = (f2 + f4) / 2.0f;
    float f15 = (f3 + f5) / 2.0f;
    float f16 = (f4 + f6) / 2.0f;
    float f17 = (f5 + f7) / 2.0f;
    float f18 = (f6 + f8) / 2.0f;
    float f19 = (f13 + f15) / 2.0f;
    float f20 = (f14 + f16) / 2.0f;
    float f21 = (f15 + f17) / 2.0f;
    float f22 = (f16 + f18) / 2.0f;
    float f23 = (f19 + f21) / 2.0f;
    float f24 = (f20 + f22) / 2.0f;
    SvgCodec.updateState2(list, f, f2, f13, f14, f19, f20, f23, f24);
    SvgCodec.updateState2(list, f23, f24, f21, f22, f17, f18, f7, f8);
  }

  private static void updateState3(
      List<Float> list, float f, float f2, float f3, float f4, float f5, float f6) {
    SvgCodec.updateState2(
        list,
        f,
        f2,
        f + Float.intBitsToFloat(1059760811) * (f3 - f),
        f2 + Float.intBitsToFloat(1059760811) * (f4 - f2),
        f5 + Float.intBitsToFloat(1059760811) * (f3 - f5),
        f6 + Float.intBitsToFloat(1059760811) * (f4 - f6),
        f5,
        f6);
  }

  private static SubPath mfosr0czatrc(List<Float> list, boolean bl) {
    float[] fArray = new float[list.size()];
    for (int i = 0; i < fArray.length; ++i) {
      fArray[i] = list.get(i).floatValue();
    }
    return new SubPath(fArray, bl);
  }

  public record SubPath(float[] points, boolean closed) {
    public int vertexCount() {
      return this.points.length / 2;
    }

    public float x(int n) {
      return this.points[n * 2];
    }

    public float y(int n) {
      return this.points[n * 2 + 1];
    }
  }
}
