package dev.felix.ellice.hud.layout;

public final class LayoutResolveXService {
   private LayoutResolveXService() {
   }

   public static float resolveX(float value, float currentValue, LayoutCodec layoutCodec, float nextValue, float previousValue) {
      float sourceValue = value + currentValue * layoutCodec.h.factor;
      return sourceValue + nextValue - previousValue * layoutCodec.h.factor;
   }

   public static float resolveY(float value, float currentValue, LayoutCodec layoutCodec, float nextValue, float previousValue) {
      float sourceValue = value + currentValue * layoutCodec.v.factor;
      return sourceValue + nextValue - previousValue * layoutCodec.v.factor;
   }
}
