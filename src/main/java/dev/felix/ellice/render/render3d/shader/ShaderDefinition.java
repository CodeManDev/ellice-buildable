package dev.felix.ellice.render.render3d.shader;

import java.util.Objects;

public record ShaderDefinition(String id, String vertexAsset, String fragmentAsset) {
  public ShaderDefinition(String id, String vertexAsset, String fragmentAsset) {
    id = createText(id, "id");
    vertexAsset = ShaderOverrideRootService.normalizeAsset(vertexAsset);
    fragmentAsset = ShaderOverrideRootService.normalizeAsset(fragmentAsset);
    if (!vertexAsset.endsWith(".vert")) {
      throw new IllegalArgumentException("Vertex shader must end in .vert: " + vertexAsset);
    }

    if (!fragmentAsset.endsWith(".frag")) {
      throw new IllegalArgumentException("Fragment shader must end in .frag: " + fragmentAsset);
    }

    this.id = id;
    this.vertexAsset = vertexAsset;
    this.fragmentAsset = fragmentAsset;
  }

  private static String createText(String text, String currentText) {
    Objects.requireNonNull(text, currentText);
    String nextText = text.trim();
    if (nextText.isEmpty()) {
      throw new IllegalArgumentException(currentText + " must not be blank");
    } else {
      return nextText;
    }
  }
}
