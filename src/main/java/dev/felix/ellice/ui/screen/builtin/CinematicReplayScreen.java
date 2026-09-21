package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.feature.terrain.CinematicExporter;
import dev.felix.ellice.module.impl.ImplClipsService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;

public final class CinematicReplayScreen implements ScreenOperationHandler {
   private final ImplClipsService implClipsService;
   private MaterialTerrainTooltipsService materialTerrainTooltipsService;
   private ComponentMountService componentMountService;
   private ScreenScreenIdService screenScreenIdService;
   private SceneTextureService sceneTextureService;
   private TIMELINEComponent tIMELINEComponent;
   private float value2 = 1040.0F;
   private float value3 = 720.0F;
   private float value4;
   private float value5;
   private float value6;
   private boolean enabled;
   private boolean enabled2;
   private float value7 = 1.0F;
   private float value8 = 1.0F;
   private double value9;
   private long timestamp;
   private CinematicExporter.State renderer;

   public CinematicReplayScreen(ImplClipsService implClips) {
      this.implClipsService = implClips;
   }

   @Override
   public String id() {
      return "cinematic-replay";
   }

   @Override
   public boolean overlaysPreviousScreen() {
      return true;
   }

   @Override
   public boolean opensAsWindow() {
      return true;
   }

   @Override
   public float backgroundDesaturation() {
      return 0.0F;
   }

   @Override
   public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
      this.screenScreenIdService = screenScreenId;
      this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService().terrainTooltips(true);
      this.materialTerrainTooltipsService
         .id("cinematic-replay.root")
         .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
         .direction(ScenePctService.Direction.NONE)
         .interactive(true);
      this.materialTerrainTooltipsService
         .onLayout(
            () -> {
               float value = this.materialTerrainTooltipsService.computedW();
               float currentValue = this.materialTerrainTooltipsService.computedH();
               if (Math.abs(this.value2 - value) > 0.5F
                  || Math.abs(this.value3 - currentValue) > 0.5F) {
                  this.value2 = value;
                  this.value3 = currentValue;
                  this.updateState();
               }
            }
         );
      this.componentMountService = new ComponentMountService().mount(this::createComponentKeyService, screenScreenId.theme());
      this.componentMountService.absolute().inset(LayoutOperationHandler.px(0.0F)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
      this.materialTerrainTooltipsService.addChild(this.componentMountService);
      return this.materialTerrainTooltipsService;
   }

   @Override
   public void onClose(ScreenScreenIdService screenScreenId) {
      this.enabled2 = false;
      this.updateState3();
      if (this.implClipsService.clips() != null) {
         this.implClipsService.clips().clearPreview();
      }

      this.sceneTextureService = null;
      this.tIMELINEComponent = null;
   }

   @Override
   public void tick(ScreenScreenIdService screenScreenId) {
      CinematicExporter cinematicExporter = this.implClipsService.clips();
      if (cinematicExporter == null) {
         screenScreenId.close();
      } else {
         double doubleValue = cinematicExporter.takeDuration();
         if (cinematicExporter.state() == CinematicExporter.State.READY && (!this.enabled || doubleValue != this.value9)) {
            this.enabled = true;
            this.value9 = doubleValue;
            this.value4 = Math.clamp(this.implClipsService.trimStart(), 0.0F, (float)doubleValue);
            this.value5 = Math.clamp((float)doubleValue - this.implClipsService.trimEnd(), this.value4, (float)doubleValue);
            if (this.value5 - this.value4 < 0.041666668F) {
               this.value4 = 0.0F;
               this.value5 = (float)doubleValue;
            }

            this.value6 = this.value4;
            this.updateState();
         }

         if (cinematicExporter.state() != CinematicExporter.State.READY) {
            this.enabled2 = false;
         }

         if (this.enabled2) {
            this.value6 = this.value6 + Math.min(screenScreenId.deltaTime(), 0.1F) * this.value8;
            if (this.value6 > this.value5) {
               this.value6 = this.value4;
            }
         }

         if (cinematicExporter.state() == CinematicExporter.State.READY) {
            cinematicExporter.requestPreview(this.value6);
         }

         if (this.sceneTextureService != null) {
            this.sceneTextureService.texture(cinematicExporter.previewTexture()).invalidate();
         }

         if (this.tIMELINEComponent != null) {
            this.tIMELINEComponent
               .state((float)doubleValue, this.value4, this.value5, this.value6, this.value7, this.enabled2);
         }

         long longValue = System.nanoTime();
         if (cinematicExporter.state() != this.renderer || longValue - this.timestamp > 200000000L) {
            this.renderer = cinematicExporter.state();
            this.timestamp = longValue;
            this.updateState();
         }
      }
   }

