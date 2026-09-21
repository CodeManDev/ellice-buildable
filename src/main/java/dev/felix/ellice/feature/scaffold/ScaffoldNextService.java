package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import java.util.Objects;
import java.util.Random;

public final class ScaffoldNextService {
  private final RotationVanillaGcdService rotationVanillaGcdService =
      new RotationVanillaGcdService();
  private final Random random;
  private int count = Integer.MIN_VALUE;
  private long timestamp;
  private long timestamp2 = Long.MIN_VALUE;
  private ScaffoldNextService.Step step;
  private double value;
  private double value2;
  private double value3;
  private double value4;
  private double value5;
  private double value6;
  private double value7;
  private int count2;
  private double value8;
  private boolean enabled;

  public ScaffoldNextService(long longValue) {
    this.random = new Random(longValue);
  }

  public ScaffoldNextService.Step next(ScaffoldNextService.Request request) {
    return this.next(request, true);
  }

  public ScaffoldNextService.Step next(
      ScaffoldNextService.Request request, boolean currentEnabled) {
    if (this.step != null && request.tick() <= this.timestamp2) {
      return this.step;
    }

    this.timestamp2 = request.tick();
    if (this.count != request.motorPlanId()) {
      this.updateState(request);
    }

    if (request.maximumReactionTicks() == 0 && request.tick() < this.timestamp) {
      this.timestamp = request.tick();
    }

    boolean nextEnabled = this.enabled;
    if (request.rayInsideTarget()) {
      this.count2 = 0;
      this.value8 = request.rayMargin();
    } else {
      this.count2++;
    }

    if (request.tick() < this.timestamp) {
      this.updateState2();
      return this.step =
          this.createStep(
              request.current(), request.sensitivity(), ScaffoldNextService.Phase.REACTION);
    }

    double doubleValue = this.calculateValue3(request);
    double currentDoubleValue = Math.max(request.minimumHoldMargin(), doubleValue - 0.045);
    int currentValue =
        request.rayInsideTarget()
                && request.rayMargin() >= (nextEnabled ? currentDoubleValue : doubleValue)
            ? 1
            : 0;
    if (currentValue != 0) {
      this.enabled = true;
      this.count2 = 0;
      this.value8 = request.rayMargin();
      this.updateState2();
      return this.step =
          this.createStep(request.current(), request.sensitivity(), ScaffoldNextService.Phase.HOLD);
    }

    if (currentEnabled
        && !request.rayInsideTarget()
        && this.count2 == 1
        && this.value8 >= Math.max(0.12, currentDoubleValue)
        && request.situation().shouldCoastSingleRayMiss()) {
      this.enabled = nextEnabled;
      this.updateState2();
      return this.step =
          this.createStep(
              request.current(), request.sensitivity(), ScaffoldNextService.Phase.COAST);
    }

    this.enabled = false;
    if (request.situation().suppressEndpointError()) {
      this.value6 = 0.0;
      this.value7 = 0.0;
    }

    RotationData rotationData =
        new RotationData(
                request.desired().yaw() + this.value6, request.desired().pitch() + this.value7)
            .withPitchClamped();
    RotationData currentRotationData = request.desired().withPitchClamped();
    double nextDoubleValue = this.calculateValue2(request);
    double previousDoubleValue = this.value;
    double sourceDoubleValue = this.value2;
    ScaffoldNextService.AxisStep axisStep =
        this.createAxisStep(
            RotationData.yawDelta(request.current().yaw(), rotationData.yaw()),
            RotationData.yawDelta(request.current().yaw(), currentRotationData.yaw()),
            this.value,
            this.value3,
            nextDoubleValue,
            nextDoubleValue * (0.3 + (1.0 - request.smoothness()) * 0.18));
    ScaffoldNextService.AxisStep currentAxisStep =
        this.createAxisStep(
            rotationData.pitch() - request.current().pitch(),
            currentRotationData.pitch() - request.current().pitch(),
            this.value2,
            this.value4,
            nextDoubleValue * 0.72,
            nextDoubleValue * (0.24 + (1.0 - request.smoothness()) * 0.15));
    double targetDoubleValue = Math.hypot(axisStep.velocity(), currentAxisStep.velocity());
    double inputDoubleValue =
        targetDoubleValue > nextDoubleValue ? nextDoubleValue / targetDoubleValue : 1.0;
    RotationData nextRotationData =
        new RotationData(
                request.current().yaw() + axisStep.velocity() * inputDoubleValue,
                request.current().pitch() + currentAxisStep.velocity() * inputDoubleValue)
            .withPitchClamped();
    ScaffoldNextService.CountPair countPair =
        this.createCountPair(
            request.current(),
            nextRotationData,
            currentRotationData,
            request.sensitivity(),
            nextDoubleValue);
    RotationData previousRotationData =
        applyMouseCounts(
            request.current(), countPair.yaw(), countPair.pitch(), request.sensitivity());
    double outputDoubleValue = RotationData.distance(request.current(), previousRotationData);
    double resultDoubleValue = outputDoubleValue - this.value5;
    this.value5 = outputDoubleValue;
    double candidateDoubleValue =
        RotationData.yawDelta(request.current().yaw(), previousRotationData.yaw());
    double selectedDoubleValue = previousRotationData.pitch() - request.current().pitch();
    this.value3 = candidateDoubleValue - previousDoubleValue;
    this.value4 = selectedDoubleValue - sourceDoubleValue;
    this.value = candidateDoubleValue;
    this.value2 = selectedDoubleValue;
    ScaffoldNextService.Phase phase = this.createPhase(request, rotationData);
    return this.step =
        new ScaffoldNextService.Step(
            previousRotationData,
            phase,
            countPair.yaw(),
            countPair.pitch(),
            this.rotationVanillaGcdService.vanillaGcd(request.sensitivity()),
            outputDoubleValue,
            resultDoubleValue,
            countPair.yaw() == 0L && countPair.pitch() == 0L);
  }

