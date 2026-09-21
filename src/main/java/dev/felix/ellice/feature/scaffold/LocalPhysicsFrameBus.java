package dev.felix.ellice.feature.scaffold;

import java.util.Objects;
import java.util.Optional;

public final class LocalPhysicsFrameBus {
   private static volatile LocalPhysicsFrameBus.Frame frame2;
   private static volatile long timestamp = Long.MIN_VALUE;
   private static final ThreadLocal<Boolean> threadLocal = ThreadLocal.withInitial(() -> false);

   private LocalPhysicsFrameBus() {
   }

   public static void publish(LocalPhysicsFrameBus.Frame currentFrame) {
      LocalPhysicsFrameBus.Frame nextFrame = Objects.requireNonNull(currentFrame, "frame");
      if (timestamp != nextFrame.tick()) {
         timestamp = Long.MIN_VALUE;
         frame2 = nextFrame;
      }
   }

   public static boolean publishAppliedInput(long longValue, ScaffoldRemapService.Input currentInput, boolean enabled) {
      Objects.requireNonNull(currentInput, "applied");
      LocalPhysicsFrameBus.Frame frame = frame2;
      if (frame != null && frame.tick() == longValue) {
         ScaffoldRemapService.Input nextInput = frame.input();
         ScaffoldRemapService.Input previousInput = new ScaffoldRemapService.Input(
            nextInput.forward(), nextInput.backward(), nextInput.left(), nextInput.right(), currentInput.jump(), nextInput.sneak(), nextInput.sprint()
         );
         frame2 = frame.withAppliedInput(previousInput, enabled);
         return true;
      } else {
         return false;
      }
   }

   public static Optional<LocalPhysicsFrameBus.Frame> current() {
      return Optional.ofNullable(frame2);
   }

   public static Optional<Float> yaw() {
      if (!threadLocal.get()) {
         return Optional.empty();
      }

      LocalPhysicsFrameBus.Frame frame = frame2;
      return frame == null ? Optional.empty() : Optional.of(frame.yaw());
   }

   public static void beginLocalPhysics() {
      threadLocal.set(frame2 != null);
   }

   public static void endLocalPhysics() {
      threadLocal.set(false);
   }

   public static boolean isLocalPhysicsActive() {
      return threadLocal.get();
   }

   public static boolean abortLocalPhysics() {
      LocalPhysicsFrameBus.Frame frame = frame2;
      if (frame == null) {
         return false;
      }

      timestamp = frame.tick();
      frame2 = null;
      threadLocal.set(false);
      return true;
   }

   public static boolean wasAborted(long longValue) {
      return timestamp == longValue;
   }

   public static void clear() {
      frame2 = null;
      timestamp = Long.MIN_VALUE;
      threadLocal.set(false);
   }

   public record Frame(long tick, float yaw, ScaffoldRemapService.Input input, boolean suppressSprint) {
      public Frame(long tick, float yaw, ScaffoldRemapService.Input input, boolean suppressSprint) {
         Objects.requireNonNull(input, "input");
         if (!Float.isFinite(yaw)) {
            throw new IllegalArgumentException("Physics yaw must be finite");
         }

         this.tick = tick;
         this.yaw = yaw;
         this.input = input;
         this.suppressSprint = suppressSprint;
      }

      public LocalPhysicsFrameBus.Frame withAppliedInput(ScaffoldRemapService.Input input, boolean enabled) {
         return new LocalPhysicsFrameBus.Frame(this.tick, this.yaw, input, enabled);
      }
   }
}
