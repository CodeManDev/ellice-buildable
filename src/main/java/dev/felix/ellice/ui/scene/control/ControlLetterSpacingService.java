package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextEndsService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.Consumer;

public class ControlLetterSpacingService extends ScenePctService<ControlLetterSpacingService> {
  private String text2 = "";
  private int count;
  private int count2 = -1;
  private boolean enabled;
  private float value = Float.NaN;
  private int count3 = Integer.MIN_VALUE;
  private int count4 = Integer.MIN_VALUE;
  private int count5 = Integer.MIN_VALUE;
  private int count6 = Integer.MIN_VALUE;
  private int count7 = Integer.MIN_VALUE;
  private float value2 = Float.NaN;
  private int ff6vta1kve = 32;
  private String text3 = "";
  private boolean enabled2;
  private float value3;
  private float value4;
  private float value5 = 4.0F;
  private String text4;
  private float value6;
  private boolean enabled3;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;
  private float value7;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite(
          "materialInputFocus", item -> item instanceof ControlLetterSpacingService);
  public Consumer<String> onSubmit;
  public Consumer<String> onChanged;
  public Runnable onUnfocus;
  private long timestamp;
  private int count8;
  private int count9 = -1;
  private int count10 = -1;
  private static final long timestamp2 = 450000000L;
  private final ArrayDeque<ControlLetterSpacingService.Snapshot> items = new ArrayDeque<>();
  private final ArrayDeque<ControlLetterSpacingService.Snapshot> items2 = new ArrayDeque<>();
  private static final int count11 = 64;
  private char char2;

  public ControlLetterSpacingService letterSpacing(float value) {
    if (!Float.isFinite(value)) {
      throw new IllegalArgumentException("Non-finite letter spacing");
    }

    if (this.value6 != value) {
      this.value6 = value;
      this.invalidateLayout();
    }

    return this;
  }

  public ControlLetterSpacingService fontFamily(String text) {
    if (!Objects.equals(this.text4, text)) {
      this.text4 = text;
      this.invalidateLayout();
    }

    return this;
  }

  public ControlLetterSpacingService material(boolean enabled) {
    this.enabled3 = enabled;
    this.value5 = enabled ? 16.0F : 4.0F;
    return this;
  }

  public ControlLetterSpacingService materialSearch(boolean enabled) {
    this.enabled4 = enabled;
    return this;
  }

  public ControlLetterSpacingService materialError(boolean enabled) {
    this.enabled5 = enabled;
    this.invalidate();
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return "materialInputFocus".equals(text) ? this.value7 : super.getAnimProperty(text);
  }

  @Override
  public void setAnimProperty(String text, float value) {
    if ("materialInputFocus".equals(text)) {
      this.value7 = value;
    } else {
      super.setAnimProperty(text, value);
    }
  }

  public ControlLetterSpacingService() {
    this.interactive = true;
    this.cursorStyle(ScenePctService.CursorStyle.TEXT);
  }

  private void updateState() {
    ControlLetterSpacingService.Snapshot snapshot = this.items.peekFirst();
    if (snapshot == null || !snapshot.text.equals(this.text2)) {
      this.items.push(
          new ControlLetterSpacingService.Snapshot(this.text2, this.count, this.count2));
      if (this.items.size() > 64) {
        this.items.pollLast();
      }

      this.items2.clear();
    }
  }

  private void updateState2() {
    if (!this.items.isEmpty()) {
      this.items2.push(
          new ControlLetterSpacingService.Snapshot(this.text2, this.count, this.count2));
      ControlLetterSpacingService.Snapshot snapshot = this.items.pop();
      this.text2 = snapshot.text;
      this.count = snapshot.cursor;
      this.count2 = snapshot.selStart;
      this.value3 = 0.0F;
      if (this.onChanged != null) {
        this.onChanged.accept(this.text2);
      }
    }
  }

  private void updateState3() {
    if (!this.items2.isEmpty()) {
      this.items.push(
          new ControlLetterSpacingService.Snapshot(this.text2, this.count, this.count2));
      ControlLetterSpacingService.Snapshot snapshot = this.items2.pop();
      this.text2 = snapshot.text;
      this.count = snapshot.cursor;
      this.count2 = snapshot.selStart;
      this.value3 = 0.0F;
      if (this.onChanged != null) {
        this.onChanged.accept(this.text2);
      }
    }
  }

