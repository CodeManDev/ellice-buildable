package dev.felix.ellice.hud;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.HudStyleSettings;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.text.TextMode;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;

public final class HudItemsService {
   private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
   private static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("h:mm a");
   private static final List<HudData> items2 = List.of(
      new HudData(
         "fps", "FPS", "Current rendered frames per second", 8.0F, 8.0F
      ),
      new HudData(
         "coordinates", "Coordinates", "Client-side player position", 8.0F, 30.0F
      ),
      new HudData(
         "ping", "Ping", "Server latency from the player list", 8.0F, 52.0F
      ),
      new HudData("clock", "Clock", "Local time", 8.0F, 74.0F),
      new HudData(
         "session", "Session", "Account, connection, and session time", 8.0F, 96.0F
      ),
      new HudData(
         "plugins", "Plugins", "Loaded ellice plugin count", 8.0F, 118.0F
      ),
      new HudData("text", "Text", "Custom text label", 8.0F, 210.0F)
   );
   private final List<HudIdService> items3 = new ArrayList<>();
   private int count2 = 1;
   private int count3;
   private int count4;
   private long timestamp = System.nanoTime();
   private final long timestamp2 = System.currentTimeMillis();

   public List<HudIdService> items() {
      return Collections.unmodifiableList(this.items3);
   }

   public List<HudData> catalog() {
      return items2;
   }

   public boolean isEmpty() {
      return this.items3.isEmpty();
   }

   public HudIdService get(String text) {
      for (HudIdService hudId : this.items3) {
         if (hudId.id().equals(text)) {
            return hudId;
         }
      }

      return null;
   }

   public HudIdService add(String currentText) {
      HudData hudData = this.createHudData(currentText);
      if (hudData == null) {
         hudData = this.createHudData("text");
      }

      int index = 0;

      for (HudIdService hudId : this.items3) {
         if (hudId.type().equals(hudData.id())) {
            index++;
         }
      }

      float value = index * 12;
      HudIdService currentHudId = new HudIdService(
         hudData.id() + "-" + this.count2++, hudData.id(), hudData.label(), hudData.defaultX() + value, hudData.defaultY() + value
      );
      this.updateState9(currentHudId);
      this.items3.add(currentHudId);
      return currentHudId;
   }

   public HudIdService duplicate(HudIdService hudId) {
      if (hudId == null) {
         return this.add("text");
      }

      HudIdService currentX = new HudIdService(
         hudId.type() + "-" + this.count2++,
         hudId.type(),
         hudId.label(),
         hudId.x() + 12.0F,
         hudId.y() + 12.0F
      );
      currentX.visible(hudId.visible());
      currentX.storedBounds(hudId.width(), hudId.height(), hudId.customSize());
      currentX.settings(hudId.settings());
      this.items3.add(currentX);
      return currentX;
   }

   public void remove(HudIdService hudId) {
      this.items3.remove(hudId);
   }

   public boolean move(HudIdService hudId, int value) {
      if (hudId != null && value != 0) {
         int index = this.items3.indexOf(hudId);
         if (index < 0) {
            return false;
         }

         int currentSize = Math.max(0, Math.min(this.items3.size() - 1, index + value));
         if (currentSize == index) {
            return false;
         }

         this.items3.remove(index);
         this.items3.add(currentSize, hudId);
         return true;
      } else {
         return false;
      }
   }

   public void ensureDefaults() {
      if (this.items3.isEmpty()) {
         this.add("fps");
         this.add("coordinates");
         this.add("ping");
         this.add("clock");
         this.add("session");
         this.add("plugins");
      }
   }

   public void renderAll(float x, float y) {
      this.updateState2();

      for (HudIdService hudId : this.items3) {
         if (hudId.visible()) {
            this.updateState(hudId, x, y);
         }
      }
   }

