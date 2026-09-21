package dev.felix.ellice.diagnostics;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class DiagnosticsAddFrameListenerService {
  public static final int HISTORY = 240;
  public static final String[] SECTIONS =
      new String[] {"render3d", "world-postfx", "input", "scene", "compositor", "other"};
  public static final String[] SECTIONS_COMPOSITOR =
      new String[] {"blur", "rects", "glass", "text", "postfx", "afterflush"};
  private static final DiagnosticsAddFrameListenerService items =
      new DiagnosticsAddFrameListenerService();
  private final DiagnosticsAddFrameListenerService.Frame[] items2 =
      new DiagnosticsAddFrameListenerService.Frame[240];
  private int count;
  private int count2;
  private final Deque<DiagnosticsAddFrameListenerService.Section> items3 = new ArrayDeque<>();
  private DiagnosticsAddFrameListenerService.Frame items4;
  private long timestamp;
  private long timestamp2;
  private final List<Consumer<DiagnosticsAddFrameListenerService.Frame>> items5 =
      new CopyOnWriteArrayList<>();

  public static DiagnosticsAddFrameListenerService get() {
    return items;
  }

  private DiagnosticsAddFrameListenerService() {
    for (int index = 0; index < 240; index++) {
      this.items2[index] = new DiagnosticsAddFrameListenerService.Frame();
    }
  }

  public void addFrameListener(Consumer<DiagnosticsAddFrameListenerService.Frame> consumer) {
    this.items5.add(consumer);
  }

  public void removeFrameListener(Consumer<DiagnosticsAddFrameListenerService.Frame> consumer) {
    this.items5.remove(consumer);
  }

  public void beginFrame() {
    this.timestamp = System.nanoTime();
    this.items4 = this.items2[this.count];
    this.items4.totalNanos = 0L;
    this.items4.sections.clear();
    this.items3.clear();
  }

  public void begin(String text) {
    if (this.items4 != null) {
      this.items3.push(new DiagnosticsAddFrameListenerService.Section(text, System.nanoTime()));
    }
  }

  public void end() {
    if (this.items4 != null && !this.items3.isEmpty()) {
      DiagnosticsAddFrameListenerService.Section section = this.items3.pop();
      long longValue = System.nanoTime() - section.startNanos;
      this.items4.sections.merge(section.name, longValue, Long::sum);
    }
  }

  public void endFrame() {
    if (this.items4 != null) {
      long longValue = System.nanoTime();
      long currentLongValue =
          this.timestamp2 > 0L
              ? longValue - this.timestamp2
              : Math.max(1L, longValue - this.timestamp);
      this.timestamp2 = longValue;
      this.items4.totalNanos = currentLongValue;
      long nextLongValue = 0L;

      for (String text : SECTIONS) {
        if (!"other".equals(text)) {
          Long previousLongValue = this.items4.sections.get(text);
          if (previousLongValue != null) {
            nextLongValue += previousLongValue;
          }
        }
      }

      long sourceLongValue = Math.max(0L, currentLongValue - nextLongValue);
      this.items4.sections.put("other", sourceLongValue);
      DiagnosticsAddFrameListenerService.Frame frame = this.items4;
      this.count = (this.count + 1) % 240;
      if (this.count2 < 240) {
        this.count2++;
      }

      this.items4 = null;
      this.items3.clear();
      if (!this.items5.isEmpty()) {
        for (Consumer consumer : this.items5) {
          try {
            consumer.accept(frame);
          } catch (Exception exception) {
          }
        }
      }
    }
  }

  public DiagnosticsAddFrameListenerService.Frame[] history() {
    return this.items2;
  }

  public int head() {
    return this.count;
  }

  public int filled() {
    return this.count2;
  }

  public DiagnosticsAddFrameListenerService.Frame latest() {
    if (this.count2 == 0) {
      return null;
    }

    int index = (this.count - 1 + 240) % 240;
    return this.items2[index];
  }

  public static final class Frame {
    public long totalNanos;
    public final Map<String, Long> sections = new LinkedHashMap<>(8);
  }

  private static final class Section {
    final String name;
    final long startNanos;

    Section(String text, long longValue) {
      this.name = text;
      this.startNanos = longValue;
    }
  }
}
