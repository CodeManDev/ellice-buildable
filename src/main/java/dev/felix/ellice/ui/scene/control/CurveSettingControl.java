



package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.ui.text.TextMode;
import java.util.Iterator;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.Locale;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.scene.LayoutContainerNode;

public final class CurveSettingControl extends LayoutContainerNode
{
    public static final float DEFAULT_WIDTH = 220.0f;
    public static final float TYPE_ROW_HEIGHT = 22.0f;
    public static final float STEP_ROW_HEIGHT = 21.0f;
    public static final float MIN_GRAPH_HEIGHT = 82.0f;
    private static final float fj4ad6tx47bn = 6.0f;
    private static final float fich5wc5xap2 = 4.0f;
    private static final int fjg5gyjpp4rq = -1206445535;
    private static final int f7vv8x1oe5i4 = -769382093;
    private static final int feqfs4wq9k4z = -534040517;
    private static final int f9omo0jtz1a0 = 1232983036;
    private static final int fbpi72fyknro = 1568527356;
    private static final int f6pfawbn6b5x = 637534207;
    private static final int fb0o9tvgzosk = -1073741825;
    private static final int fik535tgblqf = -1;
    private final ModuleSetting.Curve fe6dj2f6bnvu;
    private final CurveGraphControl fhf8y38h6es1;
    private final LayoutContainerNode fdytxxoqtgjr;
    private final LayoutContainerNode f6fzdnuz2mkn;
    private final LayoutContainerNode fg7f2k9gkgvb;
    private final LayoutContainerNode fgjetkv3noaf;
    private final SceneTextService f9h8a405uyy7;
    private final List<TypeView> f53y6n43ojad;
    private final List<StepModeView> fdwfmom4ayrf;
    private final AutoCloseable f4w7xent696i;
    private ModuleSetting.CurveValue febzk2ojfmcv;
    private ModuleSetting.CurveValue f7154a26hhrr;
    private ModuleSetting.CurveValue f47180tsrbrl;
    private Consumer<ModuleSetting.CurveValue> f8h73yr8sjnn;
    private Runnable feuold808ecy;
    private boolean f5r9jv51t69g;
    private boolean f1xy82jwdmmm;
    private boolean fcpxg8j5ai2b;
    private boolean f2u0xc0ov15i;
    private boolean ffjt7q6xpmge;
    
