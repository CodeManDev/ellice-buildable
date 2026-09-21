package dev.felix.ellice.feature.cape;

import java.util.ArrayList;

public final class CapePlayerService {
   private final ArrayList<Float> items = new ArrayList<>();

   private CapePlayerService() {
   }

   public static float[] player(boolean enabled, boolean currentEnabled) {
      CapePlayerService capePlayer = new CapePlayerService();
      int value = enabled ? 3 : 4;
      capePlayer.updateState(
         -4.0F,
         24.0F,
         -4.0F,
         8,
         8,
         8,
         0,
         0,
         64,
         currentEnabled ? 32 : 64,
         0.0F
      );
      capePlayer.updateState(
         -4.0F,
         12.0F,
         -2.0F,
         8,
         12,
         4,
         16,
         16,
         64,
         currentEnabled ? 32 : 64,
         0.0F
      );
      capePlayer.updateState(
         -4 - value, 12.0F, -2.0F, value, 12, 4, 40, 16, 64, currentEnabled ? 32 : 64, 0.0F
      );
      capePlayer.updateState(
         4.0F,
         12.0F,
         -2.0F,
         value,
         12,
         4,
         currentEnabled ? 40 : 32,
         currentEnabled ? 16 : 48,
         64,
         currentEnabled ? 32 : 64,
         0.0F
      );
      capePlayer.updateState(
         -4.0F, 0.0F, -2.0F, 4, 12, 4, 0, 16, 64, currentEnabled ? 32 : 64, 0.0F
      );
      capePlayer.updateState(0.0F, 0.0F, -2.0F, 4, 12, 4, currentEnabled ? 0 : 16, currentEnabled ? 16 : 48, 64, currentEnabled ? 32 : 64, 0.0F);
      capePlayer.updateState(
         -4.0F,
         24.0F,
         -4.0F,
         8,
         8,
         8,
         32,
         0,
         64,
         currentEnabled ? 32 : 64,
         0.45F
      );
      if (!currentEnabled) {
         capePlayer.updateState(
            -4.0F,
            12.0F,
            -2.0F,
            8,
            12,
            4,
            16,
            32,
            64,
            64,
            0.23F
         );
         capePlayer.updateState(
            -4 - value,
            12.0F,
            -2.0F,
            value,
            12,
            4,
            40,
            32,
            64,
            64,
            0.23F
         );
         capePlayer.updateState(
            4.0F,
            12.0F,
            -2.0F,
            value,
            12,
            4,
            48,
            48,
            64,
            64,
            0.23F
         );
         capePlayer.updateState(
            -4.0F,
            0.0F,
            -2.0F,
            4,
            12,
            4,
            0,
            32,
            64,
            64,
            0.23F
         );
         capePlayer.updateState(0.0F, 0.0F, -2.0F, 4, 12, 4, 0, 48, 64, 64, 0.23F);
      }

      return capePlayer.createFloat();
   }

   public static float[] cape(double doubleValue) {
      CapePlayerService capePlayer = new CapePlayerService();
      capePlayer.updateState(
         -5.0F,
         8.0F,
         -1.0F,
         10,
         16,
         1,
         0,
         0,
         64,
         32,
         0.0F
      );
      float[] floats = capePlayer.createFloat();
      double currentDoubleValue = Math.toRadians(
         10.0
            + Math.sin(doubleValue * 1.7) * 2.0
      );
      double nextDoubleValue = Math.cos(currentDoubleValue);
      double previousDoubleValue = Math.sin(currentDoubleValue);

      for (byte index = 0; index < floats.length; index += 8) {
         double sourceDoubleValue = 24.0F - floats[index + 1];
         double targetDoubleValue = -floats[index + 2];
         floats[index] = -floats[index];
         floats[index + 1] = (float)(24.0 - sourceDoubleValue * nextDoubleValue + targetDoubleValue * previousDoubleValue);
         floats[index + 2] = (float)(3.0 + sourceDoubleValue * previousDoubleValue + targetDoubleValue * nextDoubleValue);
         double inputDoubleValue = floats[index + 4];
         double outputDoubleValue = -floats[index + 5];
         floats[index + 3] = -floats[index + 3];
         floats[index + 4] = (float)(inputDoubleValue * nextDoubleValue + outputDoubleValue * previousDoubleValue);
         floats[index + 5] = (float)(-inputDoubleValue * previousDoubleValue + outputDoubleValue * nextDoubleValue);
      }

      return floats;
   }

