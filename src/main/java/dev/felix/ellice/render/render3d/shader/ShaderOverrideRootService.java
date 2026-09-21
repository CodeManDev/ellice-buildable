


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
    private static final String fe4isk0qno5l = "/assets/ellice/shaders/ellice/";
    private static final Pattern fde6kek93xmh = Pattern.compile("^\\s*#include\\s*([\\\"<])([^\\\">]+)[\\\">]\\s*(?://.*)?$");
    private static final long finyp0d22c0b = 250000000L;
    private final Path fbhd17rb6ub1;
    private final Map<String, ResolvedSource> f2hgihnclxoe = new HashMap<String, ResolvedSource>();
    private long fd7n8pp9wxvs;
    private long ffmkv649sj1b = 1L;
    private long f5cf5ukzjtgx;

    public ShaderOverrideRootService(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("overrideRoot is required");
        }
        this.fbhd17rb6ub1 = path.toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fbhd17rb6ub1, new FileAttribute[0]);
        }
        catch (IOException iOException) {
            CoreIsInitializedHandler.LOGGER.warn("Could not create shader override directory {}", (Object)this.fbhd17rb6ub1, (Object)iOException);
        }
        this.fd7n8pp9wxvs = this.m1pk9yb4fnmy();
    }

    public Path overrideRoot() {
        return this.fbhd17rb6ub1;
    }

    public synchronized long revision() {
        return this.ffmkv649sj1b;
    }

    public synchronized boolean pollChanges() {
        long l = System.nanoTime();
        if (l < this.f5cf5ukzjtgx) {
            return false;
        }
        this.f5cf5ukzjtgx = l + 250000000L;
        long l2 = this.m1pk9yb4fnmy();
        if (l2 == this.fd7n8pp9wxvs) {
            return false;
        }
        this.fd7n8pp9wxvs = l2;
        this.f2hgihnclxoe.clear();
        ++this.ffmkv649sj1b;
        CoreIsInitializedHandler.LOGGER.info("ellice 3D shaders changed; scheduling atomic reload (revision {})", (Object)this.ffmkv649sj1b);
        return true;
    }

    public synchronized void reloadAll() {
        this.f2hgihnclxoe.clear();
        this.fd7n8pp9wxvs = this.m1pk9yb4fnmy();
        ++this.ffmkv649sj1b;
    }

    public synchronized ResolvedSource resolve(String string) {
        String string2 = ShaderOverrideRootService.normalizeAsset(string);
        ResolvedSource resolvedSource = this.f2hgihnclxoe.get(string2);
        if (resolvedSource != null) {
            return resolvedSource;
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        StringBuilder stringBuilder = new StringBuilder(8192);
        this.m6qj9yomh3l6(string2, stringBuilder, linkedHashSet, new HashSet<String>(), new ArrayDeque<String>());
        ResolvedSource resolvedSource2 = new ResolvedSource(string2, stringBuilder.toString(), List.copyOf(linkedHashSet), this.ffmkv649sj1b);
        this.f2hgihnclxoe.put(string2, resolvedSource2);
        return resolvedSource2;
    }

    private void m6qj9yomh3l6(String string2, StringBuilder stringBuilder, Set<String> set, Set<String> set2, ArrayDeque<String> arrayDeque) {
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
            throw new IllegalArgumentException("Cyclic shader include: " + String.valueOf(stringBuilder2));
        }
        String string4 = this.mgvunbggwl9c(string2);
        boolean bl = string4.lines().anyMatch(string -> string.trim().equals("#pragma once"));
        if (bl && !set2.add(string2)) {
            return;
        }
        set.add(string2);
        arrayDeque.addLast(string2);
        String string5 = ShaderOverrideRootService.m4wj6wek3ayc(string2);
        String[] stringArray = string4.split("\\R", -1);
        for (int i = 0; i < stringArray.length; ++i) {
            String string6 = stringArray[i];
            if (string6.trim().equals("#pragma once")) continue;
            Matcher matcher = fde6kek93xmh.matcher(string6);
            if (!matcher.matches()) {
                stringBuilder.append(string6).append('\n');
                continue;
            }
            boolean bl2 = "<".equals(matcher.group(1));
            String string7 = matcher.group(2).trim();
            String string8 = ShaderOverrideRootService.normalizeAsset((String)(bl2 || string5.isEmpty() ? string7 : string5 + "/" + string7));
            stringBuilder.append("// begin include ").append(string8).append('\n');
            this.m6qj9yomh3l6(string8, stringBuilder, set, set2, arrayDeque);
            stringBuilder.append("// end include ").append(string8).append("; resume ").append(string2).append(':').append(i + 2).append('\n');
        }
        arrayDeque.removeLast();
    }

    private String mgvunbggwl9c(String string) {
        Path path = this.fbhd17rb6ub1.resolve(string).normalize();
        if (!path.startsWith(this.fbhd17rb6ub1)) {
            throw new IllegalArgumentException("Shader path escapes override root: " + string);
        }
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try {
                return Files.readString(path, StandardCharsets.UTF_8);
            } catch (IOException exception) {
                throw new IllegalStateException("Failed to read shader override: " + path, exception);
            }
        }
        String resourcePath = fe4isk0qno5l + string;
        try (InputStream inputStream = ShaderOverrideRootService.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException(resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read bundled shader: " + resourcePath, exception);
        }
    }

    private long m1pk9yb4fnmy() {
        if (!Files.isDirectory(this.fbhd17rb6ub1, new LinkOption[0])) {
            return 0L;
        }
        try (Stream<Path> stream = Files.walk(this.fbhd17rb6ub1, new FileVisitOption[0])) {
            List<Path> files = stream.filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(ShaderOverrideRootService::maa44m3pgkc).sorted().toList();
            long fingerprint = -3750763034362895579L;
            for (Path file : files) {
                String relativePath = this.fbhd17rb6ub1.relativize(file).toString().replace('\\', '/');
                fingerprint = ShaderOverrideRootService.mfm4m3reeznz(fingerprint, relativePath.hashCode());
                fingerprint = ShaderOverrideRootService.mfm4m3reeznz(fingerprint, Files.size(file));
                fingerprint = ShaderOverrideRootService.mfm4m3reeznz(fingerprint, Files.getLastModifiedTime(file, new LinkOption[0]).toMillis());
                for (byte value : Files.readAllBytes(file)) {
                    fingerprint = ShaderOverrideRootService.mfm4m3reeznz(fingerprint, (long)value & 0xFFL);
                }
            }
            return fingerprint;
        } catch (IOException exception) {
            CoreIsInitializedHandler.LOGGER.debug("Could not scan shader overrides at {}", (Object)this.fbhd17rb6ub1, (Object)exception);
            return this.fd7n8pp9wxvs;
        }
    }

    private static boolean maa44m3pgkc(Path path) {
        String string = path.getFileName().toString();
        return (string.endsWith(".vert") || string.endsWith(".frag") || string.endsWith(".glsl") || string.endsWith(".comp") ? 1 : 0) != 0;
    }

    private static long mfm4m3reeznz(long l, long l2) {
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
        return String.join((CharSequence)"/", arrayList);
    }

    private static String m4wj6wek3ayc(String string) {
        int n = string.lastIndexOf(47);
        return n < 0 ? "" : string.substring(0, n);
    }

    public record ResolvedSource(String asset, String source, List<String> dependencies, long revision) {
        public ResolvedSource {
            dependencies = Collections.unmodifiableList(List.copyOf(dependencies));
        }
    }
}
