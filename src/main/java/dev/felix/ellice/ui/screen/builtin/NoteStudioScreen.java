package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.feature.notebot.NotebotRepository;
import dev.felix.ellice.feature.notebot.NotebotTitleService;
import dev.felix.ellice.feature.notebot.NotebotDecoderTracker;
import dev.felix.ellice.feature.notebot.NotebotCodec;
import dev.felix.ellice.feature.notebot.NotebotChannelData;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlCompactService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class NoteStudioScreen implements ScreenOperationHandler {
   private final NotebotDecoderTracker notebotDecoderTracker;
   private ComponentMountService componentMountService;
   private MaterialTerrainTooltipsService materialTerrainTooltipsService;
   private float value2 = 1120.0F;
   private float value3 = 720.0F;
   private ScreenScreenIdService screenScreenIdService;
   private AutoCloseable autoCloseable;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private int count;
   private Path path2;
   private List<NoteStudioScreen.FileEntry> items2 = List.of();
   private String text2 = "";
   private ControlLetterSpacingService controlLetterSpacingService;

   public NoteStudioScreen(NotebotDecoderTracker currentNotebotDecoderTracker) {
      this.notebotDecoderTracker = currentNotebotDecoderTracker;
   }

   @Override
   public String id() {
      return "note-studio";
   }

   @Override
   public float backgroundDesaturation() {
      return 0.0F;
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
   public SceneCodec.Preset transitionPreset() {
      return SceneCodec.Preset.MODAL;
   }

   @Override
   public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
      this.screenScreenIdService = screenScreenId;
      this.enabled2 = false;
      this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService().terrainTooltips(true);
      this.materialTerrainTooltipsService
         .id("note-studio.root")
         .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
         .direction(ScenePctService.Direction.NONE)
         .interactive(true);
      this.materialTerrainTooltipsService.onLayout(() -> this.viewport(this.materialTerrainTooltipsService.computedW(), this.materialTerrainTooltipsService.computedH()));
      this.componentMountService = new ComponentMountService();
      this.componentMountService.absolute().inset(LayoutOperationHandler.px(0.0F)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
      this.componentMountService.mount(this::createComponentKeyService, screenScreenId == null ? new ThemeIsSetService() : screenScreenId.theme());
      this.materialTerrainTooltipsService.addChild(this.componentMountService);
      this.autoCloseable = this.notebotDecoderTracker.listen(this::updateState);
      return this.materialTerrainTooltipsService;
   }

   @Override
   public void onOpen(ScreenScreenIdService screenScreenId) {
      this.notebotDecoderTracker.rescan();
   }

   @Override
   public void onClose(ScreenScreenIdService screenScreenId) {
      this.enabled2 = true;
      this.count++;
      if (this.autoCloseable != null) {
         try {
            this.autoCloseable.close();
         } catch (Exception exception) {
         }
      }

      this.autoCloseable = null;
   }

   @Override
   public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
      if (value == 256 && this.enabled3) {
         this.enabled3 = false;
         this.count++;
         this.updateState();
         return true;
      } else if (value == 32 && !this.enabled3) {
         this.notebotDecoderTracker.playPause();
         return true;
      } else {
         return false;
      }
   }

   private void updateState() {
      if (!this.enabled2 && this.componentMountService != null) {
         this.componentMountService.invalidateComponent();
      }
   }

   public void viewport(float x, float y) {
      if (!(x <= 0.0F)
         && !(y <= 0.0F)
         && (
            !(Math.abs(this.value2 - x) < 0.5F)
               || !(Math.abs(this.value3 - y) < 0.5F)
         )) {
         this.value2 = x;
         this.value3 = y;
         this.updateState();
      }
   }

   @Override
   public void tick(ScreenScreenIdService screenScreenId) {
      if (this.materialTerrainTooltipsService != null) {
         this.viewport(this.materialTerrainTooltipsService.computedW(), this.materialTerrainTooltipsService.computedH());
      }
   }

   private boolean checkCondition() {
      return this.value2 < 840.0F;
   }

   private boolean checkCondition2() {
      return this.value2 < 600.0F;
   }

   private boolean checkCondition3() {
      return this.value3 < 430.0F;
   }

   private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
      this.materialTerrainTooltipsService.backgroundColor(ThemeCornerData.current().dimBackground() ? 1375731712 : 0);
      ComponentKeyService<?> currentSize = ComponentBoxService.row(
            item -> item.id("note-studio.header")
               .size(
                  LayoutOperationHandler.percent(100.0F),
                  LayoutOperationHandler.px(this.checkCondition3() ? 56.0F : 88.0F)
               )
               .padding(
                  4.0F,
                  this.checkCondition2() ? 12.0F : 24.0F
               )
               .align(ScenePctService.Align.CENTER)
               .gap(16.0F)
               .flexShrink(0.0F),
            ComponentBoxService.panel(
               item -> item.size(48.0F, 48.0F)
                  .cornerRadius(16.0F)
                  .flexShrink(0.0F)
                  .visible(!this.checkCondition2())
                  .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER),
               createComponentKeyService11("music_note", 26.0F, MaterialIsLightService.ON_PRIMARY_CONTAINER)
            ),
            ComponentBoxService.column(
               item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
               createComponentKeyService12(
                  "Note Studio",
                  this.checkCondition2() ? 24.0F : 28.0F,
                  MaterialIsLightService.ON_SURFACE,
                  false
               ),
               createComponentKeyService12("Your music, played by the world.", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
                  .props(item -> item.visible(!this.checkCondition3()))
            ),
            createComponentKeyService10("close", "close", "Close studio; playback keeps running", () -> {
               if (this.screenScreenIdService != null) {
                  this.screenScreenIdService.close();
               }
            })
         )
         .key("header");
      ComponentKeyService<?> nextSize = ComponentBoxService.column(
            item -> item.id("note-studio.body")
               .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
               .flex(1.0F)
               .minHeight(0.0F)
               .padding(
                  0.0F,
                  this.checkCondition2() ? 12.0F : 24.0F,
                  16.0F,
                  this.checkCondition2() ? 12.0F : 24.0F
               )
               .gap(16.0F)
               .scrollable(true)
               .scrollBounce(true)
               .scrollbarAutoHide(true)
               .scrollbarWidth(3.0F)
               .scrollbarColor(MaterialIsLightService.OUTLINE)
               .clip(true),
            this.enabled3 ? this.createComponentKeyService4() : this.createComponentKeyService2()
         )
         .key(this.enabled3 ? "browser-body" : "player-body");
      return ComponentBoxService.column(
         item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
            .padding(
               this.checkCondition2()
                  ? 0.0F
                  : (
                     this.value3 < 680.0F
                        ? 16.0F
                        : 24.0F
                  )
            )
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER),
         ComponentBoxService.panel(
               item -> item.id("note-studio.card")
                  .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
                  .minHeight(0.0F)
                  .maxWidth(1120.0F)
                  .maxHeight(820.0F)
                  .direction(ScenePctService.Direction.COLUMN)
                  .align(ScenePctService.Align.STRETCH)
                  .cornerRadius(this.checkCondition2() ? 0.0F : 28.0F)
                  .backgroundColor(MaterialIsLightService.SURFACE)
                  .clip(true)
                  .stopPropagation(true),
               currentSize,
               nextSize,
               MaterialTextService.divider(),
               this.enabled3 ? this.createComponentKeyService5() : this.createComponentKeyService3()
            )
            .key("studio")
      );
   }

   private ComponentKeyService<?> createComponentKeyService2() {
      NotebotRepository notebotRepository = this.notebotDecoderTracker.playback();
      NotebotTitleService notebotTitle = notebotRepository.song();
      NotebotChannelData notebotChannelData = notebotRepository.arrangement();
      String text = notebotTitle == null ? "Turn an MP3 into a live performance" : notebotTitle.title();
      String currentSize = notebotTitle == null
         ? "Choose a track. Find your blocks. Press play."
         : NotebotTitleService.time(notebotTitle.seconds()) + "  /  " + notebotTitle.notes().size() + " detected notes  /  Local audio";
      String currentText = notebotTitle == null
         ? "0:00 / 0:00"
         : NotebotTitleService.time(notebotRepository.position() / 20.0) + " / " + NotebotTitleService.time(notebotTitle.seconds());
      ComponentKeyService<?> nextSize = ComponentBoxService.panel(
            item -> item.id("note-studio.track")
               .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
               .flexShrink(0.0F)
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(24.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(this.checkCondition2() ? 16.0F : 24.0F)
               .gap(12.0F),
            createComponentKeyService12(
               notebotTitle == null ? "YOUR NEXT PERFORMANCE" : "NOW PLAYING", 11.0F, MaterialIsLightService.PRIMARY, true
            ),
            createComponentKeyService12(
                  text,
                  this.checkCondition() ? 22.0F : 26.0F,
                  MaterialIsLightService.ON_SURFACE,
                  false
               )
               .props(
                  item -> item.id("note-studio.track-title")
                     .width(LayoutOperationHandler.percent(100.0F))
                     .overflow(SceneTextService.Overflow.ELLIPSIS)
               ),
            createComponentKeyService12(currentSize, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false).props(item -> item.wordWrap(true)),
            ComponentBoxService.<TimelineScrubber>node(
                  "note-waveform",
                  () -> new TimelineScrubber(this.notebotDecoderTracker),
                  item -> item.enabled(notebotTitle != null && !this.notebotDecoderTracker.importing())
                     .id("note-studio.waveform")
                     .size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.px(this.checkCondition3() ? 88.0F : 140.0F)
                     )
               )
               .key("waveform"),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).align(ScenePctService.Align.CENTER),
               createComponentKeyService12(
                     this.notebotDecoderTracker.importing()
                        ? "Analyzing "
                           + Math.round(this.notebotDecoderTracker.importProgress() * 100.0)
                           + "%"
                        : notebotRepository.phase().name().replace('_', ' '),
                     11.0F,
                     MaterialIsLightService.PRIMARY,
                     true
                  )
                  .props(item -> item.id("note-studio.phase").flex(1.0F)),
               createComponentKeyService12(currentText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
            )
         )
         .key("track");
      ComponentKeyService<?> previousSize = ComponentBoxService.row(
            item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
               .gap(8.0F),
            createComponentKeyService6(this.checkCondition2() ? "Blocks" : "Reachable blocks", String.valueOf(this.notebotDecoderTracker.scan().blocks().size())),
            createComponentKeyService6("Pitch matches", notebotChannelData == null ? "--" : notebotChannelData.matchedPercent() + "%"),
            createComponentKeyService6(
               notebotRepository.phase() == NotebotRepository.Phase.TUNING ? "Tuning clicks" : "Voices",
               notebotRepository.phase() == NotebotRepository.Phase.TUNING
                  ? notebotRepository.tunedClicks() + " / " + notebotChannelData.tuningClicks()
                  : String.valueOf(this.notebotDecoderTracker.voices())
            )
         )
         .key("stats");
      ComponentKeyService<?> sourceSize = ComponentBoxService.panel(
            item -> item.id("note-studio.setup")
               .width(
                  this.checkCondition()
                     ? LayoutOperationHandler.percent(100.0F)
                     : LayoutOperationHandler.px(320.0F)
               )
               .height(LayoutOperationHandler.auto())
               .flexShrink(0.0F)
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .cornerRadius(24.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(20.0F)
               .gap(12.0F),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F))
                  .align(ScenePctService.Align.CENTER)
                  .gap(6.0F),
               createComponentKeyService12("Performance", 20.0F, MaterialIsLightService.ON_SURFACE, false).props(item -> item.flex(1.0F)),
               createComponentKeyService10("rescan", "refresh", "Scan reachable note blocks", this.notebotDecoderTracker::rescan)
            ),
            ComponentBoxService.panel(
               item -> item.id("note-studio.auto-tune")
                  .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(64.0F))
                  .backgroundColor(0)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .gap(8.0F)
                  .cursorStyle(ScenePctService.CursorStyle.POINTER)
                  .stopPropagation(true)
                  .onClick(() -> this.notebotDecoderTracker.autoTune(!this.notebotDecoderTracker.autoTune()))
                  .pointerEvents(!this.notebotDecoderTracker.arrangementLocked())
                  .opacity(this.notebotDecoderTracker.arrangementLocked() ? 0.45F : 1.0F),
               ComponentBoxService.column(
                  item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
                  createComponentKeyService12("Auto-tune", 15.0F, MaterialIsLightService.ON_SURFACE, true),
                  createComponentKeyService12("Tune nearby blocks", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
               ),
               ComponentBoxService.node(
                  "auto-tune-switch",
                  ControlCompactService::new,
                  item -> item.material(true)
                     .compact(false)
                     .value(this.notebotDecoderTracker.autoTune())
                     .size(56.0F, 40.0F)
                     .flexShrink(0.0F)
                     .pointerEvents(false)
               )
            ),
            MaterialTextService.divider(),
            createComponentKeyService12("Arrangement", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(4.0F),
               this.createComponentKeyService7("melody", "Melody", this.notebotDecoderTracker.voices() == 1, () -> this.notebotDecoderTracker.voices(1)),
               this.createComponentKeyService7("duet", "2 voices", this.notebotDecoderTracker.voices() == 2, () -> this.notebotDecoderTracker.voices(2)),
               this.createComponentKeyService7("chords", "3 voices", this.notebotDecoderTracker.voices() == 3, () -> this.notebotDecoderTracker.voices(3))
            ),
            createComponentKeyService12("Playback speed", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, true),
            ComponentBoxService.row(
               item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(4.0F),
               this.createComponentKeyService8("slow", "0.75x", 0.75),
               this.createComponentKeyService8("normal", "1.0x", 1.0),
               this.createComponentKeyService8("fast", "1.25x", 1.25)
            ),
            createComponentKeyService9(
                  "loop", notebotRepository.loop() ? "Loop on" : "Loop off", "repeat", notebotRepository.loop(), true, () -> this.notebotDecoderTracker.loop(!notebotRepository.loop())
               )
               .props(
                  item -> item.width(LayoutOperationHandler.percent(100.0F))
                     .height(LayoutOperationHandler.px(48.0F))
                     .flex(0.0F)
                     .tooltip("Repeat the track")
               )
         )
         .key("setup");
      String nextText = this.notebotDecoderTracker.scan().context().problem();
      if (nextText.isEmpty()) {
         nextText = this.notebotDecoderTracker.scan().blocks().isEmpty()
            ? "Stand beside uncovered note blocks, then scan again."
            : this.notebotDecoderTracker.scan().blocks().size()
               + " melodic blocks ready. "
               + this.notebotDecoderTracker.scan().obstructed()
               + " blocked or unreachable, "
               + this.notebotDecoderTracker.scan().percussion()
               + " percussion/head blocks skipped.";
      }

      ComponentKeyService<?> currentWidth = ComponentBoxService.panel(
            item -> item.id("note-studio.guidance")
               .width(LayoutOperationHandler.percent(100.0F))
               .backgroundColor(MaterialIsLightService.SURFACE_LOW)
               .cornerRadius(20.0F)
               .direction(ScenePctService.Direction.COLUMN)
               .padding(16.0F)
               .gap(8.0F),
            createComponentKeyService12(
                  this.notebotDecoderTracker.message(),
                  14.0F,
                  this.notebotDecoderTracker.playback().phase() == NotebotRepository.Phase.ERROR ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE,
                  false
               )
               .props(item -> item.id("note-studio.status").wordWrap(true)),
            createComponentKeyService12(nextText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
               .props(item -> item.wordWrap(true)),
            createComponentKeyService12(
                  notebotRepository.skippedNotes() == 0
                     ? "Drag the waveform to seek. Tempo and loop can change while playing."
                     : notebotRepository.skippedNotes() + " notes skipped to keep playback bounded after timing overlap.",
                  12.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               )
               .props(item -> item.wordWrap(true)),
            createComponentKeyService12(
                  "Audio becomes a simplified instrumental arrangement. Clear melodies work best; vocals and dense mixes are approximate.",
                  12.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               )
               .props(item -> item.wordWrap(true))
         )
         .key("guidance");
      return ComponentBoxService.panel(
            item -> item.id("note-studio.player")
               .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
               .direction(this.checkCondition() ? ScenePctService.Direction.COLUMN : ScenePctService.Direction.ROW)
               .align(ScenePctService.Align.START)
               .gap(16.0F)
               .flexShrink(0.0F),
            ComponentBoxService.column(
               item -> item.id("note-studio.overview")
                  .width(this.checkCondition() ? LayoutOperationHandler.percent(100.0F) : LayoutOperationHandler.auto())
                  .flex(this.checkCondition() ? 0.0F : 1.0F)
                  .minWidth(0.0F)
                  .gap(16.0F),
               nextSize,
               previousSize,
               currentWidth
            ),
            sourceSize
         )
         .key("player");
   }

   private ComponentKeyService<?> createComponentKeyService3() {
      boolean enabled = this.notebotDecoderTracker.performing();
      int value = this.notebotDecoderTracker.playback().phase() == NotebotRepository.Phase.PAUSED ? 1 : 0;
      String text = enabled ? "Pause" : (value != 0 ? "Resume" : (this.notebotDecoderTracker.autoTune() ? "Tune & play" : "Play"));
      return ComponentBoxService.row(
            item -> item.id("note-studio.transport")
               .size(
                  LayoutOperationHandler.percent(100.0F),
                  LayoutOperationHandler.px(this.checkCondition3() ? 64.0F : 80.0F)
               )
               .padding(
                  8.0F,
                  this.checkCondition2() ? 12.0F : 24.0F
               )
               .gap(this.checkCondition2() ? 6.0F : 12.0F)
               .align(ScenePctService.Align.CENTER)
               .flexShrink(0.0F),
            createComponentKeyService9(
               "choose",
               this.checkCondition2() ? "Audio" : "Choose audio",
               "folder",
               false,
               true,
               () -> {
                  Path path = Path.of(System.getProperty("user.home"), "Music");
                  this.updateState4(
                     this.path2 != null
                        ? this.path2
                        : (Files.isDirectory(path) ? path : Path.of(System.getProperty("user.home")))
                  );
               }
            ),
            createComponentKeyService9(
               "play",
               this.checkCondition2() && !enabled && value == 0 ? "Play" : text,
               enabled ? "pause" : "play_arrow",
               true,
               this.notebotDecoderTracker.playback().song() != null && !this.notebotDecoderTracker.importing(),
               this.notebotDecoderTracker::playPause
            ),
            createComponentKeyService9(
               "stop",
               this.notebotDecoderTracker.importing() ? "Cancel" : "Stop",
               "stop",
               false,
               this.notebotDecoderTracker.importing() || this.notebotDecoderTracker.playback().song() != null,
               this.notebotDecoderTracker::stop
            )
         )
         .key("transport");
   }

   private ComponentKeyService<?> createComponentKeyService4() {
      ArrayList arrayList = new ArrayList();

      for (NoteStudioScreen.FileEntry fileEntry : this.items2) {
         arrayList.add(
            ComponentBoxService.<MaterialJoinedService>node(
                  "material-button",
                  MaterialJoinedService::new,
                  item -> {
                     item.colors(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE).shape(16.0F, 0.0F);
                     item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(64.0F))
                        .flexShrink(0.0F)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .padding(0.0F, 16.0F)
                        .gap(16.0F)
                        .tooltip(fileEntry.path().toString())
                        .onClick(() -> {
                           if (fileEntry.directory()) {
                              this.updateState4(fileEntry.path());
                           } else {
                              this.updateState2(fileEntry.path());
                           }
                        });
                  },
                  createComponentKeyService11(
                     fileEntry.directory() ? "folder" : "music_note",
                     24.0F,
                     fileEntry.directory() ? MaterialIsLightService.ON_SURFACE_VARIANT : MaterialIsLightService.PRIMARY
                  ),
                  createComponentKeyService12(fileEntry.path().getFileName().toString(), 14.0F, MaterialIsLightService.ON_SURFACE, false)
                     .props(item -> item.flex(1.0F).minWidth(0.0F).overflow(SceneTextService.Overflow.ELLIPSIS)),
                  createComponentKeyService11(
                     fileEntry.directory() ? "chevron_right" : "play_arrow", 20.0F, MaterialIsLightService.ON_SURFACE_VARIANT
                  )
               )
               .key(fileEntry.path().toString())
         );
      }

      if (arrayList.isEmpty()) {
         arrayList.add(
            createComponentKeyService12(
               this.enabled4 ? "Opening folder..." : "No audio files in this folder.",
               14.0F,
               MaterialIsLightService.ON_SURFACE_VARIANT,
               false
            )
         );
      }

      return ComponentBoxService.column(
            item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
               .gap(16.0F)
               .flexShrink(0.0F),
            createComponentKeyService12("Choose your soundtrack", 24.0F, MaterialIsLightService.ON_SURFACE, false),
            createComponentKeyService12(
                  "MP3, WAV, FLAC, OGG or M4A. Files stay on your computer.",
                  12.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT,
                  false
               )
               .props(item -> item.wordWrap(true)),
            ComponentBoxService.row(
               item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(56.0F))
                  .gap(8.0F)
                  .align(ScenePctService.Align.CENTER),
               createComponentKeyService10("up", "arrow_back", "Parent folder", () -> {
                  if (this.path2 != null && this.path2.getParent() != null) {
                     this.updateState4(this.path2.getParent());
                  }
               }),
               ComponentBoxService.<ControlLetterSpacingService>node(
                     "audio-path",
                     ControlLetterSpacingService::new,
                     item -> {
                        MaterialTextService.input(item);
                        item.id("note-studio.path")
                           .size(LayoutOperationHandler.auto(), LayoutOperationHandler.percent(100.0F))
                           .flex(1.0F)
                           .minWidth(0.0F)
                           .maxLength(4096)
                           .fontSize(14.0F)
                           .placeholder("Folder or audio file path")
                           .cornerRadius(16.0F)
                           .onSubmit(this::updateState3);
                     }
                  )
                  .key("path-input")
                  .onMount(item -> {
                     this.controlLetterSpacingService = item;
                     item.text(this.path2 == null ? "" : this.path2.toString());
                  })
                  .onUnmount(item -> {
                     item.unfocus();
                     this.controlLetterSpacingService = null;
                  }),
               createComponentKeyService10("go", "chevron_right", "Open this path", () -> {
                  if (this.controlLetterSpacingService != null) {
                     this.updateState3(this.controlLetterSpacingService.text());
                  }
               })
            ),
            ComponentBoxService.row(
               item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(48.0F))
                  .gap(8.0F),
               createComponentKeyService9(
                  "music-folder",
                  "Music",
                  this.checkCondition2() ? null : "music_note",
                  false,
                  true,
                  () -> this.updateState4(Path.of(System.getProperty("user.home"), "Music"))
               ),
               createComponentKeyService9(
                  "downloads-folder",
                  "Downloads",
                  this.checkCondition2() ? null : "folder",
                  false,
                  true,
                  () -> this.updateState4(Path.of(System.getProperty("user.home"), "Downloads"))
               ),
               createComponentKeyService9(
                  "home-folder",
                  "Home",
                  this.checkCondition2() ? null : "home",
                  false,
                  true,
                  () -> this.updateState4(Path.of(System.getProperty("user.home")))
               )
            ),
            createComponentKeyService12(this.text2, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
               .props(item -> item.wordWrap(true)),
            ComponentBoxService.column(
               item -> item.id("note-studio.files")
                  .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
                  .gap(6.0F)
                  .flexShrink(0.0F),
               arrayList.toArray(ComponentKeyService[]::new)
            )
         )
         .key("browser");
   }

   private ComponentKeyService<?> createComponentKeyService5() {
      return ComponentBoxService.row(
            item -> item.id("note-studio.browser-footer")
               .size(
                  LayoutOperationHandler.percent(100.0F),
                  LayoutOperationHandler.px(this.checkCondition3() ? 64.0F : 80.0F)
               )
               .padding(
                  8.0F,
                  this.checkCondition2() ? 12.0F : 24.0F
               )
               .align(ScenePctService.Align.CENTER)
               .flexShrink(0.0F),
            createComponentKeyService9("back", "Back to player", "arrow_back", false, true, () -> {
               this.enabled3 = false;
               this.count++;
               this.updateState();
            })
         )
         .key("browser-footer");
   }

   private void updateState2(Path path) {
      this.enabled3 = false;
      this.count++;
      this.notebotDecoderTracker.importSong(path);
      this.updateState();
   }

   private void updateState3(String text) {
      try {
         String currentText = text.strip();
         if (currentText.equals("~") || currentText.startsWith("~/") || currentText.startsWith("~\\")) {
            currentText = System.getProperty("user.home") + currentText.substring(1);
         }

         Path currentPath = Path.of(currentText).toAbsolutePath().normalize();
         if (NotebotCodec.supports(currentPath)) {
            this.updateState2(currentPath);
         } else {
            this.updateState4(currentPath);
         }
      } catch (RuntimeException exception) {
         this.text2 = "That path could not be opened.";
         this.updateState();
      }
   }

   private void updateState4(Path currentPath) {
      this.enabled3 = true;
      this.enabled4 = true;
      this.text2 = "Opening folder...";
      this.items2 = List.of();
      int currentCount = ++this.count;
      this.updateState();
      CompletableFuture.<NoteStudioScreen.DirectoryPage>supplyAsync(
            () -> {
               try {
                   Path resolvedPath = currentPath.toRealPath();
                  ArrayList arrayList = new ArrayList();
                  byte byteValue = 0;

                   try (DirectoryStream directoryStream = Files.newDirectoryStream(resolvedPath)) {
                     for (Path nextPath : (Iterable<Path>) (Iterable<?>) (directoryStream)) {
                        if (!nextPath.getFileName().toString().startsWith(".")) {
                           boolean currentDirectory = Files.isDirectory(nextPath);
                           if (currentDirectory || NotebotCodec.supports(nextPath) && Files.isRegularFile(nextPath)) {
                              arrayList.add(new NoteStudioScreen.FileEntry(nextPath, currentDirectory));
                              if (arrayList.size() >= 500) {
                                 byteValue = 1;
                                 break;
                              }
                           }
                        }
                     }
                  }

                  arrayList.sort(
                     Comparator.comparing(NoteStudioScreen.FileEntry::directory)
                        .reversed()
                        .thenComparing(item -> item.path().getFileName().toString(), String.CASE_INSENSITIVE_ORDER)
                  );
                  return new NoteStudioScreen.DirectoryPage(currentPath, List.copyOf(arrayList), (byteValue != 0));
               } catch (IOException iOException) {
                  throw new UncheckedIOException(iOException);
               }
            }
         )
         .whenComplete(
            (item, currentItem) -> this.notebotDecoderTracker
               .dispatch(
                  () -> {
                     if (!this.enabled2 && currentCount == this.count) {
                        this.enabled4 = false;
                        if (currentItem != null) {
                           this.text2 = "Folder unavailable. Enter a different path or choose Home.";
                        } else {
                           this.path2 = item.path();
                           this.items2 = item.entries();
                           this.text2 = item.truncated()
                              ? "First 500 items shown. Enter a full file path to open another track."
                              : this.items2.size() + " folders and audio files";
                           if (this.controlLetterSpacingService != null) {
                              this.controlLetterSpacingService.text(this.path2.toString());
                           }
                        }

                        this.updateState();
                     }
                  }
               )
         );
   }

   private static ComponentKeyService<?> createComponentKeyService6(String text, String currentText) {
      return ComponentBoxService.panel(
         item -> item.flex(1.0F)
            .minWidth(0.0F)
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.px(88.0F))
            .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
            .cornerRadius(20.0F)
            .padding(12.0F)
            .direction(ScenePctService.Direction.COLUMN)
            .gap(4.0F)
            .tooltip(text),
         createComponentKeyService12(currentText, 24.0F, MaterialIsLightService.PRIMARY, false),
         createComponentKeyService12(text, 11.0F, MaterialIsLightService.ON_SURFACE_VARIANT, false)
      );
   }

   private ComponentKeyService<?> createComponentKeyService7(String text, String currentText, boolean enabled, Runnable runnable) {
      return createComponentKeyService9(text, currentText, null, enabled, !this.notebotDecoderTracker.arrangementLocked(), runnable)
         .props(
            item -> item.joined(text.equals("melody"), text.equals("chords"), enabled)
               .height(LayoutOperationHandler.px(48.0F))
         );
   }

   private ComponentKeyService<?> createComponentKeyService8(String text, String currentText, double doubleValue) {
      return createComponentKeyService9(text, currentText, null, this.notebotDecoderTracker.speed() == doubleValue, true, () -> this.notebotDecoderTracker.speed(doubleValue))
         .props(
            item -> item.joined(text.equals("slow"), text.equals("fast"), this.notebotDecoderTracker.speed() == doubleValue)
               .height(LayoutOperationHandler.px(48.0F))
         );
   }

   private static ComponentKeyService<MaterialJoinedService> createComponentKeyService9(String text, String currentText, String nextText, boolean enabled, boolean currentEnabled, Runnable runnable) {
      ArrayList arrayList = new ArrayList();
      int value = enabled ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER;
      if (nextText != null) {
         arrayList.add(createComponentKeyService11(nextText, 20.0F, value));
      }

      arrayList.add(
         createComponentKeyService12(currentText, 14.0F, value, true)
            .props(item -> item.minWidth(0.0F).flexShrink(1.0F).overflow(SceneTextService.Overflow.ELLIPSIS))
      );
      return ComponentBoxService.<MaterialJoinedService>node(
            "material-button",
            MaterialJoinedService::new,
            item -> {
               item.colors(enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER, value)
                  .shape(20.0F, 40.0F)
                  .available(currentEnabled);
               item.id("note-studio." + text)
                  .size(LayoutOperationHandler.auto(), LayoutOperationHandler.px(48.0F))
                  .flex(1.0F)
                  .minWidth(0.0F)
                  .flexShrink(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .padding(0.0F, 12.0F)
                  .gap(8.0F)
                  .opacity(currentEnabled ? 1.0F : 0.4F)
                  .onClick(runnable);
            },
            arrayList.toArray(ComponentKeyService[]::new)
         )
         .key(text);
   }

   private static ComponentKeyService<MaterialJoinedService> createComponentKeyService10(String text, String currentText, String nextText, Runnable runnable) {
      return MaterialTextService.iconButton("note-studio." + text, currentText, nextText, runnable);
   }

   private static ComponentKeyService<?> createComponentKeyService11(String text, float value, int currentValue) {
      return MaterialTextService.icon(text, currentValue).props(item -> item.size(value, value));
   }

   private static ComponentKeyService<SceneTextService> createComponentKeyService12(String currentText, float value, int currentValue, boolean enabled) {
      return MaterialTextService.text(currentText, value, currentValue).props(item -> item.fontFamily(enabled ? "material-roboto-medium" : "material-roboto"));
   }

   private record DirectoryPage(Path path, List<NoteStudioScreen.FileEntry> entries, boolean truncated) {
   }

   private record FileEntry(Path path, boolean directory) {
   }
}

