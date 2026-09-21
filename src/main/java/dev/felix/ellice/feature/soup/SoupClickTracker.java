package dev.felix.ellice.feature.soup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import dev.felix.ellice.feature.inventory.InventorySlotData;
import java.util.Set;

public final class SoupClickTracker {
   private final Deque<SoupClickTracker.Step> items = new ArrayDeque<>();
   private SoupClickTracker.Phase phase = SoupClickTracker.Phase.IDLE;
   private Object object;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private long timestamp4;
   private long timestamp5;
   private long timestamp6;
   private float value = Float.NaN;
   private int count2;
   private int count3;
   private int count4;
   private int count5;
   private int count6;
   private SoupKindData soupKindData;
   private SoupKindData soupKindData2;
   private SoupKindData soupKindData3;
   private SoupClickTracker.Step step;
   private boolean enabled = true;
   private String text = "Ready";

   public String status() {
      return this.text;
   }

   public boolean usingSoup() {
      return this.phase == SoupClickTracker.Phase.USE;
   }

   public void stop(SoupClickTracker.Environment environment) {
      environment.finishUse(this.enabled);
      SoupConfirmedData soupConfirmedData = environment.capture();
      if (soupConfirmedData != null && soupConfirmedData.ownedInventory() && !soupConfirmedData.manualInput() && soupConfirmedData.canReturnCraftingItems()) {
         environment.closeInventory(soupConfirmedData);
      }

      environment.yieldInventory();
      this.reset();
      this.text = "Disabled";
   }

   public void reset() {
      this.phase = SoupClickTracker.Phase.IDLE;
      this.object = null;
      this.items.clear();
      this.step = null;
      this.timestamp = this.timestamp2 = this.timestamp5 = this.timestamp6 = 0L;
      this.value = Float.NaN;
   }

