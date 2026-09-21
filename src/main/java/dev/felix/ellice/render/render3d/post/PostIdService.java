package dev.felix.ellice.render.render3d.post;

import dev.felix.ellice.render.render3d.shader.ShaderSetColorService;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import java.util.Objects;

public final class PostIdService {
   private final String text;
   private final ShaderDefinition shaderData2;
   private volatile boolean enabled2;
   private final ShaderSetColorService values2 = new ShaderSetColorService();
   private volatile boolean enabled3;
   private PostIdService.Stage stage2 = PostIdService.Stage.BEFORE_HUD;

   public PostIdService(String currentText, ShaderDefinition shaderDefinition, boolean enabled) {
      if (currentText != null && !currentText.isBlank()) {
         this.text = currentText;
         this.shaderData2 = Objects.requireNonNull(shaderDefinition, "shader");
         this.enabled2 = enabled;
      } else {
         throw new IllegalArgumentException("Effect id is required");
      }
   }

   public String id() {
      return this.text;
   }

   public ShaderDefinition shader() {
      return this.shaderData2;
   }

   public boolean requiresDepth() {
      return this.enabled2;
   }

   public ShaderSetColorService bindings() {
      return this.values2;
   }

   public boolean enabled() {
      return this.enabled3;
   }

   public PostIdService.Stage stage() {
      return this.stage2;
   }

   public PostIdService stage(PostIdService.Stage currentStage) {
      this.stage2 = Objects.requireNonNull(currentStage);
      return this;
   }

   public PostIdService enabled(boolean currentEnabled) {
      this.enabled3 = currentEnabled;
      return this;
   }

   public PostIdService requiresDepth(boolean enabled) {
      this.enabled2 = enabled;
      return this;
   }

   public PostIdService uniform(String text, int value) {
      this.values2.set(text, value);
      return this;
   }

   public PostIdService uniform(String text, float value) {
      this.values2.set(text, value);
      return this;
   }

   public PostIdService uniform(String text, float value, float currentValue) {
      this.values2.set(text, value, currentValue);
      return this;
   }

   public PostIdService uniform(String text, float value, float currentValue, float nextValue) {
      this.values2.set(text, value, currentValue, nextValue);
      return this;
   }

   public PostIdService uniform(String text, float value, float currentValue, float nextValue, float previousValue) {
      this.values2.set(text, value, currentValue, nextValue, previousValue);
      return this;
   }

   public PostIdService color(String text, int currentColor) {
      this.values2.setColor(text, currentColor);
      return this;
   }

   public enum Stage {
      BEFORE_HAND,
      BEFORE_HUD;


      private static PostIdService.Stage[] $values() {
         return new PostIdService.Stage[]{BEFORE_HAND, BEFORE_HUD};
      }
   }
}
