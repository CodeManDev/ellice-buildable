package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import java.util.ArrayList;
import java.util.UUID;
import org.joml.Matrix4f;
import org.joml.Vector3d;

public final class TerrainFarForService {
   private static final double value2 = 0.016666666666666666;
   private static final double value3 = 1.0;
   private static final double value4 = 0.5;
   private static final double value5 = 0.5;
   private static final double value6 = 0.3;
   private static final double value7 = 5.5;
   private static final double value8 = 26.0;
   private static final double value9 = 36.0;
   private static final double value10 = 50.0;
   private static final double value11 = Math.toRadians(2.0);
   private static final double value12 = 1.7777777777777777;
   public static final float NEAR = 0.3F;
   private static final double[] double2 = new double[]{
      0.3,
      0.48,
      0.7,
      1.0,
      1.35
   };
   private final TerrainAddService terrainAddService;
   private final UUID uUID;
   private UUID uUID2;
   private double value13 = -1.0;
   private final double value14;
   private double value15;
   private final ArrayList<TerrainFarForService.Pose> items = new ArrayList<>();
   private final TerrainFarForService.Spring spring;
   private final TerrainFarForService.Spring spring2;
   private final TerrainFarForService.Spring spring3;
   private final TerrainFarForService.Spring spring4;
   private final TerrainFarForService.Spring spring5;
   private final TerrainFarForService.Spring spring6;
   private final TerrainFarForService.Spring spring7;

   public static float farFor(double doubleValue) {
      double currentDoubleValue = Double.isFinite(doubleValue)
         ? Math.max(0.5, doubleValue)
         : 20.0;
      return (float)Math.max(800.0, currentDoubleValue * 40.0);
   }

   public TerrainFarForService(TerrainAddService terrainAdd, UUID currentUUID) {
      this.terrainAddService = terrainAdd;
      this.uUID = currentUUID;
      TerrainAddService.Sample sample = terrainAdd.at(0.0);
      this.updateState(sample, 0.0);
      Vector3d vector3d = this.createVector3d(sample);
      Vector3d currentVector3d = this.createVector3d2(sample);
      double doubleValue = calculateValue13(sample, currentVector3d.x, currentVector3d.z, currentVector3d.y);
      if (currentVector3d.y < doubleValue + 0.5) {
         currentVector3d.y = doubleValue + 0.5;
      }

      this.value14 = vector3d == null
         ? -0.6
         : Math.atan2(vector3d.x - sample.player().x, vector3d.z - sample.player().z) + 1.5707963267948966;
      this.spring = new TerrainFarForService.Spring(currentVector3d.x);
      this.spring2 = new TerrainFarForService.Spring(currentVector3d.y);
      this.spring3 = new TerrainFarForService.Spring(currentVector3d.z);
      double currentDoubleValue = calculateValue6(0.0);
      this.spring6 = new TerrainFarForService.Spring(currentDoubleValue);
      this.spring7 = new TerrainFarForService.Spring(0.0);
      this.spring4 = new TerrainFarForService.Spring(this.calculateValue3(sample, currentVector3d, currentDoubleValue, 0.0));
      this.spring5 = new TerrainFarForService.Spring(
         calculateValue12(sample, currentVector3d, this.value14, this.spring4.value, currentDoubleValue) + calculateValue9(0.0)
      );
      this.value15 = 0.0;
      this.items.add(this.createPose(sample, currentVector3d, 0.0));
   }

   private void updateState(TerrainAddService.Sample sample, double doubleValue) {
      Vector3d vector3d = this.createVector3d(sample);
      if (vector3d != null && vector3d.distanceSquared(sample.player()) < 576.0) {
         this.value13 = -1.0;
      } else {
         if (this.uUID2 != null) {
            if (this.value13 < 0.0) {
               this.value13 = doubleValue;
            }

            if (doubleValue - this.value13 < 1.0) {
               return;
            }
         }

         double currentDoubleValue = 324.0;
         UUID currentUUID = null;

         for (CompatLoadedHandler.MapPlayer mapPlayer : sample.players()) {
            if (!mapPlayer.id().equals(this.uUID)) {
               double currentX = sample.player().distanceSquared(mapPlayer.x(), mapPlayer.y(), mapPlayer.z());
               if (currentX < currentDoubleValue) {
                  currentDoubleValue = currentX;
                  currentUUID = mapPlayer.id();
               }
            }
         }

         this.uUID2 = currentUUID;
         this.value13 = -1.0;
      }
   }

