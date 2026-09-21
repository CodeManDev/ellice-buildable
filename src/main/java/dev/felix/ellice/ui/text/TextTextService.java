package dev.felix.ellice.ui.text;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorIntersectService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.system.MemoryUtil;

public class TextTextService {
   private static final int count = 4096;
   private static final int count2 = 32768;
   private static final int count3 = 12;
   private static final int count4 = 4;
   private static final int count5 = 6;
   private final RhiOperationHandler rhiOperationHandler;
   private final Map<TextMode, TextTextureService> entries;
   private final TextTextureService textTextureService;
   private final Map<TextTextService.RuntimeFace, GlyphAtlas> entries2 = new HashMap<>();
   private final Map<String, TextTextureService> text2 = new HashMap<>();
   private RhiBlendStateService.PipelineHandle pipelineHandle;
   private RhiBlendStateService.PipelineHandle pipelineHandle2;
   private RhiBlendStateService.BufferHandle bufferHandle;
   private RhiBlendStateService.BufferHandle fighjurpy8;
   private boolean enabled;
   private int count6 = 4096;
   private float[] float2 = new float[196608];
   private float[] float3 = new float[4096];
   private float[] float4 = new float[4096];
   private float[] float5 = new float[196608];
   private boolean enabled2;
   private int count7;
   private final List<TextTextService.Batch> items = new ArrayList<>();
   private TextData textData = TextData.NONE;
   private float[] float6;
   private TextOperationHandler textOperationHandler;
   private boolean enabled3;
   private int count8;
   private int count9 = -1;
   private double value = Double.NaN;

   public TextTextService(RhiOperationHandler rhiOperation, Map<TextMode, TextTextureService> entries) {
      this(rhiOperation, entries, null);
   }

   public TextTextService(RhiOperationHandler rhiOperation, Map<TextMode, TextTextureService> currentEntries, TextTextureService textTexture) {
      this.rhiOperationHandler = rhiOperation;
      this.entries = currentEntries;
      this.textTextureService = textTexture;
   }

   public void text(float value, float currentValue, String currentText, float nextValue, int previousValue) {
      this.text(value, currentValue, currentText, nextValue, previousValue, TextData.NONE, null);
   }

   public void text(float value, float currentValue, String currentText, float nextValue, int previousValue, TextData textData) {
      this.text(value, currentValue, currentText, nextValue, previousValue, textData, null);
   }

   public void text(float value, float currentValue, String currentText, float nextValue, int previousValue, TextData textData, float[] floats) {
      if (currentText != null && !currentText.isEmpty()) {
         TextData currentTextData = textData != null ? textData : TextData.NONE;
         if (TextCodec.contains(currentText)) {
            this.updateState2(value, currentValue, currentText, nextValue, previousValue, currentTextData, floats);
         } else {
            this.updateState(value, currentValue, currentText, nextValue, previousValue, currentTextData, floats);
         }
      }
   }

   private void updateState(float value, float currentValue, String text, float nextValue, int previousValue, TextData textData, float[] floats) {
      if (!checkCondition4(text) && textData.letterSpacing() != 0.0F) {
         textData = textData.withLetterSpacing(0.0F);
      }

      TextMode textMode = this.createTextMode(textData.variant());
      TextTextureService textTexture = this.createTextTextureService2(textData.fontFamily(), textMode);
      if (textData.letterSpacing() == 0.0F && this.checkCondition3(text, textData.fontFamily(), textMode)) {
         this.updateState3(textTexture, this.createTextTextureService22(textTexture, textMode), textMode, textData, floats, value, currentValue, text, nextValue, previousValue);
      } else {
         this.updateState4(textTexture, textMode, textData, floats, value, currentValue, text, nextValue, previousValue);
      }
   }

   private void updateState2(float value, float currentValue, String currentText, float nextValue, int previousValue, TextData textData, float[] floats) {
      float sourceValue = 0.0F;
      float targetValue = currentValue;

      for (TextCodec.Segment segment : TextCodec.parse(currentText, previousValue, textData)) {
         TextData currentTextData = segment.style();
         TextMode textMode = this.createTextMode(currentTextData.variant());
         String[] strings = segment.text().split("\\n", -1);

         for (int index = 0; index < strings.length; index++) {
            String nextText = strings[index];
            if (!nextText.isEmpty()) {
               this.updateState(value + sourceValue, targetValue, nextText, nextValue, segment.color(), currentTextData, floats);
               sourceValue += this.calculateValue2(nextText, nextValue, textMode, currentTextData.fontFamily(), currentTextData.letterSpacing());
            }

            if (index + 1 < strings.length) {
               sourceValue = 0.0F;
               targetValue += this.lineHeight(nextValue, textMode, currentTextData.fontFamily());
            }
         }
      }
   }

   private static boolean checkCondition(float[] floats, float[] currentFloats) {
      return CompositorIntersectService.same(floats, currentFloats);
   }

   public float measureWidth(String id, float value) {
      return this.measureWidth(id, value, TextMode.REGULAR);
   }

