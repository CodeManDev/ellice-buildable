package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.text.TextTextureService;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SceneRenderer {
  static final float MARGIN = 16.0F;
  private static final float value = 344.0F;
  private static final float value2 = 16.0F;
  private static final float value3 = 14.0F;
  private static final float value4 = 32.0F;
  private static final float value5 = 12.0F;
  private final List<SceneRenderer.Toast> items = new ArrayList<>();
  private final ComponentMountService componentMountService = new ComponentMountService();
  private float value6;
  private float value7;
  private long timestamp;
  private boolean enabled = true;
  private boolean enabled2;
  private ThemeCornerData.Corner corner = ThemeCornerData.current().notificationCorner();
  private int[] int2;

  public SceneRenderer() {
    this.componentMountService
        .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
        .pointerEvents(false)
        .clip(true);
    this.componentMountService.mount(
        item -> this.createComponentKeyService(), new ThemeIsSetService());
  }

  public void show(String text, String currentText, float value, SceneRenderer.Type type) {
    ThemeCornerData themeCornerData = ThemeCornerData.current();
    if (themeCornerData.acceptsNotification(
        type == SceneRenderer.Type.WARNING || type == SceneRenderer.Type.ERROR)) {
      float currentValue = Float.isFinite(value) ? Math.clamp(value, 0.5F, 30.0F) : 3.0F;
      this.items.add(
          new SceneRenderer.Toast(++this.timestamp, text, currentText, currentValue, type));
      this.enabled = true;
      this.updateState2(themeCornerData);
    }
  }

  public void show(String text, String currentText, float value) {
    this.show(text, currentText, value, SceneRenderer.Type.INFO);
  }

  public void show(String text, float value) {
    this.show(text, null, value, SceneRenderer.Type.INFO);
  }

  public void show(String text) {
    this.show(text, null, 3.0F, SceneRenderer.Type.INFO);
  }

  public void tick(float value, float currentValue, float nextValue) {
    if (!Float.isFinite(value) || value < 0.0F) {
      value = 0.0F;
    }

    if (this.value6 != currentValue || this.value7 != nextValue) {
      this.value6 = currentValue;
      this.value7 = nextValue;
      this.enabled = true;
    }

    this.updateState();
    this.componentMountService.tickAnimations(value);

    for (SceneRenderer.Toast toast : this.items) {
      toast.age += value;
      if (!toast.exiting
          && toast.age
              > toast.duration * ThemeCornerData.current().notificationDuration().scale()) {
        toast.exiting = true;
        MotionAnimateService.animate(
            toast.card,
            MotionColorsContainer.Floats.TRANSLATE_X,
            this.corner.left() ? -40.0F : 40.0F,
            SceneEaseHandler.Tween.exit(0.2F));
        MotionAnimateService.animate(
            toast.card,
            MotionColorsContainer.Floats.OPACITY,
            0.0F,
            SceneEaseHandler.Tween.ease(0.2F),
            0.0F,
            () -> toast.dead = true);
      }
    }

    if (this.items.removeIf(item -> item.dead)) {
      this.enabled = true;
      this.updateState();
    }
  }

  public void render(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    this.updateState();
    if (!this.items.isEmpty()) {
      compositorPushPresentationScale.requestFontFamily(
          "material-roboto",
          TextTextureService.FontSource.CLASSPATH,
          "/assets/ellice/fonts/material/Roboto-Regular.ttf");
      compositorPushPresentationScale.requestFontFamily(
          "material-roboto-medium",
          TextTextureService.FontSource.CLASSPATH,
          "/assets/ellice/fonts/material/Roboto-Medium.ttf");
      this.componentMountService.performLayout(
          compositorPushPresentationScale, 0.0F, 0.0F, this.value6, this.value7);
      this.componentMountService.drawTree(compositorPushPresentationScale, 1.0F);
    }
  }

  private void updateState() {
    this.updateState2(ThemeCornerData.current());
    ThemeCornerData.Corner currentCorner = ThemeCornerData.current().notificationCorner();
    int[] ints =
        new int[] {
          MaterialIsLightService.SURFACE_HIGH,
          MaterialIsLightService.ON_SURFACE,
          MaterialIsLightService.ON_SURFACE_VARIANT,
          MaterialIsLightService.OUTLINE_VARIANT,
          MaterialIsLightService.PRIMARY,
          MaterialIsLightService.SECONDARY,
          MaterialIsLightService.TERTIARY,
          MaterialIsLightService.ERROR
        };
    if (currentCorner != this.corner || !Arrays.equals(this.int2, ints)) {
      this.corner = currentCorner;
      this.int2 = ints;
      this.enabled = true;
    }

    if (this.enabled) {
      this.enabled = false;
      if (!this.items.isEmpty() || this.enabled2) {
        this.componentMountService.invalidateComponent();
        this.enabled2 = !this.items.isEmpty();
      }
    }
  }

  private ComponentKeyService<?> createComponentKeyService() {
    ArrayList<SceneRenderer.Toast> arrayList = new ArrayList<>(this.items);
    if (this.corner.top()) {
      Collections.reverse(arrayList);
    }

    ComponentKeyService[] componentKeies =
        arrayList.stream()
            .map(this::createComponentKeyService2)
            .toArray(ComponentKeyService[]::new);
    return ComponentBoxService.column(
        item ->
            item.id("toast.stack")
                .absolute()
                .size(
                    LayoutOperationHandler.px(
                        Math.max(1.0F, Math.min(344.0F, this.value6 - 32.0F))),
                    LayoutOperationHandler.auto())
                .inset(
                    this.corner.top()
                        ? LayoutOperationHandler.px(16.0F)
                        : LayoutOperationHandler.auto(),
                    this.corner.left()
                        ? LayoutOperationHandler.auto()
                        : LayoutOperationHandler.px(16.0F),
                    this.corner.top()
                        ? LayoutOperationHandler.auto()
                        : LayoutOperationHandler.px(16.0F),
                    this.corner.left()
                        ? LayoutOperationHandler.px(16.0F)
                        : LayoutOperationHandler.auto())
                .gap(10.0F)
                .pointerEvents(false),
        componentKeies);
  }

  private ComponentKeyService<?> createComponentKeyService2(SceneRenderer.Toast currentToast) {
    int value = currentToast.type.ink();
    return ComponentBoxService.<SceneRenderer.ToastCard>node(
            "material-toast",
            SceneRenderer.ToastCard::new,
            item ->
                item.id("toast." + currentToast.id)
                    .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.auto())
                    .minHeight(64.0F)
                    .flexShrink(0.0F)
                    .padding(14.0F, 16.0F)
                    .gap(12.0F)
                    .direction(ScenePctService.Direction.ROW)
                    .align(ScenePctService.Align.CENTER)
                    .cornerRadius(20.0F)
                    .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                    .border(0.5F, MaterialIsLightService.OUTLINE_VARIANT)
                    .shadow(8.0F)
                    .shadowColor(603979776)
                    .pointerEvents(false),
            ComponentBoxService.panel(
                item ->
                    item.size(32.0F, 32.0F)
                        .flexShrink(0.0F)
                        .cornerRadius(12.0F)
                        .backgroundColor(
                            MaterialIsLightService.layer(
                                MaterialIsLightService.SURFACE_HIGH, value, 0.12F))
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .justify(ScenePctService.Justify.CENTER)
                        .pointerEvents(false),
                MaterialTextService.icon(currentToast.type.symbol, value)
                    .props(item -> item.size(20.0F, 20.0F))),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F).pointerEvents(false),
                MaterialTextService.label(currentToast.title, MaterialIsLightService.ON_SURFACE)
                    .props(
                        item ->
                            item.id("toast." + currentToast.id + ".title")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .wordWrap(true)
                                .maxLines(2)
                                .flexShrink(0.0F)),
                MaterialTextService.text(
                        currentToast.subtitle, 13.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        item ->
                            item.id("toast." + currentToast.id + ".body")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .wordWrap(true)
                                .maxLines(3)
                                .flexShrink(0.0F)
                                .visible(!currentToast.subtitle.isBlank()))))
        .key("toast-" + currentToast.id)
        .onMount(
            item -> {
              currentToast.card = item;
              item.opacity(0.0F).translateX(this.corner.left() ? -32.0F : 32.0F);
              MotionAnimateService.animate(
                  item,
                  MotionColorsContainer.Floats.OPACITY,
                  1.0F,
                  SceneEaseHandler.Tween.ease(0.18F));
              MotionAnimateService.animate(
                  item,
                  MotionColorsContainer.Floats.TRANSLATE_X,
                  0.0F,
                  MaterialIsLightService.SPATIAL);
            });
  }

  private void updateState2(ThemeCornerData themeCornerData) {
    if (this.items.removeIf(
        item ->
            !themeCornerData.acceptsNotification(
                item.type == SceneRenderer.Type.WARNING
                    || item.type == SceneRenderer.Type.ERROR))) {
      this.enabled = true;
    }

    while (this.items.size() > themeCornerData.notificationLimit()) {
      this.items.removeFirst();
      this.enabled = true;
    }
  }

  public boolean hasToasts() {
    return !this.items.isEmpty();
  }

  public static final class Toast {
    final long id;
    final String title;
    final String subtitle;
    final float duration;
    final SceneRenderer.Type type;
    float age;
    boolean exiting;
    boolean dead;
    SceneCornerRadiusService card;

    Toast(
        long longValue,
        String text,
        String currentText,
        float value,
        SceneRenderer.Type currentType) {
      this.id = longValue;
      this.title = text == null ? "" : text;
      this.subtitle = currentText == null ? "" : currentText;
      this.duration = value;
      this.type = currentType == null ? SceneRenderer.Type.INFO : currentType;
    }
  }

  private static final class ToastCard extends SceneCornerRadiusService {
    @Override
    public float intrinsicHeight(
        CompositorPushPresentationScaleService compositorPushPresentationScale, float value) {
      if (this.children().size() == 2 && Float.isFinite(value)) {
        float currentValue = Math.max(1.0F, value - 32.0F - 32.0F - 12.0F);
        return 28.0F
            + Math.max(
                32.0F,
                this.children()
                    .get(1)
                    .intrinsicHeight(compositorPushPresentationScale, currentValue));
      } else {
        return super.intrinsicHeight(compositorPushPresentationScale, value);
      }
    }
  }

  public enum Type {
    INFO(-10262799, "info"),
    SUCCESS(-14498466, "check"),
    WARNING(-680437, "warning"),
    ERROR(-1096636, "error");

    public final int accent;
    final String symbol;

    Type(int value, String text) {
      this.accent = value;
      this.symbol = text;
    }

    public static SceneRenderer.Type from(String text) {
      if (text == null) {
        return INFO;
      }

      return switch (text.toLowerCase(Locale.ROOT)) {
        case "success" -> SUCCESS;
        case "warning", "warn" -> WARNING;
        case "error", "danger" -> ERROR;
        default -> INFO;
      };
    }

    int ink() {
      return switch (this) {
        case INFO -> MaterialIsLightService.SECONDARY;
        case SUCCESS -> MaterialIsLightService.PRIMARY;
        case WARNING -> MaterialIsLightService.TERTIARY;
        case ERROR -> MaterialIsLightService.ERROR;
      };
    }

    private static SceneRenderer.Type[] $values() {
      return new SceneRenderer.Type[] {INFO, SUCCESS, WARNING, ERROR};
    }
  }
}
