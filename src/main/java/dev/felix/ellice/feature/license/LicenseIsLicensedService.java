package dev.felix.ellice.feature.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.event.EventSubscribeService;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.loader.api.FabricLoader;

public final class LicenseIsLicensedService {
  public static final long GRACE_MS = 86400000L;
  public static final long HEARTBEAT_MS = 900000L;
  static final long SKEW_MS = 60000L;
  static final long JOIN_COOLDOWN_MS = 120000L;
  static final int DEVICE_LOCK_STRIKES = 2;
  private static volatile LicenseIsLicensedService.Verdict verdict2 = null;
  private static volatile String text = "not evaluated";
  private static volatile String text2 = "";
  private static volatile String text3 = "";
  private static volatile long timestamp = 0L;
  private static volatile long timestamp2 = 0L;
  private static final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
  private static final AtomicInteger atomicInteger = new AtomicInteger(0);

  private LicenseIsLicensedService() {}

  public static boolean isLicensed() {
    return true;
  }

  public static boolean modulesAllowed() {
    return true;
  }

  public static String denyReason() {
    return text;
  }

  static void resetForTests() {
    verdict2 = null;
    text = "not evaluated";
    text2 = "";
    text3 = "";
    timestamp = 0L;
    timestamp2 = 0L;
    atomicInteger.set(0);
  }

  public static LicenseIsLicensedService.BootResult evaluateAtBoot(Path path, String currentText) {
    List items = LicenseDetectService.detect();
    if (!items.isEmpty()) {
      return createBootResult2(
          "Security check failed (unsupported Java agent: "
              + String.join(", ", items)
              + "). Restart without additional Java agents.");
    }

    verdict2 = LicenseIsLicensedService.Verdict.ALLOWED;
    text = "";
    text2 = "";
    return new LicenseIsLicensedService.BootResult(
        true, LicenseIsLicensedService.Verdict.ALLOWED, "free release");
  }

  public static void beginSession(EventSubscribeService eventSubscribe, Path path, String text) {}

  private static void updateState(Path path, String currentText, String nextText) {
    long offset = System.currentTimeMillis();
    if (LicenseExpiryMsService.expired(nextText, offset, 60000L)) {
      LicenseIsLicensedService.BootResult bootResult = createBootResult(path, offset);
      if (bootResult != null) {
        text2 =
            LicenseRepository.load(path) != null ? LicenseRepository.load(path).jwt() : nextText;
      } else {
        updateState2("Your session has expired. Sign in again in the launcher.");
      }
    } else {
      LicenseBaseUrlService.Heartbeat currentHeartbeat =
          LicenseBaseUrlService.heartbeat(
              nextText, LicenseStableService.stable(), currentText, text3);
      if (currentHeartbeat.result() == LicenseBaseUrlService.HeartbeatResult.OK) {
        timestamp = System.currentTimeMillis();
        atomicInteger.set(0);
        LicenseRepository.save(path, nextText, timestamp);
        if (verdict2 == LicenseIsLicensedService.Verdict.GRACE) {
          verdict2 = LicenseIsLicensedService.Verdict.ALLOWED;
          text = "";
        }
      } else if (currentHeartbeat.result() != LicenseBaseUrlService.HeartbeatResult.NETWORK_ERROR) {
        if (currentHeartbeat.result() == LicenseBaseUrlService.HeartbeatResult.DEVICE_LOCKED) {
          if (atomicInteger.incrementAndGet() >= 2) {
            updateState2(deviceLockedMessage(currentHeartbeat.error()));
          }
        } else {
          String previousText =
              switch (currentHeartbeat.result()) {
                case UPDATE_REQUIRED ->
                    "Update ellice in the launcher"
                        + (currentHeartbeat.minVersion().isEmpty()
                            ? "."
                            : " (minimum build " + currentHeartbeat.minVersion() + ").");
                default -> deniedMessage(currentHeartbeat.error());
              };
          updateState2(previousText);
        }
      }
    }
  }

  private static void updateState2(String currentText) {
    verdict2 = LicenseIsLicensedService.Verdict.DENIED;
    text = currentText;
    FatalIsTrippedService.trip(
        new IllegalStateException(currentText), FatalCaptureService.Kind.LICENSE);
  }

  private static LicenseIsLicensedService.BootResult createBootResult(Path path, long size) {
    if (!graceUsable(LicenseRepository.load(path), size)) {
      return null;
    }

    LicenseRepository.CachedLicense cachedLicense = LicenseRepository.load(path);
    text2 = cachedLicense.jwt();
    verdict2 = LicenseIsLicensedService.Verdict.GRACE;
    text = "";
    return new LicenseIsLicensedService.BootResult(
        true, LicenseIsLicensedService.Verdict.GRACE, "offline grace (24h)");
  }

  static boolean graceUsable(LicenseRepository.CachedLicense cachedLicense, long longValue) {
    if (cachedLicense != null && cachedLicense.jwt() != null && !cachedLicense.jwt().isBlank()) {
      return LicenseExpiryMsService.expiryMs(cachedLicense.jwt()) < 0L
          ? false
          : longValue - cachedLicense.checkedAtMs() <= 86400000L;
    } else {
      return false;
    }
  }

  static String deviceLockedMessage(String text) {
    return text != null && !text.isBlank()
        ? text.trim()
        : "A new device was detected. Sign in again in the launcher to approve it, then restart the"
              + " game.";
  }

  static String deniedMessage(String text) {
    if (text != null && !text.isBlank()) {
      String currentText = text.trim();
      return currentText.toLowerCase(Locale.ROOT).contains("beta")
          ? "No beta access for this account. Redeem a beta key in the launcher."
          : "Your session is invalid (" + currentText + "). Sign in again in the launcher.";
    } else {
      return "Beta access was revoked or your session is invalid. Sign in again in the launcher.";
    }
  }

  static String readNonce(Path path) {
    try {
      Path currentPath = path.resolve("mods").resolve(".ellice-id");
      if (!Files.isRegularFile(currentPath)) {
        return "";
      }

      String text = Files.readString(currentPath, StandardCharsets.UTF_8);
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      return jsonObject.has("nonce") ? jsonObject.get("nonce").getAsString() : "";
    } catch (Exception exception) {
      return "";
    }
  }

  static String readSessionFile(Path path) {
    try {
      Path currentPath = path.resolve(".ellice-session");
      return !Files.isRegularFile(currentPath)
          ? ""
          : Files.readString(currentPath, StandardCharsets.UTF_8).trim();
    } catch (Exception exception) {
      return "";
    }
  }

  static boolean isDevelopmentEnvironment() {
    try {
      return FabricLoader.getInstance().isDevelopmentEnvironment();
    } catch (Throwable exception) {
      return false;
    }
  }

  private static LicenseIsLicensedService.BootResult createBootResult2(String currentText) {
    verdict2 = LicenseIsLicensedService.Verdict.DENIED;
    text = currentText;
    return new LicenseIsLicensedService.BootResult(
        false, LicenseIsLicensedService.Verdict.DENIED, currentText);
  }

  public record BootResult(
      boolean licensed, LicenseIsLicensedService.Verdict verdict, String reason) {}

  public enum Verdict {
    ALLOWED,
    GRACE,
    DENIED;

    private static LicenseIsLicensedService.Verdict[] $values() {
      return new LicenseIsLicensedService.Verdict[] {ALLOWED, GRACE, DENIED};
    }
  }
}
