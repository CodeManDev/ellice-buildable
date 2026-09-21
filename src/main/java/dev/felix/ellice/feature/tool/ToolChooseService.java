package dev.felix.ellice.feature.tool;

import java.util.List;

public final class ToolChooseService {
   private ToolChooseService() {
   }

   public static int choose(List<ToolChooseService.Candidate> items, int value, int currentValue) {
      ToolChooseService.Candidate candidate = null;

      for (ToolChooseService.Candidate currentCandidate : items) {
         if (currentCandidate.slot >= 0
            && currentCandidate.slot < 9
            && Float.isFinite(currentCandidate.progress)
            && !(currentCandidate.progress <= 0.0F)
            && (!currentCandidate.damageable || currentCandidate.durability > currentValue)
            && (
               candidate == null
                  || currentCandidate.harvests && !candidate.harvests
                  || currentCandidate.harvests == candidate.harvests
                     && (
                        currentCandidate.progress > candidate.progress + 1.0E-7F
                           || Math.abs(currentCandidate.progress - candidate.progress) <= 1.0E-7F && currentCandidate.slot == value
                     )
            )) {
            candidate = currentCandidate;
         }
      }

      return candidate == null ? -1 : candidate.slot;
   }

   public record Candidate(int slot, boolean harvests, float progress, int durability, boolean damageable) {
   }
}
