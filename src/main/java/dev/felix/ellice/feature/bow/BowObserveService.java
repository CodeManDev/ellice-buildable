package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BowObserveService {
  public static final int HORIZON = 88;
  private static final int count = 96;
  private static final int[] int2 = new int[] {2, 5, 10, 16};
  private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
  private final ArrayDeque<RotationVector> items = new ArrayDeque<>();
  private final ArrayDeque<BowObserveService.Issued> items2 = new ArrayDeque<>();
  private final BowObserveService.Error[][] error2 =
      new BowObserveService.Error[BowObserveService.Model.values().length][int2.length];
  private final int[][] int3 = new int[BowObserveService.Model.values().length][int2.length];
  private RotationVector rotationData72;
  private RotationVector rotationData73 = rotationData7;
  private RotationVector rotationData74 = rotationData7;
  private long timestamp;
  private double value;
  private double value2;
  private int count2;
  private int count3 = 1;
  private BowObserveService.Estimate estimate2;

  public BowObserveService() {
    this.clear();
  }

  public BowObserveService.Estimate observe(long longValue, RotationVector rotationVector) {
    if (this.rotationData72 == null) {
      this.rotationData72 = rotationVector;
      this.timestamp = longValue;
    } else {
      int index = (int) (longValue - this.timestamp);
      if (index <= 0
          || index > 8
          || rotationVector.subtract(this.rotationData72).length() > 1.5 * index) {
        this.clear();
        return this.observe(longValue, rotationVector);
      }

      RotationVector currentRotationVector =
          createRotationData7(rotationVector.subtract(this.rotationData72)).multiply(1.0 / index);
      if (this.estimate2 != null) {
        double doubleValue = Double.POSITIVE_INFINITY;

        for (BowObserveService.Weighted weighted : this.estimate2.at(index)) {
          if (!(weighted.weight < 0.12)) {
            double currentLength =
                weighted
                    .hypothesis
                    .offset(index)
                    .subtract(createRotationData7(rotationVector.subtract(this.rotationData72)))
                    .length();
            doubleValue = Math.min(doubleValue, currentLength / index);
          }
        }

        if (Double.isFinite(doubleValue)) {
          this.value2 = this.value2 * 0.35 + doubleValue * 0.65;
        }
      }

      for (int currentIndex = 1; currentIndex <= index; currentIndex++) {
        RotationVector nextRotationVector =
            this.rotationData72.add(
                rotationVector
                    .subtract(this.rotationData72)
                    .multiply((double) currentIndex / index));
        this.updateState(this.timestamp + currentIndex, nextRotationVector);
        this.items.addLast(currentRotationVector);
        if (this.items.size() > 96) {
          this.items.removeFirst();
        }
      }

      double currentDoubleValue =
          calculateValue2(this.rotationData73, currentRotationVector)
              / ((index + this.count3) * 0.5);
      this.value =
          Math.abs(currentDoubleValue) < 0.65 ? this.value * 0.2 + currentDoubleValue * 0.8 : 0.0;
      this.rotationData74 =
          createRotationData72(
              currentRotationVector.subtract(this.rotationData73).multiply(1.0 / index), 0.09);
      this.rotationData73 = createRotationData72(currentRotationVector, 1.0);
      this.count3 = index;
      this.rotationData72 = rotationVector;
      this.timestamp = longValue;
      this.count2++;
    }

    List currentItems = this.collectValues();
    double nextDoubleValue = Math.min(1.0, this.count2 / 6.0) * Math.exp(-2.5 * this.value2);
    this.estimate2 =
        new BowObserveService.Estimate(
            longValue, this.rotationData73, currentItems, nextDoubleValue);
    this.items2.addLast(new BowObserveService.Issued(longValue, rotationVector, currentItems));

    while (!this.items2.isEmpty()
        && longValue - this.items2.getFirst().tick >= int2[int2.length - 1]) {
      this.items2.removeFirst();
    }

    return this.estimate2;
  }

  public BowObserveService.Estimate estimate() {
    return this.estimate2;
  }

  public void clear() {
    this.items.clear();
    this.items2.clear();
    this.rotationData72 = null;
    this.rotationData73 = this.rotationData74 = rotationData7;
    this.value = this.value2 = 0.0;
    this.count2 = 0;
    this.count3 = 1;
    this.estimate2 = null;

    for (int index = 0; index < this.error2.length; index++) {
      Arrays.fill(this.int3[index], 0);

      for (int currentIndex = 0; currentIndex < int2.length; currentIndex++) {
        this.error2[index][currentIndex] = new BowObserveService.Error(0.01, 0.0, 0.01);
      }
    }
  }

  private void updateState(long currentOffset, RotationVector rotationVector) {
    for (BowObserveService.Issued issued : this.items2) {
      long nextOffset = currentOffset - issued.tick;

      for (int index = 0; index < int2.length; index++) {
        if (nextOffset == int2[index]) {
          for (BowObserveService.Hypothesis hypothesis : issued.hypotheses) {
            RotationVector currentRotationVector =
                createRotationData7(rotationVector.subtract(issued.origin))
                    .subtract(hypothesis.offset(nextOffset));
            currentRotationVector = createRotationData72(currentRotationVector, 4.0);
            BowObserveService.Error currentX =
                new BowObserveService.Error(
                    currentRotationVector.x() * currentRotationVector.x(),
                    currentRotationVector.x() * currentRotationVector.z(),
                    currentRotationVector.z() * currentRotationVector.z());
            int currentIndex = hypothesis.model.ordinal();
            double doubleValue = ++this.int3[currentIndex][index] <= 3 ? 0.5 : 0.16;
            this.error2[currentIndex][index] =
                this.error2[currentIndex][index].blend(currentX, doubleValue);
          }
        }
      }
    }
  }

  private List<BowObserveService.Hypothesis> collectValues() {
    ArrayList arrayList = new ArrayList<>(this.items);
    int currentValue = calculateValue(arrayList);
    ArrayList currentArrayList = new ArrayList();

    for (BowObserveService.Model model : BowObserveService.Model.values()) {
      if (model != BowObserveService.Model.REPEATING || currentValue != 0) {
        ArrayList nextArrayList = new ArrayList(88);
        RotationVector rotationVector = this.rotationData73;
        double doubleValue = 0.65;
        if (arrayList.size() >= 2) {
          double currentSize = ((RotationVector) arrayList.get(arrayList.size() - 2)).length();
          if (currentSize > 0.03) {
            doubleValue = Math.max(0.3, Math.min(0.8, this.rotationData73.length() / currentSize));
          }
        }

        if (model == BowObserveService.Model.TURNING) {
          rotationVector =
              createRotationData73(rotationVector, this.value * (this.count3 - 1) * 0.5);
        }

        for (int index = 0; index < 88; index++) {
          rotationVector =
              switch (model) {
                case LINEAR -> this.rotationData73;
                case ACCELERATING ->
                    createRotationData72(
                        rotationVector.add(this.rotationData74.multiply(Math.pow(0.55, index))),
                        1.0);
                case BRAKING -> rotationVector.multiply(doubleValue);
                case TURNING -> createRotationData73(rotationVector, this.value);
                case REPEATING ->
                    (RotationVector)
                        arrayList.get(arrayList.size() - currentValue + index % currentValue);
              };
          nextArrayList.add(rotationVector);
        }
        double currentDoubleValue =
            switch (model) {
              case LINEAR -> 0.4;
              case ACCELERATING -> 0.18;
              case BRAKING -> 0.1;
              case TURNING -> 0.22;
              case REPEATING -> 0.22;
            };
        currentArrayList.add(
            new BowObserveService.Hypothesis(
                model,
                nextArrayList,
                currentDoubleValue,
                List.of((BowObserveService.Error[]) this.error2[model.ordinal()].clone())));
      }
    }

    return List.copyOf(currentArrayList);
  }

  private static int calculateValue(List<RotationVector> items) {
    int currentSize = items.size();
    double doubleValue = 0.3;
    int value = 0;

    for (int index = 6; index <= Math.min(40, (currentSize - 4) / 2); index++) {
      int currentValue = Math.min(index * 2, currentSize - index);
      RotationVector rotationVector = rotationData7;

      for (int currentIndex = currentSize - currentValue;
          currentIndex < currentSize;
          currentIndex++) {
        rotationVector = rotationVector.add((RotationVector) items.get(currentIndex));
      }

      rotationVector = rotationVector.multiply(1.0 / currentValue);
      double currentDoubleValue = 0.0;
      double nextDoubleValue = 0.0;

      for (int nextIndex = currentSize - currentValue; nextIndex < currentSize; nextIndex++) {
        currentDoubleValue +=
            ((RotationVector) items.get(nextIndex)).subtract(rotationVector).lengthSquared();
        nextDoubleValue +=
            ((RotationVector) items.get(nextIndex))
                .subtract((RotationVector) items.get(nextIndex - index))
                .lengthSquared();
      }

      if (!(currentDoubleValue / currentValue < 0.002)) {
        double previousDoubleValue = nextDoubleValue / currentDoubleValue + index * 2.0E-4;
        if (previousDoubleValue < doubleValue) {
          doubleValue = previousDoubleValue;
          value = index;
        }
      }
    }

    return value;
  }

  private static RotationVector createRotationData7(RotationVector rotationVector) {
    return new RotationVector(rotationVector.x(), 0.0, rotationVector.z());
  }

  private static RotationVector createRotationData72(
      RotationVector rotationVector, double doubleValue) {
    double currentLength = rotationVector.length();
    return currentLength > doubleValue
        ? rotationVector.multiply(doubleValue / currentLength)
        : rotationVector;
  }

  private static double calculateValue2(
      RotationVector rotationVector, RotationVector currentRotationVector) {
    return !(rotationVector.length() < 0.03) && !(currentRotationVector.length() < 0.03)
        ? Math.atan2(
            rotationVector.x() * currentRotationVector.z()
                - rotationVector.z() * currentRotationVector.x(),
            rotationVector.x() * currentRotationVector.x()
                + rotationVector.z() * currentRotationVector.z())
        : 0.0;
  }

  private static RotationVector createRotationData73(
      RotationVector rotationVector, double doubleValue) {
    double currentDoubleValue = Math.sin(doubleValue);
    double nextDoubleValue = Math.cos(doubleValue);
    return new RotationVector(
        rotationVector.x() * nextDoubleValue - rotationVector.z() * currentDoubleValue,
        0.0,
        rotationVector.x() * currentDoubleValue + rotationVector.z() * nextDoubleValue);
  }

  public record Error(double xx, double xz, double zz) {
    public double squared() {
      return this.xx + this.zz;
    }

    public BowObserveService.Error blend(BowObserveService.Error error, double doubleValue) {
      return new BowObserveService.Error(
          this.xx + (error.xx - this.xx) * doubleValue,
          this.xz + (error.xz - this.xz) * doubleValue,
          this.zz + (error.zz - this.zz) * doubleValue);
    }

    public RotationVector sample(double doubleValue, double currentDoubleValue) {
      double nextDoubleValue = Math.sqrt(Math.max(1.0E-10, this.xx));
      double previousDoubleValue = this.xz / nextDoubleValue;
      return new RotationVector(
          nextDoubleValue * doubleValue,
          0.0,
          previousDoubleValue * doubleValue
              + Math.sqrt(Math.max(0.0, this.zz - previousDoubleValue * previousDoubleValue))
                  * currentDoubleValue);
    }
  }

  public record Estimate(
      long tick,
      RotationVector velocity,
      List<BowObserveService.Hypothesis> hypotheses,
      double confidence) {
    public Estimate(
        long tick,
        RotationVector velocity,
        List<BowObserveService.Hypothesis> hypotheses,
        double confidence) {
      hypotheses = List.copyOf(hypotheses);
      this.tick = tick;
      this.velocity = velocity;
      this.hypotheses = hypotheses;
      this.confidence = confidence;
    }

    public List<BowObserveService.Weighted> at(double doubleValue) {
      double currentDoubleValue =
          this.hypotheses.stream()
              .mapToDouble(item -> item.error(doubleValue).squared())
              .min()
              .orElse(0.0);
      double nextDoubleValue = 0.025 + 6.0E-4 * doubleValue * doubleValue;
      double previousDoubleValue = 0.0;
      double[] currentSize = new double[this.hypotheses.size()];

      for (int index = 0; index < this.hypotheses.size(); index++) {
        BowObserveService.Hypothesis hypothesis = this.hypotheses.get(index);
        currentSize[index] =
            hypothesis.prior
                * (0.015
                    + Math.exp(
                        -Math.min(
                            40.0,
                            (hypothesis.error(doubleValue).squared() - currentDoubleValue)
                                / nextDoubleValue)));
        previousDoubleValue += currentSize[index];
      }

      ArrayList arrayList = new ArrayList();

      for (int currentIndex = 0; currentIndex < this.hypotheses.size(); currentIndex++) {
        arrayList.add(
            new BowObserveService.Weighted(
                this.hypotheses.get(currentIndex),
                currentSize[currentIndex] / previousDoubleValue));
      }

      return List.copyOf(arrayList);
    }
  }

  public record Hypothesis(
      BowObserveService.Model model,
      List<RotationVector> steps,
      double prior,
      List<BowObserveService.Error> errors) {
    public Hypothesis(
        BowObserveService.Model model,
        List<RotationVector> steps,
        double prior,
        List<BowObserveService.Error> errors) {
      steps = List.copyOf(steps);
      errors = List.copyOf(errors);
      this.model = model;
      this.steps = steps;
      this.prior = prior;
      this.errors = errors;
    }

    public BowObserveService.Error error(double doubleValue) {
      if (doubleValue <= BowObserveService.int2[0]) {
        return this.errors.getFirst();
      }

      for (int index = 1; index < BowObserveService.int2.length; index++) {
        if (doubleValue <= BowObserveService.int2[index]) {
          return this.errors
              .get(index - 1)
              .blend(
                  this.errors.get(index),
                  (doubleValue - BowObserveService.int2[index - 1])
                      / (BowObserveService.int2[index] - BowObserveService.int2[index - 1]));
        }
      }

      double currentLength =
          Math.pow(doubleValue / BowObserveService.int2[BowObserveService.int2.length - 1], 2.0);
      BowObserveService.Error currentError = this.errors.getLast();
      return new BowObserveService.Error(
          currentError.xx * currentLength,
          currentError.xz * currentLength,
          currentError.zz * currentLength);
    }

    public RotationVector offset(double doubleValue) {
      double currentSize = Math.max(0.0, Math.min(this.steps.size(), doubleValue));
      int index = (int) currentSize;
      RotationVector rotationVector = BowObserveService.rotationData7;

      for (int currentIndex = 0; currentIndex < index; currentIndex++) {
        rotationVector = rotationVector.add(this.steps.get(currentIndex));
      }

      return index < this.steps.size()
          ? rotationVector.add(this.steps.get(index).multiply(currentSize - index))
          : rotationVector;
    }
  }

  private record Issued(
      long tick, RotationVector origin, List<BowObserveService.Hypothesis> hypotheses) {}

  public enum Model {
    LINEAR,
    ACCELERATING,
    BRAKING,
    TURNING,
    REPEATING;

    private static BowObserveService.Model[] $values() {
      return new BowObserveService.Model[] {LINEAR, ACCELERATING, BRAKING, TURNING, REPEATING};
    }
  }

  public record Weighted(BowObserveService.Hypothesis hypothesis, double weight) {}
}
