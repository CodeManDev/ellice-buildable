package dev.felix.ellice.feature.task;

import java.util.Map;

public interface TaskWorldView {
   boolean loaded(int value, int currentValue);

   String blockId(int value, int currentValue, int nextValue);

   default Map<String, String> blockProps(int value, int currentValue, int nextValue) {
      return Map.of();
   }

   int minY();

   int maxY();
}
