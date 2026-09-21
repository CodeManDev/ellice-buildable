package dev.felix.ellice.hud.layout;

public final class LayoutCodec {
   public static final LayoutCodec TOP_LEFT = new LayoutCodec(LayoutCodec.H.LEFT, LayoutCodec.V.TOP);
   public static final LayoutCodec TOP_CENTER = new LayoutCodec(LayoutCodec.H.CENTER, LayoutCodec.V.TOP);
   public static final LayoutCodec TOP_RIGHT = new LayoutCodec(LayoutCodec.H.RIGHT, LayoutCodec.V.TOP);
   public static final LayoutCodec MIDDLE_LEFT = new LayoutCodec(LayoutCodec.H.LEFT, LayoutCodec.V.CENTER);
   public static final LayoutCodec CENTER = new LayoutCodec(LayoutCodec.H.CENTER, LayoutCodec.V.CENTER);
   public static final LayoutCodec MIDDLE_RIGHT = new LayoutCodec(LayoutCodec.H.RIGHT, LayoutCodec.V.CENTER);
   public static final LayoutCodec BOTTOM_LEFT = new LayoutCodec(LayoutCodec.H.LEFT, LayoutCodec.V.BOTTOM);
   public static final LayoutCodec BOTTOM_CENTER = new LayoutCodec(LayoutCodec.H.CENTER, LayoutCodec.V.BOTTOM);
   public static final LayoutCodec BOTTOM_RIGHT = new LayoutCodec(LayoutCodec.H.RIGHT, LayoutCodec.V.BOTTOM);
   public final LayoutCodec.H h;
   public final LayoutCodec.V v;

   public LayoutCodec(LayoutCodec.H currentH, LayoutCodec.V currentV) {
      this.h = currentH != null ? currentH : LayoutCodec.H.LEFT;
      this.v = currentV != null ? currentV : LayoutCodec.V.TOP;
   }

   public static LayoutCodec parse(String text, String currentText) {
      return new LayoutCodec(createH(text), createV(currentText));
   }

   private static LayoutCodec.H createH(String text) {
      if (text == null) {
         return LayoutCodec.H.LEFT;
      }

      return switch (text.toLowerCase()) {
         case "right" -> LayoutCodec.H.RIGHT;
         case "center", "centre", "middle" -> LayoutCodec.H.CENTER;
         default -> LayoutCodec.H.LEFT;
      };
   }

   private static LayoutCodec.V createV(String text) {
      if (text == null) {
         return LayoutCodec.V.TOP;
      }

      return switch (text.toLowerCase()) {
         case "bottom" -> LayoutCodec.V.BOTTOM;
         case "center", "centre", "middle" -> LayoutCodec.V.CENTER;
         default -> LayoutCodec.V.TOP;
      };
   }

   @Override
   public boolean equals(Object value) {
      return value instanceof LayoutCodec layoutCodec && layoutCodec.h == this.h && layoutCodec.v == this.v;
   }

   @Override
   public int hashCode() {
      return this.h.ordinal() * 3 + this.v.ordinal();
   }

   public enum H {
      LEFT(0.0F),
      CENTER(0.5F),
      RIGHT(1.0F);

      public final float factor;

      H(float value) {
         this.factor = value;
      }


      private static LayoutCodec.H[] $values() {
         return new LayoutCodec.H[]{LEFT, CENTER, RIGHT};
      }
   }

   public enum V {
      TOP(0.0F),
      CENTER(0.5F),
      BOTTOM(1.0F);

      public final float factor;

      V(float value) {
         this.factor = value;
      }


      private static LayoutCodec.V[] $values() {
         return new LayoutCodec.V[]{TOP, CENTER, BOTTOM};
      }
   }
}
