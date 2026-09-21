package dev.felix.ellice.diagnostics;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.render3d.RenderDiagnosticsSnapshot;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;
import java.util.Locale;
import java.util.function.Supplier;

public final class DiagnosticsRecorderService {
   private static final float value = 360.0F;
   private static final float value2 = 640.0F;
   private static final long timestamp = 200000000L;
   private final SceneDtService sceneDtService;
   private final DiagnosticsAddFrameListenerService fpky0sollh;
   private final Supplier<RenderDiagnosticsSnapshot> renderer;
   private final DiagnosticsRecorderService.OverlayPanel overlayPanel;
   private boolean enabled;
   private boolean enabled2;
   private DiagnosticsIsRecordingService diagnosticsIsRecordingService;

   public DiagnosticsRecorderService(SceneDtService sceneDt, DiagnosticsAddFrameListenerService diagnosticsAddFrameListener) {
      this(sceneDt, diagnosticsAddFrameListener, () -> RenderDiagnosticsSnapshot.IDLE);
   }

   public DiagnosticsRecorderService(SceneDtService sceneDt, DiagnosticsAddFrameListenerService diagnosticsAddFrameListener, Supplier<RenderDiagnosticsSnapshot> supplier) {
      this.sceneDtService = sceneDt;
      this.fpky0sollh = diagnosticsAddFrameListener;
      this.renderer = supplier;
      this.overlayPanel = new DiagnosticsRecorderService.OverlayPanel();
      this.overlayPanel
         .id("perf.panel")
         .size(LayoutOperationHandler.px(360.0F), LayoutOperationHandler.percent(100.0F))
         .maxWidth(LayoutOperationHandler.percent(100.0F))
         .maxHeight(640.0F)
         .minWidth(0.0F)
         .minHeight(0.0F)
         .cornerRadius(24.0F)
         .direction(ScenePctService.Direction.NONE)
         .clip(true)
         .pointerEvents(false)
         .layerBreak(true)
         .visible(false);
   }

   public void recorder(DiagnosticsIsRecordingService diagnosticsIsRecording) {
      this.diagnosticsIsRecordingService = diagnosticsIsRecording;
   }

   public void show() {
      this.updateState(true);
   }

   public void hide() {
      this.updateState(false);
   }

   public void toggle() {
      this.updateState(!this.enabled2);
   }

   public boolean isVisible() {
      return this.enabled2;
   }

   private void updateState(boolean currentEnabled) {
      this.enabled2 = currentEnabled;
      this.overlayPanel.visible(currentEnabled);
      if (currentEnabled) {
         this.overlayPanel.timestamp2 = 0L;
      }

      if (currentEnabled && !this.enabled) {
         LayoutContainerNode currentSize = new LayoutContainerNode()
            .id("perf.anchor")
            .absolute()
            .inset(LayoutOperationHandler.px(0.0F))
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
            .direction(ScenePctService.Direction.ROW)
            .justify(ScenePctService.Justify.END)
            .align(ScenePctService.Align.START)
            .padding(12.0F)
            .pointerEvents(false);
         currentSize.addChild(this.overlayPanel);
         this.sceneDtService.root().addChild(currentSize);
         this.enabled = true;
      }
   }

   private static String createText(String text, Object... objects) {
      return String.format(Locale.ROOT, text, objects);
   }

   private final class OverlayPanel extends SceneCornerRadiusService {
      private long timestamp2;
      private DiagnosticsData diagnosticsData = DiagnosticsData.capture(DiagnosticsRecorderService.this.fpky0sollh);
      private RenderDiagnosticsSnapshot renderer2 = RenderDiagnosticsSnapshot.IDLE;
      private long timestamp3;
      private long timestamp4;

