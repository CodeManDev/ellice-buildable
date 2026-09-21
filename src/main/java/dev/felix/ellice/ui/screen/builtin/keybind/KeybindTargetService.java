package dev.felix.ellice.ui.screen.builtin.keybind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public final class KeybindTargetService {
   private final KeybindTargetService.Target target2;
   private final List<KeybindTargetService.Target> items;
   private final Map<Integer, List<String>> text;
   private final Map<Integer, String> text2;
   private int count;
   private int count2;

   public KeybindTargetService(KeybindTargetService.Target target, List<KeybindTargetService.Target> currentItems, Map<Integer, List<String>> entries, Map<Integer, String> currentEntries) {
      this.target2 = target;
      this.items = List.copyOf(currentItems);
      this.text = Map.copyOf(entries);
      this.text2 = Map.copyOf(currentEntries);
      this.count = this.count2 = target.read.getAsInt();
   }

   public KeybindTargetService.Target target() {
      return this.target2;
   }

   public int selected() {
      return this.count;
   }

   public int original() {
      return this.count2;
   }

   public boolean choose(int value) {
      if (!KeyCatalog.valid(value)) {
         return false;
      }

      this.count = value;
      return true;
   }

   public String name(int value) {
      return this.text2.getOrDefault(value, KeyCatalog.name(value));
   }

   public List<KeybindTargetService.Use> uses(int value) {
      if (value < 0) {
         return List.of();
      }

      ArrayList arrayList = new ArrayList();

      for (KeybindTargetService.Target target : this.items) {
         if (!target.id.equals(this.target2.id) && target.read.getAsInt() == value) {
            arrayList.add(new KeybindTargetService.Use(target.owner + " · " + target.title, false, target.exclusive));
         }
      }

      for (String currentText : this.text.getOrDefault(value, List.of())) {
         arrayList.add(new KeybindTargetService.Use(currentText, true, false));
      }

      return List.copyOf(arrayList);
   }

   public boolean hasElliceConflict() {
      return this.uses(this.count).stream().anyMatch(item -> !item.minecraft);
   }

   public boolean replacesModule() {
      return this.target2.exclusive && this.uses(this.count).stream().anyMatch(item -> !item.minecraft && item.exclusive);
   }

   public boolean canApply() {
      return this.target2.available.getAsBoolean()
         && KeyCatalog.valid(this.count)
         && KeyCatalog.reserved(this.count).isEmpty();
   }

   public boolean apply(boolean enabled) {
      if (this.canApply() && (!this.hasElliceConflict() || enabled)) {
         this.target2.write.accept(this.count);
         this.count2 = this.count;
         return true;
      } else {
         return false;
      }
   }

   public String tooltip(int value) {
      String text = KeyCatalog.reserved(value);
      if (!text.isEmpty()) {
         return this.name(value) + " · Reserved\n" + text + ". Pick another key for this binding.";
      }

      List items = this.uses(value);
      StringBuilder stringBuilder = new StringBuilder(this.name(value));
      if (value == this.target2.read.getAsInt()) {
         stringBuilder.append(" · Current binding");
      }

      if (items.isEmpty()) {
         stringBuilder.append("\nFree for ").append(this.target2.owner).append(" · ").append(this.target2.title);
      } else {
         for (KeybindTargetService.Use use : (Iterable<KeybindTargetService.Use>) (Iterable<?>) (items)) {
            stringBuilder.append("\n").append(use.minecraft ? "Minecraft · " : "ellice · ").append(use.label);
         }
      }

      stringBuilder.append("\nClick to select. Apply to save.");
      return stringBuilder.toString();
   }

   public record Target(
      String id, String title, String owner, IntSupplier read, IntConsumer write, BooleanSupplier available, boolean exclusive
   ) {
      public Target(
         String id, String title, String owner, IntSupplier read, IntConsumer write, BooleanSupplier available, boolean exclusive
      ) {
         Objects.requireNonNull(id);
         Objects.requireNonNull(read);
         Objects.requireNonNull(write);
         Objects.requireNonNull(available);
         this.id = id;
         this.title = title;
         this.owner = owner;
         this.read = read;
         this.write = write;
         this.available = available;
         this.exclusive = exclusive;
      }
   }

   public record Use(String label, boolean minecraft, boolean exclusive) {
   }
}
