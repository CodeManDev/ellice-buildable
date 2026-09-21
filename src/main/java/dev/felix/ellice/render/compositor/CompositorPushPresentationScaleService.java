package dev.felix.ellice.render.compositor;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.DiagnosticsAddFrameListenerService;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialPalette;
import dev.felix.ellice.ui.svg.SvgQueueService;
import dev.felix.ellice.ui.svg.SvgGetTextureService;
import dev.felix.ellice.ui.text.TextTextService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.theme.ThemeBlurQualityData;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class CompositorPushPresentationScaleService {
   private RhiOperationHandler rhiOperationHandler;
   private GlRenderer renderer;
   private RhiBlendStateService.SamplerHandle samplerHandle;
   private RhiBlendStateService.PipelineHandle pipelineHandle;
   private RhiBlendStateService.PipelineHandle pipelineHandle2;
   private RhiBlendStateService.PipelineHandle pipelineHandle3;
   private RhiBlendStateService.PipelineHandle pipelineHandle4;
   private RhiBlendStateService.PipelineHandle pipelineHandle5;
   private RhiBlendStateService.PipelineHandle pipelineHandle6;
   private RhiBlendStateService.PipelineHandle pipelineHandle7;
   private RhiBlendStateService.PipelineHandle pipelineHandle8;
   private RhiBlendStateService.PipelineHandle pipelineHandle9;
   private RhiBlendStateService.PipelineHandle pipelineHandle10;
   private RhiBlendStateService.PipelineHandle pipelineHandle11;
   private RhiBlendStateService.PipelineHandle pipelineHandle12;
   private RhiBlendStateService.PipelineHandle pipelineHandle13;
   private RhiBlendStateService.PipelineHandle pipelineHandle14;
   private RhiBlendStateService.PipelineHandle pipelineHandle15;
   private RhiBlendStateService.PipelineHandle pipelineHandle16;
   private RhiBlendStateService.PipelineHandle pipelineHandle17;
   private CompositorEmptyService compositorEmptyService;
   private static final String[] text2 = IntStream.range(0, 40).mapToObj(item -> "uPoints[" + item + "]").toArray(String[]::new);
   private CompositorAmplitudeService compositorAmplitudeService;
   private static final String[] text3 = IntStream.range(0, 128).mapToObj(item -> "uSamples[" + item + "]").toArray(String[]::new);
   private RhiBlendStateService.PipelineHandle pipelineHandle18;
   private boolean enabled2;
   private RhiBlendStateService.BufferHandle bufferHandle;
   private RhiBlendStateService.BufferHandle bufferHandle2;
   private int count;
   private float[] float2 = new float[10240];
   private CompositorShutdownService compositorShutdownService;
   private Map<TextMode, TextTextureService> entries;
   private TextTextureService textTextureService;
   private TextTextService textTextService;
   private ExecutorService executor;
   private final ConcurrentLinkedQueue<CompositorPushPresentationScaleService.PendingFont> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
   private final Set<String> text4 = ConcurrentHashMap.newKeySet();
   private CompositorRenderer renderer2;
   private SvgQueueService svgQueueService;
   private SvgGetTextureService svgGetTextureService;
   private boolean enabled3;
   private RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.FramebufferHandle framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
   private int count2;
   private int count3;
   private boolean enabled4;
   private RhiBlendStateService.TextureHandle textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.FramebufferHandle framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
   private int count4;
   private int count5;
   private RhiCommandBuffer rhiOperationHandler2;
   private RhiBlendStateService.FramebufferHandle framebufferHandle3 = RhiBlendStateService.FramebufferHandle.NONE;
   private int count6;
   private int count7;
   private int count8;
   private int count9;
   private long timestamp;
   private final long timestamp2 = System.nanoTime();
   private float value;
   private float value2;
   private boolean enabled5;
   private final Map<String, RhiBlendStateService.TextureHandle> text5 = new HashMap<>();
   private final List<CompositorPushPresentationScaleService.RectCmd> items = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.GlassCmd> items2 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.TexCmd> items3 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.SdfIconCmd> items4 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.GradCmd> items5 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.ColorPickerCmd> items6 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.CurveGraphCmd> items7 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.WaveformCmd> items8 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.TimeSeriesCmd> items9 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.RectCmd> items10 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.MeshCmd> items11 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.LiquidSwitchCmd> items12 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.TooltipBubbleCmd> items13 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.LoadingCmd> items14 = new ArrayList<>();
   private boolean enabled6;
   private float value3;
   private int count10;
   private final List<Integer> items15 = new ArrayList<>();
   private final List<Integer> items16 = new ArrayList<>();
   private final List<Integer> items17 = new ArrayList<>();
   private final List<Integer> items18 = new ArrayList<>();
   private final List<Integer> items19 = new ArrayList<>();
   private final List<Integer> items20 = new ArrayList<>();
   private final List<Integer> items21 = new ArrayList<>();
   private final List<Integer> items22 = new ArrayList<>();
   private final List<Integer> items23 = new ArrayList<>();
   private final List<Integer> items24 = new ArrayList<>();
   private final List<Integer> items25 = new ArrayList<>();
   private final List<Integer> items26 = new ArrayList<>();
   private final List<Integer> items27 = new ArrayList<>();
   private final List<Integer> items28 = new ArrayList<>();
   private final List<Integer> items29 = new ArrayList<>();
   private final ArrayList<Boolean> items30 = new ArrayList<>();
   private final List<CompositorPushPresentationScaleService.LayerEffect> items31 = new ArrayList<>();
   private final Map<Integer, CompositorPushPresentationScaleService.LayerCache> entries2 = new HashMap<>();
   private final List<Runnable> items32 = new ArrayList<>();
   private final ArrayList<float[]> items33 = new ArrayList<>();
   private float[] float3;
   private final ArrayList<Float> items34 = new ArrayList<>();
   private float value4 = 1.0F;
   public static final int CURVE_HANDLE_NONE = 0;
   public static final int CURVE_HANDLE_FIRST = 1;
   public static final int CURVE_HANDLE_SECOND = 2;
   public static final CompositorPushPresentationScaleService.CurveGraphStyle DEFAULT_CURVE_GRAPH_STYLE = new CompositorPushPresentationScaleService.CurveGraphStyle(
      -65923812,
      956301311,
      352321535,
      788529151,
      -9253131,
      645780476,
      343790588,
      1560281087,
      -722693,
      -4330497,
      -234090474,
      -9253131,
      1.35F,
      2.4F
   );
   private static final int count11 = 40;
   private int count12;
   private int count13;

   public void pushPresentationScale(float scale) {
      this.items34.add(this.value4);
      this.value4 = Float.isFinite(scale) ? Math.max(0.0F, scale) : 1.0F;
   }

   public void popPresentationScale() {
      this.value4 = this.items34.isEmpty() ? 1.0F : this.items34.remove(this.items34.size() - 1);
   }

   private float calculateValue(float value) {
      return value * this.value4;
   }

   public void timeSeries(float value, float currentValue, float nextValue, float previousValue, CompositorEmptyService compositorEmpty, int sourceValue, float targetValue) {
      if (compositorEmpty != null
         && Float.isFinite(value)
         && Float.isFinite(currentValue)
         && Float.isFinite(nextValue)
         && Float.isFinite(previousValue)
         && Float.isFinite(targetValue)
         && !(nextValue <= 0.0F)
         && !(previousValue <= 0.0F)
         && !(targetValue <= 0.001F)) {
         this.items9
            .add(
               new CompositorPushPresentationScaleService.TimeSeriesCmd(
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  compositorEmpty,
                  sourceValue,
                  Math.min(1.0F, targetValue),
                  Math.max(1.0E-4F, this.value4),
                  this.items.size(),
                  this.float3
               )
            );
      }
   }

   int queuedTimeSeriesCount() {
      return this.items9.size();
   }

   int queuedTimeSeriesRectIndex(int x) {
      return this.items9.get(x).rectIdx;
   }

   float[] queuedTimeSeriesClip(int x) {
      return this.items9.get(x).clip;
   }

   public void waveform(
      float value, float currentValue, float nextValue, float previousValue, CompositorAmplitudeService compositorAmplitude, float sourceValue, float targetValue, boolean enabled, boolean currentEnabled, float inputValue
   ) {
      this.waveform(value, currentValue, nextValue, previousValue, compositorAmplitude, sourceValue, targetValue, enabled, currentEnabled, inputValue, -9513222, -11705470, -1508609);
   }

   public void waveform(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      CompositorAmplitudeService compositorAmplitude,
      float sourceValue,
      float targetValue,
      boolean enabled,
      boolean currentEnabled,
      float inputValue,
      int outputValue,
      int resultValue,
      int candidateValue
   ) {
      if (compositorAmplitude != null
         && Float.isFinite(value)
         && Float.isFinite(currentValue)
         && Float.isFinite(nextValue)
         && Float.isFinite(previousValue)
         && Float.isFinite(sourceValue)
         && Float.isFinite(targetValue)
         && Float.isFinite(inputValue)
         && !(nextValue <= 0.0F)
         && !(previousValue <= 0.0F)
         && !(inputValue <= 0.001F)) {
         this.items8
            .add(
               new CompositorPushPresentationScaleService.WaveformCmd(
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  compositorAmplitude,
                  Math.max(0.0F, Math.min(1.0F, sourceValue)),
                  Math.max(-1.0F, Math.min(1.0F, targetValue)),
                  enabled,
                  currentEnabled,
                  Math.min(1.0F, inputValue),
                  outputValue,
                  resultValue,
                  candidateValue,
                  Math.max(1.0E-4F, this.value4),
                  this.items.size(),
                  this.float3
               )
            );
      }
   }

   int queuedWaveformCount() {
      return this.items8.size();
   }

   int queuedWaveformRectIndex(int x) {
      return this.items8.get(x).rectIdx;
   }

   float[] queuedWaveformClip(int x) {
      return this.items8.get(x).clip;
   }

   public void roundedRect(float x, float y, float width, float height, float value, int currentValue) {
      this.updateState3(x, y, width, height, value, value, value, value, currentValue, 0.0F, 0.0F, 1073741824, 0.0F, 0);
   }

   public void roundedRect(float x, float y, float width, float height, float value, int currentValue, float nextValue) {
      this.updateState3(x, y, width, height, value, value, value, value, currentValue, nextValue, 0.0F, 1073741824, 0.0F, 0);
   }

   public void roundedRect(float x, float y, float width, float height, float value, int currentValue, float nextValue, float previousValue) {
      this.updateState3(x, y, width, height, value, value, value, value, currentValue, nextValue, previousValue, 1073741824, 0.0F, 0);
   }

   public void roundedRect(float x, float y, float width, float height, float value, float currentValue, float nextValue, float previousValue, int sourceValue) {
      this.updateState3(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, 0.0F, 0.0F, 1073741824, 0.0F, 0);
   }

   public void roundedRect(
      float x, float y, float width, float height, float value, float currentValue, float nextValue, float previousValue, int sourceValue, float targetValue
   ) {
      this.updateState3(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, 0.0F, 1073741824, 0.0F, 0);
   }

   public void roundedRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue
   ) {
      this.updateState3(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue);
   }

   public void roundedRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue,
      float selectedValue
   ) {
      this.updateState4(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue, selectedValue, 0.0F);
   }

   public void roundedRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue,
      float selectedValue,
      float defaultValue
   ) {
      this.updateState4(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue, selectedValue, defaultValue);
   }

   public void gradientRect(float x, float y, float width, float height, int value, int currentValue) {
      this.items5.add(new CompositorPushPresentationScaleService.GradCmd(x, y, width, height, value, currentValue, this.float3));
   }

   public void curveGraph(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      ModuleSetting.CurveValue curveValue,
      int candidateValue,
      float selectedValue,
      float defaultValue,
      CompositorPushPresentationScaleService.CurveGraphStyle curveGraphStyle
   ) {
      if (curveValue != null
         && checkCondition6(value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, selectedValue, defaultValue)
         && !(nextValue <= 0.0F)
         && !(previousValue <= 0.0F)
         && !(inputValue <= 0.0F)
         && !(outputValue <= 0.0F)
         && !(selectedValue <= 0.001F)) {
         float initialValue = Math.max(value, sourceValue);
         float resolvedValue = Math.max(currentValue, targetValue);
         float computedValue = Math.min(value + nextValue, sourceValue + inputValue);
         float cachedValue = Math.min(currentValue + previousValue, targetValue + outputValue);
         float pendingValue = computedValue - initialValue;
         float activeValue = cachedValue - resolvedValue;
         if (!(pendingValue <= 0.0F) && !(activeValue <= 0.0F)) {
            CompositorPushPresentationScaleService.CurveGraphStyle currentCurveGraphStyle = curveGraphStyle != null ? curveGraphStyle : DEFAULT_CURVE_GRAPH_STYLE;
            this.items7
               .add(
                  new CompositorPushPresentationScaleService.CurveGraphCmd(
                     value,
                     currentValue,
                     nextValue,
                     previousValue,
                     initialValue,
                     resolvedValue,
                     pendingValue,
                     activeValue,
                     Math.max(0.0F, this.calculateValue(resultValue)),
                     curveValue,
                     Math.max(0, Math.min(2, candidateValue)),
                     Math.max(0.0F, Math.min(1.0F, selectedValue)),
                     Math.max(0.0F, this.calculateValue(defaultValue)),
                     Math.max(1.0E-4F, this.value4),
                     this.calculateValue(currentCurveGraphStyle.curveWidth()),
                     this.calculateValue(currentCurveGraphStyle.curveFringeWidth()),
                     currentCurveGraphStyle,
                     this.items.size(),
                     this.float3
                  )
               );
         }
      }
   }

   int queuedCurveGraphCount() {
      return this.items7.size();
   }

   int queuedCurveGraphRectIndex(int x) {
      return this.items7.get(x).rectIdx;
   }

   float[] queuedCurveGraphClip(int x) {
      return this.items7.get(x).clip;
   }

   float[] queuedCurveGraphPlot(int value) {
      CompositorPushPresentationScaleService.CurveGraphCmd curveGraphCmd = this.items7.get(value);
      return new float[]{curveGraphCmd.plotX, curveGraphCmd.plotY, curveGraphCmd.plotW, curveGraphCmd.plotH};
   }

   public void addMesh(
      float value, float currentValue, float nextValue, float previousValue, float sourceValue, int targetValue, int inputValue, int outputValue, int resultValue, int candidateValue, float selectedValue
   ) {
      this.items11
         .add(
            new CompositorPushPresentationScaleService.MeshCmd(
               value,
               currentValue,
               nextValue,
               previousValue,
               this.calculateValue(sourceValue),
               targetValue,
               inputValue,
               outputValue,
               resultValue,
               candidateValue,
               selectedValue,
               this.items.size(),
               this.float3,
               null,
               null
            )
         );
   }

   public void studioPrimitive(
      float value, float currentValue, float nextValue, float previousValue, float sourceValue, int targetValue, int inputValue, float outputValue, CompositorPushPresentationScaleService.StudioPaint studioPaint
   ) {
      if (studioPaint != null
         && studioPaint.kind >= 0
         && studioPaint.kind <= 6
         && Float.isFinite(value)
         && Float.isFinite(currentValue)
         && Float.isFinite(nextValue)
         && Float.isFinite(previousValue)
         && Float.isFinite(sourceValue)
         && Float.isFinite(outputValue)
         && Float.isFinite(studioPaint.time)
         && Float.isFinite(studioPaint.a)
         && Float.isFinite(studioPaint.b)
         && Float.isFinite(studioPaint.c)
         && Float.isFinite(studioPaint.d)
         && !(nextValue <= 0.0F)
         && !(previousValue <= 0.0F)
         && !(outputValue <= 0.001F)) {
         this.items11
            .add(
               new CompositorPushPresentationScaleService.MeshCmd(
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  this.calculateValue(Math.max(0.0F, sourceValue)),
                  targetValue,
                  inputValue,
                  0,
                  0,
                  0,
                  Math.min(1.0F, outputValue),
                  this.items.size(),
                  this.float3,
                  studioPaint,
                  null
               )
            );
      }
   }

   public void menuBackground(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue) {
      this.menuBackground(value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, 0.0F, 0.0F);
   }

   public void menuBackground(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue, float outputValue, float resultValue) {
      this.menuBackground(value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, 0.0F);
   }

   public void menuBackground(
      float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue, float outputValue, float resultValue, float candidateValue
   ) {
      if (Float.isFinite(value)
         && Float.isFinite(currentValue)
         && Float.isFinite(nextValue)
         && Float.isFinite(previousValue)
         && Float.isFinite(sourceValue)
         && Float.isFinite(targetValue)
         && Float.isFinite(inputValue)
         && Float.isFinite(outputValue)
         && Float.isFinite(resultValue)
         && Float.isFinite(candidateValue)
         && !(nextValue <= 0.0F)
         && !(previousValue <= 0.0F)
         && !(inputValue <= 0.0F)) {
         if (!ThemeBlurQualityData.current().animatedBackground()) {
            this.addRect(
               value,
               currentValue,
               nextValue,
               previousValue,
               sourceValue,
               sourceValue,
               sourceValue,
               sourceValue,
               MaterialIsLightService.SURFACE,
               0.0F,
               0.0F,
               0,
               0.0F,
               0,
               inputValue,
               0.0F,
               false,
               MaterialIsLightService.SURFACE_CONTAINER
            );
         } else {
            MaterialPalette materialPalette = MaterialPalette.current();
            this.items11
               .add(
                  new CompositorPushPresentationScaleService.MeshCmd(
                     value,
                     currentValue,
                     nextValue,
                     previousValue,
                     this.calculateValue(Math.max(0.0F, sourceValue)),
                     materialPalette.base(),
                     materialPalette.first(),
                     materialPalette.second(),
                     materialPalette.third(),
                     materialPalette.fourth(),
                     Math.min(1.0F, inputValue),
                     this.items.size(),
                     this.float3,
                     null,
                     new CompositorPushPresentationScaleService.MenuPaint(targetValue, outputValue, resultValue, materialPalette.light(), candidateValue)
                  )
               );
         }
      }
   }

   public void glassRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      int targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      float initialValue,
      float resolvedValue,
      float computedValue,
      int cachedValue,
      int pendingValue,
      int activeValue,
      float fallbackValue
   ) {
      this.glassRect(
         x,
         y,
         width,
         height,
         value,
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         targetValue,
         inputValue,
         outputValue,
         resultValue,
         candidateValue,
         selectedValue,
         defaultValue,
         initialValue,
         resolvedValue,
         computedValue,
         cachedValue,
         pendingValue,
         activeValue,
         fallbackValue,
         0.0F,
         72.0F,
         -1.0F,
         0.0F,
         0.0F,
         1.0F
      );
   }

   public void glassRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      int targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      float initialValue,
      float resolvedValue,
      float computedValue,
      int cachedValue,
      int pendingValue,
      int activeValue,
      float fallbackValue,
      float primaryValue,
      float secondaryValue
   ) {
      this.glassRect(
         x,
         y,
         width,
         height,
         value,
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         targetValue,
         inputValue,
         outputValue,
         resultValue,
         candidateValue,
         selectedValue,
         defaultValue,
         initialValue,
         resolvedValue,
         computedValue,
         cachedValue,
         pendingValue,
         activeValue,
         fallbackValue,
         primaryValue,
         secondaryValue,
         -1.0F,
         0.0F,
         0.0F,
         1.0F
      );
   }

   public void glassRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      int targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      float initialValue,
      float resolvedValue,
      float computedValue,
      int cachedValue,
      int pendingValue,
      int activeValue,
      float fallbackValue,
      float primaryValue,
      float secondaryValue,
      float tertiaryValue,
      float temporaryValue,
      float requestedValue,
      float actualValue
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(outputValue <= 0.0F)) {
         if (!ThemeBlurQualityData.current().advancedGlass()) {
            this.addRect(
               x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, inputValue, 0.0F, 0, computedValue, cachedValue, outputValue, fallbackValue, false, targetValue, pendingValue
            );
         } else {
            this.items2
               .add(
                  new CompositorPushPresentationScaleService.GlassCmd(
                     x,
                     y,
                     width,
                     height,
                     this.calculateValue(value),
                     this.calculateValue(currentValue),
                     this.calculateValue(nextValue),
                     this.calculateValue(previousValue),
                     sourceValue,
                     targetValue,
                     this.calculateValue(ThemeBlurQualityData.current().blur(inputValue)),
                     outputValue,
                     resultValue,
                     candidateValue,
                     selectedValue,
                     Math.max(0.0F, this.calculateValue(defaultValue)),
                     Math.max(0.0F, initialValue),
                     Math.max(0.0F, this.calculateValue(resolvedValue)),
                     Math.max(0.0F, this.calculateValue(computedValue)),
                     cachedValue,
                     pendingValue,
                     activeValue,
                     Math.max(0.0F, this.calculateValue(fallbackValue)),
                     Math.max(0.0F, this.calculateValue(primaryValue)),
                     Math.max(this.value4, this.calculateValue(secondaryValue)),
                     tertiaryValue,
                     temporaryValue,
                     this.calculateValue(requestedValue),
                     Math.max(1.0F, actualValue),
                     this.items.size(),
                     this.float3
                  )
               );
            this.enabled6 = true;
            this.updateState();
         }
      }
   }

   public void addLiquidSwitch(
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
      int initialValue
   ) {
      if (!(nextValue <= 0.0F) && !(previousValue <= 0.0F) && !(defaultValue <= 0.0F)) {
         this.items12
            .add(
               new CompositorPushPresentationScaleService.LiquidSwitchCmd(
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  this.calculateValue(sourceValue),
                  targetValue,
                  inputValue,
                  outputValue,
                  resultValue,
                  Math.max(0.0F, Math.min(1.0F, candidateValue)),
                  Math.max(0.0F, Math.min(1.0F, selectedValue)),
                  defaultValue,
                  initialValue,
                  this.items.size(),
                  this.float3
               )
            );
      }
   }

   public void addTooltipBubble(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      int candidateValue,
      int selectedValue,
      float defaultValue,
      int initialValue,
      float resolvedValue,
      int computedValue,
      float cachedValue,
      float pendingValue,
      boolean enabled,
      float activeValue,
      float fallbackValue
   ) {
      if (!(nextValue <= 0.0F) && !(previousValue <= 0.0F) && !(activeValue <= 0.0F)) {
         enabled = enabled && ThemeBlurQualityData.current().blurEnabled();
         this.items13
            .add(
               new CompositorPushPresentationScaleService.TooltipBubbleCmd(
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  Math.max(0.0F, this.calculateValue(sourceValue)),
                  targetValue,
                  Math.max(0.0F, this.calculateValue(inputValue)),
                  Math.max(0.0F, this.calculateValue(outputValue)),
                  Math.max(0.5F * this.value4, this.calculateValue(resultValue)),
                  candidateValue,
                  selectedValue,
                  this.calculateValue(ThemeBlurQualityData.current().shadow(defaultValue)),
                  ThemeBlurQualityData.current().shadowColor(initialValue),
                  Math.max(0.0F, this.calculateValue(resolvedValue)),
                  computedValue,
                  Math.max(0.0F, Math.min(1.0F, cachedValue)),
                  Math.max(0.0F, this.calculateValue(pendingValue)),
                  (boolean)enabled,
                  Math.max(0.0F, Math.min(1.0F, activeValue)),
                  Math.max(0.0F, this.calculateValue(fallbackValue)),
                  this.items.size(),
                  this.float3
               )
            );
         if (enabled) {
            this.enabled6 = true;
            this.updateState();
         }
      }
   }

   public void colorField(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue) {
      this.colorField(value, currentValue, nextValue, previousValue, sourceValue, targetValue, 1.0F);
   }

   public void colorField(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue) {
      if (!(inputValue <= 0.001F)) {
         this.items6
            .add(
               new CompositorPushPresentationScaleService.ColorPickerCmd(
                  value, currentValue, nextValue, previousValue, sourceValue, 1.0F, 1.0F, this.calculateValue(targetValue), 0, inputValue, this.float3
               )
            );
      }
   }

   public void hueBar(float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      this.hueBar(value, currentValue, nextValue, previousValue, sourceValue, 1.0F);
   }

   public void hueBar(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue) {
      if (!(targetValue <= 0.001F)) {
         this.items6
            .add(
               new CompositorPushPresentationScaleService.ColorPickerCmd(
                  value, currentValue, nextValue, previousValue, 0.0F, 1.0F, 1.0F, this.calculateValue(sourceValue), 1, targetValue, this.float3
               )
            );
      }
   }

   public void alphaBar(float opacity, float currentOpacity, float nextOpacity, float previousOpacity, float sourceOpacity, float targetOpacity, float inputOpacity, float outputOpacity, float resultOpacity) {
      if (!(resultOpacity <= 0.001F)) {
         this.items6
            .add(
               new CompositorPushPresentationScaleService.ColorPickerCmd(
                  opacity, currentOpacity, nextOpacity, previousOpacity, sourceOpacity, targetOpacity, inputOpacity, this.calculateValue(outputOpacity), 4, resultOpacity, this.float3
               )
            );
      }
   }

   public void colorWheel(float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      this.items6.add(new CompositorPushPresentationScaleService.ColorPickerCmd(value, currentValue, nextValue, previousValue, sourceValue, 1.0F, 1.0F, 0.0F, 2, 1.0F, this.float3));
   }

   public void colorTriangle(float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      this.colorTriangle(value, currentValue, nextValue, previousValue, sourceValue, 1.0F);
   }

   public void colorTriangle(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue) {
      if (!(targetValue <= 0.001F)) {
         this.items6.add(new CompositorPushPresentationScaleService.ColorPickerCmd(value, currentValue, nextValue, previousValue, sourceValue, 1.0F, 1.0F, 0.0F, 3, targetValue, this.float3));
      }
   }

   public void overlayRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue
   ) {
      targetValue = ThemeBlurQualityData.current().blur(targetValue);
      inputValue = ThemeBlurQualityData.current().shadow(inputValue);
      outputValue = ThemeBlurQualityData.current().shadowColor(outputValue);
      float selectedValue = (sourceValue >> 24 & 0xFF) / 255.0F;
      float defaultValue = (sourceValue >> 16 & 0xFF) / 255.0F;
      float initialValue = (sourceValue >> 8 & 0xFF) / 255.0F;
      float resolvedValue = (sourceValue & 0xFF) / 255.0F;
      this.items10
         .add(
            new CompositorPushPresentationScaleService.RectCmd(
               x,
               y,
               width,
               height,
               this.calculateValue(value),
               this.calculateValue(currentValue),
               this.calculateValue(nextValue),
               this.calculateValue(previousValue),
               defaultValue,
               initialValue,
               resolvedValue,
               selectedValue,
               this.calculateValue(targetValue),
               this.calculateValue(inputValue),
               outputValue,
               this.calculateValue(resultValue),
               candidateValue,
               1.0F,
               0.0F,
               false,
               0.0F,
               0.0F,
               0.0F,
               0.0F,
               0,
               0.0F,
               0.0F,
               this.float3
            )
         );
      if (targetValue > 0.0F) {
         this.enabled6 = true;
         this.updateState();
      }
   }

   public void afterFlush(Runnable runnable) {
      this.items32.add(runnable);
   }

   public void nextLayer() {
      this.nextLayer(null);
   }

   public void nextLayer(CompositorPushPresentationScaleService.LayerEffect layerEffect) {
      this.items15.add(this.items.size());
      this.items16.add(this.items2.size());
      this.items17.add(this.items3.size());
      this.items18.add(this.items4.size());
      this.items19.add(this.textTextService != null ? this.textTextService.batchCount() : 0);
      this.items20.add(this.items5.size());
      this.items26.add(this.items11.size());
      this.items21.add(this.items6.size());
      this.items22.add(this.items7.size());
      this.items23.add(this.items8.size());
      this.items24.add(this.items9.size());
      this.items25.add(this.items10.size());
      this.items29.add(this.svgQueueService != null ? this.svgQueueService.cmdCount() : 0);
      this.items27.add(this.items12.size());
      this.items28.add(this.items13.size());
      this.count10++;

      while (this.items31.size() <= this.count10) {
         this.items31.add(null);
      }

      this.items31.set(this.count10, layerEffect);

      while (this.items30.size() <= this.count10) {
         this.items30.add(Boolean.FALSE);
      }
   }

   private void updateState() {
      while (this.items30.size() <= this.count10) {
         this.items30.add(Boolean.FALSE);
      }

      this.items30.set(this.count10, Boolean.TRUE);
   }

   private boolean checkCondition(int index) {
      return index >= 0 && index < this.items30.size() && this.items30.get(index);
   }

   public boolean isLayerCacheValid(int value, int currentValue, float nextValue, float previousValue, float sourceValue, float targetValue) {
      if (value != 0 && this.count8 > 0 && this.count9 > 0) {
         CompositorPushPresentationScaleService.LayerCache layerCache = this.entries2.get(value);
         return layerCache != null
            && layerCache.texture.valid()
            && layerCache.version == currentValue
            && layerCache.effectsRevision == ThemeBlurQualityData.revision()
            && layerCache.textRevision == this.fontMetricsRevision()
            && layerCache.fbW == this.count8
            && layerCache.fbH == this.count9
            && checkCondition2(layerCache, nextValue, previousValue, sourceValue, targetValue);
      } else {
         return false;
      }
   }

   public void rect(float x, float y, float width, float height, int value) {
      this.updateState3(x, y, width, height, 0.0F, 0.0F, 0.0F, 0.0F, value, 0.0F, 0.0F, 1073741824, 0.0F, 0);
   }

   private String createText(String text) {
      return this.count12 == 0 ? PrivacyRevisionService.replace(text) : text;
   }

   public void textLiteral(float value, float currentValue, String currentText, float nextValue, int previousValue, TextData textData) {
      this.count12++;

      try {
         this.text(value, currentValue, currentText, nextValue, previousValue, textData);
      } finally {
         this.count12--;
      }
   }

   public float textWidthLiteral(String id, float value, TextMode textMode, String currentId, float currentValue) {
      this.count12++;

      try {
         return this.textWidth(id, value, textMode, currentId, currentValue);
      } finally {
         this.count12--;
      }
   }

   public void text(float value, float currentValue, String currentText, float nextValue, int previousValue) {
      if (this.textTextService != null) {
         this.textTextService.text(value, currentValue, this.createText(currentText), this.calculateValue(nextValue), previousValue, TextData.NONE, this.float3);
      }
   }

   public void text(float value, float currentValue, String currentText, float nextValue, int previousValue, TextData textData) {
      if (this.textTextService != null) {
         this.textTextService
            .text(value, currentValue, this.createText(currentText), this.calculateValue(nextValue), previousValue, this.createTextData(textData), this.float3);
      }
   }

   public float textWidth(String id, float value) {
      return this.textTextService != null ? this.textTextService.measureWidth(this.createText(id), this.calculateValue(value)) : 0.0F;
   }

   public float textWidth(String id, float value, TextMode textMode) {
      return this.textTextService != null ? this.textTextService.measureWidth(this.createText(id), this.calculateValue(value), textMode) : 0.0F;
   }

   public float textWidth(String id, float value, TextMode textMode, String currentId) {
      return this.textTextService != null ? this.textTextService.measureWidth(this.createText(id), this.calculateValue(value), textMode, currentId) : 0.0F;
   }

   private TextData createTextData(TextData textData) {
      TextData currentTextData = textData != null ? textData : TextData.NONE;
      return this.value4 != 1.0F || currentTextData.hasShadow() && ThemeBlurQualityData.current().shadowStrength() != 1.0F
         ? new TextData(
            this.calculateValue(currentTextData.shadowX()),
            this.calculateValue(currentTextData.shadowY()),
            ThemeBlurQualityData.current().shadowColor(currentTextData.shadowColor()),
            this.calculateValue(currentTextData.outlineWidth()),
            currentTextData.outlineColor(),
            currentTextData.variant(),
            currentTextData.fontFamily(),
            this.calculateValue(currentTextData.edgeSoftness()),
            this.calculateValue(currentTextData.letterSpacing())
         )
         : currentTextData;
   }

   public float textWidth(String id, float value, TextMode textMode, String currentId, float currentValue) {
      id = this.createText(id);
      if (currentValue == 0.0F) {
         return this.textWidth(id, value, textMode, currentId);
      }

      if (id == null || id.isEmpty()) {
         return 0.0F;
      }

      if (this.textTextService == null) {
         float nextValue = 0.0F;

         for (String nextId : id.split("\n", -1)) {
            nextValue = Math.max(
               nextValue,
               this.textWidth(nextId, value, textMode, currentId) + TextTextService.trackingAdvance(nextId, nextId.length(), this.calculateValue(currentValue))
            );
         }

         return nextValue;
      } else {
         float previousValue = 0.0F;

         for (String previousId : id.split("\n", -1)) {
            previousValue = Math.max(previousValue, this.textTextService.measureWidth(previousId, this.calculateValue(value), textMode, currentId, this.calculateValue(currentValue)));
         }

         return previousValue;
      }
   }

   public int fontMetricsRevision() {
      return this.count13 + (this.textTextService != null ? 1 : 0) + PrivacyRevisionService.revision();
   }

   public boolean isFontFamilyReady(String text) {
      return this.textTextService != null && this.textTextService.hasFontFamily(text);
   }

   public boolean isFontFamilyLoading(String text) {
      return this.text4.contains(text);
   }

   public void requestFontFamily(String text, TextTextureService.FontSource fontSource, String currentText) {
      if (text != null && currentText != null) {
         if (this.textTextService == null || !this.textTextService.hasFontFamily(text)) {
            if (this.text4.add(text)) {
               if (this.executor == null) {
                  this.executor = Executors.newSingleThreadExecutor(item -> {
                     Thread thread = new Thread(item, "ellice-font-loader");
                     thread.setDaemon(true);
                     return thread;
                  });
               }

               this.executor
                  .submit(
                     () -> {
                        try {
                           TextTextureService textTexture = fontSource == TextTextureService.FontSource.CLASSPATH
                              ? new TextTextureService(TextMode.REGULAR, (String)null, currentText)
                              : new TextTextureService(TextMode.REGULAR, fontSource, currentText);
                           textTexture.prepare();
                           this.concurrentLinkedQueue.add(new CompositorPushPresentationScaleService.PendingFont(text, textTexture));
                        } catch (Throwable exception) {
                           CoreIsInitializedHandler.LOGGER.error("Font rasterize failed for {} ({})", new Object[]{text, currentText, exception});
                           this.text4.remove(text);
                        }
                     }
                  );
            }
         }
      }
   }

   private void updateState2() {
      if (!this.concurrentLinkedQueue.isEmpty() && this.textTextService != null) {
         CompositorPushPresentationScaleService.PendingFont pendingFont;
         while ((pendingFont = this.concurrentLinkedQueue.poll()) != null) {
            try {
               pendingFont.atlas.uploadStaged(this.rhiOperationHandler);
               this.textTextService.registerFontFamily(pendingFont.key, pendingFont.atlas);
               this.count13++;
               CoreIsInitializedHandler.LOGGER.info("Font family ready: {}", pendingFont.key);
            } catch (Exception exception) {
               CoreIsInitializedHandler.LOGGER.error("Font upload failed for {}", pendingFont.key, exception);
            } finally {
               this.text4.remove(pendingFont.key);
            }
         }
      }
   }

   public void addLoading(float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, int inputValue, float outputValue) {
      this.items14.add(new CompositorPushPresentationScaleService.LoadingCmd(value, currentValue, nextValue, previousValue, this.calculateValue(sourceValue), targetValue, inputValue, outputValue));
   }

   public float textLineHeight(float value, TextMode textMode, String text) {
      if (text == null) {
         return this.textLineHeight(value, textMode);
      } else {
         return this.textTextService != null ? this.textTextService.lineHeight(this.calculateValue(value), textMode, text) : this.calculateValue(value);
      }
   }

   public float textLineHeight(float value) {
      return this.textTextService != null ? this.textTextService.lineHeight(this.calculateValue(value)) : this.calculateValue(value);
   }

   public float textLineHeight(float value, TextMode textMode) {
      return this.textTextService != null ? this.textTextService.lineHeight(this.calculateValue(value), textMode) : this.calculateValue(value);
   }

   private PrivacyRevisionService.Mapping createMap(String text) {
      return this.count12 == 0 ? PrivacyRevisionService.map(text) : new PrivacyRevisionService.Mapping(text, text, List.of());
   }

   public float textCaretX(String currentText, int value, float currentValue, TextMode textMode, String nextText) {
      PrivacyRevisionService.Mapping mapping = this.createMap(currentText);
      return this.textTextService == null
         ? 0.0F
         : this.textTextService.caretX(mapping.text(), mapping.displayOffset(value), this.calculateValue(currentValue), textMode, nextText);
   }

   public float textCaretX(String currentText, int value, float currentValue, TextMode textMode, String nextText, float nextValue) {
      PrivacyRevisionService.Mapping mapping = this.createMap(currentText);
      int previousValue = mapping.displayOffset(value);
      this.count12++;

      try {
         return this.textCaretX(mapping.text(), previousValue, currentValue, textMode, nextText)
            + TextTextService.trackingAdvance(mapping.text(), previousValue, this.calculateValue(nextValue));
      } finally {
         this.count12--;
      }
   }

   public float[] textSelectionSpans(String text, int value, int currentValue, float nextValue, TextMode textMode, String currentText, float previousValue) {
      if (previousValue != 0.0F
         && text != null
         && TextTextService.trackingAdvance(this.createText(text), this.createText(text).length(), previousValue) != 0.0F) {
         int currentLength = Math.max(0, Math.min(text.length(), Math.min(value, currentValue)));
         int nextLength = Math.max(currentLength, Math.min(text.length(), Math.max(value, currentValue)));
         if (currentLength == nextLength) {
            return new float[0];
         }

         float sourceValue = this.textCaretX(text, currentLength, nextValue, textMode, currentText, previousValue);
         float targetValue = this.textCaretX(text, nextLength, nextValue, textMode, currentText, previousValue);
         return new float[]{sourceValue, targetValue - sourceValue};
      } else {
         return this.textSelectionSpans(text, value, currentValue, nextValue, textMode, currentText);
      }
   }

   public float[] textSelectionSpans(String currentText, int value, int currentValue, float nextValue, TextMode textMode, String nextText) {
      PrivacyRevisionService.Mapping mapping = this.createMap(currentText);
      return this.textTextService == null
         ? new float[0]
         : this.textTextService
            .selectionSpans(mapping.text(), mapping.displayOffset(value), mapping.displayOffset(currentValue), this.calculateValue(nextValue), textMode, nextText);
   }

   public float textCaretX(String text, int value, float currentValue) {
      return this.textCaretX(text, value, currentValue, TextMode.REGULAR, null, 0.0F);
   }

   public float textCaretX(String text, int value, float currentValue, TextMode textMode) {
      return this.textCaretX(text, value, currentValue, textMode, null, 0.0F);
   }

   public float[] textSelectionSpans(String text, int value, int currentValue, float nextValue) {
      return this.textSelectionSpans(text, value, currentValue, nextValue, TextMode.REGULAR, null, 0.0F);
   }

   public float[] textSelectionSpans(String text, int value, int currentValue, float nextValue, TextMode textMode) {
      return this.textSelectionSpans(text, value, currentValue, nextValue, textMode, null, 0.0F);
   }

   public void drawTexture(RhiBlendStateService.TextureHandle textureHandle, float y, float width, float height, float value, float currentValue, float nextValue) {
      this.drawTexture(textureHandle, y, width, height, value, currentValue, nextValue, 0);
   }

   public void drawTexture(
      RhiBlendStateService.TextureHandle textureHandle, float y, float width, float height, float value, float currentValue, float nextValue, int textureId
   ) {
      this.drawTexture(textureHandle, y, width, height, value, currentValue, nextValue, textureId, 0.0F);
   }

   public void drawTexture(
      RhiBlendStateService.TextureHandle textureHandle, float y, float width, float height, float value, float currentValue, float nextValue, int textureId, float previousValue
   ) {
      this.drawTexture(textureHandle, y, width, height, value, currentValue, nextValue, nextValue, nextValue, nextValue, textureId, previousValue, 0.0F);
   }

   public void drawTexture(
      RhiBlendStateService.TextureHandle textureHandle,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      int textureId,
      float inputValue,
      float outputValue
   ) {
      this.drawTextureRegion(textureHandle, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, textureId, inputValue, outputValue, 0.0F, 0.0F, 1.0F, 1.0F);
   }

   public void drawTextureRegion(
      RhiBlendStateService.TextureHandle textureHandle,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      int textureId,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue
   ) {
      this.drawTextureRegion(
         textureHandle, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, textureId, inputValue, outputValue, resultValue, candidateValue, selectedValue, defaultValue, false
      );
   }

   public void drawTextureRegion(
      RhiBlendStateService.TextureHandle textureHandle,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      int textureId,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      boolean enabled
   ) {
      if (textureHandle != null && textureHandle.valid()) {
         this.items3
            .add(
               new CompositorPushPresentationScaleService.TexCmd(
                  textureHandle,
                  y,
                  width,
                  height,
                  value,
                  currentValue,
                  this.calculateValue(nextValue),
                  this.calculateValue(previousValue),
                  this.calculateValue(sourceValue),
                  this.calculateValue(targetValue),
                  textureId,
                  inputValue,
                  this.calculateValue(outputValue),
                  resultValue,
                  candidateValue,
                  selectedValue,
                  defaultValue,
                  false,
                  enabled,
                  this.float3
               )
            );
      }
   }

   public void drawTextureOpaque(
      RhiBlendStateService.TextureHandle textureHandle,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue
   ) {
      if (textureHandle != null && textureHandle.valid()) {
         this.items3
            .add(
               new CompositorPushPresentationScaleService.TexCmd(
                  textureHandle,
                  y,
                  width,
                  height,
                  value,
                  currentValue,
                  this.calculateValue(nextValue),
                  this.calculateValue(previousValue),
                  this.calculateValue(sourceValue),
                  this.calculateValue(targetValue),
                  0,
                  0.0F,
                  this.calculateValue(inputValue),
                  0.0F,
                  0.0F,
                  1.0F,
                  1.0F,
                  true,
                  false,
                  this.float3
               )
            );
      }
   }

   public void drawSdfTexture(
      RhiBlendStateService.TextureHandle textureHandle, float y, float width, float height, float value, float currentValue, int textureId, float nextValue, float previousValue
   ) {
      this.drawMorphSdfTexture(textureHandle, textureHandle, 0.0F, y, width, height, value, currentValue, textureId, nextValue, previousValue);
   }

   public void drawMorphSdfTexture(
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.TextureHandle currentTextureHandle,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int textureId,
      float sourceValue,
      float targetValue
   ) {
      if (textureHandle != null && textureHandle.valid() && currentTextureHandle != null && currentTextureHandle.valid() && textureId >>> 24 != 0) {
         if (Float.isFinite(width)
            && Float.isFinite(height)
            && Float.isFinite(value)
            && Float.isFinite(currentValue)
            && Float.isFinite(nextValue)
            && Float.isFinite(previousValue)
            && Float.isFinite(sourceValue)
            && Float.isFinite(targetValue)
            && !(currentValue <= 0.0F)
            && !(nextValue <= 0.0F)
            && !(previousValue <= 0.0F)
            && !(targetValue <= 0.0F)) {
            this.items4
               .add(
                  new CompositorPushPresentationScaleService.SdfIconCmd(
                     textureHandle, currentTextureHandle, Math.max(0.0F, Math.min(1.0F, width)), height, value, currentValue, nextValue, previousValue, textureId, sourceValue, targetValue, this.float3
                  )
               );
         }
      }
   }

   public void registerTexture(String text, RhiBlendStateService.TextureHandle textureHandle) {
      this.text5.put(text, textureHandle);
   }

   public RhiBlendStateService.TextureHandle getNamedTexture(String name) {
      return this.text5.get(name);
   }

   public void registerImageTexture(String text, BufferedImage bufferedImage) {
      if (this.rhiOperationHandler != null && bufferedImage != null) {
         int width = bufferedImage.getWidth();
         int height = bufferedImage.getHeight();
         if (width > 0 && height > 0) {
            RhiBlendStateService.TextureHandle textureHandle = this.text5.get(text);
            RhiBlendStateService.TextureHandle currentTextureHandle = this.uploadImageTexture(RhiBlendStateService.TextureHandle.NONE, bufferedImage);
            if (currentTextureHandle.valid()) {
               if (textureHandle != null && textureHandle.valid()) {
                  this.rhiOperationHandler.destroyTexture(textureHandle);
               }

               this.text5.put(text, currentTextureHandle);
            }
         }
      }
   }

   public RhiBlendStateService.TextureHandle uploadImageTexture(RhiBlendStateService.TextureHandle textureHandle, BufferedImage bufferedImage) {
      if (this.rhiOperationHandler != null && bufferedImage != null) {
         int width = bufferedImage.getWidth();
         int height = bufferedImage.getHeight();
         if (width > 0 && height > 0) {
            return this.uploadRgbaTexture(textureHandle, width, height, m9zhpaozvi(bufferedImage));
         } else {
            return textureHandle != null ? textureHandle : RhiBlendStateService.TextureHandle.NONE;
         }
      } else {
         return textureHandle != null ? textureHandle : RhiBlendStateService.TextureHandle.NONE;
      }
   }

   public RhiBlendStateService.TextureHandle uploadRgbaTexture(RhiBlendStateService.TextureHandle textureHandle, int textureId, int currentTextureId, ByteBuffer byteBuffer) {
      if (this.rhiOperationHandler != null && byteBuffer != null && textureId > 0 && currentTextureId > 0) {
         long longValue = (long)textureId * currentTextureId * 4L;
         if (longValue <= 2147483647L && byteBuffer.remaining() >= longValue) {
            ByteBuffer currentByteBuffer = byteBuffer.duplicate();
            currentByteBuffer.rewind();
            if (textureHandle != null && textureHandle.valid()) {
               this.rhiOperationHandler.updateTexture(textureHandle, 0, 0, textureId, currentTextureId, currentByteBuffer);
               return textureHandle;
            } else {
               return this.rhiOperationHandler
                  .createTexture(
                     new RhiBlendStateService.TextureDescriptor(
                        textureId,
                        currentTextureId,
                        RhiBlendStateService.TextureFormat.RGBA8,
                        RhiBlendStateService.FilterMode.LINEAR,
                        RhiBlendStateService.FilterMode.LINEAR,
                        RhiBlendStateService.AddressMode.CLAMP
                     ),
                     currentByteBuffer
                  );
            }
         } else {
            CoreIsInitializedHandler.LOGGER.warn("Rejected RGBA upload {}x{} (buffer too small)", textureId, currentTextureId);
            return textureHandle != null ? textureHandle : RhiBlendStateService.TextureHandle.NONE;
         }
      } else {
         return textureHandle != null ? textureHandle : RhiBlendStateService.TextureHandle.NONE;
      }
   }

   public void destroyTexture(RhiBlendStateService.TextureHandle textureHandle) {
      if (this.rhiOperationHandler != null && textureHandle != null && textureHandle.valid()) {
         this.rhiOperationHandler.destroyTexture(textureHandle);
      }
   }

   private static ByteBuffer m9zhpaozvi(BufferedImage bufferedImage) {
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      long longValue = (long)width * height;
      if (longValue > 0L && longValue <= 16777216L) {
         ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);
         int[] ints = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

         for (int value : ints) {
            byteBuffer.put((byte)(value >> 16 & 0xFF));
            byteBuffer.put((byte)(value >> 8 & 0xFF));
            byteBuffer.put((byte)(value & 0xFF));
            byteBuffer.put((byte)(value >> 24 & 0xFF));
         }

         byteBuffer.flip();
         return byteBuffer;
      } else {
         return null;
      }
   }

   public GlRenderer glEncoder() {
      return this.renderer;
   }

   public boolean isInitialized() {
      return this.enabled3;
   }

   private void updateState3(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      int resultValue,
      float candidateValue,
      float selectedValue,
      int defaultValue,
      float initialValue,
      int resolvedValue
   ) {
      this.addRect(value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue, selectedValue, defaultValue, initialValue, resolvedValue, 1.0F, 0.0F, false, 0);
   }

   private void updateState4(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      int resultValue,
      float candidateValue,
      float selectedValue,
      int defaultValue,
      float initialValue,
      int resolvedValue,
      float computedValue,
      float cachedValue
   ) {
      this.addRect(value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue, selectedValue, defaultValue, initialValue, resolvedValue, computedValue, cachedValue, false, 0);
   }

   public void addRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue,
      float selectedValue,
      float defaultValue,
      boolean enabled,
      int initialValue
   ) {
      this.addRect(x, y, width, height, value, currentValue, nextValue, previousValue, sourceValue, targetValue, inputValue, outputValue, resultValue, candidateValue, selectedValue, defaultValue, enabled, initialValue, 0);
   }

   public void addRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue,
      float selectedValue,
      float defaultValue,
      boolean enabled,
      int initialValue,
      int resolvedValue
   ) {
      this.addRect(
         x,
         y,
         width,
         height,
         value,
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         targetValue,
         inputValue,
         outputValue,
         resultValue,
         candidateValue,
         selectedValue,
         defaultValue,
         enabled,
         initialValue,
         resolvedValue,
         0.0F,
         0.0F
      );
   }

   public void addRect(
      float x,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      int outputValue,
      float resultValue,
      int candidateValue,
      float selectedValue,
      float defaultValue,
      boolean enabled,
      int initialValue,
      int resolvedValue,
      float computedValue,
      float cachedValue
   ) {
      if (Float.isFinite(x)
         && Float.isFinite(y)
         && Float.isFinite(width)
         && Float.isFinite(height)
         && !(width <= 0.0F)
         && !(height <= 0.0F)) {
         value = this.calculateValue(value);
         currentValue = this.calculateValue(currentValue);
         nextValue = this.calculateValue(nextValue);
         previousValue = this.calculateValue(previousValue);
         targetValue = this.calculateValue(ThemeBlurQualityData.current().blur(targetValue));
         inputValue = this.calculateValue(ThemeBlurQualityData.current().shadow(inputValue));
         outputValue = ThemeBlurQualityData.current().shadowColor(outputValue);
         resultValue = this.calculateValue(resultValue);
         defaultValue = this.calculateValue(defaultValue);
         cachedValue = this.calculateValue(cachedValue);
         float pendingValue = (sourceValue >> 24 & 0xFF) / 255.0F;
         float activeValue = (sourceValue >> 16 & 0xFF) / 255.0F;
         float fallbackValue = (sourceValue >> 8 & 0xFF) / 255.0F;
         float primaryValue = (sourceValue & 0xFF) / 255.0F;
         float secondaryValue = (initialValue >> 24 & 0xFF) / 255.0F;
         float tertiaryValue = (initialValue >> 16 & 0xFF) / 255.0F;
         float temporaryValue = (initialValue >> 8 & 0xFF) / 255.0F;
         float requestedValue = (initialValue & 0xFF) / 255.0F;
         this.items
            .add(
               new CompositorPushPresentationScaleService.RectCmd(
                  x,
                  y,
                  width,
                  height,
                  value,
                  currentValue,
                  nextValue,
                  previousValue,
                  activeValue,
                  fallbackValue,
                  primaryValue,
                  pendingValue,
                  targetValue,
                  inputValue,
                  outputValue,
                  resultValue,
                  candidateValue,
                  selectedValue,
                  defaultValue,
                  enabled,
                  tertiaryValue,
                  temporaryValue,
                  requestedValue,
                  secondaryValue,
                  resolvedValue,
                  computedValue,
                  cachedValue,
                  this.float3
               )
            );
         if (targetValue > 0.0F) {
            this.enabled6 = true;
            this.updateState();
         }
      }
   }

   public void renderFrame(int x, int y, double width, int height) {
      if (x > 0 && y > 0 && Double.isFinite(width) && !(width <= 0.0)) {
         int value = this.items.isEmpty()
               && this.items2.isEmpty()
               && this.items3.isEmpty()
               && this.items4.isEmpty()
               && this.items11.isEmpty()
               && this.items12.isEmpty()
               && this.items7.isEmpty()
               && this.items8.isEmpty()
               && this.items9.isEmpty()
               && this.items31.isEmpty()
               && this.items32.isEmpty()
               && (this.textTextService == null || !this.enabled3)
            ? 0
            : 1;
         if (value != 0) {
            if (this.rhiOperationHandler == null) {
               this.rhiOperationHandler = RhiDeviceService.device();
               this.renderer = (GlRenderer)this.rhiOperationHandler.encoder();
            }

            this.items33.clear();
            this.float3 = null;
            this.renderer.saveGLState();

            try {
               if (!this.enabled3) {
                  this.updateState34();
               }

               if (!this.enabled3) {
                  this.items.clear();
                  this.items2.clear();
                  this.items16.clear();
                  this.enabled6 = false;
                  return;
               }

               RhiBlendStateService.FramebufferHandle framebufferHandle = new RhiBlendStateService.FramebufferHandle(height);
               RhiCommandBuffer rhiCommandBuffer = this.rhiOperationHandler.encoder();
               this.rhiOperationHandler2 = rhiCommandBuffer;
               this.framebufferHandle3 = framebufferHandle;
               this.count6 = x;
               this.count7 = y;
               this.count8 = x;
               this.count9 = y;
               this.timestamp++;
               this.updateState2();
               rhiCommandBuffer.clearScissor();
               if (this.value3 > 0.001F && this.renderer2 != null) {
                  DiagnosticsAddFrameListenerService.get().begin("postfx");
                  this.renderer2.renderBackgroundDesaturate(rhiCommandBuffer, framebufferHandle, x, y, this.value3);
                  DiagnosticsAddFrameListenerService.get().end();
               }

               if (!ThemeBlurQualityData.current().blurEnabled()) {
                  this.compositorShutdownService.releaseTargets();
               }

               if (this.enabled6) {
                  DiagnosticsAddFrameListenerService.get().begin("blur");
                  this.compositorShutdownService.execute(rhiCommandBuffer, framebufferHandle, x, y);
                  DiagnosticsAddFrameListenerService.get().end();
               }

               if (this.enabled4) {
                  this.captureActiveFramebuffer();
                  this.enabled4 = false;
               }

               rhiCommandBuffer.beginRenderPass(framebufferHandle);
               rhiCommandBuffer.setViewport(0, 0, x, y);
               int currentValue = this.textTextService != null ? this.textTextService.batchCount() : 0;
               int currentSize = this.items15.size() + 1;

               for (int index = 0; index < currentSize; index++) {
                  int nextValue = index > 0 ? this.items15.get(index - 1) : 0;
                  int nextSize = index < this.items15.size() ? this.items15.get(index) : this.items.size();
                  int previousValue = index > 0 ? this.items16.get(index - 1) : 0;
                  int previousSize = index < this.items16.size() ? this.items16.get(index) : this.items2.size();
                  int sourceValue = index > 0 ? this.items17.get(index - 1) : 0;
                  int sourceSize = index < this.items17.size() ? this.items17.get(index) : this.items3.size();
                  int targetValue = index > 0 ? this.items18.get(index - 1) : 0;
                  int targetSize = index < this.items18.size() ? this.items18.get(index) : this.items4.size();
                  int inputValue = index > 0 ? this.items19.get(index - 1) : 0;
                  int inputSize = index < this.items19.size() ? this.items19.get(index) : currentValue;
                  int outputValue = index > 0 ? this.items20.get(index - 1) : 0;
                  int outputSize = index < this.items20.size() ? this.items20.get(index) : this.items5.size();
                  int resultValue = index > 0 ? this.items21.get(index - 1) : 0;
                  int resultSize = index < this.items21.size() ? this.items21.get(index) : this.items6.size();
                  int candidateValue = index > 0 ? this.items22.get(index - 1) : 0;
                  int candidateSize = index < this.items22.size() ? this.items22.get(index) : this.items7.size();
                  int selectedValue = index > 0 ? this.items23.get(index - 1) : 0;
                  int selectedSize = index < this.items23.size() ? this.items23.get(index) : this.items8.size();
                  int defaultValue = index > 0 ? this.items24.get(index - 1) : 0;
                  int defaultSize = index < this.items24.size() ? this.items24.get(index) : this.items9.size();
                  int initialValue = index > 0 ? this.items25.get(index - 1) : 0;
                  int initialSize = index < this.items25.size() ? this.items25.get(index) : this.items10.size();
                  int resolvedValue = index > 0 ? this.items26.get(index - 1) : 0;
                  int resolvedSize = index < this.items26.size() ? this.items26.get(index) : this.items11.size();
                  int computedValue = index > 0 ? this.items27.get(index - 1) : 0;
                  int computedSize = index < this.items27.size() ? this.items27.get(index) : this.items12.size();
                  int cachedValue = index > 0 ? this.items28.get(index - 1) : 0;
                  int cachedSize = index < this.items28.size() ? this.items28.get(index) : this.items13.size();
                  int pendingValue = index > 0 ? this.items29.get(index - 1) : 0;
                  int pendingSize = index < this.items29.size()
                     ? this.items29.get(index)
                     : (this.svgQueueService != null ? this.svgQueueService.cmdCount() : 0);
                  CompositorPushPresentationScaleService.LayerRanges layerRanges = new CompositorPushPresentationScaleService.LayerRanges(
                     nextValue,
                     nextSize,
                     previousValue,
                     previousSize,
                     sourceValue,
                     sourceSize,
                     targetValue,
                     targetSize,
                     inputValue,
                     inputSize,
                     outputValue,
                     outputSize,
                     resultValue,
                     resultSize,
                     candidateValue,
                     candidateSize,
                     selectedValue,
                     selectedSize,
                     defaultValue,
                     defaultSize,
                     initialValue,
                     initialSize,
                     resolvedValue,
                     resolvedSize,
                     computedValue,
                     computedSize,
                     cachedValue,
                     cachedSize,
                     pendingValue,
                     pendingSize
                  );
                  CompositorPushPresentationScaleService.LayerEffect activeSize = index < this.items31.size() ? this.items31.get(index) : null;
                  if (activeSize != null && this.pipelineHandle6 != null) {
                     rhiCommandBuffer.endRenderPass();
                     this.updateState7(rhiCommandBuffer, framebufferHandle, x, y, width, layerRanges, activeSize);
                  } else {
                     this.updateState8(rhiCommandBuffer, x, y, width, layerRanges);
                  }

                  if (index < currentSize - 1 && this.enabled6 && this.checkCondition(index + 1)) {
                     DiagnosticsAddFrameListenerService.get().begin("blur");
                     rhiCommandBuffer.endRenderPass();
                     this.compositorShutdownService.execute(rhiCommandBuffer, framebufferHandle, x, y);
                     rhiCommandBuffer.beginRenderPass(framebufferHandle);
                     rhiCommandBuffer.setViewport(0, 0, x, y);
                     DiagnosticsAddFrameListenerService.get().end();
                  }
               }

               if (this.textTextService != null) {
                  this.textTextService.reset();
               }

               int fallbackSize = this.items.size();
               int primarySize = this.items2.size();
               int secondarySize = this.items3.size();
               int tertiarySize = this.items4.size();
               int temporarySize = this.items5.size();
               int requestedSize = this.items6.size();
               int actualSize = this.items7.size();
               int expectedSize = this.items8.size();
               int minimumSize = this.items9.size();
               int maximumSize = this.items10.size();
               int startSize = this.items11.size();
               int endSize = this.items12.size();
               int localSize = this.items14.size();
               int storedSize = this.items13.size();
               int activeValue = this.svgQueueService != null ? this.svgQueueService.cmdCount() : 0;
               DiagnosticsAddFrameListenerService.get().begin("afterflush");

               for (Runnable runnable : this.items32) {
                  try {
                     runnable.run();
                  } catch (Exception exception) {
                     CoreIsInitializedHandler.LOGGER.error("After-flush hook error", exception);
                  }
               }

               this.items32.clear();
               DiagnosticsAddFrameListenerService.get().end();
               if (this.items.size() > fallbackSize) {
                  if (this.enabled2) {
                     this.updateState13(rhiCommandBuffer, x, y, width, fallbackSize, this.items.size());
                  } else {
                     this.updateState15(rhiCommandBuffer, x, y, width, fallbackSize, this.items.size());
                  }
               }

               if (this.items2.size() > primarySize && this.pipelineHandle5 != null) {
                  this.updateState17(rhiCommandBuffer, x, y, width, primarySize, this.items2.size());
               }

               if (this.items13.size() > storedSize && this.pipelineHandle13 != null) {
                  this.updateState25(rhiCommandBuffer, x, y, width, storedSize, this.items13.size());
               }

               if (this.items7.size() > actualSize && this.pipelineHandle15 != null) {
                  this.updateState28(rhiCommandBuffer, x, y, width, actualSize, this.items7.size());
               }

               if (this.items8.size() > expectedSize && this.pipelineHandle16 != null) {
                  this.updateState30(rhiCommandBuffer, x, y, width, expectedSize, this.items8.size());
               }

               if (this.items9.size() > minimumSize && this.pipelineHandle17 != null) {
                  this.updateState29(rhiCommandBuffer, x, y, width, minimumSize, this.items9.size());
               }

               if (this.textTextService != null) {
                  DiagnosticsAddFrameListenerService.get().begin("text");
                  this.textTextService.flush(rhiCommandBuffer, x, y, width);
                  DiagnosticsAddFrameListenerService.get().end();
               }

               if (this.items3.size() > secondarySize && this.pipelineHandle3 != null) {
                  this.updateState19(rhiCommandBuffer, x, y, width, secondarySize, this.items3.size());
               }

               if (this.items4.size() > tertiarySize && this.pipelineHandle4 != null) {
                  this.updateState20(rhiCommandBuffer, x, y, width, tertiarySize, this.items4.size());
               }

               if (this.items5.size() > temporarySize && this.pipelineHandle8 != null) {
                  this.updateState21(rhiCommandBuffer, x, y, width, temporarySize, this.items5.size());
               }

               if (this.items11.size() > startSize && this.pipelineHandle9 != null) {
                  this.updateState22(rhiCommandBuffer, x, y, width, startSize, this.items11.size());
               }

               if (this.items12.size() > endSize && this.pipelineHandle12 != null) {
                  this.updateState24(rhiCommandBuffer, x, y, width, endSize, this.items12.size());
               }

               if (this.items14.size() > localSize && this.pipelineHandle18 != null) {
                  this.updateState23(rhiCommandBuffer, x, y, width, localSize, this.items14.size());
               }

               if (this.items6.size() > requestedSize && this.pipelineHandle14 != null) {
                  this.updateState27(rhiCommandBuffer, x, y, width, requestedSize, this.items6.size());
               }

               if (this.items10.size() > maximumSize) {
                  this.updateState31(rhiCommandBuffer, x, y, width, maximumSize, this.items10.size());
               }

               if (this.svgQueueService != null && this.svgQueueService.cmdCount() > activeValue) {
                  this.svgQueueService.flushRange(rhiCommandBuffer, x, y, width, activeValue, this.svgQueueService.cmdCount());
               }

               rhiCommandBuffer.endRenderPass();
               if (this.renderer2 != null && this.renderer2.hasAnyEffect()) {
                  DiagnosticsAddFrameListenerService.get().begin("postfx");
                  this.renderer2.render(rhiCommandBuffer, framebufferHandle, x, y);
                  DiagnosticsAddFrameListenerService.get().end();
               }

               this.updateState10();
            } catch (Exception currentException) {
               CoreIsInitializedHandler.LOGGER.error("Compositor render error", currentException);
            } finally {
               try {
                  this.renderer.restoreGLState();
               } catch (Exception nextException) {
                  CoreIsInitializedHandler.LOGGER.error("Compositor restoreGLState failed", nextException);
               }

               this.items33.clear();
               this.float3 = null;
            }

            this.items.clear();
            this.items2.clear();
            this.items3.clear();
            this.items4.clear();
            this.items5.clear();
            this.items6.clear();
            this.items7.clear();
            this.items8.clear();
            this.items9.clear();
            this.items10.clear();
            this.items11.clear();
            this.items12.clear();
            this.items14.clear();
            this.items13.clear();
            this.items15.clear();
            this.items16.clear();
            this.items17.clear();
            this.items18.clear();
            this.items19.clear();
            this.items20.clear();
            this.items21.clear();
            this.items22.clear();
            this.items23.clear();
            this.items24.clear();
            this.items25.clear();
            this.items26.clear();
            this.items27.clear();
            this.items28.clear();
            this.items29.clear();
            this.items30.clear();
            if (this.svgQueueService != null) {
               this.svgQueueService.reset();
            }

            this.items31.clear();
            this.count10 = 0;
            this.enabled6 = false;
            this.value3 = 0.0F;
            this.rhiOperationHandler2 = null;
            this.framebufferHandle3 = RhiBlendStateService.FramebufferHandle.NONE;
            this.count6 = 0;
            this.count7 = 0;
         }
      } else {
         this.items.clear();
         this.items2.clear();
         this.items3.clear();
         this.items4.clear();
         this.items11.clear();
         this.items12.clear();
         this.items7.clear();
         this.items8.clear();
         this.items9.clear();
         this.items31.clear();
         this.items32.clear();
         if (this.textTextService != null) {
            this.textTextService.reset();
         }

         if (this.svgQueueService != null) {
            this.svgQueueService.reset();
         }

         this.enabled6 = false;
      }
   }

   public void backgroundDesaturate(float value) {
      this.value3 = Math.max(0.0F, Math.min(1.0F, value));
   }

   public void pointer(float x, float y, boolean enabled) {
      this.value = x;
      this.value2 = y;
      this.enabled5 = enabled;
   }

   public float pointerX() {
      return this.value;
   }

   public float pointerY() {
      return this.value2;
   }

   public boolean pointerDown() {
      return this.enabled5;
   }

   public RhiBlendStateService.TextureHandle captureActiveFramebuffer() {
      if (this.enabled3 && this.rhiOperationHandler != null && this.rhiOperationHandler2 != null && this.framebufferHandle3.valid()) {
         this.updateState5(this.count6, this.count7);
         if (!this.textureHandle.valid()) {
            return RhiBlendStateService.TextureHandle.NONE;
         }

         this.rhiOperationHandler2
            .blitFramebuffer(
               this.framebufferHandle3,
               this.framebufferHandle,
               0,
               this.count7,
               this.count6,
               0,
               0,
               0,
               this.count6,
               this.count7,
               RhiBlendStateService.FilterMode.LINEAR
            );
         this.rhiOperationHandler2.beginRenderPass(this.framebufferHandle3);
         this.rhiOperationHandler2.setViewport(0, 0, this.count6, this.count7);
         this.rhiOperationHandler2.clearScissor();
         return this.textureHandle;
      } else {
         return RhiBlendStateService.TextureHandle.NONE;
      }
   }

   public void requestFramebufferSample() {
      this.enabled4 = true;
   }

   public RhiBlendStateService.TextureHandle framebufferSample() {
      return this.textureHandle;
   }

   private void updateState5(int value, int currentValue) {
      if (value > 0 && currentValue > 0) {
         if (!this.textureHandle.valid() || value != this.count2 || currentValue != this.count3) {
            if (this.textureHandle.valid()) {
               this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
               this.rhiOperationHandler.destroyTexture(this.textureHandle);
            }

            this.count2 = value;
            this.count3 = currentValue;
            this.textureHandle = this.rhiOperationHandler
               .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(value, currentValue, RhiBlendStateService.TextureFormat.RGBA8));
            this.framebufferHandle = this.rhiOperationHandler.createFramebuffer(this.textureHandle);
         }
      }
   }

   private void updateState6(int value, int currentValue) {
      if (value > 0 && currentValue > 0) {
         if (!this.textureHandle2.valid() || value != this.count4 || currentValue != this.count5) {
            if (this.textureHandle2.valid()) {
               this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
               this.rhiOperationHandler.destroyTexture(this.textureHandle2);
            }

            this.count4 = value;
            this.count5 = currentValue;
            this.textureHandle2 = this.rhiOperationHandler
               .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(value, currentValue, RhiBlendStateService.TextureFormat.RGBA8));
            this.framebufferHandle2 = this.rhiOperationHandler.createFramebuffer(this.textureHandle2);
         }
      }
   }

   private CompositorPushPresentationScaleService.LayerCache createLayerCache(int value, int currentValue, int nextValue) {
      CompositorPushPresentationScaleService.LayerCache layerCache = this.entries2.computeIfAbsent(value, item -> new CompositorPushPresentationScaleService.LayerCache());
      if (layerCache.texture.valid() && layerCache.fbW == currentValue && layerCache.fbH == nextValue) {
         return layerCache;
      }

      if (layerCache.texture.valid()) {
         this.rhiOperationHandler.destroyFramebuffer(layerCache.fbo);
         this.rhiOperationHandler.destroyTexture(layerCache.texture);
      }

      layerCache.fbW = currentValue;
      layerCache.fbH = nextValue;
      layerCache.version = Integer.MIN_VALUE;
      layerCache.texture = this.rhiOperationHandler
         .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(currentValue, nextValue, RhiBlendStateService.TextureFormat.RGBA8));
      layerCache.fbo = this.rhiOperationHandler.createFramebuffer(layerCache.texture);
      return layerCache;
   }

   private void updateState7(
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      int value,
      int currentValue,
      double doubleValue,
      CompositorPushPresentationScaleService.LayerRanges layerRanges,
      CompositorPushPresentationScaleService.LayerEffect layerEffect
   ) {
      if (layerEffect.cacheKey() == 0) {
         this.updateState6(value, currentValue);
         if (this.textureHandle2.valid() && this.framebufferHandle2.valid()) {
            rhiCommandBuffer.clearScissor();
            rhiCommandBuffer.beginRenderPass(this.framebufferHandle2, 0.0F, 0.0F, 0.0F, 0.0F);
            rhiCommandBuffer.setViewport(0, 0, value, currentValue);
            this.updateState8(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges);
            rhiCommandBuffer.endRenderPass();
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, value, currentValue);
            rhiCommandBuffer.clearScissor();
            this.updateState9(rhiCommandBuffer, value, currentValue, doubleValue, layerEffect, this.textureHandle2);
         } else {
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, value, currentValue);
            this.updateState8(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges);
         }
      } else {
         CompositorPushPresentationScaleService.LayerCache layerCache = this.createLayerCache(layerEffect.cacheKey(), value, currentValue);
         layerCache.lastUsedFrame = this.timestamp;
         int currentX = layerCache.texture.valid()
               && layerCache.fbo.valid()
               && layerCache.version == layerEffect.cacheVersion()
               && layerCache.effectsRevision == ThemeBlurQualityData.revision()
               && layerCache.textRevision == this.fontMetricsRevision()
               && checkCondition2(layerCache, layerEffect.x(), layerEffect.y(), layerEffect.w(), layerEffect.h())
            ? 1
            : 0;
         if (currentX == 0) {
            rhiCommandBuffer.clearScissor();
            rhiCommandBuffer.beginRenderPass(layerCache.fbo, 0.0F, 0.0F, 0.0F, 0.0F);
            rhiCommandBuffer.setViewport(0, 0, value, currentValue);
            this.updateState8(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges);
            rhiCommandBuffer.endRenderPass();
            layerCache.version = layerEffect.cacheVersion();
            layerCache.effectsRevision = ThemeBlurQualityData.revision();
            layerCache.textRevision = this.fontMetricsRevision();
            layerCache.x = layerEffect.x();
            layerCache.y = layerEffect.y();
            layerCache.w = layerEffect.w();
            layerCache.h = layerEffect.h();
         }

         rhiCommandBuffer.beginRenderPass(framebufferHandle);
         rhiCommandBuffer.setViewport(0, 0, value, currentValue);
         rhiCommandBuffer.clearScissor();
         this.updateState9(rhiCommandBuffer, value, currentValue, doubleValue, layerEffect, layerCache.texture);
      }
   }

   private void updateState8(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, CompositorPushPresentationScaleService.LayerRanges layerRanges) {
      if (layerRanges.glassStart() < layerRanges.glassEnd()
         || layerRanges.meshStart() < layerRanges.meshEnd()
         || layerRanges.liquidSwitchStart() < layerRanges.liquidSwitchEnd()
         || layerRanges.tooltipBubbleStart() < layerRanges.tooltipBubbleEnd()
         || layerRanges.curveGraphStart() < layerRanges.curveGraphEnd()
         || layerRanges.waveformStart() < layerRanges.waveformEnd()
         || layerRanges.timeSeriesStart() < layerRanges.timeSeriesEnd()
         || layerRanges.rectStart() < layerRanges.rectEnd()) {
         int nextValue = layerRanges.rectStart();
         int index = layerRanges.glassStart();
         int currentIndex = layerRanges.meshStart();
         int nextIndex = layerRanges.liquidSwitchStart();
         int previousIndex = layerRanges.tooltipBubbleStart();
         int sourceIndex = layerRanges.curveGraphStart();
         int targetIndex = layerRanges.waveformStart();
         int inputIndex = layerRanges.timeSeriesStart();

         while (
            nextValue < layerRanges.rectEnd()
               || index < layerRanges.glassEnd()
               || currentIndex < layerRanges.meshEnd()
               || nextIndex < layerRanges.liquidSwitchEnd()
               || previousIndex < layerRanges.tooltipBubbleEnd()
               || sourceIndex < layerRanges.curveGraphEnd()
               || targetIndex < layerRanges.waveformEnd()
               || inputIndex < layerRanges.timeSeriesEnd()
         ) {
            int previousValue = layerRanges.rectEnd();
            if (index < layerRanges.glassEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items2.get(index).rectIdx));
            }

            if (currentIndex < layerRanges.meshEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items11.get(currentIndex).rectIdx));
            }

            if (nextIndex < layerRanges.liquidSwitchEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items12.get(nextIndex).rectIdx));
            }

            if (previousIndex < layerRanges.tooltipBubbleEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items13.get(previousIndex).rectIdx));
            }

            if (sourceIndex < layerRanges.curveGraphEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items7.get(sourceIndex).rectIdx));
            }

            if (targetIndex < layerRanges.waveformEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items8.get(targetIndex).rectIdx));
            }

            if (inputIndex < layerRanges.timeSeriesEnd()) {
               previousValue = Math.min(previousValue, Math.max(nextValue, this.items9.get(inputIndex).rectIdx));
            }

            if (previousValue > nextValue) {
               int sourceValue = Math.min(previousValue, layerRanges.rectEnd());
               if (this.enabled2) {
                  this.updateState13(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, sourceValue);
               } else {
                  this.updateState15(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, sourceValue);
               }

               nextValue = sourceValue;
            } else {
               byte byteValue;
               for (byteValue = 0; index < layerRanges.glassEnd() && (nextValue >= layerRanges.rectEnd() || this.items2.get(index).rectIdx <= nextValue); byteValue = 1) {
                  if (this.pipelineHandle5 != null) {
                     this.updateState17(rhiCommandBuffer, value, currentValue, doubleValue, index, index + 1);
                  }

                  index++;
               }

               while (currentIndex < layerRanges.meshEnd() && (nextValue >= layerRanges.rectEnd() || this.items11.get(currentIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle9 != null) {
                     this.updateState22(rhiCommandBuffer, value, currentValue, doubleValue, currentIndex, currentIndex + 1);
                  }

                  currentIndex++;
                  byteValue = 1;
               }

               while (nextIndex < layerRanges.liquidSwitchEnd() && (nextValue >= layerRanges.rectEnd() || this.items12.get(nextIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle12 != null) {
                     this.updateState24(rhiCommandBuffer, value, currentValue, doubleValue, nextIndex, nextIndex + 1);
                  }

                  nextIndex++;
                  byteValue = 1;
               }

               while (previousIndex < layerRanges.tooltipBubbleEnd() && (nextValue >= layerRanges.rectEnd() || this.items13.get(previousIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle13 != null) {
                     this.updateState25(rhiCommandBuffer, value, currentValue, doubleValue, previousIndex, previousIndex + 1);
                  }

                  previousIndex++;
                  byteValue = 1;
               }

               while (sourceIndex < layerRanges.curveGraphEnd() && (nextValue >= layerRanges.rectEnd() || this.items7.get(sourceIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle15 != null) {
                     this.updateState28(rhiCommandBuffer, value, currentValue, doubleValue, sourceIndex, sourceIndex + 1);
                  }

                  sourceIndex++;
                  byteValue = 1;
               }

               while (targetIndex < layerRanges.waveformEnd() && (nextValue >= layerRanges.rectEnd() || this.items8.get(targetIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle16 != null) {
                     this.updateState30(rhiCommandBuffer, value, currentValue, doubleValue, targetIndex, targetIndex + 1);
                  }

                  targetIndex++;
                  byteValue = 1;
               }

               while (inputIndex < layerRanges.timeSeriesEnd() && (nextValue >= layerRanges.rectEnd() || this.items9.get(inputIndex).rectIdx <= nextValue)) {
                  if (this.pipelineHandle17 != null) {
                     this.updateState29(rhiCommandBuffer, value, currentValue, doubleValue, inputIndex, inputIndex + 1);
                  }

                  inputIndex++;
                  byteValue = 1;
               }

               if (byteValue == 0) {
                  if (nextValue >= layerRanges.rectEnd()) {
                     break;
                  }

                  int targetValue = nextValue + 1;
                  if (this.enabled2) {
                     this.updateState13(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, targetValue);
                  } else {
                     this.updateState15(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, targetValue);
                  }

                  nextValue = targetValue;
               } else if (nextValue >= layerRanges.rectEnd()
                  && index >= layerRanges.glassEnd()
                  && currentIndex >= layerRanges.meshEnd()
                  && nextIndex >= layerRanges.liquidSwitchEnd()
                  && previousIndex >= layerRanges.tooltipBubbleEnd()
                  && sourceIndex >= layerRanges.curveGraphEnd()
                  && targetIndex >= layerRanges.waveformEnd()
                  && inputIndex >= layerRanges.timeSeriesEnd()) {
                  break;
               }
            }
         }
      }

      if (this.textTextService != null && layerRanges.txtStart() < layerRanges.txtEnd()) {
         DiagnosticsAddFrameListenerService.get().begin("text");
         this.textTextService.flushRange(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.txtStart(), layerRanges.txtEnd());
         DiagnosticsAddFrameListenerService.get().end();
      }

      if (this.svgQueueService != null && layerRanges.vecStart() < layerRanges.vecEnd()) {
         this.svgQueueService.flushRange(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.vecStart(), layerRanges.vecEnd());
      }

      if (layerRanges.texStart() < layerRanges.texEnd() && this.pipelineHandle3 != null) {
         this.updateState19(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.texStart(), layerRanges.texEnd());
      }

      if (layerRanges.sdfStart() < layerRanges.sdfEnd() && this.pipelineHandle4 != null) {
         this.updateState20(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.sdfStart(), layerRanges.sdfEnd());
      }

      if (layerRanges.gradStart() < layerRanges.gradEnd() && this.pipelineHandle8 != null) {
         this.updateState21(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.gradStart(), layerRanges.gradEnd());
      }

      if (layerRanges.cpStart() < layerRanges.cpEnd() && this.pipelineHandle14 != null) {
         this.updateState27(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.cpStart(), layerRanges.cpEnd());
      }

      if (layerRanges.ovStart() < layerRanges.ovEnd()) {
         this.updateState31(rhiCommandBuffer, value, currentValue, doubleValue, layerRanges.ovStart(), layerRanges.ovEnd());
      }
   }

   private void updateState9(
      RhiCommandBuffer rhiCommandBuffer, int currentValue, int nextValue, double doubleValue, CompositorPushPresentationScaleService.LayerEffect layerEffect, RhiBlendStateService.TextureHandle textureHandle
   ) {
      float previousValue = (float)doubleValue;
      float currentX = layerEffect.x() * previousValue;
      float currentY = layerEffect.y() * previousValue;
      float sourceValue = Math.max(1.0F, layerEffect.w() * previousValue);
      float targetValue = Math.max(1.0F, layerEffect.h() * previousValue);
      float inputValue = currentX / currentValue;
      float outputValue = (currentX + sourceValue) / currentValue;
      float resultValue = 1.0F - currentY / nextValue;
      float candidateValue = 1.0F - (currentY + targetValue) / nextValue;
      int selectedValue = layerEffect.material() != 0 && this.pipelineHandle7 != null && this.pipelineHandle7.valid() ? 1 : 0;
      rhiCommandBuffer.bindPipeline(selectedValue != 0 ? this.pipelineHandle7 : this.pipelineHandle6);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.pushVec2("uResolution", currentValue, nextValue);
      rhiCommandBuffer.pushVec4("uRect", currentX, currentY, sourceValue, targetValue);
      rhiCommandBuffer.pushFloat("uOpacity", layerEffect.opacity());
      rhiCommandBuffer.pushVec2("uSize", sourceValue, targetValue);
      rhiCommandBuffer.pushVec4("uRadius", layerEffect.tl() * previousValue, layerEffect.tr() * previousValue, layerEffect.br() * previousValue, layerEffect.bl() * previousValue);
      rhiCommandBuffer.pushFloat("uEdgeSoftness", layerEffect.edgeSoftness() * previousValue);
      rhiCommandBuffer.pushFloat("uRotation", 0.0F);
      rhiCommandBuffer.pushVec4("uUvRect", inputValue, resultValue, outputValue, candidateValue);
      rhiCommandBuffer.pushVec2("uTexelSize", 1.0F / currentValue, 1.0F / nextValue);
      rhiCommandBuffer.pushFloat("uBlurRadius", ThemeBlurQualityData.current().blur(layerEffect.blur()) * previousValue);
      rhiCommandBuffer.pushFloat("uSaturation", layerEffect.saturation());
      rhiCommandBuffer.pushFloat("uBrightness", layerEffect.brightness());
      rhiCommandBuffer.pushFloat("uContrast", layerEffect.contrast());
      rhiCommandBuffer.pushFloat("uGrayscale", layerEffect.grayscale());
      rhiCommandBuffer.pushInt("uMaskShape", layerEffect.maskShape());
      rhiCommandBuffer.pushInt("uMaskInvert", layerEffect.maskInvert() ? 1 : 0);
      updateState32(rhiCommandBuffer, "uTint", layerEffect.tintColor());
      if (selectedValue != 0) {
         float defaultValue = this.value * previousValue - currentX;
         float initialValue = this.value2 * previousValue - currentY;
         float resolvedValue = defaultValue >= 0.0F && initialValue >= 0.0F && defaultValue <= sourceValue && initialValue <= targetValue ? 1.0F : 0.0F;
         float computedValue = (float)(System.nanoTime() - this.timestamp2) / 1.0E9F;
         rhiCommandBuffer.pushInt("uMaterial", layerEffect.material());
         rhiCommandBuffer.pushFloat("uTime", computedValue);
         rhiCommandBuffer.pushVec4("uMouse", defaultValue, initialValue, this.enabled5 ? 1.0F : 0.0F, resolvedValue);
         rhiCommandBuffer.pushVec4("uMaterialParams", layerEffect.materialIntensity(), layerEffect.materialScale(), layerEffect.materialSpeed(), layerEffect.materialDistortion());
         rhiCommandBuffer.pushVec4(
            "uMaterialState", layerEffect.materialGlow(), layerEffect.hoverProgress(), layerEffect.pressProgress(), (float)(this.timestamp % 100000L)
         );
         updateState32(rhiCommandBuffer, "uMaterialColor", layerEffect.materialColor());
      }

      if (textureHandle != null && textureHandle.valid()) {
         rhiCommandBuffer.bindTexture(textureHandle, 0);
         rhiCommandBuffer.pushInt("uTexture", 0);
         rhiCommandBuffer.draw(4, 1, 0);
      }
   }

   private static boolean checkCondition2(CompositorPushPresentationScaleService.LayerCache layerCache, float value, float currentValue, float nextValue, float previousValue) {
      return checkCondition3(layerCache.x, value) && checkCondition3(layerCache.y, currentValue) && checkCondition3(layerCache.w, nextValue) && checkCondition3(layerCache.h, previousValue);
   }

   private static boolean checkCondition3(float value, float currentValue) {
      return Math.abs(value - currentValue) < 0.01F;
   }

   private void updateState10() {
      if (this.timestamp % 300L == 0L) {
         Iterator currentIterator = this.entries2.entrySet().iterator();

         while (currentIterator.hasNext()) {
            CompositorPushPresentationScaleService.LayerCache layerCache = (CompositorPushPresentationScaleService.LayerCache)((Entry)currentIterator.next()).getValue();
            if (this.timestamp - layerCache.lastUsedFrame > 600L) {
               if (layerCache.texture.valid()) {
                  this.rhiOperationHandler.destroyFramebuffer(layerCache.fbo);
                  this.rhiOperationHandler.destroyTexture(layerCache.texture);
               }

               currentIterator.remove();
            }
         }
      }
   }

   private boolean checkCondition4() {
      return this.enabled6 && this.compositorShutdownService != null && this.compositorShutdownService.texture() != null && this.compositorShutdownService.texture().valid();
   }

   private void updateState11(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue) {
      int currentSize = this.items.size();
      this.updateState35(currentSize);
      boolean enabled = this.checkCondition4();
      float[] floats = new float[currentSize * 40];
      int index = 0;

      for (CompositorPushPresentationScaleService.RectCmd rectCmd : this.items) {
         float nextValue = (float)doubleValue;
         floats[index++] = rectCmd.x * nextValue;
         floats[index++] = rectCmd.y * nextValue;
         floats[index++] = rectCmd.w * nextValue;
         floats[index++] = rectCmd.h * nextValue;
         floats[index++] = rectCmd.r;
         floats[index++] = rectCmd.g;
         floats[index++] = rectCmd.b;
         floats[index++] = rectCmd.a;
         floats[index++] = rectCmd.tl * nextValue;
         floats[index++] = rectCmd.tr * nextValue;
         floats[index++] = rectCmd.br * nextValue;
         floats[index++] = rectCmd.bl * nextValue;
         floats[index++] = 0.0F;
         floats[index++] = rectCmd.shadow * 0.5F * nextValue;
         floats[index++] = rectCmd.shadow * 1.5F * nextValue;
         floats[index++] = 0.0F;
         floats[index++] = (rectCmd.shadowColor >> 16 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.shadowColor >> 8 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.shadowColor & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.shadowColor >> 24 & 0xFF) / 255.0F;
         floats[index++] = rectCmd.borderWidth * nextValue;
         floats[index++] = rectCmd.blur > 0.0F && enabled ? 1.0F : 0.0F;
         floats[index++] = 1.8F;
         floats[index++] = rectCmd.panelOpacity;
         floats[index++] = (rectCmd.borderColor >> 16 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.borderColor >> 8 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.borderColor & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.borderColor >> 24 & 0xFF) / 255.0F;
         floats[index++] = rectCmd.edgeSoftness * nextValue;
         floats[index++] = rectCmd.insetShadow ? 1.0F : 0.0F;
         floats[index++] = rectCmd.innerHighlight;
         floats[index++] = rectCmd.innerHighlightSize * nextValue;
         floats[index++] = rectCmd.gr2;
         floats[index++] = rectCmd.gg2;
         floats[index++] = rectCmd.gb2;
         floats[index++] = rectCmd.ga2;
         floats[index++] = (rectCmd.secondaryBorderColor >> 16 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.secondaryBorderColor >> 8 & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.secondaryBorderColor & 0xFF) / 255.0F;
         floats[index++] = (rectCmd.secondaryBorderColor >> 24 & 0xFF) / 255.0F;
      }

      this.rhiOperationHandler.updateBuffer(this.bufferHandle2, 0L, floats);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle2);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindStorageBuffer(this.bufferHandle2, 0);
      rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
      if (enabled) {
         rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
         rhiCommandBuffer.pushInt("uBlurTexture", 0);
      }

      int previousValue = 0;

      while (previousValue < currentSize) {
         float[] currentFloats = this.items.get(previousValue).clip();
         int currentIndex = previousValue + 1;

         while (currentIndex < currentSize && checkCondition5(this.items.get(currentIndex).clip(), currentFloats)) {
            currentIndex++;
         }

         CompositorIntersectService.apply(rhiCommandBuffer, currentFloats, (float)doubleValue, currentValue);
         rhiCommandBuffer.pushInt("uRectOffset", previousValue);
         rhiCommandBuffer.draw(4, currentIndex - previousValue, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, currentFloats);
         previousValue = currentIndex;
      }
   }

   private static boolean checkCondition5(float[] floats, float[] currentFloats) {
      return CompositorIntersectService.same(floats, currentFloats);
   }

   private void updateState12(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      boolean enabled = this.checkCondition4();

      for (CompositorPushPresentationScaleService.RectCmd rectCmd : this.items) {
         float nextValue = (float)doubleValue;
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", rectCmd.x * nextValue, rectCmd.y * nextValue, rectCmd.w * nextValue, rectCmd.h * nextValue);
         rhiCommandBuffer.pushVec4("uColor", rectCmd.r, rectCmd.g, rectCmd.b, rectCmd.a);
         rhiCommandBuffer.pushVec4("uRadius", rectCmd.tl * nextValue, rectCmd.tr * nextValue, rectCmd.br * nextValue, rectCmd.bl * nextValue);
         float previousValue = rectCmd.shadow * 1.5F * nextValue;
         rhiCommandBuffer.pushVec4("uShadow", 0.0F, rectCmd.shadow * 0.5F * nextValue, previousValue, 0.0F);
         updateState32(rhiCommandBuffer, "uShadowColor", rectCmd.shadowColor);
         rhiCommandBuffer.pushVec2("uBorder", rectCmd.borderWidth * nextValue, 0.0F);
         updateState32(rhiCommandBuffer, "uBorderColor", rectCmd.borderColor);
         updateState32(rhiCommandBuffer, "uBorderColor2", rectCmd.secondaryBorderColor);
         rhiCommandBuffer.pushFloat("uOpacity", rectCmd.panelOpacity);
         rhiCommandBuffer.pushFloat("uEdgeSoftness", rectCmd.edgeSoftness * nextValue);
         rhiCommandBuffer.pushVec4("uGradientEnd", rectCmd.gr2, rectCmd.gg2, rectCmd.gb2, rectCmd.ga2);
         rhiCommandBuffer.pushInt("uInsetShadow", rectCmd.insetShadow ? 1 : 0);
         rhiCommandBuffer.pushFloat("uInnerHighlight", rectCmd.innerHighlight);
         rhiCommandBuffer.pushFloat("uInnerHighlightSize", rectCmd.innerHighlightSize * nextValue);
         if (rectCmd.blur > 0.0F && enabled) {
            rhiCommandBuffer.pushInt("uHasBlur", 1);
            rhiCommandBuffer.pushFloat("uVibrancy", 1.8F);
            rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
            rhiCommandBuffer.pushInt("uBlurTexture", 0);
         } else {
            rhiCommandBuffer.pushInt("uHasBlur", 0);
         }

         CompositorIntersectService.apply(rhiCommandBuffer, rectCmd.clip, (float)doubleValue, currentValue);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, rectCmd.clip);
      }
   }

   private void updateState13(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      int sourceValue = previousValue - nextValue;
      if (sourceValue > 0) {
         DiagnosticsAddFrameListenerService.get().begin("rects");

         try {
            this.updateState14(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, previousValue);
         } finally {
            DiagnosticsAddFrameListenerService.get().end();
         }
      }
   }

   private void updateState14(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      int sourceValue = previousValue - nextValue;
      this.updateState35(sourceValue);
      boolean enabled = this.checkCondition4();
      int index = sourceValue * 40;
      if (this.float2.length < index) {
         this.float2 = new float[index];
      }

      float[] floats = this.float2;
      int currentIndex = 0;

      for (int nextIndex = nextValue; nextIndex < previousValue; nextIndex++) {
         CompositorPushPresentationScaleService.RectCmd rectCmd = this.items.get(nextIndex);
         float targetValue = (float)doubleValue;
         floats[currentIndex++] = rectCmd.x * targetValue;
         floats[currentIndex++] = rectCmd.y * targetValue;
         floats[currentIndex++] = rectCmd.w * targetValue;
         floats[currentIndex++] = rectCmd.h * targetValue;
         floats[currentIndex++] = rectCmd.r;
         floats[currentIndex++] = rectCmd.g;
         floats[currentIndex++] = rectCmd.b;
         floats[currentIndex++] = rectCmd.a;
         floats[currentIndex++] = rectCmd.tl * targetValue;
         floats[currentIndex++] = rectCmd.tr * targetValue;
         floats[currentIndex++] = rectCmd.br * targetValue;
         floats[currentIndex++] = rectCmd.bl * targetValue;
         floats[currentIndex++] = 0.0F;
         floats[currentIndex++] = rectCmd.shadow * 0.5F * targetValue;
         floats[currentIndex++] = rectCmd.shadow * 1.5F * targetValue;
         floats[currentIndex++] = 0.0F;
         floats[currentIndex++] = (rectCmd.shadowColor >> 16 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.shadowColor >> 8 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.shadowColor & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.shadowColor >> 24 & 0xFF) / 255.0F;
         floats[currentIndex++] = rectCmd.borderWidth * targetValue;
         floats[currentIndex++] = rectCmd.blur > 0.0F && enabled ? 1.0F : 0.0F;
         floats[currentIndex++] = 1.8F;
         floats[currentIndex++] = rectCmd.panelOpacity;
         floats[currentIndex++] = (rectCmd.borderColor >> 16 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.borderColor >> 8 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.borderColor & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.borderColor >> 24 & 0xFF) / 255.0F;
         floats[currentIndex++] = rectCmd.edgeSoftness * targetValue;
         floats[currentIndex++] = rectCmd.insetShadow ? 1.0F : 0.0F;
         floats[currentIndex++] = rectCmd.innerHighlight;
         floats[currentIndex++] = rectCmd.innerHighlightSize * targetValue;
         floats[currentIndex++] = rectCmd.gr2;
         floats[currentIndex++] = rectCmd.gg2;
         floats[currentIndex++] = rectCmd.gb2;
         floats[currentIndex++] = rectCmd.ga2;
         floats[currentIndex++] = (rectCmd.secondaryBorderColor >> 16 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.secondaryBorderColor >> 8 & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.secondaryBorderColor & 0xFF) / 255.0F;
         floats[currentIndex++] = (rectCmd.secondaryBorderColor >> 24 & 0xFF) / 255.0F;
      }

      this.rhiOperationHandler.orphanBuffer(this.bufferHandle2, this.count * 40L * 4L);
      this.rhiOperationHandler.updateBuffer(this.bufferHandle2, 0L, floats, index);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle2);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindStorageBuffer(this.bufferHandle2, 0);
      rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
      if (enabled) {
         rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
         rhiCommandBuffer.pushInt("uBlurTexture", 0);
      }

      int inputValue = 0;

      while (inputValue < sourceValue) {
         float[] currentFloats = this.items.get(nextValue + inputValue).clip();
         int previousIndex = inputValue + 1;

         while (previousIndex < sourceValue && checkCondition5(this.items.get(nextValue + previousIndex).clip(), currentFloats)) {
            previousIndex++;
         }

         CompositorIntersectService.apply(rhiCommandBuffer, currentFloats, (float)doubleValue, currentValue);
         rhiCommandBuffer.pushInt("uRectOffset", inputValue);
         rhiCommandBuffer.draw(4, previousIndex - inputValue, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, currentFloats);
         inputValue = previousIndex;
      }
   }

   private void updateState15(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      if (nextValue < previousValue) {
         DiagnosticsAddFrameListenerService.get().begin("rects");

         try {
            this.updateState16(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, previousValue);
         } finally {
            DiagnosticsAddFrameListenerService.get().end();
         }
      }
   }

   private void updateState16(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      boolean enabled = this.checkCondition4();
      rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
      rhiCommandBuffer.pushFloat("uVibrancy", 1.8F);
      if (enabled) {
         rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
         rhiCommandBuffer.pushInt("uBlurTexture", 0);
      }

      int sourceValue = -1;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.RectCmd rectCmd = this.items.get(index);
         float targetValue = (float)doubleValue;
         rhiCommandBuffer.pushVec4("uRect", rectCmd.x * targetValue, rectCmd.y * targetValue, rectCmd.w * targetValue, rectCmd.h * targetValue);
         rhiCommandBuffer.pushVec4("uColor", rectCmd.r, rectCmd.g, rectCmd.b, rectCmd.a);
         rhiCommandBuffer.pushVec4("uRadius", rectCmd.tl * targetValue, rectCmd.tr * targetValue, rectCmd.br * targetValue, rectCmd.bl * targetValue);
         float inputValue = rectCmd.shadow * 1.5F * targetValue;
         rhiCommandBuffer.pushVec4("uShadow", 0.0F, rectCmd.shadow * 0.5F * targetValue, inputValue, 0.0F);
         updateState32(rhiCommandBuffer, "uShadowColor", rectCmd.shadowColor);
         rhiCommandBuffer.pushVec2("uBorder", rectCmd.borderWidth * targetValue, 0.0F);
         updateState32(rhiCommandBuffer, "uBorderColor", rectCmd.borderColor);
         updateState32(rhiCommandBuffer, "uBorderColor2", rectCmd.secondaryBorderColor);
         rhiCommandBuffer.pushFloat("uOpacity", rectCmd.panelOpacity);
         rhiCommandBuffer.pushFloat("uEdgeSoftness", rectCmd.edgeSoftness * targetValue);
         rhiCommandBuffer.pushVec4("uGradientEnd", rectCmd.gr2, rectCmd.gg2, rectCmd.gb2, rectCmd.ga2);
         rhiCommandBuffer.pushInt("uInsetShadow", rectCmd.insetShadow ? 1 : 0);
         rhiCommandBuffer.pushFloat("uInnerHighlight", rectCmd.innerHighlight);
         rhiCommandBuffer.pushFloat("uInnerHighlightSize", rectCmd.innerHighlightSize * targetValue);
         int outputValue = rectCmd.blur > 0.0F && enabled ? 1 : 0;
         if (outputValue != sourceValue) {
            rhiCommandBuffer.pushInt("uHasBlur", outputValue);
            sourceValue = outputValue;
         }

         CompositorIntersectService.apply(rhiCommandBuffer, rectCmd.clip, (float)doubleValue, currentValue);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, rectCmd.clip);
      }
   }

   private void updateState17(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      if (nextValue < previousValue) {
         DiagnosticsAddFrameListenerService.get().begin("glass");

         try {
            this.updateState18(rhiCommandBuffer, value, currentValue, doubleValue, nextValue, previousValue);
         } finally {
            DiagnosticsAddFrameListenerService.get().end();
         }
      }
   }

   private void updateState18(RhiCommandBuffer rhiCommandBuffer, int currentValue, int nextValue, double doubleValue, int previousValue, int sourceValue) {
      if (this.pipelineHandle5 != null && this.checkCondition4()) {
         rhiCommandBuffer.bindPipeline(this.pipelineHandle5);
         rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
         float targetValue = (float)doubleValue;
         float inputValue = (float)(System.nanoTime() / 1.0E9);

         for (int index = previousValue; index < sourceValue; index++) {
            CompositorPushPresentationScaleService.GlassCmd glassCmd = this.items2.get(index);
            float outputValue = glassCmd.x * targetValue;
            float resultValue = glassCmd.y * targetValue;
            float candidateValue = glassCmd.w * targetValue;
            float selectedValue = glassCmd.h * targetValue;
            float defaultValue = glassCmd.edgeWobble * targetValue;
            float initialValue = Math.max(1.0F, glassCmd.edgeWobbleRadius * targetValue);
            float resolvedValue = this.value * targetValue - outputValue;
            float computedValue = this.value2 * targetValue - resultValue;
            float cachedValue = defaultValue > 0.001F
                  && resolvedValue >= -initialValue
                  && computedValue >= -initialValue
                  && resolvedValue <= candidateValue + initialValue
                  && computedValue <= selectedValue + initialValue
               ? 1.0F
               : 0.0F;
            CompositorIntersectService.apply(rhiCommandBuffer, glassCmd.clip, targetValue, nextValue);
            rhiCommandBuffer.pushVec2("uResolution", currentValue, nextValue);
            rhiCommandBuffer.pushVec4("uRect", outputValue, resultValue, candidateValue, selectedValue);
            rhiCommandBuffer.pushVec2("uSize", candidateValue, selectedValue);
            rhiCommandBuffer.pushVec4("uRadius", glassCmd.tl * targetValue, glassCmd.tr * targetValue, glassCmd.br * targetValue, glassCmd.bl * targetValue);
            rhiCommandBuffer.pushFloat("uOpacity", glassCmd.opacity);
            rhiCommandBuffer.pushFloat("uEdgeSoftness", glassCmd.edgeSoftness * targetValue);
            rhiCommandBuffer.pushFloat("uBlurRadius", glassCmd.blur * targetValue);
            rhiCommandBuffer.pushFloat("uSaturation", glassCmd.saturation);
            rhiCommandBuffer.pushFloat("uBrightness", glassCmd.brightness);
            rhiCommandBuffer.pushFloat("uContrast", glassCmd.contrast);
            rhiCommandBuffer.pushFloat("uRefraction", glassCmd.refraction);
            rhiCommandBuffer.pushFloat("uNoise", glassCmd.noise);
            rhiCommandBuffer.pushFloat("uChromatic", glassCmd.chromatic * targetValue);
            rhiCommandBuffer.pushFloat("uBorderWidth", glassCmd.borderWidth * targetValue);
            rhiCommandBuffer.pushFloat("uTime", inputValue);
            rhiCommandBuffer.pushFloat("uRotation", 0.0F);
            rhiCommandBuffer.pushFloat(
               "uVertexPadding",
               defaultValue > 0.001F
                  ? defaultValue * 1.55F + Math.max(2.0F, glassCmd.edgeSoftness * targetValue) + 4.0F
                  : 0.0F
            );
            rhiCommandBuffer.pushVec4("uMouse", resolvedValue, computedValue, this.enabled5 ? 1.0F : 0.0F, cachedValue);
            rhiCommandBuffer.pushVec4("uEdgeWobble", defaultValue, initialValue, 0.0F, 0.0F);
            rhiCommandBuffer.pushVec4("uRubber", glassCmd.rubberSide, glassCmd.rubberTangent * targetValue, glassCmd.rubberAmount * targetValue, glassCmd.rubberWidth * targetValue);
            updateState32(rhiCommandBuffer, "uTint", glassCmd.tintColor);
            updateState32(rhiCommandBuffer, "uTint2", glassCmd.tintColor2);
            updateState32(rhiCommandBuffer, "uBorderColor", glassCmd.borderColor);
            updateState32(rhiCommandBuffer, "uBorderColor2", glassCmd.secondaryBorderColor);
            updateState32(rhiCommandBuffer, "uHighlightColor", glassCmd.highlightColor);
            rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
            rhiCommandBuffer.pushInt("uBackdrop", 0);
            RhiBlendStateService.TextureHandle textureHandle = this.compositorShutdownService.sourceTexture() != null && this.compositorShutdownService.sourceTexture().valid()
               ? this.compositorShutdownService.sourceTexture()
               : this.compositorShutdownService.texture();
            rhiCommandBuffer.bindTexture(textureHandle, 1);
            rhiCommandBuffer.pushInt("uSharpBackdrop", 1);
            rhiCommandBuffer.draw(4, 1, 0);
            CompositorIntersectService.clear(rhiCommandBuffer, glassCmd.clip);
         }
      }
   }

   private void updateState19(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle3);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.TexCmd texCmd = this.items3.get(index);
         if (texCmd.texture != null && texCmd.texture.valid()) {
            rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
            rhiCommandBuffer.pushVec4("uRect", texCmd.x * sourceValue, texCmd.y * sourceValue, texCmd.w * sourceValue, texCmd.h * sourceValue);
            rhiCommandBuffer.pushFloat("uOpacity", texCmd.opacity);
            rhiCommandBuffer.pushVec2("uSize", texCmd.w * sourceValue, texCmd.h * sourceValue);
            rhiCommandBuffer.pushVec4("uRadius", texCmd.tl * sourceValue, texCmd.tr * sourceValue, texCmd.br * sourceValue, texCmd.bl * sourceValue);
            rhiCommandBuffer.pushFloat("uEdgeSoftness", texCmd.edgeSoftness * sourceValue);
            rhiCommandBuffer.pushFloat("uRotation", texCmd.rotation);
            rhiCommandBuffer.pushVec4("uUvRect", texCmd.u0, texCmd.v0, texCmd.u1, texCmd.v1);
            rhiCommandBuffer.pushFloat("uForceOpaque", texCmd.opaque ? 1.0F : 0.0F);
            updateState32(rhiCommandBuffer, "uTint", texCmd.tintColor);
            if (texCmd.nearest) {
               rhiCommandBuffer.bindTexture(texCmd.texture, this.samplerHandle, 0);
            } else {
               rhiCommandBuffer.bindTexture(texCmd.texture, 0);
            }

            rhiCommandBuffer.pushInt("uTexture", 0);
            CompositorIntersectService.apply(rhiCommandBuffer, texCmd.clip, sourceValue, currentValue);
            rhiCommandBuffer.draw(4, 1, 0);
            CompositorIntersectService.clear(rhiCommandBuffer, texCmd.clip);
         }
      }
   }

   private void updateState20(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle4);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.SdfIconCmd sdfIconCmd = this.items4.get(index);
         if (sdfIconCmd.texture != null && sdfIconCmd.texture.valid()) {
            rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
            rhiCommandBuffer.pushVec4("uRect", sdfIconCmd.x * sourceValue, sdfIconCmd.y * sourceValue, sdfIconCmd.w * sourceValue, sdfIconCmd.h * sourceValue);
            rhiCommandBuffer.pushFloat("uOpacity", sdfIconCmd.opacity);
            rhiCommandBuffer.pushFloat("uRotation", sdfIconCmd.rotation);
            rhiCommandBuffer.pushFloat("uPxRange", sdfIconCmd.pxRange);
            updateState32(rhiCommandBuffer, "uTint", sdfIconCmd.tintColor);
            rhiCommandBuffer.bindTexture(sdfIconCmd.texture, 0);
            rhiCommandBuffer.bindTexture(sdfIconCmd.morphTexture, 1);
            rhiCommandBuffer.pushInt("uMorphTexture", 1);
            rhiCommandBuffer.pushFloat("uMorph", sdfIconCmd.morph);
            rhiCommandBuffer.pushInt("uTexture", 0);
            CompositorIntersectService.apply(rhiCommandBuffer, sdfIconCmd.clip, sourceValue, currentValue);
            rhiCommandBuffer.draw(4, 1, 0);
            CompositorIntersectService.clear(rhiCommandBuffer, sdfIconCmd.clip);
         }
      }
   }

   private void updateState21(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle8);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.GradCmd gradCmd = this.items5.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, gradCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", gradCmd.x * sourceValue, gradCmd.y * sourceValue, gradCmd.w * sourceValue, gradCmd.h * sourceValue);
         updateState33(rhiCommandBuffer, "uColorTop", gradCmd.colorTop);
         updateState33(rhiCommandBuffer, "uColorBottom", gradCmd.colorBottom);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, gradCmd.clip);
      }
   }

   private void updateState22(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle9);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;
      float targetValue = (float)(System.nanoTime() / 1.0E9);

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.MeshCmd meshCmd = this.items11.get(index);
         int inputValue = meshCmd.studio != null && this.pipelineHandle10 != null ? 1 : 0;
         int outputValue = meshCmd.menuTime != null && this.pipelineHandle11 != null ? 1 : 0;
         rhiCommandBuffer.bindPipeline(outputValue != 0 ? this.pipelineHandle11 : (inputValue != 0 ? this.pipelineHandle10 : this.pipelineHandle9));
         rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
         CompositorIntersectService.apply(rhiCommandBuffer, meshCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", meshCmd.x * sourceValue, meshCmd.y * sourceValue, meshCmd.w * sourceValue, meshCmd.h * sourceValue);
         float resultValue = meshCmd.radius * sourceValue;
         if (inputValue == 0 && outputValue == 0) {
            rhiCommandBuffer.pushVec4("uRadius", resultValue, resultValue, resultValue, resultValue);
         } else {
            rhiCommandBuffer.pushFloat("uRadius", resultValue);
         }

         rhiCommandBuffer.pushFloat("uOpacity", meshCmd.opacity);
         rhiCommandBuffer.pushFloat("uTime", outputValue != 0 ? meshCmd.menuTime.time : (inputValue != 0 ? meshCmd.studio.time : targetValue));
         if (outputValue != 0) {
            rhiCommandBuffer.pushVec2("uSize", meshCmd.w * sourceValue, meshCmd.h * sourceValue);
            rhiCommandBuffer.pushVec2("uPage", meshCmd.menuTime.servers, meshCmd.menuTime.worlds);
            rhiCommandBuffer.pushFloat("uLight", meshCmd.menuTime.light ? 1.0F : 0.0F);
            rhiCommandBuffer.pushFloat("uFocus", meshCmd.menuTime.focus);
            rhiCommandBuffer.pushFloat("uLayoutWidth", meshCmd.w);
         }

         if (inputValue != 0) {
            rhiCommandBuffer.pushVec2("uSize", meshCmd.w * sourceValue, meshCmd.h * sourceValue);
            rhiCommandBuffer.pushInt("uKind", meshCmd.studio.kind);
            rhiCommandBuffer.pushVec4("uParams", meshCmd.studio.a, meshCmd.studio.b, meshCmd.studio.c, meshCmd.studio.d);
            rhiCommandBuffer.pushVec2(
               "uAlpha",
               (meshCmd.c0 >>> 24 & 0xFF) / 255.0F,
               (meshCmd.c1 >>> 24 & 0xFF) / 255.0F
            );
            rhiCommandBuffer.pushFloat("uDensity", sourceValue);
         }

         updateState26(rhiCommandBuffer, "uColor0", meshCmd.c0);
         updateState26(rhiCommandBuffer, "uColor1", meshCmd.c1);
         updateState26(rhiCommandBuffer, "uColor2", meshCmd.c2);
         updateState26(rhiCommandBuffer, "uColor3", meshCmd.c3);
         updateState26(rhiCommandBuffer, "uColor4", meshCmd.c4);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, meshCmd.clip);
      }
   }

   private void updateState23(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle18);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;
      float targetValue = (float)(System.nanoTime() / 1.0E9);

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.LoadingCmd loadingCmd = this.items14.get(index);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", loadingCmd.x * sourceValue, loadingCmd.y * sourceValue, loadingCmd.w * sourceValue, loadingCmd.h * sourceValue);
         rhiCommandBuffer.pushFloat("uRadius", loadingCmd.radius * sourceValue);
         rhiCommandBuffer.pushFloat("uTime", targetValue);
         rhiCommandBuffer.pushFloat("uProgress", loadingCmd.progress);
         rhiCommandBuffer.pushFloat("uOpacity", loadingCmd.opacity);
         rhiCommandBuffer.pushFloat("uScale", sourceValue);
         updateState26(rhiCommandBuffer, "uAccent", loadingCmd.accent);
         rhiCommandBuffer.draw(4, 1, 0);
      }
   }

   private void updateState24(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle12);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.LiquidSwitchCmd liquidSwitchCmd = this.items12.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, liquidSwitchCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", liquidSwitchCmd.x * sourceValue, liquidSwitchCmd.y * sourceValue, liquidSwitchCmd.w * sourceValue, liquidSwitchCmd.h * sourceValue);
         rhiCommandBuffer.pushVec2("uSize", liquidSwitchCmd.w * sourceValue, liquidSwitchCmd.h * sourceValue);
         float targetValue = liquidSwitchCmd.radius * sourceValue;
         rhiCommandBuffer.pushVec4("uRadius", targetValue, targetValue, targetValue, targetValue);
         rhiCommandBuffer.pushVec4("uActive", liquidSwitchCmd.activeX * sourceValue, liquidSwitchCmd.activeW * sourceValue, liquidSwitchCmd.transition, liquidSwitchCmd.opacity);
         rhiCommandBuffer.pushVec4("uHover", liquidSwitchCmd.hoverX * sourceValue, liquidSwitchCmd.hoverW * sourceValue, liquidSwitchCmd.hoverMix, 0.0F);
         updateState32(rhiCommandBuffer, "uAccent", liquidSwitchCmd.accentColor);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, liquidSwitchCmd.clip);
      }
   }

   private void updateState25(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle13);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      boolean enabled = this.checkCondition4();
      if (enabled) {
         rhiCommandBuffer.bindTexture(this.compositorShutdownService.texture(), 0);
         rhiCommandBuffer.pushInt("uBlurTexture", 0);
      }

      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.TooltipBubbleCmd tooltipBubbleCmd = this.items13.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, tooltipBubbleCmd.clip, sourceValue, currentValue);
         float targetValue = 0.0F;
         float inputValue = tooltipBubbleCmd.shadow * 0.5F * sourceValue;
         float outputValue = tooltipBubbleCmd.shadow * 1.6F * sourceValue;
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", tooltipBubbleCmd.x * sourceValue, tooltipBubbleCmd.y * sourceValue, tooltipBubbleCmd.w * sourceValue, tooltipBubbleCmd.h * sourceValue);
         rhiCommandBuffer.pushFloat("uRadius", tooltipBubbleCmd.radius * sourceValue);
         rhiCommandBuffer.pushVec3("uTail", tooltipBubbleCmd.tailX * sourceValue, tooltipBubbleCmd.tailW * sourceValue, tooltipBubbleCmd.tailH * sourceValue);
         rhiCommandBuffer.pushFloat("uSmoothK", tooltipBubbleCmd.smoothK * sourceValue);
         updateState32(rhiCommandBuffer, "uColor", tooltipBubbleCmd.color);
         updateState32(rhiCommandBuffer, "uGradientEnd", tooltipBubbleCmd.gradientEnd);
         rhiCommandBuffer.pushVec4("uShadow", targetValue, inputValue, outputValue, 0.0F);
         updateState32(rhiCommandBuffer, "uShadowColor", tooltipBubbleCmd.shadowColor);
         rhiCommandBuffer.pushVec2("uBorder", tooltipBubbleCmd.borderWidth * sourceValue, 0.0F);
         updateState32(rhiCommandBuffer, "uBorderColor", tooltipBubbleCmd.borderColor);
         rhiCommandBuffer.pushFloat("uInnerHighlight", tooltipBubbleCmd.innerHighlight);
         rhiCommandBuffer.pushFloat("uInnerHighlightSize", tooltipBubbleCmd.innerHighlightSize * sourceValue);
         rhiCommandBuffer.pushFloat("uOpacity", tooltipBubbleCmd.opacity);
         rhiCommandBuffer.pushFloat("uEdgeSoftness", tooltipBubbleCmd.edgeSoftness * sourceValue);
         rhiCommandBuffer.pushInt("uHasBlur", tooltipBubbleCmd.blur && enabled ? 1 : 0);
         rhiCommandBuffer.pushFloat("uVibrancy", 1.8F);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, tooltipBubbleCmd.clip);
      }
   }

   private static void updateState26(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
      rhiCommandBuffer.pushVec3(
         text,
         (value >> 16 & 0xFF) / 255.0F,
         (value >> 8 & 0xFF) / 255.0F,
         (value & 0xFF) / 255.0F
      );
   }

   private void updateState27(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle14);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.ColorPickerCmd colorPickerCmd = this.items6.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, colorPickerCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", colorPickerCmd.x * sourceValue, colorPickerCmd.y * sourceValue, colorPickerCmd.w * sourceValue, colorPickerCmd.h * sourceValue);
         rhiCommandBuffer.pushInt("uMode", colorPickerCmd.mode);
         rhiCommandBuffer.pushFloat("uHue", colorPickerCmd.hue);
         rhiCommandBuffer.pushFloat("uSat", colorPickerCmd.sat);
         rhiCommandBuffer.pushFloat("uBri", colorPickerCmd.bri);
         rhiCommandBuffer.pushFloat("uRadius", colorPickerCmd.radius * sourceValue);
         rhiCommandBuffer.pushFloat("uOpacity", colorPickerCmd.opacity);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, colorPickerCmd.clip);
      }
   }

   private void updateState28(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle15);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.CurveGraphCmd curveGraphCmd = this.items7.get(index);
         ModuleSetting.CurveValue curveValue = curveGraphCmd.curve;
         CompositorPushPresentationScaleService.CurveGraphStyle curveGraphStyle = curveGraphCmd.style;
         CompositorIntersectService.apply(rhiCommandBuffer, curveGraphCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", curveGraphCmd.x * sourceValue, curveGraphCmd.y * sourceValue, curveGraphCmd.w * sourceValue, curveGraphCmd.h * sourceValue);
         rhiCommandBuffer.pushVec4("uPlot", curveGraphCmd.plotX * sourceValue, curveGraphCmd.plotY * sourceValue, curveGraphCmd.plotW * sourceValue, curveGraphCmd.plotH * sourceValue);
         rhiCommandBuffer.pushFloat("uRadius", curveGraphCmd.radius * sourceValue);

         rhiCommandBuffer.pushInt("uCurveType", switch (curveValue.type()) {
            case LINEAR -> 0;
            case CUBIC_BEZIER -> 1;
            case STEPS -> 2;
         });
         rhiCommandBuffer.pushVec4("uBezier", curveValue.x1(), curveValue.y1(), curveValue.x2(), curveValue.y2());
         rhiCommandBuffer.pushInt("uSteps", curveValue.steps());

         rhiCommandBuffer.pushInt("uStepMode", switch (curveValue.stepMode()) {
            case JUMP_START -> 0;
            case JUMP_END -> 1;
            case JUMP_NONE -> 2;
            case JUMP_BOTH -> 3;
         });
         rhiCommandBuffer.pushInt("uActiveHandle", curveGraphCmd.activeHandle);
         rhiCommandBuffer.pushFloat("uOpacity", curveGraphCmd.opacity);
         rhiCommandBuffer.pushFloat("uEdgeSoftness", curveGraphCmd.edgeSoftness * sourceValue);
         rhiCommandBuffer.pushFloat("uMetricScale", curveGraphCmd.metricScale * sourceValue);
         rhiCommandBuffer.pushFloat("uCurveWidth", curveGraphCmd.curveWidth * sourceValue);
         rhiCommandBuffer.pushFloat("uCurveFringeWidth", curveGraphCmd.curveFringeWidth * sourceValue);
         updateState32(rhiCommandBuffer, "uBackgroundColor", curveGraphStyle.backgroundColor());
         updateState32(rhiCommandBuffer, "uBorderColor", curveGraphStyle.borderColor());
         updateState32(rhiCommandBuffer, "uGridColor", curveGraphStyle.gridColor());
         updateState32(rhiCommandBuffer, "uAxisColor", curveGraphStyle.axisColor());
         updateState32(rhiCommandBuffer, "uCurveColor", curveGraphStyle.curveColor());
         updateState32(rhiCommandBuffer, "uCurveFringeColor", curveGraphStyle.curveFringeColor());
         updateState32(rhiCommandBuffer, "uFillColor", curveGraphStyle.fillColor());
         updateState32(rhiCommandBuffer, "uGuideColor", curveGraphStyle.guideColor());
         updateState32(rhiCommandBuffer, "uHandleColor", curveGraphStyle.handleColor());
         updateState32(rhiCommandBuffer, "uActiveHandleColor", curveGraphStyle.activeHandleColor());
         updateState32(rhiCommandBuffer, "uHandleBorderColor", curveGraphStyle.handleBorderColor());
         updateState32(rhiCommandBuffer, "uEndpointColor", curveGraphStyle.endpointColor());
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, curveGraphCmd.clip);
      }
   }

   private void updateState29(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle17);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.TimeSeriesCmd timeSeriesCmd = this.items9.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, timeSeriesCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", timeSeriesCmd.x * sourceValue, timeSeriesCmd.y * sourceValue, timeSeriesCmd.w * sourceValue, timeSeriesCmd.h * sourceValue);
         rhiCommandBuffer.pushInt("uCount", timeSeriesCmd.data.size());
         if (this.compositorEmptyService != timeSeriesCmd.data) {
            for (int currentIndex = 0; currentIndex < timeSeriesCmd.data.size(); currentIndex++) {
               rhiCommandBuffer.pushVec4(
                  text2[currentIndex],
                  timeSeriesCmd.data.x(currentIndex),
                  timeSeriesCmd.data.y(currentIndex),
                  timeSeriesCmd.data.valid(currentIndex) ? 1.0F : 0.0F,
                  timeSeriesCmd.data.linked(currentIndex) ? 1.0F : 0.0F
               );
            }

            this.compositorEmptyService = timeSeriesCmd.data;
         }

         rhiCommandBuffer.pushFloat("uOpacity", timeSeriesCmd.opacity);
         rhiCommandBuffer.pushFloat("uMetricScale", timeSeriesCmd.metricScale * sourceValue);
         updateState32(rhiCommandBuffer, "uColor", timeSeriesCmd.color);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, timeSeriesCmd.clip);
      }
   }

   private void updateState30(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle16);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.WaveformCmd waveformCmd = this.items8.get(index);
         CompositorIntersectService.apply(rhiCommandBuffer, waveformCmd.clip, sourceValue, currentValue);
         if (this.compositorAmplitudeService != waveformCmd.samples) {
            for (byte byteValue = 0; byteValue < 512; byteValue += 4) {
               rhiCommandBuffer.pushVec4(
                  text3[byteValue / 4],
                  waveformCmd.samples.amplitude(byteValue),
                  waveformCmd.samples.amplitude(byteValue + 1),
                  waveformCmd.samples.amplitude(byteValue + 2),
                  waveformCmd.samples.amplitude(byteValue + 3)
               );
            }

            this.compositorAmplitudeService = waveformCmd.samples;
         }

         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", waveformCmd.x * sourceValue, waveformCmd.y * sourceValue, waveformCmd.w * sourceValue, waveformCmd.h * sourceValue);
         rhiCommandBuffer.pushFloat("uProgress", waveformCmd.progress);
         rhiCommandBuffer.pushFloat("uPreview", waveformCmd.preview);
         rhiCommandBuffer.pushFloat("uPlaying", waveformCmd.playing ? 1.0F : 0.0F);
         rhiCommandBuffer.pushFloat("uLoaded", waveformCmd.loaded ? 1.0F : 0.0F);
         updateState32(rhiCommandBuffer, "uAccent", waveformCmd.accent);
         updateState32(rhiCommandBuffer, "uRemaining", waveformCmd.remaining);
         updateState32(rhiCommandBuffer, "uInk", waveformCmd.ink);
         rhiCommandBuffer.pushFloat("uOpacity", waveformCmd.opacity);
         rhiCommandBuffer.pushFloat("uMetricScale", waveformCmd.metricScale * sourceValue);
         rhiCommandBuffer.pushFloat("uTime", (float)(System.nanoTime() - this.timestamp2) / 1.0E9F);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, waveformCmd.clip);
      }
   }

   private void updateState31(RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue, int nextValue, int previousValue) {
      rhiCommandBuffer.bindPipeline(this.pipelineHandle);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float sourceValue = (float)doubleValue;

      for (int index = nextValue; index < previousValue; index++) {
         CompositorPushPresentationScaleService.RectCmd rectCmd = this.items10.get(index);
         rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
         rhiCommandBuffer.pushVec4("uRect", rectCmd.x * sourceValue, rectCmd.y * sourceValue, rectCmd.w * sourceValue, rectCmd.h * sourceValue);
         rhiCommandBuffer.pushVec4("uColor", rectCmd.r, rectCmd.g, rectCmd.b, rectCmd.a);
         rhiCommandBuffer.pushVec4("uRadius", rectCmd.tl * sourceValue, rectCmd.tr * sourceValue, rectCmd.br * sourceValue, rectCmd.bl * sourceValue);
         float targetValue = rectCmd.shadow * 1.5F * sourceValue;
         rhiCommandBuffer.pushVec4("uShadow", 0.0F, rectCmd.shadow * 0.5F * sourceValue, targetValue, 0.0F);
         updateState32(rhiCommandBuffer, "uShadowColor", rectCmd.shadowColor);
         rhiCommandBuffer.pushVec2("uBorder", rectCmd.borderWidth * sourceValue, 0.0F);
         updateState32(rhiCommandBuffer, "uBorderColor", rectCmd.borderColor);
         updateState32(rhiCommandBuffer, "uBorderColor2", rectCmd.secondaryBorderColor);
         rhiCommandBuffer.pushFloat("uOpacity", 1.0F);
         rhiCommandBuffer.pushFloat("uEdgeSoftness", 0.0F);
         rhiCommandBuffer.pushInt("uHasBlur", 0);
         rhiCommandBuffer.pushVec4("uGradientEnd", 0.0F, 0.0F, 0.0F, 0.0F);
         rhiCommandBuffer.pushInt("uInsetShadow", 0);
         rhiCommandBuffer.pushFloat("uInnerHighlight", 0.0F);
         rhiCommandBuffer.pushFloat("uInnerHighlightSize", 0.0F);
         CompositorIntersectService.apply(rhiCommandBuffer, rectCmd.clip, sourceValue, currentValue);
         rhiCommandBuffer.draw(4, 1, 0);
         CompositorIntersectService.clear(rhiCommandBuffer, rectCmd.clip);
      }
   }

   private static void updateState32(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
      rhiCommandBuffer.pushVec4(
         text,
         (value >> 16 & 0xFF) / 255.0F,
         (value >> 8 & 0xFF) / 255.0F,
         (value & 0xFF) / 255.0F,
         (value >> 24 & 0xFF) / 255.0F
      );
   }

   private static boolean checkCondition6(
      float value, float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, float inputValue, float outputValue, float resultValue, float candidateValue, float selectedValue
   ) {
      return Float.isFinite(value)
         && Float.isFinite(currentValue)
         && Float.isFinite(nextValue)
         && Float.isFinite(previousValue)
         && Float.isFinite(sourceValue)
         && Float.isFinite(targetValue)
         && Float.isFinite(inputValue)
         && Float.isFinite(outputValue)
         && Float.isFinite(resultValue)
         && Float.isFinite(candidateValue)
         && Float.isFinite(selectedValue);
   }

   private static void updateState33(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
      float currentValue = (value >> 24 & 0xFF) / 255.0F;
      rhiCommandBuffer.pushVec4(
         text,
         (value >> 16 & 0xFF) / 255.0F * currentValue,
         (value >> 8 & 0xFF) / 255.0F * currentValue,
         (value & 0xFF) / 255.0F * currentValue,
         currentValue
      );
   }

   public int readPixel(int value, int currentValue, int nextValue) {
      ByteBuffer byteBuffer = MemoryUtil.memAlloc(4);
      GL11.glReadPixels(value, nextValue - currentValue - 1, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
      int previousValue = byteBuffer.get(0) & 255;
      int sourceValue = byteBuffer.get(1) & 255;
      int targetValue = byteBuffer.get(2) & 255;
      MemoryUtil.memFree(byteBuffer);
      return 0xFF000000 | previousValue << 16 | sourceValue << 8 | targetValue;
   }

   public RhiOperationHandler device() {
      return this.rhiOperationHandler;
   }

   public CompositorRenderer postFX() {
      return this.renderer2;
   }

   public SvgQueueService vectorRenderer() {
      return this.svgQueueService;
   }

   public SvgGetTextureService svgRenderer() {
      if (this.svgGetTextureService == null && this.rhiOperationHandler != null) {
         this.svgGetTextureService = new SvgGetTextureService();
         this.svgGetTextureService.init(this.rhiOperationHandler);
      }

      return this.svgGetTextureService;
   }

   public float[] currentClip() {
      return this.float3;
   }

   public Map<TextMode, TextTextureService> fontAtlases() {
      return this.entries;
   }

   public void circle(float value, float currentValue, float nextValue, int previousValue) {
      float sourceValue = this.calculateValue(nextValue);
      this.roundedRect(value - sourceValue, currentValue - sourceValue, sourceValue * 2.0F, sourceValue * 2.0F, nextValue, previousValue);
   }

   public void pushClip(float x, float y, float width, float height) {
      this.pushClip(x, y, width, height, 0.0F);
   }

   public void pushClip(float x, float y, float width, float height, float value) {
      this.pushClip(x, y, width, height, value, value, value, value);
   }

   public void pushClip(float x, float y, float width, float height, float value, float currentValue, float nextValue, float previousValue) {
      this.items33.add(this.float3);
      this.float3 = CompositorIntersectService.intersect(
         this.float3,
         x,
         y,
         width,
         height,
         this.calculateValue(value),
         this.calculateValue(currentValue),
         this.calculateValue(nextValue),
         this.calculateValue(previousValue)
      );
   }

   public void popClip() {
      this.float3 = this.items33.isEmpty() ? null : this.items33.remove(this.items33.size() - 1);
   }

   public void circle(float value, float currentValue, float nextValue, int previousValue, float sourceValue) {
      float targetValue = this.calculateValue(nextValue);
      this.roundedRect(value - targetValue, currentValue - targetValue, targetValue * 2.0F, targetValue * 2.0F, nextValue, previousValue, sourceValue);
   }

   private void updateState34() {
      try {
         this.bufferHandle = this.rhiOperationHandler
            .createBuffer(
               RhiBlendStateService.BufferUsage.VERTEX.bit,
               RhiBlendStateService.BufferAccess.STATIC,
               new float[]{0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F}
            );
         this.samplerHandle = this.rhiOperationHandler.createSampler(RhiBlendStateService.SamplerDescriptor.NEAREST_CLAMP);
         RhiBlendStateService.VertexLayout currentVertexLayout = RhiBlendStateService.VertexLayout.of(
            8, new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L)
         );
         RhiBlendStateService.ShaderHandle shaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("sdf_rect"));
         RhiBlendStateService.ShaderHandle currentShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("sdf_rect"));
         this.pipelineHandle = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(shaderHandle)
                  .fragment(currentShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(shaderHandle);
         this.rhiOperationHandler.destroyShader(currentShaderHandle);

         try {
            RhiBlendStateService.ShaderHandle nextShaderHandle = this.rhiOperationHandler
               .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("sdf_rect_batch"));
            RhiBlendStateService.ShaderHandle previousShaderHandle = this.rhiOperationHandler
               .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("sdf_rect_batch"));
            this.pipelineHandle2 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(nextShaderHandle)
                     .fragment(previousShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
            this.rhiOperationHandler.destroyShader(nextShaderHandle);
            this.rhiOperationHandler.destroyShader(previousShaderHandle);
            this.enabled2 = true;
            CoreIsInitializedHandler.LOGGER.info("ellice: SSBO batching enabled (GLSL 430)");
         } catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.info("ellice: SSBO not available, using per-rect uniforms (GLSL 330)");
            this.enabled2 = false;
         }

         RhiBlendStateService.ShaderHandle sourceShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("texture_quad"));
         RhiBlendStateService.ShaderHandle targetShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("texture_quad"));
         this.pipelineHandle3 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(sourceShaderHandle)
                  .fragment(targetShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(sourceShaderHandle);
         this.rhiOperationHandler.destroyShader(targetShaderHandle);
         RhiBlendStateService.ShaderHandle inputShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("glass_panel"));
         RhiBlendStateService.ShaderHandle outputShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("glass_panel"));
         this.pipelineHandle5 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(inputShaderHandle)
                  .fragment(outputShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(inputShaderHandle);
         this.rhiOperationHandler.destroyShader(outputShaderHandle);
         RhiBlendStateService.ShaderHandle resultShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("texture_quad"));
         RhiBlendStateService.ShaderHandle candidateShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("sdf_icon"));
         this.pipelineHandle4 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(resultShaderHandle)
                  .fragment(candidateShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(resultShaderHandle);
         this.rhiOperationHandler.destroyShader(candidateShaderHandle);
         RhiBlendStateService.ShaderHandle selectedShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("texture_quad"));
         RhiBlendStateService.ShaderHandle defaultShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("filtered_texture"));
         this.pipelineHandle6 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(selectedShaderHandle)
                  .fragment(defaultShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(selectedShaderHandle);
         this.rhiOperationHandler.destroyShader(defaultShaderHandle);
         RhiBlendStateService.ShaderHandle initialShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle resolvedShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            initialShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("texture_quad"));
            resolvedShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("ui_material"));
            this.pipelineHandle7 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(initialShaderHandle)
                     .fragment(resolvedShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
         } catch (Exception currentException) {
            CoreIsInitializedHandler.LOGGER.warn("ellice material pipeline disabled", currentException);
            this.pipelineHandle7 = null;
         } finally {
            if (initialShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(initialShaderHandle);
            }

            if (resolvedShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(resolvedShaderHandle);
            }
         }

         RhiBlendStateService.ShaderHandle computedShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
         RhiBlendStateService.ShaderHandle cachedShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("gradient"));
         this.pipelineHandle8 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(computedShaderHandle)
                  .fragment(cachedShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(computedShaderHandle);
         this.rhiOperationHandler.destroyShader(cachedShaderHandle);
         RhiBlendStateService.ShaderHandle pendingShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
         RhiBlendStateService.ShaderHandle activeShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("mesh_card"));
         this.pipelineHandle9 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(pendingShaderHandle)
                  .fragment(activeShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(pendingShaderHandle);
         this.rhiOperationHandler.destroyShader(activeShaderHandle);
         RhiBlendStateService.ShaderHandle fallbackShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle primaryShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            fallbackShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("spotify_bg"));
            primaryShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("module_studio"));
            this.pipelineHandle10 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(fallbackShaderHandle)
                     .fragment(primaryShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
         } catch (Exception nextException) {
            CoreIsInitializedHandler.LOGGER.warn("Module Studio shader unavailable; using the mesh fallback", nextException);
         } finally {
            if (fallbackShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(fallbackShaderHandle);
            }

            if (primaryShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(primaryShaderHandle);
            }
         }

         RhiBlendStateService.ShaderHandle secondaryShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle tertiaryShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            secondaryShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("spotify_bg"));
            tertiaryShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("menu_horizon"));
            this.pipelineHandle11 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(secondaryShaderHandle)
                     .fragment(tertiaryShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
         } catch (Exception previousException) {
            CoreIsInitializedHandler.LOGGER.warn("Menu shader unavailable; using the mesh fallback", previousException);
         } finally {
            if (secondaryShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(secondaryShaderHandle);
            }

            if (tertiaryShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(tertiaryShaderHandle);
            }
         }

         RhiBlendStateService.ShaderHandle temporaryShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
         RhiBlendStateService.ShaderHandle requestedShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("liquid_switcher"));
         this.pipelineHandle12 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(temporaryShaderHandle)
                  .fragment(requestedShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(temporaryShaderHandle);
         this.rhiOperationHandler.destroyShader(requestedShaderHandle);

         try {
            RhiBlendStateService.ShaderHandle actualShaderHandle = this.rhiOperationHandler
               .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
            RhiBlendStateService.ShaderHandle expectedShaderHandle = this.rhiOperationHandler
               .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("font_loading"));
            this.pipelineHandle18 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(actualShaderHandle)
                     .fragment(expectedShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
            this.rhiOperationHandler.destroyShader(actualShaderHandle);
            this.rhiOperationHandler.destroyShader(expectedShaderHandle);
         } catch (Exception sourceException) {
            CoreIsInitializedHandler.LOGGER.warn("ellice loading-spinner pipeline disabled", sourceException);
            this.pipelineHandle18 = null;
         }

         RhiBlendStateService.ShaderHandle minimumShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("sdf_rect"));
         RhiBlendStateService.ShaderHandle maximumShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("sdf_tooltip"));
         this.pipelineHandle13 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(minimumShaderHandle)
                  .fragment(maximumShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(minimumShaderHandle);
         this.rhiOperationHandler.destroyShader(maximumShaderHandle);
         RhiBlendStateService.ShaderHandle startShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
         RhiBlendStateService.ShaderHandle endShaderHandle = this.rhiOperationHandler
            .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("colorpicker"));
         this.pipelineHandle14 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(startShaderHandle)
                  .fragment(endShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build()
            );
         this.rhiOperationHandler.destroyShader(startShaderHandle);
         this.rhiOperationHandler.destroyShader(endShaderHandle);
         RhiBlendStateService.ShaderHandle localShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle storedShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            localShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
            storedShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("curve_graph"));
            this.pipelineHandle15 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(localShaderHandle)
                     .fragment(storedShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
         } catch (Exception targetException) {
            CoreIsInitializedHandler.LOGGER.warn("ellice curve-graph shader disabled", targetException);
            this.pipelineHandle15 = null;
         } finally {
            if (localShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(localShaderHandle);
            }

            if (storedShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(storedShaderHandle);
            }
         }

         RhiBlendStateService.ShaderHandle createdShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle updatedShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            createdShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
            updatedShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("audio_waveform"));
            this.pipelineHandle16 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(createdShaderHandle)
                     .fragment(updatedShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
            this.compositorAmplitudeService = null;
         } catch (Exception inputException) {
            CoreIsInitializedHandler.LOGGER.warn("ellice audio waveform shader disabled", inputException);
            this.pipelineHandle16 = null;
         } finally {
            if (createdShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(createdShaderHandle);
            }

            if (updatedShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(updatedShaderHandle);
            }
         }

         RhiBlendStateService.ShaderHandle removedShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         RhiBlendStateService.ShaderHandle firstShaderHandle = RhiBlendStateService.ShaderHandle.NONE;

         try {
            removedShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("gradient"));
            firstShaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("time_series"));
            this.pipelineHandle17 = this.rhiOperationHandler
               .createPipeline(
                  RhiBuilderData.graphics()
                     .vertex(removedShaderHandle)
                     .fragment(firstShaderHandle)
                     .vertexLayout(currentVertexLayout)
                     .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                     .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                     .build()
               );
            this.compositorEmptyService = null;
         } catch (Exception outputException) {
            CoreIsInitializedHandler.LOGGER.warn("ellice time-series shader disabled", outputException);
            this.pipelineHandle17 = null;
         } finally {
            if (removedShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(removedShaderHandle);
            }

            if (firstShaderHandle.valid()) {
               this.rhiOperationHandler.destroyShader(firstShaderHandle);
            }
         }

         this.compositorShutdownService = new CompositorShutdownService(this.rhiOperationHandler, this.bufferHandle);
         this.compositorShutdownService.init();
         this.entries = new EnumMap<>(TextMode.class);

         for (TextMode textMode : TextMode.values()) {
            TextTextureService textTexture = new TextTextureService(textMode);
            textTexture.generate(this.rhiOperationHandler);
            if (textTexture.isReady()) {
               this.entries.put(textMode, textTexture);
            }
         }

         this.textTextureService = new TextTextureService(TextMode.REGULAR, "/assets/ellice/fonts/Minecraft", "/assets/ellice/fonts/Minecraft.ttf");
         this.textTextureService.generate(this.rhiOperationHandler);
         this.textTextService = new TextTextService(this.rhiOperationHandler, this.entries, this.textTextureService.isReady() ? this.textTextureService : null);
         this.renderer2 = new CompositorRenderer(this.rhiOperationHandler, this.bufferHandle);
         this.svgQueueService = new SvgQueueService();
         this.svgQueueService.init(this.rhiOperationHandler);
         this.enabled3 = true;
         CoreIsInitializedHandler.LOGGER.info("ellice compositor initialized");
      } catch (Throwable resultException) {
         CoreIsInitializedHandler.LOGGER.error("Failed to initialize compositor", resultException);
         FatalIsTrippedService.trip(resultException, FatalCaptureService.Kind.RENDERER);
      }
   }

   private void updateState35(int value) {
      if (this.bufferHandle2 == null || value > this.count) {
         if (this.bufferHandle2 != null) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle2);
         }

         this.count = Math.max(value, 256);
         this.bufferHandle2 = this.rhiOperationHandler
            .createBuffer(this.count * 40L * 4L, RhiBlendStateService.BufferUsage.STORAGE.bit, RhiBlendStateService.BufferAccess.STREAM);
      }
   }

   public void shutdown() {
      if (this.executor != null) {
         this.executor.shutdownNow();
         this.executor = null;
      }

      this.concurrentLinkedQueue.clear();
      this.text4.clear();
      if (this.enabled3) {
         if (this.svgGetTextureService != null) {
            this.svgGetTextureService.shutdown();
         }

         if (this.svgQueueService != null) {
            this.svgQueueService.shutdown();
         }

         if (this.renderer2 != null) {
            this.renderer2.shutdown();
         }

         this.textTextService.shutdown();

         for (TextTextureService textTexture : this.entries.values()) {
            textTexture.shutdown(this.rhiOperationHandler);
         }

         if (this.textTextureService != null) {
            this.textTextureService.shutdown(this.rhiOperationHandler);
         }

         this.compositorShutdownService.shutdown();
         if (this.samplerHandle != null) {
            this.rhiOperationHandler.destroySampler(this.samplerHandle);
         }

         this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
         if (this.pipelineHandle2 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle2);
         }

         if (this.pipelineHandle3 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle3);
         }

         if (this.pipelineHandle4 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle4);
         }

         if (this.pipelineHandle5 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle5);
         }

         if (this.pipelineHandle6 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle6);
         }

         if (this.pipelineHandle7 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle7);
         }

         if (this.pipelineHandle8 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle8);
         }

         if (this.pipelineHandle9 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle9);
         }

         if (this.pipelineHandle10 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle10);
         }

         if (this.pipelineHandle11 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle11);
         }

         if (this.pipelineHandle12 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle12);
         }

         if (this.pipelineHandle18 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle18);
         }

         if (this.pipelineHandle13 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle13);
         }

         if (this.pipelineHandle14 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle14);
         }

         if (this.pipelineHandle15 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle15);
         }

         if (this.pipelineHandle16 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle16);
         }

         if (this.pipelineHandle17 != null) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle17);
         }

         this.compositorEmptyService = null;
         this.compositorAmplitudeService = null;
         if (this.bufferHandle2 != null) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle2);
         }

         if (this.textureHandle.valid()) {
            this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
            this.rhiOperationHandler.destroyTexture(this.textureHandle);
            this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
            this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
         }

         if (this.textureHandle2.valid()) {
            this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
            this.rhiOperationHandler.destroyTexture(this.textureHandle2);
            this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
            this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
         }

         for (CompositorPushPresentationScaleService.LayerCache layerCache : this.entries2.values()) {
            if (layerCache.texture.valid()) {
               this.rhiOperationHandler.destroyFramebuffer(layerCache.fbo);
               this.rhiOperationHandler.destroyTexture(layerCache.texture);
            }
         }

         this.entries2.clear();
         this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
         this.enabled3 = false;
         CoreIsInitializedHandler.LOGGER.info("ellice compositor shut down");
      }
   }

   private record ColorPickerCmd(
      float x, float y, float w, float h, float hue, float sat, float bri, float radius, int mode, float opacity, float[] clip
   ) {
   }

   private record CurveGraphCmd(
      float x,
      float y,
      float w,
      float h,
      float plotX,
      float plotY,
      float plotW,
      float plotH,
      float radius,
      ModuleSetting.CurveValue curve,
      int activeHandle,
      float opacity,
      float edgeSoftness,
      float metricScale,
      float curveWidth,
      float curveFringeWidth,
      CompositorPushPresentationScaleService.CurveGraphStyle style,
      int rectIdx,
      float[] clip
   ) {
   }

   public record CurveGraphStyle(
      int backgroundColor,
      int borderColor,
      int gridColor,
      int axisColor,
      int curveColor,
      int curveFringeColor,
      int fillColor,
      int guideColor,
      int handleColor,
      int activeHandleColor,
      int handleBorderColor,
      int endpointColor,
      float curveWidth,
      float curveFringeWidth
   ) {
      public CurveGraphStyle(
         int backgroundColor,
         int borderColor,
         int gridColor,
         int axisColor,
         int curveColor,
         int curveFringeColor,
         int fillColor,
         int guideColor,
         int handleColor,
         int activeHandleColor,
         int handleBorderColor,
         int endpointColor,
         float curveWidth,
         float curveFringeWidth
      ) {
         if (!Float.isFinite(curveWidth) || curveWidth <= 0.0F) {
            throw new IllegalArgumentException("Curve width must be finite and positive");
         }

         if (Float.isFinite(curveFringeWidth) && !(curveFringeWidth < curveWidth)) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.gridColor = gridColor;
            this.axisColor = axisColor;
            this.curveColor = curveColor;
            this.curveFringeColor = curveFringeColor;
            this.fillColor = fillColor;
            this.guideColor = guideColor;
            this.handleColor = handleColor;
            this.activeHandleColor = activeHandleColor;
            this.handleBorderColor = handleBorderColor;
            this.endpointColor = endpointColor;
            this.curveWidth = curveWidth;
            this.curveFringeWidth = curveFringeWidth;
         } else {
            throw new IllegalArgumentException("Curve fringe width must be finite and at least the curve width");
         }
      }
   }

   private record GlassCmd(
      float x,
      float y,
      float w,
      float h,
      float tl,
      float tr,
      float br,
      float bl,
      int tintColor,
      int tintColor2,
      float blur,
      float opacity,
      float saturation,
      float brightness,
      float contrast,
      float refraction,
      float noise,
      float chromatic,
      float borderWidth,
      int borderColor,
      int secondaryBorderColor,
      int highlightColor,
      float edgeSoftness,
      float edgeWobble,
      float edgeWobbleRadius,
      float rubberSide,
      float rubberTangent,
      float rubberAmount,
      float rubberWidth,
      int rectIdx,
      float[] clip
   ) {
   }

   private record GradCmd(float x, float y, float w, float h, int colorTop, int colorBottom, float[] clip) {
   }

   private static final class LayerCache {
      RhiBlendStateService.TextureHandle texture = RhiBlendStateService.TextureHandle.NONE;
      RhiBlendStateService.FramebufferHandle fbo = RhiBlendStateService.FramebufferHandle.NONE;
      int fbW;
      int fbH;
      int version;
      int textRevision;
      long effectsRevision;
      float x;
      float y;
      float w;
      float h;
      long lastUsedFrame;
   }

   public record LayerEffect(
      float x,
      float y,
      float w,
      float h,
      float tl,
      float tr,
      float br,
      float bl,
      float opacity,
      float blur,
      float saturation,
      float brightness,
      float contrast,
      float grayscale,
      int tintColor,
      float edgeSoftness,
      int maskShape,
      boolean maskInvert,
      int cacheKey,
      int cacheVersion,
      int material,
      float materialIntensity,
      float materialScale,
      float materialSpeed,
      float materialDistortion,
      float materialGlow,
      int materialColor,
      float hoverProgress,
      float pressProgress
   ) {
   }

   private record LayerRanges(
      int rectStart,
      int rectEnd,
      int glassStart,
      int glassEnd,
      int texStart,
      int texEnd,
      int sdfStart,
      int sdfEnd,
      int txtStart,
      int txtEnd,
      int gradStart,
      int gradEnd,
      int cpStart,
      int cpEnd,
      int curveGraphStart,
      int curveGraphEnd,
      int waveformStart,
      int waveformEnd,
      int timeSeriesStart,
      int timeSeriesEnd,
      int ovStart,
      int ovEnd,
      int meshStart,
      int meshEnd,
      int liquidSwitchStart,
      int liquidSwitchEnd,
      int tooltipBubbleStart,
      int tooltipBubbleEnd,
      int vecStart,
      int vecEnd
   ) {
   }

   private record LiquidSwitchCmd(
      float x,
      float y,
      float w,
      float h,
      float radius,
      float activeX,
      float activeW,
      float hoverX,
      float hoverW,
      float hoverMix,
      float transition,
      float opacity,
      int accentColor,
      int rectIdx,
      float[] clip
   ) {
   }

   private record LoadingCmd(float x, float y, float w, float h, float radius, float progress, int accent, float opacity) {
   }

   private record MenuPaint(float time, float servers, float worlds, boolean light, float focus) {
   }

   private record MeshCmd(
      float x,
      float y,
      float w,
      float h,
      float radius,
      int c0,
      int c1,
      int c2,
      int c3,
      int c4,
      float opacity,
      int rectIdx,
      float[] clip,
      CompositorPushPresentationScaleService.StudioPaint studio,
      CompositorPushPresentationScaleService.MenuPaint menuTime
   ) {
   }

   private record PendingFont(String key, TextTextureService atlas) {
   }

   private record RectCmd(
      float x,
      float y,
      float w,
      float h,
      float tl,
      float tr,
      float br,
      float bl,
      float r,
      float g,
      float b,
      float a,
      float blur,
      float shadow,
      int shadowColor,
      float borderWidth,
      int borderColor,
      float panelOpacity,
      float edgeSoftness,
      boolean insetShadow,
      float gr2,
      float gg2,
      float gb2,
      float ga2,
      int secondaryBorderColor,
      float innerHighlight,
      float innerHighlightSize,
      float[] clip
   ) {
   }

   private record SdfIconCmd(
      RhiBlendStateService.TextureHandle texture,
      RhiBlendStateService.TextureHandle morphTexture,
      float morph,
      float x,
      float y,
      float w,
      float h,
      float opacity,
      int tintColor,
      float rotation,
      float pxRange,
      float[] clip
   ) {
   }

   public record StudioPaint(int kind, float time, float a, float b, float c, float d) {
   }

   private record TexCmd(
      RhiBlendStateService.TextureHandle texture,
      float x,
      float y,
      float w,
      float h,
      float opacity,
      float tl,
      float tr,
      float br,
      float bl,
      int tintColor,
      float rotation,
      float edgeSoftness,
      float u0,
      float v0,
      float u1,
      float v1,
      boolean opaque,
      boolean nearest,
      float[] clip
   ) {
   }

   private record TimeSeriesCmd(
      float x, float y, float w, float h, CompositorEmptyService data, int color, float opacity, float metricScale, int rectIdx, float[] clip
   ) {
   }

   private record TooltipBubbleCmd(
      float x,
      float y,
      float w,
      float h,
      float radius,
      float tailX,
      float tailW,
      float tailH,
      float smoothK,
      int color,
      int gradientEnd,
      float shadow,
      int shadowColor,
      float borderWidth,
      int borderColor,
      float innerHighlight,
      float innerHighlightSize,
      boolean blur,
      float opacity,
      float edgeSoftness,
      int rectIdx,
      float[] clip
   ) {
   }

   private record WaveformCmd(
      float x,
      float y,
      float w,
      float h,
      CompositorAmplitudeService samples,
      float progress,
      float preview,
      boolean playing,
      boolean loaded,
      float opacity,
      int accent,
      int remaining,
      int ink,
      float metricScale,
      int rectIdx,
      float[] clip
   ) {
   }
}

