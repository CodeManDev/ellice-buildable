



package dev.felix.ellice.feature.travel;

import java.util.Objects;
import org.joml.Vector3d;
import java.util.List;

public final class TravelSteerService
{
    private static final double f31tosmfssxu = 0.7;
    private static final double f14loks4pnep = 1.6;
    private static final double fc0u7hr1wgyi = 1.0;
    private static final double f44rgb6pqfyz = 0.05;
    private static final int f2s4udlmxv3n = 15;
    private static final double fizrql6k9pir = 1.5;
    private static final double f5gdd62h756v = 1.2;
    private static final double fhkzcx9kb1jm = 100.0;
    private List<TravelData> f3q8ui93gj7y;
    private int fykr8qdje7p;
    private double fd6htw5qkjqr;
    private double f44qnmmxep73;
    private int f2odu5qua5gd;
    private boolean fd4slc7uh49b;
    private int fgz891iq17rs;
    private boolean f6l253mh7llf;
    private int f5rog0nkusr9;
    private double f13p2y486nxx;
    private double f4841uf5d4kf;
    private double f9ud9jioyt1y;
    
    public TravelSteerService() {
        this.f5rog0nkusr9 = -1;
    }
    
    public void reset() {
        this.f3q8ui93gj7y = null;
        this.fykr8qdje7p = 0;
        this.f2odu5qua5gd = 0;
        this.fd4slc7uh49b = false;
        this.fgz891iq17rs = 0;
        this.f6l253mh7llf = false;
        this.f5rog0nkusr9 = -1;
    }
    
    public Steer steer(final List<TravelData> list, final Vector3d vector3d, final boolean b, final boolean b2, final boolean b3) {
        return this.steer(list, vector3d, b, b2, b3, null);
    }
    
