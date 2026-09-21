package dev.felix.ellice.ui.scene;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.feature.nametags.NametagsData;
import dev.felix.ellice.feature.nametags.NametagsRenderer;
import dev.felix.ellice.feature.terrain.TerrainReservedService;
import dev.felix.ellice.feature.terrain.TerrainKindData;
import dev.felix.ellice.feature.terrain.TerrainViewportService;
import dev.felix.ellice.feature.terrain.TerrainMapController;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;
import dev.felix.ellice.ui.text.TextCodec;
import dev.felix.ellice.ui.theme.ThemeMixService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import org.joml.Vector3d;

public final class ScenePointerService extends ScenePctService<ScenePointerService> {
   private final TerrainMapController renderer;
   private final boolean enabled;
   private ScenePointerService.Pointer pointer2 = new ScenePointerService.Pointer(0.0F, 0.0F, false, false, false, 1.0F);
   private Consumer<Vector3d> consumer = item -> {};
   private Consumer<TerrainKindData> consumer2 = item -> {};
   private float value;
   private float value2;
   private float value3;
   private float value4;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private long timestamp;
   private long timestamp2;
   private float value5;
   private float value6;
   private float value7;
   private TerrainKindData terrainKindData;
   private float value8;
   private float value9;
   private float value10;
   private float value11;
   private boolean enabled6;
   private final TerrainReservedService terrainReservedService = new TerrainReservedService();
   private TerrainKindData terrainKindData2;

   public ScenePointerService(TerrainMapController terrainMapController, boolean currentEnabled) {
      this.renderer = terrainMapController;
      this.enabled = currentEnabled;
      this.interactive(currentEnabled).pointerEvents(currentEnabled).stopPropagation(true).clip(true).cursorStyle(ScenePctService.CursorStyle.DEFAULT);
      this.addChild(this.terrainReservedService);
   }

   public ScenePointerService pointer(ScenePointerService.Pointer currentPointer) {
      this.pointer2 = currentPointer;
      return this;
   }

   public ScenePointerService radius(float value) {
      this.value7 = value;
      return this;
   }

   public ScenePointerService onPlace(Consumer<Vector3d> currentConsumer) {
      this.consumer = currentConsumer;
      return this;
   }

   public ScenePointerService onWaypoint(Consumer<TerrainKindData> consumer) {
      this.consumer2 = consumer;
      return this;
   }

   public ScenePointerService reserved(List<TerrainReservedService.Box> items) {
      this.terrainReservedService.reserved(items);
      return this;
   }

   public ScenePointerService reconcileChildren(List<ScenePctService<?>> items) {
      ArrayList arrayList = new ArrayList();
      arrayList.add(this.terrainReservedService);

      for (ScenePctService scenePct : items) {
         if (scenePct != this.terrainReservedService) {
            arrayList.add(scenePct);
         }
      }

      return (ScenePointerService)super.reconcileChildren(arrayList);
   }

   @Override
   protected void onPress(float currentValue, float nextValue) {
      this.value = currentValue - this.cx;
      this.value2 = nextValue - this.cy;
      this.enabled4 = this.pointer2.control;
      this.enabled5 = this.pointer2.shift;
      this.timestamp = System.nanoTime();
      this.renderer.camera(true).beginDrag();
      this.terrainKindData = !this.enabled4 && !this.enabled5 ? this.createTerrainKindData(this.value, this.value2) : null;
      this.enabled6 = false;
      this.value10 = this.value;
      this.value11 = this.value2;
      if (this.terrainKindData != null) {
         Vector3d currentX = this.renderer
            .camera(true)
            .frame()
            .project(this.terrainKindData.x(), this.terrainKindData.y(), this.terrainKindData.z());
         this.value8 = currentX == null ? 0.0F : this.value - (float)currentX.x;
         this.value9 = currentX == null ? 0.0F : this.value2 - (float)currentX.y;
         this.renderer.beginWaypointDrag(this.terrainKindData);
      }
   }

   @Override
   protected void updateWhilePressed(float currentValue, float nextValue) {
      float previousValue = currentValue - this.cx;
      float sourceValue = nextValue - this.cy;
      if (previousValue != this.value || sourceValue != this.value2) {
         if (this.terrainKindData != null) {
            if (Math.hypot(previousValue - this.value10, sourceValue - this.value11) > 3.0) {
               this.enabled6 = true;
            }

            if (this.enabled6) {
               this.renderer.moveWaypointPreview(this.renderer.pick(previousValue - this.value8, sourceValue - this.value9));
            }
         } else if (this.enabled4) {
            this.renderer.camera(true).orbit(previousValue - this.value, sourceValue - this.value2);
         } else {
            this.renderer.camera(true).pan(this.value, this.value2, previousValue, sourceValue, SceneDtService.dt());
         }

         this.value = previousValue;
         this.value2 = sourceValue;
         this.timestamp = System.nanoTime();
      }
   }

