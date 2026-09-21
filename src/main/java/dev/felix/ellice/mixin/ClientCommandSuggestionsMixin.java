package dev.felix.ellice.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandSuggestions.class)
public abstract class ClientCommandSuggestionsMixin {
  @Shadow @Final private EditBox input;
  @Shadow @Final private Screen screen;
  @Shadow @Final private List<FormattedCharSequence> commandUsage;
  @Shadow private ParseResults<ClientSuggestionProvider> currentParse;
  @Shadow private CompletableFuture<Suggestions> pendingSuggestions;
  @Shadow private boolean keepSuggestions;
  @Shadow private boolean allowSuggestions;
  @Shadow private boolean currentParseIsCommand;
  @Shadow private boolean currentParseIsMessage;

  @Shadow
  public abstract void hide();

  @Shadow
  public abstract void showSuggestions(boolean enabled);

  @Inject(method = "updateCommandInfo", at = @At("HEAD"), cancellable = true)
  private void ellice$localSuggestions(CallbackInfo ci) {
    if (this.screen instanceof ChatScreen
        && CoreIsInitializedHandler.isReady()
        && this.input.getValue().startsWith(".")) {
      ci.cancel();
      if (!this.keepSuggestions) {
        this.hide();
        this.input.setSuggestion(null);
        this.commandUsage.clear();
        this.currentParse = null;
        this.currentParseIsCommand = this.currentParseIsMessage = false;
        this.pendingSuggestions =
            CoreIsInitializedHandler.get()
                .commands()
                .suggest(this.input.getValue(), this.input.getCursorPosition());
        if (this.allowSuggestions) {
          this.showSuggestions(false);
        }
      }
    }
  }
}
