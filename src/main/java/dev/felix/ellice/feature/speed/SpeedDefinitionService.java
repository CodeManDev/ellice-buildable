package dev.felix.ellice.feature.speed;

import dev.felix.ellice.module.ModuleEntriesService;
import java.util.ArrayList;
import java.util.List;

public final class SpeedDefinitionService {
   public static final List<SpeedDefinitionService.Definition> ALL = collectValues();

   private SpeedDefinitionService() {
   }

   public static SpeedDefinitionService.Definition get(String text) {
      return ALL.stream().filter(item -> item.id().equals(text)).findFirst().orElseThrow(() -> new IllegalArgumentException(text));
   }

   private static List<SpeedDefinitionService.Definition> collectValues() {
      ArrayList arrayList = new ArrayList();
      updateState(arrayList, "LegitHop", "General", "Automatic vanilla jumps while moving.");
      updateState(
         arrayList,
         "Custom",
         "General",
         "Adjust acceleration, jump height, strafe, knockback and timer.",
         SpeedDefinitionService.Option.flag("Horizontal modification", true),
         SpeedDefinitionService.Option.num(
            "Horizontal acceleration", 0.0, -0.1, 0.2
         ),
         SpeedDefinitionService.Option.num("Jump off multiplier", 0.0, -0.5, 1.0),
         SpeedDefinitionService.Option.count("Boost delay", 0, 0, 20),
         SpeedDefinitionService.Option.flag("Vertical modification", true),
         SpeedDefinitionService.Option.num(
            "Jump height", 0.42, 0.0, 3.0
         ),
         SpeedDefinitionService.Option.num("Pull down", 0.0, 0.0, 1.0),
         SpeedDefinitionService.Option.num("Fall pull down", 0.0, 0.0, 1.0),
         SpeedDefinitionService.Option.flag("Strafe", true),
         SpeedDefinitionService.Option.num("Strafe strength", 1.0, 0.1, 1.0),
         SpeedDefinitionService.Option.flag("Custom speed", false),
         SpeedDefinitionService.Option.num("Speed", 1.0, 0.1, 10.0),
         SpeedDefinitionService.Option.count("Velocity timeout", 0, 0, 20),
         SpeedDefinitionService.Option.flag("Strafe knockback", false),
         SpeedDefinitionService.Option.num(
            "Timer speed", 1.0, 0.1, 10.0
         )
      );
      updateState(
         arrayList,
         "YPort",
         "General",
         "Jump, strafe, then pull down in the air.",
         SpeedDefinitionService.Option.num("Speed", 0.4, 0.1, 1.0)
      );
      updateState(
         arrayList,
         "PiercingAttack",
         "General",
         "Select a Lunge spear and perform piercing attacks. Requires Minecraft 1.21.11+.",
         SpeedDefinitionService.Option.choice("Swing", "Visible", "Visible", "Packet", "Hidden"),
         SpeedDefinitionService.Option.count("Hold minimum", 1, 1, 20),
         SpeedDefinitionService.Option.count("Hold maximum", 1, 1, 20),
         SpeedDefinitionService.Option.flag("On ground", true),
         SpeedDefinitionService.Option.flag("Ignore hunger", false),
         SpeedDefinitionService.Option.flag("Wait for cooldown", true),
         SpeedDefinitionService.Option.count("Minimum durability", 1, 0, 20)
      );
      updateState(arrayList, "VerusB3882", "Verus", "Jump boost, full movement strafe and periodic timer pulses.");
      updateState(
         arrayList,
         "HypixelBHop",
         "Watchdog",
         "Small horizontal/fall acceleration and knockback strafe.",
         SpeedDefinitionService.Option.flag("Horizontal acceleration", true),
         SpeedDefinitionService.Option.flag("Vertical acceleration", true)
      );
      updateState(
         arrayList, "HypixelLowHop", "Watchdog", "Air-tick low hops with ground proximity strafe.", SpeedDefinitionService.Option.flag("Glide", false)
      );
      updateState(arrayList, "Spartan-4.0.4.3", "Spartan", "Ground move boost and repeated sprint jumps; leather boots affect the boost.");
      updateState(arrayList, "Spartan-4.0.4.3-FastFall", "Spartan", "Repeated jumps followed by a ground packet and downward move.");
      updateState(
         arrayList,
         "SentinelDamage",
         "Sentinel",
         "Periodic fall-damage packets and movement after damage.",
         SpeedDefinitionService.Option.num(
            "Speed",
            0.5,
            0.1,
            5.0
         ),
         SpeedDefinitionService.Option.count("Reboost ticks", 30, 10, 50),
         SpeedDefinitionService.Option.count("Ping delay", 500, 0, 25000)
      );
      updateState(arrayList, "Vulcan286", "Vulcan", "Six-tick strafe and fast-fall sequence after jumping.");
      updateState(arrayList, "Vulcan288", "Vulcan", "Potion-dependent four-tick speed sequence and ground packets while descending.");
      updateState(arrayList, "VulcanGround286", "Vulcan", "Small ground hops with a position packet offset.");
      updateState(
         arrayList,
         "GrimCollide",
         "Grim",
         "Add forward motion for nearby living entities.",
         SpeedDefinitionService.Option.num(
            "Boost speed",
            0.08,
            0.01,
            0.08
         ),
         SpeedDefinitionService.Option.num(
            "Shrink box",
            0.5,
            0.1,
            2.0
         )
      );
      updateState(
         arrayList,
         "NCP",
         "NCP",
         "Configurable low hops, pull down, air strafe, damage and timer boost.",
         SpeedDefinitionService.Option.flag("Pull down", true),
         SpeedDefinitionService.Option.num(
            "Motion multiplier", 1.0, 0.01, 10.0
         ),
         SpeedDefinitionService.Option.count("On tick", 5, 1, 9),
         SpeedDefinitionService.Option.flag("On hurt", true),
         SpeedDefinitionService.Option.flag("Boost", true),
         SpeedDefinitionService.Option.num(
            "Initial boost multiplier", 1.0, 0.01, 10.0
         ),
         SpeedDefinitionService.Option.flag("Timer", true),
         SpeedDefinitionService.Option.flag("Damage boost", true),
         SpeedDefinitionService.Option.flag("Low hop", true),
         SpeedDefinitionService.Option.flag("Air strafe", true)
      );
      updateState(
         arrayList,
         "Intave14",
         "Intave",
         "Partial strafe on ground/late air ticks and a small upward boost.",
         SpeedDefinitionService.Option.flag("Strafe", true),
         SpeedDefinitionService.Option.num(
            "Strafe strength",
            0.27,
            0.01,
            0.27
         ),
         SpeedDefinitionService.Option.flag("Air boost", true)
      );
      updateState(
         arrayList,
         "Intave14Fast",
         "Intave",
         "Boost on the first four air ticks with an optional timer.",
         SpeedDefinitionService.Option.flag("Timer", true)
      );
      updateState(arrayList, "HylexLowHop", "Hylex", "Low jump height and small speed-dependent air boosts.");
      updateState(arrayList, "HylexGround", "Hylex", "Ground acceleration after five uninterrupted grounded ticks.");
      updateState(
         arrayList,
         "BlocksMC",
         "BlocksMC",
         "Low hops with directional strafe and correction slowdown.",
         SpeedDefinitionService.Option.flag("Round strafe yaw", false)
      );
      updateState(arrayList, "Matrix7", "Matrix", "Fixed jump motion and strafe at low horizontal speed.");
      updateState2(arrayList, "NCPBHop", "NCP", true, "Staged bunny hops with a five-move timer cycle.");
      updateState2(arrayList, "NCPFHop", "NCP", true, "Fast hops with increased air acceleration.");
      updateState2(arrayList, "SNCPBHop", "NCP", true, "Staged bunny hops with a collision recovery sequence.");
      updateState2(arrayList, "NCPHop", "NCP", true, "Bunny hops with air acceleration and timer.");
      updateState2(arrayList, "NCPYPort", "NCP", true, "Four-jump cycle with vertical pull down.");
      updateState2(arrayList, "UNCPHop", "NCP", false, "Potion-dependent speed decay and alternating timer.");
      updateState2(
         arrayList,
         "UNCPHopNew",
         "NCP",
         false,
         "Configurable low hop, air strafe and damage boosts.",
         SpeedDefinitionService.Option.flag("Pull down", true),
         SpeedDefinitionService.Option.count("On tick", 5, 5, 9),
         SpeedDefinitionService.Option.flag("On hurt", true),
         SpeedDefinitionService.Option.flag("Boost", true),
         SpeedDefinitionService.Option.flag("Timer", true),
         SpeedDefinitionService.Option.flag("Damage boost", true),
         SpeedDefinitionService.Option.flag("Low hop", true),
         SpeedDefinitionService.Option.flag("Air strafe", true)
      );
      updateState2(arrayList, "AACHop3.3.13", "AAC", true, "Initial forward jump boost and fall-dependent air acceleration.");
      updateState2(arrayList, "AACHop3.5.0", "AAC", true, "Post-motion hops and air acceleration.");
      updateState2(arrayList, "AACHop4", "AAC", true, "Bunny hops with fall-distance timer stages.");
      updateState2(arrayList, "AACHop5", "AAC", false, "Bunny hops with periodic fall-distance timer boosts.");
      updateState2(arrayList, "SpartanYPort", "Spartan", false, "Forward hops with alternating randomized pull down.");
      updateState2(arrayList, "SpectreLowHop", "Spectre", true, "Low vertical motion and fixed ground strafe.");
      updateState2(arrayList, "SpectreBHop", "Spectre", true, "High ground strafe followed by air strafe.");
      updateState2(arrayList, "SpectreOnGround", "Spectre", true, "Ten ground boosts followed by a stop.");
      updateState2(arrayList, "VerusHop", "Verus", false, "Potion-dependent bunny hop speed with air decay.");
      updateState2(arrayList, "VerusFHop", "Verus", false, "Different straight and diagonal ground/air speeds.");
      updateState2(arrayList, "VerusLowHop", "Verus", true, "Potion-dependent low hops with immediate pull down.");
      updateState2(arrayList, "VerusLowHopNew", "Verus", false, "Low hops with slowness-aware speed and air decay.");
      updateState2(arrayList, "VulcanHop", "Vulcan", false, "Ground strafe with ascent and descent timer stages.");
      updateState2(arrayList, "VulcanLowHop", "Vulcan", false, "Tick-dependent low hops and timer stages.");
      updateState2(arrayList, "VulcanGround2.8.8", "Vulcan", false, "Ground collision strafe, jump suppression and position offset.");
      updateState2(arrayList, "OldMatrixHop", "Matrix", true, "Bunny hops with air acceleration and strafe.");

      for (String text : List.of("MatrixHop", "MatrixSlowHop")) {
         updateState2(
            arrayList,
            text,
            "Matrix",
            false,
            "Low hops with extra ground boost; SlowHop also changes timer.",
            SpeedDefinitionService.Option.flag("Low hop", true),
            SpeedDefinitionService.Option.num(
               "Extra ground boost", 0.2, 0.0, 0.5
            )
         );
      }

      updateState2(
         arrayList,
         "IntaveHop14",
         "Intave",
         false,
         "Ground/air timers, partial strafe and upward boost.",
         SpeedDefinitionService.Option.flag("Boost", true),
         SpeedDefinitionService.Option.num(
            "Initial boost multiplier", 1.0, 0.01, 10.0
         ),
         SpeedDefinitionService.Option.flag("Low hop", true),
         SpeedDefinitionService.Option.num(
            "Strafe strength",
            0.29,
            0.1,
            0.29
         ),
         SpeedDefinitionService.Option.num(
            "Ground timer",
            0.5,
            0.1,
            5.0
         ),
         SpeedDefinitionService.Option.num(
            "Air timer",
            1.09,
            0.1,
            5.0
         )
      );
      updateState2(
         arrayList,
         "TeleportCubeCraft",
         "CubeCraft",
         true,
         "A ground movement step every 300 ms.",
         SpeedDefinitionService.Option.num(
            "Port length", 1.0, 0.1, 2.0
         )
      );
      updateState2(arrayList, "HypixelHop", "Watchdog", false, "Strafe-phase jumps with a ground speed boost.");
      updateState2(
         arrayList,
         "HypixelLowHop",
         "Watchdog",
         false,
         "Air-tick low hops and optional late-air glide.",
         SpeedDefinitionService.Option.flag("Glide", true)
      );
      updateState2(
         arrayList,
         "BlocksMCHop",
         "BlocksMC",
         false,
         "Air strafe and low hops with optional damage boost.",
         SpeedDefinitionService.Option.flag("Full strafe", true),
         SpeedDefinitionService.Option.flag("Low hop", true),
         SpeedDefinitionService.Option.flag("Damage boost", true),
         SpeedDefinitionService.Option.flag("Damage low hop", false),
         SpeedDefinitionService.Option.flag("Safe Y", true)
      );
      updateState2(arrayList, "Boost", "NCP", false, "Ground acceleration cycle with collision-checked position offsets.");
      updateState2(arrayList, "Frame", "NCP", true, "Alternating jump cycles with a delayed horizontal boost.");
      updateState2(arrayList, "MiJump", "NCP", true, "Small ground jumps with capped horizontal acceleration.");
      updateState2(arrayList, "OnGround", "NCP", false, "Ground position and motion sequence with timer boost.");
      updateState2(arrayList, "SlowHop", "General", false, "Bunny hops with gradual air acceleration.");
      updateState2(arrayList, "Legit", "General", false, "Vanilla jumps and forward sprinting.");
      updateState2(
         arrayList,
         "Custom",
         "General",
         false,
         "Separate ground/air strafe, jump height, timers and landing conditions.",
         SpeedDefinitionService.Option.num(
            "Jump height", 0.42, 0.0, 4.0
         ),
         SpeedDefinitionService.Option.num(
            "Ground strafe", 1.6, 0.0, 2.0
         ),
         SpeedDefinitionService.Option.num("Air strafe", 0.0, 0.0, 2.0),
         SpeedDefinitionService.Option.num(
            "Ground timer", 1.0, 0.1, 2.0
         ),
         SpeedDefinitionService.Option.count("Air timer tick", 5, 1, 20),
         SpeedDefinitionService.Option.num(
            "Air timer", 1.0, 0.1, 2.0
         ),
         SpeedDefinitionService.Option.flag("Reset XZ", false),
         SpeedDefinitionService.Option.flag("Reset Y", false),
         SpeedDefinitionService.Option.flag("Not on consuming", false),
         SpeedDefinitionService.Option.flag("Not on falling", false),
         SpeedDefinitionService.Option.flag("Not on void", true)
      );
      arrayList.addAll(SpeedSourceService.SOURCES.stream().map(SpeedSourceService.Source::definition).toList());
      return List.copyOf(arrayList);
   }