   public void tick(long longValue, SoupClickTracker.Environment environment, SoupClickTracker.Policy policy) {
      this.enabled = policy.restoreSlot();
      SoupConfirmedData soupConfirmedData = environment.capture();
      if (soupConfirmedData == null) {
         environment.finishUse(this.enabled);
         environment.yieldInventory();
         this.reset();
         this.text = "Waiting for gameplay";
      } else {
         if (this.object != null && this.object != soupConfirmedData.session()) {
            environment.finishUse(this.enabled);
            this.reset();
         }

         this.object = soupConfirmedData.session();
         if (Float.isFinite(this.value) && soupConfirmedData.health() < this.value) {
            this.timestamp6 = longValue;
         }

         this.value = soupConfirmedData.health();
         if (soupConfirmedData.manualInput()) {
            environment.finishUse(this.enabled);
            environment.yieldInventory();
            this.updateState3();
            this.timestamp5 = longValue + 400L;
            this.text = "Paused for your input";
         } else if (soupConfirmedData.paused()) {
            environment.finishUse(this.enabled);
            if (this.phase == SoupClickTracker.Phase.USE) {
               this.updateState3();
            }

            this.text = "Paused";
         } else if (longValue >= this.timestamp5 && longValue >= this.timestamp) {
            if (this.phase == SoupClickTracker.Phase.USE) {
               SoupConfirmedData.Confirmed currentConfirmed = soupConfirmedData.confirmed().get(this.count2);
               int currentCount = currentConfirmed.revision() <= this.timestamp4
                     || soupConfirmedData.soups() >= this.count6
                     || currentConfirmed.stack().kind() == SoupKindData.Kind.SOUP && currentConfirmed.stack().count() >= this.soupKindData.count()
                  ? 0
                  : 1;
               if (currentCount != 0) {
                  environment.finishUse(this.enabled);
                  this.updateState3();
                  this.timestamp = longValue + Math.max(200, policy.actionMillis());
                  this.text = "Soup used";
               } else if (longValue >= this.timestamp2) {
                  this.updateState2(longValue, environment, soupConfirmedData, "No soup response from server", policy);
               }
            } else if (this.phase != SoupClickTracker.Phase.IDLE && !soupConfirmedData.inventoryOpen()) {
               environment.yieldInventory();
               this.updateState3();
               this.timestamp5 = longValue + 600L;
               this.text = "Inventory closed";
            } else if (this.phase == SoupClickTracker.Phase.REFILL) {
               if (soupConfirmedData.cursor().empty()
                  && soupConfirmedData.slot(this.count3).equals(this.soupKindData3)
                  && soupConfirmedData.slot(this.count4).equals(this.soupKindData2)) {
                  this.updateState3();
                  this.updateState(longValue, environment, soupConfirmedData, policy);
                  this.text = "Hotbar refilled";
               } else {
                  this.updateState2(longValue, environment, soupConfirmedData, "Inventory changed; refill stopped", policy);
               }
            } else {
               if (this.phase == SoupClickTracker.Phase.INGREDIENTS) {
                  if (this.step != null
                     && (
                        !soupConfirmedData.slot(this.step.slot()).equals(this.step.after())
                           || !soupConfirmedData.cursor().equals(this.step.afterCursor())
                     )) {
                     this.updateState2(longValue, environment, soupConfirmedData, "Inventory changed; crafting stopped", policy);
                     return;
                  }

                  this.step = null;
                  if (!this.items.isEmpty()) {
                     SoupClickTracker.Step currentStep = this.items.removeFirst();
                     if (soupConfirmedData.slot(currentStep.slot()).equals(currentStep.item())
                        && soupConfirmedData.cursor().equals(currentStep.cursor())
                        && environment.click(soupConfirmedData, currentStep.slot(), currentStep.button(), SoupClickTracker.Click.PICKUP)) {
                        this.step = currentStep;
                        this.timestamp = longValue + policy.actionMillis();
                        return;
                     }

                     this.updateState2(longValue, environment, soupConfirmedData, "Ingredients changed", policy);
                     return;
                  }

                  this.phase = SoupClickTracker.Phase.RECIPE;
                  this.timestamp2 = longValue + policy.responseMillis();
               }

               if (this.phase == SoupClickTracker.Phase.RECIPE) {
                  if (!soupConfirmedData.cursor().empty()) {
                     this.updateState2(longValue, environment, soupConfirmedData, "Cursor changed", policy);
                  } else {
                     if (soupConfirmedData.confirmed().get(0).revision() > this.timestamp3
                        && soupConfirmedData.slot(0).kind() == SoupKindData.Kind.SOUP
                        && soupConfirmedData.slot(0).count() == 1
                        && checkCondition2(soupConfirmedData)
                        && soupConfirmedData.hasOutputRoom()) {
                        this.count5 = soupConfirmedData.soups();
                        this.timestamp3 = soupConfirmedData.confirmed().get(0).revision();
                        if (environment.click(soupConfirmedData, 0, 0, SoupClickTracker.Click.QUICK_MOVE)) {
                           this.phase = SoupClickTracker.Phase.RESULT;
                           this.timestamp2 = longValue + policy.responseMillis();
                           this.timestamp = longValue + policy.actionMillis();
                           this.text = "Taking crafted soup";
                        } else {
                           this.updateState2(longValue, environment, soupConfirmedData, "Crafting result changed", policy);
                        }
                     } else if (longValue >= this.timestamp2) {
                        this.updateState2(longValue, environment, soupConfirmedData, "Server did not supply a soup recipe", policy);
                     }
                  }
               } else if (this.phase == SoupClickTracker.Phase.RESULT) {
                  if (soupConfirmedData.confirmed().get(0).revision() > this.timestamp3
                     && soupConfirmedData.slot(0).empty()
                     && soupConfirmedData.emptyGrid()
                     && soupConfirmedData.cursor().empty()
                     && soupConfirmedData.soups() > this.count5) {
                     this.updateState3();
                     this.timestamp = longValue + policy.actionMillis();
                     this.text = "Soup crafted";
                     if (policy.needsSoup(soupConfirmedData) || soupConfirmedData.soups() >= policy.stock()) {
                        this.updateState(longValue, environment, soupConfirmedData, policy);
                     }
                  } else if (longValue >= this.timestamp2) {
                     this.updateState2(longValue, environment, soupConfirmedData, "Craft was not confirmed", policy);
                  }
               } else if (!soupConfirmedData.cursor().empty() || !soupConfirmedData.emptyGrid()) {
                  this.text = "Your cursor or crafting grid is occupied";
               } else if (soupConfirmedData.usingItem()) {
                  this.text = "Waiting for current item use";
               } else {
                  boolean currentEnabled = policy.needsSoup(soupConfirmedData);
                  int currentValue = soupConfirmedData.hotbarSoup();
                  if (currentEnabled && currentValue >= 0) {
                     if (soupConfirmedData.inventoryOpen()) {
                        this.updateState(longValue, environment, soupConfirmedData, policy);
                        this.text = "Close inventory to use soup";
                     } else {
                        this.count2 = currentValue;
                        this.soupKindData = soupConfirmedData.slot(currentValue);
                        this.timestamp4 = soupConfirmedData.confirmed().get(currentValue).revision();
                        this.count6 = soupConfirmedData.soups();
                        if (environment.use(soupConfirmedData, currentValue)) {
                           this.phase = SoupClickTracker.Phase.USE;
                           this.timestamp2 = longValue + Math.max(policy.responseMillis(), 3000);
                           this.timestamp = longValue + policy.actionMillis();
                           this.text = "Waiting for soup consumption";
                        } else {
                           this.text = "Soup use unavailable";
                           this.timestamp = longValue + 250L;
                        }
                     }
                  } else if (!policy.refill() || currentValue >= 0 || soupConfirmedData.inventorySoup() < 0) {
                     int nextValue = policy.safeCraft()
                           && (longValue - this.timestamp6 < 1500L || !(soupConfirmedData.health() >= soupConfirmedData.maxHealth() * 0.8F))
                        ? 0
                        : 1;
                     if (policy.craft() && soupConfirmedData.soups() < policy.stock() && (nextValue != 0 || currentEnabled && soupConfirmedData.soups() == 0)) {
                        List<SoupClickTracker.Step> currentItems = collectValues(soupConfirmedData);
                        if (currentItems.isEmpty()) {
                           this.text = "Need a bowl and both mushrooms";
                           this.updateState(longValue, environment, soupConfirmedData, policy);
                        } else if (!soupConfirmedData.hasOutputRoom() && currentItems.stream().noneMatch(currentItem -> currentItem.slot() >= 9 && currentItem.item().count() == 1)) {
                           this.text = "Need room for a crafted soup";
                           this.updateState(longValue, environment, soupConfirmedData, policy);
                        } else if (this.checkCondition(longValue, environment, soupConfirmedData, policy)) {
                           this.timestamp3 = soupConfirmedData.confirmed().get(0).revision();
                           if (soupConfirmedData.hasOutputRoom() && environment.placeRecipe(soupConfirmedData)) {
                              this.phase = SoupClickTracker.Phase.RECIPE;
                              this.timestamp2 = longValue + policy.responseMillis();
                           } else {
                              this.items.addAll(currentItems);
                              this.phase = SoupClickTracker.Phase.INGREDIENTS;
                           }

                           this.timestamp = longValue + policy.actionMillis();
                           this.text = "Crafting mushroom stew";
                        }
                     } else {
                        this.text = currentValue >= 0 ? "Ready · " + soupConfirmedData.soups() + " soups" : "No soup in hotbar";
                        this.updateState(longValue, environment, soupConfirmedData, policy);
                     }
                  } else if (this.checkCondition(longValue, environment, soupConfirmedData, policy)) {
                     this.count3 = soupConfirmedData.inventorySoup();
                     this.count4 = soupConfirmedData.refillTarget(policy.hotbarSlot());
                     this.soupKindData2 = soupConfirmedData.slot(this.count3);
                     this.soupKindData3 = soupConfirmedData.slot(this.count4);
                     if (environment.click(soupConfirmedData, this.count3, this.count4 - 36, SoupClickTracker.Click.SWAP)) {
                        this.phase = SoupClickTracker.Phase.REFILL;
                        this.timestamp = longValue + policy.actionMillis();
                        this.text = "Refilling hotbar";
                     }
                  }
               }
            }
         }
      }
   }

