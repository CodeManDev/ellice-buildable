package dev.felix.ellice.core;

import dev.felix.ellice.module.impl.ImplApplyZoomFovService;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.ImplReplacesVanillaOutlineService;
import dev.felix.ellice.module.impl.ImplRequestsSprintClient;
import dev.felix.ellice.module.impl.ImplSpringFovActiveService;
import dev.felix.ellice.module.impl.ImplSuspendForPearlService;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public final class CoreAutoSprintRequestedClient {
  private CoreAutoSprintRequestedClient() {}

  public static boolean autoSprintRequested(KeyMapping keyMapping) {
    Minecraft minecraft = Minecraft.getInstance();
    return minecraft != null
        && minecraft.options != null
        && keyMapping == minecraft.options.keySprint
        && ImplRequestsSprintClient.requestsSprint(minecraft);
  }

  public static boolean scaffoldOwnsBlockUse() {
    return CoreIsInitializedHandler.isReady()
        && CoreIsInitializedHandler.get()
            .modules()
            .get(ImplSuspendForPearlService.class)
            .map(item -> item.ownsBlockUse(Minecraft.getInstance()))
            .orElse(false);
  }

  public static boolean customBlockOutline() {
    return CoreIsInitializedHandler.isReady()
        && CoreIsInitializedHandler.get()
            .modules()
            .get(ImplReplacesVanillaOutlineService.class)
            .map(ImplReplacesVanillaOutlineService::replacesVanillaOutline)
            .orElse(false);
  }

  public static boolean springFovActive() {
    return ImplSpringFovActiveService.springFovActive();
  }

  public static boolean suppressCombatSprint() {
    return ImplDecisionTracker.sprintResetActive();
  }

  public static double scaffoldFovSpeed(
      AbstractClientPlayer abstractClientPlayer, Holder<Attribute> holder) {
    double doubleValue = abstractClientPlayer.getAttributeValue(holder);
    if (CoreIsInitializedHandler.isReady()
        && abstractClientPlayer == Minecraft.getInstance().player
        && CoreIsInitializedHandler.get()
            .modules()
            .get(ImplSuspendForPearlService.class)
            .map(ImplSuspendForPearlService::stableFov)
            .orElse(false)) {
      AttributeInstance attributeInstance = abstractClientPlayer.getAttribute(holder);
      if (attributeInstance == null) {
        return doubleValue;
      }

      for (AttributeModifier attributeModifier : attributeInstance.getModifiers()) {
        if (attributeModifier.id().toString().equals("minecraft:sprinting")
            && attributeModifier.operation() == Operation.ADD_MULTIPLIED_TOTAL
            && attributeModifier.amount() > -1.0) {
          return doubleValue / (1.0 + attributeModifier.amount());
        }
      }

      return doubleValue;
    } else {
      return doubleValue;
    }
  }

  public static float advanceFovSpring(float value) {
    return ImplSpringFovActiveService.advanceFovSpring(value);
  }

  public static float applyWorldFov(float value) {
    float currentValue = value;
    if (ImplApplyZoomFovService.controlsFov()) {
      ImplSpringFovActiveService.resetFovSpring();
    } else if (ImplSpringFovActiveService.springFovActive()) {
      currentValue = ImplSpringFovActiveService.advanceFovSpring(currentValue);
    }

    return ImplApplyZoomFovService.applyZoomFov(currentValue);
  }

  public static float applyZoomFov(float value) {
    return ImplApplyZoomFovService.applyZoomFov(value);
  }

  public static boolean smoothSneakActive() {
    return ImplSpringFovActiveService.smoothSneakActive();
  }

  public static float advanceEyeHeightSpring(float value, float currentValue) {
    return ImplSpringFovActiveService.advanceEyeHeightSpring(value, currentValue);
  }

  public static boolean cameraLeanActive() {
    return ImplSpringFovActiveService.cameraLeanActive();
  }

  public static float currentLeanRollRadians() {
    return ImplSpringFovActiveService.currentLeanRollRadians();
  }

  public static float itemSwapClamp() {
    return ImplSpringFovActiveService.itemSwapClamp();
  }
}
