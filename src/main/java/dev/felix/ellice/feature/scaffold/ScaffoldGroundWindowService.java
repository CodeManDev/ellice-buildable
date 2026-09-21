package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ScaffoldGroundWindowService {
   private static final int fdxlb79ss7nx = 4;
   private final ScaffoldTrajectorySolver fdyzeiyu6r1b = new ScaffoldTrajectorySolver(ScaffoldTrajectorySolver.SelectionPolicy.MINIMUM_ROTATION);
   private final RotationVanillaGcdService f5ec6jqdmhpc = new RotationVanillaGcdService();
   private ScaffoldGroundWindowService.Retained f62htl0zfsbx;

   public static Optional<ScaffoldGroundWindowService.Window> groundWindow(
      ScaffoldPlayerSnapshot var0, ScaffoldRemapService.WorldVector var1, double var2, double var4
   ) {
      Objects.requireNonNull(var0, "frame");
      Objects.requireNonNull(var1, "actualDirection");
      if (var0.onGround() && Double.isFinite(var2) && !(var2 < 0.0) && Double.isFinite(var4) && !(var4 < 0.0) && !(var4 > 1.0)) {
         double var6 = var1.length();
         double var8 = var6 < Double.longBitsToDouble(4472406533629990549L) ? 0.0 : var1.x() / var6 * var2;
         double var10 = var6 < Double.longBitsToDouble(4472406533629990549L) ? 0.0 : var1.z() / var6 * var2;
         double var12 = var0.velocity().x() + var8;
         double var14 = var0.velocity().z() + var10;
         double var16 = (float)var4 * Float.intBitsToFloat(1063843267);
         double var18 = var0.eyePosition().x() + var12;
         double var20 = var0.eyePosition().z() + var14;
         double var22 = var18 + var12 * var16 + var8;
         double var24 = var20 + var14 * var16 + var10;
         return Double.isFinite(var18) && Double.isFinite(var20) && Double.isFinite(var22) && Double.isFinite(var24)
            ? Optional.of(
               new ScaffoldGroundWindowService.Window(
                  new RotationVector(var18, var0.eyePosition().y(), var20), new RotationVector(var22, var0.eyePosition().y(), var24)
               )
            )
            : Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public ScaffoldGroundWindowService.Result select(
      RotationData var1,
      PlacementCandidate var2,
      double var3,
      int var5,
      long var6,
      double var8,
      int var10,
      int var11,
      int var12,
      ScaffoldGroundWindowService.Forecast var13,
      ScaffoldCorridorService.Probe var14
   ) {
      return this.select(var1, var2, var3, var5, var6, var8, var10, var11, var12, var13, var14, false);
   }

   public ScaffoldGroundWindowService.Result select(
      RotationData var1,
      PlacementCandidate var2,
      double var3,
      int var5,
      long var6,
      double var8,
      int var10,
      int var11,
      int var12,
      ScaffoldGroundWindowService.Forecast var13,
      ScaffoldCorridorService.Probe var14,
      boolean var15
   ) {
      Objects.requireNonNull(var1, "current");
      Objects.requireNonNull(var2, "placement");
      Objects.requireNonNull(var13, "forecast");
      Objects.requireNonNull(var14, "probe");
      if (var10 >= 1
         && var10 <= 9
         && var10 % 2 != 0
         && var11 >= 0
         && var11 <= 8
         && var12 >= 1
         && var12 <= 512
         && Double.isFinite(var8)
         && !(var8 < 0.0)
         && !(var8 > Double.longBitsToDouble(4601778099247172813L))) {
         double var16 = this.f5ec6jqdmhpc.vanillaGcd(var3);
         ScaffoldGroundWindowService.Key var18 = new ScaffoldGroundWindowService.Key(
            ScaffoldDecisionTracker.TargetKey.from(var2), var5, var6, Double.doubleToLongBits(var3)
         );
         if (this.f62htl0zfsbx != null && !this.f62htl0zfsbx.key().equals(var18)) {
            this.reset();
         }

         RotationData var19 = m12ax9eje3hk(var1);
         ScaffoldGroundWindowService.Evaluation var20 = new ScaffoldGroundWindowService.Evaluation(
            var13, var14, Math.max(var8, Double.longBitsToDouble(4585925428558828667L)), var12
         );
         Optional var21 = var20.mfjgv0r0kaw6(var2, var19);
         if (var21.isPresent()) {
            PlacementCandidate var30 = this.f62htl0zfsbx == null ? var2 : this.f62htl0zfsbx.anchor();
            ScaffoldTrajectorySolver.Solution var31 = mapyvygxhzmf(var19, var30, (ScaffoldGroundWindowService.Verified)var21.get(), 0L, 0L);
            this.mq46v5jbl80(var18, var31);
            return new ScaffoldGroundWindowService.Result(
               Optional.of(var31), true, ((ScaffoldGroundWindowService.Verified)var21.get()).margin(), var20.f3q7p60ryomy
            );
         }

         if (this.f62htl0zfsbx != null && (!var15 || (float)this.f62htl0zfsbx.rotation().yaw() == (float)var19.yaw())) {
            long var22 = Math.round(RotationData.yawDelta(var19.yaw(), this.f62htl0zfsbx.rotation().yaw()) / var16);
            long var24 = Math.round((this.f62htl0zfsbx.rotation().pitch() - var19.pitch()) / var16);
            RotationData var26 = ScaffoldNextService.applyMouseCounts(var19, var22, var24, var3);
            Optional var27 = var20.mfjgv0r0kaw6(this.f62htl0zfsbx.anchor(), var26);
            if (var27.isPresent()) {
               ScaffoldTrajectorySolver.Solution var28 = mapyvygxhzmf(
                  var26, this.f62htl0zfsbx.anchor(), (ScaffoldGroundWindowService.Verified)var27.get(), var22, var24
               );
               this.mq46v5jbl80(var18, var28);
               return new ScaffoldGroundWindowService.Result(Optional.of(var28), false, 0.0, var20.f3q7p60ryomy);
            }

            this.f62htl0zfsbx = null;
         }

         Optional var29 = var20.mhxrfro4ps04(var19);
         int var23 = var20.m2d6ts7fmprw() / 4;
         if (!var29.isEmpty() && mf8eckt2jjd(var2, (ScaffoldGroundWindowService.Window)var29.get()) && var23 != 0) {
            Optional<ScaffoldTrajectorySolver.Solution> var32 = var15
               ? this.fdyzeiyu6r1b
                  .solveKeepingYaw(
                     var19,
                     ((ScaffoldGroundWindowService.Window)var29.get()).followingEye(),
                     var2,
                     var3,
                     var20.f7mjawkftc1g,
                     var23,
                     (var1x, var2x) -> var20.mfjgv0r0kaw6(var1x, var2x).map(ScaffoldGroundWindowService.Verified::exactRay)
                  )
               : this.fdyzeiyu6r1b
                  .solve(
                     var19,
                     ((ScaffoldGroundWindowService.Window)var29.get()).followingEye(),
                     var2,
                     var3,
                     var20.f7mjawkftc1g,
                     var10,
                     var11,
                     var23,
                     (var1x, var2x) -> var20.mfjgv0r0kaw6(var1x, var2x).map(ScaffoldGroundWindowService.Verified::exactRay)
                  );
            var32.ifPresent(var2x -> this.mq46v5jbl80(var18, var2x));
            return new ScaffoldGroundWindowService.Result(var32, false, 0.0, var20.f3q7p60ryomy);
         } else {
            return new ScaffoldGroundWindowService.Result(Optional.empty(), false, 0.0, var20.f3q7p60ryomy);
         }
      } else {
         throw new IllegalArgumentException("Invalid click-window search");
      }
   }

   public void reset() {
      this.f62htl0zfsbx = null;
   }

   private void mq46v5jbl80(ScaffoldGroundWindowService.Key var1, ScaffoldTrajectorySolver.Solution var2) {
      this.f62htl0zfsbx = new ScaffoldGroundWindowService.Retained(var1, var2.rotation(), var2.anchor());
   }

   private static ScaffoldTrajectorySolver.Solution mapyvygxhzmf(
      RotationData var0, PlacementCandidate var1, ScaffoldGroundWindowService.Verified var2, long var3, long var5
   ) {
      return new ScaffoldTrajectorySolver.Solution(var0, var1, var2.exactRay(), var3, var5, var2.margin() * Double.longBitsToDouble(4621819117588971520L));
   }

   private static RotationData m12ax9eje3hk(RotationData var0) {
      return new RotationData((float)var0.yaw(), (float)var0.pitch());
   }

   private static boolean mf8eckt2jjd(PlacementCandidate var0, ScaffoldGroundWindowService.Window var1) {
      ScaffoldMode var2 = var0.clickedFace();
      RotationVector var3 = var1.followingEye();
      RotationVector var4 = var0.hitPoint();
      return (var3.x() - var4.x()) * var2.x() + (var3.y() - var4.y()) * var2.y() + (var3.z() - var4.z()) * var2.z()
         > Double.longBitsToDouble(4547007122018943789L);
   }

   private static final class Evaluation {
      private final ScaffoldGroundWindowService.Forecast fd1ktx3fet23;
      private final ScaffoldCorridorService.Probe fexk2x442zuk;
      private final double f7mjawkftc1g;
      private final int fhotgg2mhw6y;
      private final Map<ScaffoldGroundWindowService.RotationKey, Optional<ScaffoldGroundWindowService.Window>> fepdsxgq42yj = new HashMap<>();
      private final Map<ScaffoldGroundWindowService.RayKey, Optional<ScaffoldBlockHit>> fiiyn6nh060u = new HashMap<>();
      private int f3q7p60ryomy;

      private Evaluation(ScaffoldGroundWindowService.Forecast var1, ScaffoldCorridorService.Probe var2, double var3, int var5) {
         this.fd1ktx3fet23 = var1;
         this.fexk2x442zuk = var2;
         this.f7mjawkftc1g = var3;
         this.fhotgg2mhw6y = var5;
      }

      private Optional<ScaffoldGroundWindowService.Window> mhxrfro4ps04(RotationData var1) {
         RotationData var2 = ScaffoldGroundWindowService.m12ax9eje3hk(var1);
         return this.fepdsxgq42yj
            .computeIfAbsent(
               ScaffoldGroundWindowService.RotationKey.m14u13i0lhkl(var2),
               var2x -> Objects.requireNonNull(this.fd1ktx3fet23.forRotation(var2), "Click-window forecast returned null")
            );
      }

      private Optional<ScaffoldGroundWindowService.Verified> mfjgv0r0kaw6(PlacementCandidate var1, RotationData var2) {
         RotationData var3 = ScaffoldGroundWindowService.m12ax9eje3hk(var2);
         Optional var4 = this.mhxrfro4ps04(var3);
         if (!var4.isEmpty() && ScaffoldGroundWindowService.mf8eckt2jjd(var1, (ScaffoldGroundWindowService.Window)var4.get())) {
            ScaffoldGroundWindowService.Window var5 = (ScaffoldGroundWindowService.Window)var4.get();
            ScaffoldCorridorService.Evaluation var6 = ScaffoldCorridorService.evaluate(
               var1,
               var3,
               this.f7mjawkftc1g,
               ScaffoldCorridorService.corridor(var5.previousEye(), var5.previousEye(), var5.followingEye(), 2),
               this::m4n3w9p8p5g9
            );
            return !var6.ready()
               ? Optional.empty()
               : this.m4n3w9p8p5g9(var5.followingEye(), var3)
                  .map(
                     var2x -> new ScaffoldGroundWindowService.Verified(
                        new PlacementCandidate(var1.targetPosition(), var2x.blockPosition(), var2x.face(), var2x.hitPoint(), var2x.inside()),
                        var6.narrowestMargin()
                     )
                  );
         } else {
            return Optional.empty();
         }
      }

      private Optional<ScaffoldBlockHit> m4n3w9p8p5g9(RotationVector var1, RotationData var2) {
         RotationData var3 = ScaffoldGroundWindowService.m12ax9eje3hk(var2);
         ScaffoldGroundWindowService.RayKey var4 = new ScaffoldGroundWindowService.RayKey(ScaffoldGroundWindowService.RotationKey.m14u13i0lhkl(var3), var1);
         return this.fiiyn6nh060u.computeIfAbsent(var4, var3x -> {
            if (this.f3q7p60ryomy >= this.fhotgg2mhw6y) {
               return Optional.empty();
            }

            this.f3q7p60ryomy++;
            return Objects.requireNonNull(this.fexk2x442zuk.raycast(var1, var3), "Native click-window ray returned null");
         });
      }

      private int m2d6ts7fmprw() {
         return this.fhotgg2mhw6y - this.f3q7p60ryomy;
      }
   }

   @FunctionalInterface
   public interface Forecast {
      Optional<ScaffoldGroundWindowService.Window> forRotation(RotationData var1);
   }

   private record Key(ScaffoldDecisionTracker.TargetKey target, int planId, long intentEpoch, long sensitivityBits) {
   }

   private record RayKey(ScaffoldGroundWindowService.RotationKey rotation, RotationVector eye) {
   }

   public record Result(Optional<ScaffoldTrajectorySolver.Solution> solution, boolean currentUsable, double currentMargin, int nativeRays) {
      public Result(Optional<ScaffoldTrajectorySolver.Solution> solution, boolean currentUsable, double currentMargin, int nativeRays) {
         Objects.requireNonNull(solution, "solution");
         if (Double.isFinite(currentMargin)
            && !(currentMargin < 0.0)
            && !(currentMargin > Double.longBitsToDouble(4602678819172646912L))
            && nativeRays >= 0
            && nativeRays <= 512
            && (!currentUsable || !solution.isEmpty())) {
            this.solution = solution;
            this.currentUsable = currentUsable;
            this.currentMargin = currentMargin;
            this.nativeRays = nativeRays;
         } else {
            throw new IllegalArgumentException("Invalid click-window result");
         }
      }
   }

   private record Retained(ScaffoldGroundWindowService.Key key, RotationData rotation, PlacementCandidate anchor) {
   }

   private record RotationKey(int yawBits, int pitchBits) {
      private static ScaffoldGroundWindowService.RotationKey m14u13i0lhkl(RotationData var0) {
         return new ScaffoldGroundWindowService.RotationKey(Float.floatToIntBits((float)var0.yaw()), Float.floatToIntBits((float)var0.pitch()));
      }
   }

   private record Verified(PlacementCandidate exactRay, double margin) {
   }

   public record Window(RotationVector previousEye, RotationVector followingEye) {
      public Window(RotationVector previousEye, RotationVector followingEye) {
         Objects.requireNonNull(previousEye, "previousEye");
         Objects.requireNonNull(followingEye, "followingEye");
         this.previousEye = previousEye;
         this.followingEye = followingEye;
      }
   }
}
