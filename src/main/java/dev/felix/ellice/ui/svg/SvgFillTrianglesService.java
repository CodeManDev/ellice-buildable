package dev.felix.ellice.ui.svg;

import java.util.ArrayList;
import java.util.List;

public final class SvgFillTrianglesService {
   private static final int count = 8;
   private static final float value = 4.0F;
   private static final float value2 = 3.0F;

   private SvgFillTrianglesService() {
   }

   public static float[] fillTriangles(SvgCodec svgCodec) {
      ArrayList arrayList = new ArrayList();

      for (SvgCodec.SubPath subPath : svgCodec.subPaths()) {
         if (subPath.vertexCount() >= 3) {
            updateState(subPath.points(), subPath.vertexCount(), arrayList);
         }
      }

      return arrayList.isEmpty() ? null : createFloat2(arrayList);
   }

   private static void updateState(float[] floats, int index, List<Float> items) {
      if (index >= 3) {
         int[] ints = new int[index];
         if (calculateValue(floats, index) < 0.0F) {
            int currentIndex = 0;

            while (currentIndex < index) {
               ints[currentIndex] = currentIndex++;
            }
         } else {
            for (int nextIndex = 0; nextIndex < index; nextIndex++) {
               ints[nextIndex] = index - 1 - nextIndex;
            }
         }

         int value = index;
         int previousIndex = 0;
         int sourceIndex = 0;

         while (value > 2 && previousIndex < value) {
            int targetIndex = (sourceIndex - 1 + value) % value;
            int inputIndex = (sourceIndex + 1) % value;
            int currentValue = ints[targetIndex];
            int nextValue = ints[sourceIndex];
            int previousValue = ints[inputIndex];
            float sourceValue = floats[currentValue * 2];
            float targetValue = floats[currentValue * 2 + 1];
            float inputValue = floats[nextValue * 2];
            float outputValue = floats[nextValue * 2 + 1];
            float resultValue = floats[previousValue * 2];
            float candidateValue = floats[previousValue * 2 + 1];
            if (calculateValue2(sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue) > 0.0F
               && checkCondition(floats, ints, value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue)) {
               updateState5(items, sourceValue, targetValue, 0.0F);
               updateState5(items, inputValue, outputValue, 0.0F);
               updateState5(items, resultValue, candidateValue, 0.0F);
               System.arraycopy(ints, sourceIndex + 1, ints, sourceIndex, value - sourceIndex - 1);
               value += -1;
               if (sourceIndex >= value) {
                  sourceIndex = 0;
               }

               previousIndex = 0;
            } else {
               sourceIndex = (sourceIndex + 1) % value;
               previousIndex++;
            }
         }
      }
   }

   private static float calculateValue(float[] floats, int value) {
      float currentValue = 0.0F;

      for (int index = 0; index < value; index++) {
         int nextValue = (index + 1) % value;
         currentValue += floats[index * 2] * floats[nextValue * 2 + 1] - floats[nextValue * 2] * floats[index * 2 + 1];
      }

      return currentValue;
   }

   private static float calculateValue2(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue) {
      return (nextValue - value) * (targetValue - currentValue) - (previousValue - currentValue) * (sourceValue - value);
   }

