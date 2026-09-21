package dev.felix.ellice.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.components.CommandSuggestions$SuggestionsList")
public abstract class ClientSuggestionListMixin {
  @Shadow @Final private Rect2i rect;
  @Shadow @Final private String originalContents;
  @Shadow @Final private List<Suggestion> suggestionList;
  @Shadow private int offset;
  @Shadow private int current;
  @Shadow private Vec2 lastMouse;

  @Shadow
  public abstract void select(int value);

  @Inject(method = "<init>", at = @At("RETURN"))
  private void ellice$cardWidth(CallbackInfo ci) {
    if (this.originalContents.startsWith(".")) {
      Screen screen = Minecraft.getInstance().screen;
      if (screen != null) {
        int width =
            Math.min(Math.max(196, this.rect.getWidth() + 94), Math.max(32, screen.width - 8));
        this.rect.setWidth(width);
        this.rect.setX(Math.clamp(this.rect.getX(), 4, Math.max(4, screen.width - width - 4)));
      }
    }
  }

  @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
  private void ellice$card(GuiGraphicsExtractor g, int mouseX, int mouseY, CallbackInfo ci) {
    if (this.originalContents.startsWith(".") && CoreIsInitializedHandler.isReady()) {
      ci.cancel();
      int rows = Math.min(this.rect.getHeight() / 12, this.suggestionList.size() - this.offset);
      if (rows > 0) {
        if (this.lastMouse.x != mouseX || this.lastMouse.y != mouseY) {
          this.lastMouse = new Vec2(mouseX, mouseY);
          if (this.rect.contains(mouseX, mouseY)) {
            this.select(
                Math.min(
                    this.suggestionList.size() - 1,
                    this.offset + (mouseY - this.rect.getY()) / 12));
          }
        }

        Font font = Minecraft.getInstance().font;
        int x = this.rect.getX();
        int y = this.rect.getY();
        int w = this.rect.getWidth();
        int h = rows * 12;
        int header = y >= 20 ? 17 : 0;
        rounded(g, x - 2, y - header + 2, w + 4, h + header + 3, 889192448);
        rounded(
            g,
            x - 2,
            y - header - 2,
            w + 4,
            h + header + 4,
            MaterialIsLightService.OUTLINE_VARIANT);
        rounded(
            g,
            x - 1,
            y - header - 1,
            w + 2,
            h + header + 2,
            MaterialIsLightService.SURFACE_CONTAINER);
        if (header > 0) {
          g.text(font, "ellice", x + 5, y - 13, MaterialIsLightService.PRIMARY, false);
          String hint = "TAB  complete";
          g.text(
              font,
              hint,
              x + w - font.width(hint) - 5,
              y - 13,
              MaterialIsLightService.ON_SURFACE_VARIANT,
              false);
          g.fill(x + 4, y - 2, x + w - 4, y - 1, MaterialIsLightService.OUTLINE_VARIANT);
        }

        g.enableScissor(x, y, x + w, y + h);

        for (int row = 0; row < rows; row++) {
          int index = this.offset + row;
          int top = y + row * 12;
          String text = this.suggestionList.get(index).getText();
          boolean selected = index == this.current;
          int ink =
              selected
                  ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                  : MaterialIsLightService.ON_SURFACE;
          if (selected) {
            rounded(g, x + 2, top, w - 4, 12, MaterialIsLightService.SECONDARY_CONTAINER);
          }

          String name = text.replace("\"", "");
          Module module = CoreIsInitializedHandler.get().modules().get(name).orElse(null);
          String detail =
              text.equals("bind")
                  ? "Assign key"
                  : (text.equals("toggle")
                      ? "On / off"
                      : (module != null
                          ? (module.isEnabled() ? "Enabled" : "Disabled")
                          : (text.equals("NONE") ? "Unbind" : "Key")));
          int iconInk =
              selected
                  ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                  : MaterialIsLightService.PRIMARY;
          icon(g, x + 5, top + 2, text.equals("toggle") ? 1 : (module != null ? 2 : 0), iconInk);
          int detailWidth = font.width(detail);
          int available = Math.max(0, w - 29 - detailWidth - 10);
          String label = font.plainSubstrByWidth(name, available);
          g.text(font, label, x + 20, top + 2, ink, false);
          g.text(
              font,
              detail,
              x + w - detailWidth - 7,
              top + 2,
              selected
                  ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                  : MaterialIsLightService.ON_SURFACE_VARIANT,
              false);
        }

        if (this.suggestionList.size() > rows) {
          int thumb = Math.max(4, h * rows / this.suggestionList.size());
          int top = y + (h - thumb) * this.offset / Math.max(1, this.suggestionList.size() - rows);
          g.fill(x + w - 2, top, x + w - 1, top + thumb, MaterialIsLightService.PRIMARY);
        }

        g.disableScissor();
      }
    }
  }

  private static void rounded(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
    g.fill(x + 2, y, x + w - 2, y + h, color);
    g.fill(x + 1, y + 1, x + 2, y + h - 1, color);
    g.fill(x + w - 2, y + 1, x + w - 1, y + h - 1, color);
    g.fill(x, y + 2, x + 1, y + h - 2, color);
    g.fill(x + w - 1, y + 2, x + w, y + h - 2, color);
  }

  private static void icon(GuiGraphicsExtractor g, int x, int y, int kind, int color) {
    if (kind == 1) {
      g.outline(x, y + 1, 11, 6, color);
      g.fill(x + 6, y + 2, x + 9, y + 6, color);
    } else if (kind == 2) {
      for (int dx : new int[] {0, 6}) {
        for (int dy : new int[] {0, 5}) {
          g.fill(x + dx, y + dy, x + dx + 4, y + dy + 3, color);
        }
      }
    } else {
      g.outline(x, y, 11, 8, color);

      for (int dx = 2; dx <= 8; dx += 3) {
        g.fill(x + dx, y + 2, x + dx + 1, y + 3, color);
      }

      g.fill(x + 3, y + 5, x + 8, y + 6, color);
    }
  }
}
