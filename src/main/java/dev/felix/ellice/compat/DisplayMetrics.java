package dev.felix.ellice.compat;

public record DisplayMetrics(
   int framebufferWidth,
   int framebufferHeight,
   int windowWidth,
   int windowHeight,
   int vanillaWidth,
   int vanillaHeight,
   double vanillaScale,
   boolean nativeMenu
) {
   public DisplayMetrics(
      int framebufferWidth,
      int framebufferHeight,
      int windowWidth,
      int windowHeight,
      int vanillaWidth,
      int vanillaHeight,
      double vanillaScale,
      boolean nativeMenu
   ) {
      framebufferWidth = Math.max(1, framebufferWidth);
      framebufferHeight = Math.max(1, framebufferHeight);
      windowWidth = Math.max(1, windowWidth);
      windowHeight = Math.max(1, windowHeight);
      vanillaWidth = Math.max(1, vanillaWidth);
      vanillaHeight = Math.max(1, vanillaHeight);
      if (!Double.isFinite(vanillaScale) || vanillaScale <= 0.0) {
         vanillaScale = 1.0;
      }

      this.framebufferWidth = framebufferWidth;
      this.framebufferHeight = framebufferHeight;
      this.windowWidth = windowWidth;
      this.windowHeight = windowHeight;
      this.vanillaWidth = vanillaWidth;
      this.vanillaHeight = vanillaHeight;
      this.vanillaScale = vanillaScale;
      this.nativeMenu = nativeMenu;
   }

   public int width() {
      return this.nativeMenu ? this.framebufferWidth : this.vanillaWidth;
   }

   public int height() {
      return this.nativeMenu ? this.framebufferHeight : this.vanillaHeight;
   }

   public double renderScale() {
      return this.nativeMenu ? 1.0 : this.vanillaScale;
   }

   public float mouseX(double x) {
      return (float)(x * this.width() / this.windowWidth);
   }

   public float mouseY(double x) {
      return (float)(x * this.height() / this.windowHeight);
   }

   public float vanillaX(double doubleValue) {
      return (float)(doubleValue * this.width() / this.vanillaWidth);
   }

   public float vanillaY(double doubleValue) {
      return (float)(doubleValue * this.height() / this.vanillaHeight);
   }

   public float hudScale() {
      return (float)(this.vanillaScale / this.renderScale());
   }
}
