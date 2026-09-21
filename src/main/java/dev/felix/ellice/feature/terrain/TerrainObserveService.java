package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import org.joml.Vector3d;

public final class TerrainObserveService {
   private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
   private Object object;
   private boolean enabled;

   public TerrainKindData observe(CompatLoadedHandler.World world, boolean enabled, boolean currentEnabled, long longValue) {
      return this.observe(world, world == null ? null : world.identity(), enabled, currentEnabled, longValue);
   }

   public TerrainKindData observe(CompatLoadedHandler.World world, Object value, boolean currentEnabled, boolean nextEnabled, long longValue) {
      if (world == null) {
         this.object = null;
         this.enabled = false;
         return null;
      }

      if (!Objects.equals(value, this.object)) {
         this.object = value;
         this.enabled = false;
      }

      int currentValue = currentEnabled && !this.enabled ? 1 : 0;
      this.enabled = currentEnabled;
      if (nextEnabled && currentValue != 0) {
         Vector3d vector3d = world.player();
         return new TerrainKindData(
            UUID.randomUUID(),
            "Death · " + dateTimeFormatter.format(Instant.ofEpochMilli(longValue)),
            world.dimension(),
            vector3d.x,
            vector3d.y,
            vector3d.z,
            TerrainKindData.Kind.DEATH.color(),
            TerrainKindData.Kind.DEATH,
            longValue
         );
      } else {
         return null;
      }
   }
}

