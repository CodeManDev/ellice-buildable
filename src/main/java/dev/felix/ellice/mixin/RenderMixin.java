package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.DiagnosticsAddFrameListenerService;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.diagnostics.fatal.FatalShowingService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.impl.ImplRefreshSessionService;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class RenderMixin {
  @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V", at = @At("HEAD"))
  private void onFrameStart(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
    if (!FatalIsTrippedService.isTripped() && CoreIsInitializedHandler.isReady()) {
      try {
        ImplRefreshSessionService.refreshSession();
        CoreIsInitializedHandler client = CoreIsInitializedHandler.get();
        client.profiler().beginFrame();
        client.beginRenderFrame();
        if (client.renderer3D() != null) {
          client.renderer3D().beginPlayerFireFrame();
        }

        client
            .bus()
            .post(
                EventAttackInputService.WORLD_RENDER_PREPARE,
                new EventAttackInputService.WorldRenderPrepare(
                    CompatAdapterService.gameTickDelta(tickCounter)));
      } catch (Throwable t) {
        FatalIsTrippedService.trip(t, FatalCaptureService.Kind.RENDERER);

        try {
          if (CoreIsInitializedHandler.isInitialized()) {
            CoreIsInitializedHandler.get().profiler().endFrame();
          }
        } catch (Throwable exception) {
        }
      }
    }
  }

  @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V", at = @At("TAIL"))
  private void onRender(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
    if (!FatalIsTrippedService.isTripped()) {
      if (CoreIsInitializedHandler.isReady()) {
        if (FatalIsTrippedService.shouldDemoCrash() && FatalShowingService.canPresent()) {
          FatalIsTrippedService.trip(
              FatalIsTrippedService.demoShaderFailure(), FatalCaptureService.Kind.RENDERER);
        } else {
          Minecraft mc = Minecraft.getInstance();
          DiagnosticsAddFrameListenerService profiler = CoreIsInitializedHandler.get().profiler();

          try {
            CoreIsInitializedHandler.get()
                .bus()
                .post(
                    EventAttackInputService.RENDER,
                    new EventAttackInputService.Render(
                        null, CompatAdapterService.gameTickDelta(tickCounter)));
            if (CoreIsInitializedHandler.get().perfRecorder() != null) {
              CoreIsInitializedHandler.get().perfRecorder().captureGlInfoIfNeeded();
            }

            profiler.begin("compositor");
            FramebufferInfo mainTarget = CompatAdapterService.mainFramebuffer(mc);
            CoreIsInitializedHandler.get()
                .compositor()
                .renderFrame(
                    mainTarget.width(),
                    mainTarget.height(),
                    CoreIsInitializedHandler.get().viewport().renderScale(),
                    mainTarget.framebufferId());
          } catch (Throwable t) {
            FatalIsTrippedService.trip(t, FatalCaptureService.Kind.RENDERER);
          } finally {
            try {
              profiler.end();
            } catch (Throwable exception) {
            }

            try {
              profiler.endFrame();
            } catch (Throwable currentException) {
            }
          }
        }
      }
    }
  }
}
