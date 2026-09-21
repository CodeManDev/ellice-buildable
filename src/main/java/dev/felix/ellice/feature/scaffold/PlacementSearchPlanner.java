







package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.CompatPrepareService;
import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.PlacementRuleEvaluator;
import dev.felix.ellice.feature.scaffold.ScaffoldBlockHit;
import dev.felix.ellice.feature.scaffold.ScaffoldBudgetService;
import dev.felix.ellice.feature.scaffold.ScaffoldCoastTicksService;
import dev.felix.ellice.feature.scaffold.ScaffoldComponent;
import dev.felix.ellice.feature.scaffold.ScaffoldData;
import dev.felix.ellice.feature.scaffold.ScaffoldHasContinuousSupportService;
import dev.felix.ellice.feature.scaffold.ScaffoldLandingService;
import dev.felix.ellice.feature.scaffold.ScaffoldMode;
import dev.felix.ellice.feature.scaffold.ScaffoldMovementProfile;
import dev.felix.ellice.feature.scaffold.ScaffoldNextService;
import dev.felix.ellice.feature.scaffold.ScaffoldObserveService;
import dev.felix.ellice.feature.scaffold.ScaffoldOperationHandler;
import dev.felix.ellice.feature.scaffold.ScaffoldPlacementHeadingService;
import dev.felix.ellice.feature.scaffold.ScaffoldPlanService;
import dev.felix.ellice.feature.scaffold.ScaffoldPlayerSnapshot;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldReleaseTracker;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.ScaffoldSettings;
import dev.felix.ellice.feature.scaffold.ScaffoldTrajectorySolver;
import dev.felix.ellice.feature.scaffold.ScaffoldValidateBeforeMovementValidator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Input;