  @Override
  protected void onPress(float value, float currentValue) {
    this.enabled = true;
    this.value3 = 0.0F;
    this.count9 = -1;
    this.count10 = -1;
    long longValue = System.nanoTime();
    if (longValue - this.timestamp < 450000000L) {
      this.count8++;
    } else {
      this.count8 = 1;
    }

    this.timestamp = longValue;
    int nextValue = this.calculateValue(value);
    if (this.count8 >= 3) {
      this.count2 = 0;
      this.count = this.text2.length();
    } else if (this.count8 == 2) {
      this.count = nextValue;
      int previousValue = this.calculateValue4(nextValue);
      int sourceValue = this.calculateValue5(nextValue);
      this.count2 = previousValue;
      this.count = sourceValue;
      this.count9 = previousValue;
      this.count10 = sourceValue;
    } else {
      this.count = nextValue;
      this.count2 = nextValue;
    }
  }

  private int calculateValue(float currentValue) {
    CompositorPushPresentationScaleService compositorPushPresentationScale =
        CoreIsInitializedHandler.get().compositor();
    if (compositorPushPresentationScale == null) {
      return this.count;
    }

    float nextValue = this.calculateValue7(this.value, "textinput.font-size");
    String text = this.createText2();
    float previousValue = currentValue - this.cx - this.value5 + this.value4;
    if (previousValue <= 0.0F) {
      return 0;
    }

    float sourceValue =
        compositorPushPresentationScale.textWidth(
            text, nextValue, TextMode.REGULAR, this.text4, this.value6);
    if (previousValue >= sourceValue) {
      return this.text2.length();
    }

    int targetValue = 0;
    float inputValue =
        Math.abs(
            compositorPushPresentationScale.textCaretX(
                    text, 0, nextValue, TextMode.REGULAR, this.text4, this.value6)
                - previousValue);
    int index = 0;

    while (index < text.length()) {
      index = TextEndsService.nextBoundary(text, index);
      float outputValue =
          compositorPushPresentationScale.textCaretX(
              text, index, nextValue, TextMode.REGULAR, this.text4, this.value6);
      float resultValue = Math.abs(outputValue - previousValue);
      if (resultValue < inputValue) {
        inputValue = resultValue;
        targetValue = index;
      }

      if (outputValue > previousValue + 1.0F) {
        break;
      }
    }

    return targetValue;
  }

  public ControlLetterSpacingService text(String currentText) {
    this.text2 = currentText != null ? currentText : "";
    this.count = this.text2.length();
    return this;
  }

  public ControlLetterSpacingService fontSize(float currentValue) {
    if (Float.floatToIntBits(this.value) == Float.floatToIntBits(currentValue)) {
      return this;
    }

    this.value = currentValue;
    this.invalidateLayout();
    return this;
  }

  public ControlLetterSpacingService textColor(int color) {
    this.count3 = color;
    return this;
  }

  public ControlLetterSpacingService selectionColor(int color) {
    this.count4 = color;
    return this;
  }

  public ControlLetterSpacingService bgColor(int color) {
    this.count5 = color;
    return this;
  }

  public ControlLetterSpacingService focusBorder(int value) {
    this.count6 = value;
    return this;
  }

  public ControlLetterSpacingService onUnfocus(Runnable runnable) {
    this.onUnfocus = runnable;
    return this;
  }

  public ControlLetterSpacingService focus() {
    this.enabled = true;
    this.count2 = -1;
    this.value3 = 0.0F;
    if (this.count > this.text2.length()) {
      this.count = this.text2.length();
    }

    return this;
  }

  public ControlLetterSpacingService placeholderColor(int color) {
    this.count7 = color;
    return this;
  }

  public ControlLetterSpacingService cornerRadius(float value) {
    this.value2 = value;
    return this;
  }

  public ControlLetterSpacingService maxLength(int value) {
    this.ff6vta1kve = value;
    return this;
  }

  public ControlLetterSpacingService placeholder(String text) {
    this.text3 = text != null ? text : "";
    return this;
  }

  public ControlLetterSpacingService password(boolean enabled) {
    this.enabled2 = enabled;
    return this;
  }