   private Vector3d createVector3d(TerrainAddService.Sample sample) {
      for (CompatLoadedHandler.MapPlayer mapPlayer : sample.players()) {
         if (mapPlayer.id().equals(this.uUID2)) {
            return new Vector3d(mapPlayer.x(), mapPlayer.y(), mapPlayer.z());
         }
      }

      return null;
   }

   private Vector3d createVector3d2(TerrainAddService.Sample sample) {
      Vector3d vector3d = new Vector3d(sample.player());
      Vector3d currentVector3d = this.createVector3d(sample);
      if (currentVector3d != null && currentVector3d.distanceSquared(sample.player()) < 576.0) {
         vector3d.lerp(currentVector3d, 0.5);
      }

      return vector3d.add(0.0, 1.1, 0.0);
   }

   private double calculateValue(TerrainAddService.Sample sample, Vector3d vector3d) {
      double doubleValue = vector3d.distance(new Vector3d(sample.player()).add(0.0, 1.0, 0.0)) + 2.0;
      Vector3d currentVector3d = this.createVector3d(sample);
      if (currentVector3d != null && currentVector3d.distanceSquared(sample.player()) < 576.0) {
         doubleValue = Math.max(doubleValue, vector3d.distance(currentVector3d.add(0.0, 1.0, 0.0)) + 2.0);
      }

      return doubleValue;
   }

   private static double calculateValue2(double doubleValue, double currentDoubleValue) {
      double nextDoubleValue = Math.toRadians(currentDoubleValue) / 2.0 * 0.9;
      double previousDoubleValue = Math.max(0.2, Math.sin(nextDoubleValue));
      return doubleValue / previousDoubleValue * 1.05;
   }

   private double calculateValue3(TerrainAddService.Sample sample, Vector3d vector3d, double doubleValue, double currentDoubleValue) {
      double nextDoubleValue = calculateValue2(this.calculateValue(sample, vector3d), doubleValue);
      double previousDoubleValue = calculateValue7(currentDoubleValue);
      double sourceDoubleValue = nextDoubleValue * previousDoubleValue;
      return Math.clamp(sourceDoubleValue, 5.5, 26.0);
   }

   private static double calculateValue4(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = Math.clamp((nextDoubleValue - doubleValue) / (currentDoubleValue - doubleValue), 0.0, 1.0);
      return previousDoubleValue * previousDoubleValue * (3.0 - 2.0 * previousDoubleValue);
   }

   private static double calculateValue5(double doubleValue) {
      double currentDoubleValue = calculateValue4(3.5, 4.5, doubleValue);
      double nextDoubleValue = calculateValue4(13.5, 14.5, doubleValue);
      double previousDoubleValue = calculateValue4(17.5, 18.5, doubleValue);
      double sourceDoubleValue = 0.026 * (1.0 - currentDoubleValue) + 0.06 * currentDoubleValue;
      sourceDoubleValue = sourceDoubleValue * (1.0 - nextDoubleValue) + 0.07 * nextDoubleValue;
      return sourceDoubleValue * (1.0 - previousDoubleValue) + 0.03 * previousDoubleValue;
   }

   private static double calculateValue6(double doubleValue) {
      double currentDoubleValue = calculateValue4(3.5, 4.5, doubleValue);
      double nextDoubleValue = calculateValue4(13.5, 14.5, doubleValue);
      double previousDoubleValue = calculateValue4(17.5, 18.5, doubleValue);
      double sourceDoubleValue = 48.0 * (1.0 - currentDoubleValue) + 46.0 * currentDoubleValue;
      sourceDoubleValue = sourceDoubleValue * (1.0 - nextDoubleValue) + 38.0 * nextDoubleValue;
      sourceDoubleValue = sourceDoubleValue * (1.0 - previousDoubleValue) + 44.0 * previousDoubleValue;
      return Math.clamp(sourceDoubleValue, 36.0, 50.0);
   }

