package dev.felix.ellice.ui.scene;

public class RoundedSurfaceNode extends MaterialSurfaceNode {
  public RoundedSurfaceNode() {
    this.maskShape(MaterialSurfaceNode.MaskShape.ROUNDED_RECT);
  }
}
