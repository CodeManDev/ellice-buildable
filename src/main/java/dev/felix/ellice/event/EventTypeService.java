package dev.felix.ellice.event;

import java.util.Objects;

public final class EventTypeService<T> {
   private final Class<T> class2;
   private final String text;

   private EventTypeService(Class<T> classValue) {
      this.class2 = classValue;
      this.text = classValue.getSimpleName();
   }

   public static <T> EventTypeService<T> of(Class<T> classValue) {
      return new EventTypeService<>(classValue);
   }

   public Class<T> type() {
      return this.class2;
   }

   public String name() {
      return this.text;
   }

   @Override
   public boolean equals(Object value) {
      if (this == value) {
         return true;
      } else {
         return value instanceof EventTypeService eventType ? this.class2.equals(eventType.class2) : false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.class2);
   }

   @Override
   public String toString() {
      return "EventKey[" + this.text + "]";
   }
}
