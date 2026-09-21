


package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneFocusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.builtin.ServerListRenderer;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class ServerPreviewScreen
implements ScreenOperationHandler {
    private final ServerViewTracker fik8ky4qqdqo;
    private final String f5ynsi4oj7it;
    private final String f5ipdnxn2q6q;
    private final UUID f3qe9yyveqrc;
    private final ServerListRenderer f38oqsvwowjy;
    private final MaterialKeyService f1mky6v5sc65 = new MaterialKeyService();
    private ComponentMountService fgjdlo13813i;
    private MaterialTerrainTooltipsService fhhmvltp996k;
    private ScreenScreenIdService f3vsve9xru0h;
    private float fbg6iovryrvm = Float.intBitsToFloat(1150025728);
    private float fjp5nr1cogcb = Float.intBitsToFloat(0x44340000);
    private long fjkbp0wtka7k = -1L;
    private long f4ikuf3ulwuv;

    public ServerPreviewScreen(ServerViewTracker serverViewTracker, UUID uUID, String string, String string2, BiConsumer<String, String> biConsumer) {
        this.fik8ky4qqdqo = serverViewTracker;
        this.f5ynsi4oj7it = string;
        this.f5ipdnxn2q6q = string2;
        this.f3qe9yyveqrc = uUID;
        this.f38oqsvwowjy = new ServerListRenderer(serverViewTracker, biConsumer);
        this.f38oqsvwowjy.account(uUID, "", true);
    }

    @Override
    public String id() {
        return "server-preview";
    }

    @Override
    public float backgroundDesaturation() {
        return 0.0f;
    }

    @Override
    public SceneCodec.Preset transitionPreset() {
        return SceneCodec.Preset.NONE;
    }

    public void viewport(float f, float f2) {
        if (this.fbg6iovryrvm == f && this.fjp5nr1cogcb == f2) {
            return;
        }
        this.fbg6iovryrvm = f;
        this.fjp5nr1cogcb = f2;
        this.f1mky6v5sc65.clear(this.fhhmvltp996k);
        this.mdz56kdvx7ue();
    }

    @Override
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.f3vsve9xru0h = screenScreenIdService;
        this.fhhmvltp996k = new MaterialTerrainTooltipsService();
        ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)this.fhhmvltp996k.id("server-preview.canvas")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).direction(ScenePctService.Direction.NONE)).backgroundColor(MaterialIsLightService.SURFACE_LOWEST).interactive(true);
        this.fhhmvltp996k.addChild((ScenePctService<?>)((SceneFocusService)((SceneFocusService)((SceneFocusService)new SceneFocusService().backdrop(true).absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())).pointerEvents(false));
        this.fgjdlo13813i = new ComponentMountService().mount(this::mffy228vb3b7, screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme());
        ((LayoutContainerNode)((LayoutContainerNode)this.fgjdlo13813i.absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.fhhmvltp996k.addChild(this.fgjdlo13813i);
        return this.fhhmvltp996k;
    }

    private ComponentKeyService<?> mffy228vb3b7(ComponentThemeService componentThemeService) {
        this.fhhmvltp996k.backgroundColor(MaterialIsLightService.SURFACE_LOWEST);
        boolean bl = this.fbg6iovryrvm >= Float.intBitsToFloat(0x444D0000);
        ServerViewTracker.View view = this.fik8ky4qqdqo.view(this.f5ynsi4oj7it);
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
        if (view.reachable() && view.status() != null) {
            for (String object2 : view.status().samplePlayers()) {
                arrayList.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.gap(Float.intBitsToFloat(1092616192))).padding(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), MaterialTextService.icon("person", MaterialIsLightService.PRIMARY), MaterialTextService.text(object2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f))).key(object2));
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(MaterialTextService.text(view.checkedAt() == 0L ? "Checking the server\u2026" : (!view.reachable() ? "The server is not responding to status requests." : "This server does not share a public player sample."), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)));
        }
        ComponentKeyService<?> componentKeyService = this.f38oqsvwowjy.previewDetails(componentThemeService, this.f5ynsi4oj7it, this.f5ipdnxn2q6q);
        ComponentKeyService<LayoutContainerNode> componentKeyService2 = ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.id("preview.player-panel")).gap(Float.intBitsToFloat(1098907648)), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1103101952)).padding(Float.intBitsToFloat(1101004800))).gap(Float.intBitsToFloat(0x41400000)), MaterialTextService.text("Who's around", Float.intBitsToFloat(1103101952), MaterialIsLightService.ON_SURFACE), MaterialTextService.text("A public sample shared by the server. It may not include every player online.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.id("preview.players")).gap(Float.intBitsToFloat(0x40800000)), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new))), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1103101952)).padding(Float.intBitsToFloat(1101004800))).gap(Float.intBitsToFloat(0x41400000)), MaterialTextService.text("Just looking? You're welcome.", Float.intBitsToFloat(1101004800), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.text("Previewing checks public server information. Your account stays in the menu until you choose Join server.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.button("preview.favorite", this.fik8ky4qqdqo.library().usage(this.f3qe9yyveqrc, this.f5ynsi4oj7it).favorite() ? "Saved to favourites" : "Save to favourites", () -> {
            this.fik8ky4qqdqo.toggleFavorite(this.f3qe9yyveqrc, this.f5ynsi4oj7it);
            this.mdz56kdvx7ue();
        }, false)), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1103101952)).padding(Float.intBitsToFloat(1101004800))).gap(Float.intBitsToFloat(0x41000000)), MaterialTextService.text("Server chat", Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE), MaterialTextService.text("Minecraft server chat requires a signed-in play connection. A status preview cannot read or send in-game messages.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true))));
        return ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(this.fbg6iovryrvm < Float.intBitsToFloat(1142292480) ? 0.0f : Float.intBitsToFloat(1103101952))).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("preview.sheet")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1149370368))).maxHeight(Float.intBitsToFloat(0x444D0000))).backgroundColor(ScenePctService.mulAlpha(MaterialIsLightService.SURFACE, Float.intBitsToFloat(1064849900))).blur(Float.intBitsToFloat(1103101952)).cornerRadius(this.fbg6iovryrvm < Float.intBitsToFloat(1142292480) ? 0.0f : Float.intBitsToFloat(1105199104)).direction(ScenePctService.Direction.COLUMN)).clip(true), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.height(LayoutOperationHandler.px(this.fjp5nr1cogcb < Float.intBitsToFloat(1135869952) ? Float.intBitsToFloat(1112539136) : Float.intBitsToFloat(1116733440)))).padding(Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x40800000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.iconButton("preview.close", "arrow_back", "Back \u00b7 Esc", () -> {
            if (this.f3vsve9xru0h != null) {
                this.f3vsve9xru0h.close();
            }
        }), MaterialTextService.text("Server preview", Float.intBitsToFloat(1102053376), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f))), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("preview.scroll")).direction(bl ? ScenePctService.Direction.ROW : ScenePctService.Direction.COLUMN)).flex(1.0f)).minHeight(0.0f)).gap(Float.intBitsToFloat(1101004800))).padding(0.0f, this.fbg6iovryrvm < Float.intBitsToFloat(1142292480) ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1103101952), this.fbg6iovryrvm < Float.intBitsToFloat(1142292480) ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).scrollable(true)).clip(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT), componentKeyService.props(scenePctService -> ((ScenePctService)scenePctService.width(bl ? LayoutOperationHandler.px(Float.intBitsToFloat(1135869952)) : LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flexShrink(0.0f)), componentKeyService2.props(scenePctService -> ((ScenePctService)((ScenePctService)scenePctService.flex(bl ? 1.0f : 0.0f)).minWidth(0.0f)).flexShrink(0.0f)))));
    }

    private void mdz56kdvx7ue() {
        if (this.fgjdlo13813i != null) {
            this.fgjdlo13813i.invalidateComponent();
        }
    }

    @Override
    public void tick(ScreenScreenIdService screenScreenIdService) {
        if (screenScreenIdService != null) {
            DisplayMetrics displayMetrics = CoreIsInitializedHandler.get().viewport();
            this.viewport(displayMetrics.width(), displayMetrics.height());
        }
        long l = System.currentTimeMillis();
        if (screenScreenIdService != null && l >= this.f4ikuf3ulwuv) {
            this.f4ikuf3ulwuv = l + 200L;
            this.fik8ky4qqdqo.seedIcon(this.f5ynsi4oj7it, null);
            this.fik8ky4qqdqo.request(this.f5ynsi4oj7it, false);
        }
        if (this.fjkbp0wtka7k != this.fik8ky4qqdqo.revision()) {
            this.fjkbp0wtka7k = this.fik8ky4qqdqo.revision();
            this.mdz56kdvx7ue();
        }
    }

    @Override
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        this.f1mky6v5sc65.clear(this.fhhmvltp996k);
    }

    @Override
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
        return this.f1mky6v5sc65.key(this.fhhmvltp996k, n, n2);
    }
}

