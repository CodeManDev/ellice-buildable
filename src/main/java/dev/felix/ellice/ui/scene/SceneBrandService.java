package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.text.TextMode;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public final class SceneBrandService extends ScenePctService<SceneBrandService> {
  private static final String text2 = "/assets/ellice/brands/";
  private static final Map<String, String> text3 =
      Map.ofEntries(
          Map.entry("CubeCraft", "cubecraft.png"),
          Map.entry("Sentinel", "cubecraft.png"),
          Map.entry("Watchdog", "hypixel.png"),
          Map.entry("Grim", "grim.png"),
          Map.entry("NCP", "ncp.png"),
          Map.entry("Intave", "intave.png"),
          Map.entry("Spartan", "spartan.png"),
          Map.entry("Verus", "verus.png"),
          Map.entry("Vulcan", "vulcan.jpg"),
          Map.entry("Matrix", "matrix.svg"),
          Map.entry("Hylex", "hylex.png"),
          Map.entry("AAC", "aac.png"),
          Map.entry("BlocksMC", "blocksmc.png"),
          Map.entry("General", "liquidbounce.png"),
          Map.entry("Spectre", "spectre.svg"),
          Map.entry("AGC", "minemen.png"),
          Map.entry("Minemen", "minemen.png"));
  private static final Map<String, BufferedImage> text4 = new HashMap<>();
  private String text5 = "General";

  public SceneBrandService brand(String text) {
    this.text5 = text;
    return this;
  }

  public String brand() {
    return this.text5;
  }

  public boolean hasArtwork() {
    return text3.containsKey(this.text5);
  }

  public static BufferedImage image(String text) {
    if (text4.containsKey(text)) {
      return text4.get(text);
    }

    try (InputStream inputStream =
        SceneBrandService.class.getResourceAsStream("/assets/ellice/brands/" + text)) {
      BufferedImage bufferedImage = inputStream == null ? null : ImageIO.read(inputStream);
      text4.put(text, bufferedImage);
      return bufferedImage;
    } catch (IOException iOException) {
      text4.put(text, null);
      return null;
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    String currentText = text3.get(this.text5);
    RhiBlendStateService.TextureHandle textureHandle = null;
    float height = 1.0F;
    if (currentText != null && currentText.endsWith(".svg")) {
      if (compositorPushPresentationScale.svgRenderer() != null) {
        textureHandle =
            compositorPushPresentationScale
                .svgRenderer()
                .getClasspathTexture("/assets/ellice/brands/" + currentText, 64, 64);
      }
    } else if (currentText != null) {
      BufferedImage bufferedImage = image(currentText);
      if (bufferedImage != null) {
        height = (float) bufferedImage.getWidth() / bufferedImage.getHeight();
        String nextText = "ellice.catalog.brand." + currentText;
        textureHandle = compositorPushPresentationScale.getNamedTexture(nextText);
        if (textureHandle == null || !textureHandle.valid()) {
          compositorPushPresentationScale.registerImageTexture(nextText, bufferedImage);
          textureHandle = compositorPushPresentationScale.getNamedTexture(nextText);
        }
      }
    }

    if (textureHandle != null && textureHandle.valid()) {
      float value = Math.min(this.cw, this.ch * height);
      float currentValue = value / height;
      if (this.text5.equals("Grim")) {
        compositorPushPresentationScale.roundedRect(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            Math.min(this.cw, this.ch) * 0.2F,
            mulAlpha(-856833, this.effectiveOpacity));
      }

      compositorPushPresentationScale.drawTexture(
          textureHandle,
          this.cx + (this.cw - value) / 2.0F,
          this.cy + (this.ch - currentValue) / 2.0F,
          value,
          currentValue,
          this.effectiveOpacity,
          currentText.endsWith(".jpg") ? Math.min(value, currentValue) * 0.2F : 0.0F,
          currentText.endsWith(".svg") ? MaterialIsLightService.ON_SURFACE : 0);
    } else {
      int[] ints = new int[] {-9875295, -13145205, -6721221, -11176886};
      int currentLength = ints[Math.floorMod(this.text5.hashCode(), ints.length)];
      compositorPushPresentationScale.roundedRect(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          Math.min(this.cw, this.ch) * 0.28F,
          mulAlpha(currentLength, this.effectiveOpacity));

      String nextLength =
          switch (this.text5) {
            case "General" -> "S";
            case "BlocksMC" -> "B";
            case "Spectre" -> "SP";
            default -> this.text5.length() > 3 ? this.text5.substring(0, 2) : this.text5;
          };
      float previousLength = Math.min(this.cw, this.ch) * (nextLength.length() > 2 ? 0.28F : 0.38F);
      float nextValue =
          compositorPushPresentationScale.textWidth(
              nextLength, previousLength, TextMode.REGULAR, "material-roboto");
      compositorPushPresentationScale.text(
          this.cx + (this.cw - nextValue) / 2.0F,
          this.cy + (this.ch - previousLength) / 2.0F,
          nextLength,
          previousLength,
          mulAlpha(-1, this.effectiveOpacity),
          MaterialIsLightService.LABEL);
    }
  }
}
