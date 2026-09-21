package dev.felix.ellice.compat;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;

public final class CompatOpenUriService {
  private CompatOpenUriService() {}

  public static void openUri(String text) {
    if (text != null && !text.isBlank()) {
      try {
        openUri(new URI(text));
      } catch (Exception exception) {
      }
    }
  }

  public static void openPath(Path path) {
    if (path != null) {
      try {
        File file = path.toFile();
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().open(file);
          return;
        }
      } catch (Exception exception) {
      }

      openUri(path.toUri());
    }
  }

  public static void openUri(URI uRI) {
    if (uRI != null) {
      if (!checkCondition(uRI)) {
        updateState(uRI);
      }
    }
  }

  private static boolean checkCondition(URI uRI) {
    String[] strings = new String[] {"net.minecraft.Util", "net.minecraft.util.Util"};
    int currentLength = strings.length;
    int index = 0;

    while (index < currentLength) {
      String text = strings[index];

      try {
        Class classValue = Class.forName(text);
        Object value = classValue.getMethod("getPlatform").invoke(null);

        try {
          value.getClass().getMethod("openUri", URI.class).invoke(value, uRI);
        } catch (NoSuchMethodException noSuchMethodException) {
          value.getClass().getMethod("openUri", String.class).invoke(value, uRI.toString());
        }

        return true;
      } catch (ClassNotFoundException classNotFoundException) {
        index++;
      } catch (ReflectiveOperationException reflectiveOperationException) {
        return false;
      }
    }

    return false;
  }

  private static void updateState(URI uRI) {
    try {
      if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(uRI);
      }
    } catch (Exception exception) {
    }
  }
}
