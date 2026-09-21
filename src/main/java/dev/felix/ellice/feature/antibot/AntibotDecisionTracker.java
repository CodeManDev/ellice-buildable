package dev.felix.ellice.feature.antibot;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class AntibotDecisionTracker {
   private static final int count = 512;
   private static final int count2 = 1024;
   private final LinkedHashMap<Integer, AntibotDecisionTracker.Track> entries = new LinkedHashMap<>();
   private final LinkedHashMap<UUID, AntibotDecisionTracker.Life> entries2 = new LinkedHashMap<>();
   private Map<Integer, AntibotDecisionTracker.Decision> entries3 = Map.of();
   private AntibotDecisionTracker.Settings values2;
   private long timestamp = Long.MIN_VALUE;

   public Map<Integer, AntibotDecisionTracker.Decision> update(AntibotDecisionTracker.Frame frame, AntibotDecisionTracker.Settings settings) {
      if (frame.now() < this.timestamp) {
         this.reset();
      }

      if (this.values2 != null && !this.values2.equals(settings)) {
         this.reset();
      }

      this.values2 = settings;
      this.timestamp = frame.now();
      this.entries.values().removeIf(item -> frame.now() - item.seen > settings.memory());
      this.entries2.values().removeIf(item -> frame.now() - item.seen > settings.memory());
      HashMap<UUID, AntibotDecisionTracker.Profile> hashMap = new HashMap<>();
      HashMap<String, Set<UUID>> currentHashMap = new HashMap<>();

      for (AntibotDecisionTracker.Profile currentProfile : frame.roster()) {
         hashMap.put(currentProfile.uuid(), currentProfile);
         currentHashMap.computeIfAbsent(createText(currentProfile.name()), item -> new HashSet<>()).add(currentProfile.uuid());
      }

      for (AntibotDecisionTracker.Player currentPlayer : frame.players()) {
         currentHashMap.computeIfAbsent(createText(currentPlayer.name()), item -> new HashSet<>()).add(currentPlayer.key().uuid());
      }

      int value = !"Required".equals(settings.tabMode()) && (!"Auto".equals(settings.tabMode()) || !hashMap.containsKey(frame.self())) ? 0 : 1;
      LinkedHashMap<Integer, AntibotDecisionTracker.Decision> linkedHashMap = new LinkedHashMap<>();

      for (AntibotDecisionTracker.Player nextPlayer : frame.players().stream().limit(512L).toList()) {
         AntibotDecisionTracker.Track track = this.entries.get(nextPlayer.key().entityId());
         if (track == null || !track.key.equals(nextPlayer.key())) {
            track = new AntibotDecisionTracker.Track(nextPlayer.key(), frame.now());
            this.entries.remove(nextPlayer.key().entityId());
            if (this.entries.size() >= 512) {
               this.entries.remove(this.entries.keySet().iterator().next());
            }

            this.entries.put(nextPlayer.key().entityId(), track);
         }

         long offset = frame.now() - track.seen;
         long currentOffset = Math.min(100L, Math.max(0L, offset));
         if (offset > 250L) {
            track.strongFor = track.clearFor = 0L;
            track.motion.clear();
         }

         track.seen = frame.now();
         AntibotDecisionTracker.Life life = this.entries2.computeIfAbsent(nextPlayer.key().uuid(), item -> new AntibotDecisionTracker.Life());
         if (!nextPlayer.key().equals(life.key)) {
            if (life.key != null && !life.alive) {
               life.births.clear();
            }

            life.births.putIfAbsent(nextPlayer.key(), frame.now());
            life.key = nextPlayer.key();
         }

         life.seen = frame.now();
         life.alive = nextPlayer.alive() && nextPlayer.health() > 0.0;
         life.births.values().removeIf(item -> frame.now() - item > 6000L);

         while (life.births.size() > 32) {
            life.births.remove(life.births.keySet().iterator().next());
         }

         while (this.entries2.size() > 1024) {
            this.entries2.remove(this.entries2.keySet().iterator().next());
         }

         AntibotDecisionTracker.Profile nextProfile = (AntibotDecisionTracker.Profile)hashMap.get(nextPlayer.key().uuid());
         int currentValue = nextProfile != null && createText(nextProfile.name()).equals(createText(nextPlayer.name())) ? 1 : 0;
         EnumMap<AntibotDecisionTracker.Family, AntibotDecisionTracker.Evidence> enumMap = new EnumMap<>(AntibotDecisionTracker.Family.class);
         int nextValue = settings.identities() && nextPlayer.key().uuid().equals(frame.self()) ? 1 : 0;
         if (value != 0 && nextProfile == null) {
            updateState(enumMap, AntibotDecisionTracker.Family.PRESENCE, 35, "No player profile");
         } else if (value != 0 && !nextProfile.listed()) {
            updateState(enumMap, AntibotDecisionTracker.Family.PRESENCE, 12, "Not listed in tab");
         }

         if (settings.identities()) {
            if (nextValue != 0) {
               updateState(enumMap, AntibotDecisionTracker.Family.IDENTITY, 100, "Another entity uses your UUID");
            }

            if (nextProfile != null && currentValue == 0) {
               updateState(enumMap, AntibotDecisionTracker.Family.IDENTITY, 45, "Entity name conflicts with its profile");
            }

            int currentSize = currentHashMap.getOrDefault(createText(nextPlayer.name()), Set.of()).size() > 1 ? 1 : 0;
            if (currentSize != 0 && currentValue == 0) {
               updateState(enumMap, AntibotDecisionTracker.Family.IDENTITY, 35, "Duplicate name without a matching profile");
            }

            if (nextPlayer.name() == null || nextPlayer.name().isBlank() || nextPlayer.name().chars().anyMatch(Character::isISOControl)) {
               updateState(enumMap, AntibotDecisionTracker.Family.IDENTITY, 35, "Invalid profile name");
            }
         }

         if (settings.markers() && settings.npcMarkers().stream().anyMatch(item -> createText(nextPlayer.displayName()).contains(item))) {
            updateState(enumMap, AntibotDecisionTracker.Family.PRESENTATION, 45, "Configured NPC name marker");
         }

         String text = track.motion
            .observe(
               frame.now(),
               frame.selfPosition(),
               nextPlayer.position(),
               frame.selfYaw(),
               settings.motion() && frame.motionReliable() && nextPlayer.motionReliable() && nextPlayer.alive()
            );
         if (!text.isEmpty()) {
            updateState(enumMap, AntibotDecisionTracker.Family.MOTION, 45, text);
         }

         if (settings.churn()
            && life.births.size() >= 3
            && AntibotComponent.finite(nextPlayer.position())
            && nextPlayer.position().subtract(frame.selfPosition()).length() < 16.0) {
            updateState(enumMap, AntibotDecisionTracker.Family.LIFECYCLE, 35, "Repeated nearby respawns without an observed death");
         }

         if (settings.invalidBody()
            && (
               !AntibotComponent.finite(nextPlayer.position())
                  || !nextPlayer.validBody()
                  || !Double.isFinite(nextPlayer.pitch())
                  || Math.abs(nextPlayer.pitch()) > 90.01
                  || !Double.isFinite(nextPlayer.health())
            )) {
            updateState(enumMap, AntibotDecisionTracker.Family.BODY, 45, "Invalid body or rotation data");
         }

         double doubleValue = Math.min(100, enumMap.values().stream().mapToInt(AntibotDecisionTracker.Evidence::weight).sum());
         track.score = track.score + (doubleValue - track.score) * (1.0 - Math.exp(-currentOffset / 150.0));
         int nextSize = !(doubleValue >= settings.threshold()) || enumMap.size() < settings.minimumFamilies() && nextValue == 0 ? 0 : 1;
         track.strongFor = nextSize != 0 ? track.strongFor + currentOffset : 0L;
         int previousValue = frame.now() - track.born < settings.spawnGrace() ? 1 : 0;
         if (!track.filtered
            && previousValue == 0
            && nextSize != 0
            && track.score >= settings.threshold() - 0.5
            && track.strongFor >= settings.confirmation()) {
            track.filtered = true;
         }

         int previousSize = nextValue != 0
               || !(doubleValue <= Math.max(0.0, settings.threshold() - 15.0))
                  && enumMap.size() >= settings.minimumFamilies()
            ? 0
            : 1;
         track.clearFor = previousSize != 0 ? track.clearFor + currentOffset : 0L;
         if (track.filtered && previousSize != 0 && track.clearFor >= settings.releaseDelay()) {
            track.filtered = false;
         }

         AntibotDecisionTracker.State state = track.filtered
            ? AntibotDecisionTracker.State.FILTERED
            : (previousValue != 0 ? AntibotDecisionTracker.State.OBSERVING : (doubleValue > 0.0 ? AntibotDecisionTracker.State.SUSPECT : AntibotDecisionTracker.State.CLEAR));
         int sourceValue = settings.holdSuspicious() && previousValue != 0 && doubleValue >= 35.0 ? 1 : 0;
         String currentText = enumMap.values()
            .stream()
            .map(AntibotDecisionTracker.Evidence::reason)
            .reduce((item, currentItem) -> item + " · " + currentItem)
            .orElse(previousValue != 0 ? "Observing a new player" : "No conflicting evidence");
         if (checkCondition(settings.allowed(), nextPlayer)) {
            state = AntibotDecisionTracker.State.ALLOWED;
            sourceValue = 0;
            track.filtered = false;
            track.score = 0.0;
            currentText = "Allowed by your list";
         } else if (checkCondition(settings.blocked(), nextPlayer)) {
            state = AntibotDecisionTracker.State.BLOCKED;
            sourceValue = 0;
            currentText = "Blocked by your list";
         }

         linkedHashMap.put(
            nextPlayer.key().entityId(),
            new AntibotDecisionTracker.Decision(
               nextPlayer.key(), nextPlayer.name(), state, track.score, enumMap.size(), (sourceValue != 0), List.copyOf(enumMap.values()), currentText
            )
         );
      }

      this.entries3 = Map.copyOf(linkedHashMap);
      return this.entries3;
   }

   public Optional<AntibotDecisionTracker.Decision> decision(AntibotDecisionTracker.Key currentKey) {
      return Optional.ofNullable(this.entries3.get(currentKey.entityId())).filter(item -> item.key().equals(currentKey));
   }

   public Collection<AntibotDecisionTracker.Decision> decisions() {
      return this.entries3.values();
   }

   public void resetMotion() {
      this.entries.values().forEach(item -> item.motion.clear());
   }

   public void reset() {
      this.entries.clear();
      this.entries2.clear();
      this.entries3 = Map.of();
      this.values2 = null;
      this.timestamp = Long.MIN_VALUE;
   }

   public int retainedTracks() {
      return this.entries.size();
   }

   public int retainedHistory() {
      return this.entries2.size();
   }

   private static void updateState(
      EnumMap<AntibotDecisionTracker.Family, AntibotDecisionTracker.Evidence> enumMap, AntibotDecisionTracker.Family family, int value, String text
   ) {
      if (!enumMap.containsKey(family) || ((AntibotDecisionTracker.Evidence)enumMap.get(family)).weight() < value) {
         enumMap.put(family, new AntibotDecisionTracker.Evidence(family, value, text));
      }
   }

   private static boolean checkCondition(Set<String> values, AntibotDecisionTracker.Player player) {
      return values.contains(createText(player.name())) || values.contains(player.key().uuid().toString());
   }

   private static String createText(String text) {
      return text == null ? "" : text.strip().toLowerCase(Locale.ROOT);
   }

   private static Set<String> createText2(Set<String> values) {
      HashSet hashSet = new HashSet();

      for (String text : values) {
         if (!createText(text).isEmpty()) {
            hashSet.add(createText(text));
         }
      }

      return Set.copyOf(hashSet);
   }

   public record Decision(
      AntibotDecisionTracker.Key key,
      String name,
      AntibotDecisionTracker.State state,
      double score,
      int families,
      boolean hold,
      List<AntibotDecisionTracker.Evidence> evidence,
      String explanation
   ) {
      public Decision(
         AntibotDecisionTracker.Key key,
         String name,
         AntibotDecisionTracker.State state,
         double score,
         int families,
         boolean hold,
         List<AntibotDecisionTracker.Evidence> evidence,
         String explanation
      ) {
         evidence = List.copyOf(evidence);
         this.key = key;
         this.name = name;
         this.state = state;
         this.score = score;
         this.families = families;
         this.hold = hold;
         this.evidence = evidence;
         this.explanation = explanation;
      }

      public boolean filtered() {
         return this.state == AntibotDecisionTracker.State.FILTERED || this.state == AntibotDecisionTracker.State.BLOCKED;
      }

      public boolean exclude() {
         return this.filtered() || this.hold;
      }
   }

   public record Evidence(AntibotDecisionTracker.Family family, int weight, String reason) {
   }

   public enum Family {
      PRESENCE,
      IDENTITY,
      PRESENTATION,
      MOTION,
      LIFECYCLE,
      BODY;


      private static AntibotDecisionTracker.Family[] $values() {
         return new AntibotDecisionTracker.Family[]{PRESENCE, IDENTITY, PRESENTATION, MOTION, LIFECYCLE, BODY};
      }
   }

   public record Frame(
      long now,
      UUID self,
      RotationVector selfPosition,
      double selfYaw,
      boolean motionReliable,
      List<AntibotDecisionTracker.Player> players,
      List<AntibotDecisionTracker.Profile> roster
   ) {
      public Frame(
         long now,
         UUID self,
         RotationVector selfPosition,
         double selfYaw,
         boolean motionReliable,
         List<AntibotDecisionTracker.Player> players,
         List<AntibotDecisionTracker.Profile> roster
      ) {
         players = List.copyOf(players);
         roster = List.copyOf(roster);
         this.now = now;
         this.self = self;
         this.selfPosition = selfPosition;
         this.selfYaw = selfYaw;
         this.motionReliable = motionReliable;
         this.players = players;
         this.roster = roster;
      }
   }

   public record Key(int entityId, UUID uuid, long incarnation) {
   }

   private static final class Life {
      AntibotDecisionTracker.Key key;
      long seen;
      boolean alive;
      final LinkedHashMap<AntibotDecisionTracker.Key, Long> births = new LinkedHashMap<>();
   }

   public record Player(
      AntibotDecisionTracker.Key key,
      String name,
      String displayName,
      RotationVector position,
      double pitch,
      double health,
      boolean alive,
      boolean validBody,
      boolean motionReliable
   ) {
   }

   public record Profile(UUID uuid, String name, boolean listed) {
   }

   public record Settings(
      double threshold,
      int minimumFamilies,
      long spawnGrace,
      long confirmation,
      long releaseDelay,
      long memory,
      String tabMode,
      boolean identities,
      boolean markers,
      boolean motion,
      boolean churn,
      boolean invalidBody,
      boolean holdSuspicious,
      Set<String> npcMarkers,
      Set<String> allowed,
      Set<String> blocked
   ) {
      public Settings(
         double threshold,
         int minimumFamilies,
         long spawnGrace,
         long confirmation,
         long releaseDelay,
         long memory,
         String tabMode,
         boolean identities,
         boolean markers,
         boolean motion,
         boolean churn,
         boolean invalidBody,
         boolean holdSuspicious,
         Set<String> npcMarkers,
         Set<String> allowed,
         Set<String> blocked
      ) {
         if (Double.isFinite(threshold)
            && !(threshold < 1.0)
            && !(threshold > 100.0)
            && minimumFamilies >= 1
            && minimumFamilies <= 6
            && spawnGrace >= 0L
            && confirmation >= 0L
            && releaseDelay >= 0L
            && memory >= 1000L) {
            npcMarkers = AntibotDecisionTracker.createText2(npcMarkers);
            allowed = AntibotDecisionTracker.createText2(allowed);
            blocked = AntibotDecisionTracker.createText2(blocked);
            this.threshold = threshold;
            this.minimumFamilies = minimumFamilies;
            this.spawnGrace = spawnGrace;
            this.confirmation = confirmation;
            this.releaseDelay = releaseDelay;
            this.memory = memory;
            this.tabMode = tabMode;
            this.identities = identities;
            this.markers = markers;
            this.motion = motion;
            this.churn = churn;
            this.invalidBody = invalidBody;
            this.holdSuspicious = holdSuspicious;
            this.npcMarkers = npcMarkers;
            this.allowed = allowed;
            this.blocked = blocked;
         } else {
            throw new IllegalArgumentException("Invalid AntiBot bounds");
         }
      }

      public static AntibotDecisionTracker.Settings defaults() {
         return new AntibotDecisionTracker.Settings(
            65.0,
            2,
            500L,
            250L,
            750L,
            30000L,
            "Auto",
            true,
            true,
            true,
            true,
            true,
            true,
            Set.of("[npc]", "[shop]", "[bot]"),
            Set.of(),
            Set.of()
         );
      }
   }

   public enum State {
      OBSERVING,
      CLEAR,
      SUSPECT,
      FILTERED,
      ALLOWED,
      BLOCKED;


      private static AntibotDecisionTracker.State[] $values() {
         return new AntibotDecisionTracker.State[]{OBSERVING, CLEAR, SUSPECT, FILTERED, ALLOWED, BLOCKED};
      }
   }

   private static final class Track {
      final AntibotDecisionTracker.Key key;
      final AntibotComponent motion = new AntibotComponent();
      final long born;
      long seen;
      long strongFor;
      long clearFor;
      double score;
      boolean filtered;

      Track(AntibotDecisionTracker.Key currentKey, long longValue) {
         this.key = currentKey;
         this.born = this.seen = longValue;
      }
   }
}

