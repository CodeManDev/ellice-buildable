package dev.felix.ellice.plugin.api;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public final class ApiRegisterService implements ApiOperationHandler {
  @Override
  public void register(Globals globals) {
    LuaTable luaTable = new LuaTable();
    luaTable.set(
        "categories",
        new ZeroArgFunction() {
          public LuaValue call() {
            LuaTable luaTable = new LuaTable();
            ModuleFeatureType[] moduleFeatureTypes =
                ModuleFeatureType.navigation().toArray(ModuleFeatureType[]::new);

            for (int index = 0; index < moduleFeatureTypes.length; index++) {
              LuaTable currentLuaTable = new LuaTable();
              currentLuaTable.set(
                  "name", LuaValue.valueOf(moduleFeatureTypes[index].displayName()));
              currentLuaTable.set(
                  "description", LuaValue.valueOf(moduleFeatureTypes[index].description()));
              luaTable.set(index + 1, currentLuaTable);
            }

            return luaTable;
          }
        });
    luaTable.set(
        "byCategory",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            ModuleFeatureType moduleFeatureType =
                ApiRegisterService.createModuleFeatureType(luaValue.tojstring());
            if (moduleFeatureType == null) {
              return new LuaTable();
            }

            List items =
                CoreIsInitializedHandler.get().modules().byCategory(moduleFeatureType).toList();
            LuaTable luaTable = new LuaTable();

            for (int index = 0; index < items.size(); index++) {
              Module module = (Module) items.get(index);
              LuaTable currentLuaTable = new LuaTable();
              currentLuaTable.set("name", LuaValue.valueOf(module.name()));
              currentLuaTable.set("enabled", LuaValue.valueOf(module.isEnabled()));
              currentLuaTable.set("description", LuaValue.valueOf(module.description()));
              currentLuaTable.set("category", LuaValue.valueOf(module.category().displayName()));
              luaTable.set(index + 1, currentLuaTable);
            }

            return luaTable;
          }
        });
    luaTable.set(
        "toggle",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            CoreIsInitializedHandler.get()
                .modules()
                .get(luaValue.tojstring())
                .ifPresent(Module::toggle);
            return LuaValue.NIL;
          }
        });
    luaTable.set(
        "isEnabled",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            return LuaValue.valueOf(
                CoreIsInitializedHandler.get()
                    .modules()
                    .get(luaValue.tojstring())
                    .map(Module::isEnabled)
                    .orElse(false));
          }
        });
    luaTable.set(
        "settings",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            Optional result = CoreIsInitializedHandler.get().modules().get(luaValue.tojstring());
            if (result.isEmpty()) {
              return new LuaTable();
            }

            LuaTable luaTable = new LuaTable();
            List items = ((Module) result.get()).settings();

            for (int index = 0; index < items.size(); index++) {
              luaTable.set(
                  index + 1,
                  ApiRegisterService.describeSetting((ModuleSetting<?>) items.get(index)));
            }

            return luaTable;
          }
        });
    luaTable.set(
        "setSetting",
        new VarArgFunction() {
          public Varargs invoke(Varargs varargs) {
            String text = varargs.arg1().tojstring();
            String currentText = varargs.arg(2).tojstring();
            LuaValue luaValue = varargs.arg(3);
            CoreIsInitializedHandler.get()
                .modules()
                .get(text)
                .ifPresent(
                    item -> {
                      for (ModuleSetting moduleSetting : item.settings()) {
                        if (moduleSetting.name().equals(currentText)) {
                          ApiRegisterService.applySetting(moduleSetting, luaValue);
                          break;
                        }
                      }
                    });
            return LuaValue.NIL;
          }
        });
    luaTable.set(
        "keybind",
        new OneArgFunction() {
          public LuaValue call(LuaValue luaValue) {
            int value = CoreIsInitializedHandler.get().keybinds().getKey(luaValue.tojstring());
            LuaTable luaTable = new LuaTable();
            luaTable.set("keyCode", LuaValue.valueOf(value));
            luaTable.set("keyName", LuaValue.valueOf(ModuleBindService.keyName(value)));
            return luaTable;
          }
        });
    luaTable.set(
        "setKeybind",
        new TwoArgFunction() {
          public LuaValue call(LuaValue luaValue, LuaValue currentLuaValue) {
            String text = luaValue.tojstring();
            int value = currentLuaValue.toint();
            if (value > 0) {
              CoreIsInitializedHandler.get().keybinds().bind(value, text);
            } else {
              CoreIsInitializedHandler.get().keybinds().unbind(text);
            }

            if (CoreIsInitializedHandler.get().config() != null) {
              CoreIsInitializedHandler.get().config().save();
            }

            return LuaValue.NIL;
          }
        });
    globals.set("modules", luaTable);
  }

  static void applySetting(ModuleSetting<?> moduleSetting, LuaValue luaValue) {
    if (moduleSetting instanceof ModuleSetting.Bool bool) {
      bool.set(luaValue.toboolean());
    } else if (moduleSetting instanceof ModuleSetting.Number number) {
      number.set(luaValue.tofloat());
    } else if (moduleSetting instanceof ModuleSetting.Mode mode) {
      mode.set(luaValue.tojstring());
    } else if (moduleSetting instanceof ModuleSetting.Color color) {
      color.set(luaValue.toint());
    } else if (moduleSetting instanceof ModuleSetting.Curve curve) {
      ModuleSetting.CurveValue curveValue = createCurveValue(luaValue);
      if (curveValue != null) {
        curve.set(curveValue);
      }
    } else if (moduleSetting instanceof ModuleSetting.Keybind keybind) {
      keybind.set(luaValue.toint());
    } else if (moduleSetting instanceof ModuleSetting.Text text) {
      text.set(luaValue.tojstring());
    } else if (moduleSetting instanceof ModuleSetting.Range range && luaValue.istable()) {
      range.set(luaValue.get(1).tofloat(), luaValue.get(2).tofloat());
    } else if (moduleSetting instanceof ModuleSetting.MultiSelect multiSelect
        && luaValue.istable()) {
      LinkedHashSet linkedHashSet = new LinkedHashSet();
      LuaValue currentLuaValue = LuaValue.NIL;
      LuaTable luaTable = luaValue.checktable();

      while (true) {
        Varargs varargs = luaTable.next(currentLuaValue);
        if ((currentLuaValue = varargs.arg1()).isnil()) {
          multiSelect.set(linkedHashSet);
          break;
        }

        linkedHashSet.add(varargs.arg(2).tojstring());
      }
    }
  }

  static LuaTable describeSetting(ModuleSetting<?> moduleSetting) {
    LuaTable luaTable = new LuaTable();
    luaTable.set("name", LuaValue.valueOf(moduleSetting.name()));
    luaTable.set("description", LuaValue.valueOf(moduleSetting.description()));
    moduleSetting
        .category()
        .ifPresent(
            item -> {
              luaTable.set("category", LuaValue.valueOf(item.qualifiedName()));
              luaTable.set("categoryDescription", LuaValue.valueOf(item.description()));
            });
    if (moduleSetting instanceof ModuleSetting.Bool currentBool) {
      luaTable.set("type", "bool");
      luaTable.set("value", LuaValue.valueOf((Boolean) currentBool.get()));
    } else if (moduleSetting instanceof ModuleSetting.Number currentNumber) {
      luaTable.set("type", "number");
      luaTable.set("value", LuaValue.valueOf(((Float) currentNumber.get()).floatValue()));
      luaTable.set("min", LuaValue.valueOf(currentNumber.min()));
      luaTable.set("max", LuaValue.valueOf(currentNumber.max()));
      luaTable.set("step", LuaValue.valueOf(currentNumber.step()));
    } else if (moduleSetting instanceof ModuleSetting.Mode currentMode) {
      luaTable.set("type", "mode");
      luaTable.set("value", LuaValue.valueOf((String) currentMode.get()));
      LuaTable currentLuaTable = new LuaTable();

      for (int index = 0; index < currentMode.options().length; index++) {
        currentLuaTable.set(index + 1, LuaValue.valueOf(currentMode.options()[index]));
      }

      luaTable.set("options", currentLuaTable);
    } else if (moduleSetting instanceof ModuleSetting.Color currentColor) {
      luaTable.set("type", "color");
      luaTable.set("value", LuaValue.valueOf((Integer) currentColor.get()));
    } else if (moduleSetting instanceof ModuleSetting.Curve currentCurve) {
      luaTable.set("type", "curve");
      luaTable.set("value", createLuaTable((ModuleSetting.CurveValue) currentCurve.get()));
    } else if (moduleSetting instanceof ModuleSetting.Keybind currentKeybind) {
      luaTable.set("type", "keybind");
      luaTable.set("value", LuaValue.valueOf((Integer) currentKeybind.get()));
      luaTable.set("keyName", LuaValue.valueOf(currentKeybind.keyName()));
    } else if (moduleSetting instanceof ModuleSetting.Text currentText) {
      luaTable.set("type", "text");
      luaTable.set("value", LuaValue.valueOf((String) currentText.get()));
      luaTable.set("maxLength", LuaValue.valueOf(currentText.maxLength()));
    } else if (moduleSetting instanceof ModuleSetting.Range currentRange) {
      luaTable.set("type", "range");
      luaTable.set("low", LuaValue.valueOf(currentRange.low()));
      luaTable.set("high", LuaValue.valueOf(currentRange.high()));
      luaTable.set("min", LuaValue.valueOf(currentRange.min()));
      luaTable.set("max", LuaValue.valueOf(currentRange.max()));
      luaTable.set("step", LuaValue.valueOf(currentRange.step()));
    } else if (moduleSetting instanceof ModuleSetting.MultiSelect multiSelect) {
      luaTable.set("type", "multiselect");
      LuaTable nextLuaTable = new LuaTable();

      for (int currentIndex = 0; currentIndex < multiSelect.options().length; currentIndex++) {
        nextLuaTable.set(currentIndex + 1, LuaValue.valueOf(multiSelect.options()[currentIndex]));
      }

      luaTable.set("options", nextLuaTable);
      LuaTable previousLuaTable = new LuaTable();
      int nextIndex = 1;

      for (String nextText : (Iterable<String>) (Iterable<?>) ((Set) multiSelect.get())) {
        previousLuaTable.set(nextIndex++, LuaValue.valueOf(nextText));
      }

      luaTable.set("selected", previousLuaTable);
    }

    return luaTable;
  }

  private static LuaTable createLuaTable(ModuleSetting.CurveValue curveValue) {
    LuaTable luaTable = new LuaTable();
    luaTable.set("type", createText3(curveValue.type()));
    if (curveValue.type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
      luaTable.set("x1", LuaValue.valueOf(curveValue.x1()));
      luaTable.set("y1", LuaValue.valueOf(curveValue.y1()));
      luaTable.set("x2", LuaValue.valueOf(curveValue.x2()));
      luaTable.set("y2", LuaValue.valueOf(curveValue.y2()));
    } else if (curveValue.type() == ModuleSetting.CurveType.STEPS) {
      luaTable.set("count", LuaValue.valueOf(curveValue.steps()));
      luaTable.set("jump", createText4(curveValue.stepMode()));
    }

    return luaTable;
  }

  private static ModuleSetting.CurveValue createCurveValue(LuaValue luaValue) {
    if (!luaValue.istable()) {
      return null;
    }

    LuaTable luaTable = luaValue.checktable();
    String text = createText2(createText(luaTable.rawget("type")));
    if (text == null) {
      return null;
    }

    try {
      return switch (text) {
        case "linear" -> ModuleSetting.CurveValue.linear();
        case "cubic-bezier" -> {
          Float value = calculateValue(luaTable.rawget("x1"));
          Float currentValue = calculateValue(luaTable.rawget("y1"));
          Float nextValue = calculateValue(luaTable.rawget("x2"));
          Float previousValue = calculateValue(luaTable.rawget("y2"));
          yield value != null && currentValue != null && nextValue != null && previousValue != null
              ? ModuleSetting.CurveValue.cubicBezier(value, currentValue, nextValue, previousValue)
              : null;
        }
        case "steps" -> {
          Integer currentCount = createInteger(luaTable.rawget("count"));
          ModuleSetting.StepMode stepMode = createStepMode(luaTable.rawget("jump"));
          yield currentCount != null && stepMode != null
              ? ModuleSetting.CurveValue.steps(currentCount, stepMode)
              : null;
        }
        default -> null;
      };
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    }
  }

  private static Float calculateValue(LuaValue luaValue) {
    if (luaValue.type() != 3) {
      return null;
    }

    double doubleValue = luaValue.todouble();
    if (!Double.isFinite(doubleValue)) {
      return null;
    }

    float value = (float) doubleValue;
    return Float.isFinite(value) ? value : null;
  }

  private static Integer createInteger(LuaValue luaValue) {
    if (luaValue.type() != 3) {
      return null;
    }

    double doubleValue = luaValue.todouble();
    return Double.isFinite(doubleValue)
            && doubleValue == Math.rint(doubleValue)
            && !(doubleValue < -2.147483648E9)
            && !(doubleValue > 2.147483647E9)
        ? (int) doubleValue
        : null;
  }

  private static ModuleSetting.StepMode createStepMode(LuaValue luaValue) {
    String text = createText2(createText(luaValue));
    if (text == null) {
      return null;
    }

    return switch (text) {
      case "jump-start" -> ModuleSetting.StepMode.JUMP_START;
      case "jump-end" -> ModuleSetting.StepMode.JUMP_END;
      case "jump-none" -> ModuleSetting.StepMode.JUMP_NONE;
      case "jump-both" -> ModuleSetting.StepMode.JUMP_BOTH;
      default -> null;
    };
  }

  private static String createText(LuaValue luaValue) {
    return luaValue.type() == 4 ? luaValue.tojstring() : null;
  }

  private static String createText2(String text) {
    return text != null && !text.isBlank()
        ? text.trim().toLowerCase(Locale.ROOT).replace('_', '-').replace(' ', '-')
        : null;
  }

  private static String createText3(ModuleSetting.CurveType curveType) {
    return curveType.name().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  private static String createText4(ModuleSetting.StepMode stepMode) {
    return stepMode.name().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  private static ModuleFeatureType createModuleFeatureType(String text) {
    return ModuleFeatureType.find(text).orElse(null);
  }
}
