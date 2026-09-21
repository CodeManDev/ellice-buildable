package dev.felix.ellice.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felix.ellice.module.impl.ImplActiveService;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Matrix4f;

public final class RenderMatrixService {
   private static final float value = (float) (Math.PI / 180.0);
   private static final Matrix4f matrix4f = createMatrix4f(1.0F);
   private static final Matrix4f matrix4f2 = createMatrix4f(-1.0F);

   private RenderMatrixService() {
   }

   public static void apply(PoseStack poseStack, HumanoidArm humanoidArm, float value, float currentValue) {
      ImplActiveService implActive = ImplActiveService.active();
      poseStack.mulPose(matrix(humanoidArm == HumanoidArm.RIGHT, value, implActive != null && implActive.blockSwing() ? currentValue : 0.0F));
   }

   public static Matrix4f matrix(boolean enabled, float value, float currentValue) {
      float nextValue = enabled ? 1.0F : -1.0F;
      float previousValue = calculateValue(currentValue);
      float sourceValue = (float)Math.sin(previousValue * previousValue * 3.141592653589793);
      float targetValue = (float)Math.sin(Math.sqrt(previousValue) * 3.141592653589793);
      return new Matrix4f()
         .translation(
            nextValue * 0.56F,
            -0.52F - calculateValue(value) * 0.6F,
            -0.72F
         )
         .rotateY(nextValue * 45.0F * 0.017453292F)
         .rotateY(nextValue * -20.0F * sourceValue * 0.017453292F)
         .rotateZ(nextValue * -20.0F * targetValue * 0.017453292F)
         .rotateX(-80.0F * targetValue * 0.017453292F)
         .scale(0.4F)
         .translate(nextValue * -0.5F, 0.2F, 0.0F)
         .rotateY(nextValue * 30.0F * 0.017453292F)
         .rotateX(-1.3962634F)
         .rotateY(nextValue * 60.0F * 0.017453292F)
         .mul(enabled ? matrix4f : matrix4f2);
   }

   private static Matrix4f createMatrix4f(float value) {
      Matrix4f matrix4f = new Matrix4f()
         .translation(
            value * 1.13F / 16.0F,
            0.2F,
            0.070625F
         )
         .rotateY(-1.5707964F)
         .rotateZ(0.43633232F)
         .scale(0.68F);
      return new Matrix4f()
         .translation(0.0F, 0.25F, 0.125F)
         .rotateY(value * -135.0F * 0.017453292F)
         .rotateZ(value * 25.0F * 0.017453292F)
         .scale(1.7F)
         .rotateY(value < 0.0F ? 3.1415927F : 0.0F)
         .mul(matrix4f.invert());
   }

   private static float calculateValue(float value) {
      return Float.isFinite(value) ? Math.max(0.0F, Math.min(1.0F, value)) : 0.0F;
   }
}
