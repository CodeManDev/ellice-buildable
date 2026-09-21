package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleIdService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialSelectionSelector;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class PresetsComponent {
  private PresetsComponent() {}

  static ComponentKeyService<?> render(
      Module module,
      String string,
      boolean bl,
      boolean bl2,
      Consumer<String> consumer,
      Runnable runnable,
      Runnable runnable2) {
    List<ModuleIdService> list = module.presets();
    ModuleIdService moduleIdService2 =
        list.stream()
            .filter(moduleIdService -> moduleIdService.matches(module))
            .findFirst()
            .orElse(null);
    ModuleIdService moduleIdService3 =
        list.stream()
            .filter(moduleIdService -> moduleIdService.id().equals(string))
            .findFirst()
            .orElse(list.getFirst());
    int n = list.indexOf(moduleIdService3);
    List<ModuleIdService.Change> list2 = moduleIdService3.changes(module);
    ArrayList arrayList = new ArrayList();
    for (ModuleIdService moduleIdService4 : list) {
      arrayList.add(
          PresetsComponent.createComponentKeyService2(
              moduleIdService4, moduleIdService3, moduleIdService2, list.size(), consumer));
    }
    ArrayList arrayList2 = new ArrayList();
    arrayList2.add(
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
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(2.0f),
                    MaterialTextService.label("Presets", MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService ->
                                sceneTextService
                                    .fontSize(Float.intBitsToFloat(1098907648))
                                    .lineHeight(Float.intBitsToFloat(1102053376))),
                    ComponentBoxService.row(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    layoutContainerNode.gap(Float.intBitsToFloat(0x40A00000)))
                                .align(ScenePctService.Align.CENTER),
                        MaterialTextService.text(
                            "Using",
                            Float.intBitsToFloat(1093664768),
                            MaterialIsLightService.OUTLINE),
                        MaterialTextService.text(
                                moduleIdService2 == null ? "Custom" : moduleIdService2.name(),
                                Float.intBitsToFloat(1093664768),
                                MaterialIsLightService.PRIMARY)
                            .props(
                                sceneTextService ->
                                    ((SceneTextService)
                                            ((SceneTextService)
                                                    ((SceneTextService)
                                                            sceneTextService.id(
                                                                "clickgui.preset.active"))
                                                        .minWidth(0.0f))
                                                .flexShrink(1.0f))
                                        .tooltip(
                                            "Your current settings match "
                                                + (String)
                                                    (moduleIdService2 == null
                                                        ? "no preset."
                                                        : moduleIdService2.name() + "."))))),
                MaterialTextService.text(
                        n + 1 + " / " + list.size(),
                        Float.intBitsToFloat(1093664768),
                        MaterialIsLightService.OUTLINE)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.id("clickgui.preset.position"))
                                .tooltip(
                                    "Browse every preset with the arrows or scroll across the"
                                        + " cards")),
                ComponentBoxService.row(
                    layoutContainerNode -> layoutContainerNode.gap(2.0f),
                    PresetsComponent.createComponentKeyService(
                        "previous",
                        "arrow_back",
                        "Previous preset",
                        n > 0,
                        () -> consumer.accept(((ModuleIdService) list.get(n - 1)).id())),
                    PresetsComponent.createComponentKeyService(
                        "next",
                        "chevron_right",
                        "Next preset",
                        n + 1 < list.size(),
                        () -> consumer.accept(((ModuleIdService) list.get(n + 1)).id()))))
            .key("heading"));
    arrayList2.add(
        ComponentBoxService.node(
                "material-preset-deck",
                MaterialSelectionSelector::new,
                materialSelectionSelector ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            ((LayoutContainerNode)
                                                                    ((LayoutContainerNode)
                                                                            ((LayoutContainerNode)
                                                                                    materialSelectionSelector
                                                                                        .selection(
                                                                                            "clickgui.preset."
                                                                                                + moduleIdService3
                                                                                                    .id())
                                                                                        .id(
                                                                                            "clickgui.presets.choices"))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .height(
                                                                            LayoutOperationHandler
                                                                                .px(
                                                                                    Float
                                                                                        .intBitsToFloat(
                                                                                            1118830592))))
                                                                .padding(
                                                                    0.0f,
                                                                    0.0f,
                                                                    Float.intBitsToFloat(
                                                                        1092616192),
                                                                    0.0f))
                                                        .gap(Float.intBitsToFloat(0x41000000)))
                                                .scrollable(true))
                                        .scrollbarWidth(0.0f))
                                .clip(true))
                        .flexShrink(0.0f),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))
            .key("choices"));
    arrayList2.add(
        ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .gap(Float.intBitsToFloat(0x41400000)))
                        .flexShrink(0.0f),
                MaterialTextService.text(
                        moduleIdService3.description(),
                        Float.intBitsToFloat(1095761920),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneTextService ->
                            ((SceneTextService)
                                    ((SceneTextService)
                                            sceneTextService.id("clickgui.preset.description"))
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .wordWrap(true)
                                .flexShrink(0.0f)),
                PresetsComponent.createComponentKeyService3(moduleIdService3, list2, bl, runnable, runnable2))
            .key("preview:" + moduleIdService3.id())
            .onMount(
                layoutContainerNode ->
                    MaterialEnterService.reveal(
                        layoutContainerNode, 0.0f, Float.intBitsToFloat(0x40C00000))));
    if (bl && !list2.isEmpty()) {
      arrayList2.add(PresetsComponent.createComponentKeyService4(list2, bl2));
    }
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        sceneCornerRadiusService.id(
                                                            "clickgui.presets"))
                                                    .width(
                                                        LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                            .padding(Float.intBitsToFloat(1098907648)))
                                    .gap(Float.intBitsToFloat(1096810496)))
                            .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                            .cornerRadius(Float.intBitsToFloat(1102053376))
                            .direction(ScenePctService.Direction.COLUMN))
                    .flexShrink(0.0f),
            (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
        .key("presets");
  }

  private static ComponentKeyService<?> createComponentKeyService(
      String string, String string2, String string3, boolean bl, Runnable runnable) {
    return MaterialTextService.iconButton("clickgui.preset." + string, string2, string3, runnable)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .colors(
                      MaterialIsLightService.SURFACE_HIGH,
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .disabledSurface(0)
                  .shape(Float.intBitsToFloat(1093664768), Float.intBitsToFloat(1106247680))
                  .surfaceWidth(Float.intBitsToFloat(1106247680));
              materialJoinedService
                  .available(bl)
                  .size(Float.intBitsToFloat(0x42000000), Float.intBitsToFloat(0x42000000));
            })
        .children(
            MaterialTextService.icon(
                    string2,
                    bl
                        ? MaterialIsLightService.ON_SURFACE_VARIANT
                        : MaterialIsLightService.OUTLINE_VARIANT)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))));
  }

  private static ComponentKeyService<?> createComponentKeyService2(
      ModuleIdService moduleIdService,
      ModuleIdService moduleIdService2,
      ModuleIdService moduleIdService3,
      int n,
      Consumer<String> consumer) {
    int n2;
    boolean bl = moduleIdService.id().equals(moduleIdService2.id());
    boolean bl2 = moduleIdService3 != null && moduleIdService3.id().equals(moduleIdService.id());
    int n3 =
        n2 = bl ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE;
    String string =
        moduleIdService.recommended()
            ? "Recommended"
            : (moduleIdService.id().equals("defaults") ? "Original settings" : "Alternative");
    return MaterialTextService.button(
            "clickgui.preset." + moduleIdService.id(),
            moduleIdService.name(),
            () -> consumer.accept(moduleIdService.id()),
            false)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .colors(bl ? 0 : MaterialIsLightService.SURFACE_HIGH, n2)
                  .shape(Float.intBitsToFloat(1098907648), 0.0f);
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      ((SceneCornerRadiusService)
                                                              ((SceneCornerRadiusService)
                                                                      ((SceneCornerRadiusService)
                                                                              ((SceneCornerRadiusService)
                                                                                      ((SceneCornerRadiusService)
                                                                                              materialJoinedService
                                                                                                  .width(
                                                                                                      LayoutOperationHandler
                                                                                                          .px(
                                                                                                              Float
                                                                                                                  .intBitsToFloat(
                                                                                                                      1125646336))))
                                                                                          .height(
                                                                                              LayoutOperationHandler
                                                                                                  .px(
                                                                                                      Float
                                                                                                          .intBitsToFloat(
                                                                                                              1117519872))))
                                                                                  .minWidth(
                                                                                      Float
                                                                                          .intBitsToFloat(
                                                                                              0x43040000)))
                                                                          .flex(
                                                                              n <= 3 ? 1.0f : 0.0f))
                                                                  .flexShrink(0.0f))
                                                          .padding(
                                                              Float.intBitsToFloat(1092616192),
                                                              Float.intBitsToFloat(0x41400000)))
                                                  .gap(Float.intBitsToFloat(0x41000000)))
                                          .direction(ScenePctService.Direction.COLUMN))
                                  .align(ScenePctService.Align.STRETCH))
                          .justify(ScenePctService.Justify.CENTER))
                  .tooltip(
                      moduleIdService.name()
                          + "\n"
                          + moduleIdService.description()
                          + (bl2 ? "\nMatches your current settings" : ""));
            })
        .children(
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .gap(Float.intBitsToFloat(0x40C00000)))
                        .align(ScenePctService.Align.CENTER),
                MaterialTextService.icon(
                        moduleIdService.recommended()
                            ? "star_filled"
                            : (moduleIdService.id().equals("defaults") ? "history" : "tune"),
                        bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1096810496),
                                Float.intBitsToFloat(1096810496))),
                MaterialTextService.text(
                        string,
                        Float.intBitsToFloat(1092616192),
                        bl
                            ? MaterialIsLightService.PRIMARY
                            : MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                MaterialTextService.icon(
                        bl2 ? "check" : (bl ? "radio_button_checked" : "radio_button_unchecked"),
                        bl2
                            ? MaterialIsLightService.PRIMARY
                            : (bl
                                ? MaterialIsLightService.PRIMARY
                                : MaterialIsLightService.OUTLINE_VARIANT))
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1096810496),
                                Float.intBitsToFloat(1096810496)))),
            MaterialTextService.text(moduleIdService.name(), Float.intBitsToFloat(1098907648), n2)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService)
                                        sceneTextService.id(
                                            "clickgui.preset." + moduleIdService.id() + ".title"))
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .fontFamily("material-roboto-medium")
                            .lineHeight(Float.intBitsToFloat(1102053376))))
        .key(moduleIdService.id());
  }

  private static ComponentKeyService<?> createComponentKeyService3(
      ModuleIdService moduleIdService,
      List<ModuleIdService.Change> list,
      boolean bl,
      Runnable runnable,
      Runnable runnable2) {
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.icon(
                    list.isEmpty() ? "check" : "tune",
                    list.isEmpty()
                        ? MaterialIsLightService.PRIMARY
                        : MaterialIsLightService.OUTLINE)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648))),
            MaterialTextService.text(
                    (String) (list.isEmpty() ? "Already applied" : list.size() + " changes"),
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f))
                            .wordWrap(true))));
    if (!list.isEmpty()) {
      arrayList.add(
          MaterialTextService.button(
                  "clickgui.preset.preview", bl ? "Hide review" : "Review", runnable, false)
              .props(
                  materialJoinedService -> {
                    materialJoinedService
                        .colors(0, MaterialIsLightService.ON_SURFACE_VARIANT)
                        .shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1108344832));
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            materialJoinedService.minWidth(
                                                Float.intBitsToFloat(0x42400000)))
                                        .height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(0x42200000))))
                                .padding(0.0f, Float.intBitsToFloat(0x41000000)))
                        .tooltip(
                            (String)
                                (bl
                                    ? "Hide the change comparison"
                                    : "Review all "
                                        + list.size()
                                        + " setting changes before applying"));
                  })
              .children(
                  MaterialTextService.text(
                      bl ? "Hide review" : "Review",
                      Float.intBitsToFloat(0x41400000),
                      MaterialIsLightService.ON_SURFACE_VARIANT)));
    }
    arrayList.add(
        MaterialTextService.button(
                "clickgui.preset.apply",
                list.isEmpty() ? "Applied" : "Apply preset",
                runnable2,
                true)
            .props(
                materialJoinedService -> {
                  materialJoinedService
                      .shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1108344832))
                      .disabledSurface(MaterialIsLightService.SURFACE_HIGH);
                  ((SceneCornerRadiusService)
                          ((SceneCornerRadiusService)
                                  materialJoinedService
                                      .available(!list.isEmpty())
                                      .height(
                                          LayoutOperationHandler.px(
                                              Float.intBitsToFloat(0x42200000))))
                              .padding(0.0f, Float.intBitsToFloat(1096810496)))
                      .tooltip(
                          "Apply preset "
                              + moduleIdService.name()
                              + ". Keeps module state, shortcuts and text.");
                })
            .children(
                MaterialTextService.text(
                    list.isEmpty() ? "Applied" : "Apply preset",
                    Float.intBitsToFloat(1095761920),
                    list.isEmpty()
                        ? MaterialIsLightService.OUTLINE
                        : MaterialIsLightService.ON_PRIMARY)));
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .gap(Float.intBitsToFloat(0x40C00000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new));
  }

  private static ComponentKeyService<?> createComponentKeyService4(
      List<ModuleIdService.Change> list, boolean bl) {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
    arrayList.add(
        ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                                .gap(Float.intBitsToFloat(0x41000000)))
                                        .align(ScenePctService.Align.CENTER))
                                .flexShrink(0.0f))
                        .padding(0.0f, 2.0f, Float.intBitsToFloat(0x40800000), 2.0f),
                MaterialTextService.text(
                        "CHANGE PREVIEW",
                        Float.intBitsToFloat(1092616192),
                        MaterialIsLightService.OUTLINE)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                MaterialTextService.text(
                    list.size() + " settings",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.OUTLINE))
            .key("heading"));
    if (!bl) {
      arrayList.add(
          ComponentBoxService.row(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              ((LayoutContainerNode)
                                      ((LayoutContainerNode)
                                              ((LayoutContainerNode)
                                                      layoutContainerNode.width(
                                                          LayoutOperationHandler.percent(
                                                              Float.intBitsToFloat(1120403456))))
                                                  .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                                          .gap(Float.intBitsToFloat(0x41400000)))
                                  .align(ScenePctService.Align.CENTER))
                          .flexShrink(0.0f),
                  MaterialTextService.text(
                          "SETTING",
                          Float.intBitsToFloat(1092616192),
                          MaterialIsLightService.OUTLINE)
                      .props(
                          sceneTextService ->
                              ((SceneTextService)
                                      sceneTextService.flex(Float.intBitsToFloat(1069547520)))
                                  .minWidth(0.0f)),
                  MaterialTextService.text(
                          "CURRENT",
                          Float.intBitsToFloat(1092616192),
                          MaterialIsLightService.OUTLINE)
                      .props(
                          sceneTextService ->
                              ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                  ComponentBoxService.box(
                      layoutContainerNode ->
                          ((LayoutContainerNode)
                                  layoutContainerNode.size(Float.intBitsToFloat(1096810496), 1.0f))
                              .flexShrink(0.0f),
                      new ComponentKeyService[0]),
                  MaterialTextService.text(
                          "PRESET",
                          Float.intBitsToFloat(1092616192),
                          MaterialIsLightService.OUTLINE)
                      .props(
                          sceneTextService ->
                              ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)))
              .key("columns"));
    }
    for (ModuleIdService.Change change : list) {
      ComponentKeyService<SceneTextService> componentKeyService =
          MaterialTextService.text(
                  change.setting(),
                  Float.intBitsToFloat(0x41400000),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService ->
                      ((SceneTextService)
                              ((SceneTextService)
                                      ((SceneTextService)
                                              sceneTextService.width(
                                                  bl
                                                      ? LayoutOperationHandler.percent(
                                                          Float.intBitsToFloat(1120403456))
                                                      : LayoutOperationHandler.auto()))
                                          .flex(bl ? 0.0f : Float.intBitsToFloat(1069547520)))
                                  .minWidth(0.0f))
                          .wordWrap(true)
                          .flexShrink(0.0f));
      ArrayList arrayList2 = new ArrayList();
      arrayList2.add(componentKeyService);
      if (bl) {
        arrayList2.add(
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
                PresetsComponent.createComponentKeyService5(change.previous(), false, true),
                MaterialTextService.icon("chevron_right", MaterialIsLightService.OUTLINE)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1096810496),
                                Float.intBitsToFloat(1096810496))),
                PresetsComponent.createComponentKeyService5(change.value(), true, true)));
      } else {
        arrayList2.add(
            PresetsComponent.createComponentKeyService5(change.previous(), false, false));
        arrayList2.add(
            MaterialTextService.icon("chevron_right", MaterialIsLightService.OUTLINE)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1096810496))));
        arrayList2.add(PresetsComponent.createComponentKeyService5(change.value(), true, false));
      }
      arrayList.add(
          ComponentBoxService.panel(
                  sceneCornerRadiusService ->
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      ((SceneCornerRadiusService)
                                                              ((SceneCornerRadiusService)
                                                                      ((SceneCornerRadiusService)
                                                                              ((SceneCornerRadiusService)
                                                                                      sceneCornerRadiusService
                                                                                          .id(
                                                                                              "clickgui.preset.change."
                                                                                                  + change
                                                                                                      .setting()))
                                                                                  .width(
                                                                                      LayoutOperationHandler
                                                                                          .percent(
                                                                                              Float
                                                                                                  .intBitsToFloat(
                                                                                                      1120403456))))
                                                                          .minHeight(
                                                                              Float.intBitsToFloat(
                                                                                  1110441984)))
                                                                  .padding(
                                                                      Float.intBitsToFloat(
                                                                          1092616192),
                                                                      Float.intBitsToFloat(
                                                                          0x41400000)))
                                                          .gap(
                                                              bl
                                                                  ? Float.intBitsToFloat(0x40C00000)
                                                                  : Float.intBitsToFloat(
                                                                      0x41400000)))
                                                  .direction(
                                                      bl
                                                          ? ScenePctService.Direction.COLUMN
                                                          : ScenePctService.Direction.ROW))
                                          .align(
                                              bl
                                                  ? ScenePctService.Align.STRETCH
                                                  : ScenePctService.Align.CENTER))
                                  .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                                  .cornerRadius(Float.intBitsToFloat(0x41400000))
                                  .flexShrink(0.0f))
                          .tooltip(
                              change.setting()
                                  + "\nCurrent: "
                                  + ModuleIdService.label(change.previous())
                                  + "\nPreset: "
                                  + ModuleIdService.label(change.value())),
                  (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
              .key("change:" + change.setting()));
    }
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.id("clickgui.preset.changes"))
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .flexShrink(0.0f),
            (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))
        .key("changes")
        .onMount(
            layoutContainerNode ->
                MaterialEnterService.reveal(
                    layoutContainerNode, 0.0f, Float.intBitsToFloat(-1061158912)));
  }

  private static ComponentKeyService<?> createComponentKeyService5(
      Object object, boolean bl, boolean bl2) {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
    if (object instanceof Integer) {
      Integer n = (Integer) object;
      arrayList.add(
          ComponentBoxService.panel(
              sceneCornerRadiusService ->
                  ((SceneCornerRadiusService)
                          sceneCornerRadiusService.size(
                              Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x41400000)))
                      .cornerRadius(Float.intBitsToFloat(0x40800000))
                      .backgroundColor(n)
                      .border(1.0f, MaterialIsLightService.OUTLINE_VARIANT)
                      .flexShrink(0.0f),
              new ComponentKeyService[0]));
    }
    arrayList.add(
        MaterialTextService.text(
                ModuleIdService.label(object),
                Float.intBitsToFloat(0x41400000),
                bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService) sceneTextService.minWidth(0.0f)).flex(1.0f))
                        .wordWrap(true)
                        .tooltip((bl ? "Preset: " : "Current: ") + ModuleIdService.label(object))));
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                .gap(2.0f),
        MaterialTextService.text(
                bl ? "PRESET" : "CURRENT",
                Float.intBitsToFloat(0x41100000),
                MaterialIsLightService.OUTLINE)
            .props(
                sceneTextService ->
                    sceneTextService.lineHeight(Float.intBitsToFloat(0x41400000)).visible(bl2)),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40A00000)))
                    .align(ScenePctService.Align.CENTER),
            (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)));
  }
}
