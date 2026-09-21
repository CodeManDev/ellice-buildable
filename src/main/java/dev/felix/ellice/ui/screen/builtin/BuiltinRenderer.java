


package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.module.ModuleEntriesService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialFindByIdSelector;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneBrandService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class BuiltinRenderer
implements ComponentOperationHandler {
    private final ModuleEntriesService fepx93roq0ff;
    private final Runnable fedtdturpl8p;
    private final Runnable fjnzp050jnhu;
    private String f5zth1shwbzs = "";
    private String fdr7e9u6ob2e = "";
    private String fea4e4kdcuec = "";
    private float f8zs0lozmzah = Float.intBitsToFloat(1142292480);
    private float f2wjsovc0a53 = Float.intBitsToFloat(1144913920);
    private ComponentKeyService<?> f7cn4nlsts9f;
    private ComponentKeyService<?> fiu010xupgdd;
    private ScenePctService<?> fgftoyhlzgvn;
    private long fesfgyrcxcsg;

    public BuiltinRenderer(ModuleEntriesService moduleEntriesService, Runnable runnable, Runnable runnable2) {
        this.fepx93roq0ff = moduleEntriesService;
        this.fedtdturpl8p = runnable;
        this.fjnzp050jnhu = runnable2;
    }

    public void viewport(float f, float f2) {
        this.f2wjsovc0a53 = f;
        this.f8zs0lozmzah = f2;
    }

    @Override
    public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
        Object object;
        List<String> list = this.fepx93roq0ff.editions();
        List<ModuleEntriesService.Entry> list2 = this.fepx93roq0ff.search(this.f5zth1shwbzs, this.fdr7e9u6ob2e, this.fea4e4kdcuec).stream().sorted(Comparator.comparingInt((ModuleEntriesService.Entry entry) -> list.indexOf(entry.edition())).thenComparing(ModuleEntriesService.Entry::group).thenComparing(ModuleEntriesService.Entry::label)).toList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(BuiltinRenderer.m3tz22l5znoc("edition.all", "All clients / editions", this.fdr7e9u6ob2e.isEmpty(), () -> this.m2zauq6wiess(componentThemeService, () -> {
            this.fdr7e9u6ob2e = "";
            this.fea4e4kdcuec = "";
        })));
        for (String arrayList3 : this.fepx93roq0ff.editions()) {
            long object3 = this.fepx93roq0ff.entries().stream().filter(entry -> entry.edition().equals(arrayList3)).count();
            arrayList2.add(BuiltinRenderer.m3tz22l5znoc("edition." + arrayList3, arrayList3 + " \u00b7 " + object3, this.fdr7e9u6ob2e.equals(arrayList3), () -> this.m2zauq6wiess(componentThemeService, () -> {
                this.fdr7e9u6ob2e = arrayList3;
                this.fea4e4kdcuec = "";
            })));
        }
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(BuiltinRenderer.m3tz22l5znoc("group.all", "All groups", this.fea4e4kdcuec.isEmpty(), () -> this.m2zauq6wiess(componentThemeService, () -> {
            this.fea4e4kdcuec = "";
        })));
        for (String string : this.fepx93roq0ff.groups(this.fdr7e9u6ob2e)) {
            arrayList4.add(BuiltinRenderer.m3tz22l5znoc("group." + string, string, this.fea4e4kdcuec.equals(string), () -> this.m2zauq6wiess(componentThemeService, () -> {
                this.fea4e4kdcuec = string;
            })).children(BuiltinRenderer.m1kjvqpug2ue(string, 20), MaterialTextService.label(string, this.fea4e4kdcuec.equals(string) ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER)).props(scenePctService -> ((ScenePctService)((ScenePctService)scenePctService.direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).gap(Float.intBitsToFloat(0x41000000))));
        }
        ArrayList arrayList = new ArrayList();
        Object object2 = "";
        for (ModuleEntriesService.Entry entry2 : list2) {
            object = entry2.edition() + " \u00b7 " + entry2.group();
            if (!((String)object).equals(object2)) {
                arrayList.add(MaterialTextService.text((String)object, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x40800000))).flexShrink(0.0f)).key("heading:" + entry2.id()));
                object2 = object;
            }
            boolean bl = ((String)this.fepx93roq0ff.get()).equals(entry2.id());
            arrayList.add(ComponentBoxService.node("catalog-mode", MaterialJoinedService::new, materialJoinedService -> {
                materialJoinedService.colors(bl ? MaterialIsLightService.SECONDARY_CONTAINER : MaterialIsLightService.SURFACE_CONTAINER, bl ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(0x41400000), 0.0f);
                ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id("catalog.option." + entry2.id())).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minHeight(Float.intBitsToFloat(1116471296))).padding(Float.intBitsToFloat(0x41400000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).gap(Float.intBitsToFloat(0x41400000))).flexShrink(0.0f)).onClick(() -> {
                    if (this.fepx93roq0ff.isActive()) {
                        this.fepx93roq0ff.set(entry2.id());
                        this.fedtdturpl8p.run();
                        this.fjnzp050jnhu.run();
                    }
                });
            }, BuiltinRenderer.m1kjvqpug2ue(entry2.group(), 36), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).gap(Float.intBitsToFloat(0x40800000)), MaterialTextService.text(entry2.label() + (entry2.deprecated() ? " \u00b7 archived" : ""), Float.intBitsToFloat(1097859072), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)), MaterialTextService.text(entry2.description(), Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true))), MaterialTextService.icon(bl ? "check" : "chevron_right", bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800)))).key(entry2.id()));
        }
        if (arrayList.isEmpty()) {
            arrayList.add(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x41400000)), MaterialTextService.text("No matching modes", Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE), MaterialTextService.text("Try another name or clear the filters.", Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> sceneTextService.wordWrap(true)), MaterialTextService.button("catalog.reset", "Clear filters", () -> {
                this.fea4e4kdcuec = "";
                this.fdr7e9u6ob2e = "";
                this.f5zth1shwbzs = "";
                componentThemeService.invalidate();
            }, false)).key("empty"));
        }
        float f = Math.max(Float.intBitsToFloat(1126170624), Math.min(Float.intBitsToFloat(1144913920), this.f8zs0lozmzah - (float)(this.f2wjsovc0a53 < Float.intBitsToFloat(0x44200000) ? 16 : 48)));
        ComponentKeyService<LayoutContainerNode> componentKeyService = ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("catalog.results")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minHeight(0.0f)).gap(Float.intBitsToFloat(0x40800000))).scrollable(true)).clip(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).key("results:" + this.fdr7e9u6ob2e + ":" + this.fea4e4kdcuec + ":" + this.f5zth1shwbzs);
        this.f7cn4nlsts9f = componentKeyService;
        ArrayList<ComponentKeyService<?>> resultPages = new ArrayList<>();
        resultPages.add(componentKeyService.onMount(scenePctService -> {
            this.fgftoyhlzgvn = scenePctService;
            MaterialEnterService.reveal(scenePctService, 0.0f, Float.intBitsToFloat(0x41400000));
        }));
        if (this.fiu010xupgdd != null) {
            long l = this.fesfgyrcxcsg;
            resultPages.add(MaterialFindByIdSelector.of(this.fiu010xupgdd, 0, l, () -> {
                if (l == this.fesfgyrcxcsg) {
                    this.fiu010xupgdd = null;
                    componentThemeService.invalidate();
                }
            }));
        }
        return ComponentBoxService.panel(ComponentStyleService.style().width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxHeight(LayoutOperationHandler.px(f)).padding(this.f2wjsovc0a53 < Float.intBitsToFloat(0x44200000) ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1101004800)).gap(Float.intBitsToFloat(1092616192)).clip(true).build(), sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("clickgui.dialog")).direction(ScenePctService.Direction.COLUMN)).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_HIGH), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.text("Choose a mode", Float.intBitsToFloat(1102053376), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)), MaterialTextService.button("catalog.close", "Close", this.fedtdturpl8p, false)), MaterialTextService.text("Selected: " + this.fepx93roq0ff.selected().edition() + " / " + this.fepx93roq0ff.selected().label(), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.id("catalog.selected")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true).flexShrink(0.0f)), ComponentBoxService.node("catalog-search", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            MaterialTextService.input(controlLetterSpacingService);
            ((ControlLetterSpacingService)((ControlLetterSpacingService)((ControlLetterSpacingService)((ControlLetterSpacingService)controlLetterSpacingService.id("catalog.search")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1110441984)))).flexShrink(0.0f)).fontSize(Float.intBitsToFloat(1097859072)).maxLength(100).placeholder("Search clients, modes or behavior");
            if (!controlLetterSpacingService.focused()) {
                controlLetterSpacingService.text(this.f5zth1shwbzs);
            }
            controlLetterSpacingService.onChanged(string -> {
                this.fiu010xupgdd = null;
                ++this.fesfgyrcxcsg;
                this.f5zth1shwbzs = string;
                componentThemeService.invalidate();
            });
        }, new ComponentKeyService[0]).key("search"), BuiltinRenderer.me22cm6l0kaa("catalog.editions", arrayList2), BuiltinRenderer.me22cm6l0kaa("catalog.groups", arrayList4), MaterialTextService.text(list2.size() + " of " + this.fepx93roq0ff.entries().size() + " modes", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.id("catalog.count")).flexShrink(0.0f)), ComponentBoxService.stack(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Math.max(Float.intBitsToFloat(1117782016), f - Float.intBitsToFloat(1132331008))))).minHeight(0.0f)).flexShrink(1.0f)).clip(true), resultPages.toArray(ComponentKeyService[]::new)).key("result-pages")).key("catalog");
    }

    private void m2zauq6wiess(ComponentThemeService componentThemeService, Runnable runnable) {
        float f = this.fgftoyhlzgvn == null ? 0.0f : this.fgftoyhlzgvn.scrollY();
        this.fiu010xupgdd = this.f7cn4nlsts9f == null ? null : this.f7cn4nlsts9f.props(scenePctService -> scenePctService.scrollY(f));
        ++this.fesfgyrcxcsg;
        runnable.run();
        componentThemeService.invalidate();
    }

    private static ComponentKeyService<?> m1kjvqpug2ue(String string, int n) {
        return ComponentBoxService.node("catalog-brand", SceneBrandService::new, sceneBrandService -> ((SceneBrandService)((SceneBrandService)((SceneBrandService)sceneBrandService.brand(string).size(n, n)).flexShrink(0.0f)).pointerEvents(false)).tooltip(string), new ComponentKeyService[0]).key("brand");
    }

    private static ComponentKeyService<?> me22cm6l0kaa(String string, List<ComponentKeyService<?>> list) {
        return ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id(string)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(0x42200000)))).gap(Float.intBitsToFloat(0x40C00000))).scrollable(true)).scrollbarWidth(0.0f)).clip(true)).flexShrink(0.0f), (ComponentKeyService[])list.toArray(ComponentKeyService[]::new)).key(string);
    }

    private static ComponentKeyService<?> m3tz22l5znoc(String string, String string2, boolean bl, Runnable runnable) {
        return MaterialTextService.button("catalog." + string, string2, runnable, bl).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))).flexShrink(0.0f)).key(string);
    }
}