   private static double calculateValue7(double doubleValue) {
      double currentDoubleValue = calculateValue4(3.5, 4.5, doubleValue);
      double nextDoubleValue = calculateValue4(13.5, 14.5, doubleValue);
      double previousDoubleValue = calculateValue4(17.5, 18.5, doubleValue);
      double sourceDoubleValue = 1.15 * (1.0 - currentDoubleValue) + 1.0 * currentDoubleValue;
      sourceDoubleValue = sourceDoubleValue * (1.0 - nextDoubleValue) + 0.78 * nextDoubleValue;
      return sourceDoubleValue * (1.0 - previousDoubleValue) + 0.92 * previousDoubleValue;
   }

   private static double calculateValue8(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = 1.0 - doubleValue;
      double sourceDoubleValue = doubleValue * (1.0 - currentDoubleValue);
      double targetDoubleValue = currentDoubleValue * (1.0 - nextDoubleValue);
      double inputDoubleValue = nextDoubleValue;
      return 0.12 * previousDoubleValue
         + 0.0 * sourceDoubleValue
         - 0.05 * targetDoubleValue
         + 0.08 * inputDoubleValue;
   }

   private static double calculateValue9(double doubleValue) {
      return calculateValue8(
         calculateValue4(3.5, 4.5, doubleValue),
         calculateValue4(13.5, 14.5, doubleValue),
         calculateValue4(17.5, 18.5, doubleValue)
      );
   }

   private static Vector3d createVector3d3(double doubleValue, double currentDoubleValue) {
      return new Vector3d(Math.sin(doubleValue) * Math.cos(currentDoubleValue), Math.sin(currentDoubleValue), Math.cos(doubleValue) * Math.cos(currentDoubleValue));
   }

   private static double calculateValue10(TerrainAddService.Sample sample, Vector3d vector3d, Vector3d currentVector3d, double doubleValue) {
      Vector3d nextVector3d = TerrainPickService.pick(sample.terrain().values(), new TerrainViewportService.Ray(vector3d, currentVector3d), doubleValue + 1.0);
      return nextVector3d == null ? doubleValue : Math.max(0.5, vector3d.distance(nextVector3d) - 1.0);
   }

   private static double calculateValue11(TerrainAddService.Sample sample, Vector3d vector3d, double doubleValue, double currentDoubleValue, double nextDoubleValue, double previousDoubleValue) {
      double sourceDoubleValue = Math.toRadians(previousDoubleValue) / 2.0;
      double targetDoubleValue = Math.atan(Math.tan(sourceDoubleValue) * 1.7777777777777777);
      double inputDoubleValue = targetDoubleValue * 0.7;
      double outputDoubleValue = sourceDoubleValue * 0.7;
      double resultDoubleValue = calculateValue10(sample, vector3d, createVector3d3(doubleValue, currentDoubleValue), nextDoubleValue);
      resultDoubleValue = Math.min(resultDoubleValue, calculateValue10(sample, vector3d, createVector3d3(doubleValue + inputDoubleValue, currentDoubleValue), nextDoubleValue));
      resultDoubleValue = Math.min(resultDoubleValue, calculateValue10(sample, vector3d, createVector3d3(doubleValue - inputDoubleValue, currentDoubleValue), nextDoubleValue));
      resultDoubleValue = Math.min(resultDoubleValue, calculateValue10(sample, vector3d, createVector3d3(doubleValue, currentDoubleValue + outputDoubleValue), nextDoubleValue));
      return Math.min(
         resultDoubleValue, calculateValue10(sample, vector3d, createVector3d3(doubleValue, Math.max(0.05, currentDoubleValue - outputDoubleValue)), nextDoubleValue)
      );
   }

   private static double calculateValue12(TerrainAddService.Sample sample, Vector3d vector3d, double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      for (double previousDoubleValue : double2) {
         if (calculateValue11(sample, vector3d, doubleValue, previousDoubleValue, currentDoubleValue, nextDoubleValue) >= currentDoubleValue) {
            return previousDoubleValue;
         }
      }

      return 1.35;
   }

