package dev.felix.ellice.feature.esp;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.theme.ThemeMixService;

public final class EspValueService extends ScenePctService<EspValueService> {
  public static final MotionFiniteService HEALTH = createMotionFiniteService("espHealth");
  public static final MotionFiniteService DAMAGE = createMotionFiniteService("espDamage");
  public static final MotionFiniteService ABSORPTION = createMotionFiniteService("espAbsorption");
  public static final MotionFiniteService ARMOR = createMotionFiniteService("espArmor");
  private static final int count = -435153635;
  private EspData espData = EspData.defaults();
  private EspLabelData espData2;
  private int count2;
  private float value2;
  private float value3;
  private float value4;
  private float value5;
  private float value6 = -1.0F;
  private float value7 = -1.0F;
  private float value8 = -1.0F;

  public EspValueService value(
      EspLabelData espLabelData, EspData currentEspData, int currentValue) {
    this.espData2 = espLabelData;
    this.espData = currentEspData;
    this.count2 = currentValue;
    int nextValue = !(this.value6 < 0.0F) && currentEspData.animations() ? 0 : 1;
    if (nextValue != 0) {
      this.updateState6(HEALTH, espLabelData.health());
      this.updateState6(DAMAGE, espLabelData.health());
      this.updateState6(ABSORPTION, espLabelData.absorption());
      this.updateState6(ARMOR, espLabelData.armor());
    } else {
      if (espLabelData.health() != this.value6) {
        MotionAnimateService.animate(
            this, HEALTH, espLabelData.health(), SceneEaseHandler.Tween.ease(0.16F));
        MotionAnimateService.animate(
            this,
            DAMAGE,
            espLabelData.health(),
            SceneEaseHandler.Tween.ease(0.36F),
            espLabelData.health() < this.value2 ? 0.1F : 0.0F);
      }

      if (espLabelData.absorption() != this.value7) {
        MotionAnimateService.animate(
            this, ABSORPTION, espLabelData.absorption(), SceneEaseHandler.Tween.ease(0.18F));
      }

      if (espLabelData.armor() != this.value8) {
        MotionAnimateService.animate(
            this, ARMOR, espLabelData.armor(), SceneEaseHandler.Tween.ease(0.18F));
      }
    }

    this.value6 = espLabelData.health();
    this.value7 = espLabelData.absorption();
    this.value8 = espLabelData.armor();
    return this;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.espData2 != null && !(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
      int y =
          switch (this.espData.colorMode()) {
            case "Health" -> healthColor(this.value2);
            case "Team" ->
                this.espData2.teamColor() == 0 ? this.espData.color() : this.espData2.teamColor();
            default -> this.espData.color();
          };
      int width = mulAlpha(y, this.effectiveOpacity);
      int height = mulAlpha(-435153635, this.effectiveOpacity);
      float value = this.espData.lineWidth();
      int currentValue = mulAlpha(y, this.espData.fillOpacity() * this.effectiveOpacity);
      if (!this.espData.corners() && this.count2 == 0) {
        this.updateState(compositorPushPresentationScale, 0, height, value + 1.35F);
        this.updateState(compositorPushPresentationScale, currentValue, width, value);
      } else {
        if (this.espData.fillOpacity() > 0.0F) {
          compositorPushPresentationScale.rect(this.cx, this.cy, this.cw, this.ch, currentValue);
        }

        this.updateState2(compositorPushPresentationScale, value + 1.35F, height);
        this.updateState2(compositorPushPresentationScale, value, width);
      }

      this.updateState5(compositorPushPresentationScale);
    }
  }

