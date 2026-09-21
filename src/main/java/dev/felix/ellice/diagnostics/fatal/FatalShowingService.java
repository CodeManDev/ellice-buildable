



package dev.felix.ellice.diagnostics.fatal;

import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import dev.felix.ellice.compat.CompatOpenUriService;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import java.util.List;
import org.slf4j.Logger;

public final class FatalShowingService
{
    private static final Logger f6uw9b3soq6j;
    private static final FatalScreenRenderer fb4rhn20j44o;
    private static volatile boolean f3s1kydpe7xf;
    private static volatile boolean f25hmo6ybal8;
    private static String fiw56wjq9zjt;
    private static int fb7kq3ydoqmk;
    private static int f2xs4ag9wy73;
    private static int f315amczcvo1;
    private static List<FatalReportLayout.Action> f53ww8rdow6p;
    private static boolean f9fhhdejq7bs;
    private static boolean f9jd05nwa7rc;
    
    private FatalShowingService() {
    }
    
    public static boolean showing() {
        return FatalIsTrippedService.isTripped() && !FatalShowingService.f3s1kydpe7xf;
    }
    
    public static boolean prepare() {
        final long glfwGetCurrentContext = GLFW.glfwGetCurrentContext();
        if (glfwGetCurrentContext == 0L) {
            return false;
        }
        final int[] array = { 0 };
        final int[] array2 = { 0 };
        GLFW.glfwGetFramebufferSize(glfwGetCurrentContext, array, array2);
        return array[0] > 0 && array2[0] > 0 && FatalShowingService.fb4rhn20j44o.ensure();
    }
    
    public static boolean canPresent() {
        try {
            final Minecraft instance = Minecraft.getInstance();
            return instance != null && instance.isGameLoadFinished() && instance.getOverlay() == null && prepare();
        }
        catch (final Throwable t) {
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
            final int[] array = { 0 };
            final int[] array2 = { 0 };
            GLFW.glfwGetFramebufferSize(glfwGetCurrentContext, array, array2);
            if (array[0] <= 0 || array2[0] <= 0) {
                return;
            }
            m2biv6173qa(glfwGetCurrentContext);
            final int mcew379odikb = mcew379odikb(glfwGetCurrentContext, array[0], array2[0]);
            mdo0r8do2xum(glfwGetCurrentContext, mcew379odikb);
            final FatalCaptureService crash = FatalIsTrippedService.crash();
            if (crash == null || !showing()) {
                return;
            }
            if (!FatalShowingService.fb4rhn20j44o.ensure()) {
                FatalScreenRenderer.fallbackClear();
                return;
            }
            if (m3xt61sxr93u(crash, array[0], array2[0], mcew379odikb)) {
                final FatalReportLayout.Frame paint = FatalReportLayout.paint(crash, array[0], array2[0], mcew379odikb, FatalShowingService.f9fhhdejq7bs);
                final FatalScreenCanvas canvas = paint.canvas();
                FatalShowingService.f53ww8rdow6p = paint.actions();
                FatalShowingService.fb4rhn20j44o.upload(canvas.px, canvas.w, canvas.h);
                FatalShowingService.fiw56wjq9zjt = crash.id();
                FatalShowingService.fb7kq3ydoqmk = array[0];
                FatalShowingService.f2xs4ag9wy73 = array2[0];
                FatalShowingService.f315amczcvo1 = mcew379odikb;
            }
            FatalShowingService.fb4rhn20j44o.blit(array[0], array2[0]);
        }
        catch (final Throwable t) {
            FatalShowingService.f6uw9b3soq6j.error("Isolated crash overlay failed", t);
            FatalScreenRenderer.fallbackClear();
        }
    }
    
    public static boolean consumeKey(final long n, final int n2, final int n3) {
        if (!showing()) {
            return false;
        }
        if (n3 == 1) {
            meg680glfpfg(n, n2);
        }
        return true;
    }
    
    static void resetForTests() {
        FatalShowingService.f3s1kydpe7xf = false;
        FatalShowingService.f25hmo6ybal8 = false;
        FatalShowingService.fiw56wjq9zjt = "";
        FatalShowingService.fb7kq3ydoqmk = 0;
        FatalShowingService.f2xs4ag9wy73 = 0;
        FatalShowingService.f315amczcvo1 = -1;
        FatalShowingService.f9fhhdejq7bs = false;
        FatalShowingService.f53ww8rdow6p = List.of();
        FatalShowingService.f9jd05nwa7rc = false;
    }
    
