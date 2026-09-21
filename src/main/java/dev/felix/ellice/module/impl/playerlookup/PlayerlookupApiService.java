package dev.felix.ellice.module.impl.playerlookup;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.screen.builtin.BuiltinSourceTracker;

public final class PlayerlookupApiService extends ModuleSettingsService implements ModuleOperationHandler {
   private static final PlayerlookupLooksValidValidator playerlookupLooksValidValidator = new PlayerlookupLooksValidValidator();
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Auto-copy UUID", false)
         .description("Copies the dashed UUID to your system clipboard after each successful player lookup.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Remember last query", true)
         .description("Keeps the latest valid query for this session and runs it again when the lookup screen reopens.")
   );
   private String text = "";

   public PlayerlookupApiService() {
      super(
         ModuleBuilderData.builder("Player Lookup")
            .description("Looks up a Minecraft profile by name or UUID, including skin and cape data.")
            .category(ModuleFeatureType.TOOLS)
            .build()
      );
   }

   public static PlayerlookupLooksValidValidator api() {
      return playerlookupLooksValidValidator;
   }

   public boolean autoCopyUuid() {
      return (Boolean)this.moduleBool.get();
   }

   public boolean rememberLast() {
      return (Boolean)this.moduleBool2.get();
   }

   public String lastQuery() {
      return this.text;
   }

   public void rememberQuery(String currentText) {
      if ((Boolean)this.moduleBool2.get() && currentText != null) {
         this.text = currentText;
      }
   }

   @Override
   protected void onEnable() {
      this.openPanel();
   }

   @Override
   public void openPanel() {
      if (CoreIsInitializedHandler.isReady()) {
         if (!this.isEnabled()) {
            this.enable();
         } else {
            if (CoreIsInitializedHandler.get().screens() != null) {
               CoreIsInitializedHandler.get().screens().open(new BuiltinSourceTracker(this));
            }
         }
      }
   }

   @Override
   protected void onDisable() {
      if (CoreIsInitializedHandler.isReady()
         && CoreIsInitializedHandler.get().screens() != null
         && "player-lookup".equals(CoreIsInitializedHandler.get().screens().currentId())) {
         CoreIsInitializedHandler.get().screens().close();
      }
   }
}
