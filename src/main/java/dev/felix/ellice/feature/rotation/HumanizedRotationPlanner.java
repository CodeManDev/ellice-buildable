



package dev.felix.ellice.feature.rotation;

import java.util.Optional;
import java.util.Random;

public final class HumanizedRotationPlanner
{
    private final RotationVanillaGcdService fev5a07vxm0q;
    private final Random f5y18ehgwkn7;
    private final RotationSynchronizeService f7lgy86i59ya;
    private RotationData fa8ahrpqghm;
    private final double f5ls7nqr62g3;
    private Integer fgheazd560hl;
    private int f87l6w1spk60;
    private boolean f5otln66mf0y;
    private double f3zx3vq0eft4;
    private double fiyiso9cfo6u;
    private RotationData f86ynouyzakx;
    private int fearmqp5ec32;
    private int fc1xrldbbyr0;
    private boolean f9wroii5l71o;
    private int fjowozlomxgy;
    private int f7dvbohsi6rw;
    private int f6usk3qvyd78;
    private double fdk5kdhpdsmt;
    private double f64jsonus54i;
    private double f7wr9962hjhb;
    private double fhwty73bnwkx;
    private double fhvu5rhm8kga;
    private double f1145opdcvwp;
    private double f8f3ti08wxwr;
    private RotationData fi40mweed061;
    private int fisfxpz818n4;
    private int f3jsttcwlw4w;
    private RotationData fht3qiwpxoxh;
    private int f5i0wc5c2q9b;
    private double fde3wbn57o8r;
    private double f9m2emxh7bw8;
    private double f4vzlfbi512q;
    private boolean f6z872lkmpyr;
    private double fby1jsdo6j6w;
    private double f5pu7dmz9tns;
    private double fe66o0b2f7bj;
    private double ffjcs1apnxy8;
    private int f2note451ysu;
    
    public HumanizedRotationPlanner(final long seed) {
        this.fev5a07vxm0q = new RotationVanillaGcdService();
        this.f7lgy86i59ya = new RotationSynchronizeService();
        this.fe66o0b2f7bj = 1.0;
        this.ffjcs1apnxy8 = 1.0;
        this.f5y18ehgwkn7 = new Random(seed);
        this.f5ls7nqr62g3 = Double.longBitsToDouble(4606101554889448489L) + this.f5y18ehgwkn7.nextDouble() * Double.longBitsToDouble(4597814931575086776L);
    }
    