    public CurveSettingControl(final ModuleSetting.Curve obj) {
        this.f53y6n43ojad = new ArrayList<TypeView>();
        this.fdwfmom4ayrf = new ArrayList<StepModeView>();
        this.f7154a26hhrr = ModuleSetting.CurveValue.cubicBezier(Float.intBitsToFloat(1048576000), Float.intBitsToFloat(1036831949), Float.intBitsToFloat(1048576000), 1.0f);
        this.f47180tsrbrl = ModuleSetting.CurveValue.steps(4, ModuleSetting.StepMode.JUMP_END);
        this.f5r9jv51t69g = true;
        this.f1xy82jwdmmm = true;
        this.fe6dj2f6bnvu = Objects.requireNonNull(obj, "setting");
        this.m1tkonlg63b3(this.febzk2ojfmcv = Objects.requireNonNull(obj.get(), "curve value"));
        this.direction(Direction.COLUMN);
        this.align(Align.STRETCH);
        this.justify(Justify.START);
        this.gap(Float.intBitsToFloat(1086324736));
        this.size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.minWidth(0.0f);
        this.maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.flexShrink(0.0f);
        this.fdytxxoqtgjr = new LayoutContainerNode().direction(Direction.ROW).align(Align.STRETCH).justify(Justify.START).gap(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1102053376))).minWidth(0.0f).maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        this.m60mumkhwy3h(ModuleSetting.CurveType.LINEAR, "Linear");
        this.m60mumkhwy3h(ModuleSetting.CurveType.CUBIC_BEZIER, "Bezier");
        this.m60mumkhwy3h(ModuleSetting.CurveType.STEPS, "Steps");
        this.fhf8y38h6es1 = new CurveGraphControl(this.febzk2ojfmcv).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).minWidth(0.0f).maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(Float.intBitsToFloat(1118044160)).maxHeight(Float.intBitsToFloat(1124728832)).flex(1.0f).onInteraction(this::m6nrgqo4gtue).onChange(this::mdzoqi0gmn62).onCommit(this::m8jvtbgbzc15);
        this.f6fzdnuz2mkn = new LayoutContainerNode().direction(Direction.COLUMN).align(Align.STRETCH).justify(Justify.START).gap(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).minWidth(0.0f).maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        this.fg7f2k9gkgvb = new LayoutContainerNode().direction(Direction.ROW).align(Align.STRETCH).justify(Justify.START).gap(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1101529088))).minWidth(0.0f).maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        final SceneCornerRadiusService map6d025x2kk = this.map6d025x2kk("−", () -> this.trySetSteps(this.febzk2ojfmcv.steps() - 1));
        map6d025x2kk.size(LayoutOperationHandler.px(Float.intBitsToFloat(1106771968)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        this.f9h8a405uyy7 = m46qdudgy2qq("4 steps", Float.intBitsToFloat(1088421888), -1073741825).flex(1.0f).textAlign(SceneTextService.TextAlign.CENTER);
        final SceneCornerRadiusService m5ljhu1ooknb = m5ljhu1ooknb(this.f9h8a405uyy7);
        m5ljhu1ooknb.flex(1.0f).size(LayoutOperationHandler.auto(), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        final SceneCornerRadiusService map6d025x2kk2 = this.map6d025x2kk("+", () -> this.trySetSteps(this.febzk2ojfmcv.steps() + 1));
        map6d025x2kk2.size(LayoutOperationHandler.px(Float.intBitsToFloat(1106771968)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        this.fg7f2k9gkgvb.addChild(map6d025x2kk).addChild(m5ljhu1ooknb).addChild(map6d025x2kk2);
        this.fgjetkv3noaf = new LayoutContainerNode().direction(Direction.ROW).align(Align.STRETCH).justify(Justify.START).gap(Float.intBitsToFloat(1082130432)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1101529088))).minWidth(0.0f).maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        this.m8zbg8o9ftlv(ModuleSetting.StepMode.JUMP_START, "Start");
        this.m8zbg8o9ftlv(ModuleSetting.StepMode.JUMP_END, "End");
        this.m8zbg8o9ftlv(ModuleSetting.StepMode.JUMP_NONE, "None");
        this.m8zbg8o9ftlv(ModuleSetting.StepMode.JUMP_BOTH, "Both");
        this.f6fzdnuz2mkn.addChild(this.fg7f2k9gkgvb).addChild(this.fgjetkv3noaf);
        this.addChild(this.fdytxxoqtgjr).addChild(this.fhf8y38h6es1).addChild(this.f6fzdnuz2mkn);
        this.id("curve." + mj20frzk4h7(obj.name()));
        this.refreshState();
        this.f4w7xent696i = obj.onStateChanged(this::refreshState);
    }
    
    public ModuleSetting.Curve setting() {
        return this.fe6dj2f6bnvu;
    }
    
    public CurveGraphControl graph() {
        return this.fhf8y38h6es1;
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
            case CUBIC_BEZIER -> String.format(Locale.ROOT, "Bezier · %.2f, %.2f / %.2f, %.2f", this.febzk2ojfmcv.x1(), this.febzk2ojfmcv.y1(), this.febzk2ojfmcv.x2(), this.febzk2ojfmcv.y2());
            case STEPS -> "Steps · " + this.febzk2ojfmcv.steps() + " · " + mih0yf0qfofb(this.febzk2ojfmcv.stepMode());
        };
    }
    
    public boolean dependencyVisible() {
        return this.fcpxg8j5ai2b;
    }
    
    public boolean dependencyActive() {
        return this.f2u0xc0ov15i;
    }
    
    public boolean available() {
        return !this.ffjt7q6xpmge && this.f5r9jv51t69g && this.f1xy82jwdmmm && this.fcpxg8j5ai2b && this.f2u0xc0ov15i;
    }
    
    public boolean isPreviewing() {
        return this.fhf8y38h6es1.isDragging();
    }
    
    @Override
    public CurveSettingControl visible(final boolean f5r9jv51t69g) {
        this.f5r9jv51t69g = f5r9jv51t69g;
        this.refreshState();
        return this;
    }
    
    public CurveSettingControl enabled(final boolean f1xy82jwdmmm) {
        this.f1xy82jwdmmm = f1xy82jwdmmm;
        this.refreshState();
        return this;
    }
    
    public CurveSettingControl onChange(final Consumer<ModuleSetting.CurveValue> f8h73yr8sjnn) {
        this.f8h73yr8sjnn = f8h73yr8sjnn;
        return this;
    }
    
    public CurveSettingControl onInteraction(final Runnable feuold808ecy) {
        this.feuold808ecy = feuold808ecy;
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
        this.m1tkonlg63b3(this.febzk2ojfmcv);
        return this.ma2d4fscagrq(switch (obj) {
            default -> throw new MatchException(null, null);
            case LINEAR -> ModuleSetting.CurveValue.linear();
            case CUBIC_BEZIER -> this.f7154a26hhrr;
            case STEPS -> this.f47180tsrbrl;
        });
    }
    
    public boolean trySetSteps(final int b) {
        this.refreshState();
        if (!this.available() || this.febzk2ojfmcv.type() != ModuleSetting.CurveType.STEPS) {
            return false;
        }
        final int max = Math.max((this.febzk2ojfmcv.stepMode() == ModuleSetting.StepMode.JUMP_NONE) ? 2 : 1, Math.min(100, b));
        return max != this.febzk2ojfmcv.steps() && this.ma2d4fscagrq(ModuleSetting.CurveValue.steps(max, this.febzk2ojfmcv.stepMode()));
    }
    
    public boolean trySetStepMode(final ModuleSetting.StepMode obj) {
        Objects.requireNonNull(obj, "mode");
        this.refreshState();
        return this.available() && this.febzk2ojfmcv.type() == ModuleSetting.CurveType.STEPS && this.febzk2ojfmcv.stepMode() != obj && this.ma2d4fscagrq(ModuleSetting.CurveValue.steps((obj == ModuleSetting.StepMode.JUMP_NONE) ? Math.max(2, this.febzk2ojfmcv.steps()) : this.febzk2ojfmcv.steps(), obj));
    }
    
    public CurveSettingControl cancelPreview() {
        if (this.ffjt7q6xpmge) {
            return this;
        }
        this.fhf8y38h6es1.cancelGesture();
        this.m622tc4wfeth(this.fe6dj2f6bnvu.get());
        return this;
    }
    
    public void refreshState() {
        if (this.ffjt7q6xpmge) {
            return;
        }
        this.fcpxg8j5ai2b = this.fe6dj2f6bnvu.isVisible();
        this.f2u0xc0ov15i = this.fe6dj2f6bnvu.isActive();
        final boolean available = this.available();
        if (this.visible != available) {
            this.visible = available;
            this.invalidateLayout();
        }
        if (this.pointerEvents != available) {
            this.pointerEvents = available;
            this.invalidate();
        }
        this.fhf8y38h6es1.enabled(available);
        if (!available) {
            this.fhf8y38h6es1.cancelGesture();
        }
        this.m622tc4wfeth(this.fe6dj2f6bnvu.get());
    }
    
    @Override
    protected boolean handleScroll(final float n) {
        return false;
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1130102784);
    }
    
    @Override
    protected void onDetached() {
        this.dispose();
    }
    
    public void dispose() {
        if (this.ffjt7q6xpmge) {
            return;
        }
        this.ffjt7q6xpmge = true;
        this.visible = false;
        this.pointerEvents = false;
        this.fhf8y38h6es1.dispose();
        this.f8h73yr8sjnn = null;
        this.feuold808ecy = null;
        try {
            this.f4w7xent696i.close();
        }
        catch (final Exception ex) {}
    }
    
    private void m60mumkhwy3h(final ModuleSetting.CurveType curveType, final String s) {
        final SceneTextService m46qdudgy2qq = m46qdudgy2qq(s, Float.intBitsToFloat(1088421888), -1073741825);
        m46qdudgy2qq.overflow(SceneTextService.Overflow.ELLIPSIS).minWidth(0.0f);
        final SceneCornerRadiusService m6rpxp31vi4d = this.m6rpxp31vi4d(m46qdudgy2qq, () -> this.trySetType(curveType));
        m6rpxp31vi4d.flex(1.0f).flexShrink(1.0f).minWidth(0.0f).size(LayoutOperationHandler.auto(), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.fdytxxoqtgjr.addChild(m6rpxp31vi4d);
        this.f53y6n43ojad.add(new TypeView(curveType, m6rpxp31vi4d, m46qdudgy2qq));
    }
    
    private void m8zbg8o9ftlv(final ModuleSetting.StepMode stepMode, final String s) {
        final SceneTextService m46qdudgy2qq = m46qdudgy2qq(s, Float.intBitsToFloat(1087373312), -1073741825);
        m46qdudgy2qq.overflow(SceneTextService.Overflow.ELLIPSIS).minWidth(0.0f);
        final SceneCornerRadiusService m6rpxp31vi4d = this.m6rpxp31vi4d(m46qdudgy2qq, () -> this.trySetStepMode(stepMode));
        m6rpxp31vi4d.flex(1.0f).flexShrink(1.0f).minWidth(0.0f).size(LayoutOperationHandler.auto(), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.fgjetkv3noaf.addChild(m6rpxp31vi4d);
        this.fdwfmom4ayrf.add(new StepModeView(stepMode, m6rpxp31vi4d, m46qdudgy2qq));
    }
    
    private SceneCornerRadiusService map6d025x2kk(final String s, final Runnable runnable) {
        return this.m6rpxp31vi4d(m46qdudgy2qq(s, Float.intBitsToFloat(1090519040), -1073741825), runnable);
    }
    
    private SceneCornerRadiusService m6rpxp31vi4d(final SceneTextService sceneTextService, final Runnable runnable) {
        final SceneCornerRadiusService sceneCornerRadiusService = new SceneCornerRadiusService() {
            @Override
            protected void onPress(final float n, final float n2) {
                CurveSettingControl.this.m6nrgqo4gtue();
            }
        };
        sceneCornerRadiusService.cornerRadius(Float.intBitsToFloat(1084227584)).backgroundColor(-1206445535).hoverBackground(-769382093).pressBackground(-534040517).gradientEnd(0).glass(false).blur(0.0f).border(Float.intBitsToFloat(1057803469), 637534207).direction(Direction.ROW).align(Align.CENTER).justify(Justify.CENTER).cursorStyle(CursorStyle.POINTER).onClick(runnable);
        sceneTextService.pointerEvents(false);
        sceneCornerRadiusService.addChild(sceneTextService);
        return sceneCornerRadiusService;
    }
    
    private static SceneCornerRadiusService m5ljhu1ooknb(final SceneTextService sceneTextService) {
        final SceneCornerRadiusService sceneCornerRadiusService = new SceneCornerRadiusService().cornerRadius(Float.intBitsToFloat(1084227584)).backgroundColor(-1206445535).gradientEnd(0).glass(false).blur(0.0f).border(Float.intBitsToFloat(1057803469), 637534207).direction(Direction.ROW).align(Align.CENTER).justify(Justify.CENTER).pointerEvents(false);
        sceneCornerRadiusService.addChild(sceneTextService);
        return sceneCornerRadiusService;
    }
    
    private void mdzoqi0gmn62(final ModuleSetting.CurveValue febzk2ojfmcv) {
        if (this.ffjt7q6xpmge || !this.available()) {
            return;
        }
        this.m1tkonlg63b3(this.febzk2ojfmcv = febzk2ojfmcv);
        this.m11u8e3t34n1();
    }
    
    private void m8jvtbgbzc15(final ModuleSetting.CurveValue curveValue) {
        if (this.ffjt7q6xpmge) {
            return;
        }
        this.m5fbdk6omypb();
        if (!this.available()) {
            this.m622tc4wfeth(this.fe6dj2f6bnvu.get());
            return;
        }
        this.ma2d4fscagrq(curveValue);
    }
    
    private boolean ma2d4fscagrq(final ModuleSetting.CurveValue curveValue) {
        final ModuleSetting.CurveValue a = this.fe6dj2f6bnvu.get();
        this.fe6dj2f6bnvu.set(curveValue);
        final ModuleSetting.CurveValue b = this.fe6dj2f6bnvu.get();
        this.m622tc4wfeth(b);
        final boolean b2 = !Objects.equals(a, b);
        if (b2 && this.f8h73yr8sjnn != null) {
            this.f8h73yr8sjnn.accept(b);
        }
        return b2;
    }
    
    private void m5fbdk6omypb() {
        this.fcpxg8j5ai2b = this.fe6dj2f6bnvu.isVisible();
        this.f2u0xc0ov15i = this.fe6dj2f6bnvu.isActive();
        final boolean available = this.available();
        if (this.visible != available) {
            this.visible = available;
            this.invalidateLayout();
        }
        this.pointerEvents = available;
        this.fhf8y38h6es1.enabled(available);
    }
    
    private void m622tc4wfeth(final ModuleSetting.CurveValue obj) {
        final ModuleSetting.CurveValue febzk2ojfmcv = Objects.requireNonNull(obj, "curve value");
        this.m1tkonlg63b3(this.febzk2ojfmcv = febzk2ojfmcv);
        this.fhf8y38h6es1.value(febzk2ojfmcv);
        this.m11u8e3t34n1();
    }
    
    private void m1tkonlg63b3(final ModuleSetting.CurveValue curveValue) {
        if (curveValue.type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
            this.f7154a26hhrr = curveValue;
        }
        else if (curveValue.type() == ModuleSetting.CurveType.STEPS) {
            this.f47180tsrbrl = curveValue;
        }
    }
    
    private void m11u8e3t34n1() {
        for (final TypeView typeView : this.f53y6n43ojad) {
            mesee355n0ho(typeView.button, typeView.label, typeView.type == this.febzk2ojfmcv.type());
        }
        final boolean b = this.febzk2ojfmcv.type() == ModuleSetting.CurveType.STEPS;
        if (this.f6fzdnuz2mkn.visible != b) {
            this.f6fzdnuz2mkn.visible(b);
            this.invalidateLayout();
        }
        this.f9h8a405uyy7.text(this.febzk2ojfmcv.steps() + ((this.febzk2ojfmcv.steps() == 1) ? " step" : " steps"));
        for (final StepModeView stepModeView : this.fdwfmom4ayrf) {
            mesee355n0ho(stepModeView.button, stepModeView.label, b && stepModeView.mode == this.febzk2ojfmcv.stepMode());
        }
    }
    
    private static void mesee355n0ho(final SceneCornerRadiusService sceneCornerRadiusService, final SceneTextService sceneTextService, final boolean b) {
        sceneCornerRadiusService.backgroundColor(b ? 1232983036 : -1206445535);
        sceneCornerRadiusService.hoverBackground(b ? 1568527356 : -769382093);
        sceneTextService.color(b ? -1 : -1073741825);
        sceneCornerRadiusService.invalidate();
    }
    
    private void m6nrgqo4gtue() {
        if (!this.ffjt7q6xpmge && this.available() && this.feuold808ecy != null) {
            this.feuold808ecy.run();
        }
    }
    
    private void m2xil2vbnsi() {
        final String id = this.getId();
        if (id == null || id.isBlank() || this.fhf8y38h6es1 == null) {
            return;
        }
        this.fdytxxoqtgjr.id(id + ".types");
        for (TypeView typeView : this.f53y6n43ojad) {
            final String s2 = switch (typeView.type) {
                default -> throw new MatchException(null, null);
                case LINEAR -> "linear";
                case CUBIC_BEZIER -> "bezier";
                case STEPS -> "steps";
            };
            typeView.button.id(id + ".type." + s2);
            typeView.label.id(id + ".type." + s2 + ".label");
        }
        this.fhf8y38h6es1.id(id + ".graph");
        this.f6fzdnuz2mkn.id(id + ".step-controls");
        this.fg7f2k9gkgvb.id(id + ".step-count");
        if (this.fg7f2k9gkgvb.children().size() == 3) {
            this.fg7f2k9gkgvb.children().get(0).id(id + ".step-count.minus");
            this.fg7f2k9gkgvb.children().get(1).id(id + ".step-count.value");
            this.fg7f2k9gkgvb.children().get(2).id(id + ".step-count.plus");
        }
        this.f9h8a405uyy7.id(id + ".step-count.label");
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
    
    private static String mih0yf0qfofb(final ModuleSetting.StepMode stepMode) {
        return switch (stepMode) {
            default -> throw new MatchException(null, null);
            case JUMP_START -> "jump start";
            case JUMP_END -> "jump end";
            case JUMP_NONE -> "jump none";
            case JUMP_BOTH -> "jump both";
        };
    }
    
    private static String mj20frzk4h7(final String s) {
        final String replaceAll = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        return replaceAll.isEmpty() ? "setting" : replaceAll;
    }
    
    record TypeView(ModuleSetting.CurveType type, SceneCornerRadiusService button, SceneTextService label) {}
    
    record StepModeView(ModuleSetting.StepMode mode, SceneCornerRadiusService button, SceneTextService label) {}
}
