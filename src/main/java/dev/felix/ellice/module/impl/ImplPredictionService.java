package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatBeginTickService;
import dev.felix.ellice.compat.CompatContextReadyService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.bow.BowMotionTracker;
import dev.felix.ellice.feature.bow.BowNextService;
import dev.felix.ellice.feature.bow.BowReleaseTracker;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.Items;

public final class ImplPredictionService extends ModuleSettingsService {
  private final ModuleNameService fbxjg2ukdkor;
  private final ModuleNameService fjog7l7sj9p5;
  private final ModuleNameService f92fuqp628cg;
  private final ModuleSetting.Bool moduleBool;
  private final ModuleSetting.Bool moduleBool2;
  private final ModuleSetting.Number fnxde03awan;
  private final ModuleSetting.Mode fiufnku5hemq;
  private final ModuleSetting.Number moduleNumber2;
  private final ModuleSetting.Bool f18y45asie36;
  private final ModuleSetting.Number fafryocaquy5;
  private final ModuleSetting.Number moduleNumber4;
  private final ModuleSetting.Number moduleNumber5;
  private final BowMotionTracker fatzpkbhcg7t;
  private final BowReleaseTracker values2;
  private final CombatCommitService frhll77n6bm;
  private final BowNextService bowNextService;
  private CompatContextReadyService.Prediction prediction2;
  private RotationData rotationData;
  private UUID uUID;
  private long fbclw2cmdvkx;
  private long timestamp2;
  private int count;
  private boolean enabled;

