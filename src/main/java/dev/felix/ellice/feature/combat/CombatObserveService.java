


package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CombatObserveService {
    public static final int HORIZON = 3;
    static final int CAPACITY = 256;
    private static final int fh0dr9ejxbn = 12;
    private static final RotationVector fhvz141ypinr = new RotationVector(0.0, 0.0, 0.0);
    private final ArrayList<Sample> fe32rxcssqzx = new ArrayList(256);
    private long f9t0mo8hk4y9;
    private long f12d70irjhso = Long.MIN_VALUE;
    private int f2dyyd73w54s;
    private int fhtssjw01lm9;
    private double fd7k0mxzc2ue;
    private double f7q7txzpehxt;
    private double fjeuroiwyi4 = 1.0;
    private Pending f7kz7wufwhw4;
    private Forecast fffxs0jhqeyu = this.m6vzh0vm1mbu();

    public Forecast observe(long l, double d, double d2, boolean bl, RotationVector rotationVector) {
        return this.observe(l, d, d2, bl, 0, rotationVector);
    }

    public Forecast observe(long l, double d, double d2, boolean bl, int n, RotationVector rotationVector) {
        if (!Double.isFinite(d) || !Double.isFinite(d2) || rotationVector == null || !Double.isFinite(rotationVector.lengthSquared()) || Math.hypot(d, d2) > Double.longBitsToDouble(4604029899060858061L)) {
            this.interrupt();
            return this.fffxs0jhqeyu;
        }
        if (l == this.f12d70irjhso) {
            return this.fffxs0jhqeyu;
        }
        if (this.f12d70irjhso != Long.MIN_VALUE && l < this.f12d70irjhso) {
            this.clear();
        }
        if (this.f12d70irjhso != Long.MIN_VALUE && l - this.f12d70irjhso != 1L) {
            this.interrupt();
        }
        this.f12d70irjhso = l;
        RotationVector rotationVector2 = new RotationVector(d, 0.0, d2);
        this.mdktue01hva5(l, rotationVector2);
        this.fe32rxcssqzx.add(new Sample(l, this.f9t0mo8hk4y9, rotationVector2, bl, Math.max(0, Math.min(10, n))));
        if (this.fe32rxcssqzx.size() > 256) {
            this.fe32rxcssqzx.removeFirst();
        }
        ++this.f2dyyd73w54s;
        this.fffxs0jhqeyu = this.m3m9j5shufev(l, rotationVector2);
        if (this.f7kz7wufwhw4 == null && this.fffxs0jhqeyu.matches() >= 2) {
            this.f7kz7wufwhw4 = new Pending(l, this.fffxs0jhqeyu.steps(), rotationVector, fhvz141ypinr);
        }
        return this.fffxs0jhqeyu;
    }

    public Forecast forecast() {
        return this.fffxs0jhqeyu;
    }

    public long lastTick() {
        return this.f12d70irjhso;
    }

    int retainedSamples() {
        return this.fe32rxcssqzx.size();
    }

    public void interrupt() {
        if (this.f2dyyd73w54s > 0) {
            ++this.f9t0mo8hk4y9;
            this.fjeuroiwyi4 *= Double.longBitsToDouble(4604029899060858061L);
        }
        this.f2dyyd73w54s = 0;
        this.f7kz7wufwhw4 = null;
        this.fffxs0jhqeyu = this.m6vzh0vm1mbu();
    }

    public void clear() {
        this.interrupt();
        this.fe32rxcssqzx.clear();
        this.fhtssjw01lm9 = 0;
        this.f7q7txzpehxt = 0.0;
        this.fd7k0mxzc2ue = 0.0;
        this.fjeuroiwyi4 = 1.0;
        this.f12d70irjhso = Long.MIN_VALUE;
        this.fffxs0jhqeyu = this.m6vzh0vm1mbu();
    }

    private void mdktue01hva5(long l, RotationVector rotationVector) {
        this.fjeuroiwyi4 += (1.0 - this.fjeuroiwyi4) * Double.longBitsToDouble(4585925428558828667L);
        if (this.f7kz7wufwhw4 == null) {
            return;
        }
        int n = (int)(l - this.f7kz7wufwhw4.tick());
        if (n < 1 || n > 3) {
            this.f7kz7wufwhw4 = null;
            return;
        }
        double d = rotationVector.subtract(this.f7kz7wufwhw4.steps().get(n - 1)).length();
        if (d > Double.longBitsToDouble(4591870180066957722L)) {
            this.fjeuroiwyi4 *= Double.longBitsToDouble(4598175219545276416L);
        }
        RotationVector rotationVector2 = this.f7kz7wufwhw4.actual().add(rotationVector);
        if (n == 3) {
            RotationVector rotationVector3 = fhvz141ypinr;
            for (RotationVector rotationVector4 : this.f7kz7wufwhw4.steps()) {
                rotationVector3 = rotationVector3.add(rotationVector4);
            }
            double d2 = rotationVector2.subtract(rotationVector3).lengthSquared() / Double.longBitsToDouble(0x4022000000000000L);
            double d3 = rotationVector2.subtract(this.f7kz7wufwhw4.baseline()).lengthSquared() / Double.longBitsToDouble(0x4022000000000000L);
            double d4 = this.fhtssjw01lm9++ == 0 ? 1.0 : Double.longBitsToDouble(4598175219545276416L);
            this.fd7k0mxzc2ue += (d2 - this.fd7k0mxzc2ue) * d4;
            this.f7q7txzpehxt += (d3 - this.f7q7txzpehxt) * d4;
            this.f7kz7wufwhw4 = null;
        } else {
            this.f7kz7wufwhw4 = new Pending(this.f7kz7wufwhw4.tick(), this.f7kz7wufwhw4.steps(), this.f7kz7wufwhw4.baseline(), rotationVector2);
        }
    }

    private Forecast m3m9j5shufev(long l, RotationVector rotationVector) {
        int d8;
        double d;
        if (this.f2dyyd73w54s < 12) {
            return this.m6vzh0vm1mbu();
        }
        int n2 = this.fe32rxcssqzx.size() - 12;
        ArrayList<Match> arrayList = new ArrayList<Match>();
        int n3 = 11;
        while (n3 + 3 < n2) {
            Sample sample = this.fe32rxcssqzx.get(n3 - 12 + 1);
            Sample record = this.fe32rxcssqzx.get(n3 + 3);
            if (sample.segment() == record.segment() && record.tick() - sample.tick() == 14L && l - record.tick() <= 1200L) {
                d = 0.0;
                double d2 = 0.0;
                for (int match = 0; match < 12; ++match) {
                    Sample sample2 = this.fe32rxcssqzx.get(n3 - 12 + 1 + match);
                    Record record2 = this.fe32rxcssqzx.get(n2 + match);
                    d += CombatObserveService.mgjxqfg95q12(sample2.motion(), ((Sample)record2).motion());
                    if (sample2.shield() != ((Sample)record2).shield()) {
                        d += Double.longBitsToDouble(4569063951553953530L);
                    }
                    d8 = sample2.hurtTicks() - ((Sample)record2).hurtTicks();
                    d += (double)(d8 * d8) * Double.longBitsToDouble(4542503522391573293L);
                    d2 += CombatObserveService.mgjxqfg95q12(sample2.motion(), sample.motion());
                }
                if (!(d2 / Double.longBitsToDouble(4622945017495814144L) < Double.longBitsToDouble(4554169646866313825L)) && !(d / Double.longBitsToDouble(4622945017495814144L) > Double.longBitsToDouble(4567911030049346683L))) {
                    arrayList.add(new Match(n3, d /= Double.longBitsToDouble(4622945017495814144L), d + (double)(l - record.tick()) * Double.longBitsToDouble(4521832792735477133L)));
                }
            }
            ++n3;
        }
        arrayList.sort(Comparator.comparingDouble(Match::rank));
        ArrayList<Match> arrayList2 = new ArrayList<Match>(4);
        for (Match match : arrayList) {
            if (arrayList2.stream().anyMatch(match2 -> Math.abs(match2.end() - match.end()) < 12)) continue;
            arrayList2.add(match);
            if (arrayList2.size() != 4) continue;
            break;
        }
        if (arrayList2.size() < 2) {
            return this.m6vzh0vm1mbu();
        }
        double d3 = 0.0;
        d = 0.0;
        ArrayList<RotationVector> arrayList3 = new ArrayList<RotationVector>(List.of(fhvz141ypinr, fhvz141ypinr, fhvz141ypinr));
        for (Match match : arrayList2) {
            double d7 = Math.exp(-match.rank() / Double.longBitsToDouble(4564560351926583034L));
            d3 += d7;
            d += match.error() * d7;
            for (d8 = 0; d8 < 3; ++d8) {
                arrayList3.set(d8, arrayList3.get(d8).add(this.fe32rxcssqzx.get(match.end() + d8 + 1).motion().multiply(d7)));
            }
        }
        for (int i = 0; i < 3; ++i) {
            arrayList3.set(i, arrayList3.get(i).multiply(1.0 / d3));
        }
        double d4 = 0.0;
        for (Record record2 : arrayList2) {
            double d5 = Math.exp(-((Match)record2).rank() / Double.longBitsToDouble(4564560351926583034L)) / d3;
            for (int i = 0; i < 3; ++i) {
                d4 += this.fe32rxcssqzx.get(((Match)record2).end() + i + 1).motion().subtract(arrayList3.get(i)).lengthSquared() * d5 / Double.longBitsToDouble(0x4008000000000000L);
            }
        }
        double d6 = CombatObserveService.m6cmmiqls9dn((this.f7q7txzpehxt - this.fd7k0mxzc2ue) / (this.f7q7txzpehxt + Double.longBitsToDouble(4556014321273684781L)));
        double d7 = this.fhtssjw01lm9 < 4 ? 0.0 : Math.min(1.0, (double)this.fhtssjw01lm9 / Double.longBitsToDouble(0x4020000000000000L)) * d6 * Math.exp(-this.fd7k0mxzc2ue / Double.longBitsToDouble(4569063951553953530L) - d / d3 / Double.longBitsToDouble(4564560351926583034L) - d4 / Double.longBitsToDouble(4566758108544739836L)) * Math.min(1.0, (double)(arrayList2.size() - 1) / Double.longBitsToDouble(0x4000000000000000L)) * this.fjeuroiwyi4;
        Pattern pattern = Pattern.REPEATING;
        for (RotationVector rotationVector2 : arrayList3) {
            if (rotationVector.x() > Double.longBitsToDouble(4585925428558828667L) && rotationVector2.x() < Double.longBitsToDouble(-4637446608295947141L)) {
                pattern = Pattern.REENGAGE;
                break;
            }
            if (rotationVector.x() < Double.longBitsToDouble(-4637446608295947141L) && rotationVector2.x() > Double.longBitsToDouble(4585925428558828667L)) {
                pattern = Pattern.DISENGAGE;
                break;
            }
            if (!(Math.abs(rotationVector.z()) > Double.longBitsToDouble(4585925428558828667L)) || !(rotationVector.z() * rotationVector2.z() < Double.longBitsToDouble(-4658350516326350035L))) continue;
            pattern = Pattern.STRAFE_REVERSAL;
        }
        return new Forecast(arrayList3, d7 < Double.longBitsToDouble(0x3FC3333333333333L) ? Pattern.LEARNING : pattern, d7, arrayList2.size(), this.fhtssjw01lm9, Math.sqrt(this.fd7k0mxzc2ue), Math.sqrt(this.f7q7txzpehxt));
    }

    private Forecast m6vzh0vm1mbu() {
        return new Forecast(List.of(fhvz141ypinr, fhvz141ypinr, fhvz141ypinr), Pattern.LEARNING, 0.0, 0, this.fhtssjw01lm9, Math.sqrt(this.fd7k0mxzc2ue), Math.sqrt(this.f7q7txzpehxt));
    }

    private static double m6cmmiqls9dn(double d) {
        return Math.max(0.0, Math.min(1.0, d));
    }

    private static double mgjxqfg95q12(RotationVector rotationVector, RotationVector rotationVector2) {
        double d = rotationVector.x() - rotationVector2.x();
        double d2 = rotationVector.z() - rotationVector2.z();
        return d * d + d2 * d2;
    }

    public record Forecast(List<RotationVector> steps, Pattern pattern, double confidence, int matches, int validations, double error, double baselineError) {
        public Forecast {
            steps = List.copyOf(steps);
        }

        public RotationVector offset(double d) {
            double d2 = Math.max(0.0, Math.min(Double.longBitsToDouble(0x4008000000000000L), d));
            RotationVector rotationVector = fhvz141ypinr;
            for (RotationVector rotationVector2 : this.steps) {
                rotationVector = rotationVector.add(rotationVector2.multiply(Math.min(1.0, d2)));
                d2 = Math.max(0.0, d2 - 1.0);
            }
            return rotationVector;
        }
    }

    private record Sample(long tick, long segment, RotationVector motion, boolean shield, int hurtTicks) {
    }

    private record Pending(long tick, List<RotationVector> steps, RotationVector baseline, RotationVector actual) {
    }

    private record Match(int end, double error, double rank) {
    }

    public static enum Pattern {
        LEARNING,
        REPEATING,
        STRAFE_REVERSAL,
        REENGAGE,
        DISENGAGE;

    }
}
