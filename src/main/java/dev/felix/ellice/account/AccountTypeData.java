package dev.felix.ellice.account;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public record AccountTypeData(
    UUID uuid,
    String username,
    AccountTypeData.Type type,
    String msRefreshToken,
    Instant addedAt,
    Instant lastUsedAt,
    String sessionAccessToken,
    Instant sessionExpiresAt) {
  private static final Pattern VALID_OFFLINE_NAME = Pattern.compile("[A-Za-z0-9_]{1,16}");

  public AccountTypeData(
      UUID uuid,
      String username,
      AccountTypeData.Type type,
      String msRefreshToken,
      Instant addedAt,
      Instant lastUsedAt,
      String sessionAccessToken,
      Instant sessionExpiresAt) {
    Objects.requireNonNull(uuid, "uuid");
    Objects.requireNonNull(username, "username");
    Objects.requireNonNull(type, "type");
    Objects.requireNonNull(addedAt, "addedAt");
    Objects.requireNonNull(lastUsedAt, "lastUsedAt");
    msRefreshToken = msRefreshToken == null ? "" : msRefreshToken;
    sessionAccessToken = sessionAccessToken == null ? "" : sessionAccessToken;
    sessionExpiresAt = sessionExpiresAt == null ? Instant.EPOCH : sessionExpiresAt;
    if (type == AccountTypeData.Type.SESSION
        || sessionAccessToken.isEmpty() && sessionExpiresAt.equals(Instant.EPOCH)) {
      if (type == AccountTypeData.Type.SESSION && !msRefreshToken.isEmpty()) {
        throw new IllegalArgumentException("Cookie sessions do not use Microsoft refresh tokens");
      }

      if (type == AccountTypeData.Type.MICROSOFT && msRefreshToken.isBlank()) {
        throw new IllegalArgumentException("Microsoft account requires a refresh token");
      }

      if (type == AccountTypeData.Type.OFFLINE) {
        if (!isValidOfflineUsername(username)) {
          throw new IllegalArgumentException(
              "Offline username must be 1–16 characters using letters, numbers, or _.");
        }

        if (!createUUID(username).equals(uuid)) {
          throw new IllegalArgumentException("Offline account UUID must match its username");
        }

        if (!msRefreshToken.isEmpty()) {
          throw new IllegalArgumentException("Offline account cannot contain a refresh token");
        }
      }

      this.uuid = uuid;
      this.username = username;
      this.type = type;
      this.msRefreshToken = msRefreshToken;
      this.addedAt = addedAt;
      this.lastUsedAt = lastUsedAt;
      this.sessionAccessToken = sessionAccessToken;
      this.sessionExpiresAt = sessionExpiresAt;
    } else {
      throw new IllegalArgumentException(
          "Only cookie sessions can carry an in-memory Minecraft token");
    }
  }

  public AccountTypeData(
      UUID uUID,
      String text,
      AccountTypeData.Type type,
      String currentText,
      Instant instant,
      Instant currentInstant) {
    this(uUID, text, type, currentText, instant, currentInstant, "", Instant.EPOCH);
  }

  public AccountTypeData(
      UUID uUID, String text, String currentText, Instant instant, Instant currentInstant) {
    this(uUID, text, AccountTypeData.Type.MICROSOFT, currentText, instant, currentInstant);
  }

  public static AccountTypeData session(
      UUID uUID, String text, String currentText, Instant instant) {
    if (currentText != null
        && !currentText.isBlank()
        && instant != null
        && instant.isAfter(Instant.now())) {
      Instant currentInstant = Instant.now();
      return new AccountTypeData(
          uUID,
          text,
          AccountTypeData.Type.SESSION,
          "",
          currentInstant,
          currentInstant,
          currentText,
          instant);
    } else {
      throw new IllegalArgumentException("Cookie session requires a current Minecraft token");
    }
  }

  public static AccountTypeData offline(String text) {
    String currentText = text == null ? "" : text.trim();
    if (!isValidOfflineUsername(currentText)) {
      throw new IllegalArgumentException(
          "Username must be 1–16 characters using letters, numbers, or _.");
    }

    Instant instant = Instant.now();
    return new AccountTypeData(
        createUUID(currentText), currentText, AccountTypeData.Type.OFFLINE, "", instant, instant);
  }

  public static boolean isValidOfflineUsername(String name) {
    return name != null && VALID_OFFLINE_NAME.matcher(name).matches();
  }

  public boolean isOffline() {
    return this.type == AccountTypeData.Type.OFFLINE;
  }

  public boolean isSession() {
    return this.type == AccountTypeData.Type.SESSION;
  }

  public boolean hasCurrentSession() {
    return this.isSession()
        && !this.sessionAccessToken.isBlank()
        && this.sessionExpiresAt.isAfter(Instant.now());
  }

  public AccountTypeData withUsername(String name) {
    if (this.isOffline()) {
      String currentName = name == null ? "" : name.trim();
      if (!isValidOfflineUsername(currentName)) {
        throw new IllegalArgumentException(
            "Username must be 1–16 characters using letters, numbers, or _.");
      } else if (!this.username.equals(currentName)) {
        throw new IllegalStateException(
            "An offline username defines the account identity; add a new account instead");
      } else {
        return this;
      }
    } else {
      return new AccountTypeData(
          this.uuid,
          name,
          this.type,
          this.msRefreshToken,
          this.addedAt,
          this.lastUsedAt,
          this.sessionAccessToken,
          this.sessionExpiresAt);
    }
  }

  public AccountTypeData withRefreshToken(String text) {
    if (!this.isOffline() && !this.isSession()) {
      return new AccountTypeData(
          this.uuid, this.username, this.type, text, this.addedAt, this.lastUsedAt);
    } else if (text != null && !text.isEmpty()) {
      throw new IllegalStateException("This account type does not use Microsoft refresh tokens");
    } else {
      return this;
    }
  }

  public AccountTypeData touched() {
    return new AccountTypeData(
        this.uuid,
        this.username,
        this.type,
        this.msRefreshToken,
        this.addedAt,
        Instant.now(),
        this.sessionAccessToken,
        this.sessionExpiresAt);
  }

  @Override
  public String toString() {
    return "MinecraftAccount[uuid="
        + this.uuid
        + ", username="
        + this.username
        + ", type="
        + this.type
        + ", credentials=redacted]";
  }

  private static UUID createUUID(String id) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + id).getBytes(StandardCharsets.UTF_8));
  }

  public enum Type {
    MICROSOFT,
    OFFLINE,
    SESSION;

    private static AccountTypeData.Type[] $values() {
      return new AccountTypeData.Type[] {MICROSOFT, OFFLINE, SESSION};
    }
  }
}
