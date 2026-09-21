package dev.felix.ellice.feature.nametags;

import org.joml.Matrix4fc;
import org.joml.Vector4f;

public final class NametagsProjectService {
   private NametagsProjectService() {
   }

   public static NametagsProjectService.Point project(double doubleValue, double currentDoubleValue, double nextDoubleValue, Matrix4fc matrix4fc, Matrix4fc currentMatrix4fc, float value, float currentValue) {
      if (value > 0.0F && currentValue > 0.0F) {
         Vector4f vector4f = new Vector4f((float)doubleValue, (float)currentDoubleValue, (float)nextDoubleValue, 1.0F);
         matrix4fc.transform(vector4f);
         currentMatrix4fc.transform(vector4f);
         if (vector4f.isFinite() && !(vector4f.w <= 0.01F)) {
            float nextValue = vector4f.x / vector4f.w;
            float previousValue = vector4f.y / vector4f.w;
            float sourceValue = vector4f.z / vector4f.w;
            return !(nextValue < -1.0F)
                  && !(nextValue > 1.0F)
                  && !(previousValue < -1.0F)
                  && !(previousValue > 1.0F)
                  && !(sourceValue < -1.0F)
                  && !(sourceValue > 1.0F)
               ? new NametagsProjectService.Point(
                  (nextValue * 0.5F + 0.5F) * value,
                  (0.5F - previousValue * 0.5F) * currentValue
               )
               : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static NametagsProjectService.Bounds bounds(NametagsProjectService.Point point, float value, float currentValue, float nextValue, float previousValue) {
      return point != null
            && !(value <= 0.0F)
            && !(currentValue <= 0.0F)
            && !(value + 8.0F > nextValue)
            && !(currentValue + 8.0F > previousValue)
         ? new NametagsProjectService.Bounds(
            Math.clamp(point.x - value / 2.0F, 4.0F, nextValue - value - 4.0F),
            Math.clamp(
               point.y - currentValue - 5.0F,
               4.0F,
               previousValue - currentValue - 4.0F
            ),
            value,
            currentValue
         )
         : null;
   }

   public record Bounds(float x, float y, float width, float height) {
      public boolean overlaps(NametagsProjectService.Bounds bounds, float value) {
         return this.x < bounds.x + bounds.width + value
            && this.x + this.width + value > bounds.x
            && this.y < bounds.y + bounds.height + value
            && this.y + this.height + value > bounds.y;
      }
   }

   public record Point(float x, float y) {
   }
}
