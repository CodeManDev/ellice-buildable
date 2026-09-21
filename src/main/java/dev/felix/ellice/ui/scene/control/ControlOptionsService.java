package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ControlOptionsService extends ScenePctService<ControlOptionsService> {
  private String[] text = new String[0];
  private Set<String> text2 = Set.of();
  private int count = -10262799;
  private int count2 = 553648127;
  private int count3 = -3355444;
  private float value = 7.0F;
  private float value2 = 3.0F;
  private float value3 = 4.0F;
  private float value4 = 2.0F;
  public Consumer<Set<String>> onChange;

  public ControlOptionsService options(String... strings) {
    this.text = strings == null ? new String[0] : (String[]) strings.clone();
    return this;
  }

  public ControlOptionsService selected(Set<String> values) {
    this.text2 = values == null ? Set.of() : new LinkedHashSet<>(values);
    return this;
  }

  public Set<String> selectedValues() {
    return Set.copyOf(this.text2);
  }

  public ControlOptionsService activeColor(int color) {
    this.count = color;
    return this;
  }

  public ControlOptionsService inactiveColor(int color) {
    this.count2 = color;
    return this;
  }

  public ControlOptionsService textColor(int color) {
    this.count3 = color;
    return this;
  }

  public ControlOptionsService fontSize(float currentValue) {
    this.value = currentValue;
    return this;
  }

  public ControlOptionsService onChange(Consumer<Set<String>> consumer) {
    this.onChange = consumer;
    return this;
  }

  public ControlOptionsService build() {
    this.clearChildren();
    this.direction(ScenePctService.Direction.ROW);
    this.gap(this.value4);
    this.align(ScenePctService.Align.CENTER);

    for (String currentText : this.text) {
      SceneCornerRadiusService sceneCornerRadius = new SceneCornerRadiusService();
      boolean enabled = this.text2.contains(currentText);
      sceneCornerRadius
          .direction(ScenePctService.Direction.ROW)
          .align(ScenePctService.Align.CENTER)
          .justify(ScenePctService.Justify.CENTER)
          .padding(this.value3)
          .minWidth(0.0F)
          .flexShrink(1.0F)
          .cornerRadius(this.value2)
          .backgroundColor(enabled ? this.count : this.count2)
          .hoverBackground(enabled ? this.count : 822083583)
          .clip(true)
          .cursorStyle(ScenePctService.CursorStyle.POINTER);
      SceneTextService currentWidth =
          new SceneTextService(currentText, this.value, enabled ? -1 : this.count3)
              .overflow(SceneTextService.Overflow.ELLIPSIS)
              .width(LayoutOperationHandler.percent(100.0F))
              .minWidth(0.0F)
              .textAlign(SceneTextService.TextAlign.CENTER)
              .pointerEvents(false);
      sceneCornerRadius.addChild(currentWidth);
      sceneCornerRadius.onClick(
          () -> {
            LinkedHashSet linkedHashSet = new LinkedHashSet<>(this.text2);
            if (linkedHashSet.contains(currentText)) {
              linkedHashSet.remove(currentText);
            } else {
              linkedHashSet.add(currentText);
            }

            this.text2 = linkedHashSet;
            if (this.onChange != null) {
              this.onChange.accept(this.text2);
            }

            this.updateState();
          });
      this.addChild(sceneCornerRadius);
    }

    return this;
  }

  private void updateState() {
    this.build();
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float value = 0.0F;

    for (ScenePctService scenePct : this.children()) {
      if (scenePct.visible) {
        value +=
            scenePct.width == -1.0F
                ? scenePct.intrinsicWidth(compositorPushPresentationScale)
                : scenePct.width;
      }
    }

    if (this.children().size() > 1) {
      value += this.value4 * (this.children().size() - 1);
    }

    return value;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float value = 0.0F;

    for (ScenePctService scenePct : this.children()) {
      if (scenePct.visible) {
        value =
            Math.max(
                value,
                scenePct.height == -1.0F
                    ? scenePct.intrinsicHeight(compositorPushPresentationScale)
                    : scenePct.height);
      }
    }

    return value;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {}
}
