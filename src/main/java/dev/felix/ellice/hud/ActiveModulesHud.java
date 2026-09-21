package dev.felix.ellice.hud;

import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.text.TextData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;

public final class ActiveModulesHud implements ComponentOperationHandler {
  public static final float FONT_SIZE = 10.0F;
  private static final float value = 18.0F;
  private static final SceneEaseHandler sceneEaseHandler =
      new SceneEaseHandler.Spring(44.0F, 484.0F);
  private static final SceneEaseHandler sceneEaseHandler2 =
      new SceneEaseHandler.Spring(48.0F, 576.0F);
  private static final SceneEaseHandler sceneEaseHandler3 =
      new SceneEaseHandler.Spring(32.0F, 320.0F);
  private static final SceneEaseHandler sceneEaseHandler4 =
      new SceneEaseHandler.Spring(40.0F, 400.0F);
  private static final SceneEaseHandler sceneEaseHandler5 =
      new SceneEaseHandler.Spring(60.0F, 900.0F);
  private static final float value2 = 0.018F;
  private static final float value3 = 0.072F;
  private final Map<String, ActiveModulesHud.Entry> text2 = new LinkedHashMap<>();
  private Set<String> text3 = Set.of();
  private ComponentThemeService componentThemeService;
  private float value4 = 1.0F;
  private float value5 = 200.0F;
  private boolean enabled = true;
  private boolean enabled2 = true;
  private int count = Integer.MIN_VALUE;
  private boolean enabled3;

  public void fontRevision(int value) {
    if (this.count != value) {
      this.count = value;
      this.enabled3 = true;
    }
  }

  public void update(
      List<String> items,
      float value,
      float currentValue,
      boolean currentEnabled,
      boolean nextEnabled,
      ToDoubleFunction<String> toDoubleFunction) {
    Set currentValues =
        Set.copyOf(items.stream().filter(item -> item != null && !item.isBlank()).toList());
    value = Float.isFinite(value) ? Math.max(0.01F, value) : 1.0F;
    currentValue = Float.isFinite(currentValue) ? Math.max(1.0F, currentValue) : 200.0F;
    int nextValue = !this.enabled3 && this.value4 == value && this.value5 == currentValue ? 0 : 1;
    this.enabled3 = false;
    int previousValue =
        nextValue == 0
                && this.text3.equals(currentValues)
                && this.enabled == currentEnabled
                && this.enabled2 == nextEnabled
            ? 0
            : 1;
    this.text3 = currentValues;
    this.value4 = value;
    this.value5 = currentValue;
    this.enabled = currentEnabled;
    this.enabled2 = nextEnabled;

    for (String text : (Iterable<String>) (Iterable<?>) (currentValues)) {
      if (!this.text2.containsKey(text)) {
        this.text2.put(text, new ActiveModulesHud.Entry(text));
        previousValue = 1;
      }
    }

    for (ActiveModulesHud.Entry entry : this.text2.values()) {
      entry.shown = currentValues.contains(entry.name);
      if (nextValue != 0 || !entry.measured) {
        double doubleValue = toDoubleFunction.applyAsDouble(entry.name);
        entry.textWidth =
            Double.isFinite(doubleValue)
                ? (float) Math.max(0.0, Math.min(doubleValue, 3.4028234663852886E38))
                : this.value5;
        entry.measured = true;
      }
    }

    if (this.text2.values().removeIf(item -> !item.shown && (!nextEnabled || item.slot == null))) {
      previousValue = 1;
    }
    if (previousValue != 0) {
      int index = 0;

      for (ActiveModulesHud.Entry currentEntry : this.collectValues()) {
        if (currentEntry.slot == null) {
          currentEntry.entranceDelay = Math.min(0.072F, index++ * 0.018F);
          currentEntry.initialWidth = currentEntry.neighbourWidth(currentEntry.fullWidth() * 0.78F);
        }
      }
    }

    if (previousValue != 0 && this.componentThemeService != null) {
      this.componentThemeService.invalidate();
    }

    for (ActiveModulesHud.Entry nextEntry : this.text2.values()) {
      nextEntry.retarget();
    }
  }

