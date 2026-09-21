package dev.felix.ellice.ui.scene.color;

public final class ColorPickerMath {
   public static final float OUTER = 0.47F;
   public static final float INNER = 0.39F;
   public static final float TRIANGLE = 0.35F;
   private static final double value = Math.PI * 2;

   private ColorPickerMath() {
   }

   public static float hue(float value, float currentValue) {
      return wrap(
         (float)(Math.atan2(currentValue, value) / 6.283185307179586 + 0.25)
      );
   }

   public static float wrap(float value) {
      return Float.isFinite(value) ? value - (float)Math.floor(value) : 0.0F;
   }

   public static ColorPickerMath.Point polar(float value, float currentValue) {
      double doubleValue = (value - 0.25) * 6.283185307179586;
      return new ColorPickerMath.Point((float)Math.cos(doubleValue) * currentValue, (float)Math.sin(doubleValue) * currentValue);
   }

   public static ColorPickerMath.Point marker(float value, float currentValue, float nextValue) {
      ColorPickerMath.Point point = polar(value, 0.35F);
      ColorPickerMath.Point currentPoint = polar(value + 0.33333334F, 0.35F);
      ColorPickerMath.Point nextPoint = polar(value - 0.33333334F, 0.35F);
      return new ColorPickerMath.Point(
         point.x * currentValue * nextValue + currentPoint.x * (1.0F - currentValue) * nextValue + nextPoint.x * (1.0F - nextValue),
         point.y * currentValue * nextValue + currentPoint.y * (1.0F - currentValue) * nextValue + nextPoint.y * (1.0F - nextValue)
      );
   }

   public static boolean contains(float value, float currentValue, float nextValue) {
      float[] floats = createFloat(value, currentValue, nextValue);
      return floats[0] >= -1.0E-5
         && floats[1] >= -1.0E-5
         && floats[2] >= -1.0E-5;
   }

   public static ColorPickerMath.SB pick(float value, float currentValue, float nextValue) {
      if (!contains(value, currentValue, nextValue)) {
         ColorPickerMath.Point[] points = new ColorPickerMath.Point[]{
            polar(nextValue, 0.35F),
            polar(nextValue + 0.33333334F, 0.35F),
            polar(nextValue - 0.33333334F, 0.35F)
         };
         ColorPickerMath.Point point = points[0];
         float previousValue = Float.POSITIVE_INFINITY;

         for (int index = 0; index < 3; index++) {
            ColorPickerMath.Point currentPoint = points[index];
            ColorPickerMath.Point nextPoint = points[(index + 1) % 3];
            float sourceValue = nextPoint.x - currentPoint.x;
            float targetValue = nextPoint.y - currentPoint.y;
            float inputValue = Math.clamp(((value - currentPoint.x) * sourceValue + (currentValue - currentPoint.y) * targetValue) / (sourceValue * sourceValue + targetValue * targetValue), 0.0F, 1.0F);
            ColorPickerMath.Point previousPoint = new ColorPickerMath.Point(currentPoint.x + inputValue * sourceValue, currentPoint.y + inputValue * targetValue);
            float outputValue = (value - previousPoint.x) * (value - previousPoint.x) + (currentValue - previousPoint.y) * (currentValue - previousPoint.y);
            if (outputValue < previousValue) {
               previousValue = outputValue;
               point = previousPoint;
            }
         }

         value = point.x;
         currentValue = point.y;
      }

      float[] floats = createFloat(value, currentValue, nextValue);
      float resultValue = Math.clamp(floats[0] + floats[1], 0.0F, 1.0F);
      return new ColorPickerMath.SB(
         resultValue > 1.0E-5 ? Math.clamp(floats[0] / resultValue, 0.0F, 1.0F) : 0.0F, resultValue
      );
   }

   private static float[] createFloat(float value, float currentValue, float nextValue) {
      ColorPickerMath.Point point = polar(nextValue, 0.35F);
      ColorPickerMath.Point currentPoint = polar(nextValue + 0.33333334F, 0.35F);
      ColorPickerMath.Point nextPoint = polar(nextValue - 0.33333334F, 0.35F);
      float previousValue = (currentPoint.y - nextPoint.y) * (point.x - nextPoint.x) + (nextPoint.x - currentPoint.x) * (point.y - nextPoint.y);
      float sourceValue = ((currentPoint.y - nextPoint.y) * (value - nextPoint.x) + (nextPoint.x - currentPoint.x) * (currentValue - nextPoint.y)) / previousValue;
      float targetValue = ((nextPoint.y - point.y) * (value - nextPoint.x) + (point.x - nextPoint.x) * (currentValue - nextPoint.y)) / previousValue;
      return new float[]{sourceValue, targetValue, 1.0F - sourceValue - targetValue};
   }

   public record Point(float x, float y) {
   }

   public record SB(float saturation, float brightness) {
   }
}
