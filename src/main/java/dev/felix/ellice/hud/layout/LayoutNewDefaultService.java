package dev.felix.ellice.hud.layout;

import com.google.gson.JsonObject;

public final class LayoutNewDefaultService {
  private LayoutNewDefaultService() {}

  public static LayoutFindSelector newDefault() {
    LayoutFindSelector layoutFindSelector = new LayoutFindSelector();
    layoutFindSelector.name = "Default";
    layoutFindSelector.description = "Auto-generated on first run.";
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("fps"), "fps");
    layoutIsContainer.anchor = LayoutCodec.TOP_RIGHT;
    layoutIsContainer.offsetX = -8.0F;
    layoutIsContainer.offsetY = 8.0F;
    updateState(layoutIsContainer, "{fps} FPS", 9.0F, -1, "bold");
    updateState2(layoutIsContainer, 0.5F, 0.5F, Integer.MIN_VALUE);
    layoutFindSelector.elements.add(layoutIsContainer);
    LayoutIsContainerService currentLayoutIsContainer =
        new LayoutIsContainerService(createText2("coords"), "coords");
    currentLayoutIsContainer.anchor = LayoutCodec.BOTTOM_LEFT;
    currentLayoutIsContainer.offsetX = 8.0F;
    currentLayoutIsContainer.offsetY = -8.0F;
    updateState(currentLayoutIsContainer, "{player.coords}", 8.0F, -520093697, "regular");
    updateState2(currentLayoutIsContainer, 0.5F, 0.5F, Integer.MIN_VALUE);
    layoutFindSelector.elements.add(currentLayoutIsContainer);
    return layoutFindSelector;
  }

  public static LayoutIsContainerService createText() {
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("text"), "text");
    layoutIsContainer.anchor = LayoutCodec.CENTER;
    updateState(layoutIsContainer, "Text", 9.0F, -1, "regular");
    return layoutIsContainer;
  }

  public static LayoutIsContainerService createRect() {
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("rect"), "rect");
    layoutIsContainer.anchor = LayoutCodec.CENTER;
    layoutIsContainer.width = 80.0F;
    layoutIsContainer.height = 20.0F;
    layoutIsContainer.props.addProperty("color", "#80202028");
    layoutIsContainer.props.addProperty("cornerRadius", 6);
    return layoutIsContainer;
  }

  public static LayoutIsContainerService createBar(String text) {
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("bar"), "bar");
    layoutIsContainer.anchor = LayoutCodec.BOTTOM_CENTER;
    layoutIsContainer.offsetX = 0.0F;
    layoutIsContainer.offsetY = -28.0F;
    layoutIsContainer.width = 80.0F;
    layoutIsContainer.height = 6.0F;
    layoutIsContainer.props.addProperty("fillFrom", text);
    layoutIsContainer.props.addProperty("fillColor", "#FF22D399");
    layoutIsContainer.props.addProperty("trackColor", "#40FFFFFF");
    layoutIsContainer.props.addProperty("cornerRadius", 3);
    return layoutIsContainer;
  }

  public static LayoutIsContainerService createArmorHud() {
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("armorhud"), "armorhud");
    layoutIsContainer.anchor = LayoutCodec.CENTER;
    layoutIsContainer.props.addProperty("orientation", "horizontal");
    layoutIsContainer.props.addProperty("size", 26);
    layoutIsContainer.props.addProperty("gap", 5);
    return layoutIsContainer;
  }

  public static LayoutIsContainerService createKeystrokes() {
    LayoutIsContainerService layoutIsContainer =
        new LayoutIsContainerService(createText2("keystrokes"), "keystrokes");
    layoutIsContainer.anchor = LayoutCodec.CENTER;
    layoutIsContainer.props.addProperty("size", 16);
    layoutIsContainer.props.addProperty("gap", 3);
    layoutIsContainer.props.addProperty("showMouse", true);
    layoutIsContainer.props.addProperty("showSpace", true);
    return layoutIsContainer;
  }

  private static void updateState(
      LayoutIsContainerService layoutIsContainer,
      String currentText,
      float value,
      int currentValue,
      String nextText) {
    JsonObject jsonObject = layoutIsContainer.props;
    jsonObject.addProperty("text", currentText);
    jsonObject.addProperty("fontSize", value);
    jsonObject.addProperty("color", String.format("#%08X", currentValue));
    jsonObject.addProperty("variant", nextText);
  }

  private static void updateState2(
      LayoutIsContainerService layoutIsContainer, float value, float currentValue, int nextValue) {
    JsonObject jsonObject = layoutIsContainer.props;
    jsonObject.addProperty("shadowX", value);
    jsonObject.addProperty("shadowY", currentValue);
    jsonObject.addProperty("shadowColor", String.format("#%08X", nextValue));
  }

  private static String createText2(String text) {
    return text
        + "-"
        + Long.toHexString(System.nanoTime() ^ System.identityHashCode(text)).substring(0, 6);
  }
}