   private static void updateState(List<SpeedDefinitionService.Definition> items, String text, String currentText, String nextText, SpeedDefinitionService.Option... options) {
      items.add(new SpeedDefinitionService.Definition("nextgen/" + text, text, "Nextgen", currentText, nextText, false, List.of(options)));
   }

   private static void updateState2(
      List<SpeedDefinitionService.Definition> items, String text, String currentText, boolean enabled, String nextText, SpeedDefinitionService.Option... options
   ) {
      items.add(new SpeedDefinitionService.Definition("legacy/" + text, text, "Legacy", currentText, nextText, enabled, List.of(options)));
   }

   public record Definition(
      String id, String name, String edition, String group, String description, boolean deprecated, List<SpeedDefinitionService.Option> options
   ) {
      public ModuleEntriesService.Entry entry() {
         return new ModuleEntriesService.Entry(this.id, this.name, this.edition, this.group, this.description, this.deprecated);
      }

      public SpeedStrafeHandler create(SpeedOperationHandler speedOperation) {
         SpeedSourceService.Source currentSource = SpeedSourceService.source(this.id);
         if (currentSource != null) {
            return currentSource.create(speedOperation);
         } else {
            return this.edition.equals("Nextgen")
               ? new NextGenSpeedMode(this.name, speedOperation)
               : (this.group.equals("NCP") ? new NcpSpeedMode(this.name, speedOperation) : new LegacySpeedMode(this.name, speedOperation));
         }
      }
   }

   public record Option(String key, Object value, double min, double max, double step, List<String> choices) {
      static SpeedDefinitionService.Option flag(String text, boolean enabled) {
         return new SpeedDefinitionService.Option(text, enabled, 0.0, 0.0, 0.0, List.of());
      }

      static SpeedDefinitionService.Option num(String text, double doubleValue, double currentDoubleValue, double nextDoubleValue) {
         return new SpeedDefinitionService.Option(text, doubleValue, currentDoubleValue, nextDoubleValue, 0.001, List.of());
      }

      static SpeedDefinitionService.Option count(String text, int value, int currentValue, int nextValue) {
         return new SpeedDefinitionService.Option(text, (double)value, currentValue, nextValue, 1.0, List.of());
      }

      static SpeedDefinitionService.Option choice(String text, String currentText, String... strings) {
         return new SpeedDefinitionService.Option(text, currentText, 0.0, 0.0, 0.0, List.of(strings));
      }
   }
}
