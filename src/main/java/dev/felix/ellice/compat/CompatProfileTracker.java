package dev.felix.ellice.compat;

import dev.felix.ellice.feature.scaffold.ScaffoldChooseService;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.ScaffoldActiveService;
import dev.felix.ellice.feature.scaffold.ScaffoldComponent;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class CompatProfileTracker {
   private static final ScaffoldActiveService scaffoldActiveService = new ScaffoldActiveService();
   private static final CompatProfileTracker.Profile profile = new CompatProfileTracker.Profile(
      new ScaffoldChooseService.Policy(true, "Held First", 0, List.of(), false, Set.of(), 0), false, true, 4
   );
   private static CompatProfileTracker.Profile profile2 = profile;
   private static boolean enabled;
   private static long timestamp;
   private static long timestamp2;
   private static int count = -1;

   private CompatProfileTracker() {
   }

   public static void configure(CompatProfileTracker.Profile profile) {
      if (profile2.silent() != profile.silent()
         || profile2.restore() != profile.restore()
         || profile2.blocks().autoSelect() != profile.blocks().autoSelect()) {
         scaffoldActiveService.release();
      }

      if (!profile2.blocks().equals(profile.blocks())) {
         count = -1;
      }

      profile2 = profile;
      enabled = true;
   }

   public static void disable() {
      scaffoldActiveService.release(false);
      enabled = false;
      profile2 = profile;
      timestamp2 = 0L;
   }

   public static void flushPendingSync() {
      ScaffoldActiveService.flushPendingSync();
   }

   public static void release() {
      scaffoldActiveService.release();
      count = -1;
   }

   public static void selectedByScaffold(int value) {
      if (enabled) {
         count = value;
      }
   }

   public static boolean paused() {
      return enabled && timestamp < timestamp2;
   }

   public static int serverSlot() {
      return scaffoldActiveService.blockSlot();
   }

   public static boolean depletedVisibleHand(Minecraft minecraft) {
      return scaffoldActiveService.active() && scaffoldActiveService.owned() && minecraft.player.getMainHandItem().isEmpty();
   }

   public static void tick(Minecraft minecraft) {
      timestamp++;
      inspectInput(minecraft);
   }

   public static void inspectInput(Minecraft minecraft) {
      if (enabled) {
         if (minecraft.player != null
            && minecraft.level != null
            && minecraft.screen == null
            && minecraft.getOverlay() == null
            && minecraft.player.isAlive()
            && (!scaffoldActiveService.active() || scaffoldActiveService.owned())) {
            if (!ScaffoldComponent.manualAttack(minecraft)
               && !ScaffoldComponent.manualItemUse(minecraft)
               && !checkCondition(minecraft.options.keyDrop)
               && !checkCondition(minecraft.options.keySwapOffhand)
               && !checkCondition(minecraft.options.keyPickItem)
               && !checkCondition(minecraft.options.keyInventory)) {
               for (KeyMapping keyMapping : minecraft.options.keyHotbarSlots) {
                  if (checkCondition(keyMapping)) {
                     manualInput();
                     return;
                  }
               }
            } else {
               manualInput();
            }
         } else {
            scaffoldActiveService.release();
         }
      }
   }

   private static boolean checkCondition(KeyMapping keyMapping) {
      return ((ScaffoldKeyMappingAccess)keyMapping).ellice$pendingClicks() > 0;
   }

   public static void manualInput() {
      if (enabled && !scaffoldActiveService.scoped()) {
         release();
         timestamp2 = timestamp + Math.max(1, profile2.manualPause());
      }
   }

   public static void beforeInteraction() {
      if (scaffoldActiveService.active() && !scaffoldActiveService.scoped()) {
         manualInput();
      }
   }

   public static void consumed(KeyMapping keyMapping, boolean currentEnabled) {
      if (enabled && currentEnabled) {
         Options currentOptions = Minecraft.getInstance().options;
         if (keyMapping != currentOptions.keyDrop && keyMapping != currentOptions.keySwapOffhand && keyMapping != currentOptions.keyPickItem && keyMapping != currentOptions.keyInventory) {
            for (KeyMapping currentKeyMapping : currentOptions.keyHotbarSlots) {
               if (keyMapping == currentKeyMapping) {
                  manualInput();
                  return;
               }
            }
         } else {
            manualInput();
         }
      }
   }

   public static void synchronize(Runnable runnable) {
      scaffoldActiveService.synchronize(runnable);
   }

   public static ScaffoldChooseService.Choice stock(Minecraft minecraft) {
      if (minecraft.player == null) {
         return new ScaffoldChooseService.Choice(-1, 0, 0, "No usable blocks");
      }

      ArrayList arrayList = new ArrayList(9);

      for (int index = 0; index < 9; index++) {
         ItemStack itemStack = minecraft.player.getInventory().getItem(index);
         arrayList.add(
            new ScaffoldChooseService.Stack(
               index,
               BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
               itemStack.getCount(),
               checkCondition2(itemStack) && (minecraft.level == null || itemStack.isItemEnabled(minecraft.level.enabledFeatures()))
            )
         );
      }

      int value = scaffoldActiveService.active() ? scaffoldActiveService.visibleSlot() : InventoryInputActions.selected(minecraft);
      return ScaffoldChooseService.choose(arrayList, value, scaffoldActiveService.active() ? scaffoldActiveService.blockSlot() : count, profile2.blocks());
   }

   public static int find(Minecraft minecraft) {
      return stock(minecraft).slot();
   }

   private static boolean checkCondition2(ItemStack itemStack) {
      if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem blockItem && !(blockItem.getBlock() instanceof FallingBlock)) {
         BlockState blockState = blockItem.getBlock().defaultBlockState();
         return !blockState.canBeReplaced() && blockState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
      } else {
         return false;
      }
   }

   public static boolean place(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, int value, PlacementCandidate placementCandidate) {
      if (!paused() && value >= 0 && value == find(minecraft)) {
         scaffoldActiveService.acquire(new CompatProfileTracker.Hand(minecraft), value, profile2.silent(), profile2.restore());
         return scaffoldActiveService.use(() -> scaffoldCompatibility.place(minecraft, placementCandidate));
      } else {
         return false;
      }
   }

   private static final class Hand implements ScaffoldActiveService.Hand {
      private final Minecraft minecraft2;
      private final LocalPlayer localPlayer;
      private final ClientLevel client2;
      private final MultiPlayerGameMode multiPlayerGameMode;

      private Hand(Minecraft minecraft) {
         this.minecraft2 = minecraft;
         this.localPlayer = minecraft.player;
         this.client2 = minecraft.level;
         this.multiPlayerGameMode = minecraft.gameMode;
      }

      @Override
      public boolean valid() {
         return this.minecraft2.player == this.localPlayer
            && this.minecraft2.level == this.client2
            && this.minecraft2.gameMode == this.multiPlayerGameMode
            && this.minecraft2.getConnection() == this.localPlayer.connection
            && this.localPlayer.connection.getConnection().isConnected();
      }

      @Override
      public int selected() {
         return InventoryInputActions.selected(this.minecraft2);
      }

      @Override
      public void select(int value) {
         InventoryInputActions.select(this.minecraft2, value);
      }

      @Override
      public void sync() {
         this.multiPlayerGameMode.ensureHasSentCarriedItem();
      }
   }

   public record Profile(ScaffoldChooseService.Policy blocks, boolean silent, boolean restore, int manualPause) {
   }
}

