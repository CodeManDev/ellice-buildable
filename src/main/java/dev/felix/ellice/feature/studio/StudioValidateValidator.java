package dev.felix.ellice.feature.studio;

import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class StudioValidateValidator {
  public static List<String> validate(
      final StudioShapeService studioShapeService, final boolean b) {
    final ArrayList coll = new ArrayList();
    if (studioShapeService == null) {
      coll.add("Project is missing.");
      return coll;
    }
    if (studioShapeService.version != 1) {
      coll.add("Unsupported project version.");
    }
    try {
      if (!UUID.fromString(studioShapeService.id).toString().equals(studioShapeService.id)) {
        coll.add("Invalid project identifier.");
      }
    } catch (final Exception ex) {
      coll.add("Invalid project identifier.");
    }
    if (studioShapeService.name == null
        || studioShapeService.name.isBlank()
        || studioShapeService.name.length() > 48) {
      coll.add("Use a project name with 1–48 characters.");
    }
    if (!checkCondition2(
            studioShapeService.width,
            Float.intBitsToFloat(1107296256),
            Float.intBitsToFloat(1153957888))
        || !checkCondition2(
            studioShapeService.height,
            Float.intBitsToFloat(1107296256),
            Float.intBitsToFloat(1148846080))) {
      coll.add("Artboard size is out of range.");
    }
    if (studioShapeService.shapes == null || studioShapeService.blocks == null) {
      coll.add("Project contents are missing.");
      return coll;
    }
    if (studioShapeService.shapes.size() > 64 || studioShapeService.blocks.size() > 96) {
      coll.add("Project limit: 64 shapes and 96 blocks.");
      return coll;
    }
    final HashSet set = new HashSet();
    for (StudioShapeService.Shape shape : studioShapeService.shapes) {
      if (shape == null
          || shape.id == null
          || shape.kind == null
          || shape.finish == null
          || shape.alignment == null
          || shape.name == null
          || shape.parent == null
          || shape.text == null) {
        coll.add("Invalid shape.");
      } else {
        if (shape.id.isBlank() || !set.add(shape.id)) {
          coll.add("Invalid or duplicate shape identifier.");
        }
        if (shape.id.length() > 64
            || shape.name.length() > 48
            || shape.text.length() > 256
            || shape.parent.length() > 64) {
          coll.add("Shape text is too long.");
        }
        if (checkCondition2(
                shape.x, Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296))
            && checkCondition2(
                shape.y, Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296))
            && checkCondition2(
                shape.width, Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1157234688))
            && checkCondition2(
                shape.height, Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1157234688))
            && checkCondition2(shape.opacity, 0.0f, 1.0f)
            && checkCondition2(shape.radius, 0.0f, Float.intBitsToFloat(1140457472))
            && checkCondition2(
                shape.fontSize, Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1126170624))
            && checkCondition2(shape.stroke, 1.0f, Float.intBitsToFloat(1120403456))) {
          continue;
        }
        coll.add("Invalid dimensions for " + shape.name);
      }
    }
    if (!coll.isEmpty()) {
      return coll;
    }
    for (final StudioShapeService.Shape shape2 : studioShapeService.shapes) {
      final HashSet set2 = new HashSet();
      StudioShapeService.Shape shape3 = shape2;
      while (!shape3.parent.isEmpty()) {
        if (!set2.add(shape3.id)) {
          coll.add("Groups cannot contain themselves.");
          break;
        }
        shape3 = studioShapeService.shape(shape3.parent);
        if (shape3 == null || shape3.kind != StudioShapeService.ShapeKind.GROUP) {
          coll.add("A shape has an invalid parent group.");
          break;
        }
      }
    }
    for (final StudioShapeService.Block block : studioShapeService.blocks) {
      if (block == null
          || block.id == null
          || block.kind == null
          || block.inputs == null
          || block.defaults == null
          || block.text == null
          || block.target == null) {
        coll.add("Invalid block.");
      } else {
        if (block.id.isBlank() || !set.add(block.id)) {
          coll.add("Invalid or duplicate block identifier.");
        }
        if (!checkCondition2(
                block.x, Float.intBitsToFloat(-969179136), Float.intBitsToFloat(1178304512))
            || !checkCondition2(
                block.y, Float.intBitsToFloat(-969179136), Float.intBitsToFloat(1178304512))
            || !checkCondition2(
                block.number, Float.intBitsToFloat(-943501312), Float.intBitsToFloat(1203982336))
            || block.id.length() > 64
            || block.text.length() > 256
            || block.target.length() > 80
            || block.inputs.size() > 8
            || block.defaults.size() > 8) {
          coll.add("Invalid block properties.");
        }
        for (final Map.Entry entry : block.defaults.entrySet()) {
          if (block.kind.port((String) entry.getKey()) == null
              || entry.getValue() == null
              || !checkCondition2(
                  (float) entry.getValue(),
                  Float.intBitsToFloat(-943501312),
                  Float.intBitsToFloat(1203982336))) {
            coll.add("Invalid input default.");
          }
        }
      }
    }
    if (!coll.isEmpty()) {
      return coll;
    }
    for (StudioShapeService.Block block2 : studioShapeService.blocks) {
      for (Map.Entry entry2 : block2.inputs.entrySet()) {
        final StudioMode.Port port = block2.kind.port((String) entry2.getKey());
        final StudioShapeService.Block block3 =
            studioShapeService.block((String) entry2.getValue());
        if (port == null || block3 == null || block3.kind.output != port.type()) {
          coll.add("An incompatible connection reaches " + block2.kind.title);
        }
      }
      if (b && block2.kind.shapeOutput() && studioShapeService.shape(block2.target) == null) {
        coll.add("Choose a shape for " + block2.kind.title);
      }
      if (b && block2.kind == StudioMode.MODULE_GATE && block2.target.isBlank()) {
        coll.add("Choose a module for the switch action.");
      }
    }
    final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    final Iterator<StudioShapeService.Block> iterator7 = studioShapeService.blocks.iterator();
    while (iterator7.hasNext()) {
      if (mhkyqdmqzra0(studioShapeService, iterator7.next(), hashMap)) {
        coll.add("This graph contains a connection loop.");
        break;
      }
    }
    if (b
        && studioShapeService.shapes.isEmpty()
        && studioShapeService.blocks.stream()
            .noneMatch(block4 -> block4.kind == StudioMode.MODULE_GATE)) {
      coll.add("Add a shape or module action first.");
    }
    return (List<String>) List.copyOf((Collection<?>) coll);
  }

  private static boolean mhkyqdmqzra0(
      final StudioShapeService studioShapeService,
      final StudioShapeService.Block block,
      final Map<String, Integer> map) {
    final int intValue = map.getOrDefault(block.id, 0);
    if (intValue == 1) {
      return true;
    }
    if (intValue == 2) {
      return false;
    }
    map.put(block.id, 1);
    final Iterator<String> iterator = block.inputs.values().iterator();
    while (iterator.hasNext()) {
      final StudioShapeService.Block block2 = studioShapeService.block(iterator.next());
      if (block2 != null && mhkyqdmqzra0(studioShapeService, block2, map)) {
        return true;
      }
    }
    map.put(block.id, 2);
    return false;
  }

  public static String connectionError(
      final StudioShapeService studioShapeService,
      final String s,
      final String s2,
      final String s3) {
    final StudioShapeService.Block block = studioShapeService.block(s);
    final StudioShapeService.Block block2 = studioShapeService.block(s2);
    if (block == null || block2 == null) {
      return "Choose two existing blocks.";
    }
    final StudioMode.Port port = block2.kind.port(s3);
    if (port == null || block.kind.output != port.type()) {
      return "Port types must match: numbers, conditions, text or colors.";
    }
    final String s4 = block2.inputs.put(s3, s);
    final boolean mhkyqdmqzra0 =
        mhkyqdmqzra0(studioShapeService, block2, new HashMap<String, Integer>());
    if (s4 == null) {
      block2.inputs.remove(s3);
    } else {
      block2.inputs.put(s3, s4);
    }
    return mhkyqdmqzra0 ? "That connection would create a loop." : null;
  }

  public static Frame evaluate(
      final StudioShapeService studioShapeService,
      final LayoutOperationHandler layoutOperationHandler,
      final float n) {
    final Evaluation evaluation = new Evaluation(studioShapeService, layoutOperationHandler, n);
    final LinkedHashMap linkedHashMap = new LinkedHashMap();
    for (final StudioShapeService.Shape shape : studioShapeService.shapes) {
      final Appearance appearance = new Appearance(shape);
      appearance.text = layoutOperationHandler.interpolate(appearance.text);
      linkedHashMap.put(shape.id, appearance);
    }
    final LinkedHashMap linkedHashMap2 = new LinkedHashMap();
    for (final StudioShapeService.Block block : studioShapeService.blocks) {
      if (block.kind.output != null) {
        evaluation.value(block);
      } else if (block.kind == StudioMode.MODULE_GATE) {
        linkedHashMap2.put(block.target, evaluation.input(block, "Enabled").truth);
      } else {
        final Appearance appearance2 = (Appearance) linkedHashMap.get(block.target);
        if (appearance2 == null) {
          continue;
        }
        if (block.kind.port("When") != null && !evaluation.input(block, "When").truth) {
          continue;
        }
        final Value input = evaluation.input(block, "Value");
        switch (block.kind) {
          case FILL:
            {
              appearance2.progress = clamp(input.number, 0.0f, 1.0f);
              continue;
            }
          case TINT:
            {
              appearance2.color = input.color;
              continue;
            }
          case SHOW:
            {
              appearance2.visible = (appearance2.visible && input.truth);
              continue;
            }
          case CAPTION:
            {
              appearance2.text = input.text;
              continue;
            }
          case SCALE:
            {
              appearance2.scale =
                  clamp(
                      input.number,
                      Float.intBitsToFloat(1028443341),
                      Float.intBitsToFloat(1082130432));
              continue;
            }
          case OPACITY:
            {
              appearance2.opacity = clamp(input.number, 0.0f, 1.0f);
              continue;
            }
          case POSITION_X:
            {
              appearance2.x =
                  clamp(
                      input.number,
                      Float.intBitsToFloat(-981860352),
                      Float.intBitsToFloat(1165623296));
              continue;
            }
          case POSITION_Y:
            {
              appearance2.y =
                  clamp(
                      input.number,
                      Float.intBitsToFloat(-981860352),
                      Float.intBitsToFloat(1165623296));
              continue;
            }
        }
      }
    }
    return new Frame(linkedHashMap, evaluation.cache, linkedHashMap2);
  }

  public static int mix(final int n, final int n2, float clamp) {
    clamp = clamp(clamp, 0.0f, 1.0f);
    int n3 = 0;
    final int[] array = {24, 16, 8, 0};
    for (int length = array.length, i = 0; i < length; ++i) {
      final int n4 = array[i];
      n3 |= Math.round((n >>> n4 & 0xFF) * (1.0f - clamp) + (n2 >>> n4 & 0xFF) * clamp) << n4;
    }
    return n3;
  }

  public static float clamp(final float n, final float a, final float a2) {
    return Math.max(a, Math.min(a2, mevwdwqvjs3m(n)));
  }

  private static float mevwdwqvjs3m(final float f) {
    return Float.isFinite(f) ? f : 0.0f;
  }

  private static boolean checkCondition2(final float f, final float n, final float n2) {
    return Float.isFinite(f) && f >= n && f <= n2;
  }

  private static final class Evaluation {
    final StudioShapeService p;
    final LayoutOperationHandler vars;
    final float seconds;
    final Map<String, Value> cache;
    final Set<String> visiting;

    Evaluation(final StudioShapeService p3, final LayoutOperationHandler vars, final float n) {
      this.cache = new HashMap<String, Value>();
      this.visiting = new HashSet<String>();
      this.p = p3;
      this.vars = vars;
      this.seconds = StudioValidateValidator.mevwdwqvjs3m(n);
    }

    Value input(final StudioShapeService.Block block, final String key) {
      final StudioMode.Port port = block.kind.port(key);
      final StudioShapeService.Block block2 = this.p.block(block.inputs.get(key));
      if (block2 != null && block2.kind.output == port.type()) {
        return this.value(block2);
      }
      final float floatValue = block.defaults.getOrDefault(key, port.fallback());
      return switch (port.type()) {
        default -> throw new MatchException(null, null);
        case NUMBER -> Value.number(floatValue);
        case BOOLEAN -> Value.bool(floatValue != 0.0f);
        case TEXT -> Value.text(block.text);
        case COLOR -> Value.color(block.color);
      };
    }

    Value value(final StudioShapeService.Block block) {
      final Value value = this.cache.get(block.id);
      if (value != null) {
        return value;
      }
      if (!this.visiting.add(block.id)) {
        return Value.number(0.0f);
      }
      final Value value3 =
          switch (block.kind) {
            case HEALTH -> Value.number(this.vars.number("player.health"));
            case HUNGER -> Value.number(this.vars.number("player.hunger"));
            case FPS -> Value.number(this.vars.number("client.fps"));
            case PING -> Value.number(this.vars.number("server.ping"));
            case TIME -> Value.number(this.seconds);
            case NUMBER -> Value.number(block.number);
            case TEXT -> Value.text(this.vars.interpolate(block.text));
            case COLOR -> Value.color(block.color);
            case ADD -> Value.number(this.input(block, "A").number + this.input(block, "B").number);
            case MULTIPLY ->
                Value.number(this.input(block, "A").number * this.input(block, "B").number);
            case DIVIDE ->
                Value.number(
                    (Math.abs(this.input(block, "B").number) < Float.intBitsToFloat(925353388))
                        ? 0.0f
                        : (this.input(block, "A").number / this.input(block, "B").number));
            case CLAMP ->
                Value.number(
                    StudioValidateValidator.clamp(this.input(block, "Value").number, 0.0f, 1.0f));
            case WAVE ->
                Value.number(
                    (1.0f
                            + (float)
                                Math.sin(
                                    this.input(block, "Time").number
                                        * this.input(block, "Speed").number
                                        * Double.longBitsToDouble(4614256656552045848L)
                                        * Double.longBitsToDouble(4611686018427387904L)))
                        * Float.intBitsToFloat(1056964608));
            case LESS -> Value.bool(this.input(block, "A").number < this.input(block, "B").number);
            case GREATER ->
                Value.bool(this.input(block, "A").number > this.input(block, "B").number);
            case AND -> Value.bool(this.input(block, "A").truth && this.input(block, "B").truth);
            case NOT -> Value.bool(!this.input(block, "Value").truth);
            case FORMAT ->
                Value.text(
                    this.vars.interpolate(
                        block.text.replace("{value}", this.input(block, "Value").caption())));
            case MIX_COLOR ->
                Value.color(
                    StudioValidateValidator.mix(
                        this.input(block, "From").color,
                        this.input(block, "To").color,
                        this.input(block, "Amount").number));
            default -> Value.number(0.0f);
          };
      this.visiting.remove(block.id);
      this.cache.put(block.id, value3);
      return value3;
    }
  }

  public record Value(StudioMode.Type type, float number, String text, int color, boolean truth) {
    static Value number(final float n) {
      return new Value(
          StudioMode.Type.NUMBER, StudioValidateValidator.mevwdwqvjs3m(n), "", 0, false);
    }

    static Value text(final String s) {
      return new Value(StudioMode.Type.TEXT, 0.0f, (s == null) ? "" : s, 0, false);
    }

    static Value color(final int n) {
      return new Value(StudioMode.Type.COLOR, 0.0f, "", n, false);
    }

    static Value bool(final boolean b) {
      return new Value(StudioMode.Type.BOOLEAN, 0.0f, "", 0, b);
    }

    public String caption() {
      return switch (this.type) {
        default -> throw new MatchException(null, null);
        case NUMBER ->
            String.format(
                Locale.ROOT,
                (Math.abs(this.number - Math.round(this.number))
                        < Double.longBitsToDouble(4562254508917369340L))
                    ? "%.0f"
                    : "%.2f",
                this.number);
        case TEXT -> this.text;
        case COLOR -> String.format(Locale.ROOT, "#%06X", this.color & 0xFFFFFF);
        case BOOLEAN -> this.truth ? "True" : "False";
      };
    }
  }

  public static final class Appearance {
    public float x;
    public float y;
    public float scale;
    public float opacity;
    public float progress;
    public int color;
    public boolean visible;
    public String text;

    Appearance(final StudioShapeService.Shape shape) {
      this.scale = 1.0f;
      this.progress = 1.0f;
      this.x = shape.x;
      this.y = shape.y;
      this.opacity = shape.opacity;
      this.color = shape.color;
      this.visible = shape.visible;
      this.text = shape.text;
    }
  }

  public record Frame(
      Map<String, Appearance> shapes,
      Map<String, Value> values,
      Map<String, Boolean> moduleGates) {}
}
