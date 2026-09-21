package dev.felix.ellice.feature.travel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.joml.Vector3d;

public final class TravelStatusTracker {
  private static final double value = 2.0;
  private static final double value2 = 1.0;
  private static final int count = 100000;
  private static final long timestamp = 8000000000L;
  private static final int count2 = 50;
  private static final double value3 = 5.0;
  private final TravelLoadedHandler travelLoadedHandler;
  private TravelPolicy travelData2;
  private TravelIsInGoalHandler travelIsInGoalHandler;
  private final PriorityQueue<TravelStatusTracker.Entry> priorityQueue =
      new PriorityQueue<>(
          Comparator.comparingDouble(TravelStatusTracker.Entry::f)
              .thenComparing(
                  (item, currentItem) -> Double.compare(currentItem.step.g, item.step.g)));
  private final Map<TravelStatusTracker.Key, Double> entries = new HashMap<>();
  private final Map<Long, Double> entries2 = new HashMap<>();
  private final Set<TravelStatusTracker.Key> values2 = new HashSet<>();
  private TravelStatusTracker.Status status2 = TravelStatusTracker.Status.IDLE;
  private List<TravelData> items = List.of();
  private double value4 = Double.POSITIVE_INFINITY;
  private double value5 = 2.0;
  private boolean enabled;
  private int count3;
  private int count4;
  private long timestamp2;
  private long timestamp3;
  private Vector3d vector3d;
  private double value6;
  private double value7;
  private double value8;
  private int count5;
  private boolean enabled2;
  private long timestamp4;
  private double value9;

  public TravelStatusTracker(TravelLoadedHandler travelLoaded) {
    this.travelLoadedHandler = TravelStatusTracker.Objects.requireNonNull(travelLoaded, "env");
  }

  public TravelStatusTracker.Status status() {
    return this.status2;
  }

  public List<TravelData> route() {
    return this.items;
  }

  public long revision() {
    return this.timestamp2;
  }

  public double remaining() {
    return this.value8;
  }

  public double progress() {
    return this.value7;
  }

  public int visited() {
    return this.count3;
  }

  public boolean isSegment() {
    return this.enabled2;
  }

  public void stop() {
    this.travelIsInGoalHandler = null;
    this.items = List.of();
    this.priorityQueue.clear();
    this.entries.clear();
    this.entries2.clear();
    this.values2.clear();
    this.status2 = TravelStatusTracker.Status.IDLE;
    this.timestamp2++;
  }

  private boolean checkCondition(TravelIsInGoalHandler travelIsInGoal) {
    if (travelIsInGoal instanceof TravelIsInGoalHandler.BlockGoal blockGoal) {
      return this.travelLoadedHandler.loaded(blockGoal.gx(), blockGoal.gz());
    } else if (travelIsInGoal instanceof TravelIsInGoalHandler.XZGoal xZGoal) {
      return this.travelLoadedHandler.loaded(xZGoal.gx(), xZGoal.gz());
    } else if (travelIsInGoal instanceof TravelIsInGoalHandler.NearGoal nearGoal) {
      return this.travelLoadedHandler.loaded(nearGoal.gx(), nearGoal.gz());
    } else if (travelIsInGoal instanceof TravelIsInGoalHandler.CompositeGoal compositeGoal) {
      if (compositeGoal.goals().isEmpty()) {
        return true;
      }

      for (TravelIsInGoalHandler currentTravelIsInGoal : compositeGoal.goals()) {
        if (this.checkCondition(currentTravelIsInGoal)) {
          return true;
        }
      }

      return false;
    } else {
      return true;
    }
  }

  public void favorPath(List<TravelData> items) {
    this.values2.clear();
    if (items != null) {
      for (TravelData travelData : items) {
        Vector3d vector3d = travelData.pos();
        this.values2.add(
            key((int) Math.floor(vector3d.x), (int) Math.floor(vector3d.z), vector3d.y));
      }
    }
  }

