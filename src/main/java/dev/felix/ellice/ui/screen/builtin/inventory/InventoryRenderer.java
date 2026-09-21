


package dev.felix.ellice.ui.screen.builtin.inventory;

import dev.felix.ellice.feature.inventory.InventoryRoleData;
import dev.felix.ellice.module.ModuleLayoutService;
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
import dev.felix.ellice.ui.screen.builtin.inventory.InventoryRoleService;
import java.util.ArrayList;

public final class InventoryRenderer
implements ComponentOperationHandler {
    private InventoryRoleData f1e90p6nm57i;
    private int f4nfd9kx3zq4;
    private boolean fbc2hcsc4hiq;
    private float f2nm5vdd1q10 = Float.intBitsToFloat(1146552320);
    private ComponentThemeService f7yxt5pb51gr;

    public InventoryRenderer(InventoryRoleData inventoryRoleData) {
        this.f1e90p6nm57i = inventoryRoleData;
    }

    public InventoryRoleData draft() {
        return this.f1e90p6nm57i;
    }

    public void viewport(float f) {
        this.f2nm5vdd1q10 = f;
    }

    public void select(int n) {
        if (this.fbc2hcsc4hiq) {
            this.f1e90p6nm57i = this.f1e90p6nm57i.swap(this.f4nfd9kx3zq4, n);
            this.fbc2hcsc4hiq = false;
        }
        this.f4nfd9kx3zq4 = n;
        this.m5pkfdqfrjqs();
    }

    public void assign(InventoryRoleData.Role role) {
        this.f1e90p6nm57i = this.f1e90p6nm57i.with(this.f4nfd9kx3zq4, role);
        this.fbc2hcsc4hiq = false;
        this.m5pkfdqfrjqs();
    }

    private void m5nrryg3kxff(InventoryRoleData inventoryRoleData) {
        this.f1e90p6nm57i = inventoryRoleData;
        this.fbc2hcsc4hiq = false;
        this.m5pkfdqfrjqs();
    }

    private void m5pkfdqfrjqs() {
        if (this.f7yxt5pb51gr != null) {
            this.f7yxt5pb51gr.invalidate();
        }
    }

    public boolean keyPressed(int n, int n2) {
        if (n >= 49 && n <= 57) {
            this.select(n - 49);
            return true;
        }
        if ((n2 & 1) != 0 && (n == 263 || n == 262)) {
            int n3 = Math.clamp((long)(this.f4nfd9kx3zq4 + (n == 263 ? -1 : 1)), 0, 8);
            this.f1e90p6nm57i = this.f1e90p6nm57i.swap(this.f4nfd9kx3zq4, n3);
            this.f4nfd9kx3zq4 = n3;
            this.m5pkfdqfrjqs();
            return true;
        }
        return false;
    }

    @Override
    public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
        int n;
        int n2;
        this.f7yxt5pb51gr = componentThemeService;
        int n3 = this.f2nm5vdd1q10 < Float.intBitsToFloat(0x44200000) ? 1 : 0;
        ArrayList arrayList = new ArrayList();
        arrayList.add(MaterialTextService.text("Select a slot, then choose what belongs there. The best matching item fills each role.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)).key("intro"));
        ArrayList<ComponentKeyService<MaterialJoinedService>> arrayList2 = new ArrayList<ComponentKeyService<MaterialJoinedService>>();
        arrayList2.add(MaterialTextService.button("hotbar.preset.balanced", "Balanced", () -> this.m5nrryg3kxff(InventoryRoleData.BALANCED), this.f1e90p6nm57i.equals(InventoryRoleData.BALANCED)));
        arrayList2.add(MaterialTextService.button("hotbar.preset.combat", "Combat", () -> this.m5nrryg3kxff(InventoryRoleData.COMBAT), this.f1e90p6nm57i.equals(InventoryRoleData.COMBAT)));
        arrayList2.add(MaterialTextService.button("hotbar.preset.building", "Building", () -> this.m5nrryg3kxff(InventoryRoleData.BUILDING), this.f1e90p6nm57i.equals(InventoryRoleData.BUILDING)));
        arrayList.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000))).flexShrink(0.0f), (ComponentKeyService[])arrayList2.stream().map(componentKeyService -> componentKeyService.props(scenePctService -> ((ScenePctService)((ScenePctService)scenePctService.flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x40800000)))).toArray(ComponentKeyService[]::new)).key("presets"));
        int n4 = n3 != 0 ? 3 : 9;
        ArrayList<ComponentKeyService<LayoutContainerNode>> arrayList3 = new ArrayList<ComponentKeyService<LayoutContainerNode>>();
        for (n2 = 0; n2 < 9; n2 += n4) {
            ArrayList<ComponentKeyService<?>> roleArray = new ArrayList<>();
            for (n = n2; n < Math.min(9, n2 + n4); ++n) {
                roleArray.add(this.mv36lcf1ym5(n));
            }
            arrayList3.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000))).flexShrink(0.0f), (ComponentKeyService[])roleArray.toArray(ComponentKeyService[]::new)).key("row-" + n2));
        }
        arrayList.add(ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("hotbar.slots")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).direction(ScenePctService.Direction.COLUMN)).padding(Float.intBitsToFloat(1092616192))).gap(Float.intBitsToFloat(0x40C00000))).backgroundColor(MaterialIsLightService.SURFACE_LOWEST).cornerRadius(Float.intBitsToFloat(1099956224)).flexShrink(0.0f), (ComponentKeyService[])arrayList3.toArray(ComponentKeyService[]::new)).key("deck"));
        arrayList.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).align(ScenePctService.Align.CENTER)).gap(Float.intBitsToFloat(0x41000000))).flexShrink(0.0f), MaterialTextService.text((String)(this.fbc2hcsc4hiq ? "Choose the other slot" : "Slot " + (this.f4nfd9kx3zq4 + 1) + " \u00b7 " + this.f1e90p6nm57i.slots().get(this.f4nfd9kx3zq4).title()), Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)), MaterialTextService.button("hotbar.swap", this.fbc2hcsc4hiq ? "Cancel swap" : "Swap slots", () -> {
            this.fbc2hcsc4hiq = !this.fbc2hcsc4hiq;
            this.m5pkfdqfrjqs();
        }, this.fbc2hcsc4hiq).props(materialJoinedService -> materialJoinedService.tooltip("Swap two slot roles \u00b7 Or use Shift + Left / Right to move the selected role"))));
        n2 = n3 != 0 ? 2 : 4;
        InventoryRoleData.Role[] roles = InventoryRoleData.Role.values();
        for (n = 0; n < roles.length; n += n2) {
            ArrayList<ComponentKeyService<?>> arrayList4 = new ArrayList<>();
            for (int i = n; i < Math.min(roles.length, n + n2); ++i) {
                arrayList4.add(this.m1j8i2ol5t38(roles[i]));
            }
            arrayList.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000))).flexShrink(0.0f), (ComponentKeyService[])arrayList4.toArray(ComponentKeyService[]::new)).key("roles-" + n));
        }
        arrayList.add(MaterialTextService.text("Locked slots stay untouched. Missing items leave their slots alone. Open your inventory to apply the arrangement in game.", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)).key("help"));
        return ComponentBoxService.node("hotbar-layout-editor", MaterialTerrainTooltipsService::new, materialTerrainTooltipsService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialTerrainTooltipsService.terrainTooltips(true).id("hotbar.editor")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).direction(ScenePctService.Direction.COLUMN)).gap(Float.intBitsToFloat(1092616192))).flexShrink(0.0f), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new));
    }

    private ComponentKeyService<?> mv36lcf1ym5(int n) {
        InventoryRoleData.Role role = this.f1e90p6nm57i.slots().get(n);
        boolean bl = this.f4nfd9kx3zq4 == n;
        int n2 = bl ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE;
        return ComponentBoxService.node("hotbar-slot", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SURFACE_HIGH, n2).shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1119879168));
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id("hotbar.slot." + n)).flex(1.0f)).minWidth(0.0f)).height(LayoutOperationHandler.px(Float.intBitsToFloat(1119879168)))).padding(Float.intBitsToFloat(0x40C00000), 2.0f)).gap(Float.intBitsToFloat(0x40400000))).direction(ScenePctService.Direction.COLUMN)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).tooltip("Slot " + (n + 1) + " \u00b7 " + role.title() + "\n" + role.help())).onClick(() -> this.select(n));
        }, MaterialTextService.text(Integer.toString(n + 1), Float.intBitsToFloat(1092616192), n2), ComponentBoxService.node("hotbar-item", InventoryRoleService::new, inventoryRoleService -> ((InventoryRoleService)inventoryRoleService.role(role).size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1106247680))).pointerEvents(false), new ComponentKeyService[0]), MaterialTextService.text(role.title(), Float.intBitsToFloat(1092616192), n2).props(sceneTextService -> ((SceneTextService)sceneTextService.maxWidth(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).minWidth(0.0f))).key("slot-" + n).onMount(materialJoinedService -> {
            materialJoinedService.opacity(0.0f);
            materialJoinedService.translateY(Float.intBitsToFloat(0x41000000));
            MotionAnimateService.animate(materialJoinedService, MotionColorsContainer.Floats.OPACITY, 1.0f, (SceneEaseHandler)MaterialIsLightService.EFFECTS, (float)n * Float.intBitsToFloat(1016296636));
            MotionAnimateService.animate(materialJoinedService, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0f, (SceneEaseHandler)MaterialIsLightService.SPATIAL, (float)n * Float.intBitsToFloat(1016296636));
        });
    }

    private ComponentKeyService<?> m1j8i2ol5t38(InventoryRoleData.Role role) {
        boolean bl = this.f1e90p6nm57i.slots().get(this.f4nfd9kx3zq4) == role;
        int n = bl ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT;
        return ComponentBoxService.node("hotbar-role", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(bl ? MaterialIsLightService.SECONDARY_CONTAINER : MaterialIsLightService.SURFACE_CONTAINER, n).shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x42400000)).outlined(bl);
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id("hotbar.role." + role.name())).height(LayoutOperationHandler.px(Float.intBitsToFloat(1112539136)))).flex(1.0f)).minWidth(0.0f)).padding(0.0f, Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x41000000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).tooltip(role.title() + "\n" + role.help())).onClick(() -> this.assign(role));
        }, ComponentBoxService.node("hotbar-item", InventoryRoleService::new, inventoryRoleService -> ((InventoryRoleService)((InventoryRoleService)inventoryRoleService.role(role).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))).flexShrink(0.0f)).pointerEvents(false), new ComponentKeyService[0]), MaterialTextService.text(role.title(), Float.intBitsToFloat(1095761920), n).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f))).key(role.name());
    }

    public static ComponentKeyService<?> preview(String string, ModuleLayoutService moduleLayoutService, Runnable runnable) {
        ArrayList<ComponentKeyService<LayoutContainerNode>> arrayList = new ArrayList<ComponentKeyService<LayoutContainerNode>>();
        for (int i = 0; i < 9; ++i) {
            InventoryRoleData.Role role = moduleLayoutService.layout().slots().get(i);
            int n = i;
            arrayList.add(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.flex(1.0f)).minWidth(0.0f)).align(ScenePctService.Align.CENTER)).gap(2.0f)).pointerEvents(false), ComponentBoxService.node("hotbar-item", InventoryRoleService::new, inventoryRoleService -> ((InventoryRoleService)inventoryRoleService.role(role).size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224))).pointerEvents(false), new ComponentKeyService[0]), MaterialTextService.text(Integer.toString(n + 1), Float.intBitsToFloat(0x41100000), MaterialIsLightService.ON_SECONDARY_CONTAINER)).key("slot-" + i));
        }
        return ComponentBoxService.node("hotbar-layout-preview", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1112539136)).available(moduleLayoutService.isActive());
            ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialJoinedService.id(string + ".edit")).height(LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))).padding(Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(0x41000000))).gap(Float.intBitsToFloat(0x40800000))).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).tooltip("Edit all nine hotbar slots \u00b7 Choose roles, lock slots, and save presets")).onClick(runnable);
        }, (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).key("control");
    }
}

