package dev.felix.ellice.ui.theme;

public final class ThemeMixService {
   private ThemeMixService() {
   }

   public static int mix(int value, int currentValue, float nextValue) {
      nextValue = calculateValue6(nextValue);
      float[] floats = createFloat(value);
      float[] currentFloats = createFloat(currentValue);
      float previousValue = calculateValue5(
         (value >>> 24 & 0xFF) / 255.0F, (currentValue >>> 24 & 0xFF) / 255.0F, nextValue
      );
      return calculateValue(
         calculateValue5(floats[0], currentFloats[0], nextValue), calculateValue5(floats[1], currentFloats[1], nextValue), calculateValue5(floats[2], currentFloats[2], nextValue), previousValue
      );
   }

   public static int lighten(int value, float currentValue) {
      float[] floats = createFloat(value);
      floats[0] = calculateValue5(floats[0], 1.0F, calculateValue6(currentValue));
      return calculateValue(floats[0], floats[1], floats[2], calculateValue4(value));
   }

   public static int darken(int value, float currentValue) {
      float[] floats = createFloat(value);
      floats[0] = calculateValue5(floats[0], 0.0F, calculateValue6(currentValue));
      return calculateValue(floats[0], floats[1], floats[2], calculateValue4(value));
   }

   public static int saturate(int value, float currentValue) {
      float[] floats = createFloat(value);
      float nextValue = Math.max(0.0F, currentValue);
      return calculateValue(floats[0], floats[1] * nextValue, floats[2] * nextValue, calculateValue4(value));
   }

   public static int desaturate(int value, float currentValue) {
      return saturate(value, 1.0F - calculateValue6(currentValue));
   }

   public static int withAlpha(int value, float opacity) {
      int currentValue = Math.round(calculateValue6(opacity) * 255.0F);
      return value & 16777215 | currentValue << 24;
   }

   public static int scaleAlpha(int value, float opacity) {
      return withAlpha(value, calculateValue4(value) * Math.max(0.0F, opacity));
   }

   public static int contrastText(int value) {
      float currentValue = calculateValue2((value >>> 16 & 0xFF) / 255.0F);
      float nextValue = calculateValue2((value >>> 8 & 0xFF) / 255.0F);
      float previousValue = calculateValue2((value & 0xFF) / 255.0F);
      float sourceValue = 0.2126F * currentValue
         + 0.7152F * nextValue
         + 0.0722F * previousValue;
      return sourceValue > 0.179F ? -16777216 : -1;
   }

   private static float[] createFloat(int value) {
      float currentValue = calculateValue2((value >>> 16 & 0xFF) / 255.0F);
      float nextValue = calculateValue2((value >>> 8 & 0xFF) / 255.0F);
      float previousValue = calculateValue2((value & 0xFF) / 255.0F);
      float sourceValue = 0.41222146F * currentValue
         + 0.53633255F * nextValue
         + 0.051445995F * previousValue;
      float targetValue = 0.2119035F * currentValue
         + 0.6806995F * nextValue
         + 0.10739696F * previousValue;
      float inputValue = 0.08830246F * currentValue
         + 0.28171885F * nextValue
         + 0.6299787F * previousValue;
      float outputValue = (float)Math.cbrt(sourceValue);
      float resultValue = (float)Math.cbrt(targetValue);
      float candidateValue = (float)Math.cbrt(inputValue);
      return new float[]{
         0.21045426F * outputValue + 0.7936178F * resultValue - 0.00407205F * candidateValue,
         1.9779985F * outputValue - 2.4285922F * resultValue + 0.4505937F * candidateValue,
         0.02590404F * outputValue + 0.78277177F * resultValue - 0.80867577F * candidateValue
      };
   }

   private static int calculateValue(float value, float currentValue, float nextValue, float previousValue) {
      float sourceValue = value + 0.39633778F * currentValue + 0.21580376F * nextValue;
      float targetValue = value - 0.10556135F * currentValue - 0.06385417F * nextValue;
      float inputValue = value - 0.08948418F * currentValue - 1.2914855F * nextValue;
      float outputValue = sourceValue * sourceValue * sourceValue;
      float resultValue = targetValue * targetValue * targetValue;
      float candidateValue = inputValue * inputValue * inputValue;
      float selectedValue = 4.0767417F * outputValue
         - 3.3077116F * resultValue
         + 0.23096994F * candidateValue;
      float defaultValue = -1.268438F * outputValue
         + 2.6097574F * resultValue
         - 0.3413194F * candidateValue;
      float initialValue = -0.00419609F * outputValue
         - 0.7034186F * resultValue
         + 1.7076147F * candidateValue;
      int resolvedValue = Math.round(calculateValue6(previousValue) * 255.0F);
      int computedValue = Math.round(calculateValue6(calculateValue3(selectedValue)) * 255.0F);
      int cachedValue = Math.round(calculateValue6(calculateValue3(defaultValue)) * 255.0F);
      int pendingValue = Math.round(calculateValue6(calculateValue3(initialValue)) * 255.0F);
      return resolvedValue << 24 | computedValue << 16 | cachedValue << 8 | pendingValue;
   }

   private static float calculateValue2(float value) {
      return value <= 0.04045F
         ? value / 12.92F
         : (float)Math.pow(
            (value + 0.055F) / 1.055F, 2.4
         );
   }

   private static float calculateValue3(float value) {
      return value <= 0.0031308F
         ? 12.92F * value
         : 1.055F * (float)Math.pow(value, 0.4166666666666667)
            - 0.055F;
   }

   private static float calculateValue4(int value) {
      return (value >>> 24 & 0xFF) / 255.0F;
   }

   private static float calculateValue5(float value, float currentValue, float nextValue) {
      return value + (currentValue - value) * nextValue;
   }

   private static float calculateValue6(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
