package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Objects;

public final class ComponentStyleService {
   private final LayoutOperationHandler layoutOperationHandler;
   private final LayoutOperationHandler layoutOperationHandler2;
   private final LayoutOperationHandler layoutOperationHandler3;
   private final LayoutOperationHandler layoutOperationHandler4;
   private final LayoutOperationHandler layoutOperationHandler5;
   private final LayoutOperationHandler layoutOperationHandler6;
   private final float value;
   private final float value2;
   private final LayoutOperationHandler layoutOperationHandler7;
   private final ScenePctService.PositionType positionType2;
   private final LayoutOperationHandler layoutOperationHandler8;
   private final LayoutOperationHandler layoutOperationHandler9;
   private final LayoutOperationHandler layoutOperationHandler10;
   private final LayoutOperationHandler layoutOperationHandler11;
   private final ComponentStyleService.Edges edges;
   private final ComponentStyleService.Edges edges2;
   private final float value3;
   private final ScenePctService.Align align2;
   private final ScenePctService.Justify justify2;
   private final boolean enabled;
   private final boolean enabled2;

   private ComponentStyleService(ComponentStyleService.Builder builder) {
      this.layoutOperationHandler = builder.layoutOperationHandler12;
      this.layoutOperationHandler2 = builder.layoutOperationHandler13;
      this.layoutOperationHandler3 = builder.layoutOperationHandler14;
      this.layoutOperationHandler4 = builder.layoutOperationHandler15;
      this.layoutOperationHandler5 = builder.layoutOperationHandler16;
      this.layoutOperationHandler6 = builder.layoutOperationHandler17;
      this.value = builder.value4;
      this.value2 = builder.value5;
      this.layoutOperationHandler7 = builder.layoutOperationHandler18;
      this.positionType2 = builder.positionType3;
      this.layoutOperationHandler8 = builder.layoutOperationHandler19;
      this.layoutOperationHandler9 = builder.layoutOperationHandler20;
      this.layoutOperationHandler10 = builder.layoutOperationHandler21;
      this.layoutOperationHandler11 = builder.layoutOperationHandler22;
      this.edges = builder.edges3;
      this.edges2 = builder.edges4;
      this.value3 = builder.value6;
      this.align2 = builder.align3;
      this.justify2 = builder.justify3;
      this.enabled = builder.enabled3;
      this.enabled2 = builder.enabled4;
   }

   public static ComponentStyleService.Builder style() {
      return new ComponentStyleService.Builder();
   }

   public void apply(ScenePctService<?> scenePct) {
      Objects.requireNonNull(scenePct, "node")
         .size(this.layoutOperationHandler, this.layoutOperationHandler2)
         .minWidth(this.layoutOperationHandler3)
         .maxWidth(this.layoutOperationHandler4)
         .minHeight(this.layoutOperationHandler5)
         .maxHeight(this.layoutOperationHandler6)
         .flexGrow(this.value)
         .flexShrink(this.value2)
         .flexBasis(this.layoutOperationHandler7)
         .positionType(this.positionType2)
         .inset(this.layoutOperationHandler8, this.layoutOperationHandler9, this.layoutOperationHandler10, this.layoutOperationHandler11)
         .padding(this.edges.top, this.edges.right, this.edges.bottom, this.edges.left)
         .margin(this.edges2.top, this.edges2.right, this.edges2.bottom, this.edges2.left)
         .gap(this.value3)
         .align(this.align2)
         .justify(this.justify2)
         .scrollable(this.enabled2)
         .clip(this.enabled || this.enabled2);
   }

   private static float calculateValue(float value, String text) {
      if (Float.isFinite(value) && !(value < 0.0F)) {
         return value;
      } else {
         throw new IllegalArgumentException(text + " must be finite and >= 0: " + value);
      }
   }

   public static final class Builder {
      private LayoutOperationHandler layoutOperationHandler12 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler13 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler14 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler15 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler16 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler17 = LayoutOperationHandler.auto();
      private float value4;
      private float value5 = 1.0F;
      private LayoutOperationHandler layoutOperationHandler18 = LayoutOperationHandler.auto();
      private ScenePctService.PositionType positionType3 = ScenePctService.PositionType.FLOW;
      private LayoutOperationHandler layoutOperationHandler19 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler20 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler21 = LayoutOperationHandler.auto();
      private LayoutOperationHandler layoutOperationHandler22 = LayoutOperationHandler.auto();
      private ComponentStyleService.Edges edges3 = ComponentStyleService.Edges.all(0.0F);
      private ComponentStyleService.Edges edges4 = ComponentStyleService.Edges.all(0.0F);
      private float value6;
      private ScenePctService.Align align3 = ScenePctService.Align.STRETCH;
      private ScenePctService.Justify justify3 = ScenePctService.Justify.START;
      private boolean enabled3;
      private boolean enabled4;

