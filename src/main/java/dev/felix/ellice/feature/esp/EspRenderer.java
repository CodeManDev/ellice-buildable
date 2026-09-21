


package dev.felix.ellice.feature.esp;

import dev.felix.ellice.feature.esp.EspData;
import dev.felix.ellice.feature.esp.EspLabelData;
import dev.felix.ellice.feature.esp.EspValueService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.text.TextData;
import java.util.ArrayList;
import java.util.Objects;

public final class EspRenderer
implements ComponentOperationHandler {
    private EspLabelData ffr55iqj16da;
    private EspData fkec6a94xp6;
    private int f5e3qybbu76z;
    private Captions fi8c3p4qlblp;

    public EspRenderer(EspLabelData espLabelData, EspData espData, int n, Captions captions) {
        this.update(espLabelData, espData, n, captions);
    }

    public boolean update(EspLabelData espLabelData, EspData espData, int n, Captions captions) {
        boolean bl = !Objects.equals(this.ffr55iqj16da, espLabelData) || !Objects.equals(this.fkec6a94xp6, espData) || this.f5e3qybbu76z != n || !Objects.equals(this.fi8c3p4qlblp, captions);
        this.ffr55iqj16da = Objects.requireNonNull(espLabelData);
        this.fkec6a94xp6 = Objects.requireNonNull(espData);
        this.f5e3qybbu76z = n;
        this.fi8c3p4qlblp = Objects.requireNonNull(captions);
        return bl;
    }

    @Override
    public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
        EspLabelData espLabelData = this.ffr55iqj16da;
        EspData espData = this.fkec6a94xp6;
        Captions captions = this.fi8c3p4qlblp;
        int n = this.f5e3qybbu76z;
        float f = espData.labelScale();
        ArrayList arrayList = new ArrayList();
        arrayList.add(ComponentBoxService.node("esp-frame", EspValueService::new, espValueService -> espValueService.value(espLabelData, espData, n).pointerEvents(false), new ComponentKeyService[0]).style(ComponentStyleService.style().size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).absolute().build()).key("frame"));
        if (captions.name()) {
            arrayList.add(ComponentBoxService.panel(ComponentStyleService.style().width(LayoutOperationHandler.px(captions.nameWidth())).height(LayoutOperationHandler.px(EspRenderer.nameHeight(f))).absolute().padding(f, Float.intBitsToFloat(0x40800000) * f).align(ScenePctService.Align.CENTER).build(), sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.direction(ScenePctService.Direction.ROW)).cornerRadius(2.0f * f).position(captions.nameX(), -EspRenderer.nameHeight(f) - Float.intBitsToFloat(0x40800000) * f)).backgroundColor(-921363676).pointerEvents(false), ComponentBoxService.text(espLabelData.name(), sceneTextService -> sceneTextService.fontSize(Float.intBitsToFloat(1092091904) * f).color(-985604).textAlign(SceneTextService.TextAlign.CENTER).maxLines(1).overflow(SceneTextService.Overflow.ELLIPSIS).pointerEvents(false)).style(ComponentStyleService.style().grow(1.0f).minWidth(LayoutOperationHandler.px(0.0f)).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).build())).key("name"));
        }
        if (captions.distance()) {
            float f2 = EspRenderer.distanceOffset(espData, espLabelData);
            arrayList.add(ComponentBoxService.column(ComponentStyleService.style().width(LayoutOperationHandler.px(captions.distanceWidth())).height(LayoutOperationHandler.px(Float.intBitsToFloat(1093664768) * f)).absolute().inset(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.auto(), LayoutOperationHandler.auto(), LayoutOperationHandler.px(0.0f)).align(ScenePctService.Align.CENTER).build(), ComponentBoxService.text(espLabelData.metres() + " m", sceneTextService -> sceneTextService.fontSize(Float.intBitsToFloat(1091043328) * f).color(-3221017).style(TextData.outline(Float.intBitsToFloat(1059481190) * f, -435153635).withShadow(0.0f, Float.intBitsToFloat(0x3F000000) * f, -1342177280)).maxLines(1).textAlign(SceneTextService.TextAlign.CENTER).pointerEvents(false)).style(ComponentStyleService.style().width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).build())).props(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.position(captions.distanceX(), f2)).pointerEvents(false)).key("distance"));
        }
        return ComponentBoxService.stack(ComponentStyleService.style().size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).build(), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).props(layoutContainerNode -> {
            layoutContainerNode.pointerEvents(false);
            if (!espData.animations()) {
                MotionAnimateService.cancel(layoutContainerNode, MotionColorsContainer.Floats.OPACITY);
                layoutContainerNode.opacity(1.0f);
            }
        }).key("esp-target").onMount(layoutContainerNode -> {
            if (espData.animations()) {
                layoutContainerNode.opacity(0.0f);
                MotionAnimateService.animate(layoutContainerNode, MotionColorsContainer.Floats.OPACITY, 1.0f, (SceneEaseHandler)SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)));
            }
        });
    }

    public static float nameHeight(float f) {
        return Float.intBitsToFloat(1095761920) * f;
    }

    public static float distanceOffset(EspData espData, EspLabelData espLabelData) {
        return espData.armorBar() && espLabelData.armor() > 0.0f ? Float.intBitsToFloat(0x41000000) : Float.intBitsToFloat(0x40400000);
    }

    public record Captions(boolean name, float nameX, float nameWidth, boolean distance, float distanceX, float distanceWidth) {
        public static final Captions NONE = new Captions(false, 0.0f, 0.0f, false, 0.0f, 0.0f);
    }
}