   public float measureWidth(String id, float value, TextMode textMode) {
      return this.measureWidth(id, value, textMode, null);
   }

   public float measureWidth(String id, float value, TextMode textMode, String currentId) {
      textMode = this.createTextMode(textMode);
      TextTextureService textTexture = this.createTextTextureService2(currentId, textMode);
      if (id == null) {
         return 0.0F;
      } else if (TextCodec.contains(id)) {
         TextData textData = TextData.NONE.withVariant(textMode).withFontFamily(currentId);
         return this.calculateValue3(id, value, textData);
      } else {
         return this.calculateValue(id, value, textMode, currentId);
      }
   }

   public float measureWidth(String id, float value, TextMode textMode, String currentId, float currentValue) {
      textMode = this.createTextMode(textMode);
      if (id == null) {
         return 0.0F;
      } else if (TextCodec.contains(id)) {
         TextData textData = TextData.NONE.withVariant(textMode).withFontFamily(currentId).withLetterSpacing(currentValue);
         return this.calculateValue3(id, value, textData);
      } else {
         return this.calculateValue2(id, value, textMode, currentId, currentValue);
      }
   }

   private float calculateValue(String text, float value, TextMode textMode, String currentText) {
      return this.calculateValue2(text, value, textMode, currentText, 0.0F);
   }

   private float calculateValue2(String text, float value, TextMode textMode, String currentText, float currentValue) {
      if (currentValue != 0.0F && !checkCondition4(text)) {
         currentValue = 0.0F;
      }

      TextTextureService textTexture = this.createTextTextureService2(currentText, textMode);
      if (currentValue == 0.0F && this.checkCondition3(text, currentText, textMode)) {
         return this.calculateValue6(text, value, textMode, textTexture, currentValue);
      }

      float nextValue = 0.0F;
      float previousValue = 0.0F;
      int index = 0;

      while (index < text.length()) {
         int sourceValue = text.codePointAt(index);
         index += Character.charCount(sourceValue);
         if (sourceValue != 13) {
            if (sourceValue == 10) {
               nextValue = Math.max(nextValue, previousValue);
               previousValue = 0.0F;
            } else if (sourceValue == 9) {
               previousValue += this.calculateValue4(textTexture, textMode, value);
            } else {
               TextTextService.GlyphRun glyphRun = this.createGlyphRun(textTexture, textMode, sourceValue);
               if (glyphRun != null) {
                  previousValue += glyphRun.glyph.advance() * value + currentValue;
               }
            }
         }
      }

      return Math.max(nextValue, previousValue);
   }

   private float calculateValue3(String currentText, float value, TextData textData) {
      float currentValue = 0.0F;
      float nextValue = 0.0F;

      for (TextCodec.Segment segment : TextCodec.parse(currentText, -1, textData)) {
         TextData currentTextData = segment.style();
         TextMode textMode = this.createTextMode(currentTextData.variant());
         String nextText = segment.text();
         int previousValue = 0;

         while (true) {
            int sourceValue = nextText.indexOf(10, previousValue);
            String previousText = sourceValue >= 0 ? nextText.substring(previousValue, sourceValue) : nextText.substring(previousValue);
            nextValue += this.calculateValue2(previousText, value, textMode, currentTextData.fontFamily(), currentTextData.letterSpacing());
            currentValue = Math.max(currentValue, nextValue);
            if (sourceValue < 0) {
               break;
            }

            nextValue = 0.0F;
            previousValue = sourceValue + 1;
         }
      }

      return currentValue;
   }

   public float lineHeight(float value) {
      return this.lineHeight(value, TextMode.REGULAR);
   }

   public float lineHeight(float value, TextMode textMode) {
      return this.lineHeight(value, textMode, null);
   }

   public float lineHeight(float value, TextMode textMode, String text) {
      textMode = this.createTextMode(textMode);
      TextTextureService textTexture = this.createTextTextureService2(text, textMode);
      return textTexture != null && textTexture.isReady() && textTexture.variant() == textMode
         ? textTexture.lineHeight() * value
         : this.createTextTextureService22(textTexture, textMode).lineHeight() * value;
   }

   public float caretX(String text, int value, float currentValue, TextMode textMode) {
      return this.caretX(text, value, currentValue, textMode, null);
   }

   public float caretX(String text, int value, float currentValue, TextMode textMode, String currentText) {
      textMode = this.createTextMode(textMode);
      if (text != null && !text.isEmpty()) {
         value = TextEndsService.clampToBoundary(text, value);
         if (TextCodec.contains(text)) {
            return this.measureWidth(text.substring(0, value), currentValue, textMode, currentText);
         } else {
            return this.checkCondition3(text, currentText, textMode)
               ? this.createTextTextureService22(this.createTextTextureService2(currentText, textMode), textMode).caretX(text, value) * currentValue
               : this.measureWidth(text.substring(0, value), currentValue, textMode, currentText);
         }
      } else {
         return 0.0F;
      }
   }

   public float[] selectionSpans(String text, int value, int currentValue, float nextValue, TextMode textMode) {
      return this.selectionSpans(text, value, currentValue, nextValue, textMode, null);
   }