  public void clearSensitiveText() {
    this.text2 = "";
    this.count = 0;
    this.count2 = -1;
    this.value4 = 0.0F;
    this.char2 = 0;
    this.items.clear();
    this.items2.clear();
  }

  public ControlLetterSpacingService onSubmit(Consumer<String> consumer) {
    this.onSubmit = consumer;
    return this;
  }

  public ControlLetterSpacingService onChanged(Consumer<String> consumer) {
    this.onChanged = consumer;
    return this;
  }

  public String text() {
    return this.text2;
  }

  public boolean focused() {
    return this.enabled;
  }

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  @Override
  protected void handleClick() {
    if (this.onClick != null) {
      this.onClick.run();
    }
  }

  @Override
  protected void updateWhilePressed(float value, float currentValue) {
    if (this.enabled) {
      if (this.count8 < 3) {
        int nextValue = this.calculateValue(value);
        if (this.count8 == 2 && this.count9 >= 0) {
          if (nextValue < this.count9) {
            this.count2 = this.count10;
            this.count = this.calculateValue4(nextValue);
          } else if (nextValue > this.count10) {
            this.count2 = this.count9;
            this.count = this.calculateValue5(nextValue);
          } else {
            this.count2 = this.count9;
            this.count = this.count10;
          }
        } else {
          this.count = nextValue;
          this.value3 = 0.0F;
        }
      }
    }
  }

  public void unfocus() {
    boolean currentEnabled = this.enabled;
    this.enabled = false;
    this.count2 = -1;
    if (currentEnabled && this.onUnfocus != null) {
      this.onUnfocus.run();
    }
  }

  private boolean checkCondition() {
    return this.count2 >= 0 && this.count2 != this.count;
  }

  private int calculateValue2() {
    return Math.min(this.count2, this.count);
  }

  private int calculateValue3() {
    return Math.max(this.count2, this.count);
  }

  private void updateState4() {
    if (this.checkCondition()) {
      this.updateState();
      int value = this.calculateValue2();
      int currentValue = this.calculateValue3();
      this.text2 = this.text2.substring(0, value) + this.text2.substring(currentValue);
      this.count = TextEndsService.clampToBoundary(this.text2, value);
      this.count2 = -1;
      if (this.onChanged != null) {
        this.onChanged.accept(this.text2);
      }
    }
  }

  private String createText() {
    return this.checkCondition()
        ? this.text2.substring(this.calculateValue2(), this.calculateValue3())
        : this.text2;
  }

  public void charTyped(char character) {
    if (this.enabled) {
      if (Character.isHighSurrogate(character)) {
        this.char2 = character;
      } else {
        int value;
        if (Character.isLowSurrogate(character) && this.char2 != 0) {
          value = Character.toCodePoint(this.char2, character);
          this.char2 = 0;
        } else {
          this.char2 = 0;
          value = character;
        }

        if (!Character.isISOControl(value)) {
          this.count = TextEndsService.clampToBoundary(this.text2, this.count);
          if (this.checkCondition()) {
            this.updateState4();
          }

          String text = new String(Character.toChars(value));
          if (this.text2.length() + text.length() <= this.ff6vta1kve) {
            this.updateState();
            this.text2 =
                this.text2.substring(0, this.count) + text + this.text2.substring(this.count);
            this.count = this.count + text.length();
            this.count2 = -1;
            this.value3 = 0.0F;
            if (this.onChanged != null) {
              this.onChanged.accept(this.text2);
            }
          }
        }
      }
    }
  }

  public void keyPressed(int value) {
    this.keyPressed(value, 0);
  }

