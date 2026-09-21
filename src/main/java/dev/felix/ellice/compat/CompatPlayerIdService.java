package dev.felix.ellice.compat;

import java.util.Arrays;
import java.util.UUID;

public final class CompatPlayerIdService {
   private static final float[] float2 = new float[0];
   private final UUID uUID;
   private final int count;
   private final float[] float3;
   private final float[] float4;

   public CompatPlayerIdService(UUID currentUUID, int value, float[] floats, float[] currentFloats) {
      if (currentUUID == null) {
         throw new IllegalArgumentException("playerId is required");
      }

      this.uUID = currentUUID;
      this.count = value;
      this.float3 = createFloat("baseTrianglePositions", floats);
      this.float4 = createFloat("layerTrianglePositions", currentFloats);
   }

   public UUID playerId() {
      return this.uUID;
   }

   public int entityId() {
      return this.count;
   }

   public int baseTriangleCount() {
      return this.float3.length / 9;
   }

   public int layerTriangleCount() {
      return this.float4.length / 9;
   }

   public boolean hasGeometry() {
      return this.float3.length != 0 || this.float4.length != 0;
   }

   public float[] copyBaseTrianglePositions() {
      return (float[])this.float3.clone();
   }

   public float[] copyLayerTrianglePositions() {
      return (float[])this.float4.clone();
   }

   private static float[] createFloat(String text, float[] floats) {
      if (floats != null && floats.length != 0) {
         if (floats.length % 9 != 0) {
            throw new IllegalArgumentException(text + " must contain complete triangles");
         }

         float[] currentLength = Arrays.copyOf(floats, floats.length);

         for (float value : currentLength) {
            if (!Float.isFinite(value)) {
               throw new IllegalArgumentException(text + " must contain only finite positions");
            }
         }

         return currentLength;
      } else {
         return float2;
      }
   }
}
