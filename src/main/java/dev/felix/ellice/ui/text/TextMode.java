package dev.felix.ellice.ui.text;

public enum TextMode {
  REGULAR(0, ""),
  BOLD(1, "-bold"),
  ITALIC(2, "-italic"),
  BOLD_ITALIC(3, "-bolditalic");

  private final int count;
  private final String text2;

  TextMode(int value, String text) {
    this.count = value;
    this.text2 = text;
  }

  public int awtStyle() {
    return this.count;
  }

  public String msdfSuffix() {
    return this.text2;
  }

  public static TextMode of(boolean enabled, boolean currentEnabled) {
    if (enabled && currentEnabled) {
      return BOLD_ITALIC;
    } else if (enabled) {
      return BOLD;
    } else {
      return currentEnabled ? ITALIC : REGULAR;
    }
  }

  public static TextMode parse(String text) {
    if (text == null) {
      return REGULAR;
    }

    return switch (text.toLowerCase().replace('_', '-')) {
      case "bold" -> BOLD;
      case "italic" -> ITALIC;
      case "bold-italic", "bolditalic" -> BOLD_ITALIC;
      default -> REGULAR;
    };
  }

  private static TextMode[] $values() {
    return new TextMode[] {REGULAR, BOLD, ITALIC, BOLD_ITALIC};
  }
}