   public float[] selectionSpans(String text, int value, int currentValue, float nextValue, TextMode textMode, String currentText) {
      textMode = this.createTextMode(textMode);
      if (text != null && !text.isEmpty() && value != currentValue) {
         value = TextEndsService.clampToBoundary(text, value);
         currentValue = TextEndsService.clampToBoundary(text, currentValue);
         if (value > currentValue) {
            int previousValue = value;
            value = currentValue;
            currentValue = previousValue;
         }

         if (TextCodec.contains(text)) {
            float sourceValue = this.measureWidth(text.substring(0, value), nextValue, textMode, currentText);
            float targetValue = this.measureWidth(text.substring(0, currentValue), nextValue, textMode, currentText);
            return new float[]{Math.min(sourceValue, targetValue), Math.abs(targetValue - sourceValue)};
         }

         if (!this.checkCondition3(text, currentText, textMode)) {
            float inputValue = this.measureWidth(text.substring(0, value), nextValue, textMode, currentText);
            float outputValue = this.measureWidth(text.substring(0, currentValue), nextValue, textMode, currentText);
            return new float[]{Math.min(inputValue, outputValue), Math.abs(outputValue - inputValue)};
         }

         List items = this.createTextTextureService22(this.createTextTextureService2(currentText, textMode), textMode).selectionSpans(text, value, currentValue);
         float[] currentSize = new float[items.size() * 2];

         for (int index = 0; index < items.size(); index++) {
            currentSize[index * 2] = ((GlyphAtlas.SelectionSpan)items.get(index)).x() * nextValue;
            currentSize[index * 2 + 1] = ((GlyphAtlas.SelectionSpan)items.get(index)).width() * nextValue;
         }

         return currentSize;
      } else {
         return new float[0];
      }
   }

   public int batchCount() {
      if (this.count7 > this.count8) {
         this.updateState6();
         this.count8 = this.count7;
      }

      return this.items.size();
   }

   public void flush(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue) {
      this.flushRange(rhiCommandBuffer, value, currentValue, doubleValue, 0, -1);
      this.reset();
   }

