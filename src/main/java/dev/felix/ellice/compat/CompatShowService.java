package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.menu.MenuHardcoreHandler;
import dev.felix.ellice.mixin.CreateWorldScreenAccess;
import dev.felix.ellice.ui.screen.builtin.WorldCreationScreen;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState.SelectedGameMode;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState.WorldTypeEntry;

public final class CompatShowService implements MenuHardcoreHandler {
  private final CreateWorldScreen createWorldScreen;
  private final WorldCreationUiState worldCreationUiState;
  private boolean enabled;

  private CompatShowService(CreateWorldScreen currentCreateWorldScreen) {
    this.createWorldScreen = currentCreateWorldScreen;
    this.worldCreationUiState = currentCreateWorldScreen.getUiState();
  }

  public static boolean show(CreateWorldScreen createWorldScreen) {
    if (!CoreIsInitializedHandler.isReady()
        || CoreIsInitializedHandler.get().screens() == null
        || Minecraft.getInstance().level != null) {
      return false;
    }

    if (createWorldScreen.getUiState().isDebug()) {
      return false;
    }

    CoreIsInitializedHandler.get()
        .screens()
        .open(new WorldCreationScreen(new CompatShowService(createWorldScreen)));
    return "world-creation".equals(CoreIsInitializedHandler.get().screens().currentId());
  }

  @Override
  public MenuHardcoreHandler.State state() {
    List<WorldTypeEntry> items = this.worldCreationUiState.getNormalPresetList();
    return new MenuHardcoreHandler.State(
        this.worldCreationUiState.getName(),
        this.worldCreationUiState.getTargetFolder(),
        this.worldCreationUiState.getSeed(),
        MenuHardcoreHandler.Mode.valueOf(this.worldCreationUiState.getGameMode().name()),
        MenuHardcoreHandler.Difficulty.valueOf(this.worldCreationUiState.getDifficulty().name()),
        items.stream().map(item -> item.describePreset().getString()).toList(),
        items.indexOf(this.worldCreationUiState.getWorldType()),
        this.worldCreationUiState.isAllowCommands(),
        this.worldCreationUiState.isGenerateStructures(),
        this.worldCreationUiState.isBonusChest());
  }

  @Override
  public void name(String currentName) {
    this.worldCreationUiState.setName(currentName);
  }

  @Override
  public void seed(String text) {
    this.worldCreationUiState.setSeed(text);
  }

  @Override
  public void mode(MenuHardcoreHandler.Mode currentMode) {
    this.worldCreationUiState.setGameMode(SelectedGameMode.valueOf(currentMode.name()));
  }

  @Override
  public void difficulty(MenuHardcoreHandler.Difficulty currentDifficulty) {
    if (!this.worldCreationUiState.isHardcore()) {
      this.worldCreationUiState.setDifficulty(
          net.minecraft.world.Difficulty.valueOf(currentDifficulty.name()));
    }
  }

  @Override
  public void worldType(int index) {
    List<WorldTypeEntry> items = this.worldCreationUiState.getNormalPresetList();
    if (index >= 0 && index < items.size()) {
      this.worldCreationUiState.setWorldType((WorldTypeEntry) items.get(index));
    }
  }

  @Override
  public void commands(boolean enabled) {
    if (!this.worldCreationUiState.isHardcore()) {
      this.worldCreationUiState.setAllowCommands(enabled);
    }
  }

  @Override
  public void structures(boolean enabled) {
    this.worldCreationUiState.setGenerateStructures(enabled);
  }

  @Override
  public void bonusChest(boolean enabled) {
    if (!this.worldCreationUiState.isHardcore()) {
      this.worldCreationUiState.setBonusChest(enabled);
    }
  }

  @Override
  public void create() {
    if (!this.enabled) {
      this.enabled = true;
      CoreIsInitializedHandler.get().screens().closeForNavigation();

      try {
        ((CreateWorldScreenAccess) this.createWorldScreen).ellice$create();
      } catch (RuntimeException exception) {
        ((CreateWorldScreenAccess) this.createWorldScreen).ellice$discardTemporaryData();
        CompatReaderService.failure(
            "Could not create this world. Return to your worlds and try again.");
      }
    }
  }

  @Override
  public void cancel() {
    if (!this.enabled) {
      this.enabled = true;
      CoreIsInitializedHandler.get().screens().closeForNavigation();
      this.createWorldScreen.onClose();
    }
  }

  @Override
  public void dispose() {
    if (!this.enabled) {
      this.enabled = true;
      ((CreateWorldScreenAccess) this.createWorldScreen).ellice$discardTemporaryData();
    }
  }
}