   private void updateState() {
      if (this.componentMountService != null) {
         this.componentMountService.invalidateComponent();
      }
   }

   @Override
   public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
      if (!this.checkCondition3()) {
         return false;
      }

      switch (value) {
         case 32:
            this.enabled2 = !this.enabled2;
            break;
         case 73:
            this.value4 = Math.min(this.value6, this.value5 - 0.041666668F);
            this.updateState3();
            break;
         case 79:
            this.value5 = Math.max(this.value6, this.value4 + 0.041666668F);
            this.updateState3();
            break;
         case 262:
            this.enabled2 = false;
            this.updateState2(this.value6 + 0.041666668F);
            break;
         case 263:
            this.enabled2 = false;
            this.updateState2(this.value6 - 0.041666668F);
            break;
         case 268:
            this.enabled2 = false;
            this.updateState2(this.value4);
            break;
         case 269:
            this.enabled2 = false;
            this.updateState2(this.value5);
            break;
         default:
            return false;
      }

      this.updateState();
      return true;
   }

   private void updateState2(float value) {
      this.value6 = Math.clamp(
         Math.round(value * 24.0F) / 24.0F,
         0.0F,
         (float)this.implClipsService.clips().takeDuration()
      );
      if (this.tIMELINEComponent != null) {
         this.tIMELINEComponent
            .state(
               (float)this.implClipsService.clips().takeDuration(),
               this.value4,
               this.value5,
               this.value6,
               this.value7,
               true
            );
      }

      this.updateState();
   }

   private boolean checkCondition() {
      return this.value2 < 760.0F;
   }

   private boolean checkCondition2() {
      return this.value3 < 520.0F;
   }

   private boolean checkCondition3() {
      return this.implClipsService.clips().state() == CinematicExporter.State.READY;
   }

   private boolean checkCondition4() {
      CinematicExporter.State currentState = this.implClipsService.clips().state();
      return currentState == CinematicExporter.State.EXPORTING || currentState == CinematicExporter.State.FINISHING;
   }

   private void updateState3() {
      if (this.enabled && this.implClipsService.clips() != null && this.implClipsService.clips().takeDuration() > 0.0) {
         this.implClipsService.trim(this.value4, Math.max(0.0F, (float)this.implClipsService.clips().takeDuration() - this.value5));
      }
   }

   private void updateState4() {
      this.updateState3();
      this.enabled2 = false;
      this.implClipsService.export();
      this.updateState();
   }

   private void updateState5() {
      if (this.implClipsService.clips().recording()) {
         this.implClipsService.record();
      } else if (Minecraft.getInstance().player != null) {
         this.implClipsService.record();
         this.screenScreenIdService.close();
      }

      this.updateState();
   }

   private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
      this.materialTerrainTooltipsService.backgroundColor(ThemeCornerData.current().dimBackground() ? 1694498816 : 0);
      boolean enabled = this.checkCondition();
      boolean currentEnabled = this.checkCondition3();
      return ComponentBoxService.column(
            item -> item.size(
                  LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)
               )
               .padding(enabled ? 0.0F : (this.checkCondition2() ? 12.0F : 24.0F))
               .align(ScenePctService.Align.CENTER)
               .justify(ScenePctService.Justify.CENTER),
            ComponentBoxService.panel(
               item -> item.id("cinematic-replay.card")
                  .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
                  .minHeight(0.0F)
                  .maxWidth(1080.0F)
                  .maxHeight(820.0F)
                  .cornerRadius(enabled ? 0.0F : 28.0F)
                  .backgroundColor(MaterialIsLightService.SURFACE)
                  .direction(ScenePctService.Direction.COLUMN)
                  .clip(true)
                  .stopPropagation(true),
               this.createComponentKeyService2(),
               MaterialTextService.divider(),
               ComponentBoxService.column(
                  item -> item.id("cinematic-replay.body")
                     .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
                     .flex(1.0F)
                     .minHeight(0.0F)
                     .padding(16.0F, enabled ? 12.0F : 24.0F)
                     .gap(16.0F)
                     .scrollable(true)
                     .scrollBounce(true)
                     .scrollbarAutoHide(true)
                     .clip(true),
                  ComponentBoxService.panel(
                     item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
                        .direction(enabled ? ScenePctService.Direction.COLUMN : ScenePctService.Direction.ROW)
                        .gap(16.0F),
                     this.createComponentKeyService3(),
                     this.createComponentKeyService4()
                  ),
                  currentEnabled ? this.createComponentKeyService5() : this.createComponentKeyService6()
               ),
               MaterialTextService.divider(),
               this.createComponentKeyService7()
            )
         )
         .key("workspace");
   }

   private ComponentKeyService<?> createComponentKeyService2() {
      return ComponentBoxService.row(
            item -> item.id("cinematic-replay.header")
               .size(
                  LayoutOperationHandler.percent(100.0F),
                  LayoutOperationHandler.px(this.checkCondition2() ? 62.0F : 84.0F)
               )
               .padding(
                  8.0F,
                  this.checkCondition() ? 12.0F : 24.0F
               )
               .align(ScenePctService.Align.CENTER)
               .gap(12.0F)
               .flexShrink(0.0F),
            ComponentBoxService.panel(
               item -> item.size(48.0F, 48.0F)
                  .cornerRadius(16.0F)
                  .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER),
               MaterialTextService.icon("movie", MaterialIsLightService.ON_PRIMARY_CONTAINER)
                  .props(item -> item.size(26.0F, 26.0F))
            ),
            ComponentBoxService.column(
               item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
               createComponentKeyService11(
                  "Cinematic Replay",
                  this.checkCondition() ? 21.0F : 28.0F,
                  MaterialIsLightService.ON_SURFACE,
                  true
               ),
               createComponentKeyService11("Record · trim · render", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
            ),
            MaterialTextService.iconButton("cinematic-replay.close", "close", "Close editor", this.screenScreenIdService::close)
         )
         .key("header");
   }

   private ComponentKeyService<?> createComponentKeyService3() {
      boolean enabled = this.checkCondition3();
      return ComponentBoxService.panel(
            item -> item.id("cinematic-replay.preview-card")
               .width(this.checkCondition() ? LayoutOperationHandler.percent(100.0F) : LayoutOperationHandler.auto())
               .flex(this.checkCondition() ? 0.0F : 1.0F)
               .minWidth(0.0F)
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(24.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(12.0F)
               .gap(12.0F)
               .flexShrink(0.0F),
            ComponentBoxService.panel(
               item -> item.id("cinematic-replay.preview")
                  .size(
                     LayoutOperationHandler.percent(100.0F),
                     LayoutOperationHandler.px(this.checkCondition() ? 220.0F : 310.0F)
                  )
                  .backgroundColor(MaterialIsLightService.SURFACE_LOWEST)
                  .cornerRadius(18.0F)
                  .clip(true)
                  .direction(ScenePctService.Direction.NONE),
               ComponentBoxService.<SceneTextureService>node(
                     "replay-image",
                     SceneTextureService::new,
                     item -> item.id("cinematic-replay.image")
                        .size(
                           LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)
                        )
                        .cornerRadius(18.0F)
                        .flipY(true)
                        .pointerEvents(false)
                  )
                  .onMount(item -> this.sceneTextureService = item)
                  .onUnmount(item -> this.sceneTextureService = null)
                  .key("image"),
               enabled
                  ? createComponentKeyService11("3D CAMERA PREVIEW", 11.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true)
                     .props(item -> item.absolute().position(12.0F, 12.0F))
                  : createComponentKeyService11(
                        this.implClipsService.clips().recording() ? "Recording 3D scene…" : "Record a take to preview it here",
                        14.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT,
                        false
                     )
                     .props(item -> item.absolute().position(20.0F, 20.0F))
            ),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F))
                  .align(ScenePctService.Align.CENTER)
                  .gap(8.0F),
               createComponentKeyService11(
                     enabled
                        ? createText(this.value6) + "  /  " + createText(this.implClipsService.clips().takeDuration())
                        : this.implClipsService.clips().status(),
                     12.0F,
                     MaterialIsLightService.ON_SURFACE_VARIANT,
                     false
                  )
                  .props(item -> item.flex(1.0F).minWidth(0.0F)),
               createComponentKeyService11(enabled ? "24 FPS" : "3D TAKE", 11.0F, MaterialIsLightService.PRIMARY, true)
            )
         )
         .key("preview-card");
   }

   private ComponentKeyService<?> createComponentKeyService4() {
      int value = !this.checkCondition4() && !this.implClipsService.clips().recording() ? 1 : 0;
      return ComponentBoxService.panel(
            item -> item.id("cinematic-replay.settings")
               .width(
                  this.checkCondition()
                     ? LayoutOperationHandler.percent(100.0F)
                     : LayoutOperationHandler.px(300.0F)
               )
               .flexShrink(0.0F)
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(24.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(16.0F)
               .gap(14.0F),
            createComponentKeyService11("Capture & output", 18.0F, MaterialIsLightService.ON_SURFACE, true),
            this.createComponentKeyService9(
               "duration",
               "Next recording",
               this.implClipsService.recordingDuration(),
               5.0F,
               60.0F,
               1.0F,
               item -> this.implClipsService.recordingDuration(item),
               (value != 0)
            ),
            MaterialTextService.divider(),
            createComponentKeyService11("Resolution", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(4.0F),
               this.createComponentKeyService8("1080p", (value != 0)),
               this.createComponentKeyService8("1440p", (value != 0)),
               this.createComponentKeyService8("2160p", (value != 0))
            ),
            this.createComponentKeyService9(
               "bitrate",
               "Bitrate · " + Math.round(this.implClipsService.bitrate()) + " Mbps",
               this.implClipsService.bitrate(),
               25.0F,
               400.0F,
               5.0F,
               this.implClipsService::bitrate,
               (value != 0)
            ),
            createComponentKeyService11("MP4 files are saved in ellice/clips.", 11.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
               .props(item -> item.wordWrap(true))
         )
         .key("settings");
   }

   private ComponentKeyService<?> createComponentKeyService5() {
      float value = (float)this.implClipsService.clips().takeDuration();
      return ComponentBoxService.panel(
            item -> item.id("cinematic-replay.timeline")
               .width(LayoutOperationHandler.percent(100.0F))
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(24.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(16.0F)
               .gap(10.0F)
               .flexShrink(0.0F),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).align(ScenePctService.Align.CENTER),
               createComponentKeyService11("Edit timeline", 18.0F, MaterialIsLightService.ON_SURFACE, true)
                  .props(item -> item.flex(1.0F)),
               createComponentKeyService11(
                  createText2(this.value6) + "  /  " + createText2(value),
                  12.0F,
                  MaterialIsLightService.PRIMARY,
                  true
               )
            ),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F))
                  .gap(6.0F)
                  .align(ScenePctService.Align.CENTER),
               createComponentKeyService10("to-in", "|◀", false, true, () -> this.updateState2(this.value4)),
               createComponentKeyService10("frame-back", "−1f", false, true, () -> {
                  this.enabled2 = false;
                  this.updateState2(this.value6 - 0.041666668F);
               }),
               createComponentKeyService10("play", this.enabled2 ? "Pause" : "Play", true, true, () -> {
                  this.enabled2 = !this.enabled2;
                  if (this.value6 >= this.value5) {
                     this.updateState2(this.value4);
                  }

                  this.updateState();
               }),
               createComponentKeyService10("frame-forward", "+1f", false, true, () -> {
                  this.enabled2 = false;
                  this.updateState2(this.value6 + 0.041666668F);
               }),
               createComponentKeyService10("to-out", "▶|", false, true, () -> this.updateState2(this.value5)),
               createComponentKeyService10(
                  "speed",
                  this.value8 + "×",
                  false,
                  true,
                  () -> {
                     this.value8 = this.value8 == 0.5F
                        ? 1.0F
                        : (this.value8 == 1.0F ? 2.0F : 0.5F);
                     this.updateState();
                  }
               )
            ),
            ComponentBoxService.<TIMELINEComponent>node(
                  "cinematic-filmstrip",
                  () -> new TIMELINEComponent(this.implClipsService.clips()),
                  item -> item.id("cinematic-replay.filmstrip")
                     .state(value, this.value4, this.value5, this.value6, this.value7)
                     .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(223.0F))
                      .onChange((start, end, progress) -> {
                         this.value4 = start;
                         this.value5 = end;
                         this.value6 = progress;
                        this.enabled2 = false;
                        this.updateState();
                     })
                     .onCommit(this::updateState3)
               )
               .onMount(item -> this.tIMELINEComponent = item)
               .onUnmount(item -> this.tIMELINEComponent = null)
               .key("filmstrip"),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F),
               createComponentKeyService11("IN  " + createText2(this.value4), 12.0F, MaterialIsLightService.PRIMARY, true)
                  .props(item -> item.flex(1.0F)),
               createComponentKeyService11(
                     "SELECTED  " + createText2(this.value5 - this.value4),
                     12.0F,
                     MaterialIsLightService.ON_SURFACE_VARIANT,
                     true
                  )
                  .props(item -> item.flex(1.0F)),
               createComponentKeyService11("OUT  " + createText2(this.value5), 12.0F, MaterialIsLightService.PRIMARY, true)
            ),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F))
                  .align(ScenePctService.Align.CENTER)
                  .gap(8.0F),
               createComponentKeyService10("mark-in", "Set In  ·  I", false, true, () -> {
                  this.value4 = Math.min(this.value6, this.value5 - 0.041666668F);
                  this.updateState3();
                  this.updateState();
               }),
               createComponentKeyService10("mark-out", "Set Out  ·  O", false, true, () -> {
                  this.value5 = Math.max(this.value6, this.value4 + 0.041666668F);
                  this.updateState3();
                  this.updateState();
               }),
               createComponentKeyService10("reset", "Full take", false, true, () -> {
                  this.value4 = 0.0F;
                  this.value5 = value;
                  this.value6 = 0.0F;
                  this.updateState3();
                  this.updateState();
               }),
               createComponentKeyService10("zoom-out", "−", false, this.value7 > 1.0F, () -> {
                  this.value7 = Math.max(1.0F, this.value7 / 2.0F);
                  this.updateState();
               }),
               createComponentKeyService11(this.value7 + "×", 11.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true),
               createComponentKeyService10("zoom-in", "+", false, this.value7 < 8.0F, () -> {
                  this.value7 = Math.min(8.0F, this.value7 * 2.0F);
                  this.updateState();
               })
            )
         )
         .key("timeline");
   }

   private ComponentKeyService<?> createComponentKeyService6() {
      return ComponentBoxService.panel(
            item -> item.width(LayoutOperationHandler.percent(100.0F))
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(20.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(16.0F)
               .gap(6.0F)
               .flexShrink(0.0F),
            createComponentKeyService11(
               this.implClipsService.clips().recording()
                  ? "Recording in progress"
                  : (this.checkCondition4() ? "Rendering MP4" : "Ready when you are"),
               16.0F,
               MaterialIsLightService.ON_SURFACE,
               true
            ),
            createComponentKeyService11(
                  this.implClipsService.clips().status().isBlank()
                     ? "Set the duration, then start a recording in your world."
                     : this.implClipsService.clips().status(),
                  12.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               )
               .props(item -> item.wordWrap(true))
         )
         .key("status");
   }

   private ComponentKeyService<?> createComponentKeyService7() {
      CinematicExporter.State currentState = this.implClipsService.clips().state();
      int value = currentState == CinematicExporter.State.READY ? 1 : 0;
      int currentValue = currentState == CinematicExporter.State.RECORDING ? 1 : 0;
      return ComponentBoxService.row(
            item -> item.id("cinematic-replay.footer")
               .size(
                  LayoutOperationHandler.percent(100.0F),
                  LayoutOperationHandler.px(this.checkCondition2() ? 64.0F : 78.0F)
               )
               .padding(
                  8.0F,
                  this.checkCondition() ? 12.0F : 24.0F
               )
               .gap(8.0F)
               .align(ScenePctService.Align.CENTER)
               .flexShrink(0.0F),
            createComponentKeyService10(
               "record",
               currentValue != 0 ? "Finish take" : "Record",
               false,
               currentValue != 0 || currentState == CinematicExporter.State.EMPTY && Minecraft.getInstance().player != null,
               this::updateState5
            ),
            createComponentKeyService10("discard", "Discard", false, currentState != CinematicExporter.State.EMPTY, () -> {
               this.enabled2 = false;
               this.enabled = false;
               this.implClipsService.discard();
               this.updateState();
            }),
            createComponentKeyService10(
               "export",
               "Render MP4",
               true,
               value != 0 && this.value5 - this.value4 >= 0.041666668F,
               this::updateState4
            )
         )
         .key("footer");
   }

   private ComponentKeyService<?> createComponentKeyService8(String text, boolean enabled) {
      return createComponentKeyService10("resolution-" + text, text, text.equals(this.implClipsService.resolution()), enabled, () -> {
         this.implClipsService.resolution(text);
         this.updateState();
      });
   }

   private ComponentKeyService<?> createComponentKeyService9(
      String text, String currentText, float currentValue, float nextValue, float previousValue, float sourceValue, Consumer<Float> consumer, boolean enabled
   ) {
      return ComponentBoxService.column(
            item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(4.0F),
            createComponentKeyService11(
               currentText + (text.equals("duration") ? " · " + Math.round(currentValue) + " s" : ""),
               12.0F,
               MaterialIsLightService.ON_SURFACE_VARIANT,
               true
            ),
            ComponentBoxService.<SliderControl>node(
                  "replay-slider",
                  SliderControl::new,
                  item -> item.id("cinematic-replay." + text)
                     .material(true)
                     .compact(true)
                     .range(nextValue, previousValue)
                     .step(sourceValue)
                     .value(currentValue)
                     .width(LayoutOperationHandler.percent(100.0F))
                     .height(LayoutOperationHandler.px(40.0F))
                     .interactive(enabled)
                      .onCommit(committedValue -> {
                         consumer.accept(committedValue);
                        this.updateState();
                     })
               )
               .key(text)
         )
         .key(text + "-row");
   }

   private static ComponentKeyService<MaterialJoinedService> createComponentKeyService10(String text, String currentText, boolean enabled, boolean currentEnabled, Runnable runnable) {
      int value = enabled ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER;
      return ComponentBoxService.<MaterialJoinedService>node(
            "material-button",
            MaterialJoinedService::new,
            item -> item.colors(enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER, value)
               .shape(20.0F, 40.0F)
               .available(currentEnabled)
               .id("cinematic-replay." + text)
               .height(LayoutOperationHandler.px(48.0F))
               .minWidth(0.0F)
               .flex(1.0F)
               .padding(0.0F, 10.0F)
               .direction(ScenePctService.Direction.ROW)
               .align(ScenePctService.Align.CENTER)
               .justify(ScenePctService.Justify.CENTER)
               .opacity(currentEnabled ? 1.0F : 0.4F)
               .onClick(runnable),
            createComponentKeyService11(currentText, 12.0F, value, true)
               .props(item -> item.minWidth(0.0F).overflow(SceneTextService.Overflow.ELLIPSIS))
         )
         .key(text);
   }

   private static ComponentKeyService<SceneTextService> createComponentKeyService11(String currentText, float value, int currentValue, boolean enabled) {
      return MaterialTextService.text(currentText, value, currentValue).props(item -> item.fontFamily(enabled ? "material-roboto-medium" : "material-roboto"));
   }

   private static String createText(double doubleValue) {
      return String.format(
         Locale.ROOT,
         "%d:%04.1f",
         (int)(doubleValue / 60.0),
         doubleValue % 60.0
      );
   }

   private static String createText2(double doubleValue) {
      int value = Math.max(0, (int)Math.round(doubleValue * 24.0));
      return String.format(Locale.ROOT, "%02d:%02d:%02d:%02d", value / 86400, value / 1440 % 60, value / 24 % 60, value % 24);
   }
}