   private static double calculateValue13(TerrainAddService.Sample sample, double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      Vector3d vector3d = new Vector3d(doubleValue, nextDoubleValue + 10.0, currentDoubleValue);
      Vector3d currentVector3d = TerrainPickService.pick(
         sample.terrain().values(),
         new TerrainViewportService.Ray(vector3d, new Vector3d(0.0, -1.0, 0.0)),
         20.0
      );
      return currentVector3d == null ? Double.NEGATIVE_INFINITY : currentVector3d.y;
   }

   private static boolean checkCondition(TerrainAddService.Sample sample, Vector3d vector3d, Vector3d currentVector3d) {
      Vector3d nextVector3d = new Vector3d(currentVector3d).sub(vector3d);
      double currentLength = nextVector3d.length();
      if (currentLength < 1.0E-6) {
         return false;
      }

      Vector3d previousVector3d = nextVector3d.div(currentLength);
      Vector3d sourceVector3d = TerrainPickService.pick(sample.terrain().values(), new TerrainViewportService.Ray(vector3d, previousVector3d), currentLength);
      return sourceVector3d != null && vector3d.distance(sourceVector3d) > 1.0 && vector3d.distance(sourceVector3d) < currentLength - 0.3;
   }

   private boolean checkCondition2(TerrainAddService.Sample sample, Vector3d vector3d) {
      Vector3d currentVector3d = new Vector3d(sample.player()).add(0.0, 1.6, 0.0);
      if (checkCondition(sample, vector3d, currentVector3d)) {
         return true;
      }

      Vector3d nextVector3d = this.createVector3d(sample);
      return nextVector3d != null && nextVector3d.distanceSquared(sample.player()) < 576.0
         ? checkCondition(sample, vector3d, new Vector3d(nextVector3d).add(0.0, 1.6, 0.0))
         : false;
   }

   private TerrainFarForService.Pose createPose(TerrainAddService.Sample sample, Vector3d vector3d, double doubleValue) {
      double currentDoubleValue = this.value14 + this.value15;
      Vector3d currentVector3d = createVector3d3(currentDoubleValue, this.spring5.value);
      double nextDoubleValue = calculateValue11(sample, vector3d, currentDoubleValue, this.spring5.value, this.spring4.value, this.spring6.value);
      Vector3d nextVector3d = new Vector3d(currentVector3d).mul(Math.min(this.spring4.value, nextDoubleValue)).add(vector3d);
      double previousDoubleValue = calculateValue13(sample, nextVector3d.x, nextVector3d.z, nextVector3d.y);
      if (nextVector3d.y < previousDoubleValue + 0.3) {
         nextVector3d.y = previousDoubleValue + 0.3;
      }

      return new TerrainFarForService.Pose(vector3d, nextVector3d, this.spring6.value, this.spring7.value);
   }

