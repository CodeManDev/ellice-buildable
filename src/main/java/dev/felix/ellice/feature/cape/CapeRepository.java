package dev.felix.ellice.feature.cape;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class CapeRepository {
  private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
  private static final Pattern pattern = Pattern.compile("[a-f0-9]{64}\\.image");
  private final Path path;

  public CapeRepository(Path path) {
    this.path = path;
  }

  public Selection load() throws IOException {
    Path path = this.path.resolve("selection.json");
    if (!Files.exists(path, new LinkOption[0])) {
      return null;
    }
    if (Files.size(path) > 32768L) {
      throw new IOException("Saved cape settings are invalid.");
    }
    try {
      return (Selection) gson2.fromJson(Files.readString(path), Selection.class);
    } catch (RuntimeException runtimeException) {
      throw new IOException("Saved cape settings are invalid.", runtimeException);
    }
  }

  public void save(Selection selection, byte[] byArray) throws IOException {
    Object object;
    Files.createDirectories(this.path, new FileAttribute[0]);
    if (byArray != null) {
      if (byArray.length > 0x1000000) {
        throw new IOException("Cape file is too large.");
      }
      try {
        object =
            HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(byArray))
                + ".image";
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        throw new AssertionError((Object) noSuchAlgorithmException);
      }
      CapeRepository.mcrkgu3r4q93(this.path.resolve((String) object), byArray);
      selection =
          new Selection(
              "file",
              (String) object,
              selection.title(),
              selection.page(),
              selection.credit(),
              selection.style());
    }
    CapeRepository.mcrkgu3r4q93(
        this.path.resolve("selection.json"),
        gson2.toJson((Object) selection).getBytes(StandardCharsets.UTF_8));
    String activeReference = selection.reference();
    try (Stream<Path> stream = Files.list(this.path); ) {
      for (Path path :
          stream
              .filter(candidate -> CapeRepository.mfmdn12504wg(activeReference, candidate))
              .toList()) {
        Files.deleteIfExists(path);
      }
    }
  }

  public byte[] media(String string) throws IOException {
    if (string == null || !pattern.matcher(string).matches()) {
      throw new IOException("Invalid saved cape file.");
    }
    return CapeRepository.readFile(this.path.resolve(string));
  }

  public static byte[] readFile(Path path) throws IOException {
    if (!Files.isRegularFile(path, new LinkOption[0]) || Files.size(path) > 0x1000000L) {
      throw new IOException("Choose an image file smaller than 16 MB.");
    }
    try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]); ) {
      byte[] byArray = inputStream.readNBytes(0x1000001);
      if (byArray.length > 0x1000000) {
        throw new IOException("Image exceeds 16 MB.");
      }
      byte[] byArray2 = byArray;
      return byArray2;
    }
  }

  public String key() {
    try {
      Path path = this.path.resolve("giphy-key.txt");
      if (Files.exists(path, new LinkOption[0]) && Files.size(path) < 512L) {
        return Files.readString(path).strip();
      }
      return "";
    } catch (IOException iOException) {
      return "";
    }
  }

  public void key(String string) throws IOException {
    if (string.length() > 256 || string.contains("\n")) {
      throw new IOException("Invalid API key.");
    }
    Files.createDirectories(this.path, new FileAttribute[0]);
    Path path = this.path.resolve("giphy-key.txt");
    CapeRepository.mcrkgu3r4q93(path, string.strip().getBytes(StandardCharsets.UTF_8));
    try {
      Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rw-------"));
    } catch (UnsupportedOperationException unsupportedOperationException) {

    }
  }

  private static void mcrkgu3r4q93(Path path, byte[] byArray) throws IOException {
    Path path2 = Files.createTempFile(path.getParent(), "cape-", ".tmp", new FileAttribute[0]);
    try {
      Files.write(path2, byArray, new OpenOption[0]);
      try {
        Files.move(
            path2, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
        Files.move(path2, path, StandardCopyOption.REPLACE_EXISTING);
      }
    } finally {
      Files.deleteIfExists(path2);
    }
  }

  private static boolean mfmdn12504wg(String string, Path path) {
    return (pattern.matcher(path.getFileName().toString()).matches()
                && !path.getFileName().toString().equals(string)
            ? 1
            : 0)
        != 0;
  }

  public record Selection(
      String kind,
      String reference,
      String title,
      String page,
      String credit,
      CapePreviewRenderer.Style style) {}
}
