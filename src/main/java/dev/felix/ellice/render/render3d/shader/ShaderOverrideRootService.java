package dev.felix.ellice.render.render3d.shader;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class ShaderOverrideRootService {
  private static final String text = "/assets/ellice/shaders/ellice/";
  private static final Pattern pattern =
      Pattern.compile("^\\s*#include\\s*([\\\"<])([^\\\">]+)[\\\">]\\s*(?://.*)?$");
  private static final long timestamp = 250000000L;
  private final Path path2;
  private final Map<String, ResolvedSource> f2hgihnclxoe = new HashMap<String, ResolvedSource>();
  private long timestamp2;
  private long timestamp3 = 1L;
  private long timestamp4;

  public ShaderOverrideRootService(Path path) {
    if (path == null) {
      throw new IllegalArgumentException("overrideRoot is required");
    }
    this.path2 = path.toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.path2, new FileAttribute[0]);
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Could not create shader override directory {}",
          (Object) this.path2,
          (Object) iOException);
    }
    this.timestamp2 = this.calculateValue();
  }

  public Path overrideRoot() {
    return this.path2;
  }

  public synchronized long revision() {
    return this.timestamp3;
  }

  public synchronized boolean pollChanges() {
    long l = System.nanoTime();
    if (l < this.timestamp4) {
      return false;
    }
    this.timestamp4 = l + 250000000L;
    long l2 = this.calculateValue();
    if (l2 == this.timestamp2) {
      return false;
    }
    this.timestamp2 = l2;
    this.f2hgihnclxoe.clear();
    ++this.timestamp3;
    CoreIsInitializedHandler.LOGGER.info(
        "ellice 3D shaders changed; scheduling atomic reload (revision {})",
        (Object) this.timestamp3);
    return true;
  }

  public synchronized void reloadAll() {
    this.f2hgihnclxoe.clear();
    this.timestamp2 = this.calculateValue();
    ++this.timestamp3;
  }

  public synchronized ResolvedSource resolve(String string) {
    String string2 = ShaderOverrideRootService.normalizeAsset(string);
    ResolvedSource resolvedSource = this.f2hgihnclxoe.get(string2);
    if (resolvedSource != null) {
      return resolvedSource;
    }
    LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
    StringBuilder stringBuilder = new StringBuilder(8192);
    this.updateState(
        string2, stringBuilder, linkedHashSet, new HashSet<String>(), new ArrayDeque<String>());
    ResolvedSource resolvedSource2 =
        new ResolvedSource(
            string2, stringBuilder.toString(), List.copyOf(linkedHashSet), this.timestamp3);
    this.f2hgihnclxoe.put(string2, resolvedSource2);
    return resolvedSource2;
  }

  private void updateState(
      String string2,
      StringBuilder stringBuilder,
      Set<String> set,
      Set<String> set2,
      ArrayDeque<String> arrayDeque) {
    if (arrayDeque.contains(string2)) {
      StringBuilder stringBuilder2 = new StringBuilder();
      for (String string3 : arrayDeque) {
        if (!stringBuilder2.isEmpty()) {
          stringBuilder2.append(" -> ");
        }
        stringBuilder2.append(string3);
      }
      if (!stringBuilder2.isEmpty()) {
        stringBuilder2.append(" -> ");
      }
      stringBuilder2.append(string2);
      throw new IllegalArgumentException(
          "Cyclic shader include: " + String.valueOf(stringBuilder2));
    }
    String string4 = this.mgvunbggwl9c(string2);
    boolean bl = string4.lines().anyMatch(string -> string.trim().equals("#pragma once"));
    if (bl && !set2.add(string2)) {
      return;
    }
    set.add(string2);
    arrayDeque.addLast(string2);
    String string5 = ShaderOverrideRootService.createText2(string2);
    String[] stringArray = string4.split("\\R", -1);
    for (int i = 0; i < stringArray.length; ++i) {
      String string6 = stringArray[i];
      if (string6.trim().equals("#pragma once")) continue;
      Matcher matcher = pattern.matcher(string6);
      if (!matcher.matches()) {
        stringBuilder.append(string6).append('\n');
        continue;
      }
      boolean bl2 = "<".equals(matcher.group(1));
      String string7 = matcher.group(2).trim();
      String string8 =
          ShaderOverrideRootService.normalizeAsset(
              (String) (bl2 || string5.isEmpty() ? string7 : string5 + "/" + string7));
      stringBuilder.append("// begin include ").append(string8).append('\n');
      this.updateState(string8, stringBuilder, set, set2, arrayDeque);
      stringBuilder
          .append("// end include ")
          .append(string8)
          .append("; resume ")
          .append(string2)
          .append(':')
          .append(i + 2)
          .append('\n');
    }
    arrayDeque.removeLast();
  }

  private String mgvunbggwl9c(String string) {
    Path path = this.path2.resolve(string).normalize();
    if (!path.startsWith(this.path2)) {
      throw new IllegalArgumentException("Shader path escapes override root: " + string);
    }
    if (Files.isRegularFile(path, new LinkOption[0])) {
      try {
        return Files.readString(path, StandardCharsets.UTF_8);
      } catch (IOException exception) {
        throw new IllegalStateException("Failed to read shader override: " + path, exception);
      }
    }
    String resourcePath = text + string;
    try (InputStream inputStream =
        ShaderOverrideRootService.class.getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new FileNotFoundException(resourcePath);
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to read bundled shader: " + resourcePath, exception);
    }
  }

  private long calculateValue() {
    if (!Files.isDirectory(this.path2, new LinkOption[0])) {
      return 0L;
    }
    try (Stream<Path> stream = Files.walk(this.path2, new FileVisitOption[0])) {
      List<Path> files =
          stream
              .filter(path -> Files.isRegularFile(path, new LinkOption[0]))
              .filter(ShaderOverrideRootService::maa44m3pgkc)
              .sorted()
              .toList();
      long fingerprint = -3750763034362895579L;
      for (Path file : files) {
        String relativePath = this.path2.relativize(file).toString().replace('\\', '/');
        fingerprint =
            ShaderOverrideRootService.calculateValue2(fingerprint, relativePath.hashCode());
        fingerprint = ShaderOverrideRootService.calculateValue2(fingerprint, Files.size(file));
        fingerprint =
            ShaderOverrideRootService.calculateValue2(
                fingerprint, Files.getLastModifiedTime(file, new LinkOption[0]).toMillis());
        for (byte value : Files.readAllBytes(file)) {
          fingerprint =
              ShaderOverrideRootService.calculateValue2(fingerprint, (long) value & 0xFFL);
        }
      }
      return fingerprint;
    } catch (IOException exception) {
      CoreIsInitializedHandler.LOGGER.debug(
          "Could not scan shader overrides at {}", (Object) this.path2, (Object) exception);
      return this.timestamp2;
    }
  }

  private static boolean maa44m3pgkc(Path path) {
    String string = path.getFileName().toString();
    return (string.endsWith(".vert")
                || string.endsWith(".frag")
                || string.endsWith(".glsl")
                || string.endsWith(".comp")
            ? 1
            : 0)
        != 0;
  }

  private static long calculateValue2(long l, long l2) {
    return (l ^= l2) * 1099511628211L;
  }

  static String normalizeAsset(String string) {
    if (string == null) {
      throw new IllegalArgumentException("Shader asset is required");
    }
    String string2 = string.trim().replace('\\', '/');
    while (string2.startsWith("./")) {
      string2 = string2.substring(2);
    }
    if (string2.isEmpty() || string2.startsWith("/") || string2.indexOf(0) >= 0) {
      throw new IllegalArgumentException("Invalid shader asset path: " + string);
    }
    ArrayList<String> arrayList = new ArrayList<String>();
    for (String string3 : string2.split("/")) {
      if (string3.isEmpty() || string3.equals(".")) continue;
      if (string3.equals("..") || string3.contains(":")) {
        throw new IllegalArgumentException("Shader asset path may not traverse: " + string);
      }
      arrayList.add(string3);
    }
    if (arrayList.isEmpty()) {
      throw new IllegalArgumentException("Invalid shader asset path: " + string);
    }
    return String.join((CharSequence) "/", arrayList);
  }

  private static String createText2(String string) {
    int n = string.lastIndexOf(47);
    return n < 0 ? "" : string.substring(0, n);
  }

  public record ResolvedSource(
      String asset, String source, List<String> dependencies, long revision) {
    public ResolvedSource {
      dependencies = Collections.unmodifiableList(List.copyOf(dependencies));
    }
  }
}
