package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatSessionNameService;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public abstract class NameChangerFontRenderMixin {
   @ModifyVariable(
      method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private String ellice$nameString(String text) {
      return CompatSessionNameService.replace(text);
   }

   @ModifyVariable(
      method = {
            "drawInBatch8xOutline(Lnet/minecraft/util/FormattedCharSequence;FFIILorg/joml/Matrix4fc;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;"
      },
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private FormattedCharSequence ellice$nameSequence(FormattedCharSequence text) {
      return CompatSessionNameService.replace(text);
   }
}