  public void reset() {
    this.count = Integer.MIN_VALUE;
    this.timestamp = 0L;
    this.timestamp2 = Long.MIN_VALUE;
    this.step = null;
    this.value = 0.0;
    this.value2 = 0.0;
    this.value3 = 0.0;
    this.value4 = 0.0;
    this.value5 = 0.0;
    this.value6 = 0.0;
    this.value7 = 0.0;
    this.count2 = 0;
    this.value8 = 0.0;
    this.enabled = false;
  }

  public static RotationData applyMouseCounts(
      RotationData rotationData, long longValue, long currentLongValue, double height) {
    double doubleValue = height * 0.6 + 0.2;
    double currentDoubleValue = doubleValue * doubleValue * doubleValue * 8.0;
    float value = (float) rotationData.yaw();
    float currentValue = (float) rotationData.pitch();
    float nextValue = (float) (longValue * currentDoubleValue) * 0.15F;
    float previousValue = (float) (currentLongValue * currentDoubleValue) * 0.15F;
    float sourceValue = value + nextValue;
    float targetValue = Math.max(-90.0F, Math.min(90.0F, currentValue + previousValue));
    return new RotationData(sourceValue, targetValue);
  }

  private void updateState(ScaffoldNextService.Request request) {
    int currentCount = this.count == Integer.MIN_VALUE ? 1 : 0;
    this.count = request.motorPlanId();
    boolean predictableChain = request.situation().pathRelation().isPredictableChain();
    int currentValue =
        predictableChain
            ? (int)
                calculateValue7(
                    request.situation().pathRelation().transitionDelayTicks(),
                    0L,
                    request.maximumReactionTicks())
            : this.calculateValue4(
                request.situation().urgency(),
                request.minimumReactionTicks(),
                request.maximumReactionTicks());
    this.timestamp = request.tick() + currentValue;
    int nextValue =
        predictableChain
                && currentCount == 0
                && request.situation().motionPhase() == ScaffoldMotionPhaseData.MotionPhase.GROUNDED
            ? 1
            : 0;
    if (nextValue == 0) {
      this.value = 0.0;
      this.value2 = 0.0;
      this.value3 = 0.0;
      this.value4 = 0.0;
    }

    if (request.situation().suppressEndpointError()) {
      this.value6 = 0.0;
      this.value7 = 0.0;
    } else if (nextValue != 0) {
      this.value6 = this.value6 * 0.72;
      this.value7 = this.value7 * 0.72;
    } else {
      double doubleValue =
          Math.min(
              request.angularTargetWidth() * 0.06, 0.045 + request.maximumTurnSpeed() * 0.0011);
      this.value6 = this.random.nextGaussian() * doubleValue;
      this.value7 = this.random.nextGaussian() * doubleValue * 0.62;
    }

    this.count2 = 0;
    this.value8 = 0.0;
    this.enabled = false;
  }

