



package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import java.util.Iterator;
import dev.felix.ellice.changelog.ChangelogFeatureType;
import java.util.Map;
import java.util.ArrayList;
import dev.felix.ellice.changelog.ChangelogData;
import java.util.LinkedHashMap;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCodec;
import java.util.List;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.changelog.ChangelogRelease;
import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;

public final class ChangelogScreen implements ScreenOperationHandler
{
    private final ChangelogRepository fcd03qjvzp12;
    private ChangelogRelease ff9yd7x4gnqj;
    private MaterialTerrainTooltipsService f9l6208fuf5j;
    private ComponentMountService f64cyuz4n3j0;
    private ScreenScreenIdService farfaaomwgtu;
    private float fy8y1uw2yzk;
    private float f2vwvnbc44f0;
    private boolean fa77am6391g;
    
    public ChangelogScreen(final ChangelogRepository changelogRepository) {
        this.fy8y1uw2yzk = Float.intBitsToFloat(1150025728);
        this.f2vwvnbc44f0 = Float.intBitsToFloat(1144258560);
        this.fcd03qjvzp12 = ((changelogRepository == null) ? new ChangelogRepository(List.of()) : changelogRepository);
        this.ff9yd7x4gnqj = this.fcd03qjvzp12.latest();
    }
    
