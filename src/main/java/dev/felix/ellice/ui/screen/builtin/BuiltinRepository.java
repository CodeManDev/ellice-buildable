package dev.felix.ellice.ui.screen.builtin;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.slf4j.LoggerFactory;

public final class BuiltinRepository {
   private static final Gson gson2 = new Gson();
   private final Path path;
   private BuiltinRepository.State state2;

   private static String createText(String text) {
      if (text.startsWith("clickgui.category.")) {
         int currentLength = text.indexOf(".module.", "clickgui.category.".length());
         if (currentLength >= 0) {
            return "module:" + text.substring(currentLength + ".module.".length());
         }
      }

      return text;
   }

   public BuiltinRepository() {
      this.path = null;
      this.state2 = BuiltinRepository.State.initial();
   }

   public BuiltinRepository(Path currentPath) {
      this.path = Objects.requireNonNull(currentPath);
      this.state2 = BuiltinRepository.State.initial();

      try {
         if (Files.size(currentPath) <= 262144L) {
            BuiltinRepository.State state = (BuiltinRepository.State)gson2.fromJson(Files.readString(currentPath), BuiltinRepository.State.class);
            if (state != null) {
               this.state2 = state;
            }
         }
      } catch (IOException | RuntimeException iOExceptionRuntimeException) {
      }
   }

   public BuiltinRepository.State state() {
      return this.state2;
   }

   public void save(BuiltinRepository.State state) {
      this.state2 = Objects.requireNonNull(state);
      if (this.path != null) {
         try {
            Files.createDirectories(this.path.toAbsolutePath().getParent());
            Path currentPath = Files.createTempFile(this.path.toAbsolutePath().getParent(), "clickgui-", ".tmp");

            try {
               Files.writeString(currentPath, gson2.toJson(state));

               try {
                  Files.move(currentPath, this.path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
               } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                  Files.move(currentPath, this.path, StandardCopyOption.REPLACE_EXISTING);
               }
            } finally {
               Files.deleteIfExists(currentPath);
            }
         } catch (IOException iOException) {
            LoggerFactory.getLogger(BuiltinRepository.class).warn("Could not save ClickGUI navigation", iOException);
         }
      }
   }

   public record State(
      String category,
      String selectedModule,
      String query,
      String settingsQuery,
      String settingsGroup,
      boolean enabledOnly,
      boolean detailVisible,
      boolean showHelp,
      Set<String> collapsedSections,
      Map<String, String> presetChoices,
      Set<String> expandedPresets,
      Map<String, Float> scroll
   ) {
      public State(
         String category,
         String selectedModule,
         String query,
         String settingsQuery,
         String settingsGroup,
         boolean enabledOnly,
         boolean detailVisible,
         boolean showHelp,
         Set<String> collapsedSections,
         Map<String, String> presetChoices,
         Set<String> expandedPresets,
         Map<String, Float> scroll
      ) {
         category = createText2(category);
         selectedModule = BuiltinRepository.createText(createText2(selectedModule));
         query = createText2(query);
         settingsQuery = createText2(settingsQuery);
         settingsGroup = createText2(settingsGroup);
         collapsedSections = Set.copyOf(createText3(collapsedSections).stream().map(BuiltinRepository::createText).toList());
         expandedPresets = createText3(expandedPresets);
         LinkedHashMap linkedHashMap = new LinkedHashMap();
         if (presetChoices != null) {
            presetChoices.entrySet()
               .stream()
               .limit(512L)
               .filter(entry -> entry.getKey() != null && entry.getValue() != null)
               .forEach(entry -> linkedHashMap.put(createText2((String)entry.getKey()), createText2((String)entry.getValue())));
         }

         presetChoices = Map.copyOf(linkedHashMap);
         LinkedHashMap currentLinkedHashMap = new LinkedHashMap();
         if (scroll != null) {
            scroll.entrySet()
               .stream()
               .limit(32L)
               .forEach(
                  entry -> {
                     if (entry.getKey() != null && entry.getValue() != null && Float.isFinite((Float)entry.getValue())) {
                        currentLinkedHashMap.put(
                           createText2((String)entry.getKey()),
                           Math.max(-1000000.0F, Math.min(0.0F, (Float)entry.getValue()))
                        );
                     }
                  }
               );
         }

         scroll = Map.copyOf(currentLinkedHashMap);
         this.category = category;
         this.selectedModule = selectedModule;
         this.query = query;
         this.settingsQuery = settingsQuery;
         this.settingsGroup = settingsGroup;
         this.enabledOnly = enabledOnly;
         this.detailVisible = detailVisible;
         this.showHelp = showHelp;
         this.collapsedSections = collapsedSections;
         this.presetChoices = presetChoices;
         this.expandedPresets = expandedPresets;
         this.scroll = scroll;
      }

      public static BuiltinRepository.State initial() {
         return new BuiltinRepository.State("VISUALS", "", "", "", "", false, false, false, Set.of(), Map.of(), Set.of(), Map.of());
      }

      private static String createText2(String text) {
         return text == null ? "" : text.substring(0, Math.min(256, text.length()));
      }

      private static Set<String> createText3(Set<String> values) {
         return values == null
            ? Set.of()
            : Set.copyOf(values.stream().filter(Objects::nonNull).limit(2048L).map(BuiltinRepository.State::createText2).toList());
      }
   }
}
