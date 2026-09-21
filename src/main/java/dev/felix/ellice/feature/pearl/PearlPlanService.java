package dev.felix.ellice.feature.pearl;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class PearlPlanService {
  private static final int count = 2400;
  private static final List<RotationVector> fhxwdxf9jewi = PearlPlanService.m2hy9u5evsd();

  public Optional<Plan> plan(
      RotationVector rotationVector,
      RotationVector rotationVector3,
      List<RotationVector> list,
      double d,
      World world) {
    ArrayList<Candidate> arrayList = new ArrayList<Candidate>();
    for (RotationVector object :
        list.stream()
            .sorted(
                Comparator.comparingDouble(
                    rotationVector2 -> rotationVector2.subtract(rotationVector).lengthSquared()))
            .limit(48L)
            .toList()) {
      for (PearlTrajectorySolver.Solution solution :
          PearlTrajectorySolver.solve(
              rotationVector,
              rotationVector3,
              object.add(
                  new RotationVector(0.0, Double.longBitsToDouble(-4643571503789171016L), 0.0)),
              d)) {
        arrayList.add(new Candidate(solution));
      }
    }
    arrayList.sort(Comparator.comparingDouble(candidate -> candidate.solution().ticks()));
    int[] object = new int[] {2400};
    for (Candidate candidate2 : arrayList) {
      if (object[0] <= 0) break;
      Optional<Plan> optional =
          this.findResult(
              rotationVector,
              rotationVector3,
              candidate2.solution().rotation(),
              d,
              world,
              (int[]) object);
      if (!optional.isPresent()) continue;
      return optional;
    }
    return Optional.empty();
  }

  public Optional<Plan> verify(
      RotationVector rotationVector,
      RotationVector rotationVector2,
      RotationData rotationData,
      double d,
      World world) {
    return this.findResult(
        rotationVector, rotationVector2, rotationData, d, world, new int[] {2400});
  }

  private Optional<Plan> findResult(
      RotationVector rotationVector,
      RotationVector rotationVector2,
      RotationData rotationData,
      double d,
      World world,
      int[] nArray) {
    PearlTrajectorySolver.Launch launch =
        PearlTrajectorySolver.launch(rotationVector, rotationVector2, rotationData);
    Plan plan = this.mfeppkkgh179(launch, rotationData, d, world, nArray).orElse(null);
    if (plan == null) {
      return Optional.empty();
    }
    for (RotationVector rotationVector3 : fhxwdxf9jewi) {
      if (!this.mfeppkkgh179(
              new PearlTrajectorySolver.Launch(
                  launch.origin(), launch.velocity().add(rotationVector3)),
              rotationData,
              Math.min(d, plan.flightTicks() + Double.longBitsToDouble(0x4014000000000000L)),
              world,
              nArray)
          .isEmpty()) continue;
      return Optional.empty();
    }
    return Optional.of(plan);
  }

  private Optional<Plan> mfeppkkgh179(
      PearlTrajectorySolver.Launch launch,
      RotationData rotationData,
      double d,
      World world,
      int[] nArray) {
    RotationVector rotationVector = launch.origin();
    int n = 1;
    while ((double) n <= Math.min(Double.longBitsToDouble(0x4054000000000000L), d)) {
      nArray[0] = nArray[0] - 1;
      if (nArray[0] < 0) {
        return Optional.empty();
      }
      RotationVector rotationVector2 = PearlTrajectorySolver.position(launch, n);
      Collision collision = world.sweep(rotationVector, rotationVector2);
      if (collision != Collision.CLEAR) {
        return collision == Collision.TOP && world.safeLanding(rotationVector, n)
            ? Optional.of(new Plan(rotationData, rotationVector, n))
            : Optional.empty();
      }
      rotationVector = rotationVector2;
      ++n;
    }
    return Optional.empty();
  }

  private static List<RotationVector> m2hy9u5evsd() {
    ArrayList<RotationVector> arrayList = new ArrayList<RotationVector>();
    double d = Double.longBitsToDouble(4583105454616154357L);
    for (int n : new int[] {-1, 1}) {
      arrayList.add(new RotationVector((double) n * d, 0.0, 0.0));
      arrayList.add(new RotationVector(0.0, (double) n * d, 0.0));
      arrayList.add(new RotationVector(0.0, 0.0, (double) n * d));
    }
    for (int n : new int[] {-1, 1}) {
      for (int n2 : new int[] {-1, 1}) {
        for (int n3 : new int[] {-1, 1}) {
          arrayList.add(new RotationVector((double) n * d, (double) n2 * d, (double) n3 * d));
        }
      }
    }
    return List.copyOf(arrayList);
  }

  public static int deadline(double d, double d2, double d3, int n) {
    for (int i = 1; i <= 80 + n; ++i) {
      d += d2;
      d2 =
          (d2 - Double.longBitsToDouble(4590429028186199163L))
              * Double.longBitsToDouble(4607002274814922588L);
      if (!(d <= d3)) continue;
      return Math.max(0, i - n);
    }
    return 80;
  }

  private record Candidate(PearlTrajectorySolver.Solution solution) {}

  public static interface World {
    public Collision sweep(RotationVector var1, RotationVector var2);

    public boolean safeLanding(RotationVector var1, double var2);
  }

  public record Plan(RotationData rotation, RotationVector landing, double flightTicks) {}

  public static enum Collision {
    CLEAR,
    TOP,
    BLOCKED;
  }
}
