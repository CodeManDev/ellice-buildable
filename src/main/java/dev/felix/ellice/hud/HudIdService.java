package dev.felix.ellice.hud;

import com.google.gson.JsonObject;

public final class HudIdService {
   private final String text;
   private final String text2;
   private String text3;
   private boolean enabled = true;
   private float value;
   private float value2;
   private float value3 = 80.0F;
   private float value4 = 18.0F;
   private boolean enabled2;
   private JsonObject jsonObject = new JsonObject();

   public HudIdService(String currentText, String nextText, String previousText, float currentValue, float nextValue) {
      this.text = currentText;
      this.text2 = nextText;
      this.text3 = previousText;
      this.value = currentValue;
      this.value2 = nextValue;
   }

   public String id() {
      return this.text;
   }

   public String type() {
      return this.text2;
   }

   public String label() {
      return this.text3;
   }

   public boolean visible() {
      return this.enabled;
   }

   public float x() {
      return this.value;
   }

   public float y() {
      return this.value2;
   }

   public float width() {
      return this.value3;
   }

   public float height() {
      return this.value4;
   }

   public boolean customSize() {
      return this.enabled2;
   }

   public JsonObject settings() {
      return this.jsonObject;
   }

   public void label(String text) {
      this.text3 = text;
   }

   public void visible(boolean currentVisible) {
      this.enabled = currentVisible;
   }

   public void position(float x, float y) {
      this.value = x;
      this.value2 = y;
   }

   public void bounds(float value, float currentValue) {
      if (!this.enabled2) {
         this.updateState(value, currentValue);
      }
   }

   public void resize(float value, float currentValue) {
      this.enabled2 = true;
      this.updateState(value, currentValue);
   }

   public void clearCustomSize() {
      this.enabled2 = false;
   }

   public void storedBounds(float value, float currentValue, boolean enabled) {
      this.updateState(value, currentValue);
      this.enabled2 = enabled;
   }

   private void updateState(float value, float currentValue) {
      this.value3 = Math.max(1.0F, value);
      this.value4 = Math.max(1.0F, currentValue);
   }

   public void settings(JsonObject currentJsonObject) {
      this.jsonObject = currentJsonObject != null ? currentJsonObject.deepCopy() : new JsonObject();
   }

   public String stringSetting(String text, String currentText) {
      return this.jsonObject.has(text) ? this.jsonObject.get(text).getAsString() : currentText;
   }

   public float numberSetting(String text, float value) {
      return this.jsonObject.has(text) ? this.jsonObject.get(text).getAsFloat() : value;
   }

   public int colorSetting(String text, int color) {
      return this.jsonObject.has(text) ? this.jsonObject.get(text).getAsInt() : color;
   }

   public void setString(String text, String currentText) {
      this.jsonObject.addProperty(text, currentText);
   }

   public void setNumber(String text, float value) {
      this.jsonObject.addProperty(text, value);
   }

   public void setColor(String text, int color) {
      this.jsonObject.addProperty(text, color);
   }
}