   public void flushRange(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int index) {
      if (this.count7 != 0 || !this.items.isEmpty()) {
         if (!this.enabled) {
            this.updateState11();
         }

         if (this.enabled) {
            if (this.count7 > this.count8) {
               this.updateState6();
               this.count8 = this.count7;
            }

            if (!this.items.isEmpty()) {
               int currentSize = index >= 0 && index <= this.items.size() ? index : this.items.size();
               if (nextValue < currentSize) {
                  this.updateState8(doubleValue);

                  for (int currentIndex = nextValue; currentIndex < currentSize; currentIndex++) {
                     TextTextService.Batch batch = this.items.get(currentIndex);
                     if (batch.atlas != null) {
                        RhiBlendStateService.TextureHandle textureHandle = batch.atlas.texture();
                        if (textureHandle != null && textureHandle.valid()) {
                           rhiCommandBuffer.bindPipeline(batch.color ? this.pipelineHandle2 : this.pipelineHandle);
                           rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
                           rhiCommandBuffer.bindIndexBuffer(this.fighjurpy8, RhiBlendStateService.IndexType.UINT32);
                           rhiCommandBuffer.pushInt("uAtlas", 0);
                           rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
                           rhiCommandBuffer.bindTexture(textureHandle, 0);
                           if (!batch.color) {
                              rhiCommandBuffer.pushFloat("uPxRange", batch.atlas.pixelRange());
                           }

                           CompositorIntersectService.apply(rhiCommandBuffer, batch.clip, (float)doubleValue, currentValue);
                           float previousValue = (float)doubleValue;
                           if (batch.style.hasShadow()) {
                              rhiCommandBuffer.pushVec4(
                                 "uShadow",
                                 batch.style.shadowX() * previousValue,
                                 batch.style.shadowY() * previousValue,
                                 2.5F * previousValue,
                                 0.0F
                              );
                              updateState10(rhiCommandBuffer, "uShadowColor", batch.style.shadowColor());
                           } else {
                              rhiCommandBuffer.pushVec4("uShadow", 0.0F, 0.0F, 0.0F, 0.0F);
                           }

                           if (!batch.color) {
                              rhiCommandBuffer.pushFloat("uOutlineWidth", batch.style.outlineWidth() * previousValue);
                              if (batch.style.hasOutline()) {
                                 updateState10(rhiCommandBuffer, "uOutlineColor", batch.style.outlineColor());
                              }

                              rhiCommandBuffer.pushFloat("uEdgeSoftness", batch.style.edgeSoftness() * (float)doubleValue);
                           }

                           rhiCommandBuffer.drawIndexed(batch.quadCount * 6, 1, batch.startQuad * 6);
                           CompositorIntersectService.clear(rhiCommandBuffer, batch.clip);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void reset() {
      this.count7 = 0;
      this.items.clear();
      this.count8 = 0;
      this.textData = TextData.NONE;
      this.float6 = null;
      this.textOperationHandler = null;
      this.enabled3 = false;
      this.count9 = -1;
      this.value = Double.NaN;

      for (GlyphAtlas glyphAtlas : this.entries2.values()) {
         glyphAtlas.compactIfNeeded();
      }
   }

   private TextMode createTextMode(TextMode textMode) {
      return textMode != null ? textMode : TextMode.REGULAR;
   }

   private TextTextureService createTextTextureService(TextMode textMode) {
      TextTextureService textTexture = this.entries.get(textMode);
      return textTexture != null && textTexture.isReady() ? textTexture : this.entries.get(TextMode.REGULAR);
   }

   private TextTextureService createTextTextureService2(String text, TextMode textMode) {
      if (text != null) {
         if ("minecraft".equals(text) && this.textTextureService != null && this.textTextureService.isReady()) {
            return this.textTextureService;
         }

         TextTextureService textTexture = this.text2.get(text);
         if (textTexture != null && textTexture.isReady()) {
            return textTexture;
         }
      }

      return this.createTextTextureService(textMode);
   }

   private boolean checkCondition2(String text) {
      return text != null && ("minecraft".equals(text) || this.text2.containsKey(text));
   }

   public void registerFontFamily(String text, TextTextureService textTexture) {
      if (text != null && textTexture != null) {
         TextTextureService currentTextTexture = this.text2.put(text, textTexture);
         if (currentTextTexture != null && currentTextTexture != textTexture && this.rhiOperationHandler != null) {
            try {
               currentTextTexture.shutdown(this.rhiOperationHandler);
            } catch (RuntimeException exception) {
               CoreIsInitializedHandler.LOGGER.warn("Failed to release replaced font atlas for {}", text, exception);
            }
         }
      }
   }

   public boolean hasFontFamily(String text) {
      TextTextureService textTexture = text == null ? null : this.text2.get(text);
      return textTexture != null && textTexture.isReady();
   }

   private boolean checkCondition3(String text, String currentText, TextMode textMode) {
      TextTextureService textTexture = this.createTextTextureService2(currentText, textMode);
      if (textTexture == null) {
         return true;
      } else {
         return !this.checkCondition5(text, textTexture) ? false : !this.checkCondition2(currentText) || !checkCondition4(text);
      }
   }

   private static boolean checkCondition4(String text) {
      for (int index = 0; index < text.length(); index++) {
         if (text.charAt(index) > 127) {
            return false;
         }
      }

      return true;
   }

   public static float trackingAdvance(String text, int value, float currentValue) {
      if (text != null && currentValue != 0.0F && checkCondition4(text)) {
         int currentLength = Math.max(0, Math.min(text.length(), value));
         int index = 0;
         int currentIndex = 0;

         while (currentIndex < currentLength) {
            int nextValue = text.codePointAt(currentIndex);
            if (nextValue == 167 && currentIndex + 1 < currentLength) {
               if (++currentIndex < currentLength) {
                  char character = text.charAt(currentIndex);
                  currentIndex++;
                  if (Character.toLowerCase(character) == 'x') {
                     int previousValue = Math.min(12, currentLength - currentIndex);
                     if (previousValue >= 12) {
                        currentIndex += 12;
                     }
                  }
               }
            } else {
               if (nextValue != 13 && nextValue != 10 && nextValue != 9) {
                  index++;
               }

               currentIndex += Character.charCount(nextValue);
            }
         }

         return index * currentValue;
      } else {
         return 0.0F;
      }
   }

   private GlyphAtlas createTextTextureService22(TextTextureService textTexture, TextMode textMode) {
      return this.entries2
         .computeIfAbsent(
            new TextTextService.RuntimeFace(textTexture, textMode),
            item -> new GlyphAtlas(this.rhiOperationHandler, textMode, textTexture != null ? textTexture : new TextTextureService(textMode))
         );
   }

   private TextTextService.GlyphRun createGlyphRun(TextTextureService textTexture, TextMode textMode, int value) {
      if (textTexture != null && textTexture.isReady() && textTexture.variant() == textMode) {
         TextTextureService.GlyphInfo glyphInfo = textTexture.glyph(value);
         if (glyphInfo != null) {
            return new TextTextService.GlyphRun(textTexture, glyphInfo, textTexture.colorGlyph(glyphInfo));
         }
      }

      GlyphAtlas glyphAtlas = this.createTextTextureService22(textTexture, textMode);
      TextTextureService.GlyphInfo currentGlyphInfo = glyphAtlas.glyph(value);
      return currentGlyphInfo != null ? new TextTextService.GlyphRun(glyphAtlas, currentGlyphInfo, glyphAtlas.colorGlyph(currentGlyphInfo)) : null;
   }

   private float calculateValue4(TextTextureService textTexture, TextMode textMode, float value) {
      TextTextService.GlyphRun glyphRun = this.createGlyphRun(textTexture, textMode, 32);
      float currentValue = glyphRun != null ? glyphRun.glyph.advance() : 0.25F;
      return currentValue * value * 4.0F;
   }

   private boolean checkCondition5(String text, TextTextureService textTexture) {
      int value = 0;
      byte byteValue = 0;
      int index = 0;

      while (index < text.length()) {
         int currentValue = text.codePointAt(index);
         index += Character.charCount(currentValue);
         if (currentValue != 8205 && currentValue != 8204 && !Character.isEmojiPresentation(currentValue)) {
            if (currentValue != 13 && currentValue != 10 && currentValue != 9 && textTexture.glyph(currentValue) == null) {
               return true;
            }

            int nextValue = Character.getType(currentValue);
            if (nextValue != 6 && nextValue != 8 && nextValue != 7) {
               byte currentByteValue = Character.getDirectionality(currentValue);
               if (currentByteValue != 1 && currentByteValue != 2) {
                  int previousValue = currentValue == 102 ? 1 : 0;
                  if ((value == 0 || currentValue != 105 && currentValue != 108 && currentValue != 102) && (byteValue == 0 || currentValue != 105 && currentValue != 108)) {
                     byteValue = (byte)(value != 0 && previousValue != 0 ? 1 : 0);
                     value = previousValue;
                     continue;
                  }

                  return true;
               }

               return true;
            }

            return true;
         }

         return true;
      }

      return false;
   }

   private float calculateValue5(String text, float value, TextMode textMode, TextTextureService textTexture) {
      return this.calculateValue6(text, value, textMode, textTexture, 0.0F);
   }

   private float calculateValue6(String text, float value, TextMode textMode, TextTextureService textTexture, float currentValue) {
      GlyphAtlas glyphAtlas = this.createTextTextureService22(textTexture, textMode);
      float nextValue = 0.0F;
      float previousValue = 0.0F;
      int sourceValue = 0;
      int index = 0;

      while (index < text.length()) {
         int targetValue = index;
         int inputValue = text.codePointAt(index);
         index += Character.charCount(inputValue);
         if (inputValue == 13) {
            previousValue += calculateValue7(glyphAtlas, text.substring(sourceValue, targetValue), value, currentValue);
            sourceValue = index;
         } else if (inputValue == 10) {
            previousValue += calculateValue7(glyphAtlas, text.substring(sourceValue, targetValue), value, currentValue);
            nextValue = Math.max(nextValue, previousValue);
            previousValue = 0.0F;
            sourceValue = index;
         } else if (inputValue == 9) {
            previousValue += calculateValue7(glyphAtlas, text.substring(sourceValue, targetValue), value, currentValue);
            previousValue += this.calculateValue4(textTexture, textMode, value);
            sourceValue = index;
         }
      }

      previousValue += calculateValue7(glyphAtlas, text.substring(sourceValue), value, currentValue);
      return Math.max(nextValue, previousValue);
   }

   private static float calculateValue7(GlyphAtlas glyphAtlas, String text, float value, float currentValue) {
      if (text.isEmpty()) {
         return 0.0F;
      }

      GlyphAtlas.ShapedLine shapedLine = glyphAtlas.shapeLine(text);
      return shapedLine.width() * value + shapedLine.glyphs().size() * currentValue;
   }

   private void updateState3(
      TextTextureService textTexture,
      GlyphAtlas glyphAtlas,
      TextMode textMode,
      TextData textData,
      float[] floats,
      float value,
      float currentValue,
      String text,
      float nextValue,
      int previousValue
   ) {
      float sourceValue = (previousValue >> 16 & 0xFF) / 255.0F;
      float targetValue = (previousValue >> 8 & 0xFF) / 255.0F;
      float inputValue = (previousValue & 0xFF) / 255.0F;
      float outputValue = (previousValue >> 24 & 0xFF) / 255.0F;
      float resultValue = 0.0F;
      float candidateValue = currentValue;
      float selectedValue = this.lineHeight(nextValue, textMode, textData.fontFamily());
      int defaultValue = 0;
      int index = 0;

      while (index < text.length()) {
         int initialValue = index;
         int resolvedValue = text.codePointAt(index);
         index += Character.charCount(resolvedValue);
         if (resolvedValue == 13) {
            resultValue += this.calculateValue8(glyphAtlas, textData, floats, value, candidateValue, resultValue, text.substring(defaultValue, initialValue), nextValue, sourceValue, targetValue, inputValue, outputValue);
            defaultValue = index;
         } else if (resolvedValue == 10) {
            resultValue += this.calculateValue8(glyphAtlas, textData, floats, value, candidateValue, resultValue, text.substring(defaultValue, initialValue), nextValue, sourceValue, targetValue, inputValue, outputValue);
            resultValue = 0.0F;
            candidateValue += selectedValue;
            defaultValue = index;
         } else if (resolvedValue == 9) {
            resultValue += this.calculateValue8(glyphAtlas, textData, floats, value, candidateValue, resultValue, text.substring(defaultValue, initialValue), nextValue, sourceValue, targetValue, inputValue, outputValue);
            resultValue += this.calculateValue4(textTexture, textMode, nextValue);
            defaultValue = index;
         }
      }

      this.calculateValue8(glyphAtlas, textData, floats, value, candidateValue, resultValue, text.substring(defaultValue), nextValue, sourceValue, targetValue, inputValue, outputValue);
   }

   private float calculateValue8(
      GlyphAtlas glyphAtlas,
      TextData textData,
      float[] floats,
      float value,
      float currentValue,
      float nextValue,
      String text,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue
   ) {
      if (text.isEmpty()) {
         return 0.0F;
      }

      GlyphAtlas.ShapedLine shapedLine = glyphAtlas.shapeLine(text);
      int index = 0;

      for (GlyphAtlas.ShapedGlyph shapedGlyph : shapedLine.glyphs()) {
         TextTextureService.GlyphInfo glyphInfo = shapedGlyph.glyph();
         float resultValue = (index++ + 0.5F) * textData.letterSpacing();
         if (!(glyphInfo.width() <= 0.0F)) {
            if (!this.checkCondition6(this.count7 + 1)) {
               break;
            }

            this.updateState5(textData, floats, glyphAtlas, glyphAtlas.colorGlyph(glyphInfo));
            float currentX = value + nextValue + (shapedGlyph.x() + glyphInfo.bearingX()) * previousValue + resultValue;
            float currentY = currentValue + (shapedGlyph.y() + glyphInfo.bearingY()) * previousValue;
            this.updateState7(
               currentX,
               currentY,
               glyphInfo.width() * previousValue,
               glyphInfo.height() * previousValue,
               value,
               currentValue,
               glyphInfo.u0(),
               glyphInfo.v0(),
               glyphInfo.u1(),
               glyphInfo.v1(),
               sourceValue,
               targetValue,
               inputValue,
               outputValue
            );
         }
      }

      return shapedLine.width() * previousValue + index * textData.letterSpacing();
   }

   private void updateState4(
      TextTextureService textTexture, TextMode textMode, TextData textData, float[] floats, float value, float currentValue, String text, float nextValue, int previousValue
   ) {
      float sourceValue = (previousValue >> 16 & 0xFF) / 255.0F;
      float targetValue = (previousValue >> 8 & 0xFF) / 255.0F;
      float inputValue = (previousValue & 0xFF) / 255.0F;
      float outputValue = (previousValue >> 24 & 0xFF) / 255.0F;
      float resultValue = 0.0F;
      float candidateValue = currentValue;
      float selectedValue = this.lineHeight(nextValue, textMode, textData.fontFamily());
      int index = 0;

      while (index < text.length()) {
         int defaultValue = text.codePointAt(index);
         index += Character.charCount(defaultValue);
         if (defaultValue != 13) {
            if (defaultValue == 10) {
               resultValue = 0.0F;
               candidateValue += selectedValue;
            } else if (defaultValue == 9) {
               resultValue += this.calculateValue4(textTexture, textMode, nextValue);
            } else {
               TextTextService.GlyphRun glyphRun = this.createGlyphRun(textTexture, textMode, defaultValue);
               if (glyphRun != null) {
                  TextTextureService.GlyphInfo glyphInfo = glyphRun.glyph;
                  if (glyphInfo.width() <= 0.0F) {
                     resultValue += glyphInfo.advance() * nextValue + textData.letterSpacing();
                  } else {
                     if (!this.checkCondition6(this.count7 + 1)) {
                        break;
                     }

                     this.updateState5(textData, floats, glyphRun.atlas, glyphRun.color);
                     float initialValue = value + resultValue + glyphInfo.bearingX() * nextValue + textData.letterSpacing() / 2.0F;
                     float resolvedValue = candidateValue + glyphInfo.bearingY() * nextValue;
                     this.updateState7(
                        initialValue,
                        resolvedValue,
                        glyphInfo.width() * nextValue,
                        glyphInfo.height() * nextValue,
                        value,
                        candidateValue,
                        glyphInfo.u0(),
                        glyphInfo.v0(),
                        glyphInfo.u1(),
                        glyphInfo.v1(),
                        sourceValue,
                        targetValue,
                        inputValue,
                        outputValue
                     );
                     resultValue += glyphInfo.advance() * nextValue + textData.letterSpacing();
                  }
               }
            }
         }
      }
   }

   private void updateState5(TextData currentTextData, float[] floats, TextOperationHandler textOperation, boolean enabled) {
      int value = this.textOperationHandler == textOperation
            && this.enabled3 == enabled
            && currentTextData.equals(this.textData)
            && checkCondition(floats, this.float6)
         ? 0
         : 1;
      if (value != 0) {
         if (this.count7 > this.count8) {
            this.updateState6();
         }

         this.count8 = this.count7;
         this.textData = currentTextData;
         this.float6 = floats;
         this.textOperationHandler = textOperation;
         this.enabled3 = enabled;
      } else if (this.textOperationHandler == null) {
         this.textData = currentTextData;
         this.float6 = floats;
         this.textOperationHandler = textOperation;
         this.enabled3 = enabled;
      }
   }

   private void updateState6() {
      int value = this.count7 - this.count8;
      if (value > 0) {
         this.items
            .add(
               new TextTextService.Batch(this.count8, value, this.textData, this.float6, this.textOperationHandler, this.enabled3)
            );
      }
   }

   private boolean checkCondition6(int value) {
      if (value <= this.count6) {
         return true;
      }

      int index = this.count6;

      while (index < value) {
         index *= 2;
      }

      if (index > 32768) {
         if (!this.enabled2) {
            CoreIsInitializedHandler.LOGGER.warn("TextRenderer at glyph cap ({}); subsequent glyphs in this frame dropped", 32768);
            this.enabled2 = true;
         }

         return false;
      } else {
         float[] floats = new float[index * 4 * 12];
         System.arraycopy(this.float2, 0, floats, 0, this.count7 * 4 * 12);
         this.float2 = floats;
         float[] currentFloats = new float[index];
         float[] nextFloats = new float[index];
         System.arraycopy(this.float3, 0, currentFloats, 0, this.count7);
         System.arraycopy(this.float4, 0, nextFloats, 0, this.count7);
         this.float3 = currentFloats;
         this.float4 = nextFloats;
         this.float5 = new float[index * 4 * 12];
         if (this.enabled) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
            this.rhiOperationHandler.destroyBuffer(this.fighjurpy8);
            this.bufferHandle = this.rhiOperationHandler
               .createBuffer(index * 4L * 12L * 4L, RhiBlendStateService.BufferUsage.VERTEX.bit, RhiBlendStateService.BufferAccess.STREAM);
            int[] ints = new int[index * 6];

            for (int currentIndex = 0; currentIndex < index; currentIndex++) {
               int currentValue = currentIndex * 4;
               int nextIndex = currentIndex * 6;
               ints[nextIndex] = currentValue;
               ints[nextIndex + 1] = currentValue + 1;
               ints[nextIndex + 2] = currentValue + 2;
               ints[nextIndex + 3] = currentValue + 2;
               ints[nextIndex + 4] = currentValue + 3;
               ints[nextIndex + 5] = currentValue;
            }

            IntBuffer currentLength = MemoryUtil.memAllocInt(ints.length);
            currentLength.put(ints).flip();
            this.fighjurpy8 = this.rhiOperationHandler
               .createBuffer(
                  RhiBlendStateService.BufferUsage.INDEX.bit,
                  RhiBlendStateService.BufferAccess.STATIC,
                  MemoryUtil.memByteBuffer(MemoryUtil.memAddress(currentLength), ints.length * 4)
               );
            MemoryUtil.memFree(currentLength);
            this.count9 = -1;
         }

         this.count6 = index;
         return true;
      }
   }

   private void updateState7(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      float initialValue,
      float resolvedValue
   ) {
      int computedValue = this.count7 * 4 * 12;
      this.float3[this.count7] = sourceValue;
      this.float4[this.count7] = targetValue;
      this.updateState9(computedValue, value, currentValue, inputValue, outputValue, selectedValue, defaultValue, initialValue, resolvedValue, inputValue, outputValue, resultValue, candidateValue);
      this.updateState9(computedValue + 12, value + nextValue, currentValue, resultValue, outputValue, selectedValue, defaultValue, initialValue, resolvedValue, inputValue, outputValue, resultValue, candidateValue);
      this.updateState9(computedValue + 24, value + nextValue, currentValue + previousValue, resultValue, candidateValue, selectedValue, defaultValue, initialValue, resolvedValue, inputValue, outputValue, resultValue, candidateValue);
      this.updateState9(computedValue + 36, value, currentValue + previousValue, inputValue, candidateValue, selectedValue, defaultValue, initialValue, resolvedValue, inputValue, outputValue, resultValue, candidateValue);
      this.count7++;
   }

   private void updateState8(double doubleValue) {
      if (this.count9 != this.count7 || this.value != doubleValue) {
         int currentValue = this.count7 * 4 * 12;
         float[] floats = this.float5;
         float nextValue = (float)doubleValue;

         for (int index = 0; index < this.count7; index++) {
            int previousValue = index * 4 * 12;
            float sourceValue = this.float3[index] * nextValue;
            float targetValue = this.float4[index] * nextValue;
            float inputValue = calculateValue9(sourceValue) - sourceValue;
            float outputValue = calculateValue9(targetValue) - targetValue;

            for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
               int nextIndex = previousValue + currentIndex * 12;
               floats[nextIndex] = this.float2[nextIndex] * nextValue + inputValue;
               floats[nextIndex + 1] = this.float2[nextIndex + 1] * nextValue + outputValue;
               System.arraycopy(this.float2, nextIndex + 2, floats, nextIndex + 2, 10);
            }
         }

         this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, floats, currentValue);
         this.count9 = this.count7;
         this.value = doubleValue;
      }
   }

   private static float calculateValue9(float value) {
      return Math.round(value);
   }

   private void updateState9(
      int index,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue
   ) {
      this.float2[index] = value;
      this.float2[index + 1] = currentValue;
      this.float2[index + 2] = nextValue;
      this.float2[index + 3] = previousValue;
      this.float2[index + 4] = sourceValue;
      this.float2[index + 5] = targetValue;
      this.float2[index + 6] = inputValue;
      this.float2[index + 7] = outputValue;
      this.float2[index + 8] = resultValue;
      this.float2[index + 9] = candidateValue;
      this.float2[index + 10] = selectedValue;
      this.float2[index + 11] = defaultValue;
   }

   private static void updateState10(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
      rhiCommandBuffer.pushVec4(
         text,
         (value >> 16 & 0xFF) / 255.0F,
         (value >> 8 & 0xFF) / 255.0F,
         (value & 0xFF) / 255.0F,
         (value >> 24 & 0xFF) / 255.0F
      );
   }

   private void updateState11() {
      try {
         this.bufferHandle = this.rhiOperationHandler
            .createBuffer(this.count6 * 4L * 12L * 4L, RhiBlendStateService.BufferUsage.VERTEX.bit, RhiBlendStateService.BufferAccess.STREAM);
         int[] ints = new int[this.count6 * 6];

         for (int index = 0; index < this.count6; index++) {
            int value = index * 4;
            int currentIndex = index * 6;
            ints[currentIndex] = value;
            ints[currentIndex + 1] = value + 1;
            ints[currentIndex + 2] = value + 2;
            ints[currentIndex + 3] = value + 2;
            ints[currentIndex + 4] = value + 3;
            ints[currentIndex + 5] = value;
         }

         IntBuffer currentLength = MemoryUtil.memAllocInt(ints.length);
         currentLength.put(ints).flip();
         this.fighjurpy8 = this.rhiOperationHandler
            .createBuffer(
               RhiBlendStateService.BufferUsage.INDEX.bit,
               RhiBlendStateService.BufferAccess.STATIC,
               MemoryUtil.memByteBuffer(MemoryUtil.memAddress(currentLength), ints.length * 4)
            );
         MemoryUtil.memFree(currentLength);
         RhiBlendStateService.ShaderHandle shaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("msdf_text"));
         RhiBlendStateService.ShaderHandle currentShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("msdf_text"));
         RhiBlendStateService.ShaderHandle nextShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("color_text"));
         this.pipelineHandle = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(shaderHandle)
                  .fragment(currentShaderHandle)
                  .vertexLayout(
                     RhiBlendStateService.VertexLayout.of(
                        48,
                        new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L),
                        new RhiBlendStateService.VertexAttribute(1, 2, RhiBlendStateService.VertexFormat.FLOAT, 8L),
                        new RhiBlendStateService.VertexAttribute(2, 4, RhiBlendStateService.VertexFormat.FLOAT, 16L),
                        new RhiBlendStateService.VertexAttribute(3, 4, RhiBlendStateService.VertexFormat.FLOAT, 32L)
                     )
                  )
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build()
            );
         this.pipelineHandle2 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(shaderHandle)
                  .fragment(nextShaderHandle)
                  .vertexLayout(
                     RhiBlendStateService.VertexLayout.of(
                        48,
                        new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L),
                        new RhiBlendStateService.VertexAttribute(1, 2, RhiBlendStateService.VertexFormat.FLOAT, 8L),
                        new RhiBlendStateService.VertexAttribute(2, 4, RhiBlendStateService.VertexFormat.FLOAT, 16L),
                        new RhiBlendStateService.VertexAttribute(3, 4, RhiBlendStateService.VertexFormat.FLOAT, 32L)
                     )
                  )
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(shaderHandle);
         this.rhiOperationHandler.destroyShader(currentShaderHandle);
         this.rhiOperationHandler.destroyShader(nextShaderHandle);
         this.enabled = true;
         this.updateState12();
      } catch (Exception exception) {
         CoreIsInitializedHandler.LOGGER.error("Failed to init TextRenderer", exception);
      }
   }

   private void updateState12() {
      try {
         String text = "AaBbCc 0123 fi";
         GlyphAtlas glyphAtlas = this.createTextTextureService22(this.createTextTextureService(TextMode.REGULAR), TextMode.REGULAR);
         glyphAtlas.shapeLine(text);
         glyphAtlas.lineHeight();
      } catch (Throwable exception) {
         CoreIsInitializedHandler.LOGGER.warn("TextRenderer prewarm failed", exception);
      }
   }

   public void shutdown() {
      for (GlyphAtlas glyphAtlas : this.entries2.values()) {
         glyphAtlas.shutdown();
      }

      this.entries2.clear();

      for (TextTextureService textTexture : this.text2.values()) {
         textTexture.shutdown(this.rhiOperationHandler);
      }

      this.text2.clear();
      if (this.enabled) {
         this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
         this.rhiOperationHandler.destroyPipeline(this.pipelineHandle2);
         this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
         this.rhiOperationHandler.destroyBuffer(this.fighjurpy8);
      }
   }

   private record Batch(int startQuad, int quadCount, TextData style, float[] clip, TextOperationHandler atlas, boolean color) {
   }

   private record GlyphRun(TextOperationHandler atlas, TextTextureService.GlyphInfo glyph, boolean color) {
   }

   private record RuntimeFace(TextTextureService source, TextMode variant) {
   }
}

