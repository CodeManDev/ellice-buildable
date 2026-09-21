package dev.felix.ellice.ui.scene;

import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.function.BiConsumer;

public final class SceneResizeMarginService extends SceneCornerRadiusService {
  private static final int count = 0;
  private static final int count2 = 1;
  private static final int count3 = 2;
  private static final int count4 = 4;
  private static final int count5 = 8;
  private static final int count6 = -1;
  private float value = 8.0F;
  private float value2 = 96.0F;
  private float value3 = 72.0F;
  private float value4 = Float.POSITIVE_INFINITY;
  private BiConsumer<Float, Float> biConsumer;
  private float value5 = -1.0F;
  private float value6 = -1.0F;
  private boolean enabled = true;
  private int count7 = -1;
  private float value7;
  private float value8;
  private float value9 = 1.0F;
  private float value10;
  private float value11;
  private float value12;
  private float value13;

  public SceneResizeMarginService() {
    this.interactive = true;
    this.stopPropagation = true;
    this.layerBreak = true;
    this.cursorStyle = ScenePctService.CursorStyle.POINTER;
    this.updateState2();
  }

  public SceneResizeMarginService resizeMargin(float currentValue) {
    if (Float.isFinite(currentValue) && !(currentValue < 0.0F)) {
      this.value = currentValue;
      return this;
    } else {
      throw new IllegalArgumentException("resize margin must be finite and >= 0: " + currentValue);
    }
  }

  public SceneResizeMarginService moveHandleHeight(float value) {
    if (Float.isFinite(value) && !(value < 0.0F)) {
      this.value4 = value;
      return this;
    } else {
      throw new IllegalArgumentException("Invalid title bar height");
    }
  }

  public SceneResizeMarginService onResize(BiConsumer<Float, Float> currentBiConsumer) {
    this.biConsumer = currentBiConsumer;
    return this;
  }

  private void updateState(float value, float currentValue) {
    if (value != this.value5 || currentValue != this.value6) {
      this.value5 = value;
      this.value6 = currentValue;
      if (this.biConsumer != null) {
        this.biConsumer.accept(value, currentValue);
      }
    }
  }

  public SceneResizeMarginService minimumSize(float value, float currentValue) {
    if (!Float.isFinite(value) || value < 0.0F) {
      throw new IllegalArgumentException("minimum width must be finite and >= 0: " + value);
    } else if (Float.isFinite(currentValue) && !(currentValue < 0.0F)) {
      this.value2 = value;
      this.value3 = currentValue;
      this.updateState2();
      return this;
    } else {
      throw new IllegalArgumentException("minimum height must be finite and >= 0: " + currentValue);
    }
  }

  public SceneResizeMarginService anchored(boolean currentEnabled) {
    this.enabled = currentEnabled;
    return this;
  }

  public boolean isAnchored() {
    return this.enabled;
  }

  public SceneResizeMarginService constrainToParent() {
    SceneResizeMarginService.ParentBox parentBox = this.createParentBox();
    if (parentBox == null) {
      return this;
    }

    this.updateState3(parentBox.width, parentBox.height);
    if (this.enabled) {
      return this;
    }

    float value =
        calculateValue3(this.width, calculateValue(this.value2, parentBox.width), parentBox.width);
    float currentValue =
        calculateValue3(
            this.height, calculateValue(this.value3, parentBox.height), parentBox.height);
    float nextValue =
        calculateValue3(this.x + this.marginLeft, 0.0F, Math.max(0.0F, parentBox.width - value));
    float previousValue =
        calculateValue3(
            this.y + this.marginTop, 0.0F, Math.max(0.0F, parentBox.height - currentValue));
    this.position(nextValue - this.marginLeft, previousValue - this.marginTop);
    this.size(value, currentValue);
    this.updateState(value, currentValue);
    return this;
  }

  @Override
  protected void onScenePress(float currentValue, float nextValue) {
    ScenePctService.PresentationTransform currentPresentationTransform =
        this.presentationTransform();
    float previousValue = currentPresentationTransform.inverseX(currentValue);
    float sourceValue = currentPresentationTransform.inverseY(nextValue);
    if (this.parent != null) {
      this.parent.bringChildToFront(this);
    }

    if (Float.isFinite(previousValue)
        && Float.isFinite(sourceValue)
        && this.checkCondition2()
        && this.checkCondition()) {
      this.constrainToParent();
      SceneResizeMarginService.ParentBox parentBox = this.createParentBox();
      if (parentBox == null) {
        this.count7 = -1;
      } else {
        this.value7 = currentValue;
        this.value8 = nextValue;
        ScenePctService.PresentationTransform nextPresentationTransform =
            this.parent == null
                ? ScenePctService.PresentationTransform.IDENTITY
                : this.parent.presentationTransform();
        this.value9 = Math.max(1.0E-6F, nextPresentationTransform.scale());
        this.value10 = this.x + this.marginLeft;
        this.value11 = this.y + this.marginTop;
        this.value12 = Math.min(this.width, parentBox.width);
        this.value13 = Math.min(this.height, parentBox.height);
        float targetValue = previousValue - this.cx;
        float inputValue = sourceValue - this.cy;
        this.count7 = 0;
        if (targetValue <= this.value) {
          this.count7 |= 1;
        } else if (targetValue >= this.cw - this.value) {
          this.count7 |= 2;
        }

        if (inputValue <= this.value) {
          this.count7 |= 4;
        } else if (inputValue >= this.ch - this.value) {
          this.count7 |= 8;
        }

        if (this.count7 == 0 && inputValue > this.value4) {
          this.count7 = -1;
        }
      }
    } else {
      this.count7 = -1;
    }
  }

