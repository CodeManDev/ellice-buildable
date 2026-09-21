package dev.felix.ellice.changelog;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ChangelogLastSeenService {
  private final Path path;
  private volatile String text;

  public ChangelogLastSeenService(Path currentPath) {
    this.path = currentPath.resolve("ellice").resolve("changelog-seen.txt");

    try {
      this.text =
          Files.exists(this.path)
              ? Files.readString(this.path, StandardCharsets.UTF_8).strip()
              : "";
    } catch (Exception exception) {
      this.text = "";
      CoreIsInitializedHandler.LOGGER.warn("Could not read changelog state", exception);
    }
  }

  public String lastSeen() {
    return this.text;
  }

  public boolean isUnseen(String currentText) {
    return currentText != null && !currentText.isBlank() && !currentText.equals(this.text);
  }

  public synchronized void markSeen(String currentText) {
    if (currentText != null && !currentText.isBlank()) {
      this.text = currentText;

      try {
        Files.createDirectories(this.path.getParent());
        Path currentPath = this.path.resolveSibling(this.path.getFileName() + ".tmp");
        Files.writeString(
            currentPath, currentText + System.lineSeparator(), StandardCharsets.UTF_8);

        try {
          Files.move(
              currentPath,
              this.path,
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
          Files.move(currentPath, this.path, StandardCopyOption.REPLACE_EXISTING);
        }
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.warn("Could not persist changelog state", exception);
      }
    }
  }
}
