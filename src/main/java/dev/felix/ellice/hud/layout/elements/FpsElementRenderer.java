package dev.felix.ellice.hud.layout.elements;

import com.google.gson.JsonObject;
import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public final class FpsElementRenderer extends TextElementRenderer {
  @Override
  public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      LayoutIsContainerService layoutIsContainer,
      float width,
      float height,
      float value,
      float currentValue,
      LayoutOperationHandler layoutOperation,
      float nextValue,
      float previousValue) {
    super.draw(
        compositorPushPresentationScale,
        this.createLayoutIsContainerService(layoutIsContainer, "{fps} FPS"),
        width,
        height,
        value,
        currentValue,
        layoutOperation,
        nextValue,
        previousValue);
  }

  @Override
  public HudElementRenderer.Size measure(
      LayoutIsContainerService layoutIsContainer,
      LayoutOperationHandler layoutOperation,
      float value,
      float currentValue) {
    return super.measure(
        this.createLayoutIsContainerService(layoutIsContainer, "{fps} FPS"),
        layoutOperation,
        value,
        currentValue);
  }

  @Override
  public HudElementRenderer.Size measure(
      LayoutIsContainerService layoutIsContainer,
      LayoutOperationHandler layoutOperation,
      float value,
      float currentValue,
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return super.measure(
        this.createLayoutIsContainerService(layoutIsContainer, "{fps} FPS"),
        layoutOperation,
        value,
        currentValue,
        compositorPushPresentationScale);
  }

  private LayoutIsContainerService createLayoutIsContainerService(
      LayoutIsContainerService layoutIsContainer, String currentText) {
    if (layoutIsContainer.props != null && layoutIsContainer.props.has("text")) {
      return layoutIsContainer;
    }

    LayoutIsContainerService currentLayoutIsContainer =
        new LayoutIsContainerService(layoutIsContainer.id, layoutIsContainer.type);
    currentLayoutIsContainer.anchor = layoutIsContainer.anchor;
    currentLayoutIsContainer.offsetX = layoutIsContainer.offsetX;
    currentLayoutIsContainer.offsetY = layoutIsContainer.offsetY;
    currentLayoutIsContainer.width = layoutIsContainer.width;
    currentLayoutIsContainer.height = layoutIsContainer.height;
    currentLayoutIsContainer.rotation = layoutIsContainer.rotation;
    currentLayoutIsContainer.opacity = layoutIsContainer.opacity;
    currentLayoutIsContainer.visible = layoutIsContainer.visible;
    currentLayoutIsContainer.locked = layoutIsContainer.locked;
    currentLayoutIsContainer.props =
        layoutIsContainer.props != null ? layoutIsContainer.props.deepCopy() : new JsonObject();
    currentLayoutIsContainer.props.addProperty("text", currentText);
    return currentLayoutIsContainer;
  }
}
