package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextMode;
import java.util.function.Consumer;

final class ClickGuiToolbar implements ComponentOperationHandler {
  private final Consumer<String> text2;
  private final Consumer<Boolean> consumer;
  private final Runnable runnable;
  private ComponentThemeService componentThemeService;
  private ControlLetterSpacingService controlLetterSpacingService;
  private String text3 = "";
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private int count;
  private int count2;
  private int count3;

  ClickGuiToolbar(
      Consumer<String> currentConsumer, Consumer<Boolean> nextConsumer, Runnable currentRunnable) {
    this.text2 = currentConsumer;
    this.consumer = nextConsumer;
    this.runnable = currentRunnable;
  }

  void summary(int value, int currentValue, int nextValue) {
    if (this.count != value || this.count2 != currentValue || this.count3 != nextValue) {
      this.count = value;
      this.count2 = currentValue;
      this.count3 = nextValue;
      this.updateState3();
    }
  }

  void viewport(float x, float y) {
    int width = x <= 480.0F ? 1 : 0;
    int height = y <= 320.0F ? 1 : 0;
    if (this.enabled3 != (width != 0) || this.enabled4 != (height != 0)) {
      this.enabled3 = (width != 0);
      this.enabled4 = (height != 0);
      this.updateState3();
    }
  }

  void focusSearch() {
    if (this.controlLetterSpacingService != null) {
      if (CoreIsInitializedHandler.isReady()) {
        CoreIsInitializedHandler.get().scene().setFocusedInput(this.controlLetterSpacingService);
      } else {
        this.controlLetterSpacingService.focus();
      }
    }
  }

  boolean dismissSearch() {
    if (!this.text3.isEmpty()) {
      this.updateState();
      return true;
    } else if (this.controlLetterSpacingService != null
        && this.controlLetterSpacingService.focused()) {
      this.controlLetterSpacingService.unfocus();
      return true;
    } else {
      return false;
    }
  }

  private void updateState() {
    if (this.controlLetterSpacingService != null) {
      this.controlLetterSpacingService.text("");
    }

    this.updateState2("");
    this.focusSearch();
  }

  private void updateState2(String text) {
    this.text3 = text;
    this.text2.accept(text);
    this.updateState3();
  }

