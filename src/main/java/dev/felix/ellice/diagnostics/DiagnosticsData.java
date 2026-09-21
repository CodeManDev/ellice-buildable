package dev.felix.ellice.diagnostics;

import dev.felix.ellice.render.compositor.CompositorEmptyService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public record DiagnosticsData(
    int samples,
    double averageMs,
    double p95Ms,
    double peakMs,
    double seconds,
    Map<String, Double> sections,
    CompositorEmptyService graph) {
  public DiagnosticsData(
      int samples,
      double averageMs,
      double p95Ms,
      double peakMs,
      double seconds,
      Map<String, Double> sections,
      CompositorEmptyService graph) {
    sections = Map.copyOf(sections);
    this.samples = samples;
    this.averageMs = averageMs;
    this.p95Ms = p95Ms;
    this.peakMs = peakMs;
    this.seconds = seconds;
    this.sections = sections;
    this.graph = graph;
  }

  public double fps() {
    return this.averageMs > 0.0 ? 1000.0 / this.averageMs : 0.0;
  }

  public double section(String text) {
    return this.sections.getOrDefault(text, 0.0);
  }

  public static DiagnosticsData capture(
      DiagnosticsAddFrameListenerService diagnosticsAddFrameListener) {
    return capture(
        diagnosticsAddFrameListener.history(),
        diagnosticsAddFrameListener.head(),
        diagnosticsAddFrameListener.filled());
  }

  static DiagnosticsData capture(
      DiagnosticsAddFrameListenerService.Frame[] frames, int value, int currentValue) {
    int index = Math.min(Math.max(0, currentValue), frames.length);
    double[] doubles = new double[index];
    LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<>();
    int currentIndex = 0;
    double doubleValue = 0.0;

    for (int nextValue = index; nextValue > 0; nextValue += -1) {
      DiagnosticsAddFrameListenerService.Frame currentLength =
          frames[Math.floorMod(value - nextValue, frames.length)];
      if (currentLength != null && currentLength.totalNanos > 0L) {
        double currentDoubleValue = currentLength.totalNanos / 1000000.0;
        doubles[currentIndex++] = currentDoubleValue;
        doubleValue += currentDoubleValue;
        currentLength.sections.forEach(
            (item, currentItem) ->
                linkedHashMap.merge(item, Math.max(0L, currentItem) / 1000000.0, Double::sum));
      }
    }

    int previousValue = currentIndex;
    linkedHashMap.replaceAll(
        (item, currentItem) -> previousValue == 0 ? 0.0 : currentItem / previousValue);
    ArrayList arrayList = new ArrayList();
    int sourceValue = Math.min(currentIndex, 40);

    for (int nextIndex = 0; nextIndex < sourceValue; nextIndex++) {
      int targetValue = nextIndex * currentIndex / sourceValue;
      int inputValue = (nextIndex + 1) * currentIndex / sourceValue;
      double nextDoubleValue = 0.0;

      for (int previousIndex = targetValue; previousIndex < inputValue; previousIndex++) {
        nextDoubleValue = Math.max(nextDoubleValue, doubles[previousIndex]);
      }

      arrayList.add(new CompositorEmptyService.Sample(inputValue, nextDoubleValue));
    }

    Arrays.sort(doubles, 0, currentIndex);
    return new DiagnosticsData(
        currentIndex,
        currentIndex == 0 ? 0.0 : doubleValue / currentIndex,
        currentIndex == 0 ? 0.0 : doubles[(int) Math.ceil(currentIndex * 0.95) - 1],
        currentIndex == 0 ? 0.0 : doubles[currentIndex - 1],
        doubleValue / 1000.0,
        linkedHashMap,
        new CompositorEmptyService(arrayList, 20.0));
  }
}
