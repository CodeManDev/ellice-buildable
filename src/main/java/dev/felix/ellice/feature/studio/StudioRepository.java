package dev.felix.ellice.feature.studio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class StudioRepository {
  private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
  private final Path fhcoxazgddvx;
  private final List<String> text = new ArrayList<String>();

  public StudioRepository(Path path) {
    this.fhcoxazgddvx = path.resolve("ellice-module-studio");
  }

  public Path directory() {
    return this.fhcoxazgddvx;
  }

  public List<String> warnings() {
    return List.copyOf(this.text);
  }

  public List<Document> load() {
    this.text.clear();
    ArrayList<Document> arrayList = new ArrayList<Document>();
    if (!Files.isDirectory(this.fhcoxazgddvx, new LinkOption[0])) {
      return arrayList;
    }
    try (Stream<Path> stream = Files.list(this.fhcoxazgddvx); ) {
      for (Path path2 :
          stream
              .filter(path -> path.getFileName().toString().endsWith(".json"))
              .sorted()
              .limit(128L)
              .toList()) {
        try {
          if (Files.size(path2) > 524288L) {
            throw new IOException("Document exceeds 512 KB");
          }
          Document document = (Document) gson2.fromJson(Files.readString(path2), Document.class);
          StudioRepository.updateState(document.draft, false);
          if (document.published != null) {
            StudioRepository.updateState(document.published, true);
          }
          if (document.published != null && !document.published.id.equals(document.draft.id)) {
            throw new IOException("Project identifiers do not match");
          }
          if (!path2.getFileName().toString().equals(document.draft.id + ".json")) {
            throw new IOException("Project filename does not match its identifier");
          }
          arrayList.add(document);
        } catch (Exception exception) {
          this.text.add(String.valueOf(path2.getFileName()) + ": " + exception.getMessage());
        }
      }
    } catch (IOException iOException) {
      this.text.add(iOException.getMessage());
    }
    return arrayList;
  }

  public void save(Document document) throws IOException {
    StudioRepository.updateState(document.draft, false);
    if (document.published != null) {
      StudioRepository.updateState(document.published, true);
    }
    if (document.published != null && !document.published.id.equals(document.draft.id)) {
      throw new IOException("Project identifiers do not match");
    }
    StudioRepository.updateState2(
        this.createPath(document.draft.id), gson2.toJson((Object) document));
  }

  public Path exportProject(StudioShapeService studioShapeService) throws IOException {
    StudioRepository.updateState(studioShapeService, false);
    Path path =
        this.fhcoxazgddvx.resolve("exports").resolve(studioShapeService.id + ".ellice-module.json");
    StudioRepository.updateState2(path, gson2.toJson((Object) studioShapeService));
    return path;
  }

  public StudioShapeService importProject(Path path) throws IOException {
    StudioShapeService studioShapeService;
    if (!Files.isRegularFile(path, new LinkOption[0]) || Files.size(path) > 262144L) {
      throw new IOException("Choose a project file smaller than 256 KB.");
    }
    try {
      studioShapeService =
          (StudioShapeService) gson2.fromJson(Files.readString(path), StudioShapeService.class);
      StudioRepository.updateState(studioShapeService, false);
    } catch (RuntimeException runtimeException) {
      throw new IOException("This is not a valid Module Studio project.", runtimeException);
    }
    studioShapeService.id = UUID.randomUUID().toString();
    return studioShapeService;
  }

  private Path createPath(String string) {
    return this.fhcoxazgddvx.resolve(String.valueOf(UUID.fromString(string)) + ".json");
  }

  private static void updateState(StudioShapeService studioShapeService, boolean bl)
      throws IOException {
    List<String> list = StudioValidateValidator.validate(studioShapeService, bl);
    if (!list.isEmpty()) {
      throw new IOException(list.getFirst());
    }
  }

  private static void updateState2(Path path, String string) throws IOException {
    Files.createDirectories(path.getParent(), new FileAttribute[0]);
    Path path2 = Files.createTempFile(path.getParent(), ".studio-", ".tmp", new FileAttribute[0]);
    try {
      Files.writeString(path2, (CharSequence) string, new OpenOption[0]);
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

  public record Document(StudioShapeService draft, StudioShapeService published) {}
}
