package dev.felix.ellice.render.render3d;

import org.joml.Matrix4f;
import org.joml.Vector3d;

public final class Render3dViewService {
   private final Matrix4f matrix4f;
   private final Matrix4f matrix4f2;
   private final Matrix4f matrix4f3;
   private final Matrix4f matrix4f4;
   private final Matrix4f matrix4f5;
   private final Matrix4f matrix4f6;
   private final Vector3d vector3d;
   private final Vector3d vector3d2;
   private final int count;
   private final int count2;
   private final float value;
   private final float value2;
   private final double value3;
   private final long timestamp;

   public Render3dViewService(
      Matrix4f currentMatrix4f,
      Matrix4f nextMatrix4f,
      Matrix4f previousMatrix4f,
      Vector3d currentVector3d,
      Vector3d nextVector3d,
      int currentValue,
      int nextValue,
      float previousValue,
      float sourceValue,
      double doubleValue,
      long longValue
   ) {
      if (currentMatrix4f != null && nextMatrix4f != null && currentVector3d != null) {
         this.matrix4f = new Matrix4f(currentMatrix4f);
         this.matrix4f2 = new Matrix4f(nextMatrix4f);
         this.matrix4f3 = new Matrix4f(nextMatrix4f).mul(currentMatrix4f);
         this.matrix4f4 = new Matrix4f(currentMatrix4f).invert();
         this.matrix4f5 = new Matrix4f(nextMatrix4f).invert();
         this.matrix4f6 = previousMatrix4f == null ? new Matrix4f(this.matrix4f3) : new Matrix4f(previousMatrix4f);
         this.vector3d = new Vector3d(currentVector3d);
         this.vector3d2 = nextVector3d == null ? new Vector3d(currentVector3d) : new Vector3d(nextVector3d);
         this.count = Math.max(1, currentValue);
         this.count2 = Math.max(1, nextValue);
         this.value = previousValue;
         this.value2 = Math.max(0.0F, Math.min(sourceValue, 0.25F));
         this.value3 = doubleValue;
         this.timestamp = longValue;
      } else {
         throw new IllegalArgumentException("Camera matrices and position are required");
      }
   }

   public Matrix4f view() {
      return new Matrix4f(this.matrix4f);
   }

   public Matrix4f projection() {
      return new Matrix4f(this.matrix4f2);
   }

   public Matrix4f viewProjection() {
      return new Matrix4f(this.matrix4f3);
   }

   public Matrix4f inverseView() {
      return new Matrix4f(this.matrix4f4);
   }

   public Matrix4f inverseProjection() {
      return new Matrix4f(this.matrix4f5);
   }

   public Matrix4f previousViewProjection() {
      return new Matrix4f(this.matrix4f6);
   }

   public Vector3d cameraPosition() {
      return new Vector3d(this.vector3d);
   }

   public Vector3d previousCameraPosition() {
      return new Vector3d(this.vector3d2);
   }

   public int width() {
      return this.count;
   }

   public int height() {
      return this.count2;
   }

   public float tickDelta() {
      return this.value;
   }

   public float deltaSeconds() {
      return this.value2;
   }

   public double timeSeconds() {
      return this.value3;
   }

   public long frameIndex() {
      return this.timestamp;
   }
}
