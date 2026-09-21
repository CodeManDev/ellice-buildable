package dev.felix.ellice.module.impl;

import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.velocity.VelocityTransformConverter;
import dev.felix.ellice.feature.velocity.VelocityActivateService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import java.util.Locale;

public final class ImplSelectedModeSelector extends ModuleSettingsService {
   public final ModuleSetting.Mode mode = this.setting(
      new ModuleSetting.Mode("Mode", new String[]{"Cancel", "Percentage", "Reverse", "Push", "Jump"}, "Percentage")
         .description(
            "Cancel ignores impulses; Percentage scales them; Reverse reverses horizontal impulses; Push controls collisions; Jump requests a grounded jump after an impulse."
         )
   );
   public final ModuleSetting.Number horizontal = this.setting(
      new ModuleSetting.Number("Horizontal (%)", 0.0F, 0.0F, 200.0F, 1.0F)
         .description("Retained horizontal velocity change. 0 preserves current movement; 100 applies the full change.")
   );
   public final ModuleSetting.Number vertical = this.setting(
      new ModuleSetting.Number("Vertical (%)", 100.0F, 0.0F, 200.0F, 1.0F)
         .description("Retained vertical velocity change. 0 preserves current vertical motion; 100 applies the full change.")
   );
   public final ModuleSetting.Number reverseStrength = this.setting(
      new ModuleSetting.Number("Reverse Strength (%)", 100.0F, 0.0F, 200.0F, 1.0F)
         .description("Strength of the reversed horizontal impulse; vertical motion uses Vertical (%).")
   );
   public final ModuleSetting.Bool explosions = this.setting(
      new ModuleSetting.Bool("Explosions", true)
         .description("Also process explosion knockback, preserving explosion sounds, particles and block effects.")
   );
   public final ModuleSetting.Number chance = this.setting(
      new ModuleSetting.Number("Chance (%)", 100.0F, 0.0F, 100.0F, 1.0F)
         .description("Chance per incoming impulse to apply the selected mode.")
   );
   public final ModuleSetting.Bool onlyGround = this.setting(
      new ModuleSetting.Bool("Only on Ground", false).description("Only modify incoming impulses while standing on the ground.")
   );
   public final ModuleSetting.Number entityPush = this.setting(
      new ModuleSetting.Number("Entity Push (%)", 0.0F, 0.0F, 100.0F, 1.0F)
         .description("Retained collision push from other entities. 0 disables it; 100 is Vanilla.")
   );
   public final ModuleSetting.Bool blockPush = this.setting(
      new ModuleSetting.Bool("Block Push", false).description("Allow Vanilla to push you out of solid blocks.")
   );
   public final ModuleSetting.Number jumpDelay = this.setting(
      new ModuleSetting.Number("Jump Delay (ticks)", 0.0F, 0.0F, 5.0F, 1.0F)
         .description("Delay after an impulse before one grounded jump; expires after three additional ticks.")
   );

   public ImplSelectedModeSelector() {
      super(
         ModuleBuilderData.builder("Velocity")
            .category(ModuleFeatureType.COMBAT)
            .description("Controls incoming knockback, explosion impulses and collision pushing.")
            .build()
      );
      this.horizontal.visibleWhen(this.mode, "Percentage"::equals);
      this.vertical.visibleWhen(this.mode, item -> item.equals("Percentage") || item.equals("Reverse"));
      this.reverseStrength.visibleWhen(this.mode, "Reverse"::equals);
      this.entityPush.visibleWhen(this.mode, "Push"::equals);
      this.blockPush.visibleWhen(this.mode, "Push"::equals);
      this.jumpDelay.visibleWhen(this.mode, "Jump"::equals);
      this.explosions.visibleWhen(this.mode, item -> !item.equals("Push"));
      this.chance.visibleWhen(this.mode, item -> !item.equals("Push"));
      this.onlyGround.visibleWhen(this.mode, item -> !item.equals("Push") && !item.equals("Jump"));
      this.mode.onChange(item -> VelocityActivateService.reset());
   }

   public VelocityTransformConverter.Mode selectedMode() {
      return VelocityTransformConverter.Mode.valueOf(((String)this.mode.get()).toUpperCase(Locale.ROOT));
   }

   @Override
   protected void onEnable() {
      VelocityActivateService.activate(this);
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT).priority(EventIsAfterHandler.Priority.FIRST).run(item -> VelocityActivateService.beforeMovement());
      this.on(EventAttackInputService.WORLD).run(item -> VelocityActivateService.reset());
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> VelocityActivateService.reset());
   }

   @Override
   protected void onDisable() {
      VelocityActivateService.deactivate();
   }
}
