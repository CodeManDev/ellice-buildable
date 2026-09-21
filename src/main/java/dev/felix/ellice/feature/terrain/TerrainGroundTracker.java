package dev.felix.ellice.feature.terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public final class TerrainGroundTracker {
   private final TerrainGroundTracker.Ground ground;
   private final PriorityQueue<TerrainGroundTracker.Entry> priorityQueue = new PriorityQueue<>(
      Comparator.comparingDouble(TerrainGroundTracker.Entry::priority).thenComparingDouble(item -> -item.step.cost)
   );
   private final Map<TerrainGroundTracker.Key, Double> entries = new HashMap<>();
   private TerrainKindData terrainKindData;
   private TerrainGroundTracker.Status status2 = TerrainGroundTracker.Status.IDLE;
   private List<Vector3d> items = List.of();
   private Vector3d vector3d;
   private double value;
   private double value2;
   private double value3;
   private double value4;
   private double value5;
   private int count;
   private int count2;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;

   public TerrainGroundTracker(TerrainGroundTracker.Ground currentGround) {
      this.ground = Objects.requireNonNull(currentGround);
   }

   public TerrainKindData target() {
      return this.terrainKindData;
   }

   public TerrainGroundTracker.Status status() {
      return this.status2;
   }

   public List<Vector3d> route() {
      return this.items;
   }

   public long revision() {
      return this.timestamp;
   }

   public double remaining() {
      return this.value3;
   }

   public double progress() {
      return this.value2;
   }

   public int visited() {
      return this.count;
   }

   public void stop() {
      this.terrainKindData = null;
      this.items = List.of();
      this.priorityQueue.clear();
      this.entries.clear();
      this.status2 = TerrainGroundTracker.Status.IDLE;
      this.timestamp++;
   }

   public void start(TerrainKindData currentTerrainKindData, Vector3d vector3d) {
      this.terrainKindData = currentTerrainKindData;
      this.updateState(vector3d);
   }

   public void moved(TerrainKindData currentTerrainKindData, Vector3d vector3d) {
      if (this.terrainKindData != null && this.terrainKindData.id().equals(currentTerrainKindData.id())) {
         this.start(currentTerrainKindData, vector3d);
      }
   }

   private Double calculateValue(int value, int currentValue, double doubleValue) {
      for (byte byteValue = 0; byteValue <= 64; byteValue += 4) {
         Double currentDoubleValue = this.ground.floor(value, currentValue, doubleValue - byteValue);
         if (currentDoubleValue != null) {
            return currentDoubleValue;
         }
      }

      return null;
   }

   private void updateState(Vector3d currentVector3d) {
      this.items = List.of();
      this.priorityQueue.clear();
      this.entries.clear();
      this.count = 0;
      this.value2 = 0.0;
      this.timestamp++;
      this.vector3d = new Vector3d(currentVector3d);
      this.timestamp2 = System.nanoTime();
      int currentValue = (int)Math.floor(currentVector3d.x);
      int nextValue = (int)Math.floor(currentVector3d.z);
      int currentX = (int)Math.floor(this.terrainKindData.x());
      int previousValue = (int)Math.floor(this.terrainKindData.z());
      this.value3 = currentVector3d.distance(this.terrainKindData.x(), this.terrainKindData.y(), this.terrainKindData.z());
      if (this.ground.loaded(currentValue, nextValue) && this.ground.loaded(currentX, previousValue)) {
         Double doubleValue = this.calculateValue(currentValue, nextValue, currentVector3d.y);
         Double currentY = this.calculateValue(currentX, previousValue, this.terrainKindData.y() + 0.1);
         if (doubleValue != null && currentY != null) {
            this.value = currentY;
            TerrainGroundTracker.Step step = new TerrainGroundTracker.Step(createKey(currentValue, nextValue, doubleValue), doubleValue, 0.0, null);
            this.entries.put(step.key, 0.0);
            this.priorityQueue.add(new TerrainGroundTracker.Entry(step, this.calculateValue2(currentValue, nextValue, doubleValue)));
            this.status2 = TerrainGroundTracker.Status.PLANNING;
         } else {
            this.status2 = TerrainGroundTracker.Status.UNAVAILABLE;
         }
      } else {
         this.status2 = TerrainGroundTracker.Status.UNEXPLORED;
      }
   }

   private static TerrainGroundTracker.Key createKey(int value, int currentValue, double doubleValue) {
      return new TerrainGroundTracker.Key(value, currentValue, (int)Math.round(doubleValue * 16.0));
   }

   private double calculateValue2(int currentValue, int nextValue, double doubleValue) {
      return Math.abs(currentValue - Math.floor(this.terrainKindData.x()))
         + Math.abs(nextValue - Math.floor(this.terrainKindData.z()))
         + Math.abs(doubleValue - this.value) * 0.25;
   }

   public void tick(Vector3d currentVector3d) {
      if (this.terrainKindData != null) {
         long longValue = System.nanoTime();
         if (this.status2 == TerrainGroundTracker.Status.PLANNING) {
            int index = 0;

            while (!this.priorityQueue.isEmpty() && index++ < 96 && System.nanoTime() - longValue < 800000L) {
               TerrainGroundTracker.Step currentStep = this.priorityQueue.remove().step;
               if (!(currentStep.cost > this.entries.getOrDefault(currentStep.key, Double.POSITIVE_INFINITY))) {
                  if (++this.count > 30000 || longValue - this.timestamp2 > 8000000000L) {
                     this.status2 = TerrainGroundTracker.Status.UNAVAILABLE;
                     break;
                  }

                  if (currentStep.key.x == (int)Math.floor(this.terrainKindData.x())
                     && currentStep.key.z == (int)Math.floor(this.terrainKindData.z())
                     && Math.abs(currentStep.y - this.value) < 0.6) {
                     this.updateState2(currentStep);
                     break;
                  }

                  for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                     int currentValue = currentStep.key.x + (currentIndex == 0 ? 1 : (currentIndex == 1 ? -1 : 0));
                     int nextValue = currentStep.key.z + (currentIndex == 2 ? 1 : (currentIndex == 3 ? -1 : 0));
                     if (!(Math.abs(currentValue - this.vector3d.x) > 512.0)
                        && !(Math.abs(nextValue - this.vector3d.z) > 512.0)) {
                        Double doubleValue = this.ground.floor(currentValue, nextValue, currentStep.y);
                        if (doubleValue != null
                           && !(doubleValue - currentStep.y > 1.01)
                           && !(currentStep.y - doubleValue > 3.01)) {
                           TerrainGroundTracker.Key currentKey = createKey(currentValue, nextValue, doubleValue);
                           double currentDoubleValue = currentStep.cost + 1.0 + Math.abs(doubleValue - currentStep.y) * 0.4;
                           if (!(
                              currentDoubleValue + 1.0E-6
                                 >= this.entries.getOrDefault(currentKey, Double.POSITIVE_INFINITY)
                           )) {
                              this.entries.put(currentKey, currentDoubleValue);
                              TerrainGroundTracker.Step nextStep = new TerrainGroundTracker.Step(currentKey, doubleValue, currentDoubleValue, currentStep);
                              this.priorityQueue.add(new TerrainGroundTracker.Entry(nextStep, currentDoubleValue + this.calculateValue2(currentValue, nextValue, doubleValue)));
                           }
                        }
                     }
                  }
               }
            }

            if (this.priorityQueue.isEmpty() && this.status2 == TerrainGroundTracker.Status.PLANNING) {
               this.status2 = TerrainGroundTracker.Status.UNAVAILABLE;
               this.timestamp++;
            }
         }

         if (this.status2 == TerrainGroundTracker.Status.READY || this.status2 == TerrainGroundTracker.Status.ARRIVED) {
            this.updateState3(currentVector3d);
            if (this.value3 < 2.0
               && Math.abs(currentVector3d.y - this.value) < 2.0) {
               if (this.status2 != TerrainGroundTracker.Status.ARRIVED) {
                  this.status2 = TerrainGroundTracker.Status.ARRIVED;
                  this.timestamp++;
               }
            } else if (longValue - this.timestamp2 > 900000000L && this.value5 > 3.0) {
               this.updateState(currentVector3d);
            } else if (longValue - this.timestamp3 > 1000000000L && this.status2 == TerrainGroundTracker.Status.READY) {
               this.timestamp3 = longValue;

               for (int nextIndex = this.count2; nextIndex < Math.min(this.items.size(), this.count2 + 10); nextIndex++) {
                  Vector3d nextVector3d = this.items.get(nextIndex);
                  Double nextDoubleValue = this.ground.floor((int)Math.floor(nextVector3d.x), (int)Math.floor(nextVector3d.z), nextVector3d.y);
                  if (nextDoubleValue == null || Math.abs(nextDoubleValue - nextVector3d.y) > 0.1) {
                     this.updateState(currentVector3d);
                     break;
                  }
               }
            }
         } else if ((this.status2 == TerrainGroundTracker.Status.UNEXPLORED || this.status2 == TerrainGroundTracker.Status.UNAVAILABLE)
            && longValue - this.timestamp2 > 3000000000L
            && (
               this.status2 == TerrainGroundTracker.Status.UNEXPLORED
                  || currentVector3d.distanceSquared(this.vector3d) > 9.0
            )) {
            this.updateState(currentVector3d);
         }
      }
   }

   private void updateState2(TerrainGroundTracker.Step step) {
      ArrayList arrayList = new ArrayList();

      for (TerrainGroundTracker.Step currentStep = step; currentStep != null; currentStep = currentStep.parent) {
         arrayList.add(
            new Vector3d(
               currentStep.key.x + 0.5,
               currentStep.y,
               currentStep.key.z + 0.5
            )
         );
      }

      Collections.reverse(arrayList);
      if (arrayList.size() == 1) {
         arrayList.add(new Vector3d((Vector3dc)arrayList.getFirst()).add(0.01, 0.0, 0.0));
      }

      this.items = List.copyOf(arrayList);
      this.value4 = 0.0;

      for (int index = 1; index < this.items.size(); index++) {
         this.value4 = this.value4 + this.items.get(index - 1).distance((Vector3dc)this.items.get(index));
      }

      this.status2 = TerrainGroundTracker.Status.READY;
      this.timestamp++;
      this.priorityQueue.clear();
      this.entries.clear();
   }

   private void updateState3(Vector3d vector3d) {
      double doubleValue = Double.POSITIVE_INFINITY;
      double currentDoubleValue = 0.0;
      double nextDoubleValue = 0.0;

      for (int index = 1; index < this.items.size(); index++) {
         Vector3d currentVector3d = this.items.get(index - 1);
         Vector3d nextVector3d = this.items.get(index);
         Vector3d previousVector3d = new Vector3d(nextVector3d).sub(currentVector3d);
         double currentLength = previousVector3d.length();
         double previousDoubleValue = Math.clamp(new Vector3d(vector3d).sub(currentVector3d).dot(previousVector3d) / previousVector3d.lengthSquared(), 0.0, 1.0);
         double sourceDoubleValue = new Vector3d(previousVector3d).mul(previousDoubleValue).add(currentVector3d).distanceSquared(vector3d);
         if (sourceDoubleValue < doubleValue) {
            doubleValue = sourceDoubleValue;
            currentDoubleValue = nextDoubleValue + currentLength * previousDoubleValue;
            this.count2 = index - 1;
         }

         nextDoubleValue += currentLength;
      }

      this.value2 = currentDoubleValue;
      this.value3 = Math.max(0.0, this.value4 - currentDoubleValue);
      this.value5 = Math.sqrt(doubleValue);
   }

   public TerrainGroundTracker.Instruction instruction() {
      if (this.status2 == TerrainGroundTracker.Status.ARRIVED) {
         return new TerrainGroundTracker.Instruction("check", "You have arrived", 0.0);
      }

      if (this.status2 == TerrainGroundTracker.Status.PLANNING) {
         return new TerrainGroundTracker.Instruction("navigation", "Finding your route…", this.value3);
      }

      if (this.status2 == TerrainGroundTracker.Status.UNEXPLORED) {
         return new TerrainGroundTracker.Instruction("navigation", "Explore closer to find a route", this.value3);
      }

      if (this.status2 == TerrainGroundTracker.Status.UNAVAILABLE) {
         return new TerrainGroundTracker.Instruction("navigation", "No walkable route found", this.value3);
      }

      double doubleValue = 0.0;

      for (int index = this.count2 + 1; index + 1 < this.items.size(); index++) {
         Vector3d vector3d = new Vector3d((Vector3dc)this.items.get(index)).sub((Vector3dc)this.items.get(index - 1));
         Vector3d currentVector3d = new Vector3d((Vector3dc)this.items.get(index + 1)).sub((Vector3dc)this.items.get(index));
         doubleValue += vector3d.length();
         double currentDoubleValue = vector3d.x * currentVector3d.z - vector3d.z * currentVector3d.x;
         if (Math.abs(currentDoubleValue) > 0.2) {
            return new TerrainGroundTracker.Instruction(currentDoubleValue > 0.0 ? "turn_right" : "turn_left", currentDoubleValue > 0.0 ? "Turn right" : "Turn left", doubleValue);
         }
      }

      return new TerrainGroundTracker.Instruction("navigation", "Continue to your destination", this.value3);
   }

   private record Entry(TerrainGroundTracker.Step step, double priority) {
   }

   public interface Ground {
      Double floor(int value, int currentValue, double doubleValue);

      default boolean loaded(int value, int currentValue) {
         return true;
      }
   }

   public record Instruction(String icon, String text, double distance) {
   }

   private record Key(int x, int z, int height) {
   }

   public enum Status {
      IDLE,
      PLANNING,
      READY,
      ARRIVED,
      UNAVAILABLE,
      UNEXPLORED;


      private static TerrainGroundTracker.Status[] $values() {
         return new TerrainGroundTracker.Status[]{IDLE, PLANNING, READY, ARRIVED, UNAVAILABLE, UNEXPLORED};
      }
   }

   private record Step(TerrainGroundTracker.Key key, double y, double cost, TerrainGroundTracker.Step parent) {
   }
}