   private boolean checkCondition(long longValue, SoupClickTracker.Environment environment, SoupConfirmedData soupConfirmedData, SoupClickTracker.Policy policy) {
      if (soupConfirmedData.inventoryOpen()) {
         return true;
      }

      if (!policy.openInventory()) {
         this.text = "Open inventory to refill or craft";
         return false;
      }

      if (environment.openInventory(soupConfirmedData)) {
         this.timestamp = longValue + policy.actionMillis();
         this.text = "Opening inventory";
      }

      return false;
   }

   private void updateState(long offset, SoupClickTracker.Environment environment, SoupConfirmedData soupConfirmedData, SoupClickTracker.Policy policy) {
      if (soupConfirmedData.ownedInventory() && soupConfirmedData.cursor().empty() && soupConfirmedData.emptyGrid() && environment.closeInventory(soupConfirmedData)) {
         this.timestamp = offset + policy.actionMillis();
      }
   }

   private void updateState2(long offset, SoupClickTracker.Environment environment, SoupConfirmedData soupConfirmedData, String currentText, SoupClickTracker.Policy policy) {
      environment.finishUse(this.enabled);
      if (soupConfirmedData.ownedInventory() && soupConfirmedData.canReturnCraftingItems()) {
         environment.closeInventory(soupConfirmedData);
      }

      this.updateState3();
      this.timestamp5 = offset + Math.max(1000, policy.responseMillis());
      this.text = currentText;
   }