  private void updateState3() {
    if (this.componentThemeService != null) {
      this.componentThemeService.invalidate();
    }
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
    this.componentThemeService = componentTheme;
    ComponentStyleService currentSize =
        ComponentStyleService.style()
            .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
            .padding(this.enabled4 ? 4.0F : 8.0F, 10.0F)
            .gap(8.0F)
            .align(ScenePctService.Align.CENTER)
            .build();
    ComponentKeyService<?> nextSize =
        ComponentBoxService.row(
                item ->
                    item.size(LayoutOperationHandler.auto(), LayoutOperationHandler.percent(100.0F))
                        .gap(8.0F)
                        .align(ScenePctService.Align.CENTER)
                        .flexShrink(0.0F)
                        .pointerEvents(false),
                ComponentBoxService.panel(
                    item ->
                        item.size(26.0F, 26.0F)
                            .cornerRadius(8.0F)
                            .backgroundColor(-14664624)
                            .border(0.65F, -12424317)
                            .direction(ScenePctService.Direction.ROW)
                            .align(ScenePctService.Align.CENTER)
                            .justify(ScenePctService.Justify.CENTER),
                    createComponentKeyService("ellice", 15.0F, -8530948)),
                ComponentBoxService.column(
                    item ->
                        item.gap(1.0F)
                            .justify(ScenePctService.Justify.CENTER)
                            .visible(!this.enabled3)
                            .pointerEvents(false),
                    ComponentBoxService.text(
                        "ellice",
                        item ->
                            item.fontSize(11.0F)
                                .fontVariant(TextMode.BOLD)
                                .color(-985604)
                                .inheritEdgeSoftness(false)),
                    ComponentBoxService.text(
                        this.count == this.count2
                            ? this.count2 + " modules"
                            : this.count + " of " + this.count2 + " modules",
                        item ->
                            item.id("clickgui.toolbar.summary")
                                .fontSize(6.2F)
                                .color(-5456944)
                                .inheritEdgeSoftness(false))))
            .key("brand");
    ComponentKeyService<?> previousSize =
        ComponentBoxService.panel(
                item ->
                    item.id("clickgui.toolbar.search-field")
                        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.px(28.0F))
                        .minWidth(0.0F)
                        .flex(1.0F)
                        .cornerRadius(7.0F)
                        .backgroundColor(-15195344)
                        .border(0.65F, -13286826)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .padding(0.0F, 6.0F)
                        .gap(3.0F)
                        .cursorStyle(ScenePctService.CursorStyle.TEXT)
                        .onClick(this::focusSearch)
                        .stopPropagation(true),
                createComponentKeyService("search", 11.0F, -8154198),
                ComponentBoxService.<ControlLetterSpacingService>node(
                        "module-search",
                        ControlLetterSpacingService::new,
                        item ->
                            item.id("clickgui.toolbar.search")
                                .size(
                                    LayoutOperationHandler.auto(),
                                    LayoutOperationHandler.percent(100.0F))
                                .minWidth(0.0F)
                                .flex(1.0F)
                                .fontSize(7.3F)
                                .maxLength(96)
                                .placeholder("Search modules...")
                                .bgColor(0)
                                .textColor(-985604)
                                .placeholderColor(-8154198)
                                .selectionColor(1434309628)
                                .focusBorder(-8530948)
                                .cornerRadius(4.0F)
                                .onChanged(this::updateState2)
                                .tooltip("Search by module, description or category. Ctrl/Cmd + K")
                                .stopPropagation(true))
                    .key("search-input")
                    .onMount(item -> this.controlLetterSpacingService = item)
                    .onUnmount(
                        item -> {
                          item.unfocus();
                          this.controlLetterSpacingService = null;
                        }),
                createComponentKeyService2("clear-search", "x", "Clear search", this::updateState)
                    .props(
                        item ->
                            item.size(18.0F, 18.0F)
                                .visible(!this.text3.isEmpty())
                                .pointerEvents(!this.text3.isEmpty())))
            .key("search");
    ComponentKeyService<?> sourceSize =
        ComponentBoxService.panel(
                item ->
                    item.id("clickgui.toolbar.enabled")
                        .size(
                            LayoutOperationHandler.px(this.enabled3 ? 28.0F : 83.0F),
                            LayoutOperationHandler.px(28.0F))
                        .flexShrink(0.0F)
                        .cornerRadius(7.0F)
                        .backgroundColor(this.enabled2 ? -14664624 : -14537670)
                        .hoverBackground(this.enabled2 ? -14005406 : -13944759)
                        .pressBackground(-15195344)
                        .border(0.65F, this.enabled2 ? -12424317 : -13286826)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .justify(ScenePctService.Justify.CENTER)
                        .gap(5.0F)
                        .cursorStyle(ScenePctService.CursorStyle.POINTER)
                        .stopPropagation(true)
                        .tooltip(
                            this.enabled2
                                ? "Show all modules"
                                : "Show enabled modules (" + this.count3 + ")")
                        .onClick(
                            () -> {
                              this.enabled2 = !this.enabled2;
                              this.consumer.accept(this.enabled2);
                              componentTheme.invalidate();
                            }),
                createComponentKeyService("power", 11.0F, this.enabled2 ? -8530948 : -5456944),
                ComponentBoxService.text(
                    "Enabled " + this.count3,
                    item ->
                        item.fontSize(6.8F)
                            .color(this.enabled2 ? -8530948 : -5456944)
                            .visible(!this.enabled3)
                            .pointerEvents(false)
                            .inheritEdgeSoftness(false)))
            .key("enabled-filter");
    return ComponentBoxService.panel(
            currentSize,
            item ->
                item.id("clickgui.toolbar")
                    .direction(ScenePctService.Direction.ROW)
                    .cornerRadius(12.0F)
                    .backgroundColor(-99280092)
                    .border(0.65F, -13286826)
                    .shadow(12.0F)
                    .shadowColor(1610612736)
                    .clip(true)
                    .stopPropagation(true),
            nextSize,
            previousSize,
            sourceSize,
            createComponentKeyService2("close", "x", "Close (Esc)", this.runnable))
        .key("toolbar");
  }

  private static ComponentKeyService<SceneSrcService> createComponentKeyService(
      String text, float value, int currentValue) {
    return ComponentBoxService.svg(
        BuiltinComponent.iconPath(text),
        item ->
            item.size(value, value)
                .flexShrink(0.0F)
                .tintColor(currentValue)
                .inheritEdgeSoftness(false)
                .pointerEvents(false));
  }

  private static ComponentKeyService<SceneCornerRadiusService> createComponentKeyService2(
      String text, String currentText, String nextText, Runnable runnable) {
    return ComponentBoxService.panel(
            item ->
                item.id("clickgui.toolbar." + text)
                    .size(28.0F, 28.0F)
                    .flexShrink(0.0F)
                    .cornerRadius(7.0F)
                    .backgroundColor(0)
                    .hoverBackground(-13944759)
                    .pressBackground(-15195344)
                    .direction(ScenePctService.Direction.ROW)
                    .align(ScenePctService.Align.CENTER)
                    .justify(ScenePctService.Justify.CENTER)
                    .tooltip(nextText)
                    .cursorStyle(ScenePctService.CursorStyle.POINTER)
                    .stopPropagation(true)
                    .onClick(runnable),
            createComponentKeyService(currentText, 11.0F, -5456944))
        .key(text);
  }
}
