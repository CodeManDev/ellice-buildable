package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.compat.CompatActiveService;
import dev.felix.ellice.compat.CompatActivateService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CompatAvailableService;
import dev.felix.ellice.compat.CompatBlocksAttackFromService;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.SoupInventoryBridge;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.RotationObservationCapture;
import dev.felix.ellice.compat.CompatCanStartService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.combat.CombatAttackedService;
import dev.felix.ellice.feature.combat.CombatData;
import dev.felix.ellice.feature.combat.CombatCorrectedService;
import dev.felix.ellice.feature.combat.CombatReleaseTracker;
import dev.felix.ellice.feature.combat.CombatObserveService;
import dev.felix.ellice.feature.combat.CombatDecideService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.combat.CombatDecisionTracker;
import dev.felix.ellice.feature.combat.CombatCommitService;
import dev.felix.ellice.feature.movement.MovementAvailableService;
import dev.felix.ellice.feature.rotation.RotationNextService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.HumanizedRotationPlanner;
import dev.felix.ellice.feature.rotation.SmoothRotationStepper;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationHumanizationProfile;
import dev.felix.ellice.feature.rotation.RotationTargetFilter;
import dev.felix.ellice.feature.rotation.RotationSelectForCombatSelector;
import dev.felix.ellice.feature.rotation.RotationFrameContext;
import dev.felix.ellice.feature.rotation.RotationTarget;
import dev.felix.ellice.feature.rotation.AimPointSampler;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleSetting;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class ImplDecisionTracker extends ModuleSettingsService {
   private static boolean enabled2;
   private final ModuleNameService moduleNameService = this.settingCategory("Targets")
      .description("Controls which visible entities may be selected and from how far.");
   private final ModuleNameService moduleNameService2 = this.settingCategory("Combat")
      .description("Automatically attacks the selected opponent when the weapon is ready.");
   private final ModuleNameService moduleNameService3 = this.settingCategory("Shields")
      .description("Recognizes directional blocking and uses an equipped offhand shield during weapon recovery.");
   private final ModuleNameService moduleNameService4 = this.settingCategory("Motion")
      .description("Tunes turning and keeps local movement aligned with the server rotation.");
   private final ModuleNameService moduleNameService5 = this.settingCategory("Combat AI")
      .description("Autonomous positioning that adapts to the opponent, cooldown and health. Manual Takeover keys give movement back.");
   private final ModuleNameService moduleNameService6 = this.settingCategory("Humanization")
      .description("Adds reaction delay, overshoot, hesitation, and motor noise to test traces.");
   private final ModuleNameService moduleNameService7 = this.settingCategory("Quantization")
      .description("Controls sensitivity-based mouse-step snapping and fault offsets.");
   private final ModuleNameService moduleNameService8 = this.settingCategory("Reproducibility")
      .description("Sets the seed used to replay identical randomized rotation choices.");
   private final ModuleNameService moduleNameService9 = this.settingCategory("Negative Tests")
      .description("Injects deliberately impossible patterns for anti-cheat validation.");
   private final ModuleNameService moduleNameService10 = this.settingCategory(this.moduleNameService, "Aim")
      .description("Chooses where on a visible hitbox the motor aims.");
   private final ModuleNameService moduleNameService11 = this.settingCategory(this.moduleNameService2, "Timing")
      .description("Adds extra click, aim and switch constraints on top of weapon cooldown.");
   private final ModuleNameService moduleNameService12 = this.settingCategory(this.moduleNameService2, "Conditions")
      .description("Limits when an otherwise valid attack may be issued.");
   private final ModuleNameService moduleNameService13 = this.settingCategory(this.moduleNameService4, "Turn Limits")
      .description("Independent pitch and camera-return speeds.");
   private final ModuleNameService moduleNameService14 = this.settingCategory(this.moduleNameService6, "Ranges")
      .description("Numeric windows behind the humanization toggles.");
   private final ModuleNameService moduleNameService15 = this.settingCategory(this.moduleNameService5, "Control")
      .description("Which physical keys immediately return movement to the player.");
   private final ModuleSetting.Bool moduleBool = this.setting(
      this.moduleNameService, new ModuleSetting.Bool("Players", true).description("Allows visible player entities to be selected as targets.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      this.moduleNameService, new ModuleSetting.Bool("Mobs", true).description("Allows visible mobs to be selected; allies are excluded.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Invisible", false).description("Also selects invisible players and mobs. Blatant: humans cannot see them.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number(
            "Range", 4.5F, 1.0F, 12.0F, 0.5F
         )
         .description("Sets the maximum target distance in blocks.")
   );
   private final ModuleSetting.Mode moduleMode = this.setting(
      this.moduleNameService,
      new ModuleSetting.Mode("FOV", new String[]{"180°", "360°", "Custom"}, "180°")
         .description("Searches the front 180°, the full 360°, or a custom angle around the current server rotation.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number("FOV Degrees", 90.0F, 1.0F, 360.0F, 1.0F)
         .description("Custom search cone in degrees around the current server rotation.")
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
            "Attack Range", 3.0F, 1.0F, 6.0F, 0.1F
         )
         .description("Sets attack reach in blocks, capped by Minecraft's entity interaction range.")
   );
   private final ModuleSetting.Mode moduleMode2 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Mode("Target Priority", new String[]{"Smart", "Nearest", "Lowest Health", "Highest Health", "Crosshair"}, "Smart")
         .description("Smart weighs reach, turn angle and health. Other modes prioritize one factor.")
   );
   private final ModuleSetting.Bool moduleBool4 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Keep Combo Target", true)
         .description("Stays on a visible target within attack reach; switches when it becomes unreachable.")
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number("Switch Delay (ticks)", 0.0F, 0.0F, 20.0F, 1.0F)
         .description("Waits this many movement ticks after a target change before the first attack. Rotation continues.")
   );
   private final ModuleSetting.Mode moduleMode3 = this.setting(
      this.moduleNameService10,
      new ModuleSetting.Mode("Aim Point", new String[]{"Auto", "Head", "Chest", "Legs", "Center", "Custom"}, "Auto")
         .description("Auto keeps the visible lattice point. Other modes prefer a height on the hitbox.")
   );
   private final ModuleSetting.Number moduleNumber5 = this.setting(
      this.moduleNameService10,
      new ModuleSetting.Number(
            "Aim Height",
            0.55F,
            0.05F,
            0.95F,
            0.01F
         )
         .description("Vertical hitbox fraction used when Aim Point is Custom. 0 is feet, 1 is the top of the box.")
   );
   private final ModuleSetting.Bool moduleBool5 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Smart Clicks", true)
         .description("Waits for the target's hurt animation to end and a valid sent aim; requires two aim ticks when Combo Timing is off.")
   );
   private final ModuleSetting.Bool moduleBool6 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Combo Timing", true)
         .description("Uses the live weapon cooldown and first valid aim tick without extra pauses; adapts immediately to weapon swaps.")
   );
   private final ModuleSetting.Bool moduleBool7 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Sprint Reset", true)
         .description("Releases sprint for one grounded movement tick after a sprint attack, then permits normal sprint again.")
   );
   private final ModuleSetting.Number moduleNumber6 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Correction Recovery (ticks)", 3.0F, 0.0F, 20.0F, 1.0F)
         .description("Waits this many complete movement ticks after a server correction before reacquiring a target.")
   );
   private final ModuleSetting.Number moduleNumber7 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Sprint Reset (ticks)", 1.0F, 1.0F, 5.0F, 1.0F)
         .description("How many grounded movement ticks sprint stays released after a sprint attack.")
   );
   private final ModuleSetting.Range moduleRange = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Range(
            "Click Speed (CPS)",
            4.0F,
            7.0F,
            1.0F,
            20.0F,
            0.5F
         )
         .description("Limits click speed; weapon cooldown and Smart Clicks may reduce the actual attack rate.")
   );
   private final ModuleSetting.Bool moduleBool8 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Bool("Wait for Cooldown", true)
         .description("Requires a full weapon cooldown before attacking. Off still uses CPS and native reduced-damage swings.")
   );
   private final ModuleSetting.Bool moduleBool9 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Bool("Wait for Hurt Time", true).description("Waits until the target's hurt animation ends before the next click.")
   );
   private final ModuleSetting.Number moduleNumber8 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Number("Extra Aim Ticks", 0.0F, 0.0F, 4.0F, 1.0F)
         .description("Requires additional consecutive valid aim ticks beyond Smart Clicks / Combo Timing.")
   );
   private final ModuleSetting.Number moduleNumber9 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Number("Hit Chance (%)", 100.0F, 1.0F, 100.0F, 1.0F)
         .description("Chance that a ready click is actually issued. Missed clicks still consume the CPS deadline.")
   );
   private final ModuleSetting.Number moduleNumber10 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Number("Click Jitter (%)", 12.0F, 0.0F, 40.0F, 1.0F)
         .description("Extra interval variation used by Smart Clicks when Combo Timing is off.")
   );
   private final ModuleSetting.Number moduleNumber11 = this.setting(
      this.moduleNameService11,
      new ModuleSetting.Number("Click Pause Chance (%)", 15.0F, 0.0F, 50.0F, 1.0F)
         .description("Chance of a one-tick pause after a Smart Click when Combo Timing is off.")
   );
   private final ModuleSetting.Bool moduleBool10 = this.setting(
      this.moduleNameService12,
      new ModuleSetting.Bool("Weapons Only", false)
         .description("Tracks as usual but only attacks while holding a sword, axe, trident or mace.")
   );
   private final ModuleSetting.Mode moduleMode4 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Mode("Shield Strategy", new String[]{"Use Held Axe", "Wait for Opening", "Ignore"}, "Use Held Axe")
         .description("Waits when the opponent blocks this hit; Use Held Axe permits a held weapon that can disable their shield.")
   );
   private final ModuleSetting.Bool moduleBool11 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Bool("AutoBlock", true)
         .description(
            "Uses an equipped offhand shield against a nearby facing threat while your weapon recovers. Manual input takes priority."
         )
   );
   private final ModuleSetting.Number moduleNumber12 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number("Block Reaction (ticks)", 2.0F, 0.0F, 8.0F, 1.0F)
         .description("Observes the same threat for this many movement ticks before raising the shield.")
   );
   private final ModuleSetting.Number moduleNumber13 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number("Minimum Block (ticks)", 6.0F, 1.0F, 20.0F, 1.0F)
         .description(
            "Keeps a useful block window through the shield's activation delay. Threat loss and manual input release it immediately."
         )
   );
   private final ModuleSetting.Number moduleNumber14 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
            "Block Range", 4.0F, 1.0F, 6.0F, 0.1F
         )
         .description("Maximum distance for AutoBlock threat detection, still limited by Attack Range plus a short margin.")
   );
   private final ModuleSetting.Bool moduleBool12 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Bool("Movement Correction", true)
         .description("Matches walking, strafing and sprint-jumps to the server rotation while preserving camera-relative input.")
   );
   private final ModuleSetting.Mode moduleMode5 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Mode("Rotation Mode", new String[]{"Silent", "First Person"}, "Silent")
         .description(
            "Silent keeps your mouse look free. First Person turns the camera toward the aim every frame; packets still use the GCD motor."
         )
   );
   private final ModuleSetting.Number moduleNumber15 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Number("Camera Smoothness", 0.58F, 0.0F, 1.0F, 0.01F)
         .description(
            "How floaty the first-person view is. 0 follows the aim immediately, 1 lags behind. Independent from packet Smoothness."
         )
   );
   private final ModuleSetting.Number moduleNumber16 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Number(
            "Camera Speed", 32.0F, 5.0F, 180.0F, 1.0F
         )
         .description("Maximum first-person turn rate in degrees per tick. Packets still use Turn Speed.")
   );
   private final ModuleSetting.Number moduleNumber17 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Number("Turn Speed", 30.0F, 1.0F, 90.0F, 1.0F)
         .description("Caps yaw and pitch change per movement tick, in degrees.")
   );
   private final ModuleSetting.Number moduleNumber18 = this.setting(
      this.moduleNameService4,
      new ModuleSetting.Number("Smoothness", 0.5F, 0.0F, 1.0F, 0.01F)
         .description("Makes turns more gradual; zero snaps directly to the target.")
   );
   private final ModuleSetting.Number moduleNumber19 = this.setting(
      this.moduleNameService13,
      new ModuleSetting.Number(
            "Pitch Speed (%)", 100.0F, 10.0F, 150.0F, 1.0F
         )
         .description("Pitch cap as a percentage of Turn Speed. 100 treats yaw and pitch equally.")
   );
   private final ModuleSetting.Number moduleNumber20 = this.setting(
      this.moduleNameService13,
      new ModuleSetting.Number("Return Speed", 0.0F, 0.0F, 90.0F, 1.0F)
         .description("Turn-speed cap while returning to the camera after target loss. 0 uses Turn Speed.")
   );
   private final ModuleSetting.Bool moduleBool13 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Bool("Combat AI", false)
         .description("Autonomously pursues, flanks, spaces and evades using a local opponent model. Requires Movement Correction.")
   );
   private final ModuleSetting.Mode moduleMode6 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Mode("Fighting Style", new String[]{"Balanced", "Pressure", "Defensive"}, "Balanced")
         .description("Balanced uses cooldown windows; Pressure closes gaps; Defensive creates space earlier at low health.")
   );
   private final ModuleSetting.Number moduleNumber21 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Number(
            "Preferred Distance",
            2.45F,
            0.5F,
            4.0F,
            0.05F
         )
         .description("Preferred distance to the opponent's hitbox, adapted to reach, health and the current exchange.")
   );
   private final ModuleSetting.Number moduleNumber22 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Number(
            "Pursuit Range", 5.0F, 1.0F, 8.0F, 0.25F
         )
         .description("Maximum distance for autonomous pursuit; target acquisition Range still applies.")
   );
   private final ModuleSetting.Bool moduleBool14 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Bool("Adaptive Strafing", true)
         .description(
            "Flanks opponents and adapts sides to their strafe. A/D biases the side; sprint follows your sprint key or AutoSprint."
         )
   );
   private final ModuleSetting.Bool moduleBool15 = this.setting(
      this.moduleNameService5,
      new ModuleSetting.Bool("Pattern Learning", true)
         .description(
            "Learns each opponent's recurring strafes and approach/retreat sequences. Only validated predictions influence movement; unreliable patterns fade."
         )
   );
   private final ModuleSetting.MultiSelect moduleMultiSelect = this.setting(
      this.moduleNameService15,
      new ModuleSetting.MultiSelect("Manual Takeover", new String[]{"Backward", "Sneak", "Jump"}, "Backward", "Sneak", "Jump")
         .description("Physical keys that immediately give movement back to the player.")
   );
   private final ModuleSetting.Number moduleNumber23 = this.setting(
      this.moduleNameService6,
      new ModuleSetting.Number("Jitter Intensity", 50.0F, 0.0F, 100.0F, 1.0F)
         .description("Scales slow aim-point drift, motor noise and tracking tempo variation; zero disables them.")
   );
   private final ModuleSetting.Bool moduleBool16 = this.setting(
      this.moduleNameService6,
      new ModuleSetting.Bool("Overshoot 2-5°", true).description("Occasionally passes the target by 2–5° before correcting.")
   );
   private final ModuleSetting.Bool moduleBool17 = this.setting(
      this.moduleNameService6,
      new ModuleSetting.Bool("Reaction Delay 1-4 Ticks", true)
         .description("Waits 1–4 movement ticks before turning to a new target; an existing visible aim intersection needs no turn delay.")
   );
   private final ModuleSetting.Bool moduleBool18 = this.setting(
      this.moduleNameService6,
      new ModuleSetting.Bool("GCD Snap Variation", true).description("Varies the timing of the final sensitivity-grid correction.")
   );
   private final ModuleSetting.Bool moduleBool19 = this.setting(
      this.moduleNameService6,
      new ModuleSetting.Bool("Micro Hesitation", true).description("Occasionally pauses one tick between movement phases.")
   );
   private final ModuleSetting.Range moduleRange2 = this.setting(
      this.moduleNameService14,
      new ModuleSetting.Range(
            "Overshoot Degrees",
            2.0F,
            5.0F,
            0.0F,
            15.0F,
            0.5F
         )
         .description("How far an overshoot passes the target before the corrective submovement.")
   );
   private final ModuleSetting.Range moduleRange3 = this.setting(
      this.moduleNameService14,
      new ModuleSetting.Range("Reaction Delay (ticks)", 1.0F, 4.0F, 0.0F, 8.0F, 1.0F)
         .description("Acquisition latency window while Reaction Delay is enabled.")
   );
   private final ModuleSetting.Range moduleRange4 = this.setting(
      this.moduleNameService14,
      new ModuleSetting.Range("Target Loss (ticks)", 2.0F, 4.0F, 0.0F, 10.0F, 1.0F)
         .description("How long a fully lost target is held before camera return, while Reaction Delay is enabled.")
   );
   private final ModuleSetting.Range moduleRange5 = this.setting(
      this.moduleNameService14,
      new ModuleSetting.Range("Gap Reacquire (ticks)", 1.0F, 3.0F, 0.0F, 8.0F, 1.0F)
         .description("Delay before aiming through a newly visible opening, while Reaction Delay is enabled.")
   );
   private final ModuleSetting.Mode moduleMode7 = this.setting(
      this.moduleNameService7,
      new ModuleSetting.Mode("Sensitivity Source", new String[]{"Minecraft", "Override"}, "Minecraft")
         .description("Uses Minecraft sensitivity or a manual value for GCD quantization.")
   );
   private final ModuleSetting.Number moduleNumber24 = this.setting(
      this.moduleNameService7,
      new ModuleSetting.Number("Sensitivity Override", 0.5F, 0.0F, 1.0F, 0.01F)
         .description("Sets the sensitivity used when the source is Override.")
   );
   private final ModuleSetting.Number moduleNumber25 = this.setting(
      this.moduleNameService7,
      new ModuleSetting.Number(
            "GCD Offset Degrees",
            0.0F,
            -0.05F,
            0.05F,
            5.0E-4F
         )
         .description("Offsets the mouse-count grid for fault injection; zero is compliant.")
   );
   private final ModuleSetting.Number moduleNumber26 = this.setting(
      this.moduleNameService8,
      new ModuleSetting.Number("Random Seed", 48271.0F, 0.0F, 100000.0F, 1.0F)
         .exactInput()
         .description("Makes reaction, noise, and overshoot choices reproducible.")
   );
   private final ModuleSetting.Mode moduleMode8 = this.setting(
      this.moduleNameService9,
      new ModuleSetting.Mode("Test Profile", new String[]{"Human-like", "Impossible"}, "Human-like")
         .description(
            "Chooses a human-like trace or an intentionally impossible control. Impossible is bannable; local anti-cheat tests only."
         )
   );
   private final ModuleSetting.Mode moduleMode9 = this.setting(
      this.moduleNameService9,
      new ModuleSetting.Mode("Impossible Pattern", new String[]{"Alternating Jerk", "Zero-Latency Snap", "Half-GCD"}, "Alternating Jerk")
         .description("Selects the jerk, instant-snap, or half-GCD fault pattern. Bannable; local anti-cheat tests only.")
   );
   private final RotationSelectForCombatSelector rotationSelectForCombatSelector = new RotationSelectForCombatSelector();
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private final CombatCommitService combatCommitService = new CombatCommitService();
   private final CombatAttackedService combatAttackedService = new CombatAttackedService();
   private final CombatCorrectedService combatCorrectedService = new CombatCorrectedService();
   private final CombatDecideService combatDecideService = new CombatDecideService();
   private final SmoothRotationStepper rotationNextService3 = new SmoothRotationStepper();
   private final CombatDecisionTracker combatDecisionTracker = new CombatDecisionTracker();
   private CombatReleaseTracker values;
   private long timestamp;
   private HumanizedRotationPlanner rotationNextService2;
   private AimPointSampler rotationNextService4;
   private long timestamp2;
   private Integer integer;
   private UUID uUID;
   private HumanizedRotationPlanner.Step step2;
   private RotationTarget rotationData6;
   private String text;
   private long timestamp3;
   private RotationData rotationData;
   private int count2 = Integer.MIN_VALUE;
   private double value2 = 0.5;
   private double value3 = 0.55;
   private double value4 = 0.5;
   private Random random;
   private int count3 = Integer.MIN_VALUE;
   private double value5 = 0.5;
   private double value6 = 0.55;
   private double value7 = 0.5;
   private int count4;
   private boolean enabled3;
   private int count5;
   private int count6;
   private long timestamp4;

   public ImplDecisionTracker() {
      super(
         ModuleBuilderData.builder("KillAura").description("Automatically selects and attacks opponents.").category(ModuleFeatureType.COMBAT).build()
      );
      this.moduleNumber24.visibleWhen(this.moduleMode7, "Override"::equals);
      this.moduleMode9.visibleWhen(this.moduleMode8, "Impossible"::equals);
      this.moduleNumber25.activeWhen(this.moduleMode8, "Impossible"::equals);
      this.moduleNumber2.visibleWhen(this.moduleMode, "Custom"::equals);
      this.moduleNumber5.visibleWhen(this.moduleMode3, "Custom"::equals);
      this.moduleNumber15.visibleWhen(this.moduleMode5, "First Person"::equals);
      this.moduleNumber16.visibleWhen(this.moduleMode5, "First Person"::equals);
      this.moduleNumber7.visibleWhen(this.moduleBool7);
      this.moduleNumber10.visibleWhen(this.moduleBool5).visibleWhen(this.moduleBool6, item -> !item);
      this.moduleNumber11.visibleWhen(this.moduleBool5).visibleWhen(this.moduleBool6, item -> !item);
      this.moduleRange2.visibleWhen(this.moduleBool16);
      this.moduleRange3.visibleWhen(this.moduleBool17);
      this.moduleRange4.visibleWhen(this.moduleBool17);
      this.moduleRange5.visibleWhen(this.moduleBool17);
      this.moduleNumber12.visibleWhen(this.moduleBool11);
      this.moduleNumber13.visibleWhen(this.moduleBool11);
      this.moduleNumber14.visibleWhen(this.moduleBool11);
      this.moduleBool13.activeWhen(this.moduleBool12).activeWhen(this.moduleMode8, "Human-like"::equals);

      for (ModuleSetting moduleSetting : new ModuleSetting[]{
         this.moduleMode6, this.moduleNumber21, this.moduleNumber22, this.moduleBool14, this.moduleBool15, this.moduleMultiSelect
      }) {
         moduleSetting.visibleWhen(this.moduleBool13).activeWhen(this.moduleBool12).activeWhen(this.moduleMode8, "Human-like"::equals);
      }

      this.moduleBool15.onValueChanged(this.combatDecisionTracker::clear);
      this.moduleBool13.onValueChanged(this.combatDecisionTracker::clear);
   }

   @Override
   protected void onEnable() {
      CombatRotationOverride.cancel();
      this.updateState10();
      this.values = new CombatReleaseTracker(this.timestamp2 ^ 5840696475078001361L);
      this.timestamp = 0L;
      this.combatCorrectedService.clear();
      this.combatDecideService.clear();
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT).run(item -> this.updateState());
      this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE).run(item -> this.updateState3());
      this.on(EventAttackInputService.PRE_MOVEMENT_PACKET).run(item -> this.updateState4());
      this.on(EventAttackInputService.ATTACK_INPUT).run(item -> this.updateState6());
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> {
         this.updateState11();
         this.combatDecisionTracker.clear();
         this.combatCorrectedService.corrected(this.timestamp, Math.round((Float)this.moduleNumber6.get()));
      });
      this.on(EventAttackInputService.WORLD).run(item -> {
         CompatCanStartService.abandon();
         this.updateState11();
         this.combatDecisionTracker.clear();
         this.combatCorrectedService.clear();
      });
   }

   @Override
   protected void onDisable() {
      this.updateState7();
      CompatActiveService.stop();
      CombatRotationOverride.start(
         CoreIsInitializedHandler.mc(), CombatOwnsService.Owner.KILL_AURA, this.calculateValue2(), ((Float)this.moduleNumber18.get()).floatValue()
      );
      this.updateState11();
      this.combatDecisionTracker.clear();
      this.combatCorrectedService.clear();
      this.rotationNextService2 = null;
      this.values = null;
   }

   public Optional<HumanizedRotationPlanner.Step> lastStep() {
      return Optional.ofNullable(this.step2);
   }

   public boolean hasTarget() {
      return this.isEnabled() && this.integer != null && !this.enabled3 && CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA);
   }

   public Optional<UUID> targetUuid() {
      return this.hasTarget() ? Optional.ofNullable(this.uUID) : Optional.empty();
   }

   public Optional<ImplDecisionTracker.TickBaseWindow> tickBaseWindow() {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (this.hasTarget()
         && minecraft.player != null
         && minecraft.level != null
         && this.uUID != null
         && this.step2 != null
         && CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)
         && this.values != null
         && !this.combatCorrectedService.blocks(this.timestamp)
         && this.count6 <= 0
         && this.count4 <= 0
         && !this.enabled3
         && ((String)this.moduleMode8.get()).equals("Human-like")
         && CompatAvailableService.available(minecraft)
         && this.values.stableTicks() >= this.collectValues().requiredAimTicks()
         && this.values.cadenceReady(System.nanoTime())) {
         return minecraft.level.getEntity(this.integer) instanceof LivingEntity livingEntity
               && livingEntity.isAlive()
               && livingEntity.getUUID().equals(this.uUID)
               && (this.moduleBool3.get() || !livingEntity.isInvisibleTo(minecraft.player))
               && (!this.moduleBool9.get() || livingEntity.hurtTime <= 0)
               && !this.checkCondition2(minecraft, livingEntity)
               && (!this.moduleBool10.get() || checkCondition7(minecraft))
            ? Optional.of(
               new ImplDecisionTracker.TickBaseWindow(
                  this.uUID,
                  Math.min(minecraft.player.entityInteractionRange(), Math.min((Float)this.moduleNumber.get(), (Float)this.moduleNumber3.get())),
                  this.collectValues().requiredAimTicks(),
                  ((Float)this.moduleNumber17.get()).floatValue(),
                  this.timestamp4
               )
            )
            : Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public boolean tickBaseWeaponReady(int value) {
      LocalPlayer localPlayer = CoreIsInitializedHandler.mc().player;
      return localPlayer != null && localPlayer.getAttackStrengthScale(Math.max(0, value)) >= 1.0F;
   }

   public long issuedAttacks() {
      return this.timestamp4;
   }

   public double supportAttackRange() {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      double doubleValue = Math.min((Float)this.moduleNumber.get(), (Float)this.moduleNumber3.get());
      if (minecraft != null && minecraft.player != null) {
         doubleValue = Math.min(doubleValue, minecraft.player.entityInteractionRange());
      }

      return doubleValue;
   }

   public void tickBaseAttackPhase(UUID currentUUID) {
      if (this.isEnabled() && currentUUID.equals(this.uUID)) {
         this.updateState6();
      }
   }

   public static boolean sprintResetActive() {
      return enabled2;
   }

   public Optional<CombatDecisionTracker.Decision> combatDecision() {
      return this.combatDecisionTracker.lastDecision();
   }

   public Optional<CombatObserveService.Forecast> combatLearning() {
      return this.combatDecisionTracker.learning();
   }

   private void updateState() {
      enabled2 = false;
      this.timestamp++;
      if (!(Boolean)this.moduleBool13.get() || !(Boolean)this.moduleBool12.get() || !((String)this.moduleMode8.get()).equals("Human-like")) {
         this.combatDecisionTracker.clear();
      }

      if (CompatCanStartService.maintain(CoreIsInitializedHandler.mc())) {
         this.combatDecideService.interrupted(this.timestamp);
      }

      if (this.combatCorrectedService.blocks(this.timestamp)) {
         this.updateState11();
      } else {
         CompatOptionsTracker.reconcileOwnership();
         if (CoreIsInitializedHandler.get().modules().get(ImplSuspendForPearlService.class).filter(Module::isEnabled).isPresent()) {
            this.updateState11();
         } else if (CombatOwnsService.owns(CombatOwnsService.Owner.ROD)) {
            this.updateState7();
            this.rotationData6 = null;
            this.combatAttackedService.clear();
            this.combatDecisionTracker.suspend();
         } else if (CombatOwnsService.owns(CombatOwnsService.Owner.BOW)) {
            this.updateState11();
         } else if (!CompatAvailableService.availableForPlanning(CoreIsInitializedHandler.mc())) {
            this.updateState11();
         } else {
            this.updateState8();
            if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA) && this.rotationData != null) {
               this.updateState2();
            }

            Minecraft minecraft = CoreIsInitializedHandler.mc();
            this.updateState5();
            if (minecraft.player != null && (Boolean)this.moduleBool12.get() && checkCondition(minecraft)) {
               if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
                  this.combatCommitService.clear();
               }

               this.combatDecisionTracker.suspend();
               this.combatAttackedService.clear();
               enabled2 = false;
            } else {
               if (!(Boolean)this.moduleBool7.get() || this.rotationData6 == null) {
                  this.combatAttackedService.clear();
               }

               enabled2 = minecraft.player != null
                      && this.moduleBool7.get()
                      && this.combatAttackedService.suppress(this.timestamp, this.uUID, minecraft.player.onGround());
               if (enabled2) {
                  minecraft.player.setSprinting(false);
               }

               if (minecraft.player != null && this.rotationData != null && (Boolean)this.moduleBool12.get()) {
                  Options currentOptions = minecraft.options;
                  ScaffoldRemapService.Input currentInput = new ScaffoldRemapService.Input(
                     MovementAvailableService.requested(currentOptions.keyUp),
                     currentOptions.keyDown.isDown(),
                     currentOptions.keyLeft.isDown(),
                     currentOptions.keyRight.isDown(),
                     MovementAvailableService.requested(currentOptions.keyJump),
                     currentOptions.keyShift.isDown(),
                     currentOptions.keySprint.isDown() || ImplRequestsSprintClient.requestsSprint(minecraft)
                  );
                  RotationObservationCapture.Observation observation = this.moduleBool13.get() && ((String)this.moduleMode8.get()).equals("Human-like")
                     ? RotationObservationCapture.capture(
                           minecraft,
                           this.timestamp,
                           this.rotationData6,
                           this.rotationData,
                           Math.min((Float)this.moduleNumber.get(), (Float)this.moduleNumber3.get())
                        )
                        .orElse(null)
                     : null;
                  CombatDecisionTracker.Decision decision = null;
                  if (observation != null) {
                     CombatDecisionTracker.Style style = switch ((String)this.moduleMode6.get()) {
                        case "Pressure" -> CombatDecisionTracker.Style.PRESSURE;
                        case "Defensive" -> CombatDecisionTracker.Style.DEFENSIVE;
                        default -> CombatDecisionTracker.Style.BALANCED;
                     };
                     ScaffoldRemapService.Input nextInput = enabled2
                        ? new ScaffoldRemapService.Input(
                           currentInput.forward(), currentInput.backward(), currentInput.left(), currentInput.right(), currentInput.jump(), currentInput.sneak(), false
                        )
                        : currentInput;
                     Set values = (Set)this.moduleMultiSelect.get();
                     decision = this.combatDecisionTracker
                        .decide(
                           observation.frame(),
                           new CombatDecisionTracker.Profile(
                              style,
                              ((Float)this.moduleNumber21.get()).floatValue(),
                              Math.min((Float)this.moduleNumber.get(), (Float)this.moduleNumber22.get()),
                              (Boolean)this.moduleBool14.get(),
                              (Boolean)this.moduleBool15.get(),
                              values.contains("Backward"),
                              values.contains("Sneak"),
                              values.contains("Jump")
                           ),
                           nextInput,
                           observation.terrain()
                        )
                        .orElse(null);
                  } else {
                     this.combatDecisionTracker.suspend();
                  }

                  boolean enabled = decision == null
                     ? this.combatCommitService.commit(this.timestamp, minecraft.player.getYRot(), this.rotationData, currentInput, enabled2)
                     : this.combatCommitService
                        .commitCorrected(this.timestamp, this.rotationData, decision.input(), enabled2 || decision.suppressSprint());
                  if (enabled) {
                     minecraft.player.setSprinting(false);
                  }
               } else {
                  if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
                     this.combatCommitService.clear();
                  }

                  this.combatDecisionTracker.suspend();
               }
            }
         }
      }
   }

   private void updateState2() {
      if (this.rotationData == null) {
         this.rotationData = RotationPublishService.current().orElse(null);
      }

      RotationData currentRotationData = RotationObserveService.current().orElse(null);
      if (currentRotationData != null) {
         int value = this.rotationData == null
               || RotationData.yawDelta((float)this.rotationData.yaw(), (float)currentRotationData.yaw()) == 0.0
                  && (float)currentRotationData.pitch() == (float)this.rotationData.pitch()
            ? 0
            : 1;
         this.rotationData = currentRotationData;
         if (value != 0) {
            this.rotationNextService2.resetTarget();
         }

         this.rotationNextService2.synchronizeMotion(RotationObserveService.previousTickRotation().orElse(currentRotationData), currentRotationData);
      }
   }

   private void updateState3() {
      if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         if (LocalPhysicsFrameBus.wasAborted(this.timestamp)) {
            this.updateState11();
         } else if ((Boolean)this.moduleBool12.get() && this.rotationData != null && CoreIsInitializedHandler.mc().player != null) {
            ScaffoldRemapService.Input currentInput = PlacementOverrideBus.applied()
               .orElseGet(
                  () -> {
                      Input playerInput = CoreIsInitializedHandler.mc().player.input.keyPresses;
                     return new ScaffoldRemapService.Input(
                         playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right(), playerInput.jump(), playerInput.shift(), playerInput.sprint()
                     );
                  }
               );
            this.combatCommitService.reconcile(this.timestamp, currentInput);
         }
      }
   }

   private void updateState4() {
      if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         Minecraft minecraft = CoreIsInitializedHandler.mc();
         if (minecraft.player == null || minecraft.level == null || CompatAdapterService.rotationEnvironment().isEmpty()) {
            this.updateState11();
         }
      }
   }

   private static boolean checkCondition(Minecraft minecraft) {
      LocalPlayer localPlayer = minecraft.player;
      return localPlayer.isPassenger()
         || localPlayer.isFallFlying()
         || localPlayer.isAutoSpinAttack()
         || localPlayer.isSwimming()
         || localPlayer.isInWater()
         || localPlayer.isInLava()
         || localPlayer.getAbilities().flying;
   }

   private void updateState5() {
      this.rotationData6 = null;
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      Optional result = CompatAdapterService.rotationEnvironment();
      if (!result.isEmpty() && minecraft.player != null && minecraft.level != null) {
         CombatCompatibility combatCompatibility = (CombatCompatibility)result.get();
         double doubleValue = ((Float)this.moduleNumber.get()).floatValue();
         RotationTargetFilter rotationTargetFilter = new RotationTargetFilter(
            (Boolean)this.moduleBool.get(), (Boolean)this.moduleBool2.get(), doubleValue, (Boolean)this.moduleBool3.get()
         );
         Optional currentResult = combatCompatibility.capture(minecraft, rotationTargetFilter);
         if (currentResult.isEmpty()) {
            this.updateState11();
         } else {
            RotationFrameContext rotationFrameContext = (RotationFrameContext)currentResult.get();
            if (minecraft.screen != null) {
               this.updateState11();
            } else if ((!minecraft.player.isUsingItem() || CompatCanStartService.holding(minecraft))
               && !SoupInventoryBridge.handBusy()
               && !CompatReleaseTracker.handBusy()
               && !CompatActivateService.handBusy()
               && ((Boolean)this.moduleBool.get() || (Boolean)this.moduleBool2.get())) {
               boolean enabled = ((String)this.moduleMode8.get()).equals("Impossible");
               RotationData currentRotationData = this.rotationData != null && CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)
                  ? this.rotationData
                  : rotationFrameContext.playerRotation();
               RotationFrameContext currentRotationFrameContext = new RotationFrameContext(rotationFrameContext.eyePosition(), currentRotationData, rotationFrameContext.mouseSensitivity(), rotationFrameContext.entities());
               double currentDoubleValue = enabled ? 360.0 : this.calculateValue();
               double nextDoubleValue = Math.min(((Float)this.moduleNumber3.get()).floatValue(), minecraft.player.entityInteractionRange());
               RotationSelectForCombatSelector currentRotationSelectForCombatSelector = this.rotationSelectForCombatSelector;
               UUID currentUUID = this.uUID;

               Optional nextResult = currentRotationSelectForCombatSelector.selectForCombat(currentRotationFrameContext, currentUUID, doubleValue, nextDoubleValue, currentDoubleValue, switch ((String)this.moduleMode2.get()) {
                  case "Nearest" -> RotationSelectForCombatSelector.Priority.NEAREST;
                  case "Lowest Health" -> RotationSelectForCombatSelector.Priority.LOWEST_HEALTH;
                  case "Highest Health" -> RotationSelectForCombatSelector.Priority.HIGHEST_HEALTH;
                  case "Crosshair" -> RotationSelectForCombatSelector.Priority.CROSSHAIR;
                  default -> RotationSelectForCombatSelector.Priority.SMART;
               }, (Boolean)this.moduleBool4.get());
               if (!nextResult.isEmpty()) {
                  this.updateState15();
                  CombatOwnsService.claim(CombatOwnsService.Owner.KILL_AURA);
                  CombatRotationOverride.cancel(CombatOwnsService.Owner.KILL_AURA);
                  this.updateState2();
                  RotationSelectForCombatSelector.Selection selection = (RotationSelectForCombatSelector.Selection)nextResult.get();
                  boolean currentEnabled = selection.entity().uuid().equals(this.uUID);
                  if (!currentEnabled) {
                     this.rotationNextService3.reset();
                     this.combatAttackedService.clear();
                     this.combatDecisionTracker.suspend();
                     this.rotationNextService2.resetTarget();
                     this.rotationNextService4.reset();
                     this.count6 = this.uUID != null ? Math.max(0, Math.round((Float)this.moduleNumber4.get())) : 0;
                  }

                  this.integer = selection.entity().entityId();
                  this.uUID = selection.entity().uuid();
                  RotationVector rotationVector = this.createRotationData7(selection.entity(), selection.aimPoint());
                  if (!currentEnabled || this.count2 != this.integer) {
                     this.updateState12(selection.entity().hitbox(), rotationVector, this.integer);
                  }

                  double previousDoubleValue = ((String)this.moduleMode7.get()).equals("Override")
                     ? ((Float)this.moduleNumber24.get()).floatValue()
                     : rotationFrameContext.mouseSensitivity();
                  RotationVector currentRotationVector = selection.entity().hitbox().sample(this.value2, this.value3, this.value4);
                  if (!enabled
                     && !this.checkCondition4(
                        selection.entity().hitbox(), selection.entity().visibleAimPoints(), rotationFrameContext.eyePosition(), currentRotationVector, this.integer
                     )) {
                     if (this.rotationData != null) {
                        this.rotationData = this.createRotationData(minecraft);
                        this.updateState16(combatCompatibility, minecraft, this.rotationData);
                     }
                  } else {
                     if (enabled) {
                        RotationVector nextRotationVector = createRotationData72(currentRotationVector, selection.entity().visibleAimPoints());
                        if (nextRotationVector.subtract(currentRotationVector).lengthSquared() > 1.0E-10) {
                           this.updateState12(selection.entity().hitbox(), nextRotationVector, this.integer);
                        }
                     }

                     currentRotationVector = selection.entity().hitbox().sample(this.value2, this.value3, this.value4);
                     RotationVector previousRotationVector = currentRotationVector;
                     if (!enabled) {
                        RotationVector sourceRotationVector = this.rotationNextService4
                           .next(
                              selection.entity().hitbox(),
                              currentRotationVector,
                              this.integer,
                              ((Float)this.moduleNumber23.get()).floatValue() / 100.0
                           );
                        if (combatCompatibility.hasBlockLineOfSight(minecraft, rotationFrameContext.eyePosition(), sourceRotationVector)) {
                           previousRotationVector = sourceRotationVector;
                        }
                     }

                     RotationData nextRotationData = RotationData.lookAt(rotationFrameContext.eyePosition(), currentRotationVector);
                     RotationData previousRotationData = !enabled && this.moduleNumber23.get() > 0.0F ? RotationData.lookAt(rotationFrameContext.eyePosition(), previousRotationVector) : null;
                     double sourceDoubleValue = enabled ? ((Float)this.moduleNumber25.get()).floatValue() : 0.0;
                     RotationHumanizationProfile rotationHumanizationProfile;
                     if (enabled) {
                        switch ((String)this.moduleMode9.get()) {
                           case "Alternating Jerk":
                              double targetDoubleValue = (this.timestamp3++ & 1L) == 0L ? 1.0 : -1.0;
                              nextRotationData = new RotationData(
                                 currentRotationData.yaw() + targetDoubleValue * 120.0,
                                 targetDoubleValue * 45.0
                              );
                              break;
                           case "Half-GCD":
                              sourceDoubleValue = -this.rotationVanillaGcdService.vanillaGcd(previousDoubleValue) * 0.5;
                        }

                        rotationHumanizationProfile = new RotationHumanizationProfile(90.0, 0.0, 0.0, false, false, false, false, sourceDoubleValue);
                     } else {
                        rotationHumanizationProfile = new RotationHumanizationProfile(
                           ((Float)this.moduleNumber17.get()).floatValue(),
                           ((Float)this.moduleNumber18.get()).floatValue(),
                           ((Float)this.moduleNumber23.get()).floatValue() / 100.0,
                           (Boolean)this.moduleBool16.get(),
                           (Boolean)this.moduleBool17.get(),
                           (Boolean)this.moduleBool18.get(),
                           (Boolean)this.moduleBool19.get(),
                           sourceDoubleValue,
                           this.moduleRange2.low(),
                           this.moduleRange2.high(),
                           Math.round(this.moduleRange3.low()),
                           Math.round(this.moduleRange3.high()),
                           ((Float)this.moduleNumber19.get()).floatValue() / 100.0
                        );
                     }

                     RotationData sourceRotationData = this.rotationData == null ? rotationFrameContext.playerRotation() : this.rotationData;
                     OptionalDouble optionalDouble = selection.entity().hitbox().rayIntersection(rotationFrameContext.eyePosition(), sourceRotationData.direction(), nextDoubleValue);
                     int value = optionalDouble.isPresent()
                           && combatCompatibility.hasBlockLineOfSight(
                              minecraft,
                              rotationFrameContext.eyePosition(),
                              rotationFrameContext.eyePosition()
                                 .add(sourceRotationData.direction().multiply(optionalDouble.getAsDouble() + 1.0E-4))
                           )
                        ? 1
                        : 0;
                     double inputDoubleValue = selection.entity()
                        .hitbox()
                        .visibleAngularWidth(rotationFrameContext.eyePosition(), currentRotationVector, selection.entity().visibleAimPoints());
                     this.rotationNextService2
                        .next(sourceRotationData, nextRotationData, selection.entity().entityId(), previousDoubleValue, rotationHumanizationProfile, (value != 0), inputDoubleValue, previousRotationData)
                        .ifPresent(item -> {
                           this.step2 = item;
                           this.rotationData = item.rotation();
                           this.updateState16(combatCompatibility, minecraft, item.rotation());
                           this.rotationData6 = selection.entity();
                        });
                     if (this.count6 > 0) {
                        this.count6--;
                        this.rotationData6 = null;
                     }
                  }
               } else if (enabled || !this.checkCondition5(combatCompatibility, minecraft)) {
                  this.updateState9(combatCompatibility, minecraft, rotationFrameContext);
               }
            } else {
               this.updateState9(combatCompatibility, minecraft, rotationFrameContext);
            }
         }
      } else {
         this.updateState11();
      }
   }

   private void updateState6() {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      CompatOptionsTracker.reconcileOwnership();
      if (CompatCanStartService.maintain(minecraft)) {
         this.combatDecideService.interrupted(this.timestamp);
         this.rotationData6 = null;
      } else if (!CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         this.updateState7();
         this.rotationData6 = null;
      } else if (!this.combatCorrectedService.blocks(this.timestamp) && CompatAvailableService.availableForPlanning(minecraft)) {
         RotationTarget rotationTarget = this.rotationData6;
         this.rotationData6 = null;
         if (rotationTarget == null) {
            this.updateState7();
            this.values.loseTarget();
         } else {
            LivingEntity livingEntity = minecraft.level.getEntity(rotationTarget.entityId()) instanceof LivingEntity currentLivingEntity
                  && currentLivingEntity.getUUID().equals(rotationTarget.uuid())
                  && currentLivingEntity.isAlive()
               ? currentLivingEntity
               : null;
            int value = livingEntity != null && this.checkCondition3(minecraft, livingEntity) ? 1 : 0;
            LocalPlayer localPlayer = minecraft.player;
            CombatDecideService.Action action = this.combatDecideService
               .decide(
                  this.timestamp,
                  new CombatDecideService.Frame(
                     rotationTarget.uuid(),
                     (value != 0),
                     (Boolean)this.moduleBool11.get(),
                     CompatCanStartService.canStart(minecraft),
                     CompatCanStartService.holding(minecraft),
                     localPlayer.getAttackStrengthScale(0.0F),
                     localPlayer.getCurrentItemAttackStrengthDelay(),
                     CompatBlocksAttackFromService.activationDelayTicks(minecraft),
                     Math.round((Float)this.moduleNumber13.get()),
                     Math.round((Float)this.moduleNumber12.get())
                  )
               );
            switch (action) {
               case RAISE:
                  if (CompatCanStartService.start(minecraft)) {
                     this.combatDecideService.raised(this.timestamp, rotationTarget.uuid());
                  } else {
                     this.combatDecideService.interrupted(this.timestamp);
                  }

                  return;
               case RELEASE:
                  this.updateState7();
                  return;
               case HOLD:
               case WAIT:
                  return;
               default:
                  if (CompatAvailableService.available(minecraft)) {
                     if (livingEntity == null || this.checkCondition2(minecraft, livingEntity)) {
                        this.values.loseTarget();
                     } else if (!(Boolean)this.moduleBool3.get() && minecraft.player != null && livingEntity.isInvisibleTo(minecraft.player)) {
                        this.values.loseTarget();
                     } else {
                        CombatCompatibility combatCompatibility = CompatAdapterService.rotationEnvironment().orElse(null);
                        double doubleValue = Math.min((Float)this.moduleNumber.get(), (Float)this.moduleNumber3.get());
                        CombatData combatData = combatCompatibility == null ? null : combatCompatibility.captureAttack(CoreIsInitializedHandler.mc(), rotationTarget, doubleValue).orElse(null);
                        if (combatData == null) {
                           this.values.loseTarget();
                        } else {
                           long offset = System.nanoTime();
                           if ((Boolean)this.moduleBool10.get() && !checkCondition7(minecraft)) {
                              this.values.loseTarget();
                           } else {
                              CombatReleaseTracker.Profile profile = this.collectValues();
                              int currentValue = localPlayer != null && localPlayer.onGround() && localPlayer.isSprinting() ? 1 : 0;
                              if (this.values.ready(this.timestamp, offset, rotationTarget.uuid(), combatData, profile)) {
                                 if (profile.hitChance() < 1.0 && this.random.nextDouble() >= profile.hitChance()) {
                                    this.values.attacked(this.timestamp, offset, combatData, profile);
                                    if (localPlayer != null) {
                                       localPlayer.swing(InteractionHand.MAIN_HAND);
                                    }
                                 } else {
                                    if (combatCompatibility.attack(CoreIsInitializedHandler.mc(), rotationTarget, doubleValue, this.moduleBool8.get() ? 1.0F : 0.0F)) {
                                       this.values.attacked(this.timestamp, offset, combatData, profile);
                                       this.timestamp4++;
                                       if (Boolean.getBoolean("ellice.combat.trace")) {
                                          CoreIsInitializedHandler.LOGGER
                                             .info(
                                                "CombatTrace attack nanos={} tick={} target={} owner={} wire={}",
                                                new Object[]{
                                                   offset,
                                                   this.timestamp,
                                                   rotationTarget.entityId(),
                                                   CombatOwnsService.currentOwner().orElse(null),
                                                   RotationObserveService.snapshot().orElse(null)
                                                }
                                             );
                                       }

                                       this.combatDecisionTracker.attacked(this.timestamp, rotationTarget.uuid());
                                       if ((Boolean)this.moduleBool7.get()) {
                                          this.combatAttackedService
                                             .attacked(
                                                this.timestamp,
                                                rotationTarget.uuid(),
                                                (currentValue != 0),
                                                Math.max(1, Math.round((Float)this.moduleNumber7.get()))
                                             );
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
            }
         }
      } else {
         this.updateState7();
         this.rotationData6 = null;
         this.values.loseTarget();
      }
   }

   private boolean checkCondition2(Minecraft minecraft, LivingEntity livingEntity) {
      if (((String)this.moduleMode4.get()).equals("Ignore")) {
         return false;
      }

      boolean enabled = CompatBlocksAttackFromService.blocksAttackFrom(livingEntity, minecraft.player.position());
      RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
      if (currentSnapshot != null) {
         RotationVector rotationVector = currentSnapshot.wireFeetPosition();
         enabled |= CompatBlocksAttackFromService.blocksAttackFrom(livingEntity, new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z()));
      }

      return !enabled
         ? false
         : !((String)this.moduleMode4.get()).equals("Use Held Axe") || !CompatBlocksAttackFromService.canDisableWithHeldWeapon(minecraft, livingEntity);
   }

   private boolean checkCondition3(Minecraft minecraft, LivingEntity livingEntity) {
      LocalPlayer localPlayer = minecraft.player;
      RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
      if (currentSnapshot != null
         && !localPlayer.isAlliedTo(livingEntity)
         && !livingEntity.isAlliedTo(localPlayer)
         && !(
            localPlayer.distanceTo(livingEntity)
               > Math.min(
                  ((Float)this.moduleNumber14.get()).floatValue(),
                  ((Float)this.moduleNumber3.get()).floatValue() + 0.8
               )
         )
         && localPlayer.hasLineOfSight(livingEntity)) {
         double currentX = livingEntity.getX() - localPlayer.getX();
         double doubleValue = livingEntity.getZ() - localPlayer.getZ();
         double currentDoubleValue = Math.hypot(currentX, doubleValue);
         if (currentDoubleValue < 1.0E-5) {
            return false;
         }

         RotationVector rotationVector = currentSnapshot.rotation().direction();
         Vec3 vec3 = livingEntity.calculateViewVector(0.0F, livingEntity.getYHeadRot());
         return (rotationVector.x() * currentX + rotationVector.z() * doubleValue) / currentDoubleValue > 0.4
            && (-vec3.x * currentX - vec3.z * doubleValue) / currentDoubleValue > 0.3;
      } else {
         return false;
      }
   }

   private void updateState7() {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (CompatCanStartService.holding(minecraft) || CompatCanStartService.pendingRelease(minecraft)) {
         CompatCanStartService.release(minecraft);
         this.combatDecideService.interrupted(this.timestamp);
      }
   }

   private void updateState8() {
      long offset = Math.round((Float)this.moduleNumber26.get());
      String currentText = (String)this.moduleMode8.get() + ":" + (String)this.moduleMode9.get();
      if (this.rotationNextService2 == null || offset != this.timestamp2 || !currentText.equals(this.text)) {
         this.updateState10();
      }
   }

   private void updateState9(CombatCompatibility combatCompatibility, Minecraft minecraft, RotationFrameContext rotationFrameContext) {
      this.updateState7();
      this.updateState15();
      this.combatDecisionTracker.suspend();
      this.integer = null;
      this.uUID = null;
      this.combatAttackedService.clear();
      enabled2 = false;
      this.updateState13();
      if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         CompatActiveService.stop();
      }

      if (!this.checkCondition6() && this.rotationData != null) {
         if (this.rotationNextService3.completed(this.rotationData)) {
            CombatOwnsService.release(CombatOwnsService.Owner.KILL_AURA);
            this.rotationData = null;
            this.step2 = null;
            this.rotationNextService2.reset();
            this.rotationNextService3.reset();
         } else {
            double doubleValue = ((String)this.moduleMode7.get()).equals("Override")
               ? ((Float)this.moduleNumber24.get()).floatValue()
               : rotationFrameContext.mouseSensitivity();
            RotationNextService.Step step = this.rotationNextService3
               .next(this.rotationData, rotationFrameContext.playerRotation(), doubleValue, this.calculateValue2(), ((Float)this.moduleNumber18.get()).floatValue());
            this.step2 = null;
            this.rotationData = step.rotation();
            RotationPublishService.publishReturning(rotationFrameContext.playerRotation(), this.rotationData, minecraft.player.yBodyRot);
         }
      } else {
         if (this.rotationData != null) {
            CombatOwnsService.release(CombatOwnsService.Owner.KILL_AURA);
            this.rotationData = null;
            this.rotationNextService2.reset();
            this.rotationNextService3.reset();
         }

         this.step2 = null;
      }
   }

   private void updateState10() {
      this.updateState7();
      this.combatDecideService.clear();
      this.rotationNextService3.reset();
      this.combatDecisionTracker.clear();
      this.timestamp2 = Math.round((Float)this.moduleNumber26.get());
      this.rotationNextService2 = new HumanizedRotationPlanner(this.timestamp2);
      this.rotationNextService4 = new AimPointSampler(this.timestamp2 ^ 4354685564936845355L);
      this.random = new Random(this.timestamp2 ^ 7640891576956012809L);
      this.text = (String)this.moduleMode8.get() + ":" + (String)this.moduleMode9.get();
      this.timestamp3 = 0L;
      this.rotationData6 = null;
      this.integer = null;
      this.uUID = null;
      this.combatAttackedService.clear();
      enabled2 = false;
      this.step2 = null;
      this.rotationData = null;
      this.updateState13();
      CombatOwnsService.release(CombatOwnsService.Owner.KILL_AURA);
      this.updateState15();
      this.count6 = 0;
      if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         CompatActiveService.stop();
      }
   }

   private void updateState11() {
      this.updateState7();
      this.combatDecideService.clear();
      this.rotationNextService3.reset();
      this.combatDecisionTracker.suspend();
      CombatOwnsService.release(CombatOwnsService.Owner.KILL_AURA);
      if (this.values != null) {
         this.values.loseTarget();
      }

      this.rotationData6 = null;
      this.integer = null;
      this.uUID = null;
      this.combatAttackedService.clear();
      enabled2 = false;
      this.step2 = null;
      this.rotationData = null;
      this.updateState13();
      this.updateState15();
      this.count6 = 0;
      if (CombatOwnsService.owns(CombatOwnsService.Owner.KILL_AURA)) {
         CompatActiveService.stop();
      }

      if (this.rotationNextService2 != null) {
         this.rotationNextService2.reset();
      }
   }

   private void updateState12(RotationBoundingBox rotationBoundingBox, RotationVector rotationVector, int value) {
      this.count2 = value;
      this.value2 = calculateValue4(rotationVector.x(), rotationBoundingBox.minX(), rotationBoundingBox.maxX());
      this.value3 = calculateValue4(rotationVector.y(), rotationBoundingBox.minY(), rotationBoundingBox.maxY());
      this.value4 = calculateValue4(rotationVector.z(), rotationBoundingBox.minZ(), rotationBoundingBox.maxZ());
      this.updateState14();
   }

   private void updateState13() {
      if (this.rotationNextService4 != null) {
         this.rotationNextService4.reset();
      }

      this.count2 = Integer.MIN_VALUE;
      this.value2 = 0.5;
      this.value3 = 0.55;
      this.value4 = 0.5;
      this.updateState14();
   }

   private boolean checkCondition4(RotationBoundingBox rotationBoundingBox, List<RotationVector> items, RotationVector rotationVector, RotationVector currentRotationVector, int value) {
      RotationVector nextRotationVector = createRotationData72(currentRotationVector, items);
      int currentValue = nextRotationVector.subtract(currentRotationVector).lengthSquared() <= 1.0E-10 ? 1 : 0;
      int nextValue = currentValue != 0
            && !RotationSelectForCombatSelector.hasHorizontalMargin(rotationBoundingBox, currentRotationVector)
            && items.stream().anyMatch(item -> RotationSelectForCombatSelector.hasHorizontalMargin(rotationBoundingBox, item))
         ? 1
         : 0;
      if (currentValue != 0 && nextValue == 0) {
         this.updateState14();
         return true;
      }

      byte byteValue = 0;
      if (this.count3 == value) {
         RotationVector previousRotationVector = rotationBoundingBox.sample(this.value5, this.value6, this.value7);
         byteValue = (byte)(createRotationData72(previousRotationVector, items).subtract(previousRotationVector).lengthSquared() <= 1.0E-10 ? 1 : 0);
      }

      if (byteValue == 0) {
         RotationVector sourceRotationVector = this.rotationSelectForCombatSelector.chooseStableAimPoint(rotationVector, rotationBoundingBox, currentRotationVector, items);
         this.count3 = value;
         this.value5 = calculateValue4(sourceRotationVector.x(), rotationBoundingBox.minX(), rotationBoundingBox.maxX());
         this.value6 = calculateValue4(sourceRotationVector.y(), rotationBoundingBox.minY(), rotationBoundingBox.maxY());
         this.value7 = calculateValue4(sourceRotationVector.z(), rotationBoundingBox.minZ(), rotationBoundingBox.maxZ());
         this.count4 = currentValue == 0 && this.moduleBool17.get() ? this.calculateValue3(this.moduleRange5) : 0;
      }

      if (this.count4 > 0) {
         this.count4--;
         return false;
      } else {
         RotationVector targetRotationVector = rotationBoundingBox.sample(this.value5, this.value6, this.value7);
         this.updateState12(rotationBoundingBox, targetRotationVector, value);
         return true;
      }
   }

   private void updateState14() {
      this.count3 = Integer.MIN_VALUE;
      this.value5 = 0.5;
      this.value6 = 0.55;
      this.value7 = 0.5;
      this.count4 = 0;
   }

   private boolean checkCondition5(CombatCompatibility combatCompatibility, Minecraft minecraft) {
      if ((Boolean)this.moduleBool17.get() && this.integer != null && this.rotationData != null) {
         if (!this.enabled3) {
            this.enabled3 = true;
            this.count5 = this.calculateValue3(this.moduleRange4);
         }

         if (this.count5 <= 0) {
            return false;
         }

         this.count5--;
         this.rotationData = this.createRotationData(minecraft);
         this.updateState16(combatCompatibility, minecraft, this.rotationData);
         return true;
      } else {
         return false;
      }
   }

   private RotationData createRotationData(Minecraft minecraft) {
      double doubleValue = ((String)this.moduleMode7.get()).equals("Override")
         ? ((Float)this.moduleNumber24.get()).floatValue()
         : (Double)minecraft.options.sensitivity().get();
      return this.rotationNextService2.continueRotation(this.rotationData, this.rotationData, doubleValue, ((Float)this.moduleNumber17.get()).floatValue());
   }

   private void updateState15() {
      this.enabled3 = false;
      this.count5 = 0;
   }

   private void updateState16(CombatCompatibility combatCompatibility, Minecraft minecraft, RotationData rotationData) {
      combatCompatibility.applyServerRotation(minecraft, rotationData, CombatOwnsService.Owner.KILL_AURA);
      if (this.checkCondition6()) {
         CompatActiveService.follow(minecraft, rotationData, (Float)this.moduleNumber15.get(), (Float)this.moduleNumber16.get());
      } else {
         CompatActiveService.stop();
      }
   }

   private boolean checkCondition6() {
      return ((String)this.moduleMode5.get()).equals("First Person");
   }

   private CombatReleaseTracker.Profile collectValues() {
      int value = this.moduleBool5.get() && !this.moduleBool6.get() ? 1 : 0;
      return new CombatReleaseTracker.Profile(
         this.moduleRange.low(),
         this.moduleRange.high(),
         (Boolean)this.moduleBool5.get(),
         (Boolean)this.moduleBool6.get(),
         (Boolean)this.moduleBool8.get(),
         (Boolean)this.moduleBool9.get(),
         Math.round((Float)this.moduleNumber8.get()),
         ((Float)this.moduleNumber9.get()).floatValue() / 100.0,
         value != 0 ? ((Float)this.moduleNumber10.get()).floatValue() / 100.0 : 0.0,
         value != 0 ? ((Float)this.moduleNumber11.get()).floatValue() / 100.0 : 0.0
      );
   }

   private double calculateValue() {
      return switch ((String)this.moduleMode.get()) {
         case "180°" -> 180.0;
         case "Custom" -> Math.max(
            1.0, Math.min(360.0, ((Float)this.moduleNumber2.get()).floatValue())
         );
         default -> 360.0;
      };
   }

   private double calculateValue2() {
      float value = (Float)this.moduleNumber20.get();
      return value > 0.0F ? value : ((Float)this.moduleNumber17.get()).floatValue();
   }

   private RotationVector createRotationData7(RotationTarget rotationTarget, RotationVector rotationVector) {
      double doubleValue = switch ((String)this.moduleMode3.get()) {
         case "Head" -> 0.88;
         case "Chest" -> 0.62;
         case "Legs" -> 0.2;
         case "Center" -> 0.5;
         case "Custom" -> ((Float)this.moduleNumber5.get()).floatValue();
         default -> -1.0;
      };
      return !(doubleValue < 0.0) && !rotationTarget.visibleAimPoints().isEmpty()
         ? createRotationData72(
            rotationTarget.hitbox().sample(0.5, doubleValue, 0.5),
            rotationTarget.visibleAimPoints()
         )
         : rotationVector;
   }

   private int calculateValue3(ModuleSetting.Range range) {
      int value = Math.round(range.low());
      int currentValue = Math.round(range.high());
      if (currentValue < value) {
         int nextValue = value;
         value = currentValue;
         currentValue = nextValue;
      }

      return value != currentValue && this.random != null ? value + this.random.nextInt(currentValue - value + 1) : value;
   }

   private static boolean checkCondition7(Minecraft minecraft) {
      if (minecraft.player == null) {
         return false;
      }

      ItemStack itemStack = minecraft.player.getMainHandItem();
      return itemStack.is(ItemTags.SWORDS) || itemStack.is(ItemTags.AXES) || itemStack.is(Items.TRIDENT) || itemStack.is(Items.MACE);
   }

   private static double calculateValue4(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = nextDoubleValue - currentDoubleValue;
      return previousDoubleValue <= 1.0E-9
         ? 0.5
         : Math.max(0.0, Math.min(1.0, (doubleValue - currentDoubleValue) / previousDoubleValue));
   }

   private static RotationVector createRotationData72(RotationVector rotationVector, List<RotationVector> items) {
      RotationVector currentRotationVector = (RotationVector)items.get(0);
      double doubleValue = currentRotationVector.subtract(rotationVector).lengthSquared();

      for (int index = 1; index < items.size(); index++) {
         RotationVector nextRotationVector = (RotationVector)items.get(index);
         double currentDoubleValue = nextRotationVector.subtract(rotationVector).lengthSquared();
         if (currentDoubleValue < doubleValue) {
            currentRotationVector = nextRotationVector;
            doubleValue = currentDoubleValue;
         }
      }

      return currentRotationVector;
   }

   public record TickBaseWindow(UUID target, double reach, int stableTicks, double turnSpeed, long attacks) {
   }
}

