package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.module.ModuleSetting;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BuiltinRangeService {
   private final ModuleSetting<?> moduleNameService2;
   private final float value2;
   private final float value3;
   private final float value4;
   private String text;
   private String text2;

   public BuiltinRangeService(ModuleSetting.Number number) {
      this.moduleNameService2 = number;
      this.value2 = number.min();
      this.value3 = number.max();
      this.value4 = number.step();
      this.text = format((Float)number.get(), this.value4, this.value2);
   }

   public BuiltinRangeService(ModuleSetting.Range range) {
      this.moduleNameService2 = range;
      this.value2 = range.min();
      this.value3 = range.max();
      this.value4 = range.step();
      this.text = format(range.low(), this.value4, this.value2);
      this.text2 = format(range.high(), this.value4, this.value2);
   }

   public boolean range() {
      return this.text2 != null;
   }

   public String low() {
      return this.text;
   }

   public String high() {
      return this.text2;
   }

   public void low(String currentText) {
      this.text = currentText;
   }

   public void high(String text) {
      this.text2 = text;
   }

   public String bounds() {
      return format(this.value2, this.value4, this.value2)
         + " – "
         + format(this.value3, this.value4, this.value2);
   }

   public String stepLabel() {
      return this.value4 > 0.0F ? "Step " + format(this.value4, this.value4, 0.0F) : "Continuous";
   }

   public String error() {
      Float currentValue = calculateValue2(this.text);
      Float nextValue = this.range() ? calculateValue2(this.text2) : currentValue;
      if (currentValue != null && nextValue != null) {
         if (currentValue < this.value2 || currentValue > this.value3 || nextValue < this.value2 || nextValue > this.value3) {
            return "Use a value from " + this.bounds() + ".";
         } else {
            return currentValue > nextValue ? "The lower value must not exceed the upper value." : "";
         }
      } else {
         return "Enter a finite number.";
      }
   }

   public boolean apply() {
      if (this.moduleNameService2.isActive() && this.error().isEmpty()) {
         if (this.moduleNameService2 instanceof ModuleSetting.Number number) {
            number.set(calculateValue2(this.text));
         } else if (this.moduleNameService2 instanceof ModuleSetting.Range range) {
            range.set(calculateValue2(this.text), calculateValue2(this.text2));
         }

         return true;
      } else {
         return false;
      }
   }

   public static String format(float value, float currentValue, float nextValue) {
      int previousValue = currentValue > 0.0F ? Math.max(calculateValue(currentValue), calculateValue(nextValue)) : 6;
      return new BigDecimal(Float.toString(value)).setScale(Math.min(8, previousValue), RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
   }

   public static int maxCharacters(float value, float currentValue, float nextValue) {
      int previousValue = nextValue > 0.0F ? Math.min(8, Math.max(calculateValue(nextValue), calculateValue(value))) : 6;
      int currentLength = new BigDecimal(Float.toString(Math.max(Math.abs(value), Math.abs(currentValue))))
         .setScale(0, RoundingMode.DOWN)
         .toPlainString()
         .length();
      return currentLength + (value < 0.0F ? 1 : 0) + (previousValue > 0 ? 1 + previousValue : 0);
   }

   private static int calculateValue(float value) {
      return Math.max(0, new BigDecimal(Float.toString(value)).stripTrailingZeros().scale());
   }

   private static Float calculateValue2(String text) {
      if (text == null) {
         return null;
      }

      try {
         float value = Float.parseFloat(text.trim().replace(',', '.'));
         return Float.isFinite(value) ? value : null;
      } catch (NumberFormatException numberFormatException) {
         return null;
      }
   }
}