    @Override
    public String id() {
        return "changelog";
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
    public float backgroundDesaturation() {
        return 0.0f;
    }
    
    @Override
    public SceneCodec.Preset transitionPreset() {
        return SceneCodec.Preset.MODAL;
    }
    
    @Override
    public ScenePctService<?> build(final ScreenScreenIdService farfaaomwgtu) {
        this.farfaaomwgtu = farfaaomwgtu;
        this.ff9yd7x4gnqj = this.fcd03qjvzp12.latest();
        this.fa77am6391g = false;
        this.f9l6208fuf5j = new MaterialTerrainTooltipsService();
        this.f9l6208fuf5j.id("changelog.root").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).direction(ScenePctService.Direction.NONE).interactive(true);
        this.f9l6208fuf5j.onLayout(() -> this.viewport(this.f9l6208fuf5j.computedW(), this.f9l6208fuf5j.computedH()));
        this.f64cyuz4n3j0 = new ComponentMountService();
        this.f64cyuz4n3j0.absolute().inset(LayoutOperationHandler.px(0.0f)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.f64cyuz4n3j0.mount(this::mful88szt86n, (farfaaomwgtu == null) ? new ThemeIsSetService() : farfaaomwgtu.theme());
        this.f9l6208fuf5j.addChild(this.f64cyuz4n3j0);
        return this.f9l6208fuf5j;
    }
    
    public void viewport(final float fy8y1uw2yzk, final float f2vwvnbc44f0) {
        if (fy8y1uw2yzk <= 0.0f || f2vwvnbc44f0 <= 0.0f || (Math.abs(fy8y1uw2yzk - this.fy8y1uw2yzk) < Float.intBitsToFloat(1056964608) && Math.abs(f2vwvnbc44f0 - this.f2vwvnbc44f0) < Float.intBitsToFloat(1056964608))) {
            return;
        }
        this.fy8y1uw2yzk = fy8y1uw2yzk;
        this.f2vwvnbc44f0 = f2vwvnbc44f0;
        this.mgn57kf5f52v();
    }
    
    @Override
    public void tick(final ScreenScreenIdService screenScreenIdService) {
        if (this.f9l6208fuf5j != null) {
            this.viewport(this.f9l6208fuf5j.computedW(), this.f9l6208fuf5j.computedH());
        }
    }
    
    private void mgn57kf5f52v() {
        if (this.f64cyuz4n3j0 != null) {
            this.f64cyuz4n3j0.invalidateComponent();
        }
    }
    
    private void mjbqhsc1dxrf() {
        if (this.farfaaomwgtu != null) {
            this.farfaaomwgtu.close();
        }
    }
    
    private boolean m5wlfa93bj5p() {
        return this.fy8y1uw2yzk < Float.intBitsToFloat(1142292480);
    }
    
    private ComponentKeyService<?> mful88szt86n(final ComponentThemeService componentThemeService) {
        this.f9l6208fuf5j.backgroundColor(ThemeCornerData.current().dimBackground() ? 1375731712 : 0);
        return ComponentBoxService.column(layoutContainerNode -> layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(this.m5wlfa93bj5p() ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1103101952)).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER), ComponentBoxService.panel(sceneCornerRadiusService -> sceneCornerRadiusService.id("changelog.card").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).maxWidth(Float.intBitsToFloat(1144258560)).maxHeight(Math.max(Float.intBitsToFloat(1123024896), this.f2vwvnbc44f0 - (this.m5wlfa93bj5p() ? 16 : 128))).minHeight(0.0f).direction(ScenePctService.Direction.COLUMN).cornerRadius(Float.intBitsToFloat(1105199104)).backgroundColor(MaterialIsLightService.SURFACE).clip(true).stopPropagation(true), ComponentBoxService.row(layoutContainerNode2 -> layoutContainerNode2.padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1101004800), 0.0f, Float.intBitsToFloat(1103101952)).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f).align(ScenePctService.Align.CENTER), MaterialTextService.text("RELEASE NOTES", Float.intBitsToFloat(1094713344), MaterialIsLightService.PRIMARY).props(sceneTextService -> sceneTextService.flex(1.0f).minWidth(0.0f)), MaterialTextService.iconButton("changelog.close", "close", "Close release notes", this::mjbqhsc1dxrf)), ComponentBoxService.column(layoutContainerNode3 -> layoutContainerNode3.id("changelog.content").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).flexShrink(1.0f).minHeight(0.0f).padding(this.m5wlfa93bj5p() ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1103101952)).gap(Float.intBitsToFloat(1101004800)).scrollable(true).scrollbarAutoHide(true).scrollbarColor(MaterialIsLightService.OUTLINE).clip(true), this.fa77am6391g ? this.mbibnqa5wqi8() : ((this.ff9yd7x4gnqj == null) ? MaterialTextService.text("No release notes are available yet.", Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE_VARIANT) : this.m3l6aso1q1jh())), MaterialTextService.divider(), ComponentBoxService.row(layoutContainerNode4 -> layoutContainerNode4.padding(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f).align(ScenePctService.Align.CENTER), MaterialTextService.button("changelog.versions", this.fa77am6391g ? "Back to notes" : "Release history", () -> {
            this.fa77am6391g = !this.fa77am6391g;
            this.mgn57kf5f52v();
        }, false).props(materialJoinedService -> materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).visible(this.fcd03qjvzp12.releases().size() > 1)), ComponentBoxService.column(layoutContainerNode5 -> layoutContainerNode5.flex(1.0f), (ComponentKeyService<?>[])new ComponentKeyService[0]), MaterialTextService.button("changelog.done", "Got it", this::mjbqhsc1dxrf, true).props(materialJoinedService2 -> materialJoinedService2.height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(Float.intBitsToFloat(1120403456))))));
    }
    
    private ComponentKeyService<?> m3l6aso1q1jh() {
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (final ChangelogData changelogData : this.ff9yd7x4gnqj.entries()) {
            ((List)linkedHashMap.computeIfAbsent(changelogData.type(), p0 -> new ArrayList())).add(changelogData);
        }
        final ArrayList<ComponentKeyService<SceneCornerRadiusService>> list = new ArrayList<ComponentKeyService<SceneCornerRadiusService>>();
        for (Map.Entry entry : (Iterable<Map.Entry>) (Iterable<?>) (linkedHashMap.entrySet())) {
            final ArrayList<ComponentKeyService<SceneTextService>> list2 = new ArrayList<ComponentKeyService<SceneTextService>>();
            for (int i = 0; i < ((List)entry.getValue()).size(); ++i) {
                list2.add(MaterialTextService.text(((List<ChangelogData>)entry.getValue()).get(i).text(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap((boolean)(1 != 0)).flexShrink(0.0f)).key("line-" + i));
            }
            list.add(ComponentBoxService.panel(sceneCornerRadiusService -> sceneCornerRadiusService.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).direction(ScenePctService.Direction.COLUMN).padding(Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1094713344)).cornerRadius(Float.intBitsToFloat(1098907648)).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).flexShrink(0.0f), ComponentBoxService.row(layoutContainerNode -> layoutContainerNode.gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).flexShrink(0.0f), MaterialTextService.icon(ChangelogEntryFormatter.symbol((ChangelogFeatureType)entry.getKey()), MaterialIsLightService.PRIMARY).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))), MaterialTextService.label(((ChangelogFeatureType)entry.getKey()).label(), MaterialIsLightService.PRIMARY)), ComponentBoxService.column(layoutContainerNode2 -> layoutContainerNode2.gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f), (ComponentKeyService<?>[])list2.toArray(ComponentKeyService[]::new))).key(((ChangelogFeatureType)entry.getKey()).name()));
        }
        return ComponentBoxService.column(layoutContainerNode3 -> layoutContainerNode3.id("changelog.release").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).gap(Float.intBitsToFloat(1101004800)).flexShrink(0.0f), ComponentBoxService.column(layoutContainerNode4 -> layoutContainerNode4.gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f), MaterialTextService.text("ellice " + this.ff9yd7x4gnqj.version() + "  ·  " + ChangelogEntryFormatter.date(this.ff9yd7x4gnqj.date()), Float.intBitsToFloat(1094713344), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService2 -> sceneTextService2.id("changelog.version").wordWrap((boolean)(1 != 0)).flexShrink(0.0f)), MaterialTextService.text(this.ff9yd7x4gnqj.title(), this.m5wlfa93bj5p() ? Float.intBitsToFloat(1103101952) : Float.intBitsToFloat(1105199104), MaterialIsLightService.ON_SURFACE).props(sceneTextService3 -> sceneTextService3.id("changelog.title").wordWrap((boolean)(1 != 0)).flexShrink(0.0f))), ComponentBoxService.column(layoutContainerNode5 -> layoutContainerNode5.id("changelog.entries").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto()).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f), (ComponentKeyService<?>[])list.toArray(ComponentKeyService[]::new)));
    }
    
    private ComponentKeyService<?> mbibnqa5wqi8() {
        final ArrayList list = new ArrayList();
        for (ChangelogRelease changelogRelease : this.fcd03qjvzp12.releases()) {
            list.add(MaterialTextService.button("changelog.version." + changelogRelease.version(), "ellice " + changelogRelease.version() + " · " + ChangelogEntryFormatter.date(changelogRelease.date()), () -> {
                this.ff9yd7x4gnqj = ff9yd7x4gnqj;
                this.fa77am6391g = (0 != 0);
                this.mgn57kf5f52v();
                this.f9l6208fuf5j.findById("changelog.content").scrollTarget(0.0f);
                return;
            }, changelogRelease == this.ff9yd7x4gnqj).props(materialJoinedService -> materialJoinedService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minWidth(0.0f)));
        }
        return ComponentBoxService.column(layoutContainerNode -> layoutContainerNode.id("changelog.history").gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f), (ComponentKeyService<?>[])list.toArray(ComponentKeyService[]::new));
    }
}
