package dev.felix.ellice.feature.inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

public final class InventoryEnvironmentTracker {
   private final InventoryLootPlanner inventoryPlanService2 = new InventoryLootPlanner();
   private final RandomGenerator randomGenerator;
   private final Map<InventoryEnvironmentTracker.Fingerprint, Integer> entries = new HashMap<>();
   private Object object;
   private InventoryEnvironmentTracker.Pending pending;
   private InventoryEnvironmentTracker.Selection selection;
   private long timestamp;
   private long timestamp2;
   private long timestamp3 = -1L;
   private int count2 = -1;
   private int count3 = -1;
   private int count4;
   private double value;
   private List<InventorySlotData.Slot> items2 = List.of();
   private List<InventoryKindData> items3 = List.of();
   private String text = "Open a storage container";
   private InventoryLootPlanner.Plan plan2 = new InventoryLootPlanner.Plan(List.of(), false, false);

   public InventoryEnvironmentTracker(RandomGenerator currentRandomGenerator) {
      this.randomGenerator = currentRandomGenerator;
   }

   public String status() {
      return this.text;
   }

   public int transferredStacks() {
      return this.count4;
   }

   public InventoryLootPlanner.Plan lastPlan() {
      return this.plan2;
   }

   public void reset() {
      this.object = null;
      this.pending = null;
      this.selection = null;
      this.entries.clear();
      this.timestamp3 = -1L;
      this.count2 = this.count3 = -1;
      this.count4 = 0;
      this.text = "Open a storage container";
      this.plan2 = new InventoryLootPlanner.Plan(List.of(), false, false);
      this.items2 = List.of();
      this.items3 = List.of();
   }

   public void tick(long longValue, InventoryEnvironmentTracker.Environment environment, InventoryData inventoryData, InventoryEnvironmentTracker.Timing timing) {
      Optional result = environment.capture();
      if (result.isEmpty()) {
         this.reset();
      } else {
         InventorySlotData inventorySlotData = (InventorySlotData)result.get();
         if (inventorySlotData.menu() != this.object) {
            this.reset();
            this.object = inventorySlotData.menu();
            this.timestamp = longValue;
            this.value = this.randomGenerator.nextDouble();
            this.timestamp2 = longValue + this.calculateValue3(timing.openMinMs(), timing.openMaxMs());
            this.text = "Reading container";
         }

         if (!this.items2.equals(inventorySlotData.slots()) || !this.items3.equals(inventorySlotData.owned())) {
            this.timestamp3 = -1L;
            this.items2 = inventorySlotData.slots();
            this.items3 = inventorySlotData.owned();
         }

         if (!inventorySlotData.paused() && !inventorySlotData.carriedItem() && inventorySlotData.activationHeld()) {
            if (this.pending != null) {
               InventorySlotData.Slot currentSlot = inventorySlotData.slots()
                  .stream()
                  .filter(item -> item.index() == this.pending.slot().index())
                  .findFirst()
                  .orElse(null);
               int currentValue = currentSlot != null && InventoryEnvironmentTracker.Fingerprint.of(currentSlot).equals(InventoryEnvironmentTracker.Fingerprint.of(this.pending.slot()))
                  ? 0
                  : 1;
               if (currentValue != 0) {
                  if (inventorySlotData.ownedCount(this.pending.slot().item()) > this.pending.ownedCount()) {
                     this.count4++;
                  }

                  this.pending = null;
               } else {
                  if (longValue - this.pending.sentAt() < 900L) {
                     this.text = "Waiting for inventory change";
                     return;
                  }

                  this.pending = null;
                  this.selection = null;
                  this.timestamp2 = longValue + Math.max(250, timing.clickMinMs());
               }
            }

            HashSet hashSet = new HashSet();

            for (InventorySlotData.Slot nextSlot : inventorySlotData.slots()) {
               if (this.entries.getOrDefault(InventoryEnvironmentTracker.Fingerprint.of(nextSlot), 0) >= 2) {
                  hashSet.add(nextSlot.index());
               }
            }

            this.plan2 = this.inventoryPlanService2.plan(inventorySlotData, inventoryData, this.count2, this.count3, hashSet);
            if (this.plan2.candidates().isEmpty()) {
               this.selection = null;
               if (!this.plan2.complete()) {
                  this.timestamp3 = -1L;
                  this.text = this.plan2.retryBlocked()
                     ? "Transfer paused: reopen the container to retry"
                     : "Inventory full or reserved slots reached";
               } else {
                  this.text = "No useful items remaining";
                  if (longValue - this.timestamp >= 900L) {
                     if (this.timestamp3 < 0L) {
                        this.timestamp3 = longValue;
                     }

                     if (timing.autoClose() && longValue >= this.timestamp2 && longValue - this.timestamp3 >= timing.closeDelayMs()) {
                        environment.close(inventorySlotData);
                        this.reset();
                     }
                  }
               }
            } else {
               this.timestamp3 = -1L;
               InventoryLootPlanner.Candidate candidate = this.plan2.candidates().getFirst();
               InventoryEnvironmentTracker.Fingerprint fingerprint = InventoryEnvironmentTracker.Fingerprint.of(candidate.slot());
               InventoryEnvironmentTracker.Selection currentSelection = InventoryEnvironmentTracker.Selection.of(candidate.slot());
               if (!currentSelection.equals(this.selection)) {
                  this.selection = currentSelection;
                  this.timestamp2 = Math.max(this.timestamp2, longValue + this.calculateValue(candidate, timing));
               }

               this.text = candidate.slot().item().name() + " — " + candidate.reason();
               if (longValue >= this.timestamp2) {
                  InventorySlotData.Slot previousSlot = candidate.slot();
                  boolean enabled = environment.quickMove(inventorySlotData, previousSlot);
                  if (enabled) {
                     this.entries.merge(fingerprint, 1, Integer::sum);
                     this.pending = new InventoryEnvironmentTracker.Pending(previousSlot, inventorySlotData.ownedCount(previousSlot.item()), longValue);
                     this.count2 = previousSlot.x();
                     this.count3 = previousSlot.y();
                  }

                  this.selection = null;
                  this.timestamp2 = longValue + this.calculateValue2(timing);
                  if (!enabled) {
                     this.text = "Container changed; checking again";
                  }
               }
            }
         } else {
            this.pending = null;
            this.selection = null;
            this.timestamp3 = -1L;
            this.timestamp2 = longValue + Math.max(200, timing.clickMinMs());
            this.text = inventorySlotData.carriedItem()
               ? "Paused: item on cursor"
               : (!inventorySlotData.activationHeld() ? "Hold the activation key" : "Paused for your input");
         }
      }
   }

