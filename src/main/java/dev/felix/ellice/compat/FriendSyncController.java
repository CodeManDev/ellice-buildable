package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.friends.FriendsData;
import dev.felix.ellice.friends.FriendsStateController;
import dev.felix.ellice.friends.FriendsMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class FriendSyncController {
   private static List<FriendsData> items = List.of();

   private FriendSyncController() {
   }

   public static boolean excluded(FriendsMode friendsMode, Entity entity) {
      return entity instanceof Player && CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().friends() != null
         ? CoreIsInitializedHandler.get().friends().excludes(friendsMode, entity.getUUID(), entity.getName().getString())
         : false;
   }

   public static List<FriendsData> players() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null && minecraft.getConnection() != null) {
         UUID uUID = minecraft.getUser().getProfileId();
         return minecraft.getConnection()
            .getOnlinePlayers()
            .stream()
            .map(item -> CompatComponent.identity(item.getProfile()))
            .filter(item -> item.uuid() != null && !item.uuid().equals(uUID) && item.name() != null && !item.name().isBlank())
            .sorted(Comparator.comparing(FriendsData::name, String.CASE_INSENSITIVE_ORDER).thenComparing(FriendsData::uuid))
            .toList();
      } else {
         return List.of();
      }
   }

   public static void tick(FriendsStateController friendsStateController) {
      List currentItems = players();
      if (!currentItems.equals(items)) {
         items = currentItems;
         friendsStateController.resolve(currentItems);
      }
   }
}
