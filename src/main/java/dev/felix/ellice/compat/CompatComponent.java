package dev.felix.ellice.compat;

import com.mojang.authlib.GameProfile;
import dev.felix.ellice.friends.FriendsData;

final class CompatComponent {
  private CompatComponent() {}

  static FriendsData identity(GameProfile gameProfile) {
    return new FriendsData(gameProfile.id(), gameProfile.name());
  }
}
