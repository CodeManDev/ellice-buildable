package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.terrain.TerrainStateController;
import dev.felix.ellice.feature.terrain.TerrainSubscribeService;
import dev.felix.ellice.feature.terrain.TerrainData;
import dev.felix.ellice.feature.terrain.CinematicExporter;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.builtin.CinematicReplayScreen;
import java.util.List;
import java.util.Objects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class ImplClipsService extends ModuleSettingsService implements ModuleOperationHandler {
   private final ModuleNameService moduleNameService = this.settingCategory("Recording");
   private final ModuleNameService moduleNameService2 = this.settingCategory("Edit & export");
   private final ModuleSetting.Keybind moduleKeybind = this.setting(
      this.moduleNameService, new ModuleSetting.Keybind("Record Clip", 297).description("Start a 3D recording; press again to finish early.")
   );
   private final ModuleSetting.Keybind moduleKeybind2 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Keybind("Open Replay Editor", 296).description("Open the cinematic timeline and export workspace.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number(
            "Duration (seconds)",
            20.0F,
            5.0F,
            60.0F,
            1.0F
         )
         .description("Maximum recording length. Long recordings use more memory.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Trim Start (seconds)", 0.0F, 0.0F, 59.0F, 0.1F)
         .description("Seconds to remove from the beginning of the recorded take.")
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Trim End (seconds)", 0.0F, 0.0F, 59.0F, 0.1F)
         .description("Seconds to remove from the end of the recorded take.")
   );
   private final ModuleSetting.Mode moduleMode = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Mode("Clip Resolution", new String[]{"1080p", "1440p", "2160p"}, "1440p")
         .description("Native video resolution; higher values take longer to export.")
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
            "Clip Bitrate (Mbps)",
            200.0F,
            25.0F,
            400.0F,
            5.0F
         )
         .description("Video bitrate target. Higher values produce larger MP4 files.")
   );
   private final ModuleSetting.Keybind moduleKeybind3 = this.setting(
      this.moduleNameService2, new ModuleSetting.Keybind("Render Clip", 298).description("Export the edited take to an MP4 in ellice/clips.")
   );
   private final ModuleSetting.Keybind moduleKeybind4 = this.setting(
      this.moduleNameService2, new ModuleSetting.Keybind("Discard Clip", -1).description("Discard the take or cancel its export.")
   );
   private CompatLoadedHandler compatLoadedHandler;
   private TerrainStateController terrainStateController;
   private CinematicExporter renderer;
   private AutoCloseable autoCloseable;
   private ComponentMountService componentMountService;
   private String text2 = "";
   private long timestamp;
   private long timestamp2;
   private List<TerrainData> items = List.of();
   private Object object;
   private Object object2;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;

   public ImplClipsService() {
      super(
         ModuleBuilderData.builder("Cinematic Replay")
            .category(ModuleFeatureType.VISUALS)
            .description("Record 3D action, trim the take, and export a cinematic MP4.")
            .build()
      );
   }

   public CinematicExporter clips() {
      return this.renderer;
   }

   public float recordingDuration() {
      return (Float)this.moduleNumber.get();
   }

   public void recordingDuration(float value) {
      this.moduleNumber.set(value);
   }

   public float trimStart() {
      return (Float)this.moduleNumber2.get();
   }

   public float trimEnd() {
      return (Float)this.moduleNumber3.get();
   }

   public void trim(float value, float currentValue) {
      this.moduleNumber2.set(value);
      this.moduleNumber3.set(currentValue);
   }

   public String resolution() {
      return (String)this.moduleMode.get();
   }

   public void resolution(String text) {
      this.moduleMode.set(text);
   }

   public float bitrate() {
      return (Float)this.moduleNumber4.get();
   }

   public void bitrate(float value) {
      this.moduleNumber4.set(value);
   }

   public void record() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         this.renderer.duration(((Float)this.moduleNumber.get()).floatValue());
         this.renderer.record(minecraft.player.getUUID());
      }
   }

   public void export() {
      this.renderer.resolution(Integer.parseInt(((String)this.moduleMode.get()).replace("p", "")));
      this.renderer.bitrateMbps(((Float)this.moduleNumber4.get()).intValue());
      this.renderer.trim(((Float)this.moduleNumber2.get()).floatValue(), ((Float)this.moduleNumber3.get()).floatValue());
      this.renderer.export();
   }

   public void discard() {
      this.renderer.discard();
   }

   @Override
   public void openPanel() {
      if (!this.isEnabled()) {
         this.enable();
      }

      CoreIsInitializedHandler.get().screens().open(new CinematicReplayScreen(this));
   }

   @Override
   protected void onEnable() {
      this.compatLoadedHandler = CompatAdapterService.terrainEnvironment().orElseThrow(() -> new IllegalStateException("Missing terrain adapter"));
      this.terrainStateController = new TerrainStateController(this.compatLoadedHandler);
      this.autoCloseable = TerrainSubscribeService.subscribe(this.terrainStateController::invalidate);
      this.renderer = new CinematicExporter(
         this.compatLoadedHandler,
         CoreIsInitializedHandler.get().renderer3D(),
         FabricLoader.getInstance().getGameDir(),
         item -> CoreIsInitializedHandler.get().scene().toasts().show("Cinematic Replay", item, 6.0F)
      );
      this.componentMountService = new ComponentMountService()
         .mount(
            layoutContext -> ComponentBoxService.text(
               this.text2,
               textStyle -> textStyle.fontFamily("material-roboto-medium").fontSize(12.0F).color(MaterialIsLightService.ON_SURFACE)
            ),
            CoreIsInitializedHandler.get().theme()
         );
      this.componentMountService
         .absolute()
         .position(12.0F, 12.0F)
         .width(LayoutOperationHandler.px(340.0F))
         .visible(false)
         .pointerEvents(false);
      CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
      this.on(EventAttackInputService.TICK).run(item -> this.updateState());
      this.on(EventAttackInputService.RENDER).priority(EventIsAfterHandler.Priority.FIRST).run(item -> this.updateState2());
      this.on(EventAttackInputService.RENDER).priority(EventIsAfterHandler.Priority.LATE).run(item -> {
         this.renderer.renderPreview();
         this.renderer.render();
      });
      this.on(EventAttackInputService.WORLD).run(item -> this.renderer.stopRecording());
   }

   private void updateState() {
      Minecraft minecraft = Minecraft.getInstance();
      boolean currentEnabled = checkCondition(this.moduleKeybind, minecraft);
      boolean nextEnabled = checkCondition(this.moduleKeybind3, minecraft);
      boolean previousEnabled = checkCondition(this.moduleKeybind4, minecraft);
      boolean sourceEnabled = checkCondition(this.moduleKeybind2, minecraft);
      if (minecraft.screen == null && !CoreIsInitializedHandler.get().screens().isActive()) {
         if (currentEnabled && !this.enabled) {
            this.record();
         }

         if (nextEnabled && !this.enabled2) {
            this.export();
         }

         if (previousEnabled && !this.enabled3) {
            this.discard();
         }

         if (sourceEnabled && !this.enabled4) {
            this.openPanel();
         }
      }

      this.enabled = currentEnabled;
      this.enabled2 = nextEnabled;
      this.enabled3 = previousEnabled;
      this.enabled4 = sourceEnabled;
   }

   private static boolean checkCondition(ModuleSetting.Keybind keybind, Minecraft minecraft) {
      return keybind.isBound() && GLFW.glfwGetKey(CompatAdapterService.windowHandle(minecraft), (Integer)keybind.get()) == 1;
   }

   private void updateState2() {
      Object value = this.compatLoadedHandler.instanceIdentity();
      Object currentValue = this.compatLoadedHandler.resourceIdentity();
      if (!Objects.equals(value, this.object) || !Objects.equals(currentValue, this.object2)) {
         this.renderer.stopRecording();
         this.terrainStateController.reset();
         this.enabled5 = false;
         this.items = List.of();
         this.timestamp2 = 0L;
         this.object = value;
         this.object2 = currentValue;
      }

      CompatLoadedHandler.World currentWorld = this.compatLoadedHandler.world();
      if (this.renderer.recording() && currentWorld != null && currentWorld.player() != null) {
         this.enabled5 = true;
         long offset = System.nanoTime();
         if (offset - this.timestamp2 > 250000000L) {
            this.items = this.compatLoadedHandler.sections(currentWorld.player().x, currentWorld.player().z, 6);
            this.timestamp2 = offset;
         }

         this.terrainStateController.update(this.items, currentWorld.player().x, currentWorld.player().z);
         this.renderer.capture(this.terrainStateController);
      }

      if (!this.renderer.recording() && this.enabled5) {
         this.terrainStateController.reset();
         this.enabled5 = false;
         this.items = List.of();
         this.timestamp2 = 0L;
      }

      long currentOffset = System.nanoTime();
      if (currentOffset - this.timestamp > 200000000L) {
         this.timestamp = currentOffset;
         this.text2 = this.renderer.status();
         this.componentMountService.visible(this.renderer.state() != CinematicExporter.State.EMPTY);
         this.componentMountService.invalidateComponent();
      }
   }

   @Override
   protected void onDisable() {
      if ("cinematic-replay".equals(CoreIsInitializedHandler.get().screens().currentId())) {
         CoreIsInitializedHandler.get().screens().closeNow();
      }

      if (this.componentMountService != null) {
         CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
      }

      if (this.renderer != null) {
         this.renderer.close();
      }

      if (this.autoCloseable != null) {
         try {
            this.autoCloseable.close();
         } catch (Exception exception) {
         }
      }

      if (this.terrainStateController != null) {
         this.terrainStateController.close();
      }

      this.componentMountService = null;
      this.renderer = null;
      this.terrainStateController = null;
      this.compatLoadedHandler = null;
      this.autoCloseable = null;
      this.enabled = this.enabled2 = this.enabled3 = this.enabled4 = this.enabled5 = false;
      this.object = this.object2 = null;
      this.items = List.of();
      this.timestamp2 = this.timestamp = 0L;
   }
}