    public Steer steer(final List<TravelData> f3q8ui93gj7y, final Vector3d obj, final boolean b, final boolean b2, final boolean b3, final JumpProbe jumpProbe) {
        Objects.requireNonNull(obj, "player");
        if (f3q8ui93gj7y == null || f3q8ui93gj7y.isEmpty()) {
            return new Steer(0.0, 0.0, false, false, false, true);
        }
        if (this.f3q8ui93gj7y != f3q8ui93gj7y) {
            this.f3q8ui93gj7y = f3q8ui93gj7y;
            this.fykr8qdje7p = ((m2kn90jmn9xt(obj, f3q8ui93gj7y.getFirst().pos()) > Double.longBitsToDouble(4609434218613702656L)) ? mdy84gefhdll(f3q8ui93gj7y, obj) : 0);
            this.f5rog0nkusr9 = this.fykr8qdje7p;
            this.miftyhnyf44a(f3q8ui93gj7y, obj);
        }
        for (int i = 0; i < 4; ++i) {
            if (this.fykr8qdje7p >= f3q8ui93gj7y.size()) {
                return new Steer(0.0, 0.0, false, false, false, true);
            }
            if (!this.mgh5d2nivg9o(f3q8ui93gj7y.get(this.fykr8qdje7p), obj)) {
                break;
            }
            ++this.fykr8qdje7p;
            this.miftyhnyf44a(f3q8ui93gj7y, obj);
        }
        if (this.fykr8qdje7p >= f3q8ui93gj7y.size()) {
            return new Steer(0.0, 0.0, false, false, false, true);
        }
        while (this.fykr8qdje7p < f3q8ui93gj7y.size() && m44s955w2xge(f3q8ui93gj7y.get(this.fykr8qdje7p), obj)) {
            ++this.fykr8qdje7p;
        }
        if (this.fykr8qdje7p >= f3q8ui93gj7y.size()) {
            return new Steer(0.0, 0.0, false, false, false, true);
        }
        if (this.fykr8qdje7p != this.f5rog0nkusr9) {
            this.f5rog0nkusr9 = this.fykr8qdje7p;
            this.miftyhnyf44a(f3q8ui93gj7y, obj);
        }
        if (!this.fd4slc7uh49b) {
            this.fd6htw5qkjqr = obj.x;
            this.f44qnmmxep73 = obj.z;
            this.f2odu5qua5gd = 0;
            this.fd4slc7uh49b = true;
        }
        else {
            final double hypot = Math.hypot(obj.x - this.fd6htw5qkjqr, obj.z - this.f44qnmmxep73);
            if (b && hypot < Double.longBitsToDouble(4587366580439587226L)) {
                ++this.f2odu5qua5gd;
            }
            else {
                this.f2odu5qua5gd = 0;
                this.fd6htw5qkjqr = obj.x;
                this.f44qnmmxep73 = obj.z;
            }
        }
        final TravelData travelData = f3q8ui93gj7y.get(this.fykr8qdje7p);
        final Vector3d pos = travelData.pos();
        final double x = pos.x - obj.x;
        final double n = pos.z - obj.z;
        final double hypot2 = Math.hypot(x, n);
        final double a = pos.y - obj.y;
        final double degrees = Math.toDegrees(Math.atan2(-x, n));
        ++this.fgz891iq17rs;
        final boolean b4 = jumpProbe == null || jumpProbe.clear((int)Math.floor(pos.x), pos.y, (int)Math.floor(pos.z));
        final boolean b5 = jumpProbe == null || jumpProbe.clear((int)Math.floor(obj.x), obj.y, (int)Math.floor(obj.z));
        return switch (travelData.type()) {
            case PILLAR,  BRIDGE -> new Steer(degrees, 0.0, false, false, false, false);
            case PARKOUR,  PARKOUR_ASCEND -> {
                final Vector3d m23ucfirq4n0 = m23ucfirq4n0(f3q8ui93gj7y, this.fykr8qdje7p);
                final double hypot3 = Math.hypot(pos.x - m23ucfirq4n0.x, pos.z - m23ucfirq4n0.z);
                final double n2 = (obj.x - m23ucfirq4n0.x) * ((hypot3 < Double.longBitsToDouble(4472406533629990549L)) ? (x / Math.max(hypot2, Double.longBitsToDouble(4472406533629990549L))) : ((pos.x - m23ucfirq4n0.x) / hypot3)) + (obj.z - m23ucfirq4n0.z) * ((hypot3 < Double.longBitsToDouble(4472406533629990549L)) ? (n / Math.max(hypot2, Double.longBitsToDouble(4472406533629990549L))) : ((pos.z - m23ucfirq4n0.z) / hypot3));
                final boolean b6 = b3 && !b2 && hypot3 >= Double.longBitsToDouble(4615063718147915776L);
                final boolean b7 = b && !this.f6l253mh7llf && n2 > Double.longBitsToDouble(4602678819172646912L) && b4;
                if (b7) {
                    this.f6l253mh7llf = true;
                }
                yield new Steer(degrees, 0.0, true, b6, b7, false);
            }
            case ASCEND,  DIAGONAL_ASCEND -> {
                boolean b8 = Math.hypot(pos.x - obj.x, pos.z - obj.z) <= Double.longBitsToDouble(4608083138725491507L) && b4;
                if (!b8 && b && this.f2odu5qua5gd >= 15 && b5) {
                    b8 = true;
                }
                if (this.f2odu5qua5gd >= 15) {
                    this.f2odu5qua5gd = 0;
                }
                yield new Steer(degrees, 0.0, true, false, b8, false);
            }
            case FALL,  DESCEND -> new Steer(degrees, 0.0, true, false, false, false);
            case SWIM -> new Steer(degrees, 0.0, true, false, b2 && a > Double.longBitsToDouble(-4620693217682128896L), false);
            case CLIMB -> new Steer(degrees, 0.0, true, false, a > Double.longBitsToDouble(4599075939470750515L), false);
            default -> {
                final boolean b9 = b3 && !b2 && Math.abs(a) < Double.longBitsToDouble(4603579539098121011L) && hypot2 > Double.longBitsToDouble(4612811918334230528L);
                boolean b10 = false;
                if (b && this.f2odu5qua5gd >= 15 && b5) {
                    b10 = true;
                }
                if (this.f2odu5qua5gd >= 15) {
                    this.f2odu5qua5gd = 0;
                }
                yield new Steer(degrees, 0.0, true, b9, b10, false);
            }
        };
    }
    
