package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

public final class NumberSettingControl extends SliderControl {
  public static final float DEFAULT_WIDTH = 112.0f;
  public static final float DEFAULT_HEIGHT = 18.0f;
  private static final int fpmkv4m4kag = 738197503;
  private static final int f3nmaaotbibv = -8530948;
  private static final int count3 = -1;
  private static final int count4 = 1073741824;
  private static final int fxzgh38ier2 = -905969665;
  private final ModuleSetting.Number moduleNumber;
  private final AutoCloseable autoCloseable;
  private Consumer<Float> consumer;
  private Consumer<Float> consumer2;
  private boolean enabled2;
  private boolean currentEnabled;
  private boolean enabled4;
  private boolean enabled5;
  private boolean fx9dabrmkuz;
  private boolean enabled7;

  public NumberSettingControl(final ModuleSetting.Number obj) {
    this.enabled2 = true;
    this.currentEnabled = true;
    this.moduleNumber = Objects.requireNonNull(obj, "setting");
    super.range(obj.min(), obj.max());
    super.step(obj.step());
    super.value(obj.get());
    super.trackColor(738197503);
    super.fillColor(-8530948);
    super.thumbColor(-1);
    super.thumbShadowColor(1073741824);
    super.textColor(-905969665);
    super.fontSize(Float.intBitsToFloat(1089470464));
    super.trackHeight(Float.intBitsToFloat(1077936128));
    super.thumbRadius(Float.intBitsToFloat(1078984704));
    super.thumbShadow(Float.intBitsToFloat(1075838976));
    super.trackRadius(Float.intBitsToFloat(1069547520));
    super.format(createText(obj.step()));
    super.size(
        ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1099956224));
    super.cursorStyle(CursorStyle.POINTER);
    super.onChange(
        n -> {
          this.fx9dabrmkuz = true;
          if (this.consumer2 != null) {
            this.consumer2.accept(n);
          }
          return;
        });
    this.refreshState();
    this.autoCloseable = obj.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Number setting() {
    return this.moduleNumber;
  }

  public NumberSettingControl onPreview(final Consumer<Float> consumer2) {
    this.consumer2 = consumer2;
    return this;
  }

  @Override
  public NumberSettingControl value(final float n) {
    this.trySetValue(n);
    return this;
  }

  @Override
  public NumberSettingControl range(final float f1, final float f2) {
    if (Float.compare(f1, this.moduleNumber.min()) != 0
        || Float.compare(f2, this.moduleNumber.max()) != 0) {
      throw new UnsupportedOperationException(
          "a bound slider's range is owned by its Setting.Number");
    }
    return this;
  }

  @Override
  public NumberSettingControl step(final float f1) {
    if (Float.compare(f1, this.moduleNumber.step()) != 0) {
      throw new UnsupportedOperationException(
          "a bound slider's step is owned by its Setting.Number");
    }
    return this;
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.enabled5;
  }

  public boolean available() {
    return this.enabled4 && this.enabled5 && this.enabled2 && this.currentEnabled;
  }

  @Override
  public NumberSettingControl visible(final boolean enabled2) {
    this.enabled2 = enabled2;
    this.refreshState();
    return this;
  }

  public NumberSettingControl enabled(final boolean currentEnabled) {
    this.currentEnabled = currentEnabled;
    this.refreshState();
    return this;
  }

  @Override
  public NumberSettingControl onChange(final Consumer<Float> consumer) {
    this.consumer = consumer;
    return this;
  }

  public boolean trySetValue(final float n) {
    this.refreshState();
    if (!this.available() || this.enabled7 || !Float.isFinite(n)) {
      return false;
    }
    final float floatValue = this.moduleNumber.get();
    this.moduleNumber.set(n);
    final float floatValue2 = this.moduleNumber.get();
    this.updateState(floatValue2);
    final boolean b = Float.compare(floatValue, floatValue2) != 0;
    if (b && this.consumer != null) {
      this.consumer.accept(floatValue2);
    }
    return b;
  }

  public void refreshState() {
    if (this.enabled7) {
      return;
    }
    this.enabled4 = this.moduleNumber.isVisible();
    this.enabled5 = this.moduleNumber.isActive();
    final boolean available = this.available();
    final boolean visible = this.materialStyle ? (this.enabled2 && this.enabled4) : available;
    if (this.visible != visible) {
      this.visible = visible;
      this.invalidateLayout();
    }
    if (this.interactive != available) {
      this.interactive = available;
      this.invalidate();
    }
    if (this.pointerEvents != available) {
      this.pointerEvents = available;
      this.invalidate();
    }
    final CursorStyle cursorStyle = available ? CursorStyle.POINTER : CursorStyle.DEFAULT;
    if (this.cursorStyle != cursorStyle) {
      this.cursorStyle = cursorStyle;
      this.invalidate();
    }
    this.updateState(this.moduleNumber.get());
  }

  @Override
  protected void onRelease(final boolean b) {
    if (!this.fx9dabrmkuz) {
      return;
    }
    final float value = super.value();
    this.fx9dabrmkuz = false;
    if (!this.available() || this.enabled7) {
      this.updateState(this.moduleNumber.get());
      return;
    }
    final float floatValue = this.moduleNumber.get();
    this.moduleNumber.set(value);
    final float floatValue2 = this.moduleNumber.get();
    this.updateState(floatValue2);
    if (Float.compare(floatValue, floatValue2) != 0 && this.consumer != null) {
      this.consumer.accept(floatValue2);
    }
  }

  @Override
  protected boolean handleScroll(final float n) {
    return false;
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1121976320);
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1099956224);
  }

  @Override
  protected void onDetached() {
    this.consumer2 = null;
    this.dispose();
  }

  public void dispose() {
    if (this.enabled7) {
      return;
    }
    this.enabled7 = true;
    this.fx9dabrmkuz = false;
    this.interactive = false;
    this.pointerEvents = false;
    try {
      this.autoCloseable.close();
    } catch (final Exception ex) {
    }
  }

  private void updateState(final float n) {
    this.fx9dabrmkuz = false;
    super.value(n);
  }

  private static String createText(final float n) {
    if (n <= 0.0f || !Float.isFinite(n)) {
      return "%.2f";
    }
    return "%."
        + Math.max(0, Math.min(4, new BigDecimal(Float.toString(n)).stripTrailingZeros().scale()));
  }
}
