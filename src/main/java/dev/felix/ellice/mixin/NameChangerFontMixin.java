package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatSessionNameService;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public abstract class NameChangerFontMixin {
  @ModifyVariable(
      method = "width(Ljava/lang/String;)I",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private String ellice$nameWidth(String text) {
    return CompatSessionNameService.replace(text);
  }

  @ModifyVariable(
      method = {
        "width(Lnet/minecraft/network/chat/FormattedText;)I",
        "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;",
        "substrByWidth(Lnet/minecraft/network/chat/FormattedText;I)Lnet/minecraft/network/chat/FormattedText;",
        "wordWrapHeight(Lnet/minecraft/network/chat/FormattedText;I)I"
      },
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private FormattedText ellice$nameFormatted(FormattedText text) {
    return CompatSessionNameService.replace(text);
  }

  @ModifyVariable(
      method = "width(Lnet/minecraft/util/FormattedCharSequence;)I",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private FormattedCharSequence ellice$nameSequence(FormattedCharSequence text) {
    return CompatSessionNameService.replace(text);
  }
}