    private static boolean m3xt61sxr93u(final FatalCaptureService fatalCaptureService, final int n, final int n2, final int n3) {
        return !fatalCaptureService.id().equals(FatalShowingService.fiw56wjq9zjt) || FatalShowingService.fb7kq3ydoqmk != n || FatalShowingService.f2xs4ag9wy73 != n2 || FatalShowingService.f315amczcvo1 != n3;
    }
    
    private static void m2biv6173qa(final long n) {
        if (FatalShowingService.f25hmo6ybal8) {
            return;
        }
        GLFW.glfwSetInputMode(n, 208897, 212993);
        try {
            final Minecraft instance = Minecraft.getInstance();
            if (instance != null && instance.mouseHandler != null) {
                instance.mouseHandler.releaseMouse();
            }
        }
        catch (final Throwable t) {}
        FatalShowingService.f25hmo6ybal8 = true;
    }
    
    private static int mcew379odikb(final long n, final int n2, final int n3) {
        final int[] array = { 0 };
        final int[] array2 = { 0 };
        GLFW.glfwGetWindowSize(n, array, array2);
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        GLFW.glfwGetCursorPos(n, array3, array4);
        if (array[0] <= 0 || array2[0] <= 0) {
            return -1;
        }
        final float n4 = (float)(array3[0] * n2 / array[0]);
        final float n5 = (float)(array4[0] * n3 / array2[0]);
        for (final FatalReportLayout.Action action : FatalShowingService.f53ww8rdow6p) {
            if (n4 >= action.x() && n5 >= action.y() && n4 < action.x() + action.w() && n5 < action.y() + action.h()) {
                return action.id();
            }
        }
        return -1;
    }
    
    private static void mdo0r8do2xum(final long n, final int n2) {
        final boolean f9jd05nwa7rc = GLFW.glfwGetMouseButton(n, 0) == 1;
        if (f9jd05nwa7rc && !FatalShowingService.f9jd05nwa7rc && n2 >= 0) {
            m3lhqobulz6e(n, n2);
        }
        FatalShowingService.f9jd05nwa7rc = f9jd05nwa7rc;
    }
    
    private static void meg680glfpfg(final long n, final int n2) {
        switch (n2) {
            case 256: {
                m3lhqobulz6e(n, 0);
                break;
            }
            case 67: {
                m3lhqobulz6e(n, 1);
                break;
            }
            case 79: {
                m3lhqobulz6e(n, 2);
                break;
            }
            case 81: {
                m3lhqobulz6e(n, 3);
                break;
            }
        }
    }
    
    private static void m3lhqobulz6e(final long n, final int n2) {
        final FatalCaptureService crash = FatalIsTrippedService.crash();
        switch (n2) {
            case 0: {
                if (crash != null && crash.allowsContinue() && !FatalIsTrippedService.isProcessCrash()) {
                    FatalShowingService.f3s1kydpe7xf = true;
                    break;
                }
                break;
            }
            case 1: {
                m25qrakvfyqk(n, crash);
                break;
            }
            case 2: {
                m2l5vumamrau(crash);
                break;
            }
            case 3: {
                m8ivda8b6ht7();
                break;
            }
        }
    }
    
    private static void m25qrakvfyqk(final long n, final FatalCaptureService fatalCaptureService) {
        if (fatalCaptureService == null) {
            return;
        }
        try {
            GLFW.glfwSetClipboardString(n, (CharSequence)fatalCaptureService.clipboardText());
            FatalShowingService.f9fhhdejq7bs = true;
            FatalShowingService.fiw56wjq9zjt = "";
        }
        catch (final Throwable t) {
            FatalShowingService.f6uw9b3soq6j.warn("Could not copy ellice crash report", t);
        }
    }
    
    private static void m2l5vumamrau(final FatalCaptureService fatalCaptureService) {
        if (fatalCaptureService == null) {
            return;
        }
        final Path dumpFile = fatalCaptureService.dumpFile();
        if (dumpFile != null) {
            CompatOpenUriService.openPath((dumpFile.getParent() == null) ? dumpFile : dumpFile.getParent());
        }
    }
    
    private static void m8ivda8b6ht7() {
        try {
            final Minecraft instance = Minecraft.getInstance();
            if (instance != null) {
                instance.stop();
            }
        }
        catch (final Throwable t) {
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
