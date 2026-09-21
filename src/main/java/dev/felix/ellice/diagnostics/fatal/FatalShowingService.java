package dev.felix.ellice.diagnostics.fatal;

import dev.felix.ellice.compat.CompatOpenUriService;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FatalShowingService {
  private static final Logger f6uw9b3soq6j;
  private static final FatalScreenRenderer fb4rhn20j44o;
  private static volatile boolean enabled;
  private static volatile boolean enabled2;
  private static String fiw56wjq9zjt;
  private static int count;
  private static int count2;
  private static int f315amczcvo1;
  private static List<FatalReportLayout.Action> f53ww8rdow6p;
  private static boolean enabled3;
  private static boolean enabled4;

  private FatalShowingService() {}

  public static boolean showing() {
    return FatalIsTrippedService.isTripped() && !FatalShowingService.enabled;
  }

  public static boolean prepare() {
    final long glfwGetCurrentContext = GLFW.glfwGetCurrentContext();
    if (glfwGetCurrentContext == 0L) {
      return false;
    }
    final int[] array = {0};
    final int[] array2 = {0};
    GLFW.glfwGetFramebufferSize(glfwGetCurrentContext, array, array2);
    return array[0] > 0 && array2[0] > 0 && FatalShowingService.fb4rhn20j44o.ensure();
  }

  public static boolean canPresent() {
    try {
      final Minecraft instance = Minecraft.getInstance();
      return instance != null
          && instance.isGameLoadFinished()
          && instance.getOverlay() == null
          && prepare();
    } catch (final Throwable t) {
      return false;
    }
  }

  public static void present() {
    if (!showing()) {
      return;
    }
    try {
      final long glfwGetCurrentContext = GLFW.glfwGetCurrentContext();
      if (glfwGetCurrentContext == 0L) {
        return;
      }
      final int[] array = {0};
      final int[] array2 = {0};
      GLFW.glfwGetFramebufferSize(glfwGetCurrentContext, array, array2);
      if (array[0] <= 0 || array2[0] <= 0) {
        return;
      }
      m2biv6173qa(glfwGetCurrentContext);
      final int calculateValue = calculateValue(glfwGetCurrentContext, array[0], array2[0]);
      updateState2(glfwGetCurrentContext, calculateValue);
      final FatalCaptureService crash = FatalIsTrippedService.crash();
      if (crash == null || !showing()) {
        return;
      }
      if (!FatalShowingService.fb4rhn20j44o.ensure()) {
        FatalScreenRenderer.fallbackClear();
        return;
      }
      if (checkCondition(crash, array[0], array2[0], calculateValue)) {
        final FatalReportLayout.Frame paint =
            FatalReportLayout.paint(
                crash, array[0], array2[0], calculateValue, FatalShowingService.enabled3);
        final FatalScreenCanvas canvas = paint.canvas();
        FatalShowingService.f53ww8rdow6p = paint.actions();
        FatalShowingService.fb4rhn20j44o.upload(canvas.px, canvas.w, canvas.h);
        FatalShowingService.fiw56wjq9zjt = crash.id();
        FatalShowingService.count = array[0];
        FatalShowingService.count2 = array2[0];
        FatalShowingService.f315amczcvo1 = calculateValue;
      }
      FatalShowingService.fb4rhn20j44o.blit(array[0], array2[0]);
    } catch (final Throwable t) {
      FatalShowingService.f6uw9b3soq6j.error("Isolated crash overlay failed", t);
      FatalScreenRenderer.fallbackClear();
    }
  }

  public static boolean consumeKey(final long n, final int n2, final int n3) {
    if (!showing()) {
      return false;
    }
    if (n3 == 1) {
      updateState3(n, n2);
    }
    return true;
  }

  static void resetForTests() {
    FatalShowingService.enabled = false;
    FatalShowingService.enabled2 = false;
    FatalShowingService.fiw56wjq9zjt = "";
    FatalShowingService.count = 0;
    FatalShowingService.count2 = 0;
    FatalShowingService.f315amczcvo1 = -1;
    FatalShowingService.enabled3 = false;
    FatalShowingService.f53ww8rdow6p = List.of();
    FatalShowingService.enabled4 = false;
  }

  private static boolean checkCondition(
      final FatalCaptureService fatalCaptureService, final int n, final int n2, final int n3) {
    return !fatalCaptureService.id().equals(FatalShowingService.fiw56wjq9zjt)
        || FatalShowingService.count != n
        || FatalShowingService.count2 != n2
        || FatalShowingService.f315amczcvo1 != n3;
  }

  private static void m2biv6173qa(final long n) {
    if (FatalShowingService.enabled2) {
      return;
    }
    GLFW.glfwSetInputMode(n, 208897, 212993);
    try {
      final Minecraft instance = Minecraft.getInstance();
      if (instance != null && instance.mouseHandler != null) {
        instance.mouseHandler.releaseMouse();
      }
    } catch (final Throwable t) {
    }
    FatalShowingService.enabled2 = true;
  }

  private static int calculateValue(final long n, final int n2, final int n3) {
    final int[] array = {0};
    final int[] array2 = {0};
    GLFW.glfwGetWindowSize(n, array, array2);
    final double[] array3 = {0.0};
    final double[] array4 = {0.0};
    GLFW.glfwGetCursorPos(n, array3, array4);
    if (array[0] <= 0 || array2[0] <= 0) {
      return -1;
    }
    final float n4 = (float) (array3[0] * n2 / array[0]);
    final float n5 = (float) (array4[0] * n3 / array2[0]);
    for (final FatalReportLayout.Action action : FatalShowingService.f53ww8rdow6p) {
      if (n4 >= action.x()
          && n5 >= action.y()
          && n4 < action.x() + action.w()
          && n5 < action.y() + action.h()) {
        return action.id();
      }
    }
    return -1;
  }

  private static void updateState2(final long n, final int n2) {
    final boolean enabled4 = GLFW.glfwGetMouseButton(n, 0) == 1;
    if (enabled4 && !FatalShowingService.enabled4 && n2 >= 0) {
      updateState4(n, n2);
    }
    FatalShowingService.enabled4 = enabled4;
  }

  private static void updateState3(final long n, final int n2) {
    switch (n2) {
      case 256:
        {
          updateState4(n, 0);
          break;
        }
      case 67:
        {
          updateState4(n, 1);
          break;
        }
      case 79:
        {
          updateState4(n, 2);
          break;
        }
      case 81:
        {
          updateState4(n, 3);
          break;
        }
    }
  }

  private static void updateState4(final long n, final int n2) {
    final FatalCaptureService crash = FatalIsTrippedService.crash();
    switch (n2) {
      case 0:
        {
          if (crash != null && crash.allowsContinue() && !FatalIsTrippedService.isProcessCrash()) {
            FatalShowingService.enabled = true;
            break;
          }
          break;
        }
      case 1:
        {
          updateState5(n, crash);
          break;
        }
      case 2:
        {
          updateState6(crash);
          break;
        }
      case 3:
        {
          updateState7();
          break;
        }
    }
  }

  private static void updateState5(final long n, final FatalCaptureService fatalCaptureService) {
    if (fatalCaptureService == null) {
      return;
    }
    try {
      GLFW.glfwSetClipboardString(n, (CharSequence) fatalCaptureService.clipboardText());
      FatalShowingService.enabled3 = true;
      FatalShowingService.fiw56wjq9zjt = "";
    } catch (final Throwable t) {
      FatalShowingService.f6uw9b3soq6j.warn("Could not copy ellice crash report", t);
    }
  }

  private static void updateState6(final FatalCaptureService fatalCaptureService) {
    if (fatalCaptureService == null) {
      return;
    }
    final Path dumpFile = fatalCaptureService.dumpFile();
    if (dumpFile != null) {
      CompatOpenUriService.openPath(
          (dumpFile.getParent() == null) ? dumpFile : dumpFile.getParent());
    }
  }

  private static void updateState7() {
    try {
      final Minecraft instance = Minecraft.getInstance();
      if (instance != null) {
        instance.stop();
      }
    } catch (final Throwable t) {
      FatalShowingService.f6uw9b3soq6j.warn("Could not stop Minecraft from the crash overlay", t);
    }
  }

  static {
    f6uw9b3soq6j = LoggerFactory.getLogger("ellice-fatal");
    fb4rhn20j44o = new FatalScreenRenderer();
    FatalShowingService.fiw56wjq9zjt = "";
    FatalShowingService.f315amczcvo1 = -1;
    FatalShowingService.f53ww8rdow6p = List.of();
  }
}
