package dev.felix.ellice.feature.rotation;

public record RotationVector(double x, double y, double z) {
   public RotationVector(double x, double y, double z) {
      if (Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z)) {
         this.x = x;
         this.y = y;
         this.z = z;
      } else {
         throw new IllegalArgumentException("Vector components must be finite");
      }
   }

   public RotationVector add(RotationVector rotationVector) {
      return new RotationVector(this.x + rotationVector.x, this.y + rotationVector.y, this.z + rotationVector.z);
   }

   public RotationVector subtract(RotationVector rotationVector) {
      return new RotationVector(this.x - rotationVector.x, this.y - rotationVector.y, this.z - rotationVector.z);
   }

   public RotationVector multiply(double doubleValue) {
      return new RotationVector(this.x * doubleValue, this.y * doubleValue, this.z * doubleValue);
   }

   public double dot(RotationVector rotationVector) {
      return this.x * rotationVector.x + this.y * rotationVector.y + this.z * rotationVector.z;
   }

   public double lengthSquared() {
      return this.dot(this);
   }

   public double length() {
      return Math.sqrt(this.lengthSquared());
   }

   public RotationVector normalized() {
      double currentLength = this.length();
      if (currentLength < 1.0E-12) {
         throw new IllegalStateException("Cannot normalize a zero-length vector");
      } else {
         return this.multiply(1.0 / currentLength);
      }
   }
}
