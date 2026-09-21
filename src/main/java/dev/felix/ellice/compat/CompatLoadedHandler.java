package dev.felix.ellice.compat;

import dev.felix.ellice.feature.terrain.TerrainData;
import dev.felix.ellice.feature.terrain.TerrainLayerData;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import java.util.List;
import java.util.UUID;
import org.joml.Vector3d;
import org.joml.Vector3f;

public interface CompatLoadedHandler {
   CompatLoadedHandler.World world();

   default Object instanceIdentity() {
      CompatLoadedHandler.World currentWorld = this.world();
      return currentWorld == null ? null : currentWorld.identity();
   }

   List<TerrainData> sections(double doubleValue, double currentDoubleValue, int value);

   CompatLoadedHandler.BuildJob snapshot(TerrainData terrainData);

   CompatLoadedHandler.Textures textures();

   default List<CompatLoadedHandler.MapPlayer> mapPlayers() {
      return List.of();
   }

   Object resourceIdentity();

   default CompatLoadedHandler.SkyState skyState() {
      return null;
   }

   default Double shadowGround(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return null;
   }

   default boolean playerDead() {
      return false;
   }

   default Double walkingFloor(int value, int currentValue, double doubleValue) {
      return null;
   }

   default boolean routeLoaded(int value, int currentValue) {
      return false;
   }

   default TravelLoadedHandler traversal() {
      return this instanceof TravelLoadedHandler travelLoaded ? travelLoaded : new TravelLoadedHandler() {
         @Override
         public boolean loaded(int value, int currentValue) {
            return CompatLoadedHandler.this.routeLoaded(value, currentValue);
         }

         @Override
         public Double floor(int value, int currentValue, double doubleValue) {
            return CompatLoadedHandler.this.walkingFloor(value, currentValue, doubleValue);
         }
      };
   }

   void closeWorker();

   @FunctionalInterface
   interface BuildJob {
      TerrainLayerData build() throws Exception;
   }

   record MapPlayer(
      UUID id,
      String name,
      double x,
      double y,
      double z,
      float yaw,
      float pitch,
      boolean slim,
      int skinGlId,
      boolean legacySkin,
      float health,
      float maxHealth,
      boolean crouching,
      List<CompatLoadedHandler.PlayerPart> model,
      int blockLight,
      int skyLight
   ) {
      public MapPlayer(
         UUID uUID,
         String text,
         double doubleValue,
         double currentDoubleValue,
         double nextDoubleValue,
         float value,
         float currentValue,
         boolean enabled,
         int nextValue,
         boolean currentEnabled,
         float previousValue,
         float sourceValue,
         boolean nextEnabled,
         List<CompatLoadedHandler.PlayerPart> items
      ) {
         this(uUID, text, doubleValue, currentDoubleValue, nextDoubleValue, value, currentValue, enabled, nextValue, currentEnabled, previousValue, sourceValue, nextEnabled, items, 0, 15);
      }

      public MapPlayer(
         UUID uUID,
         String text,
         double doubleValue,
         double currentDoubleValue,
         double nextDoubleValue,
         float value,
         float currentValue,
         boolean enabled,
         int nextValue,
         boolean currentEnabled,
         float previousValue,
         float sourceValue,
         boolean nextEnabled
      ) {
         this(uUID, text, doubleValue, currentDoubleValue, nextDoubleValue, value, currentValue, enabled, nextValue, currentEnabled, previousValue, sourceValue, nextEnabled, List.of());
      }

      public MapPlayer(
         UUID id,
         String name,
         double x,
         double y,
         double z,
         float yaw,
         float pitch,
         boolean slim,
         int skinGlId,
         boolean legacySkin,
         float health,
         float maxHealth,
         boolean crouching,
         List<CompatLoadedHandler.PlayerPart> model,
         int blockLight,
         int skyLight
      ) {
         blockLight = Math.clamp(blockLight, 0, 15);
         skyLight = Math.clamp(skyLight, 0, 15);
         model = model == null ? List.of() : List.copyOf(model);
         if (id == null) {
            throw new IllegalArgumentException("Missing player identity");
         }

         name = name == null ? "Player" : name.strip().replaceAll("[\\p{Cntrl}]", "");
         if (name.isEmpty()) {
            name = "Player";
         }

         if (name.codePointCount(0, name.length()) > 32) {
            name = name.substring(0, name.offsetByCodePoints(0, 32));
         }

         if (Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z) && Float.isFinite(yaw)) {
            if (!Float.isFinite(pitch)) {
               pitch = 0.0F;
            }

            pitch = Math.clamp(pitch, -1.6F, 1.6F);
            if (!Float.isFinite(health) || health < 0.0F) {
               health = 0.0F;
            }

            if (!Float.isFinite(maxHealth) || maxHealth <= 0.0F) {
               maxHealth = 20.0F;
            }

            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.slim = slim;
            this.skinGlId = skinGlId;
            this.legacySkin = legacySkin;
            this.health = health;
            this.maxHealth = maxHealth;
            this.crouching = crouching;
            this.model = model;
            this.blockLight = blockLight;
            this.skyLight = skyLight;
         } else {
            throw new IllegalArgumentException("Invalid player position");
         }
      }
   }

   record PlayerPart(GeometryVertexCountService mesh, int texture, int color) {
   }

   record SkyState(Vector3f sunDirection, float daylight) {
      public SkyState(Vector3f sunDirection, float daylight) {
         sunDirection = sunDirection == null ? new Vector3f(0.0F, 1.0F, 0.0F) : new Vector3f(sunDirection);
         if (sunDirection.lengthSquared() < 1.0E-8F) {
            sunDirection.set(0.0F, 1.0F, 0.0F);
         }

         sunDirection.normalize();
         daylight = Float.isFinite(daylight) ? Math.clamp(daylight, 0.0F, 1.0F) : 1.0F;
         this.sunDirection = sunDirection;
         this.daylight = daylight;
      }
   }

   record Textures(int atlas, int lightmap, int width, int height, int mipLevels, boolean modernSampling) {
   }

   record World(String identity, String dimension, Vector3d player, int minSection, int maxSection) {
   }
}