  public void start(
      TravelIsInGoalHandler travelIsInGoal, TravelPolicy travelPolicy, Vector3d currentVector3d) {
    TravelStatusTracker.Objects.requireNonNull(travelIsInGoal, "goal");
    TravelStatusTracker.Objects.requireNonNull(travelPolicy, "ctx");
    TravelStatusTracker.Objects.requireNonNull(currentVector3d, "player");
    this.travelIsInGoalHandler = travelIsInGoal;
    this.travelData2 = travelPolicy;
    this.items = List.of();
    this.priorityQueue.clear();
    this.entries.clear();
    this.entries2.clear();
    this.count3 = 0;
    this.count4 = 0;
    this.value7 = 0.0;
    this.enabled2 = false;
    this.value5 = 2.0;
    this.enabled = false;
    this.value4 = Double.POSITIVE_INFINITY;
    this.vector3d = new Vector3d(currentVector3d);
    this.timestamp3 = System.nanoTime();
    this.timestamp2++;
    int value = (int) Math.floor(currentVector3d.x);
    int currentValue = (int) Math.floor(currentVector3d.z);
    if (!this.travelLoadedHandler.loaded(value, currentValue)) {
      this.status2 = TravelStatusTracker.Status.UNEXPLORED;
    } else if (!this.checkCondition(travelIsInGoal)) {
      this.status2 = TravelStatusTracker.Status.UNEXPLORED;
    } else {
      Double doubleValue = this.calculateValue(value, currentValue, currentVector3d.y);
      if (doubleValue == null) {
        this.status2 = TravelStatusTracker.Status.UNAVAILABLE;
      } else {
        this.value8 = travelIsInGoal.heuristic(value, doubleValue, currentValue);
        TravelStatusTracker.Step step =
            new TravelStatusTracker.Step(
                key(value, currentValue, doubleValue), doubleValue, 0.0, null, null);
        this.entries.put(step.key, 0.0);
        this.priorityQueue.add(
            new TravelStatusTracker.Entry(
                step, this.value5 * travelIsInGoal.heuristic(value, doubleValue, currentValue)));
        this.status2 = TravelStatusTracker.Status.PLANNING;
      }
    }
  }

  public void tick(Vector3d vector3d) {
    this.tick(vector3d, 800000L);
  }

  public void tick(Vector3d vector3d, long longValue) {
    if (this.travelIsInGoalHandler != null) {
      long currentLongValue = System.nanoTime();
      if (this.status2 == TravelStatusTracker.Status.PLANNING
          || this.status2 == TravelStatusTracker.Status.READY && !this.enabled) {
        this.updateState(currentLongValue, Math.max(50000L, longValue));
        currentLongValue = System.nanoTime();
      }

      if (this.status2 == TravelStatusTracker.Status.READY) {
        this.updateState6(vector3d);
        if (this.checkCondition4(vector3d)) {
          this.status2 = TravelStatusTracker.Status.ARRIVED;
          this.timestamp2++;
        } else if (this.value9 > 3.0 && currentLongValue - this.timestamp3 > 900000000L) {
          this.start(this.travelIsInGoalHandler, this.travelData2, vector3d);
        } else if (currentLongValue - this.timestamp4 > 1000000000L) {
          this.timestamp4 = currentLongValue;

          for (int index = this.count5;
              index < Math.min(this.items.size(), this.count5 + 12);
              index++) {
            Vector3d currentVector3d = this.items.get(index).pos();
            Double doubleValue =
                this.travelLoadedHandler.floor(
                    (int) Math.floor(currentVector3d.x),
                    (int) Math.floor(currentVector3d.z),
                    currentVector3d.y);
            if (doubleValue == null || Math.abs(doubleValue - currentVector3d.y) > 0.6) {
              this.start(this.travelIsInGoalHandler, this.travelData2, vector3d);
              break;
            }
          }
        }
      } else if ((this.status2 == TravelStatusTracker.Status.UNEXPLORED
              || this.status2 == TravelStatusTracker.Status.UNAVAILABLE)
          && currentLongValue - this.timestamp3 > 3000000000L) {
        this.start(this.travelIsInGoalHandler, this.travelData2, vector3d);
      }
    }
  }

