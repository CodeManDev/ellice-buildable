package dev.felix.ellice.feature.scaffold;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ScaffoldSweepsService {
   private final List<ScaffoldSweepsService.Sweep> items;

   private ScaffoldSweepsService(List<ScaffoldSweepsService.Sweep> currentItems) {
      this.items = List.copyOf(currentItems);
   }

   public static ScaffoldSweepsService of(ScaffoldRemapService.WorldVector worldVector, double doubleValue, ScaffoldRemapService.WorldVector currentWorldVector, double currentDoubleValue) {
      return of(worldVector, doubleValue, new ScaffoldRemapService.WorldVector(0.0, 0.0), 0.0, currentWorldVector, currentDoubleValue);
   }

   public static ScaffoldSweepsService of(
      ScaffoldRemapService.WorldVector worldVector, double doubleValue, ScaffoldRemapService.WorldVector currentWorldVector, double currentDoubleValue, ScaffoldRemapService.WorldVector nextWorldVector, double nextDoubleValue
   ) {
      Objects.requireNonNull(worldVector, "requestedDirection");
      Objects.requireNonNull(currentWorldVector, "appliedDirection");
      Objects.requireNonNull(nextWorldVector, "momentumDirection");
      if (Double.isFinite(doubleValue) && !(doubleValue < 0.0) && Double.isFinite(currentDoubleValue) && !(currentDoubleValue < 0.0) && Double.isFinite(nextDoubleValue) && !(nextDoubleValue < 0.0)) {
         ArrayList arrayList = new ArrayList(3);
         updateState(arrayList, ScaffoldSweepsService.Source.REQUESTED, worldVector, doubleValue);
         updateState(arrayList, ScaffoldSweepsService.Source.APPLIED, currentWorldVector, currentDoubleValue);
         updateState(arrayList, ScaffoldSweepsService.Source.MOMENTUM, nextWorldVector, nextDoubleValue);
         return new ScaffoldSweepsService(arrayList);
      } else {
         throw new IllegalArgumentException("Path-envelope distances must be finite and non-negative");
      }
   }

   public List<ScaffoldSweepsService.Sweep> sweeps() {
      return this.items;
   }

   public boolean contains(ScaffoldSweepsService.Source currentSource) {
      Objects.requireNonNull(currentSource, "source");
      return this.items.stream().anyMatch(item -> item.source() == currentSource);
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   private static void updateState(List<ScaffoldSweepsService.Sweep> items, ScaffoldSweepsService.Source source, ScaffoldRemapService.WorldVector worldVector, double doubleValue) {
      double currentLength = worldVector.length();
      if (!(doubleValue <= 0.0) && !(currentLength < 1.0E-9)) {
         items.add(new ScaffoldSweepsService.Sweep(source, new ScaffoldRemapService.WorldVector(worldVector.x() / currentLength, worldVector.z() / currentLength), doubleValue));
      }
   }

   public enum Source {
      REQUESTED,
      APPLIED,
      MOMENTUM;


      private static ScaffoldSweepsService.Source[] $values() {
         return new ScaffoldSweepsService.Source[]{REQUESTED, APPLIED, MOMENTUM};
      }
   }

   public record Sweep(ScaffoldSweepsService.Source source, ScaffoldRemapService.WorldVector direction, double distance) {
      public Sweep(ScaffoldSweepsService.Source source, ScaffoldRemapService.WorldVector direction, double distance) {
         Objects.requireNonNull(source, "source");
         Objects.requireNonNull(direction, "direction");
         if (Double.isFinite(distance) && !(distance < 0.0)) {
            double currentLength = direction.length();
            if (distance > 0.0 && Math.abs(currentLength - 1.0) > 1.0E-9) {
               throw new IllegalArgumentException("A non-empty sweep direction must be normalized");
            }

            this.source = source;
            this.direction = direction;
            this.distance = distance;
         } else {
            throw new IllegalArgumentException("Sweep distance must be finite and non-negative");
         }
      }
   }
}
