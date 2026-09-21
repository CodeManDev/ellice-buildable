package dev.felix.ellice.hud.layout;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.hud.layout.elements.ElementsRenderer;
import dev.felix.ellice.hud.layout.elements.ProgressBarElementRenderer;
import dev.felix.ellice.hud.layout.elements.TextElementRenderer;
import dev.felix.ellice.hud.layout.elements.KeystrokesElementRenderer;
import dev.felix.ellice.hud.layout.elements.PanelElementRenderer;
import dev.felix.ellice.hud.layout.elements.ArmorElementRenderer;
import dev.felix.ellice.hud.layout.elements.FpsElementRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LayoutRenderer {
   private final Map<String, HudElementRenderer> text2 = new LinkedHashMap<>();
   private static final LayoutOperationHandler layoutOperationHandler = new LayoutOperationHandler() {
      @Override
      public String string(String text) {
         return "";
      }

      @Override
      public float number(String text) {
         return 0.0F;
      }

      @Override
      public float max(String text) {
         return 1.0F;
      }
   };

   public LayoutRenderer() {
      this.register("text", new TextElementRenderer());
      this.register("rect", new PanelElementRenderer());
      this.register("fps", new FpsElementRenderer());
      this.register("coords", new ElementsRenderer());
      this.register("bar", new ProgressBarElementRenderer());
      this.register("armorhud", new ArmorElementRenderer());
      this.register("keystrokes", new KeystrokesElementRenderer());
   }

   public void register(String text, HudElementRenderer hudElementRenderer) {
      if (text != null && !text.isEmpty() && hudElementRenderer != null) {
         this.text2.put(text, hudElementRenderer);
      }
   }

   public HudElementRenderer renderer(String text) {
      return this.text2.get(text);
   }

   public Set<String> registeredTypes() {
      return Collections.unmodifiableSet(this.text2.keySet());
   }

   public void render(CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutFindSelector layoutFindSelector, int width, int height, LayoutOperationHandler layoutOperation) {
      this.render(compositorPushPresentationScale, layoutFindSelector, 0.0F, 0.0F, width, height, 1.0F, layoutOperation);
   }

   public void render(CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutFindSelector layoutFindSelector, float width, float height, float value, float currentValue, float nextValue, LayoutOperationHandler layoutOperation) {
      if (layoutFindSelector != null && !layoutFindSelector.elements.isEmpty()) {
         if (layoutOperation == null) {
            layoutOperation = layoutOperationHandler;
         }

         this.updateState(compositorPushPresentationScale, layoutFindSelector.elements, width, height, value, currentValue, nextValue, layoutOperation, 1.0F);
      }
   }

   public LayoutRenderer.Rect logicalRect(LayoutIsContainerService layoutIsContainer, float y, float currentWidth, LayoutOperationHandler layoutOperation) {
      if (layoutOperation == null) {
         layoutOperation = layoutOperationHandler;
      }

      HudElementRenderer hudElementRenderer = this.text2.get(layoutIsContainer.type);
      CompositorPushPresentationScaleService compositorPushPresentationScale = CoreIsInitializedHandler.isReady() ? CoreIsInitializedHandler.get().compositor() : null;
      HudElementRenderer.Size size = hudElementRenderer != null
         ? hudElementRenderer.measure(layoutIsContainer, layoutOperation, y, currentWidth, compositorPushPresentationScale)
         : new HudElementRenderer.Size(
            layoutIsContainer.width > 0.0F ? layoutIsContainer.width : 80.0F,
            layoutIsContainer.height > 0.0F ? layoutIsContainer.height : 14.0F
         );
      float value = size.w();
      float currentValue = size.h();
      float nextValue = y * layoutIsContainer.anchor.h.factor + layoutIsContainer.offsetX - value * layoutIsContainer.anchor.h.factor;
      float previousValue = currentWidth * layoutIsContainer.anchor.v.factor + layoutIsContainer.offsetY - currentValue * layoutIsContainer.anchor.v.factor;
      return new LayoutRenderer.Rect(nextValue, previousValue, value, currentValue);
   }

   public LayoutRenderer.Rect screenRect(LayoutIsContainerService layoutIsContainer, float currentY, float width, float height, float value, float currentValue, LayoutOperationHandler layoutOperation) {
      LayoutRenderer.Rect rect = this.logicalRect(layoutIsContainer, height, value, layoutOperation);
      return new LayoutRenderer.Rect(currentY + rect.x() * currentValue, width + rect.y() * currentValue, rect.w() * currentValue, rect.h() * currentValue);
   }

   private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale, List<LayoutIsContainerService> items, float value, float currentValue, float nextValue, float previousValue, float sourceValue, LayoutOperationHandler layoutOperation, float targetValue
   ) {
      for (LayoutIsContainerService layoutIsContainer : items) {
         if (layoutIsContainer.visible) {
            float inputValue = targetValue * Math.max(0.0F, Math.min(1.0F, layoutIsContainer.opacity));
            if (!(inputValue < 0.003F)) {
               HudElementRenderer hudElementRenderer = this.text2.get(layoutIsContainer.type);
               HudElementRenderer.Size size;
               if (hudElementRenderer != null) {
                  size = hudElementRenderer.measure(layoutIsContainer, layoutOperation, nextValue, previousValue, compositorPushPresentationScale);
               } else {
                  size = new HudElementRenderer.Size(
                     layoutIsContainer.width > 0.0F ? layoutIsContainer.width : 80.0F,
                     layoutIsContainer.height > 0.0F ? layoutIsContainer.height : 14.0F
                  );
               }

               float outputValue = size.w();
               float resultValue = size.h();
               float candidateValue = nextValue * layoutIsContainer.anchor.h.factor;
               float selectedValue = previousValue * layoutIsContainer.anchor.v.factor;
               float defaultValue = candidateValue + layoutIsContainer.offsetX - outputValue * layoutIsContainer.anchor.h.factor;
               float initialValue = selectedValue + layoutIsContainer.offsetY - resultValue * layoutIsContainer.anchor.v.factor;
               float resolvedValue = value + defaultValue * sourceValue;
               float computedValue = currentValue + initialValue * sourceValue;
               float cachedValue = outputValue * sourceValue;
               float pendingValue = resultValue * sourceValue;
               if (hudElementRenderer != null) {
                  try {
                     hudElementRenderer.draw(compositorPushPresentationScale, layoutIsContainer, resolvedValue, computedValue, cachedValue, pendingValue, layoutOperation, inputValue, sourceValue);
                  } catch (Exception exception) {
                     CoreIsInitializedHandler.LOGGER.warn("HUD element '{}' (type={}) failed to draw", new Object[]{layoutIsContainer.id, layoutIsContainer.type, exception});
                  }
               } else {
                  updateState2(compositorPushPresentationScale, layoutIsContainer, resolvedValue, computedValue, cachedValue, pendingValue, inputValue);
               }

               if (layoutIsContainer.isContainer()) {
                  this.updateState(compositorPushPresentationScale, layoutIsContainer.children, resolvedValue, computedValue, outputValue, resultValue, sourceValue, layoutOperation, inputValue);
               }
            }
         }
      }
   }

   private static void updateState2(CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutIsContainerService layoutIsContainer, float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      int targetValue = Math.max(40, (int)(sourceValue * 128.0F));
      int inputValue = targetValue << 24 | 4202528;
      int outputValue = targetValue << 24 | 15680580;
      compositorPushPresentationScale.roundedRect(
         value,
         currentValue,
         nextValue,
         previousValue,
         3.0F,
         3.0F,
         3.0F,
         3.0F,
         inputValue,
         0.0F,
         0.0F,
         0,
         0.6F,
         outputValue,
         1.0F,
         0.0F
      );
      compositorPushPresentationScale.text(
         value + 4.0F,
         currentValue + 3.0F,
         "?" + (layoutIsContainer.type != null ? " " + layoutIsContainer.type : ""),
         6.5F,
         targetValue << 24 | 16772846
      );
   }

   public record Rect(float x, float y, float w, float h) {
   }
}
