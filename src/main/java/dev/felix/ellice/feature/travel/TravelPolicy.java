package dev.felix.ellice.feature.travel;

public record TravelPolicy(
    boolean canSprint,
    boolean allowParkour,
    boolean allowParkourAscend,
    boolean allowDiagonal,
    boolean allowDiagonalAscend,
    boolean allowBridge,
    boolean allowTower,
    boolean hasThrowaway,
    int maxFallHeight,
    double jumpPenalty,
    double placePenalty,
    double breakPenalty,
    double backtrackFavoring) {
  public TravelPolicy(
      boolean canSprint,
      boolean allowParkour,
      boolean allowParkourAscend,
      boolean allowDiagonal,
      boolean allowDiagonalAscend,
      boolean allowBridge,
      boolean allowTower,
      boolean hasThrowaway,
      int maxFallHeight,
      double jumpPenalty,
      double placePenalty,
      double breakPenalty,
      double backtrackFavoring) {
    if (maxFallHeight < 0 || maxFallHeight > 23) {
      throw new IllegalArgumentException("Invalid max fall height");
    }

    if (!Double.isFinite(jumpPenalty) || jumpPenalty < 0.0 || jumpPenalty > 20.0) {
      throw new IllegalArgumentException("Invalid jump penalty");
    }

    if (!Double.isFinite(placePenalty) || placePenalty < 0.0 || placePenalty > 200.0) {
      throw new IllegalArgumentException("Invalid place penalty");
    }

    if (!Double.isFinite(breakPenalty) || breakPenalty < 0.0 || breakPenalty > 500.0) {
      throw new IllegalArgumentException("Invalid break penalty");
    }

    if (Double.isFinite(backtrackFavoring)
        && !(backtrackFavoring < 0.0)
        && !(backtrackFavoring > 1.0)) {
      this.canSprint = canSprint;
      this.allowParkour = allowParkour;
      this.allowParkourAscend = allowParkourAscend;
      this.allowDiagonal = allowDiagonal;
      this.allowDiagonalAscend = allowDiagonalAscend;
      this.allowBridge = allowBridge;
      this.allowTower = allowTower;
      this.hasThrowaway = hasThrowaway;
      this.maxFallHeight = maxFallHeight;
      this.jumpPenalty = jumpPenalty;
      this.placePenalty = placePenalty;
      this.breakPenalty = breakPenalty;
      this.backtrackFavoring = backtrackFavoring;
    } else {
      throw new IllegalArgumentException("Invalid backtrack favoring");
    }
  }

  public static TravelPolicy legit(boolean enabled) {
    return new TravelPolicy(
        enabled, true, true, true, false, false, false, false, 3, 2.0, 20.0, 10.0, 0.5);
  }

  public static TravelPolicy smartAssist(boolean enabled, boolean currentEnabled) {
    return new TravelPolicy(
        enabled, true, true, true, false, true, true, currentEnabled, 3, 2.0, 20.0, 10.0, 0.5);
  }

  public boolean canPlace() {
    return this.hasThrowaway && (this.allowBridge || this.allowTower);
  }
}