   @Override
   protected void onRelease(boolean enabled) {
      if (System.nanoTime() - this.timestamp > 100000000L) {
         this.renderer.camera(true).beginDrag();
      }

      this.renderer.camera(true).endDrag();
      if (this.terrainKindData != null) {
         TerrainKindData currentTerrainKindData = this.renderer.finishWaypointDrag(this.enabled6);
         this.terrainKindData = null;
         if (currentTerrainKindData != null) {
            this.consumer2.accept(currentTerrainKindData);
         }
      } else if (enabled) {
         TerrainKindData nextTerrainKindData = this.createTerrainKindData(this.value, this.value2);
         if (nextTerrainKindData != null) {
            this.consumer2.accept(nextTerrainKindData);
         } else if (this.enabled5) {
            Vector3d vector3d = this.renderer.pick(this.value, this.value2);
            if (vector3d != null) {
               this.consumer.accept(vector3d);
            }
         } else {
            long longValue = System.nanoTime();
            if (longValue - this.timestamp2 < 350000000L
               && Math.hypot(this.value - this.value5, this.value2 - this.value6)
                  < 5.0) {
               this.renderer.zoom(2.0, this.value, this.value2);
               this.timestamp2 = 0L;
            } else {
               this.timestamp2 = longValue;
               this.value5 = this.value;
               this.value6 = this.value2;
            }
         }
      }
   }

   @Override
   protected boolean handleScroll(float value) {
      if (!this.enabled) {
         return false;
      }

      if (this.terrainKindData != null) {
         return true;
      }

      ScenePctService.PresentationTransform currentPresentationTransform = this.presentationTransform();
      this.renderer.zoom(value, currentPresentationTransform.inverseX(this.pointer2.x) - this.cx, currentPresentationTransform.inverseY(this.pointer2.y) - this.cy);
      return true;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this.renderer.viewport(this.enabled, this.cw, this.ch, this.pointer2.density);
      if (this.enabled) {
         this.updateState();
      }

      RhiBlendStateService.TextureHandle textureHandle = this.renderer.texture(this.enabled);
      if (textureHandle.valid()) {
         compositorPushPresentationScale.drawTextureRegion(
            textureHandle,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.effectiveOpacity,
            this.value7,
            this.value7,
            this.value7,
            this.value7,
            0,
            0.0F,
            0.0F,
            0.0F,
            1.0F,
            1.0F,
            0.0F
         );
      } else {
         compositorPushPresentationScale.roundedRect(
            this.cx, this.cy, this.cw, this.ch, this.value7, mulAlpha(MaterialIsLightService.SURFACE_LOWEST, this.effectiveOpacity)
         );
      }

      compositorPushPresentationScale.nextLayer();
      TerrainViewportService.Frame currentFrame = this.renderer.camera(this.enabled).frame();
      if (this.renderer.world() != null) {
         Vector3d vector3d = this.renderer.world().player();
         Vector3d currentVector3d = currentFrame.project(vector3d.x, vector3d.y + 1.0, vector3d.z);
         if (currentVector3d != null) {
            float value = this.cx + (float)currentVector3d.x;
            float currentValue = this.cy + (float)currentVector3d.y;
            compositorPushPresentationScale.roundedRect(
               value - 7.0F,
               currentValue - 7.0F,
               14.0F,
               14.0F,
               7.0F,
               mulAlpha(-15063228, this.effectiveOpacity)
            );
            compositorPushPresentationScale.roundedRect(
               value - 4.0F,
               currentValue - 4.0F,
               8.0F,
               8.0F,
               4.0F,
               mulAlpha(-4794881, this.effectiveOpacity)
            );
         }
      }

      ArrayList arrayList = new ArrayList();
      Vector3d nextVector3d = this.renderer.world() == null ? currentFrame.focus() : this.renderer.world().player();
      TerrainKindData currentTerrainKindData = this.renderer.navigation().target();

      for (TerrainKindData nextTerrainKindData : this.renderer
         .visibleWaypoints()
         .stream()
         .sorted(
            Comparator.<TerrainKindData>comparingInt(item -> item.id().equals(this.renderer.selected()) ? 0 : 1)
               .thenComparingDouble(item -> currentFrame.focus().distanceSquared(item.x(), item.y(), item.z()))
         )
         .limit(64L)
         .toList()) {
         Vector3d currentX = currentFrame.project(nextTerrainKindData.x(), nextTerrainKindData.y(), nextTerrainKindData.z());
         if (currentX != null) {
            arrayList.add(
               new TerrainReservedService.Anchor(
                  nextTerrainKindData,
                  (float)currentX.x,
                  (float)currentX.y,
                  nextVector3d.distance(nextTerrainKindData.x(), nextTerrainKindData.y(), nextTerrainKindData.z()),
                  nextTerrainKindData.id().equals(this.renderer.selected()),
                  currentTerrainKindData != null && currentTerrainKindData.id().equals(nextTerrainKindData.id())
               )
            );
         }
      }

      this.terrainReservedService
         .present(
            arrayList,
            this.enabled ? TerrainReservedService.View.MAP : TerrainReservedService.View.MINI,
            this.terrainKindData2 == null ? null : this.terrainKindData2.id(),
            this.enabled6 && this.terrainKindData != null
         );
      if (this.enabled && this.renderer.showPlayerNames()) {
         this.updateState2(compositorPushPresentationScale, currentFrame);
      }

      if (this.enabled && this.renderer.loadedSections() == 0) {
         String currentText = this.renderer.cache().pending() > 0 ? "Loading terrain…" : "No explored terrain here";
         compositorPushPresentationScale.text(
            this.cx + 24.0F,
            this.cy + this.ch - 76.0F,
            currentText,
            12.0F,
            mulAlpha(MaterialIsLightService.ON_SURFACE_VARIANT, this.effectiveOpacity),
            MaterialIsLightService.BODY
         );
      }
   }

