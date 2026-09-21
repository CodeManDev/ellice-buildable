package dev.felix.ellice.compat.adapter.mc26_1;

import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.CompatAvailableService;
import dev.felix.ellice.compat.CompatDecisionTracker;
import dev.felix.ellice.compat.CompatLocalBoxService;
import dev.felix.ellice.compat.CompatStatusTracker;
import dev.felix.ellice.compat.FriendSyncController;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.combat.CombatData;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationFrameContext;
import dev.felix.ellice.feature.rotation.RotationMode;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationTarget;
import dev.felix.ellice.feature.rotation.RotationTargetFilter;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.friends.FriendsMode;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

final class MinecraftCombatCompatibility implements CombatCompatibility {
  static final MinecraftCombatCompatibility INSTANCE = new MinecraftCombatCompatibility();

  private MinecraftCombatCompatibility() {}

  @Override
  public Optional<RotationFrameContext> capture(
      Minecraft minecraft, RotationTargetFilter rotationTargetFilter) {
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()) {
      LocalPlayer localPlayer = minecraft.player;
      Vec3 vec3 = localPlayer.getEyePosition();
      double doubleValue = rotationTargetFilter.maxRange() * rotationTargetFilter.maxRange();
      ArrayList arrayList = new ArrayList();

      for (Entity entity : minecraft.level.entitiesForRendering()) {
        if (checkCondition(localPlayer, entity)) {
          RotationMode rotationMode;
          if (entity instanceof Player) {
            if (!rotationTargetFilter.players()) {
              continue;
            }

            rotationMode = RotationMode.PLAYER;
          } else {
            if (!(entity instanceof Mob) || !rotationTargetFilter.mobs()) {
              continue;
            }

            rotationMode = RotationMode.MOB;
          }

          if (rotationTargetFilter.includeInvisible() || !entity.isInvisibleTo(localPlayer)) {
            AABB aABB = entity.getBoundingBox();
            if (!(aABB.distanceToSqr(vec3) > doubleValue)) {
              RotationBoundingBox rotationBoundingBox = CompatLocalBoxService.aimBox(entity);
              ArrayList currentArrayList = collectValues(minecraft, vec3, rotationBoundingBox);
              if (!currentArrayList.isEmpty()) {
                arrayList.add(
                    new RotationTarget(
                        entity.getId(),
                        entity.getUUID(),
                        rotationMode,
                        rotationBoundingBox,
                        currentArrayList,
                        ((LivingEntity) entity).getHealth()
                            + ((LivingEntity) entity).getAbsorptionAmount()));
              }
            }
          }
        }
      }

      return Optional.of(
          new RotationFrameContext(
              new RotationVector(vec3.x, vec3.y, vec3.z),
              new RotationData(localPlayer.getYRot(), localPlayer.getXRot()),
              (Double) minecraft.options.sensitivity().get(),
              arrayList));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void applyServerRotation(Minecraft minecraft, RotationData rotationData) {
    if (minecraft.player != null) {
      RotationPublishService.publish(
          new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()), rotationData);
    }
  }

  @Override
  public void clearServerRotation(Minecraft minecraft) {
    RotationPublishService.clear();
  }

