package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.network.NetworkConnectedHandler;
import dev.felix.ellice.network.NetworkViewService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialLabelService;
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
import java.util.ArrayList;
import java.util.Objects;

public final class FritzBoxScreen implements ScreenOperationHandler {
  private final NetworkViewService networkViewService;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private ComponentMountService fcnrbahznj5;
  private MaterialTerrainTooltipsService ffqamtmez7fz;
  private ScreenScreenIdService screenScreenIdService;
  private NetworkViewService.View view2;
  private String fehxt1euvsnq;
  private float value = Float.intBitsToFloat(1150025728);
  private float value2 = Float.intBitsToFloat(0x44340000);
  private boolean enabled;

  public FritzBoxScreen(NetworkViewService networkViewService) {
    this.networkViewService = Objects.requireNonNull(networkViewService);
    this.view2 = networkViewService.view();
    this.fehxt1euvsnq = this.view2.host();
  }

  @Override
  public String id() {
    return "fritz-box";
  }

  @Override
  public boolean opensAsWindow() {
    return true;
  }

  @Override
  public boolean overlaysPreviousScreen() {
    return true;
  }

  @Override
  public float backgroundDesaturation() {
    return 0.0f;
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.WORKSPACE;
  }

  public void viewport(float f, float f2) {
    if (this.value == f && this.value2 == f2) {
      return;
    }
    this.value = f;
    this.value2 = f2;
    this.updateState4();
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
    this.screenScreenIdService = screenScreenIdService;
    this.ffqamtmez7fz = new MaterialTerrainTooltipsService().terrainTooltips(true);
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService) this.ffqamtmez7fz.id("fritz.canvas"))
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .direction(ScenePctService.Direction.NONE))
        .backgroundColor(0)
        .interactive(true);
    this.fcnrbahznj5 =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService,
                screenScreenIdService == null
                    ? new ThemeIsSetService()
                    : screenScreenIdService.theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode) this.fcnrbahznj5.absolute())
                .inset(LayoutOperationHandler.px(0.0f)))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.ffqamtmez7fz.addChild(this.fcnrbahznj5);
    return this.ffqamtmez7fz;
  }

  private boolean mdlqmgmkbakn() {
    return this.value < Float.intBitsToFloat(1142292480);
  }

  private boolean checkCondition2() {
    return this.value2 < Float.intBitsToFloat(1137180672);
  }

  private boolean checkCondition3() {
    return this.fehxt1euvsnq.strip().equalsIgnoreCase(this.view2.host());
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    boolean bl = this.value >= Float.intBitsToFloat(1147207680);
    NetworkConnectedHandler.Status status = this.checkCondition3() ? this.view2.current() : null;
    NetworkConnectedHandler.Status status2 = this.checkCondition3() ? this.view2.before() : null;
    ComponentKeyService<LayoutContainerNode> componentKeyService =
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.id("fritz.result"))
                                            .flex(bl ? 1.0f : 0.0f))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x41400000)))
                    .flexShrink(0.0f),
            this.createComponentKeyService2(),
            this.createComponentKeyService5("Current WAN address", "current", status),
            this.createComponentKeyService5("Before reconnect", "before", status2)
                .props(scenePctService -> scenePctService.visible(status2 != null)));
    ComponentKeyService<LayoutContainerNode> componentKeyService2 =
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            bl
                                                ? LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1132724224))
                                                : LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x41400000)))
                    .flexShrink(0.0f),
            this.createComponentKeyService3(),
            this.createComponentKeyService4());
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.size(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)),
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .padding(
                                        this.value < Float.intBitsToFloat(1137180672)
                                                || this.checkCondition2()
                                            ? Float.intBitsToFloat(0x41000000)
                                            : Float.intBitsToFloat(1103101952)))
                            .align(ScenePctService.Align.CENTER))
                    .justify(ScenePctService.Justify.CENTER),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            sceneCornerRadiusService.id(
                                                                "fritz.sheet"))
                                                        .size(
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456)),
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .maxWidth(Float.intBitsToFloat(1146880000)))
                                        .maxHeight(
                                            bl
                                                ? (float)
                                                    (status2 != null
                                                        ? 660
                                                        : (this.enabled ? 620 : 480))
                                                : Float.intBitsToFloat(1143930880)))
                                .backgroundColor(MaterialIsLightService.SURFACE)
                                .cornerRadius(
                                    this.mdlqmgmkbakn() || this.checkCondition2()
                                        ? Float.intBitsToFloat(1101004800)
                                        : Float.intBitsToFloat(1105199104))
                                .shadow(Float.intBitsToFloat(1102053376))
                                .shadowColor(0x70000000)
                                .direction(ScenePctService.Direction.COLUMN))
                        .clip(true),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        layoutContainerNode.height(
                                                            LayoutOperationHandler.px(
                                                                this.checkCondition2()
                                                                    ? Float.intBitsToFloat(
                                                                        0x42400000)
                                                                    : Float.intBitsToFloat(
                                                                        1117257728))))
                                                    .padding(
                                                        Float.intBitsToFloat(0x40800000),
                                                        this.mdlqmgmkbakn()
                                                            ? Float.intBitsToFloat(0x41400000)
                                                            : Float.intBitsToFloat(1101004800)))
                                            .gap(Float.intBitsToFloat(0x41400000)))
                                    .align(ScenePctService.Align.CENTER))
                            .flexShrink(0.0f),
                    MaterialTextService.icon("swap_horiz", MaterialIsLightService.PRIMARY)
                        .props(
                            sceneSrcService ->
                                sceneSrcService.size(
                                    Float.intBitsToFloat(1103101952),
                                    Float.intBitsToFloat(1103101952))),
                    ComponentBoxService.column(
                        layoutContainerNode ->
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f),
                        MaterialTextService.text(
                            "FRITZ!Box",
                            this.checkCondition2()
                                ? Float.intBitsToFloat(1101004800)
                                : Float.intBitsToFloat(1103101952),
                            MaterialIsLightService.ON_SURFACE),
                        MaterialTextService.text(
                                "Internet connection",
                                Float.intBitsToFloat(0x41400000),
                                MaterialIsLightService.ON_SURFACE_VARIANT)
                            .props(
                                sceneTextService ->
                                    sceneTextService.visible(!this.checkCondition2()))),
                    MaterialTextService.iconButton(
                            "fritz.back", "close", "Close \u00b7 Esc", this::updateState5)
                        .props(
                            materialJoinedService ->
                                materialJoinedService.size(
                                    Float.intBitsToFloat(0x42200000),
                                    Float.intBitsToFloat(0x42200000)))),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        ((LayoutContainerNode)
                                                                                ((LayoutContainerNode)
                                                                                        ((LayoutContainerNode)
                                                                                                layoutContainerNode
                                                                                                    .id(
                                                                                                        "fritz.scroll"))
                                                                                            .width(
                                                                                                LayoutOperationHandler
                                                                                                    .percent(
                                                                                                        Float
                                                                                                            .intBitsToFloat(
                                                                                                                1120403456))))
                                                                                    .flex(1.0f))
                                                                            .minHeight(0.0f))
                                                                    .padding(
                                                                        0.0f,
                                                                        this.mdlqmgmkbakn()
                                                                            ? Float.intBitsToFloat(
                                                                                0x41400000)
                                                                            : Float.intBitsToFloat(
                                                                                1101004800),
                                                                        Float.intBitsToFloat(
                                                                            1098907648),
                                                                        this.mdlqmgmkbakn()
                                                                            ? Float.intBitsToFloat(
                                                                                0x41400000)
                                                                            : Float.intBitsToFloat(
                                                                                1101004800)))
                                                            .gap(Float.intBitsToFloat(0x41400000)))
                                                    .scrollable(true))
                                            .clip(true))
                                    .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                            .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT),
                    ComponentBoxService.column(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            layoutContainerNode.width(
                                                                LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                        .direction(
                                                            bl
                                                                ? ScenePctService.Direction.ROW
                                                                : ScenePctService.Direction.COLUMN))
                                                .gap(Float.intBitsToFloat(1098907648)))
                                        .align(ScenePctService.Align.STRETCH))
                                .flexShrink(0.0f),
                        componentKeyService,
                        componentKeyService2)),
                MaterialTextService.divider(),
                this.createComponentKeyService6(status)))
        .key("fritz-window");
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    String string;
    boolean bl = this.checkCondition3();
    boolean bl2 = this.view2.busy();
    int statusColor =
        this.view2.phase() == NetworkViewService.Phase.CHANGED
            ? -4989747
            : (this.view2.phase() == NetworkViewService.Phase.ERROR
                ? MaterialIsLightService.ERROR
                : MaterialIsLightService.PRIMARY);
    if (bl) {
      switch (this.view2.phase()) {
        default:
          {
            throw new MatchException(null, null);
          }
        case IDLE:
          {
            string = "Ready when you are";
            break;
          }
        case CHECKING:
          {
            string = "Checking connection";
            break;
          }
        case READY:
          {
            if (this.view2.current() != null && this.view2.current().connected()) {
              string = "You're connected";
              break;
            }
            string = "Connection status";
            break;
          }
        case RECONNECTING:
          {
            string = "Reconnecting";
            break;
          }
        case WAITING:
          {
            string = "Waiting for recovery";
            break;
          }
        case CHANGED:
          {
            string = "Your IP has changed";
            break;
          }
        case UNCHANGED:
          {
            string = "Same IP, reconnected";
            break;
          }
        case UNVERIFIED:
          {
            string = "Result not confirmed";
            break;
          }
        case ERROR:
          {
            string = "Check your connection";
            break;
          }
      }
    } else {
      string = "Check this router";
    }
    String string2 = string;
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id("fritz.connection"))
                                                .width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(Float.intBitsToFloat(1098907648)))
                        .gap(Float.intBitsToFloat(1092616192)))
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .cornerRadius(Float.intBitsToFloat(1101004800))
                .flexShrink(0.0f),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .gap(Float.intBitsToFloat(1092616192)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            ComponentBoxService.node(
                "router-activity",
                MaterialLabelService::new,
                materialLabelService ->
                    ((MaterialLabelService)
                            ((MaterialLabelService)
                                    materialLabelService
                                        .shape("clover4")
                                        .active(bl2)
                                        .tint(statusColor)
                                        .size(
                                            Float.intBitsToFloat(0x42000000),
                                            Float.intBitsToFloat(0x42000000)))
                                .flexShrink(0.0f))
                        .pointerEvents(false),
                new ComponentKeyService[0]),
            MaterialTextService.text(
                    string2, Float.intBitsToFloat(1101004800), MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f))
                            .wordWrap(true))),
        MaterialTextService.text(
                bl ? this.view2.message() : "Check the new address to see its connection status.",
                Float.intBitsToFloat(1095761920),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService) sceneTextService.id("fritz.status"))
                                .width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                        .wordWrap(true)
                        .flexShrink(0.0f)),
        MaterialTextService.text(
                (String)
                    (this.view2.elapsedSeconds() == 0L
                        ? "Request in progress\u2026"
                        : this.view2.elapsedSeconds() + " s \u00b7 Checking recovery"),
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.PRIMARY)
            .props(
                sceneTextService ->
                    ((SceneTextService) sceneTextService.visible(bl2))
                        .wordWrap(true)
                        .flexShrink(0.0f)));
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id("fritz.setup"))
                                                .width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(Float.intBitsToFloat(1098907648)))
                        .gap(Float.intBitsToFloat(1092616192)))
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .cornerRadius(Float.intBitsToFloat(1101004800))
                .flexShrink(0.0f),
        MaterialTextService.text(
                "Your router", Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE)
            .props(sceneTextService -> sceneTextService.flexShrink(0.0f)),
        MaterialTextService.text(
                "Local address",
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(sceneTextService -> sceneTextService.flexShrink(0.0f)),
        ComponentBoxService.node(
                "fritz-address",
                ControlLetterSpacingService::new,
                controlLetterSpacingService -> {
                  MaterialTextService.input(controlLetterSpacingService);
                  ((ControlLetterSpacingService)
                          ((ControlLetterSpacingService)
                                  ((ControlLetterSpacingService)
                                          ((ControlLetterSpacingService)
                                                  ((ControlLetterSpacingService)
                                                          ((ControlLetterSpacingService)
                                                                  ((ControlLetterSpacingService)
                                                                          controlLetterSpacingService
                                                                              .id("fritz.address"))
                                                                      .size(
                                                                          LayoutOperationHandler
                                                                              .percent(
                                                                                  Float
                                                                                      .intBitsToFloat(
                                                                                          1120403456)),
                                                                          LayoutOperationHandler.px(
                                                                              Float.intBitsToFloat(
                                                                                  0x42400000))))
                                                              .minWidth(0.0f))
                                                      .maxLength(253)
                                                      .flexShrink(0.0f))
                                              .placeholder("fritz.box")
                                              .pointerEvents(!this.view2.busy()))
                                      .stopPropagation(true))
                              .tooltip(
                                  "fritz.box or your router's local IP, for example 192.168.178.1"))
                      .onChanged(
                          string -> {
                            this.fehxt1euvsnq = string;
                            this.updateState4();
                          });
                  if (!controlLetterSpacingService.focused()) {
                    controlLetterSpacingService.text(this.fehxt1euvsnq);
                  }
                },
                new ComponentKeyService[0])
            .key("address"),
        MaterialTextService.button("fritz.check", "Check connection", this::mv6ivv19wnl, false)
            .props(
                materialJoinedService ->
                    materialJoinedService
                        .available(
                            (!this.view2.busy() && !this.fehxt1euvsnq.isBlank() ? 1 : 0) != 0)
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
            .children(
                MaterialTextService.icon("refresh", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1099956224),
                                Float.intBitsToFloat(1099956224))),
                MaterialTextService.label(
                    "Check connection", MaterialIsLightService.ON_SECONDARY_CONTAINER)),
        MaterialTextService.text(
                "No router password needed. This address is saved locally.",
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            sceneTextService.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .wordWrap(true)
                        .flexShrink(0.0f)));
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
    arrayList.add(
        MaterialTextService.button(
                "fritz.help",
                "Router permissions",
                () -> {
                  this.enabled = !this.enabled;
                  this.updateState4();
                },
                false)
            .props(
                materialJoinedService -> {
                  materialJoinedService
                      .colors(
                          MaterialIsLightService.SURFACE_CONTAINER,
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .shape(Float.intBitsToFloat(1098907648), 0.0f);
                  ((SceneCornerRadiusService)
                          ((SceneCornerRadiusService)
                                  ((SceneCornerRadiusService)
                                          materialJoinedService.width(
                                              LayoutOperationHandler.percent(
                                                  Float.intBitsToFloat(1120403456))))
                                      .height(
                                          LayoutOperationHandler.px(
                                              Float.intBitsToFloat(0x42400000))))
                              .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                      .gap(Float.intBitsToFloat(0x41000000));
                })
            .children(
                MaterialTextService.icon("security", MaterialIsLightService.PRIMARY)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1099956224),
                                Float.intBitsToFloat(1099956224))),
                MaterialTextService.text(
                        "Router permissions",
                        Float.intBitsToFloat(1095761920),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                MaterialTextService.icon(
                        this.enabled ? "expand_more" : "chevron_right",
                        MaterialIsLightService.OUTLINE)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1099956224),
                                Float.intBitsToFloat(1099956224)))));
    if (this.enabled) {
      arrayList.add(
          ComponentBoxService.column(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              ((LayoutContainerNode)
                                      ((LayoutContainerNode)
                                              ((LayoutContainerNode)
                                                      layoutContainerNode.id("fritz.help.body"))
                                                  .width(
                                                      LayoutOperationHandler.percent(
                                                          Float.intBitsToFloat(1120403456))))
                                          .padding(
                                              Float.intBitsToFloat(0x40800000),
                                              Float.intBitsToFloat(0x41400000),
                                              Float.intBitsToFloat(0x41400000),
                                              Float.intBitsToFloat(0x41400000)))
                                  .gap(Float.intBitsToFloat(1092616192)))
                          .flexShrink(0.0f),
                  MaterialTextService.text(
                          "In your FRITZ!Box, open Home Network \u2192 Network \u2192 your"
                              + " computer. Check permission to disconnect and restore the internet"
                              + " connection.",
                          Float.intBitsToFloat(0x41400000),
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)),
                  MaterialTextService.text(
                          "On newer FRITZ!OS versions this can be linked to automatic port sharing."
                              + " UPnP status information must also be available.",
                          Float.intBitsToFloat(0x41400000),
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)),
                  MaterialTextService.text(
                          "Cable, DS-Lite and static connections may keep the same IP. The"
                              + " addresses shown here are reported by your router.",
                          Float.intBitsToFloat(0x41400000),
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)))
              .key("permissions")
              .onMount(
                  layoutContainerNode ->
                      MaterialEnterService.reveal(
                          layoutContainerNode, 0.0f, Float.intBitsToFloat(-1061158912))));
    }
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .gap(2.0f))
                .flexShrink(0.0f),
        (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new));
  }

  private ComponentKeyService<?> createComponentKeyService5(
      String string, String string2, NetworkConnectedHandler.Status status) {
    String string3 = status == null || status.ipv4().isEmpty() ? "Not reported" : status.ipv4();
    String string4 = status == null || status.ipv6().isEmpty() ? "Not reported" : status.ipv6();
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id("fritz." + string2))
                                                .width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(Float.intBitsToFloat(1098907648)))
                        .gap(Float.intBitsToFloat(0x40C00000)))
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .cornerRadius(Float.intBitsToFloat(1101004800))
                .flexShrink(0.0f),
        MaterialTextService.text(
                string, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            sceneTextService.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .wordWrap(true)
                        .flexShrink(0.0f)),
        ComponentBoxService.row(
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
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .gap(2.0f),
                MaterialTextService.text(
                    "WAN IPv4", Float.intBitsToFloat(1092616192), MaterialIsLightService.OUTLINE),
                MaterialTextService.text(
                        string3,
                        this.mdlqmgmkbakn()
                            ? Float.intBitsToFloat(1102053376)
                            : Float.intBitsToFloat(1104150528),
                        MaterialIsLightService.ON_SURFACE)
                    .props(
                        sceneTextService ->
                            ((SceneTextService)
                                    ((SceneTextService)
                                            sceneTextService.id("fritz." + string2 + ".ipv4"))
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .wordWrap(true))),
            MaterialTextService.iconButton(
                    "fritz." + string2 + ".copy",
                    "content_copy",
                    "Copy IPv4",
                    () -> FritzBoxScreen.updateState6(string3))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available((status != null && !status.ipv4().isEmpty() ? 1 : 0) != 0)
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832)))),
        ComponentBoxService.row(
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
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .gap(2.0f),
                MaterialTextService.text(
                    "Router IPv6",
                    Float.intBitsToFloat(1092616192),
                    MaterialIsLightService.OUTLINE),
                MaterialTextService.text(
                        string4,
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneTextService ->
                            ((SceneTextService)
                                    ((SceneTextService)
                                            sceneTextService.id("fritz." + string2 + ".ipv6"))
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .wordWrap(true))),
            MaterialTextService.iconButton(
                    "fritz." + string2 + ".copy6",
                    "content_copy",
                    "Copy IPv6",
                    () -> FritzBoxScreen.updateState6(string4))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available((status != null && !status.ipv6().isEmpty() ? 1 : 0) != 0)
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832)))),
        MaterialTextService.text(
                status == null ? "Check the connection to read its addresses." : status.device(),
                Float.intBitsToFloat(1093664768),
                MaterialIsLightService.OUTLINE)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            sceneTextService.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .wordWrap(true)
                        .flexShrink(0.0f)));
  }

  private ComponentKeyService<?> createComponentKeyService6(NetworkConnectedHandler.Status status) {
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.id("fritz.footer"))
                                                .direction(
                                                    this.mdlqmgmkbakn()
                                                        ? ScenePctService.Direction.COLUMN
                                                        : ScenePctService.Direction.ROW))
                                        .align(
                                            this.mdlqmgmkbakn()
                                                ? ScenePctService.Align.STRETCH
                                                : ScenePctService.Align.CENTER))
                                .padding(
                                    this.checkCondition2()
                                        ? Float.intBitsToFloat(0x41000000)
                                        : Float.intBitsToFloat(0x41400000),
                                    this.mdlqmgmkbakn()
                                        ? Float.intBitsToFloat(0x41400000)
                                        : Float.intBitsToFloat(1101004800)))
                        .gap(Float.intBitsToFloat(0x41000000)))
                .flexShrink(0.0f),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.flex(this.mdlqmgmkbakn() ? 0.0f : 1.0f))
                                    .minWidth(0.0f))
                            .gap(2.0f))
                    .flexShrink(0.0f),
            MaterialTextService.text(
                    "Reconnect briefly disconnects every device.",
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)),
            MaterialTextService.text(
                    "Your provider may assign the same IP again.",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.OUTLINE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.visible(!this.checkCondition2()))
                            .wordWrap(true)
                            .flexShrink(0.0f))),
        MaterialTextService.button(
                "fritz.reconnect",
                this.view2.busy() ? "Working\u2026" : "Reconnect",
                this::updateState2,
                true)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    materialJoinedService
                                        .available(
                                            (!this.view2.busy()
                                                        && !this.fehxt1euvsnq.isBlank()
                                                        && (!this.checkCondition3()
                                                            || status == null
                                                            || status.canReconnect())
                                                    ? 1
                                                    : 0)
                                                != 0)
                                        .width(
                                            this.mdlqmgmkbakn()
                                                ? LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))
                                                : LayoutOperationHandler.auto()))
                                .height(
                                    LayoutOperationHandler.px(Float.intBitsToFloat(1110441984))))
                        .tooltip(
                            "Reconnect once, then compare WAN addresses. Your provider controls IP"
                                + " assignment."))
            .children(
                MaterialTextService.icon(
                        "swap_horiz",
                        this.view2.busy()
                            ? MaterialIsLightService.OUTLINE
                            : MaterialIsLightService.ON_PRIMARY)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1099956224),
                                Float.intBitsToFloat(1099956224))),
                MaterialTextService.label(
                    this.view2.busy() ? "Working\u2026" : "Reconnect",
                    this.view2.busy()
                        ? MaterialIsLightService.OUTLINE
                        : MaterialIsLightService.ON_PRIMARY)));
  }

  private void mv6ivv19wnl() {
    this.materialKeyService.clear(this.ffqamtmez7fz);
    this.networkViewService.check(this.fehxt1euvsnq);
    this.updateState3();
  }

  private void updateState2() {
    this.materialKeyService.clear(this.ffqamtmez7fz);
    this.networkViewService.reconnect(this.fehxt1euvsnq);
    this.updateState3();
  }

  private void updateState3() {
    NetworkViewService.View view = this.networkViewService.view();
    if (view.phase() == NetworkViewService.Phase.ERROR && this.view2.phase() != view.phase()) {
      this.enabled = true;
    }
    this.view2 = view;
    this.updateState4();
  }

  private void updateState4() {
    if (this.fcnrbahznj5 != null) {
      this.fcnrbahznj5.invalidateComponent();
    }
  }

  private void updateState5() {
    if (this.screenScreenIdService != null) {
      this.screenScreenIdService.close();
    }
  }

  private static void updateState6(String string) {
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.mc().keyboardHandler.setClipboard(string);
    }
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenIdService) {
    if (this.networkViewService.view().phase() == NetworkViewService.Phase.IDLE) {
      this.mv6ivv19wnl();
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    this.materialKeyService.clear(this.ffqamtmez7fz);
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    if (this.ffqamtmez7fz != null && this.ffqamtmez7fz.computedW() > 0.0f) {
      this.viewport(this.ffqamtmez7fz.computedW(), this.ffqamtmez7fz.computedH());
    }
    if (this.view2 != this.networkViewService.view()) {
      this.updateState3();
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
    return this.materialKeyService.key(this.ffqamtmez7fz, n, n2);
  }
}
