package dev.felix.ellice.feature.notebot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class NotebotFinishService {
  public static final int SAMPLE_RATE = 22050;
  private static final int count = 4096;
  private static final int count2 = 36;
  private static final int count3 = 96;
  private final float[] float2 = new float[4096];
  private final double[] double2 = new double[4096];
  private final double[] double3 = new double[4096];
  private final double[] double4 = new double[2048];
  private final double[] double5 = new double[4096];
  private final int[] fzlm8cfati9 = new int[128];
  private final int[] int3 = new int[128];
  private final int[] int4 = new int[128];
  private final float[] float3 = new float[128];
  private final List<NotebotTitleService.Note> items = new ArrayList<NotebotTitleService.Note>();
  private final List<Float> items2 = new ArrayList<Float>();
  private int count4;
  private long timestamp;
  private int count5;

  public NotebotFinishService() {
    for (int i = 0; i < 4096; ++i) {
      this.double5[i] =
          Double.longBitsToDouble(4602678819172646912L)
              - Double.longBitsToDouble(4602678819172646912L)
                  * Math.cos(
                      Double.longBitsToDouble(4618760256179416344L)
                          * (double) i
                          / Double.longBitsToDouble(4661223415305207808L));
    }
    Arrays.fill(this.fzlm8cfati9, -100);
    Arrays.fill(this.int4, -100);
  }

  public void accept(float f) {
    this.float2[this.count4] =
        Float.isFinite(f) ? Math.max(Float.intBitsToFloat(-1082130432), Math.min(1.0f, f)) : 0.0f;
    this.count4 = this.count4 + 1 & 0xFFF;
    ++this.timestamp;
    long l =
        2048L
            + Math.round(
                (double) (this.count5 * 22050) / Double.longBitsToDouble(0x4034000000000000L));
    if (this.timestamp >= l) {
      this.mydhifje898(this.count5++);
    }
  }

  public NotebotTitleService finish(String string) {
    int n;
    int n2;
    long l = this.timestamp;
    for (n2 = 0; n2 < 2048; ++n2) {
      this.accept(0.0f);
    }
    n2 =
        Math.max(
            1,
            (int)
                Math.ceil(
                    (double) l
                        * Double.longBitsToDouble(0x4034000000000000L)
                        / Double.longBitsToDouble(4671790271803949056L)));
    float[] fArray = new float[512];
    float f = Float.intBitsToFloat(953267991);
    int n3 = Math.min(n2, this.items2.size());
    for (n = 0; n < fArray.length && n3 > 0; ++n) {
      int n4 = n * n3 / fArray.length;
      int n5 = Math.min(n3, Math.max(n4 + 1, (n + 1) * n3 / fArray.length));
      for (int i = n4; i < n5; ++i) {
        fArray[n] = Math.max(fArray[n], this.items2.get(i).floatValue());
      }
      f = Math.max(f, fArray[n]);
    }
    n = 0;
    while (n < fArray.length) {
      int n6 = n++;
      fArray[n6] = fArray[n6] / f;
    }
    return new NotebotTitleService(string, n2, this.items, fArray);
  }

  private void mydhifje898(int n2) {
    double d = 0.0;
    for (int i = 0; i < 4096; ++i) {
      float f = this.float2[this.count4 + i & 0xFFF];
      d += (double) (f * f);
      this.double2[i] = (double) f * this.double5[i];
      this.double3[i] = 0.0;
    }
    float f = (float) Math.sqrt(d / Double.longBitsToDouble(0x40B0000000000000L));
    this.items2.add(Float.valueOf(f));
    if (f < Float.intBitsToFloat(994352038)) {
      return;
    }
    NotebotFinishService.updateState2(this.double2, this.double3);
    double d2 = 0.0;
    double d3 = 0.0;
    for (int i = 1; i < this.double4.length; ++i) {
      this.double4[i] = Math.hypot(this.double2[i], this.double3[i]);
      d3 += this.double4[i];
      d2 = Math.max(d2, this.double4[i]);
    }
    d3 /= (double) this.double4.length;
    double[] dArray = new double[128];
    for (int i = 2; i < this.double4.length - 1; ++i) {
      double d4 = this.double4[i];
      if (d4 < d2 * Double.longBitsToDouble(4591870180066957722L)
          || d4 < d3 * Double.longBitsToDouble(4619567317775286272L)
          || d4 <= this.double4[i - 1]
          || d4 < this.double4[i + 1]) continue;
      double d5 =
          Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), this.double4[i - 1]));
      double d6 = Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), d4));
      double d7 =
          Math.log(Math.max(Double.longBitsToDouble(4457293557087583675L), this.double4[i + 1]));
      double d8 = d5 - Double.longBitsToDouble(0x4000000000000000L) * d6 + d7;
      double d9 =
          Math.abs(d8) < Double.longBitsToDouble(4457293557087583675L)
              ? 0.0
              : Double.longBitsToDouble(4602678819172646912L) * (d5 - d7) / d8;
      double d10 =
          ((double) i
                  + Math.max(
                      Double.longBitsToDouble(-4620693217682128896L),
                      Math.min(Double.longBitsToDouble(4602678819172646912L), d9)))
              * Double.longBitsToDouble(4671790271803949056L)
              / Double.longBitsToDouble(0x40B0000000000000L);
      double d11 =
          Double.longBitsToDouble(4634555860285128704L)
              + Double.longBitsToDouble(4622945017495814144L)
                  * Math.log(d10 / Double.longBitsToDouble(4646448178051153920L))
                  / Math.log(Double.longBitsToDouble(0x4000000000000000L));
      int n3 = (int) Math.round(d11);
      if (n3 < 36
          || n3 > 96
          || Math.abs(d11 - (double) n3) > Double.longBitsToDouble(4600877379321698714L)) continue;
      dArray[n3] = Math.max(dArray[n3], d4);
    }
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    double[] dArray2 = (double[]) dArray.clone();
    for (int i = 36; i <= 96; ++i) {
      if (dArray[i] == 0.0) continue;
      for (int j = 36; j < i - 2; ++j) {
        double d12;
        long l;
        if (dArray[j] < dArray[i] * Double.longBitsToDouble(4597094355634707497L)
            || (l =
                    Math.round(
                        d12 =
                            Math.pow(
                                Double.longBitsToDouble(0x4000000000000000L),
                                (double) (i - j) / Double.longBitsToDouble(4622945017495814144L))))
                < 2L
            || l > 6L
            || !(Math.abs(d12 - (double) l) < Double.longBitsToDouble(4585204852618449388L)))
          continue;
        int n4 = i;
        dArray2[n4] = dArray2[n4] * Double.longBitsToDouble(4585925428558828667L);
        break;
      }
      arrayList.add(i);
    }
    arrayList.sort(Comparator.comparingDouble((Integer n) -> dArray2[n]).reversed());
    double d13 = arrayList.isEmpty() ? 0.0 : dArray2[(Integer) arrayList.getFirst()];
    int n5 = 0;
    Iterator iterator = arrayList.iterator();
    while (iterator.hasNext()) {
      int n6;
      int n7 = (Integer) iterator.next();
      if (n5 == 3 || dArray2[n7] < d13 * Double.longBitsToDouble(4596373779694328218L)) break;
      ++n5;
      int n8 = n2 - this.fzlm8cfati9[n7];
      this.int3[n7] = n8 == 1 ? this.int3[n7] + 1 : 1;
      float f2 = (float) Math.min(1.0, dArray[n7] / Double.longBitsToDouble(0x4090000000000000L));
      int n9 =
          n6 =
              f2 > this.float3[n7] * Float.intBitsToFloat(1071644672) && n2 - this.int4[n7] >= 3
                  ? 1
                  : 0;
      if (this.int3[n7] >= 2
          && (n2 - this.int4[n7] >= 8
              || n2 - this.int4[n7] >= 3 && (this.int3[n7] == 2 || n6 != 0))) {
        this.items.add(
            new NotebotTitleService.Note(
                Math.max(0, n2 - 1), n7, Math.max(Float.intBitsToFloat(981668463), f2)));
        this.int4[n7] = n2;
      }
      this.fzlm8cfati9[n7] = n2;
      this.float3[n7] = f2;
    }
  }

  private static void updateState2(double[] dArray, double[] dArray2) {
    double d;
    int n;
    int n2 = dArray.length;
    int n3 = 0;
    for (n = 1; n < n2; ++n) {
      int n4 = n2 >> 1;
      while ((n3 & n4) != 0) {
        n3 ^= n4;
        n4 >>= 1;
      }
      if (n >= (n3 ^= n4)) continue;
      d = dArray[n];
      dArray[n] = dArray[n3];
      dArray[n3] = d;
      d = dArray2[n];
      dArray2[n] = dArray2[n3];
      dArray2[n3] = d;
    }
    for (n = 2; n <= n2; n <<= 1) {
      double d2 = Double.longBitsToDouble(-4604611780675359464L) / (double) n;
      d = Math.cos(d2);
      double d3 = Math.sin(d2);
      for (int i = 0; i < n2; i += n) {
        double d4 = 1.0;
        double d5 = 0.0;
        for (int j = 0; j < n / 2; ++j) {
          int n5 = i + j;
          int n6 = n5 + n / 2;
          double d6 = dArray[n6] * d4 - dArray2[n6] * d5;
          double d7 = dArray[n6] * d5 + dArray2[n6] * d4;
          dArray[n6] = dArray[n5] - d6;
          dArray2[n6] = dArray2[n5] - d7;
          int n7 = n5;
          dArray[n7] = dArray[n7] + d6;
          int n8 = n5;
          dArray2[n8] = dArray2[n8] + d7;
          double d8 = d4 * d - d5 * d3;
          d5 = d4 * d3 + d5 * d;
          d4 = d8;
        }
      }
    }
  }
}