  public void keyPressed(int value, int currentValue) {
    if (this.enabled) {
      this.value3 = 0.0F;
      this.count = TextEndsService.clampToBoundary(this.text2, this.count);
      if (this.count2 >= 0) {
        this.count2 = TextEndsService.clampToBoundary(this.text2, this.count2);
      }

      int nextValue = (currentValue & 2) == 0 && (currentValue & 8) == 0 ? 0 : 1;
      int previousValue = (currentValue & 1) != 0 ? 1 : 0;
      switch (value) {
        case 89:
          if (nextValue != 0) {
            this.updateState3();
          }
          break;
        case 90:
          if (nextValue != 0) {
            if (previousValue != 0) {
              this.updateState3();
            } else {
              this.updateState2();
            }
          }
          break;
        case 256:
          boolean currentEnabled = this.enabled;
          this.enabled = false;
          this.count2 = -1;
          if (currentEnabled && this.onUnfocus != null) {
            this.onUnfocus.run();
          }
          break;
        case 257:
        case 335:
          if (this.onSubmit != null) {
            this.onSubmit.accept(this.text2);
          }

          boolean nextEnabled = this.enabled;
          this.enabled = false;
          this.count2 = -1;
          if (nextEnabled && this.onUnfocus != null) {
            this.onUnfocus.run();
          }
          break;
        case 259:
          if (this.checkCondition()) {
            this.updateState4();
          } else if (nextValue != 0) {
            int currentCount = this.calculateValue4(this.count);
            if (currentCount != this.count) {
              this.updateState();
              this.text2 = this.text2.substring(0, currentCount) + this.text2.substring(this.count);
              this.count = currentCount;
              this.count2 = -1;
              if (this.onChanged != null) {
                this.onChanged.accept(this.text2);
              }
            }
          } else if (this.count > 0) {
            this.updateState();
            int nextCount = TextEndsService.previousBoundary(this.text2, this.count);
            this.text2 = this.text2.substring(0, nextCount) + this.text2.substring(this.count);
            this.count = nextCount;
            if (this.onChanged != null) {
              this.onChanged.accept(this.text2);
            }
          }
          break;
        case 261:
          if (this.checkCondition()) {
            this.updateState4();
          } else if (this.count < this.text2.length()) {
            this.updateState();
            int previousCount = TextEndsService.nextBoundary(this.text2, this.count);
            this.text2 = this.text2.substring(0, this.count) + this.text2.substring(previousCount);
            if (this.onChanged != null) {
              this.onChanged.accept(this.text2);
            }
          }
          break;
        case 262:
          if (previousValue != 0 && this.count2 < 0) {
            this.count2 = this.count;
          }

          if (nextValue != 0) {
            this.count = this.calculateValue5(this.count);
          } else if (this.count < this.text2.length()) {
            this.count = TextEndsService.nextBoundary(this.text2, this.count);
          }

          if (previousValue == 0) {
            this.count2 = -1;
          }
          break;
        case 263:
          if (previousValue != 0 && this.count2 < 0) {
            this.count2 = this.count;
          }

          if (nextValue != 0) {
            this.count = this.calculateValue4(this.count);
          } else if (this.count > 0) {
            this.count = TextEndsService.previousBoundary(this.text2, this.count);
          }

          if (previousValue == 0) {
            this.count2 = -1;
          }
          break;
        case 268:
          if (previousValue != 0 && this.count2 < 0) {
            this.count2 = this.count;
          }

          this.count = 0;
          if (previousValue == 0) {
            this.count2 = -1;
          }
          break;
        case 269:
          if (previousValue != 0 && this.count2 < 0) {
            this.count2 = this.count;
          }

          this.count = this.text2.length();
          if (previousValue == 0) {
            this.count2 = -1;
          }
      }
    }
  }

  public void selectAll() {
    this.count2 = 0;
    this.count = this.text2.length();
  }

  public void paste(String text) {
    if (this.enabled && text != null) {
      this.count = TextEndsService.clampToBoundary(this.text2, this.count);
      if (this.checkCondition()) {
        this.updateState4();
      }

      StringBuilder stringBuilder = new StringBuilder();
      int index = 0;

      while (index < text.length()) {
        int value = text.codePointAt(index);
        int currentValue = Character.charCount(value);
        index += currentValue;
        if (!Character.isISOControl(value)) {
          if (stringBuilder.length() + this.text2.length() + currentValue > this.ff6vta1kve) {
            break;
          }

          stringBuilder.appendCodePoint(value);
        }
      }

      if (!stringBuilder.isEmpty()) {
        this.updateState();
        this.text2 =
            this.text2.substring(0, this.count) + stringBuilder + this.text2.substring(this.count);
        this.count = this.count + stringBuilder.length();
        this.value3 = 0.0F;
        this.count2 = -1;
        if (this.onChanged != null) {
          this.onChanged.accept(this.text2);
        }
      }
    }
  }

  public String copy() {
    return this.createText();
  }