public final class PlacementSearchPlanner
implements ScaffoldOperationHandler {
    private final ScaffoldObserveService f7d7twf1tjkd = new ScaffoldObserveService();
    private final ScaffoldRemapService f4xqggkk544p = new ScaffoldRemapService();
    private final ScaffoldBudgetService fd811u1gwthp = new ScaffoldBudgetService();
    private final ScaffoldReleaseTracker fhiiiks84sb4 = new ScaffoldReleaseTracker();
    private final ScaffoldTrajectorySolver fewat8le4vak = new ScaffoldTrajectorySolver(ScaffoldTrajectorySolver.SelectionPolicy.MINIMUM_ROTATION);
    private final LinkedHashMap<Integer, PlacementCandidate> fhy64683l9tv = new LinkedHashMap();
    private RotationData f1gjo6f31d2m;
    private RotationData fdczx4ttrt8q;
    private ScaffoldRemapService.Result f5vnlx74a701;
    private PlacementCandidate f5rh82xw4qon;
    private PlacementCandidate f6aljhl71r0w;
    private PlacementCandidate f7jp557sztdt;
    private RotationVector f82b77eox8d6;
    private int ffwinzan6o40;
    private int fgvh4uk1nqbm = -1;
    private int fbmpxmzroo4g = -1;
    private int f7m2mjcgm7li;
    private long f40dyuvq9ypg;
    private long f2a2x8wkr2km;
    private long fjm6itmcwi5r = Long.MAX_VALUE;
    private long fiql9wakxkyr = Long.MIN_VALUE;
    private boolean f9d6xd7yjy2k;
    private boolean fg3afh4gwwrl;
    private boolean fakbr3dyy5yl;
    private String fbaxinrnfgkj = "Telly \u00b7 waiting for movement";

    @Override
    public void prepare(final Minecraft minecraft, final ScaffoldCompatibility scaffoldCompatibility, CombatCompatibility combatCompatibility, ScaffoldRemapService.Input input, ScaffoldSettings scaffoldSettings, Runnable runnable) {
        boolean bl;
        int n;
        Record record;
        double d;
        RotationData rotationData2;
        int n2;
        ScaffoldRemapService.WorldVector worldVector;
        ScaffoldPlayerSnapshot scaffoldPlayerSnapshot;
        ++this.f40dyuvq9ypg;
        this.f9d6xd7yjy2k = false;
        CompatPrepareService.clear();
        if (this.fjm6itmcwi5r < this.f40dyuvq9ypg) {
            this.fhy64683l9tv.entrySet().removeIf(entry -> (Integer)entry.getKey() <= this.fbmpxmzroo4g);
            this.fjm6itmcwi5r = Long.MAX_VALUE;
        }
        if ((scaffoldPlayerSnapshot = (ScaffoldPlayerSnapshot)scaffoldCompatibility.capture(minecraft).orElse(null)) == null || minecraft.screen != null || minecraft.getOverlay() != null || !minecraft.player.isAlive() || minecraft.player.isSpectator() || minecraft.player.isPassenger() || minecraft.player.getAbilities().flying || minecraft.player.isFallFlying() || minecraft.player.isAutoSpinAttack() || minecraft.player.isSwimming() || minecraft.player.isInWater() || minecraft.player.isInLava()) {
            this.suspend(minecraft, scaffoldSettings.returnSpeed(), scaffoldSettings.returnSmoothness());
            this.fbaxinrnfgkj = "Telly \u00b7 movement unavailable";
            return;
        }
        ScaffoldData scaffoldData = scaffoldSettings.tuning();
        ScaffoldMovementProfile scaffoldMovementProfile = scaffoldSettings.telly();
        if (!scaffoldData.activated(minecraft.options.keyUse.isDown(), minecraft.options.keyShift.isDown()) || ScaffoldComponent.manualItemUse(minecraft) || ScaffoldComponent.manualAttack(minecraft) || CompatProfileTracker.paused()) {
            this.suspend(minecraft, scaffoldSettings.returnSpeed(), scaffoldSettings.returnSmoothness());
            this.fbaxinrnfgkj = "Telly \u00b7 paused for manual input";
            return;
        }
        if (!input.hasHorizontalIntent() && scaffoldPlayerSnapshot.onGround() && scaffoldPlayerSnapshot.horizontalSpeed() < Double.longBitsToDouble(4576918229304087675L)) {
            this.suspend(minecraft, scaffoldSettings.returnSpeed(), scaffoldSettings.returnSmoothness());
            return;
        }
        runnable.run();
        if (this.f1gjo6f31d2m == null) {
            this.f1gjo6f31d2m = RotationObserveService.current().orElse(scaffoldPlayerSnapshot.playerRotation());
        }
        if (scaffoldPlayerSnapshot.onGround() && !this.fg3afh4gwwrl && !this.fakbr3dyy5yl || this.f82b77eox8d6 == null) {
            int n3;
            this.f82b77eox8d6 = scaffoldPlayerSnapshot.feetPosition();
            this.ffwinzan6o40 = (int)Math.floor(this.f82b77eox8d6.y() - Double.longBitsToDouble(4547007122018943789L));
            int n4 = (int)Math.floor(this.f82b77eox8d6.x());
            if (!scaffoldCompatibility.isPlayerSupporting(minecraft, new BlockCoordinates(n4, this.ffwinzan6o40, n3 = (int)Math.floor(this.f82b77eox8d6.z()))) && scaffoldCompatibility.isPlayerSupporting(minecraft, new BlockCoordinates(n4, this.ffwinzan6o40 - 1, n3))) {
                --this.ffwinzan6o40;
            }
        }
        if ((worldVector = ScaffoldRemapService.worldVector(scaffoldPlayerSnapshot.playerRotation().yaw(), input.forwardAxis(), input.leftAxis())).length() < Double.longBitsToDouble(4562254508917369340L) && scaffoldPlayerSnapshot.horizontalSpeed() > Double.longBitsToDouble(4562254508917369340L)) {
            worldVector = new ScaffoldRemapService.WorldVector(scaffoldPlayerSnapshot.velocity().x() / scaffoldPlayerSnapshot.horizontalSpeed(), scaffoldPlayerSnapshot.velocity().z() / scaffoldPlayerSnapshot.horizontalSpeed());
        }
        double d2 = Math.toDegrees(Math.atan2(-worldVector.x(), worldVector.z()));
        RotationData rotationData3 = new RotationData(d2, scaffoldMovementProfile.forwardPitch());
        if (scaffoldPlayerSnapshot.onGround() && this.fg3afh4gwwrl) {
            this.fakbr3dyy5yl = true;
        }
        this.fg3afh4gwwrl = !scaffoldPlayerSnapshot.onGround();
        double d3 = ScaffoldCoastTicksService.coastTicks(scaffoldCompatibility.groundFriction(minecraft));
        double d4 = scaffoldMovementProfile.landingReserve() + Double.longBitsToDouble(0x3FE3333333333333L);
        RotationVector rotationVector = scaffoldPlayerSnapshot.feetPosition().add(new RotationVector(scaffoldPlayerSnapshot.velocity().x() * d3 + worldVector.x() * d4, 0.0, scaffoldPlayerSnapshot.velocity().z() * d3 + worldVector.z() * d4));
        int n5 = n2 = scaffoldPlayerSnapshot.onGround() && ScaffoldHasContinuousSupportService.hasContinuousSupport(scaffoldPlayerSnapshot.feetPosition(), rotationVector, this.ffwinzan6o40, scaffoldSettings.minimumFootOverlap(), blockCoordinates -> this.mk7m7vlwf2d(minecraft, scaffoldCompatibility, blockCoordinates)) ? 1 : 0;
        if (scaffoldPlayerSnapshot.onGround() && n2 != 0) {
            this.fakbr3dyy5yl = false;
        } else if (scaffoldPlayerSnapshot.onGround() && input.hasHorizontalIntent() && !input.sneak() && !minecraft.player.isSprinting() && scaffoldPlayerSnapshot.horizontalSpeed() < Double.longBitsToDouble(4576918229304087675L)) {
            this.fakbr3dyy5yl = true;
        }
        ScaffoldPlanService.Query query = new ScaffoldPlanService.Query(){
            @Override
            public boolean isReplaceable(BlockCoordinates blockCoordinates) {
                return scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates);
            }

            @Override
            public boolean isFaceSturdy(BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
                return scaffoldCompatibility.isFaceSturdy(minecraft, blockCoordinates, scaffoldMode);
            }
        };
        double d5 = scaffoldCompatibility.airInputAccelerationEstimate(minecraft, 1, 0);
        if (!Double.isFinite(d5) || d5 < 0.0) {
            d5 = 0.0;
        }
        ScaffoldLandingService.Landing landing = ScaffoldLandingService.landing(scaffoldPlayerSnapshot, worldVector, d5, minecraft.player.getGravity(), this.ffwinzan6o40).orElse(null);
        ScaffoldLandingService.Landing landing2 = ScaffoldLandingService.landing(scaffoldPlayerSnapshot, worldVector, 0.0, minecraft.player.getGravity(), this.ffwinzan6o40).orElse(null);
        RotationVector rotationVector2 = landing == null ? scaffoldPlayerSnapshot.feetPosition() : landing.feet();
        rotationVector2 = rotationVector2.add(new RotationVector(worldVector.x() * scaffoldMovementProfile.landingReserve(), 0.0, worldVector.z() * scaffoldMovementProfile.landingReserve()));
        RotationVector rotationVector3 = landing2 == null ? rotationVector2 : landing2.feet().add(new RotationVector(worldVector.x() * d4, 0.0, worldVector.z() * d4));
        boolean bl3 = landing2 != null && ScaffoldHasContinuousSupportService.hasContinuousSupport(landing2.before(), rotationVector3, this.ffwinzan6o40, scaffoldSettings.minimumFootOverlap(), blockCoordinates -> this.mk7m7vlwf2d(minecraft, scaffoldCompatibility, blockCoordinates));
        ScaffoldObserveService.Phase phase = this.f7d7twf1tjkd.observe(this.f40dyuvq9ypg, scaffoldPlayerSnapshot.onGround(), scaffoldPlayerSnapshot.velocity().y() < 0.0, bl3, scaffoldMovementProfile.straightTicks(), scaffoldMovementProfile.earlyReturn());
        if (scaffoldPlayerSnapshot.onGround() && this.fakbr3dyy5yl) {
            phase = ScaffoldObserveService.Phase.BUILD;
            rotationVector2 = rotationVector;
        }
        RotationData rotationData4 = rotationData2 = scaffoldSettings.clearObstacles() && scaffoldPlayerSnapshot.onGround() ? CompatPrepareService.prepare(minecraft, scaffoldCompatibility, scaffoldPlayerSnapshot, input, this.f1gjo6f31d2m, scaffoldSettings.reach(), scaffoldMovementProfile.turnSpeed()) : null;
        if (rotationData2 != null) {
            this.f1gjo6f31d2m = rotationData2;
            this.f7d7twf1tjkd.reset();
            this.f9d6xd7yjy2k = true;
            this.fbaxinrnfgkj = "Telly \u00b7 clearing obstacle";
            this.m34xdc13083c(minecraft, combatCompatibility, scaffoldPlayerSnapshot, input, false, false, false, true);
            return;
        }
        int n6 = scaffoldCompatibility.findPlaceableHotbarSlot(minecraft);
        boolean bl4 = n6 >= 0 && (scaffoldData.autoSelectBlocks() || n6 == scaffoldCompatibility.selectedHotbarSlot(minecraft));
        ScaffoldRemapService.Result result = this.f4xqggkk544p.remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.f1gjo6f31d2m.yaw(), input, false, true);
        ScaffoldPlayerSnapshot scaffoldPlayerSnapshot2 = ScaffoldPlacementHeadingService.clipLanding(scaffoldPlayerSnapshot, ScaffoldPlacementHeadingService.nextFrame(scaffoldPlayerSnapshot, result.serverWorldVector(), this.fakbr3dyy5yl && scaffoldPlayerSnapshot.onGround() ? 0.0 : d5, false), this.ffwinzan6o40, blockCoordinates -> scaffoldCompatibility.isPlayerSupporting(minecraft, (BlockCoordinates)blockCoordinates));
        PlacementCandidate placementCandidate2 = phase == ScaffoldObserveService.Phase.BUILD ? (PlacementCandidate)ScaffoldLandingService.plan(scaffoldPlayerSnapshot2, this.f82b77eox8d6, rotationVector2, this.ffwinzan6o40, scaffoldSettings.reach(), this.f1gjo6f31d2m, query).orElse(null) : null;
        RotationData rotationData5 = RotationObserveService.current().orElse(null);
        double d6 = rotationData5 == null || this.fdczx4ttrt8q == null ? Double.longBitsToDouble(0x7FF0000000000000L) : RotationData.distance(this.fdczx4ttrt8q, rotationData5);
        this.fdczx4ttrt8q = rotationData5;
        boolean bl5 = this.fhiiiks84sb4.attempt(this.f40dyuvq9ypg, scaffoldData.clickRhythm(), scaffoldSettings.useInterval());
        int n7 = phase == ScaffoldObserveService.Phase.BUILD && bl4 && this.mfzpmy8n20r(minecraft, scaffoldCompatibility, scaffoldPlayerSnapshot, scaffoldSettings, rotationVector2, d6, bl5) ? 1 : 0;
        RotationData rotationData6 = rotationData3;
        if (phase == ScaffoldObserveService.Phase.BUILD) {
            rotationData6 = new RotationData(d2 + Double.longBitsToDouble(4640537203540230144L), scaffoldSettings.pitch());
            if (placementCandidate2 != null) {
                RotationVector rotationVector4 = scaffoldPlayerSnapshot2.eyePosition();
                d = new RotationVanillaGcdService().vanillaGcd(scaffoldPlayerSnapshot.mouseSensitivity());
                record = ScaffoldNextService.applyMouseCounts(this.f1gjo6f31d2m, Math.round(RotationData.yawDelta(this.f1gjo6f31d2m.yaw(), d2 + Double.longBitsToDouble(4640537203540230144L)) / d), 0L, scaffoldPlayerSnapshot.mouseSensitivity());
                ScaffoldTrajectorySolver.Probe probe = (placementCandidate, rotationData) -> scaffoldCompatibility.raycastPlacementFromEye(minecraft, placementCandidate, rotationData, scaffoldSettings.reach(), rotationVector4);
                double d7 = Math.min(Double.longBitsToDouble(4601778099247172813L), Math.max(Double.longBitsToDouble(4585204852618449388L), scaffoldSettings.faceMargin() + Double.longBitsToDouble(4582862980812216730L)));
                Optional<ScaffoldTrajectorySolver.Solution> optional = this.fewat8le4vak.solveKeepingYaw((RotationData)record, rotationVector4, placementCandidate2, scaffoldPlayerSnapshot.mouseSensitivity(), d7, Math.min(32, scaffoldSettings.raycastBudget()), probe);
                if (optional.isEmpty()) {
                    optional = this.fewat8le4vak.solve(this.f1gjo6f31d2m, rotationVector4, placementCandidate2, scaffoldPlayerSnapshot.mouseSensitivity(), d7, scaffoldSettings.searchGrid(), scaffoldSettings.searchRadius(), Math.max(1, scaffoldSettings.raycastBudget() - 32), probe);
                }
                rotationData6 = optional.map(ScaffoldTrajectorySolver.Solution::rotation).orElseGet(() -> RotationData.lookAt(rotationVector4, placementCandidate2.hitPoint()));
            }
        }
        if (n7 == 0) {
            double d8 = new RotationVanillaGcdService().vanillaGcd(scaffoldPlayerSnapshot.mouseSensitivity());
            double d9 = this.fd811u1gwthp.budget(scaffoldData.turnStyle(), scaffoldMovementProfile.turnSpeed(), scaffoldData.smoothness(), RotationData.distance(this.f1gjo6f31d2m, rotationData6), d8);
            this.f1gjo6f31d2m = ScaffoldPlacementHeadingService.turnToward(this.f1gjo6f31d2m, rotationData6, scaffoldPlayerSnapshot.mouseSensitivity(), d9);
        }
        boolean bl6 = Math.abs(RotationData.yawDelta(this.f1gjo6f31d2m.yaw(), d2)) <= Double.longBitsToDouble(0x4008000000000000L);
        d = scaffoldCompatibility.groundInputAccelerationEstimate(minecraft, 1, 0, true);
        if (!Double.isFinite(d) || d < 0.0) {
            d = 0.0;
        }
        record = scaffoldPlayerSnapshot.feetPosition().add(new RotationVector(worldVector.x() * scaffoldMovementProfile.edgeDistance(), 0.0, worldVector.z() * scaffoldMovementProfile.edgeDistance()));
        boolean bl7 = !ScaffoldHasContinuousSupportService.hasContinuousSupport(scaffoldPlayerSnapshot.feetPosition(), (RotationVector)record, this.ffwinzan6o40, scaffoldSettings.minimumFootOverlap(), blockCoordinates -> this.mk7m7vlwf2d(minecraft, scaffoldCompatibility, blockCoordinates));
        boolean bl8 = minecraft.level.noCollision((Entity)minecraft.player, minecraft.player.getBoundingBox().expandTowards(worldVector.x() * Double.longBitsToDouble(0x3FE3333333333333L), Double.longBitsToDouble(4608533498688228557L), worldVector.z() * Double.longBitsToDouble(0x3FE3333333333333L)));
        boolean bl9 = ScaffoldHasContinuousSupportService.hasContinuousSupport(scaffoldPlayerSnapshot.feetPosition(), scaffoldPlayerSnapshot.feetPosition(), this.ffwinzan6o40, scaffoldSettings.minimumFootOverlap(), blockCoordinates -> this.mk7m7vlwf2d(minecraft, scaffoldCompatibility, blockCoordinates));
        boolean shouldAutoJump = !this.fakbr3dyy5yl && scaffoldSettings.autoJump() && this.f7d7twf1tjkd.mayJump(input.hasHorizontalIntent(), input.sneak(), bl6, minecraft.player.isSprinting(), bl8, bl4 && bl9, bl7, scaffoldPlayerSnapshot.horizontalSpeed(), scaffoldMovementProfile);
        if (scaffoldSettings.diagnostics() && scaffoldPlayerSnapshot.onGround() && bl7 && this.f40dyuvq9ypg % 20L == 0L) {
            CoreIsInitializedHandler.LOGGER.info("[Scaffold/Telly] takeoff tick={} speed={} minimum={} aligned={} sprint={} headroom={} stock={} support={} pending={} jump={}", new Object[]{this.f40dyuvq9ypg, scaffoldPlayerSnapshot.horizontalSpeed(), scaffoldMovementProfile.minimumSpeed(), bl6, minecraft.player.isSprinting(), bl8, bl4, bl9, this.fhy64683l9tv.size(), shouldAutoJump});
        }
        boolean bl11 = input.jump();
        int n8 = n = input.hasHorizontalIntent() && (bl4 || !scaffoldSettings.stopWhenEmpty()) && phase != ScaffoldObserveService.Phase.RETURN ? 1 : 0;
        if (scaffoldPlayerSnapshot.onGround()) {
            bl = PlacementRuleEvaluator.allows(scaffoldPlayerSnapshot, worldVector, this.ffwinzan6o40, scaffoldSettings.minimumFootOverlap(), ScaffoldCoastTicksService.coastTicks(scaffoldCompatibility.groundFriction(minecraft)), d, blockCoordinates -> this.mk7m7vlwf2d(minecraft, scaffoldCompatibility, blockCoordinates));
            this.f9d6xd7yjy2k = this.fakbr3dyy5yl || !bl6 || !bl4 && scaffoldSettings.stopWhenEmpty() || scaffoldSettings.safetyBrake() && !shouldAutoJump && !bl11 && !input.sneak() && !bl;
            n &= !this.f9d6xd7yjy2k || this.fakbr3dyy5yl ? 1 : 0;
        }
        bl = (scaffoldMovementProfile.autoSprint() || input.sprint()) && bl6 && n != 0 && !input.sneak() && phase != ScaffoldObserveService.Phase.BUILD;
        this.fbaxinrnfgkj = switch (phase) {
            default -> throw new MatchException(null, null);
            case ScaffoldObserveService.Phase.RUN_UP -> {
                if (shouldAutoJump) {
                    yield "Telly \u00b7 sprint jump";
                }
                if (!bl4) {
                    yield "Telly \u00b7 " + CompatProfileTracker.stock(minecraft).reason();
                }
                if (input.sneak()) {
                    yield "Telly \u00b7 sneaking; automatic jumps paused";
                }
                if (!bl6) {
                    yield "Telly \u00b7 facing forward";
                }
                if (scaffoldSettings.autoJump() && !scaffoldMovementProfile.autoSprint() && !input.sprint()) {
                    yield "Telly \u00b7 hold sprint for takeoff";
                }
                if (scaffoldSettings.autoJump() && minecraft.player.getFoodData().getFoodLevel() <= 6) {
                    yield "Telly \u00b7 need food to sprint";
                }
                if (!bl8 && bl7) {
                    yield "Telly \u00b7 more headroom needed";
                }
                if (this.f9d6xd7yjy2k) {
                    yield "Telly \u00b7 more run-up needed";
                }
                yield "Telly \u00b7 run-up";
            }
            case ScaffoldObserveService.Phase.STRAIGHT -> "Telly \u00b7 forward flight";
            case ScaffoldObserveService.Phase.BUILD -> {
                if (this.fakbr3dyy5yl) {
                    yield "Telly \u00b7 securing the landing";
                }
                if (n7 != 0) {
                    yield "Telly \u00b7 placing landing blocks";
                }
                if (placementCandidate2 == null) {
                    yield "Telly \u00b7 searching for a connecting face";
                }
                yield "Telly \u00b7 aiming at landing path";
            }
            case ScaffoldObserveService.Phase.RETURN -> "Telly \u00b7 landing ready; facing forward";
        };
        this.m34xdc13083c(minecraft, combatCompatibility, scaffoldPlayerSnapshot, input, n != 0, shouldAutoJump || bl11, bl, (scaffoldPlayerSnapshot.onGround() && (this.fakbr3dyy5yl || this.f9d6xd7yjy2k && bl7 && scaffoldSettings.safetyBrake()) ? 1 : 0) != 0);
    }

    private void m34xdc13083c(Minecraft minecraft, CombatCompatibility combatCompatibility, ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldRemapService.Input input, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        ScaffoldRemapService.Input input2 = new ScaffoldRemapService.Input(bl && input.forward(), bl && input.backward(), bl && input.left(), bl && input.right(), bl2, input.sneak(), bl3);
        this.f5vnlx74a701 = this.f4xqggkk544p.remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.f1gjo6f31d2m.yaw(), input2, bl4, true);
        boolean bl5 = !this.f5vnlx74a701.serverInput().sprint();
        combatCompatibility.applyServerRotation(minecraft, this.f1gjo6f31d2m, CombatOwnsService.Owner.SCAFFOLD);
        PlacementOverrideBus.publish(new PlacementOverrideBus.Override(true, this.f5vnlx74a701.serverInput(), bl4, bl5, true, true));
        ScaffoldPublishService.publish(this.f5vnlx74a701.serverInput(), bl5);
        LocalPhysicsFrameBus.publish(new LocalPhysicsFrameBus.Frame(this.f40dyuvq9ypg, (float)this.f1gjo6f31d2m.yaw(), this.f5vnlx74a701.serverInput(), bl5));
        if (bl5) {
            minecraft.player.setSprinting(false);
        }
    }

    


    private boolean mfzpmy8n20r(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldSettings scaffoldSettings, RotationVector rotationVector, double d, boolean bl) {
        boolean bl2;
        if (!scaffoldCompatibility.canStartUseItem(minecraft) || minecraft.player.isUsingItem() || d > scaffoldSettings.telly().clickTurnSpeed()) {
            return false;
        }
        RotationObserveService.Snapshot snapshot = RotationObserveService.snapshot().orElse(null);
        if (snapshot == null || !ScaffoldValidateBeforeMovementValidator.sameSentRotation(snapshot.rotation(), this.f1gjo6f31d2m)) {
            return false;
        }
        ScaffoldBlockHit scaffoldBlockHit = scaffoldCompatibility.raycastBlockFromEye(minecraft, this.f1gjo6f31d2m, scaffoldSettings.reach(), scaffoldPlayerSnapshot.eyePosition()).orElse(null);
        if (scaffoldBlockHit == null || scaffoldBlockHit.inside() || scaffoldBlockHit.face() == ScaffoldMode.DOWN || scaffoldBlockHit.faceEdgeMargin() < scaffoldSettings.faceMargin()) {
            this.f7m2mjcgm7li = 0;
            return false;
        }
        PlacementCandidate placementCandidate = scaffoldCompatibility.placementFromRay(minecraft, scaffoldBlockHit).orElse(null);
        if (placementCandidate == null || placementCandidate.targetPosition().y() != this.ffwinzan6o40 || (double)(this.ffwinzan6o40 + 1) > scaffoldPlayerSnapshot.feetPosition().y() + Double.longBitsToDouble(4517329193108106637L) || !ScaffoldLandingService.cells(this.f82b77eox8d6, rotationVector, this.ffwinzan6o40).contains(placementCandidate.targetPosition()) || !scaffoldCompatibility.isReplaceable(minecraft, placementCandidate.targetPosition()) || !scaffoldCompatibility.isFaceSturdy(minecraft, placementCandidate.supportPosition(), placementCandidate.clickedFace()) || scaffoldCompatibility.raycastPlacementFromEye(minecraft, placementCandidate, snapshot.rotation(), scaffoldSettings.reach(), snapshot.wireEyePosition()).isEmpty()) {
            return false;
        }
        if (this.fiql9wakxkyr != this.f40dyuvq9ypg - 1L || this.f7jp557sztdt == null || !this.f7jp557sztdt.targetPosition().equals(placementCandidate.targetPosition())) {
            this.f7m2mjcgm7li = 0;
        }
        this.f7jp557sztdt = placementCandidate;
        this.fiql9wakxkyr = this.f40dyuvq9ypg;
        if (++this.f7m2mjcgm7li <= scaffoldSettings.rayDwellTicks() || !bl || this.f40dyuvq9ypg < this.f2a2x8wkr2km) {
            return false;
        }
        int n = scaffoldCompatibility.findPlaceableHotbarSlot(minecraft);
        if (n < 0 || !scaffoldSettings.tuning().autoSelectBlocks() && n != scaffoldCompatibility.selectedHotbarSlot(minecraft)) {
            return false;
        }
        this.fgvh4uk1nqbm = -1;
        this.f6aljhl71r0w = placementCandidate;
        try {
            bl2 = CompatProfileTracker.place(minecraft, scaffoldCompatibility, n, placementCandidate);
        }
        finally {
            this.f6aljhl71r0w = null;
        }
        if (this.fgvh4uk1nqbm >= 0) {
            this.fhy64683l9tv.put(this.fgvh4uk1nqbm, placementCandidate);
            if (this.fhy64683l9tv.size() > 128) {
                this.fhy64683l9tv.remove(this.fhy64683l9tv.keySet().iterator().next());
            }
        }
        if (!bl2 || this.fgvh4uk1nqbm < 0) {
            return false;
        }
        this.f5rh82xw4qon = placementCandidate;
        this.f2a2x8wkr2km = this.f40dyuvq9ypg + (long)("Held Use".equals(scaffoldSettings.tuning().clickRhythm()) ? 4 : Math.max(1, scaffoldSettings.useInterval()));
        if (scaffoldSettings.diagnostics()) {
            CoreIsInitializedHandler.LOGGER.info("[Scaffold/Telly] use tick={} air={} target={} eye={} rotation={} previous={}", new Object[]{this.f40dyuvq9ypg, this.f7d7twf1tjkd.airTicks(), placementCandidate.targetPosition(), scaffoldPlayerSnapshot.eyePosition(), this.f1gjo6f31d2m, snapshot});
        }
        return true;
    }

    private boolean mk7m7vlwf2d(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, BlockCoordinates blockCoordinates) {
        return (scaffoldCompatibility.isPlayerSupporting(minecraft, blockCoordinates) && this.fhy64683l9tv.values().stream().noneMatch(placementCandidate -> placementCandidate.targetPosition().equals(blockCoordinates)) ? 1 : 0) != 0;
    }

    @Override
    public void observeOutgoing(ScaffoldCompatibility scaffoldCompatibility, Object object) {
        int n;
        if (this.f6aljhl71r0w != null && (n = scaffoldCompatibility.placementSequence(object, this.f6aljhl71r0w)) >= 0) {
            this.fgvh4uk1nqbm = n;
        }
    }

    @Override
    public void observeServerPacket(ScaffoldCompatibility scaffoldCompatibility, Object object) {
        int n = scaffoldCompatibility.acknowledgedPlacementSequence(object);
        if (n >= 0) {
            this.fbmpxmzroo4g = Math.max(this.fbmpxmzroo4g, n);
            this.fjm6itmcwi5r = this.f40dyuvq9ypg;
        }
    }

    @Override
    public void reconcile(Minecraft minecraft) {
        if (!CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD) || minecraft.player == null) {
            return;
        }
        LocalPhysicsFrameBus.Frame frame = LocalPhysicsFrameBus.current().orElse(null);
        if (frame == null) {
            return;
        }
        Input input = minecraft.player.input.keyPresses;
        ScaffoldRemapService.Input input2 = new ScaffoldRemapService.Input(input.forward(), input.backward(), input.left(), input.right(), input.jump(), input.shift(), input.sprint());
        LocalPhysicsFrameBus.publishAppliedInput(frame.tick(), input2, frame.suppressSprint());
        ScaffoldPublishService.publish(input2, frame.suppressSprint());
    }

    @Override
    public RotationData rotation() {
        return this.f1gjo6f31d2m;
    }

    @Override
    public ScaffoldRemapService.Result movement() {
        return this.f5vnlx74a701;
    }

    @Override
    public PlacementCandidate lastPlacement() {
        return this.f5rh82xw4qon;
    }

    @Override
    public boolean braking() {
        return this.f9d6xd7yjy2k;
    }

    @Override
    public String status() {
        return this.fbaxinrnfgkj;
    }

    @Override
    public void suspend(Minecraft minecraft, double d, double d2) {
        if (this.f1gjo6f31d2m != null) {
            CombatRotationOverride.start(minecraft, CombatOwnsService.Owner.SCAFFOLD, d, d2);
        }
        this.release();
    }

    @Override
    public void release() {
        CompatProfileTracker.release();
        CompatPrepareService.clear();
        this.fdczx4ttrt8q = null;
        this.f1gjo6f31d2m = null;
        this.f5vnlx74a701 = null;
        this.f82b77eox8d6 = null;
        this.fakbr3dyy5yl = false;
        this.fg3afh4gwwrl = false;
        this.f7d7twf1tjkd.reset();
        this.fd811u1gwthp.reset();
        this.fhiiiks84sb4.reset();
        this.f7m2mjcgm7li = 0;
        this.f7jp557sztdt = null;
        this.fiql9wakxkyr = Long.MIN_VALUE;
        this.f2a2x8wkr2km = 0L;
        this.f9d6xd7yjy2k = false;
        this.fbaxinrnfgkj = "Telly \u00b7 waiting for movement";
        CombatOwnsService.release(CombatOwnsService.Owner.SCAFFOLD);
    }

    @Override
    public void reset(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, boolean bl) {
        this.release();
        this.fhy64683l9tv.clear();
        this.f40dyuvq9ypg = 0L;
        this.f6aljhl71r0w = null;
        this.f5rh82xw4qon = null;
        this.fbmpxmzroo4g = -1;
        this.fgvh4uk1nqbm = -1;
        this.fjm6itmcwi5r = Long.MAX_VALUE;
    }
}

