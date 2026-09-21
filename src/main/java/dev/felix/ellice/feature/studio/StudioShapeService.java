package dev.felix.ellice.feature.studio;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class StudioShapeService {
  public static final int VERSION = 1;
  public static final int MAX_SHAPES = 64;
  public static final int MAX_BLOCKS = 96;
  private static final Gson gson2 = new Gson();
  public int version = 1;
  public String id = UUID.randomUUID().toString();
  public String name = "Untitled module";
  public float width = 400.0F;
  public float height = 240.0F;
  public List<StudioShapeService.Shape> shapes = new ArrayList<>();
  public List<StudioShapeService.Block> blocks = new ArrayList<>();

  public StudioShapeService.Shape shape(String text) {
    return this.shapes.stream()
        .filter(item -> Objects.equals(item.id, text))
        .findFirst()
        .orElse(null);
  }

  public StudioShapeService.Block block(String text) {
    return this.blocks.stream()
        .filter(item -> Objects.equals(item.id, text))
        .findFirst()
        .orElse(null);
  }

  public StudioShapeService copy() {
    return (StudioShapeService) gson2.fromJson(gson2.toJson(this), StudioShapeService.class);
  }

  public String snapshot() {
    return gson2.toJson(this);
  }

  public static StudioShapeService restore(String text) {
    return (StudioShapeService) gson2.fromJson(text, StudioShapeService.class);
  }

  public static final class Block {
    public String id = UUID.randomUUID().toString();
    public StudioMode kind = StudioMode.HEALTH;
    public float x = 40.0F;
    public float y = 40.0F;
    public float number = 1.0F;
    public String text = "{value}";
    public String target = "";
    public int color = -4344587;
    public Map<String, String> inputs = new LinkedHashMap<>();
    public Map<String, Float> defaults = new LinkedHashMap<>();
  }

  public enum Finish {
    SOLID,
    GRADIENT,
    AURORA;

    private static StudioShapeService.Finish[] $values() {
      return new StudioShapeService.Finish[] {SOLID, GRADIENT, AURORA};
    }
  }

  public static final class Shape {
    public String id = UUID.randomUUID().toString();
    public String name = "Shape";
    public String parent = "";
    public StudioShapeService.ShapeKind kind = StudioShapeService.ShapeKind.PANEL;
    public float x = 80.0F;
    public float y = 64.0F;
    public float width = 160.0F;
    public float height = 96.0F;
    public float radius = 20.0F;
    public float opacity = 1.0F;
    public float fontSize = 24.0F;
    public float stroke = 8.0F;
    public int color = -4344587;
    public int color2 = -8795693;
    public StudioShapeService.Finish finish = StudioShapeService.Finish.SOLID;
    public StudioShapeService.TextAlignment alignment = StudioShapeService.TextAlignment.LEFT;
    public String text = "Hello, {player.name}";
    public boolean visible = true;
    public boolean locked;
  }

  public enum ShapeKind {
    PANEL,
    ELLIPSE,
    RING,
    DIAMOND,
    TEXT,
    BAR,
    GROUP;

    private static StudioShapeService.ShapeKind[] $values() {
      return new StudioShapeService.ShapeKind[] {PANEL, ELLIPSE, RING, DIAMOND, TEXT, BAR, GROUP};
    }
  }

  public enum TextAlignment {
    LEFT,
    CENTER,
    RIGHT;

    private static StudioShapeService.TextAlignment[] $values() {
      return new StudioShapeService.TextAlignment[] {LEFT, CENTER, RIGHT};
    }
  }
}
