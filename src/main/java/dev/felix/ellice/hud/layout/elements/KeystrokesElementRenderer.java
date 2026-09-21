package dev.felix.ellice.hud.layout.elements;

import com.google.gson.JsonObject;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.theme.ThemeMixService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public final class KeystrokesElementRenderer implements HudElementRenderer {
   private final Map<String, KeystrokesElementRenderer.ElementAnim> text2 = new HashMap<>();

   @Override
   public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutIsContainerService layoutIsContainer, float width, float height, float value, float currentValue, LayoutOperationHandler layoutOperation, float nextValue, float previousValue
   ) {
      float sourceValue = Math.max(6.0F, LayoutStringService.number(layoutIsContainer.props, "size", 16.0F))
         * previousValue;
      float targetValue = Math.max(0.0F, LayoutStringService.number(layoutIsContainer.props, "gap", 3.0F)) * previousValue;
      boolean enabled = LayoutStringService.bool(layoutIsContainer.props, "showMouse", true);
      boolean currentEnabled = LayoutStringService.bool(layoutIsContainer.props, "showSpace", true);
      String text = layoutIsContainer.id == null ? Integer.toHexString(System.identityHashCode(layoutIsContainer)) : layoutIsContainer.id;
      KeystrokesElementRenderer.ElementAnim elementAnim = this.text2.computeIfAbsent(text, item -> new KeystrokesElementRenderer.ElementAnim());
      long longValue = System.nanoTime();
      float inputValue = Math.max(
         0.001F,
         Math.min(0.05F, (float)(longValue - elementAnim.lastNanos) / 1.0E9F)
      );
      elementAnim.lastNanos = longValue;
      Minecraft minecraft = Minecraft.getInstance();
      KeyMapping keyMapping = minecraft.options.keyUp;
      KeyMapping currentKeyMapping = minecraft.options.keyLeft;
      KeyMapping nextKeyMapping = minecraft.options.keyDown;
      KeyMapping previousKeyMapping = minecraft.options.keyRight;
      float outputValue = sourceValue + targetValue;
      this.updateState(
         compositorPushPresentationScale, layoutIsContainer, elementAnim, "up", createText(keyMapping, "W"), keyMapping.isDown(), width + outputValue, height, sourceValue, sourceValue, nextValue, previousValue, inputValue
      );
      this.updateState(
         compositorPushPresentationScale, layoutIsContainer, elementAnim, "left", createText(currentKeyMapping, "A"), currentKeyMapping.isDown(), width, height + outputValue, sourceValue, sourceValue, nextValue, previousValue, inputValue
      );
      this.updateState(
         compositorPushPresentationScale, layoutIsContainer, elementAnim, "down", createText(nextKeyMapping, "S"), nextKeyMapping.isDown(), width + outputValue, height + outputValue, sourceValue, sourceValue, nextValue, previousValue, inputValue
      );
      this.updateState(
         compositorPushPresentationScale,
         layoutIsContainer,
         elementAnim,
         "right",
         createText(previousKeyMapping, "D"),
         previousKeyMapping.isDown(),
         width + outputValue * 2.0F,
         height + outputValue,
         sourceValue,
         sourceValue,
         nextValue,
         previousValue,
         inputValue
      );
      float resultValue = height + outputValue * 2.0F;
      float candidateValue = sourceValue * 3.0F + targetValue * 2.0F;
      if (enabled) {
         float selectedValue = (candidateValue - targetValue) * 0.5F;
         this.updateState(
            compositorPushPresentationScale, layoutIsContainer, elementAnim, "attack", "LMB", minecraft.options.keyAttack.isDown(), width, resultValue, selectedValue, sourceValue, nextValue, previousValue, inputValue
         );
         this.updateState(
            compositorPushPresentationScale, layoutIsContainer, elementAnim, "use", "RMB", minecraft.options.keyUse.isDown(), width + selectedValue + targetValue, resultValue, selectedValue, sourceValue, nextValue, previousValue, inputValue
         );
         resultValue += outputValue;
      }

      if (currentEnabled) {
         float defaultValue = Math.max(5.0F * previousValue, sourceValue * 0.42F);
         this.updateState(compositorPushPresentationScale, layoutIsContainer, elementAnim, "jump", "", minecraft.options.keyJump.isDown(), width, resultValue, candidateValue, defaultValue, nextValue, previousValue, inputValue);
      }

      if (this.text2.size() > 8) {
         this.text2.entrySet().removeIf(entry -> longValue - entry.getValue().lastNanos > 5000000000L);
      }
   }

   private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      LayoutIsContainerService layoutIsContainer,
      KeystrokesElementRenderer.ElementAnim elementAnim,
      String currentText,
      String nextText,
      boolean enabled,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue
   ) {
      KeystrokesElementRenderer.KeyAnim keyAnim = elementAnim.keys.computeIfAbsent(currentText, item -> new KeystrokesElementRenderer.KeyAnim());
      float resultValue = enabled ? 1.0F : 0.0F;

      for (int index = 0; index < 4; index++) {
         float candidateValue = outputValue * 0.25F;
         float selectedValue = (resultValue - keyAnim.value) * 180.0F - keyAnim.velocity * 16.0F;
         keyAnim.velocity += selectedValue * candidateValue;
         keyAnim.value = keyAnim.value + keyAnim.velocity * candidateValue;
      }

      keyAnim.value = Math.max(0.0F, Math.min(1.4F, keyAnim.value));
      if (enabled && !keyAnim.wasDown) {
         keyAnim.ripple = 0.01F;
      }

      keyAnim.wasDown = enabled;
      if (keyAnim.ripple > 0.0F) {
         keyAnim.ripple = keyAnim.ripple + outputValue * 2.4F;
         if (keyAnim.ripple >= 1.0F) {
            keyAnim.ripple = 0.0F;
         }
      }

      JsonObject jsonObject = layoutIsContainer.props;
      int defaultValue = LayoutStringService.color(jsonObject, "idleColor", 1713381424);
      int initialValue = LayoutStringService.color(jsonObject, "pressColor", -530882070);
      int resolvedValue = LayoutStringService.color(jsonObject, "accent", -10262799);
      int computedValue = LayoutStringService.color(jsonObject, "textColor", -218103809);
      float cachedValue = LayoutStringService.number(jsonObject, "cornerRadius", 4.0F) * inputValue;
      float pendingValue = LayoutStringService.number(jsonObject, "blur", 0.0F) * inputValue;
      boolean currentEnabled = LayoutStringService.bool(jsonObject, "liquid", true);
      float activeValue = Math.max(0.0F, Math.min(1.0F, keyAnim.value));
      float fallbackValue = 1.0F + activeValue * 0.07F;
      float primaryValue = previousValue * (fallbackValue - 1.0F);
      float secondaryValue = sourceValue * (fallbackValue - 1.0F);
      currentValue -= primaryValue * 0.5F;
      nextValue -= secondaryValue * 0.5F;
      previousValue += primaryValue;
      sourceValue += secondaryValue;
      int tertiaryValue = LayoutStringService.withOpacity(ThemeMixService.mix(defaultValue, initialValue, activeValue), targetValue);
      compositorPushPresentationScale.roundedRect(
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         cachedValue,
         cachedValue,
         cachedValue,
         cachedValue,
         tertiaryValue,
         pendingValue,
         activeValue * 8.0F * inputValue,
         LayoutStringService.withOpacity(resolvedValue, targetValue * activeValue * 0.75F),
         0.6F * inputValue,
         LayoutStringService.withOpacity(922746879, targetValue),
         targetValue
      );
      if (currentEnabled) {
         int temporaryValue = ThemeMixService.lighten(resolvedValue, 0.18F + activeValue * 0.22F);
         int requestedValue = ThemeMixService.darken(resolvedValue, 0.28F);
         compositorPushPresentationScale.addMesh(
            currentValue,
            nextValue,
            previousValue,
            sourceValue,
            cachedValue,
            requestedValue,
            resolvedValue,
            temporaryValue,
            ThemeMixService.mix(resolvedValue, initialValue, activeValue),
            ThemeMixService.desaturate(resolvedValue, 0.35F),
            targetValue * (0.45F + activeValue * 0.45F)
         );
      }

      if (keyAnim.ripple > 0.0F) {
         float actualValue = keyAnim.ripple * 5.0F * inputValue;
         int expectedValue = LayoutStringService.withOpacity(resolvedValue, targetValue * (1.0F - keyAnim.ripple) * 0.75F);
         compositorPushPresentationScale.roundedRect(
            currentValue - actualValue,
            nextValue - actualValue,
            previousValue + actualValue * 2.0F,
            sourceValue + actualValue * 2.0F,
            cachedValue + actualValue,
            cachedValue + actualValue,
            cachedValue + actualValue,
            cachedValue + actualValue,
            0,
            0.0F,
            0.0F,
            0,
            Math.max(0.7F, 1.1F * inputValue),
            expectedValue,
            targetValue
         );
      }

      if (!nextText.isEmpty()) {
         float minimumValue = Math.min(
            sourceValue * 0.42F, LayoutStringService.number(jsonObject, "fontSize", 7.5F) * inputValue
         );
         String previousText = createText2(layoutIsContainer);
         TextData textData = TextData.NONE.withVariant(TextMode.BOLD).withFontFamily(previousText);
         float maximumValue = compositorPushPresentationScale.textWidth(nextText, minimumValue, TextMode.BOLD, previousText);
         compositorPushPresentationScale.text(
            currentValue + (previousValue - maximumValue) * 0.5F,
            nextValue + (sourceValue - minimumValue) * 0.47F,
            nextText,
            minimumValue,
            LayoutStringService.withOpacity(computedValue, targetValue),
            textData
         );
      }
   }

   @Override
   public HudElementRenderer.Size measure(LayoutIsContainerService layoutIsContainer, LayoutOperationHandler layoutOperation, float value, float currentValue) {
      float nextValue = Math.max(6.0F, LayoutStringService.number(layoutIsContainer.props, "size", 16.0F));
      float previousValue = Math.max(0.0F, LayoutStringService.number(layoutIsContainer.props, "gap", 3.0F));
      boolean enabled = LayoutStringService.bool(layoutIsContainer.props, "showMouse", true);
      boolean currentEnabled = LayoutStringService.bool(layoutIsContainer.props, "showSpace", true);
      float sourceValue = nextValue * 2.0F + previousValue;
      if (enabled) {
         sourceValue += previousValue + nextValue;
      }

      if (currentEnabled) {
         sourceValue += previousValue + Math.max(5.0F, nextValue * 0.42F);
      }

      return new HudElementRenderer.Size(nextValue * 3.0F + previousValue * 2.0F, sourceValue);
   }

   private static String createText(KeyMapping keyMapping, String text) {
      try {
         String currentText = keyMapping.getTranslatedKeyMessage().getString();
         if (currentText != null && !currentText.isBlank() && currentText.length() <= 4) {
            return currentText.toUpperCase();
         }
      } catch (Throwable exception) {
      }

      return text;
   }

   private static String createText2(LayoutIsContainerService layoutIsContainer) {
      String text = LayoutStringService.string(layoutIsContainer.props, "font", "");
      return text != null && !text.isBlank() && !"client".equalsIgnoreCase(text) ? text.toLowerCase(Locale.ROOT) : null;
   }

   private static final class ElementAnim {
      final Map<String, KeystrokesElementRenderer.KeyAnim> keys = new HashMap<>();
      long lastNanos = System.nanoTime();
   }

   private static final class KeyAnim {
      float value;
      float velocity;
      boolean wasDown;
      float ripple;
   }
}