   private void updateState3() {
      this.phase = SoupClickTracker.Phase.IDLE;
      this.items.clear();
      this.step = null;
   }

   private static boolean checkCondition2(SoupConfirmedData soupConfirmedData) {
      HashSet hashSet = new HashSet();
      int index = 0;

      for (int currentIndex = 1; currentIndex <= 4; currentIndex++) {
         SoupKindData soupKindData = soupConfirmedData.slot(currentIndex);
         if (!soupKindData.empty()) {
            if (soupKindData.count() != 1 || !soupKindData.ingredient()) {
               return false;
            }

            index++;
            hashSet.add(soupKindData.kind());
         }
      }

      return index == 3 && hashSet.equals(Set.of(SoupKindData.Kind.BOWL, SoupKindData.Kind.RED_MUSHROOM, SoupKindData.Kind.BROWN_MUSHROOM));
   }

   private static List<SoupClickTracker.Step> collectValues(SoupConfirmedData soupConfirmedData) {
      if (soupConfirmedData.cursor().empty() && soupConfirmedData.emptyGrid()) {
         ArrayList arrayList = new ArrayList<>(soupConfirmedData.slots());
         ArrayList currentArrayList = new ArrayList();
         int index = 1;

         for (SoupKindData.Kind currentKind : List.of(SoupKindData.Kind.BOWL, SoupKindData.Kind.RED_MUSHROOM, SoupKindData.Kind.BROWN_MUSHROOM)) {
            int value = -1;

            for (int currentIndex = 9; currentIndex <= 44; currentIndex++) {
               if (((SoupKindData)arrayList.get(currentIndex)).kind() == currentKind
                  && ((SoupKindData)arrayList.get(currentIndex)).ingredient()
                  && (value < 0 || ((SoupKindData)arrayList.get(currentIndex)).count() < ((SoupKindData)arrayList.get(value)).count())) {
                  value = currentIndex;
               }
            }

            if (value < 0) {
               return List.of();
            }

            SoupKindData soupKindData = (SoupKindData)arrayList.get(value);
            SoupKindData currentSoupKindData = soupKindData.count(soupKindData.count() - 1);
            currentArrayList.add(new SoupClickTracker.Step(value, 0, soupKindData, SoupKindData.EMPTY, SoupKindData.EMPTY, soupKindData));
            currentArrayList.add(new SoupClickTracker.Step(index, soupKindData.count() == 1 ? 0 : 1, SoupKindData.EMPTY, soupKindData, soupKindData.count(1), currentSoupKindData));
            if (!currentSoupKindData.empty()) {
               currentArrayList.add(new SoupClickTracker.Step(value, 0, SoupKindData.EMPTY, currentSoupKindData, currentSoupKindData, SoupKindData.EMPTY));
            }

            arrayList.set(value, currentSoupKindData);
            index++;
         }

         return currentArrayList;
      } else {
         return List.of();
      }
   }

