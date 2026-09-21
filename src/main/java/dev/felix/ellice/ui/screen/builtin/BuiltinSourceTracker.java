





package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.account.AccountFetchHeadService;
import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.impl.playerlookup.PlayerlookupApiService;
import dev.felix.ellice.module.impl.playerlookup.PlayerlookupData;
import dev.felix.ellice.module.impl.playerlookup.PlayerlookupLooksValidValidator;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.awt.image.BufferedImage;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;

public final class BuiltinSourceTracker
implements ScreenOperationHandler {
    private final PlayerlookupApiService faizszxywnws;
    private final Source fb5pbjphroah;
    private final CompositorPushPresentationScaleService fewzcie2pwyo;
    private final Consumer<Runnable> f4ex2z3xnqx7;
    private final Consumer<String> f86lfd5nasn7;
    private final Consumer<String> fh8bo4llzbzf;
    private MaterialTerrainTooltipsService f7f4q1pf18kv;
    private ComponentMountService fg5fct03x0zx;
    private ScreenScreenIdService fgmfag1n197a;
    private float f4cfgnm8b97t = Float.intBitsToFloat(1150025728);
    private float fgkxnj6zemvl = Float.intBitsToFloat(0x44340000);
    private Phase f8ibb5iuezzg = Phase.INTRO;
    private String f6ye5tzszo51 = "";
    private String fjk1w49gw2o6 = "";
    private String fhxadz2u44oo = "";
    private PlayerlookupData f42z96m7k7ek;
    private long ffbgxfe6ce2g;
    private boolean f3p909m7jub6;

    public BuiltinSourceTracker(PlayerlookupApiService playerlookupApiService) {
        this(playerlookupApiService, new Source(){
            private final PlayerlookupLooksValidValidator ffgjw5b2mddp = PlayerlookupApiService.api();

            @Override
            public CompletableFuture<PlayerlookupData> lookup(String string) {
                return this.ffgjw5b2mddp.lookup(string);
            }

            @Override
            public List<String> recent() {
                return this.ffgjw5b2mddp.recent();
            }

            @Override
            public void clearRecent() {
                this.ffgjw5b2mddp.clearRecent();
            }

            @Override
            public CompletableFuture<BufferedImage> head(UUID uUID) {
                return AccountFetchHeadService.fetchHead(uUID);
            }
        }, CoreIsInitializedHandler.get().compositor(), runnable -> Minecraft.getInstance().execute(runnable), string -> Minecraft.getInstance().keyboardHandler.setClipboard(string), CompatOpenUriService::openUri);
    }

    public BuiltinSourceTracker(PlayerlookupApiService playerlookupApiService, Source source, CompositorPushPresentationScaleService compositorPushPresentationScaleService, Consumer<Runnable> consumer, Consumer<String> consumer2, Consumer<String> consumer3) {
        this.faizszxywnws = playerlookupApiService;
        this.fb5pbjphroah = source;
        this.fewzcie2pwyo = compositorPushPresentationScaleService;
        this.f4ex2z3xnqx7 = consumer;
        this.f86lfd5nasn7 = consumer2;
        this.fh8bo4llzbzf = consumer3;
    }

    @Override
    public String id() {
        return "player-lookup";
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
    public boolean opensAsWindow() {
        return true;
    }

    @Override
    public SceneCodec.Preset transitionPreset() {
        return SceneCodec.Preset.MODAL;
    }

    @Override
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.fgmfag1n197a = screenScreenIdService;
        this.f3p909m7jub6 = false;
        this.f7f4q1pf18kv = new MaterialTerrainTooltipsService().terrainTooltips(true);
        ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)this.f7f4q1pf18kv.id("player-lookup.root")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).direction(ScenePctService.Direction.NONE)).interactive(true);
        this.f7f4q1pf18kv.onLayout(() -> this.viewport(this.f7f4q1pf18kv.computedW(), this.f7f4q1pf18kv.computedH()));
        this.fg5fct03x0zx = new ComponentMountService();
        ((LayoutContainerNode)((LayoutContainerNode)this.fg5fct03x0zx.absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.fg5fct03x0zx.mount(this::mjreharcon0, screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        this.f7f4q1pf18kv.addChild(this.fg5fct03x0zx);
        this.mghlkr3leiv0();
        return this.f7f4q1pf18kv;
    }

    @Override
    public void onOpen(ScreenScreenIdService screenScreenIdService) {
        if (this.faizszxywnws.rememberLast() && !this.faizszxywnws.lastQuery().isBlank()) {
            this.maw7i8hjacd(this.faizszxywnws.lastQuery());
        }
    }

    @Override
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        this.f3p909m7jub6 = true;
        ++this.ffbgxfe6ce2g;
        this.mbo7vn43y88y();
    }

    @Override
    public void onSuspend(ScreenScreenIdService screenScreenIdService) {
        this.mbo7vn43y88y();
    }

    @Override
    public void tick(ScreenScreenIdService screenScreenIdService) {
        if (this.f7f4q1pf18kv != null) {
            this.viewport(this.f7f4q1pf18kv.computedW(), this.f7f4q1pf18kv.computedH());
        }
    }

    public void viewport(float f, float f2) {
        if (f <= 0.0f || f2 <= 0.0f || Math.abs(this.f4cfgnm8b97t - f) < Float.intBitsToFloat(0x3F000000) && Math.abs(this.fgkxnj6zemvl - f2) < Float.intBitsToFloat(0x3F000000)) {
            return;
        }
        this.f4cfgnm8b97t = f;
        this.fgkxnj6zemvl = f2;
        this.mfvtpvvupiqc();
    }

    private boolean mj0xkuh5pv1v() {
        return this.f4cfgnm8b97t < Float.intBitsToFloat(1142292480);
    }

    private void mbo7vn43y88y() {
        ScenePctService<?> scenePctService;
        if (this.f7f4q1pf18kv != null && (scenePctService = this.f7f4q1pf18kv.findById("player-lookup.query")) instanceof ControlLetterSpacingService) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService;
            controlLetterSpacingService.unfocus();
        }
    }

    private void mfvtpvvupiqc() {
        if (!this.f3p909m7jub6 && this.fg5fct03x0zx != null) {
            this.fg5fct03x0zx.invalidateComponent();
            this.mghlkr3leiv0();
        }
    }

    private void mghlkr3leiv0() {
        ScenePctService<?> scenePctService;
        if (this.f7f4q1pf18kv != null && (scenePctService = this.f7f4q1pf18kv.findById("player-lookup.query")) instanceof ControlLetterSpacingService) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService;
            controlLetterSpacingService.onSubmit(this::maw7i8hjacd);
        }
    }

    private ComponentKeyService<?> mjreharcon0(ComponentThemeService componentThemeService) {
        this.f7f4q1pf18kv.backgroundColor(ThemeCornerData.current().dimBackground() ? 0x52000000 : 0);
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.mj0xkuh5pv1v() ? 0.0f : (this.fgkxnj6zemvl < Float.intBitsToFloat(1143603200) ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1103101952)))).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("player-lookup.card")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1150025728))).maxHeight(Float.intBitsToFloat(0x444D0000))).minHeight(0.0f)).cornerRadius(this.mj0xkuh5pv1v() ? 0.0f : Float.intBitsToFloat(1105199104)).backgroundColor(MaterialIsLightService.SURFACE).direction(ScenePctService.Direction.COLUMN)).clip(true)).stopPropagation(true), this.mccooh70rqhx(), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("player-lookup.body")).flex(1.0f)).minHeight(0.0f)).padding(0.0f, this.mj0xkuh5pv1v() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1098907648), this.mj0xkuh5pv1v() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(1098907648))).scrollable(true)).scrollBounce(true)).scrollbarAutoHide(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE)).clip(true), this.migbl6z7ay43(), this.mjc0w4r0a8hi(), this.m6n1hz0qkx03()).key("body"), MaterialTextService.divider(), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flexShrink(0.0f)).padding(Float.intBitsToFloat(0x41400000), this.mj0xkuh5pv1v() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).minHeight(Float.intBitsToFloat(0x42400000))).align(ScenePctService.Align.CENTER), MaterialTextService.text(this.fhxadz2u44oo.isEmpty() ? "Profiles provided by PlayerDB" : this.fhxadz2u44oo, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).wordWrap(true))).key("footer")).key("workspace"));
    }

    private ComponentKeyService<?> mccooh70rqhx() {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(this.fgkxnj6zemvl < Float.intBitsToFloat(1138163712) ? Float.intBitsToFloat(1113587712) : Float.intBitsToFloat(1118830592)))).padding(Float.intBitsToFloat(0x40800000), this.mj0xkuh5pv1v() ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(1098907648))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.size(Float.intBitsToFloat(0x42400000), Float.intBitsToFloat(0x42400000))).cornerRadius(Float.intBitsToFloat(1098907648)).backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER).visible(!this.mj0xkuh5pv1v())).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER), MaterialTextService.icon("person", MaterialIsLightService.ON_PRIMARY_CONTAINER)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(2.0f), MaterialTextService.text("Player Lookup", this.mj0xkuh5pv1v() ? Float.intBitsToFloat(1103101952) : Float.intBitsToFloat(1105199104), MaterialIsLightService.ON_SURFACE), MaterialTextService.text("Find a player. Explore their profile.", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.visible(this.fgkxnj6zemvl >= Float.intBitsToFloat(1138163712)))), MaterialTextService.iconButton("player-lookup.close", "close", "Close lookup", () -> {
            if (this.fgmfag1n197a != null) {
                this.fgmfag1n197a.close();
            }
        }));
    }

    private ComponentKeyService<?> migbl6z7ay43() {
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto())).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000)), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), MaterialTextService.search("player-lookup.query", "Username or UUID", this.f6ye5tzszo51, string -> {
            this.f6ye5tzszo51 = string;
            ++this.ffbgxfe6ce2g;
            this.f8ibb5iuezzg = Phase.INTRO;
            this.f42z96m7k7ek = null;
            this.fhxadz2u44oo = "";
            this.mfvtpvvupiqc();
        }, this.mj0xkuh5pv1v()).props(interactiveSurfacePanel -> ((SceneCornerRadiusService)interactiveSurfacePanel.flex(1.0f)).minWidth(0.0f)), MaterialTextService.button("player-lookup.search", this.mj0xkuh5pv1v() ? "Find" : "Search", () -> this.maw7i8hjacd(this.f6ye5tzszo51), true)), MaterialTextService.text("Minecraft username or UUID, with or without dashes.", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)));
    }

    private ComponentKeyService<?> mjc0w4r0a8hi() {
        return switch (this.f8ibb5iuezzg.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> this.m96ms67vwd7d("search", "A profile starts with a name", "Search for a Minecraft player to see their UUID, skin and cape.", false);
            case 1 -> this.m96ms67vwd7d("search", "Looking up " + this.f6ye5tzszo51, "Fetching the latest profile\u2026", false);
            case 3 -> ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("player-lookup.error")).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41400000)), this.m96ms67vwd7d("search", "Couldn't load this player", this.fjk1w49gw2o6, true), MaterialTextService.button("player-lookup.retry", "Try again", () -> this.maw7i8hjacd(this.f6ye5tzszo51), false));
            case 2 -> this.m5ag7d7kqwae();
        };
    }

    private ComponentKeyService<?> m96ms67vwd7d(String string, String string2, String string3, boolean bl) {
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("player-lookup.status")).flexShrink(0.0f)).padding(this.mj0xkuh5pv1v() ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(0x42000000))).gap(Float.intBitsToFloat(0x41400000))).minHeight(this.fgkxnj6zemvl < Float.intBitsToFloat(1138163712) ? Float.intBitsToFloat(1123024896) : Float.intBitsToFloat(1130102784))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).direction(ScenePctService.Direction.COLUMN)).justify(ScenePctService.Justify.CENTER), MaterialTextService.icon(string, bl ? MaterialIsLightService.ERROR : MaterialIsLightService.PRIMARY), MaterialTextService.text(string2, Float.intBitsToFloat(1102053376), bl ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.text(string3, Float.intBitsToFloat(1096810496), bl ? MaterialIsLightService.ERROR : MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)));
    }

    private ComponentKeyService<?> m5ag7d7kqwae() {
        PlayerlookupData playerlookupData = this.f42z96m7k7ek;
        return ComponentBoxService.node("lookup-result", LayoutContainerNode::new, layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("player-lookup.result")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto())).flexShrink(0.0f)).direction(this.f4cfgnm8b97t < Float.intBitsToFloat(1146224640) ? ScenePctService.Direction.COLUMN : ScenePctService.Direction.ROW)).gap(Float.intBitsToFloat(1098907648))).align(ScenePctService.Align.STRETCH), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("player-lookup.identity")).width(this.f4cfgnm8b97t < Float.intBitsToFloat(1146224640) ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.px(Float.intBitsToFloat(1134559232)))).minWidth(0.0f)).flexShrink(0.0f)).padding(Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(1098907648))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).direction(ScenePctService.Direction.COLUMN), this.mfe9l9i98qxa(playerlookupData), MaterialTextService.text(playerlookupData.username(), Float.intBitsToFloat(1105199104), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.id("player-lookup.name")).wordWrap(true)), MaterialTextService.text(playerlookupData.skinModel(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT), MaterialTextService.button("player-lookup.copy-name", "Copy name", () -> this.mesowc23loph("Name", playerlookupData.username()), false), MaterialTextService.button("player-lookup.copy-uuid", "Copy UUID", () -> this.mesowc23loph("UUID", playerlookupData.dashedUuid()), true)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("player-lookup.details")).width(this.f4cfgnm8b97t < Float.intBitsToFloat(1146224640) ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.auto())).height(LayoutOperationHandler.auto())).flex(this.f4cfgnm8b97t < Float.intBitsToFloat(1146224640) ? 0.0f : 1.0f)).flexShrink(0.0f)).minWidth(0.0f)).gap(Float.intBitsToFloat(1098907648)), this.mibxttyxss10("Identity", MaterialTextService.text("UUID", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT), MaterialTextService.text(playerlookupData.dashedUuid(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.button("player-lookup.copy-raw", "Copy without dashes", () -> this.mesowc23loph("UUID", playerlookupData.trimmedUuid()), false)), this.mibxttyxss10("Textures", this.mekzxl4vjex8("skin", "Skin", playerlookupData.hasSkin() ? playerlookupData.skinUrl() : null, playerlookupData.skinModel()), MaterialTextService.divider(), this.mekzxl4vjex8("cape", "Cape", playerlookupData.hasCape() ? playerlookupData.capeUrl() : null, playerlookupData.hasCape() ? "Cape available" : "No cape on this profile"), MaterialTextService.text((String)(playerlookupData.texturesUpdatedAt() == null ? "Texture update time unavailable" : "Updated " + DateTimeFormatter.ofPattern("dd MMM uuuu").withZone(ZoneId.systemDefault()).format(playerlookupData.texturesUpdatedAt())), Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true))), this.mibxttyxss10("Explore profile", this.mi6ii9uf56m5("namemc", "NameMC", "https://namemc.com/profile/" + playerlookupData.dashedUuid()), this.mi6ii9uf56m5("crafty", "Crafty", "https://crafty.gg/players/" + playerlookupData.dashedUuid()), this.mi6ii9uf56m5("laby", "laby.net", "https://laby.net/@" + playerlookupData.dashedUuid()))));
    }

    private ComponentKeyService<?> mfe9l9i98qxa(PlayerlookupData playerlookupData) {
        String string = "mc.head." + String.valueOf(playerlookupData.uuid());
        int n = this.fewzcie2pwyo != null && this.fewzcie2pwyo.getNamedTexture(string) != null && this.fewzcie2pwyo.getNamedTexture(string).valid() ? 1 : 0;
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.size(Float.intBitsToFloat(1117782016), Float.intBitsToFloat(1117782016))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER), n != 0 ? ComponentBoxService.node("lookup-avatar", SceneTextureService::new, sceneTextureService -> ((SceneTextureService)sceneTextureService.size(Float.intBitsToFloat(1115684864), Float.intBitsToFloat(1115684864))).cornerRadius(Float.intBitsToFloat(1098907648)).textureName(string), new ComponentKeyService[0]) : MaterialTextService.text(playerlookupData.username().isEmpty() ? "?" : playerlookupData.username().substring(0, 1).toUpperCase(Locale.ROOT), Float.intBitsToFloat(0x42000000), MaterialIsLightService.ON_PRIMARY_CONTAINER));
    }

    private ComponentKeyService<?> mibxttyxss10(String string, ComponentKeyService<?> ... componentKeyServiceArray) {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        arrayList.add(MaterialTextService.text(string, Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.flexShrink(0.0f)));
        Collections.addAll(arrayList, componentKeyServiceArray);
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto())).flexShrink(0.0f)).padding(this.mj0xkuh5pv1v() ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x41400000))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).direction(ScenePctService.Direction.COLUMN), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).key(string);
    }

    private ComponentKeyService<?> mekzxl4vjex8(String string, String string2, String string3, String string4) {
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto())).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000)), MaterialTextService.text(string2, Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE), MaterialTextService.text(string4, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000))).visible(string3 != null), MaterialTextService.button("player-lookup.copy-" + string, "Copy URL", () -> this.mesowc23loph(string2 + " URL", string3), false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f)), MaterialTextService.button("player-lookup.open-" + string, "Open", () -> this.m2lhn5y52h0p(string3), false)));
    }

    private ComponentKeyService<?> mi6ii9uf56m5(String string, String string2, String string3) {
        return MaterialTextService.button("player-lookup.link-" + string, string2, () -> this.m2lhn5y52h0p(string3), false).props(materialJoinedService -> materialJoinedService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).children(MaterialTextService.icon("open_in_new", MaterialIsLightService.ON_SECONDARY_CONTAINER), MaterialTextService.label(string2, MaterialIsLightService.ON_SECONDARY_CONTAINER));
    }

    private ComponentKeyService<?> m6n1hz0qkx03() {
        List<String> list = this.fb5pbjphroah.recent();
        ArrayList<ComponentKeyService<MaterialJoinedService>> arrayList = new ArrayList<ComponentKeyService<MaterialJoinedService>>();
        for (String string : list) {
            arrayList.add(MaterialTextService.button("player-lookup.recent." + string, string, () -> this.maw7i8hjacd(string), false).props(materialJoinedService -> materialJoinedService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        }
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("player-lookup.recent")).flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000))).visible(!list.isEmpty()), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flexShrink(0.0f)).align(ScenePctService.Align.CENTER), MaterialTextService.text("Recent searches", Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.flex(1.0f)), MaterialTextService.iconButton("player-lookup.clear-recent", "delete", "Clear recent searches", () -> {
            this.fb5pbjphroah.clearRecent();
            this.mfvtpvvupiqc();
        })), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.flexShrink(0.0f)).gap(Float.intBitsToFloat(0x41000000)), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)));
    }

    private void maw7i8hjacd(String string) {
        if (this.f3p909m7jub6) {
            return;
        }
        long l = ++this.ffbgxfe6ce2g;
        this.f6ye5tzszo51 = string == null ? "" : string.trim();
        this.f42z96m7k7ek = null;
        this.fhxadz2u44oo = "";
        ScenePctService<?> scenePctService = this.f7f4q1pf18kv.findById("player-lookup.query");
        if (scenePctService instanceof ControlLetterSpacingService) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService)scenePctService;
            controlLetterSpacingService.text(this.f6ye5tzszo51);
        }
        if (this.f6ye5tzszo51.isEmpty()) {
            this.f8ibb5iuezzg = Phase.INTRO;
            this.mfvtpvvupiqc();
            return;
        }
        if (!PlayerlookupLooksValidValidator.looksValid(this.f6ye5tzszo51)) {
            this.f8ibb5iuezzg = Phase.ERROR;
            this.fjk1w49gw2o6 = "Enter a valid Minecraft username or UUID.";
            this.mfvtpvvupiqc();
            return;
        }
        this.faizszxywnws.rememberQuery(this.f6ye5tzszo51);
        this.f8ibb5iuezzg = Phase.LOADING;
        this.mfvtpvvupiqc();
        this.fb5pbjphroah.lookup(this.f6ye5tzszo51).whenComplete((playerlookupData, throwable) -> this.f4ex2z3xnqx7.accept(() -> {
            if (this.f3p909m7jub6 || l != this.ffbgxfe6ce2g) {
                return;
            }
            if (throwable != null || playerlookupData == null) {
                this.f8ibb5iuezzg = Phase.ERROR;
                this.fjk1w49gw2o6 = "The player could not be found or the service is unavailable. Check the name and try again.";
                this.mfvtpvvupiqc();
                return;
            }
            this.f42z96m7k7ek = playerlookupData;
            this.f8ibb5iuezzg = Phase.RESULT;
            if (this.faizszxywnws.autoCopyUuid()) {
                this.mesowc23loph("UUID", playerlookupData.dashedUuid());
            }
            this.mfvtpvvupiqc();
            if (playerlookupData.uuid() != null && this.fewzcie2pwyo != null) {
                this.fb5pbjphroah.head(playerlookupData.uuid()).whenComplete((bufferedImage, headError) -> this.f4ex2z3xnqx7.accept(() -> {
                    if (this.f3p909m7jub6 || l != this.ffbgxfe6ce2g || bufferedImage == null) {
                        return;
                    }
                    this.fewzcie2pwyo.registerImageTexture("mc.head." + String.valueOf(playerlookupData.uuid()), (BufferedImage)bufferedImage);
                    this.mfvtpvvupiqc();
                }));
            }
        }));
    }

    private void mesowc23loph(String string, String string2) {
        if (string2 == null || string2.isBlank()) {
            return;
        }
        this.f86lfd5nasn7.accept(string2);
        this.fhxadz2u44oo = string + " copied";
        this.mfvtpvvupiqc();
    }

    private void m2lhn5y52h0p(String string) {
        if (string != null && !string.isBlank()) {
            this.fh8bo4llzbzf.accept(string);
        }
    }

    public static interface Source {
        public CompletableFuture<PlayerlookupData> lookup(String var1);

        public List<String> recent();

        public void clearRecent();

        default public CompletableFuture<BufferedImage> head(UUID uUID) {
            return CompletableFuture.completedFuture(null);
        }
    }

    private static enum Phase {
        INTRO,
        LOADING,
        RESULT,
        ERROR;

    }
}

