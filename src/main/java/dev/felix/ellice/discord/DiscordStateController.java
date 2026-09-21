package dev.felix.ellice.discord;

import com.google.gson.JsonObject;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public final class DiscordStateController implements AutoCloseable {
   private static final long timestamp = 10000L;
   private static final long timestamp2 = 4100000000L;
   private final Object object = new Object();
   private final String text = createText2();
   private final long timestamp3 = Instant.now().getEpochSecond();
   private volatile boolean enabled2 = true;
   private volatile boolean enabled3;
   private volatile DiscordStateController.Activity activity;
   private volatile DiscordCloseService discordCloseService;

   public DiscordStateController() {
      if (this.text == null) {
         CoreIsInitializedHandler.LOGGER.warn("Discord Rich Presence needs discord_application_id or ELLICE_DISCORD_APPLICATION_ID");
      }

      Thread.ofVirtual().name("ellice-discord-rich-presence").start(this::updateState);
   }

   public void enabled(boolean currentEnabled) {
      this.enabled3 = currentEnabled;
      this.updateState3();
   }

   public void tick(Minecraft minecraft, int value) {
      if (this.enabled3) {
         String text;
         String currentText;
         String nextText;
         String previousText;
         if (minecraft.level == null) {
            text = "Choosing the next move";
            currentText = "Ready to queue";
            nextText = "menu";
            previousText = "Main menu";
         } else if (minecraft.isLocalServer()) {
            text = "Exploring a solo world";
            currentText = createText(value);
            nextText = "singleplayer";
            previousText = "Singleplayer";
         } else {
            ServerData serverData = minecraft.getCurrentServer();
            String sourceText = serverData == null ? "Multiplayer" : serverData.name;
            if (sourceText == null || sourceText.isBlank() || serverData.ip != null && sourceText.equalsIgnoreCase(serverData.ip)) {
               sourceText = "Multiplayer";
            }

            text = "Playing on " + createText3(sourceText, 96);
            currentText = createText(value);
            nextText = serverData != null && serverData.ip != null && !serverData.ip.isBlank() ? serverIcon(serverData.ip) : "multiplayer";
            previousText = createText3(sourceText, 128);
         }

         DiscordStateController.Activity currentActivity = new DiscordStateController.Activity(text, currentText, nextText, previousText, this.timestamp3);
         if (!currentActivity.equals(this.activity)) {
            this.activity = currentActivity;
            this.updateState3();
         }
      }
   }

   private static String createText(int value) {
      return value == 1 ? "1 module active" : value + " modules active";
   }

   private void updateState() {
      DiscordStateController.Activity currentActivity = null;
      long offset = 0L;

      while (this.enabled2) {
         try {
            if (this.enabled3 && this.text != null) {
               if (this.discordCloseService == null || !this.discordCloseService.isOpen()) {
                  this.updateState4();
                  this.discordCloseService = DiscordCloseService.connect(this.text);
                  currentActivity = null;
                  CoreIsInitializedHandler.LOGGER.info("Discord Rich Presence connected");
               }

               DiscordStateController.Activity nextActivity = this.activity;
               if (nextActivity != null && !Objects.equals(nextActivity, currentActivity)) {
                  long currentOffset = 4100000000L - (System.nanoTime() - offset);
                  if (offset != 0L && currentOffset > 0L) {
                     this.updateState2(Math.max(1L, currentOffset / 1000000L));
                     continue;
                  }

                  this.discordCloseService.setActivity(nextActivity);
                  currentActivity = nextActivity;
                  offset = System.nanoTime();
               }

               this.updateState2(5000L);
            } else {
               if (this.discordCloseService != null) {
                  if (this.discordCloseService.isOpen()) {
                     this.discordCloseService.setActivity(null);
                  }

                  this.updateState4();
               }

               currentActivity = null;
               this.updateState2(10000L);
            }
         } catch (IOException iOException) {
            this.updateState4();
            currentActivity = null;
            CoreIsInitializedHandler.LOGGER.debug("Discord Rich Presence is waiting for Discord desktop", iOException);
            this.updateState2(10000L);
         }
      }

      if (this.discordCloseService != null) {
         try {
            this.discordCloseService.setActivity(null);
         } catch (IOException currentIOException) {
         }
      }

      this.updateState4();
   }

   private void updateState2(long offset) {
      synchronized (this.object) {
         try {
            this.object.wait(offset);
         } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            this.enabled2 = false;
         }
      }
   }

   private void updateState3() {
      synchronized (this.object) {
         this.object.notifyAll();
      }
   }

   private void updateState4() {
      if (this.discordCloseService != null) {
         this.discordCloseService.close();
      }

      this.discordCloseService = null;
   }

   @Override
   public void close() {
      this.enabled2 = false;
      DiscordCloseService discordClose = this.discordCloseService;
      if (discordClose != null) {
         try {
            discordClose.setActivity(null);
         } catch (IOException iOException) {
         }

         discordClose.close();
      }

      this.updateState3();
   }

   private static String createText2() {
      String text = System.getProperty("ellice.discord.applicationId");
      if (text == null || text.isBlank()) {
         text = System.getenv("ELLICE_DISCORD_APPLICATION_ID");
      }

      if (text == null || text.isBlank()) {
         try (InputStream inputStream = DiscordStateController.class.getResourceAsStream("/assets/ellice/discord/application-id.txt")) {
            if (inputStream != null) {
               text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).strip();
            }
         } catch (IOException iOException) {
         }
      }

      return text != null && text.matches("[0-9]{17,20}") ? text : null;
   }

   private static String createText3(String text, int value) {
      return text.length() <= value ? text : text.substring(0, value - 1) + "…";
   }

   static String serverIcon(String text) {
      return "https://api.mcstatus.io/v2/icon/" + URLEncoder.encode(text, StandardCharsets.UTF_8).replace("+", "%20");
   }

   record Activity(String details, String state, String smallImage, String smallText, long startedAt) {
      JsonObject json() {
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("type", 0);
         jsonObject.addProperty("details", DiscordStateController.createText3(this.details, 128));
         jsonObject.addProperty("state", DiscordStateController.createText3(this.state, 128));
         JsonObject currentJsonObject = new JsonObject();
         currentJsonObject.addProperty("start", this.startedAt);
         jsonObject.add("timestamps", currentJsonObject);
         JsonObject nextJsonObject = new JsonObject();
         nextJsonObject.addProperty("large_image", "ellice");
         nextJsonObject.addProperty("large_text", "ellice " + CoreIsInitializedHandler.VERSION + " • " + CompatAdapterService.metadata().displayName());
         nextJsonObject.addProperty("small_image", this.smallImage);
         nextJsonObject.addProperty("small_text", this.smallText);
         jsonObject.add("assets", nextJsonObject);
         return jsonObject;
      }
   }
}