   public enum Click {
      PICKUP,
      QUICK_MOVE,
      SWAP;


      private static SoupClickTracker.Click[] $values() {
         return new SoupClickTracker.Click[]{PICKUP, QUICK_MOVE, SWAP};
      }
   }

   public interface Environment {
      SoupConfirmedData capture();

      boolean openInventory(SoupConfirmedData soupConfirmedData);

      boolean click(SoupConfirmedData soupConfirmedData, int value, int currentValue, SoupClickTracker.Click currentClick);

      boolean placeRecipe(SoupConfirmedData soupConfirmedData);

      boolean use(SoupConfirmedData soupConfirmedData, int value);

      void finishUse(boolean enabled);

      boolean closeInventory(SoupConfirmedData soupConfirmedData);

      void yieldInventory();
   }

   public enum Mode {
      SOUP_PVP,
      VANILLA;


      private static SoupClickTracker.Mode[] $values() {
         return new SoupClickTracker.Mode[]{SOUP_PVP, VANILLA};
      }
   }

   private enum Phase {
      IDLE,
      USE,
      REFILL,
      INGREDIENTS,
      RECIPE,
      RESULT;


      private static SoupClickTracker.Phase[] $values() {
         return new SoupClickTracker.Phase[]{IDLE, USE, REFILL, INGREDIENTS, RECIPE, RESULT};
      }
   }

   public record Policy(
      SoupClickTracker.Mode mode,
      float healthHearts,
      int foodLevel,
      boolean refill,
      int hotbarSlot,
      boolean craft,
      int stock,
      boolean openInventory,
      boolean safeCraft,
      int actionMillis,
      int responseMillis,
      boolean restoreSlot
   ) {
      public Policy(
         SoupClickTracker.Mode mode,
         float healthHearts,
         int foodLevel,
         boolean refill,
         int hotbarSlot,
         boolean craft,
         int stock,
         boolean openInventory,
         boolean safeCraft,
         int actionMillis,
         int responseMillis,
         boolean restoreSlot
      ) {
         if (mode != null
            && Float.isFinite(healthHearts)
            && !(healthHearts < 0.5F)
            && !(healthHearts > 40.0F)
            && foodLevel >= 0
            && foodLevel <= 19
            && hotbarSlot >= 0
            && hotbarSlot <= 8
            && stock >= 1
            && stock <= 36
            && actionMillis >= 50
            && responseMillis >= 500) {
            this.mode = mode;
            this.healthHearts = healthHearts;
            this.foodLevel = foodLevel;
            this.refill = refill;
            this.hotbarSlot = hotbarSlot;
            this.craft = craft;
            this.stock = stock;
            this.openInventory = openInventory;
            this.safeCraft = safeCraft;
            this.actionMillis = actionMillis;
            this.responseMillis = responseMillis;
            this.restoreSlot = restoreSlot;
         } else {
            throw new IllegalArgumentException("Invalid AutoSoup policy");
         }
      }

      public boolean needsSoup(SoupConfirmedData soupConfirmedData) {
         return this.mode == SoupClickTracker.Mode.SOUP_PVP
            ? soupConfirmedData.health() < soupConfirmedData.maxHealth() && soupConfirmedData.health() <= this.healthHearts * 2.0F
            : soupConfirmedData.food() <= this.foodLevel;
      }
   }

   private record Step(int slot, int button, SoupKindData item, SoupKindData cursor, SoupKindData after, SoupKindData afterCursor) {
   }
}

