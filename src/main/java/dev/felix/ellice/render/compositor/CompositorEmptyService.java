package dev.felix.ellice.render.compositor;

import java.util.List;

public final class CompositorEmptyService {
   public static final int MAX_SAMPLES = 40;
   private static final CompositorEmptyService compositorEmptyService = new CompositorEmptyService(List.of());
   private final List<CompositorEmptyService.Sample> items;
   private final double value2;

   public CompositorEmptyService(List<CompositorEmptyService.Sample> items) {
      this(items, 1.0);
   }

   public CompositorEmptyService(List<CompositorEmptyService.Sample> currentItems, double doubleValue) {
      if (Double.isFinite(doubleValue) && !(doubleValue <= 0.0)) {
         this.items = List.copyOf(currentItems.subList(Math.max(0, currentItems.size() - 40), currentItems.size()));
         long longValue = Long.MIN_VALUE;

         for (CompositorEmptyService.Sample sample : this.items) {
            if (!Double.isFinite(sample.value()) || sample.timestamp() < longValue) {
               throw new IllegalArgumentException("Measurements must be finite and ordered by time");
            }

            longValue = sample.timestamp();
         }

         this.value2 = Math.max(
            doubleValue,
            Math.ceil(
               this.items.stream().mapToDouble(CompositorEmptyService.Sample::value).max().orElse(1.0)
                  * 1.15
            )
         );
      } else {
         throw new IllegalArgumentException("Ceiling must be positive and finite");
      }
   }

   public static CompositorEmptyService empty() {
      return compositorEmptyService;
   }

   public List<CompositorEmptyService.Sample> samples() {
      return this.items;
   }

   public int size() {
      return this.items.size();
   }

   public double ceiling() {
      return this.value2;
   }

   public long validCount() {
      return this.items.stream().filter(item -> item.value() >= 0.0).count();
   }

   public float x(int value) {
      if (this.items.size() < 2) {
         return 1.0F;
      }

      long longValue = this.items.getFirst().timestamp();
      long currentLongValue = Math.max(1L, this.items.getLast().timestamp() - longValue);
      return (float)((double)(this.items.get(value).timestamp() - longValue) / currentLongValue);
   }

   public float y(int currentValue) {
      return (float)Math.max(0.0, this.items.get(currentValue).value() / this.value2);
   }

   public boolean valid(int currentValue) {
      return this.items.get(currentValue).value() >= 0.0;
   }

   public boolean linked(int value) {
      return value > 0
         && this.valid(value)
         && this.valid(value - 1)
         && this.items.get(value).timestamp() - this.items.get(value - 1).timestamp() <= 90000L;
   }

   public record Sample(long timestamp, double value) {
   }
}
