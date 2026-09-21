package dev.felix.ellice.feature.rotation;

import java.util.Optional;
import java.util.Random;

public final class HumanizedRotationPlanner {
  private final RotationVanillaGcdService fev5a07vxm0q;
  private final Random random;
  private final RotationSynchronizeService rotationSynchronizeService;
  private RotationData fa8ahrpqghm;
  private final double value;
  private Integer integer;
  private int count;
  private boolean enabled;
  private double value2;
  private double value3;
  private RotationData rotationData2;
  private int count2;
  private int count3;
  private boolean enabled2;
  private int fjowozlomxgy;
  private int count5;
  private int count6;
  private double fdk5kdhpdsmt;
  private double value5;
  private double value6;
  private double value7;
  private double value8;
  private double value9;
  private double value10;
  private RotationData rotationData3;
  private int count7;
  private int count8;
  private RotationData fht3qiwpxoxh;
  private int count9;
  private double value11;
  private double value12;
  private double value13;
  private boolean enabled3;
  private double value14;
  private double value15;
  private double value16;
  private double value17;
  private int count10;

  public HumanizedRotationPlanner(final long seed) {
    this.fev5a07vxm0q = new RotationVanillaGcdService();
    this.rotationSynchronizeService = new RotationSynchronizeService();
    this.value16 = 1.0;
    this.value17 = 1.0;
    this.random = new Random(seed);
    this.value =
        Double.longBitsToDouble(4606101554889448489L)
            + this.random.nextDouble() * Double.longBitsToDouble(4597814931575086776L);
  }

