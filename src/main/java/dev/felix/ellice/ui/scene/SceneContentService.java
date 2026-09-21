package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.List;
import java.util.Objects;

public final class SceneContentService extends ScenePctService<SceneContentService> {
   private static final float value = 0.001F;
   private static final float value2 = -5.0F;
   private static final SceneEaseHandler.Tween tween = new SceneEaseHandler.Tween(
      0.26F, SceneEaseHandler.Easing.EXPRESSIVE_OUT
   );
   private static final SceneEaseHandler.Tween tween2 = new SceneEaseHandler.Tween(
      0.2F, SceneEaseHandler.Easing.EASE_IN_OUT
   );
   private static final SceneEaseHandler.Tween tween3 = new SceneEaseHandler.Tween(
      0.22F, SceneEaseHandler.Easing.EXPRESSIVE_OUT
   );
   private ScenePctService<?> scenePctService;
   private Runnable runnable;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private float value3;
   private long timestamp;

   public SceneContentService() {
      this.direction(ScenePctService.Direction.COLUMN);
      this.clip(true);
      this.flexShrink(0.0F);
      this.size(-1.0F, 0.0F);
      super.onLayout(this::updateState4);
   }

   public SceneContentService(ScenePctService<?> scenePct) {
      this();
      this.content(scenePct);
   }

   public ScenePctService<?> content() {
      return this.scenePctService;
   }

   public SceneContentService content(ScenePctService<?> scenePct) {
      return this.scenePctService == scenePct ? this : this.reconcileChildren(scenePct == null ? List.of() : List.of(scenePct));
   }

   public boolean isExpanded() {
      return this.enabled;
   }

   public SceneContentService expand() {
      return this.setExpanded(true, true);
   }

   public SceneContentService collapse() {
      return this.setExpanded(false, true);
   }

   public SceneContentService toggle() {
      return this.setExpanded(!this.enabled, true);
   }

   public SceneContentService expanded(boolean enabled) {
      return this.setExpanded(enabled, true);
   }

   public SceneContentService setExpanded(boolean currentEnabled, boolean nextEnabled) {
      if (this.enabled == currentEnabled) {
         if (!nextEnabled) {
            this.updateState9();
         }

         return this;
      } else {
         this.enabled = currentEnabled;
         long longValue = ++this.timestamp;
         this.enabled3 = false;
         if (!currentEnabled) {
            this.enabled4 = false;
            if (this.scenePctService != null) {
               this.scenePctService.pointerEvents(false);
            }

            if (!nextEnabled) {
               this.updateState11();
               return this;
            }

            this.animate("height", 0.0F, tween2, 0.0F, 0, false, () -> this.updateState8(longValue));
            if (this.scenePctService != null) {
               this.scenePctService
                  .animate("opacity", 0.0F, tween2)
                  .animate("translateY", -5.0F, tween2);
            }

            return this;
         } else {
            int value = this.scenePctService != null && !this.scenePctService.visible ? 1 : 0;
            if (this.scenePctService != null) {
               this.scenePctService.visible(true).pointerEvents(true);
               if (value != 0) {
                  this.scenePctService
                     .cancelAnimation("opacity")
                     .cancelAnimation("translateY")
                     .opacity(0.0F)
                     .translateY(-5.0F);
               }
            }

            if (!nextEnabled) {
               this.enabled4 = false;
               this.enabled3 = !this.enabled2;
               this.setAnimProperty("height", this.enabled2 ? this.value3 : 0.0F);
               this.updateState10();
               return this;
            }

            this.enabled4 = true;
            this.updateState6(this.enabled2 ? this.value3 : 0.0F, tween, longValue);
            if (this.scenePctService != null) {
               this.scenePctService.animate("opacity", 1.0F, tween).animate("translateY", 0.0F, tween);
            }

            return this;
         }
      }
   }

   public SceneContentService addChild(ScenePctService<?> scenePct) {
      Objects.requireNonNull(scenePct, "child");
      if (this.scenePctService == scenePct) {
         this.updateState(scenePct);
         return this;
      }

      if (this.scenePctService != null && this.scenePctService != scenePct) {
         throw new IllegalStateException("AccordionNode accepts exactly one content child");
      }

      super.addChild(scenePct);
      this.scenePctService = scenePct;
      this.updateState(scenePct);
      this.updateState2();
      this.updateState3();
      return this;
   }

   public SceneContentService removeChild(ScenePctService<?> scenePct) {
      int value = this.scenePctService == scenePct ? 1 : 0;
      super.removeChild(scenePct);
      if (value != 0) {
         this.scenePctService = null;
         this.updateState3();
      }

      return this;
   }

   public SceneContentService clearChildren() {
      int value = this.scenePctService != null ? 1 : 0;
      super.clearChildren();
      this.scenePctService = null;
      if (value != 0) {
         this.updateState3();
      }

      return this;
   }