  private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      int value,
      int currentValue,
      float nextValue) {
    float previousValue = 0.85F;
    compositorPushPresentationScale.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        previousValue,
        previousValue,
        previousValue,
        previousValue,
        value,
        0.0F,
        0.0F,
        0,
        nextValue,
        currentValue);
  }

  private void updateState2(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      int currentValue) {
    float nextValue = value * this.presentationScale;
    float previousValue = Math.min(this.cw * 0.26F, 12.0F * this.presentationScale);
    float sourceValue = Math.min(this.ch * 0.2F, 12.0F * this.presentationScale);
    boolean enabled = this.checkCondition(1);
    boolean currentEnabled = this.checkCondition(2);
    boolean nextEnabled = this.checkCondition(4);
    boolean previousEnabled = this.checkCondition(8);
    if (!this.espData.corners()) {
      if (nextEnabled) {
        updateState4(
            compositorPushPresentationScale,
            this.cx,
            this.cy,
            this.cx + this.cw,
            this.cy,
            nextValue,
            currentValue);
      }

      if (previousEnabled) {
        updateState4(
            compositorPushPresentationScale,
            this.cx,
            this.cy + this.ch,
            this.cx + this.cw,
            this.cy + this.ch,
            nextValue,
            currentValue);
      }

      if (enabled) {
        updateState4(
            compositorPushPresentationScale,
            this.cx,
            this.cy,
            this.cx,
            this.cy + this.ch,
            nextValue,
            currentValue);
      }

      if (currentEnabled) {
        updateState4(
            compositorPushPresentationScale,
            this.cx + this.cw,
            this.cy,
            this.cx + this.cw,
            this.cy + this.ch,
            nextValue,
            currentValue);
      }
    } else {
      if (enabled && nextEnabled) {
        updateState3(
            compositorPushPresentationScale,
            this.cx,
            this.cy,
            previousValue,
            sourceValue,
            nextValue,
            currentValue);
      }

      if (currentEnabled && nextEnabled) {
        updateState3(
            compositorPushPresentationScale,
            this.cx + this.cw,
            this.cy,
            -previousValue,
            sourceValue,
            nextValue,
            currentValue);
      }

      if (enabled && previousEnabled) {
        updateState3(
            compositorPushPresentationScale,
            this.cx,
            this.cy + this.ch,
            previousValue,
            -sourceValue,
            nextValue,
            currentValue);
      }

      if (currentEnabled && previousEnabled) {
        updateState3(
            compositorPushPresentationScale,
            this.cx + this.cw,
            this.cy + this.ch,
            -previousValue,
            -sourceValue,
            nextValue,
            currentValue);
      }
    }
  }

  private static void updateState3(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      int targetValue) {
    updateState4(
        compositorPushPresentationScale,
        value,
        currentValue,
        value + nextValue,
        currentValue,
        sourceValue,
        targetValue);
    updateState4(
        compositorPushPresentationScale,
        value,
        currentValue,
        value,
        currentValue + previousValue,
        sourceValue,
        targetValue);
  }

  private static void updateState4(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      int targetValue) {
    compositorPushPresentationScale.rect(
        Math.min(value, nextValue) - sourceValue * 0.5F,
        Math.min(currentValue, previousValue) - sourceValue * 0.5F,
        Math.abs(nextValue - value) + sourceValue,
        Math.abs(previousValue - currentValue) + sourceValue,
        targetValue);
  }

  private void updateState5(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float value = this.presentationScale;
    float currentValue = this.effectiveOpacity;
    if (this.espData.healthBar()
        && this.checkCondition(1)
        && this.checkCondition(4)
        && this.checkCondition(8)
        && this.ch >= 6.0F * value) {
      float nextValue = this.cx - 5.0F * value;
      compositorPushPresentationScale.roundedRect(
          nextValue - 0.75F * value,
          this.cy - 0.75F * value,
          3.5F * value,
          this.ch + 1.5F * value,
          0.65F,
          mulAlpha(-435153635, currentValue));
      compositorPushPresentationScale.rect(
          nextValue, this.cy, 2.0F * value, this.ch, mulAlpha(-1289079744, currentValue));
      if (this.value3 > this.value2 + 0.001F) {
        compositorPushPresentationScale.rect(
            nextValue,
            this.cy + this.ch * (1.0F - this.value3),
            2.0F * value,
            this.ch * this.value3,
            mulAlpha(-588788053, currentValue));
      }

      if (this.value2 > 0.0F) {
        compositorPushPresentationScale.rect(
            nextValue,
            this.cy + this.ch * (1.0F - this.value2),
            2.0F * value,
            this.ch * this.value2,
            mulAlpha(healthColor(this.value2), currentValue));
      }

      if (this.value4 > 0.001F) {
        compositorPushPresentationScale.rect(
            nextValue - 2.5F * value,
            this.cy - 0.5F * value,
            2.0F * value,
            this.ch + value,
            mulAlpha(-435153635, currentValue));
        compositorPushPresentationScale.rect(
            nextValue - 2.0F * value,
            this.cy + this.ch * (1.0F - this.value4),
            value,
            this.ch * this.value4,
            mulAlpha(-996232, currentValue));
      }
    }

    if (this.espData.armorBar()
        && this.value5 > 0.001F
        && this.checkCondition(8)
        && this.checkCondition(1)
        && this.checkCondition(2)
        && this.cw >= 10.0F * value) {
      float previousValue = this.cy + this.ch + 3.5F * value;
      compositorPushPresentationScale.roundedRect(
          this.cx - 0.6F * value,
          previousValue - 0.6F * value,
          this.cw + 1.2F * value,
          2.6F * value,
          0.55F,
          mulAlpha(-435153635, currentValue));
      compositorPushPresentationScale.rect(
          this.cx, previousValue, this.cw, 1.4F * value, mulAlpha(-1289079744, currentValue));
      compositorPushPresentationScale.rect(
          this.cx,
          previousValue,
          this.cw * this.value5,
          1.4F * value,
          mulAlpha(-5584914, currentValue));
    }
  }

  private boolean checkCondition(int value) {
    return (this.count2 & value) == 0;
  }

  public static int healthColor(float value) {
    float currentValue = Math.clamp(value, 0.0F, 1.0F);
    return currentValue < 0.5F
        ? ThemeMixService.mix(-1081719, -1390725, currentValue * 2.0F)
        : ThemeMixService.mix(-1390725, -7545935, (currentValue - 0.5F) * 2.0F);
  }

  private void updateState6(MotionFiniteService motionFinite, float value) {
    MotionAnimateService.cancel(this, motionFinite);
    motionFinite.set(this, value);
  }

  private static MotionFiniteService createMotionFiniteService(String text) {
    return MotionFiniteService.of(
        text,
        item -> item instanceof EspValueService,
        item -> Float.isFinite(item) && item >= 0.0F && item <= 1.0F,
        "a fraction in [0, 1]");
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "espHealth" -> this.value2;
      case "espDamage" -> this.value3;
      case "espAbsorption" -> this.value4;
      case "espArmor" -> this.value5;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float value) {
    switch (text) {
      case "espHealth":
        this.value2 = value;
        break;
      case "espDamage":
        this.value3 = value;
        break;
      case "espAbsorption":
        this.value4 = value;
        break;
      case "espArmor":
        this.value5 = value;
        break;
      default:
        super.setAnimProperty(text, value);
    }
  }
}
