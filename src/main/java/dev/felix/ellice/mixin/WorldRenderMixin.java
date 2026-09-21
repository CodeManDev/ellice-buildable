package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.CompatData;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class WorldRenderMixin {
  @Unique private final Matrix4f ellice$worldProjection = new Matrix4f();

  @Shadow
  public abstract Camera getMainCamera();

  @ModifyArg(
      method = "renderLevel(Lnet/minecraft/client/DeltaTracker;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"),
      index = 0)
  private Matrix4f ellice$captureWorldProjection(Matrix4f projection) {
    this.ellice$worldProjection.set(projection);
    return projection;
  }

  @Inject(
      method = "renderLevel(Lnet/minecraft/client/DeltaTracker;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V",
              ordinal = 0))
  private void ellice$worldRender(DeltaTracker tickCounter, CallbackInfo ci) {
    if (this.ellice$canRenderWorld()) {
      EventAttackInputService.WorldRender frame = this.ellice$createFrame(tickCounter);
      this.ellice$withMainTarget(
          () ->
              CoreIsInitializedHandler.get()
                  .bus()
                  .post(EventAttackInputService.WORLD_RENDER, frame));
    }
  }

  @Inject(
      method = "render(Lnet/minecraft/client/DeltaTracker;Z)V",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/renderer/fog/FogRenderer;endFrame()V",
              shift = Shift.AFTER))
  private void ellice$worldPostRender(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
    if (tick && this.ellice$canRenderWorld()) {
      EventAttackInputService.WorldRender frame = this.ellice$createFrame(tickCounter);
      this.ellice$withMainTarget(
          () ->
              CoreIsInitializedHandler.get()
                  .bus()
                  .post(
                      EventAttackInputService.WORLD_POST_RENDER,
                      new EventAttackInputService.WorldPostRender(frame)));
    }
  }

  @Unique
  private boolean ellice$canRenderWorld() {
    if (!CoreIsInitializedHandler.isReady()) {
      return false;
    }

    Minecraft mc = Minecraft.getInstance();
    return mc.isGameLoadFinished() && mc.level != null;
  }

  @Unique
  private EventAttackInputService.WorldRender ellice$createFrame(DeltaTracker tickCounter) {
    GameRenderer self = (GameRenderer) (Object) this;
    Camera camera = this.getMainCamera();
    float tickDelta = CompatAdapterService.gameTickDelta(tickCounter);
    CompatData matrices = CompatAdapterService.worldMatrices(self);
    return new EventAttackInputService.WorldRender(
        camera,
        matrices.cameraPos(),
        matrices.view(),
        new Matrix4f(this.ellice$worldProjection),
        tickDelta);
  }

  @Unique
  private void ellice$bindMainTarget() {
    FramebufferInfo mainTarget = CompatAdapterService.mainFramebuffer(Minecraft.getInstance());
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.framebufferId());
    GL11.glDrawBuffer(mainTarget.framebufferId() == 0 ? GL11.GL_BACK : GL30.GL_COLOR_ATTACHMENT0);
  }

  @Unique
  private void ellice$withMainTarget(Runnable action) {
    if (!FatalIsTrippedService.isTripped()) {
      GlRenderer encoder = null;

      try {
        encoder = (GlRenderer) RhiDeviceService.device().encoder();
        encoder.saveGLState();

        try {
          this.ellice$bindMainTarget();
          action.run();
        } finally {
          encoder.restoreGLState();
        }
      } catch (Throwable t) {
        FatalIsTrippedService.trip(t, FatalCaptureService.Kind.RENDERER);
      }
    }
  }
}