  public void cut() {
    if (this.checkCondition()) {
      this.updateState4();
    }
  }

  private int calculateValue4(int value) {
    if (value <= 0) {
      return 0;
    }

    int currentValue = TextEndsService.previousBoundary(this.text2, value);

    while (currentValue > 0 && Character.isWhitespace(this.text2.codePointBefore(currentValue))) {
      currentValue = TextEndsService.previousBoundary(this.text2, currentValue);
    }

    while (currentValue > 0 && !Character.isWhitespace(this.text2.codePointBefore(currentValue))) {
      currentValue = TextEndsService.previousBoundary(this.text2, currentValue);
    }

    return currentValue;
  }

  private int calculateValue5(int value) {
    if (value >= this.text2.length()) {
      return this.text2.length();
    }

    int index = value;

    while (index < this.text2.length() && !Character.isWhitespace(this.text2.codePointAt(index))) {
      index = TextEndsService.nextBoundary(this.text2, index);
    }

    while (index < this.text2.length() && Character.isWhitespace(this.text2.codePointAt(index))) {
      index = TextEndsService.nextBoundary(this.text2, index);
    }

    return index;
  }

  private ThemeIsSetService collectValues() {
    return CoreIsInitializedHandler.get().theme();
  }

  private int calculateValue6(int value, String text) {
    return ThemeIsSetService.isSet(value) ? value : this.collectValues().color(text);
  }

  private float calculateValue7(float value, String text) {
    return ThemeIsSetService.isSet(value) ? value : this.collectValues().number(text);
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 80.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return this.enabled3
        ? 56.0F
        : compositorPushPresentationScale.textLineHeight(
                this.calculateValue7(this.value, "textinput.font-size"))
            + 6.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = this.effectiveOpacity;
    float width = this.effectiveEdgeSoftness;
    this.value3 = this.value3 + SceneDtService.dt();
    int height = this.calculateValue6(this.count5, "textinput.bg");
    int currentValue = this.calculateValue6(this.count6, "textinput.border-focus");
    int nextValue = this.calculateValue6(this.count3, "textinput.text");
    int previousValue = this.calculateValue6(this.count4, "textinput.selection");
    int sourceValue =
        ThemeIsSetService.isSet(this.count7) ? this.count7 : mulAlpha(nextValue, 0.45F);
    float targetValue = this.calculateValue7(this.value, "textinput.font-size");
    float inputValue = this.calculateValue7(this.value2, "textinput.radius");
    if (this.enabled3 && !this.interactive) {
      height =
          MaterialIsLightService.layer(
              MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.04F);
      nextValue =
          MaterialIsLightService.layer(
              MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, 0.38F);
    }

    if (this.enabled3 && this.enabled != this.enabled6) {
      this.enabled6 = this.enabled;
      MotionAnimateService.animate(
          this,
          motionFiniteService,
          this.enabled ? 1.0F : 0.0F,
          MaterialIsLightService.FAST_EFFECTS);
    }

    int outputValue = this.enabled ? currentValue : 0;
    float resultValue = this.enabled ? 1.0F : 0.0F;
    if (this.enabled3) {
      outputValue = 0;
      resultValue = 0.0F;
    }

    compositorPushPresentationScale.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        inputValue,
        inputValue,
        this.enabled3 ? 0.0F : inputValue,
        this.enabled3 ? 0.0F : inputValue,
        mulAlpha(height, y),
        0.0F,
        0.0F,
        0,
        resultValue,
        mulAlpha(outputValue, y),
        1.0F,
        width);
    if (this.enabled3) {
      int candidateValue =
          this.enabled5 ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE_VARIANT;
      compositorPushPresentationScale.roundedRect(
          this.cx,
          this.cy + this.ch - 1.0F,
          this.cw,
          1.0F,
          0.0F,
          mulAlpha(candidateValue, y * (this.interactive ? 1.0F : 0.38F)));
      float selectedValue = Math.max(0.0F, Math.min(1.0F, this.value7));
      if (selectedValue > 0.001F) {
        compositorPushPresentationScale.roundedRect(
            this.cx + (1.0F - selectedValue) * this.cw / 2.0F,
            this.cy + this.ch - 2.0F,
            this.cw * selectedValue,
            2.0F,
            0.0F,
            mulAlpha(this.enabled5 ? MaterialIsLightService.ERROR : currentValue, y));
      }
    }

