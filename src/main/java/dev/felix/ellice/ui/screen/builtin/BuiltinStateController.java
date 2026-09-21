



package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.ui.material.MaterialJoinedService;
import java.util.function.Supplier;
import dev.felix.ellice.ui.component.ComponentBoxService;
import java.util.function.Consumer;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import java.util.ArrayList;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.changelog.ChangelogRelease;

public final class BuiltinStateController
{
    private final ChangelogRelease f16oqibi4kgl;
    private final Runnable fa1ot51l4aab;
    private final LayoutContainerNode f46zyc9c8juc;
    private final ComponentMountService f6hkb7w2ks7;
    private float f3v01610wluj;
    private float f8szjrtdt70j;
    private boolean fafjc3e9hjl4;
    private boolean f1uw83isq3wk;
    private long f48q7ukoz5m;
    
    public BuiltinStateController(final ChangelogRepository changelogRepository, final Runnable fa1ot51l4aab, final ThemeIsSetService themeIsSetService) {
        this.f46zyc9c8juc = new LayoutContainerNode();
        this.f6hkb7w2ks7 = new ComponentMountService();
        this.f3v01610wluj = Float.intBitsToFloat(1150025728);
        this.f8szjrtdt70j = Float.intBitsToFloat(1144258560);
        this.f16oqibi4kgl = changelogRepository.latest();
        this.fa1ot51l4aab = fa1ot51l4aab;
        this.f46zyc9c8juc.id("menu.news.layer").layerBreak(true).absolute().inset(LayoutOperationHandler.px(0.0f)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto()).interactive(false);
        this.f6hkb7w2ks7.absolute().inset(LayoutOperationHandler.px(0.0f)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto()).interactive(false);
        this.f6hkb7w2ks7.mount(this::m4re3oi63d5s, themeIsSetService);
        this.f46zyc9c8juc.addChild(this.f6hkb7w2ks7);
    }
    
    public ScenePctService<?> node() {
        return this.f46zyc9c8juc;
    }
    
    public boolean isOpen() {
        return this.fafjc3e9hjl4;
    }
    
    public void viewport(final float f3v01610wluj, final float f8szjrtdt70j) {
        if (this.f3v01610wluj == f3v01610wluj && this.f8szjrtdt70j == f8szjrtdt70j) {
            return;
        }
        this.f3v01610wluj = f3v01610wluj;
        this.f8szjrtdt70j = f8szjrtdt70j;
        this.f6hkb7w2ks7.invalidateComponent();
        final ScenePctService<?> byId = this.f46zyc9c8juc.findById("menu.news.panel");
        MotionAnimateService.cancel(byId, MotionColorsContainer.Floats.TRANSLATE_X);
        byId.translateX(this.fafjc3e9hjl4 ? 0.0f : (this.m31p2gptobjs() + Float.intBitsToFloat(1107296256)));
    }
    
    public void tick() {
        if (!this.f1uw83isq3wk && this.f48q7ukoz5m != 0L && System.nanoTime() >= this.f48q7ukoz5m) {
            this.close();
        }
    }
    
    public void close() {
        this.f1uw83isq3wk = false;
        this.f48q7ukoz5m = 0L;
        this.mj2nk9xplwjw(false);
    }
    
    private float m31p2gptobjs() {
        return Math.max(Float.intBitsToFloat(1127481344), Math.min(Float.intBitsToFloat(1136132096), this.f3v01610wluj - Float.intBitsToFloat(1103101952)));
    }
    
    private float m2n87nonv962() {
        return (this.f8szjrtdt70j < Float.intBitsToFloat(1137836032)) ? Float.intBitsToFloat(1120927744) : Float.intBitsToFloat(1125122048);
    }
    
    private void mj6y32t7mih2(final boolean b) {
        if (b) {
            this.f48q7ukoz5m = 0L;
            this.mj2nk9xplwjw(true);
        }
        else if (!this.f1uw83isq3wk) {
            this.f48q7ukoz5m = System.nanoTime() + 220000000L;
        }
    }
    
