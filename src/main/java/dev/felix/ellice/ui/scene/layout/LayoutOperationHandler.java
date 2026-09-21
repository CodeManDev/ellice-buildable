package dev.felix.ellice.ui.scene.layout;

public sealed interface LayoutOperationHandler
    permits LayoutOperationHandler.Auto, LayoutOperationHandler.Px, LayoutOperationHandler.Percent {
  LayoutOperationHandler.Auto AUTO = new LayoutOperationHandler.Auto();

  static LayoutOperationHandler auto() {
    return AUTO;
  }

  static LayoutOperationHandler px(float value) {
    return new LayoutOperationHandler.Px(value);
  }

  static LayoutOperationHandler percent(float value) {
    return new LayoutOperationHandler.Percent(value);
  }

  record Auto() implements LayoutOperationHandler {}

  record Percent(float value) implements LayoutOperationHandler {
    public Percent(float value) {
      if (Float.isFinite(value) && !(value < 0.0F)) {
        this.value = value;
      } else {
        throw new IllegalArgumentException("Percentage must be finite and >= 0: " + value);
      }
    }
  }

  record Px(float value) implements LayoutOperationHandler {
    public Px(float value) {
      if (Float.isFinite(value) && !(value < 0.0F)) {
        this.value = value;
      } else {
        throw new IllegalArgumentException("Pixel length must be finite and >= 0: " + value);
      }
    }
  }
}
