package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.impl.ImplActiveService;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CompatBlockingService {
  private CompatBlockingService() {}

  public static boolean blocking(AbstractClientPlayer abstractClientPlayer) {
    ImplActiveService implActive = ImplActiveService.active();
    if (implActive != null && CoreIsInitializedHandler.isReady()) {
      Minecraft minecraft = Minecraft.getInstance();
      if (abstractClientPlayer == minecraft.player
          && !abstractClientPlayer.isSpectator()
          && abstractClientPlayer.isAlive()) {
        int value =
            abstractClientPlayer.isUsingItem()
                    && abstractClientPlayer.getUsedItemHand() == InteractionHand.OFF_HAND
                    && abstractClientPlayer.getUseItem().is(Items.SHIELD)
                ? 1
                : 0;
        boolean enabled =
            CoreIsInitializedHandler.get()
                .modules()
                .get(ImplDecisionTracker.class)
                .map(ImplDecisionTracker::hasTarget)
                .orElse(false);
        return implActive.shouldBlock(
            abstractClientPlayer.getMainHandItem().is(ItemTags.SWORDS),
            minecraft.screen == null && minecraft.options.keyUse.isDown(),
            enabled,
            (value != 0),
            abstractClientPlayer.isUsingItem() && value == 0,
            abstractClientPlayer.isScoping() || abstractClientPlayer.isAutoSpinAttack());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public static boolean sword(
      AbstractClientPlayer abstractClientPlayer,
      InteractionHand interactionHand,
      ItemStack itemStack) {
    return interactionHand == InteractionHand.MAIN_HAND
        && itemStack.is(ItemTags.SWORDS)
        && blocking(abstractClientPlayer);
  }

  public static boolean hideShield(
      AbstractClientPlayer abstractClientPlayer,
      InteractionHand interactionHand,
      ItemStack itemStack) {
    ImplActiveService implActive = ImplActiveService.active();
    return implActive != null
        && implActive.hideShield()
        && interactionHand == InteractionHand.OFF_HAND
        && itemStack.is(Items.SHIELD)
        && blocking(abstractClientPlayer);
  }
}