      @Override
      protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
         compositorPushPresentationScale.requestFontFamily("material-roboto", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Regular.ttf");
         compositorPushPresentationScale.requestFontFamily(
            "material-roboto-medium", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Medium.ttf"
         );
         long longValue = System.nanoTime();
         if (this.timestamp2 == 0L || longValue - this.timestamp2 >= 200000000L) {
            this.diagnosticsData = DiagnosticsData.capture(DiagnosticsRecorderService.this.fpky0sollh);
            this.renderer2 = DiagnosticsRecorderService.this.renderer.get();
            if (this.renderer2 == null) {
               this.renderer2 = RenderDiagnosticsSnapshot.IDLE;
            }

            Runtime runtime = Runtime.getRuntime();
            this.timestamp3 = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
            this.timestamp4 = runtime.maxMemory() / 1048576L;
            this.timestamp2 = longValue;
         }

         this.backgroundColor(MaterialIsLightService.SURFACE);
         super.draw(compositorPushPresentationScale);
         if (!(this.cw < 150.0F) && !(this.ch < 150.0F)) {
            float height = this.cx + 16.0F;
            float value = this.cw - 32.0F;
            float currentValue = this.cy + 14.0F;
            this.updateState7(compositorPushPresentationScale, height, currentValue, "Performance", 16.0F, MaterialIsLightService.ON_SURFACE, true);
            int nextValue = DiagnosticsRecorderService.this.diagnosticsIsRecordingService != null && DiagnosticsRecorderService.this.diagnosticsIsRecordingService.isRecording() ? 1 : 0;
            this.updateState6(
               compositorPushPresentationScale,
               height + value,
               currentValue + 2.0F,
               nextValue != 0
                  ? "REC " + DiagnosticsRecorderService.this.diagnosticsIsRecordingService.framesCaptured() + "/" + DiagnosticsRecorderService.this.diagnosticsIsRecordingService.framesTarget()
                  : "LIVE  /  F4",
               10.0F,
               nextValue != 0 ? MaterialIsLightService.ERROR : MaterialIsLightService.PRIMARY
            );
            float previousValue = value / 3.0F;
            this.updateState5(
               compositorPushPresentationScale,
               height,
               currentValue + 28.0F,
               previousValue,
               this.diagnosticsData.samples() == 0 ? "--" : DiagnosticsRecorderService.createText("%.0f", this.diagnosticsData.fps()),
               "FPS",
               MaterialIsLightService.PRIMARY
            );
            this.updateState5(
               compositorPushPresentationScale,
               height + previousValue,
               currentValue + 28.0F,
               previousValue,
               DiagnosticsRecorderService.createText("%.1f", this.diagnosticsData.averageMs()),
               "Frame / ms",
               MaterialIsLightService.ON_SURFACE
            );
            this.updateState5(
               compositorPushPresentationScale,
               height + previousValue * 2.0F,
               currentValue + 28.0F,
               previousValue,
               DiagnosticsRecorderService.createText("%.1f", this.diagnosticsData.p95Ms()),
               "P95 / ms",
               this.diagnosticsData.p95Ms() > 16.67 ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE
            );
            float sourceValue = currentValue + 88.0F;
            float targetValue = this.ch < 360.0F ? 52.0F : 96.0F;
            this.updateState7(
               compositorPushPresentationScale,
               height,
               sourceValue - 10.0F,
               "FRAME TIME",
               10.0F,
               MaterialIsLightService.ON_SURFACE_VARIANT,
               true
            );
            this.updateState6(
               compositorPushPresentationScale,
               height + value,
               sourceValue - 10.0F,
               DiagnosticsRecorderService.createText("peak %.1f ms", this.diagnosticsData.peakMs()),
               10.0F,
               MaterialIsLightService.ON_SURFACE_VARIANT
            );
            this.updateState2(compositorPushPresentationScale, height, sourceValue + 10.0F, value, targetValue);
            float inputValue = sourceValue + targetValue + 28.0F;
            if (this.ch < 360.0F) {
               if (inputValue + 15.0F < this.cy + this.ch - 8.0F) {
                  this.updateState7(
                     compositorPushPresentationScale,
                     height,
                     inputValue,
                     "ShaderESP  " + this.renderer2.status(),
                     11.0F,
                     MaterialIsLightService.PRIMARY,
                     true
                  );
                  if (inputValue + 32.0F < this.cy + this.ch - 8.0F) {
                     this.updateState7(
                        compositorPushPresentationScale,
                        height,
                        inputValue + 16.0F,
                        DiagnosticsRecorderService.createText(
                           "%.2f ms CPU  /  %d captured", this.diagnosticsData.section("shaderesp"), this.renderer2.captured()
                        ),
                        10.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT,
                        false
                     );
                  }
               }
            } else {
               this.updateState7(
                  compositorPushPresentationScale,
                  height,
                  inputValue - 4.0F,
                  this.diagnosticsData.samples()
                     + " frames / "
                     + DiagnosticsRecorderService.createText("%.1f s", this.diagnosticsData.seconds())
                     + " / peaks per bucket",
                  10.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               );
               inputValue += 18.0F;
               this.updateState3(compositorPushPresentationScale, height, inputValue, value);
               inputValue += 136.0F;
               if (inputValue + 142.0F < this.cy + this.ch - 12.0F) {
                  this.updateState7(
                     compositorPushPresentationScale, height, inputValue, "CPU FRAME BREAKDOWN", 10.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true
                  );
                  inputValue += 22.0F;

                  for (int index = 0; index < DiagnosticsAddFrameListenerService.SECTIONS.length; index++) {
                     String text = DiagnosticsAddFrameListenerService.SECTIONS[index];
                     String currentText = "other".equals(text) ? "MC / wait / other" : text;
                     this.updateState4(
                        compositorPushPresentationScale,
                        height,
                        inputValue,
                        value,
                        currentText,
                        this.diagnosticsData.section(text),
                        index < 2 ? MaterialIsLightService.TERTIARY : (index == 4 ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY)
                     );
                     inputValue += 18.0F;
                  }
               }

               if (inputValue + 76.0F < this.cy + this.ch - 12.0F) {
                  this.updateState7(
                     compositorPushPresentationScale,
                     height,
                     inputValue + 2.0F,
                     "INSIDE COMPOSITOR",
                     10.0F,
                     MaterialIsLightService.ON_SURFACE_VARIANT,
                     true
                  );

                  for (int currentIndex = 0; currentIndex < DiagnosticsAddFrameListenerService.SECTIONS_COMPOSITOR.length; currentIndex++) {
                     String nextText = DiagnosticsAddFrameListenerService.SECTIONS_COMPOSITOR[currentIndex];
                     float outputValue = height + currentIndex % 2 * (value / 2.0F + 4.0F);
                     float resultValue = inputValue + 22.0F + currentIndex / 2 * 16;
                     this.updateState7(compositorPushPresentationScale, outputValue, resultValue, nextText, 10.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false);
                     this.updateState6(
                        compositorPushPresentationScale,
                        outputValue + value / 2.0F - 12.0F,
                        resultValue,
                        DiagnosticsRecorderService.createText("%.2f", this.diagnosticsData.section(nextText)),
                        10.0F,
                        MaterialIsLightService.ON_SURFACE
                     );
                  }
               }

               compositorPushPresentationScale.roundedRect(
                  height,
                  this.cy + this.ch - 32.0F,
                  value,
                  1.0F,
                  0.0F,
                  mulAlpha(MaterialIsLightService.OUTLINE_VARIANT, this.effectiveOpacity)
               );
               this.updateState7(
                  compositorPushPresentationScale,
                  height,
                  this.cy + this.ch - 22.0F,
                  "Heap " + this.timestamp3 + " / " + this.timestamp4 + " MiB",
                  10.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               );
               this.updateState6(
                  compositorPushPresentationScale,
                  height + value,
                  this.cy + this.ch - 22.0F,
                  "F6 / record",
                  10.0F,
                  MaterialIsLightService.PRIMARY
               );
            }
         }
      }

