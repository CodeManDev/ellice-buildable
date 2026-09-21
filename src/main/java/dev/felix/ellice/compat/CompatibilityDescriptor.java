package dev.felix.ellice.compat;

import java.util.List;

public record CompatibilityDescriptor(
    String id,
    String displayName,
    String versionFamily,
    List<String> testedMinecraftVersions,
    CompatMode supportTier,
    CompatibilityCapabilities capabilities) {
  public boolean supportsVersion(String text) {
    if (text != null && !text.isBlank()) {
      for (String currentText : this.testedMinecraftVersions) {
        if (currentText.equals(text)) {
          return true;
        }

        if (currentText.endsWith(".x")) {
          String currentLength = currentText.substring(0, currentText.length() - 1);
          if (text.startsWith(currentLength)) {
            return true;
          }
        }
      }

      return false;
    } else {
      return false;
    }
  }
}