   private static boolean checkCondition(
      float[] floats,
      int[] ints,
      int value,
      int currentValue,
      int nextValue,
      int previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue
   ) {
      for (int index = 0; index < value; index++) {
         int selectedValue = ints[index];
         if (selectedValue != currentValue && selectedValue != nextValue && selectedValue != previousValue) {
            float defaultValue = floats[selectedValue * 2];
            float initialValue = floats[selectedValue * 2 + 1];
            if (checkCondition2(defaultValue, initialValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue)) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean checkCondition2(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue, float outputValue) {
      float resultValue = calculateValue2(value, currentValue, nextValue, previousValue, sourceValue, targetValue);
      float candidateValue = calculateValue2(value, currentValue, sourceValue, targetValue, inputValue, outputValue);
      float selectedValue = calculateValue2(value, currentValue, inputValue, outputValue, nextValue, previousValue);
      return !(resultValue < 0.0F) && !(candidateValue < 0.0F) && !(selectedValue < 0.0F) || !(resultValue > 0.0F) && !(candidateValue > 0.0F) && !(selectedValue > 0.0F);
   }

   public static float[] strokeTriangles(SvgCodec svgCodec, float value, SvgFillTrianglesService.LineCap lineCap, SvgFillTrianglesService.LineJoin lineJoin) {
      return strokeTriangles(svgCodec, value, lineCap, lineJoin, 4.0F);
   }

   public static float[] strokeTriangles(SvgCodec svgCodec, float value, SvgFillTrianglesService.LineCap lineCap, SvgFillTrianglesService.LineJoin lineJoin, float currentValue) {
      ArrayList arrayList = new ArrayList();
      float nextValue = value * 0.5F;

      for (SvgCodec.SubPath subPath : svgCodec.subPaths()) {
         updateState2(arrayList, subPath, nextValue, lineCap, lineJoin, currentValue);
      }

      return arrayList.isEmpty() ? null : createFloat2(arrayList);
   }

   private static void updateState2(
      List<Float> items, SvgCodec.SubPath subPath, float value, SvgFillTrianglesService.LineCap lineCap, SvgFillTrianglesService.LineJoin lineJoin, float currentValue
   ) {
      int index = subPath.vertexCount();
      if (index >= 2) {
         boolean enabled = subPath.closed();
         if (enabled && index >= 3) {
            float currentX = subPath.x(index - 1) - subPath.x(0);
            float currentY = subPath.y(index - 1) - subPath.y(0);
            if (currentX * currentX + currentY * currentY < 0.01F) {
               index += -1;
            }
         }

         if (index >= 2) {
            int currentIndex = enabled ? index : index - 1;
            float[] floats = new float[currentIndex];
            float[] currentFloats = new float[currentIndex];
            float[] nextFloats = new float[currentIndex];

            for (int nextIndex = 0; nextIndex < currentIndex; nextIndex++) {
               int nextValue = (nextIndex + 1) % index;
               float nextX = subPath.x(nextValue) - subPath.x(nextIndex);
               float nextY = subPath.y(nextValue) - subPath.y(nextIndex);
               float previousValue = (float)Math.sqrt(nextX * nextX + nextY * nextY);
               nextFloats[nextIndex] = previousValue;
               if (previousValue > 0.001F) {
                  floats[nextIndex] = -nextY / previousValue;
                  currentFloats[nextIndex] = nextX / previousValue;
               }
            }

            float[] previousFloats = new float[index];

            for (int previousIndex = 1; previousIndex < index; previousIndex++) {
               previousFloats[previousIndex] = previousFloats[previousIndex - 1] + nextFloats[previousIndex - 1];
            }

            for (int sourceIndex = 0; sourceIndex < currentIndex; sourceIndex++) {
               int targetIndex = sourceIndex;
               int inputIndex = (sourceIndex + 1) % index;
               if (!(nextFloats[sourceIndex] < 0.001F)) {
                  float sourceValue = previousFloats[targetIndex];
                  float targetValue = inputIndex > targetIndex ? previousFloats[inputIndex] : previousFloats[index - 1] + nextFloats[currentIndex - 1];
                  float[] sourceFloats = createFloat(targetIndex, sourceIndex, index, currentIndex, enabled, floats, currentFloats, nextFloats, value, lineJoin, currentValue);
                  float[] targetFloats = createFloat(inputIndex, sourceIndex, index, currentIndex, enabled, floats, currentFloats, nextFloats, value, lineJoin, currentValue);
                  updateState5(items, subPath.x(targetIndex) + sourceFloats[0], subPath.y(targetIndex) + sourceFloats[1], sourceValue);
                  updateState5(items, subPath.x(targetIndex) + sourceFloats[2], subPath.y(targetIndex) + sourceFloats[3], sourceValue);
                  updateState5(items, subPath.x(inputIndex) + targetFloats[0], subPath.y(inputIndex) + targetFloats[1], targetValue);
                  updateState5(items, subPath.x(targetIndex) + sourceFloats[2], subPath.y(targetIndex) + sourceFloats[3], sourceValue);
                  updateState5(items, subPath.x(inputIndex) + targetFloats[2], subPath.y(inputIndex) + targetFloats[3], targetValue);
                  updateState5(items, subPath.x(inputIndex) + targetFloats[0], subPath.y(inputIndex) + targetFloats[1], targetValue);
               }
            }

            int inputValue = enabled ? 0 : 1;
            int outputValue = enabled ? index : index - 1;

            for (int outputIndex = inputValue; outputIndex < outputValue; outputIndex++) {
               int resultIndex = enabled ? (outputIndex - 1 + currentIndex) % currentIndex : outputIndex - 1;
               int candidateIndex = enabled ? outputIndex % currentIndex : outputIndex;
               if (!(nextFloats[resultIndex] < 0.001F) && !(nextFloats[candidateIndex] < 0.001F)) {
                  updateState3(
                     items,
                     subPath.x(outputIndex),
                     subPath.y(outputIndex),
                     previousFloats[outputIndex],
                     floats[resultIndex],
                     currentFloats[resultIndex],
                     floats[candidateIndex],
                     currentFloats[candidateIndex],
                     value,
                     lineJoin,
                     currentValue
                  );
               }
            }

            if (!enabled) {
               float previousX = nextFloats[0] > 0.001F ? (subPath.x(1) - subPath.x(0)) / nextFloats[0] : 1.0F;
               float previousY = nextFloats[0] > 0.001F ? (subPath.y(1) - subPath.y(0)) / nextFloats[0] : 0.0F;
               updateState4(items, subPath.x(0), subPath.y(0), previousX, previousY, floats[0], currentFloats[0], value, previousFloats[0], lineCap, true);
               int selectedIndex = currentIndex - 1;
               float sourceX = nextFloats[selectedIndex] > 0.001F ? (subPath.x(index - 1) - subPath.x(index - 2)) / nextFloats[selectedIndex] : 1.0F;
               float sourceY = nextFloats[selectedIndex] > 0.001F ? (subPath.y(index - 1) - subPath.y(index - 2)) / nextFloats[selectedIndex] : 0.0F;
               updateState4(
                  items, subPath.x(index - 1), subPath.y(index - 1), sourceX, sourceY, floats[selectedIndex], currentFloats[selectedIndex], value, previousFloats[index - 1], lineCap, false
               );
            }
         }
      }
   }

   private static float[] createFloat(
      int value,
      int index,
      int currentValue,
      int nextValue,
      boolean enabled,
      float[] floats,
      float[] currentFloats,
      float[] nextFloats,
      float previousValue,
      SvgFillTrianglesService.LineJoin lineJoin,
      float sourceValue
   ) {
      float targetValue = floats[index];
      float inputValue = currentFloats[index];
      int outputValue = value == index ? 1 : 0;
      if (enabled || (outputValue == 0 || value != 0) && (outputValue != 0 || value != currentValue - 1)) {
         int currentIndex = outputValue != 0 ? (index - 1 + nextValue) % nextValue : (index + 1) % nextValue;
         if (nextFloats[currentIndex] < 0.001F) {
            return new float[]{targetValue * previousValue, inputValue * previousValue, -targetValue * previousValue, -inputValue * previousValue};
         }

         float resultValue = floats[currentIndex];
         float candidateValue = currentFloats[currentIndex];
         float selectedValue;
         float defaultValue;
         float initialValue;
         float resolvedValue;
         if (outputValue != 0) {
            selectedValue = resultValue;
            defaultValue = candidateValue;
            initialValue = targetValue;
            resolvedValue = inputValue;
         } else {
            selectedValue = targetValue;
            defaultValue = inputValue;
            initialValue = resultValue;
            resolvedValue = candidateValue;
         }

         float computedValue = selectedValue + initialValue;
         float cachedValue = defaultValue + resolvedValue;
         float pendingValue = (float)Math.sqrt(computedValue * computedValue + cachedValue * cachedValue);
         if (pendingValue < 0.001F) {
            return new float[]{targetValue * previousValue, inputValue * previousValue, -targetValue * previousValue, -inputValue * previousValue};
         }

         computedValue /= pendingValue;
         cachedValue /= pendingValue;
         float activeValue = computedValue * selectedValue + cachedValue * defaultValue;
         if (activeValue < 0.01F) {
            activeValue = 0.01F;
         }

         float fallbackValue = previousValue / activeValue;
         if (fallbackValue <= sourceValue * previousValue) {
            return new float[]{computedValue * fallbackValue, cachedValue * fallbackValue, -computedValue * fallbackValue, -cachedValue * fallbackValue};
         }

         float primaryValue = Math.min(fallbackValue, 3.0F * previousValue);
         float secondaryValue = selectedValue * resolvedValue - defaultValue * initialValue;
         return secondaryValue > 0.0F
            ? new float[]{computedValue * primaryValue, cachedValue * primaryValue, -targetValue * previousValue, -inputValue * previousValue}
            : new float[]{targetValue * previousValue, inputValue * previousValue, -computedValue * primaryValue, -cachedValue * primaryValue};
      } else {
         return new float[]{targetValue * previousValue, inputValue * previousValue, -targetValue * previousValue, -inputValue * previousValue};
      }
   }

   private static void updateState3(
      List<Float> items,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      SvgFillTrianglesService.LineJoin lineJoin,
      float resultValue
   ) {
      float candidateValue = previousValue + targetValue;
      float selectedValue = sourceValue + inputValue;
      float defaultValue = (float)Math.sqrt(candidateValue * candidateValue + selectedValue * selectedValue);
      if (!(defaultValue < 0.001F)) {
         candidateValue /= defaultValue;
         selectedValue /= defaultValue;
         float initialValue = candidateValue * previousValue + selectedValue * sourceValue;
         if (initialValue < 0.01F) {
            initialValue = 0.01F;
         }

         float resolvedValue = outputValue / initialValue;
         if (!(resolvedValue <= resultValue * outputValue)) {
            float computedValue = previousValue * inputValue - sourceValue * targetValue;
            float cachedValue = computedValue > 0.0F ? -1.0F : 1.0F;
            float pendingValue = Math.min(resolvedValue, 3.0F * outputValue);
            float activeValue = value - cachedValue * candidateValue * pendingValue;
            float fallbackValue = currentValue - cachedValue * selectedValue * pendingValue;
            if (lineJoin == SvgFillTrianglesService.LineJoin.ROUND) {
               float primaryValue = (float)Math.atan2(cachedValue * sourceValue, cachedValue * previousValue);
               float secondaryValue = (float)Math.atan2(cachedValue * inputValue, cachedValue * targetValue);
               float tertiaryValue = secondaryValue - primaryValue;
               if (tertiaryValue > 3.141592653589793) {
                  tertiaryValue -= 6.2831855F;
               }

               if (tertiaryValue < -3.141592653589793) {
                  tertiaryValue += 6.2831855F;
               }

               int temporaryValue = Math.max(2, (int)Math.ceil(Math.abs(tertiaryValue) / 0.7853981633974483));

               for (int index = 0; index < temporaryValue; index++) {
                  float requestedValue = primaryValue + tertiaryValue * index / temporaryValue;
                  float actualValue = primaryValue + tertiaryValue * (index + 1) / temporaryValue;
                  updateState5(items, activeValue, fallbackValue, nextValue);
                  updateState5(items, value + outputValue * (float)Math.cos(requestedValue), currentValue + outputValue * (float)Math.sin(requestedValue), nextValue);
                  updateState5(items, value + outputValue * (float)Math.cos(actualValue), currentValue + outputValue * (float)Math.sin(actualValue), nextValue);
               }
            } else {
               updateState5(items, activeValue, fallbackValue, nextValue);
               updateState5(items, value + cachedValue * previousValue * outputValue, currentValue + cachedValue * sourceValue * outputValue, nextValue);
               updateState5(items, value + cachedValue * targetValue * outputValue, currentValue + cachedValue * inputValue * outputValue, nextValue);
            }
         }
      }
   }

   private static void updateState4(
      List<Float> items,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      SvgFillTrianglesService.LineCap lineCap,
      boolean enabled
   ) {
      if (lineCap != SvgFillTrianglesService.LineCap.BUTT) {
         if (lineCap == SvgFillTrianglesService.LineCap.ROUND) {
            float resultValue = enabled ? -nextValue : nextValue;
            float candidateValue = enabled ? -previousValue : previousValue;
            float selectedValue = (float)Math.atan2(candidateValue, resultValue);
            float defaultValue = selectedValue - 1.5707964F;
            float initialValue = selectedValue + 1.5707964F;

            for (int index = 0; index < 8; index++) {
               float resolvedValue = defaultValue + (initialValue - defaultValue) * index / 8.0F;
               float computedValue = defaultValue + (initialValue - defaultValue) * (index + 1) / 8.0F;
               updateState5(items, value, currentValue, outputValue);
               updateState5(items, value + inputValue * (float)Math.cos(resolvedValue), currentValue + inputValue * (float)Math.sin(resolvedValue), outputValue);
               updateState5(items, value + inputValue * (float)Math.cos(computedValue), currentValue + inputValue * (float)Math.sin(computedValue), outputValue);
            }
         } else if (lineCap == SvgFillTrianglesService.LineCap.SQUARE) {
            float cachedValue;
            float pendingValue;
            if (enabled) {
               cachedValue = -nextValue * inputValue;
               pendingValue = -previousValue * inputValue;
            } else {
               cachedValue = nextValue * inputValue;
               pendingValue = previousValue * inputValue;
            }

            float activeValue = sourceValue * inputValue;
            float fallbackValue = targetValue * inputValue;
            updateState5(items, value + activeValue, currentValue + fallbackValue, outputValue);
            updateState5(items, value - activeValue, currentValue - fallbackValue, outputValue);
            updateState5(items, value + activeValue + cachedValue, currentValue + fallbackValue + pendingValue, outputValue);
            updateState5(items, value - activeValue, currentValue - fallbackValue, outputValue);
            updateState5(items, value - activeValue + cachedValue, currentValue - fallbackValue + pendingValue, outputValue);
            updateState5(items, value + activeValue + cachedValue, currentValue + fallbackValue + pendingValue, outputValue);
         }
      }
   }

   public static float pathLength(SvgCodec svgCodec) {
      float value = 0.0F;

      for (SvgCodec.SubPath subPath : svgCodec.subPaths()) {
         int currentValue = subPath.vertexCount();

         for (int index = 0; index < currentValue - 1; index++) {
            float currentX = subPath.x(index + 1) - subPath.x(index);
            float currentY = subPath.y(index + 1) - subPath.y(index);
            value += (float)Math.sqrt(currentX * currentX + currentY * currentY);
         }
      }

      return value;
   }

   private static void updateState5(List<Float> items, float value, float currentValue, float nextValue) {
      items.add(Float.isFinite(value) ? value : 0.0F);
      items.add(Float.isFinite(currentValue) ? currentValue : 0.0F);
      items.add(nextValue);
   }

   private static float[] createFloat2(List<Float> items) {
      float[] currentSize = new float[items.size()];

      for (int index = 0; index < currentSize.length; index++) {
         currentSize[index] = (Float)items.get(index);
      }

      return currentSize;
   }

   public enum LineCap {
      BUTT,
      ROUND,
      SQUARE;


      private static SvgFillTrianglesService.LineCap[] $values() {
         return new SvgFillTrianglesService.LineCap[]{BUTT, ROUND, SQUARE};
      }
   }

   public enum LineJoin {
      MITER,
      ROUND,
      BEVEL;


      private static SvgFillTrianglesService.LineJoin[] $values() {
         return new SvgFillTrianglesService.LineJoin[]{MITER, ROUND, BEVEL};
      }
   }
}
