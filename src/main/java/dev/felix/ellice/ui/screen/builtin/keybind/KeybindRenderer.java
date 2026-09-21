package dev.felix.ellice.ui.screen.builtin.keybind;

import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class KeybindRenderer implements ComponentOperationHandler {
  public static final int ELLICE = -5773619;
  public static final int GAME = -10848;
  private final KeybindTargetService keybindTargetService;
  private final Runnable runnable;
  private final Runnable runnable2;
  private ComponentThemeService componentThemeService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ScenePctService<?> scenePctService;
  private boolean enabled2;
  private float value2 = Float.intBitsToFloat(1150681088);
  private float text2 = Float.intBitsToFloat(1145569280);
  private String fcixh0orecre = "keyboard";
  private String text3 = "";
  private boolean enabled = true;
  private int count = -1;

  public KeybindRenderer(
      KeybindTargetService keybindTargetService, Runnable runnable, Runnable runnable2) {
    this.keybindTargetService = keybindTargetService;
    this.runnable = runnable;
    this.runnable2 = runnable2;
    updateState2(keybindTargetService.selected());
  }

  public KeybindTargetService selection() {
    return this.keybindTargetService;
  }

  public boolean listening() {
    return this.enabled;
  }

  public void viewport(float f, float f2) {
    if (this.value2 == f && this.text2 == f2) {
      return;
    }
    this.value2 = f;
    this.text2 = f2;
    refresh();
  }

  public void refresh() {
    if (this.componentThemeService != null) {
      this.componentThemeService.invalidate();
    }
  }

  private boolean checkCondition() {
    return this.value2 < Float.intBitsToFloat(1142620160);
  }

  private boolean checkCondition2() {
    return this.text2 < Float.intBitsToFloat(1143111680);
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
    this.componentThemeService = componentThemeService;
    Supplier<MaterialTerrainTooltipsService> supplier = MaterialTerrainTooltipsService::new;
    Consumer<MaterialTerrainTooltipsService> consumer =
        materialTerrainTooltipsService -> {
          this.materialTerrainTooltipsService = materialTerrainTooltipsService;
          materialTerrainTooltipsService.terrainTooltips(true);
          materialTerrainTooltipsService
              .id("keybind.picker")
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .maxHeight(
                  Math.max(
                      Float.intBitsToFloat(1124859904),
                      this.text2 - (checkCondition() ? 16 : 48)))
              .backgroundColor(MaterialIsLightService.SURFACE)
              .cornerRadius(Float.intBitsToFloat(1105199104))
              .direction(ScenePctService.Direction.COLUMN)
              .padding(
                  checkCondition2()
                      ? Float.intBitsToFloat(1092616192)
                      : Float.intBitsToFloat(1101004800))
              .gap(
                  checkCondition2()
                      ? Float.intBitsToFloat(1090519040)
                      : Float.intBitsToFloat(1094713344))
              .clip(true);
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[4];
    componentKeyServiceArr[0] = createComponentKeyService();
    Consumer<LayoutContainerNode> consumer2 =
        layoutContainerNode -> {
          layoutContainerNode
              .id("keybind.body")
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .minHeight(0.0f)
              .flexShrink(1.0f)
              .gap(
                  checkCondition2()
                      ? Float.intBitsToFloat(1090519040)
                      : Float.intBitsToFloat(1094713344))
              .scrollable(true)
              .scrollbarWidth(Float.intBitsToFloat(1077936128))
              .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
              .padding(2.0f, Float.intBitsToFloat(1082130432), 2.0f, 2.0f)
              .clip(true);
        };
    ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[4];
    componentKeyServiceArr2[0] =
        (checkCondition2()
                ? ComponentBoxService.box(
                    layoutContainerNode2 -> {
                      layoutContainerNode2.visible(false);
                    },
                    new ComponentKeyService[0])
                : createComponentKeyService2())
            .props(
                scenePctService -> {
                  scenePctService.flexShrink(0.0f);
                });
    componentKeyServiceArr2[1] =
        createComponentKeyService3()
            .props(
                scenePctService2 -> {
                  scenePctService2.flexShrink(0.0f);
                });
    componentKeyServiceArr2[2] =
        createComponentKeyService4()
            .props(
                scenePctService3 -> {
                  scenePctService3.flexShrink(0.0f);
                });
    componentKeyServiceArr2[3] =
        createComponentKeyService9()
            .props(
                scenePctService4 -> {
                  scenePctService4.flexShrink(0.0f);
                });
    componentKeyServiceArr[1] =
        ComponentBoxService.column(
            (Consumer<LayoutContainerNode>) consumer2,
            (ComponentKeyService<?>[]) componentKeyServiceArr2);
    componentKeyServiceArr[2] =
        createComponentKeyService10()
            .props(
                scenePctService5 -> {
                  scenePctService5.flexShrink(0.0f);
                });
    componentKeyServiceArr[3] = m4jkqzsgdibd();
    return ComponentBoxService.node(
            "keybind-picker-scope", supplier, consumer, componentKeyServiceArr)
        .key("keybind-picker");
  }

  private ComponentKeyService<?> createComponentKeyService() {
    ArrayList arrayList = new ArrayList();
    if (!checkCondition()) {
      arrayList.add(
          ComponentBoxService.panel(
                  sceneCornerRadiusService -> {
                    sceneCornerRadiusService
                        .size(Float.intBitsToFloat(1109917696), Float.intBitsToFloat(1109917696))
                        .cornerRadius(Float.intBitsToFloat(1096810496))
                        .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .justify(ScenePctService.Justify.CENTER)
                        .flexShrink(0.0f);
                  },
                  MaterialTextService.icon("keyboard", MaterialIsLightService.PRIMARY))
              .key("emblem"));
    }
    Consumer<LayoutContainerNode> consumer =
        layoutContainerNode -> {
          layoutContainerNode.flex(1.0f).minWidth(0.0f).gap(0.0f);
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
    componentKeyServiceArr[0] =
        MaterialTextService.text(
            "Choose a key",
            checkCondition() ? Float.intBitsToFloat(1100480512) : Float.intBitsToFloat(1103101952),
            MaterialIsLightService.ON_SURFACE);
    componentKeyServiceArr[1] =
        MaterialTextService.text(
                this.keybindTargetService.target().owner()
                    + "  /  "
                    + this.keybindTargetService.target().title(),
                Float.intBitsToFloat(1093664768),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService -> {
                  sceneTextService.width(
                      LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                });
    arrayList.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>) consumer,
                (ComponentKeyService<?>[]) componentKeyServiceArr)
            .key("heading"));
    if (checkCondition2()) {
      arrayList.add(
          MaterialTextService.text(
                  this.keybindTargetService.name(this.keybindTargetService.selected()),
                  Float.intBitsToFloat(1094713344),
                  MaterialIsLightService.PRIMARY)
              .props(
                  sceneTextService2 -> {
                    sceneTextService2.maxWidth(Float.intBitsToFloat(1118044160)).flexShrink(0.0f);
                  }));
    }
    arrayList.add(
        MaterialTextService.iconButton(
                "keybind.close",
                "close",
                "Cancel selection\nClose without changing the current binding. Esc",
                this.runnable)
            .props(
                materialJoinedService -> {
                  materialJoinedService.size(
                      Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1108344832));
                }));
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode2 -> {
                  layoutContainerNode2
                      .id("keybind.header")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .align(ScenePctService.Align.CENTER)
                      .gap(Float.intBitsToFloat(1094713344))
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }))
        .key("header");
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    String str;
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .backgroundColor(MaterialIsLightService.SURFACE_LOW)
              .cornerRadius(Float.intBitsToFloat(1101004800))
              .padding(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1098907648))
              .direction(ScenePctService.Direction.ROW)
              .align(ScenePctService.Align.CENTER)
              .gap(Float.intBitsToFloat(1098907648));
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
    Consumer<SceneCornerRadiusService> consumer2 =
        sceneCornerRadiusService2 -> {
          sceneCornerRadiusService2
              .id("keybind.selected-key")
              .size(
                  checkCondition()
                      ? Float.intBitsToFloat(1116995584)
                      : Float.intBitsToFloat(1120403456),
                  Float.intBitsToFloat(1116471296))
              .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
              .cornerRadius(Float.intBitsToFloat(1098907648))
              .direction(ScenePctService.Direction.COLUMN)
              .justify(ScenePctService.Justify.CENTER)
              .align(ScenePctService.Align.CENTER)
              .padding(Float.intBitsToFloat(1090519040))
              .flexShrink(0.0f);
        };
    ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[1];
    componentKeyServiceArr2[0] =
        MaterialTextService.text(
                this.keybindTargetService.name(this.keybindTargetService.selected()),
                this.keybindTargetService.name(this.keybindTargetService.selected()).length() > 5
                    ? Float.intBitsToFloat(1095761920)
                    : Float.intBitsToFloat(1103101952),
                MaterialIsLightService.PRIMARY)
            .props(
                sceneTextService -> {
                  sceneTextService
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .textAlign(SceneTextService.TextAlign.CENTER);
                });
    componentKeyServiceArr[0] = ComponentBoxService.panel(consumer2, componentKeyServiceArr2);
    Consumer<LayoutContainerNode> consumer3 =
        layoutContainerNode -> {
          layoutContainerNode.flex(1.0f).minWidth(0.0f).gap(Float.intBitsToFloat(1077936128));
        };
    ComponentKeyService[] componentKeyServiceArr3 = new ComponentKeyService[3];
    if (this.enabled) {
      str = "Listening for your next key…";
    } else {
      str = this.keybindTargetService.selected() < 0 ? "No key assigned" : "Ready when you are";
    }
    componentKeyServiceArr3[0] =
        MaterialTextService.text(
                str,
                checkCondition()
                    ? Float.intBitsToFloat(1097859072)
                    : Float.intBitsToFloat(1099956224),
                MaterialIsLightService.ON_SURFACE)
            .props(
                sceneTextService2 -> {
                  sceneTextService2
                      .wordWrap(true)
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                });
    componentKeyServiceArr3[1] =
        MaterialTextService.text(
                this.enabled
                    ? "Click a keycap or press a key on your keyboard."
                    : "Inspect the assignments below, then apply your selection.",
                Float.intBitsToFloat(1093664768),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService3 -> {
                  sceneTextService3
                      .wordWrap(true)
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                });
    componentKeyServiceArr3[2] =
        MaterialTextService.text(
            "Current: " + this.keybindTargetService.name(this.keybindTargetService.original()),
            Float.intBitsToFloat(1092616192),
            MaterialIsLightService.PRIMARY);
    componentKeyServiceArr[1] =
        ComponentBoxService.column(
            (Consumer<LayoutContainerNode>) consumer3,
            (ComponentKeyService<?>[]) componentKeyServiceArr3);
    return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("selected-card");
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    ArrayList arrayList = new ArrayList();
    for (String str : List.of("keyboard", "navigation", "numpad")) {
      arrayList.add(
          createComponentKeyService12(
                  "keybind.page." + str,
                  str.equals("navigation")
                      ? "Nav / F13+"
                      : str.equals("numpad") ? "Numpad" : "Keyboard",
                  () -> {
                    this.fcixh0orecre = str;
                    updateState8();
                    refresh();
                  },
                  this.fcixh0orecre.equals(str))
              .props(
                  materialJoinedService -> {
                    materialJoinedService
                        .flex(1.0f)
                        .minWidth(0.0f)
                        .padding(
                            0.0f,
                            checkCondition()
                                ? Float.intBitsToFloat(1084227584)
                                : Float.intBitsToFloat(1094713344));
                  })
              .key(str));
    }
    ComponentKeyService<LayoutContainerNode> componentKeyServiceRow =
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .gap(Float.intBitsToFloat(1082130432))
                      .flex(1.0f)
                      .minWidth(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }));
    Consumer<LayoutContainerNode> consumer =
        layoutContainerNode2 -> {
          layoutContainerNode2
              .gap(Float.intBitsToFloat(1082130432))
              .align(ScenePctService.Align.CENTER);
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
    componentKeyServiceArr[0] =
        createComponentKeyService12(
                "keybind.listen",
                this.enabled ? "Listening…" : "Press a key",
                () -> {
                  this.enabled = !this.enabled;
                  updateState8();
                  refresh();
                },
                this.enabled)
            .props(
                materialJoinedService2 -> {
                  materialJoinedService2.tooltip(
                      "Listen for a key\n"
                          + "The next physical key selects a candidate. Nothing changes until you"
                          + " apply it. Esc cancels.");
                });
    componentKeyServiceArr[1] =
        createComponentKeyService12(
                "keybind.fit",
                this.enabled2 ? "Fit" : "Larger keys",
                () -> {
                  this.enabled2 = !this.enabled2;
                  refresh();
                },
                this.enabled2)
            .props(
                materialJoinedService3 -> {
                  materialJoinedService3.tooltip(
                      "Keyboard size\n"
                          + "Fit the whole keyboard or use larger keycaps and scroll sideways.");
                });
    ComponentKeyService<LayoutContainerNode> componentKeyServiceRow2 =
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>) consumer,
            (ComponentKeyService<?>[]) componentKeyServiceArr);
    return checkCondition()
        ? ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode3 -> {
                      layoutContainerNode3
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(Float.intBitsToFloat(1086324736));
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      componentKeyServiceRow.props(
                          scenePctService -> {
                            scenePctService.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                          }),
                      componentKeyServiceRow2.props(
                          scenePctService2 -> {
                            scenePctService2.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                          })
                    })
            .key("tools")
        : ComponentBoxService.row(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode4 -> {
                      layoutContainerNode4
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(Float.intBitsToFloat(1098907648))
                          .align(ScenePctService.Align.CENTER);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {componentKeyServiceRow, componentKeyServiceRow2})
            .key("tools");
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    float fIntBitsToFloat;
    ComponentKeyService<?> componentKeyServiceRow;
    if (this.text2 < Float.intBitsToFloat(1137180672)) {
      fIntBitsToFloat = Float.intBitsToFloat(1106247680);
    } else if (checkCondition2()) {
      fIntBitsToFloat = checkCondition() ? 36 : 31;
    } else {
      fIntBitsToFloat =
          this.value2 < Float.intBitsToFloat(1142620160)
              ? Float.intBitsToFloat(1109917696)
              : Float.intBitsToFloat(1112539136);
    }
    float f = fIntBitsToFloat;
    if (this.fcixh0orecre.equals("numpad")) {
      componentKeyServiceRow = createComponentKeyService8(f);
    } else if (this.fcixh0orecre.equals("navigation")) {
      ArrayList arrayList = new ArrayList();
      int i = 0;
      while (true) {
        int i2 = i;
        if (i2 >= 3) {
          break;
        }
        ArrayList arrayList2 = new ArrayList();
        int i3 = 0;
        while (true) {
          int i4 = i3;
          if (i4 >= 5) {
            break;
          }
          int i5 = 302 + (i2 * 5) + i4;
          if (i5 <= 314) {
            arrayList2.add(KeyCatalog.key(i5));
          }
          i3 = i4 + 1;
        }
        if (i2 == 2) {
          arrayList2.add(KeyCatalog.key(161));
          arrayList2.add(KeyCatalog.key(162));
        }
        arrayList.add(createComponentKeyService6(arrayList2, i2 + 8, f));
        i = i2 + 1;
      }
      componentKeyServiceRow =
          ComponentBoxService.row(
              (Consumer<LayoutContainerNode>)
                  layoutContainerNode -> {
                    layoutContainerNode
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .gap(Float.intBitsToFloat(1099956224));
                  },
              (ComponentKeyService<?>[])
                  new ComponentKeyService[] {
                    createComponentKeyService5(KeyCatalog.NAV, 3, f)
                        .props(
                            scenePctService -> {
                              scenePctService.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
                            }),
                    ComponentBoxService.column(
                        (Consumer<LayoutContainerNode>)
                            layoutContainerNode2 -> {
                              layoutContainerNode2
                                  .flex(Float.intBitsToFloat(1084227584))
                                  .minWidth(0.0f)
                                  .gap(0.0f);
                            },
                        (ComponentKeyService<?>[])
                            arrayList.toArray(
                                i6 -> {
                                  return new ComponentKeyService[i6];
                                }))
                  });
    } else {
      ArrayList arrayList3 = new ArrayList();
      arrayList3.add(
          createComponentKeyService5(KeyCatalog.MAIN, 0, f)
              .props(
                  scenePctService2 -> {
                    scenePctService2.flex(Float.intBitsToFloat(1097859072)).minWidth(0.0f);
                  }));
      if (this.value2 >= Float.intBitsToFloat(1148518400)) {
        arrayList3.add(
            createComponentKeyService5(KeyCatalog.NAV, 6, f)
                .props(
                    scenePctService3 -> {
                      scenePctService3.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
                    }));
      }
      componentKeyServiceRow =
          ComponentBoxService.row(
              (Consumer<LayoutContainerNode>)
                  layoutContainerNode3 -> {
                    layoutContainerNode3
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .gap(
                            this.value2 >= Float.intBitsToFloat(1148518400)
                                ? Float.intBitsToFloat(1099956224)
                                : 0.0f);
                  },
              (ComponentKeyService<?>[])
                  arrayList3.toArray(
                      i7 -> {
                        return new ComponentKeyService[i7];
                      }));
    }
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService
              .id("keybind.deck")
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .backgroundColor(MaterialIsLightService.SURFACE_LOWEST)
              .border(Float.intBitsToFloat(1059481190), MaterialIsLightService.OUTLINE_VARIANT)
              .cornerRadius(Float.intBitsToFloat(1101004800))
              .padding(
                  checkCondition()
                      ? Float.intBitsToFloat(1086324736)
                      : Float.intBitsToFloat(1094713344))
              .direction(ScenePctService.Direction.COLUMN)
              .gap(Float.intBitsToFloat(1086324736));
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
    componentKeyServiceArr[0] =
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode4 -> {
                  layoutContainerNode4
                      .id("keybind.keyboard-scroll")
                      .size(
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                          LayoutOperationHandler.px(f * Float.intBitsToFloat(1086324736)))
                      .flexShrink(0.0f)
                      .scrollable(true)
                      .scrollbarWidth(Float.intBitsToFloat(1077936128))
                      .scrollbarColor(MaterialIsLightService.OUTLINE)
                      .clip(true);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  componentKeyServiceRow
                      .props(
                          scenePctService4 -> {
                            float f2;
                            ScenePctService scenePctServiceId =
                                scenePctService4.id("keybind.keyboard");
                            if (this.enabled2) {
                              f2 = this.fcixh0orecre.equals("keyboard") ? 780 : 440;
                            } else {
                              f2 = 0.0f;
                            }
                            scenePctServiceId.minWidth(f2).flexShrink(0.0f);
                          })
                      .key("board:" + this.fcixh0orecre)
                });
    componentKeyServiceArr[1] =
        MaterialTextService.text(
                this.enabled2
                    ? "Scroll sideways to reach every key"
                    : "System key labels · click a keycap to select",
                Float.intBitsToFloat(1091567616),
                MaterialIsLightService.OUTLINE)
            .props(
                sceneTextService -> {
                  sceneTextService.padding(2.0f, Float.intBitsToFloat(1082130432));
                });
    return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("keyboard-deck");
  }

  private ComponentKeyService<?> createComponentKeyService5(
      List<List<KeyCatalog.Key>> list, int i, float f) {
    ArrayList arrayList = new ArrayList();
    int i2 = 0;
    while (true) {
      int i3 = i2;
      if (i3 >= list.size()) {
        return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode.gap(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i4 -> {
                      return new ComponentKeyService[i4];
                    }));
      }
      arrayList.add(createComponentKeyService6(list.get(i3), i3 + i, f));
      i2 = i3 + 1;
    }
  }

  private ComponentKeyService<?> createComponentKeyService6(
      List<KeyCatalog.Key> list, int i, float f) {
    ArrayList arrayList = new ArrayList();
    int i2 = 0;
    while (true) {
      int i3 = i2;
      if (i3 >= list.size()) {
        return ComponentBoxService.row(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .height(LayoutOperationHandler.px(f))
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    arrayList.toArray(
                        i4 -> {
                          return new ComponentKeyService[i4];
                        }))
            .key("row:" + i);
      }
      KeyCatalog.Key key = list.get(i3);
      if (key.spacer()) {
        arrayList.add(
            ComponentBoxService.box(
                    layoutContainerNode2 -> {
                      layoutContainerNode2.flex(key.units()).minWidth(0.0f);
                    },
                    new ComponentKeyService[0])
                .key("gap:" + i3));
      } else {
        arrayList.add(
            createComponentKeyService7(key, i, i3)
                .props(
                    keybindKeyService -> {
                      keybindKeyService
                          .flex(key.units())
                          .minWidth(0.0f)
                          .height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                    }));
      }
      i2 = i3 + 1;
    }
  }

  private ComponentKeyService<KeybindKeyService> createComponentKeyService7(
      KeyCatalog.Key key, int i, int i2) {
    String strLabel;
    int i3;
    List<KeybindTargetService.Use> listUses = this.keybindTargetService.uses(key.code());
    String strReserved = KeyCatalog.reserved(key.code());
    if (strReserved.isEmpty()) {
      strLabel = listUses.isEmpty() ? "" : ((KeybindTargetService.Use) listUses.getFirst()).label();
    } else {
      strLabel = strReserved;
    }
    String strSubstring = strLabel;
    if (strSubstring.contains(" · ")) {
      strSubstring = strSubstring.substring(0, strSubstring.indexOf(" · "));
    }
    if (listUses.stream()
        .anyMatch(
            use -> {
              return !use.minecraft();
            })) {
      i3 = ELLICE;
    } else {
      i3 = listUses.isEmpty() ? 0 : GAME;
    }
    int i4 = i3;
    boolean z = this.keybindTargetService.target().read().getAsInt() == key.code();
    String strName = this.keybindTargetService.name(key.code());
    if (this.fcixh0orecre.equals("numpad") && strName.startsWith("Num ")) {
      strName = strName.substring(4);
    }
    String str = strName;
    String str2 = strSubstring;
    return ComponentBoxService.node(
            "keyboard-keycap",
            KeybindKeyService::new,
            keybindKeyService -> {
              keybindKeyService
                  .key(
                      key.code(),
                      str,
                      createText(key.code(), str2),
                      i4,
                      this.keybindTargetService.selected() == key.code(),
                      z,
                      !strReserved.isEmpty())
                  .id("keybind.key." + key.code())
                  .tooltip(this.keybindTargetService.tooltip(key.code()))
                  .onClick(
                      () -> {
                        updateState(key.code());
                      });
              if (this.count == key.code()) {
                keybindKeyService.pulse();
                this.count = -1;
              }
            },
            new ComponentKeyService[0])
        .key("key:" + key.code())
        .onMount(
            keybindKeyService2 -> {
              keybindKeyService2.opacity(0.0f);
              keybindKeyService2.translateY(Float.intBitsToFloat(1088421888));
              float fMin =
                  Math.min(
                      Float.intBitsToFloat(1041194025),
                      (i * Float.intBitsToFloat(1014350479))
                          + (i2 * Float.intBitsToFloat(990057071)));
              MotionAnimateService.animate(
                  keybindKeyService2,
                  MotionColorsContainer.Floats.OPACITY,
                  1.0f,
                  SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)),
                  fMin);
              MotionAnimateService.animate(
                  keybindKeyService2,
                  MotionColorsContainer.Floats.TRANSLATE_Y,
                  0.0f,
                  MaterialIsLightService.SPATIAL,
                  fMin);
            });
  }

  private String createText(int i, String str) {
    return i == this.keybindTargetService.original() ? "Current" : str;
  }

  private ComponentKeyService<?> createComponentKeyService8(float f) {
    return ComponentBoxService.column(
        (Consumer<LayoutContainerNode>)
            layoutContainerNode -> {
              layoutContainerNode
                  .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .align(ScenePctService.Align.CENTER);
            },
        (ComponentKeyService<?>[])
            new ComponentKeyService[] {
              ComponentBoxService.column(
                  (Consumer<LayoutContainerNode>)
                      layoutContainerNode2 -> {
                        layoutContainerNode2
                            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .maxWidth(Float.intBitsToFloat(1138491392))
                            .gap(0.0f);
                      },
                  (ComponentKeyService<?>[])
                      new ComponentKeyService[] {
                        createComponentKeyService6(
                            List.of(
                                KeyCatalog.key(282),
                                KeyCatalog.key(331),
                                KeyCatalog.key(332),
                                KeyCatalog.key(333)),
                            30,
                            f),
                        ComponentBoxService.row(
                            (Consumer<LayoutContainerNode>)
                                layoutContainerNode3 -> {
                                  layoutContainerNode3.width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456)));
                                },
                            (ComponentKeyService<?>[])
                                new ComponentKeyService[] {
                                  ComponentBoxService.column(
                                      (Consumer<LayoutContainerNode>)
                                          layoutContainerNode4 -> {
                                            layoutContainerNode4
                                                .flex(Float.intBitsToFloat(1077936128))
                                                .minWidth(0.0f);
                                          },
                                      (ComponentKeyService<?>[])
                                          new ComponentKeyService[] {
                                            createComponentKeyService6(
                                                List.of(
                                                    KeyCatalog.key(327),
                                                    KeyCatalog.key(328),
                                                    KeyCatalog.key(329)),
                                                31,
                                                f),
                                            createComponentKeyService6(
                                                List.of(
                                                    KeyCatalog.key(324),
                                                    KeyCatalog.key(325),
                                                    KeyCatalog.key(326)),
                                                32,
                                                f)
                                          }),
                                  createComponentKeyService7(KeyCatalog.key(334), 31, 3)
                                      .props(
                                          keybindKeyService -> {
                                            keybindKeyService
                                                .flex(1.0f)
                                                .minWidth(0.0f)
                                                .height(LayoutOperationHandler.px(f * 2.0f));
                                          })
                                }),
                        ComponentBoxService.row(
                            (Consumer<LayoutContainerNode>)
                                layoutContainerNode5 -> {
                                  layoutContainerNode5.width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456)));
                                },
                            (ComponentKeyService<?>[])
                                new ComponentKeyService[] {
                                  ComponentBoxService.column(
                                      (Consumer<LayoutContainerNode>)
                                          layoutContainerNode6 -> {
                                            layoutContainerNode6
                                                .flex(Float.intBitsToFloat(1077936128))
                                                .minWidth(0.0f);
                                          },
                                      (ComponentKeyService<?>[])
                                          new ComponentKeyService[] {
                                            createComponentKeyService6(
                                                List.of(
                                                    KeyCatalog.key(321),
                                                    KeyCatalog.key(322),
                                                    KeyCatalog.key(323)),
                                                33,
                                                f),
                                            createComponentKeyService6(
                                                List.of(
                                                    KeyCatalog.key(320, 2.0f), KeyCatalog.key(330)),
                                                34,
                                                f)
                                          }),
                                  createComponentKeyService7(KeyCatalog.key(335), 33, 3)
                                      .props(
                                          keybindKeyService2 -> {
                                            keybindKeyService2
                                                .flex(1.0f)
                                                .minWidth(0.0f)
                                                .height(LayoutOperationHandler.px(f * 2.0f));
                                          })
                                }),
                        createComponentKeyService6(
                            List.of(
                                KeyCatalog.key(336),
                                KeyCatalog.gap(Float.intBitsToFloat(1077936128))),
                            35,
                            f)
                      })
            });
  }

  private ComponentKeyService<?> createComponentKeyService9() {
    ArrayList arrayList = new ArrayList();
    String[] strArr = {"Selected", "ellice bind", "Minecraft", "Reserved"};
    int[] iArr = {MaterialIsLightService.PRIMARY, ELLICE, GAME, MaterialIsLightService.OUTLINE};
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= strArr.length) {
        return ComponentBoxService.row(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(
                              checkCondition()
                                  ? Float.intBitsToFloat(1092616192)
                                  : Float.intBitsToFloat(1101004800))
                          .align(ScenePctService.Align.CENTER)
                          .justify(ScenePctService.Justify.CENTER);
                    },
                (ComponentKeyService<?>[])
                    arrayList.toArray(
                        i3 -> {
                          return new ComponentKeyService[i3];
                        }))
            .key("legend");
      }
      int i4 = iArr[i2];
      Consumer<LayoutContainerNode> consumer =
          layoutContainerNode2 -> {
            layoutContainerNode2
                .align(ScenePctService.Align.CENTER)
                .gap(Float.intBitsToFloat(1084227584))
                .minWidth(0.0f);
          };
      ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
      componentKeyServiceArr[0] =
          ComponentBoxService.panel(
              sceneCornerRadiusService -> {
                sceneCornerRadiusService
                    .size(Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584))
                    .cornerRadius(Float.intBitsToFloat(1075838976))
                    .backgroundColor(i4)
                    .flexShrink(0.0f);
              },
              new ComponentKeyService[0]);
      componentKeyServiceArr[1] =
          MaterialTextService.text(
              strArr[i2],
              checkCondition()
                  ? Float.intBitsToFloat(1091567616)
                  : Float.intBitsToFloat(1092616192),
              MaterialIsLightService.ON_SURFACE_VARIANT);
      arrayList.add(
          ComponentBoxService.row(
              (Consumer<LayoutContainerNode>) consumer,
              (ComponentKeyService<?>[]) componentKeyServiceArr));
      i = i2 + 1;
    }
  }

  private ComponentKeyService<?> createComponentKeyService10() {
    String str;
    String strJoin;
    String strReserved = KeyCatalog.reserved(this.keybindTargetService.selected());
    List<KeybindTargetService.Use> listUses =
        this.keybindTargetService.uses(this.keybindTargetService.selected());
    boolean z =
        this.keybindTargetService.selected() >= 0
            && this.keybindTargetService.selected()
                == this.keybindTargetService.target().read().getAsInt();
    if (!this.text3.isEmpty()) {
      str = this.text3;
    } else if (!strReserved.isEmpty()) {
      str = "Reserved for " + strReserved;
    } else if (this.keybindTargetService.selected() < 0) {
      str = "Remove this shortcut";
    } else if (this.keybindTargetService.hasElliceConflict()) {
      str = "This key already has a ellice binding";
    } else {
      str = listUses.isEmpty() ? "This key is free" : "Also used in Minecraft";
    }
    String str2 = str;
    if (z && strReserved.isEmpty() && listUses.isEmpty()) {
      str2 = "Your current binding";
    }
    if (!strReserved.isEmpty()) {
      strJoin = "Choose a different key. This client shortcut stays available.";
    } else if (this.keybindTargetService.selected() < 0) {
      strJoin = "Apply to remove the current binding.";
    } else if (listUses.isEmpty()) {
      strJoin =
          z
              ? "Choose another key to change it, or close to keep it."
              : "Apply when you're happy with your selection.";
    } else {
      strJoin =
          String.join(
              " · ",
              listUses.stream()
                  .map(
                      (v0) -> {
                        return v0.label();
                      })
                  .toList());
    }
    String str3 = strJoin;
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService
              .id("keybind.assignment-info")
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
              .cornerRadius(Float.intBitsToFloat(1098907648))
              .padding(
                  checkCondition2()
                      ? Float.intBitsToFloat(1090519040)
                      : Float.intBitsToFloat(1094713344))
              .gap(Float.intBitsToFloat(1077936128))
              .direction(ScenePctService.Direction.COLUMN)
              .tooltip(this.keybindTargetService.tooltip(this.keybindTargetService.selected()));
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
    String str4 = str2;
    float fIntBitsToFloat = Float.intBitsToFloat(1094713344);
    int i =
        (strReserved.isEmpty() && !this.keybindTargetService.hasElliceConflict()) ? ELLICE : GAME;
    componentKeyServiceArr[0] =
        MaterialTextService.text(str4, fIntBitsToFloat, i)
            .props(
                sceneTextService -> {
                  sceneTextService
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .wordWrap(true);
                });
    componentKeyServiceArr[1] =
        MaterialTextService.text(
                str3, Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService2 -> {
                  sceneTextService2
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .wordWrap(true)
                      .maxLines(3);
                });
    return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("assignment-info");
  }

  private ComponentKeyService<?> m4jkqzsgdibd() {
    String str;
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        createComponentKeyService12(
                "keybind.clear",
                "Unbind",
                () -> {
                  updateState(-1);
                },
                false)
            .props(
                materialJoinedService -> {
                  materialJoinedService.tooltip(
                      "Remove binding\nStage an unassigned key. Apply to save it.");
                }));
    arrayList.add(
        createComponentKeyService12(
                "keybind.suggest",
                checkCondition() ? "Free key" : "Find a free key",
                this::updateState3,
                false)
            .props(
                materialJoinedService2 -> {
                  materialJoinedService2.tooltip(
                      "Find a free key\nSuggest a key unused by ellice and Minecraft.");
                }));
    arrayList.add(
        ComponentBoxService.box(
            layoutContainerNode -> {
              layoutContainerNode.flex(1.0f).minWidth(0.0f);
            },
            new ComponentKeyService[0]));
    if (!checkCondition()) {
      arrayList.add(createComponentKeyService12("keybind.cancel", "Cancel", this.runnable, false));
    }
    boolean zHasElliceConflict = this.keybindTargetService.hasElliceConflict();
    if (this.keybindTargetService.replacesModule()) {
      str = "Replace bind";
    } else {
      str = zHasElliceConflict ? "Use anyway" : "Apply";
    }
    arrayList.add(
        MaterialTextService.button(
                "keybind.apply",
                str,
                () -> {
                  updateState4(zHasElliceConflict);
                },
                MaterialIsLightService.PRIMARY,
                MaterialIsLightService.ON_PRIMARY,
                Float.intBitsToFloat(1096810496))
            .props(
                materialJoinedService3 -> {
                  String str2;
                  materialJoinedService3.available(this.keybindTargetService.canApply());
                  SceneCornerRadiusService sceneCornerRadiusServicePadding =
                      materialJoinedService3
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1108869120)))
                          .minWidth(Float.intBitsToFloat(1114636288))
                          .padding(
                              0.0f,
                              checkCondition()
                                  ? Float.intBitsToFloat(1092616192)
                                  : Float.intBitsToFloat(1099956224));
                  if (this.keybindTargetService.replacesModule()) {
                    str2 =
                        "Replace the existing module binding\nThis key will toggle "
                            + this.keybindTargetService.target().owner()
                            + ". The previous module loses this shortcut.";
                  } else {
                    str2 =
                        zHasElliceConflict
                            ? "Keep the shared key\n"
                                  + "Both ellice actions will use this key. Apply only if that is"
                                  + " what you want."
                            : "Apply selection\nSave this shortcut and return to the ClickGUI.";
                  }
                  sceneCornerRadiusServicePadding.tooltip(str2);
                }));
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode2 -> {
                  layoutContainerNode2
                      .id("keybind.footer")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .gap(
                          checkCondition()
                              ? Float.intBitsToFloat(1082130432)
                              : Float.intBitsToFloat(1090519040))
                      .align(ScenePctService.Align.CENTER)
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }))
        .key("footer");
  }

  private static ComponentKeyService<MaterialJoinedService> createComponentKeyService12(
      String str, String str2, Runnable runnable, boolean z) {
    return MaterialTextService.button(
            str,
            str2,
            runnable,
            z ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
            z
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1094713344))
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1107820544)))
                  .minWidth(Float.intBitsToFloat(1105199104))
                  .padding(0.0f, Float.intBitsToFloat(1092616192));
            });
  }

  private void updateState(int i) {
    if (this.keybindTargetService.choose(i)) {
      this.enabled = false;
      this.text3 = "";
      this.count = i;
      updateState8();
      updateState2(i);
      refresh();
      ScenePctService<?> scenePctServiceFindById =
          this.materialTerrainTooltipsService.findById("keybind.selected-key");
      if (scenePctServiceFindById != null) {
        scenePctServiceFindById.scale(Float.intBitsToFloat(1064346583));
        MotionAnimateService.animate(
            scenePctServiceFindById,
            MotionColorsContainer.Floats.SCALE,
            1.0f,
            MaterialIsLightService.SPATIAL);
      }
    }
  }

  private void updateState2(int i) {
    if (i < 0) {
      return;
    }
    if ((i >= 320 && i <= 336) || i == 282) {
      this.fcixh0orecre = "numpad";
      return;
    }
    if ((i >= 302 && i <= 314) || i == 161 || i == 162) {
      this.fcixh0orecre = "navigation";
      return;
    }
    if (KeyCatalog.NAV.stream()
            .flatMap(
                (v0) -> {
                  return v0.stream();
                })
            .anyMatch(
                key -> {
                  return key.code() == i;
                })
        && this.value2 < Float.intBitsToFloat(1148518400)) {
      this.fcixh0orecre = "navigation";
    } else if (KeyCatalog.MAIN.stream()
        .flatMap(
            (v0) -> {
              return v0.stream();
            })
        .anyMatch(
            key2 -> {
              return key2.code() == i;
            })) {
      this.fcixh0orecre = "keyboard";
    }
  }

  private void updateState3() {
    int[] iArr = {71, 86, 66, 72, 74, 75, 76, 82, 70, 90, 88, 67, 298, 299, 300, 301};
    int length = iArr.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= length) {
        this.text3 = "No free suggested key. Inspect the keyboard to choose one.";
        refresh();
        return;
      }
      int i3 = iArr[i2];
      if (i3 != this.keybindTargetService.selected()
          && this.keybindTargetService.uses(i3).isEmpty()
          && KeyCatalog.reserved(i3).isEmpty()) {
        updateState(i3);
        return;
      }
      i = i2 + 1;
    }
  }

  private void updateState4(boolean z) {
    try {
      if (this.keybindTargetService.apply(z)) {
        this.runnable2.run();
      } else {
        this.text3 =
            this.keybindTargetService.canApply()
                ? "Review the shared binding before applying."
                : "This key cannot be assigned here.";
        refresh();
      }
    } catch (RuntimeException e) {
      this.text3 = "Could not update this binding. Try again.";
      refresh();
    }
  }

  public boolean keyPressed(int i, int i2) {
    if (i == 256) {
      this.runnable.run();
      return true;
    }
    if (this.enabled) {
      if (i < 0) {
        return true;
      }
      updateState(i);
      return true;
    }
    if (i == 258) {
      updateState5((i2 & 1) != 0 ? -1 : 1);
      return true;
    }
    if (i != 257 && i != 32) {
      if (i != 263 && i != 262 && i != 265 && i != 264) {
        return true;
      }
      updateState6(i);
      return true;
    }
    ScenePctService<?> scenePctService = this.scenePctService;
    if (scenePctService instanceof KeybindKeyService) {
      ((KeybindKeyService) scenePctService).activate();
      return true;
    }
    ScenePctService<?> scenePctService2 = this.scenePctService;
    if (scenePctService2 instanceof MaterialJoinedService) {
      ((MaterialJoinedService) scenePctService2).activate();
      return true;
    }
    if (i != 257) {
      return true;
    }
    updateState4(false);
    return true;
  }

  private void updateState5(int i) {
    int iFloorMod;
    ArrayList arrayList = new ArrayList();
    updateState9(this.materialTerrainTooltipsService, arrayList);
    if (arrayList.isEmpty()) {
      return;
    }
    int iIndexOf = arrayList.indexOf(this.scenePctService);
    if (iIndexOf < 0) {
      iFloorMod = i < 0 ? arrayList.size() - 1 : 0;
    } else {
      iFloorMod = Math.floorMod(iIndexOf + i, arrayList.size());
    }
    updateState7((ScenePctService) arrayList.get(iFloorMod));
  }

  private void updateState6(int i) {
    float f;
    ArrayList arrayList = new ArrayList();
    updateState9(this.materialTerrainTooltipsService, arrayList);
    List<KeybindKeyService> list =
        arrayList.stream()
            .filter(
                scenePctService -> {
                  return scenePctService instanceof KeybindKeyService;
                })
            .map(
                scenePctService2 -> {
                  return (KeybindKeyService) scenePctService2;
                })
            .toList();
    if (list.isEmpty()) {
      return;
    }
    ScenePctService<?> scenePctService3 = this.scenePctService;
    KeybindKeyService keybindKeyService =
        scenePctService3 instanceof KeybindKeyService
            ? (KeybindKeyService) scenePctService3
            : (KeybindKeyService)
                list.stream()
                    .filter(
                        keybindKeyService2 -> {
                          return keybindKeyService2.code() == this.keybindTargetService.selected();
                        })
                    .findFirst()
                    .orElse((KeybindKeyService) list.getFirst());
    float fComputedX = keybindKeyService.computedX() + (keybindKeyService.computedW() / 2.0f);
    float fComputedY = keybindKeyService.computedY() + (keybindKeyService.computedH() / 2.0f);
    KeybindKeyService keybindKeyService3 = null;
    float fIntBitsToFloat = Float.intBitsToFloat(2139095039);
    boolean z = i == 263 || i == 262;
    for (KeybindKeyService keybindKeyService4 : list) {
      if (keybindKeyService4 != keybindKeyService) {
        float fComputedX2 =
            (keybindKeyService4.computedX() + (keybindKeyService4.computedW() / 2.0f)) - fComputedX;
        float fComputedY2 =
            (keybindKeyService4.computedY() + (keybindKeyService4.computedH() / 2.0f)) - fComputedY;
        if (i == 263) {
          f = -fComputedX2;
        } else if (i == 262) {
          f = fComputedX2;
        } else {
          f = i == 265 ? -fComputedY2 : fComputedY2;
        }
        float f2 = f;
        if (f2 > 1.0f) {
          float fAbs =
              f2 + (Math.abs(z ? fComputedY2 : fComputedX2) * Float.intBitsToFloat(1082130432));
          if (fAbs < fIntBitsToFloat) {
            fIntBitsToFloat = fAbs;
            keybindKeyService3 = keybindKeyService4;
          }
        }
      }
    }
    updateState7(keybindKeyService3 == null ? keybindKeyService : keybindKeyService3);
  }

  private void updateState7(ScenePctService<?> scenePctService) {
    updateState8();
    this.scenePctService = scenePctService;
    if (scenePctService instanceof KeybindKeyService) {
      ((KeybindKeyService) scenePctService).keyboardFocused(true);
    }
    if (scenePctService instanceof MaterialJoinedService) {
      ((MaterialJoinedService) scenePctService).keyboardFocused(true);
    }
    updateState10(scenePctService);
  }

  private void updateState8() {
    ScenePctService<?> scenePctService = this.scenePctService;
    if (scenePctService instanceof KeybindKeyService) {
      ((KeybindKeyService) scenePctService).keyboardFocused(false);
    }
    ScenePctService<?> scenePctService2 = this.scenePctService;
    if (scenePctService2 instanceof MaterialJoinedService) {
      ((MaterialJoinedService) scenePctService2).keyboardFocused(false);
    }
    this.scenePctService = null;
  }

  private static void updateState9(
      ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
    if (scenePctService != null && scenePctService.visible && scenePctService.pointerEvents()) {
      if ((scenePctService instanceof KeybindKeyService)
          || ((scenePctService instanceof MaterialJoinedService)
              && ((MaterialJoinedService) scenePctService).available())) {
        list.add(scenePctService);
      }
      Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
      while (it.hasNext()) {
        updateState9(it.next(), list);
      }
    }
  }

  private static void updateState10(ScenePctService<?> scenePctService) {
    ScenePctService<?> scenePctServiceParent = scenePctService.parent();
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctServiceParent;
      if (scenePctService2 == null) {
        return;
      }
      if (scenePctService2.scrollMin() < 0.0f) {
        boolean zEquals = "keybind.keyboard-scroll".equals(scenePctService2.getId());
        float fComputedX = zEquals ? scenePctService.computedX() : scenePctService.computedY();
        float fComputedW =
            fComputedX + (zEquals ? scenePctService.computedW() : scenePctService.computedH());
        float fComputedX2 = zEquals ? scenePctService2.computedX() : scenePctService2.computedY();
        float fIntBitsToFloat =
            fComputedX < fComputedX2 + Float.intBitsToFloat(1084227584)
                ? (fComputedX2 + Float.intBitsToFloat(1084227584)) - fComputedX
                : Math.min(
                    0.0f,
                    ((fComputedX2
                                + (zEquals
                                    ? scenePctService2.computedW()
                                    : scenePctService2.computedH()))
                            - Float.intBitsToFloat(1084227584))
                        - fComputedW);
        if (fIntBitsToFloat != 0.0f) {
          float fMax =
              Math.max(
                  scenePctService2.scrollMin(),
                  Math.min(0.0f, scenePctService2.scrollY() + fIntBitsToFloat));
          scenePctService2.scrollTarget(fMax);
          MotionAnimateService.animate(
              scenePctService2,
              MotionColorsContainer.Floats.SCROLL_Y,
              fMax,
              MaterialIsLightService.SPATIAL);
        }
      }
      scenePctServiceParent = scenePctService2.parent();
    }
  }
}
