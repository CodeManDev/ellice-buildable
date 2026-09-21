package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public final class TerrainRecoveredFactory {
  private TerrainRecoveredFactory() {}

  public static GeometryVertexCountService create(List<Vector3d> items) {
    if (items.size() < 2) {
      throw new IllegalArgumentException("A route needs two points");
    }

    ArrayList arrayList = new ArrayList();

    for (int index = 0; index < items.size(); index++) {
      Vector3d vector3d = (Vector3d) items.get(index);
      if (index > 0) {
        Vector3d currentVector3d = (Vector3d) items.get(index - 1);
        if (Math.abs(vector3d.y - currentVector3d.y) > 0.01) {
          arrayList.add(
              new Vector3d(
                  (vector3d.x + currentVector3d.x) * 0.5,
                  Math.max(vector3d.y, currentVector3d.y),
                  (vector3d.z + currentVector3d.z) * 0.5));
        }
      }

      arrayList.add(new Vector3d(vector3d));
    }

    Vector3d nextVector3d = (Vector3d) items.getFirst();
    float[] currentSize = new float[arrayList.size() * 2 * 12];
    int[] nextSize = new int[(arrayList.size() - 1) * 6];
    double doubleValue = 0.0;

    for (int currentIndex = 0; currentIndex < arrayList.size(); currentIndex++) {
      Vector3d previousVector3d = (Vector3d) arrayList.get(currentIndex);
      Vector3d sourceVector3d =
          new Vector3d(previousVector3d)
              .sub((Vector3dc) arrayList.get(Math.max(0, currentIndex - 1)));
      Vector3d previousSize =
          new Vector3d((Vector3dc) arrayList.get(Math.min(arrayList.size() - 1, currentIndex + 1)))
              .sub(previousVector3d);
      if (currentIndex > 0) {
        doubleValue += sourceVector3d.length();
      }

      sourceVector3d.y = previousSize.y = 0.0;
      if (sourceVector3d.lengthSquared() < 1.0E-10) {
        sourceVector3d.set(previousSize);
      }

      if (previousSize.lengthSquared() < 1.0E-10) {
        previousSize.set(sourceVector3d);
      }

      if (sourceVector3d.lengthSquared() < 1.0E-10) {
        sourceVector3d.set(0.0, 0.0, 1.0);
      }

      if (previousSize.lengthSquared() < 1.0E-10) {
        previousSize.set(0.0, 0.0, 1.0);
      }

      sourceVector3d.normalize();
      previousSize.normalize();
      Vector3d targetVector3d = new Vector3d(sourceVector3d).add(previousSize);
      if (targetVector3d.lengthSquared() < 1.0E-8) {
        targetVector3d.set(previousSize);
      }

      targetVector3d.normalize();
      Vector3d inputVector3d = new Vector3d(-targetVector3d.z, 0.0, targetVector3d.x);
      double currentDoubleValue =
          Math.min(2.0, 1.0 / Math.max(0.5, targetVector3d.dot(previousSize)));

      for (int nextIndex = 0; nextIndex < 2; nextIndex++) {
        int previousIndex = (currentIndex * 2 + nextIndex) * 12;
        double nextDoubleValue = (nextIndex == 0 ? -1 : 1) * currentDoubleValue;
        currentSize[previousIndex] =
            (float)
                (previousVector3d.x - nextVector3d.x + inputVector3d.x * nextDoubleValue * 0.125);
        currentSize[previousIndex + 1] = (float) (previousVector3d.y - nextVector3d.y + 0.07);
        currentSize[previousIndex + 2] =
            (float)
                (previousVector3d.z - nextVector3d.z + inputVector3d.z * nextDoubleValue * 0.125);
        currentSize[previousIndex + 4] = 1.0F;
        currentSize[previousIndex + 6] = (float) ((nextIndex == 0 ? -1 : 1) * currentDoubleValue);
        currentSize[previousIndex + 7] = (float) doubleValue;
        currentSize[previousIndex + 8] = (float) inputVector3d.x;
        currentSize[previousIndex + 10] = (float) inputVector3d.z;
        currentSize[previousIndex + 11] = 1.0F;
      }

      if (currentIndex + 1 < arrayList.size()) {
        int sourceIndex = currentIndex * 6;
        int value = currentIndex * 2;
        nextSize[sourceIndex] = value;
        nextSize[sourceIndex + 1] = value + 1;
        nextSize[sourceIndex + 2] = value + 2;
        nextSize[sourceIndex + 3] = value + 1;
        nextSize[sourceIndex + 4] = value + 3;
        nextSize[sourceIndex + 5] = value + 2;
      }
    }

    return new GeometryVertexCountService(currentSize, nextSize);
  }
}
