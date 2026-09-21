package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.compat.PearlThrowController;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.ContinuousSupportChecker;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.PlacementRuleEvaluator;
import dev.felix.ellice.feature.scaffold.PlacementSearchPlanner;
import dev.felix.ellice.feature.scaffold.ScaffoldAllowsService;
import dev.felix.ellice.feature.scaffold.ScaffoldBeginService;
import dev.felix.ellice.feature.scaffold.ScaffoldChooseService;
import dev.felix.ellice.feature.scaffold.ScaffoldCoastTicksService;
import dev.felix.ellice.feature.scaffold.ScaffoldCorridorService;
import dev.felix.ellice.feature.scaffold.ScaffoldData;
import dev.felix.ellice.feature.scaffold.ScaffoldDecisionTracker;
import dev.felix.ellice.feature.scaffold.ScaffoldGroundWindowService;
import dev.felix.ellice.feature.scaffold.ScaffoldMode;
import dev.felix.ellice.feature.scaffold.ScaffoldMotionPhaseData;
import dev.felix.ellice.feature.scaffold.ScaffoldMovementProfile;
import dev.felix.ellice.feature.scaffold.ScaffoldNextService;
import dev.felix.ellice.feature.scaffold.ScaffoldObservationTracker;
import dev.felix.ellice.feature.scaffold.ScaffoldOperationHandler;
import dev.felix.ellice.feature.scaffold.ScaffoldPlanService;
import dev.felix.ellice.feature.scaffold.ScaffoldPlayerSnapshot;
import dev.felix.ellice.feature.scaffold.ScaffoldPrepareService;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.ScaffoldSettings;
import dev.felix.ellice.feature.scaffold.ScaffoldShouldActivateService;
import dev.felix.ellice.feature.scaffold.ScaffoldStateController;
import dev.felix.ellice.feature.scaffold.ScaffoldSweepsService;
import dev.felix.ellice.feature.scaffold.ScaffoldTrajectorySolver;
import dev.felix.ellice.feature.scaffold.ScaffoldUsableRayService;
import dev.felix.ellice.feature.scaffold.ScaffoldValidateBeforeMovementValidator;
import dev.felix.ellice.hud.ScaffoldStatusHud;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.BlockItem;

