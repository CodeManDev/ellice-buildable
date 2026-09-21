package dev.felix.ellice.ui.theme;

import java.util.Objects;

public record ThemeCornerData(
    ThemeCornerData.NotificationMode notifications,
    ThemeCornerData.NotificationDuration notificationDuration,
    ThemeCornerData.Corner notificationCorner,
    int notificationLimit,
    boolean tooltips,
    float tooltipDelay,
    float scrollSpeed,
    boolean rememberClickGui,
    boolean focusSearch,
    boolean dimBackground,
    boolean discordRichPresence) {
  private static ThemeCornerData current = defaults();

  public ThemeCornerData(
      ThemeCornerData.NotificationMode notifications,
      ThemeCornerData.NotificationDuration notificationDuration,
      ThemeCornerData.Corner notificationCorner,
      int notificationLimit,
      boolean tooltips,
      float tooltipDelay,
      float scrollSpeed,
      boolean rememberClickGui,
      boolean focusSearch,
      boolean dimBackground,
      boolean discordRichPresence) {
    if (notifications == null) {
      notifications = ThemeCornerData.NotificationMode.ALL;
    }

    if (notificationDuration == null) {
      notificationDuration = ThemeCornerData.NotificationDuration.NORMAL;
    }

    if (notificationCorner == null) {
      notificationCorner = ThemeCornerData.Corner.BOTTOM_RIGHT;
    }

    notificationLimit = Math.clamp(notificationLimit, 1, 5);
    tooltipDelay = Float.isFinite(tooltipDelay) ? Math.clamp(tooltipDelay, 0.0F, 1.5F) : 0.4F;
    scrollSpeed = Float.isFinite(scrollSpeed) ? Math.clamp(scrollSpeed, 0.5F, 2.0F) : 1.0F;
    this.notifications = notifications;
    this.notificationDuration = notificationDuration;
    this.notificationCorner = notificationCorner;
    this.notificationLimit = notificationLimit;
    this.tooltips = tooltips;
    this.tooltipDelay = tooltipDelay;
    this.scrollSpeed = scrollSpeed;
    this.rememberClickGui = rememberClickGui;
    this.focusSearch = focusSearch;
    this.dimBackground = dimBackground;
    this.discordRichPresence = discordRichPresence;
  }

  public ThemeCornerData(
      ThemeCornerData.NotificationMode notificationMode,
      ThemeCornerData.NotificationDuration notificationDuration,
      ThemeCornerData.Corner corner,
      int value,
      boolean enabled,
      float currentValue,
      float nextValue,
      boolean currentEnabled,
      boolean nextEnabled,
      boolean previousEnabled) {
    this(
        notificationMode,
        notificationDuration,
        corner,
        value,
        enabled,
        currentValue,
        nextValue,
        currentEnabled,
        nextEnabled,
        previousEnabled,
        true);
  }

  public static ThemeCornerData defaults() {
    return new ThemeCornerData(
        ThemeCornerData.NotificationMode.ALL,
        ThemeCornerData.NotificationDuration.NORMAL,
        ThemeCornerData.Corner.BOTTOM_RIGHT,
        3,
        true,
        0.4F,
        1.0F,
        true,
        false,
        true,
        true);
  }

  public static ThemeCornerData current() {
    return current;
  }

  public static void apply(ThemeCornerData themeCornerData) {
    current = Objects.requireNonNull(themeCornerData);
  }

  public boolean acceptsNotification(boolean enabled) {
    return this.notifications == ThemeCornerData.NotificationMode.ALL
        || this.notifications == ThemeCornerData.NotificationMode.IMPORTANT && enabled;
  }

  public enum Corner {
    TOP_LEFT("Top left", true, true),
    TOP_RIGHT("Top right", false, true),
    BOTTOM_LEFT("Bottom left", true, false),
    BOTTOM_RIGHT("Bottom right", false, false);

    private final String text;
    private final boolean enabled;
    private final boolean enabled2;

    Corner(String currentText, boolean currentEnabled, boolean nextEnabled) {
      this.text = currentText;
      this.enabled = currentEnabled;
      this.enabled2 = nextEnabled;
    }

    public String label() {
      return this.text;
    }

    public boolean left() {
      return this.enabled;
    }

    public boolean top() {
      return this.enabled2;
    }

    private static ThemeCornerData.Corner[] $values() {
      return new ThemeCornerData.Corner[] {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    }
  }

  public enum NotificationDuration {
    SHORT("Short", 0.65F),
    NORMAL("Normal", 1.0F),
    LONG("Long", 1.75F);

    private final String text2;
    private final float value;

    NotificationDuration(String text, float currentValue) {
      this.text2 = text;
      this.value = currentValue;
    }

    public String label() {
      return this.text2;
    }

    public float scale() {
      return this.value;
    }

    private static ThemeCornerData.NotificationDuration[] $values() {
      return new ThemeCornerData.NotificationDuration[] {SHORT, NORMAL, LONG};
    }
  }

  public enum NotificationMode {
    ALL("All"),
    IMPORTANT("Important"),
    OFF("Off");

    private final String text3;

    NotificationMode(String text) {
      this.text3 = text;
    }

    public String label() {
      return this.text3;
    }

    private static ThemeCornerData.NotificationMode[] $values() {
      return new ThemeCornerData.NotificationMode[] {ALL, IMPORTANT, OFF};
    }
  }
}
