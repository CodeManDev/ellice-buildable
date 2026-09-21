package dev.felix.ellice.feature.travel;

import java.util.Objects;
import org.joml.Vector3d;

public record TravelData(Vector3d pos, TravelTypeData.Type type, double costTicks) {
  public TravelData(Vector3d pos, TravelTypeData.Type type, double costTicks) {
    pos = new Vector3d(Objects.requireNonNull(pos, "pos"));
    Objects.requireNonNull(type, "type");
    if (Double.isFinite(costTicks) && !(costTicks < 0.0) && !(costTicks >= 1000000.0)) {
      this.pos = pos;
      this.type = type;
      this.costTicks = costTicks;
    } else {
      throw new IllegalArgumentException("Invalid step cost");
    }
  }

  public static TravelData walk(Vector3d vector3d, double doubleValue) {
    return new TravelData(vector3d, TravelTypeData.Type.TRAVERSE, doubleValue);
  }
}
