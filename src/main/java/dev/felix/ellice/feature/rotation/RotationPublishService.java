package dev.felix.ellice.feature.rotation;

import java.util.Optional;

public final class RotationPublishService {
   private static final double value = 0.3;
   private static final double value2 = 50.0;
   private static volatile RotationPublishService.Snapshot snapshot2;

   private RotationPublishService() {
   }

   public static void publish(RotationData rotationData, RotationData currentRotationData) {
      RotationPublishService.Snapshot snapshot = snapshot2;
      RotationData nextRotationData = snapshot == null ? rotationData : snapshot.current();
      double doubleValue = snapshot == null ? rotationData.yaw() : snapshot.bodyYaw();
      double currentDoubleValue = RotationData.yawDelta(doubleValue, currentRotationData.yaw());
      double nextDoubleValue = doubleValue + currentDoubleValue * 0.3;
      double previousDoubleValue = RotationData.yawDelta(nextDoubleValue, currentRotationData.yaw());
      if (Math.abs(previousDoubleValue) > 50.0) {
         nextDoubleValue += previousDoubleValue - Math.copySign(50.0, previousDoubleValue);
      }

      snapshot2 = new RotationPublishService.Snapshot(nextRotationData, currentRotationData, doubleValue, nextDoubleValue);
   }

   public static Optional<RotationPublishService.Snapshot> snapshot() {
      return Optional.ofNullable(snapshot2);
   }

   public static void publishReturning(RotationData rotationData, RotationData currentRotationData, double doubleValue) {
      RotationPublishService.Snapshot snapshot = snapshot2;
      RotationData nextRotationData = snapshot == null ? rotationData : snapshot.current();
      double currentDoubleValue = snapshot == null ? doubleValue : snapshot.bodyYaw();
      double nextDoubleValue = currentDoubleValue + RotationData.yawDelta(currentDoubleValue, doubleValue) * 0.45;
      double previousDoubleValue = RotationData.yawDelta(nextDoubleValue, currentRotationData.yaw());
      double sourceDoubleValue = Math.abs(RotationData.yawDelta(doubleValue, rotationData.yaw()));
      double targetDoubleValue = Math.max(50.0, sourceDoubleValue);
      if (Math.abs(previousDoubleValue) > targetDoubleValue) {
         nextDoubleValue += previousDoubleValue - Math.copySign(targetDoubleValue, previousDoubleValue);
      }

      snapshot2 = new RotationPublishService.Snapshot(nextRotationData, currentRotationData, currentDoubleValue, nextDoubleValue);
   }

   public static Optional<RotationData> current() {
      RotationPublishService.Snapshot snapshot = snapshot2;
      return snapshot == null ? Optional.empty() : Optional.of(snapshot.current());
   }

   public static void clear() {
      snapshot2 = null;
   }

   public record Snapshot(RotationData previous, RotationData current, double previousBodyYaw, double bodyYaw) {
   }
}
