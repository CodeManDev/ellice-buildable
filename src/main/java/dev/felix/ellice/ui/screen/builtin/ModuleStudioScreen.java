





package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.studio.StudioListenerService;
import dev.felix.ellice.feature.studio.StudioMode;
import dev.felix.ellice.feature.studio.StudioOrbitService;
import dev.felix.ellice.feature.studio.StudioRenderer;
import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.feature.studio.StudioStringService;
import dev.felix.ellice.feature.studio.StudioValidateValidator;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialColorService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialTabsService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.color.ColorFeatureType;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.HueTrianglePicker;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioActionsService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioComponent;
import dev.felix.ellice.ui.screen.builtin.studio.StudioKindService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioShowService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioZoomService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;

public final class ModuleStudioScreen
implements ScreenOperationHandler {
    private ColorFeatureType fplk09zljn3 = ColorFeatureType.GRADIENT;
    private static final int f9ysbz25csoe = -14998990;
    private static final int fhi1rct8zx40 = -14077369;
    private static final int f3w5cjis9rdj = -12827301;
    private static final int f6fde408scpy = -1447948;
    private static final int f7tnnxyuy3aw = -5787963;
    private static final int f3zzhpds2p00 = -3096579;
    private static final int fgsdym2zj7zx = -6366515;
    private final StudioRenderer fjb3u1gjzup6;
    private final LayoutOperationHandler fezvex33hj2m;
    private StudioListenerService fgnfsukx1t8a;
    private StudioZoomService f4k4n4a25bbg;
    private StudioShowService f683q9hkl9pt;
    private MaterialTerrainTooltipsService fd0ynw0ybvq8;
    private ComponentMountService f5efodmo7zzh;
    private ScreenScreenIdService ffxw54q00oha;
    private float fgekss93cmvl = Float.intBitsToFloat(1150681088);
    private float fd73xoxawncj = Float.intBitsToFloat(0x44480000);
    private float fgnryqln6c3q;
    private long f1mhsr6yc4ar;
    private String faswzxueb2ko = "";
    private boolean f5be0gsnpezk;
    private boolean f1lp8uhuevve;
    private boolean f1un6uikl6di;
    private boolean fo3nquhz6bp;
    private String f31jugmorhr9 = "";
    private List<String> fd4jq8jvtguw = List.of();
    private Consumer<Integer> ff08vg9pbi3u;
    private IntConsumer fr70cu2sdes;
    private float fiy4t3w1ce9l;
    private float fdhsp3ga0vy2;
    private float f5dsvs688vnk;
    private float f2ylvl15o9za;
    private ScenePctService<?> fdd90i7fhfc6;
    private final StudioStringService f5wrlrvic1hj = new StudioStringService();
    private String fj6kyy2i3tib = "";

    public ModuleStudioScreen(StudioRenderer studioRenderer) {
        this(studioRenderer, studioRenderer.firstOrExample(), null);
    }

    public ModuleStudioScreen(StudioRenderer studioRenderer, StudioShapeService studioShapeService, LayoutOperationHandler layoutOperationHandler) {
        this.fjb3u1gjzup6 = studioRenderer;
        this.fezvex33hj2m = layoutOperationHandler;
        this.fgnfsukx1t8a = new StudioListenerService(studioShapeService);
    }

    public StudioListenerService session() {
        return this.fgnfsukx1t8a;
    }

    public StudioZoomService canvas() {
        return this.f4k4n4a25bbg;
    }

    public void viewport(float f, float f2) {
        this.fgekss93cmvl = f;
        this.fd73xoxawncj = f2;
        this.mbri28uaxzzm();
    }

    @Override
    public String id() {
        return "module-studio";
    }

    @Override
    public SceneCodec.Preset transitionPreset() {
        return SceneCodec.Preset.NONE;
    }

    @Override
    public float backgroundDesaturation() {
        return 0.0f;
    }

    @Override
    public boolean overlaysPreviousScreen() {
        return true;
    }

    @Override
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.ffxw54q00oha = screenScreenIdService;
        this.fo3nquhz6bp = false;
        this.f1mhsr6yc4ar = System.nanoTime();
        this.fd0ynw0ybvq8 = new MaterialTerrainTooltipsService();
        ((SceneCornerRadiusService)((SceneCornerRadiusService)this.fd0ynw0ybvq8.id("studio.root")).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).backgroundColor(-15722715).direction(ScenePctService.Direction.COLUMN);
        this.fgnfsukx1t8a.listener(this::mbri28uaxzzm);
        this.f5efodmo7zzh = new ComponentMountService().mount(this::m4pxo73eue3n, screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        this.f5efodmo7zzh.size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.fd0ynw0ybvq8.addChild(this.f5efodmo7zzh);
        return this.fd0ynw0ybvq8;
    }

    @Override
    public void tick(ScreenScreenIdService screenScreenIdService) {
        long l = System.nanoTime();
        if (this.fgnfsukx1t8a.playing) {
            this.fgnryqln6c3q += Math.min(Float.intBitsToFloat(0x3DCCCCCD), (float)(l - this.f1mhsr6yc4ar) / Float.intBitsToFloat(1315859240));
        }
        this.f1mhsr6yc4ar = l;
        if (this.fd0ynw0ybvq8 != null && (Math.abs(this.fd0ynw0ybvq8.computedW() - this.fgekss93cmvl) > Float.intBitsToFloat(0x3F000000) || Math.abs(this.fd0ynw0ybvq8.computedH() - this.fd73xoxawncj) > Float.intBitsToFloat(0x3F000000))) {
            this.fgekss93cmvl = this.fd0ynw0ybvq8.computedW();
            this.fd73xoxawncj = this.fd0ynw0ybvq8.computedH();
            this.mbri28uaxzzm();
        }
    }

    public void previewTime(float f) {
        this.fgnryqln6c3q = f;
    }

    private StudioValidateValidator.Frame mcspsno0q4iz() {
        return StudioValidateValidator.evaluate(this.fgnfsukx1t8a.project, this.m9k6sxqcqu45(), this.fgnryqln6c3q);
    }

    private LayoutOperationHandler m9k6sxqcqu45() {
        if (this.fezvex33hj2m != null) {
            return this.fezvex33hj2m;
        }
        if (CoreIsInitializedHandler.isReady() && Minecraft.getInstance().level != null) {
            return CoreIsInitializedHandler.get().hudVariables();
        }
        return this.f5wrlrvic1hj;
    }

    private void mbri28uaxzzm() {
        if (!this.fo3nquhz6bp && this.f5efodmo7zzh != null) {
            this.f5efodmo7zzh.invalidateComponent();
        }
    }

    private ComponentKeyService<?> m4pxo73eue3n(ComponentThemeService componentThemeService) {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        if (this.fgekss93cmvl >= Float.intBitsToFloat(0x44480000)) {
            arrayList.add(this.mexsbgng1sfx());
        }
        arrayList.add(this.m2x8dbxh0nro());
        if (this.fgekss93cmvl >= Float.intBitsToFloat(0x44480000)) {
            arrayList.add(this.ma71c3bhxs3e());
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.fgekss93cmvl < Float.intBitsToFloat(1142292480) ? Float.intBitsToFloat(0x40C00000) : Float.intBitsToFloat(0x41400000))).gap(Float.intBitsToFloat(0x41000000))).pointerEvents((this.f31jugmorhr9.isEmpty() && (!(this.fgekss93cmvl < Float.intBitsToFloat(0x44480000)) || !this.f1lp8uhuevve && !this.f1un6uikl6di) ? 1 : 0) != 0), this.ma21c0i2pizo(), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).gap(Float.intBitsToFloat(0x41000000)), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)), this.maoobl7dtufz()).key("main"));
        if (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000) && (this.f1lp8uhuevve || this.f1un6uikl6di)) {
            arrayList2.add(this.m92mn41d9852());
        }
        if (!this.f31jugmorhr9.isEmpty()) {
            arrayList2.add(this.mfd0e7vbetqj());
        }
        arrayList2.add(ComponentBoxService.node("studio-drag-preview", StudioShowService::new, studioShowService -> {
            this.f683q9hkl9pt = studioShowService;
            ((StudioShowService)((StudioShowService)((StudioShowService)studioShowService.absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0f))).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).pointerEvents(false);
        }, new ComponentKeyService[0]).key("drag-preview"));
        return ComponentBoxService.box(layoutContainerNode -> layoutContainerNode.size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))), (ComponentKeyService[])arrayList2.toArray(ComponentKeyService[]::new)).key("studio:" + this.fgnfsukx1t8a.project.id);
    }

    private ComponentKeyService<?> ma21c0i2pizo() {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        arrayList.add(ComponentBoxService.node("studio-mark", MaterialLabelService::new, materialLabelService -> ((MaterialLabelService)materialLabelService.shape("cookie9").active(true).tint(-3096579).size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1106247680))).flexShrink(0.0f), new ComponentKeyService[0]).key("mark"));
        arrayList.add(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(0.0f), ModuleStudioScreen.m9ot837msoew("Module Studio", this.fgekss93cmvl < Float.intBitsToFloat(1142292480) ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1100480512), -1447948).key("title"), ModuleStudioScreen.m9ot837msoew(this.fgnfsukx1t8a.project.name + (this.fgnfsukx1t8a.dirty() ? " \u00b7 Unsaved" : " \u00b7 Draft"), Float.intBitsToFloat(1093664768), -5787963).props(sceneTextService -> ((SceneTextService)sceneTextService.id("studio.project-caption")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).key("name")).key("title"));
        if (this.fgekss93cmvl >= Float.intBitsToFloat(1142292480)) {
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.projects", "Projects", () -> this.mf9529rpup4j("Projects"), false));
        }
        arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.new", "New", () -> this.mf9529rpup4j("New project"), false));
        if (this.fgekss93cmvl >= Float.intBitsToFloat(1142292480)) {
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.save", "Save", this::m1e73rhrvs0b, false));
        }
        arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.publish", this.fjb3u1gjzup6.published(this.fgnfsukx1t8a.project.id) ? "Update module" : "Create module", this::m1euf0ffb6eq, true).props(materialJoinedService -> materialJoinedService.tooltip("Save this version as a module in ClickGUI \u2192 HUD")));
        arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.close", "\u00d7", this::mal3t6jcm4g0, false).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.size(Float.intBitsToFloat(0x42000000), Float.intBitsToFloat(1108344832))).padding(0.0f)).tooltip("Back to ClickGUI")));
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.header")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1112539136)))).gap(this.fgekss93cmvl < Float.intBitsToFloat(1142292480) ? Float.intBitsToFloat(0x40800000) : Float.intBitsToFloat(1092616192))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).key("header");
    }

    private ComponentKeyService<?> m2x8dbxh0nro() {
        ComponentKeyService<LayoutContainerNode> componentKeyService = ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.gap(2.0f)).align(ScenePctService.Align.CENTER), ModuleStudioScreen.mah97mno42lx("studio.mode.design", "Design", () -> this.mxjeo5pbpyt(false), !this.fgnfsukx1t8a.logic), ModuleStudioScreen.mah97mno42lx("studio.mode.logic", "Logic", () -> this.mxjeo5pbpyt(true), this.fgnfsukx1t8a.logic));
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        if (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000)) {
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.library", "Library", () -> {
                this.f1lp8uhuevve = true;
                this.f1un6uikl6di = false;
                this.mbri28uaxzzm();
            }, false));
        }
        arrayList.add(componentKeyService.props(scenePctService -> scenePctService.flex(1.0f)));
        arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.play", this.fgnfsukx1t8a.playing ? "Pause" : "Play", () -> {
            this.fgnfsukx1t8a.playing = !this.fgnfsukx1t8a.playing;
            this.mbri28uaxzzm();
        }, false));
        if (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000)) {
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.inspect", "Inspect", () -> {
                this.f1un6uikl6di = true;
                this.f1lp8uhuevve = false;
                this.mbri28uaxzzm();
            }, false));
        }
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.workspace")).flex(1.0f)).minWidth(0.0f)).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000)), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).align(ScenePctService.Align.CENTER)).gap(Float.intBitsToFloat(0x40C00000))).flexShrink(0.0f), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)), ComponentBoxService.node("studio-canvas", () -> new StudioZoomService(this.fgnfsukx1t8a, this::mcspsno0q4iz, () -> Float.valueOf(this.fgnryqln6c3q)), studioZoomService -> {
            this.f4k4n4a25bbg = studioZoomService;
            ((StudioZoomService)((StudioZoomService)((StudioZoomService)studioZoomService.beforeSelect(() -> {
                this.m8lski6g0f3w();
                ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
            }).id("studio.canvas")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f);
        }, new ComponentKeyService[0]).key("canvas"), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x42000000)))).gap(Float.intBitsToFloat(0x40800000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), ModuleStudioScreen.mah97mno42lx("studio.undo", "Undo", () -> this.mhx16xih6his(this.fgnfsukx1t8a::undo), false).props(materialJoinedService -> materialJoinedService.available(this.fgnfsukx1t8a.canUndo())), ModuleStudioScreen.mah97mno42lx("studio.redo", "Redo", () -> this.mhx16xih6his(this.fgnfsukx1t8a::redo), false).props(materialJoinedService -> materialJoinedService.available(this.fgnfsukx1t8a.canRedo())), ComponentBoxService.box(layoutContainerNode -> layoutContainerNode.flex(1.0f), new ComponentKeyService[0]), ModuleStudioScreen.mah97mno42lx("studio.snap", "Snap", () -> {
            this.fgnfsukx1t8a.snap = !this.fgnfsukx1t8a.snap;
            this.mbri28uaxzzm();
        }, this.fgnfsukx1t8a.snap), ModuleStudioScreen.mah97mno42lx("studio.zoom-out", "\u2212", () -> this.f4k4n4a25bbg.zoomBy(Float.intBitsToFloat(0x3F555555)), false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.minWidth(Float.intBitsToFloat(1105199104))).padding(0.0f, Float.intBitsToFloat(0x40C00000))), ModuleStudioScreen.mah97mno42lx("studio.fit", "Fit", () -> this.f4k4n4a25bbg.fit(), false), ModuleStudioScreen.mah97mno42lx("studio.zoom-in", "+", () -> this.f4k4n4a25bbg.zoomBy(Float.intBitsToFloat(1067030938)), false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.minWidth(Float.intBitsToFloat(1105199104))).padding(0.0f, Float.intBitsToFloat(0x40C00000))))).key("workspace");
    }

    private ComponentKeyService<?> mexsbgng1sfx() {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        if (this.f5be0gsnpezk) {
            for (StudioShapeService.Shape shape : this.fgnfsukx1t8a.project.shapes.reversed()) {
                if (!this.mdpvk87r2lwx(shape.name)) continue;
                arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.layer." + shape.id, shape.name, () -> {
                    ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
                    this.fgnfsukx1t8a.logic = false;
                    this.fgnfsukx1t8a.select(shape.id);
                }, shape.id.equals(this.fgnfsukx1t8a.selected)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).justify(ScenePctService.Justify.START)).tooltip(String.valueOf((Object)shape.kind) + (!shape.visible ? " \u00b7 Hidden" : "") + (shape.locked ? " \u00b7 Locked" : ""))));
            }
            if (arrayList.isEmpty()) {
                arrayList.add(ModuleStudioScreen.m9ot837msoew("Your shapes appear here.", Float.intBitsToFloat(0x41400000), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true).padding(Float.intBitsToFloat(0x41000000))));
            }
        } else if (!this.fgnfsukx1t8a.logic) {
            for (StudioShapeService.ShapeKind shapeKind : StudioShapeService.ShapeKind.values()) {
                if (!this.mdpvk87r2lwx(StudioListenerService.title(shapeKind))) continue;
                arrayList.add(this.mb4y5urydwbm(shapeKind));
            }
        } else {
            String string = "";
            for (StudioMode studioMode : StudioMode.values()) {
                if (!this.mdpvk87r2lwx(studioMode.title + " " + studioMode.group)) continue;
                if (!string.equals(studioMode.group)) {
                    arrayList.add(ModuleStudioScreen.m9ot837msoew(studioMode.group.toUpperCase(Locale.ROOT), Float.intBitsToFloat(1092616192), -5787963).props(sceneTextService -> sceneTextService.padding(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x40400000), Float.intBitsToFloat(0x40800000))).key("group:" + studioMode.group));
                    string = studioMode.group;
                }
                arrayList.add(this.m26ttemygk4i(studioMode));
            }
        }
        arrayList.replaceAll(componentKeyService -> componentKeyService.props(scenePctService -> ((ScenePctService)scenePctService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flexShrink(0.0f)));
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("studio.library-panel")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(this.fgekss93cmvl >= Float.intBitsToFloat(1148846080) ? Float.intBitsToFloat(1127743488) : (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000) ? Float.intBitsToFloat(1130889216) : Float.intBitsToFloat(1125908480))))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flexShrink(0.0f)).backgroundColor(-14998990).cornerRadius(Float.intBitsToFloat(1101004800)).border(Float.intBitsToFloat(1061158912), -12827301).padding(Float.intBitsToFloat(1092616192))).gap(Float.intBitsToFloat(0x41000000))).direction(ScenePctService.Direction.COLUMN), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(2.0f), ModuleStudioScreen.mah97mno42lx("studio.library.blocks", this.fgnfsukx1t8a.logic ? "Blocks" : "Shapes", () -> {
            this.f5be0gsnpezk = false;
            this.mbri28uaxzzm();
        }, !this.f5be0gsnpezk).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40C00000))), ModuleStudioScreen.mah97mno42lx("studio.library.layers", "Layers", () -> {
            this.f5be0gsnpezk = true;
            this.mbri28uaxzzm();
        }, this.f5be0gsnpezk).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40C00000)))), ComponentBoxService.node("studio-library-search", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            ModuleStudioScreen.md6cgkmpx3bm(controlLetterSpacingService);
            if (!controlLetterSpacingService.focused()) {
                controlLetterSpacingService.text(this.faswzxueb2ko);
            }
            ((ControlLetterSpacingService)((ControlLetterSpacingService)controlLetterSpacingService.id("studio.library-search")).placeholder(this.f5be0gsnpezk ? "Find a layer" : (this.fgnfsukx1t8a.logic ? "Find a block" : "Find a shape")).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x42000000)))).onChanged(string -> {
                this.faswzxueb2ko = string;
                this.mbri28uaxzzm();
            });
        }, new ComponentKeyService[0]).key("search"), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.library-items")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).gap(Float.intBitsToFloat(0x40A00000))).scrollable(true)).scrollbarWidth(2.0f)).scrollbarColor(-12827301)).clip(true), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)), ModuleStudioScreen.m9ot837msoew(this.f5be0gsnpezk ? "Front to back" : "Click or drag to the canvas", Float.intBitsToFloat(1092616192), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true))).key("library");
    }

    private boolean mdpvk87r2lwx(String string) {
        return (this.faswzxueb2ko.isBlank() || string.toLowerCase(Locale.ROOT).contains(this.faswzxueb2ko.toLowerCase(Locale.ROOT)) ? 1 : 0) != 0;
    }

    private ComponentKeyService<?> mb4y5urydwbm(StudioShapeService.ShapeKind shapeKind) {
        return ComponentBoxService.node("studio-library-tile", StudioActionsService::new, studioActionsService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)studioActionsService.actions(() -> this.mbqsabi0j2zl(shapeKind, null, null), (f, f2) -> this.mbqsabi0j2zl(shapeKind, (Float)f, (Float)f2)).preview((f, f2) -> this.f683q9hkl9pt.show(StudioListenerService.title(shapeKind), -3096579, f.floatValue(), f2.floatValue()), () -> this.f683q9hkl9pt.clear()).id("studio.add.shape." + shapeKind.name().toLowerCase(Locale.ROOT))).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1110966272)))).flexShrink(0.0f)).backgroundColor(-14077369).hoverBackground(-13353900).cornerRadius(Float.intBitsToFloat(0x41400000)).padding(Float.intBitsToFloat(0x40C00000), Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x41000000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER), ComponentBoxService.node("studio-shape-icon", StudioKindService::new, studioKindService -> ((StudioKindService)studioKindService.kind(shapeKind).size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1106247680))).flexShrink(0.0f), new ComponentKeyService[0]), ModuleStudioScreen.m9ot837msoew(StudioListenerService.title(shapeKind), Float.intBitsToFloat(0x41400000), -1447948).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f))).key(shapeKind.name());
    }

    private ComponentKeyService<?> m26ttemygk4i(StudioMode studioMode) {
        return ComponentBoxService.node("studio-library-tile", StudioActionsService::new, studioActionsService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)studioActionsService.actions(() -> this.m37ts4mcxejq(studioMode, null, null), (f, f2) -> this.m37ts4mcxejq(studioMode, (Float)f, (Float)f2)).preview((f, f2) -> this.f683q9hkl9pt.show(studioMode.title, studioMode.color(), f.floatValue(), f2.floatValue()), () -> this.f683q9hkl9pt.clear()).id("studio.add.block." + studioMode.name().toLowerCase(Locale.ROOT))).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).flexShrink(0.0f)).backgroundColor(StudioValidateValidator.mix(-14077369, studioMode.color(), Float.intBitsToFloat(1032134328))).hoverBackground(-13353900).cornerRadius(Float.intBitsToFloat(0x41400000)).padding(0.0f, Float.intBitsToFloat(0x41100000))).gap(Float.intBitsToFloat(0x40E00000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)sceneCornerRadiusService.size(Float.intBitsToFloat(0x40A00000), Float.intBitsToFloat(1098907648))).cornerRadius(Float.intBitsToFloat(0x40400000)).backgroundColor(studioMode.color()).pointerEvents(false), new ComponentKeyService[0]), ModuleStudioScreen.m9ot837msoew(studioMode.title, Float.intBitsToFloat(1093664768), -1447948).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f))).key(studioMode.name());
    }

    private void mbqsabi0j2zl(StudioShapeService.ShapeKind shapeKind, Float f, Float f2) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        if (f != null && !this.f4k4n4a25bbg.containsScene(f.floatValue(), f2.floatValue())) {
            this.fgnfsukx1t8a.message = "Drop onto the canvas to add a shape.";
            this.mbri28uaxzzm();
            return;
        }
        float[] fArray = this.f4k4n4a25bbg.insertion();
        this.fgnfsukx1t8a.addShape(shapeKind, f == null ? fArray[0] : this.f4k4n4a25bbg.worldX(f.floatValue()), f2 == null ? fArray[1] : this.f4k4n4a25bbg.worldY(f2.floatValue()));
        this.f1lp8uhuevve = false;
        this.mbri28uaxzzm();
    }

    private void m37ts4mcxejq(StudioMode studioMode, Float f, Float f2) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        if (f != null && !this.f4k4n4a25bbg.containsScene(f.floatValue(), f2.floatValue())) {
            this.fgnfsukx1t8a.message = "Drop onto the canvas to add a block.";
            this.mbri28uaxzzm();
            return;
        }
        float[] fArray = this.f4k4n4a25bbg.insertion();
        this.fgnfsukx1t8a.addBlock(studioMode, f == null ? fArray[0] : this.f4k4n4a25bbg.worldX(f.floatValue()), f2 == null ? fArray[1] : this.f4k4n4a25bbg.worldY(f2.floatValue()));
        this.f1lp8uhuevve = false;
        this.mbri28uaxzzm();
    }

    private void mxjeo5pbpyt(boolean bl) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        this.fgnfsukx1t8a.logic = bl;
        this.f5be0gsnpezk = false;
        this.faswzxueb2ko = "";
        if (bl && this.fgnfsukx1t8a.block() == null) {
            String string = this.fgnfsukx1t8a.selected = this.fgnfsukx1t8a.project.blocks.isEmpty() ? "" : this.fgnfsukx1t8a.project.blocks.getFirst().id;
        }
        if (!bl && this.fgnfsukx1t8a.shape() == null) {
            this.fgnfsukx1t8a.selected = this.fgnfsukx1t8a.project.shapes.isEmpty() ? "" : this.fgnfsukx1t8a.project.shapes.getFirst().id;
        }
        this.mbri28uaxzzm();
    }

    private ComponentKeyService<?> maoobl7dtufz() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1103101952)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), ModuleStudioScreen.m9ot837msoew(this.fgnfsukx1t8a.message, Float.intBitsToFloat(1093664768), -5787963).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.id("studio.status")).flex(1.0f)).minWidth(0.0f)), ModuleStudioScreen.m9ot837msoew(this.fgnfsukx1t8a.project.shapes.size() + " shapes \u00b7 " + this.fgnfsukx1t8a.project.blocks.size() + " blocks", Float.intBitsToFloat(1092616192), -5787963).props(sceneTextService -> sceneTextService.visible(this.fgekss93cmvl >= Float.intBitsToFloat(1142292480))));
    }

    private ComponentKeyService<?> ma71c3bhxs3e() {
        return this.meeqrbovpqe2();
    }

    private ComponentKeyService<?> m92mn41d9852() {
        return this.mdrogl6fbkup();
    }

    private ComponentKeyService<?> mfd0e7vbetqj() {
        return this.meva05ehgifz();
    }

    private ComponentKeyService<?> meeqrbovpqe2() {
        StudioShapeService.Block block;
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        StudioShapeService.Shape shape = this.fgnfsukx1t8a.logic ? null : this.fgnfsukx1t8a.shape();
        StudioShapeService.Block block2 = block = this.fgnfsukx1t8a.logic ? this.fgnfsukx1t8a.block() : null;
        if (shape != null) {
            this.mexj16mljf9a(shape, arrayList);
        } else if (block != null) {
            this.m8phesbxj80c(block, arrayList);
        } else {
            arrayList.add(ModuleStudioScreen.m9ot837msoew("PROJECT", Float.intBitsToFloat(1092616192), -3096579));
            arrayList.add(this.mh0ne9kjucf9("studio.project-name", "Name", this.fgnfsukx1t8a.project.name, string -> {
                if (string.isBlank() || string.length() > 48) {
                    this.msihguhqsap("Use a name with 1\u201348 characters.");
                    return;
                }
                this.fgnfsukx1t8a.edit(studioShapeService -> {
                    studioShapeService.name = string.strip();
                });
            }));
            arrayList.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000)), this.m9c8nle36p7l("studio.artboard.width", "Width", this.fgnfsukx1t8a.project.width, Float.intBitsToFloat(0x42000000), Float.intBitsToFloat(1153957888), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                studioShapeService.width = f.floatValue();
            })), this.m9c8nle36p7l("studio.artboard.height", "Height", this.fgnfsukx1t8a.project.height, Float.intBitsToFloat(0x42000000), Float.intBitsToFloat(1148846080), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                studioShapeService.height = f.floatValue();
            }))));
            arrayList.add(ModuleStudioScreen.m9ot837msoew("Shapes create the look. Logic connects live data to their properties.", Float.intBitsToFloat(0x41400000), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true)));
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.export", "Export project", this::m7hzyslkgqmz, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
            if (this.fjb3u1gjzup6.published(this.fgnfsukx1t8a.project.id)) {
                arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.unpublish", "Remove installed module", this::muyuxbyd7jv, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
            }
        }
        boolean bl = this.fezvex33hj2m != null || !CoreIsInitializedHandler.isReady() || Minecraft.getInstance().level == null;
        arrayList.replaceAll(componentKeyService -> componentKeyService.props(scenePctService -> ((ScenePctService)((ScenePctService)scenePctService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(0.0f)).flexShrink(0.0f)));
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("studio.inspector")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(this.fgekss93cmvl >= Float.intBitsToFloat(1148846080) ? Float.intBitsToFloat(1131937792) : (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000) ? Float.intBitsToFloat(1133248512) : Float.intBitsToFloat(1129840640))))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flexShrink(0.0f)).backgroundColor(-14998990).cornerRadius(Float.intBitsToFloat(1101004800)).border(Float.intBitsToFloat(1061158912), -12827301).padding(Float.intBitsToFloat(0x41400000))).gap(Float.intBitsToFloat(0x41000000))).direction(ScenePctService.Direction.COLUMN), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).align(ScenePctService.Align.CENTER), ModuleStudioScreen.m9ot837msoew("LIVE PREVIEW", Float.intBitsToFloat(1092616192), -6366515).props(sceneTextService -> sceneTextService.flex(1.0f)), ModuleStudioScreen.m9ot837msoew(bl ? "Sample data" : "Game data", Float.intBitsToFloat(1092616192), -5787963)), ComponentBoxService.node("studio-preview", () -> new StudioComponent(this.fgnfsukx1t8a, this::mcspsno0q4iz, () -> Float.valueOf(this.fgnryqln6c3q)), studioComponent -> ((StudioComponent)((StudioComponent)((StudioComponent)studioComponent.id("studio.preview")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(this.fd73xoxawncj < Float.intBitsToFloat(1140457472) ? Float.intBitsToFloat(1119354880) : Float.intBitsToFloat(1124990976)))).flexShrink(0.0f), new ComponentKeyService[0]).key("preview"), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1103101952)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).visible((bl && this.fezvex33hj2m == null ? 1 : 0) != 0)).flexShrink(0.0f), ModuleStudioScreen.m9ot837msoew("Health", Float.intBitsToFloat(1092616192), -5787963), ComponentBoxService.node("studio-sample-slider", SliderControl::new, sliderControl -> ((SliderControl)((SliderControl)((SliderControl)((SliderControl)sliderControl.material(true).compact(true).range(0.0f, Float.intBitsToFloat(1101004800)).step(1.0f).value(this.f5wrlrvic1hj.health).id("studio.preview-health")).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1103101952)))).flex(1.0f)).minWidth(0.0f)).onChange(f -> {
            this.f5wrlrvic1hj.health = f.floatValue();
        }), new ComponentKeyService[0])), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)sceneCornerRadiusService.size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(1.0f))).backgroundColor(-12827301).pointerEvents(false), new ComponentKeyService[0]), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.properties")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x40C00000), 0.0f)).gap(Float.intBitsToFloat(1092616192))).scrollable(true)).scrollbarWidth(2.0f)).scrollbarColor(-12827301)).clip(true), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new))).key("inspector");
    }

    private void mexj16mljf9a(StudioShapeService.Shape shape, List<ComponentKeyService<?>> list) {
        StudioShapeService.Shape shape2;
        list.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000))).align(ScenePctService.Align.CENTER), ModuleStudioScreen.m9ot837msoew(shape.kind.name(), Float.intBitsToFloat(1092616192), -3096579).props(sceneTextService -> sceneTextService.flex(1.0f)), ModuleStudioScreen.mah97mno42lx("studio.select-project", "Project", () -> {
            ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
            this.fgnfsukx1t8a.select("");
        }, false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1105199104)))).padding(0.0f, Float.intBitsToFloat(0x41000000)))));
        list.add(this.mh0ne9kjucf9("studio.shape.name", "Name", shape.name, string -> {
            if (string.isBlank() || string.length() > 48) {
                this.msihguhqsap("Use a shape name with 1\u201348 characters.");
                return;
            }
            this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.name = string;
            });
        }));
        list.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000)), this.m9c8nle36p7l("studio.shape.x", "X", shape.x, Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.x = f.floatValue();
        })), this.m9c8nle36p7l("studio.shape.y", "Y", shape.y, Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.y = f.floatValue();
        }))));
        list.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000)), this.m9c8nle36p7l("studio.shape.width", "Width", shape.width, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(1157234688), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.width = f.floatValue();
        })), this.m9c8nle36p7l("studio.shape.height", "Height", shape.height, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(1157234688), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.height = f.floatValue();
        }))));
        if (shape.kind == StudioShapeService.ShapeKind.TEXT) {
            list.add(this.mh0ne9kjucf9("studio.shape.text", "Text \u00b7 {player.name}, {client.fps}", shape.text, string -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.text = string;
            })));
            list.add(this.m9c8nle36p7l("studio.shape.font", "Font size", shape.fontSize, Float.intBitsToFloat(0x41000000), Float.intBitsToFloat(1126170624), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.fontSize = f.floatValue();
            })));
            list.add(ModuleStudioScreen.mah97mno42lx("studio.shape.alignment", "Align \u00b7 " + shape.alignment.name().toLowerCase(Locale.ROOT), () -> this.mf9ngisuqd1b("Text alignment", List.of("Left", "Center", "Right"), n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.alignment = StudioShapeService.TextAlignment.values()[n];
            })), false));
        }
        if (shape.kind != StudioShapeService.ShapeKind.GROUP) {
            list.add(this.mb5t1ww04yoo("studio.shape.color", "Color", shape.color, n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.color = n;
            })));
            if (shape.kind != StudioShapeService.ShapeKind.TEXT) {
                list.add(ModuleStudioScreen.mah97mno42lx("studio.shape.finish", "Finish \u00b7 " + shape.finish.name().toLowerCase(Locale.ROOT), () -> this.mf9ngisuqd1b("Surface finish", List.of("Solid", "Gradient", "Aurora"), n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                    shape.finish = StudioShapeService.Finish.values()[n];
                })), false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
                if (shape.finish != StudioShapeService.Finish.SOLID) {
                    list.add(this.mb5t1ww04yoo("studio.shape.color2", "Second color", shape.color2, n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                        shape.color2 = n;
                    })));
                }
                list.add(this.m9c8nle36p7l("studio.shape.radius", shape.kind == StudioShapeService.ShapeKind.RING ? "Ring width" : "Corner radius", shape.kind == StudioShapeService.ShapeKind.RING ? shape.stroke : shape.radius, shape.kind == StudioShapeService.ShapeKind.RING ? 1.0f : 0.0f, shape.kind == StudioShapeService.ShapeKind.RING ? Float.intBitsToFloat(1120403456) : Float.intBitsToFloat(1140457472), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                    if (shape.kind == StudioShapeService.ShapeKind.RING) {
                        shape.stroke = f.floatValue();
                    } else {
                        shape.radius = f.floatValue();
                    }
                })));
            }
            list.add(this.m9c8nle36p7l("studio.shape.opacity", "Opacity \u00b7 0\u20131", shape.opacity, 0.0f, 1.0f, f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                shape.opacity = f.floatValue();
            })));
        }
        list.add(ModuleStudioScreen.mah97mno42lx("studio.shape.parent", "Group \u00b7 " + ((shape2 = this.fgnfsukx1t8a.project.shape(shape.parent)) == null ? "Artboard" : shape2.name), () -> {
            List<StudioShapeService.Shape> availableGroups = this.fgnfsukx1t8a.project.shapes.stream().filter(candidateShape -> candidateShape.kind == StudioShapeService.ShapeKind.GROUP && this.fgnfsukx1t8a.parentError(candidateShape.id) == null).toList();
            ArrayList<String> arrayList = new ArrayList<String>(List.of("Artboard"));
            arrayList.addAll(availableGroups.stream().map(candidateShape -> candidateShape.name).toList());
            this.mf9ngisuqd1b("Parent group", arrayList, n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                float[] fArray = this.m5drsjhw1wcu(shape.parent);
                String string = n == 0 ? "" : availableGroups.get(n.intValue() - 1).id;
                float[] fArray2 = this.m5drsjhw1wcu(string);
                shape.x = StudioValidateValidator.clamp(shape.x + fArray[0] - fArray2[0], Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296));
                shape.y = StudioValidateValidator.clamp(shape.y + fArray[1] - fArray2[1], Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296));
                shape.parent = string;
            }));
        }, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        list.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40800000)), ModuleStudioScreen.mah97mno42lx("studio.shape.visible", shape.visible ? "Visible" : "Hidden", () -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.visible = !shape.visible;
        }), shape.visible).props(materialJoinedService -> materialJoinedService.flex(1.0f)), ModuleStudioScreen.mah97mno42lx("studio.shape.lock", shape.locked ? "Unlock" : "Lock", () -> this.fgnfsukx1t8a.edit(studioShapeService -> {
            shape.locked = !shape.locked;
        }), shape.locked).props(materialJoinedService -> materialJoinedService.flex(1.0f))));
        list.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40800000)), ModuleStudioScreen.mah97mno42lx("studio.shape.backward", "Back", () -> this.mhx16xih6his(() -> this.fgnfsukx1t8a.reorder(-1)), false).props(materialJoinedService -> materialJoinedService.flex(1.0f)), ModuleStudioScreen.mah97mno42lx("studio.shape.forward", "Front", () -> this.mhx16xih6his(() -> this.fgnfsukx1t8a.reorder(1)), false).props(materialJoinedService -> materialJoinedService.flex(1.0f))));
        list.add(this.mfu38xxh8qxo());
    }

    private float[] m5drsjhw1wcu(String string) {
        float f = 0.0f;
        float f2 = 0.0f;
        StudioShapeService.Shape shape = this.fgnfsukx1t8a.project.shape(string);
        while (shape != null) {
            f += shape.x;
            f2 += shape.y;
            shape = this.fgnfsukx1t8a.project.shape(shape.parent);
        }
        return new float[]{f, f2};
    }

    private void m8phesbxj80c(StudioShapeService.Block block, List<ComponentKeyService<?>> list) {
        list.add(ModuleStudioScreen.m9ot837msoew(block.kind.group.toUpperCase(Locale.ROOT), Float.intBitsToFloat(1092616192), block.kind.color()));
        list.add(ModuleStudioScreen.m9ot837msoew(block.kind.title, Float.intBitsToFloat(1099431936), -1447948));
        if (block.kind == StudioMode.NUMBER) {
            list.add(this.m9c8nle36p7l("studio.block.number", "Value", block.number, Float.intBitsToFloat(-943501312), Float.intBitsToFloat(1203982336), f -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                block.number = f.floatValue();
            })));
        }
        if (block.kind == StudioMode.TEXT || block.kind == StudioMode.FORMAT || block.kind == StudioMode.CAPTION) {
            list.add(this.mh0ne9kjucf9("studio.block.text", block.kind == StudioMode.FORMAT ? "Template \u00b7 {value}" : "Text", block.text, string -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                block.text = string;
            })));
        }
        if (block.kind == StudioMode.COLOR || block.kind == StudioMode.TINT || block.kind == StudioMode.MIX_COLOR) {
            list.add(this.mb5t1ww04yoo("studio.block.color", "Default color", block.color, n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                block.color = n;
            })));
        }
        if (block.kind.shapeOutput()) {
            StudioShapeService.Shape shape = this.fgnfsukx1t8a.project.shape(block.target);
            list.add(ModuleStudioScreen.mah97mno42lx("studio.block.target", shape == null ? "Choose a shape" : shape.name, () -> {
                List<StudioShapeService.Shape> targetShapes = List.copyOf(this.fgnfsukx1t8a.project.shapes);
                this.mf9ngisuqd1b("Target shape", targetShapes.stream().map(targetShape -> targetShape.name).toList(), n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                    block.target = targetShapes.get(n.intValue()).id;
                }));
            }, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        } else if (block.kind == StudioMode.MODULE_GATE) {
            list.add(ModuleStudioScreen.mah97mno42lx("studio.block.target", block.target.isBlank() ? "Choose a module" : block.target, () -> {
                List<String> moduleNames = this.fjb3u1gjzup6.moduleNames();
                this.mf9ngisuqd1b("Target module", moduleNames, n -> this.fgnfsukx1t8a.edit(studioShapeService -> {
                    block.target = moduleNames.get(n);
                }));
            }, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
            list.add(ModuleStudioScreen.m9ot837msoew("Runs while your module is enabled. The previous state is restored when it stops.", Float.intBitsToFloat(1093664768), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true)));
        }
        for (StudioMode.Port port : block.kind.ports) {
            StudioShapeService.Block block2 = this.fgnfsukx1t8a.project.block(block.inputs.get(port.name()));
            list.add(ModuleStudioScreen.m9ot837msoew(port.name() + " \u00b7 " + port.type().name().toLowerCase(Locale.ROOT), Float.intBitsToFloat(1092616192), port.type().ink));
            list.add(ModuleStudioScreen.mah97mno42lx("studio.port." + port.name(), (String)(block2 == null ? "Use a block\u2026" : "\u21b3 " + block2.kind.title), () -> {
                List<StudioShapeService.Block> compatibleBlocks = this.fgnfsukx1t8a.project.blocks.stream().filter(candidateBlock -> candidateBlock.kind.output == port.type() && !candidateBlock.id.equals(block.id)).toList();
                ArrayList<String> arrayList = new ArrayList<String>(List.of("Use default value"));
                arrayList.addAll(compatibleBlocks.stream().map(candidateBlock -> candidateBlock.kind.title + (candidateBlock.kind == StudioMode.NUMBER ? " \u00b7 " + ModuleStudioScreen.mitpalpfj76m(candidateBlock.number) : "")).toList());
                this.mf9ngisuqd1b("Connect " + port.name(), arrayList, n -> {
                    if (n == 0) {
                        this.fgnfsukx1t8a.disconnect(block.id, port.name());
                    } else {
                        this.fgnfsukx1t8a.connect(compatibleBlocks.get(n.intValue() - 1).id, block.id, port.name());
                    }
                });
            }, false).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
            if (block2 == null && port.type() == StudioMode.Type.NUMBER) {
                list.add(this.m9c8nle36p7l("studio.default." + port.name(), "Default", block.defaults.getOrDefault(port.name(), Float.valueOf(port.fallback())).floatValue(), Float.intBitsToFloat(-943501312), Float.intBitsToFloat(1203982336), f -> this.fgnfsukx1t8a.edit(studioShapeService -> block.defaults.put(port.name(), (Float)f))));
            }
            if (block2 != null || port.type() != StudioMode.Type.BOOLEAN) continue;
            boolean bl = block.defaults.getOrDefault(port.name(), Float.valueOf(port.fallback())).floatValue() != 0.0f;
            list.add(ModuleStudioScreen.mah97mno42lx("studio.default." + port.name(), bl ? "True" : "False", () -> this.fgnfsukx1t8a.edit(studioShapeService -> block.defaults.put(port.name(), Float.valueOf(bl ? 0.0f : 1.0f))), bl).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        }
        list.add(this.mfu38xxh8qxo());
    }

    private ComponentKeyService<?> mfu38xxh8qxo() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40800000)), ModuleStudioScreen.mah97mno42lx("studio.duplicate", "Duplicate", () -> this.mhx16xih6his(this.fgnfsukx1t8a::duplicate), false).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40C00000))), ModuleStudioScreen.mah97mno42lx("studio.delete", "Delete", () -> this.mhx16xih6his(this.fgnfsukx1t8a::delete), false).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40C00000))));
    }

    private ComponentKeyService<?> mh0ne9kjucf9(String string, String string2, String string3, Consumer<String> consumer) {
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minWidth(0.0f)).gap(Float.intBitsToFloat(0x40800000))).flexShrink(0.0f), ModuleStudioScreen.m9ot837msoew(string2, Float.intBitsToFloat(1092616192), -5787963).props(sceneTextService -> sceneTextService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))), ComponentBoxService.node("studio-input", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            ModuleStudioScreen.md6cgkmpx3bm(controlLetterSpacingService);
            ((ControlLetterSpacingService)controlLetterSpacingService.id(string)).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            if (!controlLetterSpacingService.focused()) {
                controlLetterSpacingService.text(string3);
            }
            controlLetterSpacingService.onSubmit(consumer).onUnfocus(() -> {
                if (!this.fo3nquhz6bp) {
                    consumer.accept(controlLetterSpacingService.text());
                }
            });
        }, new ComponentKeyService[0]).key(string)).key("field:" + string);
    }

    private ComponentKeyService<?> m9c8nle36p7l(String string, String string3, float f, float minimum, float maximum, Consumer<Float> consumer) {
        return this.mh0ne9kjucf9(string, string3, ModuleStudioScreen.mitpalpfj76m(f), string2 -> {
            try {
                float parsedValue = Float.parseFloat(string2.strip().replace(',', '.'));
                if (!Float.isFinite(parsedValue) || parsedValue < minimum || parsedValue > maximum) {
                    throw new NumberFormatException();
                }
                consumer.accept(parsedValue);
            }
            catch (NumberFormatException numberFormatException) {
                this.msihguhqsap(string3 + ": enter a value from " + ModuleStudioScreen.mitpalpfj76m(minimum) + " to " + ModuleStudioScreen.mitpalpfj76m(maximum) + ".");
            }
        }).props(scenePctService -> ((ScenePctService)((ScenePctService)scenePctService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).flex(1.0f)).minWidth(0.0f));
    }

    private ComponentKeyService<?> mb5t1ww04yoo(String string2, String string3, int n, IntConsumer intConsumer) {
        ArrayList<ComponentKeyService<MaterialJoinedService>> arrayList = new ArrayList<ComponentKeyService<MaterialJoinedService>>();
        arrayList.add(ModuleStudioScreen.mah97mno42lx(string2 + ".picker", "Mix", () -> {
            this.fr70cu2sdes = intConsumer;
            this.mcxoirceyord(n);
            this.mf9529rpup4j("Color \u00b7 " + string3);
        }, false).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1106247680)))).padding(0.0f, Float.intBitsToFloat(0x40E00000))).tooltip("Open the color mixer")));
        for (int n2 : new int[]{-4344587, -7086900, -1001069, -856583, -14208444}) {
            arrayList.add(ComponentBoxService.node("studio-swatch", MaterialJoinedService::new, materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.colors(0, -1447948).shape(Float.intBitsToFloat(0x41000000), Float.intBitsToFloat(1104150528)).id(string2 + ".swatch." + Integer.toHexString(n2))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1106247680)))).flex(1.0f)).minWidth(0.0f)).padding(Float.intBitsToFloat(0x40400000))).onClick(() -> intConsumer.accept(n2)), ComponentBoxService.node("studio-color", MaterialColorService::new, materialColorService -> ((MaterialColorService)materialColorService.color(n2).radius(Float.intBitsToFloat(0x40C00000)).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).pointerEvents(false), new ComponentKeyService[0])));
        }
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40800000)), this.mh0ne9kjucf9(string2, string3, String.format(Locale.ROOT, "#%08X", n), string -> {
            String colorHex = string.strip().replace("#", "");
            if (!colorHex.matches("[0-9A-Fa-f]{6}|[0-9A-Fa-f]{8}")) {
                this.msihguhqsap("Use #RRGGBB or #AARRGGBB.");
                return;
            }
            long l = Long.parseLong(colorHex, 16);
            intConsumer.accept((int)(colorHex.length() == 6 ? 0xFF000000L | l : l));
        }), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40400000)), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)));
    }

    private static String mitpalpfj76m(float f) {
        return new BigDecimal(Float.toString(f)).stripTrailingZeros().toPlainString();
    }

    private void msihguhqsap(String string) {
        this.fgnfsukx1t8a.message = string;
        this.mbri28uaxzzm();
    }

    private void mf9ngisuqd1b(String string, List<String> list, Consumer<Integer> consumer) {
        this.fd4jq8jvtguw = list;
        this.ff08vg9pbi3u = consumer;
        this.mf9529rpup4j(string);
    }

    private ComponentKeyService<?> mdrogl6fbkup() {
        return ComponentBoxService.box(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0f))).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).layerBreak(true), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0f))).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).backgroundColor(-2063200998).interactive(true)).onClick(() -> {
            this.f1un6uikl6di = false;
            this.f1lp8uhuevve = false;
            this.mbri28uaxzzm();
        }), new ComponentKeyService[0]), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1115684864)), this.f1un6uikl6di ? dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x41000000)) : dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)), this.f1lp8uhuevve ? dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x41000000)) : dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(this.f1un6uikl6di ? Math.min(Float.intBitsToFloat(1133248512), this.fgekss93cmvl - Float.intBitsToFloat(1098907648)) : Float.intBitsToFloat(1130889216)))).minHeight(0.0f), this.f1un6uikl6di ? this.ma71c3bhxs3e() : this.mexsbgng1sfx()).key(this.f1un6uikl6di ? "inspect-drawer" : "library-drawer").onMount(layoutContainerNode -> {
            layoutContainerNode.translateX(this.f1un6uikl6di ? Float.intBitsToFloat(1099956224) : Float.intBitsToFloat(-1047527424));
            MotionAnimateService.animate(layoutContainerNode, MotionColorsContainer.Floats.TRANSLATE_X, 0.0f, (SceneEaseHandler)MaterialEnterService.PAGE);
        })).key("drawer");
    }

    private ComponentKeyService<?> meva05ehgifz() {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        if (this.f31jugmorhr9.startsWith("Color \u00b7 ")) {
            arrayList.add(MaterialTabsService.tabs("studio.color-mode", this.fplk09zljn3, colorFeatureType -> {
                this.fplk09zljn3 = colorFeatureType;
                this.mbri28uaxzzm();
            }));
            arrayList.add(ComponentBoxService.node("studio-color-preview", MaterialColorService::new, materialColorService -> ((MaterialColorService)materialColorService.color(this.m4r8396etdam()).radius(Float.intBitsToFloat(1096810496)).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1110441984)))).pointerEvents(false), new ComponentKeyService[0]));
            if (this.fplk09zljn3 == ColorFeatureType.TRIANGLE) {
                arrayList.add(ComponentBoxService.node("studio-color-triangle", HueTrianglePicker::new, hueTrianglePicker -> ((HueTrianglePicker)hueTrianglePicker.hue(this.fiy4t3w1ce9l).sat(this.fdhsp3ga0vy2).bri(this.f5dsvs688vnk).onHueChange(f -> {
                    this.fiy4t3w1ce9l = f.floatValue();
                    this.mbri28uaxzzm();
                }).onSBChange((f, f2) -> {
                    this.fdhsp3ga0vy2 = f.floatValue();
                    this.f5dsvs688vnk = f2.floatValue();
                    this.mbri28uaxzzm();
                }).id("studio.color-triangle")).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1130102784))), new ComponentKeyService[0]).key("triangle"));
            } else {
                arrayList.add(ComponentBoxService.node("studio-color-field", SaturationBrightnessPicker::new, saturationBrightnessPicker -> ((SaturationBrightnessPicker)saturationBrightnessPicker.material(true).hue(this.fiy4t3w1ce9l).sat(this.fdhsp3ga0vy2).bri(this.f5dsvs688vnk).cornerRadius(Float.intBitsToFloat(1098907648)).onChange((f, f2) -> {
                    this.fdhsp3ga0vy2 = f.floatValue();
                    this.f5dsvs688vnk = f2.floatValue();
                    this.mbri28uaxzzm();
                }).id("studio.color-field")).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1125908480))), new ComponentKeyService[0]).key("sv"));
                arrayList.add(ModuleStudioScreen.m9ot837msoew("Hue", Float.intBitsToFloat(1092616192), -5787963));
                arrayList.add(ComponentBoxService.node("studio-hue", ColorGetAnimPropertyService::new, colorGetAnimPropertyService -> ((ColorGetAnimPropertyService)colorGetAnimPropertyService.material(true).hue(this.fiy4t3w1ce9l).cornerRadius(Float.intBitsToFloat(0x41400000)).onChange(f -> {
                    this.fiy4t3w1ce9l = f.floatValue();
                    this.mbri28uaxzzm();
                }).id("studio.color-hue")).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x42000000))), new ComponentKeyService[0]).key("hue"));
            }
            arrayList.add(ModuleStudioScreen.m9ot837msoew("Alpha", Float.intBitsToFloat(1092616192), -5787963));
            arrayList.add(ComponentBoxService.node("studio-alpha", AlphaSliderControl::new, alphaSliderControl -> ((AlphaSliderControl)alphaSliderControl.material(true).hue(this.fiy4t3w1ce9l).sat(this.fdhsp3ga0vy2).bri(this.f5dsvs688vnk).alpha(this.f2ylvl15o9za).cornerRadius(Float.intBitsToFloat(0x41400000)).onChange(f -> {
                this.f2ylvl15o9za = f.floatValue();
                this.mbri28uaxzzm();
            }).id("studio.color-alpha")).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(0x42000000))), new ComponentKeyService[0]).key("alpha"));
            arrayList.add(this.mh0ne9kjucf9("studio.color-hex", "Hex", String.format(Locale.ROOT, "#%08X", this.m4r8396etdam()), string -> {
                String string2 = string.strip().replace("#", "");
                if (!string2.matches("[0-9A-Fa-f]{6}|[0-9A-Fa-f]{8}")) {
                    this.msihguhqsap("Use #RRGGBB or #AARRGGBB.");
                    return;
                }
                this.mcxoirceyord((int)(Long.parseLong(string2, 16) | (string2.length() == 6 ? 0xFF000000L : 0L)));
                this.mbri28uaxzzm();
            }));
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.color.apply", "Apply color", () -> {
                ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
                this.fr70cu2sdes.accept(this.m4r8396etdam());
                this.f31jugmorhr9 = "";
                this.mbri28uaxzzm();
            }, true).props(materialJoinedService -> materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        } else if (this.f31jugmorhr9.equals("New project")) {
            arrayList.add(this.mgksk3el38x("studio.template.orbit", "Vital orbit", "A health ring, live text and connected data.", StudioOrbitService::orbit));
            arrayList.add(this.mgksk3el38x("studio.template.session", "Session glass", "An FPS card with a soft gradient surface.", StudioOrbitService::session));
            arrayList.add(this.mgksk3el38x("studio.template.pulse", "Signal garden", "Time, a wave and a breathing shape.", StudioOrbitService::pulse));
            arrayList.add(this.mgksk3el38x("studio.template.blank", "Blank canvas", "Start with your own shapes and ideas.", StudioOrbitService::blank));
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.show-projects", "Open saved projects", () -> {
                this.f31jugmorhr9 = "Projects";
                this.mbri28uaxzzm();
            }, false));
        } else if (this.f31jugmorhr9.equals("Projects")) {
            for (StudioShapeService studioShapeService : this.fjb3u1gjzup6.projects()) {
                arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.project." + studioShapeService.id, studioShapeService.name + (this.fjb3u1gjzup6.published(studioShapeService.id) ? " \u00b7 Installed" : " \u00b7 Draft"), () -> this.m7h9b59p2gvq(studioShapeService), false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).justify(ScenePctService.Justify.START)));
            }
            if (this.fjb3u1gjzup6.projects().isEmpty()) {
                arrayList.add(ModuleStudioScreen.m9ot837msoew("Save a draft to add it to your project library.", Float.intBitsToFloat(1095761920), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true)));
            }
            arrayList.add(this.mh0ne9kjucf9("studio.import-path", "Import a .ellice-module.json file", this.fj6kyy2i3tib, string -> {
                this.fj6kyy2i3tib = string;
            }));
            arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.import", "Import project", this::mbzubvm8vnej, false));
            if (!this.fjb3u1gjzup6.warnings().isEmpty()) {
                arrayList.add(ModuleStudioScreen.m9ot837msoew("Some saved files could not be loaded. Their originals were kept.", Float.intBitsToFloat(1093664768), -1001069).props(sceneTextService -> sceneTextService.wordWrap(true)));
            }
        } else {
            for (int i = 0; i < this.fd4jq8jvtguw.size(); ++i) {
                int n = i;
                arrayList.add(ModuleStudioScreen.mah97mno42lx("studio.choice." + i, this.fd4jq8jvtguw.get(i), () -> {
                    this.f31jugmorhr9 = "";
                    this.ff08vg9pbi3u.accept(n);
                    this.mbri28uaxzzm();
                }, false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).justify(ScenePctService.Justify.START)));
            }
            if (this.fd4jq8jvtguw.isEmpty()) {
                arrayList.add(ModuleStudioScreen.m9ot837msoew("Add a compatible item to the project first.", Float.intBitsToFloat(1095761920), -5787963).props(sceneTextService -> sceneTextService.wordWrap(true)));
            }
        }
        arrayList.replaceAll(componentKeyService -> componentKeyService.props(scenePctService -> scenePctService.flexShrink(0.0f)));
        float f = Math.min(Math.max(Float.intBitsToFloat(1120403456), this.fd73xoxawncj - Float.intBitsToFloat(0x42000000)), this.f31jugmorhr9.equals("New project") ? Float.intBitsToFloat(1137836032) : (this.f31jugmorhr9.startsWith("Color \u00b7 ") ? Float.intBitsToFloat(0x440E0000) : (float)Math.min(520, 112 + arrayList.size() * 64)));
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.modal")).absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0f))).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).padding(Float.intBitsToFloat(1098907648))).layerBreak(true)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.absolute()).inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0f))).size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())).backgroundColor(-1442312936).interactive(true)).onClick(() -> {
            this.f31jugmorhr9 = "";
            this.mbri28uaxzzm();
        }), new ComponentKeyService[0]), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("studio.dialog")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1138491392))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(f))).minHeight(0.0f)).backgroundColor(-14998990).border(1.0f, -12827301).cornerRadius(Float.intBitsToFloat(1103101952)).padding(Float.intBitsToFloat(1101004800))).gap(Float.intBitsToFloat(0x41400000))).direction(ScenePctService.Direction.COLUMN)).stopPropagation(true)).clip(true), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), ModuleStudioScreen.m9ot837msoew(this.f31jugmorhr9.startsWith("Color \u00b7 ") ? "Color mixer" : this.f31jugmorhr9, Float.intBitsToFloat(1101004800), -1447948).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)), ModuleStudioScreen.mah97mno42lx("studio.modal.close", "\u00d7", () -> {
            this.f31jugmorhr9 = "";
            this.mbri28uaxzzm();
        }, false)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("studio.dialog-scroll")).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).gap(Float.intBitsToFloat(0x41000000))).scrollable(true)).scrollbarWidth(2.0f)).scrollbarColor(-12827301)).clip(true), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new))).onMount(sceneCornerRadiusService -> {
            sceneCornerRadiusService.translateY(Float.intBitsToFloat(1096810496));
            MotionAnimateService.animate(sceneCornerRadiusService, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0f, (SceneEaseHandler)MaterialEnterService.PAGE);
        })).key("modal");
    }

    private int m4r8396etdam() {
        return Color.HSBtoRGB(this.fiy4t3w1ce9l, this.fdhsp3ga0vy2, this.f5dsvs688vnk) & 0xFFFFFF | Math.round(this.f2ylvl15o9za * Float.intBitsToFloat(1132396544)) << 24;
    }

    private void mcxoirceyord(int n) {
        float[] fArray = Color.RGBtoHSB(n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, null);
        this.fiy4t3w1ce9l = fArray[0];
        this.fdhsp3ga0vy2 = fArray[1];
        this.f5dsvs688vnk = fArray[2];
        this.f2ylvl15o9za = (float)(n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
    }

    private ComponentKeyService<?> mgksk3el38x(String string, String string2, String string3, Supplier<StudioShapeService> supplier) {
        return ComponentBoxService.node("studio-template", MaterialJoinedService::new, materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.colors(-14077369, -1447948).shape(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1115160576)).id(string)).width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1115947008)))).padding(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1096810496))).flexShrink(0.0f)).direction(ScenePctService.Direction.COLUMN)).gap(2.0f)).justify(ScenePctService.Justify.CENTER)).onClick(() -> this.m58dr25q30qk((Supplier)supplier)), ModuleStudioScreen.m9ot837msoew(string2, Float.intBitsToFloat(1096810496), -1447948), ModuleStudioScreen.m9ot837msoew(string3, Float.intBitsToFloat(1093664768), -5787963).props(sceneTextService -> ((SceneTextService)sceneTextService.width(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true))).key(string);
    }

    private void m7h9b59p2gvq(StudioShapeService studioShapeService) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            this.fjb3u1gjzup6.save(this.fgnfsukx1t8a.project);
        }
        catch (IOException iOException) {
            this.msihguhqsap(iOException.getMessage());
            return;
        }
        this.fgnfsukx1t8a = new StudioListenerService(studioShapeService);
        this.fgnfsukx1t8a.listener(this::mbri28uaxzzm);
        this.fgnryqln6c3q = 0.0f;
        this.f31jugmorhr9 = "";
        this.f5be0gsnpezk = false;
        this.faswzxueb2ko = "";
        this.fdd90i7fhfc6 = null;
        this.mbri28uaxzzm();
    }

    private void m7hzyslkgqmz() {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            Path path = this.fjb3u1gjzup6.exportProject(this.fgnfsukx1t8a.project);
            this.msihguhqsap("Exported to " + String.valueOf(path.toAbsolutePath()));
        }
        catch (IOException iOException) {
            this.msihguhqsap(iOException.getMessage());
        }
    }

    private void mbzubvm8vnej() {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            this.m7h9b59p2gvq(this.fjb3u1gjzup6.importProject(Path.of(this.fj6kyy2i3tib.strip(), new String[0])));
        }
        catch (Exception exception) {
            this.msihguhqsap("Import failed: " + exception.getMessage());
        }
    }

    private void muyuxbyd7jv() {
        try {
            this.fjb3u1gjzup6.unpublish(this.fgnfsukx1t8a.project.id);
            if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
                CoreIsInitializedHandler.get().config().save();
            }
            this.msihguhqsap("Installed module removed. Your draft is kept.");
        }
        catch (IOException iOException) {
            this.msihguhqsap(iOException.getMessage());
        }
    }

    private void mf9529rpup4j(String string) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        this.f31jugmorhr9 = string;
        this.mbri28uaxzzm();
    }

    private void mhx16xih6his(Runnable runnable) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        runnable.run();
    }

    private boolean m1e73rhrvs0b() {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            this.fjb3u1gjzup6.save(this.fgnfsukx1t8a.project);
            this.fgnfsukx1t8a.saved();
            return true;
        }
        catch (IOException iOException) {
            this.fgnfsukx1t8a.message = iOException.getMessage();
            this.mbri28uaxzzm();
            return false;
        }
    }

    private void m1euf0ffb6eq() {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            this.fjb3u1gjzup6.publish(this.fgnfsukx1t8a.project);
            this.fgnfsukx1t8a.saved();
            this.fgnfsukx1t8a.message = "Module ready \u00b7 ClickGUI \u2192 HUD \u2192 " + this.fgnfsukx1t8a.project.name;
            if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
                CoreIsInitializedHandler.get().config().save();
            }
            this.mbri28uaxzzm();
        }
        catch (IOException iOException) {
            this.fgnfsukx1t8a.message = iOException.getMessage();
            this.mbri28uaxzzm();
        }
    }

    private void mal3t6jcm4g0() {
        if (this.m1e73rhrvs0b() && this.ffxw54q00oha != null) {
            this.ffxw54q00oha.open("clickgui");
        }
    }

    @Override
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
        try {
            this.fjb3u1gjzup6.save(this.fgnfsukx1t8a.project);
        }
        catch (IOException iOException) {
            CoreIsInitializedHandler.LOGGER.warn("Could not save Module Studio draft", (Throwable)iOException);
        }
        this.fo3nquhz6bp = true;
    }

    @Override
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
        int n3;
        int n4 = n3 = (n2 & 0xA) != 0 ? 1 : 0;
        if (n == 256) {
            this.m8lski6g0f3w();
            ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
            if (!this.f31jugmorhr9.isEmpty()) {
                this.f31jugmorhr9 = "";
                this.mbri28uaxzzm();
            } else if (this.f1lp8uhuevve || this.f1un6uikl6di) {
                this.f1un6uikl6di = false;
                this.f1lp8uhuevve = false;
                this.mbri28uaxzzm();
            } else if (this.f4k4n4a25bbg != null && this.f4k4n4a25bbg.cancelInteraction()) {
                this.msihguhqsap("Gesture cancelled");
            } else {
                this.mal3t6jcm4g0();
            }
            return true;
        }
        if (n3 != 0 && n == 83) {
            this.m1e73rhrvs0b();
            return true;
        }
        if (n3 != 0 && n == 70 && this.f31jugmorhr9.isEmpty()) {
            ScenePctService<?> scenePctService;
            this.m8lski6g0f3w();
            ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
            if (this.fgekss93cmvl < Float.intBitsToFloat(0x44480000)) {
                this.f1lp8uhuevve = true;
                this.f1un6uikl6di = false;
                this.mbri28uaxzzm();
            }
            if ((scenePctService = this.fd0ynw0ybvq8.findById("studio.library-search")) instanceof ControlLetterSpacingService) {
                ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService;
                if (CoreIsInitializedHandler.isReady()) {
                    CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacingService);
                } else {
                    controlLetterSpacingService.focus();
                }
                controlLetterSpacingService.selectAll();
            }
            return true;
        }
        if (n == 258) {
            ArrayList arrayList = new ArrayList();
            ModuleStudioScreen.m7i6fc1pbhc9(this.fd0ynw0ybvq8, arrayList);
            if (!arrayList.isEmpty()) {
                ScenePctService scenePctService;
                ScenePctService scenePctService2 = this.fdd90i7fhfc6;
                for (ScenePctService scenePctService3 : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
                    if (!(scenePctService3 instanceof ControlLetterSpacingService) || !((ControlLetterSpacingService)(scenePctService = (ControlLetterSpacingService)scenePctService3)).focused()) continue;
                    scenePctService2 = scenePctService3;
                }
                int n5 = arrayList.indexOf(scenePctService2);
                int n6 = (n2 & 1) != 0 ? 1 : 0;
                this.m8lski6g0f3w();
                ModuleStudioScreen.m8pvl0iapk57(this.fd0ynw0ybvq8);
                arrayList.clear();
                ModuleStudioScreen.m7i6fc1pbhc9(this.fd0ynw0ybvq8, arrayList);
                if (arrayList.isEmpty()) {
                    return true;
                }
                this.fdd90i7fhfc6 = (ScenePctService)arrayList.get(n5 < 0 ? (n6 != 0 ? arrayList.size() - 1 : 0) : Math.floorMod(n5 + (n6 != 0 ? -1 : 1), arrayList.size()));
                ScenePctService<?> scenePctService4 = this.fdd90i7fhfc6;
                if (scenePctService4 instanceof MaterialJoinedService) {
                    scenePctService = (MaterialJoinedService)scenePctService4;
                    ((MaterialJoinedService)scenePctService).keyboardFocused(true);
                }
                if ((scenePctService4 = this.fdd90i7fhfc6) instanceof StudioActionsService) {
                    scenePctService = (StudioActionsService)scenePctService4;
                    ((StudioActionsService)scenePctService).focus(true);
                }
                if ((scenePctService4 = this.fdd90i7fhfc6) instanceof ControlLetterSpacingService) {
                    scenePctService = (ControlLetterSpacingService)scenePctService4;
                    if (CoreIsInitializedHandler.isReady()) {
                        CoreIsInitializedHandler.get().scene().setFocusedInput((ControlLetterSpacingService)scenePctService);
                    } else {
                        ((ControlLetterSpacingService)scenePctService).focus();
                    }
                }
                ModuleStudioScreen.m3bcx6aizrlj(this.fdd90i7fhfc6);
            }
            return true;
        }
        if (ModuleStudioScreen.m39ncr87jhz7(this.fd0ynw0ybvq8)) {
            return false;
        }
        if ((n == 257 || n == 32) && ModuleStudioScreen.mcoee0flezbr(this.fdd90i7fhfc6)) {
            SceneCornerRadiusService sceneCornerRadiusService;
            ScenePctService<?> scenePctService = this.fdd90i7fhfc6;
            if (scenePctService instanceof MaterialJoinedService && ((MaterialJoinedService)(sceneCornerRadiusService = (MaterialJoinedService)scenePctService)).available()) {
                ((MaterialJoinedService)sceneCornerRadiusService).activate();
                return true;
            }
            scenePctService = this.fdd90i7fhfc6;
            if (scenePctService instanceof StudioActionsService) {
                sceneCornerRadiusService = (StudioActionsService)scenePctService;
                ((StudioActionsService)sceneCornerRadiusService).activate();
                return true;
            }
        }
        if (!this.f31jugmorhr9.isEmpty() || this.f1lp8uhuevve || this.f1un6uikl6di) {
            return false;
        }
        if (n3 != 0 && n == 90) {
            if ((n2 & 1) != 0) {
                this.fgnfsukx1t8a.redo();
            } else {
                this.fgnfsukx1t8a.undo();
            }
            return true;
        }
        if (n3 != 0 && n == 89) {
            this.fgnfsukx1t8a.redo();
            return true;
        }
        if (n3 != 0 && n == 68) {
            this.fgnfsukx1t8a.duplicate();
            return true;
        }
        if (n == 261 || n == 259) {
            this.fgnfsukx1t8a.delete();
            return true;
        }
        if (n == 32) {
            this.fgnfsukx1t8a.playing = !this.fgnfsukx1t8a.playing;
            this.mbri28uaxzzm();
            return true;
        }
        return false;
    }

    private void m8lski6g0f3w() {
        SceneCornerRadiusService sceneCornerRadiusService;
        ScenePctService<?> scenePctService = this.fdd90i7fhfc6;
        if (scenePctService instanceof MaterialJoinedService) {
            sceneCornerRadiusService = (MaterialJoinedService)scenePctService;
            ((MaterialJoinedService)sceneCornerRadiusService).keyboardFocused(false);
        }
        if ((scenePctService = this.fdd90i7fhfc6) instanceof StudioActionsService) {
            sceneCornerRadiusService = (StudioActionsService)scenePctService;
            ((StudioActionsService)sceneCornerRadiusService).focus(false);
        }
        this.fdd90i7fhfc6 = null;
    }

    private static boolean m39ncr87jhz7(ScenePctService<?> scenePctService) {
        Object object;
        if (scenePctService == null) {
            return false;
        }
        if (scenePctService instanceof ControlLetterSpacingService && ((ControlLetterSpacingService)(object = (ControlLetterSpacingService)scenePctService)).focused()) {
            return true;
        }
        for (ScenePctService scenePctService2 : scenePctService.children()) {
            if (!ModuleStudioScreen.m39ncr87jhz7(scenePctService2)) continue;
            return true;
        }
        return false;
    }

    private static boolean mcoee0flezbr(ScenePctService<?> scenePctService) {
        if (scenePctService == null) {
            return false;
        }
        for (ScenePctService<?> scenePctService2 = scenePctService; scenePctService2 != null; scenePctService2 = scenePctService2.parent()) {
            if (scenePctService2.visible && scenePctService2.pointerEvents()) continue;
            return false;
        }
        return true;
    }

    private static void m7i6fc1pbhc9(ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
        Object object;
        if (scenePctService == null || !scenePctService.visible || !scenePctService.pointerEvents()) {
            return;
        }
        if (scenePctService instanceof MaterialJoinedService && ((MaterialJoinedService)(object = (MaterialJoinedService)scenePctService)).available() || scenePctService instanceof ControlLetterSpacingService || scenePctService instanceof StudioActionsService) {
            list.add(scenePctService);
        }
        for (ScenePctService scenePctService2 : scenePctService.children()) {
            ModuleStudioScreen.m7i6fc1pbhc9(scenePctService2, list);
        }
    }

    private static void m3bcx6aizrlj(ScenePctService<?> scenePctService) {
        for (ScenePctService<?> scenePctService2 = scenePctService.parent(); scenePctService2 != null; scenePctService2 = scenePctService2.parent()) {
            if (!(scenePctService2.scrollMin() < 0.0f)) continue;
            float scrollDelta = scenePctService.computedY() < scenePctService2.computedY() + Float.intBitsToFloat(0x40C00000) ? scenePctService2.computedY() + Float.intBitsToFloat(0x40C00000) - scenePctService.computedY() : (scenePctService.computedY() + scenePctService.computedH() > scenePctService2.computedY() + scenePctService2.computedH() - Float.intBitsToFloat(0x40C00000) ? scenePctService2.computedY() + scenePctService2.computedH() - Float.intBitsToFloat(0x40C00000) - scenePctService.computedY() - scenePctService.computedH() : 0.0f);
            if (scrollDelta == 0.0f) continue;
            float f3 = Math.max(scenePctService2.scrollMin(), Math.min(0.0f, scenePctService2.scrollY() + scrollDelta));
            scenePctService2.scrollTarget(f3);
            MotionAnimateService.animate(scenePctService2, MotionColorsContainer.Floats.SCROLL_Y, f3, (SceneEaseHandler)MaterialEnterService.PAGE);
        }
    }

    private static ComponentKeyService<SceneTextService> m9ot837msoew(String string, float f, int n) {
        return MaterialTextService.text(string, f, n).props(sceneTextService -> sceneTextService.lineHeight(f + Float.intBitsToFloat(0x40A00000)).letterSpacing(Float.intBitsToFloat(0x3DCCCCCD)));
    }

    private static ComponentKeyService<MaterialJoinedService> mah97mno42lx(String string, String string2, Runnable runnable, boolean bl) {
        return ComponentBoxService.node("studio-button", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(bl ? -3096579 : -14077369, bl ? -13622199 : -1447948).shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1106247680));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id(string)).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).minWidth(Float.intBitsToFloat(0x42000000))).padding(0.0f, Float.intBitsToFloat(1092616192))).flexShrink(0.0f)).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).onClick(runnable);
        }, ModuleStudioScreen.m9ot837msoew(string2, Float.intBitsToFloat(1093664768), bl ? -13622199 : -1447948).props(sceneTextService -> sceneTextService.fontFamily("material-roboto-medium"))).key(string);
    }

    private static void md6cgkmpx3bm(ControlLetterSpacingService controlLetterSpacingService) {
        MaterialTextService.input(controlLetterSpacingService);
        ((ControlLetterSpacingService)controlLetterSpacingService.fontSize(Float.intBitsToFloat(0x41400000)).letterSpacing(Float.intBitsToFloat(0x3DCCCCCD)).bgColor(-14077369).focusBorder(-3096579).cornerRadius(Float.intBitsToFloat(0x40C00000)).maxLength(256).height(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(Float.intBitsToFloat(1107820544)))).minWidth(0.0f);
    }

    private static void m8pvl0iapk57(ScenePctService<?> scenePctService) {
        Object object;
        if (scenePctService == null) {
            return;
        }
        if (scenePctService instanceof ControlLetterSpacingService && ((ControlLetterSpacingService)(object = (ControlLetterSpacingService)scenePctService)).focused()) {
            ((ControlLetterSpacingService)object).unfocus();
        }
        for (ScenePctService scenePctService2 : List.copyOf(scenePctService.children())) {
            ModuleStudioScreen.m8pvl0iapk57(scenePctService2);
        }
    }

    private  void m58dr25q30qk(Supplier supplier) {
        this.m7h9b59p2gvq((StudioShapeService)supplier.get());
    }
}