  private void updateState(long offset, long currentOffset) {
    long nextOffset = offset + currentOffset;

    while (!this.priorityQueue.isEmpty() && System.nanoTime() < nextOffset) {
      TravelStatusTracker.Step currentStep = this.priorityQueue.remove().step;
      if (!(currentStep.g
          > this.entries.getOrDefault(currentStep.key, Double.POSITIVE_INFINITY) + 1.0E-9)) {
        if (++this.count3 > 100000 || System.nanoTime() - this.timestamp3 > 8000000000L) {
          break;
        }

        if (this.travelIsInGoalHandler.isInGoal(
            currentStep.key.x, currentStep.y, currentStep.key.z)) {
          double doubleValue = currentStep.g;
          if (doubleValue + 0.01 < this.value4) {
            this.value4 = doubleValue;
            this.updateState5(currentStep, false);
          }

          if (!(this.value5 > 1.0)) {
            this.enabled = true;
            break;
          }

          this.value5 = 1.0;
          this.updateState3();
        } else {
          if (this.checkCondition3(currentStep.key.x, currentStep.key.z)) {
            this.count4++;
          }

          this.updateState2(currentStep);
          if (this.value4 < Double.POSITIVE_INFINITY
              && !this.priorityQueue.isEmpty()
              && this.priorityQueue.peek().f >= this.value4) {
            this.enabled = true;
            break;
          }
        }
      }
    }

    if (this.status2 == TravelStatusTracker.Status.PLANNING) {
      if (this.value4 < Double.POSITIVE_INFINITY) {
        this.status2 = TravelStatusTracker.Status.READY;
        this.timestamp2++;
        if (!(this.value5 > 1.0)) {
          this.enabled = true;
        }
      } else if (this.priorityQueue.isEmpty()) {
        this.status2 = TravelStatusTracker.Status.UNAVAILABLE;
        this.timestamp2++;
      } else if (this.count3 > 100000 || System.nanoTime() - this.timestamp3 > 8000000000L) {
        this.updateState4();
      }
    } else if (this.status2 == TravelStatusTracker.Status.READY
        && !this.enabled
        && this.priorityQueue.isEmpty()) {
      this.enabled = true;
      this.timestamp2++;
    }
  }

  private void updateState2(TravelStatusTracker.Step step) {
    for (TravelTypeData travelTypeData :
        TravelTypeData.generate(
            this.travelData2, this.travelLoadedHandler, step.key.x, step.y, step.key.z)) {
      double doubleValue = travelTypeData.costTicks();
      if (this.values2.contains(
          key(travelTypeData.destX(), travelTypeData.destZ(), travelTypeData.destY()))) {
        doubleValue *= 1.0 - this.travelData2.backtrackFavoring() * 0.9;
      }

      TravelStatusTracker.Step currentStep = step;
      double currentDoubleValue = step.g + doubleValue;
      if (step.parent != null
          && checkCondition2(travelTypeData)
          && TravelTypeData.lineOfSight(
              this.travelLoadedHandler,
              step.parent.key.x,
              step.parent.y,
              step.parent.key.z,
              travelTypeData.destX(),
              travelTypeData.destY(),
              travelTypeData.destZ())) {
        double nextDoubleValue =
            step.parent.g
                + TravelFallCostService.walkCost(
                    Math.hypot(
                        travelTypeData.destX() - step.parent.key.x,
                        travelTypeData.destZ() - step.parent.key.z),
                    this.travelData2.canSprint())
                + Math.max(0.0, travelTypeData.destY() - step.parent.y) * 1.2;
        if (nextDoubleValue < currentDoubleValue) {
          currentStep = step.parent;
          currentDoubleValue = nextDoubleValue;
        }
      }

      TravelStatusTracker.Key currentKey =
          key(travelTypeData.destX(), travelTypeData.destZ(), travelTypeData.destY());
      if (!(currentDoubleValue + 0.01
          >= this.entries.getOrDefault(currentKey, Double.POSITIVE_INFINITY))) {
        this.entries.put(currentKey, currentDoubleValue);
        TravelStatusTracker.Step nextStep =
            new TravelStatusTracker.Step(
                currentKey,
                travelTypeData.destY(),
                currentDoubleValue,
                currentStep,
                travelTypeData);
        this.priorityQueue.add(
            new TravelStatusTracker.Entry(
                nextStep,
                currentDoubleValue
                    + this.value5
                        * this.travelIsInGoalHandler.heuristic(
                            currentKey.x, travelTypeData.destY(), currentKey.z)));
      }
    }
  }

  private static boolean checkCondition2(TravelTypeData travelTypeData) {
    return switch (travelTypeData.type()) {
          case TRAVERSE, DIAGONAL, DESCEND, FALL -> 1;
          default -> 0;
        }
        != 0;
  }

  private boolean checkCondition3(int value, int currentValue) {
    return !this.travelLoadedHandler.loaded(value + 1, currentValue)
        || !this.travelLoadedHandler.loaded(value - 1, currentValue)
        || !this.travelLoadedHandler.loaded(value, currentValue + 1)
        || !this.travelLoadedHandler.loaded(value, currentValue - 1);
  }

  private void updateState3() {
    ArrayList arrayList = new ArrayList<>(this.priorityQueue);
    this.priorityQueue.clear();

    for (TravelStatusTracker.Entry entry :
        (Iterable<TravelStatusTracker.Entry>) (Iterable<?>) (arrayList)) {
      TravelStatusTracker.Step currentStep = entry.step;
      this.priorityQueue.add(
          new TravelStatusTracker.Entry(
              currentStep,
              currentStep.g
                  + this.value5
                      * this.travelIsInGoalHandler.heuristic(
                          currentStep.key.x, currentStep.y, currentStep.key.z)));
    }
  }

