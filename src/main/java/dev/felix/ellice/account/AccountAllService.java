package dev.felix.ellice.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public final class AccountAllService {
   private final Path path;
   private final List<AccountTypeData> items = new ArrayList<>();
   private UUID uUID;

   public AccountAllService(Path currentPath) {
      Path nextPath = currentPath.resolve("config").resolve("ellice");

      try {
         Files.createDirectories(nextPath);
      } catch (IOException iOException) {
      }

      this.path = nextPath.resolve("minecraft-accounts.json");
      this.updateState();
   }

   public synchronized List<AccountTypeData> all() {
      return Collections.unmodifiableList(new ArrayList<>(this.items));
   }

   public synchronized Optional<AccountTypeData> active() {
      return this.uUID == null
         ? Optional.empty()
         : this.items.stream().filter(item -> this.uUID.equals(item.uuid())).findFirst();
   }

   public synchronized Optional<AccountTypeData> get(UUID uUID) {
      return this.items.stream().filter(item -> uUID.equals(item.uuid())).findFirst();
   }

   public synchronized AccountTypeData upsert(AccountTypeData accountTypeData) {
      for (int index = 0; index < this.items.size(); index++) {
         if (this.items.get(index).uuid().equals(accountTypeData.uuid())) {
            this.items.set(index, accountTypeData);
            this.updateState2();
            return accountTypeData;
         }
      }

      this.items.add(accountTypeData);
      if (this.uUID == null) {
         this.uUID = accountTypeData.uuid();
      }

      this.updateState2();
      return accountTypeData;
   }

   public synchronized void setActive(UUID currentUUID) {
      if (!this.items.stream().noneMatch(item -> item.uuid().equals(currentUUID))) {
         this.uUID = currentUUID;

         for (int index = 0; index < this.items.size(); index++) {
            if (this.items.get(index).uuid().equals(currentUUID)) {
               this.items.set(index, this.items.get(index).touched());
               break;
            }
         }

         this.updateState2();
      }
   }

   public synchronized void remove(UUID currentUUID) {
      this.items.removeIf(item -> item.uuid().equals(currentUUID));
      if (currentUUID.equals(this.uUID)) {
         this.uUID = this.items.isEmpty() ? null : this.items.get(0).uuid();
      }

      this.updateState2();
   }

   public synchronized void clear() {
      this.items.clear();
      this.uUID = null;
      this.updateState2();
   }

   private void updateState() {
      if (Files.isReadable(this.path)) {
         try {
            String text = Files.readString(this.path, StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
            if (jsonObject.has("activeUuid") && !jsonObject.get("activeUuid").isJsonNull()) {
               try {
                  this.uUID = UUID.fromString(jsonObject.get("activeUuid").getAsString());
               } catch (IllegalArgumentException illegalArgumentException) {
               }
            }

            if (jsonObject.has("accounts") && jsonObject.get("accounts").isJsonArray()) {
               for (JsonElement jsonElement : jsonObject.getAsJsonArray("accounts")) {
                  try {
                     this.items.add(createAccountTypeData(jsonElement.getAsJsonObject()));
                  } catch (Exception exception) {
                  }
               }
            }
         } catch (IOException iOException) {
         }
      }
   }

   private void updateState2() {
      JsonObject jsonObject = new JsonObject();
      if (this.uUID != null) {
         jsonObject.addProperty("activeUuid", this.uUID.toString());
      }

      JsonArray jsonArray = new JsonArray();

      for (AccountTypeData accountTypeData : this.items) {
         jsonArray.add(createJsonObject(accountTypeData));
      }

      jsonObject.add("accounts", jsonArray);
      Path currentPath = this.path.resolveSibling(this.path.getFileName() + ".tmp");

      try {
         Files.writeString(currentPath, jsonObject.toString(), StandardCharsets.UTF_8);
         Files.move(currentPath, this.path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException iOException) {
      }
   }

   private static JsonObject createJsonObject(AccountTypeData accountTypeData) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("uuid", accountTypeData.uuid().toString());
      jsonObject.addProperty("username", accountTypeData.username());
      jsonObject.addProperty("type", accountTypeData.type().name().toLowerCase(Locale.ROOT));
      if (accountTypeData.type() == AccountTypeData.Type.MICROSOFT) {
         jsonObject.addProperty("msRefreshToken", accountTypeData.msRefreshToken());
      }

      jsonObject.addProperty("addedAt", accountTypeData.addedAt().toString());
      jsonObject.addProperty("lastUsedAt", accountTypeData.lastUsedAt().toString());
      return jsonObject;
   }

   private static AccountTypeData createAccountTypeData(JsonObject jsonObject) {
      AccountTypeData.Type currentType = AccountTypeData.Type.MICROSOFT;
      if (jsonObject.has("type") && !jsonObject.get("type").isJsonNull()) {
         currentType = AccountTypeData.Type.valueOf(jsonObject.get("type").getAsString().toUpperCase(Locale.ROOT));
      }

      String text = jsonObject.has("msRefreshToken") && !jsonObject.get("msRefreshToken").isJsonNull() ? jsonObject.get("msRefreshToken").getAsString() : "";
      return new AccountTypeData(
         UUID.fromString(jsonObject.get("uuid").getAsString()),
         jsonObject.get("username").getAsString(),
         currentType,
         text,
         Instant.parse(jsonObject.get("addedAt").getAsString()),
         Instant.parse(jsonObject.get("lastUsedAt").getAsString())
      );
   }
}
