package dev.felix.ellice.module.impl.playerlookup;

import java.time.Instant;
import java.util.UUID;

public record PlayerlookupData(
   UUID uuid, String username, String skinUrl, String capeUrl, boolean slim, Instant texturesUpdatedAt, String avatarUrl
) {
   public String dashedUuid() {
      return this.uuid != null ? this.uuid.toString() : "";
   }

   public String trimmedUuid() {
      return this.dashedUuid().replace("-", "");
   }

   public boolean hasCape() {
      return this.capeUrl != null && !this.capeUrl.isBlank();
   }

   public boolean hasSkin() {
      return this.skinUrl != null && !this.skinUrl.isBlank();
   }

   public String skinModel() {
      return this.slim ? "Slim (Alex)" : "Classic (Steve)";
   }
}
