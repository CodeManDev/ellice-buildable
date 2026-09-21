





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
    private static final DateTimeFormatter fiw60u3110oq = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneOffset.UTC);
    private static final int f3u3nx5puawe = 280;
    private static final int fhs67wjy0g92 = 16;
    private final String fiez6v06j7tb;
    private final Instant fez6wcs15aop;
    private final Kind f9q3cwj5bj9v;
    private final String fifurl5vhowx;
    private final String fbb9tktk0np2;
    private final String f7a27u1vg6bq;
    private final String fefjw8zkcx49;
    private final String f8ltnyfzfp3l;
    private final Path f1gi2xx0f7go;
    private final String[] farqqqv0y8io;

    private FatalCaptureService(String string, Instant instant, Kind kind, String string2, String string3, String string4, String string5, String string6, Path path, String[] stringArray) {
        this.fiez6v06j7tb = string;
        this.fez6wcs15aop = instant;
        this.f9q3cwj5bj9v = kind;
        this.fifurl5vhowx = string2;
        this.fbb9tktk0np2 = string3;
        this.f7a27u1vg6bq = string4;
        this.fefjw8zkcx49 = string5;
        this.f8ltnyfzfp3l = string6;
        this.f1gi2xx0f7go = path;
        this.farqqqv0y8io = stringArray;
    }

    public static FatalCaptureService capture(Throwable throwable, String string, Path path) {
        return FatalCaptureService.capture(throwable, string, FatalCaptureService.minecraftVersion(), Kind.CLIENT, path);
    }

    public static FatalCaptureService capture(Throwable throwable, String string, String string2, Kind kind, Path path) {
        Instant instant = Instant.now();
        String string3 = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Kind kind2 = kind == null ? Kind.CLIENT : kind;
        Throwable throwable2 = FatalCaptureService.majqccoioetv(throwable);
        String string4 = kind2.title();
        String string5 = throwable2 == null ? "UnknownError" : throwable2.getClass().getName();
        String string6 = throwable2 == null || throwable2.getMessage() == null || throwable2.getMessage().isBlank() ? "No message" : throwable2.getMessage().replace('\t', ' ');
        String string7 = string5 + ": " + FatalCaptureService.mcuwphricvef(string6, 320);
        String string8 = FatalCaptureService.me4s84dxhxuu(throwable);
        String string9 = Thread.currentThread().getName();
        String string10 = System.getProperty("java.version", "?");
        String string11 = System.getProperty("os.name", "?") + " " + System.getProperty("os.version", "");
        String[] stringArray = FatalCaptureService.mggne9b4qw6z(string3, string, string2, string10, string11, string9, throwable2);
        String string12 = FatalCaptureService.m436dhq6d686(string3, instant, string, string2, string10, string11, string9, kind2, string7, string8);
        String string13 = FatalCaptureService.mienvdab5omw(string3, string, string2, string7);
        return new FatalCaptureService(string3, instant, kind2, string4, string7, string8, string12, string13, path, stringArray);
    }

    public String id() {
        return this.fiez6v06j7tb;
    }

    public Instant at() {
        return this.fez6wcs15aop;
    }

    public Kind kind() {
        return this.f9q3cwj5bj9v;
    }

    public String title() {
        return this.fifurl5vhowx;
    }

    public String summary() {
        return this.fbb9tktk0np2;
    }

    public String details() {
        return this.f7a27u1vg6bq;
    }

    public String clipboardText() {
        return this.fefjw8zkcx49;
    }

    public String qrPayload() {
        return this.f8ltnyfzfp3l;
    }

    public Path dumpFile() {
        return this.f1gi2xx0f7go;
    }

    public String[] screenLines() {
        return this.farqqqv0y8io;
    }

    public boolean allowsContinue() {
        return this.f9q3cwj5bj9v != Kind.PROCESS;
    }

    public String guidance() {
        return this.f9q3cwj5bj9v.guidance();
    }

    public String exceptionType() {
        return this.fbb9tktk0np2.substring(0, this.fbb9tktk0np2.indexOf(": "));
    }

    public String message() {
        return this.fbb9tktk0np2.substring(this.fbb9tktk0np2.indexOf(": ") + 2);
    }

    public String fileStamp() {
        return fiw60u3110oq.format(this.fez6wcs15aop);
    }

    public String dumpName() {
        return "crash-" + this.fileStamp() + "-" + this.fiez6v06j7tb + ".txt";
    }

    FatalCaptureService withDumpFile(Path path) {
        return new FatalCaptureService(this.fiez6v06j7tb, this.fez6wcs15aop, this.f9q3cwj5bj9v, this.fifurl5vhowx, this.fbb9tktk0np2, this.f7a27u1vg6bq, this.fefjw8zkcx49, this.f8ltnyfzfp3l, path, this.farqqqv0y8io);
    }

    private static String m436dhq6d686(String string, Instant instant, String string2, String string3, String string4, String string5, String string6, Kind kind, String string7, String string8) {
        return "ellice fatal %s crash\nid: %s\ntime: %s\nellice: %s\nminecraft: %s\njava: %s\nos: %s\nthread: %s\n\n%s\n\n%s\n".formatted(kind.name().toLowerCase(Locale.ROOT), string, instant.toString(), string2 == null ? "?" : string2, string3 == null ? "?" : string3, string4, string5, string6, string7, string8).trim() + "\n";
    }

    private static String mienvdab5omw(String string, String string2, String string3, String string4) {
        return FatalCaptureService.mcuwphricvef("ellice crash\nid: " + string + "\nv: " + string2 + "\nmc: " + string3 + "\n" + string4, 280);
    }

    private static String[] mggne9b4qw6z(String string, String string2, String string3, String string4, String string5, String string6, Throwable throwable) {
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        arrayList.add((CallSite)((Object)("id " + string)));
        arrayList.add((CallSite)((Object)("ellice " + FatalCaptureService.mgfy6vll98we(string2) + "   mc " + FatalCaptureService.mgfy6vll98we(string3) + "   java " + string4)));
        arrayList.add((CallSite)((Object)("os " + string5.trim() + "   thread " + string6)));
        if (throwable != null && throwable.getStackTrace() != null) {
            int n = Math.min(16, throwable.getStackTrace().length);
            for (int i = 0; i < n; ++i) {
                StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
                arrayList.add((CallSite)((Object)("at " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")")));
            }
        }
        return (String[])arrayList.toArray(String[]::new);
    }

    private static String me4s84dxhxuu(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private static Throwable majqccoioetv(Throwable throwable) {
        Throwable throwable2 = throwable;
        for (int n = 0; throwable2 != null && throwable2.getCause() != null && throwable2.getCause() != throwable2 && n < 8; throwable2 = throwable2.getCause(), ++n) {
        }
        return throwable2;
    }

    static String minecraftVersion() {
        try {
            return FabricLoader.getInstance().getModContainer("minecraft").map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString()).orElse("?");
        }
        catch (Throwable throwable) {
            return "?";
        }
    }

    private static String mgfy6vll98we(String string) {
        return string == null || string.isBlank() ? "?" : string;
    }

    private static String mcuwphricvef(String string, int n) {
        if (string.length() <= n) {
            return string;
        }
        return string.substring(0, Math.max(0, n - 1)) + "\u2026";
    }

    public static enum Kind {
        CLIENT("Client error", "Something went wrong", "ellice has been disabled for this session. You can continue in Minecraft."),
        STARTUP("Startup error", "ellice could not start", "Something failed during startup. Restart the game after checking the report."),
        LICENSE("Account access", "Access needs attention", "ellice is unavailable. Resolve the issue below in the launcher, then restart."),
        RENDERER("Rendering error", "Rendering interrupted", "ellice has been disabled for this session. You can continue in Minecraft."),
        PROCESS("Game crash", "Minecraft stopped", "The game cannot continue. Copy the report before closing Minecraft.");

        private final String f3wrhb0jzxii;
        private final String ffrw1blwu0zg;
        private final String fgkby2m9uaxp;

        private Kind(String string2, String string3, String string4) {
            this.f3wrhb0jzxii = string2;
            this.ffrw1blwu0zg = string3;
            this.fgkby2m9uaxp = string4;
        }

        public String label() {
            return this.f3wrhb0jzxii;
        }

        public String title() {
            return this.ffrw1blwu0zg;
        }

        public String guidance() {
            return this.fgkby2m9uaxp;
        }
    }
}

