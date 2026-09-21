package dev.felix.ellice.compat;

import com.mojang.blaze3d.pipeline.RenderTarget;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public interface CompatOperationHandler {
  CompatibilityCapabilities capabilities();

  FramebufferInfo mainFramebuffer(Minecraft minecraft);

  int framebufferId(RenderTarget renderTarget);

  CompatData worldMatrices(GameRenderer gameRenderer);

  Vector3f cameraForward(Camera camera);

  Vector3f cameraUp(Camera camera);

  float gameTickDelta(DeltaTracker deltaTracker);

  long windowHandle(Minecraft minecraft);

  default Optional<CombatCompatibility> rotationEnvironment() {
    return Optional.empty();
  }

  default Optional<ScaffoldCompatibility> scaffoldEnvironment() {
    return Optional.empty();
  }

  default Optional<CompatLoadedHandler> terrainEnvironment() {
    return Optional.empty();
  }

  default void registerScreenClickGuard(RotationGate rotationGate) {}

  default TextureUvRegion itemSprite(ItemStack itemStack) {
    return null;
  }
}
