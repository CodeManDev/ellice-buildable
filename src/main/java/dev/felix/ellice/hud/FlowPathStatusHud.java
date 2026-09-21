package dev.felix.ellice.hud;

import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextData;

public final class FlowPathStatusHud implements ComponentOperationHandler {
  private ComponentThemeService componentThemeService;
  private String text2 = "FlowPath";

  public void update(String text) {
    if (!this.text2.equals(text)) {
      this.text2 = text;
      if (this.componentThemeService != null) {
        this.componentThemeService.invalidate();
      }
    }
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
    this.componentThemeService = componentTheme;
    return ComponentBoxService.row(
            ComponentStyleService.style()
                .width(LayoutOperationHandler.percent(100.0F))
                .align(ScenePctService.Align.CENTER)
                .justify(ScenePctService.Justify.CENTER)
                .build(),
            ComponentBoxService.panel(
                    ComponentStyleService.style()
                        .height(LayoutOperationHandler.px(16.0F))
                        .maxWidth(LayoutOperationHandler.percent(90.0F))
                        .padding(0.0F, 6.0F)
                        .align(ScenePctService.Align.CENTER)
                        .build(),
                    item ->
                        item.direction(ScenePctService.Direction.ROW)
                            .cornerRadius(5.0F)
                            .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                            .pointerEvents(false),
                    ComponentBoxService.text(
                            this.text2,
                            item ->
                                item.fontSize(8.0F)
                                    .color(MaterialIsLightService.ON_SURFACE)
                                    .style(TextData.shadow(0.6F, 0.6F, -2013265920))
                                    .maxLines(1)
                                    .overflow(SceneTextService.Overflow.ELLIPSIS)
                                    .minWidth(0.0F)
                                    .pointerEvents(false))
                        .key("label"))
                .key("flowpath-status"))
        .key("flowpath-status-row");
  }
}
