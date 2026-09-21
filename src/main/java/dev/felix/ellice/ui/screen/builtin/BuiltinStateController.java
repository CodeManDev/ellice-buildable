package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.changelog.ChangelogRelease;
import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;

public final class BuiltinStateController {
  private final ChangelogRelease changelogData2;
  private final Runnable runnable;
  private final LayoutContainerNode f46zyc9c8juc;
  private final ComponentMountService f6hkb7w2ks7;
  private float f3v01610wluj;
  private float value2;
  private boolean enabled;
  private boolean enabled2;
  private long f48q7ukoz5m;

  public BuiltinStateController(
      final ChangelogRepository changelogRepository,
      final Runnable runnable,
      final ThemeIsSetService themeIsSetService) {
    this.f46zyc9c8juc = new LayoutContainerNode();
    this.f6hkb7w2ks7 = new ComponentMountService();
    this.f3v01610wluj = Float.intBitsToFloat(1150025728);
    this.value2 = Float.intBitsToFloat(1144258560);
    this.changelogData2 = changelogRepository.latest();
    this.runnable = runnable;
    this.f46zyc9c8juc
        .id("menu.news.layer")
        .layerBreak(true)
        .absolute()
        .inset(LayoutOperationHandler.px(0.0f))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
        .interactive(false);
    this.f6hkb7w2ks7
        .absolute()
        .inset(LayoutOperationHandler.px(0.0f))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
        .interactive(false);
    this.f6hkb7w2ks7.mount(this::createComponentKeyService, themeIsSetService);
    this.f46zyc9c8juc.addChild(this.f6hkb7w2ks7);
  }

  public ScenePctService<?> node() {
    return this.f46zyc9c8juc;
  }

  public boolean isOpen() {
    return this.enabled;
  }

  public void viewport(final float f3v01610wluj, final float value2) {
    if (this.f3v01610wluj == f3v01610wluj && this.value2 == value2) {
      return;
    }
    this.f3v01610wluj = f3v01610wluj;
    this.value2 = value2;
    this.f6hkb7w2ks7.invalidateComponent();
    final ScenePctService<?> byId = this.f46zyc9c8juc.findById("menu.news.panel");
    MotionAnimateService.cancel(byId, MotionColorsContainer.Floats.TRANSLATE_X);
    byId.translateX(
        this.enabled ? 0.0f : (this.calculateValue() + Float.intBitsToFloat(1107296256)));
  }

  public void tick() {
    if (!this.enabled2 && this.f48q7ukoz5m != 0L && System.nanoTime() >= this.f48q7ukoz5m) {
      this.close();
    }
  }

  public void close() {
    this.enabled2 = false;
    this.f48q7ukoz5m = 0L;
    this.updateState2(false);
  }

  private float calculateValue() {
    return Math.max(
        Float.intBitsToFloat(1127481344),
        Math.min(
            Float.intBitsToFloat(1136132096),
            this.f3v01610wluj - Float.intBitsToFloat(1103101952)));
  }

  private float calculateValue2() {
    return (this.value2 < Float.intBitsToFloat(1137836032))
        ? Float.intBitsToFloat(1120927744)
        : Float.intBitsToFloat(1125122048);
  }

  private void updateState(final boolean b) {
    if (b) {
      this.f48q7ukoz5m = 0L;
      this.updateState2(true);
    } else if (!this.enabled2) {
      this.f48q7ukoz5m = System.nanoTime() + 220000000L;
    }
  }

