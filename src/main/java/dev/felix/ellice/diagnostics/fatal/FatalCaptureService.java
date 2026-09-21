package dev.felix.ellice.diagnostics.fatal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.CallSite;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;

public final class FatalCaptureService {
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneOffset.UTC);
  private static final int count = 280;
  private static final int count2 = 16;
  private final String text;
  private final Instant instant;
  private final Kind kind2;
  private final String fifurl5vhowx;
  private final String text3;
  private final String text4;
  private final String text5;
  private final String text6;
  private final Path path;
  private final String[] text7;

  private FatalCaptureService(
      String string,
      Instant instant,
      Kind kind,
      String string2,
      String string3,
      String string4,
      String string5,
      String string6,
      Path path,
      String[] stringArray) {
    this.text = string;
    this.instant = instant;
    this.kind2 = kind;
    this.fifurl5vhowx = string2;
    this.text3 = string3;
    this.text4 = string4;
    this.text5 = string5;
    this.text6 = string6;
    this.path = path;
    this.text7 = stringArray;
  }

  public static FatalCaptureService capture(Throwable throwable, String string, Path path) {
    return FatalCaptureService.capture(
        throwable, string, FatalCaptureService.minecraftVersion(), Kind.CLIENT, path);
  }

  public static FatalCaptureService capture(
      Throwable throwable, String string, String string2, Kind kind, Path path) {
    Instant instant = Instant.now();
    String string3 = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    Kind kind2 = kind == null ? Kind.CLIENT : kind;
    Throwable throwable2 = FatalCaptureService.majqccoioetv(throwable);
    String string4 = kind2.title();
    String string5 = throwable2 == null ? "UnknownError" : throwable2.getClass().getName();
    String string6 =
        throwable2 == null || throwable2.getMessage() == null || throwable2.getMessage().isBlank()
            ? "No message"
            : throwable2.getMessage().replace('\t', ' ');
    String string7 = string5 + ": " + FatalCaptureService.mcuwphricvef(string6, 320);
    String string8 = FatalCaptureService.createText4(throwable);
    String string9 = Thread.currentThread().getName();
    String string10 = System.getProperty("java.version", "?");
    String string11 =
        System.getProperty("os.name", "?") + " " + System.getProperty("os.version", "");
    String[] stringArray =
        FatalCaptureService.createText3(
            string3, string, string2, string10, string11, string9, throwable2);
    String string12 =
        FatalCaptureService.createText(
            string3, instant, string, string2, string10, string11, string9, kind2, string7,
            string8);
    String string13 = FatalCaptureService.mienvdab5omw(string3, string, string2, string7);
    return new FatalCaptureService(
        string3, instant, kind2, string4, string7, string8, string12, string13, path, stringArray);
  }

  public String id() {
    return this.text;
  }

  public Instant at() {
    return this.instant;
  }

  public Kind kind() {
    return this.kind2;
  }

  public String title() {
    return this.fifurl5vhowx;
  }

  public String summary() {
    return this.text3;
  }

  public String details() {
    return this.text4;
  }

  public String clipboardText() {
    return this.text5;
  }

  public String qrPayload() {
    return this.text6;
  }

  public Path dumpFile() {
    return this.path;
  }

  public String[] screenLines() {
    return this.text7;
  }

  public boolean allowsContinue() {
    return this.kind2 != Kind.PROCESS;
  }

  public String guidance() {
    return this.kind2.guidance();
  }

  public String exceptionType() {
    return this.text3.substring(0, this.text3.indexOf(": "));
  }

  public String message() {
    return this.text3.substring(this.text3.indexOf(": ") + 2);
  }

  public String fileStamp() {
    return dateTimeFormatter.format(this.instant);
  }

  public String dumpName() {
    return "crash-" + this.fileStamp() + "-" + this.text + ".txt";
  }

  FatalCaptureService withDumpFile(Path path) {
    return new FatalCaptureService(
        this.text,
        this.instant,
        this.kind2,
        this.fifurl5vhowx,
        this.text3,
        this.text4,
        this.text5,
        this.text6,
        path,
        this.text7);
  }

  private static String createText(
      String string,
      Instant instant,
      String string2,
      String string3,
      String string4,
      String string5,
      String string6,
      Kind kind,
      String string7,
      String string8) {
    return "ellice fatal %s crash\nid: %s\ntime: %s\nellice: %s\nminecraft: %s\njava: %s\nos: %s\nthread: %s\n\n%s\n\n%s\n"
            .formatted(
                kind.name().toLowerCase(Locale.ROOT),
                string,
                instant.toString(),
                string2 == null ? "?" : string2,
                string3 == null ? "?" : string3,
                string4,
                string5,
                string6,
                string7,
                string8)
            .trim()
        + "\n";
  }

  private static String mienvdab5omw(
      String string, String string2, String string3, String string4) {
    return FatalCaptureService.mcuwphricvef(
        "ellice crash\nid: " + string + "\nv: " + string2 + "\nmc: " + string3 + "\n" + string4,
        280);
  }

  private static String[] createText3(
      String string,
      String string2,
      String string3,
      String string4,
      String string5,
      String string6,
      Throwable throwable) {
    ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
    arrayList.add((CallSite) ((Object) ("id " + string)));
    arrayList.add(
        (CallSite)
            ((Object)
                ("ellice "
                    + FatalCaptureService.createText5(string2)
                    + "   mc "
                    + FatalCaptureService.createText5(string3)
                    + "   java "
                    + string4)));
    arrayList.add((CallSite) ((Object) ("os " + string5.trim() + "   thread " + string6)));
    if (throwable != null && throwable.getStackTrace() != null) {
      int n = Math.min(16, throwable.getStackTrace().length);
      for (int i = 0; i < n; ++i) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
        arrayList.add(
            (CallSite)
                ((Object)
                    ("at "
                        + stackTraceElement.getClassName()
                        + "."
                        + stackTraceElement.getMethodName()
                        + "("
                        + stackTraceElement.getFileName()
                        + ":"
                        + stackTraceElement.getLineNumber()
                        + ")")));
      }
    }
    return (String[]) arrayList.toArray(String[]::new);
  }

  private static String createText4(Throwable throwable) {
    if (throwable == null) {
      return "";
    }
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  private static Throwable majqccoioetv(Throwable throwable) {
    Throwable throwable2 = throwable;
    for (int n = 0;
        throwable2 != null
            && throwable2.getCause() != null
            && throwable2.getCause() != throwable2
            && n < 8;
        throwable2 = throwable2.getCause(), ++n) {}
    return throwable2;
  }

  static String minecraftVersion() {
    try {
      return FabricLoader.getInstance()
          .getModContainer("minecraft")
          .map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString())
          .orElse("?");
    } catch (Throwable throwable) {
      return "?";
    }
  }

  private static String createText5(String string) {
    return string == null || string.isBlank() ? "?" : string;
  }

  private static String mcuwphricvef(String string, int n) {
    if (string.length() <= n) {
      return string;
    }
    return string.substring(0, Math.max(0, n - 1)) + "\u2026";
  }

  public static enum Kind {
    CLIENT(
        "Client error",
        "Something went wrong",
        "ellice has been disabled for this session. You can continue in Minecraft."),
    STARTUP(
        "Startup error",
        "ellice could not start",
        "Something failed during startup. Restart the game after checking the report."),
    LICENSE(
        "Account access",
        "Access needs attention",
        "ellice is unavailable. Resolve the issue below in the launcher, then restart."),
    RENDERER(
        "Rendering error",
        "Rendering interrupted",
        "ellice has been disabled for this session. You can continue in Minecraft."),
    PROCESS(
        "Game crash",
        "Minecraft stopped",
        "The game cannot continue. Copy the report before closing Minecraft.");

    private final String text8;
    private final String text9;
    private final String fgkby2m9uaxp;

    private Kind(String string2, String string3, String string4) {
      this.text8 = string2;
      this.text9 = string3;
      this.fgkby2m9uaxp = string4;
    }

    public String label() {
      return this.text8;
    }

    public String title() {
      return this.text9;
    }

    public String guidance() {
      return this.fgkby2m9uaxp;
    }
  }
}