      public ComponentStyleService.Builder size(LayoutOperationHandler layoutOperation, LayoutOperationHandler currentLayoutOperation) {
         this.layoutOperationHandler12 = createLayoutOperationHandler(layoutOperation);
         this.layoutOperationHandler13 = createLayoutOperationHandler(currentLayoutOperation);
         return this;
      }

      public ComponentStyleService.Builder width(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler12 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder height(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler13 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder minWidth(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler14 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder maxWidth(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler15 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder minHeight(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler16 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder maxHeight(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler17 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder grow(float value) {
         this.value4 = ComponentStyleService.calculateValue(value, "grow");
         return this;
      }

      public ComponentStyleService.Builder shrink(float value) {
         this.value5 = ComponentStyleService.calculateValue(value, "shrink");
         return this;
      }

      public ComponentStyleService.Builder basis(LayoutOperationHandler layoutOperation) {
         this.layoutOperationHandler18 = createLayoutOperationHandler(layoutOperation);
         return this;
      }

      public ComponentStyleService.Builder absolute() {
         this.positionType3 = ScenePctService.PositionType.ABSOLUTE;
         return this;
      }

      public ComponentStyleService.Builder flow() {
         this.positionType3 = ScenePctService.PositionType.FLOW;
         return this;
      }

      public ComponentStyleService.Builder inset(LayoutOperationHandler layoutOperation) {
         return this.inset(layoutOperation, layoutOperation, layoutOperation, layoutOperation);
      }

      public ComponentStyleService.Builder inset(LayoutOperationHandler layoutOperation, LayoutOperationHandler currentLayoutOperation, LayoutOperationHandler nextLayoutOperation, LayoutOperationHandler previousLayoutOperation) {
         this.layoutOperationHandler19 = createLayoutOperationHandler(layoutOperation);
         this.layoutOperationHandler20 = createLayoutOperationHandler(currentLayoutOperation);
         this.layoutOperationHandler21 = createLayoutOperationHandler(nextLayoutOperation);
         this.layoutOperationHandler22 = createLayoutOperationHandler(previousLayoutOperation);
         return this;
      }

      public ComponentStyleService.Builder padding(float value) {
         this.edges3 = ComponentStyleService.Edges.all(value);
         return this;
      }

      public ComponentStyleService.Builder padding(float value, float currentValue) {
         this.edges3 = ComponentStyleService.Edges.symmetric(value, currentValue);
         return this;
      }

      public ComponentStyleService.Builder padding(float value, float currentValue, float nextValue, float previousValue) {
         this.edges3 = new ComponentStyleService.Edges(value, currentValue, nextValue, previousValue);
         return this;
      }

      public ComponentStyleService.Builder margin(float value) {
         this.edges4 = ComponentStyleService.Edges.all(value);
         return this;
      }

      public ComponentStyleService.Builder margin(float value, float currentValue) {
         this.edges4 = ComponentStyleService.Edges.symmetric(value, currentValue);
         return this;
      }

      public ComponentStyleService.Builder margin(float value, float currentValue, float nextValue, float previousValue) {
         this.edges4 = new ComponentStyleService.Edges(value, currentValue, nextValue, previousValue);
         return this;
      }

      public ComponentStyleService.Builder gap(float value) {
         this.value6 = ComponentStyleService.calculateValue(value, "gap");
         return this;
      }

      public ComponentStyleService.Builder align(ScenePctService.Align currentAlign) {
         this.align3 = Objects.requireNonNull(currentAlign);
         return this;
      }

      public ComponentStyleService.Builder justify(ScenePctService.Justify currentJustify) {
         this.justify3 = Objects.requireNonNull(currentJustify);
         return this;
      }

      public ComponentStyleService.Builder clip(boolean enabled) {
         this.enabled3 = enabled;
         return this;
      }

      public ComponentStyleService.Builder scroll(boolean enabled) {
         this.enabled4 = enabled;
         return this;
      }

      public ComponentStyleService build() {
         return new ComponentStyleService(this);
      }

      private static LayoutOperationHandler createLayoutOperationHandler(LayoutOperationHandler layoutOperation) {
         return Objects.requireNonNull(layoutOperation, "length");
      }
   }

   public record Edges(float top, float right, float bottom, float left) {
      public Edges(float top, float right, float bottom, float left) {
         ComponentStyleService.calculateValue(top, "top");
         ComponentStyleService.calculateValue(right, "right");
         ComponentStyleService.calculateValue(bottom, "bottom");
         ComponentStyleService.calculateValue(left, "left");
         this.top = top;
         this.right = right;
         this.bottom = bottom;
         this.left = left;
      }

      public static ComponentStyleService.Edges all(float value) {
         return new ComponentStyleService.Edges(value, value, value, value);
      }

      public static ComponentStyleService.Edges symmetric(float value, float currentValue) {
         return new ComponentStyleService.Edges(value, currentValue, value, currentValue);
      }
   }
}
