package dev.felix.ellice.compat.adapter.mc26_1;

import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDeviceBackend;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.felix.ellice.compat.CompatData;
import dev.felix.ellice.compat.CompatMode;
import dev.felix.ellice.compat.TextureUvRegion;
import dev.felix.ellice.compat.CompatibilityProvider;
import dev.felix.ellice.compat.CompatibilityDescriptor;
import dev.felix.ellice.compat.CompatibilityCapabilities;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.compat.RotationGate;
import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.mixin.GlDeviceAccessor;
import dev.felix.ellice.mixin.GpuDeviceAccessor;
import dev.felix.ellice.mixin.RenderTargetAccessor;
import dev.felix.ellice.security.ElliceKeep;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents.AllowMouseClick;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

@ElliceKeep
public final class Mc26_1Adapter implements CompatibilityProvider {
   private static final CompatibilityCapabilities CAPABILITIES = new CompatibilityCapabilities("26.1.x", true, true, true);
   private static final CompatibilityDescriptor METADATA = new CompatibilityDescriptor(
      "mc26_1", "Minecraft 26.1.x", "26.1.x", List.of("26.1.x", "26.1.2"), CompatMode.SUPPORTED, CAPABILITIES
   );
   private static volatile boolean spriteDiagLogged = false;

   @Override
   public CompatibilityDescriptor metadata() {
      return METADATA;
   }

   @Override
   public Vector3f cameraForward(Camera camera) {
      return new Vector3f(camera.forwardVector());
   }

   @Override
   public Vector3f cameraUp(Camera camera) {
      return new Vector3f(camera.upVector());
   }

   @Override
   public TextureUvRegion itemSprite(ItemStack stack) {
      try {
         if (stack != null && !stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
               return spriteNull("level==null");
            }

            ItemStackRenderState state = new ItemStackRenderState();
            mc.getItemModelResolver().updateForTopItem(state, stack, ItemDisplayContext.GUI, mc.level, mc.player, 0);
            if (state.isEmpty()) {
               return spriteNull("state.isEmpty");
            }

            Baked material = state.pickParticleMaterial(RandomSource.create());
            if (material == null) {
               return spriteNull("material==null");
            }

            TextureAtlasSprite sprite = material.sprite();
            if (sprite == null) {
               return spriteNull("sprite==null");
            }

            AbstractTexture tex = mc.getTextureManager().getTexture(sprite.atlasLocation());
            GpuTexture gpu = tex.getTexture();
            return gpu instanceof GlTexture gl
               ? new TextureUvRegion(gl.glId(), sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1())
               : spriteNull("not GlTexture: " + (gpu == null ? "null" : gpu.getClass().getName()));
         } else {
            return null;
         }
      } catch (Throwable t) {
         if (!spriteDiagLogged) {
            spriteDiagLogged = true;
            CoreIsInitializedHandler.LOGGER.warn("[armor] itemSprite threw", t);
         }

         return null;
      }
   }

   private static TextureUvRegion spriteNull(String why) {
      if (!spriteDiagLogged) {
         spriteDiagLogged = true;
         CoreIsInitializedHandler.LOGGER.warn("[armor] itemSprite null: {}", why);
      }

      return null;
   }

   @Override
   public FramebufferInfo mainFramebuffer(Minecraft minecraft) {
      RenderTarget target = minecraft.getMainRenderTarget();
      return new FramebufferInfo(target.width, target.height, this.framebufferId(target));
   }

   @Override
   public int framebufferId(RenderTarget target) {
      if (((RenderTargetAccessor)target).ellice$colorTexture() instanceof GlTexture glColor) {
         GpuTexture depth = ((RenderTargetAccessor)target).ellice$depthTexture();
         GpuDeviceBackend backend = ((GpuDeviceAccessor)RenderSystem.getDevice()).ellice$backend();
         DirectStateAccess dsa = ((GlDeviceAccessor)backend).ellice$directStateAccess();
         return glColor.getFbo(dsa, depth);
      } else {
         return GL30.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
      }
   }

   @Override
   public CompatData worldMatrices(GameRenderer renderer) {
      CameraRenderState camera = renderer.getGameRenderState().levelRenderState.cameraRenderState;
      return new CompatData(new Matrix4f(camera.viewRotationMatrix), new Matrix4f(camera.projectionMatrix), camera.pos);
   }

   @Override
   public float gameTickDelta(DeltaTracker tracker) {
      return tracker.getGameTimeDeltaPartialTick(true);
   }

   @Override
   public long windowHandle(Minecraft minecraft) {
      return minecraft.getWindow().handle();
   }

   @Override
   public Optional<CombatCompatibility> rotationEnvironment() {
      return Optional.of(MinecraftCombatCompatibility.INSTANCE);
   }

   @Override
   public Optional<ScaffoldCompatibility> scaffoldEnvironment() {
      return Optional.of(MinecraftScaffoldCompatibility.INSTANCE);
   }

   @Override
   public Optional<CompatLoadedHandler> terrainEnvironment() {
      return Optional.of(new Mc261SnapshotService());
   }

   @Override
   public void registerScreenClickGuard(RotationGate guard) {
      ScreenEvents.AFTER_INIT
         .register(
            (AfterInit)(mc, screen, w, h) -> ScreenMouseEvents.allowMouseClick(screen)
               .register((AllowMouseClick)(s, event) -> guard.allow((float)event.x(), (float)event.y()))
         );
   }
}
