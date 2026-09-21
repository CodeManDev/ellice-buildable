package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.terrain.TerrainReservedService;
import dev.felix.ellice.feature.terrain.TerrainKindData;
import dev.felix.ellice.feature.terrain.TerrainMapRenderer;
import dev.felix.ellice.feature.terrain.TerrainMapController;
import dev.felix.ellice.module.impl.ImplControllerService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.screen.builtin.TerrainMapScreen;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.ImageIO;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.storage.LevelResource;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public final class CompatInitializeService {
   private final Path path = FabricLoader.getInstance().getGameDir().resolve("terrain-map-evidence");
   private final ArrayList<String> text = new ArrayList<>();
   private ImplControllerService implControllerService;
   private TerrainMapScreen builtinStateController13;
   private TerrainKindData terrainKindData;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private int count;
   private boolean enabled;
   private boolean enabled2;
   private final long timestamp4 = System.nanoTime();

   private CompatInitializeService() {
   }

   public static void initialize() {
      if (Boolean.getBoolean("ellice.terrain.qa") && FabricLoader.getInstance().isDevelopmentEnvironment()) {
         CoreIsInitializedHandler.LOGGER.info("TERRAIN_MAP_QA_ENABLED");
         CompatInitializeService compatInitialize = new CompatInitializeService();
         CoreIsInitializedHandler.get().bus().subscribe(EventAttackInputService.TICK, item -> compatInitialize.updateState());
         CoreIsInitializedHandler.get().bus().subscribe(EventAttackInputService.RENDER, EventIsAfterHandler.Priority.MONITOR, item -> compatInitialize.updateState2());
      }
   }

   private void updateState() {
      Minecraft currentMinecraft = Minecraft.getInstance();
      if (currentMinecraft.level == null
         && currentMinecraft.getSingleplayerServer() == null
         && currentMinecraft.getOverlay() == null
         && !this.enabled2
         && System.nanoTime() - this.timestamp4 > 8000000000L) {
         this.enabled2 = true;
         CompatReaderService.play("ellice Map QA");
      }

      if (currentMinecraft.player != null && currentMinecraft.level != null && currentMinecraft.getSingleplayerServer() != null && this.count >= 0) {
         IntegratedServer integratedServer = currentMinecraft.getSingleplayerServer();
         Path currentPath = FabricLoader.getInstance().getGameDir().resolve("saves/ellice Map QA").toAbsolutePath().normalize();
         if (currentPath.equals(integratedServer.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize()) && currentPath.toString().contains("run-map-qa")) {
            long offset = System.nanoTime();
            if (this.count == 0) {
               this.timestamp2 = offset;
               this.count = 1;
               currentMinecraft.options.pauseOnLostFocus = false;
               if (currentMinecraft.player.isDeadOrDying()) {
                  currentMinecraft.player.respawn();
               }

               currentMinecraft.options.guiScale().set(2);
               currentMinecraft.options.renderDistance().set(10);
               integratedServer.execute(() -> {
                  for (String text : new String[]{"gamemode creative @a", "tp @a 0 265 20 180 30", "weather clear", "time set 6000"}) {
                     integratedServer.getCommands().performPrefixedCommand(integratedServer.createCommandSourceStack(), text);
                  }
               });
               CoreIsInitializedHandler.LOGGER.info("TERRAIN_MAP_QA waiting for terrain");
            } else if (this.count == 1 && offset - this.timestamp2 > 6000000000L) {
               this.count = 2;
               this.timestamp2 = offset;
               integratedServer.execute(
                  () -> {
                     for (String text : new String[]{
                        "fill -24 260 -24 24 260 24 minecraft:grass_block",
                        "fill -24 261 -24 24 272 24 air",
                        "fill -20 260 -16 -7 260 3 minecraft:water",
                        "fill 7 261 -12 13 264 -12 minecraft:stone_bricks",
                        "fill 7 265 -12 13 265 -12 minecraft:oak_stairs[facing=south]",
                        "fill 5 261 -10 5 263 -6 minecraft:glass",
                        "fill 12 261 3 16 261 7 minecraft:oak_leaves[persistent=true]",
                        "setblock 1 261 1 minecraft:chest",
                        "setblock 3 261 1 minecraft:oak_slab[type=bottom]",
                        "setblock -4 261 6 minecraft:lantern",
                        "setblock -3 261 6 minecraft:torch",
                        "setblock -1 261 6 minecraft:sea_lantern",
                        "fill -2 261 6 8 263 6 minecraft:stone_bricks",
                        "tp @a 0 261 20 180 30"
                     }) {
                        integratedServer.getCommands().performPrefixedCommand(integratedServer.createCommandSourceStack(), text);
                     }
                  }
               );
               CoreIsInitializedHandler.LOGGER.info("TERRAIN_MAP_QA world prepared");
               this.implControllerService = CoreIsInitializedHandler.get().modules().get(ImplControllerService.class).orElseThrow();
               if (!this.implControllerService.isEnabled()) {
                  this.implControllerService.enable();
               }

               this.implControllerService.openPanel();
            } else if (this.count == 2 && this.implControllerService.controller().world() != null && offset - this.timestamp2 > 500000000L) {
               TerrainMapController terrainMapController = this.implControllerService.controller();
               terrainMapController.camera(true).center(0.0, 261.0, 0.0);
               terrainMapController.camera(true).distance(108.0);

               for (TerrainKindData currentTerrainKindData : terrainMapController.savedWaypoints()) {
                  terrainMapController.remove(currentTerrainKindData.id());
               }

               terrainMapController.filter(null);
               this.terrainKindData = terrainMapController.add(
                  "Home base",
                  new Vector3d(
                     12.0,
                     261.0,
                     -8.0
                  ),
                  TerrainKindData.Kind.BASE
               );
               terrainMapController.add(
                  "Waterfront",
                  new Vector3d(
                     -14.0,
                     260.9,
                     -6.0
                  ),
                  -4794881
               );
               terrainMapController.add(
                  "Backup stash",
                  new Vector3d(
                     18.0,
                     261.0,
                     15.0
                  ),
                  TerrainKindData.Kind.STASH
               );
               terrainMapController.add(
                  "Unsafe crossing",
                  new Vector3d(
                     -19.0,
                     261.0,
                     15.0
                  ),
                  TerrainKindData.Kind.DANGER
               );
               if (this.terrainKindData != null) {
                  terrainMapController.navigate(this.terrainKindData);
               }

               this.count = Boolean.getBoolean("ellice.terrain.qa.featuresOnly") ? 11 : 3;
               this.timestamp2 = offset;
            } else if (this.count == 3 && offset - this.timestamp2 > 12000000000L) {
               this.updateState3("01-day");
               this.count = 4;
               this.timestamp2 = offset;
            } else if (this.count == 4 && offset - this.timestamp2 > 2000000000L) {
               this.implControllerService.controller().camera(true).flyTo(0.0, 261.0, 0.0);
               this.implControllerService
                  .controller()
                  .zoom(3.0, 360.0F, 225.0F);
               this.count = 5;
               this.timestamp2 = offset;
            } else if (this.count == 5 && offset - this.timestamp2 > 3000000000L) {
               this.updateState3("02-close");
               integratedServer.execute(() -> integratedServer.getCommands().performPrefixedCommand(integratedServer.createCommandSourceStack(), "time set 18000"));
               this.count = 6;
               this.timestamp2 = offset;
            } else if (this.count == 6 && offset - this.timestamp2 > 4000000000L) {
               this.updateState3("03-night");
               this.count = 7;
               this.timestamp2 = offset;
            } else if (this.count == 7 && offset - this.timestamp2 > 2000000000L) {
               integratedServer.execute(() -> integratedServer.getCommands().performPrefixedCommand(integratedServer.createCommandSourceStack(), "time set 6000"));
               this.implControllerService.controller().camera(true).topDown(true);
               this.count = 8;
               this.timestamp2 = offset;
            } else if (this.count == 8 && offset - this.timestamp2 > 3000000000L) {
               this.updateState3("04-overhead");
               this.count = 9;
               this.timestamp2 = offset;
            } else if (this.count == 9 && offset - this.timestamp2 > 2000000000L) {
               CoreIsInitializedHandler.get().screens().closeNow();
               currentMinecraft.setScreen(null);
               this.count = 10;
               this.timestamp2 = offset;
            } else if (this.count == 10 && offset - this.timestamp2 > 4000000000L) {
               this.updateState3("05-minimap");
               this.count = 11;
               this.timestamp2 = offset;
            } else if (this.count == 11 && offset - this.timestamp2 > 2000000000L) {
               this.builtinStateController13 = new TerrainMapScreen(this.implControllerService.controller(), 77);
               this.builtinStateController13.select(this.terrainKindData);
               this.builtinStateController13.showPortalPlanner();
               CoreIsInitializedHandler.get().screens().open(this.builtinStateController13);
               this.implControllerService.controller().camera(true).topDown(false);
               this.implControllerService.controller().camera(true).flyTo(0.0, 261.0, 0.0);
               this.implControllerService.controller().camera(true).zoomTo(108.0);
               this.count = 12;
               this.timestamp2 = offset;
            } else if (this.count == 12 && offset - this.timestamp2 > 2000000000L) {
               this.updateState3("06-portal-planner");
               this.count = 13;
               this.timestamp2 = offset;
            } else if (this.count == 13 && offset - this.timestamp2 > 1000000000L) {
               this.builtinStateController13.edit(this.terrainKindData);
               this.count = 14;
               this.timestamp2 = offset;
            } else if (this.count == 14 && offset - this.timestamp2 > 2000000000L) {
               this.updateState3("07-marker-editor");
               this.count = 15;
               this.timestamp2 = offset;
            } else if (this.count == 15 && offset - this.timestamp2 > 1000000000L) {
               CoreIsInitializedHandler.get().screens().closeNow();
               currentMinecraft.setScreen(null);
               this.timestamp = System.currentTimeMillis();
               integratedServer.execute(() -> integratedServer.getCommands().performPrefixedCommand(integratedServer.createCommandSourceStack(), "kill @a"));
               this.count = 16;
               this.timestamp2 = offset;
            } else if (this.count == 16 && offset - this.timestamp2 > 2000000000L) {
               TerrainKindData nextTerrainKindData = this.implControllerService.controller().lastDeath();
               if (nextTerrainKindData == null || nextTerrainKindData.createdAt() < this.timestamp) {
                  this.count = -1;
                  currentMinecraft.stop();
                  throw new IllegalStateException("QA death did not create an automatic waypoint");
               }

               CoreIsInitializedHandler.LOGGER.info("TERRAIN_MAP_QA_DEATH {} {} {} {}", new Object[]{nextTerrainKindData.dimension(), nextTerrainKindData.x(), nextTerrainKindData.y(), nextTerrainKindData.z()});
               currentMinecraft.player.respawn();
               currentMinecraft.setScreen(null);
               this.count = 17;
               this.timestamp2 = offset;
            } else if (this.count == 17 && offset - this.timestamp2 > 2000000000L) {
               this.builtinStateController13 = new TerrainMapScreen(this.implControllerService.controller(), 77);
               this.builtinStateController13.select(this.implControllerService.controller().lastDeath());
               CoreIsInitializedHandler.get().screens().open(this.builtinStateController13);
               this.implControllerService.controller().goTo(this.implControllerService.controller().lastDeath());
               this.implControllerService.controller().camera(true).zoomTo(108.0);
               this.count = 18;
               this.timestamp2 = offset;
            } else if (this.count == 18 && offset - this.timestamp2 > 2000000000L) {
               this.updateState3("08-death-waypoint");
               this.count = 19;
               this.timestamp2 = offset;
            } else if (this.count == 19 && offset - this.timestamp2 > 1000000000L) {
               try {
                  Files.write(this.path.resolve("frames.csv"), this.text);
               } catch (Exception exception) {
                  throw new RuntimeException(exception);
               }

               CoreIsInitializedHandler.LOGGER.info("TERRAIN_MAP_QA_COMPLETE {}", this.path);
               this.count = -1;
               currentMinecraft.stop();
            }
         } else {
            this.count = -1;
            throw new IllegalStateException("Map QA requires its isolated world copy");
         }
      }
   }

   private void updateState2() {
      long offset = System.nanoTime();
      if (this.implControllerService != null && this.implControllerService.controller() != null) {
         TerrainMapController terrainMapController = this.implControllerService.controller();
         if (this.text.isEmpty()) {
            this.text
               .add("stage,frame_ms,render_ms,snapshot_ms,build_ms,upload_ms,sections,pending,draws,triangles,cpu_bytes,gpu_bytes");
         }

         this.text
            .add(
               String.format(
                  Locale.ROOT,
                  "%d,%.3f,%.3f,%.3f,%.3f,%.3f,%d,%d,%d,%d,%d,%d",
                  this.count,
                  this.timestamp3 == 0L ? 0.0 : (offset - this.timestamp3) / 1000000.0,
                  terrainMapController.renderer().renderMillis(),
                  terrainMapController.cache().snapshotMillis(),
                  terrainMapController.cache().buildMillis(),
                  terrainMapController.renderer().uploadMillis(),
                  terrainMapController.loadedSections(),
                  terrainMapController.cache().pending(),
                  terrainMapController.renderer().drawCalls(),
                  terrainMapController.renderer().triangles(),
                  terrainMapController.cache().bytes(),
                  terrainMapController.renderer().gpuBytes()
               )
            );
      }

      this.timestamp3 = offset;
   }

   private void updateState3(String currentText) {
      if (!this.enabled) {
         this.enabled = true;
         CoreIsInitializedHandler.get()
            .compositor()
            .afterFlush(
               () -> {
                  try {
                     Files.createDirectories(this.path);
                     Files.write(this.path.resolve("frames.csv"), this.text);
                     FramebufferInfo framebufferInfo = CompatAdapterService.mainFramebuffer(Minecraft.getInstance());
                     if (this.implControllerService.controller().expanded()) {
                        ScenePctService scenePct = CoreIsInitializedHandler.get().scene().root().findById("terrain-map.canvas");
                        if (scenePct == null || scenePct.children().stream().noneMatch(item -> item instanceof TerrainReservedService)) {
                           throw new IllegalStateException("Waypoint overlay was removed by component reconciliation");
                        }

                        TerrainReservedService terrainReserved = (TerrainReservedService)scenePct.children()
                           .stream()
                           .filter(item -> item instanceof TerrainReservedService)
                           .findFirst()
                           .orElseThrow();
                        TerrainKindData terrainKindData = this.implControllerService
                           .controller()
                           .waypoints()
                           .stream()
                           .filter(item -> item.id().equals(this.implControllerService.controller().selected()))
                           .findFirst()
                           .orElse(null);
                        if (terrainKindData != null) {
                           Vector3d currentX = this.implControllerService.controller().camera(true).frame().project(terrainKindData.x(), terrainKindData.y(), terrainKindData.z());
                           if (currentX != null
                              && currentX.x > 25.0
                              && currentX.x < scenePct.computedW() - 25.0F
                              && currentX.y > 55.0
                              && currentX.y < scenePct.computedH() - 20.0F
                              && terrainReserved.hit((float)currentX.x, (float)currentX.y - 22.0F) == null) {
                              throw new IllegalStateException("Visible selected waypoint has no drag hit target");
                           }
                        }
                     }

                     updateState4(this.path.resolve(currentText + ".png"), framebufferInfo.framebufferId(), framebufferInfo.width(), framebufferInfo.height());
                     TerrainMapRenderer.Target currentTarget = this.implControllerService.controller().target(this.implControllerService.controller().expanded());
                     updateState4(
                        this.path.resolve(currentText + "-terrain.png"), currentTarget.view().framebuffer().id(), currentTarget.width(), currentTarget.height()
                     );
                     CoreIsInitializedHandler.LOGGER
                        .info(
                           "TERRAIN_MAP_QA_CAPTURE {}: {} sections; {}",
                           new Object[]{
                              currentText, this.implControllerService.controller().loadedSections(), this.implControllerService.controller().cache().lastError()
                           }
                        );
                     CoreIsInitializedHandler.LOGGER
                        .info(
                           "MAP_NAV {} route={} visited={} focus={}",
                           new Object[]{
                              this.implControllerService.controller().navigation().status(),
                              this.implControllerService.controller().navigation().route().size(),
                              this.implControllerService.controller().navigation().visited(),
                              this.implControllerService.controller().camera(true).frame().focus()
                           }
                        );
                  } catch (Exception exception) {
                     CoreIsInitializedHandler.LOGGER.error("Map capture failed", exception);
                  } finally {
                     this.enabled = false;
                  }
               }
            );
      }
   }

   private static void updateState4(Path path, int value, int currentValue, int nextValue) throws Exception {
      int previousValue = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING);
      ByteBuffer byteBuffer = MemoryUtil.memAlloc(currentValue * nextValue * 4);

      try {
         GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, value);
         GL11.glReadPixels(0, 0, currentValue, nextValue, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
         BufferedImage bufferedImage = new BufferedImage(currentValue, nextValue, 1);

         for (int index = 0; index < nextValue; index++) {
            for (int currentIndex = 0; currentIndex < currentValue; currentIndex++) {
               int sourceValue = ((nextValue - 1 - index) * currentValue + currentIndex) * 4;
               bufferedImage.setRGB(currentIndex, index, (byteBuffer.get(sourceValue) & 255) << 16 | (byteBuffer.get(sourceValue + 1) & 255) << 8 | byteBuffer.get(sourceValue + 2) & 255);
            }
         }

         ImageIO.write(bufferedImage, "png", path.toFile());
      } finally {
         MemoryUtil.memFree(byteBuffer);
         GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previousValue);
      }
   }
}

