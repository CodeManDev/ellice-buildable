package dev.felix.ellice.feature.rotation;

public record RotationHumanizationProfile(
    double maxTurnSpeed,
    double smoothness,
    double jitterIntensity,
    boolean overshoot,
    boolean reactionDelay,
    boolean snapVariation,
    boolean hesitation,
    double gcdOffsetDegrees,
    double overshootMinDegrees,
    double overshootMaxDegrees,
    int reactionMinTicks,
    int reactionMaxTicks,
    double pitchSpeedScale) {
  public RotationHumanizationProfile(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      boolean enabled,
      boolean currentEnabled,
      boolean nextEnabled,
      boolean previousEnabled,
      double previousDoubleValue) {
    this(
        doubleValue,
        currentDoubleValue,
        nextDoubleValue,
        enabled,
        currentEnabled,
        nextEnabled,
        previousEnabled,
        previousDoubleValue,
        2.0,
        5.0,
        1,
        4,
        1.0);
  }

  public RotationHumanizationProfile(
      double maxTurnSpeed,
      double smoothness,
      double jitterIntensity,
      boolean overshoot,
      boolean reactionDelay,
      boolean snapVariation,
      boolean hesitation,
      double gcdOffsetDegrees,
      double overshootMinDegrees,
      double overshootMaxDegrees,
      int reactionMinTicks,
      int reactionMaxTicks,
      double pitchSpeedScale) {
    if (!Double.isFinite(maxTurnSpeed) || maxTurnSpeed <= 0.0 || maxTurnSpeed > 180.0) {
      throw new IllegalArgumentException("maxTurnSpeed must be in (0, 180]");
    }

    if (!Double.isFinite(smoothness) || smoothness < 0.0 || smoothness > 1.0) {
      throw new IllegalArgumentException("smoothness must be in [0, 1]");
    }

    if (!Double.isFinite(jitterIntensity) || jitterIntensity < 0.0 || jitterIntensity > 1.0) {
      throw new IllegalArgumentException("jitterIntensity must be in [0, 1]");
    }

    if (!Double.isFinite(gcdOffsetDegrees)) {
      throw new IllegalArgumentException("gcdOffsetDegrees must be finite");
    }

    if (Double.isFinite(overshootMinDegrees)
        && Double.isFinite(overshootMaxDegrees)
        && !(overshootMinDegrees < 0.0)
        && !(overshootMaxDegrees < 0.0)
        && !(overshootMinDegrees > 30.0)
        && !(overshootMaxDegrees > 30.0)) {
      if (overshootMinDegrees > overshootMaxDegrees) {
        double doubleValue = overshootMinDegrees;
        overshootMinDegrees = overshootMaxDegrees;
        overshootMaxDegrees = doubleValue;
      }

      if (reactionMinTicks >= 0
          && reactionMaxTicks >= 0
          && reactionMinTicks <= 20
          && reactionMaxTicks <= 20) {
        if (reactionMinTicks > reactionMaxTicks) {
          int value = reactionMinTicks;
          reactionMinTicks = reactionMaxTicks;
          reactionMaxTicks = value;
        }

        if (Double.isFinite(pitchSpeedScale)
            && !(pitchSpeedScale <= 0.0)
            && !(pitchSpeedScale > 2.0)) {
          this.maxTurnSpeed = maxTurnSpeed;
          this.smoothness = smoothness;
          this.jitterIntensity = jitterIntensity;
          this.overshoot = overshoot;
          this.reactionDelay = reactionDelay;
          this.snapVariation = snapVariation;
          this.hesitation = hesitation;
          this.gcdOffsetDegrees = gcdOffsetDegrees;
          this.overshootMinDegrees = overshootMinDegrees;
          this.overshootMaxDegrees = overshootMaxDegrees;
          this.reactionMinTicks = reactionMinTicks;
          this.reactionMaxTicks = reactionMaxTicks;
          this.pitchSpeedScale = pitchSpeedScale;
        } else {
          throw new IllegalArgumentException("pitchSpeedScale must be in (0, 2]");
        }
      } else {
        throw new IllegalArgumentException("reaction ticks must be in [0, 20]");
      }
    } else {
      throw new IllegalArgumentException("overshoot degrees must be in [0, 30]");
    }
  }

  public double maxPitchSpeed() {
    return this.maxTurnSpeed * this.pitchSpeedScale;
  }
}
