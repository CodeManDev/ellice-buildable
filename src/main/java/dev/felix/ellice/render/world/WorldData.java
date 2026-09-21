package dev.felix.ellice.render.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public record WorldData(float[] faces, float[] edges) {
  public static WorldData of(VoxelShape voxelShape) {
    List items = voxelShape.toAabbs();
    ArrayList arrayList = new ArrayList();
    double[][] doubles = new double[3][];

    for (int index = 0; index < 3; index++) {
      TreeSet<Double> treeSet = new TreeSet<>();

      for (AABB aABB : (Iterable<AABB>) (Iterable<?>) (items)) {
        treeSet.add(calculateValue(aABB, index));
        treeSet.add(calculateValue2(aABB, index));
      }

      doubles[index] = treeSet.stream().mapToDouble(Double::doubleValue).toArray();
    }

    int currentIndex = doubles[0].length - 1;
    int nextIndex = doubles[1].length - 1;
    int previousIndex = doubles[2].length - 1;
    if (currentIndex > 0
        && nextIndex > 0
        && previousIndex > 0
        && (long) currentIndex * nextIndex * previousIndex <= 32768L) {
      boolean[][][] booleans = new boolean[currentIndex][nextIndex][previousIndex];

      for (AABB currentAABB : (Iterable<AABB>) (Iterable<?>) (items)) {
        int value = Arrays.binarySearch(doubles[0], currentAABB.minX);
        int currentValue = Arrays.binarySearch(doubles[0], currentAABB.maxX);
        int nextValue = Arrays.binarySearch(doubles[1], currentAABB.minY);
        int previousValue = Arrays.binarySearch(doubles[1], currentAABB.maxY);
        int sourceValue = Arrays.binarySearch(doubles[2], currentAABB.minZ);
        int targetValue = Arrays.binarySearch(doubles[2], currentAABB.maxZ);

        for (int sourceIndex = value; sourceIndex < currentValue; sourceIndex++) {
          for (int targetIndex = nextValue; targetIndex < previousValue; targetIndex++) {
            for (int inputIndex = sourceValue; inputIndex < targetValue; inputIndex++) {
              booleans[sourceIndex][targetIndex][inputIndex] = true;
            }
          }
        }
      }

      for (int outputIndex = 0; outputIndex < currentIndex; outputIndex++) {
        for (int resultIndex = 0; resultIndex < nextIndex; resultIndex++) {
          for (int candidateIndex = 0; candidateIndex < previousIndex; candidateIndex++) {
            if (booleans[outputIndex][resultIndex][candidateIndex]) {
              int[] ints = new int[] {outputIndex, resultIndex, candidateIndex};

              for (int selectedIndex = 0; selectedIndex < 3; selectedIndex++) {
                for (int inputValue : new int[] {-1, 1}) {
                  int[] currentInts = (int[]) ints.clone();
                  currentInts[selectedIndex] += inputValue;
                  if (currentInts[0] < 0
                      || currentInts[0] >= currentIndex
                      || currentInts[1] < 0
                      || currentInts[1] >= nextIndex
                      || currentInts[2] < 0
                      || currentInts[2] >= previousIndex
                      || !booleans[currentInts[0]][currentInts[1]][currentInts[2]]) {
                    updateState(arrayList, doubles, ints, selectedIndex, inputValue);
                  }
                }
              }
            }
          }
        }
      }
    }

    ArrayList currentArrayList = new ArrayList();
    voxelShape.forAllEdges(
        (item, currentItem, nextItem, previousItem, sourceItem, targetItem) -> {
          for (int value : new int[] {0, 1, 2, 0, 2, 3}) {
            Collections.addAll(
                currentArrayList,
                (float) item,
                (float) currentItem,
                (float) nextItem,
                (float) previousItem,
                (float) sourceItem,
                (float) targetItem,
                value != 0 && value != 3 ? 1.0F : 0.0F,
                value < 2 ? -1.0F : 1.0F);
          }
        });
    return new WorldData(createFloat(arrayList), createFloat(currentArrayList));
  }

  private static void updateState(
      List<Float> items, double[][] doubles, int[] ints, int index, int value) {
    int currentIndex = (index + 1) % 3;
    int nextIndex = (index + 2) % 3;

    for (int currentValue : new int[] {0, 1, 2, 0, 2, 3}) {
      float[] floats = new float[3];
      floats[index] = (float) doubles[index][ints[index] + (value > 0 ? 1 : 0)];
      floats[currentIndex] =
          (float)
              doubles[currentIndex][
                  ints[currentIndex] + (currentValue != 1 && currentValue != 2 ? 0 : 1)];
      floats[nextIndex] = (float) doubles[nextIndex][ints[nextIndex] + (currentValue >= 2 ? 1 : 0)];
      Collections.addAll(
          items,
          floats[0],
          floats[1],
          floats[2],
          index == 0 ? value : 0.0F,
          index == 1 ? value : 0.0F,
          index == 2 ? value : 0.0F);
    }
  }

  private static double calculateValue(AABB aABB, int value) {
    return value == 0 ? aABB.minX : (value == 1 ? aABB.minY : aABB.minZ);
  }

  private static double calculateValue2(AABB aABB, int value) {
    return value == 0 ? aABB.maxX : (value == 1 ? aABB.maxY : aABB.maxZ);
  }

  private static float[] createFloat(List<Float> items) {
    float[] currentSize = new float[items.size()];

    for (int index = 0; index < currentSize.length; index++) {
      currentSize[index] = (Float) items.get(index);
    }

    return currentSize;
  }
}