      private void updateState2(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, float nextValue, float previousValue) {
         compositorPushPresentationScale.roundedRect(
            value, currentValue, nextValue, previousValue, 16.0F, mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity)
         );
         float sourceValue = nextValue - 44.0F;
         compositorPushPresentationScale.pushClip(value, currentValue, nextValue, previousValue, 16.0F);

         for (double doubleValue : new double[]{6.944444444444445, 16.666666666666668}) {
            if (!(doubleValue >= this.diagnosticsData.graph().ceiling())) {
               float targetValue = currentValue
                  + 5.0F
                  + (previousValue - 10.0F) * (1.0F - (float)(doubleValue / this.diagnosticsData.graph().ceiling()));
               compositorPushPresentationScale.roundedRect(
                  value + 8.0F,
                  targetValue,
                  sourceValue - 8.0F,
                  0.6F,
                  0.0F,
                  mulAlpha(MaterialIsLightService.OUTLINE_VARIANT, this.effectiveOpacity)
               );
               this.updateState6(
                  compositorPushPresentationScale,
                  value + nextValue - 8.0F,
                  targetValue - 5.0F,
                  doubleValue > 10.0 ? "60" : "144",
                  9.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT
               );
            }
         }

         compositorPushPresentationScale.timeSeries(
            value + 4.0F,
            currentValue,
            sourceValue,
            previousValue,
            this.diagnosticsData.graph(),
            MaterialIsLightService.PRIMARY,
            this.effectiveOpacity
         );
         if (this.diagnosticsData.samples() == 0) {
            this.updateState7(
               compositorPushPresentationScale,
               value + 12.0F,
               currentValue + previousValue / 2.0F - 6.0F,
               "Waiting for frame samples",
               11.0F,
               MaterialIsLightService.ON_SURFACE_VARIANT,
               false
            );
         }

         compositorPushPresentationScale.popClip();
      }

