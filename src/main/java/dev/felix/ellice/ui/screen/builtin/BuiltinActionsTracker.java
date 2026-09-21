


package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.menu.MenuLoaderTracker;
import dev.felix.ellice.server.ServerKeyService;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.component.ComponentUnmountService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.SceneFocusService;
import dev.felix.ellice.ui.scene.SceneImageService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneSelectionSelector;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.builtin.BuiltinStateController;
import dev.felix.ellice.ui.screen.builtin.ServerListRenderer;
import dev.felix.ellice.ui.screen.builtin.WorldListRenderer;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BuiltinActionsTracker
implements ScreenOperationHandler {
    private final ServerViewTracker fak082bn9t7m;
    private final MenuLoaderTracker fa81jdvf5c6w;
    private final Supplier<Session> fe78alkn4oqq;
    private final Actions fe5fm4nwbka;
    private final String f4fudvbq0rtu;
    private final ServerListRenderer fjgtw9rgyyc6;
    private final WorldListRenderer fjfgkucz72e1;
    private ComponentMountService fru87gio83d;
    private ScreenScreenIdService f1n980i402fz;
    private MaterialTerrainTooltipsService f2lnr7jlwqj7;
    private SceneFocusService fds04nrcye91;
    private Session fg1qifvr2o4t;
    private Page fukeosqhtbl = Page.HOME;
    private float fc0fjka99anh = Float.intBitsToFloat(1150025728);
    private float f718gi8fifcn = Float.intBitsToFloat(0x44340000);
    private long fhh18s83d95g = -1L;
    private long faos4ztjpj8t = -1L;
    private long fcq8mwkebm7e;
    private ScenePctService<?> fic1uu0899v0;
    private float fe0404fx1874;
    private int f2fd0zjmgy51 = 1;
    private final EnumSet<Page> fchakgedxeah = EnumSet.of(Page.HOME);
    private boolean f68m7h1knt6z;
    private ComponentMountService f1lghizzt89u;
    private LayoutContainerNode f31gsm0clfwn;
    private BuiltinStateController f2np82eubib2;

    public BuiltinActionsTracker(ServerViewTracker serverViewTracker, MenuLoaderTracker menuLoaderTracker, Supplier<Session> supplier, Actions actions, String string) {
        this.fak082bn9t7m = Objects.requireNonNull(serverViewTracker);
        this.fa81jdvf5c6w = Objects.requireNonNull(menuLoaderTracker);
        this.fe78alkn4oqq = Objects.requireNonNull(supplier);
        this.fe5fm4nwbka = Objects.requireNonNull(actions);
        this.f4fudvbq0rtu = string;
        this.fjgtw9rgyyc6 = new ServerListRenderer(serverViewTracker, actions.connect());
        this.fjgtw9rgyyc6.expanded(true);
        this.fjgtw9rgyyc6.preview(actions.preview());
        this.fjfgkucz72e1 = new WorldListRenderer(menuLoaderTracker, actions.playWorld(), actions.editWorld(), actions.createWorld());
    }

    @Override
    public String id() {
        return "main-menu";
    }

    @Override
    public boolean closesOnEscape() {
        return false;
    }

    @Override
    public float backgroundDesaturation() {
        return 0.0f;
    }

    @Override
    public SceneCodec.Preset transitionPreset() {
        return SceneCodec.Preset.NONE;
    }

    public Page page() {
        return this.fukeosqhtbl;
    }

    public void page(Page page) {
        Objects.requireNonNull(page);
        if (this.fukeosqhtbl == page) {
            return;
        }
        if (this.f2lnr7jlwqj7 != null && this.fukeosqhtbl == Page.HOME && this.f2lnr7jlwqj7.findById("menu.home.scroll") != null) {
            this.fe0404fx1874 = this.f2lnr7jlwqj7.findById("menu.home.scroll").scrollY();
        }
        boolean bl = this.fchakgedxeah.contains((Object)page);
        this.f2fd0zjmgy51 = Integer.compare(page.ordinal(), this.fukeosqhtbl.ordinal());
        this.m4uhdcd0ccgm();
        this.fukeosqhtbl = page;
        this.fchakgedxeah.add(page);
        if (this.fds04nrcye91 != null) {
            this.fds04nrcye91.page(page.ordinal()).focus(false);
        }
        this.m1e1r7v3o7np();
        if (this.f2lnr7jlwqj7 == null) {
            return;
        }
        for (Page page2 : EnumSet.copyOf(this.fchakgedxeah)) {
            int n;
            ScenePctService<?> scenePctService = this.f2lnr7jlwqj7.findById("menu.view." + String.valueOf((Object)page2));
            if (scenePctService == null) continue;
            int n2 = n = page2 == this.fukeosqhtbl ? 1 : 0;
            if (n != 0 && !bl) {
                ((ScenePctService)((ScenePctService)scenePctService.opacity(0.0f)).translateY(Float.intBitsToFloat(1099956224))).translateX(this.f2fd0zjmgy51 * 8);
            }
            MotionAnimateService.animate(scenePctService, MotionColorsContainer.Floats.OPACITY, n != 0 ? 1.0f : 0.0f, SceneEaseHandler.Tween.ease(n != 0 ? Float.intBitsToFloat(1050924810) : Float.intBitsToFloat(1047904911)), 0.0f, () -> {
                if (page2 != this.fukeosqhtbl) {
                    this.fchakgedxeah.remove((Object)page2);
                    this.m1e1r7v3o7np();
                }
            });
            MotionAnimateService.animate(scenePctService, MotionColorsContainer.Floats.TRANSLATE_Y, n != 0 ? 0.0f : Float.intBitsToFloat(-1054867456), (SceneEaseHandler)new SceneEaseHandler.Spring(Float.intBitsToFloat(1105723392), Float.intBitsToFloat(1130758144)));
            MotionAnimateService.animate(scenePctService, MotionColorsContainer.Floats.TRANSLATE_X, n != 0 ? 0.0f : (float)(-this.f2fd0zjmgy51 * 8), (SceneEaseHandler)new SceneEaseHandler.Spring(Float.intBitsToFloat(1107820544), Float.intBitsToFloat(1133248512)));
        }
    }

    public void viewport(float f, float f2) {
        if (this.fc0fjka99anh == f && this.f718gi8fifcn == f2) {
            return;
        }
        this.fc0fjka99anh = f;
        this.f718gi8fifcn = f2;
        this.m4uhdcd0ccgm();
        this.m1e1r7v3o7np();
        if (this.f1lghizzt89u != null) {
            this.f1lghizzt89u.invalidateComponent();
        }
        if (this.f2np82eubib2 != null) {
            this.f2np82eubib2.viewport(f, f2);
        }
    }

    private boolean m1lc7eja7fps() {
        return this.fc0fjka99anh < Float.intBitsToFloat(1142620160);
    }

    private boolean mc8ne6rdnuf5() {
        return this.fc0fjka99anh >= Float.intBitsToFloat(1148518400);
    }

    private boolean magoo8oxviwc() {
        return this.f718gi8fifcn < Float.intBitsToFloat(1137836032);
    }

    private float m2yao9t77lpr() {
        return this.m1lc7eja7fps() || this.magoo8oxviwc() ? Float.intBitsToFloat(0x41400000) : (this.fc0fjka99anh >= Float.intBitsToFloat(1149861888) ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(1103101952));
    }

    private float mc4mqd55b7pt() {
        return Math.min(Float.intBitsToFloat(0x44B40000), this.fc0fjka99anh - this.m2yao9t77lpr() * 2.0f);
    }

    @Override
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.fchakgedxeah.clear();
        this.fchakgedxeah.add(this.fukeosqhtbl);
        this.f1n980i402fz = screenScreenIdService;
        this.fg1qifvr2o4t = this.fe78alkn4oqq.get();
        this.f2lnr7jlwqj7 = new MaterialTerrainTooltipsService();
        ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)this.f2lnr7jlwqj7.id("main-menu.canvas")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).direction(ScenePctService.Direction.NONE)).backgroundColor(MaterialIsLightService.SURFACE_LOWEST).interactive(true);
        this.fds04nrcye91 = new SceneFocusService().backdrop(true).page(this.fukeosqhtbl.ordinal());
        ((SceneFocusService)((SceneFocusService)((SceneFocusService)((SceneFocusService)this.fds04nrcye91.id("main-menu.material")).absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())).pointerEvents(false);
        this.f2lnr7jlwqj7.addChild(this.fds04nrcye91);
        this.fru87gio83d = new ComponentMountService().mount(this::m3hcxq1d01a3, screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        ((LayoutContainerNode)((LayoutContainerNode)this.fru87gio83d.absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.f2lnr7jlwqj7.addChild(this.fru87gio83d);
        this.f2np82eubib2 = new BuiltinStateController(ChangelogRepository.load(), this.fe5fm4nwbka.changelog(), screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        this.f2np82eubib2.viewport(this.fc0fjka99anh, this.f718gi8fifcn);
        this.f2lnr7jlwqj7.addChild(this.f2np82eubib2.node());
        return this.f2lnr7jlwqj7;
    }

    @Override
    public void onOpen(ScreenScreenIdService screenScreenIdService) {
        if (screenScreenIdService != null) {
            this.fak082bn9t7m.reloadSaved();
        }
        this.fa81jdvf5c6w.reload();
        this.m1e1r7v3o7np();
    }

    @Override
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        this.mfpeui8nw9l8();
        this.m4uhdcd0ccgm();
        if (this.f2np82eubib2 != null) {
            this.f2np82eubib2.close();
        }
    }

    @Override
    public void onSuspend(ScreenScreenIdService screenScreenIdService) {
        this.mfpeui8nw9l8();
        this.m4uhdcd0ccgm();
        if (this.f2np82eubib2 != null) {
            this.f2np82eubib2.close();
        }
    }

    private void m1e1r7v3o7np() {
        if (this.fru87gio83d != null) {
            this.fru87gio83d.invalidateComponent();
        }
    }

    private ComponentKeyService<?> m3hcxq1d01a3(ComponentThemeService componentThemeService) {
        this.f2lnr7jlwqj7.backgroundColor(MaterialIsLightService.SURFACE_LOWEST);
        this.fjgtw9rgyyc6.account(this.fg1qifvr2o4t.uuid(), this.fg1qifvr2o4t.name(), true);
        this.fjgtw9rgyyc6.shortHeight(this.magoo8oxviwc());
        this.fjfgkucz72e1.shortHeight(this.magoo8oxviwc());
        this.fjgtw9rgyyc6.width(this.mc4mqd55b7pt() - this.m2yao9t77lpr() * 2.0f);
        this.fjfgkucz72e1.width(this.mc4mqd55b7pt() - this.m2yao9t77lpr() * 2.0f);
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.layout")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.m2yao9t77lpr())).justify(ScenePctService.Justify.CENTER), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.content")).flex(1.0f)).minWidth(0.0f)).maxWidth(Float.intBitsToFloat(0x44B40000))).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(this.magoo8oxviwc() ? Float.intBitsToFloat(0x41000000) : Float.intBitsToFloat(1103101952)), this.m3wa17ou9gy2(), ComponentBoxService.stack(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.pages")).flex(1.0f)).minHeight(0.0f)).minWidth(0.0f)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))), this.m23z7owhx705(componentThemeService, Page.HOME), this.m23z7owhx705(componentThemeService, Page.SERVERS), this.m23z7owhx705(componentThemeService, Page.WORLDS)), this.m3ys1yj7tdv5(false).props(scenePctService -> scenePctService.visible(!this.mc8ne6rdnuf5())), this.mbkdhqxmkbvk().props(scenePctService -> scenePctService.visible((this.mc8ne6rdnuf5() && !this.magoo8oxviwc() ? 1 : 0) != 0))).key("content")).key("menu-layout");
    }

    private ComponentKeyService<?> m23z7owhx705(ComponentThemeService componentThemeService, Page page) {
        ComponentKeyService<?> componentKeyService = switch (page.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> this.m9jd7wxs8wpr();
            case 1 -> this.m55y11i1sk6s(this.fjgtw9rgyyc6.render(componentThemeService)).props(scenePctService -> scenePctService.id("menu.page.servers"));
            case 2 -> this.m55y11i1sk6s(this.fjfgkucz72e1.render(componentThemeService)).props(scenePctService -> scenePctService.id("menu.page.worlds"));
        };
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.view." + String.valueOf((Object)page))).absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())).pointerEvents(this.fukeosqhtbl == page)).visible(this.fchakgedxeah.contains((Object)page))).layerBreak(true), componentKeyService.props(scenePctService -> ((ScenePctService)((ScenePctService)((ScenePctService)scenePctService.flex(1.0f)).minHeight(0.0f)).minWidth(0.0f)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))).key("page:" + String.valueOf((Object)page)).onMount(layoutContainerNode -> {
            layoutContainerNode.opacity(0.0f);
            if (this.fukeosqhtbl == page) {
                MotionAnimateService.animate(layoutContainerNode, MotionColorsContainer.Floats.OPACITY, 1.0f, (SceneEaseHandler)SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1050253722)));
            }
        });
    }

    private ComponentKeyService<?> m3wa17ou9gy2() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.toolbar")).height(LayoutOperationHandler.px(this.magoo8oxviwc() ? Float.intBitsToFloat(0x42400000) : Float.intBitsToFloat(1113587712)))).gap(this.m1lc7eja7fps() ? Float.intBitsToFloat(0x40800000) : Float.intBitsToFloat(0x41400000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), ComponentBoxService.node("ellice-emblem", MaterialLabelService::new, materialLabelService -> ((MaterialLabelService)materialLabelService.shape("clover4").active(true).tint(MaterialIsLightService.PRIMARY).size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1106247680))).pointerEvents(false), new ComponentKeyService[0]), MaterialTextService.text("ellice", Float.intBitsToFloat(1104150528), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.fontFamily("material-roboto-medium").letterSpacing(Float.intBitsToFloat(-1082130432)).visible(this.fc0fjka99anh >= Float.intBitsToFloat(1136525312))), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f), new ComponentKeyService[0]), this.m3ys1yj7tdv5(true).props(scenePctService -> scenePctService.visible(this.mc8ne6rdnuf5())), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).visible(this.mc8ne6rdnuf5()), new ComponentKeyService[0]), MaterialTextService.button("menu.accounts", this.fg1qifvr2o4t.name(), this.fe5fm4nwbka.accounts(), ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(0x3F666666)), MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1103101952)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.maxWidth(this.m1lc7eja7fps() ? Float.intBitsToFloat(0x42400000) : Float.intBitsToFloat(1129447424))).minWidth(Float.intBitsToFloat(0x42400000))).padding(0.0f, this.m1lc7eja7fps() ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(0x41400000))).blur(Float.intBitsToFloat(1099956224)).tooltip("Manage accounts & favourites")).children(this.mgorlin1xta(), MaterialTextService.label(this.fg1qifvr2o4t.name(), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.minWidth(0.0f)).flex(1.0f)).visible(!this.m1lc7eja7fps())), MaterialTextService.icon("expand_more", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> ((SceneSrcService)sceneSrcService.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))).visible(!this.m1lc7eja7fps()))), MaterialTextService.iconButton("menu.appearance", "palette", "Appearance & performance", this::m9fb9o7fqgkp), MaterialTextService.iconButton("menu.tools", "tune", "Client tools & settings", this::mhz4et2ynkqk)).key("toolbar");
    }

    private ComponentKeyService<?> mgorlin1xta() {
        String string = "mc.head." + String.valueOf(this.fg1qifvr2o4t.uuid());
        boolean bl = CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().compositor().getNamedTexture(string) != null && CoreIsInitializedHandler.get().compositor().getNamedTexture(string).valid();
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.size(Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1105199104))).flexShrink(0.0f)).cornerRadius(Float.intBitsToFloat(1096810496)).backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER).direction(ScenePctService.Direction.NONE)).pointerEvents(false), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1105199104))).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).visible(!bl), MaterialTextService.text(this.fg1qifvr2o4t.name(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_PRIMARY_CONTAINER).props(sceneTextService -> sceneTextService.initial(true))), ComponentBoxService.node("menu-avatar", SceneTextureService::new, sceneTextureService -> ((SceneTextureService)sceneTextureService.textureName(string).cornerRadius(Float.intBitsToFloat(1096810496)).size(Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1105199104))).visible(bl), new ComponentKeyService[0])).key("avatar:" + String.valueOf(this.fg1qifvr2o4t.uuid()));
    }

    private ComponentKeyService<?> m3ys1yj7tdv5(boolean bl) {
        return ComponentBoxService.node("menu-navigation-surface", SceneSelectionSelector::new, sceneSelectionSelector -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)sceneSelectionSelector.selection(this.fukeosqhtbl.ordinal()).id(bl ? "menu.navigation" : "menu.bottom-nav")).direction(ScenePctService.Direction.ROW)).height(LayoutOperationHandler.px(Float.intBitsToFloat(1112539136)))).width(bl ? LayoutOperationHandler.px(Float.intBitsToFloat(1135869952)) : LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flexShrink(0.0f)).padding(Float.intBitsToFloat(0x40800000))).gap(2.0f)).align(ScenePctService.Align.CENTER), this.m13bb1k09wtz(Page.HOME, "Home", bl), this.m13bb1k09wtz(Page.SERVERS, "Servers", bl), this.m13bb1k09wtz(Page.WORLDS, "Worlds", bl)).key(bl ? "top-navigation" : "bottom-navigation");
    }

    private ComponentKeyService<?> m13bb1k09wtz(Page page, String string, boolean bl) {
        int n = this.fukeosqhtbl == page ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT;
        return MaterialTextService.button("menu.nav." + (bl ? "top." : "bottom.") + page.name().toLowerCase(Locale.ROOT), string, () -> this.page(page), 0, n, Float.intBitsToFloat(1103101952)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.width(LayoutOperationHandler.auto())).height(LayoutOperationHandler.px(Float.intBitsToFloat(1110441984)))).flex(1.0f)).minWidth(0.0f)).flexShrink(1.0f)).padding(0.0f, Float.intBitsToFloat(0x41000000))).children(MaterialTextService.label(string, n));
    }

    private ComponentKeyService<?> mbkdhqxmkbvk() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.footer")).height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.button("menu.changelog", "ellice " + CoreIsInitializedHandler.VERSION, this.fe5fm4nwbka.changelog(), 0, MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1099956224)).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).padding(0.0f, Float.intBitsToFloat(0x40800000))).children(MaterialTextService.text("ellice " + CoreIsInitializedHandler.VERSION, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT)), MaterialTextService.text("/  Minecraft " + this.f4fudvbq0rtu, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f), new ComponentKeyService[0]), MaterialTextService.button("menu.modules", "Modules", this.fe5fm4nwbka.modules(), 0, MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1099956224)).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.padding(0.0f, Float.intBitsToFloat(0x41400000))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))), MaterialTextService.iconButton("menu.network", "swap_horiz", "FRITZ!Box \u00b7 Reconnect", this::m6jvoseevjdh).props(materialJoinedService -> materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))), MaterialTextService.iconButton("menu.options", "settings", "Minecraft settings", this.fe5fm4nwbka.options()).props(materialJoinedService -> materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))), MaterialTextService.iconButton("menu.quit", "power_settings_new", "Quit Minecraft", this.fe5fm4nwbka.quit()).props(materialJoinedService -> materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))))).key("footer");
    }

    private void mhz4et2ynkqk() {
        if (this.f31gsm0clfwn != null) {
            return;
        }
        this.m4uhdcd0ccgm();
        this.fru87gio83d.pointerEvents(false);
        this.f1lghizzt89u = new ComponentMountService().mount(this::mh6ycx3dkim1, this.f1n980i402fz == null ? new ThemeIsSetService() : this.f1n980i402fz.theme());
        ((LayoutContainerNode)((LayoutContainerNode)this.f1lghizzt89u.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1136525312))).maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.f31gsm0clfwn = ComponentUnmountService.mountCentered(this.f2lnr7jlwqj7, this.f1lghizzt89u, Float.intBitsToFloat(1098907648), this::mfpeui8nw9l8);
        this.f31gsm0clfwn.id("menu.tools.layer");
        ((SceneCornerRadiusService)this.f31gsm0clfwn.children().getFirst()).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_LOWEST, Float.intBitsToFloat(1047904911))).blur(Float.intBitsToFloat(0x41400000));
        MaterialEnterService.enter(this.f1lghizzt89u, 0.0f, Float.intBitsToFloat(1098907648));
    }

    private ComponentKeyService<?> mh6ycx3dkim1(ComponentThemeService componentThemeService) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("menu.tools.sheet")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Math.min(Float.intBitsToFloat(1136525312), this.f718gi8fifcn - Float.intBitsToFloat(0x42000000))))).direction(ScenePctService.Direction.COLUMN)).padding(Float.intBitsToFloat(0x41400000))).gap(Float.intBitsToFloat(0x41000000))).cornerRadius(Float.intBitsToFloat(1105199104)).backgroundColor(MaterialIsLightService.SURFACE), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(Float.intBitsToFloat(0x42400000)))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.text("Make it yours", Float.intBitsToFloat(1102053376), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)), MaterialTextService.iconButton("menu.tools.close", "close", "Close \u00b7 Esc", this::mfpeui8nw9l8)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minHeight(0.0f)).scrollable(true)).clip(true)).gap(Float.intBitsToFloat(0x40800000)), this.mfwbq7ti0vuv("appearance", "palette", "Appearance & performance", this::m9fb9o7fqgkp), this.mfwbq7ti0vuv("modules", "tune", "ellice modules", this.fe5fm4nwbka.modules()), this.mfwbq7ti0vuv("network", "swap_horiz", "Network tools", this::m6jvoseevjdh), this.mfwbq7ti0vuv("options", "settings", "Minecraft settings", this.fe5fm4nwbka.options()), this.mfwbq7ti0vuv("quit", "power_settings_new", "Quit Minecraft", this.fe5fm4nwbka.quit())));
    }

    private ComponentKeyService<?> mfwbq7ti0vuv(String string, String string2, String string3, Runnable runnable) {
        return MaterialTextService.button("menu.tools." + string, string3, () -> {
            this.mfpeui8nw9l8();
            runnable.run();
        }, MaterialIsLightService.SURFACE_LOW, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1099956224)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(0x42400000)))).padding(0.0f, Float.intBitsToFloat(0x41400000))).justify(ScenePctService.Justify.START)).children(MaterialTextService.icon(string2, MaterialIsLightService.ON_SURFACE_VARIANT), MaterialTextService.label(string3, MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)));
    }

    private void mfpeui8nw9l8() {
        if (this.f31gsm0clfwn == null) {
            return;
        }
        this.m4uhdcd0ccgm();
        ComponentUnmountService.unmount(this.f2lnr7jlwqj7, this.f31gsm0clfwn);
        this.f31gsm0clfwn = null;
        this.f1lghizzt89u = null;
        if (this.fru87gio83d != null) {
            this.fru87gio83d.pointerEvents(true);
        }
    }

    private void m9fb9o7fqgkp() {
        if (this.f1n980i402fz != null) {
            this.f1n980i402fz.open("client-settings");
        }
    }

    private void m6jvoseevjdh() {
        if (this.f1n980i402fz != null) {
            this.f1n980i402fz.open("fritz-box");
        }
    }

    private ComponentKeyService<?> m55y11i1sk6s(ComponentKeyService<?> componentKeyService) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("menu.page")).direction(ScenePctService.Direction.COLUMN)).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_LOW, Float.intBitsToFloat(1064682127))).blur(Float.intBitsToFloat(1103101952)).cornerRadius(Float.intBitsToFloat(1105199104)).padding(this.m2yao9t77lpr())).clip(true), componentKeyService.props(scenePctService -> ((ScenePctService)scenePctService.flex(1.0f)).minHeight(0.0f)));
    }

    private ComponentKeyService<?> m9jd7wxs8wpr() {
        int n;
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        int n2 = n = this.mc4mqd55b7pt() >= Float.intBitsToFloat(1149861888) ? 3 : 2;
        if (this.f68m7h1knt6z) {
            for (MenuLoaderTracker.World world : this.fa81jdvf5c6w.worlds().stream().limit(n).toList()) {
                arrayList.add(this.mfrvvrsa9yib(world));
            }
            if (arrayList.isEmpty()) {
                arrayList.add(this.m78z366yhubt("Room for a new world", this.fa81jdvf5c6w.loading() ? "Finding your saves\u2026" : "Build a place of your own.", "Create world", this.fe5fm4nwbka.createWorld(), "landscape"));
            }
        } else {
            List<Server> list = this.m2u7kjcok1b().stream().filter(server -> server.usage().favorite()).toList();
            for (Server server2 : (list.isEmpty() ? this.m2u7kjcok1b() : list).stream().limit(n).toList()) {
                arrayList.add(this.mc3hoou073r(server2));
            }
            if (arrayList.isEmpty()) {
                arrayList.add(this.m78z366yhubt("Keep good company", "Your favourite servers will appear here.", "Browse servers", () -> this.page(Page.SERVERS), "dns"));
            }
        }
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.home.scroll")).gap(this.magoo8oxviwc() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1105199104))).scrollable(true)).clip(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)).padding(0.0f, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x41400000), 0.0f), this.mfq3dcysh44y().props(scenePctService -> scenePctService.flexShrink(0.0f)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.home.libraries")).gap(Float.intBitsToFloat(0x41400000))).flexShrink(0.0f), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.gap(Float.intBitsToFloat(0x40800000))).align(ScenePctService.Align.CENTER), this.m6u3p6g2m57a("Saved servers", false), this.m6u3p6g2m57a("Your worlds", true), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f), new ComponentKeyService[0]), MaterialTextService.iconButton("menu.shelf.all", "chevron_right", this.f68m7h1knt6z ? "All worlds" : "All servers", () -> this.page(this.f68m7h1knt6z ? Page.WORLDS : Page.SERVERS))), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.direction(this.mc4mqd55b7pt() >= Float.intBitsToFloat(1144913920) ? ScenePctService.Direction.ROW : ScenePctService.Direction.COLUMN)).gap(Float.intBitsToFloat(0x41400000)), (ComponentKeyService[])arrayList.stream().map(componentKeyService -> componentKeyService.props(scenePctService -> ((ScenePctService)scenePctService.flex(this.mc4mqd55b7pt() >= Float.intBitsToFloat(1144913920) ? 1.0f : 0.0f)).minWidth(0.0f))).toArray(ComponentKeyService[]::new)))).onMount(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.scrollY(this.fe0404fx1874)).scrollTarget(this.fe0404fx1874));
    }

    private ComponentKeyService<?> m6u3p6g2m57a(String string, boolean bl) {
        int n = this.f68m7h1knt6z == bl ? 1 : 0;
        return MaterialTextService.button("menu.shelf." + (bl ? "worlds" : "servers"), string, () -> {
            this.f68m7h1knt6z = bl;
            this.m1e1r7v3o7np();
        }, n != 0 ? ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(1064346583)) : 0, n != 0 ? MaterialIsLightService.ON_SURFACE : MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1101004800)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.padding(0.0f, this.m1lc7eja7fps() ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(1098907648))).minWidth(0.0f)).flexShrink(1.0f));
    }

    private ComponentKeyService<?> mfq3dcysh44y() {
        int n;
        boolean bl;
        MenuLoaderTracker.World world = this.fa81jdvf5c6w.worlds().stream().filter(MenuLoaderTracker.World::canPlay).max(Comparator.comparingLong(MenuLoaderTracker.World::lastPlayed)).orElse(null);
        Server server2 = this.m2u7kjcok1b().stream().filter(server -> server.usage().lastJoined() > 0L).max(Comparator.comparingLong(server -> server.usage().lastJoined())).orElse(null);
        int n2 = server2 != null && (world == null || server2.usage().lastJoined() > world.lastPlayed()) ? 1 : 0;
        boolean bl2 = bl = world != null || server2 != null;
        String selectedWorldName = bl ? (n2 != 0 ? server2.name() : world.name()) : "Create your first world";
        Runnable runnable = bl ? (n2 != 0 ? () -> this.fe5fm4nwbka.connect().accept(server2.address(), server2.name()) : () -> this.fe5fm4nwbka.playWorld().accept(world)) : this.fe5fm4nwbka.createWorld();
        int n3 = n = this.f718gi8fifcn < Float.intBitsToFloat(1134559232) ? 1 : 0;
        if (n != 0) {
            return this.m4gcqv1otv56(selectedWorldName, runnable, bl, true).key("stage-tiny");
        }
        float f = this.magoo8oxviwc() ? Float.intBitsToFloat(1126957056) : (this.f718gi8fifcn >= Float.intBitsToFloat(1143930880) ? Float.intBitsToFloat(1136525312) : Float.intBitsToFloat(1134559232));
        boolean bl3 = this.mc4mqd55b7pt() >= Float.intBitsToFloat(1144913920);
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("menu.stage")).height(LayoutOperationHandler.px(f))).gap(Float.intBitsToFloat(1103101952))).align(ScenePctService.Align.CENTER), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(bl3 ? LayoutOperationHandler.px(Math.min(Float.intBitsToFloat(0x440C0000), this.mc4mqd55b7pt() * Float.intBitsToFloat(1057300152))) : LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minWidth(0.0f)).gap(this.magoo8oxviwc() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1101004800))).padding(0.0f, this.m1lc7eja7fps() ? Float.intBitsToFloat(0x40800000) : Float.intBitsToFloat(0x41400000))).flexShrink(0.0f), MaterialTextService.text("YOUR TIME. YOUR WORLD.", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.letterSpacing(Float.intBitsToFloat(1072064102)).visible(!this.magoo8oxviwc())), MaterialTextService.text(this.magoo8oxviwc() ? "Ready to play?" : "Make time\nfor play.", this.magoo8oxviwc() ? Float.intBitsToFloat(1106247680) : (bl3 ? Float.intBitsToFloat(1115684864) : Float.intBitsToFloat(1109917696)), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.fontFamily("material-roboto-medium").lineHeight(bl3 ? Float.intBitsToFloat(1116209152) : Float.intBitsToFloat(0x42400000)).height(LayoutOperationHandler.px(bl3 ? Float.intBitsToFloat(1124597760) : Float.intBitsToFloat(1119879168)))).flexShrink(0.0f)).letterSpacing(Float.intBitsToFloat(-1073741824)).wordWrap(true).maxLines(2).visible(this.f718gi8fifcn >= Float.intBitsToFloat(1137836032))), this.m4gcqv1otv56(selectedWorldName, runnable, bl, false), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).visible(!this.magoo8oxviwc()), MaterialTextService.button("menu.launch.create-world", "New world", this.fe5fm4nwbka.createWorld(), 0, MaterialIsLightService.PRIMARY, Float.intBitsToFloat(1101004800)).props(materialJoinedService -> materialJoinedService.padding(0.0f, Float.intBitsToFloat(0x41000000))).children(MaterialTextService.icon("add", MaterialIsLightService.PRIMARY).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))), MaterialTextService.label("New world", MaterialIsLightService.PRIMARY)), MaterialTextService.button("menu.hero.servers", "Explore servers", () -> this.page(Page.SERVERS), 0, MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1101004800)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.padding(0.0f, Float.intBitsToFloat(0x41000000))).minWidth(0.0f)).flexShrink(1.0f)))).key("stage-copy").onMount(layoutContainerNode -> {
            layoutContainerNode.translateY(Float.intBitsToFloat(1096810496));
            MotionAnimateService.animate(layoutContainerNode, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0f, (SceneEaseHandler)new SceneEaseHandler.Spring(Float.intBitsToFloat(1106771968), Float.intBitsToFloat(1128792064)), Float.intBitsToFloat(1027101164));
        }), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).visible(bl3), new ComponentKeyService[0]).key("sculpture-space")).key("stage");
    }

    private ComponentKeyService<?> m4gcqv1otv56(String string, Runnable runnable, boolean bl, boolean bl2) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("menu.resume")).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).height(LayoutOperationHandler.px(bl2 ? Float.intBitsToFloat(1118306304) : Float.intBitsToFloat(1118568448)))).padding(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1099956224))).gap(Float.intBitsToFloat(0x41400000))).cornerRadius(Float.intBitsToFloat(1104150528)).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(0x3F666666))).blur(Float.intBitsToFloat(1101004800)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(Float.intBitsToFloat(0x40800000)), MaterialTextService.text(bl ? "CONTINUE PLAYING" : "A FRESH START", Float.intBitsToFloat(1092616192), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.letterSpacing(1.0f)), MaterialTextService.text(string, bl2 ? Float.intBitsToFloat(1099956224) : Float.intBitsToFloat(1101004800), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.id("menu.resume.title")).fontFamily("material-roboto-medium"))), MaterialTextService.button("menu.resume.play", bl ? "Play" : "Create", runnable, MaterialIsLightService.PRIMARY, MaterialIsLightService.ON_PRIMARY, Float.intBitsToFloat(1103101952)).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.padding(0.0f, bl2 ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1098907648))).minWidth(0.0f)).tooltip((bl ? "Continue playing \u00b7 " : "Create \u00b7 ") + string)).onHoverChange(hovered -> {
            if (this.fds04nrcye91 != null) {
                this.fds04nrcye91.focus(hovered);
            }
        })).children(MaterialTextService.icon("play_arrow", MaterialIsLightService.ON_PRIMARY).props(sceneSrcService -> ((SceneSrcService)sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800))).visible(!bl2)), MaterialTextService.label(bl ? "Play" : "Create", MaterialIsLightService.ON_PRIMARY)));
    }

    private ComponentKeyService<?> mc3hoou073r(Server server) {
        int n;
        ServerViewTracker.View view = this.fak082bn9t7m.view(server.address());
        int n2 = n = view.reachable() && view.status() != null ? 1 : 0;
        String string = view.checkedAt() == 0L ? "Checking status\u2026" : (n != 0 ? (view.status().players() < 0 ? "Online" : String.format(Locale.ROOT, "%,d online", view.status().players())) + (String)(view.status().latencyMs() < 0L ? "" : " \u00b7 " + view.status().latencyMs() + " ms") : "Status unavailable");
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("menu.server." + server.address())).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).gap(Float.intBitsToFloat(0x40800000))).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(1064011039))).blur(Float.intBitsToFloat(1099956224)).cornerRadius(Float.intBitsToFloat(1102053376)).padding(Float.intBitsToFloat(0x40800000))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1117257728))), ComponentBoxService.node("home-server-select", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1116209152));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id("menu.server.details." + server.address())).height(LayoutOperationHandler.px(Float.intBitsToFloat(1116209152)))).flex(1.0f)).minWidth(0.0f)).padding(Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x41400000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).onClick(() -> this.fe5fm4nwbka.preview().accept(server.address(), server.name()));
        }, ComponentBoxService.node("home-server-icon", SceneImageService::new, sceneImageService -> ((SceneImageService)((SceneImageService)sceneImageService.identity(server.address(), server.name()).image(this.fak082bn9t7m.icon(server.address())).material(true).size(Float.intBitsToFloat(0x42400000), Float.intBitsToFloat(0x42400000))).flexShrink(0.0f)).pointerEvents(false), new ComponentKeyService[0]), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(Float.intBitsToFloat(0x40400000)), MaterialTextService.text(server.name(), Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE), MaterialTextService.text(string, Float.intBitsToFloat(0x41400000), n != 0 ? MaterialIsLightService.TERTIARY : MaterialIsLightService.ON_SURFACE_VARIANT))), MaterialTextService.iconButton("menu.server.join." + server.address(), "play_arrow", "Join " + server.name(), () -> this.fe5fm4nwbka.connect().accept(server.address(), server.name())).props(materialJoinedService -> materialJoinedService.marginRight(Float.intBitsToFloat(0x40800000)))).key("home-server:" + server.address());
    }

    private ComponentKeyService<?> mfrvvrsa9yib(MenuLoaderTracker.World world) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("menu.world." + world.id())).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(1064011039))).blur(Float.intBitsToFloat(1099956224)).cornerRadius(Float.intBitsToFloat(1102053376)).padding(Float.intBitsToFloat(0x40800000))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1117257728))), ComponentBoxService.node("home-world-select", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1116209152));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id("menu.world.details." + world.id())).height(LayoutOperationHandler.px(Float.intBitsToFloat(1116209152)))).flex(1.0f)).minWidth(0.0f)).padding(Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x41400000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).onClick(() -> {
                this.fjfgkucz72e1.select(world.id());
                this.page(Page.WORLDS);
            });
        }, WorldListRenderer.thumbnail(world, Float.intBitsToFloat(0x42400000)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(Float.intBitsToFloat(0x40400000)), MaterialTextService.text(world.name(), Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE), MaterialTextService.text((world.hardcore() ? "Hardcore" : world.mode()) + " \u00b7 " + WorldListRenderer.lastPlayed(world.lastPlayed()), Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT))), MaterialTextService.iconButton("menu.world.play." + world.id(), "play_arrow", world.playLabel(), () -> {
            if (world.canPlay()) {
                this.fe5fm4nwbka.playWorld().accept(world);
            }
        }).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.available(world.canPlay()).marginRight(Float.intBitsToFloat(0x40800000))).tooltip(world.canPlay() ? world.playLabel() : world.info()))).key("home-world:" + world.id());
    }

    private ComponentKeyService<?> m78z366yhubt(String string, String string2, String string3, Runnable runnable, String string4) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN)).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(1064346583))).blur(Float.intBitsToFloat(1099956224)).cornerRadius(Float.intBitsToFloat(1103101952)).padding(Float.intBitsToFloat(1101004800))).gap(Float.intBitsToFloat(1092616192)), MaterialTextService.icon(string4, MaterialIsLightService.PRIMARY), MaterialTextService.text(string, Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.text(string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.button("menu.empty." + string4, string3, runnable, false));
    }

    private List<Server> m2u7kjcok1b() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        for (CompatSavedService.Saved object : this.fak082bn9t7m.saved()) {
            linkedHashMap.put(object.address(), object.name());
        }
        for (String string : this.fak082bn9t7m.library().history(this.fg1qifvr2o4t.uuid()).keySet()) {
            linkedHashMap.putIfAbsent(string, this.fak082bn9t7m.library().name(string));
        }
        return linkedHashMap.entrySet().stream().map(entry -> new Server(entry.getKey(), entry.getValue(), this.fak082bn9t7m.library().usage(this.fg1qifvr2o4t.uuid(), entry.getKey()))).sorted(Comparator.<Server, Boolean>comparing(server -> !server.usage().favorite()).thenComparing(Comparator.comparingLong((Server server) -> server.usage().lastJoined()).reversed()).thenComparing(Server::name)).toList();
    }

    @Override
    public void tick(ScreenScreenIdService screenScreenIdService) {
        if (this.f2np82eubib2 != null) {
            this.f2np82eubib2.tick();
        }
        if (screenScreenIdService != null) {
            DisplayMetrics displayMetrics = CoreIsInitializedHandler.get().viewport();
            this.viewport(displayMetrics.width(), displayMetrics.height());
        }
        Session session = this.fe78alkn4oqq.get();
        if (!session.equals(this.fg1qifvr2o4t)) {
            this.fg1qifvr2o4t = session;
            this.m1e1r7v3o7np();
        }
        this.fa81jdvf5c6w.tick();
        if (screenScreenIdService != null) {
            this.fjgtw9rgyyc6.tick(this.fukeosqhtbl == Page.SERVERS);
            long l = System.currentTimeMillis();
            if (this.fukeosqhtbl == Page.HOME && l >= this.fcq8mwkebm7e) {
                this.fcq8mwkebm7e = l + 250L;
                for (Server server : this.m2u7kjcok1b().stream().limit(3L).toList()) {
                    this.fak082bn9t7m.seedIcon(server.address(), null);
                    this.fak082bn9t7m.request(server.address(), false);
                }
            }
        }
        if (this.fhh18s83d95g != this.fak082bn9t7m.revision() || this.faos4ztjpj8t != this.fa81jdvf5c6w.revision()) {
            this.fhh18s83d95g = this.fak082bn9t7m.revision();
            this.faos4ztjpj8t = this.fa81jdvf5c6w.revision();
            this.m1e1r7v3o7np();
        }
    }

    @Override
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
        MaterialJoinedService materialJoinedService;
        Object object;
        if (n == 256) {
            if (this.f2np82eubib2 != null && this.f2np82eubib2.isOpen()) {
                this.f2np82eubib2.close();
                return true;
            }
            if (this.f31gsm0clfwn != null) {
                this.mfpeui8nw9l8();
            } else {
                this.page(Page.HOME);
            }
            return true;
        }
        if (this.f31gsm0clfwn == null && n == 70 && (n2 & 0xA) != 0) {
            ScenePctService<?> scenePctService2;
            if (this.fukeosqhtbl == Page.HOME) {
                this.page(Page.SERVERS);
            }
            if ((scenePctService2 = this.f2lnr7jlwqj7.findById(this.fukeosqhtbl == Page.SERVERS ? "servers.search" : "worlds.search")) instanceof ControlLetterSpacingService) {
                ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService2;
                this.m4uhdcd0ccgm();
                BuiltinActionsTracker.mc4tbxd03cn3(controlLetterSpacingService);
                controlLetterSpacingService.selectAll();
                BuiltinActionsTracker.m1mxp3gzr5pw(controlLetterSpacingService);
            }
            return true;
        }
        if (n == 258) {
            ArrayList<ScenePctService<?>> arrayList = new ArrayList<>();
            BuiltinActionsTracker.m2kdgwdvv5ta(this.f31gsm0clfwn == null ? this.f2lnr7jlwqj7 : this.f31gsm0clfwn, arrayList);
            if (!arrayList.isEmpty()) {
                ScenePctService scenePctService3;
                ScenePctService<?> scenePctService4 = this.fic1uu0899v0;
                for (ScenePctService<?> scenePctService5 : (Iterable<ScenePctService<?>>) (Iterable<?>) (arrayList)) {
                    if (!(scenePctService5 instanceof ControlLetterSpacingService) || !((ControlLetterSpacingService)(scenePctService3 = (ControlLetterSpacingService)scenePctService5)).focused()) continue;
                    scenePctService4 = scenePctService5;
                }
                int n3 = arrayList.indexOf(scenePctService4);
                int n4 = (n2 & 1) != 0 ? 1 : 0;
                this.m4uhdcd0ccgm();
                ScenePctService<?> scenePctService6 = this.fic1uu0899v0 = arrayList.get(n3 < 0 ? (n4 != 0 ? arrayList.size() - 1 : 0) : Math.floorMod(n3 + (n4 != 0 ? -1 : 1), arrayList.size()));
                if (scenePctService6 instanceof MaterialJoinedService) {
                    scenePctService3 = (MaterialJoinedService)scenePctService6;
                    ((MaterialJoinedService)scenePctService3).keyboardFocused(true);
                }
                if ((scenePctService6 = this.fic1uu0899v0) instanceof ControlLetterSpacingService) {
                    scenePctService3 = (ControlLetterSpacingService)scenePctService6;
                    BuiltinActionsTracker.mc4tbxd03cn3((ControlLetterSpacingService)scenePctService3);
                }
                BuiltinActionsTracker.m1mxp3gzr5pw(this.fic1uu0899v0);
            }
            return true;
        }
        if ((n == 257 || n == 32) && (object = this.fic1uu0899v0) instanceof MaterialJoinedService && (materialJoinedService = (MaterialJoinedService)object).available()) {
            ArrayList<ScenePctService<?>> focusableNodes = new ArrayList<>();
            BuiltinActionsTracker.m2kdgwdvv5ta(this.f31gsm0clfwn == null ? this.f2lnr7jlwqj7 : this.f31gsm0clfwn, focusableNodes);
            if (focusableNodes.stream().anyMatch(scenePctService -> {
                ControlLetterSpacingService controlLetterSpacingService;
                return (scenePctService instanceof ControlLetterSpacingService && (controlLetterSpacingService = (ControlLetterSpacingService)scenePctService).focused() ? 1 : 0) != 0;
            })) {
                return false;
            }
            if (focusableNodes.contains(materialJoinedService)) {
                materialJoinedService.activate();
                return true;
            }
        }
        return false;
    }

    private void m4uhdcd0ccgm() {
        Object object;
        ScenePctService<?> scenePctService = this.fic1uu0899v0;
        if (scenePctService instanceof MaterialJoinedService) {
            object = (MaterialJoinedService)scenePctService;
            ((MaterialJoinedService)object).keyboardFocused(false);
        }
        this.fic1uu0899v0 = null;
        ArrayList<ScenePctService<?>> focusableNodes = new ArrayList<>();
        BuiltinActionsTracker.m2kdgwdvv5ta(this.f31gsm0clfwn == null ? this.f2lnr7jlwqj7 : this.f31gsm0clfwn, focusableNodes);
        for (ScenePctService<?> scenePctService2 : focusableNodes) {
            if (!(scenePctService2 instanceof ControlLetterSpacingService)) continue;
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService2;
            controlLetterSpacingService.unfocus();
        }
        if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
            CoreIsInitializedHandler.get().scene().clearFocus();
        }
    }

    private static void mc4tbxd03cn3(ControlLetterSpacingService controlLetterSpacingService) {
        if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
            CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacingService);
        } else {
            controlLetterSpacingService.focus();
        }
    }

    private static void m2kdgwdvv5ta(ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
        Object object;
        if (scenePctService == null || !scenePctService.visible || !scenePctService.pointerEvents()) {
            return;
        }
        if (scenePctService instanceof MaterialJoinedService && ((MaterialJoinedService)(object = (MaterialJoinedService)scenePctService)).available() || scenePctService instanceof ControlLetterSpacingService) {
            list.add(scenePctService);
        }
        for (ScenePctService scenePctService2 : scenePctService.children()) {
            BuiltinActionsTracker.m2kdgwdvv5ta(scenePctService2, list);
        }
    }

    private static void m1mxp3gzr5pw(ScenePctService<?> scenePctService) {
        for (ScenePctService<?> scenePctService2 = scenePctService.parent(); scenePctService2 != null; scenePctService2 = scenePctService2.parent()) {
            if (scenePctService2.scrollMin() >= 0.0f) continue;
            float scrollDelta = scenePctService.computedY() < scenePctService2.computedY() + Float.intBitsToFloat(0x40C00000) ? scenePctService2.computedY() + Float.intBitsToFloat(0x40C00000) - scenePctService.computedY() : (scenePctService.computedY() + scenePctService.computedH() > scenePctService2.computedY() + scenePctService2.computedH() - Float.intBitsToFloat(0x40C00000) ? scenePctService2.computedY() + scenePctService2.computedH() - Float.intBitsToFloat(0x40C00000) - scenePctService.computedY() - scenePctService.computedH() : 0.0f);
            if (scrollDelta == 0.0f) continue;
            float f3 = Math.max(scenePctService2.scrollMin(), Math.min(0.0f, scenePctService2.scrollY() + scrollDelta));
            scenePctService2.scrollTarget(f3);
            MotionAnimateService.animate(scenePctService2, MotionColorsContainer.Floats.SCROLL_Y, f3, (SceneEaseHandler)MaterialIsLightService.FAST_SPATIAL);
        }
    }

    public static enum Page {
        HOME,
        SERVERS,
        WORLDS;

    }

    public record Actions(BiConsumer<String, String> connect, BiConsumer<String, String> preview, Consumer<MenuLoaderTracker.World> playWorld, Consumer<MenuLoaderTracker.World> editWorld, Runnable createWorld, Runnable accounts, Runnable options, Runnable modules, Runnable changelog, Runnable quit) {
    }

    public record Session(UUID uuid, String name) {
    }

    private record Server(String address, String name, ServerKeyService.Usage usage) {
    }
}

