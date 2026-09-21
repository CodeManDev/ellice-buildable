package dev.felix.ellice.hud.layout;

import java.util.ArrayList;
import java.util.List;

public final class LayoutFindSelector {
   public static final int SCHEMA_VERSION = 1;
   public int version = 1;
   public String name = "Untitled HUD";
   public String author = "";
   public String description = "";
   public final List<LayoutIsContainerService> elements = new ArrayList<>();

   public LayoutIsContainerService find(String name) {
      return name == null ? null : createLayoutIsContainerService(this.elements, name);
   }

   private static LayoutIsContainerService createLayoutIsContainerService(List<LayoutIsContainerService> items, String text) {
      if (items == null) {
         return null;
      }

      for (LayoutIsContainerService layoutIsContainer : items) {
         if (text.equals(layoutIsContainer.id)) {
            return layoutIsContainer;
         }

         LayoutIsContainerService currentLayoutIsContainer = createLayoutIsContainerService(layoutIsContainer.children, text);
         if (currentLayoutIsContainer != null) {
            return currentLayoutIsContainer;
         }
      }

      return null;
   }
}