  private void updateState4() {
    for (double doubleValue : new double[] {1.0, 1.5, 2.5, 5.0}) {
      TravelStatusTracker.Step currentStep = null;
      double currentDoubleValue = Double.POSITIVE_INFINITY;

      for (TravelStatusTracker.Entry entry : this.priorityQueue) {
        TravelStatusTracker.Step nextStep = entry.step;
        double nextDoubleValue =
            Math.hypot(
                nextStep.key.x + 0.5 - this.vector3d.x, nextStep.key.z + 0.5 - this.vector3d.z);
        if (!(nextDoubleValue < 5.0)) {
          double previousDoubleValue =
              nextStep.g
                  + doubleValue
                      * this.travelIsInGoalHandler.heuristic(
                          nextStep.key.x, nextStep.y, nextStep.key.z);
          if (previousDoubleValue < currentDoubleValue) {
            currentDoubleValue = previousDoubleValue;
            currentStep = nextStep;
          }
        }
      }

      if (currentStep != null) {
        this.updateState5(currentStep, true);
        this.status2 = TravelStatusTracker.Status.READY;
        this.timestamp2++;
        return;
      }
    }

    this.status2 = TravelStatusTracker.Status.UNAVAILABLE;
    this.timestamp2++;
  }

  private void updateState5(TravelStatusTracker.Step step, boolean enabled) {
    ArrayList arrayList = new ArrayList();

    for (TravelStatusTracker.Step currentStep = step;
        currentStep != null;
        currentStep = currentStep.parent) {
      arrayList.add(currentStep);
    }

    Collections.reverse(arrayList);
    ArrayList currentArrayList = this.collectValues(arrayList);
    if (currentArrayList.size() == 1) {
      currentArrayList.add(
          TravelData.walk(
              new Vector3d(((TravelData) currentArrayList.getFirst()).pos()).add(0.01, 0.0, 0.0),
              0.0));
    }

    this.items = List.copyOf(currentArrayList);
    this.enabled2 = enabled;
    this.value6 = 0.0;

    for (int index = 1; index < this.items.size(); index++) {
      this.value6 =
          this.value6 + this.items.get(index - 1).pos().distance(this.items.get(index).pos());
    }

    this.value8 = this.value6;
    this.value4 = step.g;
    this.timestamp2++;
  }

  private ArrayList<TravelData> collectValues(ArrayList<TravelStatusTracker.Step> arrayList) {
    ArrayList currentArrayList = new ArrayList();
    currentArrayList.add(createTravelData((TravelStatusTracker.Step) arrayList.getFirst()));
    if (arrayList.size() <= 2) {
      for (int index = 1; index < arrayList.size(); index++) {
        currentArrayList.add(createTravelData((TravelStatusTracker.Step) arrayList.get(index)));
      }

      return currentArrayList;
    } else {
      for (int currentIndex = 2; currentIndex < arrayList.size(); currentIndex++) {
        Vector3d vector3d = ((TravelData) currentArrayList.getLast()).pos();
        Vector3d currentVector3d =
            createVector3d((TravelStatusTracker.Step) arrayList.get(currentIndex));
        int value = Math.abs(vector3d.y - currentVector3d.y) < 0.6 ? 1 : 0;
        if (value == 0
            || !TravelTypeData.lineOfSight(
                this.travelLoadedHandler,
                (int) Math.floor(vector3d.x),
                vector3d.y,
                (int) Math.floor(vector3d.z),
                (int) Math.floor(currentVector3d.x),
                currentVector3d.y,
                (int) Math.floor(currentVector3d.z))) {
          Vector3d nextVector3d =
              createVector3d((TravelStatusTracker.Step) arrayList.get(currentIndex - 1));
          double doubleValue = Math.hypot(nextVector3d.x - vector3d.x, nextVector3d.z - vector3d.z);
          currentArrayList.add(
              TravelData.walk(
                  nextVector3d,
                  TravelFallCostService.walkCost(doubleValue, this.travelData2.canSprint())));
        }
      }

      currentArrayList.add(createTravelData((TravelStatusTracker.Step) arrayList.getLast()));
      return currentArrayList;
    }
  }

  private static Vector3d createVector3d(TravelStatusTracker.Step step) {
    return new Vector3d(step.key.x + 0.5, step.y, step.key.z + 0.5);
  }

  private static TravelData createTravelData(TravelStatusTracker.Step step) {
    return step.move == null
        ? TravelData.walk(createVector3d(step), 0.0)
        : new TravelData(createVector3d(step), step.move.type(), step.move.costTicks());
  }

