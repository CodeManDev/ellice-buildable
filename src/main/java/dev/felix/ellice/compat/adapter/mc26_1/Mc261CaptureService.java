package dev.felix.ellice.compat.adapter.mc26_1;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felix.ellice.compat.CompatListenService;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class Mc261CaptureService implements VertexConsumer {
   private static final float[] float2 = new float[0];
   private static final int count = 4608;
   private final CompatListenService.Ticket items;
   private float[] float3 = new float[1536];
   private final float[] float4 = new float[12];
   private int count2;
   private int count3;
   private boolean enabled;

   private Mc261CaptureService(CompatListenService.Ticket ticket) {
      this.items = ticket;
   }

   public static void capture(UUID uUID, int value, PlayerModel playerModel, AvatarRenderState avatarRenderState, PoseStack poseStack) {
      CompatListenService.Ticket ticket = CompatListenService.request(uUID, value);
      if (ticket != null) {
         Mc261CaptureService mc261Capture = new Mc261CaptureService(ticket);
         playerModel.setupAnim(avatarRenderState);
         playerModel.renderToBuffer(poseStack, mc261Capture, avatarRenderState.lightCoords, 0, -1);
         mc261Capture.updateState();
      }
   }

   private void updateState() {
      this.items
         .publish(
            this.count2 != 0 && !this.enabled ? Arrays.copyOf(this.float3, this.count2) : new float[0], float2
         );
   }

   public VertexConsumer addVertex(float value, float currentValue, float nextValue) {
      int index = this.count3 * 3;
      this.float4[index] = value;
      this.float4[index + 1] = currentValue;
      this.float4[index + 2] = nextValue;
      this.count3++;
      if (this.count3 == 4) {
         this.updateState2(0, 1, 2);
         this.updateState2(0, 2, 3);
         this.count3 = 0;
      }

      return this;
   }

   public void addVertex(
      float value, float currentValue, float nextValue, int previousValue, float sourceValue, float targetValue, int inputValue, int outputValue, float resultValue, float candidateValue, float selectedValue
   ) {
      this.addVertex(value, currentValue, nextValue);
   }

   public VertexConsumer setColor(int color, int currentColor, int nextColor, int previousColor) {
      return this;
   }

   public VertexConsumer setColor(int color) {
      return this;
   }

   public VertexConsumer setUv(float value, float currentValue) {
      return this;
   }

   public VertexConsumer setUv1(int value, int currentValue) {
      return this;
   }

   public VertexConsumer setUv2(int value, int currentValue) {
      return this;
   }

   public VertexConsumer setNormal(float value, float currentValue, float nextValue) {
      return this;
   }

   public VertexConsumer setLineWidth(float value) {
      return this;
   }

   private void updateState2(int value, int currentValue, int nextValue) {
      if (!this.enabled) {
         if (this.count2 + 9 > 4608) {
            this.enabled = true;
            this.count2 = 0;
         } else {
            this.updateState4(this.count2 + 9);
            this.updateState3(value);
            this.updateState3(currentValue);
            this.updateState3(nextValue);
         }
      }
   }

   private void updateState3(int value) {
      int index = value * 3;
      this.float3[this.count2++] = this.float4[index];
      this.float3[this.count2++] = this.float4[index + 1];
      this.float3[this.count2++] = this.float4[index + 2];
   }

   private void updateState4(int value) {
      if (value > this.float3.length) {
         this.float3 = Arrays.copyOf(
            this.float3, Math.min(4608, Math.max(value, this.float3.length + this.float3.length / 2))
         );
      }
   }
}

