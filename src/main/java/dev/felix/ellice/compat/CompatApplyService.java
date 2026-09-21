package dev.felix.ellice.compat;

import com.mojang.blaze3d.platform.NativeImage;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.cape.CapeEnabledService;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CompatApplyService {
   private static final Identifier identifier = Identifier.fromNamespaceAndPath("ellice", "dynamic/local_cape");
   private static DynamicTexture dynamicTexture;
   private static BufferedImage bufferedImage;
   private static boolean enabled;

   private CompatApplyService() {
   }

   public static PlayerSkin apply(PlayerSkin playerSkin) {
      if (CapeEnabledService.active() && !enabled) {
         try {
            BufferedImage currentBufferedImage = CapeEnabledService.frame();
            if (currentBufferedImage == null) {
               return playerSkin;
            }

            if (dynamicTexture == null || dynamicTexture.getPixels() == null) {
               NativeImage width = new NativeImage(currentBufferedImage.getWidth(), currentBufferedImage.getHeight(), false);
               updateState(width, currentBufferedImage);
               dynamicTexture = new DynamicTexture(() -> "ellice animated cape", width);
               Minecraft.getInstance().getTextureManager().register(identifier, dynamicTexture);
               bufferedImage = currentBufferedImage;
            } else if (bufferedImage != currentBufferedImage) {
               updateState(dynamicTexture.getPixels(), currentBufferedImage);
               dynamicTexture.upload();
               bufferedImage = currentBufferedImage;
            }

            return new PlayerSkin(playerSkin.body(), new ResourceTexture(identifier, identifier), playerSkin.elytra(), playerSkin.model(), playerSkin.secure());
         } catch (RuntimeException exception) {
            enabled = true;
            CoreIsInitializedHandler.LOGGER.error("Cape texture upload failed", exception);
            return playerSkin;
         }
      } else {
         return playerSkin;
      }
   }

   private static void updateState(NativeImage nativeImage, BufferedImage bufferedImage) {
      for (int index = 0; index < bufferedImage.getHeight(); index++) {
         for (int currentIndex = 0; currentIndex < bufferedImage.getWidth(); currentIndex++) {
            nativeImage.setPixel(currentIndex, index, bufferedImage.getRGB(currentIndex, index));
         }
      }
   }

   public static void close() {
      if (dynamicTexture != null) {
         Minecraft.getInstance().getTextureManager().release(identifier);
      }

      dynamicTexture = null;
      bufferedImage = null;
      enabled = false;
   }
}

