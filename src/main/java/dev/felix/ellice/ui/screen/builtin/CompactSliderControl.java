package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import java.util.function.Consumer;

final class CompactSliderControl extends SliderControl {
  CompactSliderControl(
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      int targetValue,
      int inputValue,
      Consumer<Float> consumer) {
    this.range(currentValue, nextValue);
    this.step(previousValue);
    this.value(sourceValue);
    this.trackColor(687865855);
    this.fillColor(targetValue);
    this.thumbColor(-1);
    this.thumbShadowColor(inputValue);
    this.fontSize(0.0F);
    this.trackHeight(4.0F);
    this.thumbRadius(3.5F);
    this.thumbShadow(3.0F);
    this.format("");
    this.onChange(consumer);
    this.size(ScenePctService.pct(100.0F), 13.0F);
  }

  @Override
  protected boolean handleScroll(float value) {
    return false;
  }
}
