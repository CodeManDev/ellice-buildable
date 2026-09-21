package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CombatObserveService {
  public static final int HORIZON = 3;
  static final int CAPACITY = 256;
  private static final int fh0dr9ejxbn = 12;
  private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
  private final ArrayList<Sample> items = new ArrayList(256);
  private long timestamp;
  private long timestamp2 = Long.MIN_VALUE;
  private int count2;
  private int count3;
  private double value;
  private double value2;
  private double fjeuroiwyi4 = 1.0;
  private Pending pending;
  private Forecast fffxs0jhqeyu = this.createForecast2();

  public Forecast observe(long l, double d, double d2, boolean bl, RotationVector rotationVector) {
    return this.observe(l, d, d2, bl, 0, rotationVector);
  }

  public Forecast observe(
      long l, double d, double d2, boolean bl, int n, RotationVector rotationVector) {
    if (!Double.isFinite(d)
        || !Double.isFinite(d2)
        || rotationVector == null
        || !Double.isFinite(rotationVector.lengthSquared())
        || Math.hypot(d, d2) > Double.longBitsToDouble(4604029899060858061L)) {
      this.interrupt();
      return this.fffxs0jhqeyu;
    }
    if (l == this.timestamp2) {
      return this.fffxs0jhqeyu;
    }
    if (this.timestamp2 != Long.MIN_VALUE && l < this.timestamp2) {
      this.clear();
    }
    if (this.timestamp2 != Long.MIN_VALUE && l - this.timestamp2 != 1L) {
      this.interrupt();
    }
    this.timestamp2 = l;
    RotationVector rotationVector2 = new RotationVector(d, 0.0, d2);
    this.updateState(l, rotationVector2);
    this.items.add(
        new Sample(l, this.timestamp, rotationVector2, bl, Math.max(0, Math.min(10, n))));
    if (this.items.size() > 256) {
      this.items.removeFirst();
    }
    ++this.count2;
    this.fffxs0jhqeyu = this.m3m9j5shufev(l, rotationVector2);
    if (this.pending == null && this.fffxs0jhqeyu.matches() >= 2) {
      this.pending = new Pending(l, this.fffxs0jhqeyu.steps(), rotationVector, rotationData7);
    }
    return this.fffxs0jhqeyu;
  }

  public Forecast forecast() {
    return this.fffxs0jhqeyu;
  }

  public long lastTick() {
    return this.timestamp2;
  }

  int retainedSamples() {
    return this.items.size();
  }

  public void interrupt() {
    if (this.count2 > 0) {
      ++this.timestamp;
      this.fjeuroiwyi4 *= Double.longBitsToDouble(4604029899060858061L);
    }
    this.count2 = 0;
    this.pending = null;
    this.fffxs0jhqeyu = this.createForecast2();
  }

  public void clear() {
    this.interrupt();
    this.items.clear();
    this.count3 = 0;
    this.value2 = 0.0;
    this.value = 0.0;
    this.fjeuroiwyi4 = 1.0;
    this.timestamp2 = Long.MIN_VALUE;
    this.fffxs0jhqeyu = this.createForecast2();
  }

  private void updateState(long l, RotationVector rotationVector) {
    this.fjeuroiwyi4 += (1.0 - this.fjeuroiwyi4) * Double.longBitsToDouble(4585925428558828667L);
    if (this.pending == null) {
      return;
    }
    int n = (int) (l - this.pending.tick());
    if (n < 1 || n > 3) {
      this.pending = null;
      return;
    }
    double d = rotationVector.subtract(this.pending.steps().get(n - 1)).length();
    if (d > Double.longBitsToDouble(4591870180066957722L)) {
      this.fjeuroiwyi4 *= Double.longBitsToDouble(4598175219545276416L);
    }
    RotationVector rotationVector2 = this.pending.actual().add(rotationVector);
    if (n == 3) {
      RotationVector rotationVector3 = rotationData7;
      for (RotationVector rotationVector4 : this.pending.steps()) {
        rotationVector3 = rotationVector3.add(rotationVector4);
      }
      double d2 =
          rotationVector2.subtract(rotationVector3).lengthSquared()
              / Double.longBitsToDouble(0x4022000000000000L);
      double d3 =
          rotationVector2.subtract(this.pending.baseline()).lengthSquared()
              / Double.longBitsToDouble(0x4022000000000000L);
      double d4 = this.count3++ == 0 ? 1.0 : Double.longBitsToDouble(4598175219545276416L);
      this.value += (d2 - this.value) * d4;
      this.value2 += (d3 - this.value2) * d4;
      this.pending = null;
    } else {
      this.pending =
          new Pending(
              this.pending.tick(), this.pending.steps(), this.pending.baseline(), rotationVector2);
    }
  }

  private Forecast m3m9j5shufev(long l, RotationVector rotationVector) {
    int d8;
    double d;
    if (this.count2 < 12) {
      return this.createForecast2();
    }
    int n2 = this.items.size() - 12;
    ArrayList<Match> arrayList = new ArrayList<Match>();
    int n3 = 11;
    while (n3 + 3 < n2) {
      Sample sample = this.items.get(n3 - 12 + 1);
      Sample record = this.items.get(n3 + 3);
      if (sample.segment() == record.segment()
          && record.tick() - sample.tick() == 14L
          && l - record.tick() <= 1200L) {
        d = 0.0;
        double d2 = 0.0;
        for (int match = 0; match < 12; ++match) {
          Sample sample2 = this.items.get(n3 - 12 + 1 + match);
          Record record2 = this.items.get(n2 + match);
          d += CombatObserveService.calculateValue2(sample2.motion(), ((Sample) record2).motion());
          if (sample2.shield() != ((Sample) record2).shield()) {
            d += Double.longBitsToDouble(4569063951553953530L);
          }
          d8 = sample2.hurtTicks() - ((Sample) record2).hurtTicks();
          d += (double) (d8 * d8) * Double.longBitsToDouble(4542503522391573293L);
          d2 += CombatObserveService.calculateValue2(sample2.motion(), sample.motion());
        }
        if (!(d2 / Double.longBitsToDouble(4622945017495814144L)
                < Double.longBitsToDouble(4554169646866313825L))
            && !(d / Double.longBitsToDouble(4622945017495814144L)
                > Double.longBitsToDouble(4567911030049346683L))) {
          arrayList.add(
              new Match(
                  n3,
                  d /= Double.longBitsToDouble(4622945017495814144L),
                  d
                      + (double) (l - record.tick())
                          * Double.longBitsToDouble(4521832792735477133L)));
        }
      }
      ++n3;
    }
    arrayList.sort(Comparator.comparingDouble(Match::rank));
    ArrayList<Match> arrayList2 = new ArrayList<Match>(4);
    for (Match match : arrayList) {
      if (arrayList2.stream().anyMatch(match2 -> Math.abs(match2.end() - match.end()) < 12))
        continue;
      arrayList2.add(match);
      if (arrayList2.size() != 4) continue;
      break;
    }
    if (arrayList2.size() < 2) {
      return this.createForecast2();
    }
    double d3 = 0.0;
    d = 0.0;
    ArrayList<RotationVector> arrayList3 =
        new ArrayList<RotationVector>(List.of(rotationData7, rotationData7, rotationData7));
    for (Match match : arrayList2) {
      double d7 = Math.exp(-match.rank() / Double.longBitsToDouble(4564560351926583034L));
      d3 += d7;
      d += match.error() * d7;
      for (d8 = 0; d8 < 3; ++d8) {
        arrayList3.set(
            d8, arrayList3.get(d8).add(this.items.get(match.end() + d8 + 1).motion().multiply(d7)));
      }
    }
    for (int i = 0; i < 3; ++i) {
      arrayList3.set(i, arrayList3.get(i).multiply(1.0 / d3));
    }
    double d4 = 0.0;
    for (Record record2 : arrayList2) {
      double d5 =
          Math.exp(-((Match) record2).rank() / Double.longBitsToDouble(4564560351926583034L)) / d3;
      for (int i = 0; i < 3; ++i) {
        d4 +=
            this.items
                    .get(((Match) record2).end() + i + 1)
                    .motion()
                    .subtract(arrayList3.get(i))
                    .lengthSquared()
                * d5
                / Double.longBitsToDouble(0x4008000000000000L);
      }
    }
    double d6 =
        CombatObserveService.calculateValue(
            (this.value2 - this.value)
                / (this.value2 + Double.longBitsToDouble(4556014321273684781L)));
    double d7 =
        this.count3 < 4
            ? 0.0
            : Math.min(1.0, (double) this.count3 / Double.longBitsToDouble(0x4020000000000000L))
                * d6
                * Math.exp(
                    -this.value / Double.longBitsToDouble(4569063951553953530L)
                        - d / d3 / Double.longBitsToDouble(4564560351926583034L)
                        - d4 / Double.longBitsToDouble(4566758108544739836L))
                * Math.min(
                    1.0,
                    (double) (arrayList2.size() - 1) / Double.longBitsToDouble(0x4000000000000000L))
                * this.fjeuroiwyi4;
    Pattern pattern = Pattern.REPEATING;
    for (RotationVector rotationVector2 : arrayList3) {
      if (rotationVector.x() > Double.longBitsToDouble(4585925428558828667L)
          && rotationVector2.x() < Double.longBitsToDouble(-4637446608295947141L)) {
        pattern = Pattern.REENGAGE;
        break;
      }
      if (rotationVector.x() < Double.longBitsToDouble(-4637446608295947141L)
          && rotationVector2.x() > Double.longBitsToDouble(4585925428558828667L)) {
        pattern = Pattern.DISENGAGE;
        break;
      }
      if (!(Math.abs(rotationVector.z()) > Double.longBitsToDouble(4585925428558828667L))
          || !(rotationVector.z() * rotationVector2.z()
              < Double.longBitsToDouble(-4658350516326350035L))) continue;
      pattern = Pattern.STRAFE_REVERSAL;
    }
    return new Forecast(
        arrayList3,
        d7 < Double.longBitsToDouble(0x3FC3333333333333L) ? Pattern.LEARNING : pattern,
        d7,
        arrayList2.size(),
        this.count3,
        Math.sqrt(this.value),
        Math.sqrt(this.value2));
  }

  private Forecast createForecast2() {
    return new Forecast(
        List.of(rotationData7, rotationData7, rotationData7),
        Pattern.LEARNING,
        0.0,
        0,
        this.count3,
        Math.sqrt(this.value),
        Math.sqrt(this.value2));
  }

  private static double calculateValue(double d) {
    return Math.max(0.0, Math.min(1.0, d));
  }

  private static double calculateValue2(
      RotationVector rotationVector, RotationVector rotationVector2) {
    double d = rotationVector.x() - rotationVector2.x();
    double d2 = rotationVector.z() - rotationVector2.z();
    return d * d + d2 * d2;
  }

  public record Forecast(
      List<RotationVector> steps,
      Pattern pattern,
      double confidence,
      int matches,
      int validations,
      double error,
      double baselineError) {
    public Forecast {
      steps = List.copyOf(steps);
    }

    public RotationVector offset(double d) {
      double d2 = Math.max(0.0, Math.min(Double.longBitsToDouble(0x4008000000000000L), d));
      RotationVector rotationVector = rotationData7;
      for (RotationVector rotationVector2 : this.steps) {
        rotationVector = rotationVector.add(rotationVector2.multiply(Math.min(1.0, d2)));
        d2 = Math.max(0.0, d2 - 1.0);
      }
      return rotationVector;
    }
  }

  private record Sample(
      long tick, long segment, RotationVector motion, boolean shield, int hurtTicks) {}

  private record Pending(
      long tick, List<RotationVector> steps, RotationVector baseline, RotationVector actual) {}

  private record Match(int end, double error, double rank) {}

  public static enum Pattern {
    LEARNING,
    REPEATING,
    STRAFE_REVERSAL,
    REENGAGE,
    DISENGAGE;
  }
}
