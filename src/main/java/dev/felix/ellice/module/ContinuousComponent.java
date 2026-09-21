package dev.felix.ellice.module;

import dev.felix.ellice.module.impl.AccentComponent;
import dev.felix.ellice.module.impl.AutoSoupModule;
import dev.felix.ellice.module.impl.BacktrackModule;
import dev.felix.ellice.module.impl.CapeComponent;
import dev.felix.ellice.module.impl.ColorComponent;
import dev.felix.ellice.module.impl.EffectsComponent;
import dev.felix.ellice.module.impl.EnabledComponent;
import dev.felix.ellice.module.impl.FullbrightModule;
import dev.felix.ellice.module.impl.ImplApplyZoomFovService;
import dev.felix.ellice.module.impl.ImplConfigureEffectsService;
import dev.felix.ellice.module.impl.ImplControllerService;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.ImplDetectionSettingsService;
import dev.felix.ellice.module.impl.ImplFireOverlayYService;
import dev.felix.ellice.module.impl.ImplHideVignetteService;
import dev.felix.ellice.module.impl.ImplHotbarLayoutService;
import dev.felix.ellice.module.impl.ImplRememberInteractionService;
import dev.felix.ellice.module.impl.ImplReplacesVanillaOutlineService;
import dev.felix.ellice.module.impl.ImplSpringFovActiveService;
import dev.felix.ellice.module.impl.ImplSuspendForPearlService;
import dev.felix.ellice.module.impl.NoFogModule;
import dev.felix.ellice.module.impl.PearlsComponent;
import dev.felix.ellice.module.impl.SmallTotemModule;
import dev.felix.ellice.module.impl.StrengthComponent;
import dev.felix.ellice.module.impl.TargetsComponent;
import dev.felix.ellice.module.impl.playerlookup.PlayerlookupApiService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class ContinuousComponent {
  private ContinuousComponent() {}

  static List<ModuleIdService> forModule(Module currentModule, Map<String, Object> entries) {
    if (entries.isEmpty()) {
      return List.of();
    }

    ContinuousComponent.Catalog catalog = new ContinuousComponent.Catalog(currentModule, entries);
    switch (currentModule) {
      case ImplSuspendForPearlService implSuspendForPearl:
        catalog
            .recommended(
                "Continuous",
                "Walks with a held bridge angle. Automatic jumps start after six placements and"
                    + " pause while sneaking.",
                "Bridge Mode",
                "Continuous",
                "Bridge Pitch",
                75.5F,
                "Jump Every Blocks",
                6.0F,
                "Movement Mode",
                "Follow Input",
                "Stable FOV",
                true,
                "Lock Bridge Rotation",
                true,
                "Sneak Mode",
                "Off",
                "Look Ahead",
                0.8F,
                "Peak Turn Speed",
                80.0F,
                "Motor Smoothness",
                0.3F,
                "Minimum Reaction Ticks",
                0.0F,
                "Maximum Reaction Ticks",
                1.0F,
                "Endpoint Dispersion",
                0.0F,
                "Minimum Face Margin",
                0.005F,
                "Maximum Click Speed",
                6.0F,
                "Use Interval Ticks",
                1.0F,
                "Wire Diagnostics",
                false)
            .option(
                "sneak-bridge",
                "Sneak Bridge",
                "Continuous bridging with automatic edge sneak and manual jumping. Distance, lead"
                    + " time and release delay remain adjustable.",
                "Auto Jump",
                false,
                "Sneak Mode",
                "Edge + Acquire",
                "Sneak Release Delay",
                2.0F)
            .option(
                "manual-jump",
                "Manual Jump",
                "Continuous bridging without automatic jumps. Hold sneak or press jump whenever you"
                    + " need to.",
                "Auto Jump",
                false)
            .option(
                "manual-aim",
                "Manual Aim",
                "Hold use and aim yourself. Places beneath your feet with your camera ray; jump and"
                    + " sneak remain manual.",
                "Aim Control",
                "Camera",
                "Activation",
                "Hold Use",
                "Auto Jump",
                false,
                "Auto Select Blocks",
                false,
                "Click Rhythm",
                "Held Use")
            .option(
                "silent-builder",
                "Silent Builder",
                "Keeps your view and hotbar slot, uses your preferred materials and leaves eight"
                    + " building blocks in reserve. Jump manually.",
                "Aim Control",
                "Silent",
                "Silent Hotbar",
                true,
                "Auto Jump",
                false,
                "Block Selection",
                "Preferred Blocks",
                "Block Reserve",
                8.0F,
                "Manual Override Ticks",
                6.0F,
                "Status Detail",
                "Detailed")
            .option(
                "selected-slot",
                "Fixed Slot",
                "Builds from hotbar slot nine with a visible switch. Other materials remain"
                    + " untouched; an empty slot waits for a refill.",
                "Silent Hotbar",
                false,
                "Block Selection",
                "Fixed Slot",
                "Building Slot",
                9.0F,
                "Auto Jump",
                false,
                "Stop When Empty",
                true)
            .option(
                "classic-sneak",
                "Classic Sneak",
                "Holds an 80-degree backward angle and sneaks throughout. Smooth acquisition,"
                    + " held-use timing and manual jumps.",
                "Bridge Pitch",
                80.0F,
                "Auto Pitch",
                false,
                "Sneak Mode",
                "Always",
                "Auto Jump",
                false,
                "Turn Style",
                "Smooth",
                "Peak Turn Speed",
                40.0F,
                "Motor Smoothness",
                0.45F,
                "Start Delay Ticks",
                1.0F,
                "Click Rhythm",
                "Held Use")
            .option(
                "diagonal-cruise",
                "Diagonal Cruise",
                "Acquires a diagonal backward angle once, with a 75.7-degree starting pitch and"
                    + " edge help at startup.",
                "Bridge Angle",
                "Auto Diagonal",
                "Bridge Pitch",
                75.7F,
                "Sneak Mode",
                "Start",
                "Jump Timing",
                "Edge Window",
                "Jump Every Blocks",
                5.0F)
            .option(
                "eagle",
                "Eagle",
                "Sneaks on alternating placement cycles. Uses smooth turns and leaves jumping to"
                    + " you.",
                "Sneak Mode",
                "Eagle",
                "Eagle Interval",
                1.0F,
                "Auto Jump",
                false,
                "Turn Style",
                "Smooth",
                "Motor Smoothness",
                0.35F)
            .option(
                "blatant",
                "Blatant",
                "Instant acquisition, attempts every tick and frequent automatic jumps. Disables"
                    + " the edge brake; uses ordinary walking physics.",
                "Peak Turn Speed",
                360.0F,
                "Maximum Click Speed",
                360.0F,
                "Minimum Face Margin",
                0.0F,
                "Jump Every Blocks",
                2.0F,
                "Safety Brake",
                false,
                "Look Ahead",
                1.2F,
                "Placement Foot Distance",
                1.0F,
                "Maximum Rescue Drop",
                3.0F,
                "Rescue Search Depth",
                6.0F)
            .option(
                "blatant-ground",
                "Blatant Ground",
                "Fast unrestricted turns and wider placement selection without automatic jumping or"
                    + " the edge brake.",
                "Peak Turn Speed",
                360.0F,
                "Maximum Click Speed",
                360.0F,
                "Minimum Face Margin",
                0.0F,
                "Auto Jump",
                false,
                "Safety Brake",
                false,
                "Lock Bridge Rotation",
                false,
                "Look Ahead",
                1.2F,
                "Placement Foot Distance",
                1.25F,
                "Maximum Rescue Drop",
                3.0F,
                "Rescue Search Depth",
                6.0F)
            .option(
                "fast-bridge",
                "Long Runs",
                "Keeps walking and waits for eight placements between automatic jumps.",
                "Jump Every Blocks",
                8.0F,
                "Look Ahead",
                1.0F,
                "Peak Turn Speed",
                110.0F,
                "Motor Smoothness",
                0.2F,
                "Maximum Reaction Ticks",
                0.0F,
                "Maximum Click Speed",
                8.0F,
                "Emergency Click Speed",
                8.0F,
                "Sneak Release Delay",
                1.0F)
            .option(
                "high-latency",
                "High Latency",
                "Waits longer for server confirmation and sneaks earlier. Trades bridge speed for"
                    + " more response time.",
                "Bridge Mode",
                "Adaptive",
                "Feedback Timeout Ticks",
                16.0F,
                "Reconciliation Grace Ticks",
                40.0F,
                "Sneak Mode",
                "Edge + Acquire",
                "Edge Sneak Distance",
                0.24F,
                "Sneak Lead Ticks",
                2.0F,
                "Sneak Release Delay",
                3.0F)
            .option(
                "telly",
                "Telly",
                "Sprints toward the edge, jumps forward and builds a connected flat landing path"
                    + " while airborne. Start with room for a short run-up.",
                "Bridge Mode",
                "Telly",
                "Auto Jump",
                true,
                "Turn Style",
                "Smooth",
                "Motor Smoothness",
                0.2F,
                "Telly Straight Ticks",
                1.0F,
                "Telly Turn Speed",
                100.0F,
                "Use Interval Ticks",
                1.0F)
            .option(
                "telly-manual",
                "Telly Manual Jump",
                "You sprint and jump; Telly handles the airborne turn and landing blocks. Automatic"
                    + " sprint is off.",
                "Bridge Mode",
                "Telly",
                "Auto Jump",
                false,
                "Telly Auto Sprint",
                false,
                "Turn Style",
                "Smooth",
                "Motor Smoothness",
                0.2F,
                "Use Interval Ticks",
                1.0F)
            .option(
                "tower",
                "Tower",
                "Hold jump without horizontal input to build upward. Requires a centered position"
                    + " with little sideways drift.",
                "Bridge Mode",
                "Continuous",
                "Tower Mode",
                "Hold Jump",
                "Tower Maximum Drift",
                0.03F,
                "Tower Foot Margin",
                0.12F,
                "Peak Turn Speed",
                100.0F,
                "Maximum Reaction Ticks",
                0.0F)
            .option(
                "low-cpu",
                "Low CPU",
                "Reduces face sampling and mouse-grid search. Best for simple full-block bridges;"
                    + " narrow faces may take longer.",
                "Bridge Mode",
                "Adaptive",
                "Face Search Grid",
                3.0F,
                "GCD Search Radius",
                1.0F,
                "Face Raycast Budget",
                96.0F);
        break;
      case ImplDetectionSettingsService implDetectionSettings:
        catalog
            .recommended(
                "Balanced",
                "Requires corroborating evidence and sustained confirmation. Temporarily holds"
                    + " suspicious new targets while their profiles arrive.")
            .option(
                "conservative",
                "Conservative",
                "Longer observation and stronger evidence reduce false positives on unusual"
                    + " servers.",
                "Evidence Threshold",
                80.0F,
                "Spawn Grace (ms)",
                1000.0F,
                "Confirmation (ms)",
                500.0F,
                "Release Delay (ms)",
                1250.0F,
                "Hold Suspicious Spawns",
                false)
            .option(
                "strict",
                "Strict",
                "Filters on a sustained strong clue sooner. Can exclude custom server players;"
                    + " inspect decisions and allow exceptions.",
                "Evidence Threshold",
                45.0F,
                "Minimum Evidence Families",
                1.0F,
                "Spawn Grace (ms)",
                200.0F,
                "Confirmation (ms)",
                100.0F,
                "Release Delay (ms)",
                400.0F)
            .option(
                "observe",
                "Observe",
                "Balanced detection with no target filtering. Aim at players to inspect evidence"
                    + " before choosing a filter policy.",
                "Mode",
                "Observe",
                "Status Display",
                "Inspect");
        break;
      case ImplDecisionTracker implDecisionTracker:
        catalog
            .recommended(
                "Balanced",
                "Stable cooldown-based attacks with prompt turns and no artificial aim noise."
                    + " Searches nearby players and mobs.",
                "Range",
                4.5F,
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.25F,
                "Jitter Intensity",
                0.0F,
                "Overshoot 2-5°",
                false,
                "Reaction Delay 1-4 Ticks",
                false,
                "GCD Snap Variation",
                false,
                "Micro Hesitation",
                false,
                "Click Speed (CPS)",
                new ModuleSetting.RangeValue(6.0F, 9.0F),
                "Target Priority",
                "Smart",
                "Keep Combo Target",
                true,
                "Combo Timing",
                true,
                "Sprint Reset",
                true,
                "Wait for Cooldown",
                true,
                "Wait for Hurt Time",
                true,
                "Weapons Only",
                false,
                "Aim Point",
                "Auto",
                "Hit Chance (%)",
                100.0F,
                "Switch Delay (ticks)",
                0.0F)
            .option(
                "combos",
                "Combos",
                "Keeps one reachable player as the combo target, turns promptly and uses"
                    + " full-cooldown follow-ups with a grounded sprint reset.",
                "Players",
                true,
                "Mobs",
                false,
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.18F,
                "Click Speed (CPS)",
                new ModuleSetting.RangeValue(8.0F, 10.0F),
                "Smart Clicks",
                false,
                "Wait for Hurt Time",
                false,
                "Keep Combo Target",
                true,
                "Sprint Reset (ticks)",
                1.0F)
            .option(
                "responsive",
                "Responsive",
                "Turns faster and attacks as soon as a valid ray and full weapon cooldown permit."
                    + " Removes extra waiting.",
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.08F,
                "Smart Clicks",
                false,
                "Wait for Hurt Time",
                false,
                "Extra Aim Ticks",
                0.0F,
                "Switch Delay (ticks)",
                0.0F,
                "Pitch Speed (%)",
                100.0F)
            .option(
                "players",
                "Players",
                "Player-only PvP tracking with combo timing, AutoBlock and a grounded sprint"
                    + " reset.",
                "Players",
                true,
                "Mobs",
                false,
                "AutoBlock",
                true,
                "Keep Combo Target",
                true,
                "Target Priority",
                "Smart",
                "Sprint Reset",
                true)
            .option(
                "mobs",
                "Mobs",
                "Faster mob farming. Players, shields and sprint resets stay out of the way.",
                "Players",
                false,
                "Mobs",
                true,
                "AutoBlock",
                false,
                "Sprint Reset",
                false,
                "Keep Combo Target",
                false,
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.12F,
                "Smart Clicks",
                false,
                "Wait for Hurt Time",
                false,
                "FOV",
                "360°")
            .option(
                "smooth",
                "Smooth",
                "Slower first-person turns with a short acquisition delay. Prioritizes gradual"
                    + " camera motion over immediate target changes.",
                "Rotation Mode",
                "First Person",
                "Camera Smoothness",
                0.62F,
                "Camera Speed",
                28.0F,
                "Turn Speed",
                48.0F,
                "Smoothness",
                0.58F,
                "Reaction Delay 1-4 Ticks",
                true,
                "Pitch Speed (%)",
                72.0F,
                "Return Speed",
                40.0F,
                "Jitter Intensity",
                20.0F)
            .option(
                "legit",
                "Legit",
                "Narrower FOV, visible first-person aim, weapons only, slower humanized turns and a"
                    + " short switch delay.",
                "Players",
                true,
                "Mobs",
                false,
                "Range",
                3.2F,
                "Attack Range",
                3.0F,
                "FOV",
                "Custom",
                "FOV Degrees",
                110.0F,
                "Turn Speed",
                48.0F,
                "Smoothness",
                0.62F,
                "Jitter Intensity",
                35.0F,
                "Overshoot 2-5°",
                true,
                "Reaction Delay 1-4 Ticks",
                true,
                "GCD Snap Variation",
                true,
                "Micro Hesitation",
                true,
                "Overshoot Degrees",
                new ModuleSetting.RangeValue(2.0F, 4.0F),
                "Reaction Delay (ticks)",
                new ModuleSetting.RangeValue(2.0F, 4.0F),
                "Click Speed (CPS)",
                new ModuleSetting.RangeValue(4.0F, 7.0F),
                "Smart Clicks",
                true,
                "Wait for Hurt Time",
                true,
                "Weapons Only",
                true,
                "Switch Delay (ticks)",
                2.0F,
                "Rotation Mode",
                "First Person",
                "Camera Smoothness",
                0.7F,
                "Camera Speed",
                24.0F,
                "Pitch Speed (%)",
                72.0F,
                "Aim Point",
                "Chest",
                "Combat AI",
                false)
            .option(
                "rage",
                "Rage",
                "Instant wide-FOV tracking with no motor noise. Still requires a real ray, reach"
                    + " and the click ceiling.",
                "Range",
                6.0F,
                "Attack Range",
                3.0F,
                "FOV",
                "360°",
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.0F,
                "Jitter Intensity",
                0.0F,
                "Overshoot 2-5°",
                false,
                "Reaction Delay 1-4 Ticks",
                false,
                "GCD Snap Variation",
                false,
                "Micro Hesitation",
                false,
                "Smart Clicks",
                false,
                "Wait for Hurt Time",
                false,
                "Combo Timing",
                true,
                "Click Speed (CPS)",
                new ModuleSetting.RangeValue(12.0F, 16.0F),
                "Switch Delay (ticks)",
                0.0F,
                "Pitch Speed (%)",
                100.0F,
                "Aim Point",
                "Auto",
                "Weapons Only",
                false)
            .option(
                "pressure-ai",
                "Pressure AI",
                "Autonomous Pressure movement with strafing and player-only combos. S, Shift or"
                    + " Space still take over.",
                "Players",
                true,
                "Mobs",
                false,
                "Combat AI",
                true,
                "Fighting Style",
                "Pressure",
                "Adaptive Strafing",
                true,
                "Pattern Learning",
                true,
                "Preferred Distance",
                2.2F,
                "Pursuit Range",
                5.5F,
                "Movement Correction",
                true,
                "Keep Combo Target",
                true,
                "Sprint Reset",
                true,
                "Turn Speed",
                90.0F,
                "Smoothness",
                0.2F)
            .option(
                "defensive-ai",
                "Defensive AI",
                "Creates space earlier, keeps AutoBlock and prefers a slightly safer distance.",
                "Players",
                true,
                "Mobs",
                false,
                "Combat AI",
                true,
                "Fighting Style",
                "Defensive",
                "Adaptive Strafing",
                true,
                "Preferred Distance",
                2.8F,
                "Pursuit Range",
                5.0F,
                "AutoBlock",
                true,
                "Block Reaction (ticks)",
                1.0F,
                "Movement Correction",
                true,
                "Turn Speed",
                85.0F,
                "Smoothness",
                0.28F)
            .option(
                "high-latency",
                "High Latency",
                "Longer correction recovery, switch delay and Smart Clicks. Trades speed for more"
                    + " time after lag spikes.",
                "Correction Recovery (ticks)",
                8.0F,
                "Switch Delay (ticks)",
                3.0F,
                "Extra Aim Ticks",
                1.0F,
                "Reaction Delay 1-4 Ticks",
                true,
                "Smart Clicks",
                true,
                "Wait for Hurt Time",
                true,
                "Click Speed (CPS)",
                new ModuleSetting.RangeValue(5.0F, 7.0F),
                "Turn Speed",
                70.0F,
                "Smoothness",
                0.4F,
                "Target Loss (ticks)",
                new ModuleSetting.RangeValue(3.0F, 6.0F));
        break;
      case BacktrackModule backtrackModule:
        catalog
            .recommended(
                "Balanced",
                "Follows your last attacked opponent with a short retreat-only window and a"
                    + " received-position outline.")
            .option(
                "subtle",
                "Subtle",
                "Keeps the delay short and gives each window more recovery time.",
                "Delay (ms)",
                new ModuleSetting.RangeValue(50.0F, 80.0F),
                "Window Interval (ms)",
                200.0F)
            .option(
                "combo",
                "Combo",
                "Uses a slightly longer window while the last attacked opponent retreats.",
                "Delay (ms)",
                new ModuleSetting.RangeValue(150.0F, 200.0F),
                "Window Interval (ms)",
                75.0F,
                "Attack Window (ms)",
                1500.0F)
            .option(
                "aura",
                "Aura Assist",
                "Follows KillAura's selected opponent; all normal packet and reset boundaries still"
                    + " apply.",
                "Target Mode",
                "KillAura",
                "Delay (ms)",
                new ModuleSetting.RangeValue(80.0F, 120.0F))
            .option(
                "proximity",
                "Proximity",
                "Tracks the nearest eligible opponent without requiring an attack first.",
                "Target Mode",
                "Nearest",
                "Delay (ms)",
                new ModuleSetting.RangeValue(80.0F, 120.0F))
            .option(
                "mobs",
                "Mobs",
                "Follows manually attacked mobs, with players excluded.",
                "Players",
                false,
                "Mobs",
                true);
        break;
      case AutoSoupModule autoSoupModule:
        catalog
            .recommended(
                "Soup PvP",
                "Uses one healing soup at a time, restores the selected slot and refills through"
                    + " the real inventory.")
            .option(
                "vanilla",
                "Vanilla Food",
                "Uses stew for hunger with normal eating duration; does not expect server healing.",
                "Soup mode",
                "Vanilla",
                "Inventory access",
                "Only while open",
                "Use below (food)",
                14.0F)
            .option(
                "manual-inventory",
                "Manual Inventory",
                "Maintains supplies only while you have opened the inventory.",
                "Inventory access",
                "Only while open",
                "Action interval (ms)",
                150.0F);
        break;
      case ImplHotbarLayoutService implHotbarLayout:
        catalog
            .recommended(
                "Balanced",
                "Equips upgrades, organizes the hotbar and removes junk and inferior equipment when"
                    + " inventory is open. Keeps your personal slot layout.")
            .option(
                "armor",
                "Armor Only",
                "Equips better armor without sorting or discarding items.",
                "Organize hotbar",
                false,
                "Drop junk",
                false,
                "Drop inferior equipment",
                false)
            .option(
                "careful",
                "Durable Gear",
                "Rejects worn replacements and gives inventory actions more time.",
                "Minimum durability (%)",
                25.0F,
                "Action delay (ms)",
                new ModuleSetting.RangeValue(220.0F, 350.0F));
        break;
      case ImplRememberInteractionService implRememberInteraction:
        catalog
            .recommended(
                "Useful Supplies",
                "Collects upgrades and useful stock with normal paced inventory actions.",
                "Keep free slots",
                2.0F)
            .option(
                "building",
                "Building",
                "Carries more blocks and food while preserving equipment selection and personal"
                    + " exclusions.",
                "Block target",
                384.0F,
                "Food target",
                48.0F,
                "Keep free slots",
                1.0F)
            .option(
                "manual",
                "Hold to Collect",
                "Requires holding your collection shortcut and leaves the container open"
                    + " afterward.",
                "Hold to collect",
                true,
                "Close when done",
                false);
        break;
      case ImplReplacesVanillaOutlineService implReplacesVanillaOutline:
        catalog
            .recommended("Soft Glass", "Theme-matched glass faces and a fine, softly lit contour.")
            .option(
                "filled",
                "ellice",
                "Slow two-tone light flowing across the exact selected shape.",
                "Material",
                "ellice",
                "Edge Glow",
                0.6F)
            .option(
                "clean",
                "Clean",
                "A crisp outline with no face fill or glow.",
                "Material",
                "Clean",
                "Fill",
                false,
                "Line Width",
                1.2F)
            .option(
                "bold",
                "High Contrast",
                "A stronger contour for busy textures, with restrained face shading.",
                "Line Width",
                2.5F,
                "Edge Glow",
                0.2F);
        break;
      case ImplHideVignetteService implHideVignette:
        catalog
            .recommended(
                "Clear View",
                "Removes common visual obstructions while keeping powder-snow and spyglass cues.")
            .option(
                "minimal",
                "Minimal Overlays",
                "Also removes powder-snow and spyglass overlays for the clearest view.",
                "Powder Snow",
                true,
                "Spyglass Scope",
                true);
        break;
      case CapeComponent capeComponent:
        catalog
            .recommended(
                "Personal Cape", "Renders your own cape with moderate motion and shader energy.")
            .option(
                "lightweight",
                "Lightweight",
                "Reduces effect energy and render distance for a quieter personal cape.",
                "Shader Energy",
                0.2F,
                "Render Range",
                32.0F,
                "Motion Response",
                0.45F)
            .option(
                "showcase",
                "Showcase",
                "Adds nearby players' capes and a brighter Aurora material; costs more rendering"
                    + " work.",
                "Other Players",
                true,
                "Material",
                "Aurora",
                "Shader Energy",
                0.8F,
                "Opacity",
                0.9F);
        break;
      case FullbrightModule fullbrightModule:
        catalog
            .recommended(
                "Readable",
                "Brightens dark areas while retaining a little lighting contrast.",
                "Strength",
                0.8F)
            .option(
                "full",
                "Full Brightness",
                "Maximum lightmap brightness and darkness reduction.",
                "Strength",
                1.0F)
            .option(
                "soft",
                "Soft",
                "A smaller lighting lift for a more atmospheric world.",
                "Strength",
                0.45F);
        break;
      case ImplSpringFovActiveService implSpringFovActive:
        catalog
            .recommended(
                "Calm",
                "Keeps gentle FOV and sneak transitions with no camera roll, damage punch or red"
                    + " flash.",
                "FOV Feel",
                "Smooth",
                "Hit Punch",
                false,
                "Damage Flash",
                false,
                "Camera Lean",
                false,
                "Item Swap Smoothing",
                false)
            .option(
                "instant",
                "Instant",
                "Uses immediate Vanilla FOV, crouch and equip motion.",
                "Spring FOV",
                false,
                "Smooth Sneak",
                false);
        break;
      case ImplFireOverlayYService implFireOverlayY:
        catalog
            .recommended(
                "Clear View",
                "Low, lightly transparent flames leave the center of the screen readable.",
                "Height",
                0.2F,
                "Opacity",
                0.55F)
            .option(
                "hidden",
                "Hidden Flames",
                "Hides the first-person fire overlay.",
                "Height",
                0.0F,
                "Opacity",
                0.0F);
        break;
      case StrengthComponent strengthComponent:
        catalog
            .recommended(
                "Subtle",
                "A light history blend with short trails keeps movement readable.",
                "Strength",
                0.18F,
                "Persistence",
                0.35F)
            .option(
                "cinematic",
                "Cinematic",
                "A stronger, longer blend for smooth-looking camera movement.",
                "Strength",
                0.45F,
                "Persistence",
                0.6F)
            .option(
                "trails",
                "Trails",
                "An intentionally visible trail effect for screenshots and visual experiments.",
                "Mode",
                "Trail",
                "Strength",
                0.6F,
                "Persistence",
                0.8F);
        break;
      case TargetsComponent targetsComponent:
        catalog
            .recommended(
                "Balanced",
                "Compact labels for players, valuable drops and named entities with overlap"
                    + " avoidance.")
            .option(
                "combat",
                "Combat",
                "Player-only labels with health and distance; fewer labels keep fights readable.",
                "Dropped Items",
                false,
                "Named Entities",
                false,
                "Health Bar",
                true,
                "Distance",
                true,
                "Max Tags",
                16.0F)
            .option(
                "lightweight",
                "Lightweight",
                "Limits label count and range, and removes fade animations.",
                "Range",
                32.0F,
                "Max Tags",
                12.0F,
                "Animations",
                false);
        break;
      case NoFogModule noFogModule:
        catalog
            .recommended("Clear", "Removes terrain, water and lava fog.")
            .option(
                "liquid-depth",
                "Keep Liquid Fog",
                "Clears terrain fog while preserving underwater and lava depth cues.",
                "Affect Liquids",
                false);
        break;
      case AccentComponent accentComponent:
        catalog
            .recommended("Balanced", "ellice's default accent with a moderate backdrop blur.")
            .option(
                "sharp",
                "Sharp",
                "Opaque panels without backdrop blur reduce overlay rendering work.",
                "Blur",
                0.0F,
                "Surface",
                -267580136)
            .option(
                "glass",
                "Glass",
                "Stronger blur and translucent surfaces emphasize the glass treatment.",
                "Blur",
                24.0F,
                "Surface",
                1343032600);
        break;
      case EffectsComponent effectsComponent:
        catalog
            .recommended(
                "Compact Glow",
                "Four tightly layered glow bands with a bright, clear silhouette.",
                "2D Boxes",
                false,
                "Glow",
                true,
                "Fire",
                false,
                "Outline",
                false,
                "Interference",
                false,
                "Kawase Bloom",
                false,
                "ellice",
                false,
                "Glow Radius",
                6.0F,
                "Glow Intensity",
                1.35F,
                "Glow Highlights",
                0.65F)
            .option(
                "classic",
                "Classic Boxes",
                "Fine projected boxes, names and living-entity health.",
                "Glow",
                false,
                "2D Boxes",
                true)
            .option(
                "outline",
                "Fine Outline",
                "A thin, softened outline around the actual model.",
                "Glow",
                false,
                "Outline",
                true,
                "Outline Thickness",
                1.5F)
            .option(
                "fire",
                "Fire",
                "Animated contour flames with moderate bloom and heat distortion.",
                "Glow",
                false,
                "Fire",
                true,
                "Intensity",
                1.2F,
                "Bloom Strength",
                0.8F,
                "Blur Radius",
                16.0F,
                "Backdrop Blur",
                0.35F,
                "Heat Distortion",
                2.0F)
            .option(
                "glow",
                "Soft Kawase",
                "Wide, soft contour bloom.",
                "Glow",
                false,
                "Kawase Bloom",
                true,
                "Kawase Radius",
                16.0F,
                "Kawase Levels",
                3.0F)
            .option(
                "interference",
                "Interference",
                "Complex phase vortices weave cyan and violet filaments.",
                "Glow",
                false,
                "Interference",
                true)
            .option(
                "ellice",
                "ellice",
                "Faceted, iridescent contour light with moving highlights.",
                "Glow",
                false,
                "ellice",
                true);
        break;
      case ColorComponent colorComponent:
        catalog
            .recommended(
                "Subtle",
                "A smaller heart with restrained glow and slow pulsing.",
                "Size",
                0.7F,
                "Glow Strength",
                1.2F,
                "Blur Radius",
                16.0F,
                "Pulse Speed",
                0.5F)
            .option(
                "static",
                "Static",
                "Stops pulsing and removes glow blur for a simple material preview.",
                "Glow Strength",
                0.0F,
                "Blur Radius",
                0.0F,
                "Pulse Speed",
                0.0F);
        break;
      case PearlsComponent pearlsComponent:
        catalog
            .recommended(
                "Balanced",
                "Predicts bows and pearls with movement compensation, impact information and a"
                    + " bounded trajectory.")
            .option(
                "lightweight",
                "Lightweight",
                "Shorter trajectories and fewer target details reduce per-frame prediction work.",
                "Max Ticks",
                90.0F,
                "Target Range",
                32.0F,
                "Target Intel",
                false,
                "Impact Card",
                false)
            .option(
                "detailed",
                "Detailed",
                "Extends the trajectory and target search for longer shots; needs more raycasts.",
                "Max Ticks",
                220.0F,
                "Target Range",
                96.0F,
                "Line Width",
                2.5F);
        break;
      case SmallTotemModule smallTotemModule:
        catalog
            .recommended(
                "Compact", "A small but visible totem activation animation.", "Scale", 0.3F)
            .option("tiny", "Tiny", "Keeps the activation cue very small.", "Scale", 0.18F);
        break;
      case ImplControllerService implController:
        catalog
            .recommended(
                "Balanced",
                "A medium minimap with waypoints, navigation and automatic death markers.")
            .option(
                "lightweight",
                "Lightweight",
                "Samples fewer loaded chunks and uses a smaller minimap.",
                "Minimap Size",
                136.0F,
                "Map Radius",
                6.0F,
                "World Navigation Line",
                false,
                "Show Players",
                false)
            .option(
                "explorer",
                "Explorer",
                "A larger minimap covering more loaded chunks; uses more memory.",
                "Minimap Size",
                216.0F,
                "Map Radius",
                20.0F);
        break;
      case ImplConfigureEffectsService implConfigureEffects:
        catalog
            .recommended(
                "Natural",
                "A light color grade with no chromatic fringing and restrained vignette.",
                "Intensity",
                0.35F,
                "Contrast",
                1.0F,
                "Saturation",
                1.02F,
                "Vignette",
                0.1F,
                "Chromatic",
                0.0F,
                "Film Grain",
                0.0F)
            .option(
                "cinematic",
                "Cinematic",
                "Teal shadows, warm highlights, soft bloom and a touch of film halation.",
                "Intensity",
                0.75F,
                "Contrast",
                1.1F,
                "Saturation",
                1.05F,
                "Vignette",
                0.2F,
                "Chromatic",
                5.0E-4F,
                "Depth Fog",
                0.1F,
                "Bloom",
                0.22F,
                "Halation",
                0.14F,
                "Film Grain",
                0.008F)
            .option(
                "golden-hour",
                "Golden Hour",
                "Honey-colored light, glowing highlights and a warm decorative sunset sky.",
                "Preset",
                "Golden Hour",
                "Intensity",
                0.85F,
                "Exposure",
                0.1F,
                "Saturation",
                1.05F,
                "Bloom",
                0.28F,
                "Bloom Radius",
                12.0F,
                "Halation",
                0.24F,
                "Depth Fog",
                0.18F,
                "Sky Effect",
                "Sunset",
                "Sky Strength",
                0.42F,
                "Vignette",
                0.16F)
            .option(
                "sakura",
                "Sakura",
                "Soft pink pastels, lifted shadows, rose haze and gentle floating lights.",
                "Preset",
                "Sakura",
                "Intensity",
                0.8F,
                "Contrast",
                0.94F,
                "Saturation",
                0.95F,
                "Bloom",
                0.32F,
                "Bloom Radius",
                14.0F,
                "Depth Fog",
                0.18F,
                "Light Motes",
                0.45F,
                "Mote Color",
                -17704,
                "Atmosphere Speed",
                0.2F)
            .option(
                "nordic",
                "Nordic",
                "Quiet blue-green shadows and restrained saturation for clean landscape shots.",
                "Preset",
                "Nordic",
                "Intensity",
                0.8F,
                "Contrast",
                1.06F,
                "Saturation",
                0.92F,
                "Depth Fog",
                0.2F,
                "Fog Distance",
                160.0F,
                "Bloom",
                0.1F)
            .option(
                "emerald-grove",
                "Emerald Grove",
                "Warm forest greens, low mist and drifting firefly lights. Adjust Mist Height to"
                    + " the terrain.",
                "Preset",
                "Emerald",
                "Intensity",
                0.85F,
                "Saturation",
                1.08F,
                "Bloom",
                0.2F,
                "Depth Fog",
                0.18F,
                "Height Mist",
                0.4F,
                "Mist Height",
                72.0F,
                "Light Motes",
                0.8F,
                "Mote Color",
                -2883648,
                "Atmosphere Speed",
                0.3F)
            .option(
                "aurora-night",
                "Aurora Night",
                "Moon-blue tones under moving green-violet aurora ribbons and a twinkling star"
                    + " field.",
                "Preset",
                "Moonlight",
                "Intensity",
                0.95F,
                "Exposure",
                -0.25F,
                "Contrast",
                1.08F,
                "Sky Effect",
                "Aurora",
                "Sky Strength",
                0.92F,
                "Stars",
                0.8F,
                "Bloom",
                0.28F,
                "Depth Fog",
                0.18F,
                "Vignette",
                0.24F,
                "Light Motes",
                0.25F,
                "Mote Color",
                -5709825)
            .option(
                "cosmic",
                "Cosmic",
                "A layered violet-teal nebula sky, tiny stars and luminous highlights.",
                "Preset",
                "Vaporwave",
                "Intensity",
                0.9F,
                "Exposure",
                -0.15F,
                "Saturation",
                1.05F,
                "Sky Effect",
                "Nebula",
                "Sky Strength",
                0.92F,
                "Stars",
                1.0F,
                "Bloom",
                0.35F,
                "Bloom Radius",
                12.0F,
                "Depth Fog",
                0.16F,
                "Atmosphere Speed",
                0.2F)
            .option(
                "dream",
                "Dream",
                "Creamy lifted tones, broad soft bloom and slow champagne-colored light motes.",
                "Preset",
                "Dream",
                "Intensity",
                0.85F,
                "Contrast",
                0.94F,
                "Saturation",
                0.94F,
                "Bloom",
                0.42F,
                "Bloom Radius",
                18.0F,
                "Bloom Threshold",
                0.58F,
                "Halation",
                0.1F,
                "Depth Fog",
                0.12F,
                "Light Motes",
                0.35F,
                "Atmosphere Speed",
                0.15F)
            .option(
                "neon",
                "Neon",
                "Violet shadows and cyan highlights with luminous bloom and a subtle chromatic"
                    + " edge.",
                "Preset",
                "Neon",
                "Intensity",
                0.8F,
                "Saturation",
                1.2F,
                "Bloom",
                0.38F,
                "Bloom Threshold",
                0.6F,
                "Chromatic",
                0.001F)
            .option(
                "amber-film",
                "Amber Film",
                "Warm faded film, fine grain and orange halation around bright light.",
                "Preset",
                "Amber Film",
                "Intensity",
                0.9F,
                "Contrast",
                1.06F,
                "Saturation",
                0.9F,
                "Bloom",
                0.18F,
                "Halation",
                0.4F,
                "Film Grain",
                0.026F,
                "Vignette",
                0.26F)
            .option(
                "arctic",
                "Arctic",
                "Icy cyan light and pale distant haze with crisp, clean highlights.",
                "Preset",
                "Arctic",
                "Intensity",
                0.8F,
                "Exposure",
                0.1F,
                "Saturation",
                0.92F,
                "Depth Fog",
                0.28F,
                "Fog Distance",
                144.0F,
                "Bloom",
                0.18F)
            .option(
                "noir-film",
                "Noir Film",
                "Silver monochrome, deep contrast, fine film grain and a soft highlight glow.",
                "Preset",
                "Noir",
                "Intensity",
                1.0F,
                "Contrast",
                1.16F,
                "Saturation",
                0.0F,
                "Bloom",
                0.16F,
                "Film Grain",
                0.024F,
                "Vignette",
                0.32F);
        break;
      case ImplApplyZoomFovService implApplyZoomFov:
        catalog
            .recommended(
                "Balanced",
                "Useful magnification with a quick, smooth transition. Keeps your zoom key.")
            .option(
                "precision",
                "Precision",
                "Stronger magnification with a slightly slower transition for distant details.",
                "Zoom Factor",
                0.15F,
                "Ease Speed",
                10.0F)
            .option(
                "instant",
                "Instant",
                "Immediate moderate magnification with no easing.",
                "Zoom Factor",
                0.35F,
                "Smooth",
                false);
        break;
      case PlayerlookupApiService playerlookupApi:
        catalog
            .recommended(
                "Manual Copy",
                "Remembers your query and leaves clipboard copying under your control.")
            .option(
                "quick-copy",
                "Quick Copy",
                "Copies the UUID after each successful lookup.",
                "Auto-copy UUID",
                true);
        break;
      case EnabledComponent enabledComponent:
        catalog.recommended(
            "Control Gallery", "Restores the sample controls for UI previews and testing.");
        break;
      default:
        catalog.recommended(
            "Recommended",
            "Restores this module's initial tuning while keeping shortcuts and user-entered text.");
    }

    return catalog.finish();
  }

  private static final class Catalog {
    private final Module moduleOperationHandler2;
    private final Map<String, Object> text2;
    private Map<String, Object> text3;
    private final List<ModuleIdService> items2 = new ArrayList<>();

    Catalog(Module module, Map<String, Object> entries) {
      this.moduleOperationHandler2 = module;
      this.text2 = entries;
      this.text3 = entries;
    }

    ContinuousComponent.Catalog recommended(String text, String currentText, Object... objects) {
      ModuleIdService moduleId =
          this.createModuleIdService("recommended", text, currentText, true, objects);
      this.items2.add(moduleId);
      this.text3 = moduleId.values();
      return this;
    }

    ContinuousComponent.Catalog option(
        String text, String currentText, String nextText, Object... objects) {
      this.items2.add(this.createModuleIdService(text, currentText, nextText, false, objects));
      return this;
    }

    private ModuleIdService createModuleIdService(
        String id, String currentId, String nextId, boolean enabled, Object... objects) {
      if (objects.length % 2 != 0) {
        throw new IllegalArgumentException("Expected setting/value pairs");
      }

      LinkedHashMap linkedHashMap = new LinkedHashMap<>(this.text3);

      for (byte index = 0; index < objects.length; index += 2) {
        linkedHashMap.put((String) objects[index], objects[index + 1]);
      }

      return new ModuleIdService(
          this.moduleOperationHandler2, id, currentId, nextId, enabled, linkedHashMap);
    }

    List<ModuleIdService> finish() {
      if (this.items2.stream().noneMatch(item -> item.values().equals(this.text2))) {
        this.items2.add(
            new ModuleIdService(
                this.moduleOperationHandler2,
                "defaults",
                "Defaults",
                "Restores the original module tuning. Keeps your shortcuts and text.",
                false,
                this.text2));
      }

      return List.copyOf(this.items2);
    }
  }
}
