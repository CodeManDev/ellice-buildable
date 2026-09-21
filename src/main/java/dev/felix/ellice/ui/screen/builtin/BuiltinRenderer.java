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

public final class BuiltinRenderer implements ComponentOperationHandler {
  private final ModuleEntriesService moduleEntriesService;
  private final Runnable fedtdturpl8p;
  private final Runnable runnable2;
  private String text2 = "";
  private String text3 = "";
  private String text4 = "";
  private float value = Float.intBitsToFloat(1142292480);
  private float value2 = Float.intBitsToFloat(1144913920);
  private ComponentKeyService<?> componentKeyService;
  private ComponentKeyService<?> componentKeyService2;
  private ScenePctService<?> fgftoyhlzgvn;
  private long fesfgyrcxcsg;

  public BuiltinRenderer(
      ModuleEntriesService moduleEntriesService, Runnable runnable, Runnable runnable2) {
    this.moduleEntriesService = moduleEntriesService;
    this.fedtdturpl8p = runnable;
    this.runnable2 = runnable2;
  }

  public void viewport(float f, float f2) {
    this.value2 = f;
    this.value = f2;
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
    Object object;
    List<String> list = this.moduleEntriesService.editions();
    List<ModuleEntriesService.Entry> list2 =
        this.moduleEntriesService.search(this.text2, this.text3, this.text4).stream()
            .sorted(
                Comparator.comparingInt(
                        (ModuleEntriesService.Entry entry) -> list.indexOf(entry.edition()))
                    .thenComparing(ModuleEntriesService.Entry::group)
                    .thenComparing(ModuleEntriesService.Entry::label))
            .toList();
    ArrayList arrayList2 = new ArrayList();
    arrayList2.add(
        BuiltinRenderer.createComponentKeyService3(
            "edition.all",
            "All clients / editions",
            this.text3.isEmpty(),
            () ->
                this.updateState(
                    componentThemeService,
                    () -> {
                      this.text3 = "";
                      this.text4 = "";
                    })));
    for (String arrayList3 : this.moduleEntriesService.editions()) {
      long object3 =
          this.moduleEntriesService.entries().stream()
              .filter(entry -> entry.edition().equals(arrayList3))
              .count();
      arrayList2.add(
          BuiltinRenderer.createComponentKeyService3(
              "edition." + arrayList3,
              arrayList3 + " \u00b7 " + object3,
              this.text3.equals(arrayList3),
              () ->
                  this.updateState(
                      componentThemeService,
                      () -> {
                        this.text3 = arrayList3;
                        this.text4 = "";
                      })));
    }
    ArrayList arrayList4 = new ArrayList();
    arrayList4.add(
        BuiltinRenderer.createComponentKeyService3(
            "group.all",
            "All groups",
            this.text4.isEmpty(),
            () ->
                this.updateState(
                    componentThemeService,
                    () -> {
                      this.text4 = "";
                    })));
    for (String string : this.moduleEntriesService.groups(this.text3)) {
      arrayList4.add(
          BuiltinRenderer.createComponentKeyService3(
                  "group." + string,
                  string,
                  this.text4.equals(string),
                  () ->
                      this.updateState(
                          componentThemeService,
                          () -> {
                            this.text4 = string;
                          }))
              .children(
                  BuiltinRenderer.createComponentKeyService(string, 20),
                  MaterialTextService.label(
                      string,
                      this.text4.equals(string)
                          ? MaterialIsLightService.ON_PRIMARY
                          : MaterialIsLightService.ON_SECONDARY_CONTAINER))
              .props(
                  scenePctService ->
                      ((ScenePctService)
                              ((ScenePctService)
                                      scenePctService.direction(ScenePctService.Direction.ROW))
                                  .align(ScenePctService.Align.CENTER))
                          .gap(Float.intBitsToFloat(0x41000000))));
    }
    ArrayList arrayList = new ArrayList();
    Object object2 = "";
    for (ModuleEntriesService.Entry entry2 : list2) {
      object = entry2.edition() + " \u00b7 " + entry2.group();
      if (!((String) object).equals(object2)) {
        arrayList.add(
            MaterialTextService.text(
                    (String) object,
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService)
                                        sceneTextService.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .padding(
                                        Float.intBitsToFloat(0x41400000),
                                        Float.intBitsToFloat(0x40800000),
                                        Float.intBitsToFloat(0x40800000),
                                        Float.intBitsToFloat(0x40800000)))
                            .flexShrink(0.0f))
                .key("heading:" + entry2.id()));
        object2 = object;
      }
      boolean bl = ((String) this.moduleEntriesService.get()).equals(entry2.id());
      arrayList.add(
          ComponentBoxService.node(
                  "catalog-mode",
                  MaterialJoinedService::new,
                  materialJoinedService -> {
                    materialJoinedService
                        .colors(
                            bl
                                ? MaterialIsLightService.SECONDARY_CONTAINER
                                : MaterialIsLightService.SURFACE_CONTAINER,
                            bl
                                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                : MaterialIsLightService.ON_SURFACE)
                        .shape(Float.intBitsToFloat(0x41400000), 0.0f);
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            ((SceneCornerRadiusService)
                                                                    ((SceneCornerRadiusService)
                                                                            ((SceneCornerRadiusService)
                                                                                    materialJoinedService
                                                                                        .id(
                                                                                            "catalog.option."
                                                                                                + entry2
                                                                                                    .id()))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .minHeight(
                                                                            Float.intBitsToFloat(
                                                                                1116471296)))
                                                                .padding(
                                                                    Float.intBitsToFloat(
                                                                        0x41400000)))
                                                        .direction(ScenePctService.Direction.ROW))
                                                .align(ScenePctService.Align.CENTER))
                                        .gap(Float.intBitsToFloat(0x41400000)))
                                .flexShrink(0.0f))
                        .onClick(
                            () -> {
                              if (this.moduleEntriesService.isActive()) {
                                this.moduleEntriesService.set(entry2.id());
                                this.fedtdturpl8p.run();
                                this.runnable2.run();
                              }
                            });
                  },
                  BuiltinRenderer.createComponentKeyService(entry2.group(), 36),
                  ComponentBoxService.column(
                      layoutContainerNode ->
                          ((LayoutContainerNode)
                                  ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                      .minWidth(0.0f))
                              .gap(Float.intBitsToFloat(0x40800000)),
                      MaterialTextService.text(
                              entry2.label() + (entry2.deprecated() ? " \u00b7 archived" : ""),
                              Float.intBitsToFloat(1097859072),
                              MaterialIsLightService.ON_SURFACE)
                          .props(
                              sceneTextService ->
                                  ((SceneTextService)
                                          sceneTextService.width(
                                              LayoutOperationHandler.percent(
                                                  Float.intBitsToFloat(1120403456))))
                                      .wordWrap(true)),
                      MaterialTextService.text(
                              entry2.description(),
                              Float.intBitsToFloat(0x41400000),
                              MaterialIsLightService.ON_SURFACE_VARIANT)
                          .props(
                              sceneTextService ->
                                  ((SceneTextService)
                                          sceneTextService.width(
                                              LayoutOperationHandler.percent(
                                                  Float.intBitsToFloat(1120403456))))
                                      .wordWrap(true))),
                  MaterialTextService.icon(
                          bl ? "check" : "chevron_right",
                          bl
                              ? MaterialIsLightService.PRIMARY
                              : MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(
                          sceneSrcService ->
                              sceneSrcService.size(
                                  Float.intBitsToFloat(1101004800),
                                  Float.intBitsToFloat(1101004800))))
              .key(entry2.id()));
    }
    if (arrayList.isEmpty()) {
      arrayList.add(
          ComponentBoxService.column(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              ((LayoutContainerNode)
                                      layoutContainerNode.width(
                                          LayoutOperationHandler.percent(
                                              Float.intBitsToFloat(1120403456))))
                                  .padding(
                                      Float.intBitsToFloat(1101004800),
                                      Float.intBitsToFloat(0x41000000)))
                          .gap(Float.intBitsToFloat(0x41400000)),
                  MaterialTextService.text(
                      "No matching modes",
                      Float.intBitsToFloat(1099956224),
                      MaterialIsLightService.ON_SURFACE),
                  MaterialTextService.text(
                          "Try another name or clear the filters.",
                          Float.intBitsToFloat(1095761920),
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(sceneTextService -> sceneTextService.wordWrap(true)),
                  MaterialTextService.button(
                      "catalog.reset",
                      "Clear filters",
                      () -> {
                        this.text4 = "";
                        this.text3 = "";
                        this.text2 = "";
                        componentThemeService.invalidate();
                      },
                      false))
              .key("empty"));
    }
    float f =
        Math.max(
            Float.intBitsToFloat(1126170624),
            Math.min(
                Float.intBitsToFloat(1144913920),
                this.value
                    - (float) (this.value2 < Float.intBitsToFloat(0x44200000) ? 16 : 48)));
    ComponentKeyService<LayoutContainerNode> componentKeyService =
        ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            ((LayoutContainerNode)
                                                                    ((LayoutContainerNode)
                                                                            layoutContainerNode.id(
                                                                                "catalog.results"))
                                                                        .size(
                                                                            LayoutOperationHandler
                                                                                .percent(
                                                                                    Float
                                                                                        .intBitsToFloat(
                                                                                            1120403456)),
                                                                            LayoutOperationHandler
                                                                                .percent(
                                                                                    Float
                                                                                        .intBitsToFloat(
                                                                                            1120403456))))
                                                                .minHeight(0.0f))
                                                        .gap(Float.intBitsToFloat(0x40800000)))
                                                .scrollable(true))
                                        .clip(true))
                                .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                        .scrollbarColor(MaterialIsLightService.OUTLINE),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))
            .key("results:" + this.text3 + ":" + this.text4 + ":" + this.text2);
    this.componentKeyService = componentKeyService;
    ArrayList<ComponentKeyService<?>> resultPages = new ArrayList<>();
    resultPages.add(
        componentKeyService.onMount(
            scenePctService -> {
              this.fgftoyhlzgvn = scenePctService;
              MaterialEnterService.reveal(scenePctService, 0.0f, Float.intBitsToFloat(0x41400000));
            }));
    if (this.componentKeyService2 != null) {
      long l = this.fesfgyrcxcsg;
      resultPages.add(
          MaterialFindByIdSelector.of(
              this.componentKeyService2,
              0,
              l,
              () -> {
                if (l == this.fesfgyrcxcsg) {
                  this.componentKeyService2 = null;
                  componentThemeService.invalidate();
                }
              }));
    }
    return ComponentBoxService.panel(
            ComponentStyleService.style()
                .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                .maxHeight(LayoutOperationHandler.px(f))
                .padding(
                    this.value2 < Float.intBitsToFloat(0x44200000)
                        ? Float.intBitsToFloat(0x41400000)
                        : Float.intBitsToFloat(1101004800))
                .gap(Float.intBitsToFloat(1092616192))
                .clip(true)
                .build(),
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService) sceneCornerRadiusService.id("clickgui.dialog"))
                            .direction(ScenePctService.Direction.COLUMN))
                    .cornerRadius(Float.intBitsToFloat(1103101952))
                    .backgroundColor(MaterialIsLightService.SURFACE_HIGH),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .gap(Float.intBitsToFloat(0x41000000)))
                                .align(ScenePctService.Align.CENTER))
                        .flexShrink(0.0f),
                MaterialTextService.text(
                        "Choose a mode",
                        Float.intBitsToFloat(1102053376),
                        MaterialIsLightService.ON_SURFACE)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                MaterialTextService.button("catalog.close", "Close", this.fedtdturpl8p, false)),
            MaterialTextService.text(
                    "Selected: "
                        + this.moduleEntriesService.selected().edition()
                        + " / "
                        + this.moduleEntriesService.selected().label(),
                    Float.intBitsToFloat(1095761920),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService) sceneTextService.id("catalog.selected"))
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .wordWrap(true)
                            .flexShrink(0.0f)),
            ComponentBoxService.node(
                    "catalog-search",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService -> {
                      MaterialTextService.input(controlLetterSpacingService);
                      ((ControlLetterSpacingService)
                              ((ControlLetterSpacingService)
                                      ((ControlLetterSpacingService)
                                              ((ControlLetterSpacingService)
                                                      controlLetterSpacingService.id(
                                                          "catalog.search"))
                                                  .width(
                                                      LayoutOperationHandler.percent(
                                                          Float.intBitsToFloat(1120403456))))
                                          .height(
                                              LayoutOperationHandler.px(
                                                  Float.intBitsToFloat(1110441984))))
                                  .flexShrink(0.0f))
                          .fontSize(Float.intBitsToFloat(1097859072))
                          .maxLength(100)
                          .placeholder("Search clients, modes or behavior");
                      if (!controlLetterSpacingService.focused()) {
                        controlLetterSpacingService.text(this.text2);
                      }
                      controlLetterSpacingService.onChanged(
                          string -> {
                            this.componentKeyService2 = null;
                            ++this.fesfgyrcxcsg;
                            this.text2 = string;
                            componentThemeService.invalidate();
                          });
                    },
                    new ComponentKeyService[0])
                .key("search"),
            BuiltinRenderer.createComponentKeyService2("catalog.editions", arrayList2),
            BuiltinRenderer.createComponentKeyService2("catalog.groups", arrayList4),
            MaterialTextService.text(
                    list2.size() + " of " + this.moduleEntriesService.entries().size() + " modes",
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.id("catalog.count")).flexShrink(0.0f)),
            ComponentBoxService.stack(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        layoutContainerNode.width(
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            Math.max(
                                                                Float.intBitsToFloat(1117782016),
                                                                f
                                                                    - Float.intBitsToFloat(
                                                                        1132331008)))))
                                            .minHeight(0.0f))
                                    .flexShrink(1.0f))
                            .clip(true),
                    resultPages.toArray(ComponentKeyService[]::new))
                .key("result-pages"))
        .key("catalog");
  }

  private void updateState(ComponentThemeService componentThemeService, Runnable runnable) {
    float f = this.fgftoyhlzgvn == null ? 0.0f : this.fgftoyhlzgvn.scrollY();
    this.componentKeyService2 =
        this.componentKeyService == null
            ? null
            : this.componentKeyService.props(scenePctService -> scenePctService.scrollY(f));
    ++this.fesfgyrcxcsg;
    runnable.run();
    componentThemeService.invalidate();
  }

  private static ComponentKeyService<?> createComponentKeyService(String string, int n) {
    return ComponentBoxService.node(
            "catalog-brand",
            SceneBrandService::new,
            sceneBrandService ->
                ((SceneBrandService)
                        ((SceneBrandService)
                                ((SceneBrandService) sceneBrandService.brand(string).size(n, n))
                                    .flexShrink(0.0f))
                            .pointerEvents(false))
                    .tooltip(string),
            new ComponentKeyService[0])
        .key("brand");
  }

  private static ComponentKeyService<?> createComponentKeyService2(
      String string, List<ComponentKeyService<?>> list) {
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        layoutContainerNode.id(
                                                                            string))
                                                                    .width(
                                                                        LayoutOperationHandler
                                                                            .percent(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1120403456))))
                                                            .height(
                                                                LayoutOperationHandler.px(
                                                                    Float.intBitsToFloat(
                                                                        0x42200000))))
                                                    .gap(Float.intBitsToFloat(0x40C00000)))
                                            .scrollable(true))
                                    .scrollbarWidth(0.0f))
                            .clip(true))
                    .flexShrink(0.0f),
            (ComponentKeyService[]) list.toArray(ComponentKeyService[]::new))
        .key(string);
  }

  private static ComponentKeyService<?> createComponentKeyService3(
      String string, String string2, boolean bl, Runnable runnable) {
    return MaterialTextService.button("catalog." + string, string2, runnable, bl)
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        materialJoinedService.height(
                            LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))))
                    .flexShrink(0.0f))
        .key(string);
  }
}
