package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class ControlOnPreviewHandler extends RangeSliderControl {
  public static final float DEFAULT_WIDTH = 112.0f;
  public static final float DEFAULT_HEIGHT = 18.0f;
  private static final int count = 738197503;
  private static final int count2 = -8530948;
  private static final int count3 = -1;
  private static final int count4 = 1073741824;
  private static final int count5 = -905969665;
  private final ModuleSetting.Range moduleRange;
  private final AutoCloseable autoCloseable;
  private BiConsumer<Float, Float> biConsumer;
  private BiConsumer<Float, Float> biConsumer2;
  private boolean enabled2;
  private boolean feukfxelseco;
  private boolean enabled4;
  private boolean fair2ejcgsbi;
  private boolean enabled6;
  private boolean enabled7;

  public ControlOnPreviewHandler onPreview(final BiConsumer<Float, Float> biConsumer2) {
    this.biConsumer2 = biConsumer2;
    return this;
  }

  public ControlOnPreviewHandler(final ModuleSetting.Range obj) {
    this.enabled2 = true;
    this.feukfxelseco = true;
    this.moduleRange = Objects.requireNonNull(obj, "setting");
    super.range(obj.min(), obj.max());
    super.step(obj.step());
    super.values(obj.low(), obj.high());
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
        (n, n2) -> {
          this.enabled6 = true;
          if (this.biConsumer2 != null) {
            this.biConsumer2.accept(n, n2);
          }
          return;
        });
    this.refreshState();
    this.autoCloseable = obj.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Range setting() {
    return this.moduleRange;
  }

  @Override
  public ControlOnPreviewHandler low(final float n) {
    if (!Float.isFinite(n)) {
      return this;
    }
    this.trySetRange(Math.min(n, this.moduleRange.high()), this.moduleRange.high());
    return this;
  }

  @Override
  public ControlOnPreviewHandler high(final float n) {
    if (!Float.isFinite(n)) {
      return this;
    }
    this.trySetRange(this.moduleRange.low(), Math.max(this.moduleRange.low(), n));
    return this;
  }

  @Override
  public ControlOnPreviewHandler values(final float n, final float n2) {
    this.trySetRange(n, n2);
    return this;
  }

  @Override
  public ControlOnPreviewHandler range(final float f1, final float f2) {
    if (Float.compare(f1, this.moduleRange.min()) != 0
        || Float.compare(f2, this.moduleRange.max()) != 0) {
      throw new UnsupportedOperationException(
          "a bound range slider's domain is owned by its Setting.Range");
    }
    return this;
  }

  @Override
  public ControlOnPreviewHandler step(final float f1) {
    if (Float.compare(f1, this.moduleRange.step()) != 0) {
      throw new UnsupportedOperationException(
          "a bound range slider's step is owned by its Setting.Range");
    }
    return this;
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.fair2ejcgsbi;
  }

  public boolean available() {
    return this.enabled4 && this.fair2ejcgsbi && this.enabled2 && this.feukfxelseco;
  }

  @Override
  public ControlOnPreviewHandler visible(final boolean enabled2) {
    this.enabled2 = enabled2;
    this.refreshState();
    return this;
  }

  public ControlOnPreviewHandler enabled(final boolean feukfxelseco) {
    this.feukfxelseco = feukfxelseco;
    this.refreshState();
    return this;
  }

  @Override
  public ControlOnPreviewHandler onChange(final BiConsumer<Float, Float> biConsumer) {
    this.biConsumer = biConsumer;
    return this;
  }

  public boolean trySetRange(final float f, final float f2) {
    this.refreshState();
    if (!this.available() || this.enabled7 || !Float.isFinite(f) || !Float.isFinite(f2)) {
      return false;
    }
    final ModuleSetting.RangeValue rangeValue = this.moduleRange.get();
    this.moduleRange.set(f, f2);
    final ModuleSetting.RangeValue rangeValue2 = this.moduleRange.get();
    this.updateState(rangeValue2);
    final boolean b = !rangeValue.equals(rangeValue2);
    if (b && this.biConsumer != null) {
      this.biConsumer.accept(rangeValue2.low(), rangeValue2.high());
    }
    return b;
  }

  public void refreshState() {
    if (this.enabled7) {
      return;
    }
    this.enabled4 = this.moduleRange.isVisible();
    this.fair2ejcgsbi = this.moduleRange.isActive();
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
    this.updateState(this.moduleRange.get());
  }

  @Override
  protected void onRelease(final boolean b) {
    super.onRelease(b);
    if (!this.enabled6) {
      return;
    }
    final float low = super.low();
    final float high = super.high();
    this.enabled6 = false;
    if (!this.available() || this.enabled7) {
      this.updateState(this.moduleRange.get());
      return;
    }
    final ModuleSetting.RangeValue rangeValue = this.moduleRange.get();
    this.moduleRange.set(low, high);
    final ModuleSetting.RangeValue rangeValue2 = this.moduleRange.get();
    this.updateState(rangeValue2);
    if (!rangeValue.equals(rangeValue2) && this.biConsumer != null) {
      this.biConsumer.accept(rangeValue2.low(), rangeValue2.high());
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
    this.biConsumer2 = null;
    this.dispose();
  }

  public void dispose() {
    if (this.enabled7) {
      return;
    }
    this.enabled7 = true;
    this.enabled6 = false;
    this.interactive = false;
    this.pointerEvents = false;
    try {
      this.autoCloseable.close();
    } catch (final Exception ex) {
    }
  }

  private void updateState(final ModuleSetting.RangeValue rangeValue) {
    this.enabled6 = false;
    super.values(rangeValue.low(), rangeValue.high());
  }

  private static String createText(final float n) {
    if (n <= 0.0f || !Float.isFinite(n)) {
      return "%.2f";
    }
    return "%."
        + Math.max(0, Math.min(4, new BigDecimal(Float.toString(n)).stripTrailingZeros().scale()));
  }
}