  private void updateState2(final boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }
    this.enabled = enabled;
    final ScenePctService<?> byId = this.f46zyc9c8juc.findById("menu.news.panel");
    byId.pointerEvents(enabled);
    MotionAnimateService.animate(
        byId,
        MotionColorsContainer.Floats.TRANSLATE_X,
        enabled ? 0.0f : (this.calculateValue() + Float.intBitsToFloat(1107296256)),
        enabled
            ? new SceneEaseHandler.Spring(
                Float.intBitsToFloat(1100480512), Float.intBitsToFloat(1129447424))
            : new SceneEaseHandler.Spring(
                Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1131413504)));
    MotionAnimateService.animate(
        byId,
        MotionColorsContainer.Floats.OPACITY,
        enabled ? 1.0f : 0.0f,
        SceneEaseHandler.Tween.ease(
            enabled ? Float.intBitsToFloat(1043878380) : Float.intBitsToFloat(1042536202)));
  }

  private ComponentKeyService<?> createComponentKeyService(final ComponentThemeService componentThemeService) {
    final String s = (this.changelogData2 == null) ? "" : this.changelogData2.version();
    final ArrayList list = new ArrayList();
    if (this.changelogData2 != null) {
      for (int i = 0; i < Math.min(3, this.changelogData2.highlights().size()); ++i) {
        list.add(
            MaterialTextService.text(
                    this.changelogData2.highlights().get(i),
                    Float.intBitsToFloat(1096810496),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        sceneTextService
                            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .wordWrap((boolean) (1 != 0))
                            .flexShrink(0.0f))
                .key("highlight-" + i));
      }
    }
    return ComponentBoxService.node(
        "news-layer",
        LayoutContainerNode::new,
        layoutContainerNode ->
            layoutContainerNode
                .absolute()
                .inset(LayoutOperationHandler.px(0.0f))
                .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
                .interactive((boolean) (0 != 0)),
        ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    sceneCornerRadiusService
                        .id("menu.news.panel")
                        .absolute()
                        .right(LayoutOperationHandler.px(Float.intBitsToFloat(1094713344)))
                        .top(LayoutOperationHandler.px(this.calculateValue2()))
                        .size(
                            LayoutOperationHandler.px(this.calculateValue()),
                            LayoutOperationHandler.auto())
                        .maxHeight(
                            Math.max(
                                Float.intBitsToFloat(1120403456),
                                this.value2
                                    - this.calculateValue2()
                                    - Float.intBitsToFloat(1098907648)))
                        .direction(ScenePctService.Direction.COLUMN)
                        .cornerRadius(Float.intBitsToFloat(1103101952))
                        .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                        .border(1.0f, MaterialIsLightService.OUTLINE_VARIANT)
                        .clip((boolean) (1 != 0))
                        .onHoverChange(this::updateState)
                        .pointerEvents(this.enabled)
                        .stopPropagation((boolean) (1 != 0)),
                ComponentBoxService.row(
                    layoutContainerNode2 ->
                        layoutContainerNode2
                            .padding(
                                Float.intBitsToFloat(1090519040),
                                Float.intBitsToFloat(1098907648),
                                0.0f,
                                Float.intBitsToFloat(1101004800))
                            .gap(Float.intBitsToFloat(1090519040))
                            .flexShrink(0.0f)
                            .align(ScenePctService.Align.CENTER),
                    MaterialTextService.text(
                            "LATEST UPDATE",
                            Float.intBitsToFloat(1093664768),
                            MaterialIsLightService.PRIMARY)
                        .props(sceneTextService2 -> sceneTextService2.flex(1.0f).minWidth(0.0f)),
                    MaterialTextService.text(
                        s,
                        Float.intBitsToFloat(1094713344),
                        MaterialIsLightService.ON_SURFACE_VARIANT),
                    MaterialTextService.iconButton(
                            "menu.news.close", "close", "Close update", this::close)
                        .props(
                            materialJoinedService ->
                                materialJoinedService
                                    .size(
                                        Float.intBitsToFloat(1108344832),
                                        Float.intBitsToFloat(1108344832))
                                    .onHoverChange(this::updateState))),
                ComponentBoxService.column(
                    layoutContainerNode3 ->
                        layoutContainerNode3
                            .id("menu.news.content")
                            .size(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                                LayoutOperationHandler.auto())
                            .flexShrink(1.0f)
                            .minHeight(0.0f)
                            .padding(
                                Float.intBitsToFloat(1090519040),
                                Float.intBitsToFloat(1101004800),
                                Float.intBitsToFloat(1098907648),
                                Float.intBitsToFloat(1101004800))
                            .gap(Float.intBitsToFloat(1094713344))
                            .scrollable((boolean) (1 != 0))
                            .clip((boolean) (1 != 0))
                            .scrollbarAutoHide((boolean) (1 != 0))
                            .onHoverChange(this::updateState),
                    MaterialTextService.text(
                            (this.changelogData2 == null)
                                ? "You're up to date"
                                : this.changelogData2.title(),
                            Float.intBitsToFloat(1102053376),
                            MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService3 ->
                                sceneTextService3
                                    .wordWrap((boolean) (1 != 0))
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)))
                                    .flexShrink(0.0f)),
                    ComponentBoxService.column(
                        layoutContainerNode4 ->
                            layoutContainerNode4
                                .id("menu.news.highlights")
                                .size(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456)),
                                    LayoutOperationHandler.auto())
                                .gap(Float.intBitsToFloat(1094713344))
                                .flexShrink(0.0f),
                        (ComponentKeyService<?>[]) list.toArray(ComponentKeyService[]::new))),
                ComponentBoxService.row(
                    layoutContainerNode5 ->
                        layoutContainerNode5
                            .padding(
                                0.0f,
                                Float.intBitsToFloat(1101004800),
                                Float.intBitsToFloat(1098907648),
                                Float.intBitsToFloat(1101004800))
                            .gap(Float.intBitsToFloat(1090519040))
                            .align(ScenePctService.Align.CENTER)
                            .flexShrink(0.0f),
                    MaterialTextService.text(
                            (this.changelogData2 == null)
                                ? ""
                                : ChangelogEntryFormatter.date(this.changelogData2.date()),
                            Float.intBitsToFloat(1094713344),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService4 -> sceneTextService4.flex(1.0f).minWidth(0.0f)),
                    MaterialTextService.button(
                            "menu.news.history",
                            "View update",
                            () -> {
                              this.close();
                              this.runnable.run();
                            },
                            true)
                        .props(
                            materialJoinedService2 ->
                                materialJoinedService2
                                    .height(
                                        LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                    .onHoverChange(this::updateState))))
            .key("panel")
            .onMount(
                sceneCornerRadiusService2 ->
                    sceneCornerRadiusService2
                        .translateX(this.calculateValue() + Float.intBitsToFloat(1107296256))
                        .opacity(0.0f)),
        MaterialTextService.button(
                "menu.news.tab",
                "What's new",
                () -> {
                  this.enabled2 = (1 != 0);
                  this.f48q7ukoz5m = 0L;
                  this.updateState2((boolean) (1 != 0));
                },
                false)
            .props(
                materialJoinedService3 ->
                    materialJoinedService3
                        .absolute()
                        .right(LayoutOperationHandler.px(Float.intBitsToFloat(1094713344)))
                        .top(
                            LayoutOperationHandler.px(
                                Math.max(
                                    Float.intBitsToFloat(1090519040),
                                    this.calculateValue2() - Float.intBitsToFloat(1111490560))))
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                        .onHoverChange(this::updateState)
                        .tooltip("Hover to peek · click to keep open")));
  }
}
