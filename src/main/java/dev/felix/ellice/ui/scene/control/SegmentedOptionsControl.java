package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Arrays;
import java.util.function.Consumer;

public class SegmentedOptionsControl extends ScenePctService<SegmentedOptionsControl> {
  private String[] text2 = new String[0];
  private int count;
  private int count2 = 553648127;
  private int count3 = 822083583;
  private int count4 = -3355444;
  private float value = 8.0F;
  private float value2 = 4.0F;
  public Consumer<Integer> onChange;

  public SegmentedOptionsControl() {
    this.interactive = true;
  }

  public SegmentedOptionsControl options(String... strings) {
    String[] currentStrings = strings != null ? strings : new String[0];
    if (Arrays.equals(this.text2, currentStrings)) {
      return this;
    }

    this.text2 = currentStrings;
    this.count = Math.max(0, Math.min(this.count, Math.max(0, this.text2.length - 1)));
    this.invalidateLayout();
    return this;
  }

  public SegmentedOptionsControl selected(int value) {
    this.count = value;
    return this;
  }

  public SegmentedOptionsControl selected(String text) {
    for (int index = 0; index < this.text2.length; index++) {
      if (this.text2[index].equals(text)) {
        this.count = index;
        break;
      }
    }

    return this;
  }

  public SegmentedOptionsControl backgroundColor(int color) {
    this.count2 = color;
    return this;
  }

  public SegmentedOptionsControl hoverColor(int color) {
    this.count3 = color;
    return this;
  }

  public SegmentedOptionsControl textColor(int color) {
    this.count4 = color;
    return this;
  }

  public SegmentedOptionsControl fontSize(float currentValue) {
    if (Float.floatToIntBits(this.value) == Float.floatToIntBits(currentValue)) {
      return this;
    }

    this.value = currentValue;
    this.invalidateLayout();
    return this;
  }

  public SegmentedOptionsControl cornerRadius(float value) {
    this.value2 = value;
    return this;
  }

  public SegmentedOptionsControl onChange(Consumer<Integer> consumer) {
    this.onChange = consumer;
    return this;
  }

  public int selectedIndex() {
    return this.count;
  }

  public String selectedValue() {
    return this.text2.length > 0 ? this.text2[this.count] : "";
  }

  @Override
  protected void handleClick() {
    if (this.text2.length > 0) {
      this.count = (this.count + 1) % this.text2.length;
      if (this.onChange != null) {
        this.onChange.accept(this.count);
      }
    }

    if (this.onClick != null) {
      this.onClick.run();
    }
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float currentValue = 0.0F;

    for (String id : this.text2) {
      currentValue =
          Math.max(currentValue, compositorPushPresentationScale.textWidth(id, this.value));
    }

    return currentValue + 12.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return compositorPushPresentationScale.textLineHeight(this.value) + 6.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = this.effectiveOpacity;
    float width = this.effectiveEdgeSoftness;
    int height = this.hovered ? this.count3 : this.count2;
    float currentValue = this.value2;
    compositorPushPresentationScale.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        currentValue,
        currentValue,
        currentValue,
        currentValue,
        mulAlpha(height, y),
        0.0F,
        0.0F,
        0,
        0.0F,
        0,
        1.0F,
        width);
    String currentText = this.selectedValue();
    float nextValue = compositorPushPresentationScale.textWidth(currentText, this.value);
    compositorPushPresentationScale.text(
        this.cx + (this.cw - nextValue) / 2.0F,
        this.cy + (this.ch - this.value) / 2.0F,
        currentText,
        this.value,
        mulAlpha(this.count4, y));
  }
}
