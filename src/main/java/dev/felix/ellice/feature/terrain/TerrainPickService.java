package dev.felix.ellice.feature.terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.joml.Vector3d;

public final class TerrainPickService {
  private TerrainPickService() {}

  public static Vector3d pick(
      Iterable<TerrainLayerData> items, TerrainViewportService.Ray ray, double doubleValue) {
    double currentDoubleValue = doubleValue;
    byte byteValue = 0;

    for (TerrainLayerData terrainLayerData : items) {
      TerrainData terrainData = terrainLayerData.key();
      double nextDoubleValue =
          calculateValue(
              ray,
              terrainData.blockX(),
              terrainData.blockY(),
              terrainData.blockZ(),
              terrainData.blockX() + 16,
              terrainData.blockY() + 16,
              terrainData.blockZ() + 16);
      if (!(nextDoubleValue < 0.0) && !(nextDoubleValue > currentDoubleValue)) {
        double previousDoubleValue = ray.origin().x - terrainData.blockX();
        double sourceDoubleValue = ray.origin().y - terrainData.blockY();
        double targetDoubleValue = ray.origin().z - terrainData.blockZ();

        for (TerrainLayerData.Layer layer : terrainLayerData.layers()) {
          ByteBuffer byteBuffer = ByteBuffer.wrap(layer.vertices()).order(ByteOrder.nativeOrder());

          for (byte currentByteValue = 0;
              currentByteValue < layer.vertexCount();
              currentByteValue += 4) {
            int value = currentByteValue * 28;
            int currentValue = value + 28;
            int nextValue = currentValue + 28;
            int previousValue = nextValue + 28;
            double inputDoubleValue =
                calculateValue2(
                    byteBuffer,
                    value,
                    currentValue,
                    nextValue,
                    previousDoubleValue,
                    sourceDoubleValue,
                    targetDoubleValue,
                    ray.direction());
            if (inputDoubleValue >= 0.0 && inputDoubleValue < currentDoubleValue) {
              currentDoubleValue = inputDoubleValue;
              byteValue = 1;
            }

            double outputDoubleValue =
                calculateValue2(
                    byteBuffer,
                    nextValue,
                    previousValue,
                    value,
                    previousDoubleValue,
                    sourceDoubleValue,
                    targetDoubleValue,
                    ray.direction());
            if (outputDoubleValue >= 0.0 && outputDoubleValue < currentDoubleValue) {
              currentDoubleValue = outputDoubleValue;
              byteValue = 1;
            }
          }
        }
      }
    }

    return byteValue != 0 ? ray.at(currentDoubleValue) : null;
  }

  private static double calculateValue(
      TerrainViewportService.Ray ray,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue,
      double sourceDoubleValue,
      double targetDoubleValue) {
    double inputDoubleValue = 0.0;
    double outputDoubleValue = Double.POSITIVE_INFINITY;

    for (int index = 0; index < 3; index++) {
      double resultDoubleValue = ray.origin().get(index);
      double candidateDoubleValue = ray.direction().get(index);
      double selectedDoubleValue =
          index == 0 ? doubleValue : (index == 1 ? currentDoubleValue : nextDoubleValue);
      double defaultDoubleValue =
          index == 0 ? previousDoubleValue : (index == 1 ? sourceDoubleValue : targetDoubleValue);
      if (Math.abs(candidateDoubleValue) < 1.0E-12) {
        if (resultDoubleValue < selectedDoubleValue || resultDoubleValue > defaultDoubleValue) {
          return -1.0;
        }
      } else {
        double initialDoubleValue =
            (selectedDoubleValue - resultDoubleValue) / candidateDoubleValue;
        double resolvedDoubleValue =
            (defaultDoubleValue - resultDoubleValue) / candidateDoubleValue;
        inputDoubleValue =
            Math.max(inputDoubleValue, Math.min(initialDoubleValue, resolvedDoubleValue));
        outputDoubleValue =
            Math.min(outputDoubleValue, Math.max(initialDoubleValue, resolvedDoubleValue));
        if (inputDoubleValue > outputDoubleValue) {
          return -1.0;
        }
      }
    }

    return inputDoubleValue;
  }

  private static double calculateValue2(
      ByteBuffer byteBuffer,
      int value,
      int currentValue,
      int nextValue,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      Vector3d vector3d) {
    double previousDoubleValue = byteBuffer.getFloat(value);
    double sourceDoubleValue = byteBuffer.getFloat(value + 4);
    double targetDoubleValue = byteBuffer.getFloat(value + 8);
    double inputDoubleValue = byteBuffer.getFloat(currentValue) - previousDoubleValue;
    double outputDoubleValue = byteBuffer.getFloat(currentValue + 4) - sourceDoubleValue;
    double resultDoubleValue = byteBuffer.getFloat(currentValue + 8) - targetDoubleValue;
    double candidateDoubleValue = byteBuffer.getFloat(nextValue) - previousDoubleValue;
    double selectedDoubleValue = byteBuffer.getFloat(nextValue + 4) - sourceDoubleValue;
    double defaultDoubleValue = byteBuffer.getFloat(nextValue + 8) - targetDoubleValue;
    double initialDoubleValue = vector3d.y * defaultDoubleValue - vector3d.z * selectedDoubleValue;
    double resolvedDoubleValue =
        vector3d.z * candidateDoubleValue - vector3d.x * defaultDoubleValue;
    double computedDoubleValue =
        vector3d.x * selectedDoubleValue - vector3d.y * candidateDoubleValue;
    double cachedDoubleValue =
        inputDoubleValue * initialDoubleValue
            + outputDoubleValue * resolvedDoubleValue
            + resultDoubleValue * computedDoubleValue;
    if (Math.abs(cachedDoubleValue) < 1.0E-10) {
      return -1.0;
    } else {
      double pendingDoubleValue = 1.0 / cachedDoubleValue;
      double activeDoubleValue = doubleValue - previousDoubleValue;
      double fallbackDoubleValue = currentDoubleValue - sourceDoubleValue;
      double primaryDoubleValue = nextDoubleValue - targetDoubleValue;
      double secondaryDoubleValue =
          (activeDoubleValue * initialDoubleValue
                  + fallbackDoubleValue * resolvedDoubleValue
                  + primaryDoubleValue * computedDoubleValue)
              * pendingDoubleValue;
      if (!(secondaryDoubleValue < 0.0) && !(secondaryDoubleValue > 1.0)) {
        double tertiaryDoubleValue =
            fallbackDoubleValue * resultDoubleValue - primaryDoubleValue * outputDoubleValue;
        double temporaryDoubleValue =
            primaryDoubleValue * inputDoubleValue - activeDoubleValue * resultDoubleValue;
        double requestedDoubleValue =
            activeDoubleValue * outputDoubleValue - fallbackDoubleValue * inputDoubleValue;
        double actualDoubleValue =
            (vector3d.x * tertiaryDoubleValue
                    + vector3d.y * temporaryDoubleValue
                    + vector3d.z * requestedDoubleValue)
                * pendingDoubleValue;
        return !(actualDoubleValue < 0.0) && !(secondaryDoubleValue + actualDoubleValue > 1.0)
            ? (candidateDoubleValue * tertiaryDoubleValue
                    + selectedDoubleValue * temporaryDoubleValue
                    + defaultDoubleValue * requestedDoubleValue)
                * pendingDoubleValue
            : -1.0;
      } else {
        return -1.0;
      }
    }
  }
}
