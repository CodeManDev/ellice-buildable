package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ScaffoldCorridorService {
   public static final double MINIMUM_WIRE_MARGIN = 0.04;

   private ScaffoldCorridorService() {
   }

   public static List<ScaffoldCorridorService.EyeSample> corridor(RotationVector var0, RotationVector var1, RotationVector var2, int var3) {
      Objects.requireNonNull(var0, "previousWireEye");
      Objects.requireNonNull(var1, "clientPreUseEye");
      Objects.requireNonNull(var2, "followingWireEye");
      if (var3 >= 0 && var3 <= 8) {
         ArrayList var4 = new ArrayList(var3 + 3);
         var4.add(new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.PREVIOUS_WIRE_EYE, var0));
         var4.add(new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.CLIENT_PRE_USE_EYE, var1));
         RotationVector var5 = var2.subtract(var1);

         for (int var6 = 1; var6 <= var3; var6++) {
            double var7 = var6 / (var3 + 1.0);
            var4.add(new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.MOVEMENT_CORRIDOR, var1.add(var5.multiply(var7))));
         }

         var4.add(new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.FOLLOWING_WIRE_EYE, var2));
         return List.copyOf(var4);
      } else {
         throw new IllegalArgumentException("Intermediate sample count must be in [0, 8]");
      }
   }

   public static ScaffoldCorridorService.Evaluation evaluate(
      PlacementCandidate var0, RotationData var1, double var2, List<ScaffoldCorridorService.EyeSample> var4, ScaffoldCorridorService.Probe var5
   ) {
      Objects.requireNonNull(var0, "intended");
      Objects.requireNonNull(var1, "wireRotation");
      Objects.requireNonNull(var4, "eyeSamples");
      Objects.requireNonNull(var5, "probe");
      double var6 = mixxl3u9zivo(var2);
      byte var8 = 0;
      byte var9 = 0;

      for (ScaffoldCorridorService.EyeSample var11 : var4) {
         Objects.requireNonNull(var11, "eye sample");
         var8 |= var11.phase() == ScaffoldCorridorService.Phase.PREVIOUS_WIRE_EYE ? 1 : 0;
         var9 |= var11.phase() == ScaffoldCorridorService.Phase.FOLLOWING_WIRE_EYE ? 1 : 0;
      }

      return var8 != 0 && var9 != 0
         ? mgvuyvuryyac(var0, var1, var6, var4, var5)
         : new ScaffoldCorridorService.Evaluation(false, ScaffoldCorridorService.Failure.MISSING_WIRE_ENDPOINT, var6, 0.0, List.of());
   }

   public static ScaffoldCorridorService.Preflight evaluateBeforePhysics(
      PlacementCandidate var0, RotationData var1, double var2, RotationVector var4, RotationVector var5, ScaffoldCorridorService.Probe var6
   ) {
      Objects.requireNonNull(var0, "intended");
      Objects.requireNonNull(var1, "wireRotation");
      Objects.requireNonNull(var4, "previousWireEye");
      Objects.requireNonNull(var5, "clientPreUseEye");
      Objects.requireNonNull(var6, "probe");
      List var7 = List.of(
         new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.PREVIOUS_WIRE_EYE, var4),
         new ScaffoldCorridorService.EyeSample(ScaffoldCorridorService.Phase.CLIENT_PRE_USE_EYE, var5)
      );
      ScaffoldCorridorService.Evaluation var8 = mgvuyvuryyac(var0, var1, mixxl3u9zivo(var2), var7, var6);
      return new ScaffoldCorridorService.Preflight(var8.ready(), var8.firstFailure(), var8.requiredMargin(), var8.narrowestMargin(), var8.samples());
   }

   private static ScaffoldCorridorService.Evaluation mgvuyvuryyac(
      PlacementCandidate var0, RotationData var1, double var2, List<ScaffoldCorridorService.EyeSample> var4, ScaffoldCorridorService.Probe var5
   ) {
      HashMap<RotationVector, Optional<ScaffoldBlockHit>> var6 = new HashMap<>();
      ArrayList<ScaffoldCorridorService.SampleResult> var7 = new ArrayList<>(var4.size());
      ScaffoldCorridorService.Failure var8 = ScaffoldCorridorService.Failure.NONE;
      double var9 = Double.longBitsToDouble(4602678819172646912L);

      for (ScaffoldCorridorService.EyeSample var12 : var4) {
         Optional<ScaffoldBlockHit> var13 = var6.computeIfAbsent(var12.eye(), var2x -> Objects.requireNonNull(var5.raycast(var2x, var1), "Rotation-place probe returned null"));
         ScaffoldCorridorService.SampleResult var14 = m95rlvofrsav(var0, var12, var13, var2);
         var7.add(var14);
         if (var14.usable()) {
            var9 = Math.min(var9, var14.edgeMargin());
         } else if (var8 == ScaffoldCorridorService.Failure.NONE) {
            var8 = var14.failure();
         }
      }

      int var15 = var8 == ScaffoldCorridorService.Failure.NONE ? 1 : 0;
      return new ScaffoldCorridorService.Evaluation((var15 != 0), var8, var2, var15 != 0 ? var9 : 0.0, var7);
   }

   private static double mixxl3u9zivo(double var0) {
      if (Double.isFinite(var0) && !(var0 < 0.0) && !(var0 > Double.longBitsToDouble(4602678819172646912L))) {
         return Math.max(Double.longBitsToDouble(4585925428558828667L), var0);
      } else {
         throw new IllegalArgumentException("Configured margin must be in [0, 0.5]");
      }
   }

   private static ScaffoldCorridorService.SampleResult m95rlvofrsav(
      PlacementCandidate var0, ScaffoldCorridorService.EyeSample var1, Optional<ScaffoldBlockHit> var2, double var3
   ) {
      if (var2.isEmpty()) {
         return new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.RAY_MISS, 0.0);
      } else {
         ScaffoldBlockHit var5 = (ScaffoldBlockHit)var2.get();
         if (!var0.supportPosition().equals(var5.blockPosition())) {
            return new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.WRONG_SUPPORT_FACE, 0.0);
         } else if (var1.phase() == ScaffoldCorridorService.Phase.FOLLOWING_WIRE_EYE && var0.clickedFace() != var5.face()) {
            return new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.WRONG_SUPPORT_FACE, 0.0);
         } else {
            double var6 = var5.faceEdgeMargin();
            if (var5.inside()) {
               return new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.INSIDE_SUPPORT, var6);
            } else {
               return var6 < var3
                  ? new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.EDGE_MARGIN, var6)
                  : new ScaffoldCorridorService.SampleResult(var1, ScaffoldCorridorService.Failure.NONE, var6);
            }
         }
      }
   }

   public record Evaluation(
      boolean ready,
      ScaffoldCorridorService.Failure firstFailure,
      double requiredMargin,
      double narrowestMargin,
      List<ScaffoldCorridorService.SampleResult> samples
   ) {
      public Evaluation(
         boolean ready,
         ScaffoldCorridorService.Failure firstFailure,
         double requiredMargin,
         double narrowestMargin,
         List<ScaffoldCorridorService.SampleResult> samples
      ) {
         Objects.requireNonNull(firstFailure, "firstFailure");
         samples = List.copyOf(samples);
         if (Double.isFinite(requiredMargin)
            && !(requiredMargin < Double.longBitsToDouble(4585925428558828667L))
            && !(requiredMargin > Double.longBitsToDouble(4602678819172646912L))
            && Double.isFinite(narrowestMargin)
            && !(narrowestMargin < 0.0)
            && !(narrowestMargin > Double.longBitsToDouble(4602678819172646912L))) {
            this.ready = ready;
            this.firstFailure = firstFailure;
            this.requiredMargin = requiredMargin;
            this.narrowestMargin = narrowestMargin;
            this.samples = samples;
         } else {
            throw new IllegalArgumentException("Invalid rotation-place evaluation");
         }
      }
   }

   public record EyeSample(ScaffoldCorridorService.Phase phase, RotationVector eye) {
      public EyeSample(ScaffoldCorridorService.Phase phase, RotationVector eye) {
         Objects.requireNonNull(phase, "phase");
         Objects.requireNonNull(eye, "eye");
         this.phase = phase;
         this.eye = eye;
      }
   }

   public enum Failure {
      NONE,
      MISSING_WIRE_ENDPOINT,
      RAY_MISS,
      WRONG_SUPPORT_FACE,
      INSIDE_SUPPORT,
      EDGE_MARGIN;
   }

   public enum Phase {
      PREVIOUS_WIRE_EYE,
      CLIENT_PRE_USE_EYE,
      MOVEMENT_CORRIDOR,
      FOLLOWING_WIRE_EYE;
   }

   public record Preflight(
      boolean ready,
      ScaffoldCorridorService.Failure firstFailure,
      double requiredMargin,
      double narrowestMargin,
      List<ScaffoldCorridorService.SampleResult> samples
   ) {
      public Preflight(
         boolean ready,
         ScaffoldCorridorService.Failure firstFailure,
         double requiredMargin,
         double narrowestMargin,
         List<ScaffoldCorridorService.SampleResult> samples
      ) {
         Objects.requireNonNull(firstFailure, "firstFailure");
         samples = List.copyOf(samples);
         if (Double.isFinite(requiredMargin)
            && !(requiredMargin < Double.longBitsToDouble(4585925428558828667L))
            && !(requiredMargin > Double.longBitsToDouble(4602678819172646912L))
            && Double.isFinite(narrowestMargin)
            && !(narrowestMargin < 0.0)
            && !(narrowestMargin > Double.longBitsToDouble(4602678819172646912L))) {
            this.ready = ready;
            this.firstFailure = firstFailure;
            this.requiredMargin = requiredMargin;
            this.narrowestMargin = narrowestMargin;
            this.samples = samples;
         } else {
            throw new IllegalArgumentException("Invalid rotation-place preflight");
         }
      }
   }

   @FunctionalInterface
   public interface Probe {
      Optional<ScaffoldBlockHit> raycast(RotationVector var1, RotationData var2);
   }

   public record SampleResult(ScaffoldCorridorService.EyeSample sample, ScaffoldCorridorService.Failure failure, double edgeMargin) {
      public SampleResult(ScaffoldCorridorService.EyeSample sample, ScaffoldCorridorService.Failure failure, double edgeMargin) {
         Objects.requireNonNull(sample, "sample");
         Objects.requireNonNull(failure, "failure");
         if (Double.isFinite(edgeMargin) && !(edgeMargin < 0.0) && !(edgeMargin > Double.longBitsToDouble(4602678819172646912L))) {
            this.sample = sample;
            this.failure = failure;
            this.edgeMargin = edgeMargin;
         } else {
            throw new IllegalArgumentException("Edge margin must be in [0, 0.5]");
         }
      }

      public boolean usable() {
         return this.failure == ScaffoldCorridorService.Failure.NONE;
      }
   }
}
