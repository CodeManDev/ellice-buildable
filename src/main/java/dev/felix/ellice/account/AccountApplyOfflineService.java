package dev.felix.ellice.account;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

public final class AccountApplyOfflineService {
   private AccountApplyOfflineService() {
   }

   public static void apply(AccountNewThreadService.LoginResult loginResult) {
      AccountTypeData accountTypeData = loginResult.account();
      updateState(accountTypeData, loginResult.accessToken());
   }

   public static void applyOffline(AccountTypeData accountTypeData) {
      if (!accountTypeData.isOffline()) {
         throw new IllegalArgumentException("Expected an offline account");
      }

      updateState(accountTypeData, "-");
   }

   private static void updateState(AccountTypeData accountTypeData, String text) {
      User currentUser = new User(accountTypeData.username(), accountTypeData.uuid(), text, Optional.empty(), Optional.empty());
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.user = currentUser;
      minecraft.profileFuture = CompletableFuture.completedFuture(new ProfileResult(new GameProfile(accountTypeData.uuid(), accountTypeData.username())));
   }
}
