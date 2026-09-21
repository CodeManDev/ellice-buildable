package dev.felix.ellice.ui.screen;

import dev.felix.ellice.core.CoreSetBehindScreenService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.screen.builtin.ClickGuiScreen;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ScreenDelayQueue {
  private static final Logger logger = LoggerFactory.getLogger(ScreenDelayQueue.class);
  private final SceneDtService sceneDtService;
  private final ThemeIsSetService values2;
  private final ScreenDelayQueue.HostBridge hostBridge;
  private final LayoutContainerNode sceneComponent4 = new LayoutContainerNode();
  private final Map<String, Supplier<? extends ScreenOperationHandler>> text = new HashMap<>();
  private ScreenOperationHandler screenOperationHandler;
  private ScreenScreenIdService screenScreenIdService;
  private ScenePctService<?> scenePctService;
  private SceneCodec.Preset values3;
  private SceneCodec.Options options2;
  private ScreenDelayQueue.Lifecycle lifecycle;
  private boolean enabled;
  private long timestamp;
  private final Deque<ScreenDelayQueue.Suspended> items = new ArrayDeque<>();

  public ScreenDelayQueue(SceneDtService sceneDt, ThemeIsSetService themeIsSet) {
    this(sceneDt, themeIsSet, new ScreenDelayQueue.MinecraftHostBridge());
  }

  ScreenDelayQueue(
      SceneDtService sceneDt,
      ThemeIsSetService themeIsSet,
      ScreenDelayQueue.HostBridge currentHostBridge) {
    this.sceneDtService = Objects.requireNonNull(sceneDt, "scene");
    this.values2 = Objects.requireNonNull(themeIsSet, "theme");
    this.hostBridge = Objects.requireNonNull(currentHostBridge, "screenHost");
    this.sceneComponent4
        .id("client.screenHost")
        .size(ScenePctService.pct(100.0F), ScenePctService.pct(100.0F))
        .direction(ScenePctService.Direction.NONE)
        .layerBreak(true)
        .visible(false);
    sceneDt.root().addChild(this.sceneComponent4);
  }

  public void register(String currentText, Supplier<? extends ScreenOperationHandler> supplier) {
    if (currentText != null && !currentText.isBlank() && supplier != null) {
      this.text.put(currentText, supplier);
    }
  }

  public boolean open(String currentText) {
    Supplier supplier = this.text.get(currentText);
    if (supplier == null) {
      return false;
    }

    if (this.screenOperationHandler != null
        && currentText.equals(this.screenOperationHandler.id())
        && this.lifecycle == ScreenDelayQueue.Lifecycle.CLOSING
        && this.scenePctService != null
        && this.values3 == SceneCodec.Preset.WORKSPACE) {
      long longValue = ++this.timestamp;
      ScreenOperationHandler screenOperation = this.screenOperationHandler;
      this.lifecycle = ScreenDelayQueue.Lifecycle.OPEN;
      this.enabled = false;
      this.scenePctService.pointerEvents(true);
      this.sceneDtService.transitions().show(this.scenePctService, this.values3, this.options2);
      this.hostBridge.activate(screenOperation);

      try {
        screenOperation.onOpen(this.screenScreenIdService);
        return true;
      } catch (RuntimeException exception) {
        logger.error("Failed to reopen {}", createText(screenOperation), exception);
        if (longValue == this.timestamp && this.screenOperationHandler == screenOperation) {
          this.close();
        }

        return false;
      }
    } else {
      ScreenOperationHandler currentScreenOperation;
      try {
        currentScreenOperation = (ScreenOperationHandler) supplier.get();
      } catch (RuntimeException currentException) {
        logger.error("Failed to create client screen '{}'", currentText, currentException);
        return false;
      }

      return this.checkCondition(currentScreenOperation);
    }
  }

  public void open(ScreenOperationHandler screenOperation) {
    this.checkCondition(screenOperation);
  }

  private boolean checkCondition(ScreenOperationHandler screenOperation) {
    if (screenOperation == null) {
      return false;
    }

    if (screenOperation == this.screenOperationHandler
        && this.lifecycle == ScreenDelayQueue.Lifecycle.OPEN) {
      return true;
    }

    long longValue = ++this.timestamp;

    ScreenScreenIdService screenScreenId;
    ScenePctService scenePct;
    SceneCodec.Preset preset;
    SceneCodec.Options options;
    try {
      String text = screenOperation.id();
      screenScreenId = new ScreenScreenIdService(this, this.values2, text);
      scenePct = screenOperation.build(screenScreenId);
      preset = screenOperation.transitionPreset();
      options = createOptions(screenOperation.transitionOptions());
    } catch (RuntimeException exception) {
      logger.error("Failed to build client screen {}", createText(screenOperation), exception);
      return false;
    }

    if (scenePct == null) {
      logger.error("Client screen {} returned a null root", createText(screenOperation));
      return false;
    }

    if (longValue != this.timestamp) {
      return false;
    }

    scenePct
        .size(ScenePctService.pct(100.0F), ScenePctService.pct(100.0F))
        .layerBreak(true)
        .opacity(0.0F)
        .pointerEvents(true)
        .visible(false);
    if (this.screenOperationHandler != null) {
      if (screenOperation.opensAsWindow() && this.lifecycle == ScreenDelayQueue.Lifecycle.OPEN) {
        ScreenOperationHandler currentScreenOperation = this.screenOperationHandler;

        try {
          currentScreenOperation.onSuspend(this.screenScreenIdService);
        } catch (RuntimeException currentException) {
          logger.error(
              "Failed to suspend {}", createText(currentScreenOperation), currentException);
        }

        if (longValue != this.timestamp || this.screenOperationHandler != currentScreenOperation) {
          return false;
        }

        this.sceneDtService.transitions().show(this.scenePctService, SceneCodec.Preset.NONE, null);
        this.scenePctService.pointerEvents(false);
        this.items.push(
            new ScreenDelayQueue.Suspended(
                this.screenOperationHandler,
                this.screenScreenIdService,
                this.scenePctService,
                this.values3,
                this.options2));
        this.updateState3();
      } else if (!this.checkCondition2(longValue)) {
        return false;
      }
    }

    this.sceneDtService.clearFocus();
    this.sceneComponent4.visible(true);
    this.updateState4();
    this.screenOperationHandler = screenOperation;
    this.screenScreenIdService = screenScreenId;
    this.scenePctService = scenePct;
    this.values3 = preset != null ? preset : SceneCodec.Preset.MODAL;
    this.options2 = options;
    this.lifecycle = ScreenDelayQueue.Lifecycle.OPEN;
    this.enabled = false;
    this.sceneComponent4.addChild(scenePct);
    this.hostBridge.activate(screenOperation);
    this.sceneDtService.transitions().show(scenePct, this.values3, this.options2);

    try {
      screenOperation.onOpen(screenScreenId);
      return true;
    } catch (RuntimeException nextException) {
      logger.error(
          "Client screen {} failed during onOpen", createText(screenOperation), nextException);
      if (this.screenOperationHandler == screenOperation && this.scenePctService == scenePct) {
        this.updateState();
        this.sceneDtService.transitions().hide(scenePct, SceneCodec.Preset.NONE, null);
        this.updateState2(scenePct);
      }

      return false;
    }
  }

  public void close() {
    if (this.screenOperationHandler != null
        && this.lifecycle != ScreenDelayQueue.Lifecycle.CLOSING) {
      long longValue = ++this.timestamp;
      ScreenOperationHandler screenOperation = this.screenOperationHandler;
      ScenePctService scenePct = this.scenePctService;
      this.lifecycle = ScreenDelayQueue.Lifecycle.CLOSING;
      this.sceneDtService.clearFocus();
      if (scenePct != null) {
        scenePct.pointerEvents(false);
        this.sceneDtService.transitions().hide(scenePct, this.values3, this.options2);
      }

      this.updateState();
      if (longValue == this.timestamp
          && this.screenOperationHandler == screenOperation
          && this.scenePctService == scenePct) {
        if (scenePct == null || !this.sceneDtService.transitions().isTransitioning(scenePct)) {
          this.updateState2(scenePct);
        }
      }
    }
  }

  public void closeNow() {
    if (this.screenOperationHandler == null) {
      this.sceneDtService.clearFocus();
      this.sceneComponent4.clearChildren();
      this.sceneComponent4.visible(false);
      this.hostBridge.release();
    } else {
      long longValue = ++this.timestamp;
      ScreenOperationHandler screenOperation = this.screenOperationHandler;
      ScenePctService scenePct = this.scenePctService;
      this.lifecycle = ScreenDelayQueue.Lifecycle.CLOSING;
      this.sceneDtService.clearFocus();
      if (scenePct != null) {
        scenePct.pointerEvents(false);
        this.sceneDtService.transitions().hide(scenePct, SceneCodec.Preset.NONE, null);
      }

      this.updateState();
      if (longValue == this.timestamp
          && this.screenOperationHandler == screenOperation
          && this.scenePctService == scenePct) {
        if (this.checkCondition3(longValue)) {
          this.updateState2(scenePct);
        }
      }
    }
  }

  public void closeForNavigation() {
    long longValue = ++this.timestamp;
    if (this.screenOperationHandler == null || this.checkCondition2(longValue)) {
      if (this.checkCondition3(longValue)) {
        this.sceneDtService.clearFocus();
        this.sceneComponent4.clearChildren();
        this.sceneComponent4.visible(false);
        this.hostBridge.abandon();
      }
    }
  }

  public void frame(float value, float currentValue) {
    this.frame(0.016666668F, value, currentValue);
  }

  public void frame(float value, float currentValue, float nextValue) {
    if (this.screenOperationHandler != null) {
      this.updateState4();
      if (this.lifecycle != ScreenDelayQueue.Lifecycle.CLOSING) {
        this.hostBridge.ensureActive(this.screenOperationHandler);
        ScreenOperationHandler screenOperation = this.screenOperationHandler;
        ScreenScreenIdService screenScreenId = this.screenScreenIdService;
        screenScreenId.beginFrame(value);

        try {
          screenOperation.tick(screenScreenId);
        } catch (RuntimeException exception) {
          logger.error(
              "Client screen {} failed during tick", createText(screenOperation), exception);
        }
      } else {
        ScenePctService scenePct = this.scenePctService;
        if (scenePct == null || !this.sceneDtService.transitions().isTransitioning(scenePct)) {
          this.updateState2(scenePct);
        }
      }
    }
  }

  public boolean keyPressed(int value, int currentValue) {
    if (this.screenOperationHandler == null) {
      return false;
    } else if (this.lifecycle == ScreenDelayQueue.Lifecycle.CLOSING) {
      return true;
    } else {
      ScreenOperationHandler screenOperation = this.screenOperationHandler;
      ScreenScreenIdService screenScreenId = this.screenScreenIdService;
      if (screenOperation.keyPressed(screenScreenId, value, currentValue)) {
        return true;
      } else if (this.screenOperationHandler != screenOperation
          || this.lifecycle == ScreenDelayQueue.Lifecycle.CLOSING) {
        return true;
      } else if (value == 256 && screenOperation.closesOnEscape()) {
        this.close();
        return true;
      } else {
        return true;
      }
    }
  }

  public boolean navigationKeyPressed(int value, int currentValue) {
    if (this.capturesKeybindInput()) {
      return this.keyPressed(value, currentValue);
    } else if (this.screenOperationHandler != null
        && ("clickgui".equals(this.screenOperationHandler.id())
            || "client-settings".equals(this.screenOperationHandler.id())
            || "configs".equals(this.screenOperationHandler.id())
            || "anticheats".equals(this.screenOperationHandler.id())
            || "friends".equals(this.screenOperationHandler.id())
            || "fritz-box".equals(this.screenOperationHandler.id())
            || "account-manager".equals(this.screenOperationHandler.id())
            || "main-menu".equals(this.screenOperationHandler.id())
            || "module-studio".equals(this.screenOperationHandler.id()))) {
      int nextValue = value == 70 && (currentValue & 10) != 0 ? 1 : 0;
      int previousValue =
          !"module-studio".equals(this.screenOperationHandler.id())
                  || value != 256 && (value != 83 || (currentValue & 10) == 0)
              ? 0
              : 1;
      return value != 258 && nextValue == 0 && previousValue == 0
          ? false
          : this.keyPressed(value, currentValue);
    } else {
      return false;
    }
  }

  public boolean isActive() {
    return this.screenOperationHandler != null;
  }

  public boolean capturesKeybindInput() {
    return this.screenOperationHandler instanceof ClickGuiScreen clickGuiScreen
        && clickGuiScreen.capturesKeybindInput();
  }

  public boolean isClosing() {
    return this.screenOperationHandler != null
        && this.lifecycle == ScreenDelayQueue.Lifecycle.CLOSING;
  }

  public float backgroundDesaturation() {
    return this.screenOperationHandler != null
        ? this.screenOperationHandler.backgroundDesaturation()
        : 0.0F;
  }

  public String currentId() {
    return this.screenOperationHandler != null ? this.screenOperationHandler.id() : null;
  }

  private boolean checkCondition2(long longValue) {
    ScreenOperationHandler screenOperation = this.screenOperationHandler;
    ScenePctService scenePct = this.scenePctService;
    this.lifecycle = ScreenDelayQueue.Lifecycle.CLOSING;
    this.updateState();
    if (longValue == this.timestamp
        && this.screenOperationHandler == screenOperation
        && this.scenePctService == scenePct) {
      this.sceneDtService.clearFocus();
      if (scenePct != null) {
        scenePct.pointerEvents(false);
        this.sceneDtService.transitions().hide(scenePct, SceneCodec.Preset.NONE, null);
      }

      if (scenePct != null) {
        this.sceneComponent4.removeChild(scenePct);
      }

      this.updateState3();
      return true;
    } else {
      return false;
    }
  }

  private boolean checkCondition3(long longValue) {
    while (!this.items.isEmpty()) {
      ScreenDelayQueue.Suspended suspended = this.items.pop();

      try {
        suspended.screen.onClose(suspended.context);
      } catch (RuntimeException exception) {
        logger.error("Failed to close {}", createText(suspended.screen), exception);
      }

      this.sceneDtService.transitions().hide(suspended.root, SceneCodec.Preset.NONE, null);
      this.sceneComponent4.removeChild(suspended.root);
      if (longValue != this.timestamp) {
        return false;
      }
    }

    return true;
  }

  private void updateState() {
    if (this.screenOperationHandler != null && !this.enabled) {
      this.enabled = true;

      try {
        this.screenOperationHandler.onClose(this.screenScreenIdService);
      } catch (RuntimeException exception) {
        logger.error(
            "Client screen {} failed during onClose",
            createText(this.screenOperationHandler),
            exception);
      }
    }
  }

  private void updateState2(ScenePctService<?> scenePct) {
    if (this.screenOperationHandler != null && this.scenePctService == scenePct) {
      this.sceneDtService.clearFocus();
      if (scenePct != null) {
        this.sceneComponent4.removeChild(scenePct);
      }

      this.updateState3();
      if (!this.items.isEmpty()) {
        ScreenDelayQueue.Suspended suspended = this.items.pop();
        this.screenOperationHandler = suspended.screen;
        this.screenScreenIdService = suspended.context;
        this.scenePctService = suspended.root;
        this.values3 = suspended.preset;
        this.options2 = suspended.options;
        this.lifecycle = ScreenDelayQueue.Lifecycle.OPEN;
        this.scenePctService.pointerEvents(true);
        this.hostBridge.activate(this.screenOperationHandler);
      } else {
        this.sceneComponent4.visible(false);
        this.hostBridge.release();
      }
    }
  }

  private void updateState3() {
    this.screenOperationHandler = null;
    this.screenScreenIdService = null;
    this.scenePctService = null;
    this.values3 = null;
    this.options2 = null;
    this.lifecycle = null;
    this.enabled = false;
  }

  private void updateState4() {
    List items = this.sceneDtService.root().children();
    if (items.isEmpty() || items.get(items.size() - 1) != this.sceneComponent4) {
      this.sceneDtService.root().bringChildToFront(this.sceneComponent4);
    }
  }

  private static SceneCodec.Options createOptions(SceneCodec.Options options) {
    SceneCodec.Options currentOptions = new SceneCodec.Options();
    if (options == null) {
      return currentOptions;
    }

    currentOptions.duration = options.duration;
    currentOptions.offset = options.offset;
    currentOptions.opacity = options.opacity;
    currentOptions.scale = options.scale;
    return currentOptions;
  }

  private static String createText(ScreenOperationHandler screenOperation) {
    if (screenOperation == null) {
      return "<null>";
    }

    try {
      String text = screenOperation.id();
      return text != null ? "'" + text + "'" : screenOperation.getClass().getSimpleName();
    } catch (RuntimeException exception) {
      return screenOperation.getClass().getSimpleName();
    }
  }

  interface HostBridge {
    void activate(ScreenOperationHandler screenOperation);

    void ensureActive(ScreenOperationHandler screenOperation);

    void release();

    default void abandon() {}
  }

  private enum Lifecycle {
    OPEN,
    CLOSING;

    private static ScreenDelayQueue.Lifecycle[] $values() {
      return new ScreenDelayQueue.Lifecycle[] {OPEN, CLOSING};
    }
  }

  private static final class MinecraftHostBridge implements ScreenDelayQueue.HostBridge {
    private boolean enabled2;
    private Screen screen2;

    @Override
    public void activate(ScreenOperationHandler screenOperation) {
      Minecraft minecraft = Minecraft.getInstance();
      if (!(minecraft.screen instanceof CoreSetBehindScreenService)) {
        this.enabled2 = true;
        this.screen2 = minecraft.screen;
        CoreSetBehindScreenService coreSetBehindScreen = new CoreSetBehindScreenService();
        if (screenOperation.overlaysPreviousScreen() && !(this.screen2 instanceof TitleScreen)) {
          coreSetBehindScreen.setBehindScreen(this.screen2);
        }

        minecraft.setScreen(coreSetBehindScreen);
      } else if (minecraft.screen
          instanceof CoreSetBehindScreenService currentCoreSetBehindScreen) {
        currentCoreSetBehindScreen.setBehindScreen(
            screenOperation.overlaysPreviousScreen() && !(this.screen2 instanceof TitleScreen)
                ? this.screen2
                : null);
      }
    }

    @Override
    public void ensureActive(ScreenOperationHandler screenOperation) {
      Minecraft minecraft = Minecraft.getInstance();
      if (!(minecraft.screen instanceof CoreSetBehindScreenService)) {
        if (!this.enabled2) {
          this.enabled2 = true;
          this.screen2 = minecraft.screen;
        }

        CoreSetBehindScreenService coreSetBehindScreen = new CoreSetBehindScreenService();
        if (screenOperation.overlaysPreviousScreen() && !(this.screen2 instanceof TitleScreen)) {
          coreSetBehindScreen.setBehindScreen(this.screen2);
        }

        minecraft.setScreen(coreSetBehindScreen);
      }
    }

    @Override
    public void release() {
      if (this.enabled2) {
        Minecraft minecraft = Minecraft.getInstance();
        Screen currentScreen = this.screen2;
        this.abandon();
        if (minecraft.screen instanceof CoreSetBehindScreenService) {
          Object value = currentScreen;
          if (minecraft.level == null
              && !(value instanceof JoinMultiplayerScreen)
              && !(value instanceof SelectWorldScreen)) {
            value = new TitleScreen();
          }

          minecraft.setScreen((Screen) value);
        }
      }
    }

    @Override
    public void abandon() {
      this.enabled2 = false;
      this.screen2 = null;
    }
  }

  private record Suspended(
      ScreenOperationHandler screen,
      ScreenScreenIdService context,
      ScenePctService<?> root,
      SceneCodec.Preset preset,
      SceneCodec.Options options) {}
}