  private ScaffoldNextService.AxisStep createAxisStep(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue,
      double sourceDoubleValue,
      double targetDoubleValue) {
    double inputDoubleValue = Math.signum(doubleValue);
    double outputDoubleValue =
        Math.sqrt(Math.max(0.0, 2.0 * targetDoubleValue * Math.abs(doubleValue)));
    double resultDoubleValue = inputDoubleValue * Math.min(sourceDoubleValue, outputDoubleValue);
    if (Math.abs(doubleValue) < 0.35) {
      resultDoubleValue *= Math.abs(doubleValue) / 0.35;
    }

    double candidateDoubleValue =
        calculateValue6(resultDoubleValue - nextDoubleValue, -targetDoubleValue, targetDoubleValue);
    double selectedDoubleValue = Math.max(0.08, targetDoubleValue * 0.48);
    double defaultDoubleValue =
        calculateValue5(previousDoubleValue, candidateDoubleValue, selectedDoubleValue);
    double initialDoubleValue =
        calculateValue6(
            nextDoubleValue + defaultDoubleValue, -sourceDoubleValue, sourceDoubleValue);
    initialDoubleValue = calculateValue(initialDoubleValue, currentDoubleValue);
    defaultDoubleValue = initialDoubleValue - nextDoubleValue;
    if (Math.abs(doubleValue) < 0.02 && Math.abs(initialDoubleValue) < 0.04) {
      initialDoubleValue = 0.0;
      defaultDoubleValue = -nextDoubleValue;
    }

    return new ScaffoldNextService.AxisStep(initialDoubleValue, defaultDoubleValue);
  }

  private ScaffoldNextService.CountPair createCountPair(
      RotationData rotationData,
      RotationData currentRotationData,
      RotationData nextRotationData,
      double doubleValue,
      double currentDoubleValue) {
    double nextDoubleValue = this.rotationVanillaGcdService.vanillaGcd(doubleValue);
    long size =
        Math.round(
            RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw()) / nextDoubleValue);
    long currentSize =
        Math.round((currentRotationData.pitch() - rotationData.pitch()) / nextDoubleValue);
    ScaffoldNextService.CountPair countPair = new ScaffoldNextService.CountPair(0L, 0L);
    RotationData previousRotationData = applyMouseCounts(rotationData, 0L, 0L, doubleValue);
    double previousDoubleValue = RotationData.distance(previousRotationData, currentRotationData);
    double sourceDoubleValue = RotationData.yawDelta(rotationData.yaw(), nextRotationData.yaw());
    double targetDoubleValue = nextRotationData.pitch() - rotationData.pitch();

    for (int index = 0; index < 6; index++) {
      long nextSize = index == 0 ? 0L : size + index - 3L;

      for (int currentIndex = 0; currentIndex < 6; currentIndex++) {
        long previousSize = currentIndex == 0 ? 0L : currentSize + currentIndex - 3L;
        RotationData sourceRotationData =
            applyMouseCounts(rotationData, nextSize, previousSize, doubleValue);
        double inputDoubleValue =
            RotationData.yawDelta(rotationData.yaw(), sourceRotationData.yaw());
        double outputDoubleValue = sourceRotationData.pitch() - rotationData.pitch();
        if (checkCondition(sourceDoubleValue, inputDoubleValue)
            && checkCondition(targetDoubleValue, outputDoubleValue)
            && !(Math.hypot(inputDoubleValue, outputDoubleValue) > currentDoubleValue)) {
          double resultDoubleValue = RotationData.distance(sourceRotationData, currentRotationData);
          if (resultDoubleValue < previousDoubleValue) {
            previousDoubleValue = resultDoubleValue;
            countPair = new ScaffoldNextService.CountPair(nextSize, previousSize);
          }
        }
      }
    }

