package dev.felix.ellice.feature.terrain;

import java.util.UUID;

public record TerrainKindData(
    UUID id,
    String name,
    String dimension,
    double x,
    double y,
    double z,
    int color,
    TerrainKindData.Kind kind,
    long createdAt) {
  public TerrainKindData(
      UUID uUID,
      String text,
      String currentText,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      int value) {
    this(
        uUID,
        text,
        currentText,
        doubleValue,
        currentDoubleValue,
        nextDoubleValue,
        value,
        TerrainKindData.Kind.PLACE,
        System.currentTimeMillis());
  }

  public TerrainKindData(
      UUID id,
      String name,
      String dimension,
      double x,
      double y,
      double z,
      int color,
      TerrainKindData.Kind kind,
      long createdAt) {
    if (id != null && dimension != null && !dimension.isBlank()) {
      if (Double.isFinite(x)
          && Double.isFinite(y)
          && Double.isFinite(z)
          && !(Math.abs(x) > 3.0E7)
          && !(Math.abs(z) > 3.0E7)
          && !(Math.abs(y) > 4096.0)) {
        name = name == null ? "Waypoint" : name.strip().replaceAll("[\\p{Cntrl}]", "");
        if (name.isEmpty()) {
          name = "Waypoint";
        }

        if (name.codePointCount(0, name.length()) > 64) {
          name = name.substring(0, name.offsetByCodePoints(0, 64));
        }

        color |= -16777216;
        kind = kind == null ? TerrainKindData.Kind.PLACE : kind;
        dimension = TerrainCanonicalService.canonical(dimension);
        createdAt = Math.max(0L, createdAt);
        this.id = id;
        this.name = name;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.kind = kind;
        this.createdAt = createdAt;
      } else {
        throw new IllegalArgumentException("Invalid waypoint coordinates");
      }
    } else {
      throw new IllegalArgumentException("Missing waypoint identity");
    }
  }

  public TerrainKindData renamed(String name) {
    return new TerrainKindData(
        this.id,
        name,
        this.dimension,
        this.x,
        this.y,
        this.z,
        this.color,
        this.kind,
        this.createdAt);
  }

  public TerrainKindData movedTo(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return new TerrainKindData(
        this.id,
        this.name,
        this.dimension,
        doubleValue,
        currentDoubleValue,
        nextDoubleValue,
        this.color,
        this.kind,
        this.createdAt);
  }

  public TerrainKindData withKind(TerrainKindData.Kind currentKind) {
    return currentKind == this.kind
        ? this
        : new TerrainKindData(
            this.id,
            this.name,
            this.dimension,
            this.x,
            this.y,
            this.z,
            currentKind.color(),
            currentKind,
            this.createdAt);
  }

  public enum Kind {
    PLACE("Place", "flag", -3097345),
    BASE("Base", "base", -4794881),
    STASH("Stash", "stash", -3610966),
    PORTAL("Portal", "portal", -3097345),
    DANGER("Danger", "danger", -735349),
    DEATH("Death", "death", -870219);

    private final String text;
    private final String text2;
    private final int count;

    Kind(String currentText, String nextText, int value) {
      this.text = currentText;
      this.text2 = nextText;
      this.count = value;
    }

    public String label() {
      return this.text;
    }

    public String icon() {
      return this.text2;
    }

    public int color() {
      return this.count;
    }

    private static TerrainKindData.Kind[] $values() {
      return new TerrainKindData.Kind[] {PLACE, BASE, STASH, PORTAL, DANGER, DEATH};
    }
  }
}
