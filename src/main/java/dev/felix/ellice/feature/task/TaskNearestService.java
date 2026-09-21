package dev.felix.ellice.feature.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class TaskNearestService {
   private TaskNearestService() {
   }

   public static List<TaskNearestService.Find> nearest(
      TaskWorldView taskWorldView, double doubleValue, double currentDoubleValue, double nextDoubleValue, Set<String> values, int value, int currentValue, int nextValue
   ) {
      Objects.requireNonNull(taskWorldView, "view");
      Objects.requireNonNull(values, "ids");
      if (!values.isEmpty() && value >= 1 && value <= 64 && currentValue >= 0 && currentValue <= 64 && nextValue >= 1) {
         int previousValue = (int)Math.floor(doubleValue);
         int sourceValue = (int)Math.floor(nextDoubleValue);
         int targetValue = Math.max(taskWorldView.minY(), (int)Math.floor(currentDoubleValue - currentValue));
         int inputValue = Math.min(taskWorldView.maxY(), (int)Math.ceil(currentDoubleValue + currentValue));
         ArrayList arrayList = new ArrayList();

         for (int index = previousValue - value; index <= previousValue + value; index++) {
            for (int currentIndex = sourceValue - value; currentIndex <= sourceValue + value; currentIndex++) {
               if (taskWorldView.loaded(index, currentIndex)) {
                  for (int nextIndex = targetValue; nextIndex <= inputValue; nextIndex++) {
                     String text = taskWorldView.blockId(index, nextIndex, currentIndex);
                     if (values.contains(text)) {
                        double previousDoubleValue = index + 0.5 - doubleValue;
                        double sourceDoubleValue = nextIndex + 0.5 - currentDoubleValue;
                        double targetDoubleValue = currentIndex + 0.5 - nextDoubleValue;
                        arrayList.add(new TaskNearestService.Find(index, nextIndex, currentIndex, text, previousDoubleValue * previousDoubleValue + sourceDoubleValue * sourceDoubleValue + targetDoubleValue * targetDoubleValue));
                     }
                  }
               }
            }
         }

         arrayList.sort(Comparator.comparingDouble(TaskNearestService.Find::distSq));
         return arrayList.size() > nextValue ? List.copyOf(arrayList.subList(0, nextValue)) : List.copyOf(arrayList);
      } else {
         throw new IllegalArgumentException("Invalid scan window");
      }
   }

   public record Find(int x, int y, int z, String id, double distSq) {
      public TaskBlockPosition pos() {
         return new TaskBlockPosition(this.x, this.y, this.z);
      }
   }
}
