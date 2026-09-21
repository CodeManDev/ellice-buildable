package dev.felix.ellice.diagnostics.fatal;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FatalIsTrippedService {
  public static final boolean FORCE_DEMO_CRASH = false;
  private static final Logger logger = LoggerFactory.getLogger("ellice-fatal");
  private static final AtomicReference<FatalCaptureService> atomicReference =
      new AtomicReference<>();
  private static final AtomicBoolean atomicBoolean = new AtomicBoolean();

  private FatalIsTrippedService() {}

  public static boolean isTripped() {
    return atomicReference.get() != null;
  }

  public static boolean isProcessCrash() {
    return atomicBoolean.get();
  }

  public static FatalCaptureService crash() {
    return atomicReference.get();
  }

  public static boolean trip(Throwable exception) {
    return trip(exception, FatalCaptureService.Kind.CLIENT, createPath());
  }

  public static boolean trip(Throwable exception, FatalCaptureService.Kind kind) {
    return trip(exception, kind, createPath());
  }

  public static boolean tripProcess(Throwable exception) {
    atomicBoolean.set(true);
    return trip(exception, FatalCaptureService.Kind.PROCESS, createPath());
  }

  public static boolean shouldDemoCrash() {
    return false;
  }

  public static RuntimeException demoShaderFailure() {
    return new RuntimeException(
        "Shader compilation failed (FRAGMENT):\nERROR: 0:41: 'uResolution' : undeclared identifier\nERROR: 0:41: '' : compilation terminated\nFailed to load shader: /assets/ellice/shaders/ellice/sdf_rect.frag\n"
            .trim());
  }

  static boolean trip(Throwable exception, Path path) {
    return trip(exception, FatalCaptureService.Kind.CLIENT, path);
  }

  static boolean trip(Throwable exception, FatalCaptureService.Kind currentKind, Path path) {
    if (exception == null) {
      return false;
    }

    if (currentKind == FatalCaptureService.Kind.PROCESS) {
      atomicBoolean.set(true);
    }

    if (atomicReference.get() != null) {
      return false;
    }

    try {
      String text = createText();
      String currentText = FatalCaptureService.minecraftVersion();
      FatalCaptureService fatalCapture =
          FatalCaptureService.capture(exception, text, currentText, currentKind, null);
      if (path != null) {
        try {
          Files.createDirectories(path);
          Path currentPath = path.resolve(fatalCapture.dumpName());
          Files.writeString(currentPath, fatalCapture.clipboardText());
          fatalCapture = fatalCapture.withDumpFile(currentPath);
        } catch (Throwable currentException) {
          logger.warn("Could not write ellice crash dump", currentException);
        }
      }

      if (!atomicReference.compareAndSet(null, fatalCapture)) {
        return false;
      }

      logger.error(
          "ellice fatal {} crash (id {})",
          new Object[] {
            fatalCapture.kind().name().toLowerCase(Locale.ROOT), fatalCapture.id(), exception
          });
      return true;
    } catch (Throwable nextException) {
      logger.error("Failed to record ellice crash", nextException);
      FatalCaptureService currentFatalCapture =
          FatalCaptureService.capture(exception, "?", "?", currentKind, null);
      return atomicReference.compareAndSet(null, currentFatalCapture);
    }
  }

  static void resetForTests() {
    atomicReference.set(null);
    atomicBoolean.set(false);
  }

  private static Path createPath() {
    try {
      return FabricLoader.getInstance().getGameDir().resolve("ellice-ui/crashes");
    } catch (Throwable exception) {
      try {
        return Path.of(System.getProperty("java.io.tmpdir", "."), "ellice-crashes");
      } catch (Throwable currentException) {
        return null;
      }
    }
  }

  private static String createText() {
    try {
      return CoreIsInitializedHandler.VERSION;
    } catch (Throwable exception) {
      return "?";
    }
  }
}