   private void updateState2(int currentValue) {
      double doubleValue = currentValue * 0.016666666666666666;
      TerrainAddService.Sample sample = this.terrainAddService.at(doubleValue);
      this.updateState(sample, doubleValue);
      Vector3d vector3d = this.createVector3d2(sample);
      this.spring.advance(vector3d.x, 8.0);
      this.spring2.advance(vector3d.y, 7.0);
      this.spring3.advance(vector3d.z, 8.0);
      Vector3d currentVector3d = new Vector3d(this.spring.value, this.spring2.value, this.spring3.value);
      double currentDoubleValue = calculateValue13(sample, currentVector3d.x, currentVector3d.z, currentVector3d.y);
      if (currentVector3d.y < currentDoubleValue + 0.5) {
         currentVector3d.y = currentDoubleValue + 0.5;
      }

      double nextDoubleValue = calculateValue5(doubleValue);
      this.value15 = this.value15 + nextDoubleValue * 0.016666666666666666;
      double previousDoubleValue = this.value14 + this.value15;
      double sourceDoubleValue = calculateValue6(doubleValue);
      double targetDoubleValue = this.calculateValue(sample, currentVector3d);
      double inputDoubleValue = calculateValue7(doubleValue);
      double outputDoubleValue = calculateValue2(targetDoubleValue, sourceDoubleValue) * inputDoubleValue;
      if (outputDoubleValue > 26.0 && targetDoubleValue > 1.0E-9) {
         double resultDoubleValue = Math.clamp(
            targetDoubleValue * 1.05 * inputDoubleValue / 26.0,
            0.2,
            1.0
         );
         sourceDoubleValue = Math.clamp(
            Math.toDegrees(2.0 * Math.asin(resultDoubleValue) / 0.9),
            36.0,
            50.0
         );
      }

      this.spring6.advance(sourceDoubleValue, 3.0);
      double candidateDoubleValue = Math.clamp(-nextDoubleValue * 0.3, -value11, value11);
      this.spring7.advance(candidateDoubleValue, 5.0);
      double selectedDoubleValue = Math.clamp(
         calculateValue2(targetDoubleValue, this.spring6.value) * inputDoubleValue,
         5.5,
         26.0
      );
      double defaultDoubleValue = this.spring4.value;
      this.spring4.advance(selectedDoubleValue, 4.0);
      double initialDoubleValue = this.spring4.value - defaultDoubleValue;
      double resolvedDoubleValue = 0.2;
      double computedDoubleValue = 0.06666666666666667;
      if (initialDoubleValue < -resolvedDoubleValue) {
         this.spring4.value = defaultDoubleValue - resolvedDoubleValue;
         this.spring4.velocity = 0.0;
      } else if (initialDoubleValue > computedDoubleValue) {
         this.spring4.value = defaultDoubleValue + computedDoubleValue;
         this.spring4.velocity = 0.0;
      }

      double cachedDoubleValue = calculateValue8(
         calculateValue4(3.5, 4.5, doubleValue),
         calculateValue4(13.5, 14.5, doubleValue),
         calculateValue4(17.5, 18.5, doubleValue)
      );
      double pendingDoubleValue = calculateValue12(sample, currentVector3d, previousDoubleValue, this.spring4.value, this.spring6.value) + cachedDoubleValue;
      TerrainAddService.Sample currentSample = this.terrainAddService
         .at(Math.min(this.terrainAddService.duration(), doubleValue + 1.2));
      double activeDoubleValue = previousDoubleValue
         + calculateValue5(doubleValue + 0.6) * 1.2;
      pendingDoubleValue = Math.max(
         pendingDoubleValue,
         calculateValue12(currentSample, this.createVector3d2(currentSample), activeDoubleValue, this.spring4.value, this.spring6.value)
            + calculateValue8(
               calculateValue4(
                  3.5,
                  4.5,
                  doubleValue + 1.2
               ),
               calculateValue4(
                  13.5,
                  14.5,
                  doubleValue + 1.2
               ),
               calculateValue4(
                  17.5,
                  18.5,
                  doubleValue + 1.2
               )
            )
      );
      if (this.checkCondition2(sample, currentVector3d)) {
         pendingDoubleValue = Math.min(1.35, pendingDoubleValue + 0.1);
      }

      double fallbackDoubleValue = calculateValue13(
         sample,
         currentVector3d.x + Math.sin(previousDoubleValue) * Math.cos(pendingDoubleValue) * this.spring4.value,
         currentVector3d.z + Math.cos(previousDoubleValue) * Math.cos(pendingDoubleValue) * this.spring4.value,
         currentVector3d.y + Math.sin(pendingDoubleValue) * this.spring4.value
      );
      if (fallbackDoubleValue + 0.3 > currentVector3d.y + Math.sin(pendingDoubleValue) * this.spring4.value) {
         pendingDoubleValue = Math.min(1.35, pendingDoubleValue + 0.08);
      }

      pendingDoubleValue = Math.clamp(pendingDoubleValue, 0.05, 1.35);
      this.spring5
         .advance(
            pendingDoubleValue,
            pendingDoubleValue > this.spring5.value ? 8.0 : 3.0
         );
      this.items.add(this.createPose(sample, currentVector3d, doubleValue));
   }

