package dev.felix.ellice.feature.license;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

public final class LicenseRepository {
   private static final Gson gson2 = new Gson();

   private LicenseRepository() {
   }

   static Path file(Path path) {
      return path.resolve("ellice").resolve("license.json");
   }

   public static LicenseRepository.CachedLicense load(Path path) {
      try {
         Path currentPath = file(path);
         if (!Files.isRegularFile(currentPath)) {
            return null;
         }

         LicenseRepository.CachedLicense cachedLicense = (LicenseRepository.CachedLicense)gson2.fromJson(
            Files.readString(currentPath, StandardCharsets.UTF_8), LicenseRepository.CachedLicense.class
         );
         return cachedLicense != null && cachedLicense.jwt() != null && !cachedLicense.jwt().isBlank() ? cachedLicense : null;
      } catch (Exception exception) {
         return null;
      }
   }

   public static void save(Path path, String text, long longValue) {
      try {
         Path currentPath = file(path);
         Files.createDirectories(currentPath.getParent());
         Path nextPath = currentPath.resolveSibling("license.json.tmp");
         Files.writeString(nextPath, gson2.toJson(new LicenseRepository.CachedLicense(text, longValue)), StandardCharsets.UTF_8);

         try {
            Files.setPosixFilePermissions(nextPath, PosixFilePermissions.fromString("rw-------"));
         } catch (Exception exception) {
         }

         try {
            Files.move(nextPath, currentPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (Exception currentException) {
            Files.move(nextPath, currentPath, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (Exception nextException) {
      }
   }

   public static void clear(Path path) {
      try {
         Files.deleteIfExists(file(path));
      } catch (Exception exception) {
      }
   }

   public record CachedLicense(String jwt, long checkedAtMs) {
   }
}
