package dev.felix.ellice.ui.material;

import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;

public final class MeasuredTextNode extends ScenePctService<MeasuredTextNode> {
   private String text2 = "";
   private List<String> text3 = List.of();
   private String text4;
   private float value = -1.0F;
   private float value2;
   private long timestamp = -1L;

   public MeasuredTextNode() {
      this.pointerEvents(false);
   }

   public MeasuredTextNode text(String currentText) {
      this.text2 = currentText == null ? "" : currentText;
      return this;
   }

   private void updateState(CompositorPushPresentationScaleService compositorPushPresentationScale, float currentValue) {
      String text = PrivacyRevisionService.replace(this.text2);
      if (!text.equals(this.text4) || currentValue != this.value || this.timestamp != compositorPushPresentationScale.fontMetricsRevision()) {
         this.text4 = text;
         this.value = currentValue;
         this.timestamp = compositorPushPresentationScale.fontMetricsRevision();
         ArrayList arrayList = new ArrayList();
         StringBuilder stringBuilder = new StringBuilder();

         for (String currentText : text.split("\\s+")) {
            String nextText = stringBuilder.isEmpty() ? currentText : stringBuilder + " " + currentText;
            if (!stringBuilder.isEmpty()
               && compositorPushPresentationScale.textWidthLiteral(
                     nextText, 12.0F, TextMode.REGULAR, "material-roboto", 0.4F
                  )
                  > currentValue) {
               arrayList.add(stringBuilder.toString());
               stringBuilder.setLength(0);
            }

            if (!stringBuilder.isEmpty()) {
               stringBuilder.append(' ');
            }

            stringBuilder.append(currentText);
         }

         if (!stringBuilder.isEmpty()) {
            arrayList.add(stringBuilder.toString());
         }

         this.text3 = List.copyOf(arrayList);
         this.value2 = 0.0F;

         for (String previousText : this.text3) {
            this.value2 = Math.max(
               this.value2,
               Math.min(
                  currentValue,
                  compositorPushPresentationScale.textWidthLiteral(
                     previousText, 12.0F, TextMode.REGULAR, "material-roboto", 0.4F
                  )
               )
            );
         }
      }
   }

   @Override
   public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this.updateState(compositorPushPresentationScale, 224.0F);
      return (float)Math.ceil(this.value2) + 16.0F;
   }

   @Override
   public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this.updateState(compositorPushPresentationScale, 224.0F);
      return Math.max(24, this.text3.size() * 16 + 8);
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this.updateState(compositorPushPresentationScale, Math.max(1.0F, this.cw - 16.0F));
      compositorPushPresentationScale.roundedRect(
         this.cx,
         this.cy,
         this.cw,
         this.ch,
         4.0F,
         mulAlpha(MaterialIsLightService.INVERSE_SURFACE, this.effectiveOpacity)
      );
      compositorPushPresentationScale.pushClip(this.cx + 8.0F, this.cy, this.cw - 16.0F, this.ch);
      float y = (
            16.0F
               - compositorPushPresentationScale.textLineHeight(12.0F, TextMode.REGULAR, "material-roboto")
         )
         / 2.0F;

      for (int index = 0; index < this.text3.size(); index++) {
         compositorPushPresentationScale.textLiteral(
            this.cx + 8.0F,
            this.cy + 4.0F + y + index * 16,
            this.text3.get(index),
            12.0F,
            mulAlpha(MaterialIsLightService.INVERSE_ON_SURFACE, this.effectiveOpacity),
            MaterialIsLightService.BODY.withLetterSpacing(0.4F)
         );
      }

      compositorPushPresentationScale.popClip();
   }
}
