package dev.felix.ellice.feature.soup;

import java.util.ArrayList;
import java.util.List;

public record SoupConfirmedData(
   Object session,
   List<SoupKindData> slots,
   SoupKindData cursor,
   List<SoupConfirmedData.Confirmed> confirmed,
   int selected,
   float health,
   float maxHealth,
   int food,
   boolean inventoryOpen,
   boolean ownedInventory,
   boolean paused,
   boolean manualInput,
   boolean usingItem
) {
   public SoupConfirmedData(
      Object session,
      List<SoupKindData> slots,
      SoupKindData cursor,
      List<SoupConfirmedData.Confirmed> confirmed,
      int selected,
      float health,
      float maxHealth,
      int food,
      boolean inventoryOpen,
      boolean ownedInventory,
      boolean paused,
      boolean manualInput,
      boolean usingItem
   ) {
      slots = List.copyOf(slots);
      confirmed = List.copyOf(confirmed);
      if (slots.size() == 46 && confirmed.size() == 46 && selected >= 0 && selected <= 8) {
         this.session = session;
         this.slots = slots;
         this.cursor = cursor;
         this.confirmed = confirmed;
         this.selected = selected;
         this.health = health;
         this.maxHealth = maxHealth;
         this.food = food;
         this.inventoryOpen = inventoryOpen;
         this.ownedInventory = ownedInventory;
         this.paused = paused;
         this.manualInput = manualInput;
         this.usingItem = usingItem;
      } else {
         throw new IllegalArgumentException("Expected the Vanilla player inventory");
      }
   }

   public SoupKindData slot(int value) {
      return this.slots.get(value);
   }

   public static int playerSlotToMenu(int value) {
      if (value < 0 || value > 40) {
         return -1;
      } else if (value < 9) {
         return value + 36;
      } else if (value < 36) {
         return value;
      } else {
         return value == 40 ? 45 : 44 - value;
      }
   }

   public boolean emptyGrid() {
      for (int index = 1; index <= 4; index++) {
         if (!this.slot(index).empty()) {
            return false;
         }
      }

      return true;
   }

   public int soups() {
      int value = 0;

      for (int index = 9; index <= 45; index++) {
         if (this.slot(index).kind() == SoupKindData.Kind.SOUP) {
            value += this.slot(index).count();
         }
      }

      return value;
   }

   public int hotbarSoup() {
      if (this.slot(36 + this.selected).kind() == SoupKindData.Kind.SOUP) {
         return 36 + this.selected;
      }

      if (this.slot(45).kind() == SoupKindData.Kind.SOUP) {
         return 45;
      }

      for (int index = 36; index <= 44; index++) {
         if (this.slot(index).kind() == SoupKindData.Kind.SOUP) {
            return index;
         }
      }

      return -1;
   }

   public int inventorySoup() {
      for (int index = 9; index <= 35; index++) {
         if (this.slot(index).kind() == SoupKindData.Kind.SOUP) {
            return index;
         }
      }

      return -1;
   }

   public int refillTarget(int value) {
      int currentValue = 36 + value;
      if (!this.slot(currentValue).empty() && this.slot(currentValue).kind() != SoupKindData.Kind.BOWL) {
         for (int index = 36; index <= 44; index++) {
            if (this.slot(index).empty() || this.slot(index).kind() == SoupKindData.Kind.BOWL) {
               return index;
            }
         }

         return currentValue;
      } else {
         return currentValue;
      }
   }

   public boolean hasOutputRoom() {
      for (int index = 9; index <= 44; index++) {
         if (this.slot(index).empty()) {
            return true;
         }
      }

      return false;
   }

   public boolean canReturnCraftingItems() {
      ArrayList arrayList = new ArrayList<>(this.slots.subList(9, 45));
      ArrayList currentArrayList = new ArrayList<>(this.slots.subList(1, 5));
      currentArrayList.add(this.cursor);

      for (SoupKindData soupKindData : (Iterable<SoupKindData>) (Iterable<?>) (currentArrayList)) {
         int currentCount = soupKindData.count();

         for (int index = 0; index < arrayList.size() && currentCount > 0; index++) {
            SoupKindData currentSoupKindData = (SoupKindData)arrayList.get(index);
            if (currentSoupKindData.merges(soupKindData)) {
               int nextCount = Math.min(currentCount, Math.max(0, currentSoupKindData.maxCount() - currentSoupKindData.count()));
               arrayList.set(index, currentSoupKindData.count(currentSoupKindData.count() + nextCount));
               currentCount -= nextCount;
            }
         }

         for (int currentIndex = 0; currentIndex < arrayList.size() && currentCount > 0; currentIndex++) {
            if (((SoupKindData)arrayList.get(currentIndex)).empty()) {
               int value = Math.min(currentCount, soupKindData.maxCount());
               arrayList.set(currentIndex, soupKindData.count(value));
               currentCount -= value;
            }
         }

         if (currentCount > 0) {
            return false;
         }
      }

      return true;
   }

   public record Confirmed(long revision, SoupKindData stack) {
   }
}