   private int calculateValue(InventoryLootPlanner.Candidate candidate, InventoryEnvironmentTracker.Timing timing) {
      if (!timing.thoughtfulPacing()) {
         return 0;
      }

      int currentX = this.count2 < 0
         ? 30
         : (int)Math.min(
            90.0,
            Math.hypot(candidate.slot().x() - this.count2, candidate.slot().y() - this.count3)
               * 0.5
         );
      return currentX + (candidate.slot().item().kind().equipment() ? this.calculateValue3(65, 145) : this.calculateValue3(15, 55));
   }

   private int calculateValue2(InventoryEnvironmentTracker.Timing timing) {
      if (!timing.thoughtfulPacing()) {
         return timing.clickMinMs();
      }

      double doubleValue = 0.55 * this.value
         + 0.225 * this.randomGenerator.nextDouble()
         + 0.225 * this.randomGenerator.nextDouble();
      int currentValue = timing.clickMinMs() + (int)((timing.clickMaxMs() - timing.clickMinMs()) * doubleValue);
      return currentValue + (this.randomGenerator.nextDouble() < 0.08 ? this.calculateValue3(90, 220) : 0);
   }

   private int calculateValue3(int value, int currentValue) {
      return value + (int)((currentValue - value) * this.randomGenerator.nextDouble());
   }

   public interface Environment {
      Optional<InventorySlotData> capture();

      boolean quickMove(InventorySlotData inventorySlotData, InventorySlotData.Slot slot);

      void close(InventorySlotData inventorySlotData);
   }

   private record Fingerprint(int slot, String id, Object components, int count) {
      static InventoryEnvironmentTracker.Fingerprint of(InventorySlotData.Slot slot) {
         InventoryKindData inventoryKindData = slot.item();
         return new InventoryEnvironmentTracker.Fingerprint(slot.index(), inventoryKindData.id(), inventoryKindData.components(), inventoryKindData.count());
      }
   }

   private record Pending(InventorySlotData.Slot slot, int ownedCount, long sentAt) {
   }

   private record Selection(int slot, String id) {
      static InventoryEnvironmentTracker.Selection of(InventorySlotData.Slot slot) {
         return new InventoryEnvironmentTracker.Selection(slot.index(), slot.item().id());
      }
   }

   public record Timing(
      int openMinMs, int openMaxMs, int clickMinMs, int clickMaxMs, boolean thoughtfulPacing, boolean autoClose, int closeDelayMs
   ) {
      public Timing(
         int openMinMs, int openMaxMs, int clickMinMs, int clickMaxMs, boolean thoughtfulPacing, boolean autoClose, int closeDelayMs
      ) {
         openMinMs = Math.max(100, openMinMs);
         openMaxMs = Math.max(openMinMs, openMaxMs);
         clickMinMs = Math.max(50, clickMinMs);
         clickMaxMs = Math.max(clickMinMs, clickMaxMs);
         closeDelayMs = Math.max(250, closeDelayMs);
         this.openMinMs = openMinMs;
         this.openMaxMs = openMaxMs;
         this.clickMinMs = clickMinMs;
         this.clickMaxMs = clickMaxMs;
         this.thoughtfulPacing = thoughtfulPacing;
         this.autoClose = autoClose;
         this.closeDelayMs = closeDelayMs;
      }
   }
}
