


package dev.felix.ellice.ui.text;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.text.TextTextureService;
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
    private final Path fe4pfdrt22oo;
    private final List<FontEntry> f9i3rdp6ee7l = new ArrayList<FontEntry>();
    private volatile List<FontEntry> f91sglz1lty5 = List.of();
    private volatile boolean fj7gakprdv6c;

    public TextFontDirectoryService(Path path) {
        this.fe4pfdrt22oo = path.resolve("ellice-fonts");
        this.scanUserFonts();
        this.m2dvay3qw86y();
    }

    public Path fontDirectory() {
        return this.fe4pfdrt22oo;
    }

    public boolean systemFontsLoaded() {
        return this.fj7gakprdv6c;
    }

    public synchronized void scanUserFonts() {
        this.f9i3rdp6ee7l.clear();
        try {
            Files.createDirectories(this.fe4pfdrt22oo, new FileAttribute[0]);
            try (Stream<Path> stream = Files.list(this.fe4pfdrt22oo);){
                stream.filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(TextFontDirectoryService::mggllbx00w5c).sorted().forEach(this::micwzdrz7y9n);
            }
        }
        catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.warn("Could not scan ellice font directory {}", (Object)this.fe4pfdrt22oo, (Object)exception);
        }
    }

    


    public List<FontEntry> entries() {
        LinkedHashMap<String, FontEntry> linkedHashMap = new LinkedHashMap<String, FontEntry>();
        linkedHashMap.put("", new FontEntry("", "Client", Kind.CLIENT, null, null));
        linkedHashMap.put("minecraft", new FontEntry("minecraft", "Minecraft", Kind.MINECRAFT, null, null));
        TextFontDirectoryService textFontDirectoryService = this;
        synchronized (textFontDirectoryService) {
            for (FontEntry fontEntry : this.f9i3rdp6ee7l) {
                linkedHashMap.putIfAbsent(fontEntry.key(), fontEntry);
            }
        }
        for (FontEntry fontEntry : this.f91sglz1lty5) {
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

    private void micwzdrz7y9n(Path path) {
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            Font font = Font.createFont(TextFontDirectoryService.mc7qfwo6qbms(path), inputStream);
            String string = font.getFamily(Locale.ROOT);
            String string2 = "user:" + TextFontDirectoryService.m6gjkdt26opa(string);
            this.f9i3rdp6ee7l.add(new FontEntry(string2, string, Kind.USER, TextTextureService.FontSource.FILESYSTEM, path.toAbsolutePath().toString()));
        }
        catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.warn("Ignoring invalid font file {}", (Object)path.getFileName());
        }
    }

    private void m2dvay3qw86y() {
        CompletableFuture.runAsync(() -> {
            try {
                String[] stringArray = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ROOT);
                ArrayList<FontEntry> arrayList = new ArrayList<FontEntry>(stringArray.length);
                for (String string : stringArray) {
                    arrayList.add(new FontEntry("sys:" + TextFontDirectoryService.m6gjkdt26opa(string), string, Kind.SYSTEM, TextTextureService.FontSource.SYSTEM_NAME, string));
                }
                arrayList.sort(Comparator.comparing(FontEntry::display, String.CASE_INSENSITIVE_ORDER));
                this.f91sglz1lty5 = List.copyOf(arrayList);
            }
            catch (Throwable throwable) {
                CoreIsInitializedHandler.LOGGER.warn("Could not enumerate system fonts", throwable);
                this.f91sglz1lty5 = List.of();
            }
            finally {
                this.fj7gakprdv6c = true;
            }
        });
    }

    private static boolean mggllbx00w5c(Path path) {
        String string = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return (string.endsWith(".ttf") || string.endsWith(".otf") ? 1 : 0) != 0;
    }

    private static int mc7qfwo6qbms(Path path) {
        return path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".otf") ? 1 : 0;
    }

    private static String m6gjkdt26opa(String string) {
        String string2 = Normalizer.normalize(string, Normalizer.Form.NFKD).replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        return string2.isEmpty() ? "font" : string2;
    }

    public record FontEntry(String key, String display, Kind kind, TextTextureService.FontSource source, String ref) {
    }

    public static enum Kind {
        CLIENT,
        MINECRAFT,
        USER,
        SYSTEM;

    }
}

