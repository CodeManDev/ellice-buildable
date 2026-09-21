



package dev.felix.ellice.module.impl;

import net.minecraft.network.protocol.Packet;
import dev.felix.ellice.compat.CompatBeginTickService;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import net.minecraft.world.entity.player.Input;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import net.minecraft.client.Options;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.module.Module;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import java.util.UUID;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.compat.CompatContextReadyService;
import dev.felix.ellice.feature.bow.BowNextService;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.bow.BowReleaseTracker;
import dev.felix.ellice.feature.bow.BowMotionTracker;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSettingsService;

public final class ImplPredictionService extends ModuleSettingsService
{
    private final ModuleNameService fbxjg2ukdkor;
    private final ModuleNameService fjog7l7sj9p5;
    private final ModuleNameService f92fuqp628cg;
    private final ModuleSetting.Bool fbp4s4key4tn;
    private final ModuleSetting.Bool fabrsaj5nh3i;
    private final ModuleSetting.Number fnxde03awan;
    private final ModuleSetting.Mode fiufnku5hemq;
    private final ModuleSetting.Number f8i3v1yoca61;
    private final ModuleSetting.Bool f18y45asie36;
    private final ModuleSetting.Number fafryocaquy5;
    private final ModuleSetting.Number fi691oy3g6n3;
    private final ModuleSetting.Number f67spqtw4f14;
    private final BowMotionTracker fatzpkbhcg7t;
    private final BowReleaseTracker fiwzl5nthw36;
    private final CombatCommitService frhll77n6bm;
    private final BowNextService fdcyqi371lj7;
    private CompatContextReadyService.Prediction f2k2ocdleb48;
    private RotationData f4t6yyxfgix0;
    private UUID fhmi3ykae2l2;
    private long fbclw2cmdvkx;
    private long fie9rnospv75;
    private int f4komu552zk4;
    private boolean fe7l55ofrkc9;
    
