package dev.felix.ellice.hud;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.svg.SvgQueueService;
import java.util.List;

final class HudComponent extends LayoutContainerNode {
   private List<HudMeshService.Row> items = List.of();
   private HudMeshService.Mesh mesh;
   private float[] float2 = new float[0];
   private float value = 1.0F;
   private float value2;
   private boolean enabled = true;

   void appearance(float currentValue, boolean currentEnabled) {
      this.value = currentValue;
      this.enabled = currentEnabled;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      List currentItems = this.children().stream().map(item -> new HudMeshService.Row(item.computedW(), item.computedH())).toList();
      if (this.mesh == null || !currentItems.equals(this.items) || this.value2 != this.value) {
         this.mesh = HudMeshService.build(
            currentItems, 5.0F * this.value, 0.8F * this.value
         );
         this.float2 = createFloat(this.mesh.fill(), this.value);
         this.items = currentItems;
         this.value2 = this.value;
      }

      if (!(this.mesh.height() <= 0.001F)) {
         this.effectiveOpacity = this.effectiveOpacity
            * ActiveModuleSlot.smooth(
               this.mesh.height() / Math.max(0.001F, 12.0F * this.value)
            );
         float currentWidth = this.cx + 8.0F * this.presentationScale;
         float currentHeight = this.cy + 8.0F * this.presentationScale;
         if (compositorPushPresentationScale.vectorRenderer() == null) {
            compositorPushPresentationScale.roundedRect(
               currentWidth,
               currentHeight,
               this.mesh.width() * this.presentationScale,
               this.mesh.height() * this.presentationScale,
               5.0F * this.value,
               this.enabled ? MaterialIsLightService.SURFACE_CONTAINER : 0
            );
         } else {
            if (this.enabled) {
               this.updateState(compositorPushPresentationScale, this.float2, currentWidth, currentHeight, 1073741824, false, true);
               this.updateState(compositorPushPresentationScale, this.mesh.fill(), currentWidth, currentHeight, MaterialIsLightService.SURFACE_CONTAINER, false, false);
               this.updateState(compositorPushPresentationScale, this.mesh.edge(), currentWidth, currentHeight, MaterialIsLightService.SURFACE_CONTAINER, true, false);
               compositorPushPresentationScale.nextLayer();
            }
         }
      }
   }

   private static float[] createFloat(float[] floats, float value) {
      int[] ints = new int[]{1, 6, 15, 20, 15, 6, 1};
      float[] currentLength = new float[floats.length * 49];
      int index = 0;

      for (int currentIndex = 0; currentIndex < 7; currentIndex++) {
         for (int nextIndex = 0; nextIndex < 7; nextIndex++) {
            float currentValue = (nextIndex - 3) * 3.5F * value;
            float nextValue = (currentIndex - 2) * 3.5F * value;
            float previousValue = ints[nextIndex] * ints[currentIndex] / 4096.0F;

            for (byte previousIndex = 0; previousIndex < floats.length; previousIndex += 3) {
               currentLength[index++] = floats[previousIndex] + currentValue;
               currentLength[index++] = floats[previousIndex + 1] + nextValue;
               currentLength[index++] = previousValue;
            }
         }
      }

      return currentLength;
   }

   private void updateState(CompositorPushPresentationScaleService compositorPushPresentationScale, float[] floats, float value, float currentValue, int nextValue, boolean enabled, boolean currentEnabled) {
      if (floats.length != 0) {
         float previousValue = (nextValue >>> 24) / 255.0F * this.effectiveOpacity;
         compositorPushPresentationScale.vectorRenderer()
            .queue(
               new SvgQueueService.VectorCmd(
                  floats,
                  floats.length / 3,
                  value,
                  currentValue,
                  this.presentationScale,
                  this.presentationScale,
                  0.0F,
                  0.0F,
                  0.0F,
                  (nextValue >>> 16 & 0xFF) / 255.0F * previousValue,
                  (nextValue >>> 8 & 0xFF) / 255.0F * previousValue,
                  (nextValue & 0xFF) / 255.0F * previousValue,
                  previousValue,
                  0.0F,
                  0.0F,
                  0.0F,
                  compositorPushPresentationScale.currentClip(),
                  enabled,
                  currentEnabled
               )
            );
      }
   }
}