  private void updateState6(Vector3d vector3d) {
    double doubleValue = Double.POSITIVE_INFINITY;
    double currentDoubleValue = 0.0;
    double nextDoubleValue = 0.0;

    for (int index = 1; index < this.items.size(); index++) {
      Vector3d currentVector3d = this.items.get(index - 1).pos();
      Vector3d nextVector3d = this.items.get(index).pos();
      double previousDoubleValue = nextVector3d.x - currentVector3d.x;
      double sourceDoubleValue = nextVector3d.y - currentVector3d.y;
      double targetDoubleValue = nextVector3d.z - currentVector3d.z;
      double inputDoubleValue =
          previousDoubleValue * previousDoubleValue
              + sourceDoubleValue * sourceDoubleValue
              + targetDoubleValue * targetDoubleValue;
      double outputDoubleValue =
          inputDoubleValue < 1.0E-9
              ? 0.0
              : ((vector3d.x - currentVector3d.x) * previousDoubleValue
                      + (vector3d.y - currentVector3d.y) * sourceDoubleValue
                      + (vector3d.z - currentVector3d.z) * targetDoubleValue)
                  / inputDoubleValue;
      outputDoubleValue = Math.clamp(outputDoubleValue, 0.0, 1.0);
      double resultDoubleValue = currentVector3d.x + previousDoubleValue * outputDoubleValue;
      double candidateDoubleValue = currentVector3d.y + sourceDoubleValue * outputDoubleValue;
      double selectedDoubleValue = currentVector3d.z + targetDoubleValue * outputDoubleValue;
      double defaultDoubleValue =
          (resultDoubleValue - vector3d.x) * (resultDoubleValue - vector3d.x)
              + (candidateDoubleValue - vector3d.y) * (candidateDoubleValue - vector3d.y)
              + (selectedDoubleValue - vector3d.z) * (selectedDoubleValue - vector3d.z);
      double initialDoubleValue = Math.sqrt(inputDoubleValue);
      if (defaultDoubleValue < doubleValue) {
        doubleValue = defaultDoubleValue;
        currentDoubleValue = nextDoubleValue + initialDoubleValue * outputDoubleValue;
        this.count5 = index - 1;
      }

      nextDoubleValue += initialDoubleValue;
    }

    this.value7 = currentDoubleValue;
    this.value8 = Math.max(0.0, this.value6 - currentDoubleValue);
    this.value9 = Math.sqrt(doubleValue);
  }

  private boolean checkCondition4(Vector3d vector3d) {
    if (this.items.isEmpty()) {
      return false;
    }

    Vector3d currentVector3d = this.items.getLast().pos();
    double doubleValue = Math.hypot(vector3d.x - currentVector3d.x, vector3d.z - currentVector3d.z);
    return doubleValue < 2.0 && Math.abs(vector3d.y - currentVector3d.y) < 2.0 && !this.enabled2;
  }

  private Double calculateValue(int value, int currentValue, double doubleValue) {
    long longValue =
        (long) value << 42 ^ (long) currentValue << 20 ^ Math.round(doubleValue * 8.0) & 1048575L;
    Double currentDoubleValue = this.entries2.get(longValue);
    if (currentDoubleValue == null && !this.entries2.containsKey(longValue)) {
      Double nextDoubleValue = this.travelLoadedHandler.floor(value, currentValue, doubleValue);
      this.entries2.put(longValue, nextDoubleValue);
      return nextDoubleValue;
    } else {
      return currentDoubleValue;
    }
  }

  static TravelStatusTracker.Key key(int value, int currentValue, double doubleValue) {
    return new TravelStatusTracker.Key(value, currentValue, (int) Math.round(doubleValue * 8.0));
  }

  private record Entry(TravelStatusTracker.Step step, double f) {}

  private record Key(int x, int z, int yq) {}

  private static final class Objects {
    static <T> T requireNonNull(T t, String text) {
      return java.util.Objects.requireNonNull((T) t, text);
    }
  }

  public enum Status {
    IDLE,
    PLANNING,
    READY,
    ARRIVED,
    UNAVAILABLE,
    UNEXPLORED;

    private static TravelStatusTracker.Status[] $values() {
      return new TravelStatusTracker.Status[] {
        IDLE, PLANNING, READY, ARRIVED, UNAVAILABLE, UNEXPLORED
      };
    }
  }

  private record Step(
      TravelStatusTracker.Key key,
      double y,
      double g,
      TravelStatusTracker.Step parent,
      TravelTypeData move) {}
}
