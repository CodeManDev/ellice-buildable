package dev.felix.ellice.compat;

import dev.felix.ellice.feature.terrain.TerrainCanonicalService;
import dev.felix.ellice.feature.terrain.TerrainData;
import dev.felix.ellice.feature.terrain.TerrainSubscribeService;
import dev.felix.ellice.feature.terrain.TerrainSunDirectionService;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;

public abstract class CompatInstanceIdentityService implements CompatLoadedHandler, TravelLoadedHandler {
   @Override
   public Object instanceIdentity() {
      return Minecraft.getInstance().level;
   }

   @Override
   public CompatLoadedHandler.World world() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.level != null && var1.player != null) {
         String var2;
         if (var1.getSingleplayerServer() != null) {
            var2 = "local:" + var1.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize();
         } else {
            ServerData var3 = var1.getCurrentServer();
            var2 = var3 == null ? "session" : "server:" + var3.ip.toLowerCase(Locale.ROOT);
         }

         return new CompatLoadedHandler.World(
            var2,
            TerrainCanonicalService.canonical(var1.level.dimension().toString()),
            new Vector3d(var1.player.getX(), var1.player.getY(), var1.player.getZ()),
            var1.level.getMinSectionY(),
            var1.level.getMaxSectionY()
         );
      } else {
         return null;
      }
   }

   @Override
   public List<TerrainData> sections(double var1, double var3, int var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (var6.level == null) {
         return List.of();
      }

      int var7 = Math.floorDiv((int)Math.floor(var1), 16);
      int var8 = Math.floorDiv((int)Math.floor(var3), 16);
      int var9 = Math.clamp(var5, 1, 24);
      ArrayList var10 = new ArrayList();

      for (int var11 = var7 - var9; var11 <= var7 + var9; var11++) {
         for (int var12 = var8 - var9; var12 <= var8 + var9; var12++) {
            ChunkAccess var13 = var6.level.getChunk(var11, var12, ChunkStatus.FULL, false);
            if (var13 != null) {
               LevelChunkSection[] var14 = var13.getSections();

               for (int var15 = var14.length - 1; var15 >= 0; var15 += -1) {
                  if (!var14[var15].hasOnlyAir()) {
                     var10.add(new TerrainData(var11, var13.getSectionYFromSectionIndex(var15), var12));
                  }
               }
            }
         }
      }

      var10.sort(Comparator.<TerrainData>comparingDouble(var4 -> {
         double var5x = var4.blockX() + 8 - var1;
         double var7x = var4.blockZ() + 8 - var3;
         return var5x * var5x + var7x * var7x;
      }).thenComparing(Comparator.comparingInt(TerrainData::y).reversed()));
      return var10;
   }

   @Override
   public Object resourceIdentity() {
      return TerrainSubscribeService.resourceEpoch();
   }

   protected abstract CompatInstanceIdentityService.SkinInfo skinInfo(Player var1);

   @Override
   public List<CompatLoadedHandler.MapPlayer> mapPlayers() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.level != null && var1.player != null) {
         ArrayList<Player> var2 = new ArrayList<>();

         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof Player var5 && !var4.isSpectator() && var4.isAlive() && !var5.isInvisible() && !var5.isInvisibleTo(var1.player)) {
               var2.add(var5);
            }
         }

         var2.sort(Comparator.comparingDouble(var1x -> var1x.distanceToSqr(var1.player)));
         ArrayList var13 = new ArrayList();
         float var14 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);

         for (Player var6 : (Iterable<Player>) (Iterable<?>) (var2.subList(0, Math.min(var2.size(), 16)))) {
            CompatInstanceIdentityService.SkinInfo var7;
            try {
               var7 = this.skinInfo(var6);
            } catch (Exception var12) {
               var7 = new CompatInstanceIdentityService.SkinInfo(-1, false, false);
            }

            if (var7 == null) {
               var7 = new CompatInstanceIdentityService.SkinInfo(-1, false, false);
            }

            String var8;
            try {
               var8 = var6.getDisplayName().getString();
            } catch (Exception var11) {
               var8 = "Player";
            }

            try {
               Vec3 var9 = var6.getPosition(var14);
               var13.add(
                  new CompatLoadedHandler.MapPlayer(
                     var6.getUUID(),
                     var8,
                     var9.x,
                     var9.y,
                     var9.z,
                     var6.getYRot(),
                     (float)Math.toRadians(var6.getXRot()),
                     var7.slim(),
                     var7.glId(),
                     var7.legacy(),
                     var6.getHealth(),
                     var6.getMaxHealth(),
                     var6.isCrouching(),
                     CompatCaptureService.capture(var6, var14),
                     var1.level.getBrightness(LightLayer.BLOCK, var6.blockPosition()),
                     var1.level.getBrightness(LightLayer.SKY, var6.blockPosition())
                  )
               );
            } catch (IllegalArgumentException var10) {
            }
         }

         return var13;
      } else {
         return List.of();
      }
   }

   @Override
   public boolean playerDead() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return var1 != null && var1.isDeadOrDying();
   }

   @Override
   public CompatLoadedHandler.SkyState skyState() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.level == null) {
         return null;
      }

      try {
         return new CompatLoadedHandler.SkyState(
            TerrainSunDirectionService.sunDirection(m7g5lbg4ynwd(var1.level)), TerrainSunDirectionService.daylight(mhgo4zhu466h(var1.level))
         );
      } catch (Exception var3) {
         return null;
      }
   }

   private static long m7g5lbg4ynwd(Object var0) {
      for (String var4 : new String[]{"getOverworldClockTime", "getDayTime", "getGameTime"}) {
         try {
            if (var0.getClass().getMethod(var4).invoke(var0) instanceof Number var6) {
               return var6.longValue();
            }
         } catch (ReflectiveOperationException var7) {
         }
      }

      return 6000L;
   }

   private static float mhgo4zhu466h(Object var0) {
      try {
         if (var0.getClass().getMethod("getSkyDarken").invoke(var0) instanceof Number var2) {
            return var2.floatValue();
         }
      } catch (ReflectiveOperationException var3) {
      }

      return 0.0F;
   }

   @Override
   public Double shadowGround(double var1, double var3, double var5) {
      Minecraft var7 = Minecraft.getInstance();
      if (var7.level != null && Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5)) {
         int var8 = (int)Math.floor(var1);
         int var9 = (int)Math.floor(var3);
         if (!this.routeLoaded(var8, var9)) {
            return null;
         }

         try {
            BlockPos var10 = var7.level.getHeightmapPos(Types.MOTION_BLOCKING, new BlockPos(var8, 0, var9));
            VoxelShape var11 = var7.level.getBlockState(var10).getCollisionShape(var7.level, var10);
            double var12 = var11.isEmpty() ? var10.getY() + 1.0 : var10.getY() + var11.max(Axis.Y);
            if (var12 > var5 + 1.0) {
               return null;
            } else {
               return var12 < var7.level.getMinY() ? null : var12;
            }
         } catch (Exception var14) {
            return null;
         }
      } else {
         return null;
      }
   }

   @Override
   public boolean routeLoaded(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3.level != null && var3.level.getChunk(Math.floorDiv(var1, 16), Math.floorDiv(var2, 16), ChunkStatus.FULL, false) != null;
   }

   @Override
   public Double walkingFloor(int var1, int var2, double var3) {
      Minecraft var5 = Minecraft.getInstance();
      if (!this.routeLoaded(var1, var2)) {
         return null;
      }

      MutableBlockPos var6 = new MutableBlockPos();

      for (int var7 = (int)Math.floor(var3 + 1.0); var7 >= (int)Math.floor(var3 - Double.longBitsToDouble(4616189618054758400L)); var7 += -1) {
         var6.set(var1, var7, var2);
         BlockState var8 = var5.level.getBlockState(var6);
         if (var8.getFluidState().isEmpty()
            && !var8.is(Blocks.CACTUS)
            && !var8.is(Blocks.MAGMA_BLOCK)
            && !var8.is(Blocks.CAMPFIRE)
            && !var8.is(Blocks.SOUL_CAMPFIRE)
            && !var8.is(Blocks.POWDER_SNOW)) {
            VoxelShape var9 = var8.getCollisionShape(var5.level, var6);
            if (!var9.isEmpty()) {
               double var10 = var7 + var9.max(Axis.Y);
               if (!(var10 > var3 + Double.longBitsToDouble(4607227454796291113L)) && !(var10 < var3 - Double.longBitsToDouble(4613960336239210004L))) {
                  BlockPos var12 = new BlockPos(var1, (int)Math.floor(var10 + Double.longBitsToDouble(4587366580439587226L)), var2);
                  BlockState var13 = var5.level.getBlockState(var12);
                  if (var13.getFluidState().isEmpty()
                     && !var13.is(Blocks.FIRE)
                     && !var13.is(Blocks.SOUL_FIRE)
                     && !var13.is(Blocks.SWEET_BERRY_BUSH)
                     && !var13.is(Blocks.WITHER_ROSE)
                     && var5.level
                        .noCollision(
                           new AABB(
                              var1 + Double.longBitsToDouble(4596013491724138578L),
                              var10 + Double.longBitsToDouble(4581421828931458171L),
                              var2 + Double.longBitsToDouble(4596013491724138578L),
                              var1 + Double.longBitsToDouble(4605471050941616620L),
                              var10 + Double.longBitsToDouble(4610875370494461215L),
                              var2 + Double.longBitsToDouble(4605471050941616620L)
                           )
                        )) {
                     return var10;
                  }
               }
            }
         }
      }

      return null;
   }

   @Override
   public boolean loaded(int var1, int var2) {
      return this.routeLoaded(var1, var2);
   }

   @Override
   public Double floor(int var1, int var2, double var3) {
      return this.walkingFloor(var1, var2, var3);
   }

   @Override
   public boolean headroom(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2) && this.routeLoaded(var1, var4)) {
         try {
            return var5.level
               .noCollision(
                  new AABB(
                     var1 + Double.longBitsToDouble(4596013491724138578L),
                     var2 + Double.longBitsToDouble(4581421828931458171L),
                     var4 + Double.longBitsToDouble(4596013491724138578L),
                     var1 + Double.longBitsToDouble(4605471050941616620L),
                     var2 + Double.longBitsToDouble(4610875370494461215L),
                     var4 + Double.longBitsToDouble(4605471050941616620L)
                  )
               );
         } catch (Exception var7) {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean jumpHeadroom(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2) && this.routeLoaded(var1, var4)) {
         try {
            return var5.level
               .noCollision(
                  new AABB(
                     var1 + Double.longBitsToDouble(4596013491724138578L),
                     var2 + Double.longBitsToDouble(4581421828931458171L),
                     var4 + Double.longBitsToDouble(4596013491724138578L),
                     var1 + Double.longBitsToDouble(4605471050941616620L),
                     var2 + Double.longBitsToDouble(4612811918334230528L),
                     var4 + Double.longBitsToDouble(4605471050941616620L)
                  )
               );
         } catch (Exception var7) {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public TravelLoadedHandler.Hazard hazard(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2)) {
         try {
            if (var2 < var5.level.getMinY() - 4) {
               return TravelLoadedHandler.Hazard.VOID;
            }
         } catch (Exception var8) {
            return TravelLoadedHandler.Hazard.NONE;
         }

         if (!this.routeLoaded(var1, var4)) {
            return TravelLoadedHandler.Hazard.NONE;
         }

         try {
            BlockPos var6 = new BlockPos(var1, (int)Math.floor(var2 + Double.longBitsToDouble(4587366580439587226L)), var4);
            return !mbbqgymo78n3(var6) && !mbbqgymo78n3(var6.below()) ? TravelLoadedHandler.Hazard.NONE : TravelLoadedHandler.Hazard.DANGER;
         } catch (Exception var7) {
            return TravelLoadedHandler.Hazard.NONE;
         }
      } else {
         return TravelLoadedHandler.Hazard.NONE;
      }
   }

   private static boolean mbbqgymo78n3(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      BlockState var2 = var1.level.getBlockState(var0);
      return !var2.getFluidState().isEmpty() && !var2.getFluidState().is(FluidTags.WATER)
         ? true
         : var2.is(Blocks.FIRE)
            || var2.is(Blocks.SOUL_FIRE)
            || var2.is(Blocks.LAVA)
            || var2.is(Blocks.CACTUS)
            || var2.is(Blocks.MAGMA_BLOCK)
            || var2.is(Blocks.CAMPFIRE)
            || var2.is(Blocks.SOUL_CAMPFIRE)
            || var2.is(Blocks.SWEET_BERRY_BUSH)
            || var2.is(Blocks.WITHER_ROSE)
            || var2.is(Blocks.POWDER_SNOW)
            || var2.is(Blocks.COBWEB)
            || var2.is(Blocks.END_PORTAL)
            || var2.is(Blocks.END_GATEWAY)
            || var2.is(Blocks.NETHER_PORTAL);
   }

   @Override
   public boolean water(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2) && this.routeLoaded(var1, var4)) {
         try {
            return var5.level
               .getFluidState(new BlockPos(var1, (int)Math.floor(var2 + Double.longBitsToDouble(4587366580439587226L)), var4))
               .is(FluidTags.WATER);
         } catch (Exception var7) {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean climbable(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2) && this.routeLoaded(var1, var4)) {
         try {
            BlockPos var6 = new BlockPos(var1, (int)Math.floor(var2 + Double.longBitsToDouble(4587366580439587226L)), var4);
            return miaou52ksy3p(var6) || miaou52ksy3p(var6.above());
         } catch (Exception var7) {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean miaou52ksy3p(BlockPos var0) {
      BlockState var1 = Minecraft.getInstance().level.getBlockState(var0);
      return var1.is(Blocks.LADDER)
         || var1.is(Blocks.VINE)
         || var1.is(Blocks.TWISTING_VINES)
         || var1.is(Blocks.TWISTING_VINES_PLANT)
         || var1.is(Blocks.WEEPING_VINES)
         || var1.is(Blocks.WEEPING_VINES_PLANT)
         || var1.is(Blocks.CAVE_VINES)
         || var1.is(Blocks.CAVE_VINES_PLANT)
         || var1.is(Blocks.SCAFFOLDING);
   }

   @Override
   public boolean passableDoor(int var1, double var2, int var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.level != null && Double.isFinite(var2) && this.routeLoaded(var1, var4)) {
         try {
            Block var6 = var5.level.getBlockState(new BlockPos(var1, (int)Math.floor(var2 + Double.longBitsToDouble(4587366580439587226L)), var4)).getBlock();
            return var6 instanceof DoorBlock || var6 instanceof FenceGateBlock || var6 instanceof TrapDoorBlock;
         } catch (Exception var7) {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean placeable(int var1, int var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level == null) {
         return false;
      }

      try {
         if (var2 >= var4.level.getMinY() && var2 <= var4.level.getMaxY()) {
            BlockPos var5 = new BlockPos(var1, var2, var3);
            if (var4.level.isInWorldBounds(var5) && this.routeLoaded(var1, var3)) {
               BlockState var6 = var4.level.getBlockState(var5);
               return var6.isAir() || var6.canBeReplaced() || !var6.getFluidState().isEmpty();
            } else {
               return false;
            }
         } else {
            return false;
         }
      } catch (Exception var7) {
         return false;
      }
   }

   @Override
   public boolean placeAllowed(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.level == null) {
         return false;
      }

      try {
         return var3.level.getWorldBorder().isWithinBounds(new BlockPos(var1, 0, var2));
      } catch (Exception var5) {
         return true;
      }
   }

   public record SkinInfo(int glId, boolean slim, boolean legacy) {
   }
}
