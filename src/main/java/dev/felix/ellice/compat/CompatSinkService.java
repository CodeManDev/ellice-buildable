package dev.felix.ellice.compat;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joml.Vector3f;

public final class CompatSinkService {
   private static final int count = 65536;
   private final Map<CompatSinkService.Key, ArrayList<float[]>> entries = new LinkedHashMap<>();
   private final List<CompatSinkService.Sink> items = new ArrayList<>();
   private int count2;

   public VertexConsumer sink(int value) {
      CompatSinkService.Sink currentSink = new CompatSinkService.Sink(value);
      this.items.add(currentSink);
      return currentSink;
   }

   public List<CompatLoadedHandler.PlayerPart> finish() {
      for (CompatSinkService.Sink sink : this.items) {
         sink.updateState();
      }

      if (this.count2 > 65536) {
         return List.of();
      }

      ArrayList arrayList = new ArrayList();
      this.entries
         .forEach(
            (item, currentItem) -> {
               float[] currentSize = new float[currentItem.size() * 4 * 12];
               int[] nextSize = new int[currentItem.size() * 6];
               int value = 0;

               for (int index = 0; index < currentItem.size(); index++) {
                  System.arraycopy(currentItem.get(index), 0, currentSize, index * 48, 48);
                  int currentValue = index * 4;
                  value = calculateValue(currentSize, nextSize, value, currentValue, currentValue + 1, currentValue + 2);
                  value = calculateValue(currentSize, nextSize, value, currentValue, currentValue + 2, currentValue + 3);
               }

               if (value > 0) {
                  int[] currentLength = new int[currentSize.length / 12];
                  Arrays.fill(currentLength, -1);

                  for (int currentIndex = 0; currentIndex < value; currentIndex++) {
                     currentLength[nextSize[currentIndex]] = 0;
                  }

                  int nextIndex = 0;

                  for (int previousIndex = 0; previousIndex < currentLength.length; previousIndex++) {
                     if (currentLength[previousIndex] == 0) {
                        currentLength[previousIndex] = nextIndex;
                        System.arraycopy(currentSize, previousIndex * 12, currentSize, nextIndex++ * 12, 12);
                     }
                  }

                  for (int sourceIndex = 0; sourceIndex < value; sourceIndex++) {
                     nextSize[sourceIndex] = currentLength[nextSize[sourceIndex]];
                  }

                  arrayList.add(
                     new CompatLoadedHandler.PlayerPart(
                        new GeometryVertexCountService(Arrays.copyOf(currentSize, nextIndex * 12), Arrays.copyOf(nextSize, value)), item.texture, item.color
                     )
                  );
               }
            }
         );
      return List.copyOf(arrayList);
   }

   private static int calculateValue(float[] floats, int[] ints, int index, int value, int currentValue, int nextValue) {
      int currentIndex = value * 12;
      int nextIndex = currentValue * 12;
      int previousIndex = nextValue * 12;
      Vector3f vector3f = new Vector3f(floats[nextIndex] - floats[currentIndex], floats[nextIndex + 1] - floats[currentIndex + 1], floats[nextIndex + 2] - floats[currentIndex + 2]);
      Vector3f currentVector3f = new Vector3f(floats[previousIndex] - floats[currentIndex], floats[previousIndex + 1] - floats[currentIndex + 1], floats[previousIndex + 2] - floats[currentIndex + 2]);
      Vector3f nextVector3f = vector3f.cross(currentVector3f);
      float previousValue = nextVector3f.dot(
         floats[currentIndex + 3] + floats[nextIndex + 3] + floats[previousIndex + 3],
         floats[currentIndex + 4] + floats[nextIndex + 4] + floats[previousIndex + 4],
         floats[currentIndex + 5] + floats[nextIndex + 5] + floats[previousIndex + 5]
      );
      if (Float.isFinite(previousValue) && previousValue != 0.0F) {
         ints[index++] = value;
         ints[index++] = previousValue > 0.0F ? currentValue : nextValue;
         ints[index++] = previousValue > 0.0F ? nextValue : currentValue;
         return index;
      } else {
         return index;
      }
   }

   private record Key(int texture, int color) {
   }

   private final class Sink implements VertexConsumer {
      private final int count3;
      private float[] float2 = new float[48];
      private int count4;
      private int count5 = -1;
      private int count6 = -1;
      private boolean enabled;

      Sink(int value) {
         this.count3 = value;
      }

      private void updateState() {
         if (this.enabled) {
            this.enabled = false;
            int value = this.count4 * 12;
            Vector3f vector3f = new Vector3f(this.float2[value + 3], this.float2[value + 4], this.float2[value + 5]);
            if (vector3f.lengthSquared() < 1.0E-10F) {
               vector3f.set(0.0F, 1.0F, 0.0F);
            }

            vector3f.normalize();
            Vector3f currentVector3f = Math.abs(vector3f.y) < 0.9F
               ? new Vector3f(0.0F, 1.0F, 0.0F)
               : new Vector3f(1.0F, 0.0F, 0.0F);
            currentVector3f.cross(vector3f).normalize();
            this.float2[value + 3] = vector3f.x;
            this.float2[value + 4] = vector3f.y;
            this.float2[value + 5] = vector3f.z;
            this.float2[value + 8] = currentVector3f.x;
            this.float2[value + 9] = currentVector3f.y;
            this.float2[value + 10] = currentVector3f.z;
            this.float2[value + 11] = 1.0F;
            if (this.count4 == 0) {
               this.count6 = this.count5;
            }

            if (++this.count4 == 4) {
               CompatSinkService.this.count2 += 4;
               if (this.count3 > 0 && CompatSinkService.this.count2 <= 65536) {
                  CompatSinkService.this.entries
                     .computeIfAbsent(new CompatSinkService.Key(this.count3, this.count6), item -> new ArrayList<>())
                     .add(this.float2);
               }

               this.float2 = new float[48];
               this.count4 = 0;
            }
         }
      }

      public VertexConsumer addVertex(float value, float currentValue, float nextValue) {
         this.updateState();
         this.enabled = true;
         int index = this.count4 * 12;
         this.float2[index] = value;
         this.float2[index + 1] = currentValue;
         this.float2[index + 2] = nextValue;
         return this;
      }

      public void addVertex(
         float value, float currentValue, float nextValue, int previousValue, float sourceValue, float targetValue, int inputValue, int outputValue, float resultValue, float candidateValue, float selectedValue
      ) {
         this.addVertex(value, currentValue, nextValue);
         this.setColor(previousValue);
         this.setUv(sourceValue, targetValue);
         this.setNormal(resultValue, candidateValue, selectedValue);
      }

      public VertexConsumer setColor(int color, int currentColor, int nextColor, int previousColor) {
         this.count5 = previousColor << 24 | color << 16 | currentColor << 8 | nextColor;
         return this;
      }

      public VertexConsumer setColor(int color) {
         this.count5 = color;
         return this;
      }

      public VertexConsumer setUv(float value, float currentValue) {
         this.float2[this.count4 * 12 + 6] = value;
         this.float2[this.count4 * 12 + 7] = currentValue;
         return this;
      }

      public VertexConsumer setNormal(float value, float currentValue, float nextValue) {
         this.float2[this.count4 * 12 + 3] = value;
         this.float2[this.count4 * 12 + 4] = currentValue;
         this.float2[this.count4 * 12 + 5] = nextValue;
         return this;
      }

      public VertexConsumer setUv1(int value, int currentValue) {
         return this;
      }

      public VertexConsumer setUv2(int value, int currentValue) {
         return this;
      }

      public VertexConsumer setLineWidth(float value) {
         return this;
      }
   }
}