      private void updateState3(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, float nextValue) {
         compositorPushPresentationScale.roundedRect(
            value,
            currentValue,
            nextValue,
            122.0F,
            18.0F,
            mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity)
         );
         this.updateState7(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 12.0F,
            "ShaderESP",
            14.0F,
            MaterialIsLightService.PRIMARY,
            true
         );
         this.updateState6(
            compositorPushPresentationScale,
            value + nextValue - 12.0F,
            currentValue + 14.0F,
            DiagnosticsRecorderService.createText("%.2f ms CPU", this.diagnosticsData.section("shaderesp")),
            11.0F,
            MaterialIsLightService.ON_SURFACE
         );
         this.updateState8(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 36.0F,
            this.renderer2.status() + " / " + this.renderer2.effects(),
            11.0F,
            !this.renderer2.status().contains("failed") && !this.renderer2.status().contains("unavailable")
               ? MaterialIsLightService.ON_SURFACE
               : MaterialIsLightService.ERROR,
            false,
            nextValue - 24.0F
         );
         this.updateState8(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 54.0F,
            this.renderer2.selected()
               + " selected / "
               + this.renderer2.captured()
               + " captured / "
               + this.renderer2.regions()
               + " regions",
            11.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT,
            false,
            nextValue - 24.0F
         );
         this.updateState8(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 72.0F,
            this.renderer2.vertices() / 3 + " triangles / " + this.renderer2.width() + " x " + this.renderer2.height() + " mask",
            11.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT,
            false,
            nextValue - 24.0F
         );
         this.updateState8(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 90.0F,
            "Depth "
               + (this.renderer2.terrainDepth() ? "ready" : "--")
               + " / Hand "
               + (this.renderer2.handProtection() ? "protected" : "--")
               + DiagnosticsRecorderService.createText(" / %.2f ms copy", this.diagnosticsData.section("shaderesp-depth")),
            10.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT,
            false,
            nextValue - 24.0F
         );
         this.updateState8(
            compositorPushPresentationScale,
            value + 12.0F,
            currentValue + 106.0F,
            "Nested CPU scopes, not GPU execution time",
            9.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT,
            false,
            nextValue - 24.0F
         );
      }

      private void updateState4(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, float nextValue, String text, double doubleValue, int previousValue) {
         this.updateState7(compositorPushPresentationScale, value, currentValue, text, 11.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false);
         this.updateState6(
            compositorPushPresentationScale,
            value + 174.0F,
            currentValue,
            DiagnosticsRecorderService.createText("%.2f", doubleValue),
            11.0F,
            MaterialIsLightService.ON_SURFACE
         );
         float sourceValue = Math.max(0.0F, nextValue - 184.0F);
         compositorPushPresentationScale.roundedRect(
            value + 184.0F,
            currentValue + 4.0F,
            sourceValue,
            6.0F,
            3.0F,
            mulAlpha(MaterialIsLightService.SURFACE_HIGHEST, this.effectiveOpacity)
         );
         float targetValue = (float)Math.clamp(
            doubleValue / Math.max(0.001, this.diagnosticsData.averageMs()), 0.0, 1.0
         );
         if (targetValue > 0.0F) {
            compositorPushPresentationScale.roundedRect(
               value + 184.0F,
               currentValue + 4.0F,
               sourceValue * targetValue,
               6.0F,
               3.0F,
               mulAlpha(previousValue, this.effectiveOpacity)
            );
         }
      }

      private void updateState5(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, float nextValue, String text, String currentText, int previousValue) {
         this.updateState8(compositorPushPresentationScale, value, currentValue, text, 28.0F, previousValue, false, nextValue - 8.0F);
         this.updateState7(
            compositorPushPresentationScale,
            value,
            currentValue + 34.0F,
            currentText,
            10.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT,
            false
         );
      }

      private void updateState6(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, String text, float nextValue, int previousValue) {
         this.updateState7(compositorPushPresentationScale, value - compositorPushPresentationScale.textWidth(text, nextValue, TextMode.REGULAR, "material-roboto"), currentValue, text, nextValue, previousValue, false);
      }

      private void updateState7(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, String currentText, float nextValue, int previousValue, boolean enabled) {
         compositorPushPresentationScale.text(value, currentValue, currentText, nextValue, mulAlpha(previousValue, this.effectiveOpacity), enabled ? MaterialIsLightService.LABEL : MaterialIsLightService.BODY);
      }

      private void updateState8(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, String text, float nextValue, int previousValue, boolean enabled, float sourceValue) {
         String currentText = enabled ? "material-roboto-medium" : "material-roboto";
         if (compositorPushPresentationScale.textWidth(text, nextValue, TextMode.REGULAR, currentText) > sourceValue) {
            while (!text.isEmpty() && compositorPushPresentationScale.textWidth(text + "...", nextValue, TextMode.REGULAR, currentText) > sourceValue) {
               text = text.substring(0, text.length() - 1);
            }

            text = text + "...";
         }

         this.updateState7(compositorPushPresentationScale, value, currentValue, text, nextValue, previousValue, enabled);
      }
   }
}

