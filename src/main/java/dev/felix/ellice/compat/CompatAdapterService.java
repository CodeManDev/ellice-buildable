package dev.felix.ellice.compat;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public final class CompatAdapterService {
   private static final CompatibilityProvider compatOperationHandler2 = MinecraftAdapters.active();

   private CompatAdapterService() {
   }

   public static CompatibilityProvider adapter() {
      return compatOperationHandler2;
   }

   public static CompatibilityDescriptor metadata() {
      return compatOperationHandler2.metadata();
   }

   public static CompatibilityCapabilities capabilities() {
      return compatOperationHandler2.capabilities();
   }

   public static FramebufferInfo mainFramebuffer(Minecraft minecraft) {
      return compatOperationHandler2.mainFramebuffer(minecraft);
   }

   public static int framebufferId(RenderTarget renderTarget) {
      return compatOperationHandler2.framebufferId(renderTarget);
   }

   public static CompatData worldMatrices(GameRenderer gameRenderer) {
      return compatOperationHandler2.worldMatrices(gameRenderer);
   }

   public static Vector3f cameraForward(Camera camera) {
      return compatOperationHandler2.cameraForward(camera);
   }

   public static Vector3f cameraUp(Camera camera) {
      return compatOperationHandler2.cameraUp(camera);
   }

   public static float gameTickDelta(DeltaTracker deltaTracker) {
      return compatOperationHandler2.gameTickDelta(deltaTracker);
   }

   public static long windowHandle(Minecraft minecraft) {
      return compatOperationHandler2.windowHandle(minecraft);
   }

   public static DisplayMetrics uiViewport(Minecraft minecraft, boolean enabled) {
      Window window = minecraft.getWindow();
      return new DisplayMetrics(
         window.getWidth(),
         window.getHeight(),
         window.getScreenWidth(),
         window.getScreenHeight(),
         window.getGuiScaledWidth(),
         window.getGuiScaledHeight(),
         window.getGuiScale(),
         enabled
      );
   }

   public static Optional<CombatCompatibility> rotationEnvironment() {
      return compatOperationHandler2.rotationEnvironment();
   }

   public static Optional<ScaffoldCompatibility> scaffoldEnvironment() {
      return compatOperationHandler2.scaffoldEnvironment();
   }

   public static Optional<CompatLoadedHandler> terrainEnvironment() {
      return compatOperationHandler2.terrainEnvironment();
   }

   public static void registerScreenClickGuard(RotationGate rotationGate) {
      compatOperationHandler2.registerScreenClickGuard(rotationGate);
   }

   public static TextureUvRegion itemSprite(ItemStack itemStack) {
      return compatOperationHandler2.itemSprite(itemStack);
   }
}