    private void mj2nk9xplwjw(final boolean fafjc3e9hjl4) {
        if (this.fafjc3e9hjl4 == fafjc3e9hjl4) {
            return;
        }
        this.fafjc3e9hjl4 = fafjc3e9hjl4;
        final ScenePctService<?> byId = this.f46zyc9c8juc.findById("menu.news.panel");
        byId.pointerEvents(fafjc3e9hjl4);
        MotionAnimateService.animate(byId, MotionColorsContainer.Floats.TRANSLATE_X, fafjc3e9hjl4 ? 0.0f : (this.m31p2gptobjs() + Float.intBitsToFloat(1107296256)), fafjc3e9hjl4 ? new SceneEaseHandler.Spring(Float.intBitsToFloat(1100480512), Float.intBitsToFloat(1129447424)) : new SceneEaseHandler.Spring(Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1131413504)));
        MotionAnimateService.animate(byId, MotionColorsContainer.Floats.OPACITY, fafjc3e9hjl4 ? 1.0f : 0.0f, SceneEaseHandler.Tween.ease(fafjc3e9hjl4 ? Float.intBitsToFloat(1043878380) : Float.intBitsToFloat(1042536202)));
    }
    
    private ComponentKeyService<?> m4re3oi63d5s(final ComponentThemeService componentThemeService) {
        final String s = (this.f16oqibi4kgl == null) ? "" : this.f16oqibi4kgl.version();
        final ArrayList list = new ArrayList();
        if (this.f16oqibi4kgl != null) {
            for (int i = 0; i < Math.min(3, this.f16oqibi4kgl.highlights().size()); ++i) {
                list.add(MaterialTextService.text(this.f16oqibi4kgl.highlights().get(i), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap((boolean)(1 != 0)).flexShrink(0.0f)).key("highlight-" + i));
            }
        }
        return ComponentBoxService.node("news-layer", LayoutContainerNode::new, layoutContainerNode -> layoutContainerNode.absolute().inset(LayoutOperationHandler.px(0.0f)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto()).interactive((boolean)(0 != 0)), ComponentBoxService.panel(sceneCornerRadiusService -> sceneCornerRadiusService.id("menu.news.panel").absolute().right(LayoutOperationHandler.px(Float.intBitsToFloat(1094713344))).top(LayoutOperationHandler.px(this.m2n87nonv962())).size(LayoutOperationHandler.px(this.m31p2gptobjs()), LayoutOperationHandler.auto()).maxHeight(Math.max(Float.intBitsToFloat(1120403456), this.f8szjrtdt70j - this.m2n87nonv962() - Float.intBitsToFloat(1098907648))).direction(ScenePctService.Direction.COLUMN).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).border(1.0f, MaterialIsLightService.OUTLINE_VARIANT).clip((boolean)(1 != 0)).onHoverChange(this::mj6y32t7mih2).pointerEvents(this.fafjc3e9hjl4).stopPropagation((boolean)(1 != 0)), ComponentBoxService.row(layoutContainerNode2 -> layoutContainerNode2.padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1098907648), 0.0f, Float.intBitsToFloat(1101004800)).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f).align(ScenePctService.Align.CENTER), MaterialTextService.text("LATEST UPDATE", Float.intBitsToFloat(1093664768), MaterialIsLightService.PRIMARY).props(sceneTextService2 -> sceneTextService2.flex(1.0f).minWidth(0.0f)), MaterialTextService.text(s, Float.intBitsToFloat(1094713344), MaterialIsLightService.ON_SURFACE_VARIANT), MaterialTextService.iconButton("menu.news.close", "close", "Close update", this::close).props(materialJoinedService -> materialJoinedService.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1108344832)).onHoverChange(this::mj6y32t7mih2))), ComponentBoxService.column(layoutContainerNode3 -> layoutContainerNode3.id("menu.news.content").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).flexShrink(1.0f).minHeight(0.0f).padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800)).gap(Float.intBitsToFloat(1094713344)).scrollable((boolean)(1 != 0)).clip((boolean)(1 != 0)).scrollbarAutoHide((boolean)(1 != 0)).onHoverChange(this::mj6y32t7mih2), MaterialTextService.text((this.f16oqibi4kgl == null) ? "You're up to date" : this.f16oqibi4kgl.title(), Float.intBitsToFloat(1102053376), MaterialIsLightService.ON_SURFACE).props(sceneTextService3 -> sceneTextService3.wordWrap((boolean)(1 != 0)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f)), ComponentBoxService.column(layoutContainerNode4 -> layoutContainerNode4.id("menu.news.highlights").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f), (ComponentKeyService<?>[])list.toArray(ComponentKeyService[]::new))), ComponentBoxService.row(layoutContainerNode5 -> layoutContainerNode5.padding(0.0f, Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800)).gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).flexShrink(0.0f), MaterialTextService.text((this.f16oqibi4kgl == null) ? "" : ChangelogEntryFormatter.date(this.f16oqibi4kgl.date()), Float.intBitsToFloat(1094713344), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService4 -> sceneTextService4.flex(1.0f).minWidth(0.0f)), MaterialTextService.button("menu.news.history", "View update", () -> {
            this.close();
            this.fa1ot51l4aab.run();
        }, true).props(materialJoinedService2 -> materialJoinedService2.height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).onHoverChange(this::mj6y32t7mih2)))).key("panel").onMount(sceneCornerRadiusService2 -> sceneCornerRadiusService2.translateX(this.m31p2gptobjs() + Float.intBitsToFloat(1107296256)).opacity(0.0f)), MaterialTextService.button("menu.news.tab", "What's new", () -> {
            this.f1uw83isq3wk = (1 != 0);
            this.f48q7ukoz5m = 0L;
            this.mj2nk9xplwjw((boolean)(1 != 0));
        }, false).props(materialJoinedService3 -> materialJoinedService3.absolute().right(LayoutOperationHandler.px(Float.intBitsToFloat(1094713344))).top(LayoutOperationHandler.px(Math.max(Float.intBitsToFloat(1090519040), this.m2n87nonv962() - Float.intBitsToFloat(1111490560)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).onHoverChange(this::mj6y32t7mih2).tooltip("Hover to peek · click to keep open")));
    }
}