   public TerrainViewportService.Frame frame(double doubleValue, int value, int currentValue) {
      if (Double.isFinite(doubleValue) && value > 0 && currentValue > 0) {
         double currentDoubleValue = Math.clamp(doubleValue, 0.0, this.terrainAddService.duration()) / 0.016666666666666666;
         int nextValue = (int)Math.floor(currentDoubleValue);
         int previousValue = (int)Math.ceil(currentDoubleValue);

         while (this.items.size() <= previousValue) {
            this.updateState2(this.items.size());
         }

         TerrainFarForService.Pose pose = this.items.get(nextValue);
         TerrainFarForService.Pose currentPose = this.items.get(previousValue);
         double nextDoubleValue = currentDoubleValue - nextValue;
         Vector3d vector3d = new Vector3d(pose.focus).lerp(currentPose.focus, nextDoubleValue);
         Vector3d currentVector3d = new Vector3d(pose.eye).lerp(currentPose.eye, nextDoubleValue);
         if (Double.isFinite(doubleValue)) {
            double previousDoubleValue = Math.max(0.5, currentVector3d.distance(vector3d))
               * 0.0035;
            Vector3d nextVector3d = new Vector3d(vector3d).sub(currentVector3d);
            if (nextVector3d.lengthSquared() > 1.0E-12) {
               nextVector3d.normalize();
               Vector3d previousVector3d = new Vector3d(nextVector3d).cross(0.0, 1.0, 0.0);
               if (previousVector3d.lengthSquared() > 1.0E-8) {
                  previousVector3d.normalize();
                  Vector3d sourceVector3d = new Vector3d(previousVector3d).cross(nextVector3d).normalize();
                  currentVector3d.fma(Math.sin(doubleValue * 0.9) * previousDoubleValue, previousVector3d)
                     .fma(
                        Math.sin(doubleValue * 1.27 + 1.3)
                           * previousDoubleValue
                           * 0.7,
                        sourceVector3d
                     );
               }
            }
         }

         double sourceDoubleValue = pose.fovDeg + (currentPose.fovDeg - pose.fovDeg) * nextDoubleValue;
         double targetDoubleValue = pose.roll + (currentPose.roll - pose.roll) * nextDoubleValue;
         Vector3d targetVector3d = new Vector3d(currentVector3d).sub(vector3d);
         Matrix4f matrix4f = new Matrix4f().lookAt((float)targetVector3d.x, (float)targetVector3d.y, (float)targetVector3d.z, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
         if (Math.abs(targetDoubleValue) > 1.0E-6) {
            matrix4f.rotateZ((float)targetDoubleValue);
         }

         double inputDoubleValue = Math.max(0.5, currentVector3d.distance(vector3d));
         float sourceValue = farFor(inputDoubleValue);
         Matrix4f currentMatrix4f = new Matrix4f()
            .perspective((float)Math.toRadians(sourceDoubleValue), (float)value / currentValue, 0.3F, sourceValue);
         Matrix4f nextMatrix4f = new Matrix4f(currentMatrix4f).mul(matrix4f);
         return new TerrainViewportService.Frame(vector3d, currentVector3d, nextMatrix4f, new Matrix4f(nextMatrix4f).invert(), value, currentValue, inputDoubleValue, matrix4f, currentMatrix4f);
      } else {
         throw new IllegalArgumentException("Invalid camera frame");
      }
   }

   private record Pose(Vector3d focus, Vector3d eye, double fovDeg, double roll) {
   }

   private static final class Spring {
      double value;
      double velocity;

      Spring(double doubleValue) {
         this.value = doubleValue;
      }

      void advance(double doubleValue, double currentDoubleValue) {
         double nextDoubleValue = this.value - doubleValue;
         double previousDoubleValue = this.velocity + currentDoubleValue * nextDoubleValue;
         double sourceDoubleValue = Math.exp(-currentDoubleValue * 0.016666666666666666);
         this.value = doubleValue + (nextDoubleValue + previousDoubleValue * 0.016666666666666666) * sourceDoubleValue;
         this.velocity = (this.velocity - currentDoubleValue * previousDoubleValue * 0.016666666666666666) * sourceDoubleValue;
      }
   }
}
