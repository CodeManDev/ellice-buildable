package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.menu.MenuLoaderTracker;
import dev.felix.ellice.server.ServerCodec;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.LevelSummary;

public final class CompatReaderService {
  private CompatReaderService() {}

  public static MenuLoaderTracker.Loader reader() {
    return () -> {
      LevelStorageSource levelStorageSource = Minecraft.getInstance().getLevelSource();
      List items =
          (List)
              levelStorageSource.loadLevelSummaries(levelStorageSource.findLevelCandidates()).get();
      ArrayList arrayList = new ArrayList();

      for (LevelSummary levelSummary : (Iterable<LevelSummary>) (Iterable<?>) (items)) {
        int value = levelSummary.getSettings() != null ? 1 : 0;
        String text =
            value != 0
                ? levelSummary.getGameMode().getShortDisplayName().getString()
                : "Unavailable";
        arrayList.add(
            new MenuLoaderTracker.World(
                levelSummary.getLevelId(),
                levelSummary.getLevelName(),
                levelSummary.getLastPlayed(),
                text,
                value != 0 ? levelSummary.getWorldVersionName().getString() : "Unknown",
                levelSummary.getInfo().getString(),
                value != 0 && levelSummary.isHardcore(),
                value != 0 && levelSummary.hasCommands(),
                levelSummary.isExperimental(),
                levelSummary.primaryActionActive(),
                levelSummary.primaryActionMessage().getString(),
                levelSummary.canEdit(),
                createBufferedImage(levelSummary.getIcon())));
      }

      return arrayList;
    };
  }

  private static BufferedImage createBufferedImage(Path path) {
    try {
      if (path != null
          && Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)
          && Files.size(path) <= 262144L) {
        try (InputStream inputStream = Files.newInputStream(path)) {
          return ServerCodec.decode(inputStream.readNBytes(262145));
        }
      } else {
        return null;
      }
    } catch (Exception exception) {
      return null;
    }
  }

  public static void play(String text) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      CoreIsInitializedHandler.get().screens().closeForNavigation();

      try {
        minecraft
            .createWorldOpenFlows()
            .openWorld(text, () -> minecraft.setScreen(new TitleScreen()));
      } catch (Exception exception) {
        failure("Could not open this world.");
      }
    }
  }

  public static void edit(String text) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      CoreIsInitializedHandler.get().screens().closeForNavigation();
      LevelStorageAccess levelStorageAccess = null;

      try {
        levelStorageAccess = minecraft.getLevelSource().validateAndCreateAccess(text);
        LevelStorageAccess currentLevelStorageAccess = levelStorageAccess;
        EditWorldScreen editWorldScreen =
            EditWorldScreen.create(
                minecraft,
                levelStorageAccess,
                item -> {
                  updateState(currentLevelStorageAccess);
                  minecraft.setScreen(new TitleScreen());
                });
        if (editWorldScreen == null) {
          updateState(levelStorageAccess);
          minecraft.setScreen(new TitleScreen());
        } else {
          minecraft.setScreen(editWorldScreen);
        }
      } catch (Exception exception) {
        updateState(levelStorageAccess);
        failure("Could not edit this world. It may be open in another Minecraft instance.");
      }
    }
  }

  private static void updateState(LevelStorageAccess levelStorageAccess) {
    if (levelStorageAccess != null) {
      try {
        levelStorageAccess.close();
      } catch (Exception exception) {
      }
    }
  }

  static void failure(String message) {
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.setScreen(
        new AlertScreen(
            () -> minecraft.setScreen(new TitleScreen()),
            Component.literal("World unavailable"),
            Component.literal(message)));
  }
}
