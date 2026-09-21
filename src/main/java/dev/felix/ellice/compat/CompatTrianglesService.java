package dev.felix.ellice.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class CompatTrianglesService {
   private static final Map<Object, float[]> entries = new WeakHashMap<>();

   private CompatTrianglesService() {
   }

   public static float[] triangles(Object value, float[] floats, float[] currentFloats, TextureAtlasSprite textureAtlasSprite, int currentValue) {
      return entries.computeIfAbsent(value, item -> createFloat(floats, currentFloats, textureAtlasSprite, currentValue));
   }

   private static float[] createFloat(float[] floats, float[] currentFloats, TextureAtlasSprite textureAtlasSprite, int value) {
      TriangleVertexCollector triangleVertexCollector = new TriangleVertexCollector();
      SpriteContents spriteContents = textureAtlasSprite.contents();
      float[] nextFloats = (float[])currentFloats.clone();

      for (int index = 0; index < 4; index++) {
         nextFloats[index * 2] = (currentFloats[index * 2] - textureAtlasSprite.getU0()) / (textureAtlasSprite.getU1() - textureAtlasSprite.getU0());
         nextFloats[index * 2 + 1] = (currentFloats[index * 2 + 1] - textureAtlasSprite.getV0()) / (textureAtlasSprite.getV1() - textureAtlasSprite.getV0());
      }

      float currentValue = nextFloats[2] - nextFloats[0];
      float nextValue = nextFloats[3] - nextFloats[1];
      float previousValue = nextFloats[6] - nextFloats[0];
      float sourceValue = nextFloats[7] - nextFloats[1];
      float targetValue = currentValue * sourceValue - nextValue * previousValue;
      if (Math.abs(targetValue) < 1.0E-8) {
         triangleVertexCollector.triangle(floats, 0, 1, 2);
         triangleVertexCollector.triangle(floats, 0, 2, 3);
         return triangleVertexCollector.triangles();
      }

      float inputValue = 1.0F;
      float outputValue = 0.0F;
      float resultValue = 1.0F;
      float candidateValue = 0.0F;

      for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
         inputValue = Math.min(inputValue, nextFloats[currentIndex * 2]);
         outputValue = Math.max(outputValue, nextFloats[currentIndex * 2]);
         resultValue = Math.min(resultValue, nextFloats[currentIndex * 2 + 1]);
         candidateValue = Math.max(candidateValue, nextFloats[currentIndex * 2 + 1]);
      }

      int currentWidth = spriteContents.width();
      int currentHeight = spriteContents.height();
      byte byteValue = 1;

      for (int nextIndex = Math.max(0, (int)(resultValue * currentHeight)); nextIndex < Math.min(currentHeight, (int)Math.ceil(candidateValue * currentHeight)) && byteValue != 0; nextIndex++) {
         for (int previousIndex = Math.max(0, (int)(inputValue * currentWidth)); previousIndex < Math.min(currentWidth, (int)Math.ceil(outputValue * currentWidth)); previousIndex++) {
            if (spriteContents.isTransparent(value, previousIndex, nextIndex)) {
               byteValue = 0;
               break;
            }
         }
      }

      if (byteValue != 0) {
         triangleVertexCollector.triangle(floats, 0, 1, 2);
         triangleVertexCollector.triangle(floats, 0, 2, 3);
         return triangleVertexCollector.triangles();
      }

      for (int sourceIndex = Math.max(0, (int)(resultValue * currentHeight)); sourceIndex < Math.min(currentHeight, (int)Math.ceil(candidateValue * currentHeight)); sourceIndex++) {
         int targetIndex = Math.max(0, (int)(inputValue * currentWidth));
         int selectedValue = Math.min(currentWidth, (int)Math.ceil(outputValue * currentWidth));

         while (targetIndex < selectedValue) {
            while (targetIndex < selectedValue && spriteContents.isTransparent(value, targetIndex, sourceIndex)) {
               targetIndex++;
            }

            int defaultValue = targetIndex;

            while (targetIndex < selectedValue && !spriteContents.isTransparent(value, targetIndex, sourceIndex)) {
               targetIndex++;
            }

            if (defaultValue != targetIndex) {
               ArrayList arrayList = new ArrayList();

               for (int inputIndex = 0; inputIndex < 4; inputIndex++) {
                  arrayList.add(new float[]{nextFloats[inputIndex * 2], nextFloats[inputIndex * 2 + 1]});
               }

               List items = collectValues(arrayList, 0, (float)defaultValue / currentWidth, true);
               items = collectValues(items, 0, (float)targetIndex / currentWidth, false);
               items = collectValues(items, 1, (float)sourceIndex / currentHeight, true);
               items = collectValues(items, 1, (float)(sourceIndex + 1) / currentHeight, false);
               float[] currentSize = new float[items.size() * 3];

               for (int outputIndex = 0; outputIndex < items.size(); outputIndex++) {
                  float initialValue = ((float[])items.get(outputIndex))[0] - nextFloats[0];
                  float resolvedValue = ((float[])items.get(outputIndex))[1] - nextFloats[1];
                  float computedValue = (initialValue * sourceValue - resolvedValue * previousValue) / targetValue;
                  float cachedValue = (currentValue * resolvedValue - nextValue * initialValue) / targetValue;

                  for (int resultIndex = 0; resultIndex < 3; resultIndex++) {
                     currentSize[outputIndex * 3 + resultIndex] = floats[resultIndex]
                        + computedValue * (floats[3 + resultIndex] - floats[resultIndex])
                        + cachedValue * (floats[9 + resultIndex] - floats[resultIndex]);
                  }
               }

               for (int candidateIndex = 1; candidateIndex + 1 < items.size(); candidateIndex++) {
                  triangleVertexCollector.triangle(currentSize, 0, candidateIndex, candidateIndex + 1);
               }
            }
         }
      }

      return triangleVertexCollector.triangles();
   }

   private static List<float[]> collectValues(List<float[]> items, int index, float value, boolean enabled) {
      ArrayList arrayList = new ArrayList();
      if (items.isEmpty()) {
         return arrayList;
      }

      float[] floats = (float[])items.getLast();
      int currentValue = enabled ? (floats[index] >= value ? 1 : 0) : (floats[index] <= value ? 1 : 0);

      for (float[] currentFloats : items) {
         int nextValue = enabled ? (currentFloats[index] >= value ? 1 : 0) : (currentFloats[index] <= value ? 1 : 0);
         if (nextValue != currentValue) {
            float previousValue = (value - floats[index]) / (currentFloats[index] - floats[index]);
            arrayList.add(new float[]{floats[0] + previousValue * (currentFloats[0] - floats[0]), floats[1] + previousValue * (currentFloats[1] - floats[1])});
         }

         if (nextValue != 0) {
            arrayList.add(currentFloats);
         }

         floats = currentFloats;
         currentValue = nextValue;
      }

      return arrayList;
   }
}
