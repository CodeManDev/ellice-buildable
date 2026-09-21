package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleFunction;

public final class BowRankCalculator {
  private BowRankCalculator() {}

  public static List<BowRankCalculator.Candidate> rank(
      RotationVector var0,
      RotationVector var1,
      double var2,
      List<BowCoverageCalculator.Body> var4,
      RotationData var5) {
    ArrayList var6 = new ArrayList();

    for (BowCoverageCalculator.Body var8 : var4) {
      if (!(var8.weight() < Double.longBitsToDouble(4582862980812216730L))) {
        for (double var12 :
            new double[] {
              Double.longBitsToDouble(4602678819172646912L),
              Double.longBitsToDouble(4604480259023595110L),
              Double.longBitsToDouble(4599075939470750515L)
            }) {
          RotationVector var14 =
              var8.box()
                  .sample(
                      Double.longBitsToDouble(4602678819172646912L),
                      var12,
                      Double.longBitsToDouble(4602678819172646912L));
          updateState(var6, var0, var1, var2, var2x -> var14.add(var8.offset().apply(var2x)));
        }
      }
    }

    updateState(
        var6,
        var0,
        var1,
        var2,
        var1x -> {
          RotationVector var3 = new RotationVector(0.0, 0.0, 0.0);
          double var4x = 0.0;

          for (BowCoverageCalculator.Body var7 : var4) {
            var3 =
                var3.add(
                    var7.box()
                        .sample(
                            Double.longBitsToDouble(4602678819172646912L),
                            Double.longBitsToDouble(4602678819172646912L),
                            Double.longBitsToDouble(4602678819172646912L))
                        .add(var7.offset().apply(var1x))
                        .multiply(var7.weight()));
            var4x += var7.weight();
          }

          return var3.multiply(
              1.0 / Math.max(Double.longBitsToDouble(4472406533629990549L), var4x));
        });
    ArrayList var15 = new ArrayList();

    for (BowTrajectorySolver.Solution var17 :
        (Iterable<BowTrajectorySolver.Solution>) (Iterable<?>) (var6)) {
      double var18 =
          BowCoverageCalculator.rankCoverage(
              var0,
              var17.rotation().direction(),
              var1,
              var2,
              var4,
              (int) Math.ceil(var17.flightTicks()) + 3);
      if (var18 > 0.0) {
        var15.add(new BowRankCalculator.Candidate(var17, var18));
      }
    }

    var15.sort(
        Comparator.<BowRankCalculator.Candidate>comparingDouble(
                var1x ->
                    var1x.coverage()
                        - (var5 == null
                            ? 0.0
                            : Math.min(
                                    1.0,
                                    RotationData.distance(var5, var1x.solution.rotation())
                                        / Double.longBitsToDouble(4629137466983448576L))
                                * Double.longBitsToDouble(4573567551181324026L)))
            .reversed());
    return List.copyOf(var15);
  }

  private static void updateState(
      List<BowTrajectorySolver.Solution> var0,
      RotationVector var1,
      RotationVector var2,
      double var3,
      DoubleFunction<RotationVector> var5) {
    BowTrajectorySolver.solve(var1, var2, var3, var5)
        .ifPresent(
            var1x -> {
              if (var0.stream()
                  .noneMatch(
                      var1xx ->
                          RotationData.distance(var1xx.rotation(), var1x.rotation())
                              < Double.longBitsToDouble(4582862980812216730L))) {
                var0.add(var1x);
              }
            });
  }

  public record Candidate(BowTrajectorySolver.Solution solution, double coverage) {}
}
