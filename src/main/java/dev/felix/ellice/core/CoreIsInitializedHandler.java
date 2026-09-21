package dev.felix.ellice.core;

import dev.felix.ellice.account.AccountAllService;
import dev.felix.ellice.anticheat.AnticheatRepository;
import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.changelog.ChangelogLastSeenService;
import dev.felix.ellice.command.CommandExecuteService;
import dev.felix.ellice.compat.FriendSyncController;
import dev.felix.ellice.compat.CompatReaderService;
import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.compat.CompatOptionsService;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.config.ConfigRepository;
import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.diagnostics.DiagnosticsRecorderService;
import dev.felix.ellice.diagnostics.DiagnosticsIsRecordingService;
import dev.felix.ellice.diagnostics.DiagnosticsAddFrameListenerService;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.discord.DiscordStateController;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventSubscribeService;
import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.feature.studio.StudioRenderer;
import dev.felix.ellice.friends.FriendsStateController;
import dev.felix.ellice.hud.HudItemsService;
import dev.felix.ellice.hud.layout.LayoutRenderer;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutFindSelector;
import dev.felix.ellice.hud.layout.LayoutNewDefaultService;
import dev.felix.ellice.hud.layout.LayoutRepository;
import dev.felix.ellice.hud.layout.HudVariableResolver;
import dev.felix.ellice.menu.MenuLoaderTracker;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.module.HudStyleSettings;
import dev.felix.ellice.module.ModuleRegisterService;
import dev.felix.ellice.module.impl.AccentComponent;
import dev.felix.ellice.module.impl.ImplOpenPanelService;
import dev.felix.ellice.module.impl.ImplClipsService;
import dev.felix.ellice.module.impl.FlowPathModule;
import dev.felix.ellice.module.impl.NoteStudioModule;
import dev.felix.ellice.module.impl.ColorComponent;
import dev.felix.ellice.network.NetworkViewService;
import dev.felix.ellice.plugin.PluginLoadAllService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneRenderer;
import dev.felix.ellice.ui.screen.ScreenDelayQueue;
import dev.felix.ellice.ui.screen.builtin.ChangelogScreen;
import dev.felix.ellice.ui.screen.builtin.BuiltinRepository;
import dev.felix.ellice.ui.screen.builtin.ClientSettingsScreen;
import dev.felix.ellice.ui.screen.builtin.ServerPreviewScreen;
import dev.felix.ellice.ui.screen.builtin.FriendsScreen;
import dev.felix.ellice.ui.screen.builtin.ConfigsScreen;
import dev.felix.ellice.ui.screen.builtin.ClickGuiScreen;
import dev.felix.ellice.ui.screen.builtin.BuiltinSessionTracker;
import dev.felix.ellice.ui.screen.builtin.FritzBoxScreen;
import dev.felix.ellice.ui.screen.builtin.ModuleStudioScreen;
import dev.felix.ellice.ui.screen.builtin.BuiltinActionsTracker;
import dev.felix.ellice.ui.screen.builtin.HudEditorScreen;
import dev.felix.ellice.ui.screen.builtin.AnticheatScreen;
import dev.felix.ellice.ui.screen.overlay.OverlayOnActivateHandler;
import dev.felix.ellice.ui.screen.overlay.NewsOverlayController;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.nio.file.Path;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CoreIsInitializedHandler {
   public static final String NAME = "ellice";
   public static final String VERSION = createText();
   public static final Logger LOGGER = LoggerFactory.getLogger("ellice");
   private static volatile CoreIsInitializedHandler coreIsInitializedHandler;
   private final EventSubscribeService eventSubscribeService = new EventSubscribeService();
   private final ModuleRegisterService moduleRegisterService = new ModuleRegisterService();
   private final ModuleBindService moduleBindService = new ModuleBindService(this.moduleRegisterService);
   private final CommandExecuteService commandExecuteService = new CommandExecuteService(this.moduleRegisterService, this.moduleBindService, () -> {
      if (this.configRepository2 != null) {
         this.configRepository2.save();
      }
   }, item -> this.sceneDtService.toasts().show("Client commands", item, 4.0F, SceneRenderer.Type.INFO));
   private final HudStyleSettings values = new HudStyleSettings();
   private final HudItemsService hudItemsService = new HudItemsService();
   private final LayoutRenderer renderer = new LayoutRenderer();
   private final LayoutOperationHandler layoutOperationHandler = new HudVariableResolver();
   private StudioRenderer renderer2;
   private LayoutFindSelector layoutFindSelector;
   private final CompositorPushPresentationScaleService compositorPushPresentationScaleService = new CompositorPushPresentationScaleService();
   private final ThemeIsSetService values2 = new ThemeIsSetService();
   private ConfigRepository configRepository;
   private DiscordStateController discordStateController;
   private final DiagnosticsAddFrameListenerService items = DiagnosticsAddFrameListenerService.get();
   private SceneDtService sceneDtService;
   private ScreenDelayQueue screenDelayQueue;
   private DisplayMetrics compatData6 = new DisplayMetrics(1, 1, 1, 1, 1, 1, 1.0, false);
   private OverlayOnActivateHandler overlayOnActivateHandler;
   private NewsOverlayController overlayOnActivateHandler2;
   private ChangelogRepository changelogRepository;
   private ChangelogLastSeenService changelogLastSeenService;
   private AccountAllService accountAllService;
   private ServerViewTracker serverViewTracker;
   private NetworkViewService networkViewService;
   private AnticheatRepository anticheatRepository;
   private FriendsStateController friendsStateController;
   private MenuLoaderTracker menuLoaderTracker;
   private BuiltinActionsTracker builtinActionsTracker;
   private PluginLoadAllService pluginLoadAllService;
   private Render3dSceneService renderer3;
   private DiagnosticsRecorderService diagnosticsRecorderService;
   private DiagnosticsIsRecordingService diagnosticsIsRecordingService;
   private LocalConfigRepository configRepository2;
   private long timestamp;
   private long timestamp2;
   private float value;
   private boolean enabled2;

   private static String createText() {
      try {
         return FabricLoader.getInstance()
            .getModContainer("ellice")
            .map(item -> item.getMetadata().getVersion().getFriendlyString())
            .orElse("0.0.0");
      } catch (Throwable exception) {
         return "0.0.0";
      }
   }

   public static CoreIsInitializedHandler get() {
      CoreIsInitializedHandler coreIsInitialized = coreIsInitializedHandler;
      if (coreIsInitialized == null) {
         throw new IllegalStateException("Client not initialized");
      } else {
         return coreIsInitialized;
      }
   }

   public static void initialize() {
      if (coreIsInitializedHandler != null) {
         throw new IllegalStateException("Already initialized");
      }

      coreIsInitializedHandler = new CoreIsInitializedHandler();

      try {
         coreIsInitializedHandler.updateState();
      } catch (Throwable exception) {
         FatalIsTrippedService.trip(exception, FatalCaptureService.Kind.STARTUP);
      }
   }

   public static boolean isInitialized() {
      return coreIsInitializedHandler != null;
   }

   public static boolean isReady() {
      return coreIsInitializedHandler != null && !FatalIsTrippedService.isTripped();
   }

   public CommandExecuteService commands() {
      return this.commandExecuteService;
   }

   public FriendsStateController friends() {
      return this.friendsStateController;
   }

   private CoreIsInitializedHandler() {
   }

   private void updateState() {
      LOGGER.info("{} v{} initializing...", "ellice", VERSION);
      Path path = FabricLoader.getInstance().getGameDir();
      this.configRepository = new ConfigRepository(path.resolve("ellice-ui/client-settings.json"), this.values2);
      this.renderer3 = new Render3dSceneService(path);
      this.friendsStateController = new FriendsStateController(path);
      this.moduleRegisterService
         .discoverAll(
            "dev.felix.ellice.module.impl",
            ImplClipsService.class,
            FlowPathModule.class,
            ImplOpenPanelService.class,
            NoteStudioModule.class,
            AccentComponent.class,
            ColorComponent.class
         );
      this.discordStateController = new DiscordStateController();
      this.configRepository.onPreferencesChanged(item -> this.discordStateController.enabled(item.discordRichPresence()));
      this.renderer2 = new StudioRenderer(path, this.moduleRegisterService);
      this.renderer2.load();
      this.configRepository2 = new LocalConfigRepository(path, this.moduleRegisterService, this.values, this.hudItemsService);
      this.sceneDtService = new SceneDtService(this.compositorPushPresentationScaleService);
      this.pluginLoadAllService = new PluginLoadAllService(path);
      this.pluginLoadAllService.loadAll();
      this.screenDelayQueue = new ScreenDelayQueue(this.sceneDtService, this.values2);
      BuiltinRepository builtinRepository = new BuiltinRepository(path.resolve("ellice-ui/clickgui.json"));
      this.screenDelayQueue.register("clickgui", () -> new ClickGuiScreen(this.moduleRegisterService, builtinRepository));
      this.screenDelayQueue.register("client-settings", () -> new ClientSettingsScreen(this.configRepository));
      this.screenDelayQueue.register("configs", () -> new ConfigsScreen(this.configRepository2));
      this.renderer2.opener(item -> {
         StudioShapeService studioShape = this.renderer2.project(item);
         if (studioShape != null) {
            this.screenDelayQueue.open(new ModuleStudioScreen(this.renderer2, studioShape, null));
         }
      });
      this.screenDelayQueue.register("hudeditor", () -> new HudEditorScreen(this.layoutFindSelector, this.renderer, this.layoutOperationHandler));
      this.changelogRepository = ChangelogRepository.load();
      this.changelogLastSeenService = new ChangelogLastSeenService(FabricLoader.getInstance().getGameDir());
      this.screenDelayQueue.register("changelog", () -> new ChangelogScreen(this.changelogRepository));
      this.overlayOnActivateHandler = new OverlayOnActivateHandler(this.sceneDtService, this.compositorPushPresentationScaleService);
      this.overlayOnActivateHandler2 = new NewsOverlayController(this.sceneDtService, VERSION);
      this.overlayOnActivateHandler2.onActivate(() -> {
         this.screenDelayQueue.open("changelog");
         this.changelogLastSeenService.markSeen(this.createText2());
         this.overlayOnActivateHandler2.setHasNews(false);
      });
      this.overlayOnActivateHandler2.setHasNews(this.changelogRepository.latest() != null && this.changelogLastSeenService.isUnseen(this.createText2()));
      this.diagnosticsRecorderService = new DiagnosticsRecorderService(this.sceneDtService, this.items, () -> this.renderer3.shaderEspDiagnostics());
      this.eventSubscribeService.subscribe(EventAttackInputService.WORLD_RENDER, EventIsAfterHandler.Priority.LAST, item -> {
         FramebufferInfo framebufferInfo = CompatAdapterService.mainFramebuffer(Minecraft.getInstance());
         this.items.begin("render3d");

         try {
            this.renderer3.renderGeometry(item, framebufferInfo);
         } catch (Throwable exception) {
            FatalIsTrippedService.trip(exception, FatalCaptureService.Kind.RENDERER);
         } finally {
            this.items.end();
         }
      });
      this.eventSubscribeService.subscribe(EventAttackInputService.WORLD_POST_RENDER, EventIsAfterHandler.Priority.LAST, item -> {
         FramebufferInfo framebufferInfo = CompatAdapterService.mainFramebuffer(Minecraft.getInstance());
         this.items.begin("world-postfx");

         try {
            this.renderer3.renderPost(item, framebufferInfo);
         } catch (Throwable exception) {
            FatalIsTrippedService.trip(exception, FatalCaptureService.Kind.RENDERER);
         } finally {
            this.items.end();
         }
      });
      this.eventSubscribeService.subscribe(EventAttackInputService.WORLD, item -> this.renderer3.clearWorld());
      this.eventSubscribeService.subscribe(EventAttackInputService.WORLD, item -> this.renderer2.releaseActions());
      Path currentPath = LayoutRepository.fileFor(path, "active");
      this.layoutFindSelector = LayoutRepository.load(currentPath);
      if (this.layoutFindSelector == null) {
         this.layoutFindSelector = LayoutNewDefaultService.newDefault();
         LayoutRepository.save(currentPath, this.layoutFindSelector);
         LOGGER.info("Seeded default HUD layout at {}", currentPath);
      } else {
         LOGGER.info("Loaded HUD layout '{}' ({} elements)", this.layoutFindSelector.name, this.layoutFindSelector.elements.size());
      }

      this.configRepository2.bindHudLayout(this.layoutFindSelector, currentPath);
      this.diagnosticsIsRecordingService = new DiagnosticsIsRecordingService(this.items, FabricLoader.getInstance().getGameDir());
      this.diagnosticsRecorderService.recorder(this.diagnosticsIsRecordingService);
      this.accountAllService = new AccountAllService(FabricLoader.getInstance().getGameDir());
      this.serverViewTracker = new ServerViewTracker(path);
      this.networkViewService = new NetworkViewService(path);
      this.anticheatRepository = new AnticheatRepository(path);
      this.screenDelayQueue.register("friends", () -> new FriendsScreen(this.friendsStateController, FriendSyncController::players));
      this.screenDelayQueue.register("anticheats", () -> new AnticheatScreen(this.anticheatRepository));
      this.screenDelayQueue.register("fritz-box", () -> new FritzBoxScreen(this.networkViewService));
      this.menuLoaderTracker = new MenuLoaderTracker(CompatReaderService.reader());
      this.screenDelayQueue.register("account-manager", () -> new BuiltinSessionTracker(this.accountAllService, this.compositorPushPresentationScaleService));
      this.builtinActionsTracker = new BuiltinActionsTracker(
         this.serverViewTracker,
         this.menuLoaderTracker,
         () -> new BuiltinActionsTracker.Session(mc().getUser().getProfileId(), mc().getUser().getName()),
         new BuiltinActionsTracker.Actions(
            (item, currentItem) -> {
               this.screenDelayQueue.closeForNavigation();
               CompatSavedService.connect(item, currentItem);
            },
            (item, currentItem) -> this.screenDelayQueue
               .open(new ServerPreviewScreen(this.serverViewTracker, mc().getUser().getProfileId(), item, currentItem, (address, direct) -> {
                  this.screenDelayQueue.closeForNavigation();
                  CompatSavedService.connect(address, direct);
               })),
            item -> CompatReaderService.play(item.id()),
            item -> CompatReaderService.edit(item.id()),
            CompatOptionsService::createWorld,
            () -> this.screenDelayQueue.open("account-manager"),
            CompatOptionsService::options,
            () -> this.screenDelayQueue.open("clickgui"),
            () -> {
               this.screenDelayQueue.open("changelog");
               this.changelogLastSeenService.markSeen(this.createText2());
               this.overlayOnActivateHandler2.setHasNews(false);
            },
            CompatOptionsService::quit
         ),
         FabricLoader.getInstance()
            .getModContainer("minecraft")
            .map(item -> item.getMetadata().getVersion().getFriendlyString())
            .orElse("")
      );
      this.screenDelayQueue.register("main-menu", () -> this.builtinActionsTracker);
      this.overlayOnActivateHandler.onActivate(item -> this.screenDelayQueue.open(new BuiltinSessionTracker(this.accountAllService, this.compositorPushPresentationScaleService)));
      CompatAdapterService.registerScreenClickGuard((item, currentItem) -> {
         if (!isReady()) {
            return true;
         }

         SceneDtService sceneDt = get().scene();
         if (sceneDt == null) {
            return true;
         }

         DisplayMetrics displayMetrics = get().viewport();
         return !sceneDt.hasInteractiveAt(displayMetrics.vanillaX(item), displayMetrics.vanillaY(currentItem));
      });
      this.eventSubscribeService
         .subscribe(
            EventAttackInputService.RENDER,
            item -> {
               Minecraft minecraft = Minecraft.getInstance();
               float currentWidth = this.compatData6.width();
               float currentHeight = this.compatData6.height();
               long longValue = System.nanoTime();
               float currentValue = this.timestamp2 == 0L
                  ? 0.016666668F
                  : (float)(longValue - this.timestamp2) / 1.0E9F;
               this.timestamp2 = longValue;
               if (!Float.isFinite(currentValue) || currentValue < 0.0F) {
                  currentValue = 0.0F;
               }

               currentValue = Math.min(currentValue, 0.1F);
               int nextValue = !(minecraft.screen instanceof CoreSetBehindScreenService)
                     && (this.screenDelayQueue == null || !this.screenDelayQueue.isActive())
                     && (this.overlayOnActivateHandler == null || !this.overlayOnActivateHandler.isVisible())
                     && (this.overlayOnActivateHandler2 == null || !this.overlayOnActivateHandler2.isVisible())
                  ? 0
                  : 1;
               this.items.begin("input");
               if (nextValue != 0) {
                  float previousValue = this.compatData6.mouseX(minecraft.mouseHandler.xpos());
                  float sourceValue = this.compatData6.mouseY(minecraft.mouseHandler.ypos());
                  long currentLongValue = CompatAdapterService.windowHandle(minecraft);
                  int targetValue = GLFW.glfwGetMouseButton(currentLongValue, 0) == 1 ? 1 : 0;
                  int inputValue = GLFW.glfwGetMouseButton(currentLongValue, 1) == 1 ? 1 : 0;
                  this.sceneDtService.mouseInput(previousValue, sourceValue, (targetValue != 0), (inputValue != 0));
               }

               this.items.end();
               this.sceneDtService.setFramebufferInfo((float)this.compatData6.renderScale(), this.compatData6.framebufferHeight());
               if (minecraft.level != null) {
                  this.renderer
                     .render(
                        this.compositorPushPresentationScaleService,
                        this.layoutFindSelector,
                        0.0F,
                        0.0F,
                        this.compatData6.vanillaWidth(),
                        this.compatData6.vanillaHeight(),
                        this.compatData6.hudScale(),
                        this.layoutOperationHandler
                     );
                  this.renderer2
                     .render(
                        this.compositorPushPresentationScaleService,
                        this.layoutOperationHandler,
                        this.compatData6.vanillaWidth(),
                        this.compatData6.vanillaHeight(),
                        this.compatData6.hudScale()
                     );
               }

               if (this.screenDelayQueue != null) {
                  this.screenDelayQueue.frame(currentValue, currentWidth, currentHeight);
               }

               if (this.overlayOnActivateHandler != null) {
                  this.overlayOnActivateHandler.update();
               }

               if (this.overlayOnActivateHandler2 != null) {
                  this.overlayOnActivateHandler2.update();
               }

               float outputValue = 0.0F;
               if (this.screenDelayQueue != null) {
                  outputValue = Math.max(outputValue, this.screenDelayQueue.backgroundDesaturation());
               }

               float resultValue = 1.0F - (float)Math.exp(-Math.min(currentValue, 0.1F) * 12.0F);
               this.value = this.value + (outputValue - this.value) * resultValue;
               if (Math.abs(outputValue - this.value) < 0.001F) {
                  this.value = outputValue;
               }

               this.compositorPushPresentationScaleService.backgroundDesaturate(this.value);
               this.items.begin("scene");
               this.sceneDtService.frame(currentValue, currentWidth, currentHeight);
               this.items.end();
            }
         );
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)item -> {
         if (isReady()) {
            this.discordStateController.tick(item, (int)this.moduleRegisterService.enabled().count());
            FriendSyncController.tick(this.friendsStateController);
            this.serverViewTracker.tick();
            this.menuLoaderTracker.tick();
            if (item.level != null && item.player != null) {
               this.renderer2.tick(this.layoutOperationHandler);
            }

            this.pluginLoadAllService.pollHotReload();
            long longValue = System.nanoTime();
            if (longValue - this.timestamp > 500000000L) {
               this.timestamp = longValue;
               this.pluginLoadAllService.fireEvent("tick");
            }
         }
      });
      this.eventSubscribeService
         .subscribe(
            EventAttackInputService.KEY,
            item -> {
               if (this.screenDelayQueue == null || !this.screenDelayQueue.capturesKeybindInput()) {
                  if (item.isRepeat() && checkCondition(item.keyCode())) {
                     item.cancel();
                  } else {
                     this.moduleBindService
                        .onKey(
                           item.keyCode(),
                           item.action(),
                           mc().screen == null
                              && mc().getOverlay() == null
                              && mc().player != null
                              && mc().level != null
                              && (this.screenDelayQueue == null || !this.screenDelayQueue.isActive())
                        );
                     if (item.isPress() && item.keyCode() == 298) {
                        FatalIsTrippedService.trip(FatalIsTrippedService.demoShaderFailure(), FatalCaptureService.Kind.RENDERER);
                        item.cancel();
                     } else if (item.isPress() && item.keyCode() == 293) {
                        if (this.diagnosticsRecorderService != null) {
                           this.diagnosticsRecorderService.toggle();
                        }

                        item.cancel();
                     } else if (item.isPress() && item.keyCode() == 295) {
                        if (this.diagnosticsIsRecordingService != null) {
                           if (this.diagnosticsIsRecordingService.isRecording()) {
                              this.diagnosticsIsRecordingService.cancel();
                           } else {
                              if (this.diagnosticsRecorderService != null && !this.diagnosticsRecorderService.isVisible()) {
                                 this.diagnosticsRecorderService.show();
                              }

                              this.diagnosticsIsRecordingService.start();
                           }
                        }

                        item.cancel();
                     } else if (item.isPress() && item.keyCode() == 344) {
                        if (this.screenDelayQueue != null) {
                           if ("clickgui".equals(this.screenDelayQueue.currentId())) {
                              if (this.screenDelayQueue.isClosing()) {
                                 this.screenDelayQueue.open("clickgui");
                              } else {
                                 this.screenDelayQueue.close();
                              }

                              item.cancel();
                           } else if (!this.screenDelayQueue.isActive() && mc().screen == null) {
                              this.screenDelayQueue.open("clickgui");
                              item.cancel();
                           }
                        }
                     } else if (item.isPress() && item.keyCode() == 345) {
                        if (this.screenDelayQueue != null) {
                           if ("hudeditor".equals(this.screenDelayQueue.currentId())) {
                              this.screenDelayQueue.close();
                              item.cancel();
                           } else if (!this.screenDelayQueue.isActive()) {
                              this.screenDelayQueue.open("hudeditor");
                              item.cancel();
                           }
                        }
                     } else {
                        if (item.isPress() && item.keyCode() == 297 && this.screenDelayQueue != null) {
                           if ("hudeditor".equals(this.screenDelayQueue.currentId())) {
                              this.screenDelayQueue.close();
                              item.cancel();
                           } else if (!this.screenDelayQueue.isActive()) {
                              this.screenDelayQueue.open("hudeditor");
                              item.cancel();
                           }
                        }

                        if (mc().screen == null) {
                           this.pluginLoadAllService.onKey(item.keyCode(), item.action());
                        }
                     }
                  }
               }
            }
         );
      this.eventSubscribeService.subscribe(EventAttackInputService.RENDER, new EventIsAfterHandler<EventAttackInputService.Render>() {
         boolean done;

         public void handle(EventAttackInputService.Render render) {
            if (!this.done) {
               this.done = true;
               CoreIsInitializedHandler.this.updateState2(CoreIsInitializedHandler.this.sceneDtService.root());
            }
         }
      });
      ClientLifecycleEvents.CLIENT_STARTED.register((ClientStarted)item -> this.configRepository2.load());
      LOGGER.info("{} initialized with {} module(s)", "ellice", this.moduleRegisterService.size());
   }

   public boolean showMainMenu() {
      return !this.enabled2 && this.builtinActionsTracker != null && this.screenDelayQueue != null && mc().level == null
         ? this.screenDelayQueue.open("main-menu")
         : false;
   }

   public boolean showServerMenu() {
      if (!this.enabled2 && this.builtinActionsTracker != null && this.screenDelayQueue != null && mc().level == null) {
         this.builtinActionsTracker.page(BuiltinActionsTracker.Page.SERVERS);
         return this.screenDelayQueue.open("main-menu");
      } else {
         return false;
      }
   }

   private String createText2() {
      return this.changelogRepository != null && this.changelogRepository.latest() != null ? this.changelogRepository.latest().version() : VERSION;
   }

   public boolean handleKeyInput(long longValue, int value, int currentValue, int nextValue) {
      if (!this.enabled2 && longValue == CompatAdapterService.windowHandle(mc()) && mc().isWindowActive()) {
         Screen currentScreen = mc().screen;
         EventAttackInputService.Key key = this.eventSubscribeService.post(EventAttackInputService.KEY, new EventAttackInputService.Key(value, currentValue, nextValue));
         return key.isCancelled() || mc().screen != currentScreen;
      } else {
         return false;
      }
   }

   private static boolean checkCondition(int value) {
      return value == 344 || value == 345 || value == 293 || value == 295 || value == 296 || value == 297 || value == 298;
   }

   public void shutdown() {
      if (!this.enabled2) {
         this.enabled2 = true;
         LOGGER.info("Shutting down...");
         if (this.discordStateController != null) {
            this.discordStateController.close();
         }

         if (this.renderer2 != null) {
            this.renderer2.releaseActions();
         }

         if (this.configRepository2 != null) {
            this.configRepository2.save();
         }

         if (this.serverViewTracker != null) {
            this.serverViewTracker.close();
         }

         if (this.networkViewService != null) {
            this.networkViewService.close();
         }

         if (this.anticheatRepository != null) {
            this.anticheatRepository.close();
         }

         if (this.menuLoaderTracker != null) {
            this.menuLoaderTracker.close();
         }

         if (this.screenDelayQueue != null) {
            this.screenDelayQueue.closeNow();
         }

         if (this.configRepository != null) {
            this.configRepository.save();
         }

         if (this.sceneDtService != null) {
            this.sceneDtService.shutdown();
         }

         LocalConfigRepository.runWithoutSaving(this.moduleRegisterService::disableAll);
         if (this.renderer3 != null) {
            this.renderer3.close();
         }

         this.compositorPushPresentationScaleService.shutdown();
         this.eventSubscribeService.clear();
      }
   }

   public EventSubscribeService bus() {
      return this.eventSubscribeService;
   }

   public ModuleRegisterService modules() {
      return this.moduleRegisterService;
   }

   public HudStyleSettings hudStyle() {
      return this.values;
   }

   public HudItemsService hudItems() {
      return this.hudItemsService;
   }

   public CompositorPushPresentationScaleService compositor() {
      return this.compositorPushPresentationScaleService;
   }

   public ThemeIsSetService theme() {
      return this.values2;
   }

   public LocalConfigRepository config() {
      return this.configRepository2;
   }

   public ServerViewTracker serverHub() {
      return this.serverViewTracker;
   }

   public ModuleBindService keybinds() {
      return this.moduleBindService;
   }

   public SceneDtService scene() {
      return this.sceneDtService;
   }

   public ScreenDelayQueue screens() {
      return this.screenDelayQueue;
   }

   public PluginLoadAllService pluginLoader() {
      return this.pluginLoadAllService;
   }

   public DiagnosticsAddFrameListenerService profiler() {
      return this.items;
   }

   public DiagnosticsRecorderService perfOverlay() {
      return this.diagnosticsRecorderService;
   }

   public DiagnosticsIsRecordingService perfRecorder() {
      return this.diagnosticsIsRecordingService;
   }

   public Render3dSceneService renderer3D() {
      return this.renderer3;
   }

   public LayoutFindSelector hudLayout() {
      return this.layoutFindSelector;
   }

   public LayoutRenderer hudLayoutRenderer() {
      return this.renderer;
   }

   public LayoutOperationHandler hudVariables() {
      return this.layoutOperationHandler;
   }

   public StudioRenderer moduleStudio() {
      return this.renderer2;
   }

   public void closeClickGui() {
      if (this.screenDelayQueue != null && "clickgui".equals(this.screenDelayQueue.currentId())) {
         this.screenDelayQueue.close();
      }
   }

   private void updateState2(ScenePctService<?> scenePct) {
      if (scenePct.isDraggable() && scenePct.getId() != null) {
         Minecraft minecraft = Minecraft.getInstance();
         DisplayMetrics displayMetrics = CompatAdapterService.uiViewport(minecraft, minecraft.screen instanceof CoreSetBehindScreenService);
         float currentWidth = displayMetrics.width();
         float currentHeight = displayMetrics.height();
         float value = scenePct.width > 0.0F ? scenePct.width : 100.0F;
         float currentValue = scenePct.height > 0.0F ? scenePct.height : 50.0F;
         float[] floats = this.configRepository2.getPosition(scenePct.getId(), currentWidth, currentHeight, value, currentValue);
         if (floats != null) {
            scenePct.x = floats[0];
            scenePct.y = floats[1];
         }
      }

      for (ScenePctService currentScenePct : scenePct.children()) {
         this.updateState2(currentScenePct);
      }
   }

   public void beginRenderFrame() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.screen instanceof TitleScreen && this.screenDelayQueue != null && !this.screenDelayQueue.isActive()) {
         this.showMainMenu();
      }

      this.compatData6 = CompatAdapterService.uiViewport(
         minecraft, minecraft.screen instanceof CoreSetBehindScreenService || this.screenDelayQueue != null && this.screenDelayQueue.isActive()
      );
   }

   public DisplayMetrics viewport() {
      return this.compatData6;
   }

   public static Minecraft mc() {
      return Minecraft.getInstance();
   }
}

