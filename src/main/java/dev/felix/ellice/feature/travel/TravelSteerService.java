package dev.felix.ellice.feature.travel;

import java.util.List;
import java.util.Objects;
import org.joml.Vector3d;

public final class TravelSteerService {
  private static final double value = 0.7;
  private static final double value2 = 1.6;
  private static final double value3 = 1.0;
  private static final double value4 = 0.05;
  private static final int count = 15;
  private static final double value5 = 1.5;
  private static final double value6 = 1.2;
  private static final double value7 = 100.0;
  private List<TravelData> items;
  private int fykr8qdje7p;
  private double value8;
  private double value9;
  private int count3;
  private boolean enabled;
  private int count4;
  private boolean enabled2;
  private int count5;
  private double value10;
  private double value11;
  private double value12;

  public TravelSteerService() {
    this.count5 = -1;
  }

  public void reset() {
    this.items = null;
    this.fykr8qdje7p = 0;
    this.count3 = 0;
    this.enabled = false;
    this.count4 = 0;
    this.enabled2 = false;
    this.count5 = -1;
  }

  public Steer steer(
      final List<TravelData> list,
      final Vector3d vector3d,
      final boolean b,
      final boolean b2,
      final boolean b3) {
    return this.steer(list, vector3d, b, b2, b3, null);
  }

  public Steer steer(
      final List<TravelData> items,
      final Vector3d obj,
      final boolean b,
      final boolean b2,
      final boolean b3,
      final JumpProbe jumpProbe) {
    Objects.requireNonNull(obj, "player");
    if (items == null || items.isEmpty()) {
      return new Steer(0.0, 0.0, false, false, false, true);
    }
    if (this.items != items) {
      this.items = items;
      this.fykr8qdje7p =
          ((calculateValue(obj, items.getFirst().pos())
                  > Double.longBitsToDouble(4609434218613702656L))
              ? calculateValue2(items, obj)
              : 0);
      this.count5 = this.fykr8qdje7p;
      this.updateState(items, obj);
    }
    for (int i = 0; i < 4; ++i) {
      if (this.fykr8qdje7p >= items.size()) {
        return new Steer(0.0, 0.0, false, false, false, true);
      }
      if (!this.checkCondition(items.get(this.fykr8qdje7p), obj)) {
        break;
      }
      ++this.fykr8qdje7p;
      this.updateState(items, obj);
    }
    if (this.fykr8qdje7p >= items.size()) {
      return new Steer(0.0, 0.0, false, false, false, true);
    }
    while (this.fykr8qdje7p < items.size()
        && checkCondition2(items.get(this.fykr8qdje7p), obj)) {
      ++this.fykr8qdje7p;
    }
    if (this.fykr8qdje7p >= items.size()) {
      return new Steer(0.0, 0.0, false, false, false, true);
    }
    if (this.fykr8qdje7p != this.count5) {
      this.count5 = this.fykr8qdje7p;
      this.updateState(items, obj);
    }
    if (!this.enabled) {
      this.value8 = obj.x;
      this.value9 = obj.z;
      this.count3 = 0;
      this.enabled = true;
    } else {
      final double hypot = Math.hypot(obj.x - this.value8, obj.z - this.value9);
      if (b && hypot < Double.longBitsToDouble(4587366580439587226L)) {
        ++this.count3;
      } else {
        this.count3 = 0;
        this.value8 = obj.x;
        this.value9 = obj.z;
      }
    }
    final TravelData travelData = items.get(this.fykr8qdje7p);
    final Vector3d pos = travelData.pos();
    final double x = pos.x - obj.x;
    final double n = pos.z - obj.z;
    final double hypot2 = Math.hypot(x, n);
    final double a = pos.y - obj.y;
    final double degrees = Math.toDegrees(Math.atan2(-x, n));
    ++this.count4;
    final boolean b4 =
        jumpProbe == null
            || jumpProbe.clear((int) Math.floor(pos.x), pos.y, (int) Math.floor(pos.z));
    final boolean b5 =
        jumpProbe == null
            || jumpProbe.clear((int) Math.floor(obj.x), obj.y, (int) Math.floor(obj.z));
    return switch (travelData.type()) {
      case PILLAR, BRIDGE -> new Steer(degrees, 0.0, false, false, false, false);
      case PARKOUR, PARKOUR_ASCEND -> {
        final Vector3d createVector3d = createVector3d(items, this.fykr8qdje7p);
        final double hypot3 = Math.hypot(pos.x - createVector3d.x, pos.z - createVector3d.z);
        final double n2 =
            (obj.x - createVector3d.x)
                    * ((hypot3 < Double.longBitsToDouble(4472406533629990549L))
                        ? (x / Math.max(hypot2, Double.longBitsToDouble(4472406533629990549L)))
                        : ((pos.x - createVector3d.x) / hypot3))
                + (obj.z - createVector3d.z)
                    * ((hypot3 < Double.longBitsToDouble(4472406533629990549L))
                        ? (n / Math.max(hypot2, Double.longBitsToDouble(4472406533629990549L)))
                        : ((pos.z - createVector3d.z) / hypot3));
        final boolean b6 = b3 && !b2 && hypot3 >= Double.longBitsToDouble(4615063718147915776L);
        final boolean b7 =
            b && !this.enabled2 && n2 > Double.longBitsToDouble(4602678819172646912L) && b4;
        if (b7) {
          this.enabled2 = true;
        }
        yield new Steer(degrees, 0.0, true, b6, b7, false);
      }
      case ASCEND, DIAGONAL_ASCEND -> {
        boolean b8 =
            Math.hypot(pos.x - obj.x, pos.z - obj.z)
                    <= Double.longBitsToDouble(4608083138725491507L)
                && b4;
        if (!b8 && b && this.count3 >= 15 && b5) {
          b8 = true;
        }
        if (this.count3 >= 15) {
          this.count3 = 0;
        }
        yield new Steer(degrees, 0.0, true, false, b8, false);
      }
      case FALL, DESCEND -> new Steer(degrees, 0.0, true, false, false, false);
      case SWIM ->
          new Steer(
              degrees,
              0.0,
              true,
              false,
              b2 && a > Double.longBitsToDouble(-4620693217682128896L),
              false);
      case CLIMB ->
          new Steer(
              degrees, 0.0, true, false, a > Double.longBitsToDouble(4599075939470750515L), false);
      default -> {
        final boolean b9 =
            b3
                && !b2
                && Math.abs(a) < Double.longBitsToDouble(4603579539098121011L)
                && hypot2 > Double.longBitsToDouble(4612811918334230528L);
        boolean b10 = false;
        if (b && this.count3 >= 15 && b5) {
          b10 = true;
        }
        if (this.count3 >= 15) {
          this.count3 = 0;
        }
        yield new Steer(degrees, 0.0, true, b9, b10, false);
      }
    };
  }