    private void miftyhnyf44a(final List<TravelData> list, final Vector3d vector3d) {
        this.fgz891iq17rs = 0;
        this.f6l253mh7llf = false;
        final Vector3d vector3d2 = (list.size() == 1) ? vector3d : m23ucfirq4n0(list, this.fykr8qdje7p);
        this.f13p2y486nxx = vector3d2.x;
        this.f4841uf5d4kf = vector3d2.y;
        this.f9ud9jioyt1y = vector3d2.z;
    }
    
    private static Vector3d m23ucfirq4n0(final List<TravelData> list, final int n) {
        if (n > 0) {
            return list.get(n - 1).pos();
        }
        return list.get(0).pos();
    }
    
    private boolean mgh5d2nivg9o(final TravelData travelData, final Vector3d vector3d) {
        if (this.fgz891iq17rs <= travelData.costTicks() + Double.longBitsToDouble(4636737291354636288L)) {
            return switch (travelData.type()) {
                case FALL,  DESCEND,  SWIM -> false;
                default -> vector3d.y < Math.min(this.f4841uf5d4kf, travelData.pos().y) - Double.longBitsToDouble(4603579539098121011L);
            };
        }
        return true;
    }
    
    private static boolean m44s955w2xge(final TravelData travelData, final Vector3d vector3d) {
        final Vector3d pos = travelData.pos();
        final double x = pos.x - vector3d.x;
        final double y = pos.z - vector3d.z;
        final double abs = Math.abs(pos.y - vector3d.y);
        return switch (travelData.type()) {
            case PILLAR,  PARKOUR_ASCEND,  ASCEND,  DIAGONAL_ASCEND,  
                 CLIMB -> Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L) && abs < Double.longBitsToDouble(4600877379321698714L);
            case FALL,  DESCEND -> Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L) && abs < 1.0;
            default -> Math.hypot(x, y) < Double.longBitsToDouble(4604480259023595110L) && abs < Double.longBitsToDouble(4609884578576439706L);
        };
    }
    
    private static double m2kn90jmn9xt(final Vector3d vector3d, final Vector3d vector3d2) {
        return Math.hypot(vector3d2.x - vector3d.x, vector3d2.z - vector3d.z);
    }
    
    private static int mdy84gefhdll(final List<TravelData> list, final Vector3d vector3d) {
        int n = 0;
        double longBitsToDouble = Double.longBitsToDouble(9218868437227405312L);
        double longBitsToDouble2 = Double.longBitsToDouble(9218868437227405312L);
        for (int i = 0; i < list.size(); ++i) {
            final Vector3d pos = list.get(i).pos();
            final double m2kn90jmn9xt = m2kn90jmn9xt(vector3d, pos);
            final double abs = Math.abs(pos.y - vector3d.y);
            if (m2kn90jmn9xt < longBitsToDouble - Double.longBitsToDouble(4472406533629990549L) || (m2kn90jmn9xt <= longBitsToDouble + Double.longBitsToDouble(4472406533629990549L) && (abs < longBitsToDouble2 - Double.longBitsToDouble(4472406533629990549L) || (Math.abs(abs - longBitsToDouble2) <= Double.longBitsToDouble(4472406533629990549L) && i > n)))) {
                n = i;
                longBitsToDouble = m2kn90jmn9xt;
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
        if (obj.floor((int)Math.floor(obj2.x), (int)Math.floor(obj2.z), obj2.y) == null) {
            return false;
        }
        for (int i = 1; i <= 2; ++i) {
            final Double floor = obj.floor((int)Math.floor(obj2.x + n * i), (int)Math.floor(obj2.z + cos * i), obj2.y);
            if (floor != null && floor >= obj2.y - Double.longBitsToDouble(4602678819172646912L)) {
                return false;
            }
        }
        return true;
    }
    
    public interface JumpProbe
    {
        boolean clear(final int p0, final double p1, final int p2);
    }
    
    public record Steer(double yawDeg, double pitchDeg, boolean forward, boolean sprint, boolean jump, boolean done) {}
    
    public interface FloorProbe
    {
        Double floor(final int p0, final int p1, final double p2);
    }
}