  @Override
  protected void updateWhileScenePressed(float value, float currentValue) {
    if (this.count7 != -1) {
      SceneResizeMarginService.ParentBox parentBox = this.createParentBox();
      if (parentBox != null) {
        float nextValue = calculateValue2(value, this.value7) / this.value9;
        float previousValue = calculateValue2(currentValue, this.value8) / this.value9;
        float sourceValue = calculateValue(this.value2, parentBox.width);
        float targetValue = calculateValue(this.value3, parentBox.height);
        if (this.count7 == 0) {
          float inputValue =
              calculateValue3(
                  this.value10 + nextValue, 0.0F, Math.max(0.0F, parentBox.width - this.value12));
          float outputValue =
              calculateValue3(
                  this.value11 + previousValue,
                  0.0F,
                  Math.max(0.0F, parentBox.height - this.value13));
          this.position(inputValue - this.marginLeft, outputValue - this.marginTop);
        } else {
          float resultValue = this.value10;
          float candidateValue = this.value10 + this.value12;
          float selectedValue = this.value11;
          float defaultValue = this.value11 + this.value13;
          if ((this.count7 & 1) != 0) {
            resultValue =
                calculateValue3(this.value10 + nextValue, 0.0F, candidateValue - sourceValue);
          } else if ((this.count7 & 2) != 0) {
            candidateValue =
                calculateValue3(
                    candidateValue + nextValue, resultValue + sourceValue, parentBox.width);
          }

          if ((this.count7 & 4) != 0) {
            selectedValue =
                calculateValue3(this.value11 + previousValue, 0.0F, defaultValue - targetValue);
          } else if ((this.count7 & 8) != 0) {
            defaultValue =
                calculateValue3(
                    defaultValue + previousValue, selectedValue + targetValue, parentBox.height);
          }

          this.position(resultValue - this.marginLeft, selectedValue - this.marginTop);
          this.size(candidateValue - resultValue, defaultValue - selectedValue);
          this.updateState(candidateValue - resultValue, defaultValue - selectedValue);
        }
      }
    }
  }

  private boolean checkCondition() {
    if (!this.enabled) {
      return true;
    } else {
      SceneResizeMarginService.ParentBox parentBox = this.createParentBox();
      if (parentBox != null && this.checkCondition2()) {
        float value = this.cx - parentBox.x - this.marginLeft;
        float currentValue = this.cy - parentBox.y - this.marginTop;
        this.absolute().inset(LayoutOperationHandler.auto());
        this.position(value, currentValue);
        this.size(this.cw, this.ch);
        this.enabled = false;
        return true;
      } else {
        return false;
      }
    }
  }

  private boolean checkCondition2() {
    return Float.isFinite(this.cx)
        && Float.isFinite(this.cy)
        && Float.isFinite(this.cw)
        && Float.isFinite(this.ch)
        && this.cw > 0.0F
        && this.ch > 0.0F;
  }

  private SceneResizeMarginService.ParentBox createParentBox() {
    if (this.parent != null
        && Float.isFinite(this.parent.cx)
        && Float.isFinite(this.parent.cy)
        && Float.isFinite(this.parent.cw)
        && Float.isFinite(this.parent.ch)) {
      float value = this.parent.cx + this.parent.paddingLeft;
      float currentValue = this.parent.cy + this.parent.paddingTop;
      if (this.parent.scrollable && this.parent.direction != ScenePctService.Direction.NONE) {
        if (this.parent.direction == ScenePctService.Direction.ROW) {
          value += this.parent.scrollY;
        } else {
          currentValue += this.parent.scrollY;
        }
      }

      float nextValue =
          Math.max(0.0F, this.parent.cw - this.parent.paddingLeft - this.parent.paddingRight);
      float previousValue =
          Math.max(0.0F, this.parent.ch - this.parent.paddingTop - this.parent.paddingBottom);
      return Float.isFinite(nextValue) && Float.isFinite(previousValue)
          ? new SceneResizeMarginService.ParentBox(value, currentValue, nextValue, previousValue)
          : null;
    } else {
      return null;
    }
  }

  private void updateState2() {
    SceneResizeMarginService.ParentBox parentBox = this.createParentBox();
    if (parentBox == null) {
      this.minWidth(this.value2);
      this.minHeight(this.value3);
    } else {
      this.updateState3(parentBox.width, parentBox.height);
    }
  }

  private void updateState3(float value, float currentValue) {
    this.minWidth(calculateValue(this.value2, value));
    this.minHeight(calculateValue(this.value3, currentValue));
  }

  private static float calculateValue(float value, float currentValue) {
    return Math.max(0.0F, Math.min(value, Math.max(0.0F, currentValue)));
  }

  private static float calculateValue2(float value, float currentValue) {
    float nextValue = value - currentValue;
    return Float.isFinite(nextValue) ? nextValue : 0.0F;
  }

  private static float calculateValue3(float value, float currentValue, float nextValue) {
    float previousValue = Math.min(currentValue, nextValue);
    float sourceValue = Math.max(currentValue, nextValue);
    return !Float.isFinite(value)
        ? previousValue
        : Math.max(previousValue, Math.min(sourceValue, value));
  }

  private record ParentBox(float x, float y, float width, float height) {}
}
