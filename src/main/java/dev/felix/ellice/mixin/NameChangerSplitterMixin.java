package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatSessionNameService;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StringSplitter.class)
public abstract class NameChangerSplitterMixin {
  @ModifyVariable(
      method = {
        "stringWidth(Ljava/lang/String;)F",
        "splitLines(Ljava/lang/String;ILnet/minecraft/network/chat/Style;)Ljava/util/List;"
      },
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private String ellice$nameString(String text) {
    return CompatSessionNameService.replace(text);
  }

  @ModifyVariable(
      method = {
        "stringWidth(Lnet/minecraft/network/chat/FormattedText;)F",
        "headByWidth(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/FormattedText;",
        "splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;)Ljava/util/List;",
        "splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;Ljava/util/function/BiConsumer;)V"
      },
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private FormattedText ellice$nameFormatted(FormattedText text) {
    return CompatSessionNameService.replace(text);
  }

  @ModifyVariable(
      method = "stringWidth(Lnet/minecraft/util/FormattedCharSequence;)F",
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0)
  private FormattedCharSequence ellice$nameSequence(FormattedCharSequence text) {
    return CompatSessionNameService.replace(text);
  }
}