    public Optional<Step> next(final RotationData rotationData, final RotationData rotationData2, final int n, final double n2, final RotationHumanizationProfile rotationHumanizationProfile) {
        return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, false, 1.0);
    }
    
    public Optional<Step> next(final RotationData rotationData, final RotationData rotationData2, final int n, final double n2, final RotationHumanizationProfile rotationHumanizationProfile, final boolean b) {
        return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, b, 1.0);
    }
    
    public Optional<Step> next(final RotationData rotationData, final RotationData rotationData2, final int n, final double n2, final RotationHumanizationProfile rotationHumanizationProfile, final boolean b, final double n3) {
        return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, b, n3, null);
    }
    
    public Optional<Step> next(final RotationData rotationData, RotationData withPitchClamped, final int n, final double n2, final RotationHumanizationProfile rotationHumanizationProfile, final boolean b, final double d, final RotationData rotationData2) {
        withPitchClamped = withPitchClamped.withPitchClamped();
        if (!Double.isFinite(d) || d <= 0.0) {
            throw new IllegalArgumentException("angularTargetWidth must be positive");
        }
        if (this.fgheazd560hl == null || this.fgheazd560hl != n) {
            this.m62j90xwmq8s(rotationData, withPitchClamped, n, rotationHumanizationProfile);
        }
        if (b) {
            this.f87l6w1spk60 = 0;
            this.f5otln66mf0y = false;
        }
        final boolean b2 = rotationHumanizationProfile.smoothness() == 0.0 && rotationHumanizationProfile.jitterIntensity() == 0.0 && !rotationHumanizationProfile.reactionDelay() && !rotationHumanizationProfile.overshoot() && !rotationHumanizationProfile.snapVariation() && !rotationHumanizationProfile.hesitation();
        final RotationData rotationData3 = (rotationHumanizationProfile.reactionDelay() && this.fa8ahrpqghm != null) ? this.fa8ahrpqghm : withPitchClamped;
        this.fa8ahrpqghm = withPitchClamped;
        RotationData rotationData4 = this.m8werzpnjiwt(rotationData3, d, b2);
        final boolean b3 = rotationData2 != null && rotationHumanizationProfile.jitterIntensity() > 0.0 && !b2;
        if (b3) {
            rotationData4 = new RotationData(rotationData4.yaw() + RotationData.yawDelta(withPitchClamped.yaw(), rotationData2.yaw()), rotationData4.pitch() + rotationData2.pitch() - withPitchClamped.pitch()).withPitchClamped();
        }
        if (this.f87l6w1spk60 <= 0) {
            final double max = Math.max(Double.longBitsToDouble(4517329193108106637L), this.fev5a07vxm0q.vanillaGcd(n2) + rotationHumanizationProfile.gcdOffsetDegrees());
            final double max2 = Math.max(Double.longBitsToDouble(4585204852618449388L), Math.min(Double.longBitsToDouble(4593311331947716280L), d * Double.longBitsToDouble(4576918229304087675L)));
            final boolean b4 = !b2 && this.f3jsttcwlw4w == 0 && Math.hypot(this.fde3wbn57o8r, this.f9m2emxh7bw8) > max2;
            if (b2) {
                this.fht3qiwpxoxh = rotationData4;
                this.f86ynouyzakx = null;
                this.f5i0wc5c2q9b = 0;
                this.m46eg7kgunij();
            }
            else if (b4 || (b3 && RotationData.distance(rotationData, rotationData4) <= Math.max(1.0, d * Double.longBitsToDouble(4602678819172646912L)))) {
                this.f6z872lkmpyr = true;
                this.f5otln66mf0y = false;
                this.f86ynouyzakx = null;
                this.f5i0wc5c2q9b = 0;
                this.f7dvbohsi6rw = 0;
                this.f9wroii5l71o = true;
                this.fjowozlomxgy = 0;
            }
            if (this.f6z872lkmpyr) {
                this.fht3qiwpxoxh = rotationData4;
            }
            final double max3 = Math.max(max * Double.longBitsToDouble(4611686018427387904L), Math.min(Double.longBitsToDouble(4609434218613702656L), d * Double.longBitsToDouble(4595653203753948938L)));
            if (this.f5i0wc5c2q9b > 0) {
                --this.f5i0wc5c2q9b;
            }
            if (!this.f6z872lkmpyr && !this.f5otln66mf0y && !b && this.f5i0wc5c2q9b == 0 && RotationData.distance(this.fht3qiwpxoxh, rotationData4) > max3) {
                this.fht3qiwpxoxh = this.m59vc8l5ktk4(rotationData4, d);
                this.f5i0wc5c2q9b = 2 + this.f5y18ehgwkn7.nextInt(3);
                this.m8afljvv517e(rotationData, this.fht3qiwpxoxh, rotationHumanizationProfile, d, true);
                if (rotationHumanizationProfile.hesitation() && this.f5y18ehgwkn7.nextDouble() < Double.longBitsToDouble(4595653203753948938L)) {
                    this.f7dvbohsi6rw = 1;
                }
            }
            RotationData rotationData5 = this.m3gduoitau71(this.fht3qiwpxoxh);
            if (!this.f6z872lkmpyr && this.f86ynouyzakx == null) {
                this.m8afljvv517e(rotationData, rotationData5, rotationHumanizationProfile, d, false);
            }
            final double max4 = Math.max(Double.longBitsToDouble(4601778099247172813L), max * Double.longBitsToDouble(4609434218613702656L));
            if (this.f5otln66mf0y && RotationData.distance(rotationData, rotationData5) <= max4) {
                this.f5otln66mf0y = false;
                this.fht3qiwpxoxh = this.m59vc8l5ktk4(rotationData4, d);
                rotationData5 = this.fht3qiwpxoxh;
                this.m8afljvv517e(rotationData, rotationData5, rotationHumanizationProfile, d, true);
                if (rotationHumanizationProfile.hesitation() && this.f5y18ehgwkn7.nextDouble() < Double.longBitsToDouble(4599976659396224614L)) {
                    this.f7dvbohsi6rw = 1;
                }
            }
            final double distance = RotationData.distance(rotationData, rotationData4);
            final double max5 = Math.max(Double.longBitsToDouble(4603129179135383962L), max * Double.longBitsToDouble(4611686018427387904L));
            final boolean b5 = !this.f5otln66mf0y && !b3 && Math.abs(this.fby1jsdo6j6w) <= mfgm50hxq29l(rotationHumanizationProfile.maxTurnSpeed()) && Math.abs(this.f5pu7dmz9tns) <= mfgm50hxq29l(rotationHumanizationProfile.maxTurnSpeed()) && ((b && this.f3jsttcwlw4w >= 2 && distance <= max5) || (this.f3jsttcwlw4w >= 3 && distance <= max5));
            RotationData rotationData6;
            if (b5) {
                this.mbm4i357b1nm();
                this.m46eg7kgunij();
                rotationData6 = (b ? rotationData : rotationData4);
            }
            else if (this.f6z872lkmpyr) {
                final RotationData rotationData7 = (rotationHumanizationProfile.reactionDelay() && b4) ? this.m59vc8l5ktk4(rotationData4, d) : rotationData4;
                final double n3 = rotationHumanizationProfile.jitterIntensity() * Math.min(1.0, RotationData.distance(rotationData, rotationData7) / Double.longBitsToDouble(4613937818241073152L));
                RotationData rotationData8 = this.mgqs7rui2nm3(rotationData7, n3);
                final double distance2 = RotationData.distance(rotationData7, rotationData8);
                final double n4 = Math.min(Double.longBitsToDouble(4599075939470750515L), d * Double.longBitsToDouble(4593311331947716280L)) * n3;
                if (distance2 > n4 && distance2 > 0.0) {
                    rotationData8 = RotationData.interpolate(rotationData7, rotationData8, n4 / distance2);
                }
                rotationData6 = this.m25fb2r09lxc(rotationData, rotationData8.withPitchClamped(), rotationHumanizationProfile);
            }
            else {
                final RotationData m7ijgvztb2ly = this.m7ijgvztb2ly(rotationData, rotationData5, rotationHumanizationProfile);
                final double distance3 = RotationData.distance(rotationData, m7ijgvztb2ly);
                final double n5 = rotationHumanizationProfile.jitterIntensity() * Math.min(1.0, Math.min(1.0, distance3 / Double.longBitsToDouble(4620130267728707584L)) * Double.longBitsToDouble(4605561122934164029L) + Math.min(1.0, Math.abs(distance3 - this.f8f3ti08wxwr) / Double.longBitsToDouble(4617315517961601024L)) * Double.longBitsToDouble(4595653203753948938L));
                this.f8f3ti08wxwr = distance3;
                rotationData6 = this.mgqs7rui2nm3(this.mhbtj2nog9es(rotationData, rotationData5, this.maoqjsnp6pw(rotationData, m7ijgvztb2ly, n5), rotationHumanizationProfile, max), n5).withPitchClamped();
                if (!b2) {
                    rotationData6 = mf96dnus5uun(rotationData, rotationData6, rotationHumanizationProfile.maxTurnSpeed(), rotationHumanizationProfile.maxPitchSpeed());
                }
            }
            final RotationVanillaGcdService.QuantizedRotation quantizedRotation = b2 ? this.fev5a07vxm0q.quantize(rotationData, rotationData6, n2, rotationHumanizationProfile.gcdOffsetDegrees()) : this.f7lgy86i59ya.next(rotationData, rotationData6, n2, rotationHumanizationProfile.gcdOffsetDegrees(), rotationHumanizationProfile.maxTurnSpeed(), !this.f6z872lkmpyr);
            if (b2) {
                this.f7lgy86i59ya.synchronize(rotationData, quantizedRotation.rotation());
            }
            this.fby1jsdo6j6w = RotationData.yawDelta(rotationData.yaw(), quantizedRotation.rotation().yaw());
            this.f5pu7dmz9tns = quantizedRotation.rotation().pitch() - rotationData.pitch();
            final boolean b6 = b5 && quantizedRotation.yawMouseCounts() == 0L && quantizedRotation.pitchMouseCounts() == 0L;
            return Optional.of(new Step(quantizedRotation.rotation(), this.f5otln66mf0y ? Phase.OVERSHOOT : ((b6 || RotationData.distance(quantizedRotation.rotation(), rotationData4) <= max4) ? Phase.TRACKING : Phase.CORRECTION), n, quantizedRotation.vanillaGcd(), quantizedRotation.effectiveGcd(), quantizedRotation.yawMouseCounts(), quantizedRotation.pitchMouseCounts(), b6));
        }
        --this.f87l6w1spk60;
        if (!this.f7lgy86i59ya.moving()) {
            return Optional.empty();
        }
        final RotationVanillaGcdService.QuantizedRotation next = this.f7lgy86i59ya.next(rotationData, rotationData, n2, rotationHumanizationProfile.gcdOffsetDegrees(), rotationHumanizationProfile.maxTurnSpeed());
        return Optional.of(new Step(next.rotation(), Phase.CORRECTION, n, next.vanillaGcd(), next.effectiveGcd(), next.yawMouseCounts(), next.pitchMouseCounts(), false));
    }
    
    public void reset() {
        this.f7lgy86i59ya.reset();
        this.resetTarget();
    }
    
    public void synchronizeMotion(final RotationData rotationData, final RotationData rotationData2) {
        this.f7lgy86i59ya.synchronize(rotationData, rotationData2);
    }
    
    public RotationData continueRotation(final RotationData rotationData, final RotationData rotationData2, final double n, final double n2) {
        return this.f7lgy86i59ya.next(rotationData, rotationData2, n, 0.0, n2).rotation();
    }
    
    public void resetTarget() {
        this.fa8ahrpqghm = null;
        this.fgheazd560hl = null;
        this.f87l6w1spk60 = 0;
        this.f86ynouyzakx = null;
        this.f9wroii5l71o = false;
        this.fjowozlomxgy = 0;
        this.f7dvbohsi6rw = 0;
        this.f6usk3qvyd78 = 0;
        this.fdk5kdhpdsmt = 0.0;
        this.f64jsonus54i = 0.0;
        this.f7wr9962hjhb = 0.0;
        this.fhwty73bnwkx = 0.0;
        this.fhvu5rhm8kga = 0.0;
        this.f1145opdcvwp = 0.0;
        this.f8f3ti08wxwr = 0.0;
        this.fi40mweed061 = null;
        this.fisfxpz818n4 = 0;
        this.f3jsttcwlw4w = 0;
        this.fht3qiwpxoxh = null;
        this.f5i0wc5c2q9b = 0;
        this.fde3wbn57o8r = 0.0;
        this.f9m2emxh7bw8 = 0.0;
        this.f4vzlfbi512q = 0.0;
        this.m46eg7kgunij();
    }
    
    public int reactionTicksRemaining() {
        return this.f87l6w1spk60;
    }
    
    private void m62j90xwmq8s(final RotationData rotationData, final RotationData fi40mweed061, final int i, final RotationHumanizationProfile rotationHumanizationProfile) {
        this.fgheazd560hl = i;
        this.fa8ahrpqghm = fi40mweed061;
        this.f87l6w1spk60 = (rotationHumanizationProfile.reactionDelay() ? this.m43jjqlwttc2(rotationHumanizationProfile) : 0);
        this.f5otln66mf0y = (rotationHumanizationProfile.overshoot() && RotationData.distance(rotationData, fi40mweed061) >= Double.longBitsToDouble(4620693217682128896L) && this.f5y18ehgwkn7.nextDouble() < Double.longBitsToDouble(4601237667291888353L));
        double signum = Math.signum(RotationData.yawDelta(rotationData.yaw(), fi40mweed061.yaw()));
        final double yawDelta = RotationData.yawDelta(rotationData.yaw(), fi40mweed061.yaw());
        final double y = fi40mweed061.pitch() - rotationData.pitch();
        final double hypot = Math.hypot(yawDelta, y);
        final double n = rotationHumanizationProfile.overshootMinDegrees() + this.f5y18ehgwkn7.nextDouble() * Math.max(0.0, rotationHumanizationProfile.overshootMaxDegrees() - rotationHumanizationProfile.overshootMinDegrees());
        if (hypot < Double.longBitsToDouble(4472406533629990549L)) {
            if (signum == 0.0) {
                signum = (this.f5y18ehgwkn7.nextBoolean() ? 1.0 : Double.longBitsToDouble(-4616189618054758400L));
            }
            this.f3zx3vq0eft4 = signum * n;
            this.fiyiso9cfo6u = 0.0;
        }
        else {
            final double n2 = yawDelta / hypot;
            final double n3 = y / hypot;
            final double n4 = this.f5y18ehgwkn7.nextGaussian() * n * Double.longBitsToDouble(4590429028186199163L);
            this.f3zx3vq0eft4 = n2 * n - n3 * n4;
            this.fiyiso9cfo6u = n3 * n + n2 * n4;
        }
        this.fht3qiwpxoxh = fi40mweed061;
        this.f86ynouyzakx = null;
        this.f9wroii5l71o = false;
        this.fjowozlomxgy = 0;
        this.fi40mweed061 = fi40mweed061;
        this.fisfxpz818n4 = 0;
        this.f3jsttcwlw4w = 1;
        this.f5i0wc5c2q9b = 2 + this.f5y18ehgwkn7.nextInt(3);
        this.fde3wbn57o8r = 0.0;
        this.f9m2emxh7bw8 = 0.0;
        this.m46eg7kgunij();
    }
    
    private RotationData m8werzpnjiwt(final RotationData rotationData, final double n, final boolean b) {
        if (this.fi40mweed061 == null) {
            this.fi40mweed061 = rotationData;
            this.f3jsttcwlw4w = 1;
            this.fisfxpz818n4 = 0;
            return rotationData;
        }
        final double yawDelta = RotationData.yawDelta(this.fi40mweed061.yaw(), rotationData.yaw());
        final double y = rotationData.pitch() - this.fi40mweed061.pitch();
        final double hypot = Math.hypot(yawDelta, y);
        ++this.fisfxpz818n4;
        if (hypot + Double.longBitsToDouble(4427486594234968593L) >= (b ? 0.0 : Math.max(Double.longBitsToDouble(4582862980812216730L), Math.min(Double.longBitsToDouble(4597094355634707497L), n * Double.longBitsToDouble(4582862980812216730L))))) {
            final double n2 = Math.max(1, this.fisfxpz818n4);
            this.fde3wbn57o8r = this.fde3wbn57o8r * Double.longBitsToDouble(4603759683083215831L) + yawDelta / n2 * Double.longBitsToDouble(4600517091351509074L);
            this.f9m2emxh7bw8 = this.f9m2emxh7bw8 * Double.longBitsToDouble(4603759683083215831L) + y / n2 * Double.longBitsToDouble(4600517091351509074L);
            this.fi40mweed061 = rotationData;
            this.fisfxpz818n4 = 0;
            this.f3jsttcwlw4w = 0;
        }
        else {
            this.fde3wbn57o8r *= Double.longBitsToDouble(4605200834963974390L);
            this.f9m2emxh7bw8 *= Double.longBitsToDouble(4605200834963974390L);
            ++this.f3jsttcwlw4w;
        }
        return this.fi40mweed061;
    }
    
    private RotationData m3gduoitau71(final RotationData rotationData) {
        if (!this.f5otln66mf0y) {
            return rotationData;
        }
        return new RotationData(rotationData.yaw() + this.f3zx3vq0eft4, rotationData.pitch() + this.fiyiso9cfo6u).withPitchClamped();
    }
    
    private void m8afljvv517e(final RotationData f86ynouyzakx, final RotationData rotationData, final RotationHumanizationProfile rotationHumanizationProfile, final double b, final boolean b2) {
        this.f86ynouyzakx = f86ynouyzakx;
        this.fearmqp5ec32 = 0;
        final double distance = RotationData.distance(f86ynouyzakx, rotationData);
        final int max = Math.max(1, (int)Math.ceil(distance / rotationHumanizationProfile.maxTurnSpeed()));
        if (rotationHumanizationProfile.smoothness() == 0.0) {
            this.fc1xrldbbyr0 = 1;
        }
        else {
            this.fc1xrldbbyr0 = Math.max(max, Math.max(1, (int)Math.ceil(((b2 ? Double.longBitsToDouble(4630122629401935872L) : Double.longBitsToDouble(4632937379169042432L)) + (b2 ? Double.longBitsToDouble(4628574517030027264L) : Double.longBitsToDouble(4631107791820423168L)) * mdke0ot2ny3p(1.0 + distance / Math.max(Double.longBitsToDouble(4591870180066957722L), b))) * this.f5ls7nqr62g3 * (Double.longBitsToDouble(4603399395113026191L) + rotationHumanizationProfile.smoothness() * Double.longBitsToDouble(4603759683083215831L)) / Double.longBitsToDouble(4632233691727265792L))));
        }
        this.f4vzlfbi512q = this.f5y18ehgwkn7.nextGaussian() * Math.min(Double.longBitsToDouble(4604480259023595110L), distance * Double.longBitsToDouble(4582862980812216730L)) * rotationHumanizationProfile.jitterIntensity();
        this.f9wroii5l71o = false;
        this.fjowozlomxgy = 0;
    }
    
    private RotationData m7ijgvztb2ly(final RotationData rotationData, final RotationData rotationData2, final RotationHumanizationProfile rotationHumanizationProfile) {
        if (rotationHumanizationProfile.smoothness() == 0.0) {
            return rotationData2;
        }
        if (this.f7dvbohsi6rw > 0) {
            --this.f7dvbohsi6rw;
            return rotationData;
        }
        this.fearmqp5ec32 = Math.min(this.fc1xrldbbyr0, this.fearmqp5ec32 + 1);
        final double m8nfsodqhngp = m8nfsodqhngp(this.fearmqp5ec32 / (double)this.fc1xrldbbyr0);
        return mf96dnus5uun(rotationData, this.m48itpo6ao2g(RotationData.interpolate(this.f86ynouyzakx, rotationData2, m8nfsodqhngp), rotationData2, m8nfsodqhngp), rotationHumanizationProfile.maxTurnSpeed(), rotationHumanizationProfile.maxPitchSpeed());
    }
    
    private RotationData m25fb2r09lxc(final RotationData rotationData, final RotationData rotationData2, final RotationHumanizationProfile rotationHumanizationProfile) {
        if (this.f2note451ysu <= 0) {
            this.ffjcs1apnxy8 = 1.0 + this.mdmi9tsbmuvr(Double.longBitsToDouble(-4631501856787818086L), Double.longBitsToDouble(4591870180066957722L)) * rotationHumanizationProfile.jitterIntensity();
            this.f2note451ysu = 4 + this.f5y18ehgwkn7.nextInt(7);
        }
        --this.f2note451ysu;
        this.fe66o0b2f7bj += (this.ffjcs1apnxy8 - this.fe66o0b2f7bj) * Double.longBitsToDouble(4596373779694328218L);
        final double n = this.f5ls7nqr62g3 * this.fe66o0b2f7bj;
        final double mgh35dknrrgy = mgh35dknrrgy(1.0 - rotationHumanizationProfile.smoothness() * Double.longBitsToDouble(4603129179135383962L) * n, Double.longBitsToDouble(4599976659396224614L), 1.0);
        final double mgh35dknrrgy2 = mgh35dknrrgy((Double.longBitsToDouble(4604029899060858061L) + (1.0 - rotationHumanizationProfile.smoothness()) * Double.longBitsToDouble(4599075939470750515L)) / n, Double.longBitsToDouble(4603129179135383962L), 1.0);
        return new RotationData(rotationData.yaw() + m13wquvwvq7e(RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw()), (this.f3jsttcwlw4w == 0) ? this.fde3wbn57o8r : 0.0, this.fby1jsdo6j6w, mgh35dknrrgy, mgh35dknrrgy2, rotationHumanizationProfile.maxTurnSpeed()), rotationData.pitch() + m13wquvwvq7e(rotationData2.pitch() - rotationData.pitch(), (this.f3jsttcwlw4w == 0) ? this.f9m2emxh7bw8 : 0.0, this.f5pu7dmz9tns, mgh35dknrrgy, mgh35dknrrgy2, rotationHumanizationProfile.maxPitchSpeed())).withPitchClamped();
    }
    
    private static double m13wquvwvq7e(final double n, final double n2, double n3, final double n4, final double n5, final double n6) {
        final double n7 = n * n4 + n2 * (1.0 - n4);
        if (n * n3 < 0.0) {
            n3 = 0.0;
        }
        final double n8 = n3 + (n7 - n3) * n5;
        return mgh35dknrrgy((n >= 0.0) ? mgh35dknrrgy(n8, 0.0, n) : mgh35dknrrgy(n8, n, 0.0), -n6, n6);
    }
    
    private static double mfgm50hxq29l(final double n) {
        return RotationSynchronizeService.accelerationLimit(n);
    }
    
    private void m46eg7kgunij() {
        this.f6z872lkmpyr = false;
        this.fby1jsdo6j6w = 0.0;
        this.f5pu7dmz9tns = 0.0;
        this.fe66o0b2f7bj = 1.0;
        this.ffjcs1apnxy8 = 1.0;
        this.f2note451ysu = 0;
    }
    
    private RotationData m48itpo6ao2g(final RotationData rotationData, final RotationData rotationData2, final double n) {
        if (Math.abs(this.f4vzlfbi512q) < Double.longBitsToDouble(4427486594234968593L)) {
            return rotationData;
        }
        final double yawDelta = RotationData.yawDelta(this.f86ynouyzakx.yaw(), rotationData2.yaw());
        final double y = rotationData2.pitch() - this.f86ynouyzakx.pitch();
        final double hypot = Math.hypot(yawDelta, y);
        if (hypot < Double.longBitsToDouble(4472406533629990549L)) {
            return rotationData;
        }
        final double n2 = 1.0 - n;
        final double n3 = Double.longBitsToDouble(4625196817309499392L) * n * n * n2 * n2;
        return new RotationData(rotationData.yaw() - y / hypot * this.f4vzlfbi512q * n3, rotationData.pitch() + yawDelta / hypot * this.f4vzlfbi512q * n3).withPitchClamped();
    }
    
    private RotationData m59vc8l5ktk4(final RotationData rotationData, final double n) {
        final double max = Math.max(n * Double.longBitsToDouble(4599976659396224614L), Math.hypot(this.fde3wbn57o8r, this.f9m2emxh7bw8));
        return new RotationData(rotationData.yaw() + mgh35dknrrgy(this.fde3wbn57o8r * Double.longBitsToDouble(4609434218613702656L), -max, max), rotationData.pitch() + mgh35dknrrgy(this.f9m2emxh7bw8 * Double.longBitsToDouble(4608308318706860032L), -max, max)).withPitchClamped();
    }
    
    private RotationData maoqjsnp6pw(final RotationData rotationData, final RotationData rotationData2, final double n) {
        final double yawDelta = RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw());
        final double n2 = rotationData2.pitch() - rotationData.pitch();
        if (n == 0.0) {
            this.fhvu5rhm8kga = yawDelta;
            this.f1145opdcvwp = n2;
            return rotationData2;
        }
        final double n3 = yawDelta - this.fhvu5rhm8kga;
        final double n4 = n2 - this.f1145opdcvwp;
        this.fhvu5rhm8kga = yawDelta;
        this.f1145opdcvwp = n2;
        final double n5 = Double.longBitsToDouble(4585925428558828667L) * n;
        return new RotationData(rotationData2.yaw() + n3 * this.mdmi9tsbmuvr(-n5, n5), rotationData2.pitch() + n4 * this.mdmi9tsbmuvr(-n5, n5));
    }
    
    private RotationData mhbtj2nog9es(final RotationData rotationData, final RotationData rotationData2, final RotationData rotationData3, final RotationHumanizationProfile rotationHumanizationProfile, final double n) {
        if (!rotationHumanizationProfile.snapVariation() || this.f5otln66mf0y) {
            return rotationData3;
        }
        final double distance = RotationData.distance(rotationData, rotationData2);
        final double min = Math.min(Double.longBitsToDouble(4618441417868443648L), Math.max(Double.longBitsToDouble(4612811918334230528L), n * Double.longBitsToDouble(4620693217682128896L)));
        if (!this.f9wroii5l71o && distance <= min) {
            this.fjowozlomxgy = (this.f5y18ehgwkn7.nextBoolean() ? 1 : (2 + this.f5y18ehgwkn7.nextInt(2)));
            this.f9wroii5l71o = true;
        }
        else if (this.f9wroii5l71o && distance > min * Double.longBitsToDouble(4611686018427387904L)) {
            this.f9wroii5l71o = false;
            this.fjowozlomxgy = 0;
        }
        if (this.fjowozlomxgy <= 0) {
            return rotationData3;
        }
        final RotationData interpolate = RotationData.interpolate(rotationData, rotationData2, 1.0 / this.fjowozlomxgy);
        --this.fjowozlomxgy;
        return interpolate;
    }
    
    private RotationData mgqs7rui2nm3(final RotationData rotationData, final double n) {
        if (n == 0.0) {
            return rotationData;
        }
        if (this.f6usk3qvyd78 <= 0) {
            this.f7wr9962hjhb = this.m5rdpq7ruzh9();
            this.fhwty73bnwkx = this.m5rdpq7ruzh9();
            this.f6usk3qvyd78 = 2 + this.f5y18ehgwkn7.nextInt(4);
        }
        --this.f6usk3qvyd78;
        this.fdk5kdhpdsmt += (this.f7wr9962hjhb - this.fdk5kdhpdsmt) * Double.longBitsToDouble(4598715651500560876L);
        this.f64jsonus54i += (this.fhwty73bnwkx - this.f64jsonus54i) * Double.longBitsToDouble(4597814931575086776L);
        return new RotationData(rotationData.yaw() + this.fdk5kdhpdsmt * n, rotationData.pitch() + this.f64jsonus54i * n);
    }
    
    private double m5rdpq7ruzh9() {
        final double n = Double.longBitsToDouble(4599075939470750515L) + this.f5y18ehgwkn7.nextDouble() * Double.longBitsToDouble(4602678819172646912L);
        return this.f5y18ehgwkn7.nextBoolean() ? n : (-n);
    }
    
    private void mbm4i357b1nm() {
        this.f7dvbohsi6rw = 0;
        this.f6usk3qvyd78 = 0;
        this.fdk5kdhpdsmt = 0.0;
        this.f64jsonus54i = 0.0;
        this.f7wr9962hjhb = 0.0;
        this.fhwty73bnwkx = 0.0;
        this.fhvu5rhm8kga = 0.0;
        this.f1145opdcvwp = 0.0;
        this.f8f3ti08wxwr = 0.0;
        this.f9wroii5l71o = true;
        this.fjowozlomxgy = 0;
    }
    
    private int m43jjqlwttc2(final RotationHumanizationProfile rotationHumanizationProfile) {
        final int reactionMinTicks = rotationHumanizationProfile.reactionMinTicks();
        final int reactionMaxTicks = rotationHumanizationProfile.reactionMaxTicks();
        if (reactionMinTicks == reactionMaxTicks) {
            return reactionMinTicks;
        }
        return (int)m7tyb7sr6ur6(Math.round(Math.exp(Math.log(Math.max(Double.longBitsToDouble(4602678819172646912L), (reactionMinTicks + reactionMaxTicks) * Double.longBitsToDouble(4602678819172646912L))) + this.f5y18ehgwkn7.nextGaussian() * Double.longBitsToDouble(4598715651500560876L))), reactionMinTicks, reactionMaxTicks);
    }
    
    private static RotationData m8qvs4nej8mm(final RotationData rotationData, final RotationData rotationData2, final double n) {
        return mf96dnus5uun(rotationData, rotationData2, n, n);
    }
    
    private static RotationData mf96dnus5uun(final RotationData rotationData, final RotationData rotationData2, final double a, final double a2) {
        return new RotationData(rotationData.yaw() + Math.max(-a, Math.min(a, RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw()))), rotationData.pitch() + Math.max(-a2, Math.min(a2, rotationData2.pitch() - rotationData.pitch())));
    }
    
    private double mdmi9tsbmuvr(final double n, final double n2) {
        return n + this.f5y18ehgwkn7.nextDouble() * (n2 - n);
    }
    
    private static double m8nfsodqhngp(final double b) {
        final double max = Math.max(0.0, Math.min(1.0, b));
        return max * max * max * (max * (max * Double.longBitsToDouble(4618441417868443648L) - Double.longBitsToDouble(4624633867356078080L)) + Double.longBitsToDouble(4621819117588971520L));
    }
    
    private static double mdke0ot2ny3p(final double a) {
        return Math.log(a) / Math.log(Double.longBitsToDouble(4611686018427387904L));
    }
    
    private static double mgh35dknrrgy(final double b, final double a, final double a2) {
        return Math.max(a, Math.min(a2, b));
    }
    
    private static long m7tyb7sr6ur6(final long b, final long a, final long a2) {
        return Math.max(a, Math.min(a2, b));
    }
    
    public record Step(RotationData rotation, Phase phase, int targetEntityId, double vanillaGcd, double effectiveGcd, long yawMouseCounts, long pitchMouseCounts, boolean settled) {}
    
    public enum Phase
    {
        OVERSHOOT, 
        CORRECTION, 
        TRACKING;
    }
}
