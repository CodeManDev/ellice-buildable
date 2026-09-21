package dev.felix.ellice.feature.nametags;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.text.TextCodec;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.Objects;

public final class NametagsRenderer implements ComponentOperationHandler {
  private NametagsData nametagsData;
  private float value2;
  private boolean enabled;

  public NametagsRenderer(NametagsData nametagsData, float value, boolean enabled) {
    this.update(nametagsData, value, enabled);
  }

  public boolean update(NametagsData currentNametagsData, float value, boolean currentEnabled) {
    int currentValue =
        Objects.equals(this.nametagsData, currentNametagsData)
                && this.value2 == value
                && this.enabled == currentEnabled
            ? 0
            : 1;
    this.nametagsData = Objects.requireNonNull(currentNametagsData);
    this.value2 = value;
    this.enabled = currentEnabled;
    return (currentValue != 0);
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
    NametagsData currentNametagsData = this.nametagsData;
    float currentWidth = this.value2;
    boolean light = MaterialIsLightService.isLight();
    String currentText =
        light ? TextCodec.normalizeWhite(currentNametagsData.name()) : currentNametagsData.name();
    String nextText =
        light
            ? TextCodec.normalizeWhite(currentNametagsData.detail())
            : currentNametagsData.detail();
    ArrayList arrayList = new ArrayList();
    if (currentNametagsData.item()) {
      arrayList.add(
          ComponentBoxService.<NametagsItemService>node(
                  "nametag-icon",
                  NametagsItemService::new,
                  currentItem ->
                      currentItem
                          .id("nametag-icon")
                          .item(currentNametagsData.icon(), currentNametagsData.accent()))
              .style(
                  ComponentStyleService.style()
                      .size(
                          LayoutOperationHandler.px(18.0F * currentWidth),
                          LayoutOperationHandler.px(18.0F * currentWidth))
                      .shrink(0.0F)
                      .build())
              .key("icon"));
    }

    arrayList.add(
        ComponentBoxService.text(
                currentText,
                currentItem ->
                    currentItem
                        .id("nametag-name")
                        .fontSize(10.5F * currentWidth)
                        .lineHeight(20.0F * currentWidth)
                        .fontFamily("material-roboto-medium")
                        .color(MaterialIsLightService.ON_SURFACE)
                        .overflow(SceneTextService.Overflow.ELLIPSIS)
                        .textAlign(
                            !currentNametagsData.item() && currentNametagsData.detail().isEmpty()
                                ? SceneTextService.TextAlign.CENTER
                                : SceneTextService.TextAlign.LEFT))
            .style(
                ComponentStyleService.style()
                    .height(LayoutOperationHandler.px(20.0F * currentWidth))
                    .minWidth(LayoutOperationHandler.px(0.0F))
                    .grow(1.0F)
                    .build())
            .key("name"));
    if (!nextText.isEmpty()) {
      arrayList.add(
          ComponentBoxService.text(
                  nextText,
                  currentItem ->
                      currentItem
                          .id("nametag-detail")
                          .fontSize(8.5F * currentWidth)
                          .lineHeight(20.0F * currentWidth)
                          .fontFamily("material-roboto")
                          .color(
                              currentNametagsData.item()
                                  ? currentNametagsData.accent()
                                  : MaterialIsLightService.ON_SURFACE_VARIANT))
              .style(
                  ComponentStyleService.style()
                      .height(LayoutOperationHandler.px(20.0F * currentWidth))
                      .shrink(0.0F)
                      .build())
              .key("detail"));
    }

    ArrayList currentArrayList = new ArrayList();
    currentArrayList.add(
        ComponentBoxService.row(
                item ->
                    item.id("nametag-row")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(20.0F * currentWidth))
                        .gap(7.0F * currentWidth)
                        .align(ScenePctService.Align.CENTER),
                arrayList.toArray(ComponentKeyService[]::new))
            .key("row"));
    if (currentNametagsData.health() >= 0.0F) {
      boolean currentEnabled = this.enabled;
      currentArrayList.add(
          ComponentBoxService.<NametagsValueService>node(
                  "nametag-health",
                  NametagsValueService::new,
                  item ->
                      item.id("nametag-health")
                          .value(
                              currentNametagsData.health(),
                              currentNametagsData.absorption(),
                              currentEnabled))
              .style(
                  ComponentStyleService.style()
                      .width(LayoutOperationHandler.percent(100.0F))
                      .height(LayoutOperationHandler.px(4.0F * currentWidth))
                      .build())
              .key("health"));
    }

    boolean nextEnabled = this.enabled;
    return ComponentBoxService.panel(
            ComponentStyleService.style()
                .width(LayoutOperationHandler.percent(100.0F))
                .height(LayoutOperationHandler.percent(100.0F))
                .padding(6.0F * currentWidth, 10.0F * currentWidth)
                .gap(4.0F * currentWidth)
                .build(),
            item -> {
              item.direction(ScenePctService.Direction.COLUMN)
                  .cornerRadius(7.0F * currentWidth)
                  .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                  .shadow(7.0F * currentWidth)
                  .shadowColor(1342177280)
                  .pointerEvents(false);
              if (!nextEnabled) {
                MotionAnimateService.cancel(item, MotionColorsContainer.Floats.OPACITY);
                MotionAnimateService.cancel(item, MotionColorsContainer.Floats.TRANSLATE_Y);
                item.opacity(1.0F).translateY(0.0F);
              }
            },
            currentArrayList.toArray(ComponentKeyService[]::new))
        .key("nametag")
        .onMount(
            item -> {
              if (nextEnabled) {
                item.opacity(0.0F);
                MotionColorsContainer.Floats.TRANSLATE_Y.set(item, 2.0F * currentWidth);
                MotionAnimateService.animate(
                    item,
                    MotionColorsContainer.Floats.OPACITY,
                    1.0F,
                    SceneEaseHandler.Tween.ease(0.14F));
                MotionAnimateService.animate(
                    item,
                    MotionColorsContainer.Floats.TRANSLATE_Y,
                    0.0F,
                    SceneEaseHandler.Tween.ease(0.14F));
              }
            });
  }

  public static float height(NametagsData nametagsData, float value) {
    return (nametagsData.health() >= 0.0F ? 40 : 32) * value;
  }

  public static float width(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      NametagsData nametagsData,
      float value,
      float currentValue) {
    float nextValue =
        compositorPushPresentationScale.textWidth(
                nametagsData.name(), 10.5F * value, TextMode.REGULAR, "material-roboto-medium")
            + (nametagsData.detail().isEmpty()
                ? 0.0F
                : compositorPushPresentationScale.textWidth(
                        nametagsData.detail(), 8.5F * value, TextMode.REGULAR, "material-roboto")
                    + 6.0F * value)
            + (nametagsData.item() ? 45 : 20) * value;
    return Math.min(currentValue, Math.clamp(nextValue, 48.0F * value, 220.0F * value));
  }
}