  private List<ActiveModulesHud.Entry> collectValues() {
    return this.text2.values().stream()
        .sorted(
            Comparator.<ActiveModulesHud.Entry>comparingDouble(item -> item.textWidth)
                .reversed()
                .thenComparing(item -> item.name, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(item -> item.name))
        .toList();
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
    this.componentThemeService = componentTheme;
    ArrayList arrayList = new ArrayList();
    this.collectValues().forEach(item -> arrayList.add(this.createComponentKeyService(item)));
    ComponentStyleService currentSize =
        ComponentStyleService.style()
            .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
            .padding(8.0F)
            .align(ScenePctService.Align.START)
            .clip(true)
            .build();
    return ComponentBoxService.<HudComponent>node(
            "connected-module-list",
            HudComponent::new,
            item -> {
              currentSize.apply(item);
              item.direction(ScenePctService.Direction.COLUMN)
                  .id("module-list.rows")
                  .pointerEvents(false);
              item.appearance(this.value4, this.enabled);
            },
            arrayList.toArray(ComponentKeyService[]::new))
        .key("module-list")
        .onUnmount(item -> this.componentThemeService = null);
  }

  private ComponentKeyService<?> createComponentKeyService(ActiveModulesHud.Entry entry) {
    float value = entry.fullWidth();
    ComponentKeyService<?> currentSize =
        ComponentBoxService.panel(
                ComponentStyleService.style()
                    .size(
                        LayoutOperationHandler.px(value),
                        LayoutOperationHandler.px(18.0F * this.value4))
                    .padding(0.0F, 8.0F * this.value4)
                    .align(ScenePctService.Align.CENTER)
                    .shrink(0.0F)
                    .build(),
                currentItem -> {
                  entry.body = currentItem;
                  currentItem
                      .id("module-list.item." + entry.name)
                      .direction(ScenePctService.Direction.ROW)
                      .backgroundColor(0)
                      .pointerEvents(false);
                },
                ComponentBoxService.text(
                        entry.name,
                        item ->
                            item.fontSize(10.0F * this.value4)
                                .color(MaterialIsLightService.ON_SURFACE)
                                .style(
                                    TextData.shadow(
                                        0.7F * this.value4, 0.7F * this.value4, -2013265920))
                                .maxLines(1)
                                .overflow(SceneTextService.Overflow.ELLIPSIS)
                                .minWidth(0.0F)
                                .flex(1.0F)
                                .pointerEvents(false))
                    .key("name"))
            .key("body")
            .onMount(
                item -> {
                  item.opacity(0.0F).translateX(-5.0F * this.value4).translateY(1.5F * this.value4);
                  entry.retarget();
                })
            .onUnmount(item -> entry.body = null);
    ComponentStyleService currentWidth =
        ComponentStyleService.style()
            .width(
                LayoutOperationHandler.px(
                    entry.slot == null
                        ? entry.initialWidth
                        : MotionColorsContainer.Floats.WIDTH.get(entry.slot)))
            .height(
                LayoutOperationHandler.px(
                    entry.slot == null
                        ? 0.0F
                        : MotionColorsContainer.Floats.HEIGHT.get(entry.slot)))
            .shrink(0.0F)
            .build();
    return ComponentBoxService.<ActiveModuleSlot>node(
            "module-list-slot",
            ActiveModuleSlot::new,
            item -> {
              currentWidth.apply(item);
              entry.slot = item;
              item.contentSize(value, 18.0F * this.value4);
              item.id("module-list.slot." + entry.name)
                  .direction(ScenePctService.Direction.COLUMN)
                  .pointerEvents(false);
            },
            currentSize)
        .key(entry.name)
        .onUnmount(item -> entry.slot = null);
  }

  private final class Entry {
    final String name;
    float textWidth;
    float initialWidth;
    float entranceDelay;
    boolean shown;
    boolean measured;
    ActiveModuleSlot slot;
    SceneCornerRadiusService body;
    float targetHeight = Float.NaN;
    float targetWidth = Float.NaN;
    float targetX = Float.NaN;
    float targetY = Float.NaN;
    Boolean targetShown;
    Boolean motionEnabled;

    Entry(String text) {
      this.name = text;
    }

    void retarget() {
      if (this.body != null && this.slot != null) {
        float value = this.shown ? 18.0F * ActiveModulesHud.this.value4 : 0.0F;
        int currentValue = this.targetShown != null && this.targetShown == this.shown ? 0 : 1;
        float nextValue =
            this.shown
                ? this.fullWidth()
                : (currentValue != 0
                    ? this.neighbourWidth(this.fullWidth() * 0.78F)
                    : this.targetWidth);
        float previousValue = this.shown ? 0.0F : -4.0F * ActiveModulesHud.this.value4;
        float sourceValue = this.shown ? 0.0F : -1.0F * ActiveModulesHud.this.value4;
        int targetValue =
            this.motionEnabled != null && this.motionEnabled == ActiveModulesHud.this.enabled2
                ? 0
                : 1;
        int inputValue = this.targetShown == null && this.shown ? 1 : 0;
        int outputValue =
            this.targetShown == null
                    || currentValue == 0
                    || !(Math.abs(
                                MotionColorsContainer.Floats.HEIGHT.get(this.slot)
                                    - this.targetHeight)
                            > 0.01F)
                        && !(Math.abs(
                                MotionColorsContainer.Floats.OPACITY.get(this.body)
                                    - (this.targetShown ? 1.0F : 0.0F))
                            > 0.01F)
                ? 0
                : 1;
        float resultValue = inputValue != 0 ? this.entranceDelay : 0.0F;
        float candidateValue = !this.shown && outputValue == 0 ? 0.045F : 0.0F;
        if (targetValue != 0 || this.targetHeight != value) {
          if (ActiveModulesHud.this.enabled2) {
            MotionAnimateService.animate(
                this.slot,
                MotionColorsContainer.Floats.HEIGHT,
                value,
                ActiveModulesHud.sceneEaseHandler,
                resultValue + candidateValue,
                this.shown ? null : this::finishExit);
          } else {
            this.updateState(
                this.slot,
                MotionColorsContainer.Floats.HEIGHT,
                value,
                ActiveModulesHud.sceneEaseHandler,
                0.0F);
          }
        }

        if (targetValue != 0 || this.targetWidth != nextValue) {
          this.updateState(
              this.slot,
              MotionColorsContainer.Floats.WIDTH,
              nextValue,
              ActiveModulesHud.sceneEaseHandler2,
              resultValue + candidateValue);
        }

        if (targetValue != 0 || this.targetX != previousValue) {
          this.updateState(
              this.body,
              MotionColorsContainer.Floats.TRANSLATE_X,
              previousValue,
              ActiveModulesHud.sceneEaseHandler3,
              resultValue);
        }

        if (targetValue != 0 || this.targetY != sourceValue) {
          this.updateState(
              this.body,
              MotionColorsContainer.Floats.TRANSLATE_Y,
              sourceValue,
              ActiveModulesHud.sceneEaseHandler3,
              resultValue);
        }

        if (targetValue != 0 || currentValue != 0) {
          this.updateState(
              this.body,
              MotionColorsContainer.Floats.OPACITY,
              this.shown ? 1.0F : 0.0F,
              this.shown ? ActiveModulesHud.sceneEaseHandler4 : ActiveModulesHud.sceneEaseHandler5,
              inputValue != 0 ? resultValue + 0.025F : 0.0F);
        }

        this.targetHeight = value;
        this.targetWidth = nextValue;
        this.targetX = previousValue;
        this.targetY = sourceValue;
        this.targetShown = this.shown;
        this.motionEnabled = ActiveModulesHud.this.enabled2;
      }
    }

    float fullWidth() {
      return Math.min(
          ActiveModulesHud.this.value5, this.textWidth + 16.0F * ActiveModulesHud.this.value4);
    }

    float neighbourWidth(float value) {
      return ActiveModulesHud.this.text2.values().stream()
          .filter(item -> item != this && item.shown && item.slot != null)
          .min(Comparator.comparingDouble(item -> Math.abs(item.textWidth - this.textWidth)))
          .map(ActiveModulesHud.Entry::fullWidth)
          .orElse(value);
    }

    void finishExit() {
      if (!this.shown
          && ActiveModulesHud.this.text2.remove(this.name, this)
          && ActiveModulesHud.this.componentThemeService != null) {
        ActiveModulesHud.this.componentThemeService.invalidate();
      }
    }

    private void updateState(
        ScenePctService<?> scenePct,
        MotionFiniteService motionFinite,
        float value,
        SceneEaseHandler sceneEase,
        float currentValue) {
      if (ActiveModulesHud.this.enabled2) {
        MotionAnimateService.animate(scenePct, motionFinite, value, sceneEase, currentValue);
      } else {
        MotionAnimateService.cancel(scenePct, motionFinite);
        motionFinite.set(scenePct, value);
      }
    }
  }
}