   private void updateState(HudIdService hudId, float value, float currentValue) {
      switch (hudId.type()) {
         case "fps":
            this.updateState7(hudId, "FPS " + this.count4);
            break;
         case "coordinates":
            this.updateState3(hudId);
            break;
         case "ping":
            this.updateState4(hudId);
            break;
         case "clock":
            this.updateState5(hudId);
            break;
         case "session":
            this.updateState6(hudId);
            break;
         case "plugins":
            this.updateState7(hudId, "Plugins " + this.calculateValue());
            break;
         default:
            this.updateState7(hudId, hudId.stringSetting("Text", hudId.label()));
      }

      hudId.position(
         calculateValue4(hudId.x(), 0.0F, Math.max(0.0F, value - hudId.width())),
         calculateValue4(hudId.y(), 0.0F, Math.max(0.0F, currentValue - hudId.height()))
      );
   }

   private void updateState2() {
      this.count3++;
      long offset = System.nanoTime();
      if (offset - this.timestamp >= 1000000000L) {
         this.count4 = this.count3;
         this.count3 = 0;
         this.timestamp = offset;
      }
   }

   private void updateState3(HudIdService hudId) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      LocalPlayer localPlayer = minecraft.player;
      if (localPlayer == null) {
         this.updateState7(hudId, "XYZ -- -- --");
      } else {
         if ("Precise".equals(hudId.stringSetting("Format", "Block"))) {
            this.updateState7(hudId, String.format(Locale.ROOT, "XYZ %.1f %.1f %.1f", localPlayer.getX(), localPlayer.getY(), localPlayer.getZ()));
         } else {
            this.updateState7(hudId, "XYZ " + localPlayer.getBlockX() + " " + localPlayer.getBlockY() + " " + localPlayer.getBlockZ());
         }
      }
   }

   private void updateState4(HudIdService hudId) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (minecraft.player != null && minecraft.getConnection() != null) {
         PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID());
         int value = playerInfo != null ? playerInfo.getLatency() : -1;
         this.updateState7(hudId, value >= 0 ? "Ping " + value + " ms" : "Ping -- ms");
      } else {
         this.updateState7(hudId, "Ping -- ms");
      }
   }

   private void updateState5(HudIdService hudId) {
      DateTimeFormatter currentDateTimeFormatter = "12h".equals(hudId.stringSetting("Format", "24h")) ? dateTimeFormatter2 : dateTimeFormatter;
      this.updateState7(hudId, LocalTime.now().format(currentDateTimeFormatter));
   }

   private void updateState6(HudIdService hudId) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      String text = minecraft.getUser().getName();
      String currentText = minecraft.isLocalServer() ? "Singleplayer" : "Multiplayer";
      Duration duration = Duration.ofMillis(System.currentTimeMillis() - this.timestamp2);
      long offset = duration.toMinutes();
      long currentOffset = duration.minusMinutes(offset).toSeconds();
      this.updateState7(hudId, text + " | " + currentText + " | " + offset + "m " + currentOffset + "s");
   }

   private int calculateValue() {
      return CoreIsInitializedHandler.get().pluginLoader() != null ? CoreIsInitializedHandler.get().pluginLoader().plugins().size() : 0;
   }

   private void updateState7(HudIdService hudId, String currentText) {
      if (currentText != null && !currentText.isBlank()) {
         CompositorPushPresentationScaleService compositorPushPresentationScale = CoreIsInitializedHandler.get().compositor();
         HudStyleSettings hudStyleSettings = CoreIsInitializedHandler.get().hudStyle();
         float value = (Float)hudStyleSettings.scale.get();
         float currentValue = hudStyleSettings.scaledFontSize();
         TextMode textMode = hudStyleSettings.fontVariant();
         float nextValue = (Float)hudStyleSettings.paddingX.get() * value;
         float previousValue = (Float)hudStyleSettings.paddingY.get() * value;
         float sourceValue = Math.max(10.0F * value, compositorPushPresentationScale.textLineHeight(currentValue, textMode));
         float targetValue = Math.max(24.0F * value, compositorPushPresentationScale.textWidth(currentText, currentValue, textMode) + nextValue * 2.0F);
         float inputValue = sourceValue + previousValue * 2.0F;
         hudId.bounds(targetValue, inputValue);
         float currentWidth = hudId.width();
         float currentHeight = hudId.height();
         this.updateState8(hudId.x(), hudId.y(), currentWidth, currentHeight, (Integer)hudStyleSettings.backgroundColor.get(), false);
         compositorPushPresentationScale.pushClip(hudId.x(), hudId.y(), currentWidth, currentHeight, (Float)CoreIsInitializedHandler.get().hudStyle().cornerRadius.get() * value);
         float currentY = hudId.y() + Math.max(0.0F, Math.min(previousValue, (currentHeight - sourceValue) * 0.5F));
         compositorPushPresentationScale.text(hudId.x() + nextValue, currentY, currentText, currentValue, (Integer)hudStyleSettings.textColor.get(), hudStyleSettings.textStyle());
         compositorPushPresentationScale.popClip();
      }
   }

   private void updateState8(float value, float currentValue, float nextValue, float previousValue, int sourceValue, boolean enabled) {
      HudStyleSettings hudStyleSettings = CoreIsInitializedHandler.get().hudStyle();
      HudStyleSettings.RenderStyle currentRenderStyle = hudStyleSettings.renderStyle(previousValue);
      int targetValue = !currentRenderStyle.drawFill() && !enabled ? 0 : 1;
      CompositorPushPresentationScaleService compositorPushPresentationScale = CoreIsInitializedHandler.get().compositor();
      if ((Boolean)hudStyleSettings.glass.get() && targetValue != 0) {
         if (currentRenderStyle.shadow() > 0.0F) {
            compositorPushPresentationScale.addRect(
               value,
               currentValue,
               nextValue,
               previousValue,
               currentRenderStyle.radius(),
               currentRenderStyle.radius(),
               currentRenderStyle.radius(),
               currentRenderStyle.radius(),
               0,
               0.0F,
               currentRenderStyle.shadow(),
               (Integer)hudStyleSettings.shadowColor.get(),
               0.0F,
               0,
               1.0F,
               (Float)hudStyleSettings.edgeSoftness.get() * (Float)hudStyleSettings.scale.get(),
               (Boolean)hudStyleSettings.insetShadow.get(),
               0,
               0
            );
         }

         compositorPushPresentationScale.glassRect(
            value,
            currentValue,
            nextValue,
            previousValue,
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            sourceValue,
            (Integer)hudStyleSettings.gradientEnd.get(),
            currentRenderStyle.blur(),
            (Float)hudStyleSettings.panelOpacity.get(),
            (Float)hudStyleSettings.glassSaturation.get(),
            (Float)hudStyleSettings.glassBrightness.get(),
            (Float)hudStyleSettings.glassContrast.get(),
            (Float)hudStyleSettings.glassRefraction.get(),
            (Float)hudStyleSettings.glassNoise.get(),
            (Float)hudStyleSettings.glassChromatic.get(),
            currentRenderStyle.borderWidth(),
            currentRenderStyle.borderColor(),
            (Integer)hudStyleSettings.secondaryBorderColor.get(),
            (Integer)hudStyleSettings.glassHighlightColor.get(),
            (Float)hudStyleSettings.edgeSoftness.get() * (Float)hudStyleSettings.scale.get()
         );
      } else {
         int inputValue = targetValue != 0 ? HudStyleSettings.applyOpacity(sourceValue, (Float)hudStyleSettings.panelOpacity.get()) : 0;
         int outputValue = targetValue != 0 ? HudStyleSettings.applyOpacity((Integer)hudStyleSettings.gradientEnd.get(), (Float)hudStyleSettings.panelOpacity.get()) : 0;
         compositorPushPresentationScale.addRect(
            value,
            currentValue,
            nextValue,
            previousValue,
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            currentRenderStyle.radius(),
            inputValue,
            currentRenderStyle.blur(),
            currentRenderStyle.shadow(),
            (Integer)hudStyleSettings.shadowColor.get(),
            currentRenderStyle.borderWidth(),
            currentRenderStyle.borderColor(),
            1.0F,
            (Float)hudStyleSettings.edgeSoftness.get() * (Float)hudStyleSettings.scale.get(),
            (Boolean)hudStyleSettings.insetShadow.get(),
            outputValue,
            (Integer)hudStyleSettings.secondaryBorderColor.get()
         );
      }
   }

   public JsonArray toJson() {
      JsonArray jsonArray = new JsonArray();

      for (HudIdService hudId : this.items3) {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("id", hudId.id());
         jsonObject.addProperty("type", hudId.type());
         jsonObject.addProperty("label", hudId.label());
         jsonObject.addProperty("visible", hudId.visible());
         jsonObject.addProperty("x", hudId.x());
         jsonObject.addProperty("y", hudId.y());
         jsonObject.addProperty("width", hudId.width());
         jsonObject.addProperty("height", hudId.height());
         jsonObject.addProperty("customSize", hudId.customSize());
         jsonObject.add("settings", hudId.settings().deepCopy());
         jsonArray.add(jsonObject);
      }

      return jsonArray;
   }

   public void fromJson(JsonArray jsonArray) {
      this.items3.clear();
      int value = 0;
      if (jsonArray != null) {
         for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
               JsonObject jsonObject = jsonElement.getAsJsonObject();
               String currentText = createText(jsonObject, "type", "text");
               HudData hudData = this.createHudData(currentText);
               if (hudData != null) {
                  String nextText = createText(jsonObject, "id", currentText + "-" + this.count2++);
                  HudIdService hudId = new HudIdService(
                     nextText,
                     hudData.id(),
                     createText(jsonObject, "label", hudData.label()),
                     calculateValue2(jsonObject, "x", hudData.defaultX()),
                     calculateValue2(jsonObject, "y", hudData.defaultY())
                  );
                  hudId.visible(checkCondition(jsonObject, "visible", true));
                  hudId.storedBounds(
                     calculateValue2(jsonObject, "width", hudId.width()),
                     calculateValue2(jsonObject, "height", hudId.height()),
                     checkCondition(jsonObject, "customSize", false)
                  );
                  if (jsonObject.has("settings") && jsonObject.get("settings").isJsonObject()) {
                     hudId.settings(jsonObject.getAsJsonObject("settings"));
                  } else {
                     this.updateState9(hudId);
                  }

                  this.items3.add(hudId);
                  value = Math.max(value, calculateValue3(nextText));
               }
            }
         }
      }

      this.count2 = Math.max(this.count2, value + 1);
   }

   private HudData createHudData(String text) {
      for (HudData hudData : items2) {
         if (hudData.id().equals(text)) {
            return hudData;
         }
      }

      return null;
   }

   private void updateState9(HudIdService hudId) {
      switch (hudId.type()) {
         case "coordinates":
            hudId.setString("Format", "Block");
            break;
         case "clock":
            hudId.setString("Format", "24h");
            break;
         case "text":
            hudId.setString("Text", "Text");
      }
   }

   private static String createText(JsonObject jsonObject, String text, String currentText) {
      return jsonObject.has(text) ? jsonObject.get(text).getAsString() : currentText;
   }

   private static float calculateValue2(JsonObject jsonObject, String text, float value) {
      return jsonObject.has(text) ? jsonObject.get(text).getAsFloat() : value;
   }

   private static boolean checkCondition(JsonObject jsonObject, String text, boolean enabled) {
      return jsonObject.has(text) ? jsonObject.get(text).getAsBoolean() : enabled;
   }

   private static int calculateValue3(String text) {
      int value = text.lastIndexOf(45);
      if (value >= 0 && value != text.length() - 1) {
         try {
            return Integer.parseInt(text.substring(value + 1));
         } catch (NumberFormatException numberFormatException) {
            return 0;
         }
      } else {
         return 0;
      }
   }

   private static float calculateValue4(float value, float currentValue, float nextValue) {
      return Math.max(currentValue, Math.min(nextValue, value));
   }
}
