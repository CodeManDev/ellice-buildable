package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneContentService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class ModeSettingControl extends ScenePctService<ModeSettingControl> {
  public static final float DEFAULT_WIDTH = 112.0F;
  public static final float TRIGGER_HEIGHT = 21.0F;
  public static final float OPTION_HEIGHT = 20.0F;
  private static final int count = -1206248156;
  private static final int count2 = -803068368;
  private static final int count3 = -534303689;
  private static final int count4 = 620756991;
  private static final int count5 = -1475078371;
  private static final int count6 = -802936525;
  private static final int count7 = 964547580;
  private static final int count8 = -637534209;
  private static final int count9 = -1459617793;
  private static final int count10 = -1;
  private final ModuleSetting.Mode moduleMode;
  private final SceneCornerRadiusService sceneCornerRadiusService;
  private final SceneTextService sceneTextService;
  private final SceneTextService sceneTextService2;
  private final LayoutContainerNode sceneComponent4;
  private final SceneContentService sceneContentService;
  private final List<ModeSettingControl.OptionView> items;
  private final AutoCloseable autoCloseable;
  private Consumer<String> text2;
  private Runnable runnable;
  private boolean enabled2 = true;
  private boolean enabled3 = true;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;

  public ModeSettingControl(ModuleSetting.Mode currentMode) {
    this.moduleMode = Objects.requireNonNull(currentMode, "setting");
    this.direction(ScenePctService.Direction.COLUMN);
    this.align(ScenePctService.Align.STRETCH);
    this.justify(ScenePctService.Justify.START);
    this.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto());
    this.flexShrink(0.0F);
    this.sceneTextService =
        createSceneTextService((String) currentMode.get(), 7.2F, -637534209)
            .overflow(SceneTextService.Overflow.ELLIPSIS)
            .flex(1.0F);
    this.sceneTextService2 = createSceneTextService("⌄", 8.0F, -1459617793).flexShrink(0.0F);
    this.sceneCornerRadiusService =
        new SceneCornerRadiusService()
            .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(21.0F))
            .flexShrink(0.0F)
            .cornerRadius(5.0F)
            .backgroundColor(-1206248156)
            .hoverBackground(-803068368)
            .pressBackground(-534303689)
            .border(0.55F, 620756991)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .padding(0.0F, 7.0F)
            .gap(5.0F)
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(this::updateState);
    this.sceneCornerRadiusService.addChild(this.sceneTextService).addChild(this.sceneTextService2);
    this.sceneComponent4 =
        new LayoutContainerNode()
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .padding(2.0F, 0.0F, 0.0F, 0.0F)
            .gap(1.0F)
            .flexShrink(0.0F);
    String[] strings = currentMode.options();
    this.items = new ArrayList<>(strings.length);

    for (int index = 0; index < strings.length; index++) {
      String text = strings[index];
      float value = index == 0 ? 5.0F : 2.0F;
      float currentLength = index == strings.length - 1 ? 5.0F : 2.0F;
      SceneTextService sceneText =
          createSceneTextService(text, 7.0F, -1459617793)
              .overflow(SceneTextService.Overflow.ELLIPSIS)
              .flex(1.0F);
      SceneCornerRadiusService currentSize =
          new SceneCornerRadiusService()
              .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(20.0F))
              .flexShrink(0.0F)
              .cornerRadiusTL(value)
              .cornerRadiusTR(value)
              .cornerRadiusBR(currentLength)
              .cornerRadiusBL(currentLength)
              .backgroundColor(-1475078371)
              .hoverBackground(-802936525)
              .pressBackground(-534303689)
              .direction(ScenePctService.Direction.ROW)
              .align(ScenePctService.Align.CENTER)
              .justify(ScenePctService.Justify.START)
              .padding(0.0F, 7.0F)
              .cursorStyle(ScenePctService.CursorStyle.POINTER)
              .onClick(() -> this.updateState2(text));
      currentSize.key("option:" + index);
      currentSize.addChild(sceneText);
      this.sceneComponent4.addChild(currentSize);
      this.items.add(new ModeSettingControl.OptionView(text, currentSize, sceneText));
    }

    this.sceneContentService =
        new SceneContentService(this.sceneComponent4)
            .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(0.0F))
            .flexShrink(0.0F);
    this.addChild(this.sceneCornerRadiusService);
    this.addChild(this.sceneContentService);
    this.sceneCornerRadiusService.key("trigger");
    this.sceneContentService.key("options-reveal");
    this.sceneComponent4.key("options");
    this.id("mode." + createText2(currentMode.name()));
    this.refreshState();
    this.autoCloseable = currentMode.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Mode setting() {
    return this.moduleMode;
  }

  public String value() {
    return (String) this.moduleMode.get();
  }

  public ModeSettingControl value(String text) {
    this.trySelect(text);
    return this;
  }

  public boolean isExpanded() {
    return this.sceneContentService.isExpanded();
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.enabled5;
  }

  public boolean available() {
    return !this.enabled6 && this.enabled2 && this.enabled3 && this.enabled4 && this.enabled5;
  }

  public ModeSettingControl expand() {
    this.refreshState();
    if (!this.available()) {
      return this;
    }

    this.sceneContentService.expand();
    this.sceneTextService2.text("⌃");
    return this;
  }

  public ModeSettingControl collapse() {
    this.sceneContentService.collapse();
    this.sceneTextService2.text("⌄");
    return this;
  }

  public ModeSettingControl toggle() {
    return this.isExpanded() ? this.collapse() : this.expand();
  }

  public boolean trySelect(String text) {
    this.refreshState();
    if (this.available() && this.moduleMode.accepts(text)) {
      String currentText = (String) this.moduleMode.get();
      this.moduleMode.set(text);
      String nextText = (String) this.moduleMode.get();
      this.updateState4(nextText);
      this.collapse();
      if (!Objects.equals(currentText, nextText) && this.text2 != null) {
        this.text2.accept(nextText);
      }

      return true;
    } else {
      return false;
    }
  }

  public boolean trySelect(int index) {
    String[] strings = this.moduleMode.options();
    return index >= 0 && index < strings.length ? this.trySelect(strings[index]) : false;
  }

  public ModeSettingControl onChange(Consumer<String> consumer) {
    this.text2 = consumer;
    return this;
  }

  public ModeSettingControl onInteraction(Runnable currentRunnable) {
    this.runnable = currentRunnable;
    return this;
  }

  public ModeSettingControl visible(boolean currentVisible) {
    this.enabled2 = currentVisible;
    this.refreshState();
    return this;
  }

  public ModeSettingControl enabled(boolean currentEnabled) {
    this.enabled3 = currentEnabled;
    this.refreshState();
    return this;
  }

  public ModeSettingControl id(String currentId) {
    super.id(currentId);
    if (this.sceneCornerRadiusService == null) {
      return this;
    }

    this.sceneCornerRadiusService.id(createText(currentId, "trigger"));
    this.sceneTextService.id(createText(currentId, "value"));
    this.sceneTextService2.id(createText(currentId, "chevron"));
    this.sceneContentService.id(createText(currentId, "accordion"));
    this.sceneComponent4.id(createText(currentId, "options"));

    for (int index = 0; index < this.items.size(); index++) {
      ModeSettingControl.OptionView optionView = this.items.get(index);
      String nextId = "option." + index + "." + createText2(optionView.option);
      optionView.row.id(createText(currentId, nextId));
      optionView.label.id(createText(currentId, nextId + ".label"));
    }

    return this;
  }

  public void refreshState() {
    if (!this.enabled6) {
      this.enabled4 = this.moduleMode.isVisible();
      this.enabled5 = this.moduleMode.isActive();
      boolean enabled = this.available();
      if (!enabled && this.sceneContentService.isExpanded()) {
        this.sceneContentService.setExpanded(false, false);
        this.sceneTextService2.text("⌄");
      }

      super.visible(enabled);
      super.pointerEvents(enabled);
      this.sceneCornerRadiusService
          .interactive(enabled)
          .pointerEvents(enabled)
          .cursorStyle(
              enabled ? ScenePctService.CursorStyle.POINTER : ScenePctService.CursorStyle.DEFAULT);

      for (ModeSettingControl.OptionView optionView : this.items) {
        optionView
            .row
            .interactive(enabled)
            .pointerEvents(enabled)
            .cursorStyle(
                enabled
                    ? ScenePctService.CursorStyle.POINTER
                    : ScenePctService.CursorStyle.DEFAULT);
      }

      this.updateState4((String) this.moduleMode.get());
    }
  }

  @Override
  protected boolean handleScroll(float value) {
    return false;
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 112.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {}

  @Override
  protected void onDetached() {
    this.dispose();
  }

  public void dispose() {
    if (!this.enabled6) {
      this.enabled6 = true;
      this.sceneContentService.setExpanded(false, false);
      this.sceneTextService2.text("⌄");
      super.visible(false);
      super.pointerEvents(false);
      this.sceneCornerRadiusService.interactive(false).pointerEvents(false);

      for (ModeSettingControl.OptionView optionView : this.items) {
        optionView.row.interactive(false).pointerEvents(false);
      }

      this.text2 = null;
      this.runnable = null;

      try {
        this.autoCloseable.close();
      } catch (Exception exception) {
      }
    }
  }

  private void updateState() {
    if (this.available()) {
      this.updateState3();
      this.toggle();
    }
  }

  private void updateState2(String text) {
    if (this.available()) {
      this.updateState3();
      this.trySelect(text);
    }
  }

  private void updateState3() {
    if (this.runnable != null) {
      this.runnable.run();
    }
  }

  private void updateState4(String currentText) {
    this.sceneTextService.text(currentText);

    for (ModeSettingControl.OptionView optionView : this.items) {
      boolean enabled = optionView.option.equals(currentText);
      optionView.row.backgroundColor(enabled ? 964547580 : -1475078371);
      optionView.label.color(enabled ? -1 : -1459617793);
      optionView.row.invalidate();
    }
  }

  private static SceneTextService createSceneTextService(
      String text, float value, int currentValue) {
    return new SceneTextService(text, value, currentValue)
        .fontVariant(TextMode.REGULAR)
        .inheritEdgeSoftness(false);
  }

  private static String createText(String text, String currentText) {
    return text != null && !text.isBlank() ? text + "." + currentText : null;
  }

  private static String createText2(String text) {
    String currentText =
        text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    return currentText.isEmpty() ? "setting" : currentText;
  }

  private record OptionView(String option, SceneCornerRadiusService row, SceneTextService label) {}
}