public final class ImplSuspendForPearlService extends ModuleSettingsService {
  private static final double value = 0.12;
  private static final int count2 = 2;
  private static final long timestamp = 20L;
  private static final double value2 = 0.08;
  private static final double value3 = 0.025;
  private static final double value4 = 0.045;
  private static final double value5 = 0.2;
  private static final double value6 = 4.0;
  private final ModuleNameService moduleNameService =
      this.settingCategory("Movement")
          .description(
              "Controls movement direction, silent-yaw input remapping, sprinting, and input"
                  + " safety.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Pathing")
          .description("Controls look-ahead planning and when normal scaffold movement may start.");
  private final ModuleNameService moduleNameService3 =
      this.settingCategory("Telly")
          .description(
              "Sprint forward, jump, place a connected landing path in the air and face forward"
                  + " again. Uses a fixed bridge height.");
  private final ModuleNameService moduleNameService4 =
      this.settingCategory("Sneak")
          .description("Controls when edge-safety sneak engages and releases.");
  private final ModuleNameService moduleNameService5 =
      this.settingCategory("Rotations")
          .description(
              "Choose silent aiming or your own crosshair, then tune acquisition and return"
                  + " speed.");
  private final ModuleNameService moduleNameService6 =
      this.settingCategory("Blocks")
          .description(
              "Choose your building materials, keep a reserve and control the hotbar switch.");
  private final ModuleNameService moduleNameService7 =
      this.settingCategory("Display")
          .description("Shows usable blocks and explains when Scaffold is waiting.");
  private final ModuleNameService moduleNameService8 =
      this.settingCategory("Placement")
          .description("Controls face targeting, ray validation, use timing, and retry behavior.");
  private final ModuleNameService moduleNameService9 =
      this.settingCategory("Reliability")
          .description(
              "Configures fall protection, emergency placement, replanning, and search budgets.");
  private final ModuleNameService moduleNameService10 =
      this.settingCategory("Reproducibility")
          .description("Sets the random seed used to reproduce an automated test run.");
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Bridge Mode", new String[] {"Continuous", "Telly", "Adaptive"}, "Continuous")
              .description(
                  "Continuous holds a bridge angle. Telly uses sprint jumps with airborne placement"
                      + " and a flat landing path. Adaptive plans each face separately."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Bridge Pitch", 75.5F, 55.0F, 89.9F, 0.1F)
              .description("Held downward angle for continuous bridging."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Straight Ticks", 1.0F, 0.0F, 5.0F, 1.0F)
              .description(
                  "Airborne ticks before turning to build. Longer delays leave less time to connect"
                      + " the landing path."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Ground Ticks", 1.0F, 0.0F, 10.0F, 1.0F)
              .description(
                  "Minimum grounded ticks before the next automatic jump. The view and sprint must"
                      + " also be ready."));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Takeoff Distance", 1.15F, 0.35F, 1.5F, 0.05F)
              .description(
                  "Looks this far ahead for an edge when choosing the sprint-jump takeoff."));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Minimum Momentum", 0.07F, 0.04F, 0.2F, 0.01F)
              .description(
                  "Minimum measured horizontal velocity before an automatic sprint jump. Needs a"
                      + " short run-up."));
  private final ModuleSetting.Number moduleNumber6 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Turn Speed", 100.0F, 20.0F, 180.0F, 5.0F)
              .description(
                  "Maximum combined yaw/pitch change per tick for the airborne turn and return."));
  private final ModuleSetting.Number moduleNumber7 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Placement Turn Limit", 45.0F, 5.0F, 90.0F, 5.0F)
              .description(
                  "Waits if the preceding sent view changed faster than this. Every placement still"
                      + " requires both live and sent-eye rays."));
  private final ModuleSetting.Number moduleNumber8 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Forward Pitch", 20.0F, -45.0F, 60.0F, 1.0F)
              .description(
                  "Forward view during run-up and the first part of the jump; your camera remains"
                      + " free."));
  private final ModuleSetting.Number moduleNumber9 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Telly Landing Reserve", 0.2F, 0.0F, 1.5F, 0.05F)
              .description(
                  "Extra supported distance beyond the estimated landing before turning forward"
                      + " early."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Telly Auto Sprint", true)
              .description(
                  "Supplies the native sprint key while facing forward. Hunger and ordinary sprint"
                      + " restrictions still apply."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Telly Early Return", true)
              .description(
                  "Faces forward during descent once the landing and its reserve have confirmed"
                      + " blocks."));
  private final ModuleSetting.Mode moduleMode2 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Activation", new String[] {"Movement", "Hold Use", "Hold Sneak"}, "Movement")
              .description(
                  "Requires movement intent normally, or lets a physical use/sneak key engage the"
                      + " controller."));
  private final ModuleSetting.Mode moduleMode3 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Bridge Angle",
                  new String[] {"Backward", "Auto Diagonal", "Diagonal Left", "Diagonal Right"},
                  "Backward")
              .description(
                  "Chooses the held yaw. Auto Diagonal uses a 45-degree offset for straight travel"
                      + " and chooses the side once at acquisition."));
  private final ModuleSetting.Number moduleNumber10 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Heading Tolerance", 12.0F, 0.0F, 45.0F, 1.0F)
              .description(
                  "Replans the bridge angle for larger direction changes. Smaller mouse turns are"
                      + " followed continuously."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Diagonal Alignment", true)
              .description(
                  "Allows one ordinary sidestep at diagonal acquisition to avoid starting exactly"
                      + " on block corners."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Clear Obstacles", true)
              .description(
                  "Mines nearby snow and vegetation in your walking path using normal"
                      + " block-breaking speed."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Auto Jump", true)
              .description(
                  "Continuous jumps after the block count. Telly jumps near an edge after building"
                      + " sprint momentum. Your jump key always works."));
  private final ModuleSetting.Number moduleNumber11 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Jump Every Blocks", 6.0F, 0.0F, 12.0F, 1.0F)
              .description(
                  "Places at least this many blocks before an automatic jump. Pausing resets the"
                      + " count; zero leaves jumping to you."));
  private final ModuleSetting.Mode moduleMode4 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Jump Timing", new String[] {"Block Count", "Edge Window"}, "Block Count")
              .description(
                  "Jumps after the block count, or waits until the next edge also lacks a usable"
                      + " click. The block minimum still applies."));
  private final ModuleSetting.Mode moduleMode5 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode(
                  "Movement Mode",
                  new String[] {
                    "Follow Input",
                    "Auto Forward",
                    "Auto Backward",
                    "Auto Left",
                    "Auto Right",
                    "Auto Forward Left",
                    "Auto Forward Right",
                    "Auto Backward Left",
                    "Auto Backward Right"
                  },
                  "Follow Input")
              .description(
                  "Follows your keys or supplies one fixed automatic movement direction."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Remap Server Input", true)
              .description(
                  "Remaps movement to silent yaw so local physics and packet input agree."));
  private final ModuleSetting.Bool moduleBool7 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Suppress Invalid Sprint", true)
              .description("Stops sprinting when remapped input is not forward or is sneaking."));
  private final ModuleSetting.Bool moduleBool8 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Stable FOV", true)
              .description(
                  "Removes sprint FOV pulses while Scaffold is enabled. Keeps zoom, bow and potion"
                      + " effects."));
  private final ModuleSetting.Bool moduleBool9 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Keep Target When Stopped", true)
              .description("Keeps an active placement through brief stops and pending feedback."));
  private final ModuleSetting.Bool moduleBool10 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Sneak On Input Error", false)
              .description("Sneaks when camera-relative input diverges too far from silent yaw."));
  private final ModuleSetting.Number moduleNumber12 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Maximum Input Error", 12.0F, 0.0F, 22.5F, 0.5F)
              .description("Sets the angular mismatch in degrees that triggers safety sneak."));
  private final ModuleSetting.Number moduleNumber13 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Look Ahead", 0.65F, 0.2F, 1.2F, 0.01F)
              .description("Extends swept-footprint planning this many blocks ahead."));
  private final ModuleSetting.Number moduleNumber14 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Minimum Walk Speed", 0.02F, 0.01F, 0.2F, 0.01F)
              .description(
                  "Sets the movement threshold in blocks per tick for planning and braking."));
  private final ModuleSetting.Bool moduleBool11 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Only On Ground", false)
              .description(
                  "Starts normal scaffold movement only while grounded; jump rescue still runs."));
  private final ModuleSetting.Mode moduleMode6 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Mode("Tower Mode", new String[] {"Off", "Hold Jump"}, "Hold Jump")
              .description("Towers only while jump is physically held without horizontal input."));
  private final ModuleSetting.Number moduleNumber15 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Tower Maximum Drift", 0.08F, 0.0F, 0.2F, 0.01F)
              .description("Allows tower acquisition only below this measured horizontal speed."));
  private final ModuleSetting.Number moduleNumber16 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Tower Foot Margin", 0.08F, 0.02F, 0.3F, 0.01F)
              .description("Requires the feet to remain this far inside the tower column."));
  private final ModuleSetting.Mode moduleMode7 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Mode(
                  "Sneak Mode",
                  new String[] {
                    "Off", "Edge", "Acquire", "Edge + Acquire", "Always", "Start", "Eagle"
                  },
                  "Off")
              .description(
                  "Chooses edge, acquisition, always, or no sneak. Start helps until the first"
                      + " placement; Eagle skips configurable placement cycles."));
  private final ModuleSetting.Bool moduleBool12 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Bool("Silent Sneak", false)
              .description(
                  "Hides automatic sneak in the camera and local player model. Normal sneak physics"
                      + " and manual sneak are unchanged."));
  private final ModuleSetting.Number moduleNumber17 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Eagle Interval", 0.0F, 0.0F, 10.0F, 1.0F)
              .description(
                  "Skips this many placements between edge-sneak cycles in Eagle mode. Zero sneaks"
                      + " at every edge."));
  private final ModuleSetting.Number moduleNumber18 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Edge Sneak Distance", 0.18F, 0.02F, 0.45F, 0.01F)
              .description("Starts edge sneak this many blocks before unsupported ground."));
  private final ModuleSetting.Number moduleNumber19 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Sneak Lead Ticks", 1.0F, 0.0F, 5.0F, 1.0F)
              .description("Projects current speed ahead when deciding when to sneak."));
  private final ModuleSetting.Number moduleNumber20 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Sneak Release Delay", 2.0F, 0.0F, 8.0F, 1.0F)
              .description("Keeps automatic sneak held this many ticks after danger clears."));
  private final ModuleSetting.Bool moduleBool13 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Bool("Sneak While Falling", false)
              .description("Keeps automatic sneak active while falling toward a placement."));
  private final ModuleSetting.Number moduleNumber21 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Peak Turn Speed", 58.0F, 10.0F, 360.0F, 1.0F)
              .description("Caps silent yaw and pitch change per tick, in degrees."));
  private final ModuleSetting.Mode moduleMode8 =
      this.setting(
          this.moduleNameService5,
          (new ModuleSetting.Mode("Aim Control", new String[] {"Silent", "Camera"}, "Silent") {
                @Override
                public void set(String text) {
                  super.set("Automatic".equals(text) ? "Silent" : text);
                }
              })
              .description(
                  "Silent aims for you without moving your camera. Camera builds with your own"
                      + " crosshair and keeps aiming manual."));
  private final ModuleSetting.Number moduleNumber22 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Return Turn Speed", 80.0F, 10.0F, 180.0F, 1.0F)
              .description(
                  "Maximum degrees per tick when returning to your view after stopping, disabling"
                      + " or a manual action."));
  private final ModuleSetting.Number moduleNumber23 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Return Smoothness", 0.35F, 0.0F, 1.0F, 0.01F)
              .description(
                  "Softens the return to your current mouse view. Higher values take longer to"
                      + " release manual clicks."));
  private final ModuleSetting.Mode moduleMode9 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Mode("Turn Style", new String[] {"Linear", "Smooth"}, "Linear")
              .description(
                  "Linear uses the turn cap directly; Smooth accelerates and slows toward the"
                      + " target using Motor Smoothness."));
  private final ModuleSetting.Number moduleNumber24 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Start Delay Ticks", 0.0F, 0.0F, 10.0F, 1.0F)
              .description(
                  "Waits before automatic rotation acquisition and walking. Your physical jump and"
                      + " sneak remain available."));
  private final ModuleSetting.Bool moduleBool14 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Auto Pitch", true)
              .description(
                  "Adjusts pitch for reachable click windows. Disable to hold Bridge Pitch exactly"
                      + " on the mouse-count grid."));
  private final ModuleSetting.Range moduleRange =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Range("Pitch Limits", 55.0F, 89.9F, 45.0F, 90.0F, 0.1F)
              .description(
                  "Restricts automatic pitch search; impossible rays wait instead of silently"
                      + " exceeding these limits."));
  private final ModuleSetting.Bool moduleBool15 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Lock Bridge Rotation", true)
              .description(
                  "Faces back along the bridge once, holds the angle between blocks and adjusts"
                      + " pitch only when a new click window needs it."));
  private final ModuleSetting.Number moduleNumber25 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Motor Smoothness", 0.42F, 0.0F, 1.0F, 0.01F)
              .description("Controls turn acceleration; higher values produce gentler motion."));
  private final ModuleSetting.Number moduleNumber26 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Minimum Reaction Ticks", 1.0F, 0.0F, 5.0F, 1.0F)
              .description("Sets the shortest delay before aiming at a new block face."));
  private final ModuleSetting.Number moduleNumber27 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Maximum Reaction Ticks", 3.0F, 0.0F, 8.0F, 1.0F)
              .description("Sets the longest sampled delay before aiming at a new block face."));
  private final ModuleSetting.Number moduleNumber28 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Anchor Lead", 100.0F, 0.0F, 180.0F, 1.0F)
              .description("Controls how strongly the aim point follows predicted movement."));
  private final ModuleSetting.Number moduleNumber29 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Vertical Anchor", 50.0F, 15.0F, 85.0F, 1.0F)
              .description("Sets target height on vertical block faces, as a percentage."));
  private final ModuleSetting.Number moduleNumber30 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Placement Reach", 4.5F, 2.0F, 4.5F, 0.1F)
              .description("Sets the maximum eye-to-hit-point distance in blocks."));
  private final ModuleSetting.Number moduleNumber31 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Endpoint Dispersion", 22.0F, 0.0F, 100.0F, 1.0F)
              .description("Adds stable randomized spread to face aim points."));
  private final ModuleSetting.Number moduleNumber32 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Minimum Face Margin", 0.055F, 0.0F, 0.35F, 0.005F)
              .description("Requires the ray to land this far inside the block-face edge."));
  private final ModuleSetting.Number moduleNumber33 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Maximum Click Speed", 3.5F, 0.25F, 360.0F, 0.25F)
              .description(
                  "Maximum combined yaw/pitch change in degrees per tick before placing. Lower"
                      + " values require steadier aim and can slow building."));
  private final ModuleSetting.Mode moduleMode10 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Mode(
                  "Click Rhythm",
                  new String[] {"On Target", "Fixed Interval", "Held Use"},
                  "On Target")
              .description(
                  "On Target waits after successful uses. Fixed Interval advances on misses too;"
                      + " Held Use tries every four ticks like a held use key."));
  private final ModuleSetting.Bool moduleBool16 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Auto Select Blocks", true)
              .description(
                  "Selects a suitable block stack. Disable to build only with the block stack you"
                      + " selected yourself."));
  private final ModuleSetting.Bool moduleBool17 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Silent Hotbar", true)
              .description(
                  "Keeps your selected slot and held-item display. The server keeps the real"
                      + " building stack until you take over or Scaffold stops."));
  private final ModuleSetting.Mode moduleMode11 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Mode(
                  "Block Selection",
                  new String[] {
                    "Held First",
                    "Largest Stack",
                    "Smallest Stack",
                    "Preferred Blocks",
                    "Fixed Slot"
                  },
                  "Held First")
              .description(
                  "Chooses the next stack, then finishes it before switching between equal-priority"
                      + " stacks."));
  private final ModuleSetting.Number moduleNumber34 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Number("Building Slot", 9.0F, 1.0F, 9.0F, 1.0F)
              .description(
                  "Uses only this hotbar slot in Fixed Slot mode. An empty or unsuitable slot waits"
                      + " for a refill."));
  private final ModuleSetting.Text moduleText =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Text("Preferred Blocks", "stone, cobblestone, oak_planks", 1024)
              .description(
                  "Item IDs in priority order, separated by commas. The minecraft: prefix is"
                      + " optional."));
  private final ModuleSetting.Bool moduleBool18 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Preferred Only", false)
              .description(
                  "Uses only listed materials. Disable to fall back to other suitable blocks when"
                      + " the list runs out."));
  private final ModuleSetting.Text moduleText2 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Text("Excluded Blocks", "", 1024)
              .description(
                  "Never spends these item IDs, including from your held slot. Separate entries"
                      + " with commas; for example diamond_block, tnt."));
  private final ModuleSetting.Number moduleNumber35 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Number("Block Reserve", 0.0F, 0.0F, 128.0F, 1.0F)
              .description(
                  "Leaves this many usable hotbar blocks untouched. Excluded materials are not"
                      + " counted toward the reserve."));
  private final ModuleSetting.Bool moduleBool19 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Stop When Empty", true)
              .description(
                  "Stops walking and automatic jumps when no allowed blocks remain. Refilling the"
                      + " hotbar resumes building; your jump key still works."));
  private final ModuleSetting.Number moduleNumber36 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Number("Manual Override Ticks", 4.0F, 0.0F, 40.0F, 1.0F)
              .description(
                  "Waits this many ticks after your last hotbar, attack or item-use input before"
                      + " resuming. Zero resumes on the next tick."));
  private final ModuleSetting.Bool moduleBool20 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Bool("Status Display", true)
              .description(
                  "Shows usable blocks below the crosshair and explains why building is waiting."));
  private final ModuleSetting.Mode moduleMode12 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Mode("Status Detail", new String[] {"Compact", "Detailed"}, "Detailed")
              .description(
                  "Detailed also shows the chosen building slot and whether its hotbar switch is"
                      + " silent."));
  private final ModuleSetting.Number moduleNumber37 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Number("Low Blocks Warning", 16.0F, 0.0F, 128.0F, 1.0F)
              .description(
                  "Highlights the remaining usable supply at or below this count. Zero warns only"
                      + " when empty."));
  private final ModuleSetting.Number moduleNumber38 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Number("Status Offset", 46.0F, 20.0F, 160.0F, 1.0F)
              .description("Vertical distance below the crosshair in UI pixels."));
  private final ModuleSetting.Number moduleNumber39 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Placement Foot Distance", 0.65F, 0.3F, 1.5F, 0.05F)
              .description(
                  "Limits the horizontal gap from your feet to a placed block. Larger values also"
                      + " permit farther catching ledges."));
  private final ModuleSetting.Number moduleNumber40 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Ray Dwell Ticks", 0.0F, 0.0F, 4.0F, 1.0F)
              .description("Requires this many consecutive valid-ray ticks before placement."));
  private final ModuleSetting.Number moduleNumber41 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Use Interval Ticks", 1.0F, 1.0F, 10.0F, 1.0F)
              .description("Sets the minimum ticks between placement attempts."));
  private final ModuleSetting.Number moduleNumber42 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Feedback Timeout Ticks", 8.0F, 2.0F, 30.0F, 1.0F)
              .description("Waits this many ticks for authoritative server feedback."));
  private final ModuleSetting.Number moduleNumber43 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Reconciliation Grace Ticks", 20.0F, 4.0F, 100.0F, 1.0F)
              .description(
                  "Waits after feedback timeout before closing the exact Vanilla prediction."));
  private final ModuleSetting.Number moduleNumber44 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Retry Attempts", 2.0F, 0.0F, 5.0F, 1.0F)
              .description("Sets how many native, non-issued uses may be retried."));
  private final ModuleSetting.Number moduleNumber45 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("Retry Backoff Ticks", 1.0F, 1.0F, 10.0F, 1.0F)
              .description("Waits before retrying a native, non-issued use."));
  private final ModuleSetting.Bool moduleBool21 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Restore Hotbar Slot", true)
              .description("Restores the previously selected slot when automation stops."));
  private final ModuleSetting.Bool moduleBool22 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Jump Scaffold", true)
              .description("Keeps the bridge layer and rescue placement active while airborne."));
  private final ModuleSetting.Number moduleNumber46 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Maximum Rescue Drop", 1.5F, 0.5F, 3.0F, 0.1F)
              .description(
                  "Allows catching blocks this far below your feet, still requiring a real"
                      + " reachable ray."));
  private final ModuleSetting.Number moduleNumber47 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Rescue Search Depth", 3.0F, 1.0F, 6.0F, 1.0F)
              .description(
                  "Searches this many lower block layers when acquiring a bridge while falling."));
  private final ModuleSetting.Bool moduleBool23 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Rescue Turns", true)
              .description(
                  "Allows yaw correction during actual fall recovery, even with Lock Bridge"
                      + " Rotation enabled."));
  private final ModuleSetting.Bool moduleBool24 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Lock Bridge Height", true)
              .description("Pins placement height to the last stable deck while jumping."));
  private final ModuleSetting.Bool moduleBool25 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Safety Brake", true)
              .description("Slows or stops movement when support or placement is not ready."));
  private final ModuleSetting.Number moduleNumber48 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Minimum Foot Overlap", 0.05F, 0.02F, 0.2F, 0.01F)
              .description(
                  "Reserves this much confirmed block overlap during side-face approaches."));
  private final ModuleSetting.Bool moduleBool26 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Air Return Assist", true)
              .description("Steers back toward the last support while falling during a brake."));
  private final ModuleSetting.Number moduleNumber49 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Brake Deadline Ticks", 2.0F, 0.0F, 6.0F, 0.25F)
              .description(
                  "Advances placement urgency; ground braking uses the physical stopping"
                      + " reserve."));
  private final ModuleSetting.Bool moduleBool27 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Emergency Placement", true)
              .description("Relaxes timing and face-margin limits when a fall is imminent."));
  private final ModuleSetting.Number moduleNumber50 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Emergency Face Margin", 0.04F, 0.04F, 0.2F, 0.005F)
              .description(
                  "Sets the reduced face-edge margin; wire-safe emergencies never edge-graze."));
  private final ModuleSetting.Number moduleNumber51 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Emergency Click Speed", 6.0F, 1.0F, 12.0F, 0.25F)
              .description("Caps emergency click speed without bypassing the normal motor limit."));
  private final ModuleSetting.Bool moduleBool28 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Bool("Emergency Use Interval", true)
              .description("Lets emergencies bypass the normal delay between placements."));
  private final ModuleSetting.Number moduleNumber52 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Face Search Grid", 5.0F, 1.0F, 9.0F, 2.0F)
              .description("Sets the odd sample-grid density used across each candidate face."));
  private final ModuleSetting.Number moduleNumber53 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("GCD Search Radius", 2.0F, 0.0F, 8.0F, 1.0F)
              .description("Searches this many mouse counts around the ideal rotation."));
  private final ModuleSetting.Number moduleNumber54 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Face Raycast Budget", 256.0F, 16.0F, 512.0F, 16.0F)
              .description("Limits native raycasts per face search to control CPU cost."));
  private final ModuleSetting.Number moduleNumber55 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Ray Miss Replan Ticks", 4.0F, 1.0F, 20.0F, 1.0F)
              .description("Replans after this many consecutive misses of the target face."));
  private final ModuleSetting.Number moduleNumber56 =
      this.setting(
          this.moduleNameService9,
          new ModuleSetting.Number("Rejected Target Ticks", 3.0F, 1.0F, 20.0F, 1.0F)
              .description("Avoids a failed face for this many ticks before reconsidering it."));
  private final ModuleSetting.Number moduleNumber57 =
      this.setting(
          this.moduleNameService10,
          new ModuleSetting.Number("Seed (0 = random)", 0.0F, 0.0F, 100000.0F, 1.0F)
              .exactInput()
              .description("Uses a fixed reproducible run seed; zero generates a new seed."));
  private final ModuleSetting.Bool moduleBool29 =
      this.setting(
          this.moduleNameService10,
          new ModuleSetting.Bool("Wire Diagnostics", true)
              .description("Logs bounded UseItemOn-to-movement ordering and ray-gate failures."));
  private final ScaffoldPlanService scaffoldPlanService = new ScaffoldPlanService();
  private final ScaffoldShouldActivateService scaffoldShouldActivateService =
      new ScaffoldShouldActivateService();
  private final ScaffoldRemapService entries2 = new ScaffoldRemapService();
  private final ScaffoldTrajectorySolver scaffoldTrajectorySolver = new ScaffoldTrajectorySolver();
  private final ScaffoldGroundWindowService scaffoldGroundWindowService =
      new ScaffoldGroundWindowService();
  private final ScaffoldPrepareService scaffoldPrepareService = new ScaffoldPrepareService();
  private final PlacementSearchPlanner scaffoldPrepareService2 = new PlacementSearchPlanner();
  private String text = "";
  private boolean enabled2;
  private int count3;
  private final ScaffoldStateController scaffoldStateController = new ScaffoldStateController();
  private final ScaffoldObservationTracker scaffoldObservationTracker =
      new ScaffoldObservationTracker();
  private ScaffoldNextService scaffoldNextService;
  private ScaffoldDecisionTracker scaffoldDecisionTracker;
  private Random random2;
  private long timestamp2;
  private long timestamp3;
  private long timestamp4;
  private long timestamp5;
  private double value7;
  private double value8;
  private double value9 = Double.NaN;
  private PlacementCandidate scaffoldData6;
  private PlacementCandidate scaffoldData62;
  private ScaffoldMotionPhaseData scaffoldMotionPhaseData;
  private int count4 = -1;
  private ScaffoldDecisionTracker.TargetKey targetKey;
  private long timestamp6 = Long.MIN_VALUE;
  private PlacementCandidate scaffoldData63;
  private volatile ImplSuspendForPearlService.PlacementAttempt placementAttempt;
  private RotationData rotationData;
  private ScaffoldNextService.Step step;
  private boolean enabled3;
  private ScaffoldMotionPhaseData scaffoldMotionPhaseData2;
  private int count5 = -1;
  private int count6 = -1;
  private ScaffoldRemapService.Result entries3;
  private long timestamp7;
  private ScaffoldDecisionTracker.TargetKey targetKey2;
  private long timestamp8 = Long.MIN_VALUE;
  private boolean enabled4;
  private double value10;
  private boolean enabled5;
  private ScaffoldRemapService.Input entries4;
  private ScaffoldDecisionTracker.Decision decision;
  private boolean enabled6;
  private boolean enabled7;
  private boolean enabled8;
  private int count7;
  private ScaffoldDecisionTracker.TargetKey targetKey3;
  private long timestamp9;
  private int count8;
  private boolean enabled9;
  private BlockCoordinates scaffoldData7;
  private BlockCoordinates scaffoldData72;
  private long timestamp10;
  private long timestamp11;
  private long timestamp12;
  private long timestamp13 = Long.MIN_VALUE;
  private long timestamp14;
  private long timestamp15;
  private ScaffoldCompatibility.PlacementFeedback placementFeedback2 =
      ScaffoldCompatibility.PlacementFeedback.NONE;
  private ScaffoldBeginService.State state2;
  private boolean enabled10;
  private ImplSuspendForPearlService.PlacementAttempt placementAttempt2;
  private PlacementCandidate scaffoldData64;
  private int count9 = -1;
  private ImplSuspendForPearlService.PreparedPlacementUse preparedPlacementUse;
  private ImplSuspendForPearlService.IssuedPlacementTrace issuedPlacementTrace;
  private String text2;
  private long timestamp16 = Long.MIN_VALUE;
  private ScaffoldPlayerSnapshot scaffoldData4;
  private ScaffoldDecisionTracker.TargetKey targetKey4;
  private int count10 = -1;
  private RotationData rotationData2;
  private int count11 = -1;
  private long timestamp17 = Long.MIN_VALUE;
  private ScaffoldObservationTracker.Snapshot snapshot2 = this.scaffoldObservationTracker.current();
  private ScaffoldStatusHud renderer;
  private ComponentMountService componentMountService;

  private boolean checkCondition() {
    return !"Adaptive".equals(this.moduleMode.get());
  }

  private ScaffoldOperationHandler createScaffoldOperationHandler() {
    return "Telly".equals(this.moduleMode.get())
        ? this.scaffoldPrepareService2
        : this.scaffoldPrepareService;
  }

  public ImplSuspendForPearlService() {
    super(
        ModuleBuilderData.builder("ScaffoldWalk")
            .description("Automatically places blocks beneath and ahead of the player.")
            .category(ModuleFeatureType.MOVEMENT)
            .build());
    Set values =
        Set.of(
            "Bridge Mode",
            "Bridge Pitch",
            "Auto Jump",
            "Jump Every Blocks",
            "Movement Mode",
            "Stable FOV",
            "Peak Turn Speed",
            "Placement Reach",
            "Use Interval Ticks",
            "Restore Hotbar Slot",
            "Tower Mode",
            "Tower Maximum Drift",
            "Tower Foot Margin",
            "Wire Diagnostics",
            "Keep Target When Stopped",
            "Only On Ground",
            "Jump Scaffold",
            "Lock Bridge Height",
            "Lock Bridge Rotation",
            "Safety Brake",
            "Minimum Foot Overlap",
            "Look Ahead",
            "Sneak Mode",
            "Silent Sneak",
            "Edge Sneak Distance",
            "Sneak Lead Ticks",
            "Sneak Release Delay",
            "Sneak While Falling",
            "Sneak On Input Error",
            "Maximum Input Error",
            "Minimum Face Margin",
            "Maximum Click Speed",
            "Ray Dwell Ticks",
            "Face Search Grid",
            "GCD Search Radius",
            "Face Raycast Budget",
            "Motor Smoothness",
            "Eagle Interval");
    Set currentValues =
        Set.of(
            "Auto Select Blocks",
            "Block Selection",
            "Building Slot",
            "Preferred Blocks",
            "Preferred Only",
            "Excluded Blocks",
            "Block Reserve",
            "Status Display",
            "Status Detail",
            "Low Blocks Warning",
            "Status Offset",
            "Return Turn Speed",
            "Return Smoothness");
    Set nextValues =
        Set.of(
            "Activation",
            "Bridge Angle",
            "Heading Tolerance",
            "Clear Obstacles",
            "Diagonal Alignment",
            "Jump Timing",
            "Aim Control",
            "Turn Style",
            "Start Delay Ticks",
            "Auto Pitch",
            "Pitch Limits",
            "Click Rhythm",
            "Auto Select Blocks",
            "Placement Foot Distance",
            "Maximum Rescue Drop",
            "Rescue Search Depth",
            "Rescue Turns",
            "Silent Hotbar",
            "Manual Override Ticks",
            "Stop When Empty");

    for (ModuleSetting moduleSetting : this.settings()) {
      if (!currentValues.contains(moduleSetting.name())) {
        if (moduleSetting.name().startsWith("Telly ")) {
          moduleSetting.activeWhen(this.moduleMode, "Telly"::equals);
        } else if (nextValues.contains(moduleSetting.name())) {
          moduleSetting.activeWhen(this.moduleMode, item -> !"Adaptive".equals(item));
        } else if (!values.contains(moduleSetting.name())) {
          moduleSetting.activeWhen(this.moduleMode, "Adaptive"::equals);
        }
      }
    }

    this.moduleNumber.activeWhen(this.moduleMode, "Continuous"::equals);

    for (ModuleSetting currentModuleSetting :
        List.of(
            this.moduleMode3,
            this.moduleNumber10,
            this.moduleBool3,
            this.moduleMode4,
            this.moduleMode8,
            this.moduleBool14,
            this.moduleRange,
            this.moduleNumber24,
            this.moduleNumber33,
            this.moduleBool11,
            this.moduleBool22,
            this.moduleBool24,
            this.moduleBool15,
            this.moduleMode7,
            this.moduleBool12,
            this.moduleNumber18,
            this.moduleNumber19,
            this.moduleNumber20,
            this.moduleBool13,
            this.moduleBool10,
            this.moduleNumber12,
            this.moduleMode6,
            this.moduleNumber15,
            this.moduleNumber16,
            this.moduleNumber39,
            this.moduleNumber46,
            this.moduleNumber47,
            this.moduleBool23,
            this.moduleNumber21,
            this.moduleBool9,
            this.moduleNumber13)) {
      currentModuleSetting.activeWhen(this.moduleMode, item -> !"Telly".equals(item));
    }

    this.moduleBool5.activeWhen(this.moduleMode, item -> !"Adaptive".equals(item));
    this.moduleNumber11
        .activeWhen(this.moduleMode, "Continuous"::equals)
        .activeWhen(this.moduleBool5);
    this.moduleMode4.activeWhen(this.moduleBool5);
    this.moduleNumber17.activeWhen(this.moduleMode7, "Eagle"::equals);
    this.moduleBool12.activeWhen(this.moduleMode7, item -> !"Off".equals(item));

    for (ModuleSetting nextModuleSetting :
        List.of(
            this.moduleNumber,
            this.moduleMode3,
            this.moduleNumber10,
            this.moduleBool3,
            this.moduleNumber24,
            this.moduleBool14)) {
      nextModuleSetting.activeWhen(this.moduleMode8, "Silent"::equals);
    }

    this.moduleMode9.activeWhen(
        this.moduleMode8, item -> "Telly".equals(this.moduleMode.get()) || "Silent".equals(item));
    this.moduleRange.activeWhen(this.moduleMode8, "Silent"::equals).activeWhen(this.moduleBool14);
    this.moduleNumber25.activeWhen(
        this.moduleMode9, item -> !this.checkCondition() || "Smooth".equals(item));
    this.moduleNumber41.activeWhen(
        this.moduleMode10, item -> !this.checkCondition() || !"Held Use".equals(item));
    this.moduleMode8.label("Rotations");
    this.moduleNumber33.label("Turn Speed While Placing");

    for (ModuleSetting previousModuleSetting : List.of(this.moduleMode11, this.moduleBool17)) {
      previousModuleSetting.activeWhen(this.moduleBool16);
    }

    this.moduleNumber34
        .visibleWhen(this.moduleMode11, "Fixed Slot"::equals)
        .activeWhen(this.moduleBool16);
    this.moduleText
        .visibleWhen(this.moduleMode11, "Preferred Blocks"::equals)
        .activeWhen(this.moduleBool16);
    this.moduleBool18
        .visibleWhen(this.moduleMode11, "Preferred Blocks"::equals)
        .activeWhen(this.moduleBool16);

    for (ModuleSetting sourceModuleSetting :
        List.of(this.moduleMode12, this.moduleNumber37, this.moduleNumber38)) {
      sourceModuleSetting.visibleWhen(this.moduleBool20);
    }
  }

  @Override
  protected void onEnable() {
    this.updateState2();
    this.updateState();
    CoreIsInitializedHandler.get()
        .modules()
        .get(ImplPredictionService.class)
        .ifPresent(ImplPredictionService::suspendForScaffold);
    this.updateState35();
    this.on(EventAttackInputService.TICK)
        .priority(EventIsAfterHandler.Priority.EARLY)
        .run(item -> this.updateState4());
    this.on(EventAttackInputService.GAMEPLAY_INPUT)
        .priority(EventIsAfterHandler.Priority.EARLY)
        .run(item -> this.updateState5());
    this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE)
        .priority(EventIsAfterHandler.Priority.EARLY)
        .run(item -> this.updateState10());
    this.on(EventAttackInputService.POST_MOVEMENT_PACKET)
        .priority(EventIsAfterHandler.Priority.EARLY)
        .run(item -> this.updateState11());
    this.on(EventAttackInputService.PACKET)
        .filter(EventAttackInputService.Packet::isIncoming)
        .run(this::updateState12);
    this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED).run(this::updateState13);
    this.on(EventAttackInputService.WORLD).run(item -> this.updateState38(false));
    this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED)
        .run(item -> this.updateState38(true));
    this.renderer = new ScaffoldStatusHud();
    this.componentMountService =
        new ComponentMountService().mount(this.renderer, CoreIsInitializedHandler.get().theme());
    this.componentMountService
        .id("scaffold.status")
        .absolute()
        .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(34.0F))
        .pointerEvents(false)
        .visible(false);
    CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
    this.on(EventAttackInputService.RENDER).run(item -> this.updateState3());
  }

  @Override
  protected void onDisable() {
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.player.isAlive()
        && this.checkCondition()) {
      this.createScaffoldOperationHandler()
          .suspend(
              minecraft,
              ((Float) this.moduleNumber22.get()).floatValue(),
              ((Float) this.moduleNumber23.get()).floatValue());
    } else if (minecraft.player != null
        && minecraft.level != null
        && minecraft.player.isAlive()
        && CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)
        && RotationObserveService.current().isPresent()) {
      CombatRotationOverride.start(
          minecraft,
          CombatOwnsService.Owner.SCAFFOLD,
          ((Float) this.moduleNumber22.get()).floatValue(),
          ((Float) this.moduleNumber23.get()).floatValue());
    }

    try {
      this.updateState38(true);
    } finally {
      CompatProfileTracker.disable();
      if (this.componentMountService != null) {
        CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
      }

      this.componentMountService = null;
      this.renderer = null;
      CombatOwnsService.release(CombatOwnsService.Owner.SCAFFOLD);
      this.scaffoldNextService = null;
      this.scaffoldDecisionTracker = null;
      this.random2 = null;
    }
  }

  public void suspendForPearl() {
    try {
      this.updateState38(true);
    } finally {
      CombatOwnsService.release(CombatOwnsService.Owner.SCAFFOLD);
    }
  }

  private void updateState() {
    if (!PearlThrowController.reserved()) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (minecraft.player == null || minecraft.level == null) {
        CombatOwnsService.release(CombatOwnsService.Owner.SCAFFOLD);
      } else if (!CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
        CombatOwnsService.Owner owner = CombatOwnsService.currentOwner().orElse(null);
        if (owner == null
            || owner == CombatOwnsService.Owner.SCAFFOLD
            || this.scaffoldData6 != null
            || this.placementAttempt != null
            || this.scaffoldData72 != null
            || this.entries4 != null) {
          CompatOptionsTracker.beforeInteraction();
          CompatReleaseTracker.suspendForScaffold();
          CompatConfigureService.beforeInteraction();
          CombatOwnsService.claim(CombatOwnsService.Owner.SCAFFOLD);
          CombatRotationOverride.cancel();
        }
      }
    }
  }

  public Optional<PlacementCandidate> activePlacement() {
    return Optional.ofNullable(this.scaffoldData6);
  }

  public boolean stableFov() {
    return this.isEnabled() && (Boolean) this.moduleBool8.get();
  }

  private void updateState2() {
    ScaffoldChooseService.Policy policy =
        new ScaffoldChooseService.Policy(
            (Boolean) this.moduleBool16.get(),
            (String) this.moduleMode11.get(),
            Math.round((Float) this.moduleNumber34.get()) - 1,
            blockIds((String) this.moduleText.get()),
            (Boolean) this.moduleBool16.get()
                && "Preferred Blocks".equals(this.moduleMode11.get())
                && (Boolean) this.moduleBool18.get(),
            Set.copyOf(blockIds((String) this.moduleText2.get())),
            Math.round((Float) this.moduleNumber35.get()));
    CompatProfileTracker.configure(
        new CompatProfileTracker.Profile(
            policy,
            this.checkCondition()
                && (Boolean) this.moduleBool16.get()
                && (Boolean) this.moduleBool17.get(),
            (Boolean) this.moduleBool21.get(),
            this.checkCondition() ? Math.round((Float) this.moduleNumber36.get()) : 0));
  }

  static List<String> blockIds(String id) {
    return Arrays.stream(id.toLowerCase(Locale.ROOT).split("[,;\\s]+"))
        .filter(item -> !item.isBlank())
        .map(item -> (String) (item.contains(":") ? item : "minecraft:" + item))
        .distinct()
        .toList();
  }

  public String status() {
    if (PearlThrowController.reserved()) {
      return "Paused for pearl";
    } else if (CompatProfileTracker.paused()) {
      return "Manual input";
    } else if (this.checkCondition()) {
      return this.createScaffoldOperationHandler().status();
    } else if (this.enabled10) {
      return "Waiting for server confirmation";
    } else if (this.enabled6) {
      return "Waiting for safe footing";
    } else {
      return this.scaffoldData6 == null ? "Finding a face" : "Building";
    }
  }

  private void updateState3() {
    CoreIsInitializedHandler coreIsInitialized = CoreIsInitializedHandler.get();
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    if (this.componentMountService != null) {
      int value =
          this.moduleBool20.get()
                  && minecraft.player != null
                  && minecraft.level != null
                  && minecraft.screen == null
                  && !minecraft.options.hideGui
                  && !coreIsInitialized.screens().isActive()
              ? 1
              : 0;
      this.componentMountService.visible((value != 0));
      if (value != 0) {
        ScaffoldChooseService.Choice choice = CompatProfileTracker.stock(minecraft);
        String text = choice.available() == 0 ? choice.reason() : this.status();
        if ("Detailed".equals(this.moduleMode12.get()) && choice.slot() >= 0) {
          text =
              text
                  + " · Slot "
                  + (choice.slot() + 1)
                  + (this.checkCondition() && this.moduleBool16.get() && this.moduleBool17.get()
                      ? " · Silent"
                      : "");
        }

        this.componentMountService.position(
            0.0F, coreIsInitialized.viewport().height() / 2 + (Float) this.moduleNumber38.get());
        this.renderer.update(
            choice.available(),
            text,
            choice.available() <= Math.round((Float) this.moduleNumber37.get()));
      }
    }
  }

  public boolean ownsBlockUse(Minecraft minecraft) {
    if (this.isEnabled()
        && this.checkCondition()
        && minecraft.player != null
        && minecraft.level != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()
        && !minecraft.player.isPassenger()
        && !minecraft.player.getAbilities().flying
        && !minecraft.player.isFallFlying()
        && !minecraft.player.isAutoSpinAttack()
        && !minecraft.player.isSwimming()
        && !minecraft.player.isInWater()
        && !minecraft.player.isInLava()
        && ScaffoldData.activated(
            (String) this.moduleMode2.get(),
            minecraft.options.keyUse.isDown(),
            minecraft.options.keyShift.isDown())
        && !CompatProfileTracker.paused()
        && (minecraft.player.getMainHandItem().getItem() instanceof BlockItem
            || CompatProfileTracker.depletedVisibleHand(minecraft))) {
      ScaffoldRemapService.Input input = this.createMap(this.createMap2(minecraft), true);
      return this.createScaffoldOperationHandler().rotation() != null
          || input.hasHorizontalIntent()
          || !minecraft.player.onGround()
          || input.jump() && !"Off".equals(this.moduleMode6.get());
    } else {
      return false;
    }
  }

  public Optional<ScaffoldNextService.Step> lastRotationStep() {
    return Optional.ofNullable(this.step);
  }

  public Optional<RotationData> serverRotation() {
    return Optional.ofNullable(this.rotationData);
  }

  public Optional<ScaffoldMotionPhaseData> buildSituation() {
    return Optional.ofNullable(this.scaffoldMotionPhaseData2);
  }

  public Optional<ScaffoldRemapService.Result> lastMovementResult() {
    return Optional.ofNullable(this.entries3);
  }

  private void updateState4() {
    this.updateState2();
    CompatProfileTracker.tick(CoreIsInitializedHandler.mc());
    if (!PearlThrowController.reserved()) {
      if (!this.checkCondition()) {
        this.updateState();
      }

      this.timestamp4++;
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      boolean enabled = this.checkCondition();
      if (enabled != this.enabled2 || !this.text.equals(this.moduleMode.get())) {
        this.updateState38(true);
        this.enabled2 = enabled;
        this.text = (String) this.moduleMode.get();
      }

      if (this.enabled2) {
        if (minecraft.player == null
            || minecraft.level == null
            || minecraft.screen != null
            || minecraft.getOverlay() != null
            || minecraft.isPaused()
            || !minecraft.player.isAlive()) {
          if (minecraft.player != null && minecraft.level != null && minecraft.player.isAlive()) {
            this.createScaffoldOperationHandler()
                .suspend(
                    minecraft,
                    ((Float) this.moduleNumber22.get()).floatValue(),
                    ((Float) this.moduleNumber23.get()).floatValue());
          }

          this.createScaffoldOperationHandler()
              .reset(
                  minecraft,
                  CompatAdapterService.scaffoldEnvironment().orElse(null),
                  (Boolean) this.moduleBool21.get());
          this.updateState37();
        }
      } else {
        this.updateState8();
        if (this.preparedPlacementUse != null
            && this.preparedPlacementUse.arm().tick() < this.timestamp4) {
          this.updateState7("stale-arm", true);
        }

        Minecraft currentMinecraft = CoreIsInitializedHandler.mc();
        Optional result = CompatAdapterService.rotationEnvironment();
        Optional currentResult = CompatAdapterService.scaffoldEnvironment();
        if (!result.isEmpty()
            && !currentResult.isEmpty()
            && currentMinecraft.player != null
            && currentMinecraft.level != null) {
          CombatCompatibility combatCompatibility = (CombatCompatibility) result.get();
          ScaffoldCompatibility scaffoldCompatibility = (ScaffoldCompatibility) currentResult.get();
          this.updateState36();
          this.updateState25(scaffoldCompatibility, currentMinecraft);
          this.updateState20(scaffoldCompatibility, currentMinecraft);
          if (!currentMinecraft.player.isPassenger()
              && !currentMinecraft.player.isFallFlying()
              && !currentMinecraft.player.isAutoSpinAttack()
              && !currentMinecraft.player.isSwimming()
              && !currentMinecraft.player.isInWater()
              && !currentMinecraft.player.isInLava()
              && !currentMinecraft.player.getAbilities().flying) {
            Optional nextResult = scaffoldCompatibility.capture(currentMinecraft);
            if (nextResult.isEmpty()) {
              this.updateState33(scaffoldCompatibility, currentMinecraft, combatCompatibility);
            } else {
              ScaffoldPlayerSnapshot scaffoldPlayerSnapshot =
                  (ScaffoldPlayerSnapshot) nextResult.get();
              if (scaffoldPlayerSnapshot.onGround()) {
                this.scaffoldData72 = null;
              }

              if (this.rotationData == null) {
                this.rotationData =
                    RotationObserveService.current()
                        .or(() -> RotationPublishService.current())
                        .orElse(scaffoldPlayerSnapshot.playerRotation());
                this.rotationData =
                    new RotationData(
                        (float) this.rotationData.yaw(), (float) this.rotationData.pitch());
              }

              if (this.enabled10) {
                this.updateState34(currentMinecraft, combatCompatibility, scaffoldPlayerSnapshot);
              } else {
                boolean currentEnabled = this.enabled8;
                this.enabled8 = false;
                int value = currentMinecraft.screen == null ? 1 : 0;
                int currentValue =
                    value != 0 && this.moduleBool22.get() && !scaffoldPlayerSnapshot.onGround()
                        ? 1
                        : 0;
                int nextValue =
                    value == 0
                            || this.moduleBool11.get()
                                && !scaffoldPlayerSnapshot.onGround()
                                && currentValue == 0
                        ? 0
                        : 1;
                ScaffoldRemapService.Input input = this.createMap2(currentMinecraft);
                ScaffoldRemapService.Input currentInput = this.createMap(input, (nextValue != 0));
                this.snapshot2 =
                    this.scaffoldObservationTracker.update(
                        new ScaffoldObservationTracker.Observation(
                            this.timestamp4,
                            currentInput,
                            scaffoldPlayerSnapshot.playerRotation().yaw(),
                            scaffoldPlayerSnapshot.velocity()));
                this.enabled9 =
                    ScaffoldShouldActivateService.shouldActivate(
                        (String) this.moduleMode6.get(),
                        (value != 0),
                        currentInput,
                        scaffoldPlayerSnapshot.horizontalSpeed(),
                        ((Float) this.moduleNumber15.get()).floatValue());
                ScaffoldRemapService.Result previousResult =
                    this.createMap3(
                        scaffoldPlayerSnapshot.playerRotation().yaw(),
                        this.rotationData.yaw(),
                        currentInput,
                        false);
                ScaffoldPlayerSnapshot currentScaffoldPlayerSnapshot = scaffoldPlayerSnapshot;
                int previousValue =
                    this.scaffoldData6 == null
                            && this.placementAttempt == null
                            && this.scaffoldData72 == null
                            && this.scaffoldDecisionTracker.pendingAttemptToken() == 0L
                        ? 0
                        : 1;
                int currentY =
                    value == 0
                            || !this.enabled9
                                && (nextValue == 0 || !currentInput.hasHorizontalIntent())
                                && !(scaffoldPlayerSnapshot.horizontalSpeed()
                                    >= ((Float) this.moduleNumber14.get()).floatValue())
                                && this.placementAttempt == null
                                && this.placementAttempt2 == null
                                && (!this.moduleBool9.get() || previousValue == 0)
                                && (currentValue == 0
                                    || !input.jump()
                                        && !(Math.abs(scaffoldPlayerSnapshot.velocity().y()) > 0.01)
                                        && previousValue == 0)
                        ? 0
                        : 1;
                if (currentY == 0 && this.issuedPlacementTrace != null) {
                  this.updateState34(currentMinecraft, combatCompatibility, scaffoldPlayerSnapshot);
                } else if (currentY == 0) {
                  this.scaffoldGroundWindowService.reset();
                  this.enabled3 = false;
                  this.updateState27();
                  this.updateState24();
                  this.scaffoldDecisionTracker.prepareTick(
                      this.timestamp4, this.createContext(scaffoldCompatibility, currentMinecraft));
                  this.scaffoldData6 = null;
                  this.enabled9 = false;
                  this.scaffoldData7 = null;
                  this.scaffoldData62 = null;
                  this.scaffoldMotionPhaseData = null;
                  this.count4 = -1;
                  this.targetKey = null;
                  this.timestamp6 = Long.MIN_VALUE;
                  this.step = null;
                  this.targetKey4 = null;
                  this.count10 = -1;
                  this.timestamp17 = Long.MIN_VALUE;
                  this.scaffoldMotionPhaseData2 = null;
                  this.scaffoldNextService.reset();
                  this.scaffoldObservationTracker.reset();
                  this.snapshot2 = this.scaffoldObservationTracker.current();
                  if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
                    ScaffoldPublishService.clear();
                  }

                  if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
                    PlacementOverrideBus.clear();
                  }

                  if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
                    LocalPhysicsFrameBus.clear();
                  }

                  this.entries3 = null;
                  this.entries4 = null;
                  this.scaffoldData4 = null;
                  this.decision = null;
                  this.enabled6 = false;
                  this.enabled7 = false;
                  this.enabled8 = false;
                  this.timestamp7 = 0L;
                  if (this.placementAttempt2 != null) {
                    this.updateState34(
                        currentMinecraft, combatCompatibility, scaffoldPlayerSnapshot);
                  } else {
                    this.rotationData =
                        new RotationData(
                            (float) scaffoldPlayerSnapshot.playerRotation().yaw(),
                            (float) scaffoldPlayerSnapshot.playerRotation().pitch());
                    combatCompatibility.clearServerRotation(
                        currentMinecraft, CombatOwnsService.Owner.SCAFFOLD);
                  }
                } else {
                  boolean nextEnabled = this.checkCondition23(this.scaffoldData6);
                  if (this.enabled9 && this.scaffoldData6 != null && !nextEnabled) {
                    if (this.placementAttempt == null) {
                      this.updateState31();
                    } else {
                      this.enabled9 = false;
                    }
                  } else if (!this.enabled9 && nextEnabled && this.placementAttempt == null) {
                    this.updateState31();
                  }

                  if (!this.enabled9 && !this.checkCondition23(this.scaffoldData6)) {
                    this.scaffoldData7 = null;
                  }

                  if (!this.enabled9
                      && this.snapshot2.structuralCommit()
                      && this.snapshot2.intentEpoch() > 1
                      && this.scaffoldData6 != null
                      && this.placementAttempt == null) {
                    this.updateState31();
                  }

                  if (!this.enabled10 && this.placementAttempt2 == null) {
                    long offset = this.calculateValue();
                    Integer sourceValue = confirmedDeckLayer(offset, this.scaffoldData63);
                    this.count8 =
                        this.moduleBool24.get()
                            ? this.scaffoldStateController.update(
                                scaffoldPlayerSnapshot, sourceValue)
                            : (int) Math.floor(scaffoldPlayerSnapshot.feetPosition().y() - 1.0);
                    this.updateState15(
                        scaffoldCompatibility, currentMinecraft, scaffoldPlayerSnapshot, offset);
                    if (this.placementAttempt2 != null) {
                      this.updateState34(
                          currentMinecraft, combatCompatibility, scaffoldPlayerSnapshot);
                    } else {
                      if (this.scaffoldData6 == null
                          && offset != 0L
                          && this.checkCondition4(
                              scaffoldCompatibility, currentMinecraft, scaffoldPlayerSnapshot)
                          && this.checkCondition5(
                              scaffoldCompatibility,
                              currentMinecraft,
                              currentScaffoldPlayerSnapshot)) {
                        this.scaffoldData6 = this.scaffoldData62;
                        this.scaffoldData62 = null;
                        this.scaffoldMotionPhaseData = null;
                        this.count4 = -1;
                        this.targetKey = null;
                        this.timestamp6 = Long.MIN_VALUE;
                        this.count7 = 0;
                      }

                      if (this.scaffoldData6 == null) {
                        if (this.scaffoldData62 != null && this.scaffoldNextService != null) {
                          this.scaffoldNextService.reset();
                        }

                        this.scaffoldData62 = null;
                        this.scaffoldMotionPhaseData = null;
                        this.count4 = -1;
                        this.targetKey = null;
                        this.timestamp6 = Long.MIN_VALUE;
                        Optional sourceResult =
                            this.findResult2(
                                scaffoldCompatibility,
                                currentMinecraft,
                                currentScaffoldPlayerSnapshot);
                        if (sourceResult.isPresent()) {
                          ScaffoldMotionPhaseData currentScaffoldMotionPhaseData =
                              this.createScaffoldMotionPhaseData(
                                  currentScaffoldPlayerSnapshot,
                                  (PlacementCandidate) sourceResult.get(),
                                  this.scaffoldData63,
                                  this.value9,
                                  this.checkCondition25());
                          this.scaffoldData6 =
                              this.createScaffoldData6(
                                  scaffoldCompatibility,
                                  currentMinecraft,
                                  (PlacementCandidate) sourceResult.get(),
                                  currentScaffoldMotionPhaseData);
                          this.count7 = 0;
                        }
                      }

                      if (this.scaffoldData6 != null) {
                        this.scaffoldMotionPhaseData2 =
                            this.createScaffoldMotionPhaseData(
                                currentScaffoldPlayerSnapshot,
                                this.scaffoldData6,
                                this.scaffoldData63,
                                this.value9,
                                this.checkCondition25());
                        this.value9 = this.scaffoldMotionPhaseData2.movementYaw();
                      } else {
                        this.scaffoldMotionPhaseData2 = null;
                      }

                      this.updateState16(
                          scaffoldCompatibility, currentMinecraft, currentScaffoldPlayerSnapshot);
                      int targetValue =
                          scaffoldCompatibility.findPlaceableHotbarSlot(currentMinecraft);
                      int inputValue = scaffoldCompatibility.selectedHotbarSlot(currentMinecraft);
                      ScaffoldDecisionTracker.TargetKey currentTargetKey =
                          createTargetKey(this.scaffoldData6);
                      Optional targetResult =
                          this.findResult3(
                              scaffoldCompatibility, currentMinecraft, this.rotationData);
                      ScaffoldMotionPhaseData.PathRelation currentPathRelation =
                          this.scaffoldMotionPhaseData2 == null
                              ? ScaffoldMotionPhaseData.PathRelation.FIRST
                              : this.scaffoldMotionPhaseData2.pathRelation();
                      ScaffoldDecisionTracker.Context context =
                          new ScaffoldDecisionTracker.Context(
                              true,
                              inputValue,
                              targetValue,
                              currentTargetKey,
                              currentPathRelation,
                              offset);
                      this.scaffoldDecisionTracker.configure(
                          Math.round((Float) this.moduleNumber42.get()),
                          Math.round((Float) this.moduleNumber44.get()),
                          Math.round((Float) this.moduleNumber45.get()));
                      ScaffoldDecisionTracker.Decision currentDecision =
                          this.scaffoldDecisionTracker.prepareTick(this.timestamp4, context);
                      this.decision = currentDecision;
                      if (this.scaffoldData6 != null
                          && currentDecision.lockedTarget() == null
                          && currentDecision.cognitivePhase()
                              == ScaffoldDecisionTracker.CognitivePhase.RECOVER) {
                        this.updateState30();
                      }

                      if (currentDecision.selectBlock() && targetValue >= 0) {
                        if (this.count5 < 0) {
                          this.count5 = inputValue;
                        }

                        scaffoldCompatibility.selectHotbarSlot(currentMinecraft, targetValue);
                        this.count6 = targetValue;
                      }

                      double doubleValue =
                          !this.moduleBool27.get()
                                  || scaffoldPlayerSnapshot.onGround()
                                      && (this.scaffoldMotionPhaseData2 == null
                                          || !(this.scaffoldMotionPhaseData2.deadlineTicks()
                                              <= ((Float) this.moduleNumber49.get()).floatValue()))
                              ? ((Float) this.moduleNumber32.get()).floatValue()
                              : safeEmergencyFaceMargin(
                                  ((Float) this.moduleNumber32.get()).floatValue(),
                                  ((Float) this.moduleNumber50.get()).floatValue());
                      boolean previousEnabled =
                          ScaffoldUsableRayService.usableRay(targetResult, doubleValue);
                      int outputValue =
                          this.scaffoldData6 != null
                                  && this.timestamp8 == this.timestamp4 - 1L
                                  && createTargetKey(this.scaffoldData6).equals(this.targetKey2)
                              ? 1
                              : 0;
                      boolean sourceEnabled = this.checkCondition8(currentTargetKey);
                      long currentOffset = this.scaffoldDecisionTracker.pendingAttemptToken();
                      boolean targetEnabled =
                          authoritativePlacementReady(
                              sourceEnabled, this.checkCondition17(currentOffset));
                      if (outputValue != 0) {
                        previousEnabled = this.enabled4;
                      }

                      boolean inputEnabled = this.checkCondition9(currentTargetKey);
                      this.enabled7 = !(!currentEnabled && !inputEnabled);
                      this.enabled6 =
                          this.enabled7
                              || this.checkCondition12(
                                  scaffoldCompatibility,
                                  currentMinecraft,
                                  scaffoldPlayerSnapshot,
                                  previousResult,
                                  targetEnabled);
                      this.entries4 = currentInput;
                      this.scaffoldData4 = currentScaffoldPlayerSnapshot;
                    }
                  } else {
                    this.updateState34(
                        currentMinecraft, combatCompatibility, scaffoldPlayerSnapshot);
                  }
                }
              }
            }
          } else {
            this.updateState33(scaffoldCompatibility, currentMinecraft, combatCompatibility);
          }
        } else {
          this.updateState37();
        }
      }
    }
  }

  private void updateState5() {
    this.updateState2();
    CompatProfileTracker.inspectInput(CoreIsInitializedHandler.mc());
    if (!PearlThrowController.reserved()) {
      if (this.checkCondition() && (!this.enabled2 || !this.text.equals(this.moduleMode.get()))) {
        this.updateState38(true);
        this.enabled2 = true;
        this.text = (String) this.moduleMode.get();
      }

      if (this.enabled2) {
        Minecraft minecraft = CoreIsInitializedHandler.mc();
        ScaffoldCompatibility scaffoldCompatibility =
            CompatAdapterService.scaffoldEnvironment().orElse(null);
        CombatCompatibility combatCompatibility =
            CompatAdapterService.rotationEnvironment().orElse(null);
        if (scaffoldCompatibility != null
            && combatCompatibility != null
            && minecraft.player != null
            && minecraft.level != null) {
          this.createScaffoldOperationHandler()
              .prepare(
                  minecraft,
                  scaffoldCompatibility,
                  combatCompatibility,
                  this.createMap(this.createMap2(minecraft), minecraft.screen == null),
                  new ScaffoldSettings(
                      ((Float) this.moduleNumber21.get()).floatValue(),
                      ((Float) this.moduleNumber.get()).floatValue(),
                      (Boolean) this.moduleBool5.get(),
                      Math.round((Float) this.moduleNumber11.get()),
                      (Boolean) this.moduleBool9.get(),
                      (Boolean) this.moduleBool11.get(),
                      (Boolean) this.moduleBool22.get(),
                      (Boolean) this.moduleBool24.get(),
                      (Boolean) this.moduleBool15.get(),
                      (Boolean) this.moduleBool25.get(),
                      ((Float) this.moduleNumber48.get()).floatValue(),
                      ((Float) this.moduleNumber13.get()).floatValue(),
                      (String) this.moduleMode7.get(),
                      ((Float) this.moduleNumber18.get()).floatValue(),
                      Math.round((Float) this.moduleNumber19.get()),
                      Math.round((Float) this.moduleNumber20.get()),
                      (Boolean) this.moduleBool13.get(),
                      (Boolean) this.moduleBool10.get(),
                      ((Float) this.moduleNumber12.get()).floatValue(),
                      ((Float) this.moduleNumber32.get()).floatValue(),
                      ((Float) this.moduleNumber33.get()).floatValue(),
                      Math.round((Float) this.moduleNumber40.get()),
                      Math.round((Float) this.moduleNumber41.get()),
                      ((Float) this.moduleNumber30.get()).floatValue(),
                      Math.round((Float) this.moduleNumber52.get()),
                      Math.round((Float) this.moduleNumber53.get()),
                      Math.round((Float) this.moduleNumber54.get()),
                      (Boolean) this.moduleBool29.get(),
                      (Boolean) this.moduleBool4.get(),
                      (String) this.moduleMode6.get(),
                      ((Float) this.moduleNumber15.get()).floatValue(),
                      ((Float) this.moduleNumber16.get()).floatValue(),
                      (Boolean) this.moduleBool19.get(),
                      ((Float) this.moduleNumber22.get()).floatValue(),
                      ((Float) this.moduleNumber23.get()).floatValue(),
                      new ScaffoldData(
                          (String) this.moduleMode2.get(),
                          (String) this.moduleMode8.get(),
                          (String) this.moduleMode3.get(),
                          (String) this.moduleMode9.get(),
                          ((Float) this.moduleNumber25.get()).floatValue(),
                          Math.round((Float) this.moduleNumber24.get()),
                          ((Float) this.moduleNumber10.get()).floatValue(),
                          (Boolean) this.moduleBool14.get(),
                          ((ModuleSetting.RangeValue) this.moduleRange.get()).low(),
                          ((ModuleSetting.RangeValue) this.moduleRange.get()).high(),
                          (Boolean) this.moduleBool3.get(),
                          (String) this.moduleMode10.get(),
                          Math.round((Float) this.moduleNumber17.get()),
                          (String) this.moduleMode4.get(),
                          (Boolean) this.moduleBool16.get(),
                          ((Float) this.moduleNumber39.get()).floatValue(),
                          ((Float) this.moduleNumber46.get()).floatValue(),
                          Math.round((Float) this.moduleNumber47.get()),
                          (Boolean) this.moduleBool23.get()),
                      new ScaffoldMovementProfile(
                          Math.round((Float) this.moduleNumber2.get()),
                          Math.round((Float) this.moduleNumber3.get()),
                          ((Float) this.moduleNumber4.get()).floatValue(),
                          ((Float) this.moduleNumber5.get()).floatValue(),
                          ((Float) this.moduleNumber6.get()).floatValue(),
                          ((Float) this.moduleNumber7.get()).floatValue(),
                          ((Float) this.moduleNumber8.get()).floatValue(),
                          ((Float) this.moduleNumber9.get()).floatValue(),
                          (Boolean) this.moduleBool.get(),
                          (Boolean) this.moduleBool2.get()),
                      (Boolean) this.moduleBool12.get() && !"Telly".equals(this.moduleMode.get())),
                  this::updateState);
          this.rotationData = this.createScaffoldOperationHandler().rotation();
          this.entries3 = this.createScaffoldOperationHandler().movement();
          this.enabled6 = this.createScaffoldOperationHandler().braking();
          this.scaffoldData6 = this.createScaffoldOperationHandler().lastPlacement();
        } else {
          this.createScaffoldOperationHandler().release();
          this.rotationData = null;
          this.entries3 = null;
        }
      } else {
        ScaffoldPlayerSnapshot scaffoldPlayerSnapshot = this.scaffoldData4;
        if (scaffoldPlayerSnapshot != null && this.entries4 != null) {
          this.scaffoldData4 = null;
          Minecraft currentMinecraft = CoreIsInitializedHandler.mc();
          Optional result = CompatAdapterService.rotationEnvironment();
          Optional currentResult = CompatAdapterService.scaffoldEnvironment();
          if (!result.isEmpty()
              && !currentResult.isEmpty()
              && currentMinecraft.player != null
              && currentMinecraft.level != null
              && currentMinecraft.screen == null) {
            ScaffoldCompatibility currentScaffoldCompatibility =
                (ScaffoldCompatibility) currentResult.get();
            ScaffoldPlayerSnapshot currentScaffoldPlayerSnapshot =
                currentScaffoldCompatibility
                    .capture(currentMinecraft)
                    .orElse(scaffoldPlayerSnapshot);
            int value = this.issuedPlacementTrace != null ? 1 : 0;
            int currentValue =
                value == 0
                        && this.checkCondition2(
                            currentScaffoldCompatibility,
                            currentMinecraft,
                            currentScaffoldPlayerSnapshot)
                    ? 1
                    : 0;
            this.step = null;
            this.targetKey4 = null;
            this.count10 = -1;
            this.timestamp17 = Long.MIN_VALUE;
            boolean enabled = wireLifecycleMustHold((currentValue != 0), (value != 0));
            if (shouldAdvanceMotorAfterUse(enabled)) {
              this.updateState6(
                  currentMinecraft,
                  (CombatCompatibility) result.get(),
                  currentScaffoldCompatibility,
                  currentScaffoldPlayerSnapshot);
            } else {
              if (value != 0) {
                this.rotationData =
                    postUseServerRotation(
                        true, this.issuedPlacementTrace.arm().wireRotation(), this.rotationData);
              }

              ((CombatCompatibility) result.get())
                  .applyServerRotation(
                      currentMinecraft, this.rotationData, CombatOwnsService.Owner.SCAFFOLD);
            }

            double doubleValue =
                !this.moduleBool27.get()
                        || currentScaffoldPlayerSnapshot.onGround()
                            && (this.scaffoldMotionPhaseData2 == null
                                || !(this.scaffoldMotionPhaseData2.deadlineTicks()
                                    <= ((Float) this.moduleNumber49.get()).floatValue()))
                    ? ((Float) this.moduleNumber32.get()).floatValue()
                    : safeEmergencyFaceMargin(
                        ((Float) this.moduleNumber32.get()).floatValue(),
                        ((Float) this.moduleNumber50.get()).floatValue());
            Optional nextResult =
                this.findResult3(currentScaffoldCompatibility, currentMinecraft, this.rotationData);
            boolean currentEnabled = ScaffoldUsableRayService.usableRay(nextResult, doubleValue);
            this.updateState17(
                currentScaffoldCompatibility,
                currentMinecraft,
                currentScaffoldPlayerSnapshot,
                this.entries4,
                currentEnabled);
          } else {
            this.updateState32();
            this.entries4 = null;
          }
        }
      }
    }
  }

  private void updateState6(
      Minecraft minecraft,
      CombatCompatibility combatCompatibility,
      ScaffoldCompatibility scaffoldCompatibility,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    this.enabled3 = false;
    int value =
        this.scaffoldData6 != null && this.checkCondition8(createTargetKey(this.scaffoldData6))
            ? 1
            : 0;
    int currentValue = this.scaffoldMotionPhaseData != null && this.checkCondition10() ? 1 : 0;
    PlacementCandidate placementCandidate =
        currentValue != 0 ? this.scaffoldData62 : this.scaffoldData6;
    ScaffoldMotionPhaseData currentScaffoldMotionPhaseData =
        currentValue != 0 ? this.scaffoldMotionPhaseData : this.scaffoldMotionPhaseData2;
    int nextValue =
        this.scaffoldData6 != null
                && this.decision != null
                && createTargetKey(this.scaffoldData6).equals(this.decision.lockedTarget())
            ? 1
            : 0;
    int previousValue =
        this.decision != null
                && this.decision.rotate()
                && mayAdvancePlacementMotor((currentValue != 0), (value != 0), (nextValue != 0))
            ? 1
            : 0;
    if (value != 0 && (currentValue == 0 || previousValue == 0)) {
      this.scaffoldNextService.reset();
    }

    if (placementCandidate != null
        && currentScaffoldMotionPhaseData != null
        && previousValue != 0
        && this.checkCondition24(scaffoldCompatibility, minecraft, placementCandidate)) {
      RotationVector rotationVector = currentScaffoldMotionPhaseData.predictedEyeAtClick();
      int sourceValue =
          !this.moduleBool27.get()
                  || !this.enabled6
                      && scaffoldPlayerSnapshot.onGround()
                      && !(currentScaffoldMotionPhaseData.deadlineTicks()
                          <= ((Float) this.moduleNumber49.get()).floatValue())
              ? 0
              : 1;
      double doubleValue =
          sourceValue != 0
              ? safeEmergencyFaceMargin(
                  ((Float) this.moduleNumber32.get()).floatValue(),
                  ((Float) this.moduleNumber50.get()).floatValue())
              : ((Float) this.moduleNumber32.get()).floatValue();
      int targetValue = this.enabled9 && this.checkCondition23(placementCandidate) ? 1 : 0;
      int inputValue =
          this.moduleBool15.get()
                  && targetValue == 0
                  && scaffoldPlayerSnapshot.onGround()
                  && this.snapshot2 != null
                  && this.snapshot2.hasRequestedDirection()
              ? 1
              : 0;
      if (inputValue == 0) {
        this.rotationData2 = null;
      }

      if (inputValue != 0
          && (this.rotationData2 == null || this.count11 != this.snapshot2.intentEpoch())) {
        double currentDoubleValue = currentScaffoldMotionPhaseData.movementYaw() + 180.0;
        double nextDoubleValue =
            new RotationVanillaGcdService().vanillaGcd(scaffoldPlayerSnapshot.mouseSensitivity());
        this.rotationData2 =
            ScaffoldNextService.applyMouseCounts(
                this.rotationData,
                Math.round(
                    RotationData.yawDelta(this.rotationData.yaw(), currentDoubleValue)
                        / nextDoubleValue),
                0L,
                scaffoldPlayerSnapshot.mouseSensitivity());
        this.count11 = this.snapshot2.intentEpoch();
      }

      RotationData currentRotationData =
          inputValue != 0
              ? new RotationData(this.rotationData2.yaw(), this.rotationData.pitch())
              : this.rotationData;
      int outputValue = currentValue != 0 ? Math.max(1, this.count4) : this.decision.motorPlanId();
      int resultValue =
          currentValue == 0
                  && targetValue == 0
                  && scaffoldPlayerSnapshot.onGround()
                  && this.entries4 != null
                  && !this.entries4.jump()
              ? 1
              : 0;
      int candidateValue = Math.round((Float) this.moduleNumber54.get());
      RotationData nextRotationData =
          RotationData.lookAt(rotationVector, placementCandidate.hitPoint());
      double previousDoubleValue = 0.0;
      byte byteValue = 0;
      Optional<ScaffoldTrajectorySolver.Solution> result = Optional.empty();
      if (resultValue != 0) {
        ScaffoldGroundWindowService.Result currentResult =
            this.scaffoldGroundWindowService.select(
                currentRotationData,
                placementCandidate,
                scaffoldPlayerSnapshot.mouseSensitivity(),
                Math.max(1, outputValue),
                this.snapshot2 == null ? 0L : this.snapshot2.intentEpoch(),
                doubleValue,
                this.calculateValue4(),
                Math.round((Float) this.moduleNumber53.get()),
                candidateValue,
                item ->
                    this.findResult(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, item),
                (item, currentItem) ->
                    scaffoldCompatibility.raycastBlockFromEye(
                        minecraft,
                        currentItem,
                        ((Float) this.moduleNumber30.get()).floatValue(),
                        item),
                (inputValue != 0));
        candidateValue -= currentResult.nativeRays();
        result = currentResult.solution();
        this.enabled3 = result.isPresent();
        byteValue =
            (byte)
                (!currentResult.currentUsable()
                        || inputValue != 0
                            && (float) this.rotationData.yaw() != (float) currentRotationData.yaw()
                    ? 0
                    : 1);
        previousDoubleValue = currentResult.currentMargin();
        this.updateState9(
            "aim-window",
            false,
            "target="
                + createTargetKey(placementCandidate)
                + " useTick="
                + (this.timestamp4 + 1L)
                + " currentValid="
                + currentResult.currentUsable()
                + " goalValid="
                + this.enabled3
                + " nativeRays="
                + currentResult.nativeRays());
      } else {
        this.scaffoldGroundWindowService.reset();
      }

      if (result.isEmpty() && candidateValue > 0) {
        Optional<PlacementCandidate> nextResult =
            scaffoldCompatibility.raycastPlacementFromEye(
                minecraft,
                placementCandidate,
                this.rotationData,
                ((Float) this.moduleNumber30.get()).floatValue(),
                rotationVector);
        candidateValue += -1;
        int currentByteValue = ScaffoldUsableRayService.usableRay(nextResult, doubleValue) ? 1 : 0;
        if (currentByteValue != 0) {
          nextRotationData = this.rotationData;
        }

        if (resultValue == 0) {
          byteValue = (byte) currentByteValue;
          previousDoubleValue = nextResult.map(PlacementCandidate::faceEdgeMargin).orElse(0.0);
        }

        if (currentByteValue == 0 && targetValue == 0 && candidateValue > 0) {
          result =
              inputValue != 0
                  ? this.scaffoldTrajectorySolver.solveKeepingYaw(
                      currentRotationData,
                      rotationVector,
                      placementCandidate,
                      scaffoldPlayerSnapshot.mouseSensitivity(),
                      doubleValue,
                      candidateValue,
                      (item, currentItem) ->
                          scaffoldCompatibility.raycastPlacementFromEye(
                              minecraft,
                              item,
                              currentItem,
                              ((Float) this.moduleNumber30.get()).floatValue(),
                              rotationVector))
                  : this.scaffoldTrajectorySolver.solve(
                      this.rotationData,
                      rotationVector,
                      placementCandidate,
                      scaffoldPlayerSnapshot.mouseSensitivity(),
                      doubleValue,
                      this.calculateValue4(),
                      Math.round((Float) this.moduleNumber53.get()),
                      candidateValue,
                      (item, currentItem) ->
                          scaffoldCompatibility.raycastPlacementFromEye(
                              minecraft,
                              item,
                              currentItem,
                              ((Float) this.moduleNumber30.get()).floatValue(),
                              rotationVector));
        }

        if (currentByteValue == 0) {
          nextRotationData = RotationData.lookAt(rotationVector, placementCandidate.hitPoint());
        }
      }

      if (byteValue == 0 && targetValue != 0) {
        nextRotationData = towerLookRotation(this.rotationData);
      } else if (result.isPresent()) {
        ScaffoldTrajectorySolver.Solution currentSolution =
            (ScaffoldTrajectorySolver.Solution) result.get();
        placementCandidate = currentSolution.anchor();
        if (currentValue != 0) {
          this.scaffoldData62 = placementCandidate;
          this.scaffoldMotionPhaseData =
              this.createScaffoldMotionPhaseData(
                  scaffoldPlayerSnapshot,
                  placementCandidate,
                  this.scaffoldData6,
                  this.value9,
                  false);
          currentScaffoldMotionPhaseData = this.scaffoldMotionPhaseData;
        } else {
          this.scaffoldData6 = placementCandidate;
          this.scaffoldMotionPhaseData2 =
              this.createScaffoldMotionPhaseData(
                  scaffoldPlayerSnapshot,
                  placementCandidate,
                  this.scaffoldData63,
                  this.value9,
                  this.checkCondition25());
          currentScaffoldMotionPhaseData = this.scaffoldMotionPhaseData2;
        }

        nextRotationData = currentSolution.rotation();
      }

      if (inputValue != 0) {
        ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
        double currentX =
            scaffoldPlayerSnapshot
                .eyePosition()
                .subtract(placementCandidate.hitPoint())
                .dot(new RotationVector(scaffoldMode.x(), scaffoldMode.y(), scaffoldMode.z()));
        int selectedValue =
            this.scaffoldData63 != null
                    && currentX < 0.0
                    && this.scaffoldData63.clickedFace() == scaffoldMode
                ? 1
                : 0;
        nextRotationData =
            new RotationData(
                this.rotationData2.yaw(),
                selectedValue != 0 ? this.rotationData.pitch() : nextRotationData.pitch());
      }

      int defaultValue = Math.round((Float) this.moduleNumber26.get());
      int initialValue = Math.max(defaultValue, Math.round((Float) this.moduleNumber27.get()));
      int resolvedValue = this.enabled3 && this.moduleBool27.get() ? 1 : 0;
      int computedValue =
          sourceValue == 0 && targetValue == 0 && resolvedValue == 0 ? defaultValue : 0;
      int cachedValue =
          sourceValue == 0 && targetValue == 0 && resolvedValue == 0 ? initialValue : 0;
      double sourceDoubleValue =
          calculateValue8(
              ((Float) this.moduleNumber25.get()).floatValue()
                  + currentScaffoldMotionPhaseData.smoothnessOffset(),
              0.0,
              1.0);
      ScaffoldNextService.Step currentStep =
          this.scaffoldNextService.next(
              new ScaffoldNextService.Request(
                  this.timestamp4,
                  this.rotationData,
                  nextRotationData,
                  Math.max(1, outputValue),
                  scaffoldPlayerSnapshot.mouseSensitivity(),
                  ((Float) this.moduleNumber21.get()).floatValue(),
                  sourceDoubleValue,
                  calculateValue5(scaffoldPlayerSnapshot, placementCandidate),
                  (byteValue != 0),
                  previousDoubleValue,
                  computedValue,
                  cachedValue,
                  doubleValue,
                  currentScaffoldMotionPhaseData),
              resultValue == 0);
      this.step = currentStep;
      this.rotationData = currentStep.rotation();
      this.targetKey4 = createTargetKey(placementCandidate);
      this.count10 = outputValue;
      this.timestamp17 = this.timestamp4;
      combatCompatibility.applyServerRotation(
          minecraft, this.rotationData, CombatOwnsService.Owner.SCAFFOLD);
    } else {
      combatCompatibility.applyServerRotation(
          minecraft, this.rotationData, CombatOwnsService.Owner.SCAFFOLD);
    }
  }

  private Optional<ScaffoldGroundWindowService.Window> findResult(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      RotationData rotationData) {
    ScaffoldRemapService.Input input =
        this.enabled6
            ? this.createMap4(
                scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, this.entries4)
            : this.entries4;
    ScaffoldRemapService.Result result =
        this.createMap3(
            scaffoldPlayerSnapshot.playerRotation().yaw(), rotationData.yaw(), input, false);
    if (result.serverInput().jump()) {
      return Optional.empty();
    } else {
      double doubleValue =
          scaffoldCompatibility.groundInputAccelerationEstimate(
              minecraft,
              result.serverInput().forwardAxis(),
              result.serverInput().leftAxis(),
              !this.enabled6 && result.serverInput().sprint());
      double currentDoubleValue = scaffoldCompatibility.groundFriction(minecraft);
      Optional currentResult =
          ScaffoldGroundWindowService.groundWindow(
              scaffoldPlayerSnapshot, result.serverWorldVector(), doubleValue, currentDoubleValue);
      if (!currentResult.isEmpty() && (Boolean) this.moduleBool25.get()) {
        ScaffoldGroundWindowService.Window window =
            (ScaffoldGroundWindowService.Window) currentResult.get();
        RotationVector rotationVector =
            window.previousEye().subtract(scaffoldPlayerSnapshot.eyePosition());
        double nextDoubleValue = (float) currentDoubleValue * 0.91F;
        RotationVector currentX =
            new RotationVector(
                rotationVector.x() * nextDoubleValue,
                scaffoldPlayerSnapshot.velocity().y(),
                rotationVector.z() * nextDoubleValue);
        ScaffoldPlayerSnapshot currentScaffoldPlayerSnapshot =
            new ScaffoldPlayerSnapshot(
                window.previousEye(),
                scaffoldPlayerSnapshot.feetPosition().add(rotationVector),
                currentX,
                scaffoldPlayerSnapshot.playerRotation(),
                scaffoldPlayerSnapshot.mouseSensitivity(),
                true);
        return !this.m2nmsie7t6(
                    scaffoldCompatibility, minecraft, currentScaffoldPlayerSnapshot, result)
                && !this.checkCondition20(
                    scaffoldCompatibility,
                    minecraft,
                    currentScaffoldPlayerSnapshot,
                    input,
                    rotationData)
            ? Optional.of(
                new ScaffoldGroundWindowService.Window(
                    window.previousEye(),
                    window.previousEye().add(new RotationVector(currentX.x(), 0.0, currentX.z()))))
            : currentResult;
      } else {
        return currentResult;
      }
    }
  }

  private boolean checkCondition2(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    ScaffoldDecisionTracker.TargetKey targetKey = createTargetKey(this.scaffoldData6);
    if (this.scaffoldData6 != null
        && this.scaffoldMotionPhaseData2 != null
        && this.decision != null
        && this.step != null
        && preparedRotationMatches(
            this.timestamp4,
            targetKey,
            this.targetKey4,
            this.timestamp17,
            this.decision.motorPlanId(),
            this.count10)
        && checkCondition22(scaffoldPlayerSnapshot, this.scaffoldData6)
        && this.checkCondition24(scaffoldCompatibility, minecraft, this.scaffoldData6)) {
      Optional<RotationObserveService.Snapshot> result = RotationObserveService.snapshot();
      if (result.isEmpty()) {
        return false;
      }

      RotationObserveService.Snapshot currentSnapshot =
          (RotationObserveService.Snapshot) result.get();
      RotationData currentRotationData = currentSnapshot.rotation();
      Optional<PlacementCandidate> currentResult =
          scaffoldCompatibility.raycastPlacementFromEye(
              minecraft,
              this.scaffoldData6,
              currentRotationData,
              ((Float) this.moduleNumber30.get()).floatValue(),
              scaffoldPlayerSnapshot.eyePosition());
      int value =
          !this.moduleBool27.get()
                  || !this.enabled6
                      && scaffoldPlayerSnapshot.onGround()
                      && !(this.scaffoldMotionPhaseData2.deadlineTicks()
                          <= ((Float) this.moduleNumber49.get()).floatValue())
              ? 0
              : 1;
      double doubleValue =
          value != 0
              ? safeEmergencyFaceMargin(
                  ((Float) this.moduleNumber32.get()).floatValue(),
                  ((Float) this.moduleNumber50.get()).floatValue())
              : calculateValue6(this.step, ((Float) this.moduleNumber32.get()).floatValue());
      double currentDoubleValue = Math.max(0.04, doubleValue);
      double nextDoubleValue =
          value != 0
              ? safeEmergencyClickSpeed(
                  this.calculateValue7(this.scaffoldMotionPhaseData2),
                  ((Float) this.moduleNumber51.get()).floatValue())
              : this.calculateValue7(this.scaffoldMotionPhaseData2);
      int currentValue =
          this.placementAttempt2 != null
                  || this.timestamp4 < this.timestamp5 && (value == 0 || !this.moduleBool28.get())
              ? 0
              : 1;
      int nextValue = this.step.phase() != ScaffoldNextService.Phase.REACTION ? 1 : 0;
      int previousValue = scaffoldCompatibility.findPlaceableHotbarSlot(minecraft);
      int sourceValue = scaffoldCompatibility.selectedHotbarSlot(minecraft);
      boolean currentCanStartUseItem = scaffoldCompatibility.canStartUseItem(minecraft);
      ScaffoldCorridorService.Preflight preflight =
          ScaffoldCorridorService.evaluateBeforePhysics(
              this.scaffoldData6,
              currentRotationData,
              currentDoubleValue,
              currentSnapshot.wireEyePosition(),
              scaffoldPlayerSnapshot.eyePosition(),
              (item, currentItem) ->
                  scaffoldCompatibility.raycastBlockFromEye(
                      minecraft,
                      currentItem,
                      ((Float) this.moduleNumber30.get()).floatValue(),
                      item));
      Optional<PlacementCandidate> nextResult = currentResult;
      if (!ScaffoldUsableRayService.usableRay(nextResult, currentDoubleValue)
          && preflight.ready()
          && this.entries4 != null) {
        ScaffoldRemapService.Input input =
            this.enabled6
                ? this.createMap4(
                    scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, this.entries4)
                : this.entries4;
        ScaffoldRemapService.Result previousResult =
            this.createMap3(
                scaffoldPlayerSnapshot.playerRotation().yaw(),
                currentRotationData.yaw(),
                input,
                false);
        double previousDoubleValue =
            scaffoldCompatibility.groundInputAccelerationEstimate(
                minecraft,
                previousResult.serverInput().forwardAxis(),
                previousResult.serverInput().leftAxis(),
                !this.enabled6 && previousResult.serverInput().sprint());
        nextResult =
            nextGroundArmingEye(scaffoldPlayerSnapshot, previousResult, previousDoubleValue)
                .flatMap(
                    item ->
                        scaffoldCompatibility.raycastPlacementFromEye(
                            minecraft,
                            this.scaffoldData6,
                            currentRotationData,
                            ((Float) this.moduleNumber30.get()).floatValue(),
                            item));
      }

      ScaffoldDecisionTracker.RaySample raySample =
          preflight.ready() && ScaffoldUsableRayService.usableRay(nextResult, currentDoubleValue)
              ? new ScaffoldDecisionTracker.RaySample(
                  targetKey,
                  Math.min(
                      preflight.narrowestMargin(),
                      ((PlacementCandidate) nextResult.orElseThrow()).faceEdgeMargin()),
                  this.timestamp4)
              : null;
      int targetValue =
          ScaffoldUsableRayService.usableRay(currentResult, currentDoubleValue) && preflight.ready()
              ? 1
              : 0;
      this.targetKey2 = targetKey;
      this.timestamp8 = this.timestamp4;
      this.enabled4 = (targetValue != 0);
      this.value10 = currentResult.map(PlacementCandidate::faceEdgeMargin).orElse(0.0);
      ScaffoldDecisionTracker.Decision currentDecision =
          this.scaffoldDecisionTracker
              .evaluatePlaceWindow(
                  new ScaffoldDecisionTracker.PlaceWindow(
                      this.timestamp4,
                      this.decision.motorPlanId(),
                      targetKey,
                      raySample,
                      true,
                      sourceValue,
                      previousValue,
                      (nextValue != 0),
                      this.step.angularSpeed(),
                      nextDoubleValue,
                      currentValue != 0 && currentCanStartUseItem,
                      currentDoubleValue,
                      Math.round((Float) this.moduleNumber40.get()),
                      value != 0 || this.scaffoldMotionPhaseData2.urgency() >= 0.65))
              .orElse(null);
      if (currentDecision == null) {
        return false;
      }

      int inputValue =
          ScaffoldUsableRayService.placementReady(
                      true,
                      nextResult,
                      currentDoubleValue,
                      (nextValue != 0),
                      this.step.angularSpeed(),
                      nextDoubleValue,
                      sourceValue,
                      previousValue,
                      (currentValue != 0),
                      currentCanStartUseItem)
                  && preflight.ready()
              ? 1
              : 0;
      if (inputValue != 0 && !nextResult.isEmpty()) {
        PlacementCandidate placementCandidate = (PlacementCandidate) nextResult.get();
        ScaffoldValidateBeforeMovementValidator.Arm arm =
            new ScaffoldValidateBeforeMovementValidator.Arm(
                this.timestamp4,
                currentSnapshot.ordinal(),
                targetKey,
                currentRotationData,
                currentSnapshot.wireEyePosition(),
                currentDecision.attemptToken());
        this.preparedPlacementUse =
            new ImplSuspendForPearlService.PreparedPlacementUse(
                arm,
                placementCandidate,
                scaffoldPlayerSnapshot.eyePosition(),
                currentDoubleValue,
                this.enabled9 && this.checkCondition23(placementCandidate));
        this.rotationData = postUseServerRotation(true, currentRotationData, this.rotationData);
        return true;
      }

      this.scaffoldDecisionTracker.cancelPreparedWindow(
          currentDecision.attemptToken(), this.timestamp4);
      if (ScaffoldUsableRayService.usableRay(currentResult, currentDoubleValue)
          && !preflight.ready()) {
        this.updateState9(
            "pre-" + preflight.firstFailure(),
            true,
            "blocked before physics: target="
                + targetKey
                + " ordinal="
                + currentSnapshot.ordinal()
                + " margin="
                + preflight.narrowestMargin());
      }

      return false;
    } else {
      return false;
    }
  }

  static Optional<RotationVector> nextGroundArmingEye(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Result result,
      double doubleValue) {
    if (scaffoldPlayerSnapshot.onGround()
        && !result.serverInput().jump()
        && Double.isFinite(doubleValue)
        && !(doubleValue < 0.0)) {
      ScaffoldRemapService.WorldVector worldVector = result.serverWorldVector();
      double currentLength = worldVector.length();
      double currentDoubleValue = currentLength < 1.0E-9 ? 0.0 : doubleValue / currentLength;
      return Optional.of(
          scaffoldPlayerSnapshot
              .eyePosition()
              .add(
                  new RotationVector(
                      scaffoldPlayerSnapshot.velocity().x() + worldVector.x() * currentDoubleValue,
                      0.0,
                      scaffoldPlayerSnapshot.velocity().z()
                          + worldVector.z() * currentDoubleValue)));
    } else {
      return Optional.empty();
    }
  }

  private boolean checkCondition3(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    ImplSuspendForPearlService.PreparedPlacementUse currentPreparedPlacementUse =
        this.preparedPlacementUse;
    if (currentPreparedPlacementUse == null) {
      return false;
    }

    this.preparedPlacementUse = null;
    ScaffoldValidateBeforeMovementValidator.Arm currentArm = currentPreparedPlacementUse.arm();
    ScaffoldDecisionTracker.TargetKey targetKey = createTargetKey(this.scaffoldData6);
    Optional<RotationObserveService.Snapshot> result = RotationObserveService.snapshot();
    long longValue = result.map(RotationObserveService.Snapshot::ordinal).orElse(-1L);
    RotationData currentRotationData =
        result.map(RotationObserveService.Snapshot::rotation).orElse(null);
    RotationData nextRotationData =
        ScaffoldValidateBeforeMovementValidator.sameSentRotation(
                this.rotationData, currentRotationData)
            ? currentRotationData
            : null;
    ScaffoldCorridorService.Evaluation evaluation =
        ScaffoldCorridorService.evaluate(
            currentPreparedPlacementUse.placement(),
            currentArm.wireRotation(),
            currentPreparedPlacementUse.requiredMargin(),
            ScaffoldCorridorService.corridor(
                currentArm.previousWireEye(),
                currentPreparedPlacementUse.clientPreUseEye(),
                scaffoldPlayerSnapshot.eyePosition(),
                2),
            (item, currentItem) ->
                scaffoldCompatibility.raycastBlockFromEye(
                    minecraft,
                    currentItem,
                    ((Float) this.moduleNumber30.get()).floatValue(),
                    item));
    ScaffoldValidateBeforeMovementValidator.PrepareCheck prepareCheck =
        ScaffoldValidateBeforeMovementValidator.validateBeforeMovement(
            currentArm, this.timestamp4, longValue, targetKey, nextRotationData, evaluation);
    Optional currentResult =
        scaffoldCompatibility.raycastPlacementFromEye(
            minecraft,
            currentPreparedPlacementUse.placement(),
            currentArm.wireRotation(),
            ((Float) this.moduleNumber30.get()).floatValue(),
            scaffoldPlayerSnapshot.eyePosition());
    int value =
        prepareCheck.ready()
                && this.scaffoldData6 != null
                && checkCondition22(scaffoldPlayerSnapshot, this.scaffoldData6)
                && this.checkCondition24(scaffoldCompatibility, minecraft, this.scaffoldData6)
                && ScaffoldUsableRayService.usableRay(
                    currentResult, currentPreparedPlacementUse.requiredMargin())
                && scaffoldCompatibility.selectedHotbarSlot(minecraft)
                    == scaffoldCompatibility.findPlaceableHotbarSlot(minecraft)
                && scaffoldCompatibility.canStartUseItem(minecraft)
            ? 1
            : 0;
    if (value != 0 && !currentResult.isEmpty()) {
      PlacementCandidate placementCandidate = (PlacementCandidate) currentResult.get();
      this.scaffoldData64 = placementCandidate;
      this.count9 = -1;
      RuntimeException currentException = null;

      boolean placementSucceeded;
      try {
        placementSucceeded = scaffoldCompatibility.place(minecraft, placementCandidate);
      } catch (RuntimeException nextException) {
        placementSucceeded = false;
        currentException = nextException;
      } finally {
        this.scaffoldData64 = null;
      }

      int currentValue = this.count9;
      this.count9 = -1;
      ScaffoldValidateBeforeMovementValidator.UseDisposition useDisposition =
          ScaffoldValidateBeforeMovementValidator.classifyUse(placementSucceeded, currentValue);
      int nextValue =
          useDisposition == ScaffoldValidateBeforeMovementValidator.UseDisposition.ISSUED ? 1 : 0;
      int previousValue =
          useDisposition
                  == ScaffoldValidateBeforeMovementValidator.UseDisposition.AMBIGUOUS_NATIVE_RESULT
              ? 1
              : 0;
      this.scaffoldDecisionTracker.placementAttempted(
          currentArm.attemptToken(), nextValue != 0 || previousValue != 0, this.timestamp4);
      if (nextValue == 0) {
        this.enabled5 = false;
        this.enabled8 = true;
        if (previousValue != 0) {
          this.enabled10 = true;
          this.issuedPlacementTrace =
              new ImplSuspendForPearlService.IssuedPlacementTrace(
                  currentArm, -1, evaluation.narrowestMargin());
          this.updateState9(
              "ambiguous-native-use",
              true,
              "terminal hold: target="
                  + currentArm.target()
                  + " consumesAction=true sequence=missing");
        } else if (currentException != null) {
          this.updateState9(
              "native-use-exception",
              true,
              "use not issued: target="
                  + currentArm.target()
                  + " exception="
                  + currentException.getClass().getSimpleName());
        }

        return (previousValue != 0);
      } else {
        if (currentException != null) {
          this.updateState9(
              "issued-with-native-exception",
              true,
              "packet observed despite native exception: target="
                  + currentArm.target()
                  + " sequence="
                  + currentValue);
        }

        this.rotationData =
            postUseServerRotation(true, currentArm.wireRotation(), this.rotationData);
        this.scaffoldNextService.reset();
        this.timestamp5 = this.timestamp4 + Math.round((Float) this.moduleNumber41.get());
        this.timestamp10 = 0L;
        this.timestamp11 = 0L;
        this.timestamp12 = 0L;
        this.timestamp13 = Long.MIN_VALUE;
        this.timestamp14 = 0L;
        this.timestamp15 = 0L;
        this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
        this.placementAttempt =
            new ImplSuspendForPearlService.PlacementAttempt(
                currentArm.attemptToken(),
                placementCandidate,
                currentValue,
                currentPreparedPlacementUse.towerPlacement());
        this.state2 =
            ScaffoldBeginService.begin(
                currentArm.attemptToken(),
                currentValue,
                createTargetKey(placementCandidate),
                this.timestamp4,
                Math.round((Float) this.moduleNumber42.get()),
                Math.round((Float) this.moduleNumber43.get()));
        this.count7 = 0;
        this.targetKey2 = currentArm.target();
        this.timestamp8 = this.timestamp4;
        this.enabled4 = true;
        this.value10 = placementCandidate.faceEdgeMargin();
        this.enabled5 = this.checkCondition8(currentArm.target());
        this.issuedPlacementTrace =
            new ImplSuspendForPearlService.IssuedPlacementTrace(
                currentArm, currentValue, evaluation.narrowestMargin());
        this.updateState9(
            "issued",
            false,
            "UseItemOn->Move armed: token="
                + currentArm.attemptToken()
                + " sequence="
                + currentValue
                + " ordinal="
                + currentArm.precedingMovementOrdinal()
                + " margin="
                + evaluation.narrowestMargin());
        return true;
      }
    } else {
      this.scaffoldDecisionTracker.cancelPreparedWindow(currentArm.attemptToken(), this.timestamp4);
      this.enabled5 = false;
      boolean enabled = unissuedPreparationRequiresBrake(prepareCheck.failure());
      this.enabled8 |= enabled;
      String text = prepareCheck.ready() ? "post-runtime-gate" : prepareCheck.failure().name();
      this.updateState9(
          text,
          enabled,
          "use withheld before movement: target="
              + currentArm.target()
              + " ordinal="
              + longValue
              + " ray="
              + evaluation.firstFailure());
      return false;
    }
  }

  private void updateState7(String text, boolean enabled) {
    ImplSuspendForPearlService.PreparedPlacementUse currentPreparedPlacementUse =
        this.preparedPlacementUse;
    if (currentPreparedPlacementUse != null) {
      this.preparedPlacementUse = null;
      if (this.scaffoldDecisionTracker != null) {
        this.scaffoldDecisionTracker.cancelPreparedWindow(
            currentPreparedPlacementUse.arm().attemptToken(),
            currentPreparedPlacementUse.arm().tick());
      }

      this.enabled5 = false;
      this.enabled8 |= enabled;
      this.updateState9(
          text,
          enabled,
          "armed use cancelled before wire: target="
              + currentPreparedPlacementUse.arm().target()
              + " ordinal="
              + currentPreparedPlacementUse.arm().precedingMovementOrdinal());
    }
  }

  static boolean unissuedPreparationRequiresBrake(
      ScaffoldValidateBeforeMovementValidator.Failure failure) {
    return failure != ScaffoldValidateBeforeMovementValidator.Failure.NONE
        && failure != ScaffoldValidateBeforeMovementValidator.Failure.UNSAFE_RAY_ENVELOPE;
  }

  private void updateState8() {
    ImplSuspendForPearlService.IssuedPlacementTrace currentIssuedPlacementTrace =
        this.issuedPlacementTrace;
    if (currentIssuedPlacementTrace != null) {
      Optional result = RotationObserveService.snapshot();
      if (!result.isEmpty()) {
        RotationObserveService.Snapshot currentSnapshot =
            (RotationObserveService.Snapshot) result.get();
        long offset = currentIssuedPlacementTrace.arm().precedingMovementOrdinal();
        if (currentSnapshot.ordinal() != offset) {
          this.issuedPlacementTrace = null;
          if (ScaffoldValidateBeforeMovementValidator.followingMovementMatches(
              currentIssuedPlacementTrace.arm(),
              currentSnapshot.ordinal(),
              currentSnapshot.rotation())) {
            this.updateState9(
                "movement-confirmed",
                false,
                "following Move confirmed: token="
                    + currentIssuedPlacementTrace.arm().attemptToken()
                    + " sequence="
                    + currentIssuedPlacementTrace.sequence()
                    + " ordinal="
                    + currentSnapshot.ordinal()
                    + " margin="
                    + currentIssuedPlacementTrace.narrowestMargin());
          } else {
            this.enabled10 = true;
            this.enabled8 = true;
            this.updateState9(
                "following-movement-mismatch",
                true,
                "terminal hold: token="
                    + currentIssuedPlacementTrace.arm().attemptToken()
                    + " expectedOrdinal="
                    + (offset + 1L)
                    + " actualOrdinal="
                    + currentSnapshot.ordinal()
                    + " expectedRotation="
                    + currentIssuedPlacementTrace.arm().wireRotation()
                    + " actualRotation="
                    + currentSnapshot.rotation());
          }
        }
      }
    }
  }

  private void updateState9(String text, boolean enabled, String currentText) {
    if ((Boolean) this.moduleBool29.get()) {
      int value =
          this.timestamp16 != Long.MIN_VALUE && this.timestamp4 - this.timestamp16 < 20L ? 1 : 0;
      if ((enabled || value == 0) && (!enabled || value == 0 || !text.equals(this.text2))) {
        this.text2 = text;
        this.timestamp16 = this.timestamp4;
        if (enabled) {
          CoreIsInitializedHandler.LOGGER.warn(
              "[ScaffoldWire] tick={} kind={} {}",
              new Object[] {this.timestamp4, text, currentText});
        } else {
          CoreIsInitializedHandler.LOGGER.info(
              "[ScaffoldWire] tick={} kind={} {}",
              new Object[] {this.timestamp4, text, currentText});
        }
      }
    }
  }

  static boolean shouldAdvanceMotorAfterUse(boolean enabled) {
    return !enabled;
  }

  static boolean wireLifecycleMustHold(boolean enabled, boolean currentEnabled) {
    return enabled || currentEnabled;
  }

  static RotationData postUseServerRotation(
      boolean enabled, RotationData rotationData, RotationData currentRotationData) {
    if (!enabled) {
      return currentRotationData;
    } else if (rotationData == null) {
      throw new IllegalArgumentException("An issued use requires an actually-sent rotation");
    } else {
      return new RotationData((float) rotationData.yaw(), (float) rotationData.pitch());
    }
  }

  static boolean mayAdvancePlacementMotor(
      boolean enabled, boolean currentEnabled, boolean nextEnabled) {
    return enabled || !currentEnabled && nextEnabled;
  }

  static boolean preparedRotationMatches(
      long longValue,
      ScaffoldDecisionTracker.TargetKey targetKey,
      ScaffoldDecisionTracker.TargetKey currentTargetKey,
      long currentLongValue,
      int value,
      int currentValue) {
    return targetKey != null
        && targetKey.equals(currentTargetKey)
        && currentLongValue == longValue - 1L
        && value >= 0
        && value == currentValue;
  }

  private void updateState10() {
    if (!PearlThrowController.reserved()) {
      if (this.enabled2) {
        this.createScaffoldOperationHandler().reconcile(CoreIsInitializedHandler.mc());
      } else if (this.rotationData != null && this.entries4 != null) {
        Minecraft minecraft = CoreIsInitializedHandler.mc();
        if (LocalPhysicsFrameBus.wasAborted(this.timestamp4)) {
          this.updateState7("physics-aborted", true);
          this.step = null;
          this.entries4 = null;
        } else {
          Optional result = CompatAdapterService.rotationEnvironment();
          Optional currentResult = CompatAdapterService.scaffoldEnvironment();
          if (!result.isEmpty()
              && !currentResult.isEmpty()
              && minecraft.player != null
              && minecraft.level != null
              && minecraft.screen == null) {
            ScaffoldCompatibility scaffoldCompatibility =
                (ScaffoldCompatibility) currentResult.get();
            Optional nextResult = scaffoldCompatibility.capture(minecraft);
            if (nextResult.isEmpty()) {
              this.updateState7("post-physics-frame-missing", true);
            } else {
              ScaffoldPlayerSnapshot scaffoldPlayerSnapshot =
                  (ScaffoldPlayerSnapshot) nextResult.get();
              this.checkCondition3(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot);
              if (this.enabled10) {
                this.updateState34(
                    minecraft, (CombatCompatibility) result.get(), scaffoldPlayerSnapshot);
              } else if (this.scaffoldData6 != null
                  && !checkCondition22(scaffoldPlayerSnapshot, this.scaffoldData6)) {
                if (this.enabled9 && this.checkCondition23(this.scaffoldData6)) {
                  this.updateState18(minecraft);
                } else {
                  this.enabled8 = true;
                  this.scaffoldData72 = this.scaffoldData6.supportPosition();
                  this.updateState30();
                  this.updateState18(minecraft);
                }
              } else {
                if (this.scaffoldData6 != null
                    && this.scaffoldMotionPhaseData2 != null
                    && this.checkCondition24(
                        scaffoldCompatibility, minecraft, this.scaffoldData6)) {
                  this.scaffoldMotionPhaseData2 =
                      this.createScaffoldMotionPhaseData(
                          scaffoldPlayerSnapshot,
                          this.scaffoldData6,
                          this.scaffoldData63,
                          this.value9,
                          this.checkCondition25());
                }

                this.updateState18(minecraft);
              }
            }
          } else {
            this.updateState7("environment-lost", true);
          }
        }
      } else {
        this.updateState7("missing-movement-contract", false);
      }
    }
  }

  private void updateState11() {
    if (!PearlThrowController.reserved()) {
      if (!this.enabled2) {
        this.updateState8();
        if (this.scaffoldData6 != null
            && this.scaffoldMotionPhaseData2 != null
            && this.rotationData != null
            && this.step != null
            && createTargetKey(this.scaffoldData6).equals(this.targetKey4)
            && this.timestamp17 == this.timestamp4
            && !LocalPhysicsFrameBus.wasAborted(this.timestamp4)) {
          Minecraft minecraft = CoreIsInitializedHandler.mc();
          Optional result = CompatAdapterService.rotationEnvironment();
          Optional currentResult = CompatAdapterService.scaffoldEnvironment();
          if (!result.isEmpty()
              && !currentResult.isEmpty()
              && minecraft.player != null
              && minecraft.level != null
              && minecraft.screen == null) {
            ScaffoldCompatibility scaffoldCompatibility =
                (ScaffoldCompatibility) currentResult.get();
            Optional nextResult = scaffoldCompatibility.capture(minecraft);
            int value =
                nextResult.isPresent()
                        && checkCondition22(
                            (ScaffoldPlayerSnapshot) nextResult.get(), this.scaffoldData6)
                        && this.checkCondition24(
                            scaffoldCompatibility, minecraft, this.scaffoldData6)
                    ? 1
                    : 0;
            RotationData currentRotationData =
                RotationObserveService.current().orElse(this.rotationData);
            Optional<PlacementCandidate> previousResult =
                value != 0
                    ? scaffoldCompatibility.raycastPlacement(
                        minecraft,
                        this.scaffoldData6,
                        currentRotationData,
                        ((Float) this.moduleNumber30.get()).floatValue())
                    : Optional.empty();
            ScaffoldDecisionTracker.TargetKey targetKey = createTargetKey(this.scaffoldData6);
            int currentValue =
                !this.moduleBool27.get()
                        || !this.enabled6
                            && minecraft.player.onGround()
                            && !(this.scaffoldMotionPhaseData2.deadlineTicks()
                                <= ((Float) this.moduleNumber49.get()).floatValue())
                    ? 0
                    : 1;
            double doubleValue =
                currentValue != 0
                    ? safeEmergencyFaceMargin(
                        ((Float) this.moduleNumber32.get()).floatValue(),
                        ((Float) this.moduleNumber50.get()).floatValue())
                    : calculateValue6(this.step, ((Float) this.moduleNumber32.get()).floatValue());
            boolean enabled = ScaffoldUsableRayService.usableRay(previousResult, doubleValue);
            this.targetKey2 = targetKey;
            this.timestamp8 = this.timestamp4;
            this.enabled4 = enabled;
            this.value10 = previousResult.map(PlacementCandidate::faceEdgeMargin).orElse(0.0);
            if (enabled) {
              this.count7 = 0;
            } else if (value != 0
                && rayMissCountsTowardReplan(
                    (ScaffoldPlayerSnapshot) nextResult.orElseThrow(),
                    this.scaffoldData6,
                    this.step.phase(),
                    this.enabled3)) {
              this.count7++;
            }

            if (this.count7 >= Math.round((Float) this.moduleNumber55.get())) {
              this.updateState30();
            }
          }
        }
      }
    }
  }

  private long calculateValue() {
    if (this.placementAttempt == null) {
      return 0L;
    }

    if (this.scaffoldDecisionTracker != null
        && this.placementAttempt.token() == this.scaffoldDecisionTracker.pendingAttemptToken()) {
      long longValue = this.placementAttempt.token();
      if (!this.checkCondition16(longValue)) {
        return 0L;
      }

      if (this.checkCondition18(longValue)) {
        ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt = this.placementAttempt;
        ScaffoldDecisionTracker.TargetKey targetKey =
            createTargetKey(currentPlacementAttempt.placement());
        this.placementAttempt = null;
        this.updateState23(longValue);
        this.updateState29();
        this.scaffoldDecisionTracker.retireAttempt(
            longValue, targetKey, ScaffoldDecisionTracker.AttemptOutcome.REJECTED, this.timestamp4);
        if (this.scaffoldData6 != null && createTargetKey(this.scaffoldData6).equals(targetKey)) {
          this.updateState30();
        }

        return 0L;
      } else {
        if (!this.checkCondition17(longValue)) {
          return 0L;
        }

        PlacementCandidate placementCandidate = this.placementAttempt.placement();
        this.scaffoldDecisionTracker.retireAttempt(
            longValue,
            createTargetKey(placementCandidate),
            ScaffoldDecisionTracker.AttemptOutcome.ACCEPTED,
            this.timestamp4);
        this.updateState23(longValue);
        this.updateState29();
        this.scaffoldData63 = placementCandidate;
        this.count3++;
        this.placementAttempt = null;
        return longValue;
      }
    } else {
      long currentLongValue = this.placementAttempt.token();
      if (this.checkCondition16(currentLongValue)) {
        if (this.checkCondition17(currentLongValue)) {
          this.scaffoldData63 = this.placementAttempt.placement();
          this.count3++;
        }

        this.placementAttempt = null;
        this.updateState23(currentLongValue);
        this.updateState29();
      } else {
        this.updateState24();
      }

      return 0L;
    }
  }

  private void updateState12(EventAttackInputService.Packet currentPacket) {
    ScaffoldCompatibility scaffoldCompatibility =
        CompatAdapterService.scaffoldEnvironment().orElse(null);
    if (scaffoldCompatibility != null) {
      Packet nextPacket = currentPacket.packet();
      ClientLevel clientLevel = CoreIsInitializedHandler.mc().level;
      CoreIsInitializedHandler.mc()
          .execute(
              () -> {
                if (CoreIsInitializedHandler.mc().level == clientLevel) {
                  this.updateState14(scaffoldCompatibility, nextPacket);
                }
              });
    }
  }

  private void updateState13(
      EventAttackInputService.OutgoingPacketAccepted outgoingPacketAccepted) {
    if (this.enabled2) {
      CompatAdapterService.scaffoldEnvironment()
          .ifPresent(
              item ->
                  this.createScaffoldOperationHandler()
                      .observeOutgoing(item, outgoingPacketAccepted.packet()));
    } else {
      PlacementCandidate placementCandidate = this.scaffoldData64;
      if (placementCandidate != null) {
        ScaffoldCompatibility scaffoldCompatibility =
            CompatAdapterService.scaffoldEnvironment().orElse(null);
        if (scaffoldCompatibility != null) {
          int value =
              scaffoldCompatibility.placementSequence(
                  outgoingPacketAccepted.packet(), placementCandidate);
          if (value >= 0) {
            this.count9 = value;
          }
        }
      }
    }
  }

  private void updateState14(ScaffoldCompatibility scaffoldCompatibility, Object value) {
    if (this.enabled2) {
      this.createScaffoldOperationHandler().observeServerPacket(scaffoldCompatibility, value);
    } else {
      ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt =
          this.placementAttempt != null ? this.placementAttempt : this.placementAttempt2;
      if (currentPlacementAttempt != null) {
        ScaffoldCompatibility.PlacementFeedback currentPlacementFeedback =
            scaffoldCompatibility.placementFeedback(
                value, currentPlacementAttempt.placement().targetPosition());
        int currentValue = scaffoldCompatibility.acknowledgedPlacementSequence(value);
        if (currentPlacementFeedback != ScaffoldCompatibility.PlacementFeedback.NONE) {
          this.placementFeedback2 = currentPlacementFeedback;
          this.updateState21(currentPlacementAttempt, currentPlacementFeedback);
        }

        if (currentValue >= 0) {
          if (currentValue < currentPlacementAttempt.sequence()) {
            this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
          } else {
            ImplSuspendForPearlService.PlacementAttempt nextPlacementAttempt =
                this.placementAttempt != null ? this.placementAttempt : this.placementAttempt2;
            if (nextPlacementAttempt != null
                && nextPlacementAttempt.token() == currentPlacementAttempt.token()
                && nextPlacementAttempt.sequence() == currentPlacementAttempt.sequence()) {
              ScaffoldCompatibility.PlacementFeedback nextPlacementFeedback =
                  this.placementFeedback2;
              this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
              if (nextPlacementFeedback == ScaffoldCompatibility.PlacementFeedback.NONE) {
                this.timestamp12 = currentPlacementAttempt.token();
                this.timestamp13 = this.timestamp4;
              } else if (this.placementAttempt != null) {
                this.timestamp12 = currentPlacementAttempt.token();
                this.timestamp13 = this.timestamp4;
                if (nextPlacementFeedback == ScaffoldCompatibility.PlacementFeedback.ACCEPTED) {
                  this.timestamp10 = currentPlacementAttempt.token();
                } else {
                  this.timestamp11 = currentPlacementAttempt.token();
                }
              } else {
                this.updateState26(
                    currentPlacementAttempt,
                    nextPlacementFeedback == ScaffoldCompatibility.PlacementFeedback.ACCEPTED);
              }
            }
          }
        }
      }
    }
  }

  private void updateState15(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      long offset) {
    if (this.scaffoldData6 != null) {
      int value = offset != 0L ? 1 : 0;
      int currentValue =
          this.placementAttempt != null
                  && createTargetKey(this.placementAttempt.placement())
                      .equals(createTargetKey(this.scaffoldData6))
                  && this.scaffoldDecisionTracker.pendingAttemptToken()
                      == this.placementAttempt.token()
              ? 1
              : 0;
      int nextValue =
          currentValue != 0
              ? (!scaffoldCompatibility.isFaceSturdy(
                      minecraft,
                      this.scaffoldData6.supportPosition(),
                      this.scaffoldData6.clickedFace())
                  ? 1
                  : 0)
              : (!this.checkCondition24(scaffoldCompatibility, minecraft, this.scaffoldData6)
                  ? 1
                  : 0);
      int currentLength =
          nextValue == 0
                  && (checkCondition22(scaffoldPlayerSnapshot, this.scaffoldData6)
                      || this.enabled9 && this.checkCondition23(this.scaffoldData6))
                  && !(this.scaffoldData6
                          .hitPoint()
                          .subtract(scaffoldPlayerSnapshot.eyePosition())
                          .length()
                      > ((Float) this.moduleNumber30.get()).floatValue())
              ? 0
              : 1;
      if (currentValue == 0
          && !this.checkCondition23(this.scaffoldData6)
          && this.snapshot2 != null
          && this.snapshot2.allowsStructuralPlanning()
          && !ScaffoldPlanService.canReachFace(
              scaffoldPlayerSnapshot,
              this.scaffoldData6,
              this.snapshot2.committedDirection(),
              scaffoldCompatibility.groundFriction(minecraft))) {
        currentLength = 1;
        this.updateState9(
            "unreachable-face",
            false,
            "replanning hidden face: target=" + createTargetKey(this.scaffoldData6));
      }

      if (value != 0 || currentLength != 0) {
        if (currentLength != 0 && value == 0) {
          ScaffoldDecisionTracker.TargetKey currentTargetKey = createTargetKey(this.scaffoldData6);
          if (!scaffoldPlayerSnapshot.onGround()) {
            this.scaffoldData72 = this.scaffoldData6.supportPosition();
          }

          if (this.placementAttempt != null
              && createTargetKey(this.placementAttempt.placement()).equals(currentTargetKey)) {
            this.updateState24();
          } else {
            this.scaffoldDecisionTracker.abortTarget(currentTargetKey, this.timestamp4);
          }

          this.scaffoldData62 = null;
          this.scaffoldMotionPhaseData = null;
          this.count4 = -1;
          this.targetKey = null;
          this.timestamp6 = Long.MIN_VALUE;
        }

        this.scaffoldData6 = null;
      }
    }
  }

  private Optional<PlacementCandidate> findResult2(
      final ScaffoldCompatibility scaffoldCompatibility,
      final Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    if (this.enabled9) {
      Optional<PlacementCandidate> result =
          this.scaffoldShouldActivateService
              .plan(
                  scaffoldPlayerSnapshot,
                  new ScaffoldShouldActivateService.Query() {
                    @Override
                    public boolean isReplaceable(BlockCoordinates blockCoordinates) {
                      return !ImplSuspendForPearlService.this.checkCondition14(blockCoordinates)
                          && scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates);
                    }

                    @Override
                    public boolean isFaceSturdy(
                        BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
                      return !ImplSuspendForPearlService.this.checkCondition14(blockCoordinates)
                          && scaffoldCompatibility.isFaceSturdy(
                              minecraft, blockCoordinates, scaffoldMode);
                    }
                  },
                  ((Float) this.moduleNumber30.get()).floatValue(),
                  ((Float) this.moduleNumber16.get()).floatValue(),
                  3)
              .filter(
                  item ->
                      (this.targetKey3 == null
                              || this.timestamp4 >= this.timestamp9
                              || !createTargetKey(item).equals(this.targetKey3))
                          && !this.checkCondition14(item.targetPosition()));
      this.scaffoldData7 = result.map(PlacementCandidate::targetPosition).orElse(null);
      return result;
    }

    this.scaffoldData7 = null;
    if (this.snapshot2 != null && this.snapshot2.allowsStructuralPlanning()) {
      double doubleValue = ((Float) this.moduleNumber13.get()).floatValue();
      if (scaffoldPlayerSnapshot.onGround()
          && minecraft.options.keyJump.isDown()
          && this.snapshot2.hasRequestedDirection()) {
        int value =
            !minecraft.options.keySprint.isDown()
                    && !ImplRequestsSprintClient.requestsSprint(minecraft)
                    && (minecraft.player == null || !minecraft.player.isSprinting())
                ? 0
                : 1;
        ScaffoldRemapService.WorldVector worldVector = this.snapshot2.committedDirection();
        double currentLength = worldVector.length();
        double currentX =
            currentLength < 1.0E-9
                ? 0.0
                : (scaffoldPlayerSnapshot.velocity().x() * worldVector.x()
                        + scaffoldPlayerSnapshot.velocity().z() * worldVector.z())
                    / currentLength;
        doubleValue =
            Math.max(
                doubleValue,
                requiredPlanningLookAheadForJump(
                    scaffoldPlayerSnapshot.horizontalSpeed(), currentX, (value != 0)));
      }

      return this.scaffoldPlanService.plan(
          scaffoldPlayerSnapshot,
          new ScaffoldPlanService.Query() {
            @Override
            public double groundFriction() {
              return scaffoldCompatibility.groundFriction(minecraft);
            }

            @Override
            public boolean isReplaceable(BlockCoordinates blockCoordinates) {
              return ImplSuspendForPearlService.this.checkCondition14(blockCoordinates)
                  ? false
                  : scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates);
            }

            @Override
            public boolean isFaceSturdy(
                BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
              return ImplSuspendForPearlService.this.checkCondition14(blockCoordinates)
                  ? false
                  : scaffoldCompatibility.isFaceSturdy(minecraft, blockCoordinates, scaffoldMode);
            }
          },
          doubleValue,
          ((Float) this.moduleNumber30.get()).floatValue(),
          this.rotationData,
          this.count8,
          this.snapshot2.committedDirection(),
          item ->
              (this.targetKey3 == null
                      || this.timestamp4 >= this.timestamp9
                      || !createTargetKey(item).equals(this.targetKey3))
                  && !this.checkCondition14(item.targetPosition()));
    } else {
      return Optional.empty();
    }
  }

  private void updateState16(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    int value =
        this.scaffoldData6 != null && this.checkCondition8(createTargetKey(this.scaffoldData6))
            ? 1
            : 0;
    boolean enabled = this.checkCondition9(createTargetKey(this.scaffoldData6));
    if (shouldSuppressTowerLookahead(this.enabled9, enabled)) {
      this.scaffoldData62 = null;
      this.scaffoldMotionPhaseData = null;
      this.count4 = -1;
      this.targetKey = null;
      this.timestamp6 = Long.MIN_VALUE;
    } else if (value == 0) {
      this.scaffoldData62 = null;
      this.scaffoldMotionPhaseData = null;
      this.count4 = -1;
      this.targetKey = null;
      this.timestamp6 = Long.MIN_VALUE;
    } else {
      boolean currentEnabled =
          this.checkCondition4(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot);
      Optional<PlacementCandidate> result =
          this.findResult2(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot)
              .filter(item -> !createTargetKey(item).equals(createTargetKey(this.scaffoldData6)));
      ScaffoldDecisionTracker.TargetKey currentTargetKey = createTargetKey(this.scaffoldData62);
      ScaffoldDecisionTracker.TargetKey nextTargetKey =
          result.map(ImplSuspendForPearlService::createTargetKey).orElse(null);
      if (shouldRetainLookahead((value != 0), currentTargetKey, currentEnabled, nextTargetKey)) {
        this.targetKey = null;
        this.timestamp6 = Long.MIN_VALUE;
        this.scaffoldMotionPhaseData =
            this.createScaffoldMotionPhaseData(
                scaffoldPlayerSnapshot,
                this.scaffoldData62,
                this.scaffoldData6,
                this.value9,
                false);
      } else if (currentEnabled
          && currentTargetKey != null
          && nextTargetKey != null
          && !lookaheadReplacementConfirmed(
              this.targetKey, this.timestamp6, nextTargetKey, this.timestamp4)) {
        this.targetKey = nextTargetKey;
        this.timestamp6 = this.timestamp4;
        this.scaffoldMotionPhaseData = null;
        this.scaffoldNextService.reset();
      } else {
        ScaffoldDecisionTracker.TargetKey previousTargetKey = currentTargetKey;
        this.scaffoldData62 = null;
        this.scaffoldMotionPhaseData = null;
        this.count4 = -1;
        this.targetKey = null;
        this.timestamp6 = Long.MIN_VALUE;
        if (result.isEmpty()) {
          if (previousTargetKey != null) {
            this.scaffoldNextService.reset();
          }
        } else {
          PlacementCandidate placementCandidate = (PlacementCandidate) result.get();
          ScaffoldMotionPhaseData currentScaffoldMotionPhaseData =
              this.createScaffoldMotionPhaseData(
                  scaffoldPlayerSnapshot,
                  placementCandidate,
                  this.scaffoldData6,
                  this.value9,
                  false);
          if (changesStructuralMotorTarget(previousTargetKey, nextTargetKey)) {
            this.scaffoldNextService.reset();
          }

          this.count4 = this.scaffoldDecisionTracker.motorPlanId() + 1;
          this.scaffoldData62 =
              this.createScaffoldData6(
                  scaffoldCompatibility,
                  minecraft,
                  placementCandidate,
                  currentScaffoldMotionPhaseData);
          this.scaffoldMotionPhaseData =
              this.createScaffoldMotionPhaseData(
                  scaffoldPlayerSnapshot,
                  this.scaffoldData62,
                  this.scaffoldData6,
                  this.value9,
                  false);
        }
      }
    }
  }

  private boolean checkCondition4(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    return this.scaffoldData62 != null
        && checkCondition22(scaffoldPlayerSnapshot, this.scaffoldData62)
        && this.checkCondition24(scaffoldCompatibility, minecraft, this.scaffoldData62)
        && this.scaffoldData62.hitPoint().subtract(scaffoldPlayerSnapshot.eyePosition()).length()
            <= ((Float) this.moduleNumber30.get()).floatValue();
  }

  private boolean checkCondition5(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    ScaffoldDecisionTracker.TargetKey targetKey = createTargetKey(this.scaffoldData62);
    ScaffoldDecisionTracker.TargetKey currentTargetKey =
        this.findResult2(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot)
            .map(ImplSuspendForPearlService::createTargetKey)
            .orElse(null);
    return plannedLookaheadMatches(targetKey, currentTargetKey);
  }

  private PlacementCandidate createScaffoldData6(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      ScaffoldMotionPhaseData scaffoldMotionPhaseData) {
    Optional result =
        scaffoldCompatibility.raycastPlacement(
            minecraft,
            placementCandidate,
            this.rotationData,
            ((Float) this.moduleNumber30.get()).floatValue());
    if (result.isPresent() && ((PlacementCandidate) result.get()).faceEdgeMargin() >= 0.12) {
      return (PlacementCandidate) result.get();
    }

    double doubleValue = ((Float) this.moduleNumber31.get()).floatValue() / 100.0;
    double currentDoubleValue = doubleValue * 0.016;
    int value =
        placementCandidate.clickedFace() != ScaffoldMode.UP
                && placementCandidate.clickedFace() != ScaffoldMode.DOWN
            ? 1
            : 0;
    double nextDoubleValue = ((Float) this.moduleNumber28.get()).floatValue() / 100.0;
    double previousDoubleValue = 0.5 + scaffoldMotionPhaseData.firstAnchorBias();
    if (value != 0) {
      double currentX =
          switch (placementCandidate.clickedFace()) {
            case EAST, WEST ->
                scaffoldMotionPhaseData.predictedEyeAtClick().z()
                    - placementCandidate.supportPosition().z();
            case NORTH, SOUTH ->
                scaffoldMotionPhaseData.predictedEyeAtClick().x()
                    - placementCandidate.supportPosition().x();
            default -> previousDoubleValue;
          };
      previousDoubleValue += (currentX - previousDoubleValue) * nextDoubleValue;
    } else {
      previousDoubleValue = 0.5 + scaffoldMotionPhaseData.firstAnchorBias() * nextDoubleValue;
    }

    double sourceDoubleValue =
        value != 0
            ? ((Float) this.moduleNumber29.get()).floatValue() / 100.0
                + scaffoldMotionPhaseData.secondAnchorBias()
            : 0.5 + scaffoldMotionPhaseData.secondAnchorBias() * nextDoubleValue;
    double targetDoubleValue =
        calculateValue8(
            previousDoubleValue
                + this.value7 * doubleValue
                + this.random2.nextGaussian() * currentDoubleValue,
            0.18,
            0.82);
    double inputDoubleValue =
        calculateValue8(
            sourceDoubleValue
                + this.value8 * doubleValue
                + this.random2.nextGaussian() * currentDoubleValue,
            0.18,
            0.82);
    return placementCandidate.withSurfacePoint(targetDoubleValue, inputDoubleValue);
  }

  private Optional<PlacementCandidate> findResult3(
      ScaffoldCompatibility scaffoldCompatibility, Minecraft minecraft, RotationData rotationData) {
    return this.scaffoldData6 == null
        ? Optional.empty()
        : scaffoldCompatibility.raycastPlacement(
            minecraft,
            this.scaffoldData6,
            rotationData,
            ((Float) this.moduleNumber30.get()).floatValue());
  }

  private ScaffoldRemapService.Input createMap(ScaffoldRemapService.Input input, boolean enabled) {
    String text = (String) this.moduleMode5.get();
    if (enabled && !"Follow Input".equals(text)) {
      boolean currentEnabled = text.contains("Forward");
      boolean nextEnabled = text.contains("Backward");
      int value = !text.endsWith("Left") && !"Auto Left".equals(text) ? 0 : 1;
      int currentValue = !text.endsWith("Right") && !"Auto Right".equals(text) ? 0 : 1;
      return new ScaffoldRemapService.Input(
          currentEnabled,
          nextEnabled,
          (value != 0),
          (currentValue != 0),
          input.jump(),
          input.sneak(),
          input.sprint());
    } else {
      return input;
    }
  }

  private ScaffoldRemapService.Input createMap2(Minecraft minecraft) {
    return new ScaffoldRemapService.Input(
        minecraft.options.keyUp.isDown(),
        minecraft.options.keyDown.isDown(),
        minecraft.options.keyLeft.isDown(),
        minecraft.options.keyRight.isDown(),
        minecraft.options.keyJump.isDown(),
        minecraft.options.keyShift.isDown(),
        minecraft.options.keySprint.isDown() || ImplRequestsSprintClient.requestsSprint(minecraft));
  }

  private ScaffoldRemapService.Result createMap3(
      double doubleValue,
      double currentDoubleValue,
      ScaffoldRemapService.Input input,
      boolean enabled) {
    return this.moduleBool6.get()
        ? this.entries2.remap(
            doubleValue, currentDoubleValue, input, enabled, (Boolean) this.moduleBool7.get())
        : this.entries2.passthrough(
            doubleValue, currentDoubleValue, input, enabled, (Boolean) this.moduleBool7.get());
  }

  private ScaffoldMotionPhaseData createScaffoldMotionPhaseData(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      PlacementCandidate placementCandidate,
      PlacementCandidate currentPlacementCandidate,
      double doubleValue,
      boolean enabled) {
    ScaffoldObservationTracker.Snapshot snapshot = this.snapshot2;
    ScaffoldRemapService.WorldVector worldVector =
        snapshot == null ? null : snapshot.committedDirection();
    if (!this.enabled9 && worldVector != null && !(worldVector.length() < 1.0E-9)) {
      double currentLength = worldVector.length();
      double currentX = worldVector.x() / currentLength;
      double currentDoubleValue = worldVector.z() / currentLength;
      double nextDoubleValue =
          RotationData.wrapDegrees(Math.toDegrees(Math.atan2(currentDoubleValue, currentX)) - 90.0);
      double nextX =
          Math.max(
              0.0,
              scaffoldPlayerSnapshot.velocity().x() * currentX
                  + scaffoldPlayerSnapshot.velocity().z() * currentDoubleValue);
      return ScaffoldMotionPhaseData.analyze(
          scaffoldPlayerSnapshot,
          placementCandidate,
          currentPlacementCandidate,
          doubleValue,
          enabled,
          nextDoubleValue,
          nextX);
    } else {
      return ScaffoldMotionPhaseData.analyze(
          scaffoldPlayerSnapshot,
          placementCandidate,
          currentPlacementCandidate,
          doubleValue,
          enabled);
    }
  }

  private void updateState17(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Input input,
      boolean enabled) {
    ScaffoldRemapService.Result result =
        this.createMap3(
            scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), input, false);
    boolean currentEnabled =
        authoritativePlacementReady(
            this.checkCondition8(createTargetKey(this.scaffoldData6)),
            this.checkCondition17(this.scaffoldDecisionTracker.pendingAttemptToken()));
    this.enabled6 =
        !(!this.enabled7
            && !this.checkCondition12(
                scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, result, currentEnabled));
    boolean placementReady = this.enabled6;
    if (this.enabled6) {
      input = this.createMap4(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, input);
      result =
          this.createMap3(
              scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), input, false);
    }

    boolean nextEnabled = this.checkCondition6(scaffoldPlayerSnapshot, result, enabled);
    boolean currentShouldSynthesizeSneak =
        shouldSynthesizeSneak((String) this.moduleMode7.get(), this.enabled6, nextEnabled);
    ScaffoldRemapService.Result currentResult =
        this.createMap3(
            scaffoldPlayerSnapshot.playerRotation().yaw(),
            this.rotationData.yaw(),
            input,
            currentShouldSynthesizeSneak);
    if ((Boolean) this.moduleBool25.get()
        && scaffoldPlayerSnapshot.onGround()
        && this.checkCondition13(
            scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, currentResult)) {
      this.enabled6 = true;
      input = this.createMap4(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, input);
      this.entries4 = input;
      currentShouldSynthesizeSneak =
          shouldSynthesizeSneak((String) this.moduleMode7.get(), true, nextEnabled);
      currentResult =
          this.createMap3(
              scaffoldPlayerSnapshot.playerRotation().yaw(),
              this.rotationData.yaw(),
              input,
              currentShouldSynthesizeSneak);
      placementReady = true;
    }

    this.entries4 = input;
    this.updateState19(minecraft, currentResult);
    boolean previousEnabled = (Boolean) this.moduleBool6.get();
    ScaffoldRemapService.Input currentInput = previousEnabled ? currentResult.serverInput() : input;
    boolean sourceEnabled = ScaffoldPublishService.suppressLocalSprint();
    PlacementOverrideBus.Override override =
        new PlacementOverrideBus.Override(
                previousEnabled || placementReady || !"Follow Input".equals(this.moduleMode5.get()),
                currentInput,
                currentShouldSynthesizeSneak,
                sourceEnabled)
            .withSilentSneak(
                (Boolean) this.moduleBool12.get(), minecraft.options.keyShift.isDown());
    PlacementOverrideBus.publish(override);
    if (previousEnabled) {
      LocalPhysicsFrameBus.publish(
          new LocalPhysicsFrameBus.Frame(
              this.timestamp4,
              (float) this.rotationData.yaw(),
              override.localInput(),
              sourceEnabled));
    } else if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      LocalPhysicsFrameBus.clear();
    }
  }

  private void updateState18(Minecraft minecraft) {
    ScaffoldRemapService.Input currentInput =
        PlacementOverrideBus.applied()
            .orElseGet(
                () -> {
                  Input playerInput = minecraft.player.input.keyPresses;
                  return new ScaffoldRemapService.Input(
                      playerInput.forward(),
                      playerInput.backward(),
                      playerInput.left(),
                      playerInput.right(),
                      playerInput.jump(),
                      playerInput.shift(),
                      playerInput.sprint());
                });
    Optional result = LocalPhysicsFrameBus.current().filter(item -> item.tick() == this.timestamp4);
    ScaffoldRemapService.Input nextInput;
    boolean suppressSprint;
    if (result.isPresent()) {
      ScaffoldRemapService.Input previousInput =
          ((LocalPhysicsFrameBus.Frame) result.get()).input();
      nextInput =
          new ScaffoldRemapService.Input(
              previousInput.forward(),
              previousInput.backward(),
              previousInput.left(),
              previousInput.right(),
              currentInput.jump(),
              previousInput.sneak(),
              previousInput.sprint());
      suppressSprint = ((LocalPhysicsFrameBus.Frame) result.get()).suppressSprint();
      LocalPhysicsFrameBus.publishAppliedInput(this.timestamp4, nextInput, suppressSprint);
    } else {
      suppressSprint = this.moduleBool7.get() && (!currentInput.forward() || currentInput.sneak());
      nextInput =
          suppressSprint
              ? new ScaffoldRemapService.Input(
                  currentInput.forward(),
                  currentInput.backward(),
                  currentInput.left(),
                  currentInput.right(),
                  currentInput.jump(),
                  currentInput.sneak(),
                  false)
              : currentInput;
    }

    ScaffoldPublishService.publish(PlacementOverrideBus.serverInput(nextInput), suppressSprint);
    if (suppressSprint && minecraft.player != null) {
      minecraft.player.setSprinting(false);
    }
  }

  private void updateState19(Minecraft minecraft, ScaffoldRemapService.Result result) {
    this.entries3 = result;
    int value = result.serverInput().forward() && !result.serverInput().sneak() ? 1 : 0;
    int currentValue = !this.enabled6 && (!this.moduleBool7.get() || value != 0) ? 0 : 1;
    ScaffoldPublishService.publish(result.serverInput(), (currentValue != 0));
    if (currentValue != 0 && minecraft.player != null) {
      minecraft.player.setSprinting(false);
    }
  }

  private boolean checkCondition6(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Result result,
      boolean enabled) {
    String text = (String) this.moduleMode7.get();
    int value =
        ("Edge".equals(text)
                    || "Edge + Acquire".equals(text)
                    || "Start".equals(text) && this.count3 == 0
                    || "Eagle".equals(text)
                        && this.count3 % (Math.round((Float) this.moduleNumber17.get()) + 1) == 0)
                && this.checkCondition11(scaffoldPlayerSnapshot, result)
            ? 1
            : 0;
    int currentValue =
        ("Acquire".equals(text) || "Edge + Acquire".equals(text))
                && this.scaffoldData6 != null
                && this.scaffoldMotionPhaseData2 != null
                && this.scaffoldMotionPhaseData2.deadlineTicks()
                    <= ((Float) this.moduleNumber49.get()).floatValue() + 1.0
                && acquisitionSneakNeeded(enabled, this.step == null ? null : this.step.phase())
            ? 1
            : 0;
    int currentY =
        this.moduleBool13.get()
                && this.scaffoldData6 != null
                && scaffoldPlayerSnapshot.velocity().y() < -0.03
            ? 1
            : 0;
    int nextValue =
        this.moduleBool10.get()
                && result.moving()
                && result.angularErrorDegrees() > ((Float) this.moduleNumber12.get()).floatValue()
            ? 1
            : 0;
    int previousValue =
        !"Always".equals(text) && value == 0 && currentValue == 0 && currentY == 0 && nextValue == 0
            ? 0
            : 1;
    if ("Off".equals(text)) {
      this.timestamp7 = 0L;
      return false;
    }

    if (previousValue != 0) {
      this.timestamp7 = this.timestamp4 + Math.round((Float) this.moduleNumber20.get());
    }

    return previousValue != 0 || this.timestamp4 <= this.timestamp7;
  }

  private boolean checkCondition7() {
    return automaticSneakAllowed((String) this.moduleMode7.get());
  }

  static boolean automaticSneakAllowed(String text) {
    return !"Off".equals(text);
  }

  static boolean shouldSynthesizeSneak(String text, boolean enabled, boolean currentEnabled) {
    return automaticSneakAllowed(text) && (enabled || currentEnabled);
  }

  static boolean acquisitionSneakNeeded(boolean enabled, ScaffoldNextService.Phase phase) {
    return !enabled
        || phase != null
            && (phase == ScaffoldNextService.Phase.REACTION
                || phase == ScaffoldNextService.Phase.PRIMARY
                || phase == ScaffoldNextService.Phase.HOMING
                || phase == ScaffoldNextService.Phase.RECOVERY);
  }

  static boolean reusablePostPlacementReady(
      long longValue,
      ScaffoldDecisionTracker.TargetKey targetKey,
      long currentLongValue,
      ScaffoldDecisionTracker.TargetKey currentTargetKey,
      boolean enabled,
      boolean currentEnabled) {
    return enabled
        && currentEnabled
        && targetKey != null
        && currentLongValue == longValue - 1L
        && targetKey.equals(currentTargetKey);
  }

  static boolean authoritativePlacementReady(boolean enabled, boolean currentEnabled) {
    return enabled && currentEnabled;
  }

  static Integer confirmedDeckLayer(long longValue, PlacementCandidate placementCandidate) {
    return longValue != 0L && placementCandidate != null
        ? placementCandidate.targetPosition().y()
        : null;
  }

  static boolean towerWaitingFeedback(boolean enabled, boolean currentEnabled) {
    return enabled && currentEnabled;
  }

  static boolean shouldSuppressTowerLookahead(boolean enabled, boolean currentEnabled) {
    return enabled || currentEnabled;
  }

  static boolean towerJumpMayContinue(
      boolean enabled, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled) {
    return enabled && currentEnabled && nextEnabled && !previousEnabled;
  }

  private boolean checkCondition8(ScaffoldDecisionTracker.TargetKey targetKey) {
    ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt = this.placementAttempt;
    return pendingPlacementMatches(
        targetKey,
        currentPlacementAttempt == null
            ? null
            : createTargetKey(currentPlacementAttempt.placement()),
        this.scaffoldDecisionTracker == null ? null : this.scaffoldDecisionTracker.lockedTarget(),
        currentPlacementAttempt == null ? 0L : currentPlacementAttempt.token(),
        this.scaffoldDecisionTracker == null
            ? 0L
            : this.scaffoldDecisionTracker.pendingAttemptToken());
  }

  private boolean checkCondition9(ScaffoldDecisionTracker.TargetKey targetKey) {
    ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt = this.placementAttempt;
    return towerWaitingFeedback(
        currentPlacementAttempt != null && currentPlacementAttempt.towerPlacement(),
        this.checkCondition8(targetKey));
  }

  static boolean pendingPlacementMatches(
      ScaffoldDecisionTracker.TargetKey targetKey,
      ScaffoldDecisionTracker.TargetKey currentTargetKey,
      ScaffoldDecisionTracker.TargetKey nextTargetKey,
      long longValue,
      long currentLongValue) {
    return targetKey != null
        && targetKey.equals(currentTargetKey)
        && targetKey.equals(nextTargetKey)
        && longValue > 0L
        && longValue == currentLongValue;
  }

  private boolean checkCondition10() {
    ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt = this.placementAttempt;
    return shouldTrackLookahead(
        createTargetKey(this.scaffoldData6),
        createTargetKey(this.scaffoldData62),
        currentPlacementAttempt == null
            ? null
            : createTargetKey(currentPlacementAttempt.placement()),
        this.scaffoldDecisionTracker == null ? null : this.scaffoldDecisionTracker.lockedTarget(),
        currentPlacementAttempt == null ? 0L : currentPlacementAttempt.token(),
        this.scaffoldDecisionTracker == null
            ? 0L
            : this.scaffoldDecisionTracker.pendingAttemptToken());
  }

  static boolean shouldTrackLookahead(
      ScaffoldDecisionTracker.TargetKey targetKey,
      ScaffoldDecisionTracker.TargetKey currentTargetKey,
      ScaffoldDecisionTracker.TargetKey nextTargetKey,
      ScaffoldDecisionTracker.TargetKey previousTargetKey,
      long longValue,
      long currentLongValue) {
    return currentTargetKey != null
        && !currentTargetKey.equals(targetKey)
        && pendingPlacementMatches(
            targetKey, nextTargetKey, previousTargetKey, longValue, currentLongValue);
  }

  static boolean shouldRetainLookahead(
      boolean enabled,
      ScaffoldDecisionTracker.TargetKey targetKey,
      boolean currentEnabled,
      ScaffoldDecisionTracker.TargetKey currentTargetKey) {
    return enabled && targetKey != null && currentEnabled && targetKey.equals(currentTargetKey);
  }

  static boolean lookaheadReplacementConfirmed(
      ScaffoldDecisionTracker.TargetKey targetKey,
      long longValue,
      ScaffoldDecisionTracker.TargetKey currentTargetKey,
      long currentLongValue) {
    return currentTargetKey != null
        && currentTargetKey.equals(targetKey)
        && longValue == currentLongValue - 1L;
  }

  static boolean plannedLookaheadMatches(
      ScaffoldDecisionTracker.TargetKey targetKey,
      ScaffoldDecisionTracker.TargetKey currentTargetKey) {
    return targetKey != null && targetKey.equals(currentTargetKey);
  }

  static boolean changesStructuralMotorTarget(
      ScaffoldDecisionTracker.TargetKey targetKey,
      ScaffoldDecisionTracker.TargetKey currentTargetKey) {
    return targetKey != null && currentTargetKey != null && !targetKey.equals(currentTargetKey);
  }

  private boolean checkCondition11(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldRemapService.Result result) {
    if (this.scaffoldData6 != null && result.moving()) {
      ScaffoldMode scaffoldMode = this.scaffoldData6.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN) {
        RotationVector currentX =
            new RotationVector(scaffoldMode.x(), scaffoldMode.y(), scaffoldMode.z());
        double doubleValue = scaffoldPlayerSnapshot.velocity().dot(currentX);
        if (doubleValue <= 1.0E-4) {
          return false;
        }

        double currentDoubleValue =
            scaffoldPlayerSnapshot
                .eyePosition()
                .subtract(this.scaffoldData6.hitPoint())
                .dot(currentX);
        double nextDoubleValue =
            ((Float) this.moduleNumber18.get()).floatValue()
                + doubleValue * ((Float) this.moduleNumber19.get()).floatValue();
        return currentDoubleValue >= -nextDoubleValue;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private boolean checkCondition12(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Result result,
      boolean enabled) {
    boolean currentEnabled = this.checkCondition23(this.scaffoldData6);
    boolean nextEnabled = this.checkCondition9(createTargetKey(this.scaffoldData6));
    if (mustBrakeJumpStart(
        scaffoldPlayerSnapshot.onGround(),
        result.serverInput().jump(),
        this.enabled9,
        currentEnabled,
        nextEnabled,
        enabled)) {
      return true;
    }

    int value =
        !(scaffoldPlayerSnapshot.horizontalSpeed()
                    >= ((Float) this.moduleNumber14.get()).floatValue())
                && (this.snapshot2 == null
                    || this.snapshot2.phase() != ScaffoldObservationTracker.Phase.COASTING
                    || !(scaffoldPlayerSnapshot.horizontalSpeed() > 1.0E-9))
            ? 0
            : 1;
    int currentY =
        !this.moduleBool22.get()
                || scaffoldPlayerSnapshot.onGround()
                || !(scaffoldPlayerSnapshot.velocity().y() < -0.02)
                || this.scaffoldData6 == null && this.scaffoldData72 == null
            ? 0
            : 1;
    if ((Boolean) this.moduleBool25.get() && (result.moving() || value != 0 || currentY != 0)) {
      if (scaffoldPlayerSnapshot.onGround() && !result.serverInput().jump()) {
        return !this.m2nmsie7t6(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, result);
      } else if (this.scaffoldData6 != null && this.scaffoldMotionPhaseData2 != null) {
        int currentValue =
            this.scaffoldMotionPhaseData2.deadlineTicks()
                    <= ((Float) this.moduleNumber49.get()).floatValue()
                ? 1
                : 0;
        int nextY =
            this.moduleBool22.get()
                    && !scaffoldPlayerSnapshot.onGround()
                    && scaffoldPlayerSnapshot.velocity().y() < -0.02
                ? 1
                : 0;
        boolean previousEnabled =
            this.checkCondition13(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, result);
        return previousEnabled || !enabled && (currentValue != 0 || nextY != 0);
      } else {
        return currentY != 0 && this.scaffoldData72 != null
            ? true
            : this.checkCondition13(
                scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, result);
      }
    } else {
      return false;
    }
  }

  private boolean checkCondition13(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Result result) {
    if (scaffoldPlayerSnapshot.onGround() && !result.serverInput().jump()) {
      return !this.m2nmsie7t6(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, result);
    }

    double doubleValue = scaffoldPlayerSnapshot.horizontalSpeed();
    ScaffoldRemapService.WorldVector currentX =
        doubleValue < 1.0E-9
            ? new ScaffoldRemapService.WorldVector(0.0, 0.0)
            : new ScaffoldRemapService.WorldVector(
                scaffoldPlayerSnapshot.velocity().x() / doubleValue,
                scaffoldPlayerSnapshot.velocity().z() / doubleValue);
    ScaffoldRemapService.WorldVector worldVector =
        this.snapshot2 == null ? result.intendedWorldVector() : this.snapshot2.requestedDirection();
    double currentDoubleValue =
        this.calculateValue2(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot);
    double nextDoubleValue =
        0.31
            + Math.max(doubleValue, ((Float) this.moduleNumber14.get()).floatValue())
                * currentDoubleValue;
    int value =
        (minecraft.player == null || !minecraft.player.isSprinting())
                && !result.serverInput().sprint()
            ? 0
            : 1;
    if (result.serverInput().jump()) {
      nextDoubleValue = Math.max(nextDoubleValue, jumpSafetyHorizon(doubleValue, (value != 0)));
    }

    double currentLength = worldVector.length() < 1.0E-9 ? 0.0 : nextDoubleValue;
    double previousDoubleValue =
        doubleValue < 1.0E-9 ? 0.0 : 0.31 + doubleValue * currentDoubleValue;
    if (result.serverInput().jump() && previousDoubleValue > 0.0) {
      previousDoubleValue =
          Math.max(previousDoubleValue, jumpSafetyHorizon(doubleValue, (value != 0)));
    }

    ScaffoldSweepsService scaffoldSweeps =
        ScaffoldSweepsService.of(
            worldVector,
            currentLength,
            result.serverWorldVector(),
            result.moving() ? nextDoubleValue : 0.0,
            currentX,
            previousDoubleValue);
    BlockCoordinates blockCoordinates = this.createScaffoldData7();
    return !ContinuousSupportChecker.hasContinuousSupport(
        scaffoldPlayerSnapshot.feetPosition(),
        scaffoldSweeps,
        this.count8,
        item ->
            countableSupport(
                item,
                blockCoordinates,
                this.checkCondition14(item),
                scaffoldCompatibility.isPlayerSupporting(minecraft, item)));
  }

  private boolean m2nmsie7t6(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Result result) {
    BlockCoordinates blockCoordinates = this.createScaffoldData7();
    return PlacementRuleEvaluator.allows(
        scaffoldPlayerSnapshot,
        result.serverWorldVector(),
        this.count8,
        ((Float) this.moduleNumber48.get()).floatValue(),
        calculateValue3(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot),
        scaffoldCompatibility.groundInputAcceleration(
            minecraft,
            !(Boolean) this.moduleBool7.get()
                || result.serverInput().forward() && !result.serverInput().sneak()),
        item ->
            countableSupport(
                item,
                blockCoordinates,
                this.checkCondition14(item),
                scaffoldCompatibility.isPlayerSupporting(minecraft, item)));
  }

  static boolean countableSupport(
      BlockCoordinates blockCoordinates,
      BlockCoordinates currentBlockCoordinates,
      boolean enabled,
      boolean currentEnabled) {
    return currentEnabled
        && !enabled
        && (currentBlockCoordinates == null || !currentBlockCoordinates.equals(blockCoordinates));
  }

  private BlockCoordinates createScaffoldData7() {
    if (this.placementAttempt != null) {
      return this.placementAttempt.placement().targetPosition();
    } else if (this.placementAttempt2 != null) {
      return this.placementAttempt2.placement().targetPosition();
    } else {
      return this.scaffoldDecisionTracker != null
              && this.scaffoldDecisionTracker.pendingAttemptToken() != 0L
              && this.scaffoldData6 != null
          ? this.scaffoldData6.targetPosition()
          : null;
    }
  }

  static boolean mustBrakeJumpStart(
      boolean enabled,
      boolean currentEnabled,
      boolean nextEnabled,
      boolean previousEnabled,
      boolean sourceEnabled,
      boolean targetEnabled) {
    if (!enabled || !currentEnabled) {
      return false;
    } else {
      return sourceEnabled && !targetEnabled ? true : nextEnabled && !previousEnabled;
    }
  }

  static double jumpSafetyHorizon(double doubleValue) {
    return jumpSafetyHorizon(doubleValue, false);
  }

  static double jumpSafetyHorizon(double doubleValue, boolean enabled) {
    if (Double.isFinite(doubleValue) && !(doubleValue < 0.0)) {
      double currentDoubleValue = enabled ? 0.2 : 0.0;
      return 0.31 + (Math.max(doubleValue, 0.13) + currentDoubleValue) * 4.0;
    } else {
      throw new IllegalArgumentException("Horizontal speed must be finite and non-negative");
    }
  }

  static double requiredPlanningLookAheadForJump(
      double doubleValue, double currentDoubleValue, boolean enabled) {
    if (!Double.isFinite(currentDoubleValue)) {
      throw new IllegalArgumentException("Projected momentum must be finite");
    }

    double nextDoubleValue = jumpSafetyHorizon(doubleValue, enabled);
    return calculateValue8(nextDoubleValue - 0.3 - currentDoubleValue * 1.5, 0.0, 3.0);
  }

  private boolean checkCondition14(BlockCoordinates blockCoordinates) {
    return this.placementAttempt2 != null
        && this.placementAttempt2.placement().targetPosition().equals(blockCoordinates);
  }

  private void updateState20(ScaffoldCompatibility scaffoldCompatibility, Minecraft minecraft) {
    ScaffoldBeginService.State currentState = this.state2;
    ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt =
        this.placementAttempt != null ? this.placementAttempt : this.placementAttempt2;
    if (checkCondition15(currentState, currentPlacementAttempt)
        && !this.checkCondition16(currentPlacementAttempt.token())
        && this.timestamp12 != currentPlacementAttempt.token()) {
      BlockCoordinates blockCoordinates = currentPlacementAttempt.placement().targetPosition();
      ScaffoldBeginService.TargetState targetState;
      if (scaffoldCompatibility.isPlayerSupporting(minecraft, blockCoordinates)) {
        targetState = ScaffoldBeginService.TargetState.SUPPORTING;
      } else if (scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates)) {
        targetState = ScaffoldBeginService.TargetState.REPLACEABLE;
      } else {
        targetState = ScaffoldBeginService.TargetState.OTHER;
      }

      ScaffoldBeginService.Transition transition =
          ScaffoldBeginService.advance(
              currentState, this.timestamp4, ScaffoldBeginService.Evidence.NONE, targetState);
      this.state2 = transition.state();
      if (transition.action() == ScaffoldBeginService.Action.REQUEST_PREDICTION_RESOLUTION) {
        try {
          scaffoldCompatibility.reconcilePlacementPrediction(
              minecraft, currentState.key().sequence());
        } catch (RuntimeException exception) {
          CoreIsInitializedHandler.LOGGER.error(
              "Failed to reconcile scaffold prediction sequence {}",
              currentState.key().sequence(),
              exception);
          this.updateState22(
              currentPlacementAttempt, ScaffoldBeginService.terminalAbort(this.state2));
        }
      } else {
        this.updateState22(currentPlacementAttempt, transition);
      }
    }
  }

  private void updateState21(
      ImplSuspendForPearlService.PlacementAttempt placementAttempt,
      ScaffoldCompatibility.PlacementFeedback placementFeedback) {
    ScaffoldBeginService.State currentState = this.state2;
    if (checkCondition15(currentState, placementAttempt)) {
      ScaffoldBeginService.Evidence evidence =
          switch (placementFeedback) {
            case ACCEPTED -> ScaffoldBeginService.Evidence.ACCEPTED;
            case REJECTED -> ScaffoldBeginService.Evidence.REJECTED;
            case NONE -> ScaffoldBeginService.Evidence.NONE;
          };
      ScaffoldBeginService.Transition transition =
          ScaffoldBeginService.advance(
              currentState, this.timestamp4, evidence, ScaffoldBeginService.TargetState.OTHER);
      this.state2 = transition.state();
      this.updateState22(placementAttempt, transition);
    }
  }

  private void updateState22(
      ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt,
      ScaffoldBeginService.Transition transition) {
    ScaffoldBeginService.Action currentAction = transition.action();
    if (currentAction != ScaffoldBeginService.Action.NONE
        && currentAction != ScaffoldBeginService.Action.REQUEST_PREDICTION_RESOLUTION) {
      long offset = currentPlacementAttempt.token();
      if (currentAction == ScaffoldBeginService.Action.TERMINAL_ABORT) {
        this.enabled10 = true;
        this.placementAttempt = null;
        this.placementAttempt2 = null;
        if (this.scaffoldDecisionTracker != null) {
          this.scaffoldDecisionTracker.retireAttempt(
              offset,
              createTargetKey(currentPlacementAttempt.placement()),
              ScaffoldDecisionTracker.AttemptOutcome.TERMINAL,
              this.timestamp4);
        }

        this.scaffoldData6 = null;
        this.scaffoldData62 = null;
        this.scaffoldMotionPhaseData = null;
        this.count4 = -1;
        this.targetKey = null;
        this.timestamp6 = Long.MIN_VALUE;
        this.scaffoldMotionPhaseData2 = null;
        this.updateState29();
        CoreIsInitializedHandler.LOGGER.error(
            "Scaffold actor stopped fail-closed: placement sequence {} did not receive"
                + " authoritative resolution",
            currentPlacementAttempt.sequence());
      } else {
        int value = currentAction == ScaffoldBeginService.Action.ACCEPT ? 1 : 0;
        if (this.placementAttempt != null && this.placementAttempt.token() == offset) {
          if (value != 0) {
            this.timestamp14 = offset;
          } else {
            this.timestamp15 = offset;
          }
        } else {
          if (this.placementAttempt2 != null && this.placementAttempt2.token() == offset) {
            this.updateState26(currentPlacementAttempt, (value != 0));
          }
        }
      }
    }
  }

  private static boolean checkCondition15(
      ScaffoldBeginService.State state,
      ImplSuspendForPearlService.PlacementAttempt placementAttempt) {
    return state != null
        && state.active()
        && placementAttempt != null
        && state.key().token() == placementAttempt.token()
        && state.key().sequence() == placementAttempt.sequence()
        && state.key().target().equals(createTargetKey(placementAttempt.placement()));
  }

  private boolean checkCondition16(long longValue) {
    return this.timestamp12 == longValue
            && (this.timestamp10 == longValue || this.timestamp11 == longValue)
        || this.timestamp14 == longValue
        || this.timestamp15 == longValue;
  }

  private boolean checkCondition17(long longValue) {
    return this.timestamp12 == longValue && this.timestamp10 == longValue
        || this.timestamp14 == longValue;
  }

  private boolean checkCondition18(long longValue) {
    return this.timestamp12 == longValue && this.timestamp11 == longValue
        || this.timestamp15 == longValue;
  }

  private void updateState23(long offset) {
    ScaffoldBeginService.State state = this.state2;
    if (state != null && state.key().token() == offset) {
      this.state2 = ScaffoldBeginService.retire(state);
    }
  }

  private void updateState24() {
    if (this.placementAttempt != null) {
      if (this.scaffoldDecisionTracker != null) {
        this.scaffoldDecisionTracker.quarantinePendingTarget(
            createTargetKey(this.placementAttempt.placement()), this.timestamp4);
      }

      this.placementAttempt2 = this.placementAttempt;
      this.placementAttempt = null;
    }
  }

  private void updateState25(ScaffoldCompatibility scaffoldCompatibility, Minecraft minecraft) {
    long offset = this.timestamp12;
    if (offset != 0L && this.timestamp4 > this.timestamp13) {
      ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt =
          this.placementAttempt != null && this.placementAttempt.token() == offset
              ? this.placementAttempt
              : (this.placementAttempt2 != null && this.placementAttempt2.token() == offset
                  ? this.placementAttempt2
                  : null);
      if (currentPlacementAttempt == null) {
        this.updateState29();
      } else {
        int value = this.timestamp10 == offset ? 1 : 0;
        int currentValue = this.timestamp11 == offset ? 1 : 0;
        if (this.placementAttempt2 == null
            || this.placementAttempt2.token() != offset
            || value == 0 && currentValue == 0) {
          if (value == 0 && currentValue == 0) {
            boolean playerSupporting =
                scaffoldCompatibility.isPlayerSupporting(
                    minecraft, currentPlacementAttempt.placement().targetPosition());
            if (this.placementAttempt != null && this.placementAttempt.token() == offset) {
              if (playerSupporting) {
                this.timestamp10 = offset;
              } else {
                this.timestamp11 = offset;
              }
            } else {
              this.updateState26(currentPlacementAttempt, playerSupporting);
            }
          }
        } else {
          this.updateState26(currentPlacementAttempt, (value != 0));
        }
      }
    }
  }

  private void updateState26(
      ImplSuspendForPearlService.PlacementAttempt placementAttempt, boolean enabled) {
    if (placementAttempt != null
        && this.placementAttempt2 != null
        && this.placementAttempt2.token() == placementAttempt.token()
        && this.placementAttempt2.sequence() == placementAttempt.sequence()) {
      ScaffoldDecisionTracker.TargetKey targetKey = createTargetKey(placementAttempt.placement());
      if (enabled) {
        this.scaffoldData63 = placementAttempt.placement();
      } else {
        this.updateState28(targetKey);
      }

      if (this.scaffoldDecisionTracker != null) {
        this.scaffoldDecisionTracker.retireAttempt(
            placementAttempt.token(),
            targetKey,
            enabled
                ? ScaffoldDecisionTracker.AttemptOutcome.ACCEPTED
                : ScaffoldDecisionTracker.AttemptOutcome.REJECTED,
            this.timestamp4);
      }

      this.placementAttempt2 = null;
      this.updateState23(placementAttempt.token());
      this.updateState29();
    }
  }

  private void updateState27() {
    ImplSuspendForPearlService.PlacementAttempt currentPlacementAttempt = this.placementAttempt;
    if (currentPlacementAttempt != null && this.checkCondition16(currentPlacementAttempt.token())) {
      boolean enabled = this.checkCondition17(currentPlacementAttempt.token());
      ScaffoldDecisionTracker.TargetKey targetKey =
          createTargetKey(currentPlacementAttempt.placement());
      if (enabled) {
        this.scaffoldData63 = currentPlacementAttempt.placement();
      } else {
        this.updateState28(targetKey);
      }

      if (this.scaffoldDecisionTracker != null) {
        this.scaffoldDecisionTracker.retireAttempt(
            currentPlacementAttempt.token(),
            targetKey,
            enabled
                ? ScaffoldDecisionTracker.AttemptOutcome.ACCEPTED
                : ScaffoldDecisionTracker.AttemptOutcome.REJECTED,
            this.timestamp4);
      }

      this.placementAttempt = null;
      this.updateState23(currentPlacementAttempt.token());
      this.updateState29();
    }
  }

  private void updateState28(ScaffoldDecisionTracker.TargetKey targetKey) {
    this.targetKey3 = targetKey;
    this.timestamp9 =
        this.timestamp4
            + Math.max(
                Math.round((Float) this.moduleNumber45.get()),
                Math.round((Float) this.moduleNumber56.get()));
  }

  private void updateState29() {
    this.timestamp10 = 0L;
    this.timestamp11 = 0L;
    this.timestamp12 = 0L;
    this.timestamp13 = Long.MIN_VALUE;
    this.timestamp14 = 0L;
    this.timestamp15 = 0L;
    this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
  }

  private ScaffoldRemapService.Input createMap4(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Input input) {
    BlockCoordinates blockCoordinates =
        this.scaffoldData6 != null ? this.scaffoldData6.supportPosition() : this.scaffoldData72;
    if (shouldAirReturnAssist(
        (Boolean) this.moduleBool26.get(),
        scaffoldPlayerSnapshot.onGround(),
        scaffoldPlayerSnapshot.velocity().y(),
        blockCoordinates != null)) {
      double currentX = blockCoordinates.x() + 0.5 - scaffoldPlayerSnapshot.feetPosition().x();
      double doubleValue = blockCoordinates.z() + 0.5 - scaffoldPlayerSnapshot.feetPosition().z();
      if (Math.hypot(currentX, doubleValue) > 0.05) {
        return this.entries2.steerToward(
            scaffoldPlayerSnapshot.playerRotation().yaw(), currentX, doubleValue, input);
      }
    }

    boolean enabled = this.checkCondition9(createTargetKey(this.scaffoldData6));
    boolean currentEnabled =
        towerJumpMayContinue(
            scaffoldPlayerSnapshot.onGround(),
            this.enabled9,
            this.checkCondition23(this.scaffoldData6),
            enabled);
    return groundedSafetyInput(
        input,
        this.checkCondition19(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, input),
        !scaffoldPlayerSnapshot.onGround() || currentEnabled);
  }

  private boolean checkCondition19(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Input input) {
    return this.checkCondition20(
        scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot, input, this.rotationData);
  }

  private boolean checkCondition20(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Input input,
      RotationData rotationData) {
    if (!targetFaceApproachAllowed(
        scaffoldPlayerSnapshot, this.scaffoldData6, input, this.checkCondition7())) {
      return false;
    }

    if (scaffoldCompatibility.findPlaceableHotbarSlot(minecraft) < 0) {
      return false;
    }

    ScaffoldRemapService.Result result =
        this.createMap3(
            scaffoldPlayerSnapshot.playerRotation().yaw(), rotationData.yaw(), input, false);
    BlockCoordinates blockCoordinates = this.createScaffoldData7();
    return ScaffoldAllowsService.allows(
        scaffoldPlayerSnapshot,
        this.scaffoldData6,
        result.serverWorldVector(),
        ((Float) this.moduleNumber48.get()).floatValue(),
        calculateValue3(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot),
        scaffoldCompatibility.groundInputAcceleration(minecraft, false),
        item ->
            countableSupport(
                item,
                blockCoordinates,
                this.checkCondition14(item),
                scaffoldCompatibility.isPlayerSupporting(minecraft, item)));
  }

  private double calculateValue2(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    return Math.max(
        Math.max(1.0, ((Float) this.moduleNumber49.get()).floatValue()),
        calculateValue3(scaffoldCompatibility, minecraft, scaffoldPlayerSnapshot));
  }

  private static double calculateValue3(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    double doubleValue =
        scaffoldPlayerSnapshot.onGround() ? scaffoldCompatibility.groundFriction(minecraft) : 1.0;
    if (!Double.isFinite(doubleValue) || doubleValue < 0.0 || doubleValue > 1.0) {
      doubleValue = 1.0;
    }

    return ScaffoldCoastTicksService.coastTicks(doubleValue);
  }

  static boolean shouldAirReturnAssist(
      boolean enabled, boolean currentEnabled, double doubleValue, boolean nextEnabled) {
    if (!Double.isFinite(doubleValue)) {
      throw new IllegalArgumentException("Vertical velocity must be finite");
    } else {
      return enabled && !currentEnabled && nextEnabled;
    }
  }

  static ScaffoldRemapService.Input groundedSafetyInput(
      ScaffoldRemapService.Input input, boolean enabled, boolean currentEnabled) {
    int value = currentEnabled && input.jump() ? 1 : 0;
    return enabled
        ? new ScaffoldRemapService.Input(
            input.forward(),
            input.backward(),
            input.left(),
            input.right(),
            (value != 0),
            input.sneak(),
            false)
        : new ScaffoldRemapService.Input(
            false, false, false, false, (value != 0), input.sneak(), false);
  }

  private boolean checkCondition21(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    return (Boolean) this.moduleBool27.get()
        && (this.enabled6
            || !scaffoldPlayerSnapshot.onGround()
            || this.scaffoldMotionPhaseData2 != null
                && this.scaffoldMotionPhaseData2.deadlineTicks()
                    <= ((Float) this.moduleNumber49.get()).floatValue());
  }

  static boolean targetFaceApproachAllowed(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      PlacementCandidate placementCandidate,
      ScaffoldRemapService.Input input,
      boolean enabled) {
    if (scaffoldPlayerSnapshot != null && placementCandidate != null && input != null) {
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN) {
        RotationVector currentX =
            new RotationVector(scaffoldMode.x(), scaffoldMode.y(), scaffoldMode.z());
        double doubleValue =
            scaffoldPlayerSnapshot
                .eyePosition()
                .subtract(placementCandidate.hitPoint())
                .dot(currentX);
        ScaffoldRemapService.WorldVector currentWorldVector =
            ScaffoldRemapService.worldVector(
                scaffoldPlayerSnapshot.playerRotation().yaw(),
                input.forwardAxis(),
                input.leftAxis());
        double nextX =
            currentWorldVector.x() * currentX.x() + currentWorldVector.z() * currentX.z();
        double currentDoubleValue = scaffoldPlayerSnapshot.velocity().dot(currentX);
        if (nextX <= 1.0E-4) {
          return false;
        } else {
          return !enabled && !input.sneak()
              ? doubleValue < -0.08
                  || doubleValue < 0.045 && Math.max(0.0, currentDoubleValue) < 0.025
              : doubleValue < 0.045;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private static boolean checkCondition22(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate) {
    return placementCandidate.targetPosition().y() + 1.0
        <= scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6;
  }

  private boolean checkCondition23(PlacementCandidate placementCandidate) {
    return placementCandidate != null
        && this.scaffoldData7 != null
        && placementCandidate.targetPosition().equals(this.scaffoldData7)
        && placementCandidate.clickedFace() == ScaffoldMode.UP
        && placementCandidate
            .supportPosition()
            .offset(ScaffoldMode.UP)
            .equals(placementCandidate.targetPosition());
  }

  static RotationData towerLookRotation(RotationData rotationData) {
    if (rotationData == null) {
      throw new IllegalArgumentException("Tower rotation requires a current movement yaw");
    } else {
      return new RotationData((float) rotationData.yaw(), 90.0);
    }
  }

  private int calculateValue4() {
    int value = Math.max(1, Math.min(9, Math.round((Float) this.moduleNumber52.get())));
    if (value % 2 == 0) {
      value += value == 9 ? -1 : 1;
    }

    return value;
  }

  static boolean rayMissCountsTowardReplan(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      PlacementCandidate placementCandidate,
      ScaffoldNextService.Phase phase,
      boolean enabled) {
    if (phase != null
        && phase != ScaffoldNextService.Phase.REACTION
        && phase != ScaffoldNextService.Phase.PRIMARY
        && !enabled) {
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      double currentX =
          scaffoldPlayerSnapshot
              .eyePosition()
              .subtract(placementCandidate.hitPoint())
              .dot(new RotationVector(scaffoldMode.x(), scaffoldMode.y(), scaffoldMode.z()));
      return currentX > 1.0E-4;
    } else {
      return false;
    }
  }

  private void updateState30() {
    if (this.scaffoldData6 != null) {
      this.scaffoldGroundWindowService.reset();
      this.enabled3 = false;
      if (this.checkCondition23(this.scaffoldData6)) {
        this.scaffoldData7 = null;
      }

      this.targetKey3 = createTargetKey(this.scaffoldData6);
      this.timestamp9 = this.timestamp4 + Math.round((Float) this.moduleNumber56.get());
      if (this.placementAttempt != null
          && createTargetKey(this.placementAttempt.placement()).equals(this.targetKey3)) {
        this.updateState24();
      } else {
        this.scaffoldDecisionTracker.abortTarget(this.targetKey3, this.timestamp4);
      }

      this.scaffoldData6 = null;
      this.scaffoldData62 = null;
      this.scaffoldMotionPhaseData = null;
      this.count4 = -1;
      this.targetKey = null;
      this.timestamp6 = Long.MIN_VALUE;
      this.scaffoldMotionPhaseData2 = null;
      this.step = null;
      this.targetKey4 = null;
      this.count10 = -1;
      this.timestamp17 = Long.MIN_VALUE;
      this.count7 = 0;
      if (this.scaffoldNextService != null) {
        this.scaffoldNextService.reset();
      }
    }
  }

  private void updateState31() {
    if (this.scaffoldData6 != null && this.placementAttempt == null) {
      this.scaffoldGroundWindowService.reset();
      this.enabled3 = false;
      if (this.scaffoldDecisionTracker != null) {
        this.scaffoldDecisionTracker.abortTarget(
            createTargetKey(this.scaffoldData6), this.timestamp4);
      }

      this.scaffoldData6 = null;
      this.scaffoldData7 = null;
      this.scaffoldData62 = null;
      this.scaffoldMotionPhaseData = null;
      this.count4 = -1;
      this.targetKey = null;
      this.timestamp6 = Long.MIN_VALUE;
      this.scaffoldMotionPhaseData2 = null;
      this.step = null;
      this.targetKey4 = null;
      this.count10 = -1;
      this.timestamp17 = Long.MIN_VALUE;
      this.count7 = 0;
      if (this.scaffoldNextService != null) {
        this.scaffoldNextService.reset();
      }
    }
  }

  private void updateState32() {
    this.scaffoldGroundWindowService.reset();
    this.enabled3 = false;
    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      PlacementOverrideBus.clear();
    }

    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      ScaffoldPublishService.clear();
    }

    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      LocalPhysicsFrameBus.clear();
    }

    this.entries3 = null;
    this.timestamp7 = 0L;
  }

  private void updateState33(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      CombatCompatibility combatCompatibility) {
    this.scaffoldGroundWindowService.reset();
    this.enabled3 = false;
    this.updateState7("automation-suspended", true);
    RotationData currentRotationData =
        this.issuedPlacementTrace == null ? null : this.issuedPlacementTrace.arm().wireRotation();
    this.updateState27();
    this.updateState24();
    if (this.scaffoldDecisionTracker != null) {
      this.scaffoldDecisionTracker.prepareTick(
          this.timestamp4, this.createContext(scaffoldCompatibility, minecraft));
    }

    this.scaffoldData6 = null;
    this.scaffoldData62 = null;
    this.scaffoldMotionPhaseData = null;
    this.count4 = -1;
    this.targetKey = null;
    this.timestamp6 = Long.MIN_VALUE;
    this.rotationData = currentRotationData;
    this.step = null;
    this.targetKey4 = null;
    this.count10 = -1;
    this.timestamp17 = Long.MIN_VALUE;
    this.scaffoldMotionPhaseData2 = null;
    this.value9 = Double.NaN;
    this.targetKey2 = null;
    this.timestamp8 = Long.MIN_VALUE;
    this.enabled4 = false;
    this.value10 = 0.0;
    this.enabled5 = false;
    this.entries4 = null;
    this.scaffoldData4 = null;
    this.decision = null;
    this.enabled6 = false;
    this.enabled7 = false;
    this.enabled8 = false;
    this.count7 = 0;
    this.enabled9 = false;
    this.scaffoldData7 = null;
    this.scaffoldData72 = null;
    this.scaffoldData64 = null;
    this.count9 = -1;
    this.preparedPlacementUse = null;
    this.scaffoldStateController.reset();
    this.scaffoldObservationTracker.reset();
    this.snapshot2 = this.scaffoldObservationTracker.current();
    if (this.scaffoldNextService != null) {
      this.scaffoldNextService.reset();
    }

    ScaffoldRemapService.Input input = failClosedMovementInput(minecraft.options.keyShift.isDown());
    PlacementOverrideBus.publish(new PlacementOverrideBus.Override(true, input, false, true, true));
    ScaffoldPublishService.publish(input, true);
    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      LocalPhysicsFrameBus.clear();
    }

    if (minecraft.player != null) {
      minecraft.player.setSprinting(false);
    }

    this.entries3 = null;
    this.timestamp7 = 0L;
    if (currentRotationData == null) {
      combatCompatibility.clearServerRotation(minecraft, CombatOwnsService.Owner.SCAFFOLD);
    } else {
      combatCompatibility.applyServerRotation(
          minecraft, currentRotationData, CombatOwnsService.Owner.SCAFFOLD);
    }
  }

  private void updateState34(
      Minecraft minecraft,
      CombatCompatibility combatCompatibility,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot) {
    ScaffoldRemapService.Input input = failClosedMovementInput(minecraft.options.keyShift.isDown());
    this.rotationData =
        RotationObserveService.current()
            .orElse(
                this.rotationData == null
                    ? scaffoldPlayerSnapshot.playerRotation()
                    : this.rotationData);
    this.rotationData =
        new RotationData((float) this.rotationData.yaw(), (float) this.rotationData.pitch());
    combatCompatibility.applyServerRotation(
        minecraft, this.rotationData, CombatOwnsService.Owner.SCAFFOLD);
    PlacementOverrideBus.publish(new PlacementOverrideBus.Override(true, input, false, true, true));
    ScaffoldPublishService.publish(input, true);
    LocalPhysicsFrameBus.publish(
        new LocalPhysicsFrameBus.Frame(
            this.timestamp4, (float) this.rotationData.yaw(), input, true));
    if (minecraft.player != null) {
      minecraft.player.setSprinting(false);
    }

    this.entries3 = null;
    this.entries4 = input;
    this.scaffoldData4 = null;
    this.decision = null;
    this.enabled6 = true;
    this.enabled8 = false;
    this.timestamp7 = 0L;
  }

  static ScaffoldRemapService.Input failClosedMovementInput(boolean enabled) {
    return new ScaffoldRemapService.Input(false, false, false, false, false, enabled, false);
  }

  private boolean checkCondition24(
      ScaffoldCompatibility scaffoldCompatibility,
      Minecraft minecraft,
      PlacementCandidate placementCandidate) {
    return scaffoldCompatibility.isReplaceable(minecraft, placementCandidate.targetPosition())
        && scaffoldCompatibility.isFaceSturdy(
            minecraft, placementCandidate.supportPosition(), placementCandidate.clickedFace());
  }

  private boolean checkCondition25() {
    return this.scaffoldDecisionTracker != null
        && this.scaffoldDecisionTracker.cognitivePhase()
            == ScaffoldDecisionTracker.CognitivePhase.RECOVER;
  }

  private ScaffoldDecisionTracker.Context createContext(
      ScaffoldCompatibility scaffoldCompatibility, Minecraft minecraft) {
    return new ScaffoldDecisionTracker.Context(
        false,
        scaffoldCompatibility.selectedHotbarSlot(minecraft),
        scaffoldCompatibility.findPlaceableHotbarSlot(minecraft),
        null,
        ScaffoldMotionPhaseData.PathRelation.FIRST,
        0L);
  }

  private void updateState35() {
    this.rotationData2 = null;
    this.count11 = -1;
    this.scaffoldGroundWindowService.reset();
    this.enabled3 = false;
    this.updateState7("controller-rebuild", false);
    this.timestamp2 = Math.round((Float) this.moduleNumber57.get());
    this.timestamp3 =
        this.timestamp2 == 0L
            ? System.nanoTime() ^ Double.doubleToLongBits(Math.random())
            : this.timestamp2;
    this.scaffoldNextService = new ScaffoldNextService(this.timestamp3);
    this.scaffoldDecisionTracker =
        new ScaffoldDecisionTracker(this.timestamp3 ^ 7640891576956012809L);
    this.random2 = new Random(this.timestamp3 ^ -4942790177534073029L);
    this.value7 = this.random2.nextGaussian() * 0.01;
    this.value8 = this.random2.nextGaussian() * 0.01;
    this.scaffoldData6 = null;
    this.scaffoldData62 = null;
    this.scaffoldMotionPhaseData = null;
    this.count4 = -1;
    this.targetKey = null;
    this.timestamp6 = Long.MIN_VALUE;
    this.scaffoldData63 = null;
    this.placementAttempt = null;
    this.placementAttempt2 = null;
    this.rotationData = null;
    this.step = null;
    this.targetKey4 = null;
    this.count10 = -1;
    this.timestamp17 = Long.MIN_VALUE;
    this.scaffoldMotionPhaseData2 = null;
    this.value9 = Double.NaN;
    this.entries3 = null;
    this.targetKey2 = null;
    this.timestamp8 = Long.MIN_VALUE;
    this.enabled4 = false;
    this.value10 = 0.0;
    this.enabled5 = false;
    this.entries4 = null;
    this.scaffoldData4 = null;
    this.decision = null;
    this.enabled6 = false;
    this.enabled7 = false;
    this.enabled8 = false;
    this.count7 = 0;
    this.targetKey3 = null;
    this.timestamp9 = 0L;
    this.count8 = 0;
    this.enabled9 = false;
    this.scaffoldData7 = null;
    this.scaffoldData72 = null;
    this.timestamp10 = 0L;
    this.timestamp11 = 0L;
    this.timestamp12 = 0L;
    this.timestamp13 = Long.MIN_VALUE;
    this.timestamp14 = 0L;
    this.timestamp15 = 0L;
    this.state2 = null;
    this.enabled10 = false;
    this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
    this.scaffoldData64 = null;
    this.count9 = -1;
    this.preparedPlacementUse = null;
    this.issuedPlacementTrace = null;
    this.text2 = null;
    this.timestamp16 = Long.MIN_VALUE;
    this.scaffoldStateController.reset();
    this.scaffoldObservationTracker.reset();
    this.snapshot2 = this.scaffoldObservationTracker.current();
    this.timestamp7 = 0L;
    this.timestamp4 = 0L;
    this.timestamp5 = 0L;
    CombatOwnsService.clearFrame(CombatOwnsService.Owner.SCAFFOLD);
    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      ScaffoldPublishService.clear();
    }

    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      PlacementOverrideBus.clear();
    }

    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      LocalPhysicsFrameBus.clear();
    }
  }

  private void updateState36() {
    long offset = Math.round((Float) this.moduleNumber57.get());
    if (this.scaffoldNextService != null && this.scaffoldDecisionTracker != null) {
      if (offset != this.timestamp2) {
        if (this.placementAttempt != null
            || this.placementAttempt2 != null
            || this.preparedPlacementUse != null
            || this.issuedPlacementTrace != null
            || this.state2 != null && this.state2.active()) {
          return;
        }

        this.updateState38(true);
        this.updateState35();
      }
    } else {
      this.updateState38(true);
      this.updateState35();
    }
  }

  private void updateState37() {
    this.scaffoldGroundWindowService.reset();
    this.enabled3 = false;
    this.updateState7("lifecycle-reset", false);
    this.updateState32();
    this.scaffoldData6 = null;
    this.scaffoldData62 = null;
    this.scaffoldMotionPhaseData = null;
    this.count4 = -1;
    this.targetKey = null;
    this.timestamp6 = Long.MIN_VALUE;
    this.scaffoldData63 = null;
    this.placementAttempt = null;
    this.placementAttempt2 = null;
    this.rotationData = null;
    this.step = null;
    this.targetKey4 = null;
    this.count10 = -1;
    this.timestamp17 = Long.MIN_VALUE;
    this.scaffoldMotionPhaseData2 = null;
    this.value9 = Double.NaN;
    this.targetKey2 = null;
    this.timestamp8 = Long.MIN_VALUE;
    this.enabled4 = false;
    this.value10 = 0.0;
    this.enabled5 = false;
    this.entries4 = null;
    this.scaffoldData4 = null;
    this.decision = null;
    this.enabled6 = false;
    this.enabled7 = false;
    this.enabled8 = false;
    this.count7 = 0;
    this.targetKey3 = null;
    this.timestamp9 = 0L;
    this.count8 = 0;
    this.enabled9 = false;
    this.scaffoldData7 = null;
    this.scaffoldData72 = null;
    this.timestamp10 = 0L;
    this.timestamp11 = 0L;
    this.timestamp12 = 0L;
    this.timestamp13 = Long.MIN_VALUE;
    this.timestamp14 = 0L;
    this.timestamp15 = 0L;
    this.state2 = null;
    this.enabled10 = false;
    this.placementFeedback2 = ScaffoldCompatibility.PlacementFeedback.NONE;
    this.scaffoldData64 = null;
    this.count9 = -1;
    this.preparedPlacementUse = null;
    this.issuedPlacementTrace = null;
    this.text2 = null;
    this.timestamp16 = Long.MIN_VALUE;
    this.scaffoldStateController.reset();
    this.scaffoldObservationTracker.reset();
    this.snapshot2 = this.scaffoldObservationTracker.current();
    if (this.scaffoldNextService != null) {
      this.scaffoldNextService.reset();
    }

    if (this.scaffoldDecisionTracker != null) {
      this.scaffoldDecisionTracker.reset();
    }

    CombatOwnsService.clearFrame(CombatOwnsService.Owner.SCAFFOLD);
    if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
      LocalPhysicsFrameBus.clear();
    }
  }

  private void updateState38(boolean enabled) {
    this.count3 = 0;
    this.rotationData2 = null;
    this.count11 = -1;
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    Optional result = CompatAdapterService.scaffoldEnvironment();
    this.scaffoldPrepareService.reset(
        minecraft,
        (ScaffoldCompatibility) result.orElse(null),
        enabled && (Boolean) this.moduleBool21.get());
    this.scaffoldPrepareService2.reset(
        minecraft,
        (ScaffoldCompatibility) result.orElse(null),
        enabled && (Boolean) this.moduleBool21.get());
    this.text = "";
    this.enabled2 = false;
    if (enabled
        && (Boolean) this.moduleBool21.get()
        && this.count5 >= 0
        && result.isPresent()
        && minecraft.player != null
        && ((ScaffoldCompatibility) result.get()).selectedHotbarSlot(minecraft) == this.count6) {
      ((ScaffoldCompatibility) result.get()).selectHotbarSlot(minecraft, this.count5);
    }

    this.count5 = -1;
    this.count6 = -1;
    this.updateState37();
    CompatAdapterService.rotationEnvironment()
        .ifPresent(item -> item.clearServerRotation(minecraft, CombatOwnsService.Owner.SCAFFOLD));
    this.timestamp4 = 0L;
    this.timestamp5 = 0L;
  }

  private static ScaffoldDecisionTracker.TargetKey createTargetKey(
      PlacementCandidate placementCandidate) {
    return placementCandidate == null
        ? null
        : ScaffoldDecisionTracker.TargetKey.from(placementCandidate);
  }

  private static double calculateValue5(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate) {
    double currentLength =
        Math.max(
            0.1,
            placementCandidate.hitPoint().subtract(scaffoldPlayerSnapshot.eyePosition()).length());
    double doubleValue = Math.max(0.06, placementCandidate.faceEdgeMargin());
    return Math.max(
        0.35, Math.min(18.0, Math.toDegrees(2.0 * Math.atan2(doubleValue, currentLength))));
  }

  private static double calculateValue6(ScaffoldNextService.Step step, double doubleValue) {
    double currentDoubleValue = Math.min(0.075, step.angularSpeed() * 0.008);
    return Math.min(0.35, doubleValue + currentDoubleValue);
  }

  static double safeEmergencyFaceMargin(double doubleValue, double currentDoubleValue) {
    return Math.max(0.04, Math.min(doubleValue, currentDoubleValue));
  }

  static double safeEmergencyClickSpeed(double doubleValue, double currentDoubleValue) {
    return Math.min(doubleValue, currentDoubleValue);
  }

  private double calculateValue7(ScaffoldMotionPhaseData scaffoldMotionPhaseData) {
    double doubleValue = 0.88 + scaffoldMotionPhaseData.urgency() * 0.24;
    double currentDoubleValue =
        scaffoldMotionPhaseData.pathRelation().isPredictableChain()
            ? 1.08
            : (scaffoldMotionPhaseData.pathRelation()
                    == ScaffoldMotionPhaseData.PathRelation.RECOVERY
                ? 0.72
                : 1.0);
    return Math.max(
        0.1, ((Float) this.moduleNumber33.get()).floatValue() * doubleValue * currentDoubleValue);
  }

  private static double calculateValue8(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return Math.max(currentDoubleValue, Math.min(nextDoubleValue, doubleValue));
  }

  private record IssuedPlacementTrace(
      ScaffoldValidateBeforeMovementValidator.Arm arm, int sequence, double narrowestMargin) {}

  private record PlacementAttempt(
      long token, PlacementCandidate placement, int sequence, boolean towerPlacement) {}

  private record PreparedPlacementUse(
      ScaffoldValidateBeforeMovementValidator.Arm arm,
      PlacementCandidate placement,
      RotationVector clientPreUseEye,
      double requiredMargin,
      boolean towerPlacement) {}
}