   private void updateState() {
      ScenePctService.PresentationTransform currentPresentationTransform = this.presentationTransform();
      float value = currentPresentationTransform.inverseX(this.pointer2.x) - this.cx;
      float currentValue = currentPresentationTransform.inverseY(this.pointer2.y) - this.cy;
      if (this.pointer2.orbit && !this.enabled3 && this.isHovered() && this.terrainKindData == null) {
         this.enabled2 = true;
         this.value3 = value;
         this.value4 = currentValue;
         this.renderer.camera(true).beginDrag();
      }

      if (this.enabled2) {
         if (this.pointer2.orbit) {
            this.renderer.camera(true).orbit(value - this.value3, currentValue - this.value4);
            this.value3 = value;
            this.value4 = currentValue;
         } else {
            this.enabled2 = false;
            this.renderer.camera(true).endDrag();
         }
      }

      this.enabled3 = this.pointer2.orbit;
      this.terrainKindData2 = this.isHovered() && !this.enabled2 ? this.createTerrainKindData(value, currentValue) : null;
      this.cursorStyle(
         !this.pointer2.shift && (!this.enabled6 || this.terrainKindData == null)
            ? (this.terrainKindData2 != null ? ScenePctService.CursorStyle.POINTER : ScenePctService.CursorStyle.DEFAULT)
            : ScenePctService.CursorStyle.CROSSHAIR
      );
   }

   private TerrainKindData createTerrainKindData(float value, float currentValue) {
      return this.terrainReservedService.hit(value, currentValue);
   }

