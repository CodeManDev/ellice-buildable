package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class CurveSettingControl extends LayoutContainerNode {
  public static final float DEFAULT_WIDTH = 220.0f;
  public static final float TYPE_ROW_HEIGHT = 22.0f;
  public static final float STEP_ROW_HEIGHT = 21.0f;
  public static final float MIN_GRAPH_HEIGHT = 82.0f;
  private static final float fj4ad6tx47bn = 6.0f;
  private static final float fich5wc5xap2 = 4.0f;
  private static final int count2 = -1206445535;
  private static final int count3 = -769382093;
  private static final int count4 = -534040517;
  private static final int count5 = 1232983036;
  private static final int count6 = 1568527356;
  private static final int count7 = 637534207;
  private static final int count8 = -1073741825;
  private static final int count9 = -1;
  private final ModuleSetting.Curve moduleCurve;
  private final CurveGraphControl controlGetAnimPropertyService2;
  private final LayoutContainerNode fdytxxoqtgjr;
  private final LayoutContainerNode sceneComponent42;
  private final LayoutContainerNode sceneComponent43;
  private final LayoutContainerNode fgjetkv3noaf;
  private final SceneTextService sceneTextService;
  private final List<TypeView> items;
  private final List<StepModeView> fdwfmom4ayrf;
  private final AutoCloseable autoCloseable;
  private ModuleSetting.CurveValue febzk2ojfmcv;
  private ModuleSetting.CurveValue moduleCurveValue2;
  private ModuleSetting.CurveValue moduleCurveValue3;
  private Consumer<ModuleSetting.CurveValue> moduleConsumer;
  private Runnable runnable;
  private boolean currentVisible;
  private boolean currentEnabled;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;

  public CurveSettingControl(final ModuleSetting.Curve obj) {
    this.items = new ArrayList<TypeView>();
    this.fdwfmom4ayrf = new ArrayList<StepModeView>();
    this.moduleCurveValue2 =
        ModuleSetting.CurveValue.cubicBezier(
            Float.intBitsToFloat(1048576000),
            Float.intBitsToFloat(1036831949),
            Float.intBitsToFloat(1048576000),
            1.0f);
    this.moduleCurveValue3 = ModuleSetting.CurveValue.steps(4, ModuleSetting.StepMode.JUMP_END);
    this.currentVisible = true;
    this.currentEnabled = true;
    this.moduleCurve = Objects.requireNonNull(obj, "setting");
    this.updateState7(this.febzk2ojfmcv = Objects.requireNonNull(obj.get(), "curve value"));
    this.direction(Direction.COLUMN);
    this.align(Align.STRETCH);
    this.justify(Justify.START);
    this.gap(Float.intBitsToFloat(1086324736));
    this.size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.minWidth(0.0f);
    this.maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.flexShrink(0.0f);
    this.fdytxxoqtgjr =
        new LayoutContainerNode()
            .direction(Direction.ROW)
            .align(Align.STRETCH)
            .justify(Justify.START)
            .gap(Float.intBitsToFloat(1082130432))
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.px(Float.intBitsToFloat(1102053376)))
            .minWidth(0.0f)
            .maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .flexShrink(0.0f);
    this.updateState(ModuleSetting.CurveType.LINEAR, "Linear");
    this.updateState(ModuleSetting.CurveType.CUBIC_BEZIER, "Bezier");
    this.updateState(ModuleSetting.CurveType.STEPS, "Steps");
    this.controlGetAnimPropertyService2 =
        new CurveGraphControl(this.febzk2ojfmcv)
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.auto())
            .minWidth(0.0f)
            .maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .minHeight(Float.intBitsToFloat(1118044160))
            .maxHeight(Float.intBitsToFloat(1124728832))
            .flex(1.0f)
            .onInteraction(this::updateState10)
            .onChange(this::updateState3)
            .onCommit(this::updateState4);
    this.sceneComponent42 =
        new LayoutContainerNode()
            .direction(Direction.COLUMN)
            .align(Align.STRETCH)
            .justify(Justify.START)
            .gap(Float.intBitsToFloat(1082130432))
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.auto())
            .minWidth(0.0f)
            .maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .flexShrink(0.0f);
    this.sceneComponent43 =
        new LayoutContainerNode()
            .direction(Direction.ROW)
            .align(Align.STRETCH)
            .justify(Justify.START)
            .gap(Float.intBitsToFloat(1082130432))
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.px(Float.intBitsToFloat(1101529088)))
            .minWidth(0.0f)
            .maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .flexShrink(0.0f);
    final SceneCornerRadiusService sceneCornerRadius =
        this.sceneCornerRadius("−", () -> this.trySetSteps(this.febzk2ojfmcv.steps() - 1));
    sceneCornerRadius
        .size(
            LayoutOperationHandler.px(Float.intBitsToFloat(1106771968)),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
        .flexShrink(0.0f);
    this.sceneTextService =
        m46qdudgy2qq("4 steps", Float.intBitsToFloat(1088421888), -1073741825)
            .flex(1.0f)
            .textAlign(SceneTextService.TextAlign.CENTER);
    final SceneCornerRadiusService currentSceneCornerRadius =
        currentSceneCornerRadius(this.sceneTextService);
    currentSceneCornerRadius
        .flex(1.0f)
        .size(
            LayoutOperationHandler.auto(),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    final SceneCornerRadiusService nextSceneCornerRadius =
        this.sceneCornerRadius("+", () -> this.trySetSteps(this.febzk2ojfmcv.steps() + 1));
    nextSceneCornerRadius
        .size(
            LayoutOperationHandler.px(Float.intBitsToFloat(1106771968)),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
        .flexShrink(0.0f);
    this.sceneComponent43
        .addChild(sceneCornerRadius)
        .addChild(currentSceneCornerRadius)
        .addChild(nextSceneCornerRadius);
    this.fgjetkv3noaf =
        new LayoutContainerNode()
            .direction(Direction.ROW)
            .align(Align.STRETCH)
            .justify(Justify.START)
            .gap(Float.intBitsToFloat(1082130432))
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.px(Float.intBitsToFloat(1101529088)))
            .minWidth(0.0f)
            .maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .flexShrink(0.0f);
    this.updateState2(ModuleSetting.StepMode.JUMP_START, "Start");
    this.updateState2(ModuleSetting.StepMode.JUMP_END, "End");
    this.updateState2(ModuleSetting.StepMode.JUMP_NONE, "None");
    this.updateState2(ModuleSetting.StepMode.JUMP_BOTH, "Both");
    this.sceneComponent42.addChild(this.sceneComponent43).addChild(this.fgjetkv3noaf);
    this.addChild(this.fdytxxoqtgjr)
        .addChild(this.controlGetAnimPropertyService2)
        .addChild(this.sceneComponent42);
    this.id("curve." + mj20frzk4h7(obj.name()));
    this.refreshState();
    this.autoCloseable = obj.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Curve setting() {
    return this.moduleCurve;
  }

  public CurveGraphControl graph() {
    return this.controlGetAnimPropertyService2;
  }

  public ModuleSetting.CurveValue preview() {
    return this.febzk2ojfmcv;
  }

  public ModuleSetting.CurveType type() {
    return this.febzk2ojfmcv.type();
  }

  public String description() {
    return switch (this.febzk2ojfmcv.type()) {
      default -> throw new MatchException(null, null);
      case LINEAR -> "Linear";
      case CUBIC_BEZIER ->
          String.format(
              Locale.ROOT,
              "Bezier · %.2f, %.2f / %.2f, %.2f",
              this.febzk2ojfmcv.x1(),
              this.febzk2ojfmcv.y1(),
              this.febzk2ojfmcv.x2(),
              this.febzk2ojfmcv.y2());
      case STEPS ->
          "Steps · " + this.febzk2ojfmcv.steps() + " · " + createText(this.febzk2ojfmcv.stepMode());
    };
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.enabled5;
  }

  public boolean available() {
    return !this.enabled6
        && this.currentVisible
        && this.currentEnabled
        && this.enabled4
        && this.enabled5;
  }

  public boolean isPreviewing() {
    return this.controlGetAnimPropertyService2.isDragging();
  }

  @Override
  public CurveSettingControl visible(final boolean currentVisible) {
    this.currentVisible = currentVisible;
    this.refreshState();
    return this;
  }

  public CurveSettingControl enabled(final boolean currentEnabled) {
    this.currentEnabled = currentEnabled;
    this.refreshState();
    return this;
  }

  public CurveSettingControl onChange(final Consumer<ModuleSetting.CurveValue> moduleConsumer) {
    this.moduleConsumer = moduleConsumer;
    return this;
  }

  public CurveSettingControl onInteraction(final Runnable runnable) {
    this.runnable = runnable;
    return this;
  }

  @Override
  public CurveSettingControl id(final String s) {
    super.id(s);
    this.m2xil2vbnsi();
    return this;
  }

  public boolean trySetType(final ModuleSetting.CurveType obj) {
    Objects.requireNonNull(obj, "type");
    this.refreshState();
    if (!this.available() || this.febzk2ojfmcv.type() == obj) {
      return false;
    }
    this.updateState7(this.febzk2ojfmcv);
    return this.checkCondition(
        switch (obj) {
          default -> throw new MatchException(null, null);
          case LINEAR -> ModuleSetting.CurveValue.linear();
          case CUBIC_BEZIER -> this.moduleCurveValue2;
          case STEPS -> this.moduleCurveValue3;
        });
  }

  public boolean trySetSteps(final int b) {
    this.refreshState();
    if (!this.available() || this.febzk2ojfmcv.type() != ModuleSetting.CurveType.STEPS) {
      return false;
    }
    final int max =
        Math.max(
            (this.febzk2ojfmcv.stepMode() == ModuleSetting.StepMode.JUMP_NONE) ? 2 : 1,
            Math.min(100, b));
    return max != this.febzk2ojfmcv.steps()
        && this.checkCondition(ModuleSetting.CurveValue.steps(max, this.febzk2ojfmcv.stepMode()));
  }

  public boolean trySetStepMode(final ModuleSetting.StepMode obj) {
    Objects.requireNonNull(obj, "mode");
    this.refreshState();
    return this.available()
        && this.febzk2ojfmcv.type() == ModuleSetting.CurveType.STEPS
        && this.febzk2ojfmcv.stepMode() != obj
        && this.checkCondition(
            ModuleSetting.CurveValue.steps(
                (obj == ModuleSetting.StepMode.JUMP_NONE)
                    ? Math.max(2, this.febzk2ojfmcv.steps())
                    : this.febzk2ojfmcv.steps(),
                obj));
  }

  public CurveSettingControl cancelPreview() {
    if (this.enabled6) {
      return this;
    }
    this.controlGetAnimPropertyService2.cancelGesture();
    this.m622tc4wfeth(this.moduleCurve.get());
    return this;
  }

  public void refreshState() {
    if (this.enabled6) {
      return;
    }
    this.enabled4 = this.moduleCurve.isVisible();
    this.enabled5 = this.moduleCurve.isActive();
    final boolean available = this.available();
    if (this.visible != available) {
      this.visible = available;
      this.invalidateLayout();
    }
    if (this.pointerEvents != available) {
      this.pointerEvents = available;
      this.invalidate();
    }
    this.controlGetAnimPropertyService2.enabled(available);
    if (!available) {
      this.controlGetAnimPropertyService2.cancelGesture();
    }
    this.m622tc4wfeth(this.moduleCurve.get());
  }

  @Override
  protected boolean handleScroll(final float n) {
    return false;
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1130102784);
  }

  @Override
  protected void onDetached() {
    this.dispose();
  }

  public void dispose() {
    if (this.enabled6) {
      return;
    }
    this.enabled6 = true;
    this.visible = false;
    this.pointerEvents = false;
    this.controlGetAnimPropertyService2.dispose();
    this.moduleConsumer = null;
    this.runnable = null;
    try {
      this.autoCloseable.close();
    } catch (final Exception ex) {
    }
  }

  private void updateState(final ModuleSetting.CurveType curveType, final String s) {
    final SceneTextService m46qdudgy2qq =
        m46qdudgy2qq(s, Float.intBitsToFloat(1088421888), -1073741825);
    m46qdudgy2qq.overflow(SceneTextService.Overflow.ELLIPSIS).minWidth(0.0f);
    final SceneCornerRadiusService sceneCornerRadius =
        this.sceneCornerRadius(m46qdudgy2qq, () -> this.trySetType(curveType));
    sceneCornerRadius
        .flex(1.0f)
        .flexShrink(1.0f)
        .minWidth(0.0f)
        .size(
            LayoutOperationHandler.auto(),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.fdytxxoqtgjr.addChild(sceneCornerRadius);
    this.items.add(new TypeView(curveType, sceneCornerRadius, m46qdudgy2qq));
  }

  private void updateState2(final ModuleSetting.StepMode stepMode, final String s) {
    final SceneTextService m46qdudgy2qq =
        m46qdudgy2qq(s, Float.intBitsToFloat(1087373312), -1073741825);
    m46qdudgy2qq.overflow(SceneTextService.Overflow.ELLIPSIS).minWidth(0.0f);
    final SceneCornerRadiusService sceneCornerRadius =
        this.sceneCornerRadius(m46qdudgy2qq, () -> this.trySetStepMode(stepMode));
    sceneCornerRadius
        .flex(1.0f)
        .flexShrink(1.0f)
        .minWidth(0.0f)
        .size(
            LayoutOperationHandler.auto(),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.fgjetkv3noaf.addChild(sceneCornerRadius);
    this.fdwfmom4ayrf.add(new StepModeView(stepMode, sceneCornerRadius, m46qdudgy2qq));
  }

  private SceneCornerRadiusService sceneCornerRadius(final String s, final Runnable runnable) {
    return this.sceneCornerRadius(
        m46qdudgy2qq(s, Float.intBitsToFloat(1090519040), -1073741825), runnable);
  }

  private SceneCornerRadiusService sceneCornerRadius(
      final SceneTextService sceneTextService, final Runnable runnable) {
    final SceneCornerRadiusService sceneCornerRadiusService =
        new SceneCornerRadiusService() {
          @Override
          protected void onPress(final float n, final float n2) {
            CurveSettingControl.this.updateState10();
          }
        };
    sceneCornerRadiusService
        .cornerRadius(Float.intBitsToFloat(1084227584))
        .backgroundColor(-1206445535)
        .hoverBackground(-769382093)
        .pressBackground(-534040517)
        .gradientEnd(0)
        .glass(false)
        .blur(0.0f)
        .border(Float.intBitsToFloat(1057803469), 637534207)
        .direction(Direction.ROW)
        .align(Align.CENTER)
        .justify(Justify.CENTER)
        .cursorStyle(CursorStyle.POINTER)
        .onClick(runnable);
    sceneTextService.pointerEvents(false);
    sceneCornerRadiusService.addChild(sceneTextService);
    return sceneCornerRadiusService;
  }

  private static SceneCornerRadiusService currentSceneCornerRadius(
      final SceneTextService sceneTextService) {
    final SceneCornerRadiusService sceneCornerRadiusService =
        new SceneCornerRadiusService()
            .cornerRadius(Float.intBitsToFloat(1084227584))
            .backgroundColor(-1206445535)
            .gradientEnd(0)
            .glass(false)
            .blur(0.0f)
            .border(Float.intBitsToFloat(1057803469), 637534207)
            .direction(Direction.ROW)
            .align(Align.CENTER)
            .justify(Justify.CENTER)
            .pointerEvents(false);
    sceneCornerRadiusService.addChild(sceneTextService);
    return sceneCornerRadiusService;
  }

  private void updateState3(final ModuleSetting.CurveValue febzk2ojfmcv) {
    if (this.enabled6 || !this.available()) {
      return;
    }
    this.updateState7(this.febzk2ojfmcv = febzk2ojfmcv);
    this.updateState8();
  }

  private void updateState4(final ModuleSetting.CurveValue curveValue) {
    if (this.enabled6) {
      return;
    }
    this.updateState5();
    if (!this.available()) {
      this.m622tc4wfeth(this.moduleCurve.get());
      return;
    }
    this.checkCondition(curveValue);
  }

  private boolean checkCondition(final ModuleSetting.CurveValue curveValue) {
    final ModuleSetting.CurveValue a = this.moduleCurve.get();
    this.moduleCurve.set(curveValue);
    final ModuleSetting.CurveValue b = this.moduleCurve.get();
    this.m622tc4wfeth(b);
    final boolean b2 = !Objects.equals(a, b);
    if (b2 && this.moduleConsumer != null) {
      this.moduleConsumer.accept(b);
    }
    return b2;
  }

  private void updateState5() {
    this.enabled4 = this.moduleCurve.isVisible();
    this.enabled5 = this.moduleCurve.isActive();
    final boolean available = this.available();
    if (this.visible != available) {
      this.visible = available;
      this.invalidateLayout();
    }
    this.pointerEvents = available;
    this.controlGetAnimPropertyService2.enabled(available);
  }

  private void m622tc4wfeth(final ModuleSetting.CurveValue obj) {
    final ModuleSetting.CurveValue febzk2ojfmcv = Objects.requireNonNull(obj, "curve value");
    this.updateState7(this.febzk2ojfmcv = febzk2ojfmcv);
    this.controlGetAnimPropertyService2.value(febzk2ojfmcv);
    this.updateState8();
  }

  private void updateState7(final ModuleSetting.CurveValue curveValue) {
    if (curveValue.type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
      this.moduleCurveValue2 = curveValue;
    } else if (curveValue.type() == ModuleSetting.CurveType.STEPS) {
      this.moduleCurveValue3 = curveValue;
    }
  }

  private void updateState8() {
    for (final TypeView typeView : this.items) {
      mesee355n0ho(typeView.button, typeView.label, typeView.type == this.febzk2ojfmcv.type());
    }
    final boolean b = this.febzk2ojfmcv.type() == ModuleSetting.CurveType.STEPS;
    if (this.sceneComponent42.visible != b) {
      this.sceneComponent42.visible(b);
      this.invalidateLayout();
    }
    this.sceneTextService.text(
        this.febzk2ojfmcv.steps() + ((this.febzk2ojfmcv.steps() == 1) ? " step" : " steps"));
    for (final StepModeView stepModeView : this.fdwfmom4ayrf) {
      mesee355n0ho(
          stepModeView.button,
          stepModeView.label,
          b && stepModeView.mode == this.febzk2ojfmcv.stepMode());
    }
  }

  private static void mesee355n0ho(
      final SceneCornerRadiusService sceneCornerRadiusService,
      final SceneTextService sceneTextService,
      final boolean b) {
    sceneCornerRadiusService.backgroundColor(b ? 1232983036 : -1206445535);
    sceneCornerRadiusService.hoverBackground(b ? 1568527356 : -769382093);
    sceneTextService.color(b ? -1 : -1073741825);
    sceneCornerRadiusService.invalidate();
  }

  private void updateState10() {
    if (!this.enabled6 && this.available() && this.runnable != null) {
      this.runnable.run();
    }
  }

  private void m2xil2vbnsi() {
    final String id = this.getId();
    if (id == null || id.isBlank() || this.controlGetAnimPropertyService2 == null) {
      return;
    }
    this.fdytxxoqtgjr.id(id + ".types");
    for (TypeView typeView : this.items) {
      final String s2 =
          switch (typeView.type) {
            default -> throw new MatchException(null, null);
            case LINEAR -> "linear";
            case CUBIC_BEZIER -> "bezier";
            case STEPS -> "steps";
          };
      typeView.button.id(id + ".type." + s2);
      typeView.label.id(id + ".type." + s2 + ".label");
    }
    this.controlGetAnimPropertyService2.id(id + ".graph");
    this.sceneComponent42.id(id + ".step-controls");
    this.sceneComponent43.id(id + ".step-count");
    if (this.sceneComponent43.children().size() == 3) {
      this.sceneComponent43.children().get(0).id(id + ".step-count.minus");
      this.sceneComponent43.children().get(1).id(id + ".step-count.value");
      this.sceneComponent43.children().get(2).id(id + ".step-count.plus");
    }
    this.sceneTextService.id(id + ".step-count.label");
    this.fgjetkv3noaf.id(id + ".step-modes");
    for (StepModeView stepModeView : this.fdwfmom4ayrf) {
      final String replace = stepModeView.mode.name().toLowerCase(Locale.ROOT).replace('_', '-');
      stepModeView.button.id(id + ".step-mode." + replace);
      stepModeView.label.id(id + ".step-mode." + replace + ".label");
    }
  }

  private static SceneTextService m46qdudgy2qq(final String s, final float n, final int n2) {
    return new SceneTextService(s, n, n2).fontVariant(TextMode.REGULAR).inheritEdgeSoftness(false);
  }

  private static String createText(final ModuleSetting.StepMode stepMode) {
    return switch (stepMode) {
      default -> throw new MatchException(null, null);
      case JUMP_START -> "jump start";
      case JUMP_END -> "jump end";
      case JUMP_NONE -> "jump none";
      case JUMP_BOTH -> "jump both";
    };
  }

  private static String mj20frzk4h7(final String s) {
    final String replaceAll =
        s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    return replaceAll.isEmpty() ? "setting" : replaceAll;
  }

  record TypeView(
      ModuleSetting.CurveType type, SceneCornerRadiusService button, SceneTextService label) {}

  record StepModeView(
      ModuleSetting.StepMode mode, SceneCornerRadiusService button, SceneTextService label) {}
}
