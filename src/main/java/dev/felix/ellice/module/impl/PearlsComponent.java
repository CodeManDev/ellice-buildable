package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.FriendSyncController;
import dev.felix.ellice.compat.CompatDecisionTracker;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.core.CoreSetBehindScreenService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.world.TrajectoryOverlayRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class PearlsComponent extends ModuleSettingsService {
   private static final double value = 1.5;
   private static final double value2 = 0.03;
   private static final double value3 = 0.05;
   private static final double value4 = 0.99;
   private static final double value5 = 0.8;
   private static final double value6 = 0.6;
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Pearls", true).description("Draws a simulated ender-pearl path while a pearl is held in either hand.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Bow", true).description("Draws an arrow path while a bow is being charged, using its current draw strength.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      new ModuleSetting.Bool("Account Movement", true)
         .description("Adds your current velocity to the launch, improving predictions while walking, jumping, or falling.")
   );
   private final ModuleSetting.Bool moduleBool4 = this.setting(
      new ModuleSetting.Bool("Target Intel", true)
         .description("Checks bow paths against the future position of the player nearest your crosshair.")
   );
   private final ModuleSetting.Bool moduleBool5 = this.setting(
      new ModuleSetting.Bool("Pearl Safety", true)
         .description("Classifies pearl landings for void, lava, nearby-player, wall, and water risks.")
   );
   private final ModuleSetting.Bool moduleBool6 = this.setting(
      new ModuleSetting.Bool("Impact Marker", true)
         .description("Draws a cross on the block face or entity where the simulated projectile collides.")
   );
   private final ModuleSetting.Bool moduleBool7 = this.setting(
      new ModuleSetting.Bool("Impact Card", true)
         .description("Shows projected distance, flight time, charge, and available target or safety analysis at impact.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
            "Max Ticks", 150.0F, 40.0F, 260.0F, 1.0F
         )
         .description("Limits simulated 20 Hz flight steps; higher values extend long paths but add per-frame CPU work.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
            "Target Range", 64.0F, 16.0F, 120.0F, 1.0F
         )
         .description("Limits bow target analysis to players within this many blocks; it does not shorten the trajectory.")
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      new ModuleSetting.Number(
            "Line Width", 2.25F, 1.0F, 6.0F, 0.25F
         )
         .description("Sets the on-screen width of the trajectory core in pixels.")
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      new ModuleSetting.Number("Line Shadow", 0.7F, 0.0F, 1.0F, 0.05F)
         .description("Controls the opacity of the wider black halo behind the path; 0 removes it.")
   );
   private final ModuleSetting.Color moduleColor = this.setting(
      new ModuleSetting.Color("Start Color", -419430401).description("Sets the trajectory tint and alpha at the projectile's launch point.")
   );
   private final ModuleSetting.Color moduleColor2 = this.setting(
      new ModuleSetting.Color("End Color", -324552752)
         .description("Sets the trajectory tint and alpha near impact, forming a gradient from Start Color.")
   );
   private final TrajectoryOverlayRenderer renderer = new TrajectoryOverlayRenderer();
   private PearlsComponent.LastPrediction lastPrediction;

   public PearlsComponent() {
      super(
         ModuleBuilderData.builder("Projectile Predictor")
            .description("Predicts pearl and arrow paths, impacts, and landing or target risks.")
            .category(ModuleFeatureType.VISUALS)
            .build()
      );
   }

   @Override
   protected void onEnable() {
      this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState);
      this.on(EventAttackInputService.RENDER).run(this::updateState2);
   }

   @Override
   protected void onDisable() {
      this.lastPrediction = null;
      this.renderer.shutdown();
   }

   private void updateState(EventAttackInputService.WorldRender worldRender) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      LocalPlayer localPlayer = minecraft.player;
      ClientLevel clientLevel = minecraft.level;
      if (localPlayer != null && clientLevel != null) {
         PearlsComponent.ProjectileSpec projectileSpec = this.createProjectileSpec(localPlayer);
         if (projectileSpec == null) {
            this.lastPrediction = null;
         } else {
            TrajectoryOverlayRenderer.Trajectory trajectory = this.createTrajectory(clientLevel, localPlayer, worldRender.tickDelta(), projectileSpec);
            if (trajectory != null && trajectory.drawable()) {
               PearlsComponent.Intel intel = this.createIntel(clientLevel, localPlayer, projectileSpec, trajectory);
               this.lastPrediction = new PearlsComponent.LastPrediction(
                  trajectory,
                  projectileSpec,
                  intel,
                  new Matrix4f(worldRender.viewMatrix()),
                  new Matrix4f(worldRender.projectionMatrix()),
                  worldRender.cameraPos(),
                  System.nanoTime()
               );
               this.renderer
                  .render(
                     trajectory,
                     worldRender,
                     minecraft.getWindow().getWidth(),
                     minecraft.getWindow().getHeight(),
                     System.nanoTime() / 1.0E9,
                     new TrajectoryOverlayRenderer.Style(
                        (Integer)this.moduleColor.get(),
                        (Integer)this.moduleColor2.get(),
                        (Float)this.moduleNumber3.get(),
                        (Float)this.moduleNumber4.get(),
                        (Boolean)this.moduleBool6.get()
                     )
                  );
            } else {
               this.lastPrediction = null;
            }
         }
      } else {
         this.lastPrediction = null;
      }
   }

   private void updateState2(EventAttackInputService.Render render) {
      if ((Boolean)this.moduleBool7.get() && this.lastPrediction != null) {
         if (this.lastPrediction.intel != null || this.lastPrediction.trajectory.collided()) {
            if (System.nanoTime() - this.lastPrediction.frameNanos <= 150000000L) {
               Minecraft minecraft = CoreIsInitializedHandler.mc();
               if (!(minecraft.screen instanceof CoreSetBehindScreenService)) {
                  float currentWidth = CoreIsInitializedHandler.get().viewport().width();
                  float currentHeight = CoreIsInitializedHandler.get().viewport().height();
                  Vec3 vec3 = this.lastPrediction.intel != null ? this.lastPrediction.intel.anchor() : this.lastPrediction.trajectory.impact();
                  PearlsComponent.ScreenPoint screenPoint = this.createScreenPoint(vec3, this.lastPrediction, currentWidth, currentHeight);
                  if (screenPoint != null) {
                     double doubleValue = this.lastPrediction.trajectory.points().getFirst().distanceTo(this.lastPrediction.trajectory.impact());
                     float value = this.lastPrediction.trajectory.ticks() / 20.0F;
                     String currentText = this.lastPrediction.intel != null
                        ? this.lastPrediction.intel.title()
                        : this.lastPrediction.projectile.title() + " Landing";
                     String nextText = this.lastPrediction.intel != null
                        ? this.lastPrediction.intel.detail()
                        : (
                           this.lastPrediction.projectile.chargePercent() > 0
                              ? String.format(Locale.ROOT, "%.1fm  %.2fs  %d%%", doubleValue, value, this.lastPrediction.projectile.chargePercent())
                              : String.format(Locale.ROOT, "%.1fm  %.2fs", doubleValue, value)
                        );
                     CompositorPushPresentationScaleService compositorPushPresentationScale = CoreIsInitializedHandler.get().compositor();
                     float currentValue = compositorPushPresentationScale.textWidth(currentText, 9.0F);
                     float nextValue = compositorPushPresentationScale.textWidth(nextText, 7.5F);
                     float previousValue = Math.max(104.0F, Math.max(currentValue, nextValue) + 34.0F);
                     float sourceValue = 34.0F;
                     float targetValue = calculateValue2(
                        screenPoint.x + 12.0F,
                        6.0F,
                        currentWidth - previousValue - 6.0F
                     );
                     float inputValue = calculateValue2(
                        screenPoint.y - sourceValue - 12.0F,
                        6.0F,
                        currentHeight - sourceValue - 6.0F
                     );
                     compositorPushPresentationScale.addRect(
                        targetValue,
                        inputValue,
                        previousValue,
                        sourceValue,
                        10.0F,
                        10.0F,
                        10.0F,
                        10.0F,
                        1023410175,
                        16.0F,
                        8.0F,
                        570425344,
                        0.5F,
                        1442840575,
                        1.0F,
                        0.0F,
                        false,
                        620756991,
                        335544320
                     );
                     compositorPushPresentationScale.roundedRect(
                        targetValue + 10.0F,
                        inputValue + 10.0F,
                        3.0F,
                        sourceValue - 20.0F,
                        1.5F,
                        this.lastPrediction.intel != null
                           ? this.lastPrediction.intel.color()
                           : calculateValue((Integer)this.moduleColor2.get(), 168)
                     );
                     compositorPushPresentationScale.text(
                        targetValue + 23.0F,
                        inputValue + 6.0F,
                        currentText,
                        9.0F,
                        -434628574
                     );
                     compositorPushPresentationScale.text(
                        targetValue + 23.0F,
                        inputValue + 19.0F,
                        nextText,
                        7.5F,
                        -1776805854
                     );
                  }
               }
            }
         }
      }
   }

   private PearlsComponent.ProjectileSpec createProjectileSpec(LocalPlayer localPlayer) {
      if ((Boolean)this.moduleBool2.get() && localPlayer.isUsingItem() && localPlayer.getUseItem().is(Items.BOW)) {
         float value = BowItem.getPowerForTime(localPlayer.getTicksUsingItem());
         return value < 0.08F
            ? null
            : new PearlsComponent.ProjectileSpec(
               "Bow",
               value * 3.0,
               0.05,
               0.99,
               0.6,
               Math.round(value * 100.0F)
            );
      } else {
         return this.moduleBool.get() && this.checkCondition4(localPlayer)
            ? new PearlsComponent.ProjectileSpec(
               "Pearl",
               1.5,
               0.03,
               0.99,
               0.8,
               0
            )
            : null;
      }
   }

   private TrajectoryOverlayRenderer.Trajectory createTrajectory(ClientLevel clientLevel, LocalPlayer localPlayer, float value, PearlsComponent.ProjectileSpec projectileSpec) {
      Vec3 vec3 = localPlayer.getEyePosition().subtract(0.0, 0.1, 0.0);
      Vec3 currentVec3 = localPlayer.getViewVector(value).normalize().scale(projectileSpec.speed());
      if ((Boolean)this.moduleBool3.get()) {
         Vec3 nextVec3 = localPlayer.getDeltaMovement();
         currentVec3 = currentVec3.add(nextVec3.x, localPlayer.onGround() ? 0.0 : nextVec3.y, nextVec3.z);
      }

      ArrayList arrayList = new ArrayList();
      arrayList.add(vec3);
      int currentValue = Math.max(2, Math.round((Float)this.moduleNumber.get()));

      for (int index = 1; index <= currentValue; index++) {
         Vec3 previousVec3 = vec3.add(currentVec3);
         BlockHitResult blockHitResult = clientLevel.clip(new ClipContext(vec3, previousVec3, Block.COLLIDER, Fluid.NONE, localPlayer));
         Vec3 sourceVec3 = blockHitResult.getType() == Type.MISS ? previousVec3 : blockHitResult.getLocation();
         EntityHitResult entityHitResult = this.createEntityHitResult(clientLevel, localPlayer, vec3, sourceVec3);
         if (entityHitResult != null) {
            arrayList.add(entityHitResult.getLocation());
            Direction direction = Direction.getApproximateNearest(currentVec3.scale(-1.0));
            return new TrajectoryOverlayRenderer.Trajectory(arrayList, entityHitResult.getLocation(), direction, TrajectoryOverlayRenderer.ImpactKind.ENTITY, index);
         }

         if (blockHitResult.getType() != Type.MISS) {
            arrayList.add(blockHitResult.getLocation());
            return new TrajectoryOverlayRenderer.Trajectory(arrayList, blockHitResult.getLocation(), blockHitResult.getDirection(), TrajectoryOverlayRenderer.ImpactKind.BLOCK, index);
         }

         arrayList.add(previousVec3);
         vec3 = previousVec3;
         double doubleValue = this.checkCondition5(clientLevel, vec3) ? projectileSpec.waterDrag() : projectileSpec.airDrag();
         currentVec3 = currentVec3.scale(doubleValue).add(0.0, -projectileSpec.gravity(), 0.0);
         if (vec3.y < clientLevel.getMinY() - 16.0) {
            break;
         }
      }

      Vec3 targetVec3 = (Vec3)arrayList.getLast();
      return new TrajectoryOverlayRenderer.Trajectory(arrayList, targetVec3, Direction.UP, TrajectoryOverlayRenderer.ImpactKind.MISS, currentValue);
   }

   private PearlsComponent.Intel createIntel(
      ClientLevel clientLevel, LocalPlayer localPlayer, PearlsComponent.ProjectileSpec projectileSpec, TrajectoryOverlayRenderer.Trajectory trajectory
   ) {
      if ((Boolean)this.moduleBool4.get() && "Bow".equals(projectileSpec.title())) {
         PearlsComponent.Intel intel = this.createIntel2(localPlayer, trajectory);
         if (intel != null) {
            return intel;
         }
      }

      return this.moduleBool5.get() && "Pearl".equals(projectileSpec.title()) ? this.createIntel3(clientLevel, localPlayer, trajectory) : null;
   }

   private PearlsComponent.Intel createIntel2(LocalPlayer localPlayer, TrajectoryOverlayRenderer.Trajectory trajectory) {
      Player player = this.createPlayer(localPlayer);
      if (player == null) {
         return null;
      }

      List items = trajectory.points();
      int currentSize = trajectory.impactKind() == TrajectoryOverlayRenderer.ImpactKind.BLOCK ? trajectory.ticks() : items.size();

      for (int index = 1; index < items.size() && index <= currentSize; index++) {
         Vec3 vec3 = player.getDeltaMovement().scale(index);
         AABB aABB = player.getBoundingBox().move(vec3).inflate(0.22);
         if (this.checkCondition2(aABB, (Vec3)items.get(index - 1), (Vec3)items.get(index))) {
            float value = index / 20.0F;
            String text = String.format(Locale.ROOT, "%s  %.2fs window", player.getName().getString(), value);
            return new PearlsComponent.Intel("Target Window", text, -1474116258, player.position().add(vec3));
         }
      }

      if (trajectory.impactKind() == TrajectoryOverlayRenderer.ImpactKind.BLOCK) {
         String currentText = player.getName().getString() + "  blocked by terrain";
         return new PearlsComponent.Intel("Blocked Shot", currentText, -1461013752, trajectory.impact());
      } else {
         String nextText = player.getName().getString() + "  path misses future box";
         return new PearlsComponent.Intel("No Hit Window", nextText, -1466653768, player.position());
      }
   }

   private PearlsComponent.Intel createIntel3(ClientLevel clientLevel, LocalPlayer localPlayer, TrajectoryOverlayRenderer.Trajectory trajectory) {
      Vec3 vec3 = trajectory.impact();
      BlockPos blockPos = BlockPos.containing(vec3);
      String text = "Safe Landing";
      String currentText = "clear";
      int value = -1474116258;
      if (trajectory.impactKind() == TrajectoryOverlayRenderer.ImpactKind.MISS || vec3.y <= clientLevel.getMinY() + 2.0) {
         text = "Unsafe Pearl";
         currentText = "void risk";
         value = -1460714428;
      } else if (clientLevel.getFluidState(blockPos).is(FluidTags.LAVA) || clientLevel.getFluidState(blockPos.below()).is(FluidTags.LAVA)) {
         text = "Unsafe Pearl";
         currentText = "lava";
         value = -1460714428;
      } else if (this.checkCondition(clientLevel, localPlayer, vec3, 7.0)) {
         text = "Risky Pearl";
         currentText = "enemy nearby";
         value = -1461013752;
      } else if (trajectory.impactSide() != Direction.UP && trajectory.impactKind() == TrajectoryOverlayRenderer.ImpactKind.BLOCK) {
         text = "Risky Pearl";
         currentText = "wall impact";
         value = -1461013752;
      } else if (clientLevel.getFluidState(blockPos).is(FluidTags.WATER) || clientLevel.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
         text = "Safe Pearl";
         currentText = "water";
         value = -1472676360;
      }

      double doubleValue = trajectory.points().getFirst().distanceTo(vec3);
      String nextText = String.format(Locale.ROOT, "%s  %.1fm  %.2fs", currentText, doubleValue, trajectory.ticks() / 20.0F);
      return new PearlsComponent.Intel(text, nextText, value, vec3);
   }

   private Player createPlayer(LocalPlayer localPlayer) {
      ClientLevel clientLevel = CoreIsInitializedHandler.mc().level;
      if (clientLevel == null) {
         return null;
      }

      Vec3 vec3 = localPlayer.getEyePosition();
      Vec3 currentVec3 = localPlayer.getViewVector(1.0F).normalize();
      double doubleValue = 1.7976931348623157E308;
      Player player = null;

      for (Player currentPlayer : clientLevel.players()) {
         if (!FriendSyncController.excluded(FriendsMode.PROJECTILE_PREDICTOR, currentPlayer)
            && !CompatDecisionTracker.excluded(AntibotFeatureType.PROJECTILE_INFO, currentPlayer)
            && currentPlayer != localPlayer
            && !currentPlayer.isSpectator()
            && currentPlayer.isPickable()) {
            double currentDoubleValue = Math.sqrt(currentPlayer.distanceToSqr(localPlayer));
            if (!(currentDoubleValue > ((Float)this.moduleNumber2.get()).floatValue())) {
               Vec3 nextVec3 = currentPlayer.getEyePosition().subtract(vec3);
               if (!(nextVec3.lengthSqr() < 0.001)) {
                  double nextDoubleValue = 1.0 - currentVec3.dot(nextVec3.normalize());
                  double previousDoubleValue = nextDoubleValue * 42.0
                     + currentDoubleValue * 0.012;
                  if (nextDoubleValue < 0.045 && previousDoubleValue < doubleValue) {
                     doubleValue = previousDoubleValue;
                     player = currentPlayer;
                  }
               }
            }
         }
      }

      return player;
   }

   private boolean checkCondition(ClientLevel clientLevel, LocalPlayer localPlayer, Vec3 vec3, double doubleValue) {
      double currentDoubleValue = doubleValue * doubleValue;

      for (Player player : clientLevel.players()) {
         if (!FriendSyncController.excluded(FriendsMode.PROJECTILE_PREDICTOR, player)
            && !CompatDecisionTracker.excluded(AntibotFeatureType.PROJECTILE_INFO, player)
            && player != localPlayer
            && !player.isSpectator()
            && player.position().distanceToSqr(vec3) <= currentDoubleValue) {
            return true;
         }
      }

      return false;
   }

   private boolean checkCondition2(AABB aABB, Vec3 vec3, Vec3 currentVec3) {
      double doubleValue = 0.0;
      double currentDoubleValue = 1.0;
      double nextDoubleValue = currentVec3.x - vec3.x;
      double previousDoubleValue = currentVec3.y - vec3.y;
      double sourceDoubleValue = currentVec3.z - vec3.z;
      double[] doubles = this.createDouble(vec3.x, nextDoubleValue, aABB.minX, aABB.maxX, doubleValue, currentDoubleValue);
      if (doubles == null) {
         return false;
      }

      doubleValue = doubles[0];
      currentDoubleValue = doubles[1];
      doubles = this.createDouble(vec3.y, previousDoubleValue, aABB.minY, aABB.maxY, doubleValue, currentDoubleValue);
      if (doubles == null) {
         return false;
      }

      doubleValue = doubles[0];
      currentDoubleValue = doubles[1];
      doubles = this.createDouble(vec3.z, sourceDoubleValue, aABB.minZ, aABB.maxZ, doubleValue, currentDoubleValue);
      return doubles != null;
   }

   private double[] createDouble(double doubleValue, double currentDoubleValue, double nextDoubleValue, double previousDoubleValue, double sourceDoubleValue, double targetDoubleValue) {
      if (!(Math.abs(currentDoubleValue) < 1.0E-7)) {
         double inputDoubleValue = 1.0 / currentDoubleValue;
         double outputDoubleValue = (nextDoubleValue - doubleValue) * inputDoubleValue;
         double resultDoubleValue = (previousDoubleValue - doubleValue) * inputDoubleValue;
         if (outputDoubleValue > resultDoubleValue) {
            double candidateDoubleValue = outputDoubleValue;
            outputDoubleValue = resultDoubleValue;
            resultDoubleValue = candidateDoubleValue;
         }

         sourceDoubleValue = Math.max(sourceDoubleValue, outputDoubleValue);
         targetDoubleValue = Math.min(targetDoubleValue, resultDoubleValue);
         return sourceDoubleValue <= targetDoubleValue ? new double[]{sourceDoubleValue, targetDoubleValue} : null;
      } else {
         return doubleValue >= nextDoubleValue && doubleValue <= previousDoubleValue ? new double[]{sourceDoubleValue, targetDoubleValue} : null;
      }
   }

   private EntityHitResult createEntityHitResult(ClientLevel clientLevel, LocalPlayer localPlayer, Vec3 vec3, Vec3 currentVec3) {
      AABB aABB = new AABB(vec3, currentVec3).inflate(0.35);
      return ProjectileUtil.getEntityHitResult(clientLevel, localPlayer, vec3, currentVec3, aABB, this::checkCondition3, 0.0F);
   }

   private boolean checkCondition3(Entity entity) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      return entity != minecraft.player && !entity.isSpectator() && entity.isPickable();
   }

   private boolean checkCondition4(LocalPlayer localPlayer) {
      return localPlayer.getMainHandItem().is(Items.ENDER_PEARL) || localPlayer.getOffhandItem().is(Items.ENDER_PEARL);
   }

   private boolean checkCondition5(ClientLevel clientLevel, Vec3 vec3) {
      return clientLevel.getFluidState(BlockPos.containing(vec3)).is(FluidTags.WATER);
   }

   private PearlsComponent.ScreenPoint createScreenPoint(Vec3 vec3, PearlsComponent.LastPrediction lastPrediction, float width, float height) {
      Vec3 currentVec3 = lastPrediction.cameraPos;
      Vector4f vector4f = new Vector4f((float)(vec3.x - currentVec3.x), (float)(vec3.y - currentVec3.y), (float)(vec3.z - currentVec3.z), 1.0F);
      lastPrediction.view.transform(vector4f);
      lastPrediction.projection.transform(vector4f);
      if (vector4f.w <= 0.05F) {
         return null;
      }

      float value = vector4f.x / vector4f.w;
      float currentValue = vector4f.y / vector4f.w;
      return !(value < -1.25F)
            && !(value > 1.25F)
            && !(currentValue < -1.25F)
            && !(currentValue > 1.25F)
         ? new PearlsComponent.ScreenPoint(
            (value * 0.5F + 0.5F) * width,
            (0.5F - currentValue * 0.5F) * height
         )
         : null;
   }

   private static int calculateValue(int value, int currentValue) {
      return (currentValue & 0xFF) << 24 | value & 16777215;
   }

   private static float calculateValue2(float value, float currentValue, float nextValue) {
      return Math.max(currentValue, Math.min(nextValue, value));
   }

   private record Intel(String title, String detail, int color, Vec3 anchor) {
   }

   private record LastPrediction(
      TrajectoryOverlayRenderer.Trajectory trajectory,
      PearlsComponent.ProjectileSpec projectile,
      PearlsComponent.Intel intel,
      Matrix4f view,
      Matrix4f projection,
      Vec3 cameraPos,
      long frameNanos
   ) {
   }

   private record ProjectileSpec(String title, double speed, double gravity, double airDrag, double waterDrag, int chargePercent) {
   }

   private record ScreenPoint(float x, float y) {
   }
}
