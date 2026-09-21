package dev.felix.ellice.module;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ModuleSetting<T> {
  private final String text;
  private String text2;
  private String text3 = "";
  protected T value;
  private Consumer<T> consumer;
  private ModuleNameService moduleNameService;
  private final List<ModuleSetting.Dependency> items = new ArrayList<>();
  private final Set<ModuleSetting<?>> values2 = Collections.newSetFromMap(new IdentityHashMap<>());
  private final List<Consumer<? super T>> items2 = new ArrayList<>();
  private final List<Runnable> items3 = new ArrayList<>();

  protected ModuleSetting(String currentText, T t) {
    this.text = Objects.requireNonNull(currentText, "name");
    this.value = (T) t;
  }

  public String name() {
    return this.text;
  }

  public String displayName() {
    return this.text2 == null ? this.text : this.text2;
  }

  public ModuleSetting<T> label(String text) {
    if (text != null && !text.isBlank()) {
      this.text2 = text;
      return this;
    } else {
      throw new IllegalArgumentException("Blank setting label");
    }
  }

  public String description() {
    return this.text3;
  }

  public T get() {
    return this.value;
  }

  public Optional<ModuleNameService> category() {
    return Optional.ofNullable(this.moduleNameService);
  }

  public final <S extends ModuleSetting<T>> S description(String text) {
    Objects.requireNonNull(text, "description");
    String currentText = text.trim();
    if (currentText.isEmpty()) {
      throw new IllegalArgumentException("Setting description must not be blank");
    }

    this.text3 = currentText;
    return (S) this;
  }

  public void set(T t) {
    if (!Objects.equals(this.value, t)) {
      this.value = (T) t;
      if (this.consumer != null) {
        this.consumer.accept((T) t);
      }

      for (Consumer currentConsumer : List.copyOf(this.items2)) {
        currentConsumer.accept(t);
      }

      this.updateState2();
      updateState();
    }
  }

  private static void updateState() {
    try {
      if (LocalConfigRepository.isSavesSuppressed()) {
        return;
      }

      if (CoreIsInitializedHandler.isReady()) {
        LocalConfigRepository localConfigRepository = CoreIsInitializedHandler.get().config();
        if (localConfigRepository != null) {
          localConfigRepository.save();
        }
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn("Setting autoSave failed", exception);
    }
  }

  public ModuleSetting<T> onChange(Consumer<T> currentConsumer) {
    this.consumer = currentConsumer;
    return this;
  }

  public AutoCloseable onValueChanged(Consumer<? super T> consumer) {
    Objects.requireNonNull(consumer, "listener");
    this.items2.add(consumer);
    return () -> this.items2.remove(consumer);
  }

  public AutoCloseable onValueChanged(Runnable runnable) {
    Objects.requireNonNull(runnable, "listener");
    return this.onValueChanged(item -> runnable.run());
  }

  public AutoCloseable onStateChanged(Runnable runnable) {
    Objects.requireNonNull(runnable, "listener");
    this.items3.add(runnable);
    return () -> this.items3.remove(runnable);
  }

  public final boolean isVisible() {
    return this.checkCondition2(collectValues());
  }

  public final boolean isActive() {
    return this.checkCondition3(collectValues());
  }

  public ModuleSetting<T> visibleWhen(ModuleSetting.Bool bool) {
    return this.visibleWhen(bool, Boolean.TRUE::equals);
  }

  public <V> ModuleSetting<T> visibleWhen(
      ModuleSetting<V> moduleSetting, Predicate<? super V> predicate) {
    this.createValue(moduleSetting, predicate, ModuleSetting.Dependency.Kind.VISIBILITY);
    return this;
  }

  public ModuleSetting<T> activeWhen(ModuleSetting.Bool bool) {
    return this.activeWhen(bool, Boolean.TRUE::equals);
  }

  public <V> ModuleSetting<T> activeWhen(
      ModuleSetting<V> moduleSetting, Predicate<? super V> predicate) {
    this.createValue(moduleSetting, predicate, ModuleSetting.Dependency.Kind.ACTIVITY);
    return this;
  }

  public final List<ModuleSetting.Dependency> dependencies() {
    return List.copyOf(this.items);
  }

  void assignCategory(ModuleNameService moduleName) {
    Objects.requireNonNull(moduleName, "category");
    if (this.moduleNameService != null && this.moduleNameService != moduleName) {
      throw new IllegalStateException(
          "Setting '" + this.text + "' is already assigned to category " + this.moduleNameService);
    }

    this.moduleNameService = moduleName;
  }

  private <V> void createValue(
      ModuleSetting<V> moduleSetting,
      Predicate<? super V> predicate,
      ModuleSetting.Dependency.Kind currentKind) {
    Objects.requireNonNull(moduleSetting, "controller");
    Objects.requireNonNull(predicate, "condition");
    Objects.requireNonNull(currentKind, "kind");
    if (moduleSetting != this && !moduleSetting.checkCondition(this, collectValues())) {
      this.items.add(
          new ModuleSetting.Dependency(
              moduleSetting, currentKind, item -> predicate.test(createValue2(item))));
      moduleSetting.values2.add(this);
      this.updateState2();
    } else {
      throw new IllegalArgumentException(
          "Setting dependency cycle: '" + this.text + "' -> '" + moduleSetting.text + "'");
    }
  }

  private boolean checkCondition(ModuleSetting<?> moduleSetting, Set<ModuleSetting<?>> values) {
    if (!values.add(this)) {
      return false;
    }

    for (ModuleSetting.Dependency dependency : this.items) {
      if (dependency.moduleNameService2 == moduleSetting
          || dependency.moduleNameService2.checkCondition(moduleSetting, values)) {
        return true;
      }
    }

    return false;
  }

  private boolean checkCondition2(Set<ModuleSetting<?>> values) {
    if (!values.add(this)) {
      return false;
    }

    try {
      for (ModuleSetting.Dependency dependency : this.items) {
        if (dependency.moduleKind == ModuleSetting.Dependency.Kind.VISIBILITY
            && (!dependency.moduleNameService2.checkCondition2(values) || !dependency.matches())) {
          return false;
        }
      }

      return true;
    } finally {
      values.remove(this);
    }
  }

  private boolean checkCondition3(Set<ModuleSetting<?>> values) {
    if (!values.add(this)) {
      return false;
    }

    try {
      if (!this.isVisible()) {
        return false;
      }

      for (ModuleSetting.Dependency dependency : this.items) {
        if (dependency.moduleKind == ModuleSetting.Dependency.Kind.ACTIVITY
            && (!dependency.moduleNameService2.checkCondition3(values) || !dependency.matches())) {
          return false;
        }
      }

      return true;
    } finally {
      values.remove(this);
    }
  }

  private void updateState2() {
    this.updateState3(collectValues());
  }

  private void updateState3(Set<ModuleSetting<?>> values) {
    if (values.add(this)) {
      for (Runnable runnable : List.copyOf(this.items3)) {
        runnable.run();
      }

      for (ModuleSetting moduleSetting : Set.copyOf(this.values2)) {
        moduleSetting.updateState3(values);
      }
    }
  }

  private static Set<ModuleSetting<?>> collectValues() {
    return Collections.newSetFromMap(new IdentityHashMap<>());
  }

  private static <V> V createValue2(Object value) {
    return (V) value;
  }

  private static float calculateValue(
      float value, float currentValue, float nextValue, float previousValue) {
    double doubleValue = Math.max(currentValue, Math.min((double) nextValue, (double) value));
    if (previousValue > 0.0F) {
      double currentDoubleValue = Math.floor((doubleValue - currentValue) / previousValue + 0.5);
      doubleValue = currentValue + currentDoubleValue * previousValue;
      double nextDoubleValue =
          0.5 * (Math.ulp(currentValue) + currentDoubleValue * Math.ulp(previousValue));
      if (currentValue <= 0.0F
          && nextValue >= 0.0F
          && doubleValue != 0.0
          && Math.abs(doubleValue) <= nextDoubleValue
          && new BigDecimal(Float.toString(currentValue))
                  .add(
                      new BigDecimal(Float.toString(previousValue))
                          .multiply(BigDecimal.valueOf(currentDoubleValue)))
                  .signum()
              == 0) {
        doubleValue = 0.0;
      }

      doubleValue = Math.max(currentValue, Math.min(nextValue, doubleValue));
    }

    float sourceValue = (float) doubleValue;
    return sourceValue == 0.0F ? 0.0F : sourceValue;
  }

  public static class Bool extends ModuleSetting<Boolean> {
    public Bool(String text, boolean enabled) {
      super(text, enabled);
    }

    public void toggle() {
      this.set(!(Boolean) this.get());
    }
  }

  public static class Color extends ModuleSetting<Integer> {
    public Color(String text, int value) {
      super(text, value);
    }

    public void set(Integer value) {
      if (value != null) {
        super.set(value);
      }
    }
  }

  public static class Curve extends ModuleSetting<ModuleSetting.CurveValue> {
    public Curve(String text, ModuleSetting.CurveValue curveValue) {
      super(text, Objects.requireNonNull(curveValue, "default curve"));
    }

    public Curve(String text) {
      this(text, ModuleSetting.CurveValue.linear());
    }

    public void set(ModuleSetting.CurveValue curveValue) {
      if (curveValue != null) {
        super.set(curveValue);
      }
    }

    public void linear() {
      this.set(ModuleSetting.CurveValue.linear());
    }

    public void cubicBezier(float value, float currentValue, float nextValue, float previousValue) {
      this.set(ModuleSetting.CurveValue.cubicBezier(value, currentValue, nextValue, previousValue));
    }

    public void steps(int value, ModuleSetting.StepMode stepMode) {
      this.set(ModuleSetting.CurveValue.steps(value, stepMode));
    }
  }

  public enum CurveType {
    LINEAR,
    CUBIC_BEZIER,
    STEPS;

    private static ModuleSetting.CurveType[] $values() {
      return new ModuleSetting.CurveType[] {LINEAR, CUBIC_BEZIER, STEPS};
    }
  }

  public record CurveValue(
      ModuleSetting.CurveType type,
      float x1,
      float y1,
      float x2,
      float y2,
      int steps,
      ModuleSetting.StepMode stepMode)
      implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final float MIN_BEZIER_Y = -2.0F;
    public static final float MAX_BEZIER_Y = 2.0F;
    public static final int MIN_STEPS = 1;
    public static final int MAX_STEPS = 100;

    public CurveValue(
        ModuleSetting.CurveType type,
        float x1,
        float y1,
        float x2,
        float y2,
        int steps,
        ModuleSetting.StepMode stepMode) {
      Objects.requireNonNull(type, "type");
      Objects.requireNonNull(stepMode, "stepMode");
      if (Float.isFinite(x1) && Float.isFinite(y1) && Float.isFinite(x2) && Float.isFinite(y2)) {
        switch (type) {
          case LINEAR:
            x1 = 0.0F;
            y1 = 0.0F;
            x2 = 1.0F;
            y2 = 1.0F;
            steps = 1;
            stepMode = ModuleSetting.StepMode.JUMP_END;
            break;
          case CUBIC_BEZIER:
            x1 = calculateValue5(x1, 0.0F, 1.0F);
            y1 = calculateValue5(y1, -2.0F, 2.0F);
            x2 = calculateValue5(x2, 0.0F, 1.0F);
            y2 = calculateValue5(y2, -2.0F, 2.0F);
            steps = 1;
            stepMode = ModuleSetting.StepMode.JUMP_END;
            break;
          case STEPS:
            updateState4(steps, stepMode);
            x1 = 0.0F;
            y1 = 0.0F;
            x2 = 1.0F;
            y2 = 1.0F;
        }

        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.steps = steps;
        this.stepMode = stepMode;
      } else {
        throw new IllegalArgumentException("Curve control points must be finite");
      }
    }

    public static ModuleSetting.CurveValue linear() {
      return new ModuleSetting.CurveValue(
          ModuleSetting.CurveType.LINEAR,
          0.0F,
          0.0F,
          1.0F,
          1.0F,
          1,
          ModuleSetting.StepMode.JUMP_END);
    }

    public static ModuleSetting.CurveValue cubicBezier(
        float value, float currentValue, float nextValue, float previousValue) {
      return new ModuleSetting.CurveValue(
          ModuleSetting.CurveType.CUBIC_BEZIER,
          value,
          currentValue,
          nextValue,
          previousValue,
          1,
          ModuleSetting.StepMode.JUMP_END);
    }

    public static ModuleSetting.CurveValue steps(int value, ModuleSetting.StepMode stepMode) {
      return new ModuleSetting.CurveValue(
          ModuleSetting.CurveType.STEPS, 0.0F, 0.0F, 1.0F, 1.0F, value, stepMode);
    }

    public float sample(float value) {
      if (!Float.isFinite(value)) {
        throw new IllegalArgumentException("Curve progress must be finite");
      }

      float currentValue = calculateValue5(value, 0.0F, 1.0F);

      return switch (this.type) {
        case LINEAR -> currentValue;
        case CUBIC_BEZIER -> this.calculateValue2(currentValue);
        case STEPS -> this.calculateValue3(currentValue);
      };
    }

    private float calculateValue2(float value) {
      if (value <= 0.0F) {
        return 0.0F;
      }

      if (value >= 1.0F) {
        return 1.0F;
      }

      double doubleValue = 0.0;
      double currentDoubleValue = 1.0;

      for (int index = 0; index < 22; index++) {
        double nextDoubleValue = (doubleValue + currentDoubleValue) * 0.5;
        if (calculateValue4(nextDoubleValue, this.x1, this.x2) < value) {
          doubleValue = nextDoubleValue;
        } else {
          currentDoubleValue = nextDoubleValue;
        }
      }

      double previousDoubleValue = (doubleValue + currentDoubleValue) * 0.5;
      return (float) calculateValue4(previousDoubleValue, this.y1, this.y2);
    }

    private float calculateValue3(float value) {
      int index = (int) Math.floor(value * this.steps);
      if (this.stepMode == ModuleSetting.StepMode.JUMP_START
          || this.stepMode == ModuleSetting.StepMode.JUMP_BOTH) {
        index++;
      }
      int currentValue =
          switch (this.stepMode) {
            case JUMP_NONE -> this.steps - 1;
            case JUMP_BOTH -> this.steps + 1;
            default -> this.steps;
          };
      return calculateValue5((float) index / currentValue, 0.0F, 1.0F);
    }

    private static double calculateValue4(double doubleValue, float value, float currentValue) {
      double currentDoubleValue = 1.0 - doubleValue;
      return 3.0 * currentDoubleValue * currentDoubleValue * doubleValue * value
          + 3.0 * currentDoubleValue * doubleValue * doubleValue * currentValue
          + doubleValue * doubleValue * doubleValue;
    }

    private static void updateState4(int value, ModuleSetting.StepMode stepMode) {
      if (value < 1 || value > 100) {
        throw new IllegalArgumentException("Curve step count must be between 1 and 100");
      }

      if (stepMode == ModuleSetting.StepMode.JUMP_NONE && value < 2) {
        throw new IllegalArgumentException("JUMP_NONE requires at least two steps");
      }
    }

    private static float calculateValue5(float value, float currentValue, float nextValue) {
      float previousValue = Math.max(currentValue, Math.min(nextValue, value));
      return previousValue == 0.0F ? 0.0F : previousValue;
    }
  }

  public static final class Dependency {
    private final ModuleSetting<?> moduleNameService2;
    private final ModuleSetting.Dependency.Kind moduleKind;
    private final Predicate<Object> predicate;

    private Dependency(
        ModuleSetting<?> moduleSetting,
        ModuleSetting.Dependency.Kind kind,
        Predicate<Object> currentPredicate) {
      this.moduleNameService2 = moduleSetting;
      this.moduleKind = kind;
      this.predicate = currentPredicate;
    }

    public ModuleSetting<?> controller() {
      return this.moduleNameService2;
    }

    public ModuleSetting.Dependency.Kind kind() {
      return this.moduleKind;
    }

    public boolean matches() {
      return this.predicate.test(this.moduleNameService2.get());
    }

    public enum Kind {
      VISIBILITY,
      ACTIVITY;

      private static ModuleSetting.Dependency.Kind[] $values() {
        return new ModuleSetting.Dependency.Kind[] {VISIBILITY, ACTIVITY};
      }
    }
  }

  public static class Keybind extends ModuleSetting<Integer> {
    public Keybind(String text, int value) {
      super(text, value);
    }

    public Keybind(String text) {
      super(text, -1);
    }

    public boolean isBound() {
      return (Integer) this.get() != -1;
    }

    public void unbind() {
      this.set(-1);
    }

    public String keyName() {
      int value = (Integer) this.get();
      if (value == -1) {
        return "None";
      }

      if (value >= 65 && value <= 90) {
        return String.valueOf((char) value);
      }

      if (value >= 48 && value <= 57) {
        return String.valueOf((char) value);
      }

      return switch (value) {
        case 32 -> "Space";
        case 256 -> "Esc";
        case 257 -> "Enter";
        case 258 -> "Tab";
        case 259 -> "Backspace";
        case 261 -> "Delete";
        case 262 -> "Right";
        case 263 -> "Left";
        case 264 -> "Down";
        case 265 -> "Up";
        case 340 -> "LShift";
        case 341 -> "LCtrl";
        case 342 -> "LAlt";
        case 344 -> "RShift";
        case 345 -> "RCtrl";
        case 346 -> "RAlt";
        default -> "Key" + value;
      };
    }
  }

  public static class Mode extends ModuleSetting<String> {
    private final String[] text4;
    private final Map<String, int[]> text5 = new LinkedHashMap<>();

    public ModuleSetting.Mode preview(String text, int... ints) {
      if (this.accepts(text) && ints != null && ints.length != 0 && ints.length <= 6) {
        this.text5.put(text, (int[]) ints.clone());
        return this;
      } else {
        throw new IllegalArgumentException("A preview needs a valid option and 1–6 colors");
      }
    }

    public int[] previewColors(String text) {
      int[] ints = this.text5.get(text);
      return ints == null ? new int[0] : (int[]) ints.clone();
    }

    public boolean hasPreviews() {
      return !this.text5.isEmpty();
    }

    public Mode(String text, String[] strings, String currentText) {
      super(text, createText(strings, currentText));
      this.text4 = (String[]) strings.clone();
    }

    public String[] options() {
      return (String[]) this.text4.clone();
    }

    public boolean accepts(String text) {
      if (text == null) {
        return false;
      }

      for (String currentText : this.text4) {
        if (currentText.equals(text)) {
          return true;
        }
      }

      return false;
    }

    public void set(String text) {
      if (this.accepts(text)) {
        super.set(text);
      }
    }

    private static String createText(String[] strings, String text) {
      if (strings != null && strings.length != 0) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();

        for (String currentText : strings) {
          if (currentText == null || currentText.isBlank()) {
            throw new IllegalArgumentException("Mode options must not be null or blank");
          }

          if (!linkedHashSet.add(currentText)) {
            throw new IllegalArgumentException("Duplicate mode option: " + currentText);
          }
        }

        if (text != null && linkedHashSet.contains(text)) {
          return text;
        } else {
          throw new IllegalArgumentException("Default mode must be one of its options");
        }
      } else {
        throw new IllegalArgumentException("Mode options must not be null or empty");
      }
    }
  }

  public static class MultiSelect extends ModuleSetting<Set<String>> {
    private final String[] text6;

    public MultiSelect(String text, String[] strings, String... currentStrings) {
      super(text, new LinkedHashSet<>(Arrays.asList(currentStrings)));
      this.text6 = strings;
    }

    public String[] options() {
      return this.text6;
    }
  }

  public static class Number extends ModuleSetting<Float> {
    private final float value2;
    private final float value3;
    private final float value4;
    private boolean enabled;

    public ModuleSetting.Number exactInput() {
      this.enabled = true;
      return this;
    }

    public boolean usesExactInput() {
      return this.enabled;
    }

    public Number(
        String text, float value, float currentValue, float nextValue, float previousValue) {
      super(text, calculateValue6(value, currentValue, nextValue, previousValue));
      this.value2 = currentValue;
      this.value3 = nextValue;
      this.value4 = previousValue;
    }

    public float min() {
      return this.value2;
    }

    public float max() {
      return this.value3;
    }

    public float step() {
      return this.value4;
    }

    public void set(Float value) {
      if (value != null && Float.isFinite(value)) {
        super.set(calculateValue7(value, this.value2, this.value3, this.value4));
      }
    }

    private static float calculateValue6(
        float currentValue, float nextValue, float previousValue, float sourceValue) {
      updateState5(nextValue, previousValue, sourceValue);
      if (!Float.isFinite(currentValue)) {
        throw new IllegalArgumentException("Default number value must be finite");
      } else {
        return calculateValue7(currentValue, nextValue, previousValue, sourceValue);
      }
    }

    private static float calculateValue7(
        float value, float currentValue, float nextValue, float previousValue) {
      return ModuleSetting.calculateValue(value, currentValue, nextValue, previousValue);
    }

    private static void updateState5(float value, float currentValue, float nextValue) {
      if (!Float.isFinite(value)
          || !Float.isFinite(currentValue)
          || !Float.isFinite(nextValue)
          || Float.compare(value, currentValue) > 0
          || nextValue < 0.0F) {
        throw new IllegalArgumentException(
            "Number bounds and step must be finite, min <= max, and step >= 0");
      }
    }
  }

  public static class Range extends ModuleSetting<ModuleSetting.RangeValue> {
    private final float value5;
    private final float value6;
    private final float value7;

    public Range(
        String text,
        float value,
        float currentValue,
        float nextValue,
        float previousValue,
        float sourceValue) {
      super(text, createRangeValue(value, currentValue, nextValue, previousValue, sourceValue));
      this.value5 = nextValue;
      this.value6 = previousValue;
      this.value7 = sourceValue;
    }

    public float min() {
      return this.value5;
    }

    public float max() {
      return this.value6;
    }

    public float step() {
      return this.value7;
    }

    public float low() {
      return ((ModuleSetting.RangeValue) this.get()).low();
    }

    public float high() {
      return ((ModuleSetting.RangeValue) this.get()).high();
    }

    public void set(float value, float currentValue) {
      if (Float.isFinite(value) && Float.isFinite(currentValue)) {
        super.set(createRangeValue2(value, currentValue, this.value5, this.value6, this.value7));
      }
    }

    public void set(ModuleSetting.RangeValue rangeValue) {
      if (rangeValue != null) {
        this.set(rangeValue.low(), rangeValue.high());
      }
    }

    public void setLow(float value) {
      if (Float.isFinite(value)) {
        float currentValue = calculateValue8(value, this.value5, this.value6, this.value7);
        ModuleSetting.RangeValue rangeValue = (ModuleSetting.RangeValue) this.get();
        super.set(
            new ModuleSetting.RangeValue(
                Math.min(currentValue, rangeValue.high()), rangeValue.high()));
      }
    }

    public void setHigh(float value) {
      if (Float.isFinite(value)) {
        float currentValue = calculateValue8(value, this.value5, this.value6, this.value7);
        ModuleSetting.RangeValue rangeValue = (ModuleSetting.RangeValue) this.get();
        super.set(
            new ModuleSetting.RangeValue(
                rangeValue.low(), Math.max(rangeValue.low(), currentValue)));
      }
    }

    private static ModuleSetting.RangeValue createRangeValue(
        float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      updateState6(nextValue, previousValue, sourceValue);
      if (Float.isFinite(value) && Float.isFinite(currentValue)) {
        return createRangeValue2(value, currentValue, nextValue, previousValue, sourceValue);
      } else {
        throw new IllegalArgumentException("Default range values must be finite");
      }
    }

    private static ModuleSetting.RangeValue createRangeValue2(
        float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      float targetValue =
          calculateValue8(Math.min(value, currentValue), nextValue, previousValue, sourceValue);
      float inputValue =
          calculateValue8(Math.max(value, currentValue), nextValue, previousValue, sourceValue);
      return new ModuleSetting.RangeValue(
          Math.min(targetValue, inputValue), Math.max(targetValue, inputValue));
    }

    private static float calculateValue8(
        float value, float currentValue, float nextValue, float previousValue) {
      return ModuleSetting.calculateValue(value, currentValue, nextValue, previousValue);
    }

    private static void updateState6(float value, float currentValue, float nextValue) {
      if (!Float.isFinite(value)
          || !Float.isFinite(currentValue)
          || !Float.isFinite(nextValue)
          || Float.compare(value, currentValue) > 0
          || nextValue < 0.0F) {
        throw new IllegalArgumentException(
            "Range bounds and step must be finite, min <= max, and step >= 0");
      }
    }
  }

  public record RangeValue(float low, float high) {
    public RangeValue(float low, float high) {
      if (!Float.isFinite(low) || !Float.isFinite(high)) {
        throw new IllegalArgumentException("Range values must be finite");
      }

      if (Float.compare(low, high) > 0) {
        throw new IllegalArgumentException("Range low value must not exceed high value");
      }

      this.low = low;
      this.high = high;
    }
  }

  public enum StepMode {
    JUMP_START,
    JUMP_END,
    JUMP_NONE,
    JUMP_BOTH;

    private static ModuleSetting.StepMode[] $values() {
      return new ModuleSetting.StepMode[] {JUMP_START, JUMP_END, JUMP_NONE, JUMP_BOTH};
    }
  }

  public static class Text extends ModuleSetting<String> {
    private final int count2;

    public Text(String text, String currentText, int value) {
      super(text, currentText);
      this.count2 = value;
    }

    public Text(String text, String currentText) {
      this(text, currentText, 64);
    }

    public int maxLength() {
      return this.count2;
    }

    public void set(String text) {
      if (text != null && text.length() > this.count2) {
        text = text.substring(0, this.count2);
      }

      super.set(text);
    }
  }
}
