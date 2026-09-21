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
    private final KeybindTargetService fd0bsejmn6ch;
    private final Runnable f1lyhb6gxudb;
    private final Runnable fdlw1ze1q03x;
    private ComponentThemeService f3bju444eeiq;
    private MaterialTerrainTooltipsService f4cdccwvyga1;
    private ScenePctService<?> ficx7w1o1dz3;
    private boolean fcote9xfh50f;
    private float f2z1b50xrxua = Float.intBitsToFloat(1150681088);
    private float fg6szyblyyo1 = Float.intBitsToFloat(1145569280);
    private String fcixh0orecre = "keyboard";
    private String fauk2702al3m = "";
    private boolean f7x5adx5uf09 = true;
    private int fh7zw5aevi1j = -1;

    public KeybindRenderer(KeybindTargetService keybindTargetService, Runnable runnable, Runnable runnable2) {
        this.fd0bsejmn6ch = keybindTargetService;
        this.f1lyhb6gxudb = runnable;
        this.fdlw1ze1q03x = runnable2;
        mgyk58pxzd2a(keybindTargetService.selected());
    }

    public KeybindTargetService selection() {
        return this.fd0bsejmn6ch;
    }

    public boolean listening() {
        return this.f7x5adx5uf09;
    }

    public void viewport(float f, float f2) {
        if (this.f2z1b50xrxua == f && this.fg6szyblyyo1 == f2) {
            return;
        }
        this.f2z1b50xrxua = f;
        this.fg6szyblyyo1 = f2;
        refresh();
    }

    public void refresh() {
        if (this.f3bju444eeiq != null) {
            this.f3bju444eeiq.invalidate();
        }
    }

    private boolean maqw15krrmpy() {
        return this.f2z1b50xrxua < Float.intBitsToFloat(1142620160);
    }

    private boolean mjj5z1ri9xia() {
        return this.fg6szyblyyo1 < Float.intBitsToFloat(1143111680);
    }

    @Override 
    public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
        this.f3bju444eeiq = componentThemeService;
        Supplier<MaterialTerrainTooltipsService> supplier = MaterialTerrainTooltipsService::new;
        Consumer<MaterialTerrainTooltipsService> consumer = materialTerrainTooltipsService -> {
            this.f4cdccwvyga1 = materialTerrainTooltipsService;
            materialTerrainTooltipsService.terrainTooltips(true);
            materialTerrainTooltipsService.id("keybind.picker").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxHeight(Math.max(Float.intBitsToFloat(1124859904), this.fg6szyblyyo1 - (maqw15krrmpy() ? 16 : 48))).backgroundColor(MaterialIsLightService.SURFACE).cornerRadius(Float.intBitsToFloat(1105199104)).direction(ScenePctService.Direction.COLUMN).padding(mjj5z1ri9xia() ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(1101004800)).gap(mjj5z1ri9xia() ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1094713344)).clip(true);
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[4];
        componentKeyServiceArr[0] = mjmo9fpt16iv();
        Consumer<LayoutContainerNode> consumer2 = layoutContainerNode -> {
            layoutContainerNode.id("keybind.body").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(0.0f).flexShrink(1.0f).gap(mjj5z1ri9xia() ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1094713344)).scrollable(true).scrollbarWidth(Float.intBitsToFloat(1077936128)).scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT).padding(2.0f, Float.intBitsToFloat(1082130432), 2.0f, 2.0f).clip(true);
        };
        ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[4];
        componentKeyServiceArr2[0] = (mjj5z1ri9xia() ? ComponentBoxService.box(layoutContainerNode2 -> {
            layoutContainerNode2.visible(false);
        }, new ComponentKeyService[0]) : m3qb1tg546h2()).props(scenePctService -> {
            scenePctService.flexShrink(0.0f);
        });
        componentKeyServiceArr2[1] = m6avgk6wa94q().props(scenePctService2 -> {
            scenePctService2.flexShrink(0.0f);
        });
        componentKeyServiceArr2[2] = m9zrgn03ig63().props(scenePctService3 -> {
            scenePctService3.flexShrink(0.0f);
        });
        componentKeyServiceArr2[3] = m73gcmtowhwp().props(scenePctService4 -> {
            scenePctService4.flexShrink(0.0f);
        });
        componentKeyServiceArr[1] = ComponentBoxService.column((Consumer<LayoutContainerNode>) consumer2, (ComponentKeyService<?>[]) componentKeyServiceArr2);
        componentKeyServiceArr[2] = m970hpg806n1().props(scenePctService5 -> {
            scenePctService5.flexShrink(0.0f);
        });
        componentKeyServiceArr[3] = m4jkqzsgdibd();
        return ComponentBoxService.node("keybind-picker-scope", supplier, consumer, componentKeyServiceArr).key("keybind-picker");
    }

    private ComponentKeyService<?> mjmo9fpt16iv() {
        ArrayList arrayList = new ArrayList();
        if (!maqw15krrmpy()) {
            arrayList.add(ComponentBoxService.panel(sceneCornerRadiusService -> {
                sceneCornerRadiusService.size(Float.intBitsToFloat(1109917696), Float.intBitsToFloat(1109917696)).cornerRadius(Float.intBitsToFloat(1096810496)).backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER).flexShrink(0.0f);
            }, MaterialTextService.icon("keyboard", MaterialIsLightService.PRIMARY)).key("emblem"));
        }
        Consumer<LayoutContainerNode> consumer = layoutContainerNode -> {
            layoutContainerNode.flex(1.0f).minWidth(0.0f).gap(0.0f);
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
        componentKeyServiceArr[0] = MaterialTextService.text("Choose a key", maqw15krrmpy() ? Float.intBitsToFloat(1100480512) : Float.intBitsToFloat(1103101952), MaterialIsLightService.ON_SURFACE);
        componentKeyServiceArr[1] = MaterialTextService.text(this.fd0bsejmn6ch.target().owner() + "  /  " + this.fd0bsejmn6ch.target().title(), Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
            sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        });
        arrayList.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr).key("heading"));
        if (mjj5z1ri9xia()) {
            arrayList.add(MaterialTextService.text(this.fd0bsejmn6ch.name(this.fd0bsejmn6ch.selected()), Float.intBitsToFloat(1094713344), MaterialIsLightService.PRIMARY).props(sceneTextService2 -> {
                sceneTextService2.maxWidth(Float.intBitsToFloat(1118044160)).flexShrink(0.0f);
            }));
        }
        arrayList.add(MaterialTextService.iconButton("keybind.close", "close", "Cancel selection\nClose without changing the current binding. Esc", this.f1lyhb6gxudb).props(materialJoinedService -> {
            materialJoinedService.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1108344832));
        }));
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.id("keybind.header").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).align(ScenePctService.Align.CENTER).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("header");
    }

    private ComponentKeyService<?> m3qb1tg546h2() {
        String str;
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).backgroundColor(MaterialIsLightService.SURFACE_LOW).cornerRadius(Float.intBitsToFloat(1101004800)).padding(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1098907648)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).gap(Float.intBitsToFloat(1098907648));
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
        Consumer<SceneCornerRadiusService> consumer2 = sceneCornerRadiusService2 -> {
            sceneCornerRadiusService2.id("keybind.selected-key").size(maqw15krrmpy() ? Float.intBitsToFloat(1116995584) : Float.intBitsToFloat(1120403456), Float.intBitsToFloat(1116471296)).backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER).cornerRadius(Float.intBitsToFloat(1098907648)).direction(ScenePctService.Direction.COLUMN).justify(ScenePctService.Justify.CENTER).align(ScenePctService.Align.CENTER).padding(Float.intBitsToFloat(1090519040)).flexShrink(0.0f);
        };
        ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[1];
        componentKeyServiceArr2[0] = MaterialTextService.text(this.fd0bsejmn6ch.name(this.fd0bsejmn6ch.selected()), this.fd0bsejmn6ch.name(this.fd0bsejmn6ch.selected()).length() > 5 ? Float.intBitsToFloat(1095761920) : Float.intBitsToFloat(1103101952), MaterialIsLightService.PRIMARY).props(sceneTextService -> {
            sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).textAlign(SceneTextService.TextAlign.CENTER);
        });
        componentKeyServiceArr[0] = ComponentBoxService.panel(consumer2, componentKeyServiceArr2);
        Consumer<LayoutContainerNode> consumer3 = layoutContainerNode -> {
            layoutContainerNode.flex(1.0f).minWidth(0.0f).gap(Float.intBitsToFloat(1077936128));
        };
        ComponentKeyService[] componentKeyServiceArr3 = new ComponentKeyService[3];
        if (this.f7x5adx5uf09) {
            str = "Listening for your next key…";
        } else {
            str = this.fd0bsejmn6ch.selected() < 0 ? "No key assigned" : "Ready when you are";
        }
        componentKeyServiceArr3[0] = MaterialTextService.text(str, maqw15krrmpy() ? Float.intBitsToFloat(1097859072) : Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE).props(sceneTextService2 -> {
            sceneTextService2.wordWrap(true).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        });
        componentKeyServiceArr3[1] = MaterialTextService.text(this.f7x5adx5uf09 ? "Click a keycap or press a key on your keyboard." : "Inspect the assignments below, then apply your selection.", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService3 -> {
            sceneTextService3.wordWrap(true).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        });
        componentKeyServiceArr3[2] = MaterialTextService.text("Current: " + this.fd0bsejmn6ch.name(this.fd0bsejmn6ch.original()), Float.intBitsToFloat(1092616192), MaterialIsLightService.PRIMARY);
        componentKeyServiceArr[1] = ComponentBoxService.column((Consumer<LayoutContainerNode>) consumer3, (ComponentKeyService<?>[]) componentKeyServiceArr3);
        return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("selected-card");
    }

    private ComponentKeyService<?> m6avgk6wa94q() {
        ArrayList arrayList = new ArrayList();
        for (String str : List.of("keyboard", "navigation", "numpad")) {
            arrayList.add(m6wcotd3aoo2("keybind.page." + str, str.equals("navigation") ? "Nav / F13+" : str.equals("numpad") ? "Numpad" : "Keyboard", () -> {
                this.fcixh0orecre = str;
                makfg0o28j46();
                refresh();
            }, this.fcixh0orecre.equals(str)).props(materialJoinedService -> {
                materialJoinedService.flex(1.0f).minWidth(0.0f).padding(0.0f, maqw15krrmpy() ? Float.intBitsToFloat(1084227584) : Float.intBitsToFloat(1094713344));
            }).key(str));
        }
        ComponentKeyService<LayoutContainerNode> componentKeyServiceRow = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.gap(Float.intBitsToFloat(1082130432)).flex(1.0f).minWidth(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        }));
        Consumer<LayoutContainerNode> consumer = layoutContainerNode2 -> {
            layoutContainerNode2.gap(Float.intBitsToFloat(1082130432)).align(ScenePctService.Align.CENTER);
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
        componentKeyServiceArr[0] = m6wcotd3aoo2("keybind.listen", this.f7x5adx5uf09 ? "Listening…" : "Press a key", () -> {
            this.f7x5adx5uf09 = !this.f7x5adx5uf09;
            makfg0o28j46();
            refresh();
        }, this.f7x5adx5uf09).props(materialJoinedService2 -> {
            materialJoinedService2.tooltip("Listen for a key\nThe next physical key selects a candidate. Nothing changes until you apply it. Esc cancels.");
        });
        componentKeyServiceArr[1] = m6wcotd3aoo2("keybind.fit", this.fcote9xfh50f ? "Fit" : "Larger keys", () -> {
            this.fcote9xfh50f = !this.fcote9xfh50f;
            refresh();
        }, this.fcote9xfh50f).props(materialJoinedService3 -> {
            materialJoinedService3.tooltip("Keyboard size\nFit the whole keyboard or use larger keycaps and scroll sideways.");
        });
        ComponentKeyService<LayoutContainerNode> componentKeyServiceRow2 = ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr);
        return maqw15krrmpy() ? ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode3 -> {
            layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1086324736));
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{componentKeyServiceRow.props(scenePctService -> {
            scenePctService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        }), componentKeyServiceRow2.props(scenePctService2 -> {
            scenePctService2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        })}).key("tools") : ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode4 -> {
            layoutContainerNode4.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1098907648)).align(ScenePctService.Align.CENTER);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{componentKeyServiceRow, componentKeyServiceRow2}).key("tools");
    }

    private ComponentKeyService<?> m9zrgn03ig63() {
        float fIntBitsToFloat;
        ComponentKeyService<?> componentKeyServiceRow;
        if (this.fg6szyblyyo1 < Float.intBitsToFloat(1137180672)) {
            fIntBitsToFloat = Float.intBitsToFloat(1106247680);
        } else if (mjj5z1ri9xia()) {
            fIntBitsToFloat = maqw15krrmpy() ? 36 : 31;
        } else {
            fIntBitsToFloat = this.f2z1b50xrxua < Float.intBitsToFloat(1142620160) ? Float.intBitsToFloat(1109917696) : Float.intBitsToFloat(1112539136);
        }
        float f = fIntBitsToFloat;
        if (this.fcixh0orecre.equals("numpad")) {
            componentKeyServiceRow = m4t7f8rgu17c(f);
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
                arrayList.add(mazoopzl0wh8(arrayList2, i2 + 8, f));
                i = i2 + 1;
            }
            componentKeyServiceRow = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1099956224));
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{m39fbw59om0b(KeyCatalog.NAV, 3, f).props(scenePctService -> {
                scenePctService.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
            }), ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.flex(Float.intBitsToFloat(1084227584)).minWidth(0.0f).gap(0.0f);
            }, (ComponentKeyService<?>[]) arrayList.toArray(i6 -> {
                return new ComponentKeyService[i6];
            }))});
        } else {
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(m39fbw59om0b(KeyCatalog.MAIN, 0, f).props(scenePctService2 -> {
                scenePctService2.flex(Float.intBitsToFloat(1097859072)).minWidth(0.0f);
            }));
            if (this.f2z1b50xrxua >= Float.intBitsToFloat(1148518400)) {
                arrayList3.add(m39fbw59om0b(KeyCatalog.NAV, 6, f).props(scenePctService3 -> {
                    scenePctService3.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
                }));
            }
            componentKeyServiceRow = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode3 -> {
                layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(this.f2z1b50xrxua >= Float.intBitsToFloat(1148518400) ? Float.intBitsToFloat(1099956224) : 0.0f);
            }, (ComponentKeyService<?>[]) arrayList3.toArray(i7 -> {
                return new ComponentKeyService[i7];
            }));
        }
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.id("keybind.deck").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).backgroundColor(MaterialIsLightService.SURFACE_LOWEST).border(Float.intBitsToFloat(1059481190), MaterialIsLightService.OUTLINE_VARIANT).cornerRadius(Float.intBitsToFloat(1101004800)).padding(maqw15krrmpy() ? Float.intBitsToFloat(1086324736) : Float.intBitsToFloat(1094713344)).direction(ScenePctService.Direction.COLUMN).gap(Float.intBitsToFloat(1086324736));
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
        componentKeyServiceArr[0] = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode4 -> {
            layoutContainerNode4.id("keybind.keyboard-scroll").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(f * Float.intBitsToFloat(1086324736))).flexShrink(0.0f).scrollable(true).scrollbarWidth(Float.intBitsToFloat(1077936128)).scrollbarColor(MaterialIsLightService.OUTLINE).clip(true);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{componentKeyServiceRow.props(scenePctService4 -> {
            float f2;
            ScenePctService scenePctServiceId = scenePctService4.id("keybind.keyboard");
            if (this.fcote9xfh50f) {
                f2 = this.fcixh0orecre.equals("keyboard") ? 780 : 440;
            } else {
                f2 = 0.0f;
            }
            scenePctServiceId.minWidth(f2).flexShrink(0.0f);
        }).key("board:" + this.fcixh0orecre)});
        componentKeyServiceArr[1] = MaterialTextService.text(this.fcote9xfh50f ? "Scroll sideways to reach every key" : "System key labels · click a keycap to select", Float.intBitsToFloat(1091567616), MaterialIsLightService.OUTLINE).props(sceneTextService -> {
            sceneTextService.padding(2.0f, Float.intBitsToFloat(1082130432));
        });
        return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("keyboard-deck");
    }

    private ComponentKeyService<?> m39fbw59om0b(List<List<KeyCatalog.Key>> list, int i, float f) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= list.size()) {
                return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.gap(0.0f);
                }, (ComponentKeyService<?>[]) arrayList.toArray(i4 -> {
                    return new ComponentKeyService[i4];
                }));
            }
            arrayList.add(mazoopzl0wh8(list.get(i3), i3 + i, f));
            i2 = i3 + 1;
        }
    }

    private ComponentKeyService<?> mazoopzl0wh8(List<KeyCatalog.Key> list, int i, float f) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= list.size()) {
                return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(f)).flexShrink(0.0f);
                }, (ComponentKeyService<?>[]) arrayList.toArray(i4 -> {
                    return new ComponentKeyService[i4];
                })).key("row:" + i);
            }
            KeyCatalog.Key key = list.get(i3);
            if (key.spacer()) {
                arrayList.add(ComponentBoxService.box(layoutContainerNode2 -> {
                    layoutContainerNode2.flex(key.units()).minWidth(0.0f);
                }, new ComponentKeyService[0]).key("gap:" + i3));
            } else {
                arrayList.add(m9k8gjgnacff(key, i, i3).props(keybindKeyService -> {
                    keybindKeyService.flex(key.units()).minWidth(0.0f).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                }));
            }
            i2 = i3 + 1;
        }
    }

    private ComponentKeyService<KeybindKeyService> m9k8gjgnacff(KeyCatalog.Key key, int i, int i2) {
        String strLabel;
        int i3;
        List<KeybindTargetService.Use> listUses = this.fd0bsejmn6ch.uses(key.code());
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
        if (listUses.stream().anyMatch(use -> {
            return !use.minecraft();
        })) {
            i3 = ELLICE;
        } else {
            i3 = listUses.isEmpty() ? 0 : GAME;
        }
        int i4 = i3;
        boolean z = this.fd0bsejmn6ch.target().read().getAsInt() == key.code();
        String strName = this.fd0bsejmn6ch.name(key.code());
        if (this.fcixh0orecre.equals("numpad") && strName.startsWith("Num ")) {
            strName = strName.substring(4);
        }
        String str = strName;
        String str2 = strSubstring;
        return ComponentBoxService.node("keyboard-keycap", KeybindKeyService::new, keybindKeyService -> {
            keybindKeyService.key(key.code(), str, m5nly3moe55u(key.code(), str2), i4, this.fd0bsejmn6ch.selected() == key.code(), z, !strReserved.isEmpty()).id("keybind.key." + key.code()).tooltip(this.fd0bsejmn6ch.tooltip(key.code())).onClick(() -> {
                m9mev0awv1y9(key.code());
            });
            if (this.fh7zw5aevi1j == key.code()) {
                keybindKeyService.pulse();
                this.fh7zw5aevi1j = -1;
            }
        }, new ComponentKeyService[0]).key("key:" + key.code()).onMount(keybindKeyService2 -> {
            keybindKeyService2.opacity(0.0f);
            keybindKeyService2.translateY(Float.intBitsToFloat(1088421888));
            float fMin = Math.min(Float.intBitsToFloat(1041194025), (i * Float.intBitsToFloat(1014350479)) + (i2 * Float.intBitsToFloat(990057071)));
            MotionAnimateService.animate(keybindKeyService2, MotionColorsContainer.Floats.OPACITY, 1.0f, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)), fMin);
            MotionAnimateService.animate(keybindKeyService2, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0f, MaterialIsLightService.SPATIAL, fMin);
        });
    }

    private String m5nly3moe55u(int i, String str) {
        return i == this.fd0bsejmn6ch.original() ? "Current" : str;
    }

    private ComponentKeyService<?> m4t7f8rgu17c(float f) {
        return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).align(ScenePctService.Align.CENTER);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxWidth(Float.intBitsToFloat(1138491392)).gap(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{mazoopzl0wh8(List.of(KeyCatalog.key(282), KeyCatalog.key(331), KeyCatalog.key(332), KeyCatalog.key(333)), 30, f), ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode3 -> {
            layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode4 -> {
            layoutContainerNode4.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{mazoopzl0wh8(List.of(KeyCatalog.key(327), KeyCatalog.key(328), KeyCatalog.key(329)), 31, f), mazoopzl0wh8(List.of(KeyCatalog.key(324), KeyCatalog.key(325), KeyCatalog.key(326)), 32, f)}), m9k8gjgnacff(KeyCatalog.key(334), 31, 3).props(keybindKeyService -> {
            keybindKeyService.flex(1.0f).minWidth(0.0f).height(LayoutOperationHandler.px(f * 2.0f));
        })}), ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode5 -> {
            layoutContainerNode5.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode6 -> {
            layoutContainerNode6.flex(Float.intBitsToFloat(1077936128)).minWidth(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{mazoopzl0wh8(List.of(KeyCatalog.key(321), KeyCatalog.key(322), KeyCatalog.key(323)), 33, f), mazoopzl0wh8(List.of(KeyCatalog.key(320, 2.0f), KeyCatalog.key(330)), 34, f)}), m9k8gjgnacff(KeyCatalog.key(335), 33, 3).props(keybindKeyService2 -> {
            keybindKeyService2.flex(1.0f).minWidth(0.0f).height(LayoutOperationHandler.px(f * 2.0f));
        })}), mazoopzl0wh8(List.of(KeyCatalog.key(336), KeyCatalog.gap(Float.intBitsToFloat(1077936128))), 35, f)})});
    }

    private ComponentKeyService<?> m73gcmtowhwp() {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"Selected", "ellice bind", "Minecraft", "Reserved"};
        int[] iArr = {MaterialIsLightService.PRIMARY, ELLICE, GAME, MaterialIsLightService.OUTLINE};
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= strArr.length) {
                return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(maqw15krrmpy() ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(1101004800)).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER);
                }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                })).key("legend");
            }
            int i4 = iArr[i2];
            Consumer<LayoutContainerNode> consumer = layoutContainerNode2 -> {
                layoutContainerNode2.align(ScenePctService.Align.CENTER).gap(Float.intBitsToFloat(1084227584)).minWidth(0.0f);
            };
            ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
            componentKeyServiceArr[0] = ComponentBoxService.panel(sceneCornerRadiusService -> {
                sceneCornerRadiusService.size(Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584)).cornerRadius(Float.intBitsToFloat(1075838976)).backgroundColor(i4).flexShrink(0.0f);
            }, new ComponentKeyService[0]);
            componentKeyServiceArr[1] = MaterialTextService.text(strArr[i2], maqw15krrmpy() ? Float.intBitsToFloat(1091567616) : Float.intBitsToFloat(1092616192), MaterialIsLightService.ON_SURFACE_VARIANT);
            arrayList.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr));
            i = i2 + 1;
        }
    }

    private ComponentKeyService<?> m970hpg806n1() {
        String str;
        String strJoin;
        String strReserved = KeyCatalog.reserved(this.fd0bsejmn6ch.selected());
        List<KeybindTargetService.Use> listUses = this.fd0bsejmn6ch.uses(this.fd0bsejmn6ch.selected());
        boolean z = this.fd0bsejmn6ch.selected() >= 0 && this.fd0bsejmn6ch.selected() == this.fd0bsejmn6ch.target().read().getAsInt();
        if (!this.fauk2702al3m.isEmpty()) {
            str = this.fauk2702al3m;
        } else if (!strReserved.isEmpty()) {
            str = "Reserved for " + strReserved;
        } else if (this.fd0bsejmn6ch.selected() < 0) {
            str = "Remove this shortcut";
        } else if (this.fd0bsejmn6ch.hasElliceConflict()) {
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
        } else if (this.fd0bsejmn6ch.selected() < 0) {
            strJoin = "Apply to remove the current binding.";
        } else if (listUses.isEmpty()) {
            strJoin = z ? "Choose another key to change it, or close to keep it." : "Apply when you're happy with your selection.";
        } else {
            strJoin = String.join(" · ", listUses.stream().map((v0) -> {
                return v0.label();
            }).toList());
        }
        String str3 = strJoin;
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.id("keybind.assignment-info").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1098907648)).padding(mjj5z1ri9xia() ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1077936128)).direction(ScenePctService.Direction.COLUMN).tooltip(this.fd0bsejmn6ch.tooltip(this.fd0bsejmn6ch.selected()));
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
        String str4 = str2;
        float fIntBitsToFloat = Float.intBitsToFloat(1094713344);
        int i = (strReserved.isEmpty() && !this.fd0bsejmn6ch.hasElliceConflict()) ? ELLICE : GAME;
        componentKeyServiceArr[0] = MaterialTextService.text(str4, fIntBitsToFloat, i).props(sceneTextService -> {
            sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap(true);
        });
        componentKeyServiceArr[1] = MaterialTextService.text(str3, Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService2 -> {
            sceneTextService2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap(true).maxLines(3);
        });
        return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("assignment-info");
    }

    private ComponentKeyService<?> m4jkqzsgdibd() {
        String str;
        ArrayList arrayList = new ArrayList();
        arrayList.add(m6wcotd3aoo2("keybind.clear", "Unbind", () -> {
            m9mev0awv1y9(-1);
        }, false).props(materialJoinedService -> {
            materialJoinedService.tooltip("Remove binding\nStage an unassigned key. Apply to save it.");
        }));
        arrayList.add(m6wcotd3aoo2("keybind.suggest", maqw15krrmpy() ? "Free key" : "Find a free key", this::miju3brqpsf7, false).props(materialJoinedService2 -> {
            materialJoinedService2.tooltip("Find a free key\nSuggest a key unused by ellice and Minecraft.");
        }));
        arrayList.add(ComponentBoxService.box(layoutContainerNode -> {
            layoutContainerNode.flex(1.0f).minWidth(0.0f);
        }, new ComponentKeyService[0]));
        if (!maqw15krrmpy()) {
            arrayList.add(m6wcotd3aoo2("keybind.cancel", "Cancel", this.f1lyhb6gxudb, false));
        }
        boolean zHasElliceConflict = this.fd0bsejmn6ch.hasElliceConflict();
        if (this.fd0bsejmn6ch.replacesModule()) {
            str = "Replace bind";
        } else {
            str = zHasElliceConflict ? "Use anyway" : "Apply";
        }
        arrayList.add(MaterialTextService.button("keybind.apply", str, () -> {
            m3410v50j71f(zHasElliceConflict);
        }, MaterialIsLightService.PRIMARY, MaterialIsLightService.ON_PRIMARY, Float.intBitsToFloat(1096810496)).props(materialJoinedService3 -> {
            String str2;
            materialJoinedService3.available(this.fd0bsejmn6ch.canApply());
            SceneCornerRadiusService sceneCornerRadiusServicePadding = materialJoinedService3.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108869120))).minWidth(Float.intBitsToFloat(1114636288)).padding(0.0f, maqw15krrmpy() ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(1099956224));
            if (this.fd0bsejmn6ch.replacesModule()) {
                str2 = "Replace the existing module binding\nThis key will toggle " + this.fd0bsejmn6ch.target().owner() + ". The previous module loses this shortcut.";
            } else {
                str2 = zHasElliceConflict ? "Keep the shared key\nBoth ellice actions will use this key. Apply only if that is what you want." : "Apply selection\nSave this shortcut and return to the ClickGUI.";
            }
            sceneCornerRadiusServicePadding.tooltip(str2);
        }));
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.id("keybind.footer").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(maqw15krrmpy() ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("footer");
    }

    private static ComponentKeyService<MaterialJoinedService> m6wcotd3aoo2(String str, String str2, Runnable runnable, boolean z) {
        return MaterialTextService.button(str, str2, runnable, z ? MaterialIsLightService.SECONDARY_CONTAINER : 0, z ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1094713344)).props(materialJoinedService -> {
            materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1107820544))).minWidth(Float.intBitsToFloat(1105199104)).padding(0.0f, Float.intBitsToFloat(1092616192));
        });
    }

    private void m9mev0awv1y9(int i) {
        if (this.fd0bsejmn6ch.choose(i)) {
            this.f7x5adx5uf09 = false;
            this.fauk2702al3m = "";
            this.fh7zw5aevi1j = i;
            makfg0o28j46();
            mgyk58pxzd2a(i);
            refresh();
            ScenePctService<?> scenePctServiceFindById = this.f4cdccwvyga1.findById("keybind.selected-key");
            if (scenePctServiceFindById != null) {
                scenePctServiceFindById.scale(Float.intBitsToFloat(1064346583));
                MotionAnimateService.animate(scenePctServiceFindById, MotionColorsContainer.Floats.SCALE, 1.0f, MaterialIsLightService.SPATIAL);
            }
        }
    }

    private void mgyk58pxzd2a(int i) {
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
        if (KeyCatalog.NAV.stream().flatMap((v0) -> {
            return v0.stream();
        }).anyMatch(key -> {
            return key.code() == i;
        }) && this.f2z1b50xrxua < Float.intBitsToFloat(1148518400)) {
            this.fcixh0orecre = "navigation";
        } else if (KeyCatalog.MAIN.stream().flatMap((v0) -> {
            return v0.stream();
        }).anyMatch(key2 -> {
            return key2.code() == i;
        })) {
            this.fcixh0orecre = "keyboard";
        }
    }

    private void miju3brqpsf7() {
        int[] iArr = {71, 86, 66, 72, 74, 75, 76, 82, 70, 90, 88, 67, 298, 299, 300, 301};
        int length = iArr.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                this.fauk2702al3m = "No free suggested key. Inspect the keyboard to choose one.";
                refresh();
                return;
            }
            int i3 = iArr[i2];
            if (i3 != this.fd0bsejmn6ch.selected() && this.fd0bsejmn6ch.uses(i3).isEmpty() && KeyCatalog.reserved(i3).isEmpty()) {
                m9mev0awv1y9(i3);
                return;
            }
            i = i2 + 1;
        }
    }

    private void m3410v50j71f(boolean z) {
        try {
            if (this.fd0bsejmn6ch.apply(z)) {
                this.fdlw1ze1q03x.run();
            } else {
                this.fauk2702al3m = this.fd0bsejmn6ch.canApply() ? "Review the shared binding before applying." : "This key cannot be assigned here.";
                refresh();
            }
        } catch (RuntimeException e) {
            this.fauk2702al3m = "Could not update this binding. Try again.";
            refresh();
        }
    }

    public boolean keyPressed(int i, int i2) {
        if (i == 256) {
            this.f1lyhb6gxudb.run();
            return true;
        }
        if (this.f7x5adx5uf09) {
            if (i < 0) {
                return true;
            }
            m9mev0awv1y9(i);
            return true;
        }
        if (i == 258) {
            m7m0aswl23d6((i2 & 1) != 0 ? -1 : 1);
            return true;
        }
        if (i != 257 && i != 32) {
            if (i != 263 && i != 262 && i != 265 && i != 264) {
                return true;
            }
            m5v1t2j3by1s(i);
            return true;
        }
        ScenePctService<?> scenePctService = this.ficx7w1o1dz3;
        if (scenePctService instanceof KeybindKeyService) {
            ((KeybindKeyService) scenePctService).activate();
            return true;
        }
        ScenePctService<?> scenePctService2 = this.ficx7w1o1dz3;
        if (scenePctService2 instanceof MaterialJoinedService) {
            ((MaterialJoinedService) scenePctService2).activate();
            return true;
        }
        if (i != 257) {
            return true;
        }
        m3410v50j71f(false);
        return true;
    }

    private void m7m0aswl23d6(int i) {
        int iFloorMod;
        ArrayList arrayList = new ArrayList();
        mikm4raofd8p(this.f4cdccwvyga1, arrayList);
        if (arrayList.isEmpty()) {
            return;
        }
        int iIndexOf = arrayList.indexOf(this.ficx7w1o1dz3);
        if (iIndexOf < 0) {
            iFloorMod = i < 0 ? arrayList.size() - 1 : 0;
        } else {
            iFloorMod = Math.floorMod(iIndexOf + i, arrayList.size());
        }
        md5yv83y31d4((ScenePctService) arrayList.get(iFloorMod));
    }

    private void m5v1t2j3by1s(int i) {
        float f;
        ArrayList arrayList = new ArrayList();
        mikm4raofd8p(this.f4cdccwvyga1, arrayList);
        List<KeybindKeyService> list = arrayList.stream().filter(scenePctService -> {
            return scenePctService instanceof KeybindKeyService;
        }).map(scenePctService2 -> {
            return (KeybindKeyService) scenePctService2;
        }).toList();
        if (list.isEmpty()) {
            return;
        }
        ScenePctService<?> scenePctService3 = this.ficx7w1o1dz3;
        KeybindKeyService keybindKeyService = scenePctService3 instanceof KeybindKeyService ? (KeybindKeyService) scenePctService3 : (KeybindKeyService) list.stream().filter(keybindKeyService2 -> {
            return keybindKeyService2.code() == this.fd0bsejmn6ch.selected();
        }).findFirst().orElse((KeybindKeyService) list.getFirst());
        float fComputedX = keybindKeyService.computedX() + (keybindKeyService.computedW() / 2.0f);
        float fComputedY = keybindKeyService.computedY() + (keybindKeyService.computedH() / 2.0f);
        KeybindKeyService keybindKeyService3 = null;
        float fIntBitsToFloat = Float.intBitsToFloat(2139095039);
        boolean z = i == 263 || i == 262;
        for (KeybindKeyService keybindKeyService4 : list) {
            if (keybindKeyService4 != keybindKeyService) {
                float fComputedX2 = (keybindKeyService4.computedX() + (keybindKeyService4.computedW() / 2.0f)) - fComputedX;
                float fComputedY2 = (keybindKeyService4.computedY() + (keybindKeyService4.computedH() / 2.0f)) - fComputedY;
                if (i == 263) {
                    f = -fComputedX2;
                } else if (i == 262) {
                    f = fComputedX2;
                } else {
                    f = i == 265 ? -fComputedY2 : fComputedY2;
                }
                float f2 = f;
                if (f2 > 1.0f) {
                    float fAbs = f2 + (Math.abs(z ? fComputedY2 : fComputedX2) * Float.intBitsToFloat(1082130432));
                    if (fAbs < fIntBitsToFloat) {
                        fIntBitsToFloat = fAbs;
                        keybindKeyService3 = keybindKeyService4;
                    }
                }
            }
        }
        md5yv83y31d4(keybindKeyService3 == null ? keybindKeyService : keybindKeyService3);
    }

    private void md5yv83y31d4(ScenePctService<?> scenePctService) {
        makfg0o28j46();
        this.ficx7w1o1dz3 = scenePctService;
        if (scenePctService instanceof KeybindKeyService) {
            ((KeybindKeyService) scenePctService).keyboardFocused(true);
        }
        if (scenePctService instanceof MaterialJoinedService) {
            ((MaterialJoinedService) scenePctService).keyboardFocused(true);
        }
        m5jwkhkfl0y7(scenePctService);
    }

    private void makfg0o28j46() {
        ScenePctService<?> scenePctService = this.ficx7w1o1dz3;
        if (scenePctService instanceof KeybindKeyService) {
            ((KeybindKeyService) scenePctService).keyboardFocused(false);
        }
        ScenePctService<?> scenePctService2 = this.ficx7w1o1dz3;
        if (scenePctService2 instanceof MaterialJoinedService) {
            ((MaterialJoinedService) scenePctService2).keyboardFocused(false);
        }
        this.ficx7w1o1dz3 = null;
    }

    private static void mikm4raofd8p(ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
        if (scenePctService != null && scenePctService.visible && scenePctService.pointerEvents()) {
            if ((scenePctService instanceof KeybindKeyService) || ((scenePctService instanceof MaterialJoinedService) && ((MaterialJoinedService) scenePctService).available())) {
                list.add(scenePctService);
            }
            Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
            while (it.hasNext()) {
                mikm4raofd8p(it.next(), list);
            }
        }
    }

    private static void m5jwkhkfl0y7(ScenePctService<?> scenePctService) {
        ScenePctService<?> scenePctServiceParent = scenePctService.parent();
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctServiceParent;
            if (scenePctService2 == null) {
                return;
            }
            if (scenePctService2.scrollMin() < 0.0f) {
                boolean zEquals = "keybind.keyboard-scroll".equals(scenePctService2.getId());
                float fComputedX = zEquals ? scenePctService.computedX() : scenePctService.computedY();
                float fComputedW = fComputedX + (zEquals ? scenePctService.computedW() : scenePctService.computedH());
                float fComputedX2 = zEquals ? scenePctService2.computedX() : scenePctService2.computedY();
                float fIntBitsToFloat = fComputedX < fComputedX2 + Float.intBitsToFloat(1084227584) ? (fComputedX2 + Float.intBitsToFloat(1084227584)) - fComputedX : Math.min(0.0f, ((fComputedX2 + (zEquals ? scenePctService2.computedW() : scenePctService2.computedH())) - Float.intBitsToFloat(1084227584)) - fComputedW);
                if (fIntBitsToFloat != 0.0f) {
                    float fMax = Math.max(scenePctService2.scrollMin(), Math.min(0.0f, scenePctService2.scrollY() + fIntBitsToFloat));
                    scenePctService2.scrollTarget(fMax);
                    MotionAnimateService.animate(scenePctService2, MotionColorsContainer.Floats.SCROLL_Y, fMax, MaterialIsLightService.SPATIAL);
                }
            }
            scenePctServiceParent = scenePctService2.parent();
        }
    }
}
