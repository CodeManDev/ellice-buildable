package dev.felix.ellice.diagnostics.fatal;

import java.util.Arrays;

final class FatalScreenCanvas {
   private static final int count = 5;
   private static final int count2 = 7;
   private static final long[] long2 = new long[128];
   final int[] px;
   final int w;
   final int h;

   private static void updateState(char index, int value, int currentValue, int nextValue, int previousValue, int sourceValue, int targetValue, int inputValue) {
      if (index < 128) {
         long2[index] = (long)value << 48
            | (long)currentValue << 40
            | (long)nextValue << 32
            | (long)previousValue << 24
            | (long)sourceValue << 16
            | (long)targetValue << 8
            | inputValue;
      }
   }

   FatalScreenCanvas(int value, int currentValue) {
      this.w = Math.max(1, value);
      this.h = Math.max(1, currentValue);
      this.px = new int[this.w * this.h];
   }

   void clear(int value) {
      Arrays.fill(this.px, value);
   }

   void fill(int value, int currentValue, int nextValue, int previousValue, int sourceValue) {
      int targetValue = Math.max(0, value);
      int inputValue = Math.max(0, currentValue);
      int outputValue = Math.min(this.w, value + nextValue);
      int resultValue = Math.min(this.h, currentValue + previousValue);

      for (int index = inputValue; index < resultValue; index++) {
         int candidateValue = index * this.w;

         for (int currentIndex = targetValue; currentIndex < outputValue; currentIndex++) {
            this.px[candidateValue + currentIndex] = sourceValue;
         }
      }
   }

   int textWidth(String id, int width) {
      return id != null && !id.isEmpty() ? id.length() * 6 * width - width : 0;
   }

   int text(int value, int currentValue, int nextValue, int previousValue, String currentText) {
      if (currentText == null) {
         return value;
      }

      int sourceValue = value;
      int targetValue = 6 * nextValue;

      for (int index = 0; index < currentText.length(); index++) {
         this.updateState2(sourceValue, currentValue, nextValue, previousValue, currentText.charAt(index));
         sourceValue += targetValue;
      }

      return sourceValue;
   }

   int wrapped(int value, int currentValue, int nextValue, int previousValue, int sourceValue, int targetValue, String currentText) {
      if (currentText != null && !currentText.isEmpty()) {
         String nextText = currentText.replace('\n', ' ');
         int inputValue = Math.max(1, (nextValue + previousValue) / (6 * previousValue));

         while (!nextText.isEmpty()) {
            int currentLength = Math.min(inputValue, nextText.length());
            if (currentLength < nextText.length()) {
               int outputValue = nextText.lastIndexOf(32, currentLength);
               if (outputValue > 0) {
                  currentLength = outputValue;
               }
            }

            this.text(value, currentValue, previousValue, targetValue, nextText.substring(0, currentLength).trim());
            nextText = nextText.substring(currentLength).trim();
            currentValue += sourceValue;
         }

         return currentValue;
      } else {
         return currentValue;
      }
   }

   String clip(String text, int y, int width) {
      if (text == null) {
         return "";
      }

      if (this.textWidth(text, width) <= y) {
         return text;
      }

      String currentText = "...";
      int currentLength = text.length();

      while (currentLength > 0 && this.textWidth(text.substring(0, currentLength) + currentText, width) > y) {
         currentLength += -1;
      }

      return currentLength <= 0 ? currentText : text.substring(0, currentLength) + currentText;
   }

   void qr(boolean[][] booleans, int value, int currentValue, int nextValue) {
      if (booleans != null && booleans.length != 0) {
         byte byteValue = 4;
         int currentLength = booleans.length + byteValue * 2;
         int previousValue = Math.max(1, nextValue / currentLength);
         int sourceValue = previousValue * currentLength;
         int targetValue = value + (nextValue - sourceValue) / 2;
         int inputValue = currentValue + (nextValue - sourceValue) / 2;
         this.fill(targetValue, inputValue, sourceValue, sourceValue, -1);

         for (int index = 0; index < booleans.length; index++) {
            for (int currentIndex = 0; currentIndex < booleans[index].length; currentIndex++) {
               if (booleans[index][currentIndex]) {
                  this.fill(targetValue + (currentIndex + byteValue) * previousValue, inputValue + (index + byteValue) * previousValue, previousValue, previousValue, -15658735);
               }
            }
         }
      }
   }

   private void updateState2(int value, int currentValue, int nextValue, int previousValue, char character) {
      char index = character < 128 ? character : '?';
      long offset = long2[index];
      if (offset == 0L && index != ' ') {
         offset = long2[63];
      }

      for (int currentIndex = 0; currentIndex < 7; currentIndex++) {
         int sourceValue = (int)(offset >> (6 - currentIndex) * 8 & 31L);

         for (int nextIndex = 0; nextIndex < 5; nextIndex++) {
            if ((sourceValue & 1 << 4 - nextIndex) != 0) {
               this.fill(value + nextIndex * nextValue, currentValue + currentIndex * nextValue, nextValue, nextValue, previousValue);
            }
         }
      }
   }