   private void updateState2(CompositorPushPresentationScaleService compositorPushPresentationScale, TerrainViewportService.Frame frame) {
      compositorPushPresentationScale.requestFontFamily("material-roboto", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Regular.ttf");
      compositorPushPresentationScale.requestFontFamily("material-roboto-medium", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Medium.ttf");
      Vector3d vector3d = this.renderer.world() == null ? null : this.renderer.world().player();
      int index = 0;

      for (CompatLoadedHandler.MapPlayer mapPlayer : this.renderer.players()) {
         if (index >= 16) {
            break;
         }

         Vector3d currentX = frame.project(mapPlayer.x(), mapPlayer.y() + 2.1, mapPlayer.z());
         if (currentX != null) {
            double nextX = vector3d == null ? frame.focus().distance(mapPlayer.x(), mapPlayer.y(), mapPlayer.z()) : vector3d.distance(mapPlayer.x(), mapPlayer.y(), mapPlayer.z());
            String text = mapPlayer.name();
            String currentText = MaterialIsLightService.isLight() ? TextCodec.normalizeWhite(text) : text;
            String nextText = Math.round(nextX) + "m";
            float value = compositorPushPresentationScale.textWidth(nextText, 8.5F, TextMode.REGULAR, "material-roboto");
            String previousText = createText(compositorPushPresentationScale, currentText, 200.0F - value - 6.0F);
            float currentValue = Math.clamp(mapPlayer.health() / mapPlayer.maxHealth(), 0.0F, 1.0F);
            NametagsData nametagsData = new NametagsData(previousText, nextText, -5650945, currentValue, 0.0F, null, false);
            float currentWidth = NametagsRenderer.width(
               compositorPushPresentationScale, nametagsData, 1.0F, Math.max(56.0F, this.cw - 8.0F)
            );
            float currentHeight = NametagsRenderer.height(nametagsData, 1.0F);
            float nextValue = Math.clamp(
               this.cx + (float)currentX.x - currentWidth / 2.0F,
               this.cx + 4.0F,
               Math.max(this.cx + 4.0F, this.cx + this.cw - currentWidth - 4.0F)
            );
            float previousValue = this.cy + (float)currentX.y - currentHeight - 12.0F;
            this.updateState3(compositorPushPresentationScale, previousText, nextText, currentValue, nextValue, previousValue, currentWidth, currentHeight);
            index++;
         }
      }
   }

   private void updateState3(CompositorPushPresentationScaleService compositorPushPresentationScale, String text, String currentText, float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      compositorPushPresentationScale.roundedRect(
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         7.0F,
         7.0F,
         7.0F,
         7.0F,
         mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity),
         0.0F,
         7.0F,
         mulAlpha(1342177280, this.effectiveOpacity),
         0.0F,
         0,
         1.0F
      );
      float targetValue = compositorPushPresentationScale.textWidth(text, 10.5F, TextMode.REGULAR, "material-roboto-medium");
      compositorPushPresentationScale.textLiteral(
         currentValue + 10.0F,
         nextValue + 11.0F,
         text,
         10.5F,
         mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
         MaterialIsLightService.LABEL
      );
      if (!currentText.isEmpty()) {
         compositorPushPresentationScale.textLiteral(
            currentValue + 10.0F + targetValue + 6.0F,
            nextValue + 12.0F,
            currentText,
            8.5F,
            mulAlpha(MaterialIsLightService.ON_SURFACE_VARIANT, this.effectiveOpacity),
            MaterialIsLightService.BODY
         );
      }

      float inputValue = currentValue + 10.0F;
      float outputValue = previousValue - 20.0F;
      float resultValue = nextValue + 6.0F + 20.0F + 4.0F;
      float candidateValue = 4.0F;
      int selectedValue = MaterialIsLightService.layer(MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.ON_SURFACE, 0.18F);
      compositorPushPresentationScale.roundedRect(inputValue, resultValue, outputValue, candidateValue, candidateValue / 2.0F, mulAlpha(selectedValue, this.effectiveOpacity));
      float defaultValue = outputValue * value;
      if (defaultValue > 0.1F) {
         int initialValue = value < 0.5F
            ? ThemeMixService.mix(MaterialIsLightService.ERROR, -14739, value * 2.0F)
            : ThemeMixService.mix(-14739, -10890342, (value - 0.5F) * 2.0F);
         float resolvedValue = Math.min(1.0F, candidateValue * 0.25F);
         float computedValue = resultValue + resolvedValue;
         float cachedValue = Math.max(1.0F, candidateValue - resolvedValue * 2.0F);
         compositorPushPresentationScale.roundedRect(inputValue, computedValue, defaultValue, cachedValue, cachedValue / 2.0F, mulAlpha(initialValue, this.effectiveOpacity));
         float pendingValue = Math.min(1.0F, cachedValue * 0.45F);
         compositorPushPresentationScale.roundedRect(
            inputValue + cachedValue / 2.0F,
            computedValue,
            Math.max(0.0F, defaultValue - cachedValue / 2.0F),
            pendingValue,
            pendingValue / 2.0F,
            mulAlpha(
               ThemeMixService.lighten(initialValue, 0.22F), this.effectiveOpacity * 0.8F
            )
         );
      }
   }

   private static String createText(CompositorPushPresentationScaleService compositorPushPresentationScale, String text, float value) {
      if (compositorPushPresentationScale.textWidth(text, 10.5F, TextMode.REGULAR, "material-roboto-medium") <= value) {
         return text;
      }

      String currentText = text;

      while (
         !currentText.isEmpty()
            && compositorPushPresentationScale.textWidth(currentText + "…", 10.5F, TextMode.REGULAR, "material-roboto-medium") > value
      ) {
         currentText = currentText.substring(0, currentText.offsetByCodePoints(currentText.length(), -1));
      }

      return currentText + "…";
   }

   @Override
   protected void onDetached() {
      this.renderer.camera(this.enabled).endDrag();
      this.renderer.cancelWaypointDrag();
   }

   public record Pointer(float x, float y, boolean orbit, boolean shift, boolean control, float density) {
   }
}