   private void updateState(
      float value, float currentValue, float nextValue, int previousValue, int sourceValue, int targetValue, int inputValue, int outputValue, int resultValue, int candidateValue, float selectedValue
   ) {
      float defaultValue = value - selectedValue;
      float initialValue = value + previousValue + selectedValue;
      float resolvedValue = currentValue - selectedValue;
      float computedValue = currentValue + sourceValue + selectedValue;
      float cachedValue = nextValue - selectedValue;
      float pendingValue = nextValue + targetValue + selectedValue;
      this.updateState2(
         new float[][]{{initialValue, computedValue, cachedValue}, {defaultValue, computedValue, cachedValue}, {defaultValue, resolvedValue, cachedValue}, {initialValue, resolvedValue, cachedValue}},
         0.0F,
         0.0F,
         -1.0F,
         inputValue + targetValue,
         outputValue + targetValue,
         inputValue + targetValue + previousValue,
         outputValue + targetValue + sourceValue,
         resultValue,
         candidateValue
      );
      this.updateState2(
         new float[][]{{defaultValue, computedValue, pendingValue}, {initialValue, computedValue, pendingValue}, {initialValue, resolvedValue, pendingValue}, {defaultValue, resolvedValue, pendingValue}},
         0.0F,
         0.0F,
         1.0F,
         inputValue + targetValue + previousValue + targetValue,
         outputValue + targetValue,
         inputValue + targetValue + previousValue + targetValue + previousValue,
         outputValue + targetValue + sourceValue,
         resultValue,
         candidateValue
      );
      this.updateState2(
         new float[][]{{defaultValue, computedValue, cachedValue}, {defaultValue, computedValue, pendingValue}, {defaultValue, resolvedValue, pendingValue}, {defaultValue, resolvedValue, cachedValue}},
         -1.0F,
         0.0F,
         0.0F,
         inputValue,
         outputValue + targetValue,
         inputValue + targetValue,
         outputValue + targetValue + sourceValue,
         resultValue,
         candidateValue
      );
      this.updateState2(
         new float[][]{{initialValue, computedValue, pendingValue}, {initialValue, computedValue, cachedValue}, {initialValue, resolvedValue, cachedValue}, {initialValue, resolvedValue, pendingValue}},
         1.0F,
         0.0F,
         0.0F,
         inputValue + targetValue + previousValue,
         outputValue + targetValue,
         inputValue + targetValue + previousValue + targetValue,
         outputValue + targetValue + sourceValue,
         resultValue,
         candidateValue
      );
      this.updateState2(
         new float[][]{{defaultValue, computedValue, cachedValue}, {initialValue, computedValue, cachedValue}, {initialValue, computedValue, pendingValue}, {defaultValue, computedValue, pendingValue}},
         0.0F,
         1.0F,
         0.0F,
         inputValue + targetValue,
         outputValue,
         inputValue + targetValue + previousValue,
         outputValue + targetValue,
         resultValue,
         candidateValue
      );
      this.updateState2(
         new float[][]{{defaultValue, resolvedValue, pendingValue}, {initialValue, resolvedValue, pendingValue}, {initialValue, resolvedValue, cachedValue}, {defaultValue, resolvedValue, cachedValue}},
         0.0F,
         -1.0F,
         0.0F,
         inputValue + targetValue + previousValue,
         outputValue,
         inputValue + targetValue + previousValue + previousValue,
         outputValue + targetValue,
         resultValue,
         candidateValue
      );
   }

   private void updateState2(
      float[][] floats, float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue, int outputValue, int resultValue
   ) {
      float[][] currentFloats = new float[][]{
         {previousValue / outputValue, sourceValue / resultValue}, {targetValue / outputValue, sourceValue / resultValue}, {targetValue / outputValue, inputValue / resultValue}, {previousValue / outputValue, inputValue / resultValue}
      };

      for (int index : new int[]{0, 1, 2, 0, 2, 3}) {
         for (float candidateValue : new float[]{floats[index][0], floats[index][1], floats[index][2], value, currentValue, nextValue, currentFloats[index][0], currentFloats[index][1]}) {
            this.items.add(candidateValue);
         }
      }
   }

   private float[] createFloat() {
      float[] currentSize = new float[this.items.size()];

      for (int index = 0; index < currentSize.length; index++) {
         currentSize[index] = this.items.get(index);
      }

      return currentSize;
   }
}