    public ImplPredictionService() {
        super(ModuleBuilderData.builder("BowAimbot").description("Predicts opponents while you draw your bow and automatically releases verified shots.").category(ModuleFeatureType.COMBAT).build());
        this.fbxjg2ukdkor = this.settingCategory("Targets").description("Visible opponents while you right-click or draw a bow in your main hand.");
        this.fjog7l7sj9p5 = this.settingCategory("Shooting").description("You start drawing; automatic release waits for a verified trajectory through Vanilla.");
        this.f92fuqp628cg = this.settingCategory("Rotation").description("Uses the same silent rotation and corrected movement path as KillAura.");
        this.fbp4s4key4tn = this.setting(this.fbxjg2ukdkor, (ModuleSetting.Bool)new ModuleSetting.Bool("Players", true).description("Targets visible enemy players; excludes allies and spectators."));
        this.fabrsaj5nh3i = this.setting(this.fbxjg2ukdkor, (ModuleSetting.Bool)new ModuleSetting.Bool("Mobs", true).description("Also targets visible, attackable mobs."));
        this.fnxde03awan = this.setting(this.fbxjg2ukdkor, (ModuleSetting.Number)new ModuleSetting.Number("Range", Float.intBitsToFloat(1111490560), Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1117782016), 1.0f).description("Maximum target distance in blocks. Long flight times make sudden direction changes less predictable."));
        this.fiufnku5hemq = this.setting(this.fbxjg2ukdkor, (ModuleSetting.Mode)new ModuleSetting.Mode("FOV", new String[] { "180°", "360°" }, "360°").description("Searches in front of the camera or all around the player."));
        this.f8i3v1yoca61 = this.setting(this.fjog7l7sj9p5, (ModuleSetting.Number)new ModuleSetting.Number("Draw Ticks", Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1101004800), 1.0f).description("Minimum real draw duration before release. 20 ticks gives full power; fewer ticks trade damage and reach for speed."));
        this.f18y45asie36 = this.setting(this.fjog7l7sj9p5, (ModuleSetting.Bool)new ModuleSetting.Bool("Latency Compensation", true).description("Compensates observed packet age and round-trip latency using the latest server position, capped at four network ticks."));
        this.fafryocaquy5 = this.setting(this.fjog7l7sj9p5, (ModuleSetting.Number)new ModuleSetting.Number("Shot Quality", Float.intBitsToFloat(1116471296), Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1119748096), Float.intBitsToFloat(1084227584)).description("Minimum weighted coverage across learned movement patterns, measured prediction error and Vanilla arrow spread. Higher values wait for stronger opportunities; this is not a guaranteed live hit rate."));
        this.fi691oy3g6n3 = this.setting(this.f92fuqp628cg, (ModuleSetting.Number)new ModuleSetting.Number("Turn Speed", Float.intBitsToFloat(1113325568), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1127481344), 1.0f).description("Maximum turn per movement tick. Shots wait for the sent rotation to intersect the predicted target."));
        this.f67spqtw4f14 = this.setting(this.f92fuqp628cg, (ModuleSetting.Number)new ModuleSetting.Number("Smoothness", Float.intBitsToFloat(1048576000), 0.0f, 1.0f, Float.intBitsToFloat(1008981770)).description("Controls rotation easing while retaining sensitivity quantization and movement correction."));
        this.fatzpkbhcg7t = new BowMotionTracker();
        this.fiwzl5nthw36 = new BowReleaseTracker();
        this.frhll77n6bm = new CombatCommitService();
        this.fdcyqi371lj7 = new BowNextService();
        this.fie9rnospv75 = -1L;
    }
    
    @Override
    protected void onEnable() {
        this.mgf69xwgupxi();
        CompatContextReadyService.active(true);
        this.fbclw2cmdvkx = 0L;
        this.fie9rnospv75 = -1L;
        this.f4komu552zk4 = 0;
        this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT).priority(EventIsAfterHandler.Priority.EARLY).run(p0 -> this.m5k3mra546t6());
        this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE).run(p0 -> this.m1k2ak49x78j());
        this.on(EventAttackInputService.ATTACK_INPUT).priority(EventIsAfterHandler.Priority.FIRST).run(p0 -> this.m4lj1gkkypw8());
        this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED).run(outgoingPacketAccepted -> {
            CompatContextReadyService.observeAccepted(outgoingPacketAccepted.packet());
            if (outgoingPacketAccepted.packet() instanceof ServerboundUseItemPacket useItemPacket) {
                Minecraft minecraft = CoreIsInitializedHandler.mc();
                if (minecraft.player != null && minecraft.player.getItemInHand(useItemPacket.getHand()).is(Items.BOW)) {
                    this.fie9rnospv75 = this.fbclw2cmdvkx;
                }
            }
        });
        this.on(EventAttackInputService.WORLD).run(p0 -> this.mgf69xwgupxi());
        this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(p0 -> this.mgf69xwgupxi());
    }
    
    @Override
    protected void onDisable() {
        CompatContextReadyService.active(false);
        this.mgf69xwgupxi();
    }
    
    public Optional<CompatContextReadyService.Prediction> prediction() {
        return Optional.ofNullable(this.f2k2ocdleb48);
    }
    
    public int shots() {
        return this.f4komu552zk4;
    }
    
    public void suspendForScaffold() {
        this.mgf69xwgupxi();
    }
    
    private static boolean mg4po7u43i0r() {
        return CoreIsInitializedHandler.get().modules().get(ImplSuspendForPearlService.class).filter(Module::isEnabled).isPresent();
    }
    
    private void m5k3mra546t6() {
        ++this.fbclw2cmdvkx;
        if (mg4po7u43i0r()) {
            this.suspendForScaffold();
            return;
        }
        final Minecraft mc = CoreIsInitializedHandler.mc();
        this.f2k2ocdleb48 = null;
        this.fe7l55ofrkc9 = CompatContextReadyService.contextReady(mc);
        if (this.fe7l55ofrkc9) {
            final Optional<RotationData> claim = CombatOwnsService.claim(CombatOwnsService.Owner.BOW);
            if (this.f4t6yyxfgix0 == null) {
                this.f4t6yyxfgix0 = claim.orElse(null);
            }
        }
        else if (CoreIsInitializedHandler.get().modules().get(ImplDecisionTracker.class).filter(Module::isEnabled).isPresent() && CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
            CombatOwnsService.transfer(CombatOwnsService.Owner.BOW, CombatOwnsService.Owner.KILL_AURA);
            this.m9r54chmzk8u();
        }
        if (!CompatContextReadyService.trackingReady(mc)) {
            this.m8yyasdptp0q(true);
            return;
        }
        final LivingEntity livingEntity = CompatContextReadyService.target(mc, this.fbp4s4key4tn.get(), this.fabrsaj5nh3i.get(), this.fnxde03awan.get(), this.fiufnku5hemq.get().equals("360°") ? Double.longBitsToDouble(4645040803167600640L) : Double.longBitsToDouble(4640537203540230144L), this.fhmi3ykae2l2).orElse(null);
        if (livingEntity == null) {
            this.m8yyasdptp0q(true);
            return;
        }
        if (!livingEntity.getUUID().equals(this.fhmi3ykae2l2)) {
            this.fhmi3ykae2l2 = livingEntity.getUUID();
            this.fatzpkbhcg7t.clear();
            this.fiwzl5nthw36.resetAim();
            this.fdcyqi371lj7.reset();
        }
        final BowMotionTracker.Motion observe = this.fatzpkbhcg7t.observe(this.fbclw2cmdvkx, this.fhmi3ykae2l2, CompatContextReadyService.observedPosition((Entity)livingEntity), livingEntity.onGround());
        if (!this.fe7l55ofrkc9) {
            this.m8yyasdptp0q(false);
            return;
        }
        this.f2k2ocdleb48 = CompatContextReadyService.predict(mc, livingEntity, observe, Math.max(Math.round(this.f8i3v1yoca61.get()), CompatContextReadyService.drawing(mc) ? (mc.player.getTicksUsingItem() + 1) : 0), this.f18y45asie36.get()).orElse(null);
        if (this.f2k2ocdleb48 == null) {
            this.m8yyasdptp0q(false);
            return;
        }
        final RotationData rotationData = new RotationData(mc.player.getYRot(), mc.player.getXRot());
        final RotationData rotationData2 = (this.f4t6yyxfgix0 == null) ? rotationData : this.f4t6yyxfgix0;
        if (this.fbclw2cmdvkx != this.fie9rnospv75 + 1L) {
            this.f4t6yyxfgix0 = this.fdcyqi371lj7.next(rotationData2, this.f2k2ocdleb48.solution().rotation(), (double)mc.options.sensitivity().get(), this.fi691oy3g6n3.get(), this.f67spqtw4f14.get()).rotation();
        }
        this.mbreg2d5gd0k();
    }
    
    private void mbreg2d5gd0k() {
        if (!CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
            return;
        }
        final Minecraft mc = CoreIsInitializedHandler.mc();
        if (mc.player == null || this.f4t6yyxfgix0 == null) {
            this.frhll77n6bm.clear();
            return;
        }
        RotationPublishService.publish(new RotationData(mc.player.getYRot(), mc.player.getXRot()), this.f4t6yyxfgix0);
        final Options options = mc.options;
        if (this.frhll77n6bm.commit(this.fbclw2cmdvkx, mc.player.getYRot(), this.f4t6yyxfgix0, new ScaffoldRemapService.Input(options.keyUp.isDown(), options.keyDown.isDown(), options.keyLeft.isDown(), options.keyRight.isDown(), options.keyJump.isDown(), options.keyShift.isDown(), options.keySprint.isDown() || ImplRequestsSprintClient.requestsSprint(mc)), CompatContextReadyService.drawing(mc))) {
            mc.player.setSprinting(false);
        }
    }
    
    private void m1k2ak49x78j() {
        if (!CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
            return;
        }
        if (LocalPhysicsFrameBus.wasAborted(this.fbclw2cmdvkx)) {
            this.mgf69xwgupxi();
            return;
        }
        if (this.f4t6yyxfgix0 == null || CoreIsInitializedHandler.mc().player == null) {
            return;
        }
        this.frhll77n6bm.reconcile(this.fbclw2cmdvkx, PlacementOverrideBus.applied().orElseGet(() -> {
            final Input keyPresses = CoreIsInitializedHandler.mc().player.input.keyPresses;
            return new ScaffoldRemapService.Input(keyPresses.forward(), keyPresses.backward(), keyPresses.left(), keyPresses.right(), keyPresses.jump(), keyPresses.shift(), keyPresses.sprint());
        }));
    }
    
    private void m4lj1gkkypw8() {
        final Minecraft mc = CoreIsInitializedHandler.mc();
        final RotationObserveService.Snapshot snapshot = RotationObserveService.snapshot().orElse(null);
        if (!CompatBeginTickService.canAct() || !CombatOwnsService.owns(CombatOwnsService.Owner.BOW) || this.f2k2ocdleb48 == null || this.f4t6yyxfgix0 == null || snapshot == null || !CompatContextReadyService.contextReady(mc) || snapshot.wireEyePosition().subtract(CompatContextReadyService.vector(mc.player.getEyePosition())).length() > Double.longBitsToDouble(4591870180066957722L) || RotationData.distance(snapshot.rotation(), new RotationData((float)this.f4t6yyxfgix0.yaw(), (float)this.f4t6yyxfgix0.pitch())) > Double.longBitsToDouble(4532020583610935537L)) {
            this.fiwzl5nthw36.resetAim();
            return;
        }
        if (!CompatContextReadyService.drawing(mc)) {
            this.fiwzl5nthw36.resetAim();
            return;
        }
        final int ticksUsingItem = mc.player.getTicksUsingItem();
        if (this.fiwzl5nthw36.ready(this.fbclw2cmdvkx, this.f2k2ocdleb48.uuid(), ticksUsingItem, Math.round(this.f8i3v1yoca61.get()), this.f2k2ocdleb48.confidence(), ticksUsingItem >= Math.round(this.f8i3v1yoca61.get()) - 2 && this.f2k2ocdleb48.confidence() >= Double.longBitsToDouble(4604029899060858061L) && CompatContextReadyService.verifiedShot(mc, this.f2k2ocdleb48, snapshot.rotation(), snapshot.wireEyePosition(), this.fnxde03awan.get(), this.fafryocaquy5.get() / Double.longBitsToDouble(4636737291354636288L))) && CompatContextReadyService.release(mc)) {
            ++this.f4komu552zk4;
            this.fiwzl5nthw36.released(this.fbclw2cmdvkx);
        }
    }
    
    private void m8yyasdptp0q(final boolean b) {
        if (b) {
            this.fhmi3ykae2l2 = null;
            this.fatzpkbhcg7t.clear();
        }
        this.f2k2ocdleb48 = null;
        this.fiwzl5nthw36.resetAim();
        final Minecraft mc = CoreIsInitializedHandler.mc();
        if (mc.player == null || mc.level == null) {
            this.mgf69xwgupxi();
            return;
        }
        if (this.f4t6yyxfgix0 == null) {
            this.m9r54chmzk8u();
            return;
        }
        if (mc.player.isPassenger() || mc.player.isFallFlying() || mc.player.isAutoSpinAttack() || mc.player.isInWater() || mc.player.isInLava() || mc.player.isSwimming() || mc.player.getAbilities().flying) {
            this.mgf69xwgupxi();
            return;
        }
        final BowNextService.Step next = this.fdcyqi371lj7.next(this.f4t6yyxfgix0, new RotationData(mc.player.getYRot(), mc.player.getXRot()), (double)mc.options.sensitivity().get(), this.fi691oy3g6n3.get(), this.f67spqtw4f14.get());
        if (next.settled()) {
            this.m9r54chmzk8u();
            return;
        }
        if (this.fbclw2cmdvkx == this.fie9rnospv75 + 1L) {
            this.mbreg2d5gd0k();
            return;
        }
        this.f4t6yyxfgix0 = next.rotation();
        this.mbreg2d5gd0k();
    }
    
    private void mgf69xwgupxi() {
        this.fe7l55ofrkc9 = false;
        this.fatzpkbhcg7t.clear();
        this.fiwzl5nthw36.clear();
        this.m9r54chmzk8u();
        this.fhmi3ykae2l2 = null;
        this.f2k2ocdleb48 = null;
        this.fie9rnospv75 = -1L;
    }
    
    private void m9r54chmzk8u() {
        this.fdcyqi371lj7.reset();
        if (this.fe7l55ofrkc9) {
            CombatOwnsService.clearFrame(CombatOwnsService.Owner.BOW);
        }
        else {
            CombatOwnsService.release(CombatOwnsService.Owner.BOW);
        }
        this.f4t6yyxfgix0 = null;
    }
}
