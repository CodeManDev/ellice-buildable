package dev.felix.ellice.feature.movement;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class MovementBurstScheduleService {
   public static final double BURST_STEP = 8.0;
   public static final double SNAP_STEP = 6.0;
   public static final int SNAP_CADENCE = 20;
   public static final int SNAP_PAUSE = 60;
   public static final int SNAP_RANGE = 48;
   public static final double OOB_TRIGGER = 30.0;
   public static final double Y_PER_CLAIM = 0.28;
   public static final double MAX_CLIMB_BURST = 1.4;
   public static final double MAX_CLIMB_TRIGGER = 2.24;

   private MovementBurstScheduleService() {
   }

   public static List<MovementBurstScheduleService.SchedClaim> burstSchedule(Vec3 vec3, Vec3 currentVec3, double[] doubles, int value) {
      double doubleValue = currentVec3.x - vec3.x;
      double currentDoubleValue = currentVec3.z - vec3.z;
      double nextDoubleValue = currentVec3.y - vec3.y;
      double previousDoubleValue = Math.sqrt(doubleValue * doubleValue + currentDoubleValue * currentDoubleValue);
      double sourceDoubleValue;
      double targetDoubleValue;
      double inputDoubleValue;
      if (previousDoubleValue < 0.5) {
         double outputDoubleValue = Math.signum(nextDoubleValue) * Math.min(Math.abs(nextDoubleValue), 1.4);
         if (Math.abs(outputDoubleValue) < 1.0E-9) {
            return List.of();
         }

         sourceDoubleValue = doubles[0];
         targetDoubleValue = doubles[1];
         inputDoubleValue = 2.0;
         nextDoubleValue = outputDoubleValue;
      } else {
         sourceDoubleValue = doubleValue / previousDoubleValue;
         targetDoubleValue = currentDoubleValue / previousDoubleValue;
         inputDoubleValue = previousDoubleValue;
         nextDoubleValue = Math.signum(nextDoubleValue) * Math.min(Math.abs(nextDoubleValue), 1.4);
      }

      int currentValue = Math.max(1, (int)Math.ceil(inputDoubleValue / 8.0));
      currentValue = Math.max(currentValue, (int)Math.ceil(Math.abs(nextDoubleValue) / 0.28));
      double resultDoubleValue = inputDoubleValue / currentValue;
      double candidateDoubleValue = nextDoubleValue / currentValue;
      ArrayList arrayList = new ArrayList(currentValue);
      double selectedDoubleValue = vec3.x;
      double defaultDoubleValue = vec3.y;
      double initialDoubleValue = vec3.z;

      for (int index = 0; index < currentValue; index++) {
         selectedDoubleValue += sourceDoubleValue * resultDoubleValue;
         defaultDoubleValue += candidateDoubleValue;
         initialDoubleValue += targetDoubleValue * resultDoubleValue;
         arrayList.add(new MovementBurstScheduleService.SchedClaim(index / value, selectedDoubleValue, defaultDoubleValue, initialDoubleValue));
      }

      return arrayList;
   }

   public static List<MovementBurstScheduleService.SchedClaim> triggerSchedule(Vec3 vec3, Vec3 currentVec3, int value) {
      ArrayList arrayList = new ArrayList();
      double doubleValue = currentVec3.x - vec3.x;
      double currentDoubleValue = currentVec3.z - vec3.z;
      double nextDoubleValue = currentVec3.y - vec3.y;
      double previousDoubleValue = Math.sqrt(doubleValue * doubleValue + currentDoubleValue * currentDoubleValue);
      if (previousDoubleValue < 1.0E-9) {
         return arrayList;
      }

      double sourceDoubleValue = doubleValue / previousDoubleValue;
      double targetDoubleValue = currentDoubleValue / previousDoubleValue;
      double inputDoubleValue = Math.signum(nextDoubleValue) * Math.min(Math.abs(nextDoubleValue), 2.24);
      double outputDoubleValue = vec3.x;
      double resultDoubleValue = vec3.y;
      double candidateDoubleValue = vec3.z;
      double selectedDoubleValue = previousDoubleValue;
      byte byteValue = 0;

      for (int index = 0; index < value && selectedDoubleValue > 1.0; index++) {
         if (index > 0) {
            byteValue += 60;
         }

         int currentValue = Math.min(
            8,
            (int)Math.ceil(Math.min(selectedDoubleValue, 48.0) / 6.0)
         );
         currentValue = Math.max(1, currentValue);

         for (int currentIndex = 0; currentIndex < currentValue && selectedDoubleValue > 1.0E-9; currentIndex++) {
            double defaultDoubleValue = Math.min(6.0, selectedDoubleValue);
            double initialDoubleValue = Math.signum(inputDoubleValue) * Math.min(0.28, Math.abs(inputDoubleValue));
            if (Math.abs(inputDoubleValue) < 1.0E-9) {
               initialDoubleValue = 0.0;
            }

            outputDoubleValue += sourceDoubleValue * defaultDoubleValue;
            resultDoubleValue += initialDoubleValue;
            candidateDoubleValue += targetDoubleValue * defaultDoubleValue;
            selectedDoubleValue -= defaultDoubleValue;
            inputDoubleValue -= initialDoubleValue;
            arrayList.add(new MovementBurstScheduleService.SchedClaim(byteValue, outputDoubleValue, resultDoubleValue, candidateDoubleValue));
            byteValue += 20;
         }

         outputDoubleValue += sourceDoubleValue * 30.0;
         arrayList.add(new MovementBurstScheduleService.SchedClaim(byteValue, outputDoubleValue, resultDoubleValue, candidateDoubleValue));
         byteValue += 20;
      }

      return arrayList;
   }

   public record SchedClaim(int tick, double x, double y, double z) {
   }
}
