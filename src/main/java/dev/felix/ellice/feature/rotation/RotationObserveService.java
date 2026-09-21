package dev.felix.ellice.feature.rotation;

import dev.felix.ellice.feature.combat.CombatOwnsService;
import java.util.Optional;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.util.Mth;
import org.slf4j.LoggerFactory;

public final class RotationObserveService {
   private static volatile RotationObserveService.Snapshot snapshot2;
   private static RotationData rotationData;
   private static RotationData rotationData2;
   private static RotationData rotationData3;
   private static RotationVector rotationData7;
   private static RotationVector rotationData72;
   private static RotationVector rotationData73;
   private static boolean enabled;
   private static boolean enabled2;
   private static long timestamp;
   private static boolean enabled3;
   private static boolean enabled4;

   private RotationObserveService() {
   }

   public static synchronized void observe(ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
      RotationVector rotationVector = new RotationVector(0.0, 0.0, 0.0);
      observe(serverboundMovePlayerPacket, rotationVector, rotationVector, new RotationData(0.0, 0.0));
   }

   public static synchronized void observe(ServerboundMovePlayerPacket serverboundMovePlayerPacket, RotationVector rotationVector, RotationVector currentRotationVector, RotationData currentRotationData) {
      if (serverboundMovePlayerPacket != null && rotationVector != null && currentRotationVector != null && currentRotationData != null) {
         timestamp++;
         RotationVector nextRotationVector = rotationData7;
         if (serverboundMovePlayerPacket.hasRotation()) {
            if (rotationData2 == null) {
               rotationData2 = rotationData;
            }

            rotationData = new RotationData(serverboundMovePlayerPacket.getYRot(0.0F), serverboundMovePlayerPacket.getXRot(0.0F));
         }

         if (serverboundMovePlayerPacket.hasPosition()) {
            rotationData7 = new RotationVector(serverboundMovePlayerPacket.getX(0.0), serverboundMovePlayerPacket.getY(0.0), serverboundMovePlayerPacket.getZ(0.0));
         }

         rotationData73 = nextRotationVector != null && rotationData7 != null ? rotationData7.subtract(nextRotationVector) : null;
         enabled = true;
         enabled2 = serverboundMovePlayerPacket.isOnGround();
         rotationData72 = currentRotationVector.subtract(rotationVector);
         enabled3 = serverboundMovePlayerPacket.hasPosition();
         enabled4 = serverboundMovePlayerPacket.hasRotation();
         updateState2();
         updateState("movement");
      } else {
         throw new IllegalArgumentException("Movement packet and actor fallback state are required");
      }
   }

   public static synchronized void observe(ServerboundUseItemPacket serverboundUseItemPacket) {
      if (serverboundUseItemPacket == null) {
         throw new IllegalArgumentException("Item-use packet is required");
      }

      float value = serverboundUseItemPacket.getYRot();
      float currentValue = serverboundUseItemPacket.getXRot();
      if (Float.isFinite(value) && Float.isFinite(currentValue)) {
         rotationData = new RotationData(
            Mth.wrapDegrees(value), Mth.clamp(Mth.wrapDegrees(currentValue), -90.0F, 90.0F)
         );
         updateState2();
         updateState("use-item");
      }
   }

   private static void updateState(String text) {
      if (Boolean.getBoolean("ellice.combat.trace")) {
         LoggerFactory.getLogger("CombatTrace")
            .info(
               "wire nanos={} kind={} owner={} snapshot={}",
               new Object[]{System.nanoTime(), text, CombatOwnsService.currentOwner().orElse(null), snapshot2}
            );
      }
   }

   private static void updateState2() {
      if (rotationData != null && rotationData7 != null && rotationData72 != null) {
         snapshot2 = new RotationObserveService.Snapshot(
            timestamp, rotationData, rotationData7, rotationData7.add(rotationData72), enabled3, enabled4
         );
      } else {
         snapshot2 = null;
      }
   }

   public static synchronized Optional<RotationData> current() {
      RotationObserveService.Snapshot snapshot = snapshot2;
      return snapshot == null ? Optional.ofNullable(rotationData) : Optional.of(snapshot.rotation());
   }

   public static synchronized Optional<RotationObserveService.Snapshot> snapshot() {
      return Optional.ofNullable(snapshot2);
   }

   public static synchronized Optional<RotationVector> inheritedVelocity() {
      return Optional.ofNullable(rotationData73).map(item -> new RotationVector(item.x(), enabled2 ? 0.0 : item.y(), item.z()));
   }

   public static synchronized Optional<RotationData> previousTickRotation() {
      return Optional.ofNullable(rotationData3);
   }

   public static synchronized void endTick() {
      rotationData3 = rotationData2;
      rotationData2 = rotationData;
      if (!enabled) {
         rotationData73 = new RotationVector(0.0, 0.0, 0.0);
      }

      enabled = false;
   }

   public static synchronized void invalidateVelocity() {
      rotationData73 = null;
      enabled = true;
   }

   public static synchronized void clear() {
      snapshot2 = null;
      rotationData = null;
      rotationData3 = null;
      rotationData2 = null;
      rotationData7 = null;
      rotationData72 = null;
      rotationData73 = null;
      enabled2 = false;
      enabled = false;
      timestamp = 0L;
      enabled4 = false;
      enabled3 = false;
   }

   public record Snapshot(
      long ordinal,
      RotationData rotation,
      RotationVector wireFeetPosition,
      RotationVector wireEyePosition,
      boolean carriedPosition,
      boolean carriedRotation
   ) {
      public Snapshot(
         long ordinal,
         RotationData rotation,
         RotationVector wireFeetPosition,
         RotationVector wireEyePosition,
         boolean carriedPosition,
         boolean carriedRotation
      ) {
         if (ordinal >= 1L && rotation != null && wireFeetPosition != null && wireEyePosition != null) {
            this.ordinal = ordinal;
            this.rotation = rotation;
            this.wireFeetPosition = wireFeetPosition;
            this.wireEyePosition = wireEyePosition;
            this.carriedPosition = carriedPosition;
            this.carriedRotation = carriedRotation;
         } else {
            throw new IllegalArgumentException("Invalid movement wire snapshot");
         }
      }
   }
}

