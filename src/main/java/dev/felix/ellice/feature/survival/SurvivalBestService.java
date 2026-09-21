package dev.felix.ellice.feature.survival;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class SurvivalBestService {
   private static final Comparator<SurvivalBestService.Pick> comparator = Comparator.comparingDouble(SurvivalBestService.Pick::saturation)
      .reversed()
      .thenComparing(Comparator.comparingInt(SurvivalBestService.Pick::food).reversed())
      .thenComparing(Comparator.comparingInt(SurvivalBestService.Pick::count).reversed())
      .thenComparingInt(SurvivalBestService.Pick::index);

   private SurvivalBestService() {
   }

   public static Optional<SurvivalBestService.Pick> best(List<SurvivalItemData.Item> items) {
      if (items == null) {
         return Optional.empty();
      }

      SurvivalBestService.Pick pick = null;

      for (int index = 0; index < items.size(); index++) {
         SurvivalItemData.Item item = (SurvivalItemData.Item)items.get(index);
         if (item != null && item.count() > 0) {
            SurvivalAllService.Nutrition currentNutrition = SurvivalAllService.nutrition(item.id());
            if (currentNutrition != null) {
               SurvivalBestService.Pick currentPick = new SurvivalBestService.Pick(index, item.id(), item.count(), currentNutrition.food(), currentNutrition.saturation());
               if (pick == null || comparator.compare(currentPick, pick) < 0) {
                  pick = currentPick;
               }
            }
         }
      }

      return Optional.ofNullable(pick);
   }

   public record Pick(int index, String id, int count, int food, double saturation) {
   }
}