   public SceneContentService reconcileChildren(List<ScenePctService<?>> items) {
      Objects.requireNonNull(items, "ordered");
      if (items.size() > 1) {
         throw new IllegalArgumentException("AccordionNode accepts exactly one content child");
      }

      ScenePctService scenePct = this.scenePctService;
      super.reconcileChildren(items);
      this.scenePctService = items.isEmpty() ? null : (ScenePctService)items.get(0);
      if (this.scenePctService != null) {
         this.updateState(this.scenePctService);
      }

      if (scenePct != this.scenePctService) {
         this.updateState2();
         this.updateState3();
      }

      return this;
   }

   public SceneContentService onLayout(Runnable currentRunnable) {
      this.runnable = currentRunnable;
      super.onLayout(this::updateState4);
      return this;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
   }

   private void updateState(ScenePctService<?> scenePct) {
      scenePct.position(0.0F, 0.0F)
         .absolute()
         .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
         .inset(LayoutOperationHandler.px(0.0F), LayoutOperationHandler.px(0.0F), LayoutOperationHandler.auto(), LayoutOperationHandler.px(0.0F))
         .flexShrink(0.0F);
   }

   private void updateState2() {
      if (this.scenePctService != null) {
         if (this.enabled) {
            this.scenePctService.visible(true).pointerEvents(true).opacity(1.0F).translateY(0.0F);
         } else {
            this.scenePctService
               .cancelAnimation("opacity")
               .cancelAnimation("translateY")
               .opacity(0.0F)
               .translateY(-5.0F)
               .pointerEvents(false)
               .visible(false);
         }
      }
   }

   private void updateState3() {
      this.timestamp++;
      this.enabled2 = false;
      this.value3 = 0.0F;
      if (!this.enabled) {
         this.setAnimProperty("height", 0.0F);
      } else {
         this.setAnimProperty("height", 0.0F);
         this.enabled3 = false;
      }
   }

   private void updateState4() {
      this.updateState5();
      if (this.runnable != null) {
         this.runnable.run();
      }
   }

   private void updateState5() {
      if (this.scenePctService != null && this.scenePctService.visible) {
         float value = this.paddingTop
            + this.scenePctService.marginTop
            + Math.max(0.0F, this.scenePctService.computedH())
            + this.scenePctService.marginBottom
            + this.paddingBottom;
         if (!Float.isFinite(value)) {
            value = 0.0F;
         }

         value = Math.max(0.0F, value);
         int currentValue = this.enabled2 && !(Math.abs(value - this.value3) > 0.001F) ? 0 : 1;
         this.value3 = value;
         this.enabled2 = true;
         if (currentValue != 0 && this.enabled) {
            if (this.enabled3) {
               this.enabled3 = false;
               this.enabled4 = false;
               this.cancelAnimation("height");
               this.setAnimProperty("height", this.value3);
               this.updateState10();
            } else {
               this.updateState6(this.value3, this.enabled4 ? tween : tween3, this.timestamp);
            }
         }
      }
   }

   private void updateState6(float value, SceneEaseHandler sceneEase, long offset) {
      this.animate("height", Math.max(0.0F, value), sceneEase, 0.0F, 0, false, () -> this.updateState7(offset, value));
   }

   private void updateState7(long offset, float value) {
      if (offset == this.timestamp && this.enabled) {
         if (this.enabled2 && Math.abs(this.value3 - value) > 0.001F) {
            this.updateState6(this.value3, tween3, offset);
         } else {
            this.setAnimProperty("height", this.enabled2 ? this.value3 : Math.max(0.0F, value));
            this.enabled4 = false;
            this.updateState10();
         }
      }
   }

   private void updateState8(long offset) {
      if (offset == this.timestamp && !this.enabled) {
         this.updateState11();
      }
   }

   private void updateState9() {
      this.timestamp++;
      this.cancelAnimation("height");
      if (this.enabled) {
         this.enabled4 = false;
         this.enabled3 = !this.enabled2;
         this.setAnimProperty("height", this.enabled2 ? this.value3 : 0.0F);
         this.updateState10();
      } else {
         this.updateState11();
      }
   }

   private void updateState10() {
      if (this.scenePctService != null) {
         this.scenePctService
            .cancelAnimation("opacity")
            .cancelAnimation("translateY")
            .visible(true)
            .pointerEvents(true)
            .opacity(1.0F)
            .translateY(0.0F);
      }
   }

   private void updateState11() {
      this.cancelAnimation("height");
      this.setAnimProperty("height", 0.0F);
      this.enabled3 = false;
      this.enabled4 = false;
      if (this.scenePctService != null) {
         this.scenePctService
            .cancelAnimation("opacity")
            .cancelAnimation("translateY")
            .opacity(0.0F)
            .translateY(-5.0F)
            .pointerEvents(false)
            .visible(false);
      }
   }
}

