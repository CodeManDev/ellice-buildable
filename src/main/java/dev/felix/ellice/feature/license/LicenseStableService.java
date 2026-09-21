package dev.felix.ellice.feature.license;

import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

public final class LicenseStableService {
  static final String PREFIX = "hw2_";
  static final String UNKNOWN = "hw2_unknown";

  private LicenseStableService() {}

  public static String stable() {
    try {
      List items = parts();
      return items.size() >= 4
              && ((String) items.get(2)).isEmpty()
              && ((String) items.get(3)).isEmpty()
          ? "hw2_unknown"
          : fingerprint(items);
    } catch (Exception exception) {
      return "hw2_unknown";
    }
  }

  static List<String> parts() {
    ArrayList arrayList = new ArrayList();
    arrayList.add(normalize(System.getProperty("os.name", "")));
    arrayList.add(normalize(System.getProperty("os.arch", "")));
    arrayList.add(primaryMacAddress());
    arrayList.add(deviceSecret());
    return arrayList;
  }

  static String normalize(String text) {
    return text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
  }

  static String primaryMacAddress() {
    try {
      ArrayList<LicenseStableService.Candidate> arrayList = new ArrayList<>();
      Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
      if (enumeration == null) {
        return "";
      }

      while (enumeration.hasMoreElements()) {
        NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();

        try {
          if (!networkInterface.isLoopback()
              && networkInterface.isUp()
              && !networkInterface.isVirtual()) {
            String text =
                normalize(networkInterface.getName())
                    + " "
                    + normalize(networkInterface.getDisplayName());
            if (!isEphemeralInterface(text)) {
              byte[] bytes = networkInterface.getHardwareAddress();
              if (bytes != null && bytes.length != 0) {
                arrayList.add(
                    new LicenseStableService.Candidate(
                        rank(networkInterface.getName()), text, HexFormat.of().formatHex(bytes)));
              }
            }
          }
        } catch (Exception exception) {
        }
      }

      return arrayList.stream()
          .sorted(
              Comparator.comparingInt(LicenseStableService.Candidate::rank)
                  .thenComparing(LicenseStableService.Candidate::name))
          .map(LicenseStableService.Candidate::mac)
          .findFirst()
          .orElse("");
    } catch (Exception currentException) {
      return "";
    }
  }

  static int rank(String text) {
    String currentText = normalize(text);
    if (currentText.startsWith("eth")
        || currentText.startsWith("eno")
        || currentText.startsWith("em")
        || currentText.equals("en0")
        || currentText.startsWith("lan")) {
      return 0;
    } else {
      return !currentText.startsWith("wl")
              && !currentText.startsWith("wlan")
              && !currentText.contains("wi-fi")
              && !currentText.contains("wifi")
              && !currentText.contains("wireless")
              && !currentText.equals("en1")
          ? 2
          : 1;
    }
  }

  static boolean isEphemeralInterface(String text) {
    String currentText = text;
    String[] strings =
        new String[] {
          "utun",
          "tun",
          "tap",
          "ppp",
          "vpn",
          "docker",
          "veth",
          "virbr",
          "vmnet",
          "vbox",
          "virtualbox",
          "vmware",
          "hyper-v",
          "hyperv",
          "tailscale",
          "wireguard",
          "hamachi",
          "zerotier",
          "tether",
          "bridge",
          "br-",
          "vswitch",
          "wg0",
          "wg-",
          "nebula",
          "gpd",
          "forti",
          "anyconnect",
          "openvpn",
          "pptp",
          "l2tp",
          "ipsec",
          "zt"
        };

    for (String nextText : strings) {
      if (currentText.contains(nextText)) {
        return true;
      }
    }

    return false;
  }

  static String deviceSecret() {
    try {
      Path path = secretFile();
      if (Files.isRegularFile(path)) {
        String text =
            Files.readString(path, StandardCharsets.UTF_8).trim().toLowerCase(Locale.ROOT);
        if (text.matches("[0-9a-f-]{8,64}")) {
          return text;
        }
      }

      byte[] bytes = new byte[16];
      new SecureRandom().nextBytes(bytes);
      String currentText = HexFormat.of().formatHex(bytes);
      byte byteValue = 0;

      try {
        Files.createDirectories(path.getParent());
        Path currentPath = path.resolveSibling("device-id.tmp");
        Files.writeString(currentPath, currentText, StandardCharsets.UTF_8);

        try {
          Files.setPosixFilePermissions(currentPath, PosixFilePermissions.fromString("rw-------"));
        } catch (Exception exception) {
        }

        try {
          Files.move(
              currentPath,
              path,
              StandardCopyOption.ATOMIC_MOVE,
              StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception currentException) {
          Files.move(currentPath, path, StandardCopyOption.REPLACE_EXISTING);
        }

        byteValue = 1;
      } catch (Exception nextException) {
      }

      return byteValue != 0 ? currentText : "";
    } catch (Exception previousException) {
      return "";
    }
  }

  static Path secretFile() {
    return Path.of(System.getProperty("user.home", "."), ".ellice", "device-id");
  }

  static String fingerprint(List<String> items) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = messageDigest.digest(String.join("|", items).getBytes(StandardCharsets.UTF_8));
      return "hw2_" + HexFormat.of().formatHex(bytes);
    } catch (Exception exception) {
      return "hw2_unknown";
    }
  }

  private record Candidate(int rank, String name, String mac) {}
}
