package dev.felix.ellice.feature.survival;

import java.util.Objects;

public final class SurvivalAssessService {
   private SurvivalAssessService() {
   }

   public static SurvivalAssessService.Advice assess(SurvivalData survivalData, SurvivalAssessService.Policy currentPolicy) {
      Objects.requireNonNull(survivalData, "snapshot");
      Objects.requireNonNull(currentPolicy, "policy");
      if (!survivalData.alive()) {
         return new SurvivalAssessService.Advice(SurvivalAssessService.Threat.NONE, "Waiting for respawn");
      } else if (survivalData.eyeInFluid() && survivalData.air() <= currentPolicy.minAir()) {
         return new SurvivalAssessService.Advice(SurvivalAssessService.Threat.DROWNING, "Air low — surface now");
      } else if (survivalData.fireTicks() > 0) {
         return new SurvivalAssessService.Advice(SurvivalAssessService.Threat.BURNING, "Burning — extinguish or get to water");
      } else if (!survivalData.onGround() && !survivalData.inWater() && survivalData.fallDistance() > currentPolicy.maxSafeFall()) {
         return new SurvivalAssessService.Advice(SurvivalAssessService.Threat.FALLING, "Falling — place water or land safe");
      } else if (survivalData.effectiveHealth() <= currentPolicy.criticalHearts() * 2.0F) {
         return new SurvivalAssessService.Advice(SurvivalAssessService.Threat.CRITICAL_HEALTH, "Health critical — eat to regen or flee");
      } else {
         return survivalData.food() <= currentPolicy.foodThreshold()
            ? new SurvivalAssessService.Advice(SurvivalAssessService.Threat.HUNGRY, "Hungry — eat the best stored food")
            : new SurvivalAssessService.Advice(SurvivalAssessService.Threat.NONE, "Vitals stable");
      }
   }

   public record Advice(SurvivalAssessService.Threat threat, String reason) {
      public Advice(SurvivalAssessService.Threat threat, String reason) {
         Objects.requireNonNull(threat, "threat");
         reason = reason == null ? "" : reason;
         this.threat = threat;
         this.reason = reason;
      }

      public boolean acute() {
         return this.threat == SurvivalAssessService.Threat.DROWNING
            || this.threat == SurvivalAssessService.Threat.BURNING
            || this.threat == SurvivalAssessService.Threat.FALLING;
      }
   }

   public record Policy(float criticalHearts, int foodThreshold, int minAir, double maxSafeFall) {
      public Policy(float criticalHearts, int foodThreshold, int minAir, double maxSafeFall) {
         if (Float.isFinite(criticalHearts)
            && !(criticalHearts < 0.5F)
            && !(criticalHearts > 20.0F)
            && foodThreshold >= 0
            && foodThreshold <= 19
            && minAir >= 0
            && minAir <= 300
            && Double.isFinite(maxSafeFall)
            && !(maxSafeFall < 0.0)
            && !(maxSafeFall > 24.0)) {
            this.criticalHearts = criticalHearts;
            this.foodThreshold = foodThreshold;
            this.minAir = minAir;
            this.maxSafeFall = maxSafeFall;
         } else {
            throw new IllegalArgumentException("Invalid vitals policy");
         }
      }

      public static SurvivalAssessService.Policy defaults() {
         return new SurvivalAssessService.Policy(5.0F, 14, 60, 3.5);
      }
   }

   public enum Threat {
      NONE,
      HUNGRY,
      CRITICAL_HEALTH,
      BURNING,
      DROWNING,
      FALLING;


      private static SurvivalAssessService.Threat[] $values() {
         return new SurvivalAssessService.Threat[]{NONE, HUNGRY, CRITICAL_HEALTH, BURNING, DROWNING, FALLING};
      }
   }
}