  @Override
  public boolean hasBlockLineOfSight(
      Minecraft minecraft, RotationVector rotationVector, RotationVector currentRotationVector) {
    if (minecraft.level != null && minecraft.player != null) {
      BlockHitResult currentX =
          minecraft.level.clip(
              new ClipContext(
                  new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z()),
                  new Vec3(
                      currentRotationVector.x(),
                      currentRotationVector.y(),
                      currentRotationVector.z()),
                  Block.COLLIDER,
                  Fluid.NONE,
                  minecraft.player));
      return currentX.getType() == Type.MISS;
    } else {
      return false;
    }
  }

  @Override
  public Optional<CombatData> captureAttack(
      Minecraft minecraft, RotationTarget rotationTarget, double doubleValue) {
    return this.findResult(minecraft, rotationTarget, doubleValue)
        .map(MinecraftCombatCompatibility.AttackCandidate::state);
  }

  @Override
  public boolean attack(Minecraft minecraft, RotationTarget rotationTarget, double doubleValue) {
    return this.attack(minecraft, rotationTarget, doubleValue, 1.0F);
  }

  @Override
  public boolean attack(
      Minecraft minecraft, RotationTarget rotationTarget, double doubleValue, float value) {
    CompatStatusTracker.beforeInteraction();
    MinecraftCombatCompatibility.AttackCandidate attackCandidate =
        this.findResult(minecraft, rotationTarget, doubleValue).orElse(null);
    float currentValue = Float.isFinite(value) ? Math.max(0.0F, Math.min(1.0F, value)) : 1.0F;
    return attackCandidate != null && !(attackCandidate.state().attackStrength() < currentValue)
        ? CompatAvailableService.attack(
            minecraft, attackCandidate.hit(), attackCandidate.sentRotation(), currentValue)
        : false;
  }

  private Optional<MinecraftCombatCompatibility.AttackCandidate> findResult(
      Minecraft minecraft, RotationTarget rotationTarget, double doubleValue) {
    if (rotationTarget != null && CompatAvailableService.availableForPlanning(minecraft)) {
      LocalPlayer localPlayer = minecraft.player;
      Entity entity = minecraft.level.getEntity(rotationTarget.entityId());
      if (entity != null
          && entity.getUUID().equals(rotationTarget.uuid())
          && checkCondition(localPlayer, entity)) {
        RotationObserveService.Snapshot currentSnapshot =
            RotationObserveService.snapshot().orElse(null);
        if (currentSnapshot == null) {
          return Optional.empty();
        }

        Vec3 vec3 = localPlayer.getEyePosition();
        RotationVector rotationVector = new RotationVector(vec3.x, vec3.y, vec3.z);
        EntityHitResult entityHitResult =
            CompatAvailableService.pick(
                    minecraft, rotationVector, currentSnapshot.rotation(), doubleValue)
                .orElse(null);
        if (entityHitResult != null && entityHitResult.getEntity() == entity) {
          if (!localPlayer.isPassenger()) {
            EntityHitResult currentEntityHitResult =
                CompatAvailableService.pick(
                        minecraft,
                        currentSnapshot.wireEyePosition(),
                        currentSnapshot.rotation(),
                        doubleValue)
                    .orElse(null);
            if (currentEntityHitResult == null || currentEntityHitResult.getEntity() != entity) {
              return Optional.empty();
            }
          }

          if (CompatLocalBoxService.reachable(
                  entity,
                  rotationVector,
                  currentSnapshot.rotation(),
                  doubleValue,
                  localPlayer.entityInteractionRange())
              && (localPlayer.isPassenger()
                  || CompatLocalBoxService.reachable(
                      entity,
                      currentSnapshot.wireEyePosition(),
                      currentSnapshot.rotation(),
                      doubleValue,
                      localPlayer.entityInteractionRange()))) {
            float value = localPlayer.getAttackStrengthScale(0.0F);
            float currentValue = localPlayer.getCurrentItemAttackStrengthDelay();
            return Float.isFinite(value) && Float.isFinite(currentValue) && !(currentValue <= 0.0F)
                ? Optional.of(
                    new MinecraftCombatCompatibility.AttackCandidate(
                        entityHitResult,
                        currentSnapshot.rotation(),
                        new CombatData(
                            value, currentValue, Math.max(0, ((LivingEntity) entity).hurtTime))))
                : Optional.empty();
          } else {
            return Optional.empty();
          }
        } else {
          return Optional.empty();
        }
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  private static boolean checkCondition(Player player, Entity entity) {
    if (FriendSyncController.excluded(FriendsMode.KILL_AURA, entity)) {
      return false;
    } else {
      return CompatDecisionTracker.excluded(AntibotFeatureType.KILL_AURA, entity)
          ? false
          : (entity instanceof Player || entity instanceof Mob)
              && entity != player
              && entity.isAlive()
              && !entity.isSpectator()
              && entity.isPickable()
              && entity.isAttackable()
              && !player.isAlliedTo(entity)
              && !entity.isAlliedTo(player)
              && !(entity instanceof Player currentPlayer && !player.canHarmPlayer(currentPlayer));
    }
  }

  private static ArrayList<RotationVector> collectValues(
      Minecraft minecraft, Vec3 vec3, RotationBoundingBox rotationBoundingBox) {
    ArrayList arrayList = new ArrayList();

    for (RotationVector rotationVector : rotationBoundingBox.aimSamples()) {
      if (INSTANCE.hasBlockLineOfSight(
          minecraft, new RotationVector(vec3.x, vec3.y, vec3.z), rotationVector)) {
        arrayList.add(rotationVector);
      }
    }

    return arrayList;
  }

  private record AttackCandidate(
      EntityHitResult hit, RotationData sentRotation, CombatData state) {}
}