  public Optional<Step> next(
      final RotationData rotationData,
      final RotationData rotationData2,
      final int n,
      final double n2,
      final RotationHumanizationProfile rotationHumanizationProfile) {
    return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, false, 1.0);
  }

  public Optional<Step> next(
      final RotationData rotationData,
      final RotationData rotationData2,
      final int n,
      final double n2,
      final RotationHumanizationProfile rotationHumanizationProfile,
      final boolean b) {
    return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, b, 1.0);
  }

  public Optional<Step> next(
      final RotationData rotationData,
      final RotationData rotationData2,
      final int n,
      final double n2,
      final RotationHumanizationProfile rotationHumanizationProfile,
      final boolean b,
      final double n3) {
    return this.next(rotationData, rotationData2, n, n2, rotationHumanizationProfile, b, n3, null);
  }

  public Optional<Step> next(
      final RotationData rotationData,
      RotationData withPitchClamped,
      final int n,
      final double n2,
      final RotationHumanizationProfile rotationHumanizationProfile,
      final boolean b,
      final double d,
      final RotationData rotationData2) {
    withPitchClamped = withPitchClamped.withPitchClamped();
    if (!Double.isFinite(d) || d <= 0.0) {
      throw new IllegalArgumentException("angularTargetWidth must be positive");
    }
    if (this.integer == null || this.integer != n) {
      this.m62j90xwmq8s(rotationData, withPitchClamped, n, rotationHumanizationProfile);
    }
    if (b) {
      this.count = 0;
      this.enabled = false;
    }
    final boolean b2 =
        rotationHumanizationProfile.smoothness() == 0.0
            && rotationHumanizationProfile.jitterIntensity() == 0.0
            && !rotationHumanizationProfile.reactionDelay()
            && !rotationHumanizationProfile.overshoot()
            && !rotationHumanizationProfile.snapVariation()
            && !rotationHumanizationProfile.hesitation();
    final RotationData rotationData3 =
        (rotationHumanizationProfile.reactionDelay() && this.fa8ahrpqghm != null)
            ? this.fa8ahrpqghm
            : withPitchClamped;
    this.fa8ahrpqghm = withPitchClamped;
    RotationData rotationData4 = this.m8werzpnjiwt(rotationData3, d, b2);
    final boolean b3 =
        rotationData2 != null && rotationHumanizationProfile.jitterIntensity() > 0.0 && !b2;
    if (b3) {
      rotationData4 =
          new RotationData(
                  rotationData4.yaw()
                      + RotationData.yawDelta(withPitchClamped.yaw(), rotationData2.yaw()),
                  rotationData4.pitch() + rotationData2.pitch() - withPitchClamped.pitch())
              .withPitchClamped();
    }
    if (this.count <= 0) {
      final double max =
          Math.max(
              Double.longBitsToDouble(4517329193108106637L),
              this.fev5a07vxm0q.vanillaGcd(n2) + rotationHumanizationProfile.gcdOffsetDegrees());
      final double max2 =
          Math.max(
              Double.longBitsToDouble(4585204852618449388L),
              Math.min(
                  Double.longBitsToDouble(4593311331947716280L),
                  d * Double.longBitsToDouble(4576918229304087675L)));
      final boolean b4 = !b2 && this.count8 == 0 && Math.hypot(this.value11, this.value12) > max2;
      if (b2) {
        this.fht3qiwpxoxh = rotationData4;
        this.rotationData2 = null;
        this.count9 = 0;
        this.updateState3();
      } else if (b4
          || (b3
              && RotationData.distance(rotationData, rotationData4)
                  <= Math.max(1.0, d * Double.longBitsToDouble(4602678819172646912L)))) {
        this.enabled3 = true;
        this.enabled = false;
        this.rotationData2 = null;
        this.count9 = 0;
        this.count5 = 0;
        this.enabled2 = true;
        this.fjowozlomxgy = 0;
      }
      if (this.enabled3) {
        this.fht3qiwpxoxh = rotationData4;
      }
      final double max3 =
          Math.max(
              max * Double.longBitsToDouble(4611686018427387904L),
              Math.min(
                  Double.longBitsToDouble(4609434218613702656L),
                  d * Double.longBitsToDouble(4595653203753948938L)));
      if (this.count9 > 0) {
        --this.count9;
      }
      if (!this.enabled3
          && !this.enabled
          && !b
          && this.count9 == 0
          && RotationData.distance(this.fht3qiwpxoxh, rotationData4) > max3) {
        this.fht3qiwpxoxh = this.m59vc8l5ktk4(rotationData4, d);
        this.count9 = 2 + this.random.nextInt(3);
        this.m8afljvv517e(rotationData, this.fht3qiwpxoxh, rotationHumanizationProfile, d, true);
        if (rotationHumanizationProfile.hesitation()
            && this.random.nextDouble() < Double.longBitsToDouble(4595653203753948938L)) {
          this.count5 = 1;
        }
      }
      RotationData rotationData5 = this.createRotationData2(this.fht3qiwpxoxh);
      if (!this.enabled3 && this.rotationData2 == null) {
        this.m8afljvv517e(rotationData, rotationData5, rotationHumanizationProfile, d, false);
      }
      final double max4 =
          Math.max(
              Double.longBitsToDouble(4601778099247172813L),
              max * Double.longBitsToDouble(4609434218613702656L));
      if (this.enabled && RotationData.distance(rotationData, rotationData5) <= max4) {
        this.enabled = false;
        this.fht3qiwpxoxh = this.m59vc8l5ktk4(rotationData4, d);
        rotationData5 = this.fht3qiwpxoxh;
        this.m8afljvv517e(rotationData, rotationData5, rotationHumanizationProfile, d, true);
        if (rotationHumanizationProfile.hesitation()
            && this.random.nextDouble() < Double.longBitsToDouble(4599976659396224614L)) {
          this.count5 = 1;
        }
      }
      final double distance = RotationData.distance(rotationData, rotationData4);
      final double max5 =
          Math.max(
              Double.longBitsToDouble(4603129179135383962L),
              max * Double.longBitsToDouble(4611686018427387904L));
      final boolean b5 =
          !this.enabled
              && !b3
              && Math.abs(this.value14) <= mfgm50hxq29l(rotationHumanizationProfile.maxTurnSpeed())
              && Math.abs(this.value15) <= mfgm50hxq29l(rotationHumanizationProfile.maxTurnSpeed())
              && ((b && this.count8 >= 2 && distance <= max5)
                  || (this.count8 >= 3 && distance <= max5));
      RotationData rotationData6;
      if (b5) {
        this.updateState4();
        this.updateState3();
        rotationData6 = (b ? rotationData : rotationData4);
      } else if (this.enabled3) {
        final RotationData rotationData7 =
            (rotationHumanizationProfile.reactionDelay() && b4)
                ? this.m59vc8l5ktk4(rotationData4, d)
                : rotationData4;
        final double n3 =
            rotationHumanizationProfile.jitterIntensity()
                * Math.min(
                    1.0,
                    RotationData.distance(rotationData, rotationData7)
                        / Double.longBitsToDouble(4613937818241073152L));
        RotationData rotationData8 = this.mgqs7rui2nm3(rotationData7, n3);
        final double distance2 = RotationData.distance(rotationData7, rotationData8);
        final double n4 =
            Math.min(
                    Double.longBitsToDouble(4599075939470750515L),
                    d * Double.longBitsToDouble(4593311331947716280L))
                * n3;
        if (distance2 > n4 && distance2 > 0.0) {
          rotationData8 = RotationData.interpolate(rotationData7, rotationData8, n4 / distance2);
        }
        rotationData6 =
            this.m25fb2r09lxc(
                rotationData, rotationData8.withPitchClamped(), rotationHumanizationProfile);
      } else {
        final RotationData m7ijgvztb2ly =
            this.m7ijgvztb2ly(rotationData, rotationData5, rotationHumanizationProfile);
        final double distance3 = RotationData.distance(rotationData, m7ijgvztb2ly);
        final double n5 =
            rotationHumanizationProfile.jitterIntensity()
                * Math.min(
                    1.0,
                    Math.min(1.0, distance3 / Double.longBitsToDouble(4620130267728707584L))
                            * Double.longBitsToDouble(4605561122934164029L)
                        + Math.min(
                                1.0,
                                Math.abs(distance3 - this.value10)
                                    / Double.longBitsToDouble(4617315517961601024L))
                            * Double.longBitsToDouble(4595653203753948938L));
        this.value10 = distance3;
        rotationData6 =
            this.mgqs7rui2nm3(
                    this.mhbtj2nog9es(
                        rotationData,
                        rotationData5,
                        this.maoqjsnp6pw(rotationData, m7ijgvztb2ly, n5),
                        rotationHumanizationProfile,
                        max),
                    n5)
                .withPitchClamped();
        if (!b2) {
          rotationData6 =
              createRotationData11(
                  rotationData,
                  rotationData6,
                  rotationHumanizationProfile.maxTurnSpeed(),
                  rotationHumanizationProfile.maxPitchSpeed());
        }
      }
      final RotationVanillaGcdService.QuantizedRotation quantizedRotation =
          b2
              ? this.fev5a07vxm0q.quantize(
                  rotationData, rotationData6, n2, rotationHumanizationProfile.gcdOffsetDegrees())
              : this.rotationSynchronizeService.next(
                  rotationData,
                  rotationData6,
                  n2,
                  rotationHumanizationProfile.gcdOffsetDegrees(),
                  rotationHumanizationProfile.maxTurnSpeed(),
                  !this.enabled3);
      if (b2) {
        this.rotationSynchronizeService.synchronize(rotationData, quantizedRotation.rotation());
      }
      this.value14 = RotationData.yawDelta(rotationData.yaw(), quantizedRotation.rotation().yaw());
      this.value15 = quantizedRotation.rotation().pitch() - rotationData.pitch();
      final boolean b6 =
          b5
              && quantizedRotation.yawMouseCounts() == 0L
              && quantizedRotation.pitchMouseCounts() == 0L;
      return Optional.of(
          new Step(
              quantizedRotation.rotation(),
              this.enabled
                  ? Phase.OVERSHOOT
                  : ((b6
                          || RotationData.distance(quantizedRotation.rotation(), rotationData4)
                              <= max4)
                      ? Phase.TRACKING
                      : Phase.CORRECTION),
              n,
              quantizedRotation.vanillaGcd(),
              quantizedRotation.effectiveGcd(),
              quantizedRotation.yawMouseCounts(),
              quantizedRotation.pitchMouseCounts(),
              b6));
    }
    --this.count;
    if (!this.rotationSynchronizeService.moving()) {
      return Optional.empty();
    }
    final RotationVanillaGcdService.QuantizedRotation next =
        this.rotationSynchronizeService.next(
            rotationData,
            rotationData,
            n2,
            rotationHumanizationProfile.gcdOffsetDegrees(),
            rotationHumanizationProfile.maxTurnSpeed());
    return Optional.of(
        new Step(
            next.rotation(),
            Phase.CORRECTION,
            n,
            next.vanillaGcd(),
            next.effectiveGcd(),
            next.yawMouseCounts(),
            next.pitchMouseCounts(),
            false));
  }

  public void reset() {
    this.rotationSynchronizeService.reset();
    this.resetTarget();
  }

  public void synchronizeMotion(final RotationData rotationData, final RotationData rotationData2) {
    this.rotationSynchronizeService.synchronize(rotationData, rotationData2);
  }

  public RotationData continueRotation(
      final RotationData rotationData,
      final RotationData rotationData2,
      final double n,
      final double n2) {
    return this.rotationSynchronizeService.next(rotationData, rotationData2, n, 0.0, n2).rotation();
  }

  public void resetTarget() {
    this.fa8ahrpqghm = null;
    this.integer = null;
    this.count = 0;
    this.rotationData2 = null;
    this.enabled2 = false;
    this.fjowozlomxgy = 0;
    this.count5 = 0;
    this.count6 = 0;
    this.fdk5kdhpdsmt = 0.0;
    this.value5 = 0.0;
    this.value6 = 0.0;
    this.value7 = 0.0;
    this.value8 = 0.0;
    this.value9 = 0.0;
    this.value10 = 0.0;
    this.rotationData3 = null;
    this.count7 = 0;
    this.count8 = 0;
    this.fht3qiwpxoxh = null;
    this.count9 = 0;
    this.value11 = 0.0;
    this.value12 = 0.0;
    this.value13 = 0.0;
    this.updateState3();
  }

  public int reactionTicksRemaining() {
    return this.count;
  }

  private void m62j90xwmq8s(
      final RotationData rotationData,
      final RotationData rotationData3,
      final int i,
      final RotationHumanizationProfile rotationHumanizationProfile) {
    this.integer = i;
    this.fa8ahrpqghm = rotationData3;
    this.count =
        (rotationHumanizationProfile.reactionDelay()
            ? this.calculateValue4(rotationHumanizationProfile)
            : 0);
    this.enabled =
        (rotationHumanizationProfile.overshoot()
            && RotationData.distance(rotationData, rotationData3)
                >= Double.longBitsToDouble(4620693217682128896L)
            && this.random.nextDouble() < Double.longBitsToDouble(4601237667291888353L));
    double signum = Math.signum(RotationData.yawDelta(rotationData.yaw(), rotationData3.yaw()));
    final double yawDelta = RotationData.yawDelta(rotationData.yaw(), rotationData3.yaw());
    final double y = rotationData3.pitch() - rotationData.pitch();
    final double hypot = Math.hypot(yawDelta, y);
    final double n =
        rotationHumanizationProfile.overshootMinDegrees()
            + this.random.nextDouble()
                * Math.max(
                    0.0,
                    rotationHumanizationProfile.overshootMaxDegrees()
                        - rotationHumanizationProfile.overshootMinDegrees());
    if (hypot < Double.longBitsToDouble(4472406533629990549L)) {
      if (signum == 0.0) {
        signum = (this.random.nextBoolean() ? 1.0 : Double.longBitsToDouble(-4616189618054758400L));
      }
      this.value2 = signum * n;
      this.value3 = 0.0;
    } else {
      final double n2 = yawDelta / hypot;
      final double n3 = y / hypot;
      final double n4 =
          this.random.nextGaussian() * n * Double.longBitsToDouble(4590429028186199163L);
      this.value2 = n2 * n - n3 * n4;
      this.value3 = n3 * n + n2 * n4;
    }
    this.fht3qiwpxoxh = rotationData3;
    this.rotationData2 = null;
    this.enabled2 = false;
    this.fjowozlomxgy = 0;
    this.rotationData3 = rotationData3;
    this.count7 = 0;
    this.count8 = 1;
    this.count9 = 2 + this.random.nextInt(3);
    this.value11 = 0.0;
    this.value12 = 0.0;
    this.updateState3();
  }

  private RotationData m8werzpnjiwt(
      final RotationData rotationData, final double n, final boolean b) {
    if (this.rotationData3 == null) {
      this.rotationData3 = rotationData;
      this.count8 = 1;
      this.count7 = 0;
      return rotationData;
    }
    final double yawDelta = RotationData.yawDelta(this.rotationData3.yaw(), rotationData.yaw());
    final double y = rotationData.pitch() - this.rotationData3.pitch();
    final double hypot = Math.hypot(yawDelta, y);
    ++this.count7;
    if (hypot + Double.longBitsToDouble(4427486594234968593L)
        >= (b
            ? 0.0
            : Math.max(
                Double.longBitsToDouble(4582862980812216730L),
                Math.min(
                    Double.longBitsToDouble(4597094355634707497L),
                    n * Double.longBitsToDouble(4582862980812216730L))))) {
      final double n2 = Math.max(1, this.count7);
      this.value11 =
          this.value11 * Double.longBitsToDouble(4603759683083215831L)
              + yawDelta / n2 * Double.longBitsToDouble(4600517091351509074L);
      this.value12 =
          this.value12 * Double.longBitsToDouble(4603759683083215831L)
              + y / n2 * Double.longBitsToDouble(4600517091351509074L);
      this.rotationData3 = rotationData;
      this.count7 = 0;
      this.count8 = 0;
    } else {
      this.value11 *= Double.longBitsToDouble(4605200834963974390L);
      this.value12 *= Double.longBitsToDouble(4605200834963974390L);
      ++this.count8;
    }
    return this.rotationData3;
  }

  private RotationData createRotationData2(final RotationData rotationData) {
    if (!this.enabled) {
      return rotationData;
    }
    return new RotationData(rotationData.yaw() + this.value2, rotationData.pitch() + this.value3)
        .withPitchClamped();
  }

  private void m8afljvv517e(
      final RotationData rotationData2,
      final RotationData rotationData,
      final RotationHumanizationProfile rotationHumanizationProfile,
      final double b,
      final boolean b2) {
    this.rotationData2 = rotationData2;
    this.count2 = 0;
    final double distance = RotationData.distance(rotationData2, rotationData);
    final int max =
        Math.max(1, (int) Math.ceil(distance / rotationHumanizationProfile.maxTurnSpeed()));
    if (rotationHumanizationProfile.smoothness() == 0.0) {
      this.count3 = 1;
    } else {
      this.count3 =
          Math.max(
              max,
              Math.max(
                  1,
                  (int)
                      Math.ceil(
                          ((b2
                                      ? Double.longBitsToDouble(4630122629401935872L)
                                      : Double.longBitsToDouble(4632937379169042432L))
                                  + (b2
                                          ? Double.longBitsToDouble(4628574517030027264L)
                                          : Double.longBitsToDouble(4631107791820423168L))
                                      * mdke0ot2ny3p(
                                          1.0
                                              + distance
                                                  / Math.max(
                                                      Double.longBitsToDouble(4591870180066957722L),
                                                      b)))
                              * this.value
                              * (Double.longBitsToDouble(4603399395113026191L)
                                  + rotationHumanizationProfile.smoothness()
                                      * Double.longBitsToDouble(4603759683083215831L))
                              / Double.longBitsToDouble(4632233691727265792L))));
    }
    this.value13 =
        this.random.nextGaussian()
            * Math.min(
                Double.longBitsToDouble(4604480259023595110L),
                distance * Double.longBitsToDouble(4582862980812216730L))
            * rotationHumanizationProfile.jitterIntensity();
    this.enabled2 = false;
    this.fjowozlomxgy = 0;
  }

  private RotationData m7ijgvztb2ly(
      final RotationData rotationData,
      final RotationData rotationData2,
      final RotationHumanizationProfile rotationHumanizationProfile) {
    if (rotationHumanizationProfile.smoothness() == 0.0) {
      return rotationData2;
    }
    if (this.count5 > 0) {
      --this.count5;
      return rotationData;
    }
    this.count2 = Math.min(this.count3, this.count2 + 1);
    final double m8nfsodqhngp = m8nfsodqhngp(this.count2 / (double) this.count3);
    return createRotationData11(
        rotationData,
        this.m48itpo6ao2g(
            RotationData.interpolate(this.rotationData2, rotationData2, m8nfsodqhngp),
            rotationData2,
            m8nfsodqhngp),
        rotationHumanizationProfile.maxTurnSpeed(),
        rotationHumanizationProfile.maxPitchSpeed());
  }

  private RotationData m25fb2r09lxc(
      final RotationData rotationData,
      final RotationData rotationData2,
      final RotationHumanizationProfile rotationHumanizationProfile) {
    if (this.count10 <= 0) {
      this.value17 =
          1.0
              + this.mdmi9tsbmuvr(
                      Double.longBitsToDouble(-4631501856787818086L),
                      Double.longBitsToDouble(4591870180066957722L))
                  * rotationHumanizationProfile.jitterIntensity();
      this.count10 = 4 + this.random.nextInt(7);
    }
    --this.count10;
    this.value16 += (this.value17 - this.value16) * Double.longBitsToDouble(4596373779694328218L);
    final double n = this.value * this.value16;
    final double mgh35dknrrgy =
        mgh35dknrrgy(
            1.0
                - rotationHumanizationProfile.smoothness()
                    * Double.longBitsToDouble(4603129179135383962L)
                    * n,
            Double.longBitsToDouble(4599976659396224614L),
            1.0);
    final double mgh35dknrrgy2 =
        mgh35dknrrgy(
            (Double.longBitsToDouble(4604029899060858061L)
                    + (1.0 - rotationHumanizationProfile.smoothness())
                        * Double.longBitsToDouble(4599075939470750515L))
                / n,
            Double.longBitsToDouble(4603129179135383962L),
            1.0);
    return new RotationData(
            rotationData.yaw()
                + m13wquvwvq7e(
                    RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw()),
                    (this.count8 == 0) ? this.value11 : 0.0,
                    this.value14,
                    mgh35dknrrgy,
                    mgh35dknrrgy2,
                    rotationHumanizationProfile.maxTurnSpeed()),
            rotationData.pitch()
                + m13wquvwvq7e(
                    rotationData2.pitch() - rotationData.pitch(),
                    (this.count8 == 0) ? this.value12 : 0.0,
                    this.value15,
                    mgh35dknrrgy,
                    mgh35dknrrgy2,
                    rotationHumanizationProfile.maxPitchSpeed()))
        .withPitchClamped();
  }

  private static double m13wquvwvq7e(
      final double n,
      final double n2,
      double n3,
      final double n4,
      final double n5,
      final double n6) {
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

  private void updateState3() {
    this.enabled3 = false;
    this.value14 = 0.0;
    this.value15 = 0.0;
    this.value16 = 1.0;
    this.value17 = 1.0;
    this.count10 = 0;
  }

  private RotationData m48itpo6ao2g(
      final RotationData rotationData, final RotationData rotationData2, final double n) {
    if (Math.abs(this.value13) < Double.longBitsToDouble(4427486594234968593L)) {
      return rotationData;
    }
    final double yawDelta = RotationData.yawDelta(this.rotationData2.yaw(), rotationData2.yaw());
    final double y = rotationData2.pitch() - this.rotationData2.pitch();
    final double hypot = Math.hypot(yawDelta, y);
    if (hypot < Double.longBitsToDouble(4472406533629990549L)) {
      return rotationData;
    }
    final double n2 = 1.0 - n;
    final double n3 = Double.longBitsToDouble(4625196817309499392L) * n * n * n2 * n2;
    return new RotationData(
            rotationData.yaw() - y / hypot * this.value13 * n3,
            rotationData.pitch() + yawDelta / hypot * this.value13 * n3)
        .withPitchClamped();
  }

  private RotationData m59vc8l5ktk4(final RotationData rotationData, final double n) {
    final double max =
        Math.max(
            n * Double.longBitsToDouble(4599976659396224614L),
            Math.hypot(this.value11, this.value12));
    return new RotationData(
            rotationData.yaw()
                + mgh35dknrrgy(
                    this.value11 * Double.longBitsToDouble(4609434218613702656L), -max, max),
            rotationData.pitch()
                + mgh35dknrrgy(
                    this.value12 * Double.longBitsToDouble(4608308318706860032L), -max, max))
        .withPitchClamped();
  }

  private RotationData maoqjsnp6pw(
      final RotationData rotationData, final RotationData rotationData2, final double n) {
    final double yawDelta = RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw());
    final double n2 = rotationData2.pitch() - rotationData.pitch();
    if (n == 0.0) {
      this.value8 = yawDelta;
      this.value9 = n2;
      return rotationData2;
    }
    final double n3 = yawDelta - this.value8;
    final double n4 = n2 - this.value9;
    this.value8 = yawDelta;
    this.value9 = n2;
    final double n5 = Double.longBitsToDouble(4585925428558828667L) * n;
    return new RotationData(
        rotationData2.yaw() + n3 * this.mdmi9tsbmuvr(-n5, n5),
        rotationData2.pitch() + n4 * this.mdmi9tsbmuvr(-n5, n5));
  }

  private RotationData mhbtj2nog9es(
      final RotationData rotationData,
      final RotationData rotationData2,
      final RotationData rotationData3,
      final RotationHumanizationProfile rotationHumanizationProfile,
      final double n) {
    if (!rotationHumanizationProfile.snapVariation() || this.enabled) {
      return rotationData3;
    }
    final double distance = RotationData.distance(rotationData, rotationData2);
    final double min =
        Math.min(
            Double.longBitsToDouble(4618441417868443648L),
            Math.max(
                Double.longBitsToDouble(4612811918334230528L),
                n * Double.longBitsToDouble(4620693217682128896L)));
    if (!this.enabled2 && distance <= min) {
      this.fjowozlomxgy = (this.random.nextBoolean() ? 1 : (2 + this.random.nextInt(2)));
      this.enabled2 = true;
    } else if (this.enabled2 && distance > min * Double.longBitsToDouble(4611686018427387904L)) {
      this.enabled2 = false;
      this.fjowozlomxgy = 0;
    }
    if (this.fjowozlomxgy <= 0) {
      return rotationData3;
    }
    final RotationData interpolate =
        RotationData.interpolate(rotationData, rotationData2, 1.0 / this.fjowozlomxgy);
    --this.fjowozlomxgy;
    return interpolate;
  }

  private RotationData mgqs7rui2nm3(final RotationData rotationData, final double n) {
    if (n == 0.0) {
      return rotationData;
    }
    if (this.count6 <= 0) {
      this.value6 = this.calculateValue3();
      this.value7 = this.calculateValue3();
      this.count6 = 2 + this.random.nextInt(4);
    }
    --this.count6;
    this.fdk5kdhpdsmt +=
        (this.value6 - this.fdk5kdhpdsmt) * Double.longBitsToDouble(4598715651500560876L);
    this.value5 += (this.value7 - this.value5) * Double.longBitsToDouble(4597814931575086776L);
    return new RotationData(
        rotationData.yaw() + this.fdk5kdhpdsmt * n, rotationData.pitch() + this.value5 * n);
  }

  private double calculateValue3() {
    final double n =
        Double.longBitsToDouble(4599075939470750515L)
            + this.random.nextDouble() * Double.longBitsToDouble(4602678819172646912L);
    return this.random.nextBoolean() ? n : (-n);
  }

  private void updateState4() {
    this.count5 = 0;
    this.count6 = 0;
    this.fdk5kdhpdsmt = 0.0;
    this.value5 = 0.0;
    this.value6 = 0.0;
    this.value7 = 0.0;
    this.value8 = 0.0;
    this.value9 = 0.0;
    this.value10 = 0.0;
    this.enabled2 = true;
    this.fjowozlomxgy = 0;
  }

  private int calculateValue4(final RotationHumanizationProfile rotationHumanizationProfile) {
    final int reactionMinTicks = rotationHumanizationProfile.reactionMinTicks();
    final int reactionMaxTicks = rotationHumanizationProfile.reactionMaxTicks();
    if (reactionMinTicks == reactionMaxTicks) {
      return reactionMinTicks;
    }
    return (int)
        m7tyb7sr6ur6(
            Math.round(
                Math.exp(
                    Math.log(
                            Math.max(
                                Double.longBitsToDouble(4602678819172646912L),
                                (reactionMinTicks + reactionMaxTicks)
                                    * Double.longBitsToDouble(4602678819172646912L)))
                        + this.random.nextGaussian()
                            * Double.longBitsToDouble(4598715651500560876L))),
            reactionMinTicks,
            reactionMaxTicks);
  }

  private static RotationData m8qvs4nej8mm(
      final RotationData rotationData, final RotationData rotationData2, final double n) {
    return createRotationData11(rotationData, rotationData2, n, n);
  }

  private static RotationData createRotationData11(
      final RotationData rotationData,
      final RotationData rotationData2,
      final double a,
      final double a2) {
    return new RotationData(
        rotationData.yaw()
            + Math.max(
                -a, Math.min(a, RotationData.yawDelta(rotationData.yaw(), rotationData2.yaw()))),
        rotationData.pitch()
            + Math.max(-a2, Math.min(a2, rotationData2.pitch() - rotationData.pitch())));
  }

  private double mdmi9tsbmuvr(final double n, final double n2) {
    return n + this.random.nextDouble() * (n2 - n);
  }

  private static double m8nfsodqhngp(final double b) {
    final double max = Math.max(0.0, Math.min(1.0, b));
    return max
        * max
        * max
        * (max
                * (max * Double.longBitsToDouble(4618441417868443648L)
                    - Double.longBitsToDouble(4624633867356078080L))
            + Double.longBitsToDouble(4621819117588971520L));
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

  public record Step(
      RotationData rotation,
      Phase phase,
      int targetEntityId,
      double vanillaGcd,
      double effectiveGcd,
      long yawMouseCounts,
      long pitchMouseCounts,
      boolean settled) {}

  public enum Phase {
    OVERSHOOT,
    CORRECTION,
    TRACKING;
  }
}