  private void updateState(final List<TravelData> list, final Vector3d vector3d) {
    this.count4 = 0;
    this.enabled2 = false;
    final Vector3d vector3d2 = (list.size() == 1) ? vector3d : createVector3d(list, this.fykr8qdje7p);
    this.value10 = vector3d2.x;
    this.value11 = vector3d2.y;
    this.value12 = vector3d2.z;
  }

  private static Vector3d createVector3d(final List<TravelData> list, final int n) {
    if (n > 0) {
      return list.get(n - 1).pos();
    }
    return list.get(0).pos();
  }

  private boolean checkCondition(final TravelData travelData, final Vector3d vector3d) {
    if (this.count4 <= travelData.costTicks() + Double.longBitsToDouble(4636737291354636288L)) {
      return switch (travelData.type()) {
        case FALL, DESCEND, SWIM -> false;
        default ->
            vector3d.y
                < Math.min(this.value11, travelData.pos().y)
                    - Double.longBitsToDouble(4603579539098121011L);
      };
    }
    return true;
  }

  private static boolean checkCondition2(final TravelData travelData, final Vector3d vector3d) {
    final Vector3d pos = travelData.pos();
    final double x = pos.x - vector3d.x;
    final double y = pos.z - vector3d.z;
    final double abs = Math.abs(pos.y - vector3d.y);
    return switch (travelData.type()) {
      case PILLAR, PARKOUR_ASCEND, ASCEND, DIAGONAL_ASCEND, CLIMB ->
          Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L)
              && abs < Double.longBitsToDouble(4600877379321698714L);
      case FALL, DESCEND ->
          Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L) && abs < 1.0;
      default ->
          Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L)
              && abs < Double.longBitsToDouble(4609884578576439706L);
    };
  }

  private static double calculateValue(final Vector3d vector3d, final Vector3d vector3d2) {
    return Math.hypot(vector3d2.x - vector3d.x, vector3d2.z - vector3d.z);
  }

  private static int calculateValue2(final List<TravelData> list, final Vector3d vector3d) {
    int n = 0;
    double longBitsToDouble = Double.longBitsToDouble(9218868437227405312L);
    double longBitsToDouble2 = Double.longBitsToDouble(9218868437227405312L);
    for (int i = 0; i < list.size(); ++i) {
      final Vector3d pos = list.get(i).pos();
      final double calculateValue = calculateValue(vector3d, pos);
      final double abs = Math.abs(pos.y - vector3d.y);
      if (calculateValue < longBitsToDouble - Double.longBitsToDouble(4472406533629990549L)
          || (calculateValue <= longBitsToDouble + Double.longBitsToDouble(4472406533629990549L)
              && (abs < longBitsToDouble2 - Double.longBitsToDouble(4472406533629990549L)
                  || (Math.abs(abs - longBitsToDouble2)
                          <= Double.longBitsToDouble(4472406533629990549L)
                      && i > n)))) {
        n = i;
        longBitsToDouble = calculateValue;
        longBitsToDouble2 = abs;
      }
    }
    return n;
  }

  public static boolean gapAhead(final FloorProbe obj, final Vector3d obj2, final double angdeg) {
    Objects.requireNonNull(obj, "probe");
    Objects.requireNonNull(obj2, "player");
    final double radians = Math.toRadians(angdeg);
    final double n = -Math.sin(radians);
    final double cos = Math.cos(radians);
    if (obj.floor((int) Math.floor(obj2.x), (int) Math.floor(obj2.z), obj2.y) == null) {
      return false;
    }
    for (int i = 1; i <= 2; ++i) {
      final Double floor =
          obj.floor((int) Math.floor(obj2.x + n * i), (int) Math.floor(obj2.z + cos * i), obj2.y);
      if (floor != null && floor >= obj2.y - Double.longBitsToDouble(4602678819172646912L)) {
        return false;
      }
    }
    return true;
  }

  public interface JumpProbe {
    boolean clear(final int p0, final double p1, final int p2);
  }

  public record Steer(
      double yawDeg,
      double pitchDeg,
      boolean forward,
      boolean sprint,
      boolean jump,
      boolean done) {}

  public interface FloorProbe {
    Double floor(final int p0, final int p1, final double p2);
  }
}
