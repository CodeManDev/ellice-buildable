package dev.felix.ellice.ui.text;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class TextFontDirectoryService {
  private final Path path;
  private final List<FontEntry> items = new ArrayList<FontEntry>();
  private volatile List<FontEntry> items2 = List.of();
  private volatile boolean enabled;

  public TextFontDirectoryService(Path path) {
    this.path = path.resolve("ellice-fonts");
    this.scanUserFonts();
    this.updateState2();
  }

  public Path fontDirectory() {
    return this.path;
  }

  public boolean systemFontsLoaded() {
    return this.enabled;
  }

  public synchronized void scanUserFonts() {
    this.items.clear();
    try {
      Files.createDirectories(this.path, new FileAttribute[0]);
      try (Stream<Path> stream = Files.list(this.path); ) {
        stream
            .filter(path -> Files.isRegularFile(path, new LinkOption[0]))
            .filter(TextFontDirectoryService::checkCondition)
            .sorted()
            .forEach(this::updateState);
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Could not scan ellice font directory {}", (Object) this.path, (Object) exception);
    }
  }

  public List<FontEntry> entries() {
    LinkedHashMap<String, FontEntry> linkedHashMap = new LinkedHashMap<String, FontEntry>();
    linkedHashMap.put("", new FontEntry("", "Client", Kind.CLIENT, null, null));
    linkedHashMap.put(
        "minecraft", new FontEntry("minecraft", "Minecraft", Kind.MINECRAFT, null, null));
    TextFontDirectoryService textFontDirectoryService = this;
    synchronized (textFontDirectoryService) {
      for (FontEntry fontEntry : this.items) {
        linkedHashMap.putIfAbsent(fontEntry.key(), fontEntry);
      }
    }
    for (FontEntry fontEntry : this.items2) {
      linkedHashMap.putIfAbsent(fontEntry.key(), fontEntry);
    }
    return List.copyOf(linkedHashMap.values());
  }

  public FontEntry find(String string) {
    String string2 = string == null ? "" : string;
    for (FontEntry fontEntry : this.entries()) {
      if (!fontEntry.key().equals(string2)) continue;
      return fontEntry;
    }
    return this.entries().getFirst();
  }

  private void updateState(Path path) {
    try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]); ) {
      Font font = Font.createFont(TextFontDirectoryService.calculateValue(path), inputStream);
      String string = font.getFamily(Locale.ROOT);
      String string2 = "user:" + TextFontDirectoryService.createText(string);
      this.items.add(
          new FontEntry(
              string2,
              string,
              Kind.USER,
              TextTextureService.FontSource.FILESYSTEM,
              path.toAbsolutePath().toString()));
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Ignoring invalid font file {}", (Object) path.getFileName());
    }
  }

  private void updateState2() {
    CompletableFuture.runAsync(
        () -> {
          try {
            String[] stringArray =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames(Locale.ROOT);
            ArrayList<FontEntry> arrayList = new ArrayList<FontEntry>(stringArray.length);
            for (String string : stringArray) {
              arrayList.add(
                  new FontEntry(
                      "sys:" + TextFontDirectoryService.createText(string),
                      string,
                      Kind.SYSTEM,
                      TextTextureService.FontSource.SYSTEM_NAME,
                      string));
            }
            arrayList.sort(Comparator.comparing(FontEntry::display, String.CASE_INSENSITIVE_ORDER));
            this.items2 = List.copyOf(arrayList);
          } catch (Throwable throwable) {
            CoreIsInitializedHandler.LOGGER.warn("Could not enumerate system fonts", throwable);
            this.items2 = List.of();
          } finally {
            this.enabled = true;
          }
        });
  }

  private static boolean checkCondition(Path path) {
    String string = path.getFileName().toString().toLowerCase(Locale.ROOT);
    return (string.endsWith(".ttf") || string.endsWith(".otf") ? 1 : 0) != 0;
  }

  private static int calculateValue(Path path) {
    return path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".otf") ? 1 : 0;
  }

  private static String createText(String string) {
    String string2 =
        Normalizer.normalize(string, Normalizer.Form.NFKD)
            .replaceAll("\\p{M}+", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("(^-|-$)", "");
    return string2.isEmpty() ? "font" : string2;
  }

  public record FontEntry(
      String key, String display, Kind kind, TextTextureService.FontSource source, String ref) {}

  public static enum Kind {
    CLIENT,
    MINECRAFT,
    USER,
    SYSTEM;
  }
}
