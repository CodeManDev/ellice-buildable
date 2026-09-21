package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;

public final class ExpandableMaterialPanel extends LayoutContainerNode {
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("materialRevealHeight", item -> item instanceof ExpandableMaterialPanel);
   private static final SceneEaseHandler.Spring spring = new SceneEaseHandler.Spring(
      39.0F, 520.0F
   );
   private static final SceneEaseHandler.Spring spring2 = new SceneEaseHandler.Spring(
      48.0F, 640.0F
   );
   private boolean enabled = true;
   private boolean enabled2;
   private boolean enabled3;
   private float value;
   private float value2;
   private float value3;
   private float value4;

   public ExpandableMaterialPanel() {
      this.direction(ScenePctService.Direction.COLUMN);
      this.clip(true);
      this.flexShrink(0.0F);
      this.onLayout(this::updateState);
   }

   public ExpandableMaterialPanel expanded(boolean currentEnabled) {
      if (this.enabled == currentEnabled) {
         return this;
      }

      this.enabled = currentEnabled;
      this.pointerEvents(currentEnabled);
      if (!this.enabled2) {
         if (!currentEnabled) {
            this.height(LayoutOperationHandler.px(0.0F));
         }

         return this;
      } else {
         if (!this.enabled3) {
            this.value = this.computedH();
            this.enabled3 = true;
            this.height(LayoutOperationHandler.px(this.value));
         }

         this.value3 = currentEnabled ? this.value2 : 0.0F;
         MotionAnimateService.animate(this, motionFiniteService, this.value3, currentEnabled ? spring : spring2);
         return this;
      }
   }

   private void updateState() {
      if (!this.children().isEmpty()) {
         float currentValue = this.children().getFirst().computedH();
         int nextValue = this.enabled2 && Math.abs(this.value4 - this.computedW()) > 0.5F ? 1 : 0;
         this.value4 = this.computedW();
         if (!this.enabled2) {
            this.enabled2 = true;
            this.value2 = currentValue;
            this.value3 = this.enabled ? currentValue : 0.0F;
            this.value = this.value3;
         } else {
            if (nextValue != 0) {
               this.value2 = currentValue;
               this.value3 = this.enabled ? currentValue : 0.0F;
               MotionAnimateService.cancel(this, motionFiniteService);
               motionFiniteService.set(this, this.value3);
               this.enabled3 = true;
            } else if (Math.abs(currentValue - this.value2) > 0.5F) {
               if (!this.enabled3) {
                  this.value = this.value2;
                  this.enabled3 = true;
                  this.height(LayoutOperationHandler.px(this.value));
               }

               this.value2 = currentValue;
               this.value3 = this.enabled ? currentValue : 0.0F;
               MotionAnimateService.animate(this, motionFiniteService, this.value3, this.enabled ? spring : spring2);
            }
         }
      }
   }

   @Override
   public float getAnimProperty(String text) {
      return "materialRevealHeight".equals(text) ? this.value : super.getAnimProperty(text);
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      if ("materialRevealHeight".equals(text)) {
         this.value = currentValue;
         this.height(LayoutOperationHandler.px(Math.max(0.0F, currentValue)));
      } else {
         super.setAnimProperty(text, currentValue);
      }
   }
}

