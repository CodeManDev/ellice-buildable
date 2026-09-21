package dev.felix.ellice.module;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ModuleIdService {
  private final String text;
  private final String text2;
  private final String text3;
  private final String text4;
  private final boolean enabled;
  private final Map<String, Object> text5;

  public ModuleIdService(
      Module var1, String var2, String var3, String var4, boolean var5, Map<String, Object> var6) {
    this.text = var1.name();
    this.text2 = Objects.requireNonNull(var2);
    this.text3 = Objects.requireNonNull(var3);
    this.text4 = Objects.requireNonNull(var4);
    this.enabled = var5;
    LinkedHashMap var7 = new LinkedHashMap();
    var6.forEach(
        (var2x, var3x) ->
            var7.put(var2x, mfscoipvqvdj(createModuleNameService2(var1, var2x), var3x)));
    this.text5 = Collections.unmodifiableMap(var7);
  }

  public String id() {
    return this.text2;
  }

  public String name() {
    return this.text3;
  }

  public String description() {
    return this.text4;
  }

  public boolean recommended() {
    return this.enabled;
  }

  public Map<String, Object> values() {
    return this.text5;
  }

  static boolean isTunable(ModuleSetting<?> var0) {
    return var0 instanceof ModuleSetting.Bool
        || var0 instanceof ModuleSetting.Number
        || var0 instanceof ModuleSetting.Mode
        || var0 instanceof ModuleSetting.Color
        || var0 instanceof ModuleSetting.Curve
        || var0 instanceof ModuleSetting.Range
        || var0 instanceof ModuleSetting.MultiSelect;
  }

  static Object snapshot(Object var0) {
    return var0 instanceof Set var1 ? Set.copyOf(var1) : var0;
  }

  public List<ModuleIdService.Change> changes(Module var1) {
    this.mdhvk9fqvzt(var1);
    ArrayList var2 = new ArrayList();
    this.text5.forEach(
        (var2x, var3) -> {
          Object var4 = createModuleNameService2(var1, var2x).get();
          if (!Objects.equals(var4, var3)) {
            var2.add(new ModuleIdService.Change(var2x, snapshot(var4), var3));
          }
        });
    return List.copyOf(var2);
  }

  public boolean matches(Module var1) {
    return this.text.equals(var1.name()) && this.changes(var1).isEmpty();
  }

  public void apply(Module var1) {
    this.mdhvk9fqvzt(var1);
    LinkedHashMap<ModuleSetting<?>, Object> var2 = new LinkedHashMap<>();
    this.text5.forEach(
        (var2x, var3x) -> {
          ModuleSetting var4x = createModuleNameService2(var1, var2x);
          Object var5 = mfscoipvqvdj(var4x, var3x);
          if (!Objects.equals(var4x.get(), var5)) {
            var2.put(var4x, var5);
          }
        });
    if (!var2.isEmpty()) {
      LinkedHashMap<ModuleSetting<?>, Object> var3 = new LinkedHashMap<>();
      var2.keySet().forEach(var1x -> var3.put(var1x, snapshot(var1x.get())));
      boolean var4 = LocalConfigRepository.isSavesSuppressed();
      LocalConfigRepository.runWithoutSaving(
          () -> {
            try {
              var2.forEach(ModuleIdService::updateState2);
            } catch (RuntimeException var3x) {
              var3.forEach(
                  (var1xx, var2x) -> {
                    try {
                      updateState2(var1xx, var2x);
                    } catch (RuntimeException var4x) {
                      var3x.addSuppressed(var4x);
                    }
                  });
              throw var3x;
            }
          });
      if (!var4
          && CoreIsInitializedHandler.isReady()
          && CoreIsInitializedHandler.get().config() != null) {
        CoreIsInitializedHandler.get().config().save();
      }
    }
  }

  private void mdhvk9fqvzt(Module var1) {
    if (!this.text.equals(var1.name())) {
      throw new IllegalArgumentException("Preset belongs to " + this.text);
    }
  }

  private static ModuleSetting<?> createModuleNameService2(Module var0, String var1) {
    return var0.settings().stream()
        .filter(var1x -> var1x.name().equals(var1))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(var0.name() + ": unknown preset setting " + var1));
  }

  private static Object mfscoipvqvdj(ModuleSetting<?> var0, Object var1) {
    if (var0 instanceof ModuleSetting.Bool && var1 instanceof Boolean) {
      return var1;
    } else if (var0 instanceof ModuleSetting.Color && var1 instanceof Integer) {
      return var1;
    } else if (var0 instanceof ModuleSetting.Mode var2
        && var1 instanceof String var3
        && var2.accepts(var3)) {
      return var1;
    } else {
      if (var0 instanceof ModuleSetting.Number var5 && var1 instanceof Number var8) {
        float var4 = var8.floatValue();
        if (Float.isFinite(var4) && var4 >= var5.min() && var4 <= var5.max()) {
          return new ModuleSetting.Number(var5.name(), var4, var5.min(), var5.max(), var5.step())
              .get();
        }
      }

      if (var0 instanceof ModuleSetting.Range var6
          && var1 instanceof ModuleSetting.RangeValue var9
          && var9.low() >= var6.min()
          && var9.high() <= var6.max()
          && var9.low() <= var9.high()) {
        return new ModuleSetting.Range(
                var6.name(), var9.low(), var9.high(), var6.min(), var6.max(), var6.step())
            .get();
      } else if (var0 instanceof ModuleSetting.Curve && var1 instanceof ModuleSetting.CurveValue) {
        return var1;
      } else if (var0 instanceof ModuleSetting.MultiSelect var7
          && var1 instanceof Set var10
          && Arrays.asList(var7.options()).containsAll(var10)) {
        return Set.copyOf(var10);
      } else {
        throw new IllegalArgumentException("Invalid preset value for " + var0.name() + ": " + var1);
      }
    }
  }

  private static void updateState2(ModuleSetting var0, Object var1) {
    var0.set(var1 instanceof Set var2 ? new LinkedHashSet(var2) : var1);
  }

  public static String label(Object var0) {
    if (var0 instanceof Boolean var6) {
      return var6 ? "On" : "Off";
    } else if (var0 instanceof Float var5) {
      return String.format(Locale.ROOT, "%.3f", var5).replaceAll("0+$", "").replaceAll("\\.$", "");
    } else if (var0 instanceof Integer var4) {
      return String.format(Locale.ROOT, "#%08X", var4);
    } else if (var0 instanceof ModuleSetting.RangeValue var3) {
      return label(var3.low()) + "\u2013" + label(var3.high());
    } else if (var0 instanceof ModuleSetting.CurveValue var2) {
      return var2.type().name();
    } else {
      return var0 instanceof Set var1
          ? String.join(", ", var1.stream().map(Object::toString).sorted().toList())
          : String.valueOf(var0);
    }
  }

  public record Change(String setting, Object previous, Object value) {}
}
