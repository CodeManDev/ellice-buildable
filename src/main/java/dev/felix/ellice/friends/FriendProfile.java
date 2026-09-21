package dev.felix.ellice.friends;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record FriendProfile(
    UUID id, UUID uuid, String name, String note, boolean favorite, Set<FriendsMode> excluded) {
  public FriendProfile(
      UUID id, UUID uuid, String name, String note, boolean favorite, Set<FriendsMode> excluded) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    Objects.requireNonNull(note);
    excluded = Set.copyOf(excluded);
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.note = note;
    this.favorite = favorite;
    this.excluded = excluded;
  }

  public String displayName() {
    return this.name.isEmpty() ? this.uuid.toString().substring(0, 8) : this.name;
  }

  public boolean matches(UUID uUID, String text) {
    return this.uuid != null
        ? this.uuid.equals(uUID)
        : !this.name.isEmpty() && this.name.equalsIgnoreCase(text);
  }

  public FriendProfile identity(UUID uUID, String currentId) {
    return new FriendProfile(this.id, uUID, currentId, this.note, this.favorite, this.excluded);
  }

  public FriendProfile note(String text) {
    return new FriendProfile(this.id, this.uuid, this.name, text, this.favorite, this.excluded);
  }

  public FriendProfile favorite(boolean enabled) {
    return new FriendProfile(this.id, this.uuid, this.name, this.note, enabled, this.excluded);
  }

  public FriendProfile excluded(Set<FriendsMode> values) {
    return new FriendProfile(this.id, this.uuid, this.name, this.note, this.favorite, values);
  }
}
