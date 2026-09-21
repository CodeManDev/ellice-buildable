package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatContextService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.notebot.NotebotDecoderTracker;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.screen.builtin.NoteStudioScreen;
import net.minecraft.client.Minecraft;

public final class NoteStudioModule extends ModuleSettingsService
    implements ModuleOperationHandler {
  private NotebotDecoderTracker notebotDecoderTracker;

  public NoteStudioModule() {
    super(
        ModuleBuilderData.builder("Note Studio")
            .description(
                "Choose an MP3, auto-tune nearby note blocks, and perform its melody and chords.")
            .category(ModuleFeatureType.TOOLS)
            .build());
  }

  public NotebotDecoderTracker controller() {
    if (this.notebotDecoderTracker == null) {
      this.notebotDecoderTracker =
          new NotebotDecoderTracker(
              new CompatContextService(), item -> Minecraft.getInstance().execute(item));
    }

    return this.notebotDecoderTracker;
  }

  @Override
  protected void onEnable() {
    if (CoreIsInitializedHandler.isReady()) {
      this.on(EventAttackInputService.TICK).run(item -> this.controller().tick());
      this.on(EventAttackInputService.WORLD).run(item -> this.controller().disconnect());
      this.openPanel();
    }
  }

  @Override
  public void openPanel() {
    if (CoreIsInitializedHandler.isReady()) {
      if (!this.isEnabled()) {
        this.enable();
      } else {
        if (CoreIsInitializedHandler.get().screens() != null) {
          CoreIsInitializedHandler.get().screens().open(new NoteStudioScreen(this.controller()));
        }
      }
    }
  }

  @Override
  protected void onDisable() {
    if (this.notebotDecoderTracker != null) {
      this.notebotDecoderTracker.close();
    }

    if (CoreIsInitializedHandler.isReady()
        && CoreIsInitializedHandler.get().screens() != null
        && "note-studio".equals(CoreIsInitializedHandler.get().screens().currentId())) {
      CoreIsInitializedHandler.get().screens().close();
    }
  }
}