   static {
      updateState(' ', 0, 0, 0, 0, 0, 0, 0);
      updateState('!', 4, 4, 4, 4, 0, 4, 0);
      updateState('#', 10, 31, 10, 31, 10, 0, 0);
      updateState('%', 17, 2, 4, 8, 17, 0, 0);
      updateState('(', 2, 4, 4, 4, 4, 2, 0);
      updateState(')', 8, 4, 4, 4, 4, 8, 0);
      updateState('+', 0, 4, 4, 31, 4, 4, 0);
      updateState(',', 0, 0, 0, 0, 4, 4, 8);
      updateState('-', 0, 0, 0, 31, 0, 0, 0);
      updateState('.', 0, 0, 0, 0, 0, 4, 0);
      updateState('/', 1, 2, 4, 8, 16, 0, 0);
      updateState('0', 14, 17, 19, 21, 25, 17, 14);
      updateState('1', 4, 12, 4, 4, 4, 4, 14);
      updateState('2', 14, 17, 1, 2, 4, 8, 31);
      updateState('3', 31, 2, 4, 2, 1, 17, 14);
      updateState('4', 2, 6, 10, 18, 31, 2, 2);
      updateState('5', 31, 16, 30, 1, 1, 17, 14);
      updateState('6', 6, 8, 16, 30, 17, 17, 14);
      updateState('7', 31, 1, 2, 4, 8, 8, 8);
      updateState('8', 14, 17, 17, 14, 17, 17, 14);
      updateState('9', 14, 17, 17, 15, 1, 2, 12);
      updateState(':', 0, 4, 0, 0, 4, 0, 0);
      updateState(';', 0, 4, 0, 0, 4, 4, 8);
      updateState('<', 2, 4, 8, 16, 8, 4, 2);
      updateState('=', 0, 0, 31, 0, 31, 0, 0);
      updateState('>', 8, 4, 2, 1, 2, 4, 8);
      updateState('?', 14, 17, 1, 2, 4, 0, 4);
      updateState('A', 14, 17, 17, 31, 17, 17, 17);
      updateState('B', 30, 17, 17, 30, 17, 17, 30);
      updateState('C', 14, 17, 16, 16, 16, 17, 14);
      updateState('D', 30, 17, 17, 17, 17, 17, 30);
      updateState('E', 31, 16, 16, 30, 16, 16, 31);
      updateState('F', 31, 16, 16, 30, 16, 16, 16);
      updateState('G', 14, 17, 16, 23, 17, 17, 15);
      updateState('H', 17, 17, 17, 31, 17, 17, 17);
      updateState('I', 14, 4, 4, 4, 4, 4, 14);
      updateState('J', 1, 1, 1, 1, 1, 17, 14);
      updateState('K', 17, 18, 20, 24, 20, 18, 17);
      updateState('L', 16, 16, 16, 16, 16, 16, 31);
      updateState('M', 17, 27, 21, 21, 17, 17, 17);
      updateState('N', 17, 25, 21, 19, 17, 17, 17);
      updateState('O', 14, 17, 17, 17, 17, 17, 14);
      updateState('P', 30, 17, 17, 30, 16, 16, 16);
      updateState('Q', 14, 17, 17, 17, 21, 18, 13);
      updateState('R', 30, 17, 17, 30, 20, 18, 17);
      updateState('S', 15, 16, 16, 14, 1, 1, 30);
      updateState('T', 31, 4, 4, 4, 4, 4, 4);
      updateState('U', 17, 17, 17, 17, 17, 17, 14);
      updateState('V', 17, 17, 17, 17, 17, 10, 4);
      updateState('W', 17, 17, 17, 21, 21, 27, 17);
      updateState('X', 17, 17, 10, 4, 10, 17, 17);
      updateState('Y', 17, 17, 10, 4, 4, 4, 4);
      updateState('Z', 31, 1, 2, 4, 8, 16, 31);
      updateState('[', 14, 8, 8, 8, 8, 8, 14);
      updateState(']', 14, 2, 2, 2, 2, 2, 14);
      updateState('_', 0, 0, 0, 0, 0, 0, 31);
      updateState('\'', 4, 4, 8, 0, 0, 0, 0);
      updateState('a', 0, 0, 14, 1, 15, 17, 15);
      updateState('b', 16, 16, 30, 17, 17, 17, 30);
      updateState('c', 0, 0, 14, 17, 16, 17, 14);
      updateState('d', 1, 1, 15, 17, 17, 17, 15);
      updateState('e', 0, 0, 14, 17, 31, 16, 14);
      updateState('f', 6, 8, 8, 30, 8, 8, 8);
      updateState('g', 0, 0, 15, 17, 15, 1, 14);
      updateState('h', 16, 16, 30, 17, 17, 17, 17);
      updateState('i', 4, 0, 12, 4, 4, 4, 14);
      updateState('j', 2, 0, 2, 2, 2, 18, 12);
      updateState('k', 16, 16, 18, 20, 24, 20, 18);
      updateState('l', 12, 4, 4, 4, 4, 4, 14);
      updateState('m', 0, 0, 26, 21, 21, 21, 21);
      updateState('n', 0, 0, 30, 17, 17, 17, 17);
      updateState('o', 0, 0, 14, 17, 17, 17, 14);
      updateState('p', 0, 0, 30, 17, 30, 16, 16);
      updateState('q', 0, 0, 15, 17, 15, 1, 1);
      updateState('r', 0, 0, 22, 25, 16, 16, 16);
      updateState('s', 0, 0, 15, 16, 14, 1, 30);
      updateState('t', 8, 8, 30, 8, 8, 8, 6);
      updateState('u', 0, 0, 17, 17, 17, 17, 15);
      updateState('v', 0, 0, 17, 17, 17, 10, 4);
      updateState('w', 0, 0, 17, 17, 21, 21, 10);
      updateState('x', 0, 0, 17, 10, 4, 10, 17);
      updateState('y', 0, 0, 17, 17, 15, 1, 14);
      updateState('z', 0, 0, 31, 2, 4, 8, 31);
      updateState('{', 6, 8, 8, 16, 8, 8, 6);
      updateState('}', 12, 2, 2, 1, 2, 2, 12);
      updateState('~', 0, 0, 8, 21, 2, 0, 0);
      updateState('·', 0, 0, 0, 4, 0, 0, 0);
      updateState('?', 14, 17, 1, 2, 4, 0, 4);
   }
}
