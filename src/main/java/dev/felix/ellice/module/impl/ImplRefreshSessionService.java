package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatSessionNameService;
import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import java.util.function.Supplier;

public final class ImplRefreshSessionService extends ModuleSettingsService {
   private static volatile ImplRefreshSessionService implRefreshSessionService;
   private final Supplier<String> text;
   private final Runnable runnable;
   private final ModuleSetting.Text moduleText = this.setting((new ModuleSetting.Text("Custom name", "Player", 64) {
      @Override
      public void set(String text) {
         super.set(PrivacyRevisionService.sanitize(text));
      }
   }).description("Your local display name throughout ellice and Minecraft. Blank uses Player; your server identity stays unchanged."));

   public ImplRefreshSessionService() {
      this(CompatSessionNameService::sessionName, CompatSessionNameService::refreshChat);
   }

   public ImplRefreshSessionService(Supplier<String> supplier, Runnable currentRunnable) {
      super(
         ModuleBuilderData.builder("NameChanger")
            .description("Replaces your visible username across the client with a custom display name.")
            .category(ModuleFeatureType.CLIENT)
            .build()
      );
      this.text = supplier;
      this.runnable = currentRunnable;
      this.moduleText.onChange(item -> {
         if (this.isEnabled()) {
            this.updateState();
         }
      });
   }

   @Override
   protected void onEnable() {
      implRefreshSessionService = this;
      this.updateState();
   }

   @Override
   protected void onDisable() {
      if (implRefreshSessionService == this) {
         implRefreshSessionService = null;
         PrivacyRevisionService.clear();
         this.runnable.run();
      }
   }

   private void updateState() {
      if (PrivacyRevisionService.configure(this.text.get(), (String)this.moduleText.get())) {
         this.runnable.run();
      }
   }

   public static void refreshSession() {
      ImplRefreshSessionService implRefreshSession = implRefreshSessionService;
      if (implRefreshSession != null) {
         implRefreshSession.updateState();
      }
   }
}
