


package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.anticheat.AnticheatCodec;
import dev.felix.ellice.anticheat.AnticheatRepository;
import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.net.URI;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;

public final class AnticheatScreen
implements ScreenOperationHandler {
    private final AnticheatRepository f430kf6cmfsn;
    private final Consumer<URI> f3u4paxch97m;
    private final MaterialKeyService f4wtk1ybec86 = new MaterialKeyService();
    private AnticheatRepository.View f7azho04q9ul;
    private ScreenScreenIdService fasnmfavrpfs;
    private MaterialTerrainTooltipsService f4uz450c426t;
    private ComponentMountService f5o8mc61d2fs;
    private float f6ehsgfyeiz = Float.intBitsToFloat(1150025728);
    private float fgvjnn3sg1bz = Float.intBitsToFloat(0x44340000);
    private String fcia1l1g5y8c = "";
    private String fa2cluiy38x9 = "";
    private String fc4b1nc8u9db = "";
    private boolean fczhtab2khzf;
    private AnticheatCodec.Status fjdlqe8byl8m;
    private AnticheatCodec.Sort f4sf9qpnsqqo = AnticheatCodec.Sort.NAME;
    private boolean f3q4bpm1g7ab;
    private Menu f4kqvxi0zdgp = Menu.NONE;

    public AnticheatScreen(AnticheatRepository anticheatRepository) {
        this(anticheatRepository, CompatOpenUriService::openUri);
    }

    public AnticheatScreen(AnticheatRepository anticheatRepository, Consumer<URI> consumer) {
        this.f430kf6cmfsn = Objects.requireNonNull(anticheatRepository);
        this.f3u4paxch97m = Objects.requireNonNull(consumer);
        this.f7azho04q9ul = anticheatRepository.view();
    }

    @Override
    public String id() {
        return "anticheats";
    }

    @Override
    public boolean overlaysPreviousScreen() {
        return true;
    }

    @Override
    public boolean opensAsWindow() {
        return true;
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

    @Override
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.fasnmfavrpfs = screenScreenIdService;
        this.f4uz450c426t = new MaterialTerrainTooltipsService();
        ((SceneCornerRadiusService)((SceneCornerRadiusService)this.f4uz450c426t.id("anticheats.root")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).backgroundColor(0x52000000);
        this.f5o8mc61d2fs = new ComponentMountService().mount(this::m4ioz3di1w7y, screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        ((LayoutContainerNode)((LayoutContainerNode)this.f5o8mc61d2fs.absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.f4uz450c426t.addChild(this.f5o8mc61d2fs);
        return this.f4uz450c426t;
    }

    public void viewport(float f, float f2) {
        if (this.f6ehsgfyeiz == f && this.fgvjnn3sg1bz == f2) {
            return;
        }
        this.f6ehsgfyeiz = f;
        this.fgvjnn3sg1bz = f2;
        this.m428ucdto9gj();
    }

    private boolean mfjzyzgupop1() {
        return this.f6ehsgfyeiz < Float.intBitsToFloat(1142292480);
    }

    private boolean mjmyt544a29v() {
        return this.fgvjnn3sg1bz < Float.intBitsToFloat(1137180672);
    }

    private boolean md2jp62g9lgj() {
        return this.f6ehsgfyeiz >= Float.intBitsToFloat(1147207680);
    }

    private void m428ucdto9gj() {
        if (this.f5o8mc61d2fs != null) {
            this.f5o8mc61d2fs.invalidateComponent();
        }
    }

    private void mg5qa0jj6zx4() {
        this.fc4b1nc8u9db = "";
        this.m428ucdto9gj();
        ScenePctService<?> scenePctService = this.f4uz450c426t.findById("anticheats.list");
        if (scenePctService != null) {
            ((ScenePctService)scenePctService.scrollY(0.0f)).scrollTarget(0.0f);
        }
    }

    private ComponentKeyService<?> m4ioz3di1w7y(ComponentThemeService componentThemeService) {
        ComponentKeyService[] componentKeyServiceArray;
        List<AnticheatCodec.Entry> list = this.f7azho04q9ul.entries().stream().filter(entry -> entry.matches(this.fcia1l1g5y8c, this.fa2cluiy38x9, this.fczhtab2khzf, this.fjdlqe8byl8m)).sorted(this.f4sf9qpnsqqo.comparator(this.f3q4bpm1g7ab)).toList();
        Consumer<LayoutContainerNode> consumer = layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.mfjzyzgupop1() ? 0.0f : Float.intBitsToFloat(1098907648))).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).pointerEvents(this.f4kqvxi0zdgp == Menu.NONE);
        ComponentKeyService[] componentKeyServiceArray2 = new ComponentKeyService[1];
        Consumer<SceneCornerRadiusService> consumer2 = sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("anticheats.workspace")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1150353408))).maxHeight(Float.intBitsToFloat(1146552320))).cornerRadius(this.mfjzyzgupop1() ? 0.0f : Float.intBitsToFloat(1105199104)).backgroundColor(MaterialIsLightService.SURFACE).direction(ScenePctService.Direction.COLUMN)).clip(true);
        ComponentKeyService[] componentKeyServiceArray3 = new ComponentKeyService[7];
        componentKeyServiceArray3[0] = this.mgfoe3yuc1bl();
        componentKeyServiceArray3[1] = ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.padding(0.0f, this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.search("anticheats.search", this.mfjzyzgupop1() ? "Find an anticheat\u2026" : "Search name, version or status\u2026", this.fcia1l1g5y8c, string -> {
            this.fcia1l1g5y8c = string;
            this.mg5qa0jj6zx4();
        }, true).props(interactiveSurfacePanel -> ((SceneCornerRadiusService)interactiveSurfacePanel.flex(1.0f)).minWidth(0.0f)), MaterialTextService.button("anticheats.free", "Free", () -> {
            this.fczhtab2khzf = !this.fczhtab2khzf;
            this.mg5qa0jj6zx4();
        }, this.fczhtab2khzf).props(materialJoinedService -> {
            materialJoinedService.colors(this.fczhtab2khzf ? MaterialIsLightService.PRIMARY : 0, this.fczhtab2khzf ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT).outlined(!this.fczhtab2khzf);
            ((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.height(LayoutOperationHandler.px(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(0x42400000)))).padding(0.0f, Float.intBitsToFloat(0x41400000))).tooltip("Only free anticheats");
        }));
        componentKeyServiceArray3[2] = this.m58q3w3q73ks();
        ComponentKeyService[] componentKeyServiceArray4 = new ComponentKeyService[2];
        componentKeyServiceArray4[0] = MaterialTextService.text(list.size() + (String)(this.fcia1l1g5y8c.isBlank() && this.fa2cluiy38x9.isEmpty() && !this.fczhtab2khzf && this.fjdlqe8byl8m == null ? " anticheats" : " of " + this.f7azho04q9ul.entries().size() + " anticheats"), Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.id("anticheats.count")).flex(1.0f)).minWidth(0.0f));
        componentKeyServiceArray4[1] = MaterialTextService.text(this.f4sf9qpnsqqo == AnticheatCodec.Sort.NAME ? (this.f3q4bpm1g7ab ? "Z\u2013A" : "A\u2013Z") : this.f4sf9qpnsqqo.label, Float.intBitsToFloat(0x41400000), MaterialIsLightService.OUTLINE);
        componentKeyServiceArray3[3] = ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.padding(Float.intBitsToFloat(0x40800000), this.mfjzyzgupop1() ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1105199104))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f)).visible(!this.mjmyt544a29v()), componentKeyServiceArray4);
        componentKeyServiceArray3[4] = this.md2jp62g9lgj() && !list.isEmpty() ? this.m5c76x8t1ldw() : ComponentBoxService.box(layoutContainerNode -> layoutContainerNode.visible(false), new ComponentKeyService[0]);
        Consumer<LayoutContainerNode> consumer3 = layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("anticheats.list")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).padding(Float.intBitsToFloat(0x40800000), this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952), this.mjmyt544a29v() ? Float.intBitsToFloat(0x40800000) : Float.intBitsToFloat(0x41400000), this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x40800000))).scrollable(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE)).clip(true);
        if (list.isEmpty()) {
            ComponentKeyService[] componentKeyServiceArray5 = new ComponentKeyService[1];
            componentKeyServiceArray = componentKeyServiceArray5;
            componentKeyServiceArray5[0] = this.mdb68zh3tz2i();
        } else {
            componentKeyServiceArray = (ComponentKeyService[])list.stream().map(this::m2ibmgy9evy3).toArray(ComponentKeyService[]::new);
        }
        componentKeyServiceArray3[5] = ComponentBoxService.column(consumer3, componentKeyServiceArray);
        componentKeyServiceArray3[6] = this.mhbcym5yf785();
        componentKeyServiceArray2[0] = ComponentBoxService.panel(consumer2, componentKeyServiceArray3);
        ComponentKeyService<LayoutContainerNode> componentKeyService = ComponentBoxService.column(consumer, componentKeyServiceArray2).key("anticheats");
        return ComponentBoxService.stack(layoutContainerNode -> layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))), componentKeyService, this.f4kqvxi0zdgp == Menu.NONE ? ComponentBoxService.box(layoutContainerNode -> layoutContainerNode.visible(false), new ComponentKeyService[0]).key("menu") : this.m2gua02xp6n3());
    }

    private ComponentKeyService<?> mgfoe3yuc1bl() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42400000) : Float.intBitsToFloat(1116733440)))).padding(Float.intBitsToFloat(0x40800000), this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41000000) : Float.intBitsToFloat(1098907648))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.iconButton("anticheats.back", "arrow_back", "Back to ClickGUI", this::m5t81omkfrhb).props(materialJoinedService -> materialJoinedService.size(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(0x42400000), this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(0x42400000))), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(0.0f), MaterialTextService.text("Anticheats", this.mfjzyzgupop1() ? Float.intBitsToFloat(1102053376) : Float.intBitsToFloat(1104150528), MaterialIsLightService.ON_SURFACE), MaterialTextService.text("Minecraft Anticheat List", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.visible((this.fgvjnn3sg1bz >= Float.intBitsToFloat(1137180672) && !this.mfjzyzgupop1() ? 1 : 0) != 0))), MaterialTextService.iconButton("anticheats.refresh", "refresh", this.f7azho04q9ul.loading() ? "Updating the list\u2026" : "Refresh list", () -> {
            this.f430kf6cmfsn.load(true);
            this.ma0sxngm13uq();
        }).props(materialJoinedService -> materialJoinedService.available(!this.f7azho04q9ul.loading()).size(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(0x42400000), this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(0x42400000))));
    }

    private ComponentKeyService<?> m58q3w3q73ks() {
        ComponentKeyService[] componentKeyServiceArray = new ComponentKeyService[5];
        componentKeyServiceArray[0] = this.m85h8cfw9z2g("platform", this.fa2cluiy38x9.isEmpty() ? (this.mfjzyzgupop1() ? "Platform" : "All platforms") : this.fa2cluiy38x9, Menu.PLATFORM);
        componentKeyServiceArray[1] = this.m85h8cfw9z2g("status", this.fjdlqe8byl8m == null ? (this.mfjzyzgupop1() ? "Status" : "All statuses") : this.fjdlqe8byl8m.label, Menu.STATUS);
        componentKeyServiceArray[2] = this.m85h8cfw9z2g("sort", this.f4sf9qpnsqqo.label, Menu.SORT);
        componentKeyServiceArray[3] = MaterialTextService.iconButton("anticheats.direction", "sort", this.f3q4bpm1g7ab ? "Descending \u00b7 click for ascending" : "Ascending \u00b7 click for descending", () -> {
            this.f3q4bpm1g7ab = !this.f3q4bpm1g7ab;
            this.mg5qa0jj6zx4();
        }).props(materialJoinedService -> materialJoinedService.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1108344832))).children(MaterialTextService.text(this.f3q4bpm1g7ab ? "\u2193" : "\u2191", Float.intBitsToFloat(1101004800), MaterialIsLightService.PRIMARY));
        componentKeyServiceArray[4] = ComponentBoxService.box(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(1.0f)).visible(!this.mfjzyzgupop1()), new ComponentKeyService[0]);
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("anticheats.filters")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42200000) : Float.intBitsToFloat(1110441984)))).padding(2.0f, this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x40C00000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), componentKeyServiceArray);
    }

    private ComponentKeyService<?> m85h8cfw9z2g(String string, String string2, Menu menu) {
        return MaterialTextService.button("anticheats." + string, string2, () -> {
            this.f4wtk1ybec86.clear(this.f4uz450c426t);
            this.f4kqvxi0zdgp = menu;
            this.m428ucdto9gj();
        }, false).props(materialJoinedService -> {
            materialJoinedService.colors(MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.ON_SURFACE_VARIANT).shape(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x42000000));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).padding(0.0f, this.mfjzyzgupop1() ? Float.intBitsToFloat(0x40C00000) : Float.intBitsToFloat(0x41400000))).minWidth(0.0f)).gap(this.mfjzyzgupop1() ? Float.intBitsToFloat(0x40800000) : Float.intBitsToFloat(0x40C00000))).flex(this.mfjzyzgupop1() ? 1.0f : 0.0f)).flexShrink(1.0f)).tooltip(menu == Menu.SORT ? "Sort by " + this.f4sf9qpnsqqo.label : "Filter by " + string);
        }).children(MaterialTextService.text(string2, this.mfjzyzgupop1() ? Float.intBitsToFloat(1093664768) : Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.minWidth(0.0f)).flexShrink(1.0f)), MaterialTextService.icon("expand_more", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648))));
    }

    private ComponentKeyService<?> m2gua02xp6n3() {
        ArrayList arrayList = new ArrayList();
        String menuTitle = switch (this.f4kqvxi0zdgp) {
            case PLATFORM -> "Platform";
            case STATUS -> "Status";
            default -> "Sort by";
        };
        if (this.f4kqvxi0zdgp == Menu.PLATFORM) {
            arrayList.add(this.m9bcmd1v72cs("platform.all", "All platforms", this.fa2cluiy38x9.isEmpty(), () -> {
                this.fa2cluiy38x9 = "";
            }));
            TreeSet treeSet = new TreeSet();
            this.f7azho04q9ul.entries().forEach(entry -> treeSet.addAll(entry.platforms()));
            for (String string3 : (Iterable<String>) (Iterable<?>) (treeSet)) {
                arrayList.add(this.m9bcmd1v72cs("platform." + string3, string3, this.fa2cluiy38x9.equals(string3), () -> {
                    this.fa2cluiy38x9 = string3;
                }));
            }
        } else if (this.f4kqvxi0zdgp == Menu.STATUS) {
            arrayList.add(this.m9bcmd1v72cs("status.all", "All statuses", this.fjdlqe8byl8m == null, () -> {
                this.fjdlqe8byl8m = null;
            }));
            for (AnticheatCodec.Status status : AnticheatCodec.Status.values()) {
                if (status == AnticheatCodec.Status.CHECKING) continue;
                arrayList.add(this.m9bcmd1v72cs("status." + status.name(), status.label, this.fjdlqe8byl8m == status, () -> {
                    this.fjdlqe8byl8m = status;
                }));
            }
        } else {
            for (AnticheatCodec.Sort sort : AnticheatCodec.Sort.values()) {
                arrayList.add(this.m9bcmd1v72cs("sort." + sort.name(), sort.label, this.f4sf9qpnsqqo == sort, () -> {
                    this.f4sf9qpnsqqo = sort;
                }));
            }
        }
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("anticheats.menu-scrim")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(1098907648))).layerBreak(true)).backgroundColor(-1728053248).direction(ScenePctService.Direction.COLUMN)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).interactive(true)).onClick(this::mfhq2bcvq96l)).stopPropagation(true), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("anticheats.menu")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1135869952))).height(LayoutOperationHandler.px(Math.min(this.fgvjnn3sg1bz - Float.intBitsToFloat(0x42000000), (float)(arrayList.size() * 44 + 76))))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_HIGH).padding(Float.intBitsToFloat(0x41400000))).direction(ScenePctService.Direction.COLUMN)).gap(Float.intBitsToFloat(0x40800000))).interactive(true)).stopPropagation(true), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(Float.intBitsToFloat(1110441984)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.text(menuTitle, Float.intBitsToFloat(1101004800), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x41000000))), MaterialTextService.iconButton("anticheats.menu.close", "close", "Close", this::mfhq2bcvq96l).props(materialJoinedService -> materialJoinedService.size(Float.intBitsToFloat(0x42200000), Float.intBitsToFloat(0x42200000)))), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("anticheats.menu.options")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).scrollable(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE)).gap(2.0f), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)))).key("menu");
    }

    private ComponentKeyService<?> m9bcmd1v72cs(String string, String string2, boolean bl, Runnable runnable) {
        return MaterialTextService.button("anticheats.option." + string, string2, () -> {
            runnable.run();
            this.f4kqvxi0zdgp = Menu.NONE;
            this.f4wtk1ybec86.clear(this.f4uz450c426t);
            this.mg5qa0jj6zx4();
        }, false).props(materialJoinedService -> {
            materialJoinedService.colors(bl ? MaterialIsLightService.SECONDARY_CONTAINER : 0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x42200000));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1110441984)))).padding(0.0f, Float.intBitsToFloat(0x41400000))).justify(ScenePctService.Justify.START)).gap(Float.intBitsToFloat(0x41400000));
        }).children(MaterialTextService.icon(bl ? "check" : "radio_button_unchecked", bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800))), MaterialTextService.text(string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)));
    }

    private void mfhq2bcvq96l() {
        this.f4wtk1ybec86.clear(this.f4uz450c426t);
        this.f4kqvxi0zdgp = Menu.NONE;
        this.m428ucdto9gj();
    }

    private ComponentKeyService<?> m5c76x8t1ldw() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.padding(Float.intBitsToFloat(0x40C00000), Float.intBitsToFloat(0x42200000))).gap(Float.intBitsToFloat(1098907648))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), this.m4x15eftpugb("ANTICHEAT", Float.intBitsToFloat(1075000115), Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE), this.m4x15eftpugb("PLATFORM", Float.intBitsToFloat(1068289229), Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE), this.m4x15eftpugb("STATUS", Float.intBitsToFloat(1068289229), Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE), this.m4x15eftpugb("VERSIONS", Float.intBitsToFloat(0x3FD33333), Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE), this.m4x15eftpugb("PRICE", Float.intBitsToFloat(1066192077), Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE), ComponentBoxService.box(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.size(Float.intBitsToFloat(1101004800), 1.0f)).flexShrink(0.0f), new ComponentKeyService[0]));
    }

    private ComponentKeyService<?> m2ibmgy9evy3(AnticheatCodec.Entry entry) {
        boolean bl = entry.name().equals(this.fc4b1nc8u9db);
        String string = "anticheats.entry." + entry.name();
        ComponentKeyService<MaterialJoinedService> componentKeyService = ComponentBoxService.node("anticheat-row", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(bl ? MaterialIsLightService.SECONDARY_CONTAINER : MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1098907648), 0.0f);
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id(string)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minHeight(this.md2jp62g9lgj() ? Float.intBitsToFloat(1115160576) : (this.mjmyt544a29v() ? Float.intBitsToFloat(1116209152) : Float.intBitsToFloat(1119354880)))).padding(this.mjmyt544a29v() ? Float.intBitsToFloat(0x40C00000) : Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1098907648))).gap(this.mjmyt544a29v() ? Float.intBitsToFloat(0x41000000) : Float.intBitsToFloat(1098907648))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).flexShrink(0.0f)).onClick(() -> {
                this.f4wtk1ybec86.clear(this.f4uz450c426t);
                this.fc4b1nc8u9db = bl ? "" : entry.name();
                this.m428ucdto9gj();
            })).tooltip(bl ? "Hide project links" : "Show project links");
        }, new ComponentKeyService[0]).key(string);
        componentKeyService = this.md2jp62g9lgj() ? componentKeyService.children(this.m4x15eftpugb(entry.name(), Float.intBitsToFloat(1075000115), Float.intBitsToFloat(1097859072), MaterialIsLightService.ON_SURFACE), this.m4x15eftpugb(String.join((CharSequence)" \u00b7 ", entry.platforms()), Float.intBitsToFloat(1068289229), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(Float.intBitsToFloat(1068289229))).minWidth(0.0f), this.mdvc93qxi3rw(entry.status())), this.m4x15eftpugb(entry.versions(), Float.intBitsToFloat(0x3FD33333), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT), this.m4x15eftpugb(entry.price(), Float.intBitsToFloat(1066192077), Float.intBitsToFloat(1095761920), entry.price().equalsIgnoreCase("Free") ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE), MaterialTextService.icon(bl ? "expand_more" : "chevron_right", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800)))) : componentKeyService.children(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(this.mjmyt544a29v() ? 2.0f : Float.intBitsToFloat(0x40C00000)), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), MaterialTextService.text(entry.name(), this.mjmyt544a29v() ? Float.intBitsToFloat(1096810496) : Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).wordWrap(true)), this.mdvc93qxi3rw(entry.status())), MaterialTextService.text(String.join((CharSequence)" \u00b7 ", entry.platforms()) + "  \u00b7  " + entry.versions(), Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.text(entry.price(), Float.intBitsToFloat(0x41400000), entry.price().equalsIgnoreCase("Free") ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true))), MaterialTextService.icon(bl ? "expand_more" : "chevron_right", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800))));
        if (!bl) {
            return componentKeyService;
        }
        ComponentKeyService[] componentKeyServiceArray = (ComponentKeyService[])entry.links().stream().map(link -> MaterialTextService.button(string + ".link." + String.valueOf(link.uri()), link.name(), () -> this.mghhxamtwwqa(link.uri()), false).props(materialJoinedService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.width(this.mfjzyzgupop1() ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.auto())).maxWidth(this.mfjzyzgupop1() ? Float.intBitsToFloat(1176256512) : Float.intBitsToFloat(1133903872))).tooltip(link.uri().toString())).gap(Float.intBitsToFloat(0x41000000))).children(MaterialTextService.icon("open_in_new", MaterialIsLightService.ON_SECONDARY_CONTAINER).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))), MaterialTextService.label(link.name(), MaterialIsLightService.ON_SECONDARY_CONTAINER).props(sceneTextService -> ((SceneTextService)sceneTextService.minWidth(0.0f)).flexShrink(1.0f)))).toArray(ComponentKeyService[]::new);
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(0.0f)).flexShrink(0.0f), componentKeyService, ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id(string + ".details")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1098907648))).cornerRadius(Float.intBitsToFloat(1098907648)).backgroundColor(MaterialIsLightService.SURFACE_LOW).direction(ScenePctService.Direction.COLUMN)).gap(Float.intBitsToFloat(0x41000000))).flexShrink(0.0f), MaterialTextService.text("PROJECT LINKS", Float.intBitsToFloat(1093664768), MaterialIsLightService.PRIMARY), entry.links().isEmpty() ? MaterialTextService.text("No links listed by the source.", Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT) : ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.gap(Float.intBitsToFloat(0x40C00000))).align(ScenePctService.Align.START), componentKeyServiceArray))).key(string + ".expanded");
    }

    private ComponentKeyService<?> mdvc93qxi3rw(AnticheatCodec.Status status) {
        int n = switch (status) {
            case AnticheatCodec.Status.ACTIVE -> -5450065;
            case AnticheatCodec.Status.DISCONTINUED, AnticheatCodec.Status.UNAVAILABLE -> MaterialIsLightService.ERROR;
            case AnticheatCodec.Status.OLD, AnticheatCodec.Status.INACTIVE -> MaterialIsLightService.TERTIARY;
            default -> MaterialIsLightService.ON_SURFACE_VARIANT;
        };
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.backgroundColor(MaterialIsLightService.layer(MaterialIsLightService.SURFACE_CONTAINER, n, Float.intBitsToFloat(0x3DCCCCCD))).cornerRadius(Float.intBitsToFloat(0x41000000)).padding(Float.intBitsToFloat(0x40400000), Float.intBitsToFloat(0x40E00000))).flexShrink(0.0f)).pointerEvents(false), MaterialTextService.text(status.label, Float.intBitsToFloat(1093664768), n));
    }

    private ComponentKeyService<?> m4x15eftpugb(String string, float f, float f2, int n) {
        return MaterialTextService.text(string, f2, n).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(f)).minWidth(0.0f)).wordWrap(true).maxLines(2).tooltip(string));
    }

    private ComponentKeyService<?> mdb68zh3tz2i() {
        ComponentKeyService[] componentKeyServiceArray = new ComponentKeyService[3];
        componentKeyServiceArray[0] = MaterialTextService.icon(this.f7azho04q9ul.loading() ? "refresh" : "search", MaterialIsLightService.PRIMARY).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1105199104)));
        componentKeyServiceArray[1] = MaterialTextService.text(this.f7azho04q9ul.loading() && this.f7azho04q9ul.entries().isEmpty() ? "Loading the list\u2026" : (this.f7azho04q9ul.entries().isEmpty() ? "The list is unavailable" : "No matches"), Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE);
        componentKeyServiceArray[2] = MaterialTextService.text(this.f7azho04q9ul.entries().isEmpty() ? (this.f7azho04q9ul.notice().isEmpty() ? "Fetching the community catalog." : this.f7azho04q9ul.notice()) : "Try a different name, version or platform.", Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true));
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.fgvjnn3sg1bz < Float.intBitsToFloat(1137180672) ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1105199104))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), componentKeyServiceArray);
    }

    private ComponentKeyService<?> mhbcym5yf785() {
        String string = this.f7azho04q9ul.updated() == null ? "Community directory" : "Updated " + DateTimeFormatter.ofPattern("dd MMM \u00b7 HH:mm", Locale.ENGLISH).withZone(ZoneId.systemDefault()).format(this.f7azho04q9ul.updated());
        long l = this.f7azho04q9ul.entries().stream().filter(entry -> entry.matches(this.fcia1l1g5y8c, this.fa2cluiy38x9, this.fczhtab2khzf, this.fjdlqe8byl8m)).count();
        String string2 = this.f7azho04q9ul.loading() ? "Updating\u2026" : (this.f7azho04q9ul.notice().isEmpty() ? string : this.f7azho04q9ul.notice());
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.padding(this.mjmyt544a29v() ? 2.0f : Float.intBitsToFloat(0x40800000), this.mfjzyzgupop1() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f), MaterialTextService.text((String)(this.mjmyt544a29v() ? l + " anticheats \u00b7 " : "") + string2, Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.id("anticheats.notice")).wordWrap(!this.mjmyt544a29v()).tooltip(string2)), MaterialTextService.text("by ManInMyVan \u00b7 Community data", Float.intBitsToFloat(1093664768), MaterialIsLightService.OUTLINE).props(sceneTextService -> ((SceneTextService)sceneTextService.visible(this.fgvjnn3sg1bz >= Float.intBitsToFloat(1137180672))).wordWrap(true))), MaterialTextService.iconButton("anticheats.source", "open_in_new", "Open the original Minecraft Anticheat List", () -> this.mghhxamtwwqa(URI.create("https://maninmyvan.github.io/Minecraft-Anticheat-List/"))).props(materialJoinedService -> materialJoinedService.size(this.mjmyt544a29v() ? Float.intBitsToFloat(0x42000000) : Float.intBitsToFloat(0x42200000), this.mjmyt544a29v() ? Float.intBitsToFloat(0x42000000) : Float.intBitsToFloat(0x42200000))));
    }

    private void mghhxamtwwqa(URI uRI) {
        if (AnticheatCodec.isWebLink(uRI)) {
            this.f3u4paxch97m.accept(uRI);
        }
    }

    private void m5t81omkfrhb() {
        this.f4wtk1ybec86.clear(this.f4uz450c426t);
        if (this.fasnmfavrpfs != null) {
            this.fasnmfavrpfs.close();
        }
    }

    private void ma0sxngm13uq() {
        this.f7azho04q9ul = this.f430kf6cmfsn.view();
        this.m428ucdto9gj();
    }

    @Override
    public void onOpen(ScreenScreenIdService screenScreenIdService) {
        this.f430kf6cmfsn.load(false);
        this.ma0sxngm13uq();
    }

    @Override
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        this.f4wtk1ybec86.clear(this.f4uz450c426t);
    }

    @Override
    public void tick(ScreenScreenIdService screenScreenIdService) {
        if (this.f4uz450c426t != null && this.f4uz450c426t.computedW() > 0.0f) {
            this.viewport(this.f4uz450c426t.computedW(), this.f4uz450c426t.computedH());
        }
        if (this.f7azho04q9ul != this.f430kf6cmfsn.view()) {
            this.ma0sxngm13uq();
        }
    }

    @Override
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
        if (n == 256) {
            if (this.f4kqvxi0zdgp != Menu.NONE) {
                this.mfhq2bcvq96l();
            } else if (!this.fc4b1nc8u9db.isEmpty()) {
                this.fc4b1nc8u9db = "";
                this.m428ucdto9gj();
            } else {
                this.m5t81omkfrhb();
            }
            return true;
        }
        if (n == 70 && (n2 & 0xA) != 0) {
            ScenePctService<?> scenePctService = this.f4uz450c426t.findById("anticheats.search");
            if (scenePctService instanceof ControlLetterSpacingService) {
                ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService;
                this.f4wtk1ybec86.focus(controlLetterSpacingService);
            }
            return true;
        }
        return this.f4wtk1ybec86.key(this.f4kqvxi0zdgp == Menu.NONE ? this.f4uz450c426t : this.f4uz450c426t.findById("anticheats.menu"), n, n2);
    }

    private static enum Menu {
        NONE,
        PLATFORM,
        STATUS,
        SORT;

    }
}

