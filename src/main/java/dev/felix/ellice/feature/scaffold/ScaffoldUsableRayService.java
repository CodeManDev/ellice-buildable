package dev.felix.ellice.feature.scaffold;

import java.util.Optional;

public final class ScaffoldUsableRayService {
  private ScaffoldUsableRayService() {}

  public static boolean usableRay(Optional<PlacementCandidate> result, double doubleValue) {
    return result != null
            && Double.isFinite(doubleValue)
            && !(doubleValue < 0.0)
            && !(doubleValue > 0.5)
        ? result.filter(item -> !item.inside() && item.faceEdgeMargin() >= doubleValue).isPresent()
        : false;
  }

  public static boolean placementReady(
      boolean enabled,
      Optional<PlacementCandidate> result,
      double doubleValue,
      boolean currentEnabled,
      double currentDoubleValue,
      double nextDoubleValue,
      int value,
      int currentValue,
      boolean nextEnabled,
      boolean previousEnabled) {
    return enabled
        && usableRay(result, doubleValue)
        && currentEnabled
        && Double.isFinite(currentDoubleValue)
        && Double.isFinite(nextDoubleValue)
        && currentDoubleValue <= nextDoubleValue
        && currentValue >= 0
        && value == currentValue
        && nextEnabled
        && previousEnabled;
  }
}