  public ImplPredictionService() {
    super(
        ModuleBuilderData.builder("BowAimbot")
            .description(
                "Predicts opponents while you draw your bow and automatically releases verified"
                    + " shots.")
            .category(ModuleFeatureType.COMBAT)
            .build());
    this.fbxjg2ukdkor =
        this.settingCategory("Targets")
            .description(
                "Visible opponents while you right-click or draw a bow in your main hand.");
    this.fjog7l7sj9p5 =
        this.settingCategory("Shooting")
            .description(
                "You start drawing; automatic release waits for a verified trajectory through"
                    + " Vanilla.");
    this.f92fuqp628cg =
        this.settingCategory("Rotation")
            .description("Uses the same silent rotation and corrected movement path as KillAura.");
    this.moduleBool =
        this.setting(
            this.fbxjg2ukdkor,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Players", true)
                    .description("Targets visible enemy players; excludes allies and spectators."));
    this.moduleBool2 =
        this.setting(
            this.fbxjg2ukdkor,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Mobs", true)
                    .description("Also targets visible, attackable mobs."));
    this.fnxde03awan =
        this.setting(
            this.fbxjg2ukdkor,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Range",
                        Float.intBitsToFloat(1111490560),
                        Float.intBitsToFloat(1082130432),
                        Float.intBitsToFloat(1117782016),
                        1.0f)
                    .description(
                        "Maximum target distance in blocks. Long flight times make sudden direction"
                            + " changes less predictable."));
    this.fiufnku5hemq =
        this.setting(
            this.fbxjg2ukdkor,
            (ModuleSetting.Mode)
                new ModuleSetting.Mode("FOV", new String[] {"180°", "360°"}, "360°")
                    .description("Searches in front of the camera or all around the player."));
    this.moduleNumber2 =
        this.setting(
            this.fjog7l7sj9p5,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Draw Ticks",
                        Float.intBitsToFloat(1101004800),
                        Float.intBitsToFloat(1092616192),
                        Float.intBitsToFloat(1101004800),
                        1.0f)
                    .description(
                        "Minimum real draw duration before release. 20 ticks gives full power;"
                            + " fewer ticks trade damage and reach for speed."));
    this.f18y45asie36 =
        this.setting(
            this.fjog7l7sj9p5,
            (ModuleSetting.Bool)
                new ModuleSetting.Bool("Latency Compensation", true)
                    .description(
                        "Compensates observed packet age and round-trip latency using the latest"
                            + " server position, capped at four network ticks."));
    this.fafryocaquy5 =
        this.setting(
            this.fjog7l7sj9p5,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Shot Quality",
                        Float.intBitsToFloat(1116471296),
                        Float.intBitsToFloat(1109393408),
                        Float.intBitsToFloat(1119748096),
                        Float.intBitsToFloat(1084227584))
                    .description(
                        "Minimum weighted coverage across learned movement patterns, measured"
                            + " prediction error and Vanilla arrow spread. Higher values wait for"
                            + " stronger opportunities; this is not a guaranteed live hit rate."));
    this.moduleNumber4 =
        this.setting(
            this.f92fuqp628cg,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Turn Speed",
                        Float.intBitsToFloat(1113325568),
                        Float.intBitsToFloat(1084227584),
                        Float.intBitsToFloat(1127481344),
                        1.0f)
                    .description(
                        "Maximum turn per movement tick. Shots wait for the sent rotation to"
                            + " intersect the predicted target."));
    this.moduleNumber5 =
        this.setting(
            this.f92fuqp628cg,
            (ModuleSetting.Number)
                new ModuleSetting.Number(
                        "Smoothness",
                        Float.intBitsToFloat(1048576000),
                        0.0f,
                        1.0f,
                        Float.intBitsToFloat(1008981770))
                    .description(
                        "Controls rotation easing while retaining sensitivity quantization and"
                            + " movement correction."));
    this.fatzpkbhcg7t = new BowMotionTracker();
    this.values2 = new BowReleaseTracker();
    this.frhll77n6bm = new CombatCommitService();
    this.bowNextService = new BowNextService();
    this.timestamp2 = -1L;
  }

  @Override
  protected void onEnable() {
    this.updateState6();
    CompatContextReadyService.active(true);
    this.fbclw2cmdvkx = 0L;
    this.timestamp2 = -1L;
    this.count = 0;
    this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT)
        .priority(EventIsAfterHandler.Priority.EARLY)
        .run(p0 -> this.updateState());
    this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE).run(p0 -> this.updateState3());
    this.on(EventAttackInputService.ATTACK_INPUT)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(p0 -> this.updateState4());
    this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED)
        .run(
            outgoingPacketAccepted -> {
              CompatContextReadyService.observeAccepted(outgoingPacketAccepted.packet());
              if (outgoingPacketAccepted.packet()
                  instanceof ServerboundUseItemPacket useItemPacket) {
                Minecraft minecraft = CoreIsInitializedHandler.mc();
                if (minecraft.player != null
                    && minecraft.player.getItemInHand(useItemPacket.getHand()).is(Items.BOW)) {
                  this.timestamp2 = this.fbclw2cmdvkx;
                }
              }
            });
    this.on(EventAttackInputService.WORLD).run(p0 -> this.updateState6());
    this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(p0 -> this.updateState6());
  }

  @Override
  protected void onDisable() {
    CompatContextReadyService.active(false);
    this.updateState6();
  }

  public Optional<CompatContextReadyService.Prediction> prediction() {
    return Optional.ofNullable(this.prediction2);
  }

  public int shots() {
    return this.count;
  }

  public void suspendForScaffold() {
    this.updateState6();
  }

  private static boolean checkCondition() {
    return CoreIsInitializedHandler.get()
        .modules()
        .get(ImplSuspendForPearlService.class)
        .filter(Module::isEnabled)
        .isPresent();
  }

  private void updateState() {
    ++this.fbclw2cmdvkx;
    if (checkCondition()) {
      this.suspendForScaffold();
      return;
    }
    final Minecraft mc = CoreIsInitializedHandler.mc();
    this.prediction2 = null;
    this.enabled = CompatContextReadyService.contextReady(mc);
    if (this.enabled) {
      final Optional<RotationData> claim = CombatOwnsService.claim(CombatOwnsService.Owner.BOW);
      if (this.rotationData == null) {
        this.rotationData = claim.orElse(null);
      }
    } else if (CoreIsInitializedHandler.get()
            .modules()
            .get(ImplDecisionTracker.class)
            .filter(Module::isEnabled)
            .isPresent()
        && CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
      CombatOwnsService.transfer(CombatOwnsService.Owner.BOW, CombatOwnsService.Owner.KILL_AURA);
      this.updateState7();
    }
    if (!CompatContextReadyService.trackingReady(mc)) {
      this.updateState5(true);
      return;
    }
    final LivingEntity livingEntity =
        CompatContextReadyService.target(
                mc,
                this.moduleBool.get(),
                this.moduleBool2.get(),
                this.fnxde03awan.get(),
                this.fiufnku5hemq.get().equals("360°")
                    ? Double.longBitsToDouble(4645040803167600640L)
                    : Double.longBitsToDouble(4640537203540230144L),
                this.uUID)
            .orElse(null);
    if (livingEntity == null) {
      this.updateState5(true);
      return;
    }
    if (!livingEntity.getUUID().equals(this.uUID)) {
      this.uUID = livingEntity.getUUID();
      this.fatzpkbhcg7t.clear();
      this.values2.resetAim();
      this.bowNextService.reset();
    }
    final BowMotionTracker.Motion observe =
        this.fatzpkbhcg7t.observe(
            this.fbclw2cmdvkx,
            this.uUID,
            CompatContextReadyService.observedPosition((Entity) livingEntity),
            livingEntity.onGround());
    if (!this.enabled) {
      this.updateState5(false);
      return;
    }
    this.prediction2 =
        CompatContextReadyService.predict(
                mc,
                livingEntity,
                observe,
                Math.max(
                    Math.round(this.moduleNumber2.get()),
                    CompatContextReadyService.drawing(mc)
                        ? (mc.player.getTicksUsingItem() + 1)
                        : 0),
                this.f18y45asie36.get())
            .orElse(null);
    if (this.prediction2 == null) {
      this.updateState5(false);
      return;
    }
    final RotationData rotationData = new RotationData(mc.player.getYRot(), mc.player.getXRot());
    final RotationData rotationData2 =
        (this.rotationData == null) ? rotationData : this.rotationData;
    if (this.fbclw2cmdvkx != this.timestamp2 + 1L) {
      this.rotationData =
          this.bowNextService
              .next(
                  rotationData2,
                  this.prediction2.solution().rotation(),
                  (double) mc.options.sensitivity().get(),
                  this.moduleNumber4.get(),
                  this.moduleNumber5.get())
              .rotation();
    }
    this.updateState2();
  }

  private void updateState2() {
    if (!CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
      return;
    }
    final Minecraft mc = CoreIsInitializedHandler.mc();
    if (mc.player == null || this.rotationData == null) {
      this.frhll77n6bm.clear();
      return;
    }
    RotationPublishService.publish(
        new RotationData(mc.player.getYRot(), mc.player.getXRot()), this.rotationData);
    final Options options = mc.options;
    if (this.frhll77n6bm.commit(
        this.fbclw2cmdvkx,
        mc.player.getYRot(),
        this.rotationData,
        new ScaffoldRemapService.Input(
            options.keyUp.isDown(),
            options.keyDown.isDown(),
            options.keyLeft.isDown(),
            options.keyRight.isDown(),
            options.keyJump.isDown(),
            options.keyShift.isDown(),
            options.keySprint.isDown() || ImplRequestsSprintClient.requestsSprint(mc)),
        CompatContextReadyService.drawing(mc))) {
      mc.player.setSprinting(false);
    }
  }

  private void updateState3() {
    if (!CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
      return;
    }
    if (LocalPhysicsFrameBus.wasAborted(this.fbclw2cmdvkx)) {
      this.updateState6();
      return;
    }
    if (this.rotationData == null || CoreIsInitializedHandler.mc().player == null) {
      return;
    }
    this.frhll77n6bm.reconcile(
        this.fbclw2cmdvkx,
        PlacementOverrideBus.applied()
            .orElseGet(
                () -> {
                  final Input keyPresses = CoreIsInitializedHandler.mc().player.input.keyPresses;
                  return new ScaffoldRemapService.Input(
                      keyPresses.forward(),
                      keyPresses.backward(),
                      keyPresses.left(),
                      keyPresses.right(),
                      keyPresses.jump(),
                      keyPresses.shift(),
                      keyPresses.sprint());
                }));
  }

  private void updateState4() {
    final Minecraft mc = CoreIsInitializedHandler.mc();
    final RotationObserveService.Snapshot snapshot = RotationObserveService.snapshot().orElse(null);
    if (!CompatBeginTickService.canAct()
        || !CombatOwnsService.owns(CombatOwnsService.Owner.BOW)
        || this.prediction2 == null
        || this.rotationData == null
        || snapshot == null
        || !CompatContextReadyService.contextReady(mc)
        || snapshot
                .wireEyePosition()
                .subtract(CompatContextReadyService.vector(mc.player.getEyePosition()))
                .length()
            > Double.longBitsToDouble(4591870180066957722L)
        || RotationData.distance(
                snapshot.rotation(),
                new RotationData(
                    (float) this.rotationData.yaw(), (float) this.rotationData.pitch()))
            > Double.longBitsToDouble(4532020583610935537L)) {
      this.values2.resetAim();
      return;
    }
    if (!CompatContextReadyService.drawing(mc)) {
      this.values2.resetAim();
      return;
    }
    final int ticksUsingItem = mc.player.getTicksUsingItem();
    if (this.values2.ready(
            this.fbclw2cmdvkx,
            this.prediction2.uuid(),
            ticksUsingItem,
            Math.round(this.moduleNumber2.get()),
            this.prediction2.confidence(),
            ticksUsingItem >= Math.round(this.moduleNumber2.get()) - 2
                && this.prediction2.confidence() >= Double.longBitsToDouble(4604029899060858061L)
                && CompatContextReadyService.verifiedShot(
                    mc,
                    this.prediction2,
                    snapshot.rotation(),
                    snapshot.wireEyePosition(),
                    this.fnxde03awan.get(),
                    this.fafryocaquy5.get() / Double.longBitsToDouble(4636737291354636288L)))
        && CompatContextReadyService.release(mc)) {
      ++this.count;
      this.values2.released(this.fbclw2cmdvkx);
    }
  }

  private void updateState5(final boolean b) {
    if (b) {
      this.uUID = null;
      this.fatzpkbhcg7t.clear();
    }
    this.prediction2 = null;
    this.values2.resetAim();
    final Minecraft mc = CoreIsInitializedHandler.mc();
    if (mc.player == null || mc.level == null) {
      this.updateState6();
      return;
    }
    if (this.rotationData == null) {
      this.updateState7();
      return;
    }
    if (mc.player.isPassenger()
        || mc.player.isFallFlying()
        || mc.player.isAutoSpinAttack()
        || mc.player.isInWater()
        || mc.player.isInLava()
        || mc.player.isSwimming()
        || mc.player.getAbilities().flying) {
      this.updateState6();
      return;
    }
    final BowNextService.Step next =
        this.bowNextService.next(
            this.rotationData,
            new RotationData(mc.player.getYRot(), mc.player.getXRot()),
            (double) mc.options.sensitivity().get(),
            this.moduleNumber4.get(),
            this.moduleNumber5.get());
    if (next.settled()) {
      this.updateState7();
      return;
    }
    if (this.fbclw2cmdvkx == this.timestamp2 + 1L) {
      this.updateState2();
      return;
    }
    this.rotationData = next.rotation();
    this.updateState2();
  }

  private void updateState6() {
    this.enabled = false;
    this.fatzpkbhcg7t.clear();
    this.values2.clear();
    this.updateState7();
    this.uUID = null;
    this.prediction2 = null;
    this.timestamp2 = -1L;
  }

  private void updateState7() {
    this.bowNextService.reset();
    if (this.enabled) {
      CombatOwnsService.clearFrame(CombatOwnsService.Owner.BOW);
    } else {
      CombatOwnsService.release(CombatOwnsService.Owner.BOW);
    }
    this.rotationData = null;
  }
}
