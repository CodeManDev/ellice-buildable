package dev.felix.ellice.hud;

import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;

public final class HudRenderer implements ComponentOperationHandler {
   private ComponentThemeService componentThemeService;
   private String text2 = "AntiBot";
   private String text3 = "Observing nearby players";
   private boolean enabled;

   public void update(String text, String currentText, boolean currentEnabled) {
      if (!this.text2.equals(text) || !this.text3.equals(currentText) || this.enabled != currentEnabled) {
         this.text2 = text;
         this.text3 = currentText;
         this.enabled = currentEnabled;
         if (this.componentThemeService != null) {
            this.componentThemeService.invalidate();
         }
      }
   }

   @Override
   public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
      this.componentThemeService = componentTheme;
      return ComponentBoxService.row(
            ComponentStyleService.style().width(LayoutOperationHandler.percent(100.0F)).justify(ScenePctService.Justify.CENTER).build(),
            ComponentBoxService.panel(
                  ComponentStyleService.style()
                     .maxWidth(LayoutOperationHandler.percent(85.0F))
                     .padding(5.0F, 9.0F)
                     .gap(2.0F)
                     .build(),
                  item -> item.direction(ScenePctService.Direction.COLUMN)
                     .cornerRadius(6.0F)
                     .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                     .pointerEvents(false),
                  ComponentBoxService.text(
                        this.text2,
                        item -> item.fontSize(9.0F)
                           .color(this.enabled ? -14739 : MaterialIsLightService.ON_SURFACE)
                           .maxLines(1)
                           .overflow(SceneTextService.Overflow.ELLIPSIS)
                           .minWidth(0.0F)
                           .pointerEvents(false)
                     )
                     .key("title"),
                  ComponentBoxService.text(
                        this.text3,
                        item -> item.fontSize(8.0F)
                           .color(MaterialIsLightService.ON_SURFACE_VARIANT)
                           .maxLines(1)
                           .overflow(SceneTextService.Overflow.ELLIPSIS)
                           .minWidth(0.0F)
                           .pointerEvents(false)
                     )
                     .key("reason")
               )
               .key("antibot-card")
         )
         .key("antibot-status");
   }
}