    float defaultValue =
        this.cy
            + (this.ch
                    - (!this.enabled3 && !this.enabled4
                        ? targetValue
                        : compositorPushPresentationScale.textLineHeight(
                            targetValue, TextMode.REGULAR, this.text4)))
                / 2.0F;
    float initialValue = this.cw - this.value5 * 2.0F;
    this.count = TextEndsService.clampToBoundary(this.text2, this.count);
    if (this.count2 >= 0) {
      this.count2 = TextEndsService.clampToBoundary(this.text2, this.count2);
    }

    String currentText = this.createText2();
    int currentLength = Math.min(this.count, currentText.length());
    float resolvedValue =
        compositorPushPresentationScale.textCaretX(
            currentText, currentLength, targetValue, TextMode.REGULAR, this.text4, this.value6);
    if (this.enabled) {
      float computedValue = resolvedValue - this.value4;
      if (computedValue > initialValue - 2.0F) {
        this.value4 = resolvedValue - initialValue + 2.0F;
      }

      if (computedValue < 0.0F) {
        this.value4 = resolvedValue;
      }
    } else {
      this.value4 = 0.0F;
    }

    float cachedValue =
        compositorPushPresentationScale.textWidth(
            currentText, targetValue, TextMode.REGULAR, this.text4, this.value6);
    this.value4 = Math.max(0.0F, Math.min(this.value4, Math.max(0.0F, cachedValue - initialValue)));
    compositorPushPresentationScale.pushClip(this.cx + this.value5, this.cy, initialValue, this.ch);
    float pendingValue = this.cx + this.value5 - this.value4;
    if (this.checkCondition()) {
      float[] nextLength =
          compositorPushPresentationScale.textSelectionSpans(
              currentText,
              Math.min(this.calculateValue2(), currentText.length()),
              Math.min(this.calculateValue3(), currentText.length()),
              targetValue,
              TextMode.REGULAR,
              this.text4,
              this.value6);

      for (byte index = 0; index + 1 < nextLength.length; index += 2) {
        float activeValue =
            !this.enabled3 && !this.enabled4
                ? this.ch - 4.0F
                : Math.min(this.ch - 8.0F, targetValue + 8.0F);
        compositorPushPresentationScale.roundedRect(
            pendingValue + nextLength[index],
            this.cy + (this.ch - activeValue) / 2.0F,
            nextLength[index + 1],
            activeValue,
            2.0F,
            mulAlpha(previousValue, y));
      }
    }

    if (currentText.isEmpty() && !this.text3.isEmpty()) {
      compositorPushPresentationScale.text(
          pendingValue,
          defaultValue,
          this.text3,
          targetValue,
          mulAlpha(sourceValue, y),
          TextData.NONE.withFontFamily(this.text4).withLetterSpacing(this.value6));
    } else {
      compositorPushPresentationScale.text(
          pendingValue,
          defaultValue,
          currentText,
          targetValue,
          mulAlpha(nextValue, y),
          TextData.NONE.withFontFamily(this.text4).withLetterSpacing(this.value6));
    }

    if (this.enabled && (int) (this.value3 * 2.0F) % 2 == 0) {
      float fallbackValue = pendingValue + resolvedValue;
      float primaryValue =
          !this.enabled3 && !this.enabled4
              ? this.ch - 6.0F
              : Math.min(this.ch - 8.0F, targetValue + 8.0F);
      compositorPushPresentationScale.roundedRect(
          fallbackValue,
          this.cy + (this.ch - primaryValue) / 2.0F,
          !this.enabled3 && !this.enabled4 ? 1.0F : 2.0F,
          primaryValue,
          0.5F,
          mulAlpha(
              !this.enabled3 && !this.enabled4
                  ? nextValue
                  : (this.enabled5 ? MaterialIsLightService.ERROR : MaterialIsLightService.PRIMARY),
              y));
    }

    compositorPushPresentationScale.popClip();
  }

  private String createText2() {
    return this.enabled2 && !this.text2.isEmpty() ? "*".repeat(this.text2.length()) : this.text2;
  }

  private record Snapshot(String text, int cursor, int selStart) {}
}