    return countPair;
  }

  private static double calculateValue(double doubleValue, double currentDoubleValue) {
    if (!checkCondition(currentDoubleValue, doubleValue)) {
      return doubleValue != 0.0
              && currentDoubleValue != 0.0
              && Math.signum(doubleValue) == Math.signum(currentDoubleValue)
          ? currentDoubleValue
          : 0.0;
    } else {
      return doubleValue;
    }
  }

  private static boolean checkCondition(double doubleValue, double currentDoubleValue) {
    return currentDoubleValue == 0.0
        ? true
        : doubleValue != 0.0
            && Math.signum(currentDoubleValue) == Math.signum(doubleValue)
            && Math.abs(currentDoubleValue) <= Math.abs(doubleValue);
  }

  private double calculateValue2(ScaffoldNextService.Request request) {
    double doubleValue = 1.0 - request.smoothness() * 0.48;
    return Math.min(
        request.maximumTurnSpeed(),
        Math.max(
            0.5, request.maximumTurnSpeed() * request.situation().turnSpeedScale() * doubleValue));
  }

  private double calculateValue3(ScaffoldNextService.Request request) {
    double doubleValue = Math.min(0.08, request.situation().urgency() * 0.025);
    double currentDoubleValue = Math.min(0.08, this.value5 * 0.006);
    return request.minimumHoldMargin() + doubleValue + currentDoubleValue;
  }

  private ScaffoldNextService.Phase createPhase(
      ScaffoldNextService.Request request, RotationData rotationData) {
    if (request.situation().pathRelation() == ScaffoldMotionPhaseData.PathRelation.RECOVERY) {
      return ScaffoldNextService.Phase.RECOVERY;
    } else if (request.situation().pathRelation().isPredictableChain()) {
      return ScaffoldNextService.Phase.CHAIN_TRANSITION;
    } else {
      return RotationData.distance(request.current(), rotationData)
              <= Math.max(0.6, request.angularTargetWidth() * 0.18)
          ? ScaffoldNextService.Phase.HOMING
          : ScaffoldNextService.Phase.PRIMARY;
    }
  }

  private ScaffoldNextService.Step createStep(
      RotationData rotationData, double doubleValue, ScaffoldNextService.Phase phase) {
    return new ScaffoldNextService.Step(
        new RotationData((float) rotationData.yaw(), (float) rotationData.pitch()),
        phase,
        0L,
        0L,
        this.rotationVanillaGcdService.vanillaGcd(doubleValue),
        0.0,
        -this.value5,
        true);
  }

  private void updateState2() {
    this.value = this.value * 0.45;
    this.value2 = this.value2 * 0.45;
    this.value3 = this.value3 * 0.35;
    this.value4 = this.value4 * 0.35;
    this.value5 = 0.0;
  }

  private int calculateValue4(double doubleValue, int value, int currentValue) {
    if (value == currentValue) {
      return value;
    }

    double currentDoubleValue = Math.exp(Math.log(1.75) + this.random.nextGaussian() * 0.24);
    return (int) calculateValue7(Math.round(currentDoubleValue - doubleValue), value, currentValue);
  }

  private static double calculateValue5(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return doubleValue
        + calculateValue6(currentDoubleValue - doubleValue, -nextDoubleValue, nextDoubleValue);
  }

  private static double calculateValue6(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return Math.max(currentDoubleValue, Math.min(nextDoubleValue, doubleValue));
  }

  private static long calculateValue7(long longValue, long currentLongValue, long nextLongValue) {
    return Math.max(currentLongValue, Math.min(nextLongValue, longValue));
  }

  private record AxisStep(double velocity, double acceleration) {}

  private record CountPair(long yaw, long pitch) {}

  public enum Phase {
    REACTION,
    PRIMARY,
    HOMING,
    COAST,
    HOLD,
    CHAIN_TRANSITION,
    RECOVERY;

    private static ScaffoldNextService.Phase[] $values() {
      return new ScaffoldNextService.Phase[] {
        REACTION, PRIMARY, HOMING, COAST, HOLD, CHAIN_TRANSITION, RECOVERY
      };
    }
  }

  public record Request(
      long tick,
      RotationData current,
      RotationData desired,
      int motorPlanId,
      double sensitivity,
      double maximumTurnSpeed,
      double smoothness,
      double angularTargetWidth,
      boolean rayInsideTarget,
      double rayMargin,
      int minimumReactionTicks,
      int maximumReactionTicks,
      double minimumHoldMargin,
      ScaffoldMotionPhaseData situation) {
    public Request(
        long longValue,
        RotationData rotationData,
        RotationData currentRotationData,
        int value,
        double doubleValue,
        double currentDoubleValue,
        double nextDoubleValue,
        double previousDoubleValue,
        boolean enabled,
        double sourceDoubleValue,
        ScaffoldMotionPhaseData scaffoldMotionPhaseData) {
      this(
          longValue,
          rotationData,
          currentRotationData,
          value,
          doubleValue,
          currentDoubleValue,
          nextDoubleValue,
          previousDoubleValue,
          enabled,
          sourceDoubleValue,
          1,
          3,
          0.075,
          scaffoldMotionPhaseData);
    }

    public Request(
        long tick,
        RotationData current,
        RotationData desired,
        int motorPlanId,
        double sensitivity,
        double maximumTurnSpeed,
        double smoothness,
        double angularTargetWidth,
        boolean rayInsideTarget,
        double rayMargin,
        int minimumReactionTicks,
        int maximumReactionTicks,
        double minimumHoldMargin,
        ScaffoldMotionPhaseData situation) {
      Objects.requireNonNull(current, "current");
      Objects.requireNonNull(desired, "desired");
      Objects.requireNonNull(situation, "situation");
      if (motorPlanId >= 0
          && Double.isFinite(sensitivity)
          && !(sensitivity < 0.0)
          && !(sensitivity > 1.0)
          && Double.isFinite(maximumTurnSpeed)
          && !(maximumTurnSpeed <= 0.0)
          && !(maximumTurnSpeed > 180.0)
          && Double.isFinite(smoothness)
          && !(smoothness < 0.0)
          && !(smoothness > 1.0)
          && Double.isFinite(angularTargetWidth)
          && !(angularTargetWidth <= 0.0)
          && Double.isFinite(rayMargin)
          && !(rayMargin < 0.0)
          && !(rayMargin > 0.5)
          && minimumReactionTicks >= 0
          && maximumReactionTicks >= minimumReactionTicks
          && maximumReactionTicks <= 20
          && Double.isFinite(minimumHoldMargin)
          && !(minimumHoldMargin < 0.0)
          && !(minimumHoldMargin > 0.5)) {
        this.tick = tick;
        this.current = current;
        this.desired = desired;
        this.motorPlanId = motorPlanId;
        this.sensitivity = sensitivity;
        this.maximumTurnSpeed = maximumTurnSpeed;
        this.smoothness = smoothness;
        this.angularTargetWidth = angularTargetWidth;
        this.rayInsideTarget = rayInsideTarget;
        this.rayMargin = rayMargin;
        this.minimumReactionTicks = minimumReactionTicks;
        this.maximumReactionTicks = maximumReactionTicks;
        this.minimumHoldMargin = minimumHoldMargin;
        this.situation = situation;
      } else {
        throw new IllegalArgumentException("Invalid scaffold aim request");
      }
    }
  }

  public record Step(
      RotationData rotation,
      ScaffoldNextService.Phase phase,
      long yawMouseCounts,
      long pitchMouseCounts,
      double vanillaGcd,
      double angularSpeed,
      double angularAcceleration,
      boolean holding) {}
}
