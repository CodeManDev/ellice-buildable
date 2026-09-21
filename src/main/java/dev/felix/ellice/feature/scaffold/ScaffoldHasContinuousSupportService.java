package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public final class ScaffoldHasContinuousSupportService {
  private static final double ffapisg5urgb = 0.3;
  private static final double value2 = 8.0;
  private static final int count = 256;

  private ScaffoldHasContinuousSupportService() {}

  public static boolean hasContinuousSupport(
      RotationVector rotationVector,
      RotationVector rotationVector2,
      int n,
      double d,
      ContinuousSupportChecker.Query query) {
    double d2;
    Objects.requireNonNull(rotationVector, "from");
    Objects.requireNonNull(rotationVector2, "to");
    Objects.requireNonNull(query, "query");
    if (!Double.isFinite(d) || d <= 0.0 || d > Double.longBitsToDouble(0x3FD3333333333333L)) {
      throw new IllegalArgumentException("Minimum foot overlap must be in (0, 0.30]");
    }
    double d3 = rotationVector2.x() - rotationVector.x();
    if (Math.hypot(d3, d2 = rotationVector2.z() - rotationVector.z())
        > Double.longBitsToDouble(0x4020000000000000L)) {
      return false;
    }
    double d4 = Double.longBitsToDouble(0x3FD3333333333333L) - d;
    double d5 = Math.ceil(Math.min(rotationVector.x(), rotationVector2.x()) - 1.0 - d4);
    double d6 = Math.floor(Math.max(rotationVector.x(), rotationVector2.x()) + d4);
    double d7 = Math.ceil(Math.min(rotationVector.z(), rotationVector2.z()) - 1.0 - d4);
    double d8 = Math.floor(Math.max(rotationVector.z(), rotationVector2.z()) + d4);
    if (d5 < Double.longBitsToDouble(-4476578029606273024L)
        || d6 > Double.longBitsToDouble(4746794007244308480L)
        || d7 < Double.longBitsToDouble(-4476578029606273024L)
        || d8 > Double.longBitsToDouble(4746794007244308480L)) {
      return false;
    }
    long l = (long) d6 - (long) d5 + 1L;
    long l2 = (long) d8 - (long) d7 + 1L;
    if (l <= 0L || l2 <= 0L || l > 256L || l2 > 256L || l * l2 > 256L) {
      return false;
    }
    ArrayList<Interval> arrayList = new ArrayList<Interval>();
    for (long i = (long) d5; i <= (long) d6; ++i) {
      for (long j = (long) d7; j <= (long) d8; ++j) {
        Interval interval =
            ScaffoldHasContinuousSupportService.createInterval2(
                rotationVector.x(),
                rotationVector.z(),
                d3,
                d2,
                (double) i - d4,
                (double) i + 1.0 + d4,
                (double) j - d4,
                (double) j + 1.0 + d4);
        if (interval == null || !query.hasSupport(new BlockCoordinates((int) i, n, (int) j)))
          continue;
        arrayList.add(interval);
      }
    }
    arrayList.sort(Comparator.comparingDouble(Interval::start));
    double d9 = 0.0;
    int n2 = 0;
    for (Interval interval : arrayList) {
      if (interval.start() > d9) {
        return false;
      }
      n2 = 1;
      if (!((d9 = Math.max(d9, interval.end())) >= 1.0)) continue;
      return true;
    }
    return (n2 != 0 && d9 >= 1.0 ? 1 : 0) != 0;
  }

  public static boolean hasContinuousTriangleSupport(
      RotationVector rotationVector,
      RotationVector rotationVector2,
      RotationVector rotationVector3,
      int n,
      double d,
      ContinuousSupportChecker.Query query) {
    TreeSet<Double> treeSet;
    ArrayList<Rectangle> arrayList;
    block15:
    {
      block14:
      {
        Objects.requireNonNull(rotationVector, "origin");
        Objects.requireNonNull(rotationVector2, "releaseEnd");
        Objects.requireNonNull(rotationVector3, "poweredEnd");
        Objects.requireNonNull(query, "query");
        if (!Double.isFinite(d) || d <= 0.0 || d > Double.longBitsToDouble(0x3FD3333333333333L)) {
          throw new IllegalArgumentException("Minimum foot overlap must be in (0, 0.30]");
        }
        if (ScaffoldHasContinuousSupportService.calculateValue(rotationVector, rotationVector2)
                > Double.longBitsToDouble(0x4020000000000000L)
            || ScaffoldHasContinuousSupportService.calculateValue(rotationVector, rotationVector3)
                > Double.longBitsToDouble(0x4020000000000000L)
            || ScaffoldHasContinuousSupportService.calculateValue(rotationVector2, rotationVector3)
                > Double.longBitsToDouble(0x4020000000000000L)) {
          return false;
        }
        double d2 =
            Math.min(rotationVector.x(), Math.min(rotationVector2.x(), rotationVector3.x()));
        double d3 =
            Math.max(rotationVector.x(), Math.max(rotationVector2.x(), rotationVector3.x()));
        double d4 =
            Math.min(rotationVector.z(), Math.min(rotationVector2.z(), rotationVector3.z()));
        double d5 =
            Math.max(rotationVector.z(), Math.max(rotationVector2.z(), rotationVector3.z()));
        double d6 = Double.longBitsToDouble(0x3FD3333333333333L) - d;
        double d7 = Math.ceil(d2 - 1.0 - d6);
        double d8 = Math.floor(d3 + d6);
        double d9 = Math.ceil(d4 - 1.0 - d6);
        double d10 = Math.floor(d5 + d6);
        if (d7 < Double.longBitsToDouble(-4476578029606273024L)
            || d8 > Double.longBitsToDouble(4746794007244308480L)
            || d9 < Double.longBitsToDouble(-4476578029606273024L)
            || d10 > Double.longBitsToDouble(4746794007244308480L)) {
          return false;
        }
        long l = (long) d8 - (long) d7 + 1L;
        long l2 = (long) d10 - (long) d9 + 1L;
        if (l <= 0L || l2 <= 0L || l > 256L || l2 > 256L || l * l2 > 256L) {
          return false;
        }
        HashSet<BlockCoordinates> hashSet = new HashSet<BlockCoordinates>();
        arrayList = new ArrayList<Rectangle>();
        treeSet = new TreeSet<Double>();
        treeSet.add(rotationVector.x());
        treeSet.add(rotationVector2.x());
        treeSet.add(rotationVector3.x());
        for (long i = (long) d7; i <= (long) d8; ++i) {
          for (long j = (long) d9; j <= (long) d10; ++j) {
            BlockCoordinates blockCoordinates = new BlockCoordinates((int) i, n, (int) j);
            if (!query.hasSupport(blockCoordinates)) continue;
            hashSet.add(blockCoordinates);
            Rectangle rectangle =
                new Rectangle(
                    (double) i - d6, (double) i + 1.0 + d6, (double) j - d6, (double) j + 1.0 + d6);
            arrayList.add(rectangle);
            if (rectangle.minimumX() > d2 && rectangle.minimumX() < d3) {
              treeSet.add(rectangle.minimumX());
            }
            if (!(rectangle.maximumX() > d2) || !(rectangle.maximumX() < d3)) continue;
            treeSet.add(rectangle.maximumX());
          }
        }
        if (!ScaffoldHasContinuousSupportService.hasContinuousSupport(
            rotationVector, rotationVector2, n, d, hashSet::contains)) break block14;
        if (!ScaffoldHasContinuousSupportService.hasContinuousSupport(
            rotationVector, rotationVector3, n, d, hashSet::contains)) break block14;
        if (ScaffoldHasContinuousSupportService.hasContinuousSupport(
            rotationVector2, rotationVector3, n, d, hashSet::contains)) break block15;
      }
      return false;
    }
    double d11 =
        (rotationVector2.x() - rotationVector.x()) * (rotationVector3.z() - rotationVector.z())
            - (rotationVector2.z() - rotationVector.z())
                * (rotationVector3.x() - rotationVector.x());
    if (d11 == 0.0) {
      return true;
    }
    ArrayList arrayList2 = new ArrayList(treeSet);
    for (int i = 1; i < arrayList2.size(); ++i) {
      double d12;
      double d13 = (Double) arrayList2.get(i - 1);
      if (d13 >= (d12 = ((Double) arrayList2.get(i)).doubleValue())) continue;
      Interval interval =
          ScaffoldHasContinuousSupportService.createInterval(
              rotationVector, rotationVector2, rotationVector3, d13);
      Interval interval2 =
          ScaffoldHasContinuousSupportService.createInterval(
              rotationVector, rotationVector2, rotationVector3, d12);
      if (interval == null || interval2 == null) {
        return false;
      }
      double d14 = Math.min(interval.start(), interval2.start());
      double d15 = Math.max(interval.end(), interval2.end());
      ArrayList<Interval> arrayList3 = new ArrayList<Interval>();
      for (Rectangle rectangle : arrayList) {
        if (!(rectangle.minimumX() <= d13) || !(rectangle.maximumX() >= d12)) continue;
        arrayList3.add(new Interval(rectangle.minimumZ(), rectangle.maximumZ()));
      }
      if (ScaffoldHasContinuousSupportService.checkCondition(arrayList3, d14, d15)) continue;
      return false;
    }
    return true;
  }

  private static double calculateValue(
      RotationVector rotationVector, RotationVector rotationVector2) {
    return Math.hypot(
        rotationVector2.x() - rotationVector.x(), rotationVector2.z() - rotationVector.z());
  }

  private static Interval createInterval(
      RotationVector rotationVector,
      RotationVector rotationVector2,
      RotationVector rotationVector3,
      double d) {
    double d2 = Double.longBitsToDouble(0x7FF0000000000000L);
    double d3 = Double.longBitsToDouble(-4503599627370496L);
    RotationVector[] rotationVectorArray =
        new RotationVector[] {rotationVector, rotationVector2, rotationVector3};
    for (int i = 0; i < rotationVectorArray.length; ++i) {
      RotationVector rotationVector4 = rotationVectorArray[i];
      RotationVector rotationVector5 = rotationVectorArray[(i + 1) % rotationVectorArray.length];
      if (d < Math.min(rotationVector4.x(), rotationVector5.x())
          || d > Math.max(rotationVector4.x(), rotationVector5.x())) continue;
      if (rotationVector4.x() == rotationVector5.x()) {
        d2 = Math.min(d2, Math.min(rotationVector4.z(), rotationVector5.z()));
        d3 = Math.max(d3, Math.max(rotationVector4.z(), rotationVector5.z()));
        continue;
      }
      double d4 =
          d == rotationVector4.x()
              ? rotationVector4.z()
              : (d == rotationVector5.x()
                  ? rotationVector5.z()
                  : rotationVector4.z()
                      + (rotationVector5.z() - rotationVector4.z())
                          * ((d - rotationVector4.x())
                              / (rotationVector5.x() - rotationVector4.x())));
      d2 = Math.min(d2, d4);
      d3 = Math.max(d3, d4);
    }
    return d2 <= d3 ? new Interval(d2, d3) : null;
  }

  private static boolean checkCondition(List<Interval> list, double d, double d2) {
    list.sort(Comparator.comparingDouble(Interval::start));
    double d3 = d;
    int n = 0;
    for (Interval interval : list) {
      if (interval.end() < d3) continue;
      if (interval.start() > d3) {
        return false;
      }
      n = 1;
      if (!((d3 = Math.max(d3, interval.end())) >= d2)) continue;
      return true;
    }
    return (n != 0 && d3 >= d2 ? 1 : 0) != 0;
  }

  private static Interval createInterval2(
      double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
    double d9;
    double d10;
    double d11 = 0.0;
    double d12 = 1.0;
    if (d3 == 0.0) {
      if (d < d5 || d > d6) {
        return null;
      }
    } else {
      d10 = (d5 - d) / d3;
      d9 = (d6 - d) / d3;
      d11 = Math.max(d11, Math.min(d10, d9));
      d12 = Math.min(d12, Math.max(d10, d9));
    }
    if (d4 == 0.0) {
      if (d2 < d7 || d2 > d8) {
        return null;
      }
    } else {
      d10 = (d7 - d2) / d4;
      d9 = (d8 - d2) / d4;
      d11 = Math.max(d11, Math.min(d10, d9));
      d12 = Math.min(d12, Math.max(d10, d9));
    }
    return d11 <= d12 ? new Interval(d11, d12) : null;
  }

  private record Interval(double start, double end) {}

  private record Rectangle(double minimumX, double maximumX, double minimumZ, double maximumZ) {}
}
