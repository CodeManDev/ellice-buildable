package dev.felix.ellice.compat;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TriangleVertexCollector implements VertexConsumer {
   private static final int count = 73728;
   private float[] float2 = new float[2304];
   private final float[] float3 = new float[12];
   private int count2;
   private int count3;
   private boolean enabled;

   public float[] triangles() {
      return this.enabled ? new float[0] : Arrays.copyOf(this.float2, this.count2);
   }

   public void triangle(float[] floats, int value, int currentValue, int nextValue) {
      if (this.count2 + 9 > 73728) {
         this.enabled = true;
      } else {
         if (this.count2 + 9 > this.float2.length) {
            this.float2 = Arrays.copyOf(this.float2, Math.min(73728, this.float2.length * 2));
         }

         for (int previousValue : new int[]{value, currentValue, nextValue}) {
            System.arraycopy(floats, previousValue * 3, this.float2, this.count2, 3);
            this.count2 += 3;
         }
      }
   }

   public void transformed(float[] floats, Matrix4f matrix4f) {
      Vector3f vector3f = new Vector3f();

      for (byte byteValue = 0; byteValue + 8 < floats.length; byteValue += 9) {
         float[] currentFloats = new float[9];

         for (int index = 0; index < 3; index++) {
            matrix4f.transformPosition(floats[byteValue + index * 3], floats[byteValue + index * 3 + 1], floats[byteValue + index * 3 + 2], vector3f);
            currentFloats[index * 3] = vector3f.x;
            currentFloats[index * 3 + 1] = vector3f.y;
            currentFloats[index * 3 + 2] = vector3f.z;
         }

         this.triangle(currentFloats, 0, 1, 2);
      }
   }

   public VertexConsumer addVertex(float value, float currentValue, float nextValue) {
      this.float3[this.count3 * 3] = value;
      this.float3[this.count3 * 3 + 1] = currentValue;
      this.float3[this.count3 * 3 + 2] = nextValue;
      if (++this.count3 == 4) {
         this.triangle(this.float3, 0, 1, 2);
         this.triangle(this.float3, 0, 2, 3);
         this.count3 = 0;
      }

      return this;
   }

   public void addVertex(
      float value, float currentValue, float nextValue, int previousValue, float sourceValue, float targetValue, int inputValue, int outputValue, float resultValue, float candidateValue, float selectedValue
   ) {
      this.addVertex(value, currentValue, nextValue);
   }

   public VertexConsumer setColor(int color, int currentColor, int nextColor, int previousColor) {
      return this;
   }

   public VertexConsumer setColor(int color) {
      return this;
   }

   public VertexConsumer setUv(float value, float currentValue) {
      return this;
   }

   public VertexConsumer setUv1(int value, int currentValue) {
      return this;
   }

   public VertexConsumer setUv2(int value, int currentValue) {
      return this;
   }

   public VertexConsumer setNormal(float value, float currentValue, float nextValue) {
      return this;
   }

   public VertexConsumer setLineWidth(float value) {
      return this;
   }
}

